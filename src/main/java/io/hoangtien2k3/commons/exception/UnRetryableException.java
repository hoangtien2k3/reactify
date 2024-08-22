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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Exception class representing an error that cannot be retried.
 * <p>
 * This exception extends {@link BusinessException} and is used to signal errors
 * that are not eligible for retrying, indicating that the error condition is
 * permanent and requires manual intervention or resolution.
 * </p>
 * <p>
 * The exception carries an error code and a message to provide details about
 * the error.
 * </p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UnRetryableException extends BusinessException {

    /**
     * Constructs a new {@code UnRetryableException} with the specified error code
     * and message.
     *
     * @param errorCode
     *            the error code associated with the exception.
     * @param message
     *            the detailed message explaining the cause of the exception.
     */
    public UnRetryableException(String errorCode, String message) {
        super(errorCode, message);
    }
}
