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
package io.hoangtien2k3.reactify.aop.cache.redis;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * <p>RedisCacheKeyCounter class.</p>
 *
 * @author hoangtien2k3
 */
@Log4j2
@Component
public class RedisCacheKeyCounter {
    protected final ConcurrentMap<String, RedisCacheStats> redisKeys = new ConcurrentHashMap<>();

    /**
     * <p>recordHit.</p>
     *
     * @param cacheName a {@link java.lang.String} object
     */
    public void recordHit(String cacheName) {
        if (redisKeys.containsKey(cacheName)) {
            redisKeys.get(cacheName).getHitCount().add(1);
        } else {
            log.warn("Not found key {} to record hit", cacheName);
        }
    }

    /**
     * <p>recordMiss.</p>
     *
     * @param cacheName a {@link java.lang.String} object
     */
    public void recordMiss(String cacheName) {
        if (redisKeys.containsKey(cacheName)) {
            redisKeys.get(cacheName).getMissCount().add(1);
        } else {
            log.warn("Not found key {} to record miss", cacheName);
        }
    }

    /**
     * <p>addCache.</p>
     *
     * @param cacheName a {@link java.lang.String} object
     */
    public void addCache(String cacheName) {
        if (!redisKeys.containsKey(cacheName)) {
            redisKeys.put(cacheName, new RedisCacheStats());
        } else {
            log.warn("Redis key {} existed", cacheName);
        }
    }

    /**
     * <p>getHitsByKey.</p>
     *
     * @param cacheName a {@link java.lang.String} object
     * @return a long
     */
    public long getHitsByKey(String cacheName) {
        if (redisKeys.containsKey(cacheName)) {
            return redisKeys.get(cacheName).getHitCount().sum();
        } else {
            log.warn("Not found key {} to get hit", cacheName);
            return -1L;
        }
    }

    /**
     * <p>getMissByKey.</p>
     *
     * @param cacheName a {@link java.lang.String} object
     * @return a long
     */
    public long getMissByKey(String cacheName) {
        if (redisKeys.containsKey(cacheName)) {
            return redisKeys.get(cacheName).getMissCount().sum();
        } else {
            log.warn("Not found key {} to get miss", cacheName);
            return -1L;
        }
    }

    /**
     * <p>getRequestCountByKey.</p>
     *
     * @param cacheName a {@link java.lang.String} object
     * @return a long
     */
    public long getRequestCountByKey(String cacheName) {
        if (redisKeys.containsKey(cacheName)) {
            return saturatedAdd(this.getHitsByKey(cacheName), this.getMissByKey(cacheName));
        } else {
            log.warn("Not found key {} to get total request", cacheName);
            return -1L;
        }
    }

    private static long saturatedAdd(long a, long b) {
        long naiveSum = a + b;
        return (a ^ b) < 0L | (a ^ naiveSum) >= 0L ? naiveSum : Long.MAX_VALUE + (naiveSum >>> 63 ^ 1L);
    }

    /**
     * <p>getKeys.</p>
     *
     * @return a {@link java.util.Set} object
     */
    public Set<String> getKeys() {
        return redisKeys.keySet();
    }
}
