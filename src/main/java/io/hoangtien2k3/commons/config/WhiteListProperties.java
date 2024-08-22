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
package io.hoangtien2k3.commons.config;

import io.hoangtien2k3.commons.model.WhiteList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties class for defining and managing a whitelist of URIs
 * and HTTP methods.
 * <p>
 * This class maps properties from the application's configuration file (e.g.,
 * application.yml or application.properties) to Java objects for easier access
 * and management.
 * </p>
 * <p>
 * The properties are prefixed with "application", as defined in the
 * {@link ConfigurationProperties} annotation.
 * </p>
 * <p>
 * The main purpose of this class is to provide a way to configure and access a
 * list of allowed URIs and HTTP methods that are considered safe and do not
 * require authentication.
 * </p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "application")
public class WhiteListProperties {

    /**
     * List of {@link WhiteList} objects representing the whitelisted URIs and HTTP
     * methods.
     * <p>
     * This property contains a list of whitelist configurations. Each entry in the
     * list specifies a URI and the HTTP methods that are allowed for that URI. This
     * can be used to configure which endpoints in the application should be
     * accessible without authentication or other security measures.
     * </p>
     *
     * @see WhiteList
     */
    private List<WhiteList> whiteList;
}
