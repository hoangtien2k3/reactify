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
package io.hoangtien2k3.commons.config;

import io.r2dbc.spi.Blob;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * The {@code DatabaseConversion} class provides utility methods to configure
 * and create a {@link MappingR2dbcConverter} with custom conversions for
 * handling various data types between Java objects and database records.
 * <p>
 * This class sets up custom converters for types such as {@link Instant},
 * {@link UUID}, {@link BitSet}, and others, ensuring proper conversion between
 * database and Java types.
 * </p>
 * <p>
 * The class also includes a set of nested enums that implement
 * {@link Converter} interfaces to handle specific conversion logic for
 * different data types.
 * </p>
 * <p>
 * The custom converters handle conversions for types such as:
 * <ul>
 * <li>{@link Instant} to {@link LocalDateTime} and vice versa</li>
 * <li>{@link UUID} to binary representation and vice versa</li>
 * <li>{@link Blob} to {@link String}</li>
 * <li>{@link Duration} to {@link Long} and vice versa</li>
 * <li>{@link LocalDateTime} to {@link Date}</li>
 * <li>{@link BitSet} to {@link Boolean}</li>
 * <li>{@link ZonedDateTime} to {@link LocalDateTime} and vice versa</li>
 * </ul>
 * <p>
 * Usage example:
 *
 * <pre>
 * DatabaseConversion conversion = new DatabaseConversion();
 * MappingR2dbcConverter converter = conversion.getR2dbcConverter();
 * </pre>
 */
@Slf4j
@Component
public class DatabaseConversion {

    /**
     * Creates and returns a MappingR2dbcConverter configured with custom
     * conversions and a mapping context.
     *
     * @return MappingR2dbcConverter instance for converting between Java objects
     *         and database records.
     */
    public MappingR2dbcConverter getR2dbcConverter() {
        R2dbcMappingContext mappingContext = getR2dbcMappingContext();
        R2dbcCustomConversions r2dbcCustomConversions = getR2dbcCustomConversions();
        return new MappingR2dbcConverter(mappingContext, r2dbcCustomConversions);
    }

    /**
     * Creates and configures a R2dbcMappingContext with a naming strategy and
     * custom conversions.
     *
     * @return R2dbcMappingContext instance used to map entities to database tables
     *         and columns.
     */
    private R2dbcMappingContext getR2dbcMappingContext() {
        NamingStrategy namingStrategy = NamingStrategy.INSTANCE;
        R2dbcCustomConversions r2dbcCustomConversions = getR2dbcCustomConversions();
        R2dbcMappingContext context = new R2dbcMappingContext(namingStrategy);
        context.setSimpleTypeHolder(r2dbcCustomConversions.getSimpleTypeHolder());
        return context;
    }

    /**
     * Creates and returns an R2dbcCustomConversions instance configured with a list
     * of custom converters.
     *
     * @return R2dbcCustomConversions instance for custom conversion logic.
     */
    private R2dbcCustomConversions getR2dbcCustomConversions() {
        return R2dbcCustomConversions.of(MySqlDialect.INSTANCE, getListConverters());
    }

    /**
     * Provides a list of custom converters used for data conversion between Java
     * types and database types.
     *
     * @return List of custom converters.
     */
    public List<Object> getListConverters() {
        List<Object> converters = new ArrayList<>();
        converters.add(InstantWriteConverter.INSTANCE);
        converters.add(InstantReadConverter.INSTANCE);
        converters.add(BitSetReadConverter.INSTANCE);
        converters.add(DurationWriteConverter.INSTANCE);
        converters.add(DurationReadConverter.INSTANCE);
        converters.add(ZonedDateTimeReadConverter.INSTANCE);
        converters.add(ZonedDateTimeWriteConverter.INSTANCE);
        converters.add(BinaryToUUIDConverter.INSTANCE);
        converters.add(BlobToStringConverter.INSTANCE);
        converters.add(UUIDToBinaryConverter.INSTANCE);
        converters.add(LocalDateTimeToDateReadConverter.INSTANCE);
        return converters;
    }

    /**
     * Converter to write Instant values to LocalDateTime in UTC.
     */
    @WritingConverter
    public enum InstantWriteConverter implements Converter<Instant, LocalDateTime> {
        INSTANCE;

        @Override
        public LocalDateTime convert(@NotNull Instant source) {
            return LocalDateTime.ofInstant(source, ZoneOffset.UTC);
        }
    }

    /**
     * Converter to read Blob values as Strings.
     */
    @ReadingConverter
    public enum BlobToStringConverter implements Converter<Blob, String> {
        INSTANCE;

        @Override
        public String convert(@NotNull Blob source) {
            try {
                return Mono.from(source.stream())
                        .map(bb -> StandardCharsets.UTF_8.decode(bb).toString())
                        .toFuture()
                        .get();
            } catch (Exception e) {
                log.error("Exception when read blob value", e);
                return null;
            }
        }
    }

    /**
     * Converter to write UUID values to byte arrays.
     */
    @WritingConverter
    public enum UUIDToBinaryConverter implements Converter<UUID, byte[]> {
        INSTANCE;

        @Override
        public byte[] convert(UUID uuid) {
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            return bb.array();
        }
    }

    /**
     * Converter to read byte arrays as UUID values.
     */
    @ReadingConverter
    public enum BinaryToUUIDConverter implements Converter<byte[], UUID> {
        INSTANCE;

        @Override
        public UUID convert(@NotNull byte[] source) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(source);
            long high = byteBuffer.getLong();
            long low = byteBuffer.getLong();
            return new UUID(high, low);
        }
    }

    /**
     * Converter to read LocalDateTime values as Instant values in UTC.
     */
    @ReadingConverter
    public enum InstantReadConverter implements Converter<LocalDateTime, Instant> {
        INSTANCE;

        @Override
        public Instant convert(LocalDateTime localDateTime) {
            return localDateTime.toInstant(ZoneOffset.UTC);
        }
    }

    /**
     * Converter to read BitSet as Boolean values.
     */
    @ReadingConverter
    public enum BitSetReadConverter implements Converter<BitSet, Boolean> {
        INSTANCE;

        @Override
        public Boolean convert(BitSet bitSet) {
            return bitSet.get(0);
        }
    }

    /**
     * Converter to read LocalDateTime values as ZonedDateTime in UTC.
     */
    @ReadingConverter
    public enum ZonedDateTimeReadConverter implements Converter<LocalDateTime, ZonedDateTime> {
        INSTANCE;

        @Override
        public ZonedDateTime convert(@NotNull LocalDateTime localDateTime) {
            return ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
        }
    }

    /**
     * Converter to write ZonedDateTime values as LocalDateTime.
     */
    @WritingConverter
    public enum ZonedDateTimeWriteConverter implements Converter<ZonedDateTime, LocalDateTime> {
        INSTANCE;

        @Override
        public LocalDateTime convert(ZonedDateTime zonedDateTime) {
            return zonedDateTime.toLocalDateTime();
        }
    }

    /**
     * Converter to write Duration values as Long values representing milliseconds.
     */
    @WritingConverter
    public enum DurationWriteConverter implements Converter<Duration, Long> {
        INSTANCE;

        @Override
        public Long convert(Duration source) {
            return source.toMillis();
        }
    }

    /**
     * Converter to read LocalDateTime values as Date values in the system's default
     * timezone.
     */
    @ReadingConverter
    public enum LocalDateTimeToDateReadConverter implements Converter<LocalDateTime, Date> {
        INSTANCE;

        @Override
        public Date convert(LocalDateTime localDateTime) {
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            return Date.from(instant);
        }
    }

    /**
     * Converter to read Long values as Duration values representing milliseconds.
     */
    @ReadingConverter
    public enum DurationReadConverter implements Converter<Long, Duration> {
        INSTANCE;

        @Override
        public Duration convert(@NotNull Long source) {
            return Duration.ofMillis(source);
        }
    }
}
