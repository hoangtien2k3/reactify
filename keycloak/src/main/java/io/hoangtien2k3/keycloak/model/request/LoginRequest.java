package io.hoangtien2k3.keycloak.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotEmpty(message = "login.username.not.empty")
    @NonNull
    private String username;

    @NotEmpty(message = "login.password.not.empty")
    private String password;

    private String clientId;
}
