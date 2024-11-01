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
package io.hoangtien2k3.reactify.filter.http;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

/**
 * <p>
 * BodyInserterContext class.
 * </p>
 *
 * @author hoangtien2k3
 */
public class BodyInserterContext implements BodyInserter.Context {
    private final ExchangeStrategies exchangeStrategies;

    /**
     * <p>
     * Constructor for BodyInserterContext.
     * </p>
     */
    public BodyInserterContext() {
        this.exchangeStrategies = ExchangeStrategies.withDefaults();
    }

    /**
     * <p>
     * Constructor for BodyInserterContext.
     * </p>
     *
     * @param exchangeStrategies
     *            a {@link ExchangeStrategies} object
     */
    public BodyInserterContext(ExchangeStrategies exchangeStrategies) {
        this.exchangeStrategies = exchangeStrategies;
    }

    /** {@inheritDoc} */
    @Override
    public List<HttpMessageWriter<?>> messageWriters() {
        return exchangeStrategies.messageWriters();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<ServerHttpRequest> serverRequest() {
        return Optional.empty();
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> hints() {
        return Collections.emptyMap();
    }
}
