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
package io.hoangtien2k3.commons.annotations.logging;

import static io.hoangtien2k3.commons.constants.Constants.MAX_BYTE;

import io.hoangtien2k3.commons.factory.ObjectMapperFactory;
import io.hoangtien2k3.commons.model.logging.LogField;
import io.hoangtien2k3.commons.model.logging.LoggerDTO;
import io.hoangtien2k3.commons.utils.DataUtil;
import io.hoangtien2k3.commons.utils.RequestUtils;
import io.hoangtien2k3.commons.utils.TruncateUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * The {@code LoggerSchedule} class is a Spring-managed configuration class
 * responsible for processing and logging records from the {@link LoggerQueue}.
 * This class schedules a task that runs periodically to extract log data,
 * process it, and log it using a performance-specific logger. The log data
 * includes information such as trace IDs, IP addresses, request IDs, input
 * arguments, responses, and more.
 *
 * <p>
 * This class is designed to handle log records in a non-blocking, reactive web
 * application environment, typically using Spring WebFlux. It extracts useful
 * information from the application context and logs it for performance
 * monitoring and debugging purposes.
 * </p>
 *
 * <h2>Example Usage:</h2>
 *
 * <pre>
 * {
 * 	&#64;code
 * 	&#64;Configuration
 * 	&#64;RequiredArgsConstructor
 * 	&#64;Slf4j
 * 	public class LoggerSchedule {
 *
 * 		// Scheduled task to process log records every 3 seconds
 * 		@Scheduled(fixedDelay = 3000)
 * 		public void scheduleSaveLogClick() {
 * 			// implementation
 * 		}
 *
 * 		// Additional methods for processing records
 * 	}
 * }
 * </pre>
 *
 * <p>
 * This class is typically used in conjunction with a log queue manager like
 * {@link LoggerQueue} that accumulates log data asynchronously from various
 * parts of the application. The scheduled task then retrieves and processes
 * these logs at regular intervals.
 * </p>
 *
 * <h2>Thread Safety:</h2>
 * <p>
 * This class is thread-safe, as the scheduled method
 * {@link #scheduleSaveLogClick()} is executed periodically in a controlled
 * manner by the Spring framework's task scheduler. Internally, it relies on the
 * {@link LoggerQueue}, which is backed by a thread-safe queue implementation.
 * </p>
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class LoggerSchedule {

    /**
     * Logger instance specifically used for logging performance-related
     * information. This logger is configured separately from the main application
     * logger to focus on performance metrics.
     */
    private static final Logger logPerf = LoggerFactory.getLogger("perfLogger");

    /**
     * Scheduled task that runs every 3 seconds to process the log records in the
     * {@link LoggerQueue}.
     * <p>
     * This method retrieves log records from the queue, processes each record, and
     * logs the details. If an error occurs while processing a record, it is caught
     * and logged separately. After processing, the method resets the counters for
     * successful and failed log additions to the queue.
     * </p>
     */
    @Scheduled(fixedDelay = 3000)
    public void scheduleSaveLogClick() {
        long analyId = System.currentTimeMillis();
        int numSuccess = 0;
        int numFalse = 0;
        List<LoggerDTO> records = LoggerQueue.getInstance().getRecords();

        for (LoggerDTO record : records) {
            try {
                process(record);
                numSuccess++;
            } catch (Exception e) {
                numFalse++;
                log.error("Error while handling record in the queue: ", e.getMessage());
            }
        }

        LoggerQueue.getInstance().resetCount();
    }

    /**
     * Processes a single {@link LoggerDTO} record by extracting relevant
     * information and logging it.
     * <p>
     * This method handles trace IDs, IP addresses, request IDs, input arguments,
     * responses, and more. It also applies necessary truncations to the input and
     * output data to ensure they don't exceed the maximum allowed size.
     * </p>
     *
     * @param record
     *            The {@link LoggerDTO} record to be processed and logged.
     */
    private void process(LoggerDTO record) {
        if (record != null) {
            String traceId =
                    !DataUtil.isNullOrEmpty(record.getNewSpan().context().traceIdString())
                            ? record.getNewSpan().context().traceIdString()
                            : "";
            String ipAddress = null;
            String requestId = null;

            if (record.getContextRef().get() != null) {
                if (record.getContextRef().get().hasKey(ServerWebExchange.class)) {
                    ServerWebExchange serverWebExchange =
                            record.getContextRef().get().get(ServerWebExchange.class);
                    ServerHttpRequest request = serverWebExchange.getRequest();
                    ipAddress = RequestUtils.getIpAddress(request);

                    if (request != null && request.getHeaders() != null) {
                        requestId = request.getHeaders().getFirst("Request-Id");
                    }
                }
            }

            String inputs = null;
            try {
                if (record.getArgs() != null) {
                    inputs = ObjectMapperFactory.getInstance().writeValueAsString(getArgs(record.getArgs()));
                }
            } catch (Exception ex) {
                log.error("Error while processing input arguments: ", ex.getMessage());
            }

            String resStr = null;
            try {
                if (record.getResponse() instanceof Optional) {
                    Optional<?> output = (Optional<?>) record.getResponse();
                    if (output.isPresent()) {
                        resStr = ObjectMapperFactory.getInstance().writeValueAsString(output.get());
                    }
                } else if (record.getResponse() != null) {
                    resStr = ObjectMapperFactory.getInstance().writeValueAsString(record.getResponse());
                }
            } catch (Exception ex) {
                log.error("Error while processing response: ", ex.getMessage());
            }

            try {
                inputs = TruncateUtils.truncate(inputs, MAX_BYTE);
                resStr = TruncateUtils.truncate(resStr, MAX_BYTE);
            } catch (Exception ex) {
                log.error("Error while truncating input/output data: ", ex);
            }

            logInfo(new LogField(
                    traceId,
                    requestId,
                    record.getService(),
                    record.getEndTime() - record.getStartTime(),
                    record.getLogType(),
                    record.getActionType(),
                    record.getStartTime(),
                    record.getEndTime(),
                    ipAddress,
                    record.getTitle(),
                    inputs,
                    resStr,
                    record.getResult()));
        }
    }

    /**
     * Logs the given {@link LogField} object as a JSON string.
     *
     * @param logField
     *            The {@link LogField} object containing the log data.
     */
    private void logInfo(LogField logField) {
        try {
            logPerf.info(ObjectMapperFactory.getInstance().writeValueAsString(logField));
        } catch (Exception ex) {
            log.error("Error while logging performance information: ", ex.getMessage());
        }
    }

    /**
     * Extracts and returns a list of non-reactive arguments from the given array of
     * arguments.
     * <p>
     * This method filters out arguments that are instances of {@link Mono} or
     * {@link ServerWebExchange} to avoid unnecessary logging of reactive streams or
     * web exchange objects.
     * </p>
     *
     * @param args
     *            The array of arguments to be processed.
     * @return A list of processed arguments excluding {@link Mono} and
     *         {@link ServerWebExchange}.
     */
    private List<Object> getArgs(Object[] args) {
        List<Object> listArg = new ArrayList<>();

        for (Object arg : args) {
            if (arg instanceof Mono || arg instanceof ServerWebExchange) {
                // Skip reactive and web exchange arguments
                continue;
            }
            listArg.add(arg);
        }

        return listArg;
    }
}
