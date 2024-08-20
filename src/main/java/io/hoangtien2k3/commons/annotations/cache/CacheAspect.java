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

import java.util.Optional;

@Aspect
@Configuration
@Slf4j
public class CacheAspect {
  @Pointcut("@annotation(io.hoangtien2k3.commons.annotations.LocalCache)")
  private void processAnnotation() {
  }

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
