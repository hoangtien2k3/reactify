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
package io.hoangtien2k3.reactify.filter.http;

import io.hoangtien2k3.reactify.constants.Constants;
import io.hoangtien2k3.reactify.filter.properties.HttpLogProperties;
import io.hoangtien2k3.reactify.model.GatewayContext;
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
 * <p>
 * GatewayContextFilter class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
@Log4j2
@AllArgsConstructor
@Profile("!prod")
public class GatewayContextFilter implements WebFilter, Ordered {
    private HttpLogProperties httpLogProperties;
    private CodecConfigurer codecConfigurer;

    /** {@inheritDoc} */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        boolean enableRequest = httpLogProperties.getRequest().enable();
        boolean enableResponse = httpLogProperties.getResponse().enable();
        if (Constants.EXCLUDE_LOGGING_ENDPOINTS.contains(request.getPath().toString())
                || (!enableRequest && !enableResponse)) {
            return chain.filter(exchange);
        }
        GatewayContext gatewayContext = new GatewayContext();
        gatewayContext.setReadRequestData(httpLogProperties.getRequest().enable());
        gatewayContext.setReadResponseData(httpLogProperties.getResponse().enable());
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
     * ReadFormData
     *
     * @param exchange
     * @param chain
     * @return
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
                            BodyInserters.fromValue(formDataBodyString);
                    CachedBodyOutputMessage cachedBodyOutputMessage =
                            new CachedBodyOutputMessage(exchange, httpHeaders);
                    log.debug("[GatewayContext] Rewrite Form Data :{}", formDataBodyString);
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
     * ReadJsonBody
     *
     * @param exchange
     * @param chain
     * @return
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
