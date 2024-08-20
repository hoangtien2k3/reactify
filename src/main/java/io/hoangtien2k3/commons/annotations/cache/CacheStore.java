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

import io.hoangtien2k3.commons.annotations.LocalCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class CacheStore {

  // private static final CacheMetricsCollector CACHE_METRICS_COLLECTOR = new
  // CacheMetricsCollector().register();
  private static final HashMap<String, Cache<Object, Object>> caches = new HashMap<>();
  private static final Set<Method> autoLoadMethods = new HashSet<>();
  private static final String reflectionPath = "com.ezbuy";

  @PostConstruct
  private static void init() {
    log.info("Start initializing cache");
    Reflections reflections = new Reflections(reflectionPath, Scanners.MethodsAnnotated);
    Set<Method> methods = reflections.get(Scanners.MethodsAnnotated.with(LocalCache.class).as(Method.class));
    for (Method method : methods) {
      String className = method.getDeclaringClass().getSimpleName();
      LocalCache localCache = method.getAnnotation(LocalCache.class);
      Integer maxRecord = localCache.maxRecord();
      Integer durationInMinute = localCache.durationInMinute();
      String cacheName = className + "." + method.getName();
      boolean autoLoad = localCache.autoCache();
      Cache<Object, Object> cache;
      if (autoLoad && (method.getParameterCount() == 0)) {
        cache = Caffeine.newBuilder().scheduler(Scheduler.systemScheduler())
            .expireAfterWrite(Duration.ofMinutes(durationInMinute)).recordStats().maximumSize(maxRecord)
            .removalListener(new CustomizeRemovalListener(method)).build();
        autoLoadMethods.add(method);
      } else {
        cache = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(durationInMinute)).recordStats()
            .maximumSize(maxRecord).build();
      }
      caches.put(cacheName, cache);
      // CACHE_METRICS_COLLECTOR.addCache(cacheName, cache);
    }
    log.info("Finish initializing {} cache", caches.size());
  }

  public static Cache getCache(String key) {
    return caches.get(key);
  }

  @Async
  @EventListener
  public void autoLoad(ContextRefreshedEvent event) {
    if (!autoLoadMethods.isEmpty()) {
      log.info("Start auto load {} cache", autoLoadMethods.size());
      for (Method method : autoLoadMethods) {
        CacheUtils.invokeMethod(method);
      }
      log.info("Finish auto load cache");
    }
  }
}
