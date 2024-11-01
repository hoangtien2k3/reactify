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

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * <p>
 * WebClientLoggingFilter class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
@RequiredArgsConstructor
public class WebClientLoggingFilter implements ExchangeFilterFunction {

    private static final String OBFUSCATE_HEADER = "xxxxx";
    private final List<String> obfuscateHeader;

    /** {@inheritDoc} */
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
