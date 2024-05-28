package ru.alex.mscalc.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before(value = "loggingExecutionAllMethod()")
    public void logBeforeExecuting(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Start executing: {}()", methodName);
    }


    @AfterThrowing(pointcut = "logException()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.error("{}() - {}", methodName, exception.getMessage());
    }

    @AfterReturning(pointcut = "controllerMethodLogging()")
    public void logReturningResult(JoinPoint joinPoint) {
        log.info("Returning result: {}", joinPoint.getArgs());
    }


    @Pointcut("execution(* ru.alex.mscalc.service.*.*(..))")
    private void loggingExecutionAllMethod() {}

    @Pointcut("execution(public * ru.alex.mscalc.web.api.CalculatorApi.*(..))")
    private void controllerMethodLogging() {}


    @Pointcut("execution(* ru.alex.mscalc.service.*.*(..))")
    private void logException() {}
}
