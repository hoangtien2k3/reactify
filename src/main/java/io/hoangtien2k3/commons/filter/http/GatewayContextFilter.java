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

import io.hoangtien2k3.commons.constants.Constants;
import io.hoangtien2k3.commons.filter.properties.HttpLogProperties;
import io.hoangtien2k3.commons.model.GatewayContext;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A filter that captures and processes HTTP request and response data for
 * logging purposes in a Spring WebFlux gateway application.
 * <p>
 * This filter intercepts incoming requests and outgoing responses to record
 * their headers and content, if logging is enabled for them. The context is
 * stored in the {@link GatewayContext}, which can be accessed later in the
 * request handling pipeline.
 * </p>
 *
 * <p>
 * <strong>Note:</strong> This filter is only active in non-production profiles
 * (i.e., when the profile is not "prod").
 * </p>
 *
 * <p>
 * <strong>Logging and context management:</strong>
 * </p>
 * <ul>
 * <li>Logs request and response data based on configurations in
 * {@link HttpLogProperties}.</li>
 * <li>Stores the captured data in a {@link GatewayContext} object, which is
 * saved in the {@link ServerWebExchange} attributes.</li>
 * <li>Supports logging of JSON and form URL-encoded content types.</li>
 * </ul>
 *
 * <p>
 * The filter is executed with the highest precedence in the filter chain,
 * ensuring that it captures all relevant data before other filters process the
 * request.
 * </p>
 *
 * @author hoangtien2k3
 * @see WebFilter
 * @see Ordered
 * @see GatewayContext
 */
@Component
@Log4j2
@AllArgsConstructor
@Profile("!prod")
public class GatewayContextFilter implements WebFilter, Ordered {
    /**
     * Properties for configuring HTTP request and response logging.
     */
    private final HttpLogProperties httpLogProperties;

    /**
     * Configurer for customizing HTTP message readers and writers.
     */
    private final CodecConfigurer codecConfigurer;

    /**
     * Specifies the order in which this filter is applied. This filter has the
     * highest precedence.
     *
     * @return the highest precedence value, indicating that this filter is applied
     *         first.
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * Filters the incoming HTTP request and outgoing response, capturing data for
     * logging.
     *
     * @param exchange
     *            the current server exchange
     * @param chain
     *            the web filter chain
     * @return {@link Mono} signaling when request processing is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        boolean enableRequest = httpLogProperties.getRequest().isEnable();
        boolean enableResponse = httpLogProperties.getResponse().isEnable();
        if (Constants.EXCLUDE_LOGGING_ENDPOINTS.contains(request.getPath().toString())
                || (!enableRequest && !enableResponse)) {
            return chain.filter(exchange);
        }
        GatewayContext gatewayContext = new GatewayContext();
        gatewayContext.setReadRequestData(httpLogProperties.getRequest().isEnable());
        gatewayContext.setReadResponseData(httpLogProperties.getResponse().isEnable());
        HttpHeaders headers = request.getHeaders();
        gatewayContext.setRequestHeaders(headers);
        gatewayContext.setStartTime(System.currentTimeMillis());
        if (!gatewayContext.getReadRequestData()) {
            exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, gatewayContext);
            log.debug("[GatewayContext]Properties Set To Not Read Request Data");
            return chain.filter(exchange);
        }
        gatewayContext.getAllRequestData().addAll(request.getQueryParams());
        /*
         * save gateway context into exchange
         */
        exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, gatewayContext);
        MediaType contentType = headers.getContentType();
        if (headers.getContentLength() > 0) {
            if (MediaType.APPLICATION_JSON.includes(contentType)) {
                return readBody(exchange, chain, gatewayContext);
            }
            if (MediaType.APPLICATION_FORM_URLENCODED.includes(contentType)) {
                return readFormData(exchange, chain, gatewayContext);
            }
        }
        log.debug("[GatewayContext]ContentType:{},Gateway context is set with {}", contentType, gatewayContext);
        return chain.filter(exchange);
    }

    /**
     * Reads form data from the incoming HTTP request, processes it, and prepares
     * the data for further filtering in the chain.
     * <p>
     * This method handles form data (typically from an HTML form) in the request
     * body. It captures the form data, encodes it properly, and rewrites the
     * request body with the processed form data before passing the request along
     * the filter chain.
     * </p>
     *
     * @param exchange
     *            the current server exchange containing the request and response
     * @param chain
     *            the web filter chain
     * @param gatewayContext
     *            the context object that stores request and response data for
     *            logging
     * @return a {@link Mono} signaling when request processing is complete
     */
    private Mono<Void> readFormData(ServerWebExchange exchange, WebFilterChain chain, GatewayContext gatewayContext) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        return exchange.getFormData()
                .doOnNext(multiValueMap -> {
                    gatewayContext.setFormData(multiValueMap);
                    gatewayContext.getAllRequestData().addAll(multiValueMap);
                    log.debug("[GatewayContext]Read FormData Success");
                })
                .then(Mono.defer(() -> {
                    Charset charset = headers.getContentType().getCharset();
                    charset = charset == null ? StandardCharsets.UTF_8 : charset;
                    String charsetName = charset.name();
                    MultiValueMap<String, String> formData = gatewayContext.getFormData();
                    /*
                     * formData is empty just return
                     */
                    if (null == formData || formData.isEmpty()) {
                        return chain.filter(exchange);
                    }
                    StringBuilder formDataBodyBuilder = new StringBuilder();
                    String entryKey;
                    List<String> entryValue;
                    try {
                        /*
                         * repackage form data
                         */
                        for (Map.Entry<String, List<String>> entry : formData.entrySet()) {
                            entryKey = entry.getKey();
                            entryValue = entry.getValue();
                            if (entryValue.size() > 1) {
                                for (String value : entryValue) {
                                    formDataBodyBuilder
                                            .append(entryKey)
                                            .append("=")
                                            .append(URLEncoder.encode(value, charsetName))
                                            .append("&");
                                }
                            } else {
                                formDataBodyBuilder
                                        .append(entryKey)
                                        .append("=")
                                        .append(URLEncoder.encode(entryValue.get(0), charsetName))
                                        .append("&");
                            }
                        }
                    } catch (UnsupportedEncodingException e) {
                    }
                    /*
                     * substring with the last char '&'
                     */
                    String formDataBodyString = "";
                    if (!formDataBodyBuilder.isEmpty()) {
                        formDataBodyString = formDataBodyBuilder.substring(0, formDataBodyBuilder.length() - 1);
                    }
                    /*
                     * get data bytes
                     */
                    byte[] bodyBytes = formDataBodyString.getBytes(charset);
                    int contentLength = bodyBytes.length;
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.putAll(exchange.getRequest().getHeaders());
                    httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
                    /*
                     * in case of content-length not matched
                     */
                    httpHeaders.setContentLength(contentLength);
                    /*
                     * use BodyInserter to InsertFormData Body
                     */
                    BodyInserter<String, ReactiveHttpOutputMessage> bodyInserter =
                            BodyInserters.fromObject(formDataBodyString);
                    CachedBodyOutputMessage cachedBodyOutputMessage =
                            new CachedBodyOutputMessage(exchange, httpHeaders);
                    log.debug("[GatewayContext]Rewrite Form Data :{}", formDataBodyString);
                    return bodyInserter
                            .insert(cachedBodyOutputMessage, new BodyInserterContext())
                            .then(Mono.defer(() -> {
                                ServerHttpRequestDecorator decorator =
                                        new ServerHttpRequestDecorator(exchange.getRequest()) {
                                            @Override
                                            public HttpHeaders getHeaders() {
                                                return httpHeaders;
                                            }

                                            @Override
                                            public Flux<DataBuffer> getBody() {
                                                return cachedBodyOutputMessage.getBody();
                                            }
                                        };
                                return chain.filter(
                                        exchange.mutate().request(decorator).build());
                            }));
                }));
    }

    /**
     * Reads JSON data from the incoming HTTP request body and prepares it for
     * further filtering in the chain.
     * <p>
     * This method handles JSON data in the request body by capturing and storing it
     * in the {@link GatewayContext}. It then decorates the request to allow the
     * request body to be read multiple times before passing the request along the
     * filter chain.
     * </p>
     *
     * @param exchange
     *            the current server exchange containing the request and response
     * @param chain
     *            the web filter chain
     * @param gatewayContext
     *            the context object that stores request and response data for
     *            logging
     * @return a {@link Mono} signaling when request processing is complete
     */
    private Mono<Void> readBody(ServerWebExchange exchange, WebFilterChain chain, GatewayContext gatewayContext) {
        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            DataBufferUtils.retain(dataBuffer);
            Flux<DataBuffer> cachedFlux =
                    Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
            /*
             * repackage ServerHttpRequest
             */
            ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return cachedFlux;
                }
            };
            ServerWebExchange mutatedExchange =
                    exchange.mutate().request(mutatedRequest).build();
            return ServerRequest.create(mutatedExchange, codecConfigurer.getReaders())
                    .bodyToMono(String.class)
                    .doOnNext(objectValue -> {
                        if (objectValue != null)
                            objectValue = objectValue.replaceAll("\r", "").replaceAll("\n", "");
                        gatewayContext.setRequestBody(objectValue);
                        log.debug("[GatewayContext]Read JsonBody Success");
                    })
                    .then(chain.filter(mutatedExchange));
        });
    }
}
