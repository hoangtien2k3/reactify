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

import brave.Span;
import brave.Tracer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hoangtien2k3.reactify.constants.CommonConstant;
import io.hoangtien2k3.reactify.model.GatewayContext;
import io.hoangtien2k3.reactify.utils.DataUtil;
import io.hoangtien2k3.reactify.utils.RequestUtils;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * <p>
 * PerformanceLogFilter class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
@RequiredArgsConstructor
public class PerformanceLogFilter implements WebFilter, Ordered {
    private final Tracer tracer;
    private static final Logger logPerf = LoggerFactory.getLogger("perfLogger");
    private static final Logger reqResLog = LoggerFactory.getLogger("reqResLogger");
    private static final int MAX_BYTE = 800; // Max byte allow to print
    private final Environment environment;

    /** {@inheritDoc} */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long startMillis = System.currentTimeMillis();
        String name =
                exchange.getRequest().getPath().pathWithinApplication().value().substring(1);
        Span newSpan = tracer.nextSpan().name(name);
        var contextRef = new AtomicReference<Context>();

        if (exchange.getRequest().getPath().pathWithinApplication().value().contains("actuator"))
            return chain.filter(exchange);
        var requestId = exchange.getRequest().getHeaders().getFirst("Request-Id");
        return chain.filter(exchange)
                .doOnSuccess(o -> {
                    logPerf(exchange, newSpan, name, startMillis, "S", null);
                })
                .doOnError(o -> {
                    logPerf(exchange, newSpan, name, startMillis, "F", null);
                })
                .contextWrite(context -> {
                    var currContext = (Context) context;
                    contextRef.set(currContext);
                    // the error happens in a different thread, so get the trace from context, set
                    // in MDC
                    // and downstream
                    // to doOnError
                    setTraceIdFromContext(newSpan.context().traceIdString());
                    return context;
                })
                .then(Mono.fromRunnable(() -> {
                    if (!List.of(environment.getActiveProfiles()).contains("prod")) {
                        this.logReqResponse(exchange);
                    }
                }));
    }

    private void logPerf(
            ServerWebExchange exchange, Span newSpan, String name, Long start, String result, Throwable o) {
        newSpan.finish();
        long duration = System.currentTimeMillis() - start;
        if (duration < 50 || name.equals("health")) return;

        setTraceIdInMDC(newSpan.context().traceIdString());
        String msisdn = exchange.getAttribute(CommonConstant.MSISDN_TOKEN);
        MDC.put(CommonConstant.MSISDN_TOKEN, !DataUtil.isNullOrEmpty(msisdn) ? msisdn : "-");
        if (exchange != null
                && exchange.getRequest() != null
                && exchange.getRequest().getHeaders() != null) {
            String requestId = exchange.getRequest().getHeaders().getFirst("Request-Id");
            MDC.put(CommonConstant.REQUEST_ID, !DataUtil.isNullOrEmpty(requestId) ? requestId : "-");
        }

        logPerf.info(new StringBuilder(name)
                .append(" ")
                .append(duration)
                .append(" ")
                .append(result)
                .append(" A2 ") // M1:
                // tu
                // backend
                // Java1,
                // M2
                // tu
                // backend
                // Java2
                .append(o == null ? "-" : o.getMessage())
                .toString());
    }

    private void logPerf(
            AtomicReference<Context> contextRef, Span newSpan, String name, Long start, String result, Throwable o) {
        newSpan.finish();
        long duration = System.currentTimeMillis() - start;
        if (duration < 50 || name.equals("health")) return;

        setTraceIdInMDC(newSpan.context().traceIdString());
        if (contextRef.get() != null) {
            var context = contextRef.get();
            if (context.hasKey(CommonConstant.MSISDN_TOKEN))
                MDC.put(CommonConstant.MSISDN_TOKEN, contextRef.get().get(CommonConstant.MSISDN_TOKEN));
            else MDC.put(CommonConstant.MSISDN_TOKEN, "-");
        }

        logPerf.info(new StringBuilder(name)
                .append(" ")
                .append(duration)
                .append(" ")
                .append(result)
                .append(" A2 ") // M1:
                // tu
                // backend
                // Java1,
                // M2
                // tu
                // backend
                // Java2
                .append(o == null ? "-" : o.getMessage())
                .toString());
    }

    private void logReqResponse(ServerWebExchange exchange) {
        List<String> logs = new ArrayList<>();
        logRequest(exchange, logs);
        logResponse(exchange, logs);
        reqResLog.info(String.join(" | ", logs));
    }

    private void logRequest(ServerWebExchange exchange, List<String> logs) {
        ServerHttpRequest request = exchange.getRequest();
        URI requestURI = request.getURI();
        HttpHeaders headers = request.getHeaders();
        if (requestURI.getPath().startsWith("/"))
            logs.add(String.format("%s", requestURI.getPath().substring(1)));
        else logs.add(String.format("%s", requestURI.getPath()));
        logs.add(String.format("%s", request.getMethod()));
        logs.add(String.format("%s", RequestUtils.getIpAddress(request)));
        logs.add(String.format("%s", requestURI.getHost()));

        var logHeader = new StringBuilder();
        headers.forEach((key, value) -> logHeader.append(String.format("{%s:%s}", key, value)));
        if (!logHeader.isEmpty()) {
            logs.add(String.format("%s", logHeader));
        }

        // var logHeader = new StringBuilder();
        // headers.forEach((key, value) ->
        // {
        // if (!gatewayPluginProperties.getHideHeaderList().stream().anyMatch(header ->
        // header.equalsIgnoreCase(key))) {
        // logHeader.append(String.format("{%s:%s}", key, value));
        // }
        // });
        // if (logHeader.length() > 0) {
        // logs.add(String.format("%s", logHeader));
        // }
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (gatewayContext != null && !gatewayContext.getReadRequestData()) {
            reqResLog.debug("[RequestLogFilter]Properties Set Not To Read Request Data");
            return;
        }
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        if (!queryParams.isEmpty()) {
            var logQuery = new StringBuilder();
            queryParams.forEach((key, value) -> logQuery.append(String.format("{%s:%s}", key, value)));
            logs.add(String.format("%s", logQuery));
        } else {
            logs.add("-");
        }
        MediaType contentType = headers.getContentType();
        long length = headers.getContentLength();
        if (length > 0
                && null != contentType
                && (contentType.includes(MediaType.APPLICATION_JSON)
                        || contentType.includes(MediaType.APPLICATION_JSON))) {
            logs.add(String.format("%s", truncateBody(gatewayContext.getRequestBody())));
        } else if (length > 0 && null != contentType && (contentType.includes(MediaType.APPLICATION_FORM_URLENCODED))) {
            logs.add(String.format("%s", truncateBody(gatewayContext.getFormData())));
        } else {
            logs.add("-");
        }
    }

    private String truncateBody(MultiValueMap<String, String> formData) {
        StringBuilder messageResponse = new StringBuilder();
        Set<String> keys = formData.keySet();
        for (String key : keys) {
            messageResponse.append(key + ":" + truncateBody(formData.get(key)));
        }
        return messageResponse.toString();
    }

    private String truncateBody(List<String> messageList) {
        StringBuilder response = new StringBuilder();
        messageList.forEach(item -> {
            response.append(truncateBody(item, 200)).append(",");
        });
        return response.toString();
    }

    private String truncateBody(String s, int maxByte) {
        int b = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // ranges from http://en.wikipedia.org/wiki/UTF-8
            int skip = 0;
            int more;
            if (c <= 0x007f) {
                more = 1;
            } else if (c <= 0x07FF) {
                more = 2;
            } else if (c <= 0xd7ff) {
                more = 3;
            } else if (c <= 0xDFFF) {
                // surrogate area, consume next char as well
                more = 4;
                skip = 1;
            } else {
                more = 3;
            }

            if (b + more > maxByte) {
                return s.substring(0, i);
            }
            b += more;
            i += skip;
        }
        return s;
    }

    private String truncateBody(String s) {
        int b = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // ranges from http://en.wikipedia.org/wiki/UTF-8
            int skip = 0;
            int more;
            if (c <= 0x007f) {
                more = 1;
            } else if (c <= 0x07FF) {
                more = 2;
            } else if (c <= 0xd7ff) {
                more = 3;
            } else if (c <= 0xDFFF) {
                // surrogate area, consume next char as well
                more = 4;
                skip = 1;
            } else {
                more = 3;
            }

            if (b + more > MAX_BYTE) {
                return s.substring(0, i);
            }
            b += more;
            i += skip;
        }
        return s;
    }

    /**
     * log response exclude response body
     *
     * @param exchange
     * @param logs
     */
    private Mono<Void> logResponse(ServerWebExchange exchange, List<String> logs) {
        ServerHttpResponse response = exchange.getResponse();
        logs.add(String.format("%s", response.getStatusCode().value()));
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (gatewayContext.getReadResponseData()) {
            logs.add(String.format("%s", truncateBody(gatewayContext.getResponseBody())));
        }
        return Mono.empty();
    }

    private String truncateBody(Object responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(responseBody);
        } catch (Exception e) {
            reqResLog.error("Exception when parse response to string, ignore response", e);
            return "Truncated and remove if has exception";
        }
    }

    private void setTraceIdInMDC(String traceId) {
        if (!DataUtil.isNullOrEmpty(traceId)) {
            MDC.put("X-B3-TraceId", traceId);
        }
    }

    private void setTraceIdFromContext(String traceId) {
        setTraceIdInMDC(traceId);
    }

    /** {@inheritDoc} */
    @Override
    public int getOrder() {
        return 7;
    }
}
