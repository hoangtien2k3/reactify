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

import io.hoangtien2k3.commons.constants.Constants;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@code ClientLogProperties} represents the configuration properties for
 * logging client information.
 * <p>
 * This class is used to manage settings related to client logging, including
 * whether logging is enabled and which headers should be obfuscated.
 * </p>
 *
 * <p>
 * The properties are typically used to configure logging behavior in a client
 * context, with options for obfuscating sensitive headers to protect privacy.
 * </p>
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ClientLogProperties {
    /**
     * Indicates whether client logging is enabled.
     * <p>
     * By default, this is set to {@code true}, meaning logging is enabled unless
     * explicitly set otherwise.
     * </p>
     */
    private boolean enable = true;

    /**
     * A list of HTTP headers that should be obfuscated in the logs.
     * <p>
     * This list contains sensitive headers that are replaced or masked in the log
     * output to avoid exposing confidential information. By default, this list is
     * populated with sensitive headers from
     * {@code Constants.getSensitiveHeaders()}.
     * </p>
     */
    private List<String> obfuscateHeaders = Constants.getSensitiveHeaders();
}
