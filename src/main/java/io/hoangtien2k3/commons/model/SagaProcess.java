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
package io.hoangtien2k3.commons.model;

import io.hoangtien2k3.commons.constants.CommonErrorCode;
import io.hoangtien2k3.commons.exception.BusinessException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public abstract class SagaProcess {

    public abstract List<SagaStep> getSteps();

    protected final List<SagaStep> executedStep = new LinkedList<>();

    public Flux<?> execute() {
        log.info("==================Start execute================");
        return Flux.fromIterable(getSteps())
                .flatMap(SagaStep::execute)
                .handle((stepResult, synchronousSink) -> {
                    if (stepResult.isSuccess()) {
                        synchronousSink.next(true);
                    } else {
                        synchronousSink.error(
                                new BusinessException(CommonErrorCode.BAD_REQUEST, stepResult.getMessage()));
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(ex -> revert().then(Mono.error(ex)));
    }

    public Flux<?> revert() {
        log.info("==================Start rollback================");
        Collections.reverse(getSteps());
        return Flux.fromIterable(getSteps()).filter(SagaStep::complete).flatMap(SagaStep::revert);
    }
}
