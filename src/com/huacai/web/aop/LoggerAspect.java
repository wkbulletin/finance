package com.huacai.web.aop;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 日志切入类
 * 
 * @author EumJi
 * @package com.eumji.zblog.config
 * @name LoggerAspect
 * @date 2017/4/10
 * @time 18:08
 */
@Aspect
@Component
@Order(5)
public class LoggerAspect {
	private Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

	@Pointcut("execution(* com.huacai.web.controller.task.*.*(..))")
	public void exceptionLog() {
	}

	@Before("execution(* com.huacai.web.*.*(..))")
	public void authority() {
		System.out.println("模拟执行权限检查");
	}

	@AfterReturning(returning = "rvt", pointcut = "exceptionLog()")
	// 声明rvt时指定的类型会限制目标方法必须返回指定类型的值或没有返回值
	// 此处将rvt的类型声明为Object，意味着对目标方法的返回值不加限制
	public void log(Object rvt) {
		System.out.println("获取目标方法返回值:" + rvt);
		System.out.println("模拟记录日志功能...");
	}

	/**
	 * 异常切入方法 记录异常到数据库
	 * 
	 * @param joinPoint
	 * @param e
	 */
	@AfterThrowing(pointcut = "exceptionLog()", throwing = "e")
	public void afterThrowing(JoinPoint joinPoint, Throwable e) {
		LogInfo log = new LogInfo();
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

		HttpServletRequest request = requestAttributes.getRequest();
		log.setIp(request.getRemoteAddr());
		log.setMethod(request.getMethod());
		log.setUrl(request.getRequestURL().toString());
		log.setArgs(Arrays.toString(joinPoint.getArgs()));
		log.setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
		log.setException(e.getMessage());
		log.setOperateTime(new Date());
		logger.info(log.toString());
	}
}
