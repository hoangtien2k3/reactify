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
package io.hoangtien2k3.commons.config.security.http;

import io.hoangtien2k3.commons.config.WhiteListProperties;
import io.hoangtien2k3.commons.model.WhiteList;
import io.hoangtien2k3.commons.utils.DataUtil;
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
 * Configuration class for setting up security and CORS (Cross-Origin Resource
 * Sharing) policies in a Spring WebFlux application. This class configures how
 * security is handled across the application, including whitelisting certain
 * URIs and methods, setting up CORS configurations, and configuring OAuth2
 * resource server support.
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebConfig {

    private final WhiteListProperties whiteListProperties;

    /**
     * Configures the security filter chain for the application.
     *
     * <p>
     * Sets up various security settings including CORS, authorization rules, and
     * OAuth2 resource server configurations. The method reads whitelist
     * configurations and applies them to permit access to certain paths and methods
     * without authentication.
     * </p>
     *
     * @param http
     *            the {@link ServerHttpSecurity} instance used to configure the
     *            security settings
     * @param jwtAuthenticationConverter
     *            the {@link Converter} to convert JWT tokens to authentication
     *            tokens
     * @return a {@link SecurityWebFilterChain} configured with the defined security
     *         settings
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

    /**
     * Provides CORS configuration for the application.
     *
     * <p>
     * Sets up CORS to allow requests from any origin with any HTTP method, and
     * permits all headers. CORS settings apply to all paths in the application.
     * </p>
     *
     * @return a {@link CorsConfigurationSource} instance with the defined CORS
     *         settings
     */
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