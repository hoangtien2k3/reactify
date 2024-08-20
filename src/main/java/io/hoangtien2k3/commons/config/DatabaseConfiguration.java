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
package io.hoangtien2k3.commons.config; // package io.hoangtien2k3.commons.config;
//
// import java.util.List;
//
// import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
// import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
// import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
// import org.springframework.data.r2dbc.dialect.PostgresDialect;
// import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
// import
// org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
// import org.springframework.r2dbc.core.DatabaseClient;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//
// import io.r2dbc.pool.ConnectionPool;
// import io.r2dbc.pool.ConnectionPoolConfiguration;
// import io.r2dbc.spi.ConnectionFactories;
// import io.r2dbc.spi.ConnectionFactory;
// import lombok.RequiredArgsConstructor;
// import reactor.core.publisher.Mono;
//
// @Configuration
// @EnableR2dbcRepositories
// @RequiredArgsConstructor
// public class DatabaseConfiguration extends AbstractR2dbcConfiguration {
//
// private final DatabaseConversion databaseConversion;
//
// @Bean
// @ConfigurationProperties(prefix = "spring.r2dbc.postgresql")
// public R2dbcProperties postgresqlProperties() {
// return new R2dbcProperties();
// }
//
// @Bean
// @Override
// public ConnectionFactory connectionFactory() {
// ConnectionFactory connectionFactory =
// ConnectionFactories.get(postgresqlProperties().getUrl());
// ConnectionPoolConfiguration configuration =
// ConnectionPoolConfiguration.builder(connectionFactory)
// .maxSize(postgresqlProperties().getPool().getMaxSize())
// .initialSize(postgresqlProperties().getPool().getInitialSize())
// .build();
// return new ConnectionPool(configuration);
// }
//
// @Bean
// public R2dbcEntityOperations postgresqlEntityTemplate(ConnectionFactory
// connectionFactory) {
// DatabaseClient databaseClient = DatabaseClient.builder()
// .connectionFactory(connectionFactory)
// .bindMarkers(PostgresDialect.INSTANCE.getBindMarkersFactory())
// .build();
//
// return new R2dbcEntityTemplate(
// databaseClient, PostgresDialect.INSTANCE,
// databaseConversion.getR2dbcConverter());
// }
//
// @Override
// public List<Object> getCustomConverters() {
// return databaseConversion.getListConverters();
// }
//
// @Bean
// public BeforeConvertCallback<?> r2dbcJacksonConfiguration() {
// return (entity, table) -> {
// ObjectMapper objectMapper = new ObjectMapper();
// objectMapper.registerModule(new JavaTimeModule());
// return Mono.just(entity);
// };
// }
// }
