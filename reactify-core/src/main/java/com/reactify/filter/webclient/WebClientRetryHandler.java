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

import com.reactify.filter.properties.RetryProperties;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/**
 * <p>
 * WebClientRetryHandler class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
public record WebClientRetryHandler(RetryProperties properties) implements ExchangeFilterFunction {

    @NotNull
    @Override
    public Mono<ClientResponse> filter(@NotNull ClientRequest request, ExchangeFunction next) {
        Retry retry = Retry.max(properties.count())
                .filter(e -> properties.methods().contains(request.method())
                        && properties.exceptions().stream()
                                .anyMatch(clazz ->
                                        clazz.isInstance(e) || clazz.isInstance(NestedExceptionUtils.getRootCause(e))))
                .doBeforeRetry(retrySignal -> {
                    log.warn("Retrying: {}; Cause: {}.", retrySignal.totalRetries(), retrySignal.failure());
                })
                .onRetryExhaustedThrow(((retrySpec, retrySignal) -> retrySignal.failure()));

        return next.exchange(request).retryWhen(retry);
    }
}
