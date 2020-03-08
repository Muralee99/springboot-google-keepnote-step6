package com.stackroute.keepnote.aspectj;

/* Annotate this class with @Aspect and @Component */

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggingAspect {
	/*
     * Write loggers for each of the methods of Note controller, any particular method
     * will have all the four aspectJ annotation
     * (@Before, @After, @AfterReturning, @AfterThrowing).
     */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Before("execution(* com.stackroute.keepnote.controller.*Controller.*(..))")
	public void logBefore(JoinPoint joinPoint) {
		log.info(":::::AOP logBefore call:::::" + joinPoint);
	}

	@After("execution(* com.stackroute.keepnote.controller.*.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		log.info(":::::AOP logAfter call:::::" + joinPoint);
	}

	@AfterReturning(
			pointcut = "execution(* com.stackroute.keepnote.controller.*.*(..))",
			returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		log.info(":::::AOP logAfterReturning call:::::" + joinPoint);
	}

	@AfterThrowing(
			pointcut = "com.stackroute.keepnote.controller.*.*(..))")
	public void logAfterThrowing(JoinPoint joinPoint, Object result) {
		log.info(":::::AOP logAfterThrowing call:::::" + joinPoint);
	}
}