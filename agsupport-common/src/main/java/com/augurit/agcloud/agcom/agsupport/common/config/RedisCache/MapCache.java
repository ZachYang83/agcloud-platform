package com.augurit.agcloud.agcom.agsupport.common.config.RedisCache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangmingyang
 * @Description: 缓存类
 * @date 2019-06-27 14:06
 */
public class MapCache {
    public static final Map<Object,Object> cache = new ConcurrentHashMap<>();
    // 缓存Redis服务的状态
    public static final String redisIsAvaliableKey = "RedisIsAvaliable";
    /**
     * 获取redis服务心跳缓存信息
     * @param key
     * @return
     */
    public static boolean redisIsAvaliable(Object key) {
        Object value = cache.get(redisIsAvaliableKey);
        boolean flag = false;
        if (value != null){
            flag = Boolean.parseBoolean(value.toString());
        }
        return flag;
    }

    public static void put(Object value){
        cache.put(redisIsAvaliableKey,value);
    }

}
