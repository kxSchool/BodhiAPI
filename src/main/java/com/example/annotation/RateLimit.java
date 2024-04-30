package com.example.annotation;

import java.lang.annotation.*;

/**
 * @author Toomth
 * @date 2021/2/19 14:33
 * @explain
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    double limitNum() default 20;  //默认每秒放入桶中的token
}
