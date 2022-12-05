package com.augurit.agcloud.agcom.agsupport.sc.project.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.common.aspect.RestLog;
import com.augurit.agcloud.agcom.agsupport.common.config.condition.MultiNodeCondition;
import com.augurit.agcloud.agcom.agsupport.common.util.DESedeUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgMapParam;
import com.augurit.agcloud.agcom.agsupport.domain.AgServer;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.param.service.IAgParam;
import com.augurit.agcloud.agcom.agsupport.sc.project.controller.form.MapParamResult;
import com.augurit.agcloud.agcom.agsupport.sc.project.service.IAgProject;
import com.augurit.agcloud.agcom.agsupport.sc.server.service.IAgServer;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.agcom.agsupport.util.HttpClientUtil;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "地图专题", description = "地图专题接口")
@RestController
@RequestMapping("/agsupport/project")
public class AgProjectController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgProjectController.class);
    @Autowired
    private IAgUser iAgUser;

    @Autowired
    private IAgProject iAgProject;

    @Autowired
    private IAgServer iAgServer;
    @Autowired
    private IAgParam iAgParam;
    @Autowired
    private IAgDir iAgDir;
    /**
     * 主界面
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/index.html")
    public ModelAndView index(HttpServletRequest request, Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/project/index");
    }

    /**
     * agsupport专题管理专题下拉列表
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "专题管理专题下拉列表", notes = "专题管理专题下拉列表")
    @RestLog(value = "专题管理专题下拉列表")
    @RequestMapping(value = "/projectList", method = RequestMethod.GET)
    public ContentResultForm projectList(HttpServletRequest request) throws Exception {
        if (MultiNodeCondition.matches()) {
            return new ContentResultForm<String>(true, doSend(request, null));
        } else {
            String loginName = LoginHelpClient.getLoginName(request);
            String realPath = getPath(request);
            List<Map> list = iAgProject.projectList(loginName);
            return new ContentResultForm<List<Map>>(true, list);
        }
    }

    /**
     * 获取前端专题图层树
     *
     * @param projectName
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取前端专题图层树", notes = "获取前端专题图层树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName", value = "专题名称", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String")
    })
    @RestLog(value = "获取前端专题图层树")
    @RequestMapping(value = "/getProjectLayerTree", method = RequestMethod.GET)
    public ContentResultForm getProjectLayerTree(String projectName, String userId, HttpServletRequest request) throws Exception {
        if (MultiNodeCondition.matches()) {
            Map params = new HashMap();
            params.put("projectName", projectName);
            params.put("userId", userId);
            return new ContentResultForm<String>(true, doSend(request, params));
        } else {
            String realPath = getPath(request);
            AgUser user = iAgUser.findUserById(userId);
            if (Common.isCheckNull(user)) {
                JSONArray jsonArray = new JSONArray();
                return new ContentResultForm<JSONArray>(false, jsonArray, "找不到用户");
            }
            List result = iAgProject.getProjectLayerTree(realPath, projectName, user);
            return new ContentResultForm(true, result);
        }
    }

    /**
     * 获取前端专题列表详细信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取前端专题列表详细信息", notes = "获取前端专题列表详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", paramType = "path")
    })
    @RestLog(value = "获取前端专题列表详细信息")
    @RequestMapping(value = "/getProjectInfo/{userId}", method = RequestMethod.GET)
    public ContentResultForm getProjectInfo(@PathVariable("userId") String userId, HttpServletRequest request) throws Exception {
        if (MultiNodeCondition.matches()) {
            return new ContentResultForm<String>(true, doSend(request, null));
        } else {
            String realPath = getPath(request);
            AgUser user = iAgUser.findUserById(userId);
            if (Common.isCheckNull(user)) {
                JSONArray jsonArray = new JSONArray();
                return new ContentResultForm<JSONArray>(false, jsonArray, "找不到用户");
            }
            //List<Map> list = iAgProject.projectList(realPath,user.getLoginName());
            //前端专题只获取该用户角色拥有的专题
            List<Map> list = iAgProject.projectList(user.getLoginName());
            for (Map map : list) {
                String mapParamId = (String) map.get("mapParamId");
                AgMapParam param = iAgParam.findMapParamById(mapParamId);
                if (param != null) {
                    map.put("param", paramTrans(param));
                }
            }
            return new ContentResultForm<List<Map>>(true, list);
        }
    }

    @ApiOperation(value = "根据layer获取arcgis服务token", notes = "获取arcgis服务token接口")
    @RestLog(value = "获取arcgis服务token")
    @GetMapping("/getArcgisTokenByLayer/{layerId}")
    public ContentResultForm getArcgisTokenByLayer(@PathVariable("layerId") String layerId,HttpServletRequest request) throws Exception{
        AgLayer agLayer = iAgDir.findLayerByLayerId(layerId);
        ContentResultForm resultForm = getArcgisTokenByLayer(agLayer, request.getRemoteAddr());
        return resultForm;
    }

    private ContentResultForm getArcgisTokenByLayer(AgLayer agLayer,String ip){
        ContentResultForm resultForm = new ContentResultForm(true);
        String agLayerUrl = agLayer.getUrl();
        String siteName = agLayerUrl.replace("http://","").replace("https://","");//去除http和https前缀
        String [] arr = siteName.split("/");
        siteName = arr[0];
        String data = agLayer.getData();
        JSONObject jsonObject = JSON.parseObject(data);
        Object userName = jsonObject.get("userName");
        Object password = jsonObject.get("password");
        String token = jsonObject.get("useToken").toString();
        int index = StringUtils.ordinalIndexOf(agLayerUrl,"/", 6);
        String serverUrl = agLayerUrl.substring(0,index);
        try {
            if ("1".equals(token)){
                if (userName != null && password != null){
                    String arcUserName = userName.toString();
                    String arcPassword = password.toString();
                    if (StringUtils.isNotBlank(arcUserName) && StringUtils.isNotBlank(arcPassword)){
                        Map params = new HashMap();
                        params.put("username",arcUserName);
                        params.put("password",arcPassword);
                        params.put("request","getToken");
                        params.put("referer",ip);
                        params.put("expiration",String.valueOf(20160));
                        params.put("f","json");
                        String httpsUrl = "https://"+siteName+"/arcgis/admin/generateToken";
                        String httpUrl = "http://"+siteName+"/arcgis/admin/generateToken";
                        Object resultToken = null;
                        try {
                            String result = "";
                            if (agLayerUrl.contains("https")){
                                result = HttpClientUtil.getBySslPost(httpsUrl, params, null, "utf-8");
                                if (StringUtils.isBlank(result)){
                                    httpsUrl = "https://"+siteName+"/arcgis/sharing/rest/generateToken";
                                    result = HttpClientUtil.getBySslPost(httpsUrl, params, null, "utf-8");
                                }
                            }else {
                                result = HttpClientUtil.get(httpUrl, params, null, "utf-8");
                                if (StringUtils.isBlank(result)){
                                    httpUrl = "http://"+siteName+"/arcgis/sharing/rest/generateToken";
                                    result = HttpClientUtil.post(httpUrl, params, null, "utf-8");
                                }
                            }

                            com.alibaba.fastjson.JSONObject tokenResultJson = JSON.parseObject(result);
                            resultToken = tokenResultJson.get("token");
                        }catch (Exception e){
                            LOGGER.error("获取arcgistoken出错!");
                            e.printStackTrace();
                        }

                        if (resultToken != null){
                            String arctoken = resultToken.toString();
                            Map tokenMap = new HashMap();
                            tokenMap.put("token",arctoken);
                            tokenMap.put("site",siteName);
                            tokenMap.put("serverUrl",serverUrl);
                            resultForm.setContent(tokenMap);
                            return resultForm;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Map tokenMap = new HashMap();
        tokenMap.put("token","");
        tokenMap.put("site",siteName);
        resultForm.setContent(tokenMap);
        return resultForm;
    }

    @ApiOperation(value = "获取所有arcgis站点的服务token", notes = "获取所有arcgis站点的服务token接口")
    @RestLog(value = "获取arcgis服务token")
    @GetMapping("/getArcgisToken")
    public ContentResultForm getArcgisToken(HttpServletRequest request) throws Exception{
        AgServer agServer = new AgServer();
        agServer.setState("1");
        List<AgServer> agServers = iAgServer.findListByState(agServer);
        return getTokenMap(agServers, request.getRemoteAddr());
    }

    private ContentResultForm getTokenMap(List<AgServer> agServers,String ip){
        ContentResultForm resultForm = new ContentResultForm(true);
        List<Map> list = new ArrayList();
        // 一天
        int expires = 1440;
        for (AgServer agServer : agServers){
            String userName = agServer.getUserName();
            String password = agServer.getPassword();
            String type = agServer.getType();
            String serverUrl = agServer.getServerUrl();
            String tokenUrl = agServer.getTokenUrl();
            //String siteName = getSiteName(serverUrl);
            password = DESedeUtil.desDecrypt(password);
            if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)){
                Map params = new HashMap();
                params.put("username",userName);
                params.put("password",password);
                params.put("request","getToken");
                params.put("referer",agServer.getReferer());
                params.put("expiration",String.valueOf(expires));
                params.put("f","pjson");

                String result = "";
                Object resultToken = null;
                Map tokenMap = new HashMap();
                try {
                    if (serverUrl.contains("https")){
                        result = HttpClientUtil.getBySslPost(tokenUrl, params, null, "utf-8");
                    }else {
                        result = HttpClientUtil.post(tokenUrl, params, null, "utf-8");
                    }
                    if (StringUtils.isNotBlank(result)){
                        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
                        resultToken = jsonObject.get("token");
                        if (resultToken != null){
                            String token = resultToken.toString();
                            tokenMap.put("token",token);
                            tokenMap.put("site",agServer.getName());
                            tokenMap.put("serverUrl",serverUrl);
                            tokenMap.put("message","站点获取token成功!");
                        }else {
                            tokenMap.put("token","");
                            tokenMap.put("site",agServer.getName());
                            tokenMap.put("serverUrl",serverUrl);
                            tokenMap.put("message",jsonObject.toJSONString());
                        }

                    }else {
                        tokenMap.put("token","");
                        tokenMap.put("site",agServer.getName());
                        tokenMap.put("serverUrl",serverUrl);
                        tokenMap.put("message","token地址访问异常!");
                        if (serverUrl.contains("https")){
                            tokenMap.put("tokenUrl",tokenUrl);
                        }else {
                            tokenMap.put("tokenUrl",tokenUrl);
                        }
                    }

                }catch (Exception e){
                    LOGGER.error("获取arcgistoken出错!");
                    tokenMap.put("site",agServer.getName());
                    tokenMap.put("message","站点访问异常!");
                    tokenMap.put("token","");
                    tokenMap.put("serverUrl",serverUrl);
                    e.printStackTrace();
                }
                list.add(tokenMap);
            }
        }
        resultForm.setContent(list);
        return resultForm;
    }

    private String getSiteName(String url){
        String siteName = url.replace("http://","").replace("https://","");//去除http和https前缀
        String [] arr = siteName.split("/");
        siteName = arr[0];
        return siteName;
    }

    @ApiOperation(value = "获取所有地图参数", notes = "获取所有地图参数")
    @RestLog(value = "获取所有地图参数")
    @RequestMapping(value = "/getMapParams", method = RequestMethod.GET)
    public ContentResultForm getMapParams() throws Exception {
        List<AgMapParam> agMapParams = iAgParam.searchAll();
        List<MapParamResult> results = new ArrayList<>();
        for (AgMapParam amp : agMapParams) {
            results.add(paramTrans(amp));
        }
        return new ContentResultForm<>(true, results);
    }

    private MapParamResult paramTrans(AgMapParam param) {
        MapParamResult result = new MapParamResult();
        if (param != null) {
            result.setId(param.getId());
            result.setExtent(param.getExtent());
            result.setOrigin(param.getOrigin());
            result.setTileOrigin(param.getTileOrigin());
            result.setName(param.getName());
            result.setReference(param.getReference());
            result.setCenter(param.getCenter());
            result.setZoom(param.getZoom());
            result.setMinZoom(param.getMinZoom());
            result.setMaxZoom(param.getMaxZoom());
            result.setResolutions(param.getScales());
            if (param.getCenter() != null && param.getScales() != null) {
                String[] center = param.getCenter().split(",");
                Double flag = Double.parseDouble(center[0]);
                String scales[] = param.getScales().split(",");
                String scalesStr = "";
                int index = 0;
                if (flag >= -400 && flag <= 400) {
                    //经纬度坐标
                    for (String s : scales) {
                        float v = Float.parseFloat(s);
                        double ss = v * 96 * 2 * Math.PI * 6378137 / (0.0254 * 360);
                        if (index == scales.length - 1) {
                            scalesStr += ss;
                        } else {
                            scalesStr += ss + ",";
                        }
                        index++;
                    }
                } else {
                    for (String s : scales) {
                        float v = Float.parseFloat(s);
                        double ss = (96 / 0.0254) * v;
                        if (index == scales.length - 1) {
                            scalesStr += ss;
                        } else {
                            scalesStr += ss + ",";
                        }
                        index++;
                    }
                }
                result.setScales(scalesStr);
            }
        }
        return result;
    }

    /**
     * 负责转发请求
     *
     * @param request
     * @param param
     * @return
     */
    private String doSend(HttpServletRequest request, Map param) {
        String requestURI = request.getRequestURI();
        //注意目前的 主站点链接是格局配置的ip端口 加上请求的uri组成的，要求从站点的server.context-path要跟主站点一致
        String url = MultiNodeCondition.mainServer + requestURI;
        HttpRequester hr = new HttpRequester();
        String method = request.getMethod();
        HttpRespons httpRespons = null;
        String result = null;
        try {
            if ("post".equalsIgnoreCase(method)) {
                httpRespons = hr.sendPost(url, param);
            } else {
                httpRespons = hr.sendGet(url, param);
            }
            result = httpRespons.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getPath(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath("/") + "ui-static/agcloud/agcom/project/";
    }

    @ApiOperation(value = "根据标注id查询图层", notes = "根据标注id查询图层")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lableId", value = "标注id", dataType = "String"),
            @ApiImplicitParam(name = "projectName", value = "专题名称", dataType = "String"),
            @ApiImplicitParam(name = "asc", value = "asc", dataType = "String")
    })
    @RestLog(value = "根据标注id查询图层")
    @RequestMapping(value = "/findLayerByLayerLabel", method = RequestMethod.GET)
    public ContentResultForm findLayerByLayerLabel(String lableId, String projectName, String asc, HttpServletRequest request) throws Exception {
        if (MultiNodeCondition.matches()) {
            return new ContentResultForm<String>(true, doSend(request, null));
        } else {
            String realPath = getPath(request);
            AgUser user = iAgUser.findUserByName(lableId);
            if (Common.isCheckNull(user)) {
                JSONArray jsonArray = new JSONArray();
                return new ContentResultForm<JSONArray>(false, jsonArray, "获取失败");
            }
            List result = iAgProject.getProjectLayerTree(realPath, projectName, user);
            return new ContentResultForm(true, result);
        }
    }

}
