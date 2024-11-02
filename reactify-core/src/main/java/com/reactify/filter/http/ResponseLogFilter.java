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
package com.reactify.filter.http;

import static reactor.core.scheduler.Schedulers.single;

import com.reactify.DataUtil;
import com.reactify.LogUtils;
import com.reactify.model.GatewayContext;
import io.netty.buffer.UnpooledByteBufAllocator;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
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
 * <p>
 * ResponseLogFilter class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Log4j2
@AllArgsConstructor
@Component
public class ResponseLogFilter implements WebFilter, Ordered {
    private final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(cl -> cl.defaultCodecs().maxInMemorySize(50 * 1024 * 1024))
            .build();

    private static byte[] toByteArray(InputStream inStream) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc;
        byte[] in_b = new byte[] {};
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in_b = swapStream.toByteArray();
        } catch (Exception ignore) {

        }
        return in_b;
    }

    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (gatewayContext != null && !gatewayContext.getReadResponseData()) {
            log.debug("[ResponseLogFilter]Properties Set Not To Read Response Data");
            return chain.filter(exchange);
        }
        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @NotNull
            @Override
            public Mono<Void> writeWith(@NotNull Publisher<? extends DataBuffer> body) {
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

            @NotNull
            @Override
            public Mono<Void> writeAndFlushWith(@NotNull Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body).flatMapSequential(p -> p));
            }
        };
        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }

    private DataBuffer logRequestBody(DataBuffer buffer, ServerWebExchange exchange) {
        InputStream dataBuffer = buffer.asInputStream();
        byte[] bytes = toByteArray(dataBuffer);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (!DataUtil.isNullOrEmpty(gatewayContext)) {
            gatewayContext.setResponseBody(new String(bytes));
        }
        DataBufferUtils.release(buffer);
        return nettyDataBufferFactory.wrap(bytes);
    }

    @Override
    public int getOrder() {
        return 6;
    }

    public static class ResponseAdapter implements ClientHttpResponse {
        private final Flux<DataBuffer> flux;
        private final HttpHeaders headers;
        private final HttpStatus statusCode;
        private final MultiValueMap<String, ResponseCookie> cookies;

        public ResponseAdapter(
                Publisher<? extends DataBuffer> body,
                HttpHeaders headers,
                HttpStatus statusCode,
                MultiValueMap<String, ResponseCookie> cookies) {
            this.headers = headers;
            this.statusCode = statusCode;
            this.cookies = cookies;
            if (body instanceof Flux) {
                flux = (Flux<DataBuffer>) body;
            } else {
                flux = ((Mono<DataBuffer>) body).flux();
            }
        }

        @NotNull
        @Override
        public Flux<DataBuffer> getBody() {
            return flux;
        }

        @NotNull
        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        @NotNull
        @Override
        public HttpStatus getStatusCode() {
            return statusCode;
        }

        @NotNull
        @Override
        public MultiValueMap<String, ResponseCookie> getCookies() {
            return cookies;
        }
    }
}
