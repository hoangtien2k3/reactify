/*
 * Copyright 2024 the original author Hoàng Anh Tiến
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
package io.hoangtien2k3.reactify.config; // package io.hoangtien2k3.commons.config;
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
// import org.springframework.data.r2dbc.dialect.MySqlDialect;
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
// @ConfigurationProperties(prefix = "spring.r2dbc.mariadb")
// public R2dbcProperties mariaDbProperties() {
// return new R2dbcProperties();
// }
//
// @Bean
// @Override
// public ConnectionFactory connectionFactory() {
// ConnectionFactory connectionFactory =
// ConnectionFactories.get(mariaDbProperties().getUrl());
// ConnectionPoolConfiguration configuration =
// ConnectionPoolConfiguration.builder(connectionFactory)
// .maxSize(mariaDbProperties().getPool().getMaxSize())
// .initialSize(mariaDbProperties().getPool().getInitialSize())
// .build();
// return new ConnectionPool(configuration);
// }
//
// @Bean
// public R2dbcEntityOperations mariaDbEntityTemplate(ConnectionFactory
// connectionFactory) {
// DatabaseClient databaseClient = DatabaseClient.builder()
// .connectionFactory(connectionFactory)
// .bindMarkers(MySqlDialect.INSTANCE.getBindMarkersFactory())
// .build();
//
// return new R2dbcEntityTemplate(databaseClient, MySqlDialect.INSTANCE,
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
