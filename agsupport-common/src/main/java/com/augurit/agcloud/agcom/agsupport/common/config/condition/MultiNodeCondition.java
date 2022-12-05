package com.augurit.agcloud.agcom.agsupport.common.config.condition;

import com.common.util.Common;
import com.common.util.ConfigProperties;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 多节点部署条件判断
 *
 * @author chendingxing
 */
public class MultiNodeCondition implements Condition {

    public static boolean isMainServer = false;
    public static String mainServer = null;

    public static boolean matches() {
        mainServer = ConfigProperties.getByKey("main.server");
        boolean isMultiNode = !Common.isCheckNull(mainServer);
        if(isMultiNode){
            String port = ConfigProperties.getByKey("server.port");
            if(mainServer.contains(port)){
                isMainServer = true;
            }
        }
        return isMultiNode && (!isMainServer);
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String port = context.getEnvironment().getProperty("server.port");
        mainServer = context.getEnvironment().getProperty("main.server");
        if(mainServer.contains(port)){
            isMainServer = true;
        }
        return !Common.isCheckNull(mainServer);
    }
}
