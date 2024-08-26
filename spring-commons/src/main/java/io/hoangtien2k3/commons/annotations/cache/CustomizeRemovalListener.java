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
/** @deprecated */
@Deprecated
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
