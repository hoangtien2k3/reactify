package io.hoangtien2k3.commons.config;

import io.hoangtien2k3.commons.model.WhiteList;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "application")
public class WhiteListProperties {
    private List<WhiteList> whiteList;
}
