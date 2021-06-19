package com.example.MyBookShopApp.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class LoggingControllersAspect {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Pointcut(value = "within(com.example.MyBookShopApp.controllers.* || com.example.MyBookShopApp.security.AuthUserController)" +
            " && execution(public String *(..))")
    public void controllersMethodsThatReturnStringPageViewPointcut() {
    }

    @Pointcut(value = "execution(public String handleRemoveBookFromCartRequest(..)) || execution(public String handleChangeBookStatus(..))")
    public void bookControllerchangeBookStatusPointcut() {
    }

    @AfterReturning(pointcut = "controllersMethodsThatReturnStringPageViewPointcut()", returning = "page")
    public void controllersMethodsThatReturnStringPageViewAdvice(JoinPoint joinPoint, String page) {
        logger.info(joinPoint.toShortString() + " was invoked and returned page: " + page);
    }

    @AfterReturning(pointcut = "args(slug, ..) && bookControllerchangeBookStatusPointcut()")
    public void bookControllerchangeBookStatusAdvice(JoinPoint joinPoint, String slug) {
        logger.info(joinPoint.toShortString() + " was invoked and book status with slug: " + slug + " changed");
    }

    @AfterThrowing(pointcut = "bookControllerchangeBookStatusPointcut() || controllersMethodsThatReturnStringPageViewPointcut()", throwing = "ex")
    public void bookControllerchangeBookStatusAdvice(JoinPoint joinPoint, Exception ex) {
        logger.info("During " + joinPoint.toShortString() + " was threw exception: " + ex.getLocalizedMessage());
    }


}
