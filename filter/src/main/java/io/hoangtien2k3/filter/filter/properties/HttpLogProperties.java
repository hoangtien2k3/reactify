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
package io.hoangtien2k3.filter.filter.properties;

import io.hoangtien2k3.filter.model.logging.HttpLogRequest;
import io.hoangtien2k3.filter.model.logging.HttpLogResponse;
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

