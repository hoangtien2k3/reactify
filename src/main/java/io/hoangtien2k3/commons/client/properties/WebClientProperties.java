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
package io.hoangtien2k3.commons.client.properties;

import io.hoangtien2k3.commons.filter.properties.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebClientProperties {
    private String name;
    private String address;
    private String username;
    private String password;
    private String authorization;
    private PoolProperties pool = new PoolProperties();
    private TimeoutProperties timeout = new TimeoutProperties();
    private RetryProperties retry = new RetryProperties();
    private ClientLogProperties log = new ClientLogProperties();
    private MonitoringProperties monitoring = new MonitoringProperties();
    private ProxyProperties proxy = new ProxyProperties();
    private List<ExchangeFilterFunction> customFilters;
    private boolean internalOauth = false;
}
