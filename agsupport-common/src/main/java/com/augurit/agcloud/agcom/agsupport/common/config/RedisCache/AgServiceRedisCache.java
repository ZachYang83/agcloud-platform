package com.augurit.agcloud.agcom.agsupport.common.config.RedisCache;

import com.augurit.agcloud.agcom.agsupport.common.config.condition.RedisCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * @author zhangmingyang
 * @Description: Redis检测类
 * @date 2019-06-27 17:33
 */
@Component
public class AgServiceRedisCache {
    private static Logger logger = LoggerFactory.getLogger(AgServiceRedisCache.class);
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;
    @Value("${spring.redis.password}")
    private String password;
    /**
     * 检查redis是否存活
     * @return
     */
    public boolean checkRedisIsAvaliable() {
        boolean result = false;
        Jedis jedis = null;
        if (RedisCondition.matches()){
            try {
                //连接本地Redis服务
                jedis = new Jedis(redisHost, redisPort);
                jedis.auth(password);//密码
                String ping = jedis.ping();
                if (ping.equalsIgnoreCase("PONG")) {
                    result = true;
                }
            } catch (Exception e) {
                logger.error("Redis缓存服务异常");
                result = false;
            }finally {
                // 释放连接资源
                if (jedis != null){
                    jedis.close();
                }
            }
        }
        return result;
    }
}
