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
package io.hoangtien2k3.commons.utils;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * A utility class for handling reactive streams with various configurations.
 *
 * <p>
 * This class provides methods to execute {@link Mono} streams with different
 * configurations, including setting timeouts and handling errors. It uses a
 * bounded elastic scheduler for executing the streams.
 * </p>
 *
 * <p>
 * The methods in this class handle logging for any errors that occur during the
 * execution of the streams.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
public class AppUtils {

    /**
     * Executes a {@link Mono} stream on a bounded elastic scheduler with a timeout
     * of 2 minutes. Logs any errors that occur during execution.
     *
     * @param functionMono
     *            the {@link Mono} stream to be executed
     */
    public static void runHiddenStream(Mono<?> functionMono) {
        functionMono
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofMinutes(2))
                .doOnError(e -> log.error("runHiddenStream ex: ", e))
                .subscribe();
    }

    /**
     * Executes a {@link Mono} stream on a bounded elastic scheduler with a
     * specified timeout. Logs any errors that occur during execution.
     *
     * @param functionMono
     *            the {@link Mono} stream to be executed
     * @param timeout
     *            the timeout duration in minutes
     */
    public static void runHiddenStreamTimeout(Mono<?> functionMono, int timeout) {
        functionMono
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofMinutes(timeout))
                .doOnError(e -> log.error("runHiddenStream ex: ", e))
                .subscribe();
    }

    /**
     * Executes a {@link Mono} stream on a bounded elastic scheduler without any
     * timeout. Logs any errors that occur during execution.
     *
     * @param functionMono
     *            the {@link Mono} stream to be executed
     */
    public static void runHiddenStreamWithoutTimeout(Mono<?> functionMono) {
        functionMono
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(e -> log.error("runHiddenStream ex: ", e))
                .subscribe();
    }

    /**
     * Executes a {@link Mono} stream and returns a {@link Mono} of {@code Boolean}
     * indicating success or failure. If the stream is empty, it returns
     * {@code true}. If an error occurs, it returns {@code false}.
     *
     * @param functionMono
     *            the {@link Mono} stream to be executed
     * @return a {@link Mono} of {@code Boolean} indicating the result of the
     *         operation
     */
    public static Mono<Boolean> insertData(Mono<?> functionMono) {
        return functionMono.map(rs -> true).switchIfEmpty(Mono.just(true)).onErrorResume(throwable -> Mono.just(false));
    }
}
