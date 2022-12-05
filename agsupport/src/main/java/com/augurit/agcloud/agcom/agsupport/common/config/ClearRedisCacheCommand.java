package com.augurit.agcloud.agcom.agsupport.common.config;

import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgDataTrans;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author zhangmingyang
 * @Description: 启动清空redis缓存
 * @date 2019-05-22
 */
@Component
public class ClearRedisCacheCommand implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClearRedisCacheCommand.class);
    @Autowired
    private IAgDataTrans iAgDataTrans;
    @Value("${spring.redis.host}")
    private String redisHost;
    @Override
    public void run(String... args) {
        if (StringUtils.isNotBlank(redisHost)) {
            boolean flag = iAgDataTrans.clearRedisCache("");
            if (flag){
                LOGGER.info("已成功清空redis缓存!");
            }
        }
    }
}
