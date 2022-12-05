package com.augurit.agcloud.agcom.agsupport.common.aspect;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Component
public @interface RestLog {
    String value() default "";
}