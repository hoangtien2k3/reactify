package io.hoangtien2k3.commons.model;

import reactor.core.publisher.Mono;

public interface SagaStep {

    boolean complete();

    Mono<StepResult> execute();

    Mono<Boolean> revert();
}
