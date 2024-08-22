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

import static reactor.core.scheduler.Schedulers.single;

import io.hoangtien2k3.commons.model.GatewayContext;
import io.hoangtien2k3.commons.utils.LogUtils;
import io.netty.buffer.UnpooledByteBufAllocator;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A WebFlux filter for logging HTTP response bodies in a Spring Gateway
 * application. This filter decorates the response object to capture and log the
 * response body content. It is typically used in non-production environments to
 * help with debugging and monitoring.
 * <p>
 * The filter can handle large response bodies by configuring the maximum
 * in-memory size, and it supports both Mono and Flux types of responses.
 *
 * <h2>Key Features:</h2>
 * <ul>
 * <li>Logs HTTP response bodies for requests that match certain criteria.</li>
 * <li>Decorates the response object to intercept and modify the response
 * body.</li>
 * <li>Supports both single (Mono) and multiple (Flux) data buffers.</li>
 * <li>Configurable in-memory buffer size for handling large responses.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>
 * {
 * 	&#64;code
 * 	&#64;Configuration
 * 	public class WebFilterConfig {
 *
 * 		@Bean
 * 		&#64;Profile("!prod")
 * 		public ResponseLogFilter responseLogFilter() {
 * 			return new ResponseLogFilter();
 * 		}
 * 	}
 * }
 * </pre>
 *
 * <h2>Logging Example:</h2>
 *
 * <pre>{@code
 * // Example log output
 * [ResponseLogFilter] Response Body: {"status":"OK", "data": {...}}
 * }</pre>
 */
@Log4j2
@AllArgsConstructor
@Component
// @Profile("!prod")
public class ResponseLogFilter implements WebFilter, Ordered {

    private final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(cl -> cl.defaultCodecs().maxInMemorySize(50 * 1024 * 1024))
            .build();

    /**
     * Converts an InputStream to a byte array.
     *
     * @param inStream
     *            the InputStream to convert
     * @return the byte array representation of the input stream
     */
    private static byte[] toByteArray(InputStream inStream) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        byte[] in_b = new byte[] {};
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in_b = swapStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in_b;
    }

    /**
     * Filters the incoming HTTP request and decorates the response to log the
     * response body.
     *
     * <h3>Processing Flow:</h3>
     * <ol>
     * <li>Checks if the request should log the response body based on the
     * {@link GatewayContext} settings.</li>
     * <li>If logging is enabled, wraps the response in a
     * {@link ServerHttpResponseDecorator}.</li>
     * <li>The decorator intercepts the response body, logs it, and returns a
     * modified data buffer.</li>
     * </ol>
     *
     * @param exchange
     *            The current server web exchange containing the request and
     *            response.
     * @param chain
     *            The WebFilterChain to pass the request to the next filter.
     * @return A `Mono<Void>` that completes when the filter chain has completed.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (!gatewayContext.getReadResponseData()) {
            log.debug("[ResponseLogFilter]Properties Set Not To Read Response Data");
            return chain.filter(exchange);
        }
        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                final MediaType contentType = super.getHeaders().getContentType();
                if (LogUtils.legalLogMediaTypes.contains(contentType)) {
                    if (body instanceof Mono) {
                        final Mono<DataBuffer> monoBody = (Mono<DataBuffer>) body;
                        return super.writeWith(
                                monoBody.publishOn(single()).map(buffer -> logRequestBody(buffer, exchange)));
                    } else if (body instanceof Flux) {
                        final Flux<DataBuffer> monoBody = (Flux<DataBuffer>) body;
                        return super.writeWith(
                                monoBody.publishOn(single()).map(buffer -> logRequestBody(buffer, exchange)));
                    }
                }
                return super.writeWith(body);
            }

            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body).flatMapSequential(p -> p));
            }
        };
        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }

    /**
     * Logs the content of the response body. This method is called within the
     * decorated response object to capture the response data as it is written.
     *
     * @param buffer
     *            The DataBuffer containing the response data.
     * @param exchange
     *            The current server web exchange.
     * @return A DataBuffer containing the logged data.
     */
    private DataBuffer logRequestBody(DataBuffer buffer, ServerWebExchange exchange) {
        InputStream dataBuffer = buffer.asInputStream();
        byte[] bytes = toByteArray(dataBuffer);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        gatewayContext.setResponseBody(new String(bytes));
        DataBufferUtils.release(buffer);
        return nettyDataBufferFactory.wrap(bytes);
    }

    /**
     * Specifies the order in which this filter is applied relative to other
     * filters. Filters with a lower order value are applied first.
     *
     * @return The order value for this filter.
     */
    @Override
    public int getOrder() {
        return 6;
    }

    /**
     * A wrapper class that adapts a response body Publisher to a
     * {@link ClientHttpResponse}. This class is used to facilitate logging by
     * allowing the response body to be accessed and logged before it is returned to
     * the client.
     */
    public static class ResponseAdapter implements ClientHttpResponse {

        private final Flux<DataBuffer> flux;
        private final HttpHeaders headers;

        /**
         * Constructs a ResponseAdapter with the given body and headers.
         *
         * @param body
         *            The Publisher that provides the response body.
         * @param headers
         *            The headers associated with the response.
         */
        public ResponseAdapter(Publisher<? extends DataBuffer> body, HttpHeaders headers) {
            this.headers = headers;
            if (body instanceof Flux) {
                flux = (Flux) body;
            } else {
                flux = ((Mono) body).flux();
            }
        }

        @Override
        public Flux<DataBuffer> getBody() {
            return flux;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        @Override
        public HttpStatus getStatusCode() {
            return null;
        }

        @Override
        public MultiValueMap<String, ResponseCookie> getCookies() {
            return null;
        }
    }
}
