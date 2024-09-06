package io.hoangtien2k3.keycloak.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserKeycloakRequest {
    private String username;
    private Boolean enabled;
    private String email;
    private List<String> groups;
    private List<CredentialKeycloakRequest> credentials;
}
