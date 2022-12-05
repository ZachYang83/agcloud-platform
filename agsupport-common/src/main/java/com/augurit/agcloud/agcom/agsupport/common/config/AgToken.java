package com.augurit.agcloud.agcom.agsupport.common.config;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.common.config.RedisCache.MapCache;
import com.augurit.agcloud.agcom.agsupport.common.config.RedisCache.RedisKeyConstant;
import com.augurit.agcloud.agcom.agsupport.common.config.condition.RedisCondition;
import com.augurit.agcloud.agcom.agsupport.util.HttpClientUtil;
import com.augurit.agcloud.framework.util.Md5Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangmingyang
 * @Description: 获取token
 * @date 2019-07-16 15:49
 */
@Component
public class AgToken {
    @Value("${security.oauth2.client.clientId}")
    private String clientId;
    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;
    @Value("${agcloud.framework.sso.sso-server-url}")
    private String ssoServerUrl;
    @Value("${rest-token-username}")
    private String restTokenUsername;
    @Value("${rest-token-password}")
    private String restTokenPassword;
    @Value("${agcloud.opus.admin.orgId}")
    private String orgId;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String getToken(String username,String password) {
        String url = ssoServerUrl + "/authentication/form";
        Map map = new HashMap();
        map.put("username",username);
        map.put("password",password);
        map.put("orgId",orgId);
        map.put("deviceType","normal");
        map.put("isApp","true");
        Base64.Encoder encoder = Base64.getEncoder();
        String base = clientId + ":" +clientSecret;
        String s = encoder.encodeToString(base.getBytes());
        Map header = new HashMap();
        header.put("Content-Type","application/x-www-form-urlencoded");
        // opus-rest 目前固定值 Basic b3B1cy1yZXN0Om9wdXMtcmVzdDEyMw==
        header.put("Authorization","Basic b3B1cy1yZXN0Om9wdXMtcmVzdDEyMw==");
        String result = HttpClientUtil.post(url, map, header, "utf-8");
        JSONObject jsonObject = JSONObject.parseObject(result);
        Object success = jsonObject.get("success");
        String token = "";
        if (success != null && Boolean.valueOf(success.toString())){
            JSONObject content = jsonObject.getJSONObject("content");
            token = content.get("access_token").toString();
            int expires_in = content.getInteger("expires_in");
            // token 存入redis
            if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)){
                stringRedisTemplate.opsForValue().set(RedisKeyConstant.TOKENKEY,token,expires_in-120, TimeUnit.SECONDS);
            }
        }
        return token;
    }

    /**
     * 获取token
     * @return
     */
    public String checkToken() {
        String token = "";
        if (checkRedisIsAvailable()){
            token = stringRedisTemplate.opsForValue().get(RedisKeyConstant.TOKENKEY);
            if (StringUtils.isNotEmpty(token)){
                //去掉双引号
                token = token.replaceAll("\"", "");
            }
        }
        if (StringUtils.isBlank(token)){
            token = getToken(restTokenUsername, Md5Utils.hash(restTokenPassword));
        }
        return token;
    }

    /**
     * redis 是否可用
     * @return true可用，false不可用
     */
    boolean checkRedisIsAvailable(){
        try{
            //返回值是否失去连接，false可用，true不可用
            return !stringRedisTemplate.isExposeConnection();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
