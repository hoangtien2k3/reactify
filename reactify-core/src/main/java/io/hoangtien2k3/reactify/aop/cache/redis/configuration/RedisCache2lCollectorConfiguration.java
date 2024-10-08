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
package io.hoangtien2k3.reactify.aop.cache.redis.configuration;

import io.hoangtien2k3.reactify.aop.cache.redis.RedisCacheKeyCounter;
import io.hoangtien2k3.reactify.aop.cache.redis.RedisCacheMetricsCollector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * RedisCache2lCollectorConfiguration class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Configuration
@ConditionalOnProperty(value = "cache2l.enable-global-cache", havingValue = "true")
public class RedisCache2lCollectorConfiguration {

    /**
     * <p>
     * redisCacheKey.
     * </p>
     *
     * @return a
     *         {@link io.hoangtien2k3.reactify.aop.cache.redis.RedisCacheKeyCounter}
     *         object
     */
    @Bean
    public RedisCacheKeyCounter redisCacheKey() {
        return new RedisCacheKeyCounter();
    }

    /**
     * <p>
     * initRedisCacheMetricCollector.
     * </p>
     *
     * @param redisCacheKey
     *            a
     *            {@link io.hoangtien2k3.reactify.aop.cache.redis.RedisCacheKeyCounter}
     *            object
     * @return a
     *         {@link io.hoangtien2k3.reactify.aop.cache.redis.RedisCacheMetricsCollector}
     *         object
     */
    @Bean(name = "redisCacheMetricsCollector")
    public RedisCacheMetricsCollector initRedisCacheMetricCollector(RedisCacheKeyCounter redisCacheKey) {
        return new RedisCacheMetricsCollector(redisCacheKey).register();
    }
}
