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

import brave.Tracer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * <p>
 * TraceIdWebClientFilter class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
public record TraceIdWebClientFilter(Tracer tracer) implements ExchangeFilterFunction {
    @NotNull
    @Override
    public Mono<ClientResponse> filter(@NotNull ClientRequest request, @NotNull ExchangeFunction next) {
        return Mono.defer(() -> {
            var span = tracer.currentSpan();
            if (span != null) {
                ClientRequest modifiedRequest = ClientRequest.from(request)
                        .header("X-B3-TRACE-ID", span.context().traceIdString())
                        .build();
                return next.exchange(modifiedRequest);
            } else {
                return next.exchange(request);
            }
        });
    }
}
