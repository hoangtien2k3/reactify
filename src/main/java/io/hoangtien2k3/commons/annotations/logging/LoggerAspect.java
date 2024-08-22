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

/**
 * This aspect class is responsible for logging the performance of methods in
 * the specified packages. It uses AOP (Aspect-Oriented Programming) to
 * intercept method calls and log relevant information before and after the
 * method execution.
 *
 * <p>
 * The class uses Spring AOP and relies on two pointcuts to identify which
 * methods should be logged:
 * <ul>
 * <li>{@code performancePointCut}: Matches methods in the specified packages
 * (controller, service, repository, client) except for those in the Spring Boot
 * Actuator package.</li>
 * <li>{@code logPerfMethods}: Matches methods annotated with
 * {@link io.hoangtien2k3.commons.annotations.LogPerformance}.</li>
 * </ul>
 *
 * <p>
 * The {@code @Around} advice is applied to the methods matched by these
 * pointcuts, and it delegates the logging logic to {@link LoggerAspectUtils}.
 *
 * <p>
 * Example of usage:
 *
 * <pre>
 * {@code
 * @LogPerformance
 * public void someMethod() {
 * 	// method implementation
 * }
 * }
 * </pre>
 *
 * <p>
 * The class is annotated with {@code @Aspect} to define it as an aspect, and
 * {@code @Configuration} to ensure it is registered as a Spring bean. It also
 * uses {@code @RequiredArgsConstructor} from Lombok to generate a constructor
 * that injects the required dependencies.
 *
 * @author hoangtien2k3
 */
@Aspect
@Configuration
@RequiredArgsConstructor
public class LoggerAspect {

    private final LoggerAspectUtils loggerAspectUtils;

    /**
     * Pointcut that matches the execution of any method within the specified
     * packages (controller, service, repository, client) except for those under the
     * Spring Boot Actuator package.
     */
    @Pointcut("(execution(* io.hoangtien2k3.commons.*.controller..*(..)) || "
            + "execution(* io.hoangtien2k3.commons.*.service..*(..))  ||  "
            + "execution(* io.hoangtien2k3.commons.*.repository..*(..)) || "
            + "execution(* io.hoangtien2k3.commons.*.client..*(..))) &&"
            + "!execution(* org.springframework.boot.actuate..*(..))")
    public void performancePointCut() {}

    /**
     * Pointcut that matches methods annotated with
     * {@link io.hoangtien2k3.commons.annotations.LogPerformance}.
     */
    @Pointcut("@annotation(io.hoangtien2k3.commons.annotations.LogPerformance)")
    private void logPerfMethods() {}

    /**
     * Around advice that wraps the matched method executions, providing logging
     * functionality around the method invocation. The actual logging logic is
     * delegated to {@link LoggerAspectUtils#logAround}.
     *
     * @param joinPoint
     *            the join point representing the method execution
     * @return the result of the method execution
     * @throws Throwable
     *             if the method execution throws an exception
     */
    @Around("performancePointCut() || logPerfMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return loggerAspectUtils.logAround(joinPoint);
    }
}
