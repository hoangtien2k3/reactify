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
package io.hoangtien2k3.commons.model.logging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The {@code HttpLogResponse} class is a configuration class used to manage
 * whether HTTP response logging is enabled or disabled.
 *
 * <h2>Attributes:</h2>
 * <ul>
 * <li><strong>enable</strong>: A boolean flag indicating whether HTTP response
 * logging is enabled. Default value is {@code true}.</li>
 * </ul>
 *
 * <h2>Constructor Details:</h2>
 * <ul>
 * <li><strong>HttpLogResponse()</strong>: Default constructor that initializes
 * {@code enable} to {@code true}.</li>
 * <li><strong>HttpLogResponse(boolean enable)</strong>: Constructor that allows
 * setting the {@code enable} flag explicitly.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>
 * {
 * 	&#64;code
 * 	&#64;Configuration
 * 	public class LoggingConfig {
 *
 * 		@Bean
 * 		public HttpLogResponse httpLogResponse() {
 * 			return new HttpLogResponse(true);
 * 		}
 * 	}
 * }
 * </pre>
 *
 * <h2>Configuration Example:</h2>
 *
 * <pre>{@code
 * # application.yml
 * http:
 *   log:
 *     response:
 *       enable: false
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The {@code HttpLogResponse} class provides a mechanism to configure logging
 * of HTTP responses within an application. By setting the {@code enable}
 * attribute to {@code true}, detailed logging of HTTP responses is enabled.
 * Setting it to {@code false} disables response logging, which can be useful to
 * reduce log verbosity in production environments or when response logging is
 * not necessary.
 * </p>
 *
 * <p>
 * This class can be used in combination with other logging configuration
 * classes to control the logging behavior for HTTP responses. It centralizes
 * the logging configuration and simplifies the management of logging settings
 * for HTTP responses.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpLogResponse {
    private boolean enable = true;
}
