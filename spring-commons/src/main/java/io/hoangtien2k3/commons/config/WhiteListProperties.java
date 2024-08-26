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
