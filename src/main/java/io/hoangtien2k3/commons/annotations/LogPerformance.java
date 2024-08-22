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

    /**
     * Specifies the type of log entry, such as "API_CALL", "DB_QUERY", etc.
     * <p>
     * This value helps categorize logs based on the type of operation being logged.
     * For instance, you might use different log types for API calls versus database
     * queries to differentiate them in your log analysis.
     * </p>
     *
     * @return the type of log entry
     */
    String logType() default "";

    /**
     * Specifies the type of action being performed, such as "CREATE", "UPDATE",
     * "DELETE", etc.
     * <p>
     * This value can be used to identify what kind of action is being logged,
     * providing more context for the log entry. For example, you might have actions
     * like "FETCH_DATA", "SAVE_ENTITY", etc.
     * </p>
     *
     * @return the type of action being logged
     */
    String actionType() default "";

    /**
     * Indicates whether the output (result) of the method should be logged.
     * <p>
     * When set to {@code true}, the output of the method will be logged, provided
     * it can be serialized. This is useful for tracing the results of method
     * executions, especially when debugging or monitoring system behavior.
     * </p>
     *
     * @return {@code true} if the output should be logged, {@code false} otherwise
     */
    boolean logOutput() default true;

    /**
     * Indicates whether the input arguments of the method should be logged.
     * <p>
     * When set to {@code true}, the input arguments to the method will be logged.
     * This is useful for capturing the state of the inputs when the method was
     * called, which can be important for understanding the context of the log
     * entry.
     * </p>
     *
     * @return {@code true} if the input arguments should be logged, {@code false}
     *         otherwise
     */
    boolean logInput() default true;

    /**
     * Specifies a custom title for the log entry.
     * <p>
     * This title can be used to provide a brief description of the logged
     * operation, making it easier to identify and search for specific log entries
     * in your logging system. For example, the title might be something like "User
     * Login Attempt" or "Order Processing".
     * </p>
     *
     * @return the custom title for the log entry
     */
    String title() default "";
}
