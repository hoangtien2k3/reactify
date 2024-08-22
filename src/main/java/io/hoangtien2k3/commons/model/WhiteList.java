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
package io.hoangtien2k3.commons.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The `WhiteList` class represents the configuration for public APIs that do
 * not require authentication. This class is used to define which URIs and HTTP
 * methods are allowed to be accessed without authentication.
 *
 * <h2>Annotations:</h2>
 * <ul>
 * <li><strong>@Data</strong>: Lombok annotation that automatically generates
 * getters, setters, equals, hashCode, and toString methods, along with a
 * constructor with all arguments and a no-arguments constructor.</li>
 * <li><strong>@NoArgsConstructor</strong>: Lombok annotation that generates a
 * no-arguments constructor.</li>
 * <li><strong>@AllArgsConstructor</strong>: Lombok annotation that generates a
 * constructor with all fields as arguments.</li>
 * </ul>
 *
 * <h2>Fields:</h2>
 * <ul>
 * <li><strong>uri</strong>: The URI pattern that is whitelisted and accessible
 * without authentication. For example, "/public/api" or "/v1/resources".</li>
 * <li><strong>methods</strong>: A list of HTTP methods (e.g., GET, POST, PUT)
 * that are allowed for the given URI. This allows specifying which HTTP methods
 * are permitted for the URI. If the list is empty, all methods are allowed for
 * the URI.</li>
 * </ul>
 *
 * <h2>Configuration Example:</h2>
 *
 * <pre>{@code
 * # In application.yml
 * whitelist:
 *   - uri: /public/api
 *     methods:
 *       - GET
 *       - POST
 *   - uri: /v1/resources
 *     methods:
 *       - GET
 * }</pre>
 *
 * <h2>Usage:</h2>
 * <p>
 * The `WhiteList` class is used in the configuration of your application to
 * define which endpoints can be accessed without authentication. This
 * configuration helps in making certain APIs publicly accessible while securing
 * other parts of your application.
 * </p>
 *
 * <p>
 * The class is typically used in conjunction with Spring Boot's configuration
 * properties support, allowing you to easily inject these configurations into
 * your application's security setup.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhiteList {
    private String uri;
    private List<String> methods;
}
