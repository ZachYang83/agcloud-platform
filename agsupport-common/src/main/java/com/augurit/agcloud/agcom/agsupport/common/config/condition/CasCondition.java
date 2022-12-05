package com.augurit.agcloud.agcom.agsupport.common.config.condition;

import com.common.util.Common;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 单点登录条件判断
 *
 * @author Hunter
 */
public class CasCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String casServerValidateUrl = Common.getByKey("server.cas-server-validate-url");
        String casServerPublicUrl = Common.getByKey("server.cas-server-public-url");
        String casClientIp = Common.getByKey("server.cas-client-ip");
        return !(Common.isCheckNull(casServerValidateUrl) && Common.isCheckNull(casServerPublicUrl) && Common.isCheckNull(casClientIp));
    }
}
