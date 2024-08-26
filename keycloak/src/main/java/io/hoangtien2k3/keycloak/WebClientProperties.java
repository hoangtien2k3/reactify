package io.hoangtien2k3.keycloak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebClientProperties {
    private String name;
    private String address;
    private String username;
    private String password;
    private String authorization;
    private PoolProperties pool = new PoolProperties();
    private TimeoutProperties timeout = new TimeoutProperties();
    private RetryProperties retry = new RetryProperties();
    private ClientLogProperties log = new ClientLogProperties();
    private ProxyProperties proxy = new ProxyProperties();
    private List<ExchangeFilterFunction> customFilters;
    private boolean internalOauth = false;
}