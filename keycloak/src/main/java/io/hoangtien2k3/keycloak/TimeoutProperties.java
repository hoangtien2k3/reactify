package io.hoangtien2k3.keycloak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeoutProperties {
    private int read = 180000;
    private int connection = 500;
}
