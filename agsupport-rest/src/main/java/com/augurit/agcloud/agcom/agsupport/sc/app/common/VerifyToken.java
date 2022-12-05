package com.augurit.agcloud.agcom.agsupport.sc.app.common;

import com.augurit.agcloud.framework.security.user.OpusLoginUser;
import com.augurit.agcloud.framework.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.UnsupportedEncodingException;

/**
 * @Auther: zhangmingyang
 * @Date: 2018/9/30 11:15
 * @Description: 验证token
 */
public class VerifyToken {

    /**
     * 验证token
     * @param token
     * @return
     */
    public static Claims verifyToken(String token){
        if (StringUtils.isBlank(token)){
            return null;
        }
        if (token.contains("bearer")){
            token = token.substring(7);
        }
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey("agcloudTokenKey88".getBytes("UTF-8")).parseClaimsJws(token).getBody();
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }
        return claims;
    }

    /**
     * 根据token获取当前用户
     * @param claims
     * @return
     */
    public static OpusLoginUser getOpusLoginUser(Claims claims){
        Object object = claims.get("opusLoginUser");
        ObjectMapper objectMapper = new ObjectMapper();
        OpusLoginUser opusLoginUser = objectMapper.convertValue(object, OpusLoginUser.class);
        return  opusLoginUser;
    }
}
