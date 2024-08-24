package io.hoangtien2k3.commons.aop.cache.redis;

import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheMetricsCollector extends Collector {

    private final RedisCacheKeyCounter redisCacheKey;

    public RedisCacheMetricsCollector(RedisCacheKeyCounter redisCacheKey) {
        this.redisCacheKey = redisCacheKey;
    }

    public void addCache(String cacheName) {
        redisCacheKey.addCache(cacheName);
    }

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<>();
        List<String> labelNames = List.of("cache");
        CounterMetricFamily cacheHitTotal =
                new CounterMetricFamily("redis_cache_hit_total", "Redis Cache hit totals by key", labelNames);
        mfs.add(cacheHitTotal);
        CounterMetricFamily cacheMissTotal =
                new CounterMetricFamily("redis_cache_miss_total", "Redis Cache miss totals by key", labelNames);
        mfs.add(cacheMissTotal);
        CounterMetricFamily cacheRequestTotal =
                new CounterMetricFamily("redis_cache_request_total", "Redis Cache request totals by key", labelNames);
        mfs.add(cacheRequestTotal);

        Set<String> keys = redisCacheKey.getKeys();
        for (String key : keys) {
            List<String> redisKey = Collections.singletonList(key);
            cacheHitTotal.addMetric(redisKey, (double) redisCacheKey.getHitsByKey(key));
            cacheMissTotal.addMetric(redisKey, (double) redisCacheKey.getMissByKey(key));
            cacheRequestTotal.addMetric(redisKey, (double) redisCacheKey.getRequestCountByKey(key));
        }
        return mfs;
    }
}
