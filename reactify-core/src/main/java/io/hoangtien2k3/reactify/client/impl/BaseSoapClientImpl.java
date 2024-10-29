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
package io.hoangtien2k3.reactify.client.impl;

import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.DataWsUtil;
import io.hoangtien2k3.reactify.Translator;
import io.hoangtien2k3.reactify.client.BaseSoapClient;
import io.hoangtien2k3.reactify.constants.CommonErrorCode;
import io.hoangtien2k3.reactify.constants.Constants;
import io.hoangtien2k3.reactify.exception.BusinessException;
import java.io.StringReader;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import reactor.core.publisher.Mono;

/**
 * BaseSoapClientImpl is an implementation of the BaseSoapClient interface that
 * provides a set of methods to call SOAP APIs using WebClient. This class
 * encapsulates SOAP request/response processing logic and supports XML parsing,
 * error handling, and header setup for SOAP requests.
 *
 * <p>
 * This implementation supports different types of API calls: - Generic API call
 * to retrieve a result of a specified class type. - A raw API call that returns
 * the raw XML response. - A specific API call for fetching the KHDN profile.
 * </p>
 *
 * <p>
 * Dependencies: - Uses Spring WebFlux's WebClient for making asynchronous HTTP
 * calls. - Utilizes custom utility classes (e.g., DataUtil, DataWsUtil) for
 * data processing. - Implements error handling through custom
 * BusinessException.
 * </p>
 *
 * @param <T>
 *            The type of the result object expected from the API response.
 */
@Slf4j
@Service
public class BaseSoapClientImpl<T> implements BaseSoapClient<T> {

    /**
     * Executes a SOAP API call and returns an Optional result of the specified
     * type. This method performs the following steps: - Sets up headers and sends a
     * POST request. - Processes the response as a String and attempts to parse it.
     * - If the response is empty or parsing fails, returns an Optional.empty.
     *
     * @param webClient
     *            The WebClient instance for making HTTP requests.
     * @param headerList
     *            Headers to include in the SOAP request.
     * @param payload
     *            The request payload to send.
     * @param resultClass
     *            The expected result class type for deserialization.
     * @return A Mono containing an Optional result of type T if successful.
     */
    @Override
    public Mono<Optional<T>> call(
            WebClient webClient, Map<String, String> headerList, String payload, Class<?> resultClass) {
        MultiValueMap<String, String> header = getHeaderForCallSoap(headerList);
        return webClient
                .post()
                .headers(httpHeaders -> httpHeaders.addAll(header))
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError, BaseSoapClientImpl::handleErrorResponse)
                .bodyToMono(String.class)
                .map(response -> {
                    if (DataUtil.isNullOrEmpty(response)) {
                        return this.getDefaultValue();
                    }
                    String formattedSOAPResponse = DataWsUtil.formatXML(response);
                    String realData = DataWsUtil.getDataByTag(
                            formattedSOAPResponse
                                    .replaceAll(Constants.XmlConst.AND_LT_SEMICOLON, Constants.XmlConst.LT_CHARACTER)
                                    .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON, Constants.XmlConst.GT_CHARACTER),
                            Constants.XmlConst.TAG_OPEN_RETURN,
                            Constants.XmlConst.TAG_CLOSE_RETURN);
                    if (DataUtil.isNullOrEmpty(realData)) {
                        return this.getDefaultValue();
                    }
                    T result;
                    if (DataUtil.safeEqual(resultClass.getSimpleName(), "String")) {
                        result = (T) realData;
                    } else {
                        result = parseData(realData, resultClass);
                    }
                    if (result == null) {
                        return this.getDefaultValue();
                    }
                    return Optional.of(result);
                })
                .doOnError(err -> log.error("Call error when use method framework call", err));
    }

    /**
     * Alternative implementation of the `call` method with a similar API call
     * structure. Provides the same functionality as `call` but can be used
     * independently as needed.
     *
     * @param webClient
     *            The WebClient instance for making HTTP requests.
     * @param headerList
     *            Headers to include in the SOAP request.
     * @param payload
     *            The request payload to send.
     * @param resultClass
     *            The expected result class type for deserialization.
     * @return A Mono containing an Optional result of type T if successful.
     */
    @Override
    public Mono<Optional<T>> callV2(
            WebClient webClient, Map<String, String> headerList, String payload, Class<?> resultClass) {
        return webClient
                .post()
                .headers(httpHeaders -> httpHeaders.addAll(getHeaderForCallSoap(headerList)))
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError, BaseSoapClientImpl::handleErrorResponse)
                .bodyToMono(String.class)
                .map(response -> {
                    if (DataUtil.isNullOrEmpty(response)) {
                        return this.getDefaultValue();
                    }
                    String formattedSOAPResponse = DataWsUtil.formatXML(response);
                    String realData = DataWsUtil.getDataByTag(
                            formattedSOAPResponse
                                    .replaceAll(Constants.XmlConst.AND_LT_SEMICOLON, Constants.XmlConst.LT_CHARACTER)
                                    .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON, Constants.XmlConst.GT_CHARACTER),
                            Constants.XmlConst.TAG_OPEN_RETURN,
                            Constants.XmlConst.TAG_CLOSE_RETURN);
                    if (DataUtil.isNullOrEmpty(realData)) {
                        return this.getDefaultValue();
                    }
                    T result;
                    if (DataUtil.safeEqual(resultClass.getSimpleName(), "String")) {
                        result = (T) realData;
                    } else {
                        result = parseData(realData, resultClass);
                    }
                    if (result == null) {
                        return this.getDefaultValue();
                    }
                    return Optional.of(result);
                })
                .doOnError(err -> log.error("Call error when use method framework call", err));
    }

    /**
     * Calls the SOAP API and returns the raw XML response as a String. - Processes
     * headers, sends a POST request, and retrieves the raw response. - If the
     * response is empty, returns an error with a specified message.
     *
     * @param webClient
     *            The WebClient instance for making HTTP requests.
     * @param headerList
     *            Headers to include in the SOAP request.
     * @param payload
     *            The request payload to send.
     * @return A Mono containing the raw XML response as a String.
     */
    @Override
    public Mono<String> callRaw(WebClient webClient, Map<String, String> headerList, String payload) {
        MultiValueMap<String, String> header = getHeaderForCallSoap(headerList);
        return webClient
                .post()
                .headers(httpHeaders -> httpHeaders.addAll(header))
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError, BaseSoapClientImpl::handleErrorResponse)
                .onStatus(Objects::nonNull, clientResponse -> Mono.empty())
                .bodyToMono(String.class)
                .switchIfEmpty(Mono.error(new BusinessException("Call raw", "null")))
                .map(DataUtil::safeToString)
                .log();
    }

    /**
     * Default value provider for empty or failed SOAP responses.
     *
     * @return An empty Optional of type T.
     */
    private Optional<T> getDefaultValue() {
        return Optional.empty();
    }

    /**
     * Handles error responses from a Spring WebFlux ClientResponse.
     *
     * This method takes a ClientResponse as input, extracts the error body as a
     * String, and converts it into a Mono that emits a BusinessException containing
     * the error message. The method is designed to be used in error handling
     * scenarios where the response indicates an error (non-2xx status code).
     *
     * @param response
     *            The ClientResponse to handle, which may contain error details.
     * @return A Mono<Throwable> that emits a BusinessException with the error
     *         message from the response body. If the response body cannot be read,
     *         the error will be propagated.
     */
    public static Mono<Throwable> handleErrorResponse(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(errorBody ->
                        Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody)));
    }

    /**
     * Parses a String response to the specified class type using XML
     * deserialization.
     *
     * @param realData
     *            The XML data to parse.
     * @param clz
     *            The class type to parse into.
     * @return The deserialized object of type T.
     */
    @Override
    public T parseData(String realData, Class<?> clz) {
        return DataWsUtil.xmlToObj(
                Constants.XmlConst.TAG_OPEN_RETURN + realData + Constants.XmlConst.TAG_CLOSE_RETURN, clz);
    }

    /**
     * Constructs headers for a SOAP request, setting default values if no headers
     * are provided.
     *
     * @param headerList
     *            The headers to include in the SOAP request.
     * @return A MultiValueMap with SOAP request headers.
     */
    private MultiValueMap<String, String> getHeaderForCallSoap(Map<String, String> headerList) {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        if (DataUtil.isNullOrEmpty(headerList)) {
            header.set(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE);
            header.set(Constants.SoapHeaderConstant.X_B3_TRACEID, Constants.SoapHeaderConstant.X_B3_TRACEID_VALUE_SOAP);
        } else {
            header.setAll(headerList);
        }
        return header;
    }

    /**
     * Executes a SOAP API call specifically to fetch the KHDN profile data. -
     * Processes headers, sends a POST request, and parses the response. - Handles
     * any WebClientResponseException by attempting to parse the faultstring for
     * error messaging.
     *
     * @param webClient
     *            The WebClient instance for making HTTP requests.
     * @param headerList
     *            Headers to include in the SOAP request.
     * @param payload
     *            The request payload to send.
     * @param resultClass
     *            The expected result class type for deserialization.
     * @return A Mono containing an Optional result of type T if successful.
     */
    @Override
    public Mono<Optional<T>> callApiGetProfileKHDN(
            WebClient webClient, Map<String, String> headerList, String payload, Class<?> resultClass) {
        MultiValueMap<String, String> header = getHeaderForCallSoap(headerList);
        return webClient
                .post()
                .headers(httpHeaders -> httpHeaders.addAll(header))
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    if (DataUtil.isNullOrEmpty(response)) {
                        return this.getDefaultValue();
                    }
                    String formattedSOAPResponse = DataWsUtil.formatXML(response);
                    String realData = DataWsUtil.getDataByTag(
                            formattedSOAPResponse
                                    .replaceAll(Constants.XmlConst.AND_LT_SEMICOLON, Constants.XmlConst.LT_CHARACTER)
                                    .replaceAll(Constants.XmlConst.AND_GT_SEMICOLON, Constants.XmlConst.GT_CHARACTER),
                            Constants.XmlConst.TAG_OPEN_RETURN,
                            Constants.XmlConst.TAG_CLOSE_RETURN);
                    if (DataUtil.isNullOrEmpty(realData)) {
                        return this.getDefaultValue();
                    }
                    T result;
                    if (DataUtil.safeEqual(resultClass.getSimpleName(), "String")) {
                        result = (T) realData;
                    } else {
                        result = parseData(realData, resultClass);
                    }
                    if (result == null) {
                        return this.getDefaultValue();
                    }
                    return Optional.of(result);
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    String soapError = e.getResponseBodyAsString();
                    String faultString = null;
                    try {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        InputSource inputSource = new InputSource(new StringReader(soapError));
                        Document document = builder.parse(inputSource);
                        Element faultStringElement = (Element)
                                document.getElementsByTagName("faultstring").item(0);
                        if (faultStringElement != null) {
                            faultString = faultStringElement.getTextContent();
                        }
                    } catch (Exception ex) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.soap.order.error")));
                    }
                    return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, faultString));
                });
    }
}
