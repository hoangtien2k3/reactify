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
package com.reactify.filter.http;

import java.util.function.Supplier;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of
 * {@link org.springframework.http.client.reactive.ClientHttpRequest} that saves
 * body as a field.
 *
 * @author hoangtien2k3
 */
public class CachedBodyOutputMessage implements ReactiveHttpOutputMessage {
    private final DataBufferFactory bufferFactory;
    private final HttpHeaders httpHeaders;

    private boolean cached = false;

    @Getter
    private Flux<DataBuffer> body =
            Flux.error(new IllegalStateException("The body is not set. " + "Did handling complete with success?"));

    /**
     * <p>
     * Constructor for CachedBodyOutputMessage.
     * </p>
     *
     * @param exchange
     *            a {@link ServerWebExchange} object
     * @param httpHeaders
     *            a {@link HttpHeaders} object
     */
    public CachedBodyOutputMessage(ServerWebExchange exchange, HttpHeaders httpHeaders) {
        this.bufferFactory = exchange.getResponse().bufferFactory();
        this.httpHeaders = httpHeaders;
    }

    @Override
    public void beforeCommit(@NotNull Supplier<? extends Mono<Void>> action) {}

    @Override
    public boolean isCommitted() {
        return false;
    }

    boolean isCached() {
        return this.cached;
    }

    @NotNull
    @Override
    public HttpHeaders getHeaders() {
        return this.httpHeaders;
    }

    @NotNull
    @Override
    public DataBufferFactory bufferFactory() {
        return this.bufferFactory;
    }

    @NotNull
    public Mono<Void> writeWith(@NotNull Publisher<? extends DataBuffer> body) {
        this.body = Flux.from(body);
        this.cached = true;
        return Mono.empty();
    }

    @NotNull
    @Override
    public Mono<Void> writeAndFlushWith(@NotNull Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return writeWith(Flux.from(body).flatMap(p -> p));
    }

    @NotNull
    @Override
    public Mono<Void> setComplete() {
        return writeWith(Flux.empty());
    }
}
