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
package io.hoangtien2k3.commons.factory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

public class ObjectMapperFactory {
    private static ObjectMapper objectMapper;
    private static ObjectMapper objectMapperV2 = new ObjectMapper();
    private static ObjectMapper defaultGetInstance = new ObjectMapper();

    public static ObjectMapper getInstance() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            objectMapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true);
            SimpleModule module = new SimpleModule();
            module.addDeserializer(boolean.class, new NumericBooleanDeserializer());
            module.addDeserializer(Boolean.class, new NumericBooleanDeserializer());
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.registerModule(module);
        }
        return objectMapper;
    }

    public static ObjectMapper getInstance2() {
        objectMapperV2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperV2.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return objectMapperV2;
    }

    public static ObjectMapper defaultGetInstance() {
        defaultGetInstance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        defaultGetInstance.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        defaultGetInstance.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        defaultGetInstance.findAndRegisterModules();
        return defaultGetInstance;
    }

    private static class NumericBooleanDeserializer extends JsonDeserializer<Boolean> {
        @Override
        public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if ("1".equals(p.getText()) || "true".equals(p.getText())) {
                return Boolean.TRUE;
            }
            if ("0".equals(p.getText()) || "false".equals(p.getText())) {
                return Boolean.FALSE;
            }
            return null;
        }
    }
}
