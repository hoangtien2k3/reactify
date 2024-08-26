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
