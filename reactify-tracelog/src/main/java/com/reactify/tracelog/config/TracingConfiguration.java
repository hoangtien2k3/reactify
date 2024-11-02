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
package com.reactify.tracelog.config;

import brave.Tracing;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * TracingConfiguration class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Configuration
public class TracingConfiguration {
    /**
     * <p>
     * tracer.
     * </p>
     *
     * @param tracing
     *            a {@link Tracing} object
     * @return a {@link Tracer} object
     */
    @Bean
    public Tracer tracer(Tracing tracing) {
        return BraveTracer.NOOP;
    }

    /**
     * <p>
     * tracing.
     * </p>
     *
     * @return a {@link Tracing} object
     */
    @Bean
    public Tracing tracing() {
        return Tracing.newBuilder().build();
    }
}
