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

/**
 * The `SagaProcess` class represents an abstract base class for implementing a
 * saga pattern. It defines the structure and behavior of a saga process, which
 * consists of a series of steps that need to be executed and potentially rolled
 * back in case of errors.
 *
 * <h2>Attributes:</h2>
 * <ul>
 * <li><strong>executedStep</strong>: A `List` of `SagaStep` objects that keeps
 * track of the steps that have been executed so far. It is used for rollback
 * purposes in case of failures.</li>
 * </ul>
 *
 * <h2>Abstract Methods:</h2>
 * <ul>
 * <li><strong>getSteps()</strong>: This abstract method should be implemented
 * by subclasses to return the list of `SagaStep` objects that define the steps
 * of the saga process.</li>
 * </ul>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>execute()</strong>: Executes the steps of the saga process. It
 * processes each step in sequence and handles the results. If any step fails,
 * the process triggers a rollback of the previously executed steps. The method:
 * <ul>
 * <li>Logs the start of the execution.</li>
 * <li>Uses `Flux.fromIterable(getSteps())` to create a stream of steps.</li>
 * <li>Applies `flatMap(SagaStep::execute)` to execute each step
 * asynchronously.</li>
 * <li>Handles the result of each step using `handle()` to either continue or
 * trigger an error.</li>
 * <li>Subscribes on a bounded elastic scheduler to ensure that the operations
 * are performed in a dedicated thread pool.</li>
 * <li>On error, calls the `revert()` method to roll back the changes made by
 * the previously executed steps.</li>
 * </ul>
 * </li>
 * <li><strong>revert()</strong>: Rolls back the previously executed steps in
 * reverse order. It is called when an error occurs during the execution of the
 * saga process. The method:
 * <ul>
 * <li>Logs the start of the rollback.</li>
 * <li>Reverses the order of the steps using
 * `Collections.reverse(getSteps())`.</li>
 * <li>Filters out the steps that have not been completed successfully and
 * applies `flatMap(SagaStep::revert)` to revert each step asynchronously.</li>
 * </ul>
 * </li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * public class MySagaProcess extends SagaProcess {
 * 	@Override
 * 	public List<SagaStep> getSteps() {
 * 		return List.of(new Step1(), new Step2(), new Step3());
 * 	}
 * }
 *
 * // Create an instance of MySagaProcess
 * SagaProcess sagaProcess = new MySagaProcess();
 *
 * // Execute the saga process
 * sagaProcess.execute().doOnSuccess(result -> System.out.println("Saga executed successfully"))
 * 		.doOnError(ex -> System.out.println("Saga execution failed: " + ex.getMessage())).subscribe();
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The `SagaProcess` class is designed to manage complex transactional processes
 * using the saga pattern. It provides a way to execute a series of steps while
 * ensuring that if any step fails, the previously executed steps are rolled
 * back to maintain consistency. The class uses reactive programming constructs
 * from Project Reactor to handle asynchronous execution and rollback.
 * </p>
 *
 * <p>
 * Subclasses must implement the `getSteps()` method to provide the specific
 * steps of the saga process. The `execute()` method orchestrates the execution
 * of these steps and manages rollback in case of errors. The `revert()` method
 * ensures that any changes made by the executed steps are undone in reverse
 * order.
 * </p>
 *
 * <p>
 * This class is useful in scenarios where long-running transactions or complex
 * business processes need to be managed, and where rollback capabilities are
 * required to handle failures gracefully.
 * </p>
 */
@Slf4j
public abstract class SagaProcess {

    /**
     * Abstract method that should be implemented by subclasses to return the list
     * of `SagaStep` objects that define the steps of the saga process.
     *
     * @return A list of `SagaStep` objects.
     */
    public abstract List<SagaStep> getSteps();

    /**
     * A `List` of `SagaStep` objects that keeps track of the steps that have been
     * executed so far. This is used for rollback purposes in case of failures.
     */
    protected final List<SagaStep> executedStep = new LinkedList<>();

    /**
     * Executes the steps of the saga process. It processes each step in sequence
     * and handles the results. If any step fails, the process triggers a rollback
     * of the previously executed steps.
     *
     * @return A `Flux` indicating the result of the saga execution.
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
     * Rolls back the previously executed steps in reverse order. It is called when
     * an error occurs during the execution of the saga process.
     *
     * @return A `Flux` indicating the result of the rollback.
     */
    public Flux<?> revert() {
        log.info("==================Start rollback================");
        Collections.reverse(getSteps());
        return Flux.fromIterable(getSteps()).filter(SagaStep::complete).flatMap(SagaStep::revert);
    }
}
