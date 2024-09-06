package io.hoangtien2k3.keycloak.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestSync {
    private String clientId;
    private String clientSecret;
}