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
package io.hoangtien2k3.commons.filter.webclient;

import io.hoangtien2k3.commons.filter.properties.RetryProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/**
 * The `WebClientRetryHandler` class implements `ExchangeFilterFunction` to
 * provide retry functionality for HTTP requests made using Spring's
 * `WebClient`. It uses a configurable retry strategy to handle transient
 * failures and retry requests based on specific conditions.
 *
 * <h2>Attributes:</h2>
 * <ul>
 * <li><strong>properties</strong>: An instance of `RetryProperties` used to
 * configure retry behavior, such as the number of retry attempts, HTTP methods
 * to apply retries, and exception types that should trigger retries.</li>
 * </ul>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>filter</strong>: This method is invoked for every HTTP request
 * handled by the `WebClient`. It:
 * <ul>
 * <li>Creates a `Retry` instance with the configured properties, including the
 * maximum number of retry attempts and the conditions under which retries
 * should be performed.</li>
 * <li>Logs a warning message each time a retry is attempted, including the
 * number of retries and the cause of the failure.</li>
 * <li>Throws an exception if all retry attempts are exhausted.</li>
 * </ul>
 * <ul>
 * <li><strong>Parameters:</strong>
 * <ul>
 * <li><strong>request</strong>: The `ClientRequest` object representing the
 * HTTP request.</li>
 * <li><strong>next</strong>: The `ExchangeFunction` that processes the HTTP
 * request and returns a `ClientResponse`.</li>
 * </ul>
 * </li>
 * <li><strong>Returns:</strong> A `Mono<ClientResponse>` representing the
 * response from the exchange function, which includes retry handling.</li>
 * </ul>
 * </li>
 * </ul>
 *
 * <h2>Logging:</h2>
 * <ul>
 * <li><strong>Retry Logging:</strong> Logs a warning message each time a retry
 * is performed, including the retry count and the cause of the failure. This
 * helps in monitoring and debugging retry attempts.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>
 * {
 * 	&#64;code
 * 	&#64;Configuration
 * 	public class WebClientConfig {
 *
 * 		@Bean
 * 		public WebClient.Builder webClientBuilder(RetryProperties retryProperties) {
 * 			return WebClient.builder().filter(new WebClientRetryHandler(retryProperties));
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
 *   enable: true
 *   count: 3
 *   methods:
 *     - GET
 *     - PUT
 *   exceptions:
 *     - java.net.ConnectException
 *     - java.net.SocketTimeoutException
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The `WebClientRetryHandler` class provides a way to handle transient errors
 * and retry HTTP requests using Spring's `WebClient`. It allows you to
 * configure retry behavior based on the number of retry attempts, the HTTP
 * methods that should be retried, and the types of exceptions that should
 * trigger retries. The retry logic is built using Reactor's retry
 * functionality, which allows for configurable retry policies and handling of
 * transient failures.
 * </p>
 *
 * <p>
 * When a request fails, the filter will attempt to retry the request based on
 * the configured properties. The retry attempts are logged for monitoring
 * purposes, and an exception is thrown if all retry attempts are exhausted.
 * This functionality is useful in scenarios where transient errors are expected
 * and retrying the request can lead to successful outcomes.
 * </p>
 *
 * <p>
 * To use this filter, configure it as part of your `WebClient` setup and
 * provide a `RetryProperties` bean to specify the retry behavior. This filter
 * is helpful for ensuring reliability in HTTP communications by automatically
 * retrying requests in the event of transient failures.
 * </p>
 */
@Slf4j
@RequiredArgsConstructor
public class WebClientRetryHandler implements ExchangeFilterFunction {

    private final RetryProperties properties;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        Retry retry = Retry.max(properties.getCount())
                .filter(e -> properties.getMethods().contains(request.method())
                        && properties.getExceptions().stream()
                                .anyMatch(clazz ->
                                        clazz.isInstance(e) || clazz.isInstance(NestedExceptionUtils.getRootCause(e))))
                .doBeforeRetry(retrySignal -> {
                    log.warn("Retrying: {}; Cause: {}.", retrySignal.totalRetries(), retrySignal.failure());
                })
                .onRetryExhaustedThrow(((retrySpec, retrySignal) -> retrySignal.failure()));

        return next.exchange(request).retryWhen(retry);
    }
}
