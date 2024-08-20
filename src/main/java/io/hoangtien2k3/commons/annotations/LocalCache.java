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
package io.hoangtien2k3.commons.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for specifying caching behavior on methods.
 *
 * <p>
 * This annotation allows methods to utilize caching by specifying cache
 * parameters. The caching behavior is controlled by the
 * {@code durationInMinute}, {@code maxRecord}, and {@code autoCache}
 * attributes.
 * </p>
 *
 * <ul>
 * <li><b>durationInMinute:</b> Specifies the duration (in minutes) for which
 * the cache entries should be retained. The default value is 120 minutes.</li>
 * <li><b>maxRecord:</b> Defines the maximum number of cache records to store.
 * The default value is 1000 records.</li>
 * <li><b>autoCache:</b> Indicates whether caching should be automatically
 * applied. If {@code true}, caching is enabled without additional
 * configuration. The default value is {@code false}.</li>
 * </ul>
 *
 * <p>
 * This annotation should be applied to methods where caching is desired. The
 * cache implementation will use the specified parameters to manage caching
 * behavior, including cache expiration and size limits.
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalCache {

    /**
     * The duration (in minutes) for which cache entries should be retained.
     *
     * @return the cache duration in minutes
     */
    int durationInMinute() default 120;

    /**
     * The maximum number of cache records to store.
     *
     * @return the maximum number of cache records
     */
    int maxRecord() default 1000;

    /**
     * Whether caching should be automatically applied.
     *
     * @return {@code true} if caching should be automatically applied;
     *         {@code false} otherwise
     */
    boolean autoCache() default false;
}
