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
package io.hoangtien2k3.commons.filter.http;

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
 * Implementation of {@link ReactiveHttpOutputMessage} that saves the body as a
 * field.
 *
 * <p>
 * This class implements {@link ReactiveHttpOutputMessage} and allows for
 * caching the body of the response. It is useful for scenarios where you need
 * to access the body of the response after it has been written, or to perform
 * operations on the body before it is sent to the client.
 * </p>
 *
 * <p>
 * The body is cached as a {@link Flux<DataBuffer>}, and methods are provided to
 * write the body, flush it, or complete the message. The state of whether the
 * body has been cached is tracked by the {@code cached} field.
 * </p>
 *
 * <p>
 * This implementation also provides methods to access headers and the buffer
 * factory used for creating data buffers.
 * </p>
 *
 * @see ReactiveHttpOutputMessage
 * @see DataBuffer
 * @see DataBufferFactory
 */
public class CachedBodyOutputMessage implements ReactiveHttpOutputMessage {

    private final DataBufferFactory bufferFactory;
    private final HttpHeaders httpHeaders;

    private boolean cached = false;

    @Getter
    private Flux<DataBuffer> body =
            Flux.error(new IllegalStateException("The body is not set. " + "Did handling complete with success?"));

    /**
     * Constructs a new {@code CachedBodyOutputMessage} with the specified exchange
     * and headers.
     *
     * @param exchange
     *            the {@link ServerWebExchange} used to obtain the
     *            {@link DataBufferFactory}
     * @param httpHeaders
     *            the {@link HttpHeaders} to be used for the response
     */
    public CachedBodyOutputMessage(ServerWebExchange exchange, HttpHeaders httpHeaders) {
        this.bufferFactory = exchange.getResponse().bufferFactory();
        this.httpHeaders = httpHeaders;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation does not perform any action before committing the
     * response.
     * </p>
     *
     * @param action
     *            a {@link Supplier} providing a {@link Mono} representing an action
     *            to be performed
     */
    @Override
    public void beforeCommit(Supplier<? extends Mono<Void>> action) {}

    /**
     * {@inheritDoc}
     * <p>
     * This implementation always returns {@code false}, indicating that the
     * response is not committed.
     * </p>
     *
     * @return {@code false} as the response is not committed
     */
    @Override
    public boolean isCommitted() {
        return false;
    }

    /**
     * Returns whether the body has been cached.
     *
     * @return {@code true} if the body has been cached, {@code false} otherwise
     */
    boolean isCached() {
        return this.cached;
    }

    /**
     * {@inheritDoc}
     *
     * @return the {@link HttpHeaders} for the response
     */
    @NotNull
    @Override
    public HttpHeaders getHeaders() {
        return this.httpHeaders;
    }

    /**
     * {@inheritDoc}
     *
     * @return the {@link DataBufferFactory} used for creating data buffers
     */
    @NotNull
    @Override
    public DataBufferFactory bufferFactory() {
        return this.bufferFactory;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method caches the body provided by the {@code body} publisher.
     * </p>
     *
     * @param body
     *            a {@link Publisher} providing the body to be cached
     * @return a {@link Mono} that completes when the body is successfully written
     */
    @NotNull
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        this.body = Flux.from(body);
        this.cached = true;
        return Mono.empty();
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method flattens the {@code body} publisher and writes it.
     * </p>
     *
     * @param body
     *            a {@link Publisher} of {@link Publisher} of {@link DataBuffer} to
     *            be written
     * @return a {@link Mono} that completes when the body is successfully written
     */
    @NotNull
    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return writeWith(Flux.from(body).flatMap(p -> p));
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method completes the message with an empty body.
     * </p>
     *
     * @return a {@link Mono} that completes when the message is successfully
     *         completed
     */
    @NotNull
    @Override
    public Mono<Void> setComplete() {
        return writeWith(Flux.empty());
    }
}
