/*
 * Copyright 2024 author - Hoàng Anh Tiến
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 */
package io.hoangtien2k3.commons.annotations.logging;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@RequiredArgsConstructor
public class LoggerAspect {
    private final LoggerAspectUtils loggerAspectUtils;

    @Pointcut("(execution(* io.hoangtien2k3.commons.*.controller..*(..)) || "
            + "execution(* io.hoangtien2k3.commons.*.service..*(..))  ||  "
            + "execution(* io.hoangtien2k3.commons.*.repository..*(..)) || "
            + "execution(* io.hoangtien2k3.commons.*.client..*(..))) &&"
            + "!execution(* org.springframework.boot.actuate..*(..))")
    public void performancePointCut() {}

    @Pointcut("@annotation(io.hoangtien2k3.commons.annotations.LogPerformance)")
    private void logPerfMethods() {}

    @Around("performancePointCut() || logPerfMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return loggerAspectUtils.logAround(joinPoint);
    }
}
