package com.augurit.agcloud.agcom.agsupport.sc.system.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.sc.system.service.ISystem;
import com.augurit.agcloud.agcom.agsupport.sc.system.util.DomainCheck;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.common.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hunter on 2017-11-10.
 */
@Service
public class SystemImpl implements ISystem {

    @Autowired
    private IAgUser iAgUser;

    private Map tokenCache;

    public SystemImpl() {
        tokenCache = new RedisMap("augur_token_check", Common.checkInt(Common.getByKey("redis.expire", "60")));
    }

    /**
     * 验证token，有效1次
     *
     * @param token
     * @return
     */
    public boolean checkToken(String token) {
        if (Common.isCheckNull(token)) return false;
        if (tokenCache.containsKey(token)) {
            tokenCache.remove(token);
            return true;
        }
        return false;
    }

    public String getToken(String loginName) {
        if (!hasUserByName(loginName)) return null;
        String token = Md5Utils.encrypt16(UUID.randomUUID().toString());
        tokenCache.put(token, System.currentTimeMillis());
        return token;
    }

    /**
     * 判断是否查询到有用户 默认60秒刷新查询结果
     *
     * @return
     */
    private boolean hasUserByName(String loginName) {
        if (Common.isCheckNull(loginName)) return false;
        String obj = (String) tokenCache.get(loginName);
        if (Common.isCheckNull(obj)) {
            AgUser o = getUserByLoginName(loginName);
            boolean result = o != null && o.getId() != null;
            tokenCache.put(loginName, result);
            return result;
        }
        return "true".equalsIgnoreCase(obj);
    }

    private AgUser getUserByLoginName(String loginName) {
        try {
            AgUser user = iAgUser.findUserByName(loginName);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过验证的返回true
     *
     * @param request
     * @return
     */
    public boolean check(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        //本地白名单check
        if (DomainCheck.check(request)) {
            return true;
        }
        //解析参数
        switch (parse(request, response)) {
            case 0:
                return true;
            case 1:
                return false;
        }
        System.out.println("agsupport限制访问uri:" + uri + ", ip:" + Common.getIpAddr(request));
        request.getRequestDispatcher("/agcom/error/errorpage.html").forward(request, response);
        return false;
    }

    /**
     * 解析param
     *
     * @param request
     * @param response
     * @return 0 通过验证，需要doFilter
     * 1 再次验证，不需要doFilter
     * 2 未通过验证
     */
    private int parse(HttpServletRequest request, HttpServletResponse response) {
        OutputStream ostream = null;
        try {
            String reqUrl = request.getRequestURL().toString();
            reqUrl = URLDecoder.decode(reqUrl, "UTF-8");
            if (reqUrl.startsWith("/")) {
                String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
                reqUrl = basePath + reqUrl;
            }
            //兼容Get submit模式
            Map header = Common.getHeader(request);
            String token = (String) header.get("ag-token");
            if (!reqUrl.contains("?")) {
                if (reqUrl.contains("&")) {
                    reqUrl = reqUrl.replaceFirst("\\&", "?");
                } else {
                    String qs = request.getQueryString();
                    if (!Common.isCheckNull(qs)) {
                        reqUrl += "?" + qs;
                        if (token == null) {
                            //取出其中的ag-token
                            Map data = RestUtils.mapQueryString(qs);
                            if (data.containsKey("ag-token")) {
                                token = (String) data.get("ag-token");
                            }
                        }
                    }
                }
            }
            if (token == null) {
                //InputStream一旦被解析就必须再次请求
                byte[] ibt = RestUtils.getByteArry(request.getInputStream());
                if (request.getContentLength() > 0) {
                    Map data = RestUtils.mapBytes(ibt);
                    if (data != null) {
                        token = (String) data.get("ag-token");
                    }
                }
                if (token != null) {
                    header.put("ag-token", token);
                    //通过验证继续走Filter,必须再次请求
                    HttpRespons httpRespons = new HttpRequester().send(reqUrl, request.getMethod(), ibt, header);
                    byte[] obt = httpRespons.getBytay();
                    ostream = response.getOutputStream();
                    response.setContentType(httpRespons.getContentType());
                    ostream.write(obt);
                    ostream.flush();
                    //再次验证，不需要doFilter
                    return 1;
                }
            }
            if (checkToken(token)) {
                //通过验证，需要doFilter
                return 0;
            }
        } catch (Exception e) {
            response.setStatus(500);
            e.printStackTrace();
        } finally {
            if (ostream != null) {
                try {
                    ostream.close();
                } catch (IOException e) {
                }
            }
        }
        //未通过验证
        return 2;
    }
}
