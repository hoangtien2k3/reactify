package io.hoangtien2k3.commons.model.logging;

import brave.Span;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.util.context.Context;

import java.util.concurrent.atomic.AtomicReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggerDTO {
    AtomicReference<Context> contextRef;
    Span newSpan;
    String service;
    Long startTime;
    Long endTime;
    String result;
    Object response;
    String logType;
    String actionType;
    Object[] args;
    String title;
}
