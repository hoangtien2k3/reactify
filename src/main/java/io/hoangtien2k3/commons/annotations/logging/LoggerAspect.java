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
