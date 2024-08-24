package io.hoangtien2k3.commons.aop.cache.redis.configuration;

import io.hoangtien2k3.commons.aop.cache.redis.RedisCacheKeyCounter;
import io.hoangtien2k3.commons.aop.cache.redis.RedisCacheMetricsCollector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "cache2l.enable-global-cache", havingValue = "true")
public class RedisCache2lCollectorConfiguration {

    @Bean
    public RedisCacheKeyCounter redisCacheKey() {
        return new RedisCacheKeyCounter();
    }

    @Bean(name = "redisCacheMetricsCollector")
    public RedisCacheMetricsCollector initRedisCacheMetricCollector(RedisCacheKeyCounter redisCacheKey) {
        return new RedisCacheMetricsCollector(redisCacheKey).register();
    }
}
