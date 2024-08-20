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
package io.hoangtien2k3.commons.utils;

import io.hoangtien2k3.commons.constants.CommonErrorCode;
import io.hoangtien2k3.commons.exception.BusinessException;
import io.hoangtien2k3.commons.model.response.DataResponse;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

/**
 * Utility class for handling HTTP responses. Provides methods to create
 * ResponseEntity objects and to process DataResponse objects.
 */
public class ResponseUtils {

    /**
     * Creates a ResponseEntity with a DataResponse containing the given data and a
     * success message.
     *
     * @param data
     *            the data to be included in the response
     * @return a ResponseEntity containing the DataResponse
     */
    public static ResponseEntity<DataResponse> ok(Object data) {
        return ResponseEntity.ok(new DataResponse<>(Translator.toLocale("success"), data));
    }

    /**
     * Creates a ResponseEntity with a DataResponse containing the given data and a
     * custom message.
     *
     * @param data
     *            the data to be included in the response
     * @param message
     *            the custom message to be included in the response
     * @return a ResponseEntity containing the DataResponse
     */
    public static ResponseEntity<DataResponse> ok(Object data, String message) {
        return ResponseEntity.ok(new DataResponse<>(Translator.toLocale(message), data));
    }

    /**
     * Processes an Optional DataResponse and returns a Mono containing the data. If
     * the DataResponse is empty or contains an error code, a BusinessException is
     * thrown.
     *
     * @param response
     *            the Optional DataResponse to be processed
     * @param extraInfo
     *            additional information to be included in the error message
     * @return a Mono containing the data from the DataResponse
     */
    public static Mono<Object> getResponse(Optional<DataResponse> response, String extraInfo) {
        return getResponseWithoutData(response, extraInfo)
                .switchIfEmpty(Mono.error(new BusinessException(
                        CommonErrorCode.INTERNAL_SERVER_ERROR, extraInfo + " data is null " + response)))
                .flatMap(data -> {
                    if (data == null) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, extraInfo + " data is null " + response));
                    }
                    return Mono.just(data);
                });
    }

    /**
     * Processes an Optional DataResponse and returns a Mono containing the data
     * without any additional processing. If the DataResponse is empty or contains
     * an error code, a BusinessException is thrown.
     *
     * @param response
     *            the Optional DataResponse to be processed
     * @param extraInfo
     *            additional information to be included in the error message
     * @return a Mono containing the data from the DataResponse
     */
    public static Mono<Object> getResponseWithoutData(Optional<DataResponse> response, String extraInfo) {
        if (response.isEmpty()) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, extraInfo + " response empty"));
        }
        var data = response.get();
        if (data.getErrorCode() != null) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INTERNAL_SERVER_ERROR, extraInfo + " errorCode exist " + data.getErrorCode()));
        }
        return Mono.justOrEmpty(data.getData());
    }
}
