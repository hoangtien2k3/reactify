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
package io.hoangtien2k3.commons.filter.properties;

import io.hoangtien2k3.commons.model.logging.HttpLogRequest;
import io.hoangtien2k3.commons.model.logging.HttpLogResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for HTTP logging settings.
 *
 * <p>
 * This class is used to configure the HTTP request and response logging
 * settings in the application. The properties are loaded from the application's
 * configuration file with the prefix {@code application.http-logging}. It
 * allows you to specify settings for logging both HTTP requests and responses.
 * </p>
 *
 * <p>
 * It contains the following properties:
 * </p>
 * <ul>
 * <li>{@link HttpLogRequest} - Configuration settings for HTTP request
 * logging.</li>
 * <li>{@link HttpLogResponse} - Configuration settings for HTTP response
 * logging.</li>
 * </ul>
 *
 * <p>
 * Example configuration in {@code application.yml}:
 * </p>
 *
 * <pre>
 * application:
 *   http-logging:
 *     request:
 *       enabled: true
 *       logLevel: DEBUG
 *     response:
 *       enabled: true
 *       logLevel: INFO
 * </pre>
 *
 * @see HttpLogRequest
 * @see HttpLogResponse
 * @since 1.0
 * @author hoangtien2k3
 */
@Component
@ConfigurationProperties(prefix = "application.http-logging", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpLogProperties {
    private HttpLogRequest request = new HttpLogRequest();
    private HttpLogResponse response = new HttpLogResponse();
}
