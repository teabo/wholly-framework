package com.whollyframework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明的一个 POJO 属性忽略，在调用参数或方法时，忽略。
 * 
 * @author Chris Hsu
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Documented
public @interface Ignore {

	boolean value() default true;

}
