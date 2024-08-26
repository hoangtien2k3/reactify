/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hoangtien2k3.commons.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for Minio.
 * <p>
 * This class binds properties from the application's configuration file (e.g.,
 * application.yml or application.properties) with the prefix 'minio'. It
 * provides configuration values necessary for interacting with the Minio object
 * storage service.
 * </p>
 * <p>
 * Each property corresponds to a specific configuration parameter required for
 * setting up the Minio client.
 * </p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * The base URL of the Minio service.
     * <p>
     * This URL is used to connect to the Minio server for performing storage
     * operations.
     * </p>
     * Example: "http://localhost:9000"
     */
    private String baseUrl;

    /**
     * The public URL for accessing objects stored in Minio.
     * <p>
     * This URL can be used to access files directly from the Minio server if public
     * access is enabled.
     * </p>
     * Example: "http://localhost:9000/public"
     */
    private String publicUrl;

    /**
     * The access key used for authentication with the Minio service.
     * <p>
     * This key is used along with the secret key to authorize requests to the Minio
     * server.
     * </p>
     * Example: "minio-access-key"
     */
    private String accessKey;

    /**
     * The secret key used for authentication with the Minio service.
     * <p>
     * This key is used along with the access key to authorize requests to the Minio
     * server.
     * </p>
     * Example: "minio-secret-key"
     */
    private String secretKey;

    /**
     * The default bucket name for storing objects in Minio.
     * <p>
     * This bucket is used to organize and store objects. All objects will be stored
     * in this bucket by default.
     * </p>
     * Example: "my-bucket"
     */
    private String bucket;
}
