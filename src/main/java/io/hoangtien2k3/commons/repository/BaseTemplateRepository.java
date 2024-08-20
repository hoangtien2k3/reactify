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
package io.hoangtien2k3.commons.repository;

import io.hoangtien2k3.commons.utils.DataUtil;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class BaseTemplateRepository {

  private final R2dbcEntityTemplate entityTemplate;
  private final ObjectMapper objectMapper;

  @Autowired
  public BaseTemplateRepository(R2dbcEntityTemplate entityTemplate) {
    this.entityTemplate = entityTemplate;
    this.objectMapper = JsonMapper.builder().addModule(new JavaTimeModule())
        .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).build();
  }

  protected <T> Flux<T> listQuery(String sql, Map<String, Object> params, Class<T> type) {
    DatabaseClient.GenericExecuteSpec spec = entityTemplate.getDatabaseClient().sql(sql);
    if (!DataUtil.isNullOrEmpty(params)) {
      for (String param : params.keySet()) {
        spec = spec.bind(param, params.get(param));
      }
    }
    return spec.fetch().all().map(raw -> convert(raw, type));
  }

  protected Mono<Long> countQuery(String sql, Map<String, Object> params) {
    String query = "select count(*) as common_count_col from (" + sql + ") as common_count_alias";
    DatabaseClient.GenericExecuteSpec spec = entityTemplate.getDatabaseClient().sql(query);
    if (!DataUtil.isNullOrEmpty(params)) {
      for (String param : params.keySet()) {
        spec = spec.bind(param, params.get(param));
      }
    }
    return spec.fetch().first().map(result -> result.get("common_count_col")).cast(Long.class);
  }

  protected <T> T convert(Map<String, Object> raw, Class<T> type) {
    return objectMapper.convertValue(raw, type);
  }
}
