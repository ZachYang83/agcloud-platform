package com.augurit.agcloud.agcom.syslog.config;

import com.augurit.agcloud.agcom.syslog.domain.CommonSysLog;
import com.augurit.agcloud.agcom.syslog.service.annotation.SysLog;
import com.augurit.agcloud.agcom.syslog.service.service.CommonSysLogService;
import com.augurit.agcloud.framework.security.SecurityContext;
import eu.bitwalker.useragentutils.UserAgent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

/**
 * @author zhangmy
 * @Description: 日志切面类
 * @date 2019-10-30 10:01
 */
@Aspect
@Component
public class SysLogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysLogAspect.class);

    @Autowired
    private CommonSysLogService commonSysLogService;
    /**
     * 定义切入点，需要通配所有的路径,配置Controller下的类或者service的impl下的类，并且有记录日志注解
     */
    @Pointcut("(execution(public * com.augurit.agcloud..controller..*(..)) || execution(public * com.augurit.agcloud..impl..*(..))) && @annotation(com.augurit.agcloud.agcom.syslog.service.annotation.SysLog)")
    public void sysLog(){

    }

    @Around("sysLog()")
    public Object  doAround(ProceedingJoinPoint  joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodSignatureName = methodSignature.getName();
        Method method = methodSignature.getMethod();
        // 获取方法的日志注解
        SysLog sysLogAnno = method.getAnnotation(SysLog.class);
        // 存在则需要记录日志信息
        CommonSysLog commonSysLog = new CommonSysLog();
        if (sysLogAnno != null){
            String sysName = sysLogAnno.sysName();
            String funcName = sysLogAnno.funcName();
            String remark = sysLogAnno.remark();
            String extendData = sysLogAnno.extendData();
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            String remoteAddr = request.getRemoteAddr();
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            String browserName = userAgent.getBrowser().getName();
            String currentUserName = "";
            String loginName = "";
            try {
                loginName = SecurityContext.getCurrentUserName();
                currentUserName = SecurityContext.getOpusLoginUser().getUser().getUserName();
            }catch (Exception e){
                currentUserName = "匿名用户";
                loginName = "anonymous";
            }
            commonSysLog.setBrowser(browserName);
            commonSysLog.setExceptionMessage("");
            commonSysLog.setExtendData(extendData);
            commonSysLog.setFuncName(funcName);
            commonSysLog.setRemark(remark);
            commonSysLog.setIpAddress(remoteAddr);
            commonSysLog.setSysName(sysName);
            //commonSysLog.setId(UUID.randomUUID().toString());
            commonSysLog.setUserName(currentUserName);
            commonSysLog.setOperDate(new Date());
            commonSysLog.setType("info");
            commonSysLog.setLoginName(loginName);
            Object proceed = null;
            try {
                proceed = joinPoint.proceed();
                commonSysLog.setOperResult("成功");
            }catch (Throwable e){
                String message = e.getMessage();
                commonSysLog.setExceptionMessage(message);
                commonSysLog.setType("error");
                commonSysLog.setOperResult("失败");
                LOGGER.error("请求异常：请求方法：{}, 异常信息：{}",methodSignatureName,e.getMessage());

            }
            commonSysLogService.saveToMongodb(commonSysLog,"ag-sys-log");
            return proceed;
        }else {
            return joinPoint.proceed();
        }

    }
}
