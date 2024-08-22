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
package io.hoangtien2k3.commons.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Configuration class to set up Cross-Origin Resource Sharing (CORS) for the
 * WebFlux application.
 * <p>
 * This class implements {@link WebFluxConfigurer} to customize CORS mappings
 * globally for the application.
 * </p>
 */
@Configuration
@EnableWebFlux
public class WebSecurityCorsFilter implements WebFluxConfigurer {

    /**
     * Configures CORS settings for the application.
     * <p>
     * This method sets global CORS configuration to allow requests from any origin,
     * supports all HTTP methods, and allows all headers. Additionally, it sets the
     * maximum age of the preflight response cache to 3600 seconds (1 hour).
     * </p>
     *
     * @param corsRegistry
     *            the {@link CorsRegistry} to customize CORS mappings
     */
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry
                .addMapping("/**") // Allow CORS requests to all endpoints
                .allowedOrigins("*") // Allow requests from any origin
                .allowedMethods("*") // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
                .allowedHeaders("*") // Allow all headers in requests
                .maxAge(3600); // Cache preflight responses for 3600 seconds (1 hour)
    }
}
