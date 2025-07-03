package com.example.login.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    @Pointcut("within(com.example.login.service..*)")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("[AOP] Entering: {}.{}({})", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), argsToString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        logger.info("[AOP] Exiting: {}.{}(...) => {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), result);
    }

    private String argsToString(Object[] args) {
        if (args == null || args.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(arg);
        }
        return sb.toString();
    }
} 