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
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A converter that adapts a JWT to a {@link Mono<AbstractAuthenticationToken>}
 * using a provided {@link Converter} for converting JWT claims to
 * {@link GrantedAuthority} instances.
 *
 * <p>
 * This class implements the {@link Converter} interface to convert a
 * {@link Jwt} into a {@link Mono<AbstractAuthenticationToken>} which contains
 * the authentication information extracted from the JWT. The {@link Converter}
 * provided is used to convert JWT claims into a collection of granted
 * authorities. The username is extracted from the JWT claims, defaulting to the
 * JWT subject if the preferred username claim is not present.
 * </p>
 *
 * <p>
 * The class is used in conjunction with Spring Security's reactive support for
 * JWT authentication. It allows for customized handling of JWT claims and
 * authorities, providing a way to integrate with Keycloak or other OAuth2
 * providers.
 * </p>
 *
 * @see org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter
 */
public final class ReactiveKeycloakJwtAuthenticationConverter
        implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private static final String USERNAME_CLAIM = "preferred_username";
    private final Converter<Jwt, Flux<GrantedAuthority>> jwtGrantedAuthoritiesConverter;

    /**
     * Constructs a new {@code ReactiveKeycloakJwtAuthenticationConverter} with the
     * specified {@link Converter} for converting JWTs to a collection of
     * {@link GrantedAuthority}.
     *
     * @param jwtGrantedAuthoritiesConverter
     *            the converter used to extract authorities from the JWT
     * @throws IllegalArgumentException
     *             if {@code jwtGrantedAuthoritiesConverter} is {@code null}
     */
    public ReactiveKeycloakJwtAuthenticationConverter(
            Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
        Assert.notNull(jwtGrantedAuthoritiesConverter, "jwtGrantedAuthoritiesConverter cannot be null");
        this.jwtGrantedAuthoritiesConverter =
                new ReactiveJwtGrantedAuthoritiesConverterAdapter(jwtGrantedAuthoritiesConverter);
    }

    /**
     * Converts the given JWT into a {@link Mono<AbstractAuthenticationToken>}.
     *
     * @param jwt
     *            the JWT to convert
     * @return a {@link Mono} containing the {@link AbstractAuthenticationToken} for
     *         the JWT
     */
    @Override
    public Mono<AbstractAuthenticationToken> convert(@NotNull Jwt jwt) {
        // @formatter:off
        return Objects.requireNonNull(this.jwtGrantedAuthoritiesConverter.convert(jwt))
                .collectList()
                .map((authorities) -> new JwtAuthenticationToken(jwt, authorities, extractUsername(jwt)));
        // @formatter:on
    }

    /**
     * Extracts the username from the JWT claims.
     *
     * @param jwt
     *            the JWT from which to extract the username
     * @return the extracted username, or the JWT subject if the preferred username
     *         claim is not present
     */
    private String extractUsername(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        if (claims.containsKey(USERNAME_CLAIM)) {
            return (String) claims.get(USERNAME_CLAIM);
        }
        return jwt.getSubject();
    }
}
