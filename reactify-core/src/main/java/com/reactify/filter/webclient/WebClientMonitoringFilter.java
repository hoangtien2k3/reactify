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
package com.reactify.filter.webclient;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
public record WebClientMonitoringFilter(MeterRegistry meterRegistry) implements ExchangeFilterFunction {
    private static final String METRICS_WEBCLIENT_START_TIME =
            WebClientMonitoringFilter.class.getName() + ".START_TIME";

    @NotNull
    @Override
    public Mono<ClientResponse> filter(@NotNull ClientRequest clientRequest, ExchangeFunction exchangeFunction) {
        return exchangeFunction
                .exchange(clientRequest)
                .doOnEach(signal -> {
                    if (!signal.isOnComplete()) {
                        Long startTime = signal.getContextView().get(METRICS_WEBCLIENT_START_TIME);
                        ClientResponse clientResponse = signal.get();
                        Throwable throwable = signal.getThrowable();
                        if (throwable != null) {
                            log.error(
                                    "WebClient request to {} failed: {}", clientRequest.url(), throwable.getMessage());
                        } else {
                            assert clientResponse != null;
                            log.info(
                                    "WebClient request to {} completed with status code: {}",
                                    clientRequest.url(),
                                    clientResponse.statusCode());
                        }

                        // record the execution time
                        long duration = System.nanoTime() - startTime;
                        Timer.builder("http.client.requests")
                                .description("Timer for WebClient operations")
                                .publishPercentiles(0.95, 0.99)
                                .register(meterRegistry)
                                .record(duration, TimeUnit.NANOSECONDS);

                        log.info(
                                "Monitoring WebClient API {}: {} s",
                                clientRequest.url(),
                                (double) duration / Math.pow(10, 9));
                    }
                })
                .contextWrite((contextView) -> contextView.put(METRICS_WEBCLIENT_START_TIME, System.nanoTime()));
    }
}
