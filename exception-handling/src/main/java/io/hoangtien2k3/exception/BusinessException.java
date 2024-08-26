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

import java.util.Arrays;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
