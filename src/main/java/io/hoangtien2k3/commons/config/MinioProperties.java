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
