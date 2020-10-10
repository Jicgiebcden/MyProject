package com.huangym.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义类型注解，用于标识是一个开发人员自定义的类
 * @author huangym3
 * @time 2016年7月5日 下午1:01:14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DefineTypeAnnotation {
	
	String value();
}