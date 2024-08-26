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
package io.hoangtien2k3.commons.config.security.keycloak;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.ObjectUtils;

/**
 * A {@link Converter} implementation that converts a {@link Jwt} token from
 * Keycloak into a {@link Collection} of {@link GrantedAuthority}.
 * <p>
 * This converter handles the extraction and transformation of roles from the
 * JWT token issued by Keycloak. It combines realm roles and client-specific
 * roles into a single set of granted authorities.
 * </p>
 */
@RequiredArgsConstructor
public class KeycloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final String ROLES = "roles";
    private static final String CLAIM_REALM_ACCESS = "realm_access";
    private static final String RESOURCE_ACCESS = "resource_access";

    private final Converter<Jwt, Collection<GrantedAuthority>> defaultAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    private final String clientId;

    /**
     * Converts the provided {@link Jwt} into a {@link Collection} of
     * {@link GrantedAuthority}.
     * <p>
     * This method combines realm roles and client-specific roles extracted from the
     * JWT token. It also includes any default granted authorities provided by the
     * {@link JwtGrantedAuthoritiesConverter}.
     * </p>
     *
     * @param jwt
     *            the {@link Jwt} token to convert
     * @return a {@link Collection} of {@link GrantedAuthority} derived from the JWT
     *         token
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        var realmRoles = realmRoles(jwt);
        var clientRoles = clientRoles(jwt, clientId);

        Collection<GrantedAuthority> authorities = Stream.concat(realmRoles.stream(), clientRoles.stream())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        authorities.addAll(defaultGrantedAuthorities(jwt));

        return authorities;
    }

    /**
     * Extracts default granted authorities from the provided {@link Jwt} token
     * using the {@link JwtGrantedAuthoritiesConverter}.
     * <p>
     * If the default converter does not provide any authorities, an empty set is
     * returned.
     * </p>
     *
     * @param jwt
     *            the {@link Jwt} token to extract default authorities from
     * @return a {@link Collection} of default {@link GrantedAuthority}
     */
    private Collection<GrantedAuthority> defaultGrantedAuthorities(Jwt jwt) {
        return Optional.ofNullable(defaultAuthoritiesConverter.convert(jwt)).orElse(emptySet());
    }

    /**
     * Extracts realm roles from the provided {@link Jwt} token.
     * <p>
     * Realm roles are found in the "realm_access" claim of the JWT token.
     * </p>
     *
     * @param jwt
     *            the {@link Jwt} token to extract realm roles from
     * @return a {@link List} of realm role names
     */
    @SuppressWarnings("unchecked")
    protected List<String> realmRoles(Jwt jwt) {
        return Optional.ofNullable(jwt.getClaimAsMap(CLAIM_REALM_ACCESS))
                .map(realmAccess -> (List<String>) realmAccess.get(ROLES))
                .orElse(emptyList());
    }

    /**
     * Extracts client-specific roles from the provided {@link Jwt} token for a
     * specific client.
     * <p>
     * Client-specific roles are found in the "resource_access" claim of the JWT
     * token under the specified client ID.
     * </p>
     *
     * @param jwt
     *            the {@link Jwt} token to extract client roles from
     * @param clientId
     *            the client ID to find roles for
     * @return a {@link List} of client-specific role names
     */
    @SuppressWarnings("unchecked")
    protected List<String> clientRoles(Jwt jwt, String clientId) {
        if (ObjectUtils.isEmpty(clientId)) {
            return emptyList();
        }

        return Optional.ofNullable(jwt.getClaimAsMap(RESOURCE_ACCESS))
                .map(resourceAccess -> (Map<String, List<String>>) resourceAccess.get(clientId))
                .map(clientAccess -> clientAccess.get(ROLES))
                .orElse(emptyList());
    }
}
