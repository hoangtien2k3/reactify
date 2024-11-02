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
package com.reactify.model;

import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * <p>
 * Abstract SagaProcess class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
public abstract class SagaProcess {

    /**
     * <p>
     * getSteps.
     * </p>
     *
     * @return a {@link List} object
     */
    public abstract List<SagaStep> getSteps();

    protected final List<SagaStep> executedStep = new LinkedList<>();

    /**
     * <p>
     * execute.
     * </p>
     *
     * @return a {@link Flux} object
     */
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

    /**
     * <p>
     * revert.
     * </p>
     *
     * @return a {@link Flux} object
     */
    public Flux<?> revert() {
        log.info("==================Start rollback================");
        Collections.reverse(getSteps());
        return Flux.fromIterable(getSteps()).filter(SagaStep::complete).flatMap(SagaStep::revert);
    }
}
