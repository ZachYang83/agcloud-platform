package com.augurit.agcloud.agcom.agsupport.sc.server.controller;

import com.augurit.agcloud.agcom.agsupport.common.util.DESedeUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgServer;
import com.augurit.agcloud.agcom.agsupport.sc.log.service.IAgLog;
import com.augurit.agcloud.agcom.agsupport.sc.server.service.IAgServer;
import com.augurit.agcloud.agcom.agsupport.util.LogUtil;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;


/**
 * @author zhangmingyang
 * @Description: 服务器管理
 * @date 2018-11-12 15:56
 */
@Api(value = "服务器管理",description = "服务器管理相关接口")
@RestController
@RequestMapping("/agsupport/server")
public class AgServerController{
    @Autowired
    private IAgServer agServer;
    @Autowired
    private IAgLog log;
    @RequestMapping("/index.html")
    public ModelAndView index(){
        return new ModelAndView("agcloud/agcom/agsupport/sermanager/index");
    }

    @ApiOperation(value = "根据条件分页查询服务器信息",notes = "根据条件分页查询服务器信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "server",value = "服务器对象",dataType = "AgServer"),
            @ApiImplicitParam(name = "page",value = "分页参数:page=1&rows=10",dataType = "Page")
    })
    @RequestMapping(value = "/serverList",method = RequestMethod.GET)
    public ContentResultForm serverList(AgServer server , Page page) throws Exception{
        PageInfo<AgServer> list = agServer.findList(server, page);
        return  new ContentResultForm<PageInfo>(true,list);
    }
    @ApiOperation(value = "新增或修改服务器信息",notes = "新增或修改服务器信息接口")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResultForm save(AgServer server, HttpServletRequest request){
        JSONObject jsonObject = null;
        String funcName = StringUtils.isNotBlank(server.getId())?"服务器管理--修改":"服务器管理--新增";
        try {
            agServer.save(server);
            log.info(LogUtil.createSuccessLogInfo(request,funcName).toString());
            return new ResultForm(true,"保存成功!");
        }catch (Exception e){
            log.error(LogUtil.createFailLogInfo(request,funcName,e.getMessage()).toString());
            e.printStackTrace();
        }
        return new ResultForm(false, "保存失败!");
    }

    @ApiOperation(value = "批量删除服务器数据",notes = "批量删除服务器数据接口")
    @ApiImplicitParam(name = "ids",value = "服务器id,多个id英文逗号隔开",dataType = "String")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public ResultForm delete(String ids,HttpServletRequest request){
        JSONObject jsonObject = this.getLogInfo(request, "批量删除服务器数据");
        if (StringUtils.isNotBlank(ids)){
            String[] serverIds = ids.split(",");
            try {
                if (serverIds != null && serverIds.length > 0){
                    for (String id:serverIds){
                        agServer.deleteServerById(id);
                    }
                }
                jsonObject.put("operResult", "成功");
                log.info(jsonObject.toString());
                return new ResultForm(true,"删除成功!");
            }catch (Exception e){
                jsonObject.put("operResult", "失败");
                if (e.getMessage() != null) {
                    jsonObject.put("exceptionMessage", e.getMessage().substring(0, e.getMessage().length() > 500 ? 500 : e.getMessage().length()));
                }
                log.error(jsonObject.toString());
                e.printStackTrace();
            }
        }
        return new ResultForm(false,"删除失败!");
    }

    @ApiOperation(value = "根据id查询服务器信息",notes = "根据id查询服务器信息接口")
    @ApiImplicitParam(name = "id",value = "服务器id",dataType = "String")
    @RequestMapping(value = "/getServerById/{id}",method = RequestMethod.GET)
    public ContentResultForm getServerById(@PathVariable String id) throws Exception{
        AgServer agServer = this.agServer.selectServerById(id);
        return new ContentResultForm(true,agServer);
    }

    /**
     * 获取日志基本信息 json
     *
     * @param request
     * @param funcName
     * @return
     */
    private JSONObject getLogInfo(HttpServletRequest request, String funcName) {
        JSONObject jsonObject = new JSONObject();
        String loginName = LoginHelpClient.getLoginName(request);
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        jsonObject.put("loginName", loginName);
        jsonObject.put("sysName", "agsupport");
        jsonObject.put("ipAddress", Common.getIpAddr(request));
        jsonObject.put("browser", userAgent.getBrowser().getName());
        jsonObject.put("funcName", funcName);
        return jsonObject;
    }

    /**
     * 目前只支持 serverType是arcgis类型的服务器做连接检测
     * @return
     */
    @ApiOperation(value = "检测服务器是否能正常连接",notes = "检测服务器正常连接接口")
    @RequestMapping(value = "/connectTest",method = RequestMethod.POST)
    public ResultForm connectTest(AgServer server,HttpServletRequest request){
        try {
            InetAddress address = InetAddress.getLocalHost();
            String hostIp=address.getHostAddress().toString(); //获取本机ip
            Map<String, String> param = new HashMap<>();
            param.put("f", "json");
            param.put("username",server.getUserName());
            if (org.apache.commons.lang3.StringUtils.isNotBlank(server.getId())){
                param.put("password",DESedeUtil.desDecrypt(server.getPassword()));
            }else {
                param.put("password",server.getPassword());
            }
            param.put("client", "ip");
            param.put("ip", hostIp);
            param.put("expiration", "2");
            String url = "http://"+server.getIp()+":"+server.getPort()+"/arcgis/admin/generateToken";
            HttpRequester httpRequester = new HttpRequester();
            httpRequester.setConnectTimeout(3000);
            HttpRespons httpRespons =httpRequester.sendPost(url, param);
            String httpResponsReqult = httpRespons.getContent();
            JSONObject object = JSONObject.fromObject(httpResponsReqult);

            if (object.has("token")){
                return new ResultForm(true,"连接成功");
            }else {
                return new ResultForm(false,"连接失败,请检查信息是否正确!");
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            if (e.getMessage().contains("error url")){
                return new ResultForm(false,"地址无法访问，请仔细检查");
            }
        }
        return new ResultForm(false,"地址无法访问，请仔细检查");
    }
}
