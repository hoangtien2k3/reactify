package io.hoangtien2k3.commons.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    private String baseUrl;
    private String publicUrl;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
