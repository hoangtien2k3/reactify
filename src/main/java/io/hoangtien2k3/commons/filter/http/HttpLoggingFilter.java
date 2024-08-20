package io.hoangtien2k3.commons.filter.http;

import io.hoangtien2k3.commons.constants.Constants;
import io.hoangtien2k3.commons.filter.properties.HttpLogProperties;
import io.hoangtien2k3.commons.model.GatewayContext;
import io.hoangtien2k3.commons.utils.RequestUtils;
import io.hoangtien2k3.commons.utils.TruncateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static io.hoangtien2k3.commons.constants.Constants.MAX_BYTE;
import static reactor.core.scheduler.Schedulers.single;

/**
 * A filter for logging HTTP requests and responses.
 *
 * <p>This filter logs the details of HTTP requests and responses, including headers,
 * query parameters, and body content. It is designed to be used with Spring WebFlux.
 *
 * @author: hoangtien2k3
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class HttpLoggingFilter implements WebFilter, Ordered {
    private final HttpLogProperties httpLogProperties;

    /**
     * Returns the order of the filter.
     *
     * @return the order of the filter
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    /**
     * Filters the HTTP request and response, logging the details.
     *
     * @param exchange the current server exchange
     * @param chain the web filter chain
     * @return a Mono that indicates when request processing is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpResponseDecorator loggingServerHttpResponseDecorator =
                new ServerHttpResponseDecorator(exchange.getResponse()) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        if (httpLogProperties.getResponse().isEnable()) {
                            final MediaType contentType = super.getHeaders().getContentType();
                            if (Constants.VISIBLE_TYPES.contains(contentType)) {
                                if (body instanceof Mono) {
                                    final Mono<DataBuffer> monoBody = (Mono<DataBuffer>) body;
                                    return super.writeWith(
                                            monoBody.publishOn(single()).map(buffer -> {
                                                logResponseBody(buffer, exchange);
                                                return buffer;
                                            }));
                                } else if (body instanceof Flux) {
                                    final Flux<DataBuffer> fluxBody = (Flux<DataBuffer>) body;
                                    return super.writeWith(
                                            fluxBody.publishOn(single()).map(buffer -> {
                                                logResponseBody(buffer, exchange);
                                                return buffer;
                                            }));
                                }
                            }
                        }
                        return super.writeWith(body);
                    }
                };
        return chain.filter(exchange.mutate()
                        .response(loggingServerHttpResponseDecorator)
                        .build())
                .doOnSuccess(o -> {})
                .doOnError(err -> {})
                .then(Mono.fromRunnable(() -> {
                    logReqResponse(exchange);
                }));
    }

    /**
     * Logs the HTTP request and response details.
     *
     * @param exchange the current server exchange
     */
    private void logReqResponse(ServerWebExchange exchange) {
        if (Constants.EXCLUDE_LOGGING_ENDPOINTS.contains(
                exchange.getRequest().getPath().toString())) {
            return;
        }
        boolean enableRequest = httpLogProperties.getRequest().isEnable();
        boolean enableResponse = httpLogProperties.getResponse().isEnable();
        if (!enableRequest && !enableResponse) {
            return;
        }
        List<String> logs = new ArrayList<>();
        if (enableRequest) {
            logRequest(exchange, logs);
        }
        if (enableResponse) {
            logResponse(exchange, logs);
        }
        log.info(String.join(" | ", logs));
        log.info("Execute: {} | {}ms", exchange.getRequest().getPath(), takeDuration(exchange));
    }

    /**
     * Logs the HTTP request details.
     *
     * @param exchange the current server exchange
     * @param logs the list of log messages
     */
    private void logRequest(ServerWebExchange exchange, List<String> logs) {
        ServerHttpRequest request = exchange.getRequest();
        URI requestURI = request.getURI();
        HttpHeaders headers = request.getHeaders();
        if (requestURI.getPath().startsWith("/")) {
            logs.add(String.format("%s", requestURI.getPath().substring(1)));
        } else {
            logs.add(String.format("%s", requestURI.getPath()));
        }
        logs.add(String.format("%s", request.getMethod()));
        logs.add(String.format("%s", RequestUtils.getIpAddress(request)));
        logs.add(String.format("%s", requestURI.getHost()));

        var logHeader = new StringBuilder();
        headers.forEach((key, value) -> logHeader.append(String.format("{%s:%s}", key, value)));
        if (!logHeader.isEmpty()) {
            logs.add(String.format("%s", logHeader));
        }

        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (gatewayContext != null && !gatewayContext.getReadRequestData()) {
            log.debug("[RequestLogFilter]Properties Set Not To Read Request Data");
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
        String requestBody = null;
        if (length > 0 && null != contentType
                && (contentType.includes(MediaType.APPLICATION_JSON)
                || contentType.includes(MediaType.APPLICATION_JSON))
                && gatewayContext.getRequestBody() != null) {
            requestBody = TruncateUtils.truncateBody(gatewayContext.getRequestBody());
            logs.add(String.format("%s", TruncateUtils.truncate(requestBody, MAX_BYTE)));
        } else if (length > 0
                && null != contentType
                && (contentType.includes(MediaType.APPLICATION_FORM_URLENCODED))
                && gatewayContext.getFormData() != null) {
            requestBody = TruncateUtils.truncateBody(gatewayContext.getFormData());
            logs.add(String.format("%s", TruncateUtils.truncate(requestBody, MAX_BYTE)));
        } else {
            logs.add("-");
        }
    }

    /**
     * Calculates the duration of the request.
     *
     * @param exchange the current server exchange
     * @return the duration of the request in milliseconds
     */
    private Long takeDuration(ServerWebExchange exchange) {
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        return gatewayContext.getStartTime() != null
                ? System.currentTimeMillis() - gatewayContext.getStartTime()
                : null;
    }

    /**
     * Logs the HTTP response details.
     *
     * @param exchange the current server exchange
     * @param logs the list of log messages
     * @return a Mono that indicates when response logging is complete
     */
    private Mono<Void> logResponse(ServerWebExchange exchange, List<String> logs) {
        ServerHttpResponse response = exchange.getResponse();
        logs.add(String.format("%s", response.getStatusCode().value()));
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (gatewayContext.getReadResponseData()) {
            String body = TruncateUtils.truncateBody(gatewayContext.getResponseBody());
            logs.add(String.format("%s", TruncateUtils.truncate(body, MAX_BYTE)));
        }
        return Mono.empty();
    }

    /**
     * Logs the HTTP response body.
     *
     * @param buffer the data buffer containing the response body
     * @param exchange the current server exchange
     * @return the data buffer
     */
    private DataBuffer logResponseBody(DataBuffer buffer, ServerWebExchange exchange) {
        StringBuilder msg = new StringBuilder();
        Integer capacity = buffer.capacity();
        if (capacity < Constants.LoggingTitle.BODY_SIZE_RESPONSE_MAX) {
            msg.append(String.format("%s", StandardCharsets.UTF_8.decode(buffer.asByteBuffer())));
        } else {
            msg.append(String.format("%s", "response too log to log"));
        }
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (gatewayContext != null) {
            gatewayContext.setResponseBody(msg);
        }
        return buffer;
    }

    /**
     * Logs the HTTP request body.
     *
     * @param dataBuffer the data buffer containing the request body
     * @param prefix the prefix to be added to the log message
     * @param msg the StringBuilder to append the log message to
     */
    private void logRequestBody(DataBuffer dataBuffer, String prefix, StringBuilder msg) {
        msg.append(Constants.LoggingTitle.REQUEST_BODY);
        String message = "body request too long to log";
        try {
            Integer capacity = dataBuffer.capacity();
            if (capacity < Constants.LoggingTitle.BODY_SIZE_REQUEST_MAX) {
                message = StandardCharsets.UTF_8
                        .decode(dataBuffer.asByteBuffer())
                        .toString()
                        .replaceAll("\\s", "");
            }
        } catch (Exception ex) {
            log.error("Convert body request to string error ", ex);
        }
        msg.append(String.format("%s %s", prefix, message)).append("\n");
    }

    /**
     * Truncates the body of the message list.
     *
     * @param messageList the list of messages to be truncated
     * @return the truncated body as a string
     */
    private String truncateBody(List<String> messageList) {
        StringBuilder response = new StringBuilder();
        messageList.forEach(item -> {
            response.append(TruncateUtils.truncateBody(item, MAX_BYTE)).append(",");
        });
        return response.toString();
    }
}
