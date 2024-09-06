package io.hoangtien2k3.keycloak.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialKeycloakRequest {
    private String type;
    private String value;
    private Boolean temporary;
}
