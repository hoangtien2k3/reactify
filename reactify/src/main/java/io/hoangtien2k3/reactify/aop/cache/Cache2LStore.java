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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import io.hoangtien2k3.reactify.aop.cache.redis.RedisCacheMetricsCollector;
import io.prometheus.client.cache.caffeine.CacheMetricsCollector;
import jakarta.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.*;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class Cache2LStore {

    private static final HashMap<String, CacheInfo> caches = new HashMap<>();
    private static final Set<Method> autoLoadMethods = new HashSet<>();
    private static CacheMetricsCollector CACHE_METRICS_COLLECTOR;
    private static RedisCacheMetricsCollector REDIS_CACHE_METRICS_COLLECTOR;
    private static boolean enableGlobalCache;
    private static String reflectionPath;

    @Autowired(required = false)
    public Cache2LStore(
            @Value("${cache2l.reflectionPath:io.hoangtien2k3.commons}") String reflectionPath,
            @Value("${cache2l.enable-global-cache:true}") boolean enableGlobalCache,
            RedisCacheMetricsCollector redisCacheMetricsCollector) {
        if (CACHE_METRICS_COLLECTOR == null) {
            CACHE_METRICS_COLLECTOR = new CacheMetricsCollector().register();
        }
        if (REDIS_CACHE_METRICS_COLLECTOR == null) {
            Cache2LStore.REDIS_CACHE_METRICS_COLLECTOR = redisCacheMetricsCollector;
        }
        Cache2LStore.reflectionPath = reflectionPath;
        Cache2LStore.enableGlobalCache = enableGlobalCache;
    }

    @PostConstruct
    private static void init() {
        log.info("Start initializing cache");
        Reflections reflections = new Reflections(reflectionPath, Scanners.MethodsAnnotated);
        Set<Method> methods =
                reflections.get(Scanners.MethodsAnnotated.with(Cache2L.class).as(Method.class));

        for (Method method : methods) {
            processMethod(method);
        }

        log.info("Finish initializing {} cache", caches.size());
    }

    private static void processMethod(Method method) {
        String className = method.getDeclaringClass().getSimpleName();
        Cache2L cache2L = method.getAnnotation(Cache2L.class);

        String cacheName = className + "." + method.getName();
        Cache<Object, Object> cache = createCache(cache2L, method);

        boolean useGlobalCache = cache2L.useGlobalCache();
        if (enableGlobalCache && useGlobalCache) {
            handleGlobalCache(method, cacheName, cache);
        } else {
            caches.put(cacheName, new CacheInfo(cache, null));
        }

        CACHE_METRICS_COLLECTOR.addCache(cacheName, cache);
    }

    private static Cache<Object, Object> createCache(Cache2L cache2L, Method method) {
        Caffeine<Object, Object> builder = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(cache2L.durationInMinute()))
                .recordStats()
                .maximumSize(cache2L.maxRecord());

        if (cache2L.autoCache() && method.getParameterCount() == 0) {
            builder = builder.scheduler(Scheduler.systemScheduler())
                    .removalListener(new CustomizeRemovalListener(method));
            autoLoadMethods.add(method);
        }

        return builder.build();
    }

    private static void handleGlobalCache(Method method, String cacheName, Cache<Object, Object> cache) {
        ParameterizedType returnType = (ParameterizedType) method.getGenericReturnType();
        TypeInfo typeInfo = extractTypeInfo(returnType);

        caches.put(
                cacheName,
                new CacheInfo(
                        cache,
                        new GlobalCacheInfo(
                                typeInfo.isOptional(),
                                typeInfo.actualType,
                                typeInfo.wrapType,
                                typeInfo.keyType,
                                typeInfo.valueType)));
        REDIS_CACHE_METRICS_COLLECTOR.addCache(cacheName);
    }

    private static TypeInfo extractTypeInfo(ParameterizedType returnType) {
        TypeInfo typeInfo = new TypeInfo();
        Type actualType = null;
        Type keyType = null;
        Type valueType = null;

        if (returnType.getActualTypeArguments()[0] instanceof ParameterizedType subType) {
            Type subActualType = subType.getRawType();

            if (subActualType.equals(Optional.class)) {
                typeInfo.isOptional = true;
                actualType = subType.getActualTypeArguments()[0];
            } else if (subActualType instanceof Class && List.class.isAssignableFrom((Class<?>) subActualType)) {
                typeInfo.wrapType = (Class<?>) subActualType;
                actualType = subType.getActualTypeArguments()[0];
            } else if (subActualType instanceof Class && Map.class.isAssignableFrom((Class<?>) subActualType)) {
                typeInfo.wrapType = (Class<?>) subActualType;
                keyType = subType.getActualTypeArguments()[0];
                valueType = subType.getActualTypeArguments()[1];
            } else {
                actualType = subActualType;
            }
        } else {
            actualType = returnType.getActualTypeArguments()[0];
        }

        typeInfo.actualType = (Class<?>) actualType;
        typeInfo.keyType = (Class<?>) keyType;
        typeInfo.valueType = (Class<?>) valueType;

        return typeInfo;
    }

    @Getter
    private static class TypeInfo {
        boolean isOptional;
        Class<?> actualType;
        Class<?> wrapType;
        Class<?> keyType;
        Class<?> valueType;
    }

    @Async
    @EventListener
    public void autoLoad(ContextRefreshedEvent event) {
        if (!autoLoadMethods.isEmpty()) {
            log.info("Start auto load {} cache", autoLoadMethods.size());
            for (Method method : autoLoadMethods) {
                Cache2LUtils.invokeMethod(method);
            }
            log.info("Finish auto load cache");
        }
    }

    public static Cache<Object, Object> getCache(String key) {
        CacheInfo cacheInfo = caches.get(key);
        if (cacheInfo == null) {
            return null;
        }
        return cacheInfo.getCache();
    }

    public static boolean useGlobalCache(String key) {
        return (caches.get(key).getGlobalCacheInfo() != null);
    }

    public static boolean isOptional(String key) {
        return caches.get(key).getGlobalCacheInfo().isOptional();
    }

    public static Class<?> getType(String key) {
        return caches.get(key).getGlobalCacheInfo().getType();
    }

    public static Class<?> getWrapType(String key) {
        if (useGlobalCache(key)) {
            return caches.get(key).getGlobalCacheInfo().getWrapType();
        } else {
            return null;
        }
    }

    public static Class<?> getMapKeyType(String key) {
        if (useGlobalCache(key)) {
            return caches.get(key).getGlobalCacheInfo().getMapKeyType();
        } else {
            return null;
        }
    }

    public static Class<?> getMapValueType(String key) {
        if (useGlobalCache(key)) {
            return caches.get(key).getGlobalCacheInfo().getMapValueType();
        } else {
            return null;
        }
    }
}
