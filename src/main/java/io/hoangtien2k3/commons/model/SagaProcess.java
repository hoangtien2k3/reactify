package io.hoangtien2k3.commons.model;

import io.hoangtien2k3.commons.constants.CommonErrorCode;
import io.hoangtien2k3.commons.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
