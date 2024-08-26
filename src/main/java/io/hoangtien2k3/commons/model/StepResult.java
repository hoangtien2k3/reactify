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
package io.hoangtien2k3.commons.model;

import lombok.Getter;

/**
 * The `StepResult` class represents the result of executing a step in a saga
 * process. It encapsulates the outcome of the step execution, including whether
 * it was successful and any associated message.
 *
 * <h2>Fields:</h2>
 * <ul>
 * <li><strong>success</strong>: A boolean indicating whether the step execution
 * was successful. `true` if the execution was successful, `false`
 * otherwise.</li>
 * <li><strong>message</strong>: A string containing any message associated with
 * the result. This is typically used to provide additional information or error
 * messages when the step fails.</li>
 * </ul>
 *
 * <h2>Constructors:</h2>
 * <p>
 * The constructor is private and only accessible through the static factory
 * methods. This design ensures that instances of `StepResult` are created with
 * specific success and failure states.
 * </p>
 *
 * <h2>Static Methods:</h2>
 * <ul>
 * <li><strong>success()</strong>: Creates a `StepResult` instance representing
 * a successful execution. The message will be `null`.</li>
 * <li><strong>failure(String message)</strong>: Creates a `StepResult` instance
 * representing a failed execution. The provided message will be used to
 * describe the failure.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * // Creating a successful result
 * StepResult successResult = StepResult.success();
 *
 * // Creating a failure result with a message
 * StepResult failureResult = StepResult.failure("An error occurred while executing the step.");
 *
 * // Checking the result
 * if (successResult.isSuccess()) {
 * 	System.out.println("Step executed successfully.");
 * } else {
 * 	System.out.println("Step failed: " + failureResult.getMessage());
 * }
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The `StepResult` class is used to encapsulate the result of executing a step
 * in a saga process. It provides a clear and structured way to represent
 * whether the execution was successful or failed, along with an optional
 * message that describes the outcome. This is useful for handling success and
 * failure scenarios in the saga process and ensuring that appropriate actions
 * are taken based on the result of each step.
 * </p>
 */
@Getter
public class StepResult {
    private final boolean success;
    private final String message;

    /**
     * Private constructor to create an instance of `StepResult`.
     *
     * @param success
     *            Indicates whether the step execution was successful.
     * @param message
     *            A message associated with the result, or `null` if there is no
     *            message.
     */
    private StepResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Creates a `StepResult` instance representing a successful execution.
     *
     * @return A `StepResult` instance with `success` set to `true` and `message`
     *         set to `null`.
     */
    public static StepResult success() {
        return new StepResult(true, null);
    }

    /**
     * Creates a `StepResult` instance representing a failed execution.
     *
     * @param message
     *            A message describing the failure.
     * @return A `StepResult` instance with `success` set to `false` and the
     *         provided `message`.
     */
    public static StepResult failure(String message) {
        return new StepResult(false, message);
    }
}
