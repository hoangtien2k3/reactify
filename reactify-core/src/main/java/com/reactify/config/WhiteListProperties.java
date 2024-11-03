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
package com.reactify.config;

import com.reactify.model.WhiteList;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents the properties for the whitelist configuration.
 *
 * <p>
 * This class holds a list of {@link WhiteList} objects that define the entries
 * allowed in the application's whitelist.
 * </p>
 *
 * <p>
 * This class is used by Spring to bind properties from the application's
 * configuration. It utilizes a default constructor, which is provided by the
 * Java compiler, for creating instances.
 * </p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "application")
public class WhiteListProperties {

    /**
     * A list of whitelisted entities. This property holds a collection of
     * {@link WhiteList} objects that define the entries allowed in the
     * application's whitelist.
     *
     * @return a {@link List} of {@link WhiteList} objects.
     */
    private List<WhiteList> whiteList;

    /**
     * Constructs a new instance of {@code WhiteListProperties}.
     *
     * @param whiteList
     *            a list of {@link WhiteList} objects that are whitelisted.
     */
    public WhiteListProperties(List<WhiteList> whiteList) {
        this.whiteList = whiteList;
    }

    /**
     * Constructs a new instance of {@code WhiteListProperties}.
     */
    public WhiteListProperties() {}
}
