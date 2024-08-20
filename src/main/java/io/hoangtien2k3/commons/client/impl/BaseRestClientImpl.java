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
package io.hoangtien2k3.commons.client.impl;

import static java.nio.charset.StandardCharsets.UTF_8;

import io.hoangtien2k3.commons.client.BaseRestClient;
import io.hoangtien2k3.commons.constants.CommonErrorCode;
import io.hoangtien2k3.commons.constants.Constants;
import io.hoangtien2k3.commons.exception.BusinessException;
import io.hoangtien2k3.commons.utils.DataUtil;
import io.hoangtien2k3.commons.utils.ObjectMapperUtil;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.ProxyProvider;

/**
 * Implementation of the {@link BaseRestClient} interface that provides methods
 * for making REST API calls using {@link WebClient}.
 *
 * <p>
 * This class provides methods to perform HTTP GET and POST requests, including
 * handling errors and processing responses. It also includes methods to handle
 * raw responses and form data submissions.
 * </p>
 *
 * @param <T>
 *            the type of the response body
 */
@Slf4j
@Service
public class BaseRestClientImpl<T> implements BaseRestClient<T> {

    /**
     * Performs an HTTP GET request and returns a {@link Mono} of {@link Optional}
     * containing the response body converted to the specified type.
     *
     * <p>
     * If the response is empty or an error occurs, the method returns an empty
     * {@link Optional} or an {@link Optional} containing a parsed result from the
     * error response.
     * </p>
     *
     * @param webClient
     *            the {@link WebClient} used to perform the request
     * @param url
     *            the URL for the GET request
     * @param headerMap
     *            the headers to be included in the request
     * @param payload
     *            the query parameters to be included in the request
     * @param resultClass
     *            the class of the response body to be converted
     * @return a {@link Mono} of {@link Optional} containing the response body or an
     *         empty {@link Optional}
     */
    @Override
    public Mono<Optional<T>> get(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerMap,
            MultiValueMap<String, String> payload,
            Class<?> resultClass) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParams(getSafePayload(payload))
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerMap)))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    if (DataUtil.isNullOrEmpty(response)) {
                        return this.getDefaultValue();
                    }
                    if (DataUtil.safeEqual(resultClass.getSimpleName(), "String")) {
                        return Optional.of((T) response);
                    }
                    T result = DataUtil.parseStringToObject(response, resultClass);
                    return Optional.ofNullable(result);
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call get rest api: ", e);
                    String responseError = e.getResponseBodyAsString(UTF_8);
                    T result = DataUtil.parseStringToObject(responseError, resultClass);
                    return Mono.just(Optional.ofNullable(result));
                });
    }

    /**
     * Performs an HTTP GET request and returns the raw response body as a
     * {@link Mono} of {@link String}.
     *
     * <p>
     * If an error occurs during the request, an empty string is returned.
     * </p>
     *
     * @param webClient
     *            the {@link WebClient} used to perform the request
     * @param url
     *            the URL for the GET request
     * @param headerMap
     *            the headers to be included in the request
     * @param payload
     *            the query parameters to be included in the request
     * @return a {@link Mono} containing the raw response body as a {@link String}
     */
    @Override
    public Mono<String> getRaw(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerMap,
            MultiValueMap<String, String> payload) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParams(getSafePayload(payload))
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerMap)))
                .retrieve()
                .bodyToMono(String.class)
                .map(DataUtil::safeToString)
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call rest api ", e);
                    return Mono.just("");
                });
    }

    /**
     * Performs an HTTP POST request and returns a {@link Mono} of {@link Optional}
     * containing the response body converted to the specified type.
     *
     * <p>
     * If the response is empty or an error occurs, the method returns an empty
     * {@link Optional} or an {@link Optional} containing a parsed result from the
     * error response.
     * </p>
     *
     * @param webClient
     *            the {@link WebClient} used to perform the request
     * @param url
     *            the URL for the POST request
     * @param headerList
     *            the headers to be included in the request
     * @param payload
     *            the body of the POST request
     * @param resultClass
     *            the class of the response body to be converted
     * @return a {@link Mono} of {@link Optional} containing the response body or an
     *         empty {@link Optional}
     */
    @Override
    public Mono<Optional<T>> post(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass) {
        if (DataUtil.isNullOrEmpty(payload)) {
            payload = new LinkedMultiValueMap<>();
        }
        log.info("Payload when calling post: {}", payload);
        return webClient
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerList)))
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody ->
                                Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody))))
                .bodyToMono(String.class)
                .map(response -> processReturn(response, resultClass))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call post rest api error: ", e);
                    String responseError = e.getResponseBodyAsString(StandardCharsets.UTF_8);
                    T result = DataUtil.parseStringToObject(responseError, resultClass);
                    return Mono.just(Optional.ofNullable(result));
                });
    }

    /**
     * Performs an HTTP POST request with form data and returns a {@link Mono} of
     * {@link Optional} containing the response body converted to the specified
     * type.
     *
     * <p>
     * If the response is empty or an error occurs, the method returns an empty
     * {@link Optional} or an {@link Optional} containing a parsed result from the
     * error response.
     * </p>
     *
     * @param webClient
     *            the {@link WebClient} used to perform the request
     * @param url
     *            the URL for the POST request
     * @param headerList
     *            the headers to be included in the request
     * @param formData
     *            the form data to be included in the body of the POST request
     * @param resultClass
     *            the class of the response body to be converted
     * @return a {@link Mono} of {@link Optional} containing the response body or an
     *         empty {@link Optional}
     */
    @Override
    public Mono<Optional<T>> postFormData(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> formData,
            Class<?> resultClass) {
        if (formData == null) {
            formData = new LinkedMultiValueMap<>();
        }
        return webClient
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerList)))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> processReturn(response, resultClass))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call post rest api: ", e);
                    String responseError = e.getResponseBodyAsString(UTF_8);
                    T result = DataUtil.parseStringToObject(responseError, resultClass);
                    return Mono.just(Optional.ofNullable(result));
                });
    }

    /**
     * Processes the response from a REST API call and converts it to an
     * {@link Optional} containing the response body converted to the specified
     * type.
     *
     * <p>
     * If the response is empty, the method returns a default value.
     * </p>
     *
     * @param response
     *            the raw response body as a {@link String}
     * @param resultClass
     *            the class of the response body to be converted
     * @return an {@link Optional} containing the response body or an empty
     *         {@link Optional}
     */
    @Override
    public Optional<T> processReturn(String response, Class<?> resultClass) {
        if (DataUtil.isNullOrEmpty(response)) {
            return this.getDefaultValue();
        }
        T result = DataUtil.parseStringToObject(response, resultClass);
        return Optional.ofNullable(result);
    }

    /**
     * Performs an HTTP DELETE request and returns a {@link Mono} of
     * {@link Optional} containing the response body converted to the specified
     * type.
     *
     * <p>
     * If the response is empty or an error occurs, the method returns an empty
     * {@link Optional} or an {@link Optional} containing a parsed result from the
     * error response.
     * </p>
     *
     * @param webClient
     *            the {@link WebClient} used to perform the request
     * @param url
     *            the URL for the DELETE request
     * @param headerList
     *            the headers to be included in the request
     * @param payload
     *            the query parameters to be included in the DELETE request
     * @param resultClass
     *            the class of the response body to be converted
     * @return a {@link Mono} of {@link Optional} containing the response body or an
     *         empty {@link Optional}
     */
    @Override
    public Mono<Optional<T>> delete(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> payload,
            Class<?> resultClass) {
        return webClient
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParams(getSafePayload(payload))
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerList)))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> processReturn(response, resultClass))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call delete rest api: ", e);
                    String responseError = e.getResponseBodyAsString(UTF_8);
                    T result = DataUtil.parseStringToObject(responseError, resultClass);
                    return Mono.just(Optional.ofNullable(result));
                });
    }

    private Optional<T> getDefaultValue() {
        return Optional.empty();
    }

    private MultiValueMap<String, String> getSafeRestHeader(MultiValueMap<String, String> headerMap) {
        if (!DataUtil.isNullOrEmpty(headerMap)) {
            return headerMap;
        }
        headerMap = new LinkedMultiValueMap<>();
        headerMap.set(Constants.HeaderType.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headerMap;
    }

    private MultiValueMap<String, String> getSafePayload(MultiValueMap<String, String> payload) {
        return !DataUtil.isNullOrEmpty(payload) ? payload : new LinkedMultiValueMap<>();
    }

    /**
     * Calls an API with a certificate file service using an HTTP POST request and
     * returns the response body as a {@link Mono}.
     *
     * <p>
     * If the response is an error, an exception is thrown with a detailed error
     * message.
     * </p>
     *
     * @param webClient
     *            the {@link WebClient} used to perform the request
     * @param url
     *            the URL for the POST request
     * @param headerList
     *            the headers to be included in the request
     * @param payload
     *            the payload to be included in the body of the POST request
     * @param resultClass
     *            the class of the response body to be converted
     * @return a {@link Mono} containing the response body as a string
     */
    @Override
    public Mono<String> callApiCertificateFileService(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass) {
        if (DataUtil.isNullOrEmpty(payload)) {
            payload = new LinkedMultiValueMap<>();
        }
        return webClient
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerList)))
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody ->
                                Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody))))
                .bodyToMono(String.class)
                .map(response -> response)
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call post rest api: ", e);
                    String responseError = e.getResponseBodyAsString(UTF_8);
                    return Mono.just(responseError);
                });
    }

    /**
     * Performs an HTTP POST request with JSON body and returns a {@link Mono} of
     * {@link Optional} containing the response body converted to the specified
     * type.
     *
     * <p>
     * If the response is empty or an error occurs, the method returns an empty
     * {@link Optional} or an {@link Optional} containing a parsed result from the
     * error response.
     * </p>
     *
     * @param webClient
     *            the {@link WebClient} used to perform the request
     * @param url
     *            the URL for the POST request
     * @param headerList
     *            the headers to be included in the request
     * @param payload
     *            the JSON payload to be included in the body of the POST request
     * @param resultClass
     *            the class of the response body to be converted
     * @return a {@link Mono} of {@link Optional} containing the response body or an
     *         empty {@link Optional}
     */
    @Override
    public Mono<Optional<T>> callPostBodyJson(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass) {
        if (DataUtil.isNullOrEmpty(payload)) {
            payload = new LinkedMultiValueMap<>();
        }
        log.info("payload when call post {}", payload);

        return webClient
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerList)))
                .bodyValue(Objects.requireNonNull(ObjectMapperUtil.convertObjectToJson(payload)))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody ->
                                Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody))))
                .bodyToMono(String.class)
                .map(response -> processReturn(response, resultClass))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call post rest api: ", e);
                    String responseError = e.getResponseBodyAsString(UTF_8);
                    T result = DataUtil.parseStringToObject(responseError, resultClass);
                    return Mono.just(Optional.ofNullable(result));
                });
    }

    /**
     * Creates a {@link WebClient} instance with proxy support for HTTPS protocol.
     *
     * <p>
     * If proxy is enabled, it configures the {@link WebClient} to use the specified
     * proxy settings.
     * </p>
     *
     * @param proxyHost
     *            the proxy host
     * @param proxyPort
     *            the proxy port
     * @param proxyEnable
     *            whether to enable proxy or not
     * @return a {@link WebClient} instance with the specified proxy configuration
     */
    @Override
    public WebClient proxyClient(String proxyHost, Integer proxyPort, Boolean proxyEnable) {
        ConnectionProvider connectionProvider = ConnectionProvider.builder(Constants.POOL.REST_CLIENT_POLL)
                .maxConnections(2000)
                .pendingAcquireMaxCount(2000)
                .build();
        HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .responseTimeout(Duration.ofMillis(10000))
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(EpollChannelOption.TCP_KEEPIDLE, 300)
                .option(EpollChannelOption.TCP_KEEPINTVL, 60)
                .option(EpollChannelOption.TCP_KEEPCNT, 8);
        if (proxyEnable) {
            SslContext sslContext;
            try {
                sslContext = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build();
            } catch (Exception ex) {
                return null;
            }
            httpClient = httpClient
                    .proxy(proxy ->
                            proxy.type(ProxyProvider.Proxy.HTTP).host(proxyHost).port(proxyPort))
                    .secure(t -> t.sslContext(sslContext));
        }
        var clientConnector = new ReactorClientHttpConnector(httpClient);
        return WebClient.builder()
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer
                            .defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024); // config max memory in byte
                })
                .clientConnector(clientConnector)
                .build();
    }

    /**
     * Creates a {@link WebClient} instance with proxy support for HTTP protocol.
     *
     * <p>
     * This method configures the {@link WebClient} to use the specified proxy
     * settings.
     * </p>
     *
     * @param proxyHost
     *            the proxy host
     * @param proxyPort
     *            the proxy port
     * @return a {@link WebClient} instance with the specified proxy configuration
     */
    @Override
    public WebClient proxyHttpClient(String proxyHost, Integer proxyPort) {
        HttpClient httpClient = HttpClient.create()
                .proxy(proxy ->
                        proxy.type(ProxyProvider.Proxy.HTTP).host(proxyHost).port(proxyPort));
        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
        return WebClient.builder()
                .clientConnector(connector)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    /**
     * Performs an HTTP POST request with JSON body including LocalDateTime and
     * returns a {@link Mono} of {@link Optional} containing the response body
     * converted to the specified type.
     *
     * <p>
     * If the response is empty or an error occurs, the method returns an empty
     * {@link Optional} or an {@link Optional} containing a parsed result from the
     * error response.
     * </p>
     *
     * @param webClient
     *            the {@link WebClient} used to perform the request
     * @param url
     *            the URL for the POST request
     * @param headerList
     *            the headers to be included in the request
     * @param payload
     *            the JSON payload including LocalDateTime to be included in the
     *            body of the POST request
     * @param resultClass
     *            the class of the response body to be converted
     * @return a {@link Mono} of {@link Optional} containing the response body or an
     *         empty {@link Optional}
     */
    @Override
    public Mono<Optional<T>> callPostBodyJsonForLocalDateTime(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass) {
        if (DataUtil.isNullOrEmpty(payload)) {
            payload = new LinkedMultiValueMap<>();
        }
        return webClient
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerList)))
                .bodyValue(Objects.requireNonNull(ObjectMapperUtil.convertObjectToJsonForLocalDateTime(payload)))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody ->
                                Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody))))
                .bodyToMono(String.class)
                .map(response -> processReturn(response, resultClass))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call post rest api: ", e);
                    String responseError = e.getResponseBodyAsString(UTF_8);
                    T result = DataUtil.parseStringToObject(responseError, resultClass);
                    return Mono.just(Optional.ofNullable(result));
                });
    }

    /**
     * Performs an HTTP POST request with a raw JSON body and returns a {@link Mono}
     * of {@link String} containing the response body as a plain {@link String}.
     *
     * <p>
     * If the response status code indicates an error, the method will throw a
     * {@link BusinessException} with the error response body. If an error occurs
     * during the request, an empty {@link String} will be returned.
     * </p>
     *
     * @param webClient
     *            the {@link WebClient} used to perform the request
     * @param url
     *            the URL for the POST request
     * @param headerList
     *            the headers to be included in the request
     * @param payload
     *            the JSON payload to be included in the body of the POST request
     * @return a {@link Mono} of {@link String} containing the response body or an
     *         empty {@link String} in case of error
     */
    @Override
    public Mono<String> postRawBodyJson(
            WebClient webClient, String url, MultiValueMap<String, String> headerList, Object payload) {
        return webClient
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerList)))
                .bodyValue(Objects.requireNonNull(ObjectMapperUtil.convertObjectToJson(payload)))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody ->
                                Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody))))
                .bodyToMono(String.class)
                .map(DataUtil::safeToString)
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call rest api ", e);
                    return Mono.just("");
                });
    }

    /**
     * Performs an HTTP GET request with a fixed URI and returns a {@link Mono} of
     * {@link String} containing the response body as a plain {@link String}.
     *
     * <p>
     * If an error occurs during the request, an empty {@link String} will be
     * returned.
     * </p>
     *
     * @param webClient
     *            the {@link WebClient} used to perform the request
     * @param uri
     *            the URI for the GET request
     * @param headerMap
     *            the headers to be included in the request
     * @return a {@link Mono} of {@link String} containing the response body or an
     *         empty {@link String} in case of error
     */
    @Override
    public Mono<String> getRawWithFixedUri(WebClient webClient, String uri, MultiValueMap<String, String> headerMap) {
        return webClient
                .get()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(getSafeRestHeader(headerMap)))
                .retrieve()
                .bodyToMono(String.class)
                .map(DataUtil::safeToString)
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("call rest api ", e);
                    return Mono.just("");
                });
    }
}
