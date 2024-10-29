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
package io.hoangtien2k3.reactify.exception;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * <p>
 * CustomWebClientResponseException class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Getter
@Setter
public class CustomWebClientResponseException extends WebClientResponseException {

    private final String errorBody;
    private final HttpStatus statusCode;

    /**
     * <p>
     * Constructor for CustomWebClientResponseException.
     * </p>
     *
     * @param errorBody
     *            a {@link String} object
     * @param statusCode
     *            a {@link HttpStatus} object
     */
    public CustomWebClientResponseException(String errorBody, HttpStatus statusCode) {
        super(statusCode.value(), statusCode.getReasonPhrase(), null, errorBody.getBytes(), null);
        this.errorBody = errorBody;
        this.statusCode = statusCode;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getMessage() {
        return String.format(
                "CustomWebClientResponseException: HTTP %d %s - %s",
                statusCode.value(), statusCode.getReasonPhrase(), errorBody);
    }
}
