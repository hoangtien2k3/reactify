/*
 * Copyright 2024 author - Hoàng Anh Tiến
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 */
package io.hoangtien2k3.commons.config.security.keycloak;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

/**
 * Configuration class for setting up Keycloak-related beans in the Spring
 * Security configuration.
 * <p>
 * This configuration class defines beans to convert JWT tokens from Keycloak
 * into Spring Security authentication tokens and authorities.
 * </p>
 */
@Configuration
public class KeycloakConfiguration {

    /**
     * Provides a {@link Converter} to convert a {@link Jwt} to a {@link Collection}
     * of {@link GrantedAuthority}.
     * <p>
     * This bean is responsible for extracting and converting roles or authorities
     * from the JWT token issued by Keycloak. The
     * {@link KeycloakGrantedAuthoritiesConverter} implementation will use the
     * client ID to correctly parse and map the JWT claims to Spring Security
     * authorities.
     * </p>
     *
     * @param clientId
     *            the client ID used to configure the authority converter
     * @return a {@link Converter} that transforms a {@link Jwt} into a
     *         {@link Collection} of {@link GrantedAuthority}
     */
    @Bean
    Converter<Jwt, Collection<GrantedAuthority>> keycloakGrantedAuthoritiesConverter(
            @Value("${spring.security.oauth2.keycloak.client-id}") String clientId) {
        return new KeycloakGrantedAuthoritiesConverter(clientId);
    }

    /**
     * Provides a {@link Converter} to convert a {@link Jwt} into a {@link Mono} of
     * {@link AbstractAuthenticationToken}.
     * <p>
     * This bean sets up a converter to transform JWT tokens into reactive
     * authentication tokens, integrating with Keycloak for OAuth2 authentication.
     * The {@link ReactiveKeycloakJwtAuthenticationConverter} uses the provided
     * {@link Converter} for authorities to complete the authentication process.
     * </p>
     *
     * @param converter
     *            a {@link Converter} that transforms a {@link Jwt} into a
     *            {@link Collection} of {@link GrantedAuthority}
     * @return a {@link Converter} that converts a {@link Jwt} into a {@link Mono}
     *         of {@link AbstractAuthenticationToken}
     */
    @Bean
    Converter<Jwt, Mono<AbstractAuthenticationToken>> keycloakJwtAuthenticationConverter(
            Converter<Jwt, Collection<GrantedAuthority>> converter) {
        return new ReactiveKeycloakJwtAuthenticationConverter(converter);
    }
}
