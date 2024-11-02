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
package com.reactify;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.index.qual.NonNegative;
import reactor.core.publisher.Mono;

/**
 * <p>
 * CacheUtils class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Log4j2
public class CacheUtils {

    private static final String FIXED_KEY = "FIXED_KEY";

    private CacheUtils() {}

    /**
     * <p>
     * of.
     * </p>
     *
     * @param duration
     *            a {@link Duration} object
     * @param fn
     *            a {@link Function} object
     * @param <T>
     *            a T class
     * @return a {@link Function} object
     */
    public static <T> Function<String, T> of(@NotNull Duration duration, @NotNull Function<String, T> fn) {
        final LoadingCache<String, T> cache =
                Caffeine.newBuilder().expireAfterWrite(duration).build(fn::apply);

        return cache::get;
    }

    /**
     * <p>
     * of.
     * </p>
     *
     * @param duration
     *            a {@link Duration} object
     * @param supplier
     *            a {@link Supplier} object
     * @param <T>
     *            a T class
     * @return a {@link Supplier} object
     */
    public static <T> Supplier<T> of(@NotNull Duration duration, @NotNull Supplier<T> supplier) {
        Function<String, T> fn = of(duration, k -> supplier.get());
        return () -> fn.apply(FIXED_KEY);
    }

    /**
     * <p>
     * ofMono.
     * </p>
     *
     * @param duration
     *            a {@link Duration} object
     * @param fn
     *            a {@link Function} object
     * @param <T>
     *            a T class
     * @return a {@link Function} object
     */
    public static <T> Function<String, Mono<T>> ofMono(Duration duration, Function<String, Mono<T>> fn) {
        AsyncCache<String, T> cache =
                Caffeine.newBuilder().expireAfterWrite(duration).buildAsync();
        return key -> Mono.fromFuture(cache.get(key, k -> (T) fn.apply(k).toFuture()));
    }

    /**
     * <p>
     * ofMonoFixedKey.
     * </p>
     *
     * @param duration
     *            a {@link Duration} object
     * @param mono
     *            a {@link Mono} object
     * @param <T>
     *            a T class
     * @return a {@link Mono} object
     */
    public static <T> Mono<T> ofMonoFixedKey(@NotNull Duration duration, @NotNull Mono<T> mono) {
        Function<String, Mono<T>> monoFn = ofMono(duration, key -> mono);
        return Mono.defer(() -> monoFn.apply(FIXED_KEY));
    }

    // TODO : close cache metrics to build test local
    /**
     * <p>
     * caffeine.
     * </p>
     *
     * @param duration
     *            a {@link Duration} object
     * @param maximumSize
     *            a long
     * @param cacheClass
     *            a {@link Class} object
     * @param cacheName
     *            a {@link String} object
     * @param <K>
     *            a K class
     * @param <V>
     *            a V class
     * @return a {@link Cache} object
     */
    public static <K, V> Cache<K, V> caffeine(
            @NotNull Duration duration,
            @NonNegative long maximumSize,
            @NotNull Class<?> cacheClass,
            @NonNull String cacheName) {
        return Caffeine.newBuilder()
                .expireAfterWrite(duration)
                .recordStats()
                .maximumSize(maximumSize)
                .build();
    }
}
