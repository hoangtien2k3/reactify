package io.hoangtien2k3.commons.aop.cache.redis.configuration;

import io.hoangtien2k3.commons.aop.cache.Cache2LUtils;
import java.time.Duration;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Log4j2
@Configuration
@ConditionalOnExpression("${cache2l.enable-global-cache:false} and !${cache2l.redis.externalMode:false}")
public class RedisK8sCache2lConfiguration {

    @Value("${cache2l.redis.master.host}")
    private String redisMasterHost;

    @Value("${cache2l.redis.slave.host}")
    private String redisSlaveHost;

    @Value("${cache2l.redis.master.port}")
    private int redisMasterPort;

    @Value("${cache2l.redis.slave.port}")
    private int redisSlavePort;

    @Value("${cache2l.redis.password:}")
    private String redisPassword;

    @Bean(name = "redisMasterConnectionFactory")
    public LettuceConnectionFactory redisMasterConnectionFactory() {
        return getLettuceConnectionFactory(redisMasterHost, redisMasterPort);
    }

    @Bean(name = "redisSlaveConnectionFactory")
    public LettuceConnectionFactory redisSlaveConnectionFactory() {
        return getLettuceConnectionFactory(redisSlaveHost, redisSlavePort);
    }

    private LettuceConnectionFactory getLettuceConnectionFactory(String redisHost, int redisPort) {
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration(redisHost, redisPort);
        if (redisPassword != null && !redisPassword.isEmpty()) {
            redisStandaloneConfiguration.setPassword(redisPassword);
        }

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(30000))
                .build();
        return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfig);
    }

    @Bean(name = "redisMasterCache2LTemplate")
    public RedisTemplate<Object, Object> redisMasterTemplate(
            @Qualifier("redisMasterConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        return Cache2LUtils.getRedisCache2lTemplate(redisConnectionFactory);
    }

    @Bean(name = "redisSlaveCache2LTemplate")
    public RedisTemplate<Object, Object> redisSlaveTemplate(
            @Qualifier("redisSlaveConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        return Cache2LUtils.getRedisCache2lTemplate(redisConnectionFactory);
    }
}
