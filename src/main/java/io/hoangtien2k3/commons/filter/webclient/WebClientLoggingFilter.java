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

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * The {@code WebClientLoggingFilter} class is an implementation of
 * {@link ExchangeFilterFunction} used to log HTTP request and response details
 * when using Spring's {@link}. This filter logs information about API calls,
 * including headers and bodies, with support for obfuscating sensitive header
 * values. It utilizes Lombok annotations for logging and constructor injection.
 *
 * <h2>Attributes:</h2>
 * <ul>
 * <li><strong>OBFUSCATE_HEADER</strong>: A constant string used to replace
 * sensitive header values in logs. The default value is "xxxxx".</li>
 * <li><strong>obfuscateHeader</strong>: A list of header names that should be
 * obfuscated in the logs. This list is provided during the instantiation of the
 * filter.</li>
 * </ul>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>filter</strong>: The core method of the
 * {@code WebClientLoggingFilter} class. It intercepts the request and response,
 * logs request details, and then passes the request to the next
 * {@link ExchangeFunction} in the chain. After receiving the response, it logs
 * the response details before returning the {@link ClientResponse} to the
 * caller.
 * <ul>
 * <li><strong>Parameters:</strong>
 * <ul>
 * <li><strong>request</strong>: The original {@link ClientRequest} object
 * representing the HTTP request.</li>
 * <li><strong>next</strong>: The next {@link ExchangeFunction} in the chain
 * that will handle the request after this filter.</li>
 * </ul>
 * </li>
 * <li><strong>Returns:</strong> A {@link Mono} of {@link ClientResponse}
 * representing the response returned by the next filter in the chain.</li>
 * </ul>
 * </li>
 * </ul>
 *
 * <h2>Logging:</h2>
 * <ul>
 * <li><strong>Request Logging:</strong> Logs the HTTP method and URL of the
 * request. If the request has a body, it is logged as well. Headers are logged
 * in debug mode with sensitive values obfuscated if their names match any in
 * the {@code obfuscateHeader} list.</li>
 * <li><strong>Response Logging:</strong> Logs response headers in debug mode,
 * with sensitive values obfuscated if necessary.</li>
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
 * 		public WebClient.Builder webClientBuilder() {
 * 			List<String> obfuscateHeaders = List.of("Authorization", "Cookie");
 * 			return WebClient.builder().filter(new WebClientLoggingFilter(obfuscateHeaders));
 * 		}
 * 	}
 * }
 * </pre>
 *
 * <h2>Configuration Example:</h2>
 *
 * <pre>{@code
 * # application.yml
 * webclient:
 *   logging:
 *     obfuscate-headers:
 *       - Authorization
 *       - Cookie
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 * <p>
 * The {@code WebClientLoggingFilter} class provides detailed logging for HTTP
 * requests and responses. It is particularly useful for debugging and
 * monitoring API interactions. The filter logs important request and response
 * details, including headers and bodies (if present). Sensitive header values,
 * such as authorization tokens or cookies, can be obfuscated in the logs by
 * specifying their names in the {@code obfuscateHeader} list.
 * </p>
 *
 * <p>
 * Logging levels are controlled by the {@code log} object. Request and response
 * bodies are logged if their length is greater than zero. Header names and
 * values are logged at the debug level, with sensitive values replaced by a
 * placeholder string.
 * </p>
 *
 * <p>
 * This filter can be added to a {@link} configuration to automatically apply
 * logging to all requests made through that client.
 * </p>
 */
@Slf4j
@RequiredArgsConstructor
public class WebClientLoggingFilter implements ExchangeFilterFunction {

    private static final String OBFUSCATE_HEADER = "xxxxx";
    private final List<String> obfuscateHeader;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        log.info("Start Call API - Method: {} {}", request.method(), request.url());
        if (request.headers().getContentLength() > 0) {
            log.info("body ", request.body());
        }
        if (log.isDebugEnabled()) {
            request.headers()
                    .forEach((name, values) -> values.forEach(value -> log.debug(
                            "Request header: {}={}", name, obfuscateHeader.contains(name) ? OBFUSCATE_HEADER : value)));
        }

        return next.exchange(request).flatMap(clientResponse -> {
            if (log.isDebugEnabled()) {
                clientResponse
                        .headers()
                        .asHttpHeaders()
                        .forEach((name, values) -> values.forEach(value -> log.debug(
                                "Response header: {}={}",
                                name,
                                obfuscateHeader.contains(name) ? OBFUSCATE_HEADER : value)));
            }
            return Mono.just(clientResponse);
        });
    }
}
