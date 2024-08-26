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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import io.hoangtien2k3.commons.annotations.LocalCache;
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
 * {@code CacheStore} is a class that manages the creation and storage of caches
 * using the Caffeine library.
 *
 * <p>
 * This class uses {@link Caffeine} to create and manage caches. It initializes
 * caches from methods annotated with {@link LocalCache} and stores them in a
 * static map. Additionally, it supports auto-loading of data into caches when
 * the application starts up.
 * </p>
 *
 * <p>
 * The class uses {@link Reflections} to scan for methods within the specified
 * package that are annotated with {@link LocalCache}, and creates corresponding
 * caches based on the configuration provided in the annotation.
 * </p>
 *
 * <p>
 * Caches are configured with expiration times and maximum sizes according to
 * the {@link LocalCache} annotation. Methods with no parameters and
 * {@code autoCache} enabled are automatically populated with data when the
 * application starts.
 * </p>
 *
 * @see LocalCache
 * @see Caffeine
 * @see Reflections
 */
/** @deprecated */
@Deprecated
@Slf4j
@Component
public class CacheStore {

    private static final HashMap<String, Cache<Object, Object>> caches = new HashMap<>();
    private static final Set<Method> autoLoadMethods = new HashSet<>();
    private static final String reflectionPath = "com.ezbuy";

    /**
     * Initializes caches and configurations from methods annotated with
     * {@link LocalCache}.
     * <p>
     * This method is called after bean initialization is complete to scan for
     * methods annotated with {@link LocalCache} and configure the corresponding
     * caches.
     * </p>
     */
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
        }
        log.info("Finish initializing {} cache", caches.size());
    }

    /**
     * Retrieves a cache by its key.
     *
     * @param key
     *            the key of the cache to retrieve
     * @return the {@link Cache} instance associated with the specified key, or
     *         {@code null} if no cache is found
     */
    public static Cache<Object, Object> getCache(String key) {
        return caches.get(key);
    }

    /**
     * Automatically loads data into caches associated with methods annotated with
     * {@link LocalCache}.
     * <p>
     * This method is triggered when the application context is refreshed. It
     * invokes methods that are marked for auto-loading to populate the caches with
     * data.
     * </p>
     *
     * @param event
     *            the {@link ContextRefreshedEvent} that triggers the auto-loading
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
