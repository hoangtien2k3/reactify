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
