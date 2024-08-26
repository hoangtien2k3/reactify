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
package io.hoangtien2k3.commons.factory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

/**
 * Factory class for creating and configuring {@link ObjectMapper} instances.
 * <p>
 * The {@code ObjectMapperFactory} class provides methods to obtain different
 * configurations of Jackson's {@link ObjectMapper}. It includes configurations
 * for handling JSON deserialization, serialization, and custom deserializers.
 * </p>
 *
 * <h2>Class Overview:</h2>
 * <p>
 * This class provides static methods to retrieve {@link ObjectMapper} instances
 * with different configurations: - <code>getInstance</code>: Provides a
 * singleton instance with custom configurations. - <code>getInstance2</code>:
 * Provides a second instance with a different configuration. -
 * <code>defaultGetInstance</code>: Provides a default instance with standard
 * configurations.
 * </p>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>getInstance</strong>: Provides a singleton {@link ObjectMapper}
 * instance with custom configurations.
 * <ul>
 * <li><strong>Returns:</strong>
 * <p>
 * A singleton instance of {@link ObjectMapper} configured with custom settings
 * such as ignoring unknown properties, accepting single values as arrays,
 * unwrapping single value arrays, and registering custom deserializers.
 * </p>
 * </li>
 * <li><strong>Configuration:</strong>
 * <ul>
 * <li>DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES:
 * <code>false</code></li>
 * <li>DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY:
 * <code>true</code></li>
 * <li>DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS: <code>true</code></li>
 * <li>Custom deserializer for <code>boolean</code> and <code>Boolean</code>
 * types.</li>
 * <li>Registers <code>JavaTimeModule</code> and custom
 * <code>SimpleModule</code>.</li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * <li><strong>getInstance2</strong>: Provides a second {@link ObjectMapper}
 * instance with alternative configurations.
 * <ul>
 * <li><strong>Returns:</strong>
 * <p>
 * A new instance of {@link ObjectMapper} configured to ignore unknown
 * properties and accept single values as arrays.
 * </p>
 * </li>
 * <li><strong>Configuration:</strong>
 * <ul>
 * <li>DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES:
 * <code>false</code></li>
 * <li>DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY:
 * <code>true</code></li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * <li><strong>defaultGetInstance</strong>: Provides a default
 * {@link ObjectMapper} instance with standard configurations.
 * <ul>
 * <li><strong>Returns:</strong>
 * <p>
 * A new instance of {@link ObjectMapper} configured to ignore unknown
 * properties, accept single values as arrays, include non-null values only, and
 * register available modules.
 * </p>
 * </li>
 * <li><strong>Configuration:</strong>
 * <ul>
 * <li>DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES:
 * <code>false</code></li>
 * <li>DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY:
 * <code>true</code></li>
 * <li>SerializationInclusion: <code>NON_NULL</code></li>
 * <li>Finds and registers all available modules.</li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * </ul>
 *
 * <h2>Private Inner Classes:</h2>
 * <ul>
 * <li><strong>NumericBooleanDeserializer</strong>: Custom deserializer for
 * handling numeric and boolean values.
 * <ul>
 * <li><strong>deserialize</strong>: Converts numeric or textual representations
 * of boolean values ("1", "true", "0", "false") to <code>Boolean</code>.
 * <ul>
 * <li><strong>Parameters:</strong>
 * <ul>
 * <li><code>p</code> (<code>JsonParser</code>): The parser to read JSON
 * content.</li>
 * <li><code>ctxt</code> (<code>DeserializationContext</code>): The context for
 * deserialization.</li>
 * </ul>
 * </li>
 * <li><strong>Returns:</strong>
 * <p>
 * A <code>Boolean</code> value based on the input JSON text.
 * </p>
 * </li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * ObjectMapper mapper = ObjectMapperFactory.getInstance();
 * MyClass obj = mapper.readValue(jsonString, MyClass.class);
 * }</pre>
 *
 * <h2>Notes:</h2>
 * <p>
 * This factory class is designed to provide different configurations for
 * Jackson's {@link ObjectMapper} based on the needs of different use cases.
 * Custom deserializers and module registrations are handled within the factory
 * to ensure consistent configuration across various parts of the application.
 * </p>
 */
public class ObjectMapperFactory {
    private static ObjectMapper objectMapper;
    private static ObjectMapper objectMapperV2 = new ObjectMapper();
    private static ObjectMapper defaultGetInstance = new ObjectMapper();

    /**
     * Provides a singleton {@link ObjectMapper} instance with custom
     * configurations.
     * <p>
     * Configures the instance to ignore unknown properties, accept single values as
     * arrays, unwrap single value arrays, and register custom deserializers.
     * </p>
     *
     * @return A singleton instance of {@link ObjectMapper} with custom settings.
     */
    public static ObjectMapper getInstance() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            objectMapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true);
            SimpleModule module = new SimpleModule();
            module.addDeserializer(boolean.class, new NumericBooleanDeserializer());
            module.addDeserializer(Boolean.class, new NumericBooleanDeserializer());
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.registerModule(module);
        }
        return objectMapper;
    }

    /**
     * Provides a second {@link ObjectMapper} instance with alternative
     * configurations.
     * <p>
     * Configures the instance to ignore unknown properties and accept single values
     * as arrays.
     * </p>
     *
     * @return A new instance of {@link ObjectMapper} with alternative settings.
     */
    public static ObjectMapper getInstance2() {
        objectMapperV2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperV2.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return objectMapperV2;
    }

    /**
     * Provides a default {@link ObjectMapper} instance with standard
     * configurations.
     * <p>
     * Configures the instance to ignore unknown properties, accept single values as
     * arrays, include non-null values only, and register all available modules.
     * </p>
     *
     * @return A new instance of {@link ObjectMapper} with standard settings.
     */
    public static ObjectMapper defaultGetInstance() {
        defaultGetInstance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        defaultGetInstance.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        defaultGetInstance.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        defaultGetInstance.findAndRegisterModules();
        return defaultGetInstance;
    }

    /**
     * Custom deserializer for handling numeric and boolean values.
     */
    private static class NumericBooleanDeserializer extends JsonDeserializer<Boolean> {
        @Override
        public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if ("1".equals(p.getText()) || "true".equals(p.getText())) {
                return Boolean.TRUE;
            }
            if ("0".equals(p.getText()) || "false".equals(p.getText())) {
                return Boolean.FALSE;
            }
            return null;
        }
    }
}
