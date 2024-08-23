/// *
// * Copyright 2024 author - Hoàng Anh Tiến
// *
// * Permission is hereby granted, free of charge, to any person obtaining a
/// copy
// * of this software and associated documentation files (the "Software"), to
/// deal
// * in the Software without restriction, including without limitation the
/// rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
// */
// package io.hoangtien2k3.commons.repository;
//
// import com.fasterxml.jackson.databind.MapperFeature;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.PropertyNamingStrategies;
// import com.fasterxml.jackson.databind.json.JsonMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import io.hoangtien2k3.commons.utils.DataUtil;
// import java.util.Map;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
// import org.springframework.r2dbc.core.DatabaseClient;
// import org.springframework.stereotype.Component;
// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;
//
/// **
// * Base class for repositories providing common query operations using R2DBC.
// *
// * <p>
// * This class provides base methods for executing SQL queries and converting
// * results to objects. It uses {@link R2dbcEntityTemplate} for database
// * operations and {@link ObjectMapper} for JSON conversion.
// * </p>
// *
// * <p>
// * This class is annotated with {@link Component}, making it a Spring-managed
// * bean.
// * </p>
// *
// * @see R2dbcEntityTemplate
// * @see ObjectMapper
// * @see DatabaseClient
// */
// @Component
// public class BaseTemplateRepository {
//
// private final R2dbcEntityTemplate entityTemplate;
// private final ObjectMapper objectMapper;
//
// /**
// * Constructs a {@link BaseTemplateRepository} with the specified
// * {@link R2dbcEntityTemplate}.
// *
// * <p>
// * The constructor initializes the {@link ObjectMapper} with custom
// * configuration for JSON processing, including a {@link JavaTimeModule} for
// * handling Java 8 time types, and a naming strategy for property names.
// * </p>
// *
// * @param entityTemplate
// * the {@link R2dbcEntityTemplate} used for database operations
// */
// @Autowired
// public BaseTemplateRepository(R2dbcEntityTemplate entityTemplate) {
// this.entityTemplate = entityTemplate;
// this.objectMapper = JsonMapper.builder()
// .addModule(new JavaTimeModule())
// .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
// .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
// .build();
// }
//
// /**
// * Executes a SQL query and returns the results as a {@link Flux} of the
// * specified type.
// *
// * <p>
// * This method binds the provided parameters to the SQL query and converts the
// * raw result rows to the specified type using {@link #convert(Map, Class)}.
// * </p>
// *
// * @param sql
// * the SQL query to execute
// * @param params
// * a map of parameters to bind to the SQL query
// * @param type
// * the class of the result type
// * @param <T>
// * the type of the result
// * @return a {@link Flux} of results of the specified type
// */
// protected <T> Flux<T> listQuery(String sql, Map<String, Object> params,
/// Class<T> type) {
// DatabaseClient.GenericExecuteSpec spec =
// entityTemplate.getDatabaseClient().sql(sql);
// if (!DataUtil.isNullOrEmpty(params)) {
// for (String param : params.keySet()) {
// spec = spec.bind(param, params.get(param));
// }
// }
// return spec.fetch().all().map(raw -> convert(raw, type));
// }
//
// /**
// * Executes a SQL query to count the number of rows and returns the count as a
// * {@link Mono}.
// *
// * <p>
// * This method appends a count query to the provided SQL query and binds the
// * provided parameters. The result is cast to a {@link Long}.
// * </p>
// *
// * @param sql
// * the SQL query to count rows
// * @param params
// * a map of parameters to bind to the SQL query
// * @return a {@link Mono} containing the count of rows
// */
// protected Mono<Long> countQuery(String sql, Map<String, Object> params) {
// String query = "select count(*) as common_count_col from (" + sql + ") as
/// common_count_alias";
// DatabaseClient.GenericExecuteSpec spec =
// entityTemplate.getDatabaseClient().sql(query);
// if (!DataUtil.isNullOrEmpty(params)) {
// for (String param : params.keySet()) {
// spec = spec.bind(param, params.get(param));
// }
// }
// return spec.fetch()
// .first()
// .map(result -> result.get("common_count_col"))
// .cast(Long.class);
// }
//
// /**
// * Converts a map of raw data to an object of the specified type.
// *
// * <p>
// * This method uses {@link ObjectMapper} to convert the raw data to the
// * specified type.
// * </p>
// *
// * @param raw
// * a map of raw data
// * @param type
// * the class of the result type
// * @param <T>
// * the type of the result
// * @return the converted object of the specified type
// */
// protected <T> T convert(Map<String, Object> raw, Class<T> type) {
// return objectMapper.convertValue(raw, type);
// }
// }
