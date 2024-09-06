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

import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * The `RetryProperties` class is used to configure retry behavior for HTTP
 * requests within an application. It leverages Lombok annotations to
 * automatically generate getters, setters, and constructors.
 *
 * <h2>Attributes:</h2>
 * <ul>
 * <li><strong>isEnable</strong>: A boolean flag indicating whether retry
 * functionality is enabled. Defaults to `true` if not explicitly set.</li>
 * <li><strong>count</strong>: An integer specifying the number of retry
 * attempts. Defaults to `2` if not explicitly set.</li>
 * <li><strong>methods</strong>: A list of HTTP methods for which retry should
 * be applied. By default, it includes `HttpMethod.GET`, `HttpMethod.PUT`, and
 * `HttpMethod.DELETE`.</li>
 * <li><strong>exceptions</strong>: A list of exception classes that should
 * trigger a retry. Defaults to `ConnectTimeoutException` and
 * `ReadTimeoutException`.</li>
 * </ul>
 *
 * <h2>Lombok Annotations:</h2>
 * <ul>
 * <li><strong>@Data</strong>: Automatically generates getters, setters,
 * `equals()`, `hashCode()`, and `toString()` methods for the class.</li>
 * <li><strong>@AllArgsConstructor</strong>: Creates a constructor with all
 * fields as parameters, allowing for easy instantiation with all properties
 * set.</li>
 * <li><strong>@NoArgsConstructor</strong>: Provides a no-argument constructor
 * for creating instances with default values.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>
 * {
 * 	&#64;code
 * 	&#64;Configuration
 * 	&#64;ConfigurationProperties(prefix = "retry")
 * 	public class RetryConfig {
 *
 * 		private final RetryProperties retryProperties;
 *
 * 		&#64;Autowired
 * 		public RetryConfig(RetryProperties retryProperties) {
 * 			this.retryProperties = retryProperties;
 * 		}
 *
 * 		@Bean
 * 		public WebClient.Builder webClientBuilder() {
 * 			return WebClient.builder().filter((request, next) -> {
 * 				if (retryProperties.isEnable()) {
 * 					return next.exchange(request)
 * 							.retryWhen(Retry.backoff(retryProperties.getCount(), Duration.ofSeconds(1))
 * 									.filter(throwable -> retryProperties.getExceptions().stream()
 * 											.anyMatch(exceptionClass -> exceptionClass.isInstance(throwable))));
 * 				}
 * 				return next.exchange(request);
 * 			});
 * 		}
 * 	}
 * }
 * </pre>
 *
 * <h2>Configuration Example:</h2>
 *
 * <pre>{@code
 * # application.yml
 * retry:
 *   isEnable: true
 *   count: 3
 *   methods:
 *     - GET
 *     - POST
 *   exceptions:
 *     - java.net.ConnectException
 *     - java.net.SocketTimeoutException
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The `RetryProperties` class provides configuration options for retrying HTTP
 * requests when certain conditions are met. The `isEnable` flag determines
 * whether the retry logic is active. The `count` attribute specifies how many
 * times the request should be retried if a retryable exception occurs.
 * </p>
 *
 * <p>
 * The `methods` list specifies which HTTP methods the retry logic should apply
 * to. By default, it includes `GET`, `PUT`, and `DELETE` requests, but this can
 * be customized based on the application's requirements.
 * </p>
 *
 * <p>
 * The `exceptions` list includes exception classes that trigger a retry.
 * Commonly used exceptions like `ConnectTimeoutException` and
 * `ReadTimeoutException` are included by default, but this can be customized to
 * handle other specific exceptions as needed.
 * </p>
 *
 * <p>
 * Using this configuration allows applications to handle transient errors and
 * improve resilience by automatically retrying failed requests, which can be
 * particularly useful in distributed systems or when dealing with unreliable
 * network conditions.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetryProperties {
    private boolean isEnable = true;
    private int count = 2;
    private List<HttpMethod> methods = List.of(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE);
    private List<? extends Class<? extends Exception>> exceptions =
            List.of(ConnectTimeoutException.class, ReadTimeoutException.class);
}