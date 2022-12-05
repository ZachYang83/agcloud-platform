package com.augurit.agcloud.agcom.agsupport.common.config.condition;

import com.common.util.Common;
import com.common.util.ConfigProperties;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 类的描述
 *
 * @author Hunter
 * @Time 2017/6/15
 */
public class RedisCondition implements Condition {

    public static boolean matches() {
        return !Common.isCheckNull(ConfigProperties.getByKey("spring.redis.host"));
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String serverUrl = context.getEnvironment().getProperty("spring.redis.host");
        return !Common.isCheckNull(serverUrl);
    }
}
