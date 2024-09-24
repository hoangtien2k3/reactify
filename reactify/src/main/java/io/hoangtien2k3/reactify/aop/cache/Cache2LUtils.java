/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
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

import io.hoangtien2k3.reactify.config.ApplicationContextProvider;
import java.lang.reflect.Method;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import reactor.core.publisher.Mono;

/**
 * <p>Cache2LUtils class.</p>
 *
 * @author hoangtien2k3
 */
@Log4j2
public class Cache2LUtils {
    /**
     * <p>invokeMethod.</p>
     *
     * @param method a {@link java.lang.reflect.Method} object
     */
    public static void invokeMethod(Method method) {
        try {
            Class declaringClass = method.getDeclaringClass();
            Object t = ApplicationContextProvider.getApplicationContext().getBean(declaringClass);
            Mono<Object> rs = (Mono<Object>) method.invoke(t);
            rs.subscribe();
        } catch (Exception exception) {
            log.error(
                    "Error when autoload cache {}.{}",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    exception.getMessage(),
                    exception);
        }
    }

    /**
     * <p>getRedisCache2lTemplate.</p>
     *
     * @param redisConnectionFactory a {@link org.springframework.data.redis.connection.RedisConnectionFactory} object
     * @return a {@link org.springframework.data.redis.core.RedisTemplate} object
     */
    public static RedisTemplate<Object, Object> getRedisCache2lTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setKeySerializer(serializer);
        template.setValueSerializer(serializer);
        return template;
    }
}
