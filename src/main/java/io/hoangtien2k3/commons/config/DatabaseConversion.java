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
package io.hoangtien2k3.commons.config;

import io.r2dbc.spi.Blob;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
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
    public MappingR2dbcConverter getR2dbcConverter() {
        R2dbcMappingContext mappingContext = getR2dbcMappingContext();
        R2dbcCustomConversions r2dbcCustomConversions = getR2dbcCustomConversions();
        return new MappingR2dbcConverter(mappingContext, r2dbcCustomConversions);
    }

    private R2dbcMappingContext getR2dbcMappingContext() {
        NamingStrategy namingStrategy = NamingStrategy.INSTANCE;
        R2dbcCustomConversions r2dbcCustomConversions = getR2dbcCustomConversions();
        R2dbcMappingContext context = new R2dbcMappingContext(namingStrategy);
        context.setSimpleTypeHolder(r2dbcCustomConversions.getSimpleTypeHolder());
        return context;
    }

    private R2dbcCustomConversions getR2dbcCustomConversions() {
        return R2dbcCustomConversions.of(MySqlDialect.INSTANCE, getListConverters());
    }

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

    @WritingConverter
    public enum InstantWriteConverter implements Converter<Instant, LocalDateTime> {
        INSTANCE;

        public LocalDateTime convert(Instant source) {
            return LocalDateTime.ofInstant(source, ZoneOffset.UTC);
        }
    }

    @ReadingConverter
    public enum BlobToStringConverter implements Converter<Blob, String> {
        INSTANCE;

        @Override
        public String convert(Blob source) {
            try {
                return source == null
                        ? null
                        : Mono.from(source.stream())
                                .map(bb -> StandardCharsets.UTF_8.decode(bb).toString())
                                .toFuture()
                                .get();
            } catch (Exception e) {
                log.error("Exception when read blob value", e);
                return null;
            }
        }
    }

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

    @ReadingConverter
    public enum BinaryToUUIDConverter implements Converter<byte[], UUID> {
        INSTANCE;

        @Override
        public UUID convert(byte[] source) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(source);
            long high = byteBuffer.getLong();
            long low = byteBuffer.getLong();
            return new UUID(high, low);
        }
    }

    @ReadingConverter
    public enum InstantReadConverter implements Converter<LocalDateTime, Instant> {
        INSTANCE;

        @Override
        public Instant convert(LocalDateTime localDateTime) {
            return localDateTime.toInstant(ZoneOffset.UTC);
        }
    }

    @ReadingConverter
    public enum BitSetReadConverter implements Converter<BitSet, Boolean> {
        INSTANCE;

        @Override
        public Boolean convert(BitSet bitSet) {
            return bitSet.get(0);
        }
    }

    @ReadingConverter
    public enum ZonedDateTimeReadConverter implements Converter<LocalDateTime, ZonedDateTime> {
        INSTANCE;

        @Override
        public ZonedDateTime convert(LocalDateTime localDateTime) {
            // Be aware - we are using the UTC timezone
            return ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
        }
    }

    @WritingConverter
    public enum ZonedDateTimeWriteConverter implements Converter<ZonedDateTime, LocalDateTime> {
        INSTANCE;

        @Override
        public LocalDateTime convert(ZonedDateTime zonedDateTime) {
            return zonedDateTime.toLocalDateTime();
        }
    }

    @WritingConverter
    public enum DurationWriteConverter implements Converter<Duration, Long> {
        INSTANCE;

        @Override
        public Long convert(Duration source) {
            return source != null ? source.toMillis() : null;
        }
    }

    @ReadingConverter
    public enum LocalDateTimeToDateReadConverter implements Converter<LocalDateTime, Date> {
        INSTANCE;

        @Override
        public Date convert(LocalDateTime localDateTime) {
            // Be aware - we are using the UTC timezone
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            Date date = Date.from(instant);
            return date;
        }
    }

    @ReadingConverter
    public enum DurationReadConverter implements Converter<Long, Duration> {
        INSTANCE;

        @Override
        public Duration convert(Long source) {
            return source != null ? Duration.ofMillis(source) : null;
        }
    }
}
