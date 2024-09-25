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
package io.hoangtien2k3.reactify.annotations.logging;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * LoggerAspect class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Aspect
@Configuration
@RequiredArgsConstructor
public class LoggerAspect {
    private final LoggerAspectUtils loggerAspectUtils;

    /**
     * <p>
     * performancePointCut.
     * </p>
     */
    @Pointcut("(execution(* io.hoangtien2k3.reactify.*.controller..*(..)) || "
            + "execution(* io.hoangtien2k3.reactify.*.service..*(..))  ||  "
            + "execution(* io.hoangtien2k3.reactify.*.repository..*(..)) || "
            + "execution(* io.hoangtien2k3.reactify.*.client..*(..))) &&"
            + "!execution(* org.springframework.boot.actuate..*(..))")
    public void performancePointCut() {}

    @Pointcut("@annotation(io.hoangtien2k3.reactify.annotations.LogPerformance)")
    private void logPerfMethods() {}

    /**
     * <p>
     * logAround.
     * </p>
     *
     * @param joinPoint
     *            a {@link org.aspectj.lang.ProceedingJoinPoint} object
     * @return a {@link java.lang.Object} object
     * @throws java.lang.Throwable
     *             if any.
     */
    @Around("performancePointCut() || logPerfMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return loggerAspectUtils.logAround(joinPoint);
    }
}
