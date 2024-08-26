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
package io.hoangtien2k3.commons.annotations.cache;

import io.hoangtien2k3.commons.config.ApplicationContextProvider;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Utility class for cache-related operations.
 * <p>
 * This class provides methods to interact with cache, including invoking
 * methods dynamically and handling exceptions.
 * </p>
 */
/** @deprecated */
@Deprecated
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheUtils {

    /**
     * Invokes the specified method and handles any exceptions that occur during the
     * invocation.
     * <p>
     * This method retrieves a bean of the declaring class from the application
     * context, invokes the specified method on that bean, and subscribes to the
     * returned Mono. If an exception occurs during invocation, it logs an error
     * with the method name and exception details.
     * </p>
     *
     * @param method
     *            the method to be invoked
     */
    public static void invokeMethod(Method method) {
        try {
            Class<?> declaringClass = method.getDeclaringClass();
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

    // public static RedisTemplate<Object, Object>
    // getRedisCache2lTemplate(RedisConnectionFactory redisConnectionFactory) {
    // RedisTemplate<Object, Object> template = new RedisTemplate<>();
    // template.setConnectionFactory(redisConnectionFactory);
    // Jackson2JsonRedisSerializer<Object> serializer = new
    // Jackson2JsonRedisSerializer<>(Object.class);
    // ObjectMapper objectMapper = new ObjectMapper();
    // objectMapper.setVisibility(PropertyAccessor.ALL,
    // JsonAutoDetect.Visibility.ANY);
    // serializer.setObjectMapper(objectMapper);
    // template.setKeySerializer(serializer);
    // template.setValueSerializer(serializer);
    // template.setValueSerializer(serializer);
    // return template;
    // }
}
