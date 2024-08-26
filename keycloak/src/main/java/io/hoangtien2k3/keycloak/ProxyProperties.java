package io.hoangtien2k3.keycloak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProxyProperties {
    private boolean enable = false;
    private String httpHost;
    private Integer httpPort;
    private String httpsHost;
    private Integer httpsPort;
}
