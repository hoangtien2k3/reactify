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
package io.hoangtien2k3.utils.exception;

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
