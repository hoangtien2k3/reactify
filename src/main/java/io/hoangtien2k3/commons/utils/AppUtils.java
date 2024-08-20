package io.hoangtien2k3.commons.utils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * Utility class for handling reactive streams with various configurations.
 */
@Slf4j
public class AppUtils {

    /**
     * Runs a Mono stream on a bounded elastic scheduler with a timeout of 2 minutes.
     * Logs any errors that occur during the execution.
     *
     * @param functionMono the Mono stream to be executed
     */
    public static void runHiddenStream(Mono functionMono) {
        functionMono
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofMinutes(2))
                .doOnError(e -> log.error("runHiddenStream ex: ", e))
                .subscribe();
    }

    /**
     * Runs a Mono stream on a bounded elastic scheduler with a specified timeout.
     * Logs any errors that occur during the execution.
     *
     * @param functionMono the Mono stream to be executed
     * @param timeout      the timeout duration in minutes
     */
    public static void runHiddenStreamTimeout(Mono functionMono, int timeout) {
        functionMono
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofMinutes(timeout))
                .doOnError(e -> log.error("runHiddenStream ex: ", e))
                .subscribe();
    }

    /**
     * Runs a Mono stream on a bounded elastic scheduler without any timeout.
     * Logs any errors that occur during the execution.
     *
     * @param functionMono the Mono stream to be executed
     */
    public static void runHiddenStreamWithoutTimeout(Mono functionMono) {
        functionMono
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(e -> log.error("runHiddenStream ex: ", e))
                .subscribe();
    }

    /**
     * Inserts data by executing a Mono stream and returns a Mono of Boolean indicating success or failure.
     * If the stream is empty, it returns true. If an error occurs, it returns false.
     *
     * @param functionMono the Mono stream to be executed
     * @return a Mono of Boolean indicating the result of the operation
     */
    public static Mono<Boolean> insertData(Mono functionMono) {
        return functionMono.map(rs -> true).switchIfEmpty(Mono.just(true)).onErrorResume(throwable -> Mono.just(false));
    }
}
