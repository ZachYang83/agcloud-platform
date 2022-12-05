package com.augurit.agcloud.agcom.agsupport.common.util;

import com.augurit.agcloud.agcom.agsupport.common.config.condition.RedisCondition;
import com.common.util.RedisMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangzq on 2018-05-09.
 */
public class RedisAdaptor {

    private static Map redis;

    static {
        if (RedisCondition.matches()) {
            redis = new RedisMap();
        } else {
            redis = new HashMap();
        }
    }

    public static Map getRedis() {
        return redis;
    }
}
