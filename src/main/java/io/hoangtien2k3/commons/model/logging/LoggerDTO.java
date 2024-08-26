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
package io.hoangtien2k3.commons.model.logging;

import brave.Span;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.util.context.Context;

/**
 * The `LoggerDTO` class represents a Data Transfer Object (DTO) designed for
 * logging purposes. It encapsulates various fields related to logging
 * information, including context, span details, timing, results, and more. This
 * class is used to store and manage logging data for more structured and
 * detailed log entries.
 *
 * <h2>Attributes:</h2>
 * <ul>
 * <li><strong>contextRef</strong>: An `AtomicReference` containing the current
 * `Context`. This is used to manage and share logging context information
 * safely across threads.</li>
 * <li><strong>newSpan</strong>: A `Span` object representing a new span in a
 * distributed tracing system. This is useful for tracking the duration and
 * details of specific operations in the system.</li>
 * <li><strong>service</strong>: A `String` representing the name of the service
 * generating the log entry.</li>
 * <li><strong>startTime</strong>: A `Long` representing the start time of the
 * operation in milliseconds since epoch.</li>
 * <li><strong>endTime</strong>: A `Long` representing the end time of the
 * operation in milliseconds since epoch.</li>
 * <li><strong>result</strong>: A `String` indicating the result of the
 * operation (e.g., SUCCESS, FAILURE).</li>
 * <li><strong>response</strong>: An `Object` holding the response data returned
 * by the service or operation.</li>
 * <li><strong>logType</strong>: A `String` specifying the type of log entry
 * (e.g., INFO, ERROR, DEBUG).</li>
 * <li><strong>actionType</strong>: A `String` describing the type of action or
 * operation being logged (e.g., CREATE, UPDATE, DELETE).</li>
 * <li><strong>args</strong>: An array of `Object` holding the arguments or
 * parameters passed to the operation.</li>
 * <li><strong>title</strong>: A `String` providing a brief title or description
 * of the log entry.</li>
 * </ul>
 *
 * <h2>Constructors:</h2>
 * <ul>
 * <li><strong>LoggerDTO()</strong>: Default constructor that initializes an
 * empty instance.</li>
 * <li><strong>LoggerDTO(AtomicReference&lt;Context&gt; contextRef, Span
 * newSpan, String service, Long startTime, Long endTime, String result, Object
 * response, String logType, String actionType, Object[] args, String
 * title)</strong>: Constructor that initializes all attributes with provided
 * values.</li>
 * </ul>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>Getters and Setters:</strong> Methods to access and modify the
 * values of each attribute.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * // Create a new LoggerDTO instance with sample data
 * LoggerDTO loggerDTO = new LoggerDTO(new AtomicReference<>(Context.current()), // Current context
 * 		Span.current(), // Current span
 * 		"UserService", // Service name
 * 		System.currentTimeMillis() - 100, // Start time
 * 		System.currentTimeMillis(), // End time
 * 		"SUCCESS", // Result of the operation
 * 		"{ \"status\": \"ok\" }", // Response data
 * 		"INFO", // Log type
 * 		"CREATE", // Action type
 * 		new Object[]{"param1", "param2"}, // Arguments
 * 		"User Created" // Title
 * );
 *
 * // Use the LoggerDTO instance for logging
 * log.info("Logging entry: {}", loggerDTO);
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The `LoggerDTO` class is designed to hold comprehensive details related to
 * logging in an application. It captures information such as context, span,
 * service name, timing, results, responses, and more. This structured approach
 * helps in generating detailed and organized log entries, making it easier to
 * monitor and troubleshoot the application.
 * </p>
 *
 * <p>
 * By using `AtomicReference` for context management and `Span` for distributed
 * tracing, the class supports advanced logging scenarios in multi-threaded and
 * distributed environments. The `LoggerDTO` class is useful for creating
 * detailed log entries with all necessary context and metadata, enhancing the
 * quality and usefulness of logs.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggerDTO {
    /**
     * An `AtomicReference` containing the current `Context`, used for managing and
     * sharing logging context information safely across threads.
     */
    private AtomicReference<Context> contextRef;

    /**
     * A `Span` object representing a new span in a distributed tracing system,
     * useful for tracking the duration and details of specific operations.
     */
    private Span newSpan;

    /**
     * The name of the service generating the log entry.
     */
    private String service;

    /**
     * The start time of the operation in milliseconds since epoch.
     */
    private Long startTime;

    /**
     * The end time of the operation in milliseconds since epoch.
     */
    private Long endTime;

    /**
     * The result of the operation (e.g., SUCCESS, FAILURE).
     */
    private String result;

    /**
     * The response data returned by the service or operation.
     */
    private Object response;

    /**
     * The type of log entry (e.g., INFO, ERROR, DEBUG).
     */
    private String logType;

    /**
     * The type of action or operation being logged (e.g., CREATE, UPDATE, DELETE).
     */
    private String actionType;

    /**
     * An array of arguments or parameters passed to the operation.
     */
    private Object[] args;

    /**
     * A brief title or description of the log entry.
     */
    private String title;
}
