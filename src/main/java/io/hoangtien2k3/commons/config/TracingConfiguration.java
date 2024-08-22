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

import brave.Tracing;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up tracing in the application.
 * <p>
 * This configuration class provides beans for tracing using the Brave library.
 * It is designed to set up tracing capabilities for distributed tracing and
 * monitoring.
 * </p>
 * <p>
 * The configuration defines two beans:
 * </p>
 * <ul>
 * <li>{@link Tracer}: The primary interface used for tracing operations.</li>
 * <li>{@link Tracing}: The builder for creating a new tracing instance.</li>
 * </ul>
 */
@Configuration
public class TracingConfiguration {
    /**
     * Creates a {@link Tracer} bean that is used for tracing operations.
     * <p>
     * In this configuration, it returns a NOOP (no-operation) tracer. This is a
     * placeholder tracer that does nothing. This can be useful when tracing is
     * disabled or not properly configured.
     * </p>
     * <p>
     * In a production setup, you would typically replace this with an actual
     * implementation of a tracer.
     * </p>
     *
     * @param tracing
     *            the {@link Tracing} instance to be used for creating the tracer.
     * @return a {@link Tracer} instance configured as a NOOP tracer.
     */
    @Bean
    public Tracer tracer(Tracing tracing) {
        return BraveTracer.NOOP;
    }

    /**
     * Creates a {@link Tracing} bean used for tracing operations.
     * <p>
     * This method creates and configures a new {@link Tracing} instance using the
     * builder pattern.
     * </p>
     * <p>
     * The configuration here does not include any specific tracing settings or
     * configurations, and defaults are used. You might need to customize this setup
     * based on your tracing and monitoring needs.
     * </p>
     *
     * @return a configured {@link Tracing} instance.
     */
    @Bean
    public Tracing tracing() {
        return Tracing.newBuilder().build();
    }
}
