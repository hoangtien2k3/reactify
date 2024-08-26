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

import lombok.*;

/**
 * The `LogField` class is a data transfer object (DTO) designed to encapsulate
 * various fields of logging information that can be used for structured logging
 * in an application. This class is used to represent and store log data with
 * detailed information about requests and responses.
 *
 * <h2>Attributes:</h2>
 * <ul>
 * <li><strong>traceId</strong>: A unique identifier for tracking the trace of a
 * request across different services.</li>
 * <li><strong>requestId</strong>: A unique identifier for the specific request,
 * often used to correlate logs for a single request.</li>
 * <li><strong>service</strong>: The name of the service that generated the log
 * entry.</li>
 * <li><strong>duration</strong>: The duration of the request or operation in
 * milliseconds.</li>
 * <li><strong>logType</strong>: The type of log entry (e.g., INFO, ERROR,
 * DEBUG).</li>
 * <li><strong>actionType</strong>: The type of action or operation being logged
 * (e.g., CREATE, UPDATE, DELETE).</li>
 * <li><strong>startTime</strong>: The start time of the operation in
 * milliseconds since epoch.</li>
 * <li><strong>endTime</strong>: The end time of the operation in milliseconds
 * since epoch.</li>
 * <li><strong>clientAddress</strong>: The IP address of the client making the
 * request.</li>
 * <li><strong>title</strong>: A brief title or description of the log
 * entry.</li>
 * <li><strong>inputs</strong>: The input data or parameters provided in the
 * request.</li>
 * <li><strong>response</strong>: The response data or result returned by the
 * service.</li>
 * <li><strong>result</strong>: The outcome or result of the operation (e.g.,
 * SUCCESS, FAILURE).</li>
 * </ul>
 *
 * <h2>Constructors:</h2>
 * <ul>
 * <li><strong>LogField()</strong>: Default constructor.</li>
 * <li><strong>LogField(String traceId, String requestId, String service, Long
 * duration, String logType, String actionType, Long startTime, Long endTime,
 * String clientAddress, String title, String inputs, String response, String
 * result)</strong>: Constructor with parameters to initialize all
 * attributes.</li>
 * </ul>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>Getters and Setters:</strong> Methods to get and set the values
 * of each attribute.</li>
 * <li><strong>Builder:</strong> Provides a builder pattern for creating
 * instances of `LogField` with a fluent API.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * LogField logField = LogField.builder().traceId("12345").requestId("67890").service("UserService").duration(150L)
 * 		.logType("INFO").actionType("CREATE").startTime(System.currentTimeMillis() - 150)
 * 		.endTime(System.currentTimeMillis()).clientAddress("192.168.1.1").title("User Created")
 * 		.inputs("{\"username\":\"johndoe\"}").response("{\"status\":\"success\"}").result("SUCCESS").build();
 *
 * log.info("Log entry: {}", logField);
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The `LogField` class is used to structure and manage log data in a consistent
 * manner. It includes fields for capturing detailed information about each log
 * entry, such as trace IDs, request IDs, service names, durations, log types,
 * and more. This structured approach helps in analyzing logs more effectively
 * and correlating log entries across different components and services.
 * </p>
 *
 * <p>
 * By using the builder pattern, the `LogField` class provides a flexible and
 * convenient way to create instances with various combinations of attributes.
 * The class can be integrated with logging frameworks to enhance the quality
 * and usability of log data.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LogField {
    private String traceId;
    private String requestId;
    private String service;
    private Long duration;
    private String logType;
    private String actionType;
    private Long startTime;
    private Long endTime;
    private String clientAddress;
    private String title;
    private String inputs;
    private String response;
    private String result;
}
