package io.hoangtien2k3.commons.aop.cache.redis.configuration;

import io.hoangtien2k3.commons.aop.cache.Cache2LUtils;
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

@Configuration
@ConditionalOnExpression("${cache2l.enable-global-cache:false} and ${cache2l.redis.externalMode:false}")
public class RedisExternalCache2lConfiguration {
    @Value("${cache2l.redis.externalCluster}")
    private String cluster;

    @Value("${cache2l.redis.password:}")
    private String redisPassword;

    @Value("${cache2l.redis.username:}")
    private String redisUsername;

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

    @Bean(name = "redisExternalCache2LTemplate")
    @ConditionalOnExpression("'${cache2l.redis.externalCluster:}' != ''")
    public RedisTemplate<Object, Object> getRedisTemplate(
            @Qualifier("redisClusterExternalConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        return Cache2LUtils.getRedisCache2lTemplate(redisConnectionFactory);
    }
}
