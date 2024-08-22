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
 * The `HttpLogRequest` class is a configuration class used to control whether
 * HTTP request logging is enabled or disabled.
 *
 * <h2>Attributes:</h2>
 * <ul>
 * <li><strong>enable</strong>: A boolean flag indicating whether HTTP request
 * logging is enabled. Default value is <code>true</code>.</li>
 * </ul>
 *
 * <h2>Constructor Details:</h2>
 * <ul>
 * <li><strong>HttpLogRequest()</strong>: Default constructor that initializes
 * <code>enable</code> to <code>true</code>.</li>
 * <li><strong>HttpLogRequest(boolean enable)</strong>: Constructor that allows
 * setting the <code>enable</code> flag explicitly.</li>
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
 * 		public HttpLogRequest httpLogRequest() {
 * 			return new HttpLogRequest(true);
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
 *     enable: false
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The `HttpLogRequest` class provides a simple way to configure logging of HTTP
 * requests within an application. By setting the <code>enable</code> attribute
 * to <code>true</code>, you can enable detailed logging for HTTP requests. If
 * set to <code>false</code>, logging is disabled, which can be useful for
 * reducing log verbosity in production environments or when detailed request
 * logging is not required.
 * </p>
 *
 * <p>
 * This class can be used in conjunction with other logging configuration
 * classes or aspects to conditionally enable or disable HTTP request logging
 * based on the application's needs. It helps in centralizing the configuration
 * of logging behavior and makes it easier to manage and control logging
 * settings.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpLogRequest {
    private boolean enable = true;
}
