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

import io.hoangtien2k3.reactify.aop.cache.Cache2LUtils;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>RedisExternalCache2lConfiguration class.</p>
 *
 * @author hoangtien2k3
 */
@Configuration
@ConditionalOnExpression("${cache2l.enable-global-cache:false} and ${cache2l.redis.externalMode:false}")
public class RedisExternalCache2lConfiguration {
    @Value("${cache2l.redis.externalCluster}")
    private String cluster;

    @Value("${cache2l.redis.password:}")
    private String redisPassword;

    @Value("${cache2l.redis.username:}")
    private String redisUsername;

    /**
     * <p>getLettuceConnectionFactory.</p>
     *
     * @return a {@link org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory} object
     */
    @Bean(name = "redisClusterExternalConnectionFactory")
    public LettuceConnectionFactory getLettuceConnectionFactory() {
        List<String> clusterNodes = Arrays.asList(cluster.split(","));
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterNodes);
        if (redisPassword != null && !redisPassword.isEmpty()) {
            redisClusterConfiguration.setPassword(redisPassword);
        }
        if (redisUsername != null && !redisUsername.isEmpty()) {
            redisClusterConfiguration.setUsername(redisUsername);
        }

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(30000))
                .build();
        return new LettuceConnectionFactory(redisClusterConfiguration, clientConfig);
    }

    /**
     * <p>getRedisTemplate.</p>
     *
     * @param redisConnectionFactory a {@link org.springframework.data.redis.connection.RedisConnectionFactory} object
     * @return a {@link org.springframework.data.redis.core.RedisTemplate} object
     */
    @Bean(name = "redisExternalCache2LTemplate")
    @ConditionalOnExpression("'${cache2l.redis.externalCluster:}' != ''")
    public RedisTemplate<Object, Object> getRedisTemplate(
            @Qualifier("redisClusterExternalConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        return Cache2LUtils.getRedisCache2lTemplate(redisConnectionFactory);
    }
}
