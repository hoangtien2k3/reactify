package io.hoangtien2k3.keycloak.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Getter
@NoArgsConstructor
@Slf4j
@Component
public class KeycloakProvider {

    private static final Map<String, ClientRepresentation> clientMap = new HashMap<>();
    private volatile Keycloak keycloak = null;

    @Value("${keycloak.serverUrl}")
    public String serverURL;

    @Value("${keycloak.realm}")
    public String realm;

    @Value("${keycloak.clientId}")
    public String clientID;

    @Value("${keycloak.clientSecret}")
    public String clientSecret;

    // volatile and Double-Checked Locking -> Thread-Safe.
    public Keycloak getInstance() {
        if (keycloak == null) {
            synchronized (KeycloakProvider.class) {
                if (keycloak == null) {
                    keycloak = KeycloakBuilder.builder()
                            .serverUrl(serverURL)
                            .realm(realm)
                            .clientId(clientID)
                            .clientSecret(clientSecret)
                            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                            .build();
                }
            }
        }
        return keycloak;
    }

    public RealmResource getRealmResource() {
        return getInstance().realm(realm);
    }

    public Mono<ClientRepresentation> getClient(String clientId) {
        var clients = getRealmResource().clients().findByClientId(clientId);
        if (clients == null) {
            return Mono.empty();
        }
        for (ClientRepresentation clientRepresentation : clients) {
            if (clientRepresentation.isEnabled()) {
                return Mono.justOrEmpty(clientRepresentation);
            }
        }
        return Mono.empty();
    }

    public Mono<String> getKCIdFromClientId(String clientId) {
        return getClient(clientId).flatMap(rs -> Mono.just(rs.getId()));
    }

    public Mono<ClientRepresentation> getClientWithSecret(String clientId) {
        return getClient(clientId).flatMap(clientRepresentation -> {
            var result = getRealmResource()
                    .clients()
                    .get(clientRepresentation.getId())
                    .getSecret();
            clientRepresentation.setSecret(result.getValue());
            return Mono.just(clientRepresentation);
        });
    }
}
