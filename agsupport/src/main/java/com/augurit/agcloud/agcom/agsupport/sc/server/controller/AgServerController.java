package com.augurit.agcloud.agcom.agsupport.sc.server.controller;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.agcom.agsupport.common.util.DESedeUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgServer;
import com.augurit.agcloud.agcom.agsupport.sc.log.service.IAgLog;
import com.augurit.agcloud.agcom.agsupport.sc.server.service.IAgServer;
import com.augurit.agcloud.agcom.agsupport.sc.site.util.defaultHttpClientUtil;
import com.augurit.agcloud.agcom.agsupport.util.HttpClientUtil;
import com.augurit.agcloud.agcom.agsupport.util.LogUtil;
import com.augurit.agcloud.agcom.syslog.service.annotation.SysLog;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
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
import java.util.List;
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

    private defaultHttpClientUtil defaultHttpClientUtil = new defaultHttpClientUtil();

    private String charset = "utf-8";
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
        try {
            agServer.save(server);
            return new ResultForm(true,"保存成功!");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResultForm(false, "保存失败!");
    }
    @SysLog(sysName = "地图运维",funcName = "删除站点")
    @ApiOperation(value = "批量删除服务器数据",notes = "批量删除站点数据接口")
    @ApiImplicitParam(name = "ids",value = "站点id,多个id英文逗号隔开",dataType = "String")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public ResultForm delete(String ids,HttpServletRequest request){
        if (StringUtils.isNotBlank(ids)){
            String[] serverIds = ids.split(",");
            try {
                if (serverIds != null && serverIds.length > 0){
                    for (String id:serverIds){
                        agServer.deleteServerById(id);
                    }
                }
                return new ResultForm(true,"删除成功!");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return new ResultForm(false,"删除失败!");
    }

    @ApiOperation(value = "根据id查询站点信息",notes = "根据id查询站点信息接口")
    @ApiImplicitParam(name = "id",value = "站点id",dataType = "String")
    @RequestMapping(value = "/getServerById/{id}",method = RequestMethod.GET)
    public ContentResultForm getServerById(@PathVariable String id) throws Exception{
        AgServer server = agServer.selectServerById(id);
        return new ContentResultForm(true,server);
    }

    @ApiOperation(value = "修改站点启用状态",notes = "修改站点启用状态接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "站点id",required = true,dataType = "String"),
            @ApiImplicitParam(name = "state",value = "启用状态,0:不启用，1:启用。",required = true,dataType = "String" ),
    })
    @RequestMapping(value = "/changeState/{id}/{state}",method = RequestMethod.POST)
    public ContentResultForm changeState(@PathVariable String id,@PathVariable String state) throws Exception{
        agServer.changeState(id,state);
        return new ContentResultForm(true,null,"修改成功!");
    }

    /**
     * 目前只支持 serverType是arcgis类型的站点做连接检测
     * @return
     */
    @ApiOperation(value = "检测站点是否能正常连接",notes = "检测站点正常连接接口")
    @RequestMapping(value = "/connectTest",method = RequestMethod.POST)
    public ResultForm connectTest(AgServer server,HttpServletRequest request){
        try {
            String token = getToken(server);

            if (StringUtils.isNotBlank(token)){
                return new ResultForm(true,"连接成功");
            }else {
                return new ResultForm(false,"连接失败,请检查信息是否正确!");
            }

        }catch (Exception e){
            e.printStackTrace();
            return new ResultForm(false,"地址无法访问，请仔细检查");
        }
    }

    private String loginin(String name, String password, String host) {
        //portal
//			defaultHttpClientUtil = new defaultHttpClientUtil();
        //String url = "https://192.168.30.199/arcgis/sharing/rest/generateToken";
        //arcgis server
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
            String localname = ia.getHostName();
            String localip = ia.getHostAddress();
            String generateToken = generateToken(name, password, "requestip", localip, "", "60", host);
            try {
                JSONObject generateTokenJson = JSONObject.fromObject(generateToken);
                if (generateToken != null) {

                    String token = generateTokenJson.getString("token");
                    String expires = generateTokenJson.getString("expires");
                    System.out.println(token);
                    System.out.println(expires);
                    return token;
                } else {
                    return "geoserver";
                }
            } catch (Exception e) {
                return "geoserver";
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    private String generateToken(String username, String passWord, String client, String ip, String referer, String expiration, String host) {
        try {
            String url = "https://" + host + "/arcgis/admin/generateToken";
            Map<String, String> createMap = new HashMap<String, String>();
            createMap.put("password", passWord);
            createMap.put("username", username);
            createMap.put("client", client);
//            createMap.put("ip", ip);
            createMap.put("referer", referer);
            createMap.put("expiration", expiration);
            createMap.put("f", "pjson");
            System.out.println(createMap);

            String result = defaultHttpClientUtil.doPost(url, createMap, charset);
            if (result == null) {
                url = "http://" + host+ "/arcgis/admin/generateToken";
                result = defaultHttpClientUtil.doPost(url, createMap, charset);
            }
            return result;
        } catch (Exception e) {
            return null;
        }

    }

    private String getToken(AgServer agServer) throws Exception{
        String token = "";
        String tokenUrl = agServer.getTokenUrl();
        int expires = 1440;
        if (agServer != null){
            String sitetoken = agServer.getToken();
            if (org.apache.commons.lang.StringUtils.isNotBlank(sitetoken)){
                token = sitetoken;
            }else {
                String userName = agServer.getUserName();
                String password = agServer.getPassword();
                password = DESedeUtil.desDecrypt(password);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(userName) && org.apache.commons.lang3.StringUtils.isNotBlank(password)){
                    Map params = new HashMap();
                    params.put("username",userName);
                    params.put("password",password);
                    params.put("request","getToken");
                    params.put("referer",agServer.getReferer());
                    params.put("expiration",String.valueOf(expires));
                    params.put("f","pjson");

                    String result = "";
                    Object resultToken = null;
                    try {
                        if (tokenUrl.contains("https")){
                            result = HttpClientUtil.getBySslPost(tokenUrl, params, null, "utf-8");
                        }else {
                            result = HttpClientUtil.post(tokenUrl, params, null, "utf-8");
                        }
                        if (StringUtils.isNotBlank(result)){
                            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
                            resultToken = jsonObject.get("token");
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (resultToken != null){
                        token = resultToken.toString();
                    }
                }

            }
        }
        return token;
    }
}
