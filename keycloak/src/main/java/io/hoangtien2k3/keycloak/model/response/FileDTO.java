package io.hoangtien2k3.keycloak.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {
    private String id;
    private String name;
    private String imageBase64;
    private String filePath;
}

