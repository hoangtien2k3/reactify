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
package io.hoangtien2k3.commons.filter.http;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/** Implementation of {@link ClientHttpRequest} that saves body as a field. */
public class CachedBodyOutputMessage implements ReactiveHttpOutputMessage {
  private final DataBufferFactory bufferFactory;
  private final HttpHeaders httpHeaders;

  private boolean cached = false;

  @Getter
  private Flux<DataBuffer> body = Flux
      .error(new IllegalStateException("The body is not set. " + "Did handling complete with success?"));

  public CachedBodyOutputMessage(ServerWebExchange exchange, HttpHeaders httpHeaders) {
    this.bufferFactory = exchange.getResponse().bufferFactory();
    this.httpHeaders = httpHeaders;
  }

  @Override
  public void beforeCommit(Supplier<? extends Mono<Void>> action) {
  }

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
  public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
    this.body = Flux.from(body);
    this.cached = true;
    return Mono.empty();
  }

  @NotNull
  @Override
  public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
    return writeWith(Flux.from(body).flatMap(p -> p));
  }

  @NotNull
  @Override
  public Mono<Void> setComplete() {
    return writeWith(Flux.empty());
  }
}
