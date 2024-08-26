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

import brave.Span;
import brave.Tracer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hoangtien2k3.commons.constants.CommonConstant;
import io.hoangtien2k3.commons.model.GatewayContext;
import io.hoangtien2k3.commons.utils.DataUtil;
import io.hoangtien2k3.commons.utils.RequestUtils;
import io.hoangtien2k3.commons.utils.TruncateUtils;
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
 * A WebFlux filter that logs performance metrics and request/response data for
 * HTTP requests. This filter is applied to all incoming requests, except those
 * targeting actuator endpoints. It measures the time taken to process requests,
 * captures tracing information using Sleuth, and conditionally logs request and
 * response data based on the application's active profile.
 * <p>
 * The performance data is logged using a dedicated logger (`perfLogger`), and
 * request/response data is logged using a separate logger (`reqResLogger`).
 *
 * <h2>Key Features:</h2>
 * <ul>
 * <li>Logs the duration of HTTP request processing.</li>
 * <li>Integrates with Spring Cloud Sleuth for distributed tracing.</li>
 * <li>Logs request and response bodies in non-production environments.</li>
 * <li>Excludes actuator endpoints from logging.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>
 * {
 * 	&#64;code
 * 	// Application configuration
 * 	&#64;SpringBootApplication
 * 	public class MyApplication {
 * 		public static void main(String[] args) {
 * 			SpringApplication.run(MyApplication.class, args);
 * 		}
 * 	}
 *
 * 	// Custom filter registration
 * 	&#64;Configuration
 * 	public class WebFilterConfig {
 * 		@Bean
 * 		public PerformanceLogFilter(Tracer trace, Environment) {
 * 			return new PerformanceLogFilter(tracer, environment);
 * 		}
 * 	}
 * }
 * </pre>
 *
 * <h2>Logging Example:</h2>
 *
 * <pre>{@code
 * // Performance log (perfLogger)
 * [perfLogger] [SpanId:12345] [TraceId:abcd1234] GET /api/example took 120ms - Success
 *
 * // Request/Response log (reqResLogger)
 * [reqResLogger] [RequestId:abcdef] Request Body: {"name":"hoangtien2k3"}
 * [reqResLogger] [RequestId:abcdef] Response Body: {"status":"OK"}
 * }</pre>
 */
@Component
@RequiredArgsConstructor
public class PerformanceLogFilter implements WebFilter, Ordered {
    private final Tracer tracer;
    private static final Logger logPerf = LoggerFactory.getLogger("perfLogger");
    private static final Logger reqResLog = LoggerFactory.getLogger("reqResLogger");
    private static final int MAX_BYTE = 800; // Max byte allow to print
    private final Environment environment;

    /**
     * Filters the incoming HTTP request to log performance metrics and optionally
     * logs request/response data if the environment is not production.
     *
     * <h3>Processing Flow:</h3>
     * <ol>
     * <li>Records the start time of the request.</li>
     * <li>Creates a new tracing span using Sleuth's Tracer.</li>
     * <li>If the request targets an actuator endpoint, it bypasses logging and
     * continues the chain.</li>
     * <li>Filters the request through the chain, logging performance data upon
     * success or failure.</li>
     * <li>If the environment is not production, logs the request and response
     * bodies.</li>
     * </ol>
     *
     * @param exchange
     *            The current server web exchange containing the request and
     *            response.
     * @param chain
     *            The WebFilterChain to pass the request to the next filter.
     * @return A {@code Mono<Void>} that completes when the filter chain has
     *         completed.
     */
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
                    contextRef.set((Context) context);
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
            messageResponse.append(key).append(":").append(truncateBody(formData.get(key)));
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
        return TruncateUtils.truncateBody(s, maxByte);
    }

    private String truncateBody(String s) {
        return TruncateUtils.truncateBody(s, MAX_BYTE);
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

    @Override
    public int getOrder() {
        return 7;
    }
}
