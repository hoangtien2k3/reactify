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

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the Minio client.
 * <p>
 * This class configures a {@link MinioClient} bean that is used to interact
 * with the Minio object storage service.
 * </p>
 * <p>
 * The Minio client is configured based on properties defined in
 * {@link MinioProperties} and only created if the 'minio.enabled' property is
 * set to 'true'.
 * </p>
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MinioConfiguration {

    private final MinioProperties minioProperties;

    /**
     * Configures the {@link MinioClient} bean.
     * <p>
     * This bean is created only if the 'minio.enabled' property is set to 'true'.
     * The Minio client is initialized with the endpoint URL, access key, and secret
     * key from the {@link MinioProperties} class.
     * </p>
     * <p>
     * The Minio client allows interaction with Minio object storage, including
     * operations such as uploading, downloading, and managing objects in the
     * storage service.
     * </p>
     *
     * @return an instance of {@link MinioClient} configured with properties from
     *         {@link MinioProperties}
     */
    @Bean
    @ConditionalOnProperty(value = "minio.enabled", havingValue = "true", matchIfMissing = false)
    public MinioClient minioClient() {
        log.info("Configuring Minio client: {}", minioProperties.getBaseUrl());
        return MinioClient.builder()
                .endpoint(minioProperties.getBaseUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}
