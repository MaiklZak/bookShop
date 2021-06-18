package com.example.MyBookShopApp.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

@Component
@Aspect
public class LoggingServicesAspect {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Around(value = "@annotation(com.example.MyBookShopApp.aspects.annotations.LoggingBookServiceGetMethodsWithArg) && args(arg)")
    public Object loggingBookServiceMethodsWithArgAdvice(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
        long start = new Date().getTime();
        logger.info("attempt get books with "  + joinPoint.toShortString() + " and arg = " + arg);

        Object result = joinPoint.proceed();

        long runTime = new Date().getTime() - start;
        logger.info(joinPoint.toShortString() + " took " + runTime + " mills");
        return result;
    }

    @Around(value = "@annotation(com.example.MyBookShopApp.aspects.annotations.LoggingBookServiceGetMethods)")
    public Object loggingBookServiceMethodsAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = new Date().getTime();
        logger.info("attempt get books with "  + joinPoint.toShortString());

        Object result = joinPoint.proceed();

        long runTime = new Date().getTime() - start;
        logger.info(joinPoint.toShortString() + " took " + runTime + " mills");
        return result;
    }

    @AfterReturning(value = "@annotation(com.example.MyBookShopApp.aspects.annotations.LoggingResourceStorageMethods)")
    public void loggingResourceStorageMethodsAdvice(JoinPoint joinPoint) {
        logger.info(joinPoint.toShortString() + " with arg " + Arrays.toString(joinPoint.getArgs()) + " completed successfully");
    }

    @AfterThrowing(value = "@annotation(com.example.MyBookShopApp.aspects.annotations.LoggingResourceStorageMethods)", throwing = "ex")
    public void loggingResourceStorageMethodsAdvice(JoinPoint joinPoint, Exception ex) {
        logger.info(joinPoint.toShortString() + " with arg " + Arrays.toString(joinPoint.getArgs()) + " failed: " + ex.getLocalizedMessage());
    }

    @Around(value = "@annotation(com.example.MyBookShopApp.aspects.annotations.LoggingAuthorService)")
    public Object loggingAuthorServiceMethodsAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = new Date().getTime();
        logger.info("attempt get authors with "  + joinPoint.toShortString());

        Object result = joinPoint.proceed();

        long runTime = new Date().getTime() - start;
        logger.info(joinPoint.toShortString() + " took " + runTime + " mills");
        return result;
    }
}
