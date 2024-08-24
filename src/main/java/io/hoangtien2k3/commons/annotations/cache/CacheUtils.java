/*
 * Copyright 2024 author - Hoàng Anh Tiến
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
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
