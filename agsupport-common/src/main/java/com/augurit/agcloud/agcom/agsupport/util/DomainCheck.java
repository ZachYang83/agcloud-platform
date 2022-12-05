package com.augurit.agcloud.agcom.agsupport.util;

import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类的描述
 *
 * @author Hunter
 * @Time 2017/11/23
 */
public class DomainCheck {

    public static Set allowIpSet;

    public static Set localIpSet;

    private static Pattern p;

    static {
        allowIpSet = new HashSet();
        localIpSet = new HashSet();

        localIpSet.add("127.0.0.1");
        localIpSet.add("localhost");
        List list = null;
        if ((list = Arrays.asList(Common.getLocalAddress())).size() > 0) localIpSet.addAll(list);
        if ((list = Arrays.asList(Common.checkNull(Common.getByKey("token.allow.ip", "*")).split(", *"))).size() > 0) localIpSet.addAll(list);
        allowIpSet.addAll(localIpSet);
        p = Pattern.compile("/[^\\.]+$");
    }

    /**
     * token本地验证
     * @param request
     * @return
     */
    public static boolean check(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String context = request.getContextPath();
        Matcher m = p.matcher(uri);
        //被cas拦截过的资源及其首页可通行
        if (!m.matches()
                || uri.equals(context + "/")
                || uri.equals(context + "/logoutByCas")
                || uri.equals(context + "/system/getToken")
                || uri.equals(context + "/system/checkToken")
                ) {
            return true;
        }
        //集成了cas系统的可以通行
        String loginName = LoginHelpClient.getLoginName(request);
        if (!Common.isCheckNull(loginName)) {
            return true;
        }
        //集群白名单服务器可通行
        String remoteIp = Common.getIpAddr(request);
        if (allowIpSet.contains(remoteIp)) {
            return true;
        } else {
            for (Iterator it = allowIpSet.iterator(); it.hasNext();) {
                String ip = (String)it.next();
                if ("*".equals(ip)) return true;
                ip = ip.replaceAll("\\*", "[^\\.]*").replaceAll("\\.", "\\\\.");
                if (remoteIp.matches(ip)) {
                    return true;
                }
            }
        }
        return false;
    }
}
