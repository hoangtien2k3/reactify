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
package io.hoangtien2k3.filter.model.logging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The `HttpLogRequest` class is a configuration class used to control whether
 * HTTP request logging is enabled or disabled.
 *
 * <h2>Attributes:</h2>
 * <ul>
 * <li><strong>enable</strong>: A boolean flag indicating whether HTTP request
 * logging is enabled. Default value is <code>true</code>.</li>
 * </ul>
 *
 * <h2>Constructor Details:</h2>
 * <ul>
 * <li><strong>HttpLogRequest()</strong>: Default constructor that initializes
 * <code>enable</code> to <code>true</code>.</li>
 * <li><strong>HttpLogRequest(boolean enable)</strong>: Constructor that allows
 * setting the <code>enable</code> flag explicitly.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>
 * {
 * 	&#64;code
 * 	&#64;Configuration
 * 	public class LoggingConfig {
 *
 * 		@Bean
 * 		public HttpLogRequest httpLogRequest() {
 * 			return new HttpLogRequest(true);
 * 		}
 * 	}
 * }
 * </pre>
 *
 * <h2>Configuration Example:</h2>
 *
 * <pre>{@code
 * # application.yml
 * http:
 *   log:
 *     enable: false
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The `HttpLogRequest` class provides a simple way to configure logging of HTTP
 * requests within an application. By setting the <code>enable</code> attribute
 * to <code>true</code>, you can enable detailed logging for HTTP requests. If
 * set to <code>false</code>, logging is disabled, which can be useful for
 * reducing log verbosity in production environments or when detailed request
 * logging is not required.
 * </p>
 *
 * <p>
 * This class can be used in conjunction with other logging configuration
 * classes or aspects to conditionally enable or disable HTTP request logging
 * based on the application's needs. It helps in centralizing the configuration
 * of logging behavior and makes it easier to manage and control logging
 * settings.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpLogRequest {
    private boolean enable = true;
}
