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

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * This class implements the {@link RemovalListener} interface and provides
 * custom handling for cache entry removals. It logs information when a cache
 * entry is evicted and invokes a specified method.
 *
 * <p>
 * The class uses Lombok annotations {@code @Slf4j} for logging and
 * {@code @AllArgsConstructor} to generate a constructor with one parameter for
 * each field in the class.
 *
 * <p>
 * This listener is typically used in caching mechanisms where specific actions
 * need to be taken when an entry is removed due to eviction.
 *
 * <p>
 * The listener relies on a {@link Method} instance, which is passed through the
 * constructor. This method is invoked whenever a cache entry associated with it
 * is evicted.
 *
 * <p>
 * Example usage:
 *
 * <pre>
 * {@code
 * Cache<String, Object> cache = Caffeine.newBuilder().removalListener(new CustomizeRemovalListener(someMethod))
 * 		.build();
 * }
 * </pre>
 *
 * @author Your Name
 */
@Slf4j
@AllArgsConstructor
public class CustomizeRemovalListener implements RemovalListener {
    private final Method method;

    /**
     * Handles the removal of a cache entry. If the removal cause is eviction, this
     * method logs the event and invokes the method provided during the creation of
     * this listener.
     *
     * @param key
     *            the key of the removed cache entry, can be null
     * @param value
     *            the value of the removed cache entry, can be null
     * @param removalCause
     *            the cause of the removal, never null
     */
    @Override
    public void onRemoval(@Nullable Object key, @Nullable Object value, @NonNull RemovalCause removalCause) {
        if (removalCause.wasEvicted()) {
            log.info(
                    "Cache {}.{} was evicted because {}",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    removalCause);
            CacheUtils.invokeMethod(method);
        }
    }
}
