package com.augurit.agcloud.agcom.agsupport.sc.index.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.framework.util.JsonMapper;
import com.augurit.agcloud.agcom.agsupport.sc.system.service.ISystem;
import com.augurit.agcloud.agcom.agsupport.sc.system.util.DomainCheck;
import com.augurit.agcloud.agcom.agsupport.sc.index.service.IAgIndex;
import com.augurit.agcloud.agcom.agsupport.sc.log.service.IAgLog;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcom.common.CasLoginHelpClient;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
import com.fasterxml.jackson.annotation.JsonInclude;
import eu.bitwalker.useragentutils.UserAgent;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017.
 */
@RestController
public class AgIndexController {

    @Autowired
    private IAgIndex iAgIndex;

    @Autowired
    private IAgLog log;

    @Autowired
    private ISystem system;
    @Autowired
    private IAgUser agUser;

    @RequestMapping("/indexNew.do")
    public ModelAndView indexNew(Model model, HttpServletRequest request) throws Exception {
        String loginName = CasLoginHelpClient.getLoginName(request);
        AgUser loginUser = agUser.findUserByName(loginName);
        if (loginUser == null) loginUser = new AgUser();
        model.addAttribute("loginUser", loginUser);
        return new ModelAndView("/agcom/newIndex");
    }

    @RequestMapping("/index.do")
    public ModelAndView index(Model model, HttpServletRequest request) throws Exception {
        String userName = LoginHelpClient.getLoginName(request);
        model.addAttribute("userName", userName);
        List<Map> menus = null;
        /*boolean isNew = "new".equalsIgnoreCase(Common.getByKey("menu.v", "new"));
        if (!isNew) {
            menus = iAgIndex.getIndexMenu(userName);
        }*/
        model.addAttribute("menus", menus);
        //OmQcAuth.getInstance().init(request);
        boolean top = true;
        if (!StringUtils.isBlank(request.getParameter("top"))) {
            if ("false".equals(request.getParameter("top"))) top = false;
        }

        String title = iAgIndex.getTitle();
        if (StringUtils.isEmpty(title)) {
            title = "奥格 AGCOM 5.0";
        }
        model.addAttribute("top", top);
        model.addAttribute("title", title);
        return new ModelAndView("/agcom/index");
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/logoutByCas")
    public void logoutByCas(HttpServletRequest request, HttpServletResponse response) {
        String loginName = LoginHelpClient.getLoginName(request);
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("loginName", loginName);
        jsonObject.put("sysName", "agsupport");
        String ip = Common.getIpAddr(request);
        jsonObject.put("ipAddress", ip);
        jsonObject.put("browser", userAgent.getBrowser().getName());
        jsonObject.put("funcName", "登出系统");
        // http://172.25.101.111:8080/agcas/logout?service=http://218.20.201.111:8080/agsupport/
        try {
            String url = "logout?t=" + new Date().getTime();
            response.sendRedirect(url);
            jsonObject.put("operResult", "成功");
            log.info(jsonObject.toString());
        } catch (IOException e) {
            jsonObject.put("operResult", "失败");
            if (e.getMessage() != null) {
                jsonObject.put("exceptionMessage", e.getMessage().substring(0, e.getMessage().length() > 500 ? 500 : e.getMessage().length()));
            }
            log.error(jsonObject.toString());
            e.printStackTrace();
        }
        //应用登出从ip白名单中剔除
        if (!DomainCheck.localIpSet.contains(ip)) {
            DomainCheck.allowIpSet.remove(ip);
        }
    }

    @RequestMapping("/leftMenuDir")
    public String leftMenuDir(HttpServletRequest request, String dirId, String xPath) throws Exception{
        String userName = LoginHelpClient.getLoginName(request);
        List<Map> indexMenuDirTree = iAgIndex.getIndexMenuDirTree(userName, dirId, xPath);
        JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);//不去掉为空字段
        return mapper.toJson(indexMenuDirTree);
    }
}
