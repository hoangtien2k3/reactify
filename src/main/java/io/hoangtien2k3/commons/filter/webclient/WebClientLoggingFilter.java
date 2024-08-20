/*
 * Copyright 2024 author - Hoàng Anh Tiến
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
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
