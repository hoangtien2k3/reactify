package io.hoangtien2k3.keycloak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyCloakProperties {
    private String clientId;
    private String clientSecret;
}
