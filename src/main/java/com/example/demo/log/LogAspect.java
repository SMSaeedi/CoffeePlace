package com.example.demo.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {

    @Around("execution(* com.example.demo.dao.*.*(..))")
    public Object dao(ProceedingJoinPoint jp) {
        Object result = null;
        String logInfo = "";

        LogInfo.logger.info("before dao call " + logInfo);

        try {
            result = jp.proceed();
            LogInfo.logger.info("after dao call " + logInfo + " Method returned value is : " + result);
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                LogError.logger.error("Error Connection Doa " + logInfo + " errorDesc: " + e.getMessage());
            }
        }
        return result;
    }

    @Around("execution(* com.example.demo.service.*.*(..))")
    public Object service(ProceedingJoinPoint jp) {
        Object result = null;
        String logInfo = "";

        LogInfo.logger.info("before service call " + logInfo);

        try {
            result = jp.proceed();
            LogInfo.logger.info("after service call " + logInfo + " Method returned value is : " + result);
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                LogError.logger.error("Service exception " + logInfo + " errorDesc: " + e.getMessage());
            } else {
                LogError.logger.error("Service exception " + logInfo + " errorDesc: " + e.getMessage());
            }
        }
        return result;
    }
}