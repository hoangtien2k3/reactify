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

import com.github.benmanes.caffeine.cache.Cache;
import io.hoangtien2k3.commons.annotations.LocalCache;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

/**
 * Aspect for managing cache operations based on the {@link LocalCache}
 * annotation.
 *
 * <p>
 * This aspect intercepts method calls annotated with {@link LocalCache} and
 * handles caching of method results. It uses a cache store to retrieve results
 * if they are present, and if not, it proceeds with the method execution,
 * stores the result in the cache, and then returns it.
 * </p>
 *
 * <p>
 * The cache key is generated based on the method arguments, and the cache is
 * identified using the method name and class name of the target object.
 * </p>
 *
 * @see LocalCache
 * @see CacheStore
 * @see SimpleKeyGenerator
 * @see CacheMono
 * @see Signal
 */
@Aspect
@Configuration
@Slf4j
public class CacheAspect {

    /**
     * Pointcut for methods annotated with {@link LocalCache}.
     */
    @Pointcut("@annotation(io.hoangtien2k3.commons.annotations.LocalCache)")
    private void processAnnotation() {}

    /**
     * Around advice for caching method results.
     *
     * <p>
     * This method intercepts the execution of a method annotated with
     * {@link LocalCache}, retrieves the result from the cache if available, or
     * proceeds with the method execution if the result is not cached. The result is
     * then stored in the cache for future use.
     * </p>
     *
     * @param joinPoint
     *            the join point representing the intercepted method call
     * @return the result of the method call, either from the cache or from the
     *         method execution
     * @throws Throwable
     *             if the method call or caching operation fails
     */
    @Around("processAnnotation()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object key = SimpleKeyGenerator.generateKey(args);
        String name = ClassUtils.getUserClass(joinPoint.getTarget().getClass()).getSimpleName() + "."
                + joinPoint.getSignature().getName();
        Cache cache = CacheStore.getCache(name);

        return CacheMono.lookup(k -> Mono.justOrEmpty(cache.getIfPresent(key)).map(Signal::next), key)
                .onCacheMissResume((Mono<Object>) joinPoint.proceed(args))
                .andWriteWith((k, sig) -> Mono.fromRunnable(() -> {
                    if (sig != null && sig.get() != null) {
                        if (!(sig.get() instanceof Optional && ((Optional) sig.get()).isEmpty())) {
                            cache.put(k, sig.get());
                        }
                    }
                }));
    }
}
