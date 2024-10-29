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
package io.hoangtien2k3.reactify.repository;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.hoangtien2k3.reactify.DataUtil;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * BaseTemplateRepository class.
 * </p>
 *
 * @author hoangtien2k3
 */
public class BaseTemplateRepository {

    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    private final ObjectMapper objectMapper;

    /**
     * <p>
     * Constructor for BaseTemplateRepository.
     * </p>
     */
    public BaseTemplateRepository() {
        objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build();
    }

    /**
     * <p>
     * listQuery.
     * </p>
     *
     * @param sql
     *            a {@link String} object
     * @param params
     *            a {@link Map} object
     * @param type
     *            a {@link Class} object
     * @param <T>
     *            a T class
     * @return a {@link Flux} object
     */
    protected <T> Flux<T> listQuery(String sql, Map<String, Object> params, Class<T> type) {
        DatabaseClient.GenericExecuteSpec spec =
                entityTemplate.getDatabaseClient().sql(sql);
        if (!DataUtil.isNullOrEmpty(params)) {
            for (String param : params.keySet()) {
                spec = spec.bind(param, params.get(param));
            }
        }
        return spec.fetch().all().map(raw -> convert(raw, type));
    }

    /**
     * <p>
     * countQuery.
     * </p>
     *
     * @param sql
     *            a {@link String} object
     * @param params
     *            a {@link Map} object
     * @return a {@link Mono} object
     */
    protected Mono<Long> countQuery(String sql, Map<String, Object> params) {
        String query = "select count(*) as common_count_col from (" + sql + ") as common_count_alias";
        DatabaseClient.GenericExecuteSpec spec =
                entityTemplate.getDatabaseClient().sql(query);
        if (!DataUtil.isNullOrEmpty(params)) {
            for (String param : params.keySet()) {
                spec = spec.bind(param, params.get(param));
            }
        }
        return spec.fetch()
                .first()
                .map(result -> result.get("common_count_col"))
                .cast(Long.class);
    }

    /**
     * <p>
     * convert.
     * </p>
     *
     * @param raw
     *            a {@link Map} object
     * @param type
     *            a {@link Class} object
     * @param <T>
     *            a T class
     * @return a T object
     */
    protected <T> T convert(Map<String, Object> raw, Class<T> type) {
        return objectMapper.convertValue(raw, type);
    }
}
