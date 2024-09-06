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
package io.hoangtien2k3.filter.filter.webclient;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Slf4j
public record WebClientMonitoringFilter(MeterRegistry meterRegistry) implements ExchangeFilterFunction {

    private static final String METRICS_WEBCLIENT_START_TIME = WebClientMonitoringFilter.class.getName() + ".START_TIME";

    @Override
    public @NonNull Mono<ClientResponse> filter(@NonNull ClientRequest clientRequest, ExchangeFunction exchangeFunction) {
        long startTime = System.nanoTime();

        return exchangeFunction.exchange(clientRequest)
                .doOnEach(signal -> {
                    if (!signal.isOnComplete()) {
                        ClientResponse clientResponse = signal.get();
                        Throwable throwable = signal.getThrowable();

                        // Tạo danh sách tag đơn giản
                        Tags tags = Tags.of("uri", clientRequest.url().toString(),
                                "method", clientRequest.method().name(),
                                "status", clientResponse.statusCode().toString());

                        // Ghi lại các chỉ số thời gian
                        Timer.builder("http.client.requests")
                                .tags(tags)
                                .description("Timer of WebClient operation")
                                .publishPercentiles(0.95, 0.99)
                                .register(meterRegistry)
                                .record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);

                        // Log thông tin thời gian
                        double durationInSeconds = (System.nanoTime() - startTime) / Math.pow(10, 9);
                        log.info("Monitoring webClient API: {} s", durationInSeconds);
                    }
                })
                .contextWrite(contextView -> contextView.put(METRICS_WEBCLIENT_START_TIME, startTime));
    }
}


