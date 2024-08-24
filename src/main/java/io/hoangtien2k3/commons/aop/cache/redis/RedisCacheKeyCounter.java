package io.hoangtien2k3.commons.aop.cache.redis;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class RedisCacheKeyCounter {
    protected final ConcurrentMap<String, RedisCacheStats> redisKeys = new ConcurrentHashMap<>();

    public void recordHit(String cacheName) {
        if (redisKeys.containsKey(cacheName)) {
            redisKeys.get(cacheName).getHitCount().add(1);
        } else {
            log.warn("Not found key {} to record hit", cacheName);
        }
    }

    public void recordMiss(String cacheName) {
        if (redisKeys.containsKey(cacheName)) {
            redisKeys.get(cacheName).getMissCount().add(1);
        } else {
            log.warn("Not found key {} to record miss", cacheName);
        }
    }

    public void addCache(String cacheName) {
        if (!redisKeys.containsKey(cacheName)) {
            redisKeys.put(cacheName, new RedisCacheStats());
        } else {
            log.warn("Redis key {} existed", cacheName);
        }
    }

    public long getHitsByKey(String cacheName) {
        if (redisKeys.containsKey(cacheName)) {
            return redisKeys.get(cacheName).getHitCount().sum();
        } else {
            log.warn("Not found key {} to get hit", cacheName);
            return -1L;
        }
    }

    public long getMissByKey(String cacheName) {
        if (redisKeys.containsKey(cacheName)) {
            return redisKeys.get(cacheName).getMissCount().sum();
        } else {
            log.warn("Not found key {} to get miss", cacheName);
            return -1L;
        }
    }

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

    public Set<String> getKeys() {
        return redisKeys.keySet();
    }
}