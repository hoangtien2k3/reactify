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
package io.hoangtien2k3.commons.client.properties;

import io.hoangtien2k3.commons.filter.properties.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

/**
 * Configuration properties for setting up a WebClient instance. This class
 * encapsulates various settings required to configure a WebClient, including
 * connection details, authentication, timeout settings, retry policies,
 * logging, monitoring, and proxy configurations.
 *
 * <p>
 * This class is typically used in a Spring Boot application to configure
 * WebClient beans via properties files or programmatically. The properties can
 * be populated using Spring's configuration mechanism, such as with application
 * properties or YAML files.
 * </p>
 *
 * <h3>Example Usage:</h3>
 *
 * <pre>{@code
 * WebClientProperties webClientProperties = new WebClientProperties();
 * webClientProperties.setName("exampleClient");
 * webClientProperties.setAddress("https://api.example.com");
 * webClientProperties.setUsername("user");
 * webClientProperties.setPassword("pass");
 * // Set other properties as needed
 * }</pre>
 *
 * <p>
 * In this example, the `WebClientProperties` class is used to configure a
 * WebClient instance, setting the connection address, credentials, and other
 * relevant properties.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebClientProperties {

    /**
     * The name of the WebClient configuration. This can be used to identify
     * different WebClient configurations within the application.
     *
     * <p>
     * Example: {@code "exampleClient"}
     * </p>
     */
    private String name;

    /**
     * The base URL address that the WebClient will connect to. This is typically
     * the root URL of the service the WebClient will communicate with.
     *
     * <p>
     * Example: {@code "https://api.example.com"}
     * </p>
     */
    private String address;

    /**
     * The username used for basic authentication. This is typically used in
     * conjunction with the `password` field.
     *
     * <p>
     * Example: {@code "user"}
     * </p>
     */
    private String username;

    /**
     * The password used for basic authentication. This is used alongside the
     * `username` for securing the WebClient requests.
     *
     * <p>
     * Example: {@code "pass"}
     * </p>
     */
    private String password;

    /**
     * The authorization token or credentials that the WebClient will use for
     * securing API requests. This can be a bearer token, API key, etc.
     *
     * <p>
     * Example: {@code "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."}
     * </p>
     */
    private String authorization;

    /**
     * Configuration properties for the WebClient's connection pool. This includes
     * settings such as maximum connections, idle timeout, etc.
     */
    private PoolProperties pool = new PoolProperties();

    /**
     * Configuration properties for timeouts in WebClient operations. This includes
     * settings such as connection timeout, read timeout, etc.
     */
    private TimeoutProperties timeout = new TimeoutProperties();

    /**
     * Configuration properties for retry policies. This includes settings for
     * retrying failed requests, backoff strategies, etc.
     */
    private RetryProperties retry = new RetryProperties();

    /**
     * Configuration properties for logging requests and responses. This includes
     * settings for enabling/disabling logging, log levels, etc.
     */
    private ClientLogProperties log = new ClientLogProperties();

    /**
     * Configuration properties for monitoring WebClient metrics. This includes
     * settings for capturing and reporting WebClient performance metrics.
     */
    private MonitoringProperties monitoring = new MonitoringProperties();

    /**
     * Configuration properties for setting up a proxy. This includes settings for
     * proxy host, port, and credentials.
     */
    private ProxyProperties proxy = new ProxyProperties();

    /**
     * Custom filter functions to be applied to the WebClient's request and response
     * exchanges. These filters can modify requests and responses, implement custom
     * logging, add headers, etc.
     */
    private List<ExchangeFilterFunction> customFilters;

    /**
     * Indicates whether the WebClient should use internal OAuth for authentication.
     * When set to {@code true}, the WebClient will use an internal mechanism for
     * obtaining OAuth tokens, rather than relying on external credentials.
     */
    private boolean internalOauth = false;
}
