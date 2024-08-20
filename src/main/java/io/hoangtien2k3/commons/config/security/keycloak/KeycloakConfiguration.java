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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Configuration
public class KeycloakConfiguration {

  @Bean
  Converter<Jwt, Collection<GrantedAuthority>> keycloakGrantedAuthoritiesConverter(
      @Value("${spring.security.oauth2.keycloak.client-id}") String clientId) {
    return new KeycloakGrantedAuthoritiesConverter(clientId);
  }

  @Bean
  Converter<Jwt, Mono<AbstractAuthenticationToken>> keycloakJwtAuthenticationConverter(
      Converter<Jwt, Collection<GrantedAuthority>> converter) {
    return new ReactiveKeycloakJwtAuthenticationConverter(converter);
  }
}
