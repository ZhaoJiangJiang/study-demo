package com.study.demo.commonswitch.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通用注解开关
 */
@Target({ElementType.METHOD})       // 作用在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时起作用
public @interface ServiceSwitch {

    /**
     * 业务开关的key
     * 0 代表关
     * 1 代表开
     */
    String switchKey();

    // 提示信息 默认值可在使用注解时自行定义
    String message() default "当前开关关闭，请打开开关后重试";
}
