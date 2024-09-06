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

import io.hoangtien2k3.filter.config.exception.CustomWebClientResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class ErrorHandlingFilter implements ExchangeFilterFunction {
    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request).flatMap(clientResponse -> {
            if (clientResponse.statusCode().is5xxServerError()
                    || clientResponse.statusCode().is4xxClientError()) {
                return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                    HttpStatus status =
                            HttpStatus.resolve(clientResponse.statusCode().value());
                    if (status == null) {
                        status = HttpStatus.INTERNAL_SERVER_ERROR;
                    }
                    return Mono.error(new CustomWebClientResponseException(errorBody, status));
                });
            } else {
                return Mono.just(clientResponse);
            }
        });
    }
}
