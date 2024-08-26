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
package io.hoangtien2k3.commons.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code LogPerformance} annotation is used to mark methods or classes for
 * performance logging. It allows developers to specify what type of information
 * should be logged when the annotated method or class is executed, including
 * input arguments, output results, and other custom information.
 *
 * <p>
 * This annotation is typically applied to methods in a service layer where
 * performance monitoring is essential. The annotation can be configured to log
 * specific types of data, such as input arguments, output results, and the
 * duration of method execution. The logged information can then be used for
 * performance analysis, debugging, and auditing purposes.
 * </p>
 *
 * <h2>Example Usage:</h2>
 *
 * <pre>{@code
 * @LogPerformance(logType = "API_CALL", actionType = "FETCH_DATA", logInput = true, logOutput = true, title = "Fetching Data")
 * public Mono<DataResponse> fetchData(String id) {
 * 	// Method implementation
 * }
 * }</pre>
 *
 * <p>
 * In this example, the `fetchData` method is annotated with
 * {@code @LogPerformance}, which will log the input arguments, output results,
 * and execution duration when the method is called. The log will include the
 * specified `logType`, `actionType`, and `title`.
 * </p>
 *
 * <h2>Retention and Target:</h2>
 * <p>
 * The annotation is retained at runtime ({@link RetentionPolicy#RUNTIME}) and
 * can be applied to both methods and types (classes or interfaces)
 * ({@link ElementType#METHOD}, {@link ElementType#TYPE}).
 * </p>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogPerformance {
    String logType() default "";

    String actionType() default "";

    boolean logOutput() default true;

    boolean logInput() default true;

    String title() default "";
}
