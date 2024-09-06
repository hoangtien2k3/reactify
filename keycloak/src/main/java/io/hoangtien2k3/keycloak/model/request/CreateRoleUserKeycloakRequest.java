package io.hoangtien2k3.keycloak.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleUserKeycloakRequest {
    private String id;
    private String name;
}
