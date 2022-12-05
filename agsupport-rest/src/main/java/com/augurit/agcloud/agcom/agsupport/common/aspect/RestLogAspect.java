package com.augurit.agcloud.agcom.agsupport.common.aspect;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpuOmUser;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.common.util.Common;
import eu.bitwalker.useragentutils.UserAgent;
import net.sf.json.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Aspect
@Component
public class RestLogAspect {

    private static Logger logger = LoggerFactory.getLogger(RestLogAspect.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private HttpServletRequest request;

    @Value("${spring.kafka.topic}")
    private String topic;

    @Pointcut("@annotation(com.augurit.agcloud.agcom.agsupport.common.aspect.RestLog)")
    public void restLogPointcut() {
    }


    @AfterReturning(value = "restLogPointcut()", returning = "result")
    public void afterAspect(JoinPoint joinPoint, ResultForm result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RestLog annotation = method.getAnnotation(RestLog.class);
        sendMessage(result.isSuccess(), annotation.value(), result.getMessage());
    }


    @AfterThrowing(value = "restLogPointcut()", throwing = "ex")
    public void afterTrowingAspect(JoinPoint joinPoint, Throwable ex) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RestLog annotation = method.getAnnotation(RestLog.class);
        sendMessage(false, annotation.value(), ex.getMessage());
    }

    public void sendMessage(boolean success, String funcName, String message) {
        OpuOmUser currentUser = SecurityContext.getCurrentUser();
        String result = getMessage(currentUser.getLoginName(), currentUser.getUserName(), funcName, success, message);
        CompletableFuture.runAsync(() -> {
            try {
                kafkaTemplate.send(topic, result);
            } catch (Exception e) {
                logger.error("失败:" + e.getMessage());
            }
        });
    }

    public String getMessage(String loginName,String userName,String funcName,boolean success,String exceptionMessage){
        JSONObject jsonObject = new JSONObject();
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        jsonObject.put("loginName", loginName);
        jsonObject.put("userName", userName);
        jsonObject.put("sysName", "agsupport-rest");
        jsonObject.put("ipAddress", Common.getIpAddr(request));
        jsonObject.put("browser", userAgent.getBrowser().getName());
        jsonObject.put("funcName", funcName);
        jsonObject.put("operResult", success?"成功":"失败");
        jsonObject.put("operDate", LocalDateTime.now().toString());
        jsonObject.put("restUrl",request.getServletPath());
        if (exceptionMessage != null) {
            jsonObject.put("exceptionMessage", exceptionMessage.substring(0, Math.min(exceptionMessage.length(), 500)));
        }
        return jsonObject.toString();
    }

}




