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
package io.hoangtien2k3.commons.client;

import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface BaseRestClient<T> {

  Mono<Optional<T>> get(WebClient webClient, String url, MultiValueMap<String, String> headerList,
      MultiValueMap<String, String> payload, Class<?> resultClass);

  Mono<String> getRaw(WebClient webClient, String url, MultiValueMap<String, String> headerMap,
      MultiValueMap<String, String> payload);

  Mono<Optional<T>> post(WebClient webClient, String url, MultiValueMap<String, String> headerList, Object payload,
      Class<?> resultClass);

  Mono<Optional<T>> postFormData(WebClient webClient, String url, MultiValueMap<String, String> headerList,
      MultiValueMap<String, String> formData, Class<?> resultClass);

  Mono<Optional<T>> delete(WebClient webClient, String url, MultiValueMap<String, String> headerList,
      MultiValueMap<String, String> payload, Class<?> resultClass);

  Optional<T> processReturn(String response, Class<?> resultClass);

  Mono<String> callApiCertificateFileService(WebClient webClient, String url,
      MultiValueMap<String, String> headerList, Object payload, Class<?> resultClass);

  Mono<Optional<T>> callPostBodyJson(WebClient webClient, String url, MultiValueMap<String, String> headerList,
      Object payload, Class<?> resultClass);

  WebClient proxyClient(String proxyHost, Integer proxyPort, Boolean proxyEnable);

  WebClient proxyHttpClient(String proxyHost, Integer proxyPort);

  Mono<Optional<T>> callPostBodyJsonForLocalDateTime(WebClient webClient, String url,
      MultiValueMap<String, String> headerList, Object payload, Class<?> resultClass);

  Mono<String> postRawBodyJson(WebClient webClient, String url, MultiValueMap<String, String> headerList,
      Object payload);

  Mono<String> getRawWithFixedUri(WebClient webClient, String uri, MultiValueMap<String, String> headerMap);
}
