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
package io.hoangtien2k3.commons.client;

import java.util.Optional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Interface for a base REST client that defines methods for making various
 * types of HTTP requests using the WebClient.
 *
 * @param <T>
 *            the type of the response object
 */
public interface BaseRestClient<T> {

    /**
     * Performs an HTTP GET request and returns the response as an Optional wrapped
     * in a Mono.
     *
     * @param webClient
     *            the WebClient instance used to make the request
     * @param url
     *            the URL to send the GET request to
     * @param headerList
     *            the headers to be included in the request
     * @param payload
     *            the query parameters to be included in the request
     * @param resultClass
     *            the class type of the response body
     * @return a Mono containing an Optional with the response body of type T
     */
    Mono<Optional<T>> get(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> payload,
            Class<?> resultClass);

    /**
     * Performs an HTTP GET request and returns the raw response body as a String
     * wrapped in a Mono.
     *
     * @param webClient
     *            the WebClient instance used to make the request
     * @param url
     *            the URL to send the GET request to
     * @param headerMap
     *            the headers to be included in the request
     * @param payload
     *            the query parameters to be included in the request
     * @return a Mono containing the raw response body as a String
     */
    Mono<String> getRaw(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerMap,
            MultiValueMap<String, String> payload);

    /**
     * Performs an HTTP POST request with a JSON payload and returns the response as
     * an Optional wrapped in a Mono.
     *
     * @param webClient
     *            the WebClient instance used to make the request
     * @param url
     *            the URL to send the POST request to
     * @param headerList
     *            the headers to be included in the request
     * @param payload
     *            the JSON payload to be sent in the request body
     * @param resultClass
     *            the class type of the response body
     * @return a Mono containing an Optional with the response body of type T
     */
    Mono<Optional<T>> post(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass);

    /**
     * Performs an HTTP POST request with form data and returns the response as an
     * Optional wrapped in a Mono.
     *
     * @param webClient
     *            the WebClient instance used to make the request
     * @param url
     *            the URL to send the POST request to
     * @param headerList
     *            the headers to be included in the request
     * @param formData
     *            the form data to be sent in the request body
     * @param resultClass
     *            the class type of the response body
     * @return a Mono containing an Optional with the response body of type T
     */
    Mono<Optional<T>> postFormData(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> formData,
            Class<?> resultClass);

    /**
     * Performs an HTTP DELETE request and returns the response as an Optional
     * wrapped in a Mono.
     *
     * @param webClient
     *            the WebClient instance used to make the request
     * @param url
     *            the URL to send the DELETE request to
     * @param headerList
     *            the headers to be included in the request
     * @param payload
     *            the query parameters to be included in the request
     * @param resultClass
     *            the class type of the response body
     * @return a Mono containing an Optional with the response body of type T
     */
    Mono<Optional<T>> delete(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> payload,
            Class<?> resultClass);

    /**
     * Processes the raw response string and maps it to an instance of the specified
     * class.
     *
     * @param response
     *            the raw response string
     * @param resultClass
     *            the class type to which the response should be mapped
     * @return an Optional containing the mapped response object of type T
     */
    Optional<T> processReturn(String response, Class<?> resultClass);

    /**
     * Calls an API to handle a certificate file service and returns the response as
     * a raw String wrapped in a Mono.
     *
     * @param webClient
     *            the WebClient instance used to make the request
     * @param url
     *            the URL to send the request to
     * @param headerList
     *            the headers to be included in the request
     * @param payload
     *            the payload to be sent in the request body
     * @param resultClass
     *            the class type of the response body
     * @return a Mono containing the raw response body as a String
     */
    Mono<String> callApiCertificateFileService(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass);

    /**
     * Performs an HTTP POST request with a JSON payload and returns the response as
     * an Optional wrapped in a Mono.
     *
     * @param webClient
     *            the WebClient instance used to make the request
     * @param url
     *            the URL to send the POST request to
     * @param headerList
     *            the headers to be included in the request
     * @param payload
     *            the JSON payload to be sent in the request body
     * @param resultClass
     *            the class type of the response body
     * @return a Mono containing an Optional with the response body of type T
     */
    Mono<Optional<T>> callPostBodyJson(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass);

    /**
     * Configures a WebClient to use a proxy server.
     *
     * @param proxyHost
     *            the host of the proxy server
     * @param proxyPort
     *            the port of the proxy server
     * @param proxyEnable
     *            whether the proxy is enabled or not
     * @return a WebClient configured to use the specified proxy
     */
    WebClient proxyClient(String proxyHost, Integer proxyPort, Boolean proxyEnable);

    /**
     * Configures a WebClient to use a proxy server for HTTP requests.
     *
     * @param proxyHost
     *            the host of the proxy server
     * @param proxyPort
     *            the port of the proxy server
     * @return a WebClient configured to use the specified proxy for HTTP requests
     */
    WebClient proxyHttpClient(String proxyHost, Integer proxyPort);

    /**
     * Performs an HTTP POST request with a JSON payload specifically for handling
     * LocalDateTime and returns the response as an Optional wrapped in a Mono.
     *
     * @param webClient
     *            the WebClient instance used to make the request
     * @param url
     *            the URL to send the POST request to
     * @param headerList
     *            the headers to be included in the request
     * @param payload
     *            the JSON payload to be sent in the request body
     * @param resultClass
     *            the class type of the response body
     * @return a Mono containing an Optional with the response body of type T
     */
    Mono<Optional<T>> callPostBodyJsonForLocalDateTime(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<?> resultClass);

    /**
     * Performs an HTTP POST request with a raw JSON payload and returns the raw
     * response body as a String wrapped in a Mono.
     *
     * @param webClient
     *            the WebClient instance used to make the request
     * @param url
     *            the URL to send the POST request to
     * @param headerList
     *            the headers to be included in the request
     * @param payload
     *            the raw JSON payload to be sent in the request body
     * @return a Mono containing the raw response body as a String
     */
    Mono<String> postRawBodyJson(
            WebClient webClient, String url, MultiValueMap<String, String> headerList, Object payload);

    /**
     * Performs an HTTP GET request with a fixed URI and returns the raw response
     * body as a String wrapped in a Mono.
     *
     * @param webClient
     *            the WebClient instance used to make the request
     * @param uri
     *            the fixed URI to send the GET request to
     * @param headerMap
     *            the headers to be included in the request
     * @return a Mono containing the raw response body as a String
     */
    Mono<String> getRawWithFixedUri(WebClient webClient, String uri, MultiValueMap<String, String> headerMap);
}
