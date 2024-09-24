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
package io.hoangtien2k3.reactify.model;

import lombok.Getter;

/**
 * <p>StepResult class.</p>
 *
 * @author hoangtien2k3
 */
@Getter
public class StepResult {
    private final boolean success;
    private final String message;

    private StepResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * <p>success.</p>
     *
     * @return a {@link io.hoangtien2k3.reactify.model.StepResult} object
     */
    public static StepResult success() {
        return new StepResult(true, null);
    }

    /**
     * <p>failure.</p>
     *
     * @param message a {@link java.lang.String} object
     * @return a {@link io.hoangtien2k3.reactify.model.StepResult} object
     */
    public static StepResult failure(String message) {
        return new StepResult(false, message);
    }
}
