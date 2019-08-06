package com.system.aop.proxy;


import com.system.aop.model.AopLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;

@Configuration
@Aspect
public class AopLogProxy {
    private static Logger log = Logger.getLogger(AopLogProxy.class.getName());
    @Around("@annotation(com.system.aop.model.AopLog)")
    public Object printLog(ProceedingJoinPoint point) throws Throwable {

        MethodSignature signature = (MethodSignature) point.getSignature();
        //获取被代理的方法名
        Method method = signature.getMethod();
        AopLog annotation = method.getAnnotation(AopLog.class);
        log.info("<------------ 功能模块:"+annotation.funModule()+" ------------->");
        log.info("类名称:"+ signature.getDeclaringTypeName());
        log.info("方法名:"+ method.getName());
        log.info("参数值:" + Arrays.toString(point.getArgs()));
        log.info("执行前:" +annotation.before());
        Long startTime = System.currentTimeMillis();
        Object proceed = point.proceed();
        Long endTime = System.currentTimeMillis();
        log.info("返回值："+ proceed);
        log.info("总用时："+(endTime-startTime)+"毫秒");
        log.info("执行后:" + annotation.after());
        return proceed;
    }
} 