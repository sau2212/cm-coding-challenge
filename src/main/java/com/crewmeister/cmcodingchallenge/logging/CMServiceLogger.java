package com.crewmeister.cmcodingchallenge.logging;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Aspect
@Component
@ConditionalOnProperty(value = "enable.service.logging", havingValue = "true", matchIfMissing = false)
public class CMServiceLogger {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CMLoggingInterceptor.class);

    @Around("execution(* com.crewmeister.cmcodingchallenge.util.*.*(..)) || execution(* com.crewmeister.cmcodingchallenge.service.impl.*.*(..))")
    public Object logExecutionTime(org.aspectj.lang.ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getName();
        String methodName = methodSignature.getName();

        logger.info("Started Executing {}.{}", className, methodName);
        long startTime = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        logger.info("Completed executing {}.{} in {}ms", className, methodName, duration);

        return proceed;
    }

}
