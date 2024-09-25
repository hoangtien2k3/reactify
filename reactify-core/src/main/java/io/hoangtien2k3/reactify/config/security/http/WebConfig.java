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
package io.hoangtien2k3.reactify.config.security.http;

import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.config.WhiteListProperties;
import io.hoangtien2k3.reactify.model.WhiteList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

/**
 * <p>
 * WebConfig class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebConfig {

    private final WhiteListProperties whiteListProperties;

    /**
     * <p>
     * springSecurityFilterChain.
     * </p>
     *
     * @param http
     *            a
     *            {@link org.springframework.security.config.web.server.ServerHttpSecurity}
     *            object
     * @param jwtAuthenticationConverter
     *            a {@link org.springframework.core.convert.converter.Converter}
     *            object
     * @return a
     *         {@link org.springframework.security.web.server.SecurityWebFilterChain}
     *         object
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http, Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        List<WhiteList> whiteListList = whiteListProperties.getWhiteList();
        if (!DataUtil.isNullOrEmpty(whiteListList)) {
            for (WhiteList whiteList : whiteListList) {
                String uri = whiteList.getUri();
                log.info("whitelist: {}", uri);
                List<String> methods = whiteList.getMethods();
                if (!DataUtil.isNullOrEmpty(methods)) {
                    for (String method : methods) {
                        HttpMethod convertedMethod = HttpMethod.valueOf(method);
                        http.authorizeExchange(authorize ->
                                authorize.pathMatchers(convertedMethod, uri).permitAll());
                    }
                } else {
                    http.authorizeExchange(
                            authorize -> authorize.pathMatchers(uri).permitAll());
                }
            }
        }

        http.cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .authorizeExchange(authorize -> authorize.anyExchange().authenticated())
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        http.headers(headers -> headers.frameOptions(
                frameOptions -> frameOptions.mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN)));

        return http.build();
    }

    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() {
    // UrlBasedCorsConfigurationSource configurationSource = new
    // UrlBasedCorsConfigurationSource();
    // CorsConfiguration corsConfiguration = new CorsConfiguration();
    // corsConfiguration.setAllowedOrigins(List.of("*"));
    // corsConfiguration.setAllowedMethods(List.of("*"));
    // corsConfiguration.setAllowedHeaders(List.of("*"));
    // configurationSource.registerCorsConfiguration("/**", corsConfiguration);
    // return configurationSource;
    // }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setMaxAge(7200L);
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.applyPermitDefaultValues();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
