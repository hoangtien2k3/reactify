package io.hoangtien2k3.commons.aop.cache;

import io.hoangtien2k3.commons.config.ApplicationContextProvider;
import java.lang.reflect.Method;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import reactor.core.publisher.Mono;

@Log4j2
public class Cache2LUtils {
    public static void invokeMethod(Method method) {
        try {
            Class declaringClass = method.getDeclaringClass();
            Object t = ApplicationContextProvider.getApplicationContext().getBean(declaringClass);
            Mono<Object> rs = (Mono<Object>) method.invoke(t);
            rs.subscribe();
        } catch (Exception exception) {
            log.error(
                    "Error when autoload cache {}.{}",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    exception.getMessage(),
                    exception);
        }
    }

    public static RedisTemplate<Object, Object> getRedisCache2lTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setKeySerializer(serializer);
        template.setValueSerializer(serializer);
        return template;
    }
}
