package com.homestay.homestay.config.aop;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 张喜龙
 * @Date: 2022/11/24/15:19
 * @Description:
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";
}
