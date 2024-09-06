package io.hoangtien2k3.keycloak.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeycloakErrorResponse extends AccessToken {
    @JsonProperty("error")
    protected String error;

    @JsonProperty("error_description")
    protected String errorDescription;
}
