/*
 * Copyright 2024 author - Hoàng Anh Tiến
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
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
