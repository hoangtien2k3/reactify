package io.hoangtien2k3.keycloak.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientLogin extends ProviderLogin {
    @NotEmpty(message = "login.client.id.not.empty")
    private String clientId;

    private String redirectUri;

    private String organizationId;
}
