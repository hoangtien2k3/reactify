/*
 * Copyright 2024 the original author Hoàng Anh Tiến
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hoangtien2k3.reactify.aop.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Policy;
import io.hoangtien2k3.reactify.aop.cache.redis.RedisCacheKeyCounter;
import io.hoangtien2k3.reactify.factory.ObjectMapperFactory;
import io.lettuce.core.RedisException;
import java.time.Duration;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

/**
 * <p>Cache2LAspect class.</p>
 *
 * @author hoangtien2k3
 */
@Aspect
@Configuration
@Log4j2
public class Cache2LAspect {

    @Value("${cache2l.enable-global-cache:false}")
    private boolean enableGlobalCache;

    @Value("${cache2l.redis.externalMode:false}")
    private boolean isExternalRedis;

    private static final ObjectMapper OBJECT_MAPPER = ObjectMapperFactory.getInstance();
    private boolean isRedisAvailable = true;

    @Qualifier("redisMasterCache2LTemplate")
    @Autowired(required = false)
    private RedisTemplate redisMasterTemplate;

    @Qualifier("redisSlaveCache2LTemplate")
    @Autowired(required = false)
    private RedisTemplate redisSlaveTemplate;

    @Qualifier("redisExternalCache2LTemplate")
    @Autowired(required = false)
    private RedisTemplate redisExternalTemplate;

    @Autowired
    private RedisCacheKeyCounter redisCacheKey;

    @Pointcut("@annotation(io.hoangtien2k3.reactify.aop.cache.Cache2L)")
    private void processAnnotation() {}

    /**
     * <p>aroundAdvice.</p>
     *
     * @param pjp a {@link org.aspectj.lang.ProceedingJoinPoint} object
     * @return a {@link java.lang.Object} object
     * @throws java.lang.Throwable if any.
     */
    @Around("processAnnotation()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Object key = SimpleKeyGenerator.generateKey(args);
        String name = pjp.getTarget().getClass().getSimpleName() + "."
                + pjp.getSignature().getName();
        Object globalKey = SimpleKeyGenerator.generateKey(args, name);
        Cache<Object, Object> cache = Cache2LStore.getCache(name);
        if (cache == null) {
            log.warn("Cache for name '{}' is not available. Proceeding without caching.", name);
            return pjp.proceed(args); // Do not use cache if cache is null
        }
        Policy.FixedExpiration<?, ?> expiration =
                (Policy.FixedExpiration<?, ?>) cache.policy().expireAfterWrite().get();

        return CacheMono.lookup(k -> Mono.justOrEmpty(cache.getIfPresent(key)).map(Signal::next), key)
                .onCacheMissResume((Mono<Object>)
                        onCacheMisResumeHandler(args, pjp, globalKey, name, expiration.getExpiresAfter()))
                .andWriteWith((k, sig) -> Mono.fromRunnable(() -> {
                    if (sig != null && sig.get() != null) {
                        if (!(sig.get() instanceof Optional && ((Optional) sig.get()).isEmpty())) {
                            cache.put(k, sig.get());
                        }
                    }
                }));
    }

    private Object onCacheMisResumeHandler(
            Object[] args, ProceedingJoinPoint proceedingJoinPoint, Object key, String cacheName, Duration duration)
            throws Throwable {
        if (enableGlobalCache && Cache2LStore.useGlobalCache(cacheName) && isRedisAvailable) {
            if (Cache2LStore.isOptional(cacheName)) {
                return CacheMono.lookup(
                                k -> Mono.justOrEmpty(readValueFromRedis(key, cacheName))
                                        .map(Optional::ofNullable)
                                        .map(Signal::next),
                                key)
                        .onCacheMissResume((Mono<Optional<Object>>) proceedingJoinPoint.proceed(args))
                        .andWriteWith((k, sig) -> Mono.fromRunnable(() -> {
                            if (sig != null && sig.get() != null && sig.get().isPresent()) {
                                writeValueToRedis(k, sig.get().get(), duration);
                            }
                        }));
            } else {
                return CacheMono.lookup(
                                k -> Mono.justOrEmpty(readValueFromRedis(key, cacheName))
                                        .map(Signal::next),
                                key)
                        .onCacheMissResume(((Mono<Object>) proceedingJoinPoint.proceed(args)))
                        .andWriteWith((k, sig) -> Mono.fromRunnable(() -> {
                            if (sig != null && sig.get() != null) {
                                writeValueToRedis(k, sig.get(), duration);
                            }
                        }));
            }
        } else {
            return proceedingJoinPoint.proceed(args);
        }
    }

    private Object readValueFromRedis(Object key, String cacheName) {
        if (enableGlobalCache && isRedisAvailable) {
            try {
                Class<?> wrapType = Cache2LStore.getWrapType(cacheName);
                Class<?> type = Cache2LStore.getType(cacheName);
                Object value;
                if (isExternalRedis) {
                    value = redisExternalTemplate.opsForValue().get(key);
                } else {
                    value = redisSlaveTemplate.opsForValue().get(key);
                }
                if (value != null) {
                    redisCacheKey.recordHit(cacheName);
                    if (wrapType != null) {
                        if (wrapType.equals(List.class)
                                || wrapType.equals(LinkedList.class)
                                || wrapType.equals(ArrayList.class)
                                || wrapType.equals(Set.class)
                                || wrapType.equals(LinkedHashSet.class)
                                || wrapType.equals(HashSet.class)) {
                            return OBJECT_MAPPER.convertValue(
                                    value,
                                    OBJECT_MAPPER
                                            .getTypeFactory()
                                            .constructCollectionType((Class<? extends Collection>) wrapType, type));
                        } else {
                            return OBJECT_MAPPER.convertValue(
                                    value,
                                    OBJECT_MAPPER
                                            .getTypeFactory()
                                            .constructMapType(
                                                    (Class<? extends Map>) wrapType,
                                                    Cache2LStore.getMapKeyType(cacheName),
                                                    Cache2LStore.getMapValueType(cacheName)));
                        }
                    } else {
                        return OBJECT_MAPPER.convertValue(value, type);
                    }
                } else {
                    redisCacheKey.recordMiss(cacheName);
                    return null;
                }
            } catch (Exception e) {
                log.error("Get redis with key {} fail ", key, e);
                if (e instanceof QueryTimeoutException
                        || e instanceof RedisException
                        || e instanceof RedisConnectionFailureException
                        || e instanceof RedisSystemException) {
                    isRedisAvailable = false;
                }
                return null;
            }
        } else {
            log.error("Redis server is not available to read key {}", key);
            return null;
        }
    }

    private void writeValueToRedis(Object key, Object value, Duration expireTime) {
        if (enableGlobalCache && isRedisAvailable) {
            try {
                if (isExternalRedis) {
                    redisExternalTemplate.opsForValue().set(key, value);
                    redisExternalTemplate.expire(key, expireTime);
                } else {
                    redisMasterTemplate.opsForValue().set(key, value);
                    redisMasterTemplate.expire(key, expireTime);
                }
            } catch (Exception e) {
                log.error("Write to global cache fail", e);
                if (e instanceof QueryTimeoutException
                        || e instanceof RedisException
                        || e instanceof RedisConnectionFailureException
                        || e instanceof RedisSystemException) {
                    isRedisAvailable = false;
                }
            }
        } else {
            log.error("Redis server is not available to write value with key {}", key);
        }
    }

    /**
     * <p>redisHealthCheckJob.</p>
     */
    @Scheduled(fixedDelay = 50000)
    public void redisHealthCheckJob() {
        if (!enableGlobalCache) {
            return;
        }
        try {
            if (isExternalRedis) {
                isRedisAvailable = redisExternalTemplate
                                .getConnectionFactory()
                                .getConnection()
                                .ping()
                        != null;
            } else {
                isRedisAvailable = redisMasterTemplate
                                        .getConnectionFactory()
                                        .getConnection()
                                        .ping()
                                != null
                        && redisSlaveTemplate
                                        .getConnectionFactory()
                                        .getConnection()
                                        .ping()
                                != null;
            }
        } catch (Exception e) {
            log.warn("Redis server is not available at the moment.");
            isRedisAvailable = false;
        }
    }
}
