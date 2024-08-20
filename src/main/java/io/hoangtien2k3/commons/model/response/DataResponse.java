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
package io.hoangtien2k3.commons.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.hoangtien2k3.commons.constants.MessageConstant;
import io.hoangtien2k3.commons.utils.Translator;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Represents a standardized response structure for API responses.
 *
 * @param <T>
 *            the type of the response data
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataResponse<T> implements Serializable {
    private String errorCode;
    private String message;
    private T data;

    /**
     * Constructs a DataResponse with a message and data.
     *
     * @param message
     *            the message to be included in the response
     * @param data
     *            the data to be included in the response
     */
    public DataResponse(String message, T data) {
        this.message = Translator.toLocaleVi(message);
        this.data = data;
    }

    /**
     * Constructs a DataResponse with a message.
     *
     * @param message
     *            the message to be included in the response
     */
    public DataResponse(String message) {
        this.message = Translator.toLocaleVi(message);
    }

    /**
     * Creates a successful DataResponse with the provided data.
     *
     * @param data
     *            the data to be included in the response
     * @param <T>
     *            the type of the response data
     * @return a DataResponse indicating success
     */
    public static <T> DataResponse<T> success(T data) {
        return DataResponse.<T>builder()
                .errorCode(MessageConstant.ERROR_CODE_SUCCESS)
                .message(MessageConstant.SUCCESS)
                .data(data)
                .build();
    }

    /**
     * Creates a failed DataResponse with the provided data.
     *
     * @param data
     *            the data to be included in the response
     * @param <T>
     *            the type of the response data
     * @return a DataResponse indicating failure
     */
    public static <T> DataResponse<T> failed(T data) {
        return DataResponse.<T>builder()
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .message(MessageConstant.FAIL)
                .data(data)
                .build();
    }
}
