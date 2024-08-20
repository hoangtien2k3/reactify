package io.hoangtien2k3.commons.annotations.cache;

import io.hoangtien2k3.commons.config.ApplicationContextProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheUtils {

    public static void invokeMethod(Method method) {
        try {
            Class declaringClass = method.getDeclaringClass();
            Object t = ApplicationContextProvider.getApplicationContext().getBean(declaringClass);
            Mono<Object> rs = (Mono<Object>) method.invoke(t);
            rs.subscribe();
        } catch (Exception exception) {
            log.error("Error when autoload cache " + method.getDeclaringClass().getSimpleName() + "." + method.getName()
                    , exception.getMessage(), exception);
        }
    }

//    public static RedisTemplate<Object, Object> getRedisCache2lTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        serializer.setObjectMapper(objectMapper);
//        template.setKeySerializer(serializer);
//        template.setValueSerializer(serializer);
//        template.setValueSerializer(serializer);
//        return template;
//    }
}
