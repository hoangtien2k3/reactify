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

import io.micrometer.core.instrument.MeterRegistry;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * The `WebClientMonitoringFilter` class implements `ExchangeFilterFunction` to
 * monitor and record metrics for HTTP requests made using Spring's `WebClient`.
 * It utilizes Micrometer's `MeterRegistry` to collect and record metrics such
 * as response time.
 *
 * <h2>Attributes:</h2>
 * <ul>
 * <li><strong>METRICS_WEBCLIENT_START_TIME</strong>: A constant string used as
 * a key to store the start time of the HTTP request in the context.</li>
 * <li><strong>meterRegistry</strong>: An instance of `MeterRegistry` used to
 * register and publish metrics. It is provided during the instantiation of the
 * filter.</li>
 * <li><strong>tagsProvider</strong>: (Commented out) A
 * `WebClientExchangeTagsProvider` for generating tags for the metrics. It is
 * currently not used but can be configured for more detailed metrics.</li>
 * </ul>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>filter</strong>: This method is invoked for every HTTP request
 * handled by the `WebClient`. It:
 * <ul>
 * <li>Records the start time of the request in the context.</li>
 * <li>Registers a `Timer` metric with `MeterRegistry` to measure the duration
 * of the request and response.</li>
 * <li>Logs the request duration in seconds, including percentiles for response
 * time.</li>
 * </ul>
 * <ul>
 * <li><strong>Parameters:</strong>
 * <ul>
 * <li><strong>clientRequest</strong>: The `ClientRequest` object representing
 * the HTTP request.</li>
 * <li><strong>exchangeFunction</strong>: The `ExchangeFunction` that processes
 * the HTTP request and returns a `ClientResponse`.</li>
 * </ul>
 * </li>
 * <li><strong>Returns:</strong> A `Mono<ClientResponse>` representing the
 * response from the exchange function.</li>
 * </ul>
 * </li>
 * </ul>
 *
 * <h2>Logging:</h2>
 * <ul>
 * <li><strong>Monitoring Logging:</strong> Logs the duration of the API call in
 * seconds. The duration is calculated based on the time elapsed between the
 * start and end of the request.</li>
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
 * 		public WebClient.Builder webClientBuilder(MeterRegistry meterRegistry) {
 * 			return WebClient.builder().filter(new WebClientMonitoringFilter(meterRegistry));
 * 		}
 * 	}
 * }
 * </pre>
 *
 * <h2>Configuration Example:</h2>
 *
 * <pre>{@code
 * # application.yml
 * metrics:
 *   webclient:
 *     enabled: true
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The `WebClientMonitoringFilter` class provides monitoring capabilities for
 * HTTP requests made through Spring's `WebClient`. It is designed to track and
 * record metrics such as response times to help in performance monitoring and
 * analysis. The filter uses Micrometer's `MeterRegistry` to register and
 * publish these metrics, allowing you to monitor the duration of HTTP requests.
 * </p>
 *
 * <p>
 * When a request is processed, the filter captures the start time and records
 * the duration of the request once the response is received. The metrics are
 * published with percentiles to provide insight into response time
 * distributions. You can also log the duration of requests for further
 * analysis.
 * </p>
 *
 * <p>
 * To use this filter, configure it as part of your `WebClient` setup and
 * provide a `MeterRegistry` bean to register the metrics. This filter is useful
 * in performance monitoring scenarios, where understanding request durations
 * and response times is crucial for optimizing application performance.
 * </p>
 */
@Slf4j
@Data
@RequiredArgsConstructor
public class WebClientMonitoringFilter implements ExchangeFilterFunction {
    private static final String METRICS_WEBCLIENT_START_TIME =
            WebClientMonitoringFilter.class.getName() + ".START_TIME";
    private final MeterRegistry meterRegistry;
    // private WebClientExchangeTagsProvider tagsProvider = new
    // DefaultWebClientExchangeTagsProvider();

    @Override
    public Mono<ClientResponse> filter(ClientRequest clientRequest, ExchangeFunction exchangeFunction) {
        return exchangeFunction
                .exchange(clientRequest)
                .doOnEach(signal -> {
                    if (!signal.isOnComplete()) {
                        Long startTime = signal.getContextView().get(METRICS_WEBCLIENT_START_TIME);
                        ClientResponse clientResponse = signal.get();
                        Throwable throwable = signal.getThrowable();
                        // Iterable<Tag> tags = tagsProvider.tags(clientRequest, clientResponse,
                        // throwable);
                        // Timer.builder("http.client.requests ")
                        // .tags(tags)
                        // .description("Timer of WebClient operation")
                        // .publishPercentiles(0.95, 0.99)
                        // .register(meterRegistry)
                        // .record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                        // log.info("Monitoring webClient API {}: {} s", tags, (double)
                        // (System.nanoTime() - startTime) / Math.pow(10, 9));
                    }
                })
                .contextWrite((contextView) -> contextView.put(METRICS_WEBCLIENT_START_TIME, System.nanoTime()));
    }
}
