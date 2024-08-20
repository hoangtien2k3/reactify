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
package io.hoangtien2k3.commons.exception;

import io.hoangtien2k3.commons.utils.Translator;
import java.util.Arrays;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Exception class used to represent business logic errors in the application.
 *
 * <p>
 * This class extends {@link RuntimeException} and provides additional
 * information such as an error code, an error message, and optional parameters
 * for the message. The message and parameters are translated to a localized
 * format using the {@link Translator} class.
 * </p>
 *
 * @see RuntimeException
 * @see Translator
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BusinessException extends RuntimeException {

    private String errorCode;
    private String message;
    private Object[] paramsMsg;

    /**
     * Constructs a new {@link BusinessException} with the specified error code and
     * message.
     *
     * <p>
     * The message is translated to a localized format using the {@link Translator}
     * class.
     * </p>
     *
     * @param errorCode
     *            the code representing the specific error
     * @param message
     *            the error message describing the nature of the error
     */
    public BusinessException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = Translator.toLocaleVi(message);
    }

    /**
     * Constructs a new {@link BusinessException} with the specified error code,
     * message, and parameters.
     *
     * <p>
     * The message and parameters are translated to a localized format using the
     * {@link Translator} class. The parameters are used to format the error
     * message.
     * </p>
     *
     * @param errorCode
     *            the code representing the specific error
     * @param message
     *            the error message describing the nature of the error
     * @param paramsMsg
     *            the parameters used to format the error message
     */
    public BusinessException(String errorCode, String message, String... paramsMsg) {
        this.errorCode = errorCode;
        this.paramsMsg = Arrays.stream(paramsMsg).map(Translator::toLocaleVi).toArray(String[]::new);
        this.message = Translator.toLocaleVi(message, this.paramsMsg);
    }
}
