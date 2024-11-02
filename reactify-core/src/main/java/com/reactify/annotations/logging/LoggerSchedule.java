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
package com.reactify.annotations.logging;

import static com.reactify.constants.Constants.MAX_BYTE;

import com.reactify.DataUtil;
import com.reactify.RequestUtils;
import com.reactify.TruncateUtils;
import com.reactify.factory.ObjectMapperFactory;
import com.reactify.model.logging.LogField;
import com.reactify.model.logging.LoggerDTO;
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
 * <p>
 * LoggerSchedule class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class LoggerSchedule {
    private static final Logger logPerf = LoggerFactory.getLogger("perfLogger");

    /**
     * <p>
     * scheduleSaveLogClick.
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
                log.error("Error while handle record queue: {}", e.getMessage());
            }
        }
        LoggerQueue.getInstance().resetCount();
    }

    private void process(LoggerDTO record) {
        if (record != null) {
            String traceId = !DataUtil.isNullOrEmpty(record.newSpan().context().traceIdString())
                    ? record.newSpan().context().traceIdString()
                    : "";
            String ipAddress = null;
            String requestId = null;
            if (record.contextRef().get() != null) {
                if (record.contextRef().get().hasKey(ServerWebExchange.class)) {
                    ServerWebExchange serverWebExchange =
                            record.contextRef().get().get(ServerWebExchange.class);
                    ServerHttpRequest request = serverWebExchange.getRequest();
                    ipAddress = RequestUtils.getIpAddress(request);

                    serverWebExchange.getRequest();
                    serverWebExchange.getRequest().getHeaders();
                    if (!DataUtil.isNullOrEmpty(
                            serverWebExchange.getRequest().getHeaders().getFirst("Request-Id"))) {
                        requestId = serverWebExchange.getRequest().getHeaders().getFirst("Request-Id");
                    }
                }
            }

            String inputs = null;
            try {
                if (record.args() != null) {
                    inputs = ObjectMapperFactory.getInstance().writeValueAsString(getAgrs(record.args()));
                }
            } catch (Exception ex) {
                log.error("Error while handle record queue: {}", ex.getMessage());
            }

            String resStr = null;
            try {
                if (record.response() instanceof Optional<?> output) {
                    if (output.isPresent()) {
                        resStr = ObjectMapperFactory.getInstance().writeValueAsString(output.get());
                    }
                } else {
                    if (record.response() != null) {
                        resStr = ObjectMapperFactory.getInstance().writeValueAsString(record.response());
                    }
                }
            } catch (Exception ex) {
                log.error("Error while handle record queue: {}", ex.getMessage());
            }
            try {
                inputs = TruncateUtils.truncate(inputs, MAX_BYTE);
                resStr = TruncateUtils.truncate(resStr, MAX_BYTE);
            } catch (Exception ex) {
                log.error("Truncate input/output error ", ex);
            }
            logInfo(new LogField(
                    traceId,
                    requestId,
                    record.service(),
                    record.endTime() - record.startTime(),
                    record.logType(),
                    record.actionType(),
                    record.startTime(),
                    record.endTime(),
                    ipAddress,
                    record.title(),
                    inputs,
                    resStr,
                    record.result()));
        }
    }

    private void logInfo(LogField logField) {
        try {
            logPerf.info(ObjectMapperFactory.getInstance().writeValueAsString(logField));
        } catch (Exception ex) {
            log.error("Error while handle record queue: {}", ex.getMessage());
        }
    }

    private List<Object> getAgrs(Object[] args) {
        List<Object> listArg = new ArrayList<>();
        for (Object arg : args) {
            if (arg instanceof Mono) {
                // listArg.add(((Mono) args[i]).block());
                // skip
            } else if (arg instanceof ServerWebExchange) {
                // skip
            } else {
                listArg.add(arg);
            }
        }
        return listArg;
    }
}
