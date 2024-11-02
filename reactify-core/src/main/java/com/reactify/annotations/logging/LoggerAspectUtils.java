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

import brave.Span;
import brave.Tracer;
import com.reactify.DataUtil;
import com.reactify.annotations.LogPerformance;
import com.reactify.exception.BusinessException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * <p>
 * LoggerAspectUtils class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
@RequiredArgsConstructor
public class LoggerAspectUtils {

    private static final Logger logPerf = LoggerFactory.getLogger("perfLogger");
    private static final Logger log = LoggerFactory.getLogger("LoggerAspect");

    private final Tracer tracer;

    @Value("${debug.detailException:true}")
    private boolean detailException;

    @PostConstruct
    private void init() {}

    /**
     * <p>
     * logAround.
     * </p>
     *
     * @param joinPoint
     *            a {@link ProceedingJoinPoint} object
     * @return a {@link Object} object
     * @throws Throwable
     *             if any.
     */
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogPerformance logPerformance = method.getAnnotation(LogPerformance.class);

        long start = System.currentTimeMillis();
        String name = joinPoint.getTarget().getClass().getSimpleName() + "."
                + joinPoint.getSignature().getName();

        String logType = joinPoint.getTarget().getClass().getName();
        String actionType = joinPoint.getTarget().getClass().getSimpleName();
        boolean logOutput = true;
        boolean logInput = true;
        String title = null;
        if (logPerformance != null) {
            if (!DataUtil.isNullOrEmpty(logPerformance.logType())) {
                logType = logPerformance.logType();
            }
            if (!DataUtil.isNullOrEmpty(logPerformance.actionType())) {
                actionType = logPerformance.actionType();
            }
            logOutput = logPerformance.logOutput();
            logInput = logPerformance.logInput();
            title = logPerformance.title();
        }

        Span newSpan = tracer.nextSpan().name(name);

        var result = joinPoint.proceed();
        if (result instanceof Mono) {
            return logMonoResult(
                    joinPoint, start, (Mono<?>) result, newSpan, name, logType, actionType, logOutput, logInput, title);
        }
        if (result instanceof Flux) {
            return logFluxResult(
                    joinPoint, start, (Flux<?>) result, newSpan, name, logType, actionType, logOutput, logInput, title);
        } else {
            return result;
        }
    }

    private Mono<?> logMonoResult(
            ProceedingJoinPoint joinPoint,
            long start,
            Mono<?> result,
            Span newSpan,
            String name,
            String logType,
            String actionType,
            boolean logOutput,
            boolean logInput,
            String title) {
        var contextRef = new AtomicReference<Context>();
        return result.doOnSuccess(o -> {
                    Object[] args = null;
                    if (logInput) {
                        args = joinPoint.getArgs();
                    }
                    if (logOutput) {
                        logPerf(contextRef, newSpan, name, start, "0", o, logType, actionType, args, title);
                    } else {
                        logPerf(contextRef, newSpan, name, start, "0", null, logType, actionType, args, title);
                    }
                })
                .contextWrite(context -> {
                    contextRef.set((Context) context);
                    return context;
                })
                .doOnError(o -> {
                    if (detailException) log.error(" ", o);
                    else log.error(o.toString());

                    if (o instanceof BusinessException) {
                        logPerf(contextRef, newSpan, name, start, "0", o, logType, actionType, null, title);
                    } else {
                        logPerf(contextRef, newSpan, name, start, "1", o, logType, actionType, null, title);
                    }
                });
    }

    private Flux<?> logFluxResult(
            ProceedingJoinPoint joinPoint,
            long start,
            Flux<?> result,
            Span newSpan,
            String name,
            String logType,
            String actionType,
            boolean logOutput,
            boolean logInput,
            String title) {
        var contextRef = new AtomicReference<Context>();
        return result.doFinally(o -> {
                    logPerf(contextRef, newSpan, name, start, "1", null, logType, actionType, null, title);
                })
                .contextWrite(context -> {
                    contextRef.set((Context) context);
                    return context;
                })
                .doOnError(o -> {
                    logPerf(contextRef, newSpan, name, start, "0", o, logType, actionType, null, title);
                });
    }

    private void logPerf(
            AtomicReference<Context> contextRef, Span newSpan, String name, Long start, String result, Object o) {
        newSpan.finish();
        long duration = System.currentTimeMillis() - start;
        if (duration < 50) return;
        logPerf.info("{} {} {} M2 {}", name, duration, result, o == null ? "-" : o.toString());
    }

    private void logPerf(
            AtomicReference<Context> contextRef,
            Span newSpan,
            String name,
            Long startTime,
            String result,
            Object obj,
            String logType,
            String actionType,
            Object[] args,
            String title) {
        newSpan.finish();
        long endTime = System.currentTimeMillis();
        if (endTime - startTime > 50) {
            LoggerQueue.getInstance()
                    .addQueue(
                            contextRef,
                            newSpan,
                            name,
                            startTime,
                            endTime,
                            result,
                            obj,
                            logType,
                            actionType,
                            args,
                            title);
        }
    }
}
