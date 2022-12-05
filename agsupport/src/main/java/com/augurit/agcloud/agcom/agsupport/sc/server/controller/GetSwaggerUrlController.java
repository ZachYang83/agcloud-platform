package com.augurit.agcloud.agcom.agsupport.sc.server.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangmy
 * @Description: 根据url获取swagger接口文档地址
 * @date 2020-01-02 15:30
 */
@Api(value = "获取swagger接口文档地址",description = "根据html访问地址，获取相应模块的接口文档地址")
@RestController
@RequestMapping("/agsupport/restfulswagger")
public class GetSwaggerUrlController {
    public WebApplicationContext getWebApplicationContext(ServletContext sc) {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
    }

    @ApiOperation(value = "根据html访问地址，获取相应模块的接口文档地址")
    @ApiImplicitParam(name = "url",value = "地址的url",required = true)
    @GetMapping("getRestFulSwaggerUrl")
    public ContentResultForm getRestFulSwaggerUrl(String url, HttpServletRequest request){
        ContentResultForm resultForm = new ContentResultForm(true);
        resultForm.setContent(getSwaggerUrl(url, request));
        return resultForm;
    }

    private String getSwaggerUrl(String url,HttpServletRequest request){
        WebApplicationContext wc = getWebApplicationContext(request.getSession().getServletContext());
        RequestMappingHandlerMapping rmhp = wc.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        String result = "";
        for (Iterator<RequestMappingInfo> iterator = map.keySet().iterator(); iterator
                .hasNext();) {
            RequestMappingInfo info = iterator.next();
            HandlerMethod method = map.get(info);
            PatternsRequestCondition patternsCondition = info.getPatternsCondition();
            Set<String> urlPatterns = patternsCondition.getPatterns();
            for (String pathUrl : urlPatterns) {
                if (url.equals(pathUrl)) {
                    String name = method.getMethod().getDeclaringClass().getName();
                    String ctrName = name.substring(name.lastIndexOf(".") + 1,name.length());
                    ctrName = ctrName.replaceAll("[A-Z]", "-$0").toLowerCase();
                    ctrName = ctrName.substring(1,ctrName.length());
                    result = "swagger-ui.html#/" + ctrName;
                }
            }

        }
        return result;
    }
}
