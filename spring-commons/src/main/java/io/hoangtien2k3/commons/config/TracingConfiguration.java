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
