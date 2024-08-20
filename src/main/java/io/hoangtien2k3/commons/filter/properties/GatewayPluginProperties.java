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
package io.hoangtien2k3.commons.filter.properties;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
