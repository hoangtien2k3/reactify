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

import reactor.core.publisher.Mono;

/**
 * The {@code SagaStep} interface defines a single step in a saga process. Each
 * step represents an individual operation that can be executed, and, if
 * necessary, rolled back in case of failure. The interface provides methods for
 * executing, reverting, and checking the completion status of the step.
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>complete()</strong>: Checks whether the step has been completed
 * successfully. This is used in the rollback process to determine which steps
 * need to be reverted.</li>
 * <li><strong>execute()</strong>: Executes the step and returns a
 * {@link Mono}&lt;{@link StepResult}&gt; indicating the result of the
 * execution. This method is used in the saga process to perform the step's
 * operation.</li>
 * <li><strong>revert()</strong>: Reverts the step's changes if the execution
 * fails. It returns a {@link Mono}&lt;{@link Boolean}&gt; indicating whether
 * the revert operation was successful.</li>
 * </ul>
 *
 * <h2>Methods Description:</h2>
 *
 * <h3>boolean complete()</h3>
 * <p>
 * This method is used to check whether the step has been completed
 * successfully. It returns {@code true} if the step is complete, and
 * {@code false} otherwise. This information is crucial for determining which
 * steps need to be rolled back in case of an error.
 * </p>
 *
 * <h3>Mono&lt;StepResult&gt; execute()</h3>
 * <p>
 * This method is responsible for executing the step's operation. It returns a
 * {@link Mono}&lt;{@link StepResult}&gt;, where {@link StepResult} encapsulates
 * the outcome of the execution. The {@link StepResult} typically contains
 * information about whether the execution was successful and any relevant
 * messages. This method is called during the saga process to perform the step's
 * operation.
 * </p>
 *
 * <h3>Mono&lt;Boolean&gt; revert()</h3>
 * <p>
 * This method is used to revert the step's changes if the execution fails. It
 * returns a {@link Mono}&lt;{@link Boolean}&gt;, where {@code true} indicates
 * that the revert operation was successful and {@code false} otherwise. This
 * method is called during the rollback process to undo the changes made by the
 * step if necessary.
 * </p>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>
 * {
 * 	&#64;code
 * 	public class MySagaStep implements SagaStep {
 *
 * 		&#64;Override
 * 		public boolean complete() {
 * 			// Check if the step has been completed
 * 			return true; // or false based on the actual completion status
 * 		}
 *
 * 		&#64;Override
 * 		public Mono<StepResult> execute() {
 * 			// Perform the step's operation and return the result
 * 			return Mono.just(new StepResult(true, "Operation succeeded"));
 * 		}
 *
 * 		@Override
 * 		public Mono<Boolean> revert() {
 * 			// Revert the step's changes and return the result
 * 			return Mono.just(true); // or false based on the actual revert outcome
 * 		}
 * 	}
 * 	// Create an instance of MySagaStep
 * 	SagaStep sagaStep = new MySagaStep();
 * 	// Execute the step
 * 	sagaStep.execute().doOnNext(result -> System.out.println("Step executed successfully: " + result))
 * 			.doOnError(ex -> System.out.println("Step execution failed: " + ex.getMessage())).subscribe();
 * 	// Revert the step
 * 	sagaStep.revert().doOnNext(success -> System.out.println("Step reverted successfully: " + success))
 * 			.doOnError(ex -> System.out.println("Step revert failed: " + ex.getMessage())).subscribe();
 * }
 * </pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The {@code SagaStep} interface is a fundamental component in implementing the
 * saga pattern. It defines the operations that can be performed as part of a
 * saga process, including executing the step, checking its completion status,
 * and reverting its changes if needed. Each step in the saga must implement
 * this interface to be integrated into the saga process.
 * </p>
 *
 * <p>
 * By implementing the {@code SagaStep} interface, you can define specific
 * behaviors for each step in your saga, handle successful execution, and manage
 * rollback scenarios. This interface provides a structured way to manage
 * complex transactional processes and ensure consistency in case of failures.
 * </p>
 */
public interface SagaStep {

    /**
     * Checks whether the step has been completed successfully.
     *
     * @return {@code true} if the step is complete, {@code false} otherwise.
     */
    boolean complete();

    /**
     * Executes the step's operation and returns a {@link Mono} containing the
     * result.
     *
     * @return A {@link Mono} containing the outcome of the execution.
     */
    Mono<StepResult> execute();

    /**
     * Reverts the step's changes if the execution fails.
     *
     * @return A {@link Mono}&lt;{@link Boolean}&gt; indicating whether the revert
     *         operation was successful.
     */
    Mono<Boolean> revert();
}
