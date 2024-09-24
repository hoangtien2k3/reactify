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
package io.hoangtien2k3.reactify.filter.webclient;

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
 * <p>
 * WebClientMonitoringFilter class.
 * </p>
 *
 * @author hoangtien2k3
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

    /** {@inheritDoc} */
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
