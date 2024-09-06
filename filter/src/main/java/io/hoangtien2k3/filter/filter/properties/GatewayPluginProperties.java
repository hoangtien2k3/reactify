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
package io.hoangtien2k3.filter.filter.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/** Gateway Plugin Properties */
@Slf4j
@Getter
@Setter
@ToString
// @Profile("!prod")
@Component
public class GatewayPluginProperties implements InitializingBean {
    public static final String GATEWAY_PLUGIN_PROPERTIES_PREFIX = "spring.plugin.config";

    /**
     * Enable Or Disable Read Request Data 。 If true, all request body will cached
     */
    private Boolean readRequestData = false;

    /** Enable Or Disable Read Response Data */
    private Boolean readResponseData = false;

    /** Enable Or Disable Log Request Detail */
    private boolean logRequest = false;

    /** Enable Or Disable Log Response Detail */
    private boolean logResponse = true;

    /** Hide header on log */
    private List<String> hideHeaderList = Collections.emptyList();

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!CollectionUtils.isEmpty(hideHeaderList)) {
            hideHeaderList = hideHeaderList.stream().map(String::toLowerCase).collect(Collectors.toList());
        }
    }
}
