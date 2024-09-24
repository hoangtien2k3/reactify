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

import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;
import java.util.*;
import org.springframework.stereotype.Component;

/**
 * <p>RedisCacheMetricsCollector class.</p>
 *
 * @author hoangtien2k3
 */
@Component
public class RedisCacheMetricsCollector extends Collector {

    private final RedisCacheKeyCounter redisCacheKey;

    /**
     * <p>Constructor for RedisCacheMetricsCollector.</p>
     *
     * @param redisCacheKey a {@link io.hoangtien2k3.reactify.aop.cache.redis.RedisCacheKeyCounter} object
     */
    public RedisCacheMetricsCollector(RedisCacheKeyCounter redisCacheKey) {
        this.redisCacheKey = redisCacheKey;
    }

    /**
     * <p>addCache.</p>
     *
     * @param cacheName a {@link java.lang.String} object
     */
    public void addCache(String cacheName) {
        redisCacheKey.addCache(cacheName);
    }

    /** {@inheritDoc} */
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
