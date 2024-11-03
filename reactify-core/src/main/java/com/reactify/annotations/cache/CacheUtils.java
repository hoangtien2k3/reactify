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
package com.reactify.annotations.cache;

import com.reactify.config.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

/**
 * <p>
 * The {@code CacheUtils} class provides utility methods for invoking methods
 * that are used for cache management, particularly for methods annotated with
 * {@link com.reactify.annotations.LocalCache}. This class is designed to
 * facilitate the auto-loading of cache entries by invoking the specified
 * methods in the appropriate application context.
 * </p>
 *
 * <p>
 * This class is annotated with {@link Component}
 * to indicate that it is a Spring-managed bean, and it uses constructor
 * injection to obtain any required dependencies.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
@Component
public class CacheUtils {

    /**
     * Constructs a new instance of {@code CacheUtils}.
     */
    public CacheUtils() {}

    /**
     * <p>
     * Invokes a specified method and subscribes to the returned
     * {@link Mono} object to trigger any asynchronous cache
     * loading operations. This method retrieves the bean of the declaring class
     * from the application context and executes the method, logging any exceptions
     * that occur during invocation.
     * </p>
     *
     * @param method
     *            a {@link Method} object representing the method
     *            to be invoked for cache loading.
     */
    public static void invokeMethod(Method method) {
        try {
            Class<?> declaringClass = method.getDeclaringClass();
            Object t = ApplicationContextProvider.getApplicationContext().getBean(declaringClass);
            Mono<Object> rs = (Mono<Object>) method.invoke(t);
            rs.subscribe();
        } catch (Exception exception) {
            log.error(
                    "Error when autoload cache {}.{}.{}",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    exception.getMessage(),
                    exception);
        }
    }
}
