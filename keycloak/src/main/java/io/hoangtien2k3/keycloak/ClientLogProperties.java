package io.hoangtien2k3.keycloak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ClientLogProperties {
    private boolean enable = true;
    private List<String> obfuscateHeaders = Arrays.asList("authorization", "proxy-authorization");
}
