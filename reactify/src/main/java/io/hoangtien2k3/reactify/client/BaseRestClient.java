/*
 * Copyright 2024 the original author Hoàng Anh Tiến
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
package io.hoangtien2k3.reactify.client;

import java.util.Optional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public interface BaseRestClient<T> {

    Mono<Optional<T>> get(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> payload,
            Class<?> resultClass);

    Mono<String> getRaw(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerMap,
            MultiValueMap<String, String> payload);

    Mono<Optional<T>> post(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass);

    Mono<Optional<T>> postFormData(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> formData,
            Class<?> resultClass);

    Mono<Optional<T>> delete(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> payload,
            Class<?> resultClass);

    Optional<T> processReturn(String response, Class<?> resultClass);

    Mono<String> callApiCertificateFileService(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass);

    Mono<Optional<T>> callPostBodyJson(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass);

    WebClient proxyClient(String proxyHost, Integer proxyPort, Boolean proxyEnable);

    WebClient proxyHttpClient(String proxyHost, Integer proxyPort);

    Mono<Optional<T>> callPostBodyJsonForLocalDateTime(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass);

    Mono<String> postRawBodyJson(
            WebClient webClient, String url, MultiValueMap<String, String> headerList, Object payload);

    Mono<String> getRawWithFixedUri(WebClient webClient, String uri, MultiValueMap<String, String> headerMap);
}
