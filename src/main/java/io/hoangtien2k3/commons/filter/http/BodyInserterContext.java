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

import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BodyInserterContext implements BodyInserter.Context {
  private final ExchangeStrategies exchangeStrategies;

  public BodyInserterContext() {
    this.exchangeStrategies = ExchangeStrategies.withDefaults();
  }

  public BodyInserterContext(ExchangeStrategies exchangeStrategies) {
    this.exchangeStrategies = exchangeStrategies;
  }

  @Override
  public List<HttpMessageWriter<?>> messageWriters() {
    return exchangeStrategies.messageWriters();
  }

  @Override
  public Optional<ServerHttpRequest> serverRequest() {
    return Optional.empty();
  }

  @Override
  public Map<String, Object> hints() {
    return Collections.emptyMap();
  }
}
