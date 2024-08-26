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
package io.hoangtien2k3.commons.filter.http;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

/**
 * A context for {@link BodyInserter} operations, providing access to exchange
 * strategies and other contextual information.
 *
 * <p>
 * This class implements the {@link BodyInserter.Context} interface and provides
 * methods to access {@link HttpMessageWriter} instances, server request
 * details, and contextual hints.
 * </p>
 *
 * <p>
 * The default constructor initializes the context with default
 * {@link ExchangeStrategies}. An alternate constructor allows for specifying
 * custom {@link ExchangeStrategies}.
 * </p>
 *
 * @see BodyInserter
 * @see ExchangeStrategies
 * @see HttpMessageWriter
 */
public class BodyInserterContext implements BodyInserter.Context {

    private final ExchangeStrategies exchangeStrategies;

    /**
     * Constructs a {@link BodyInserterContext} with default
     * {@link ExchangeStrategies}.
     */
    public BodyInserterContext() {
        this.exchangeStrategies = ExchangeStrategies.withDefaults();
    }

    /**
     * Constructs a {@link BodyInserterContext} with the specified
     * {@link ExchangeStrategies}.
     *
     * @param exchangeStrategies
     *            the {@link ExchangeStrategies} to use
     */
    public BodyInserterContext(ExchangeStrategies exchangeStrategies) {
        this.exchangeStrategies = exchangeStrategies;
    }

    /**
     * Returns the list of {@link HttpMessageWriter} instances available in this
     * context.
     *
     * <p>
     * This method delegates to the {@link ExchangeStrategies} to obtain the list of
     * message writers that can be used for writing HTTP messages.
     * </p>
     *
     * @return a list of {@link HttpMessageWriter} instances
     */
    @Override
    public List<HttpMessageWriter<?>> messageWriters() {
        return exchangeStrategies.messageWriters();
    }

    /**
     * Returns the {@link ServerHttpRequest} associated with this context, if
     * available.
     *
     * <p>
     * This method returns an empty {@link Optional} as this context does not
     * provide server request details.
     * </p>
     *
     * @return an empty {@link Optional}
     */
    @Override
    public Optional<ServerHttpRequest> serverRequest() {
        return Optional.empty();
    }

    /**
     * Returns a map of hints for the context, if any are available.
     *
     * <p>
     * This method returns an empty map as this context does not provide any
     * contextual hints.
     * </p>
     *
     * @return an empty map of hints
     */
    @Override
    public Map<String, Object> hints() {
        return Collections.emptyMap();
    }
}
