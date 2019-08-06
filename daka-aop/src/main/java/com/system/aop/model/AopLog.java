package com.system.aop.model;


import java.lang.annotation.*;


/**
 * 打印日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AopLog {
    /**
     * 进入方法前打印
     * @return
     */
    String before();

    /**
     * 完成方法后打印
     * @return
     */
    String after();

    /**
     * 功能模块
     * @return
     */
    String funModule();
}
