package io.hoangtien2k3.filter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.support.DefaultClientCodecConfigurer;

@Configuration
public class CodecConfigurerConfig {

    @Bean
    public DefaultClientCodecConfigurer codecConfigurer() {
        return new DefaultClientCodecConfigurer();
    }

}
