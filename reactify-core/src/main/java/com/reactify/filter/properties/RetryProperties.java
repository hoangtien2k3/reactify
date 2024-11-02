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
package com.reactify.filter.properties;

import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import java.util.List;
import org.springframework.http.HttpMethod;

/**
 * <p>
 * RetryProperties class.
 * </p>
 *
 * @author hoangtien2k3
 */
public record RetryProperties(
        boolean isEnable, int count, List<HttpMethod> methods, List<Class<? extends Exception>> exceptions) {
    public RetryProperties() {
        this(
                true,
                2,
                List.of(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE),
                List.of(ConnectTimeoutException.class, ReadTimeoutException.class));
    }
}
