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
package io.hoangtien2k3.exception;

import io.micrometer.tracing.Tracer;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

/**
 * <h2>Exception Handlers:</h2>
 * <ul>
 * <li><b>RuntimeException</b></li>
 * <li><b>AccessDeniedException</b></li>
 * <li><b>DataBufferLimitException</b></li>
 * <li><b>ServerWebInputException</b></li>
 * <li><b>WebExchangeBindException</b></li>
 * <li><b>BusinessException</b></li>
 * </ul>
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionResponseConfig {
    private final Tracer tracer;

    /**
     * Handles runtime exceptions. Logs the exception with a trace ID and returns a
     * 500 Internal Server Error status with a trace error response.
     *
     * @param ex
     *            the runtime exception
     * @param serverWebExchange
     *            the current server web exchange
     * @return a Mono containing the ResponseEntity with TraceErrorResponse
     */
    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<TraceErrorResponse<Object>>> runtimeException(
            RuntimeException ex, ServerWebExchange serverWebExchange) {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        log.error("Runtime exception trace-id {} , error ", traceId, ex);
        return Mono.just(new ResponseEntity<>(
                new TraceErrorResponse<>(CommonErrorCode.INTERNAL_SERVER_ERROR, "Server error", null, traceId),
                HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * Handles access denied exceptions. Logs the exception with a trace ID and
     * returns a 403 Forbidden status with a trace error response.
     *
     * @param ex
     *            the access denied exception
     * @param serverWebExchange
     *            the current server web exchange
     * @return a Mono containing the ResponseEntity with TraceErrorResponse
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Mono<ResponseEntity<TraceErrorResponse<Object>>> accessDeniedException(
            AccessDeniedException ex, ServerWebExchange serverWebExchange) {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        log.error("Access denied trace-id {} , error ", traceId, ex);
        return Mono.just(new ResponseEntity<>(
                new TraceErrorResponse<>(CommonErrorCode.ACCESS_DENIED, "Access denied", null, traceId),
                HttpStatus.FORBIDDEN));
    }

    /**
     * Handles data buffer limit exceptions. Logs the exception with a trace ID and
     * returns a 400 Bad Request status with a trace error response.
     *
     * @param ex
     *            the data buffer limit exception
     * @return a Mono containing the ResponseEntity with TraceErrorResponse
     */
    @ExceptionHandler(DataBufferLimitException.class)
    public Mono<ResponseEntity<TraceErrorResponse<Object>>> dataBufferLimitException(DataBufferLimitException ex) {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        log.error("DataBuffer limit trace-id {} , error ", traceId, ex);
        return Mono.just(new ResponseEntity<>(
                new TraceErrorResponse<>(
                        CommonErrorCode.BAD_REQUEST, Translator.toLocale("request.databuffer.limit"), null, traceId),
                HttpStatus.BAD_REQUEST));
    }

    /**
     * Handles server input exceptions, such as invalid request format. Logs the
     * exception with a trace ID and returns a 400 Bad Request status with a trace
     * error response.
     *
     * @param ex
     *            the server input exception
     * @param serverWebExchange
     *            the current server web exchange
     * @return a Mono containing the ResponseEntity with TraceErrorResponse
     */
    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<TraceErrorResponse<Object>>> serverInputException(
            ServerWebInputException ex, ServerWebExchange serverWebExchange) {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        log.error("Request Input invalid format trace-id {} , error ", traceId, ex);
        return Mono.just(new ResponseEntity<>(
                new TraceErrorResponse<>(CommonErrorCode.INVALID_PARAMS, ex.getReason(), null, traceId),
                HttpStatus.BAD_REQUEST));
    }

    /**
     * Handles binding errors in request inputs. Logs the exception with a trace ID
     * and returns a 400 Bad Request status with a trace error response containing
     * error messages.
     *
     * @param ex
     *            the WebExchangeBindException
     * @param serverWebExchange
     *            the current server web exchange
     * @return a Mono containing the ResponseEntity with TraceErrorResponse
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<TraceErrorResponse<Object>>> serverInputException(
            WebExchangeBindException ex, ServerWebExchange serverWebExchange) {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .map(Translator::toLocaleVi)
                .collect(Collectors.toList());

        String errorValue = String.join(", ", errors);
        if (errorValue.contains("Failed to convert property value")) {
            return Mono.just(new ResponseEntity<>(
                    new TraceErrorResponse<>(
                            CommonErrorCode.INVALID_PARAMS,
                            Translator.toLocaleVi("params.invalid.format"),
                            null,
                            traceId),
                    HttpStatus.BAD_REQUEST));
        }
        return Mono.just(new ResponseEntity<>(
                new TraceErrorResponse<>(CommonErrorCode.INVALID_PARAMS, errorValue, null, traceId),
                HttpStatus.BAD_REQUEST));
    }

    /**
     * Handles business logic exceptions. Logs the exception with a trace ID and
     * returns an appropriate HTTP status based on the error code. Defaults to 400
     * Bad Request but can return other statuses like 404 Not Found or 403 Forbidden
     * based on specific error codes.
     *
     * @param ex
     *            the business exception
     * @param serverWebExchange
     *            the current server web exchange
     * @return a Mono containing the ResponseEntity with TraceErrorResponse
     */
    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<TraceErrorResponse<Object>>> businessException(
            BusinessException ex, ServerWebExchange serverWebExchange) {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        String errorCode = ex.getErrorCode();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        if (!errorCode.isEmpty()) {
            if (errorCode.equals(CommonErrorCode.NOT_FOUND)) {
                httpStatus = HttpStatus.NOT_FOUND;
            } else if (errorCode.equals(CommonErrorCode.NO_PERMISSION)) {
                httpStatus = HttpStatus.FORBIDDEN;
            }
        }
        return Mono.just(new ResponseEntity<>(
                new TraceErrorResponse<>(ex.getErrorCode(), ex.getMessage(), null, traceId), httpStatus));
    }
}
