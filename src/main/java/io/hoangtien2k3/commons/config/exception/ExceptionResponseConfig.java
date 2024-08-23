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
package io.hoangtien2k3.commons.config.exception;

import io.hoangtien2k3.commons.constants.CommonErrorCode;
import io.hoangtien2k3.commons.exception.BusinessException;
import io.hoangtien2k3.commons.model.response.TraceErrorResponse;
import io.hoangtien2k3.commons.utils.DataUtil;
import io.hoangtien2k3.commons.utils.Translator;
import io.micrometer.tracing.Tracer;
// import io.r2dbc.spi.R2dbcException;
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
 * Centralized exception handling configuration for the application. This class
 * provides global exception handling for various types of exceptions and
 * formats them into a consistent error response format.
 *
 * <p>
 * The `ExceptionResponseConfig` class utilizes Spring's `@RestControllerAdvice`
 * to handle exceptions thrown by controllers and provides appropriate HTTP
 * responses with trace information for debugging purposes. Each exception
 * handler method logs the error with a trace ID and returns a
 * `TraceErrorResponse` object with relevant error information.
 * </p>
 *
 * <h2>Exception Handlers:</h2>
 * <ul>
 * <li><b>RuntimeException</b> - Handles general runtime exceptions. Returns a
 * 500 Internal Server Error status with a trace ID for debugging.</li>
 * <li><b>R2dbcException</b> - Handles exceptions specific to R2DBC (Reactive
 * Relational Database Connectivity). Returns a 500 Internal Server Error status
 * with a trace ID.</li>
 * <li><b>AccessDeniedException</b> - Handles access denial exceptions,
 * indicating insufficient permissions. Returns a 403 Forbidden status with a
 * trace ID.</li>
 * <li><b>DataBufferLimitException</b> - Handles exceptions when the data buffer
 * limit is exceeded. Returns a 400 Bad Request status with a trace ID and a
 * localized error message.</li>
 * <li><b>ServerWebInputException</b> - Handles exceptions related to invalid
 * request input formats. Returns a 400 Bad Request status with a trace ID and
 * the reason for the error.</li>
 * <li><b>WebExchangeBindException</b> - Handles exceptions related to binding
 * errors in request inputs. Returns a 400 Bad Request status with a trace ID
 * and a list of error messages. Special handling is provided for property
 * conversion errors.</li>
 * <li><b>BusinessException</b> - Handles business logic exceptions. Returns
 * appropriate HTTP status based on the error code and a trace ID. Supports
 * custom error codes like NOT_FOUND and NO_PERMISSION.</li>
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
     * Handles R2DBC exceptions. Logs the exception with a trace ID and returns a
     * 500 Internal Server Error status with a trace error response.
     *
     * @param ex
     *            the R2DBC exception
     * @param serverWebExchange
     *            the current server web exchange
     * @return a Mono containing the ResponseEntity with TraceErrorResponse
     */
    // @ExceptionHandler(R2dbcException.class)
    // public Mono<ResponseEntity<TraceErrorResponse<Object>>> r2dbcException(
    // R2dbcException ex, ServerWebExchange serverWebExchange) {
    // String traceId =
    // Objects.requireNonNull(tracer.currentSpan()).context().traceId();
    // log.error("R2dbc trace-id {} , error ", traceId, ex);
    // return Mono.just(new ResponseEntity<>(
    // new TraceErrorResponse<>(CommonErrorCode.SQL_ERROR, "Server error", null,
    // traceId),
    // HttpStatus.INTERNAL_SERVER_ERROR));
    // }

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
        if (!DataUtil.isNullOrEmpty(errorCode)) {
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
