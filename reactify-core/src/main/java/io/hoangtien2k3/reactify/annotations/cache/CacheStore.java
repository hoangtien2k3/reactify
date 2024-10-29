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
package io.hoangtien2k3.reactify.annotations.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import io.hoangtien2k3.reactify.annotations.LocalCache;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * <p>
 * CacheStore class.
 * </p>
 *
 * @author hoangtien2k3
 */
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
        Set<Method> methods =
                reflections.get(Scanners.MethodsAnnotated.with(LocalCache.class).as(Method.class));
        for (Method method : methods) {
            String className = method.getDeclaringClass().getSimpleName();
            LocalCache localCache = method.getAnnotation(LocalCache.class);
            Integer maxRecord = localCache.maxRecord();
            Integer durationInMinute = localCache.durationInMinute();
            String cacheName = className + "." + method.getName();
            boolean autoLoad = localCache.autoCache();
            Cache<Object, Object> cache;
            if (autoLoad && (method.getParameterCount() == 0)) {
                cache = Caffeine.newBuilder()
                        .scheduler(Scheduler.systemScheduler())
                        .expireAfterWrite(Duration.ofMinutes(durationInMinute))
                        .recordStats()
                        .maximumSize(maxRecord)
                        .removalListener(new CustomizeRemovalListener(method))
                        .build();
                autoLoadMethods.add(method);
            } else {
                cache = Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(durationInMinute))
                        .recordStats()
                        .maximumSize(maxRecord)
                        .build();
            }
            caches.put(cacheName, cache);
            // CACHE_METRICS_COLLECTOR.addCache(cacheName, cache);
        }
        log.info("Finish initializing {} cache", caches.size());
    }

    /**
     * <p>
     * getCache.
     * </p>
     *
     * @param key
     *            a {@link String} object
     * @return a {@link Cache} object
     */
    public static Cache<Object, Object> getCache(String key) {
        return caches.get(key);
    }

    /**
     * <p>
     * autoLoad.
     * </p>
     *
     * @param event
     *            a {@link ContextRefreshedEvent} object
     */
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
