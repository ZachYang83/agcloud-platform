package com.augurit.agcloud.agcom.syslog.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangmy
 * @Description: 系统日志注解类
 * @date 2019-10-28 16:24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysLog {
    /**
     *系统名称
     */
    String sysName() default "";
    /**
     * 操作功能名称
     */
    String funcName() default "";
    /**
     * 描述信息
     * @return
     */
    String remark() default "";
    /**
     * 扩展字段json格式
     */
    String extendData() default "";
}
