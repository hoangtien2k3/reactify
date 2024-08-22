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
package io.hoangtien2k3.commons.filter.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The `TimeoutProperties` class is used to configure timeout settings for HTTP
 * requests within an application. It leverages Lombok annotations to
 * automatically generate getters, setters, and constructors.
 *
 * <h2>Attributes:</h2>
 * <ul>
 * <li><strong>read</strong>: An integer specifying the read timeout in
 * milliseconds. The default value is `180000` milliseconds (3 minutes). This
 * timeout controls how long the application will wait for data to be read from
 * the server before timing out.</li>
 * <li><strong>connection</strong>: An integer specifying the connection timeout
 * in milliseconds. The default value is `500` milliseconds (0.5 seconds). This
 * timeout controls how long the application will wait to establish a connection
 * to the server before timing out.</li>
 * </ul>
 *
 * <h2>Lombok Annotations:</h2>
 * <ul>
 * <li><strong>@Data</strong>: Automatically generates getters, setters,
 * `equals()`, `hashCode()`, and `toString()` methods for the class.</li>
 * <li><strong>@AllArgsConstructor</strong>: Creates a constructor with all
 * fields as parameters, allowing for easy instantiation with all properties
 * set.</li>
 * <li><strong>@NoArgsConstructor</strong>: Provides a no-argument constructor
 * for creating instances with default values.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>
 * {
 * 	&#64;code
 * 	&#64;Configuration
 * 	&#64;ConfigurationProperties(prefix = "timeout")
 * 	public class TimeoutConfig {
 *
 * 		private final TimeoutProperties timeoutProperties;
 *
 * 		&#64;Autowired
 * 		public TimeoutConfig(TimeoutProperties timeoutProperties) {
 * 			this.timeoutProperties = timeoutProperties;
 * 		}
 *
 * 		@Bean
 * 		public WebClient.Builder webClientBuilder() {
 * 			return WebClient.builder()
 * 					.clientConnector(new ReactorClientHttpConnector(HttpClient.create()
 * 							.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutProperties.getConnection())
 * 							.responseTimeout(Duration.ofMillis(timeoutProperties.getRead()))));
 * 		}
 * 	}
 * }
 * </pre>
 *
 * <h2>Configuration Example:</h2>
 *
 * <pre>{@code
 * # application.yml
 * timeout:
 *   read: 180000
 *   connection: 500
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The `TimeoutProperties` class provides configuration options for setting
 * timeout values for HTTP requests. The `read` attribute specifies how long the
 * client will wait for data to be read from the server before giving up. The
 * `connection` attribute specifies how long the client will wait to establish a
 * connection to the server.
 * </p>
 *
 * <p>
 * Using this configuration allows you to fine-tune how your application handles
 * network delays and connection issues. By setting appropriate timeout values,
 * you can improve the responsiveness of your application and handle network
 * conditions more effectively.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeoutProperties {
    private int read = 180000;
    private int connection = 500;
}
