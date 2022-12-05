package com.augurit.agcloud.agcom.agsupport.sc.site.controller;


import com.augurit.agcloud.agcom.agsupport.common.util.DESedeUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.RedisAdaptor;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.domain.AgServer;
import com.augurit.agcloud.agcom.agsupport.domain.AgSite;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.ReadUrlUtil;
import com.augurit.agcloud.agcom.agsupport.sc.server.service.IAgServer;
import com.augurit.agcloud.agcom.agsupport.sc.site.service.IAgSite;
import com.augurit.agcloud.agcom.agsupport.sc.site.util.defaultHttpClientUtil;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.common.util.Common;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTStyleList;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.util.*;


/**
 * Created by yzq on 2017.
 */
@RestController
@RequestMapping("/agsupport/site")
public class AgSiteController {

    private static Logger logger = LoggerFactory.getLogger(AgSiteController.class);

    @Autowired
    public IAgSite IAgSite;
    @Autowired
    private IAgDic iAgDic;
    @Autowired
    private IAgServer iAgServer;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private defaultHttpClientUtil defaultHttpClientUtil = new defaultHttpClientUtil();
    private String url = "https://192.168.19.96/arcgis/sharing/rest/generateToken";
    private String charset = "utf-8";
    @Value("${agcloud.opus.admin.orgId}")
    private String orgId;//组织id


    //GET注解设置接受请求类型为GET
    //  @GET
    //Produces表明发送出去的数据类型为text/plain
    // 与Produces对应的是@Consumes，表示接受的数据类型为text/plain@Produces("text/plain")
    //  public String getMessage() {
    //       return "Hello world!";
    //  }
    @RequestMapping("/index.do")
    public ModelAndView index(HttpServletRequest request, Model model) {

        return new ModelAndView("agcom/sermanager/serviceRegist");
    }

    //arcgis 服务停止
    @RequestMapping("/stops")
    public String stops(HttpServletRequest request) {
        String serviceName = request.getParameter("serviceName");
        String folderName = request.getParameter("folderName");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        String token = loginin(name, password, ip, port);
        String url = "https://" + ip + ":" + port + "/arcgis/admin/services/" + folderName + "/" + serviceName + ".MapServer/stop";
        Map<String, String> createMap = new HashMap<String, String>();
        createMap.put("token", token);
        createMap.put("f", "json");
        String result = defaultHttpClientUtil.doPost(url, createMap, charset);
        if (result == null) {
            String url1 = "http://" + ip + ":" + port + "/arcgis/admin/services/" + folderName + "/" + serviceName + ".MapServer/stop";
            result = defaultHttpClientUtil.doPost(url1, createMap, charset);
        }
        System.out.println(result);
        return result;
    }

    //arcgis 服务编辑
    @RequestMapping("/editser")
    public String editser(HttpServletRequest request) {
        String serviceName = request.getParameter("serviceName");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        String folderName = request.getParameter("folderName");

        String token = loginin(name, password, ip, port);
        String url1 = "https://" + ip + ":" + port + "/arcgis/admin/services/" + folderName + "/" + serviceName + ".MapServer?f=pjson&token=" + token;
        String result1 = defaultHttpClientUtil.doGet(url1, charset);
        JSONObject JSON1 = JSONObject.fromObject(result1);
        JSONObject JSON2 = JSON1.getJSONObject("properties");
        JSONObject jsb = new JSONObject();
        String where = JSON2.getString("filePath");


        String url = "https://" + ip + ":" + port + "/arcgis/admin/services/" + serviceName + ".MapServer/edit";
        Map<String, String> createMap = new HashMap<String, String>();
        createMap.put("token", token);
        createMap.put("f", "json");
        createMap.put("service", result1);

        String result = defaultHttpClientUtil.doPost(url, createMap, charset);
        System.out.println(result);
        return result;
    }

    //arcgis 服务启动
    @RequestMapping("/starts")
    public String starts(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        String folderName = request.getParameter("folderName");
        String token = loginin(name, password, ip, port);
        String serviceName = request.getParameter("serviceName");
        String url = "https://" + ip + ":" + port + "/arcgis/admin/services/" + folderName + "/" + serviceName + ".MapServer/start";
        Map<String, String> createMap = new HashMap<String, String>();
        createMap.put("token", token);
        createMap.put("f", "json");
        String result = defaultHttpClientUtil.doPost(url, createMap, charset);
        if (result == null) {
            String url1 = "http://" + ip + ":" + port + "/arcgis/admin/services/" + folderName + "/" + serviceName + ".MapServer/start";
            result = defaultHttpClientUtil.doPost(url1, createMap, charset);
        }
        System.out.println(result);
        return result;
    }

    //arcgis 服务删除
    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");

        String token = loginin(name, password, ip, port);
        String serviceName = request.getParameter("serviceName");
        if (token.equals("geoserver")) {
            String result = deGeoServ(name, password, ip, port, serviceName);
            return result;
        }

        String url = "https://" + ip + ":" + port + "/arcgis/admin/services/" + serviceName + ".MapServer/delete";
        Map<String, String> createMap = new HashMap<String, String>();
        createMap.put("token", token);
        createMap.put("f", "json");
        String result = defaultHttpClientUtil.doPost(url, createMap, charset);
        System.out.println(result);
        return result;
    }

    //geoserver 服务启动
    public String deGeoServ(String name, String password, String ip, String port, String serviceName) {
        String RESTURL = "http://" + ip + ":" + port + "/geoserver";
        String RESTUSER = name;
        String RESTPW = password;
        String[] service = serviceName.split(":");
        List<String> workspaces = null;
        String result = "";
        try {
            File zipFile = new File("F:\\shp\\shp2.zip");
            GeoServerRESTReader reader = new GeoServerRESTReader(RESTURL, RESTUSER, RESTPW);
            GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(RESTURL, RESTUSER, RESTPW);
            workspaces = reader.getWorkspaceNames();

            if (workspaces.contains("luogang")) {
                System.out.println(workspaces);
//                if(publisher.publishShp("luogang", "sfdem", "zm", zipFile, "EPSG:4326")){
                if (publisher.unpublishFeatureType(service[0], "021002", service[1])) {
                    result = "success";
                } else {
                    result = "false!";
                }
                System.out.println(result);
            }
        } catch (Exception mue) {
            mue.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/getregis")
    public String getregis(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String serviceName = request.getParameter("serviceName");
        String type = request.getParameter("type");
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        List<Map> dields = IAgSite.getregis();
        JSONArray jsonarray = new JSONArray();
        if (dields.size() > 0) {
            for (Map map : dields) {
                JSONObject jsonobj = new JSONObject();
                if (map.containsKey("url") && map.get("url") != null) {
                    String url = map.get("url").toString();

                    //&&url.indexOf("6080")!=-1&&url.indexOf(type)!=-1&&url.indexOf(serviceName)!=-1
                    if (serviceName == null || type == null || ip == null) break;
                    if (url.indexOf(ip) != -1 && url.indexOf(port) != -1 && url.indexOf(type) != -1 && url.indexOf(serviceName) != -1) {
                        String layer = url + "/" + map.get("layer_table").toString();
                        jsonobj.put("layer", layer);
                        jsonobj.put("name", map.get("name_cn").toString());
                        String types = map.get("layer_type").toString();
                        if (types.equals("040002")) {
                            jsonobj.put("type", "WFSServer");
                        } else if (types.equals("030002")) {
                            jsonobj.put("type", "WMSServer");
                        } else if (types.equals("070002")) {
                            jsonobj.put("type", "FeatureServer");
                        } else if (types.equals("020202")) {
                            jsonobj.put("type", "MapServer");
                        }

                        jsonarray.add(jsonobj);
                    }
                }
            }
        }
        return jsonarray.toString();
    }

    @RequestMapping("/getReports")
    public ContentResultForm getReports(String page, String rows, String serverId) throws Exception {
        try {
            if(Common.isCheckNull(serverId)){
                return new ContentResultForm(false,new JSONArray(),"服务器编号(serverId)不能为空!");
            }
            AgServer agServer = iAgServer.selectServerById(serverId);
            String ip = agServer.getIp();
            String port = agServer.getPort();
            String userName = agServer.getUserName();
            String password = agServer.getPassword();
            String token = agServer.getToken();
            password = DESedeUtil.desDecrypt(password);
            String key = ip + port + userName + password;
            String values = (String) RedisAdaptor.getRedis().get("IAgSite:" + key);
            String urlString = "http://" + ip + ":" + port + "/arcgis/rest/services?f=pjson";
            JSONArray jsonArray = new JSONArray();
            if (!Common.isCheckNull(values)) {
                jsonArray = JSONArray.fromObject(values);
            } else {
                if(!Common.isCheckNull(token)){
                    urlString += "&token="+token;
                }else if(!Common.isCheckNull(userName)&&!Common.isCheckNull(password)){
                    token = loginin(userName, password, ip, port);
                    urlString += "&token="+token;
                }
                if (token.indexOf("geoserver") == -1) {
                    URL url = new URL(urlString);
                    getListService(url, jsonArray);
                    RedisAdaptor.getRedis().put("IAgSite:" + key, jsonArray.toString());
                }
                else {
                    url = "http://" + ip + ":" + port + "/geoserver/wfs?service=wfs&version=1.1.0&request=GetCapabilities";
                    Map<String, String> result = ReadUrlUtil.queryOGC_WFSLayerTable(url);
                    if (result == null || result.size() == 0) {
                        return new ContentResultForm(false, new JSONArray(), "服务帐号错误");
                    }
                    JSONObject jsonobj = new JSONObject();
                    for (Map.Entry<String, String> entry : result.entrySet()) {
                        jsonobj.put("serviceName", entry.getKey());
                        jsonobj.put("typeName", entry.getValue());
                        jsonobj.put("folderName", "/");
                        jsonobj.put("type", "/");
                        jsonobj.put("status", "STARTED");
                        jsonArray.add(jsonobj);
                    }
                }
            }
            if (StringUtils.isEmpty(page)) page = "1";
            if (StringUtils.isEmpty(rows)) rows = "10";
            JSONArray resultJSONArray = getPageListService(Integer.parseInt(page), Integer.parseInt(rows), jsonArray);
            JSONObject result = new JSONObject();
            result.put("total",jsonArray.size());
            result.put("rows",resultJSONArray);
            return new ContentResultForm(true, result);
        }
        catch (Exception e){
            return new  ContentResultForm(false,new JSONArray(),e.getMessage());
        }

    }

    /*
    * 获取指定url下的service列表以及子目录下的service列表
    * */
    private void getListService(URL url,JSONArray jsonArraay) throws Exception{
         if(jsonArraay == null){
             jsonArraay = new JSONArray();
         }
        String charSeparator = "/";
        String paramSeparator = "?";
        String urlString = url.toString();
        String floder = url.getPath().substring(url.getPath().lastIndexOf('/')+1);//服务(service）所在目录；
        if(floder.equals("services")){
            floder = "/";//根目录
        }
        String responseResult = defaultHttpClientUtil.doGet(urlString, charset);
        JSONObject responseResultObj = JSONObject.fromObject(responseResult);
        JSONArray floderJsonArray = responseResultObj.getJSONArray("folders");
        JSONObject serviceJsonObj = new JSONObject();
        JSONArray servicesJsonArray = responseResultObj.getJSONArray("services");
        //获取当前目录下的服务列表
        for (int i = 0; i < servicesJsonArray.size(); i++) {
            try {
                JSONObject itemJsonObject = servicesJsonArray.getJSONObject(i);
                String serviceName = itemJsonObject.getString("name");
                String serviceType = "MapServer";
                serviceJsonObj.put("serviceName", serviceName);
                serviceJsonObj.put("type", serviceType);
                int lastIndex = url.toString().lastIndexOf(paramSeparator);
                String serviceUrlString = urlString.substring(0,lastIndex)+charSeparator+serviceName.replace(floder+charSeparator,"")+charSeparator+serviceType+paramSeparator+url.getQuery();
                URL serviceUrl = new URL(serviceUrlString);
                String serviceResponseResult = defaultHttpClientUtil.doGet(serviceUrlString, charset);
                JSONObject serviceResponseResultObj = JSONObject.fromObject(serviceResponseResult);
                if (serviceResponseResultObj != null && serviceResponseResultObj.has("error")){
                    continue;
                }
                String supportedExtensionsType = serviceResponseResultObj.getString("supportedExtensions");
                if ("FeatureServer".equals(itemJsonObject.getString("type"))) {
                    i += 1;
                }
                String typeName = serviceType;
                if (supportedExtensionsType != "") {
                    supportedExtensionsType = supportedExtensionsType.replaceAll(",", "、");
                    typeName = typeName + "、" + supportedExtensionsType;
                }

                serviceJsonObj.put("type", "MapServer");
                serviceJsonObj.put("typeName", typeName);
                serviceJsonObj.put("folderName", floder);
                serviceJsonObj.put("status", "STARTED");
                jsonArraay.add(serviceJsonObj);
            } catch (Exception e) {
                continue;
            }
        }
        if(floderJsonArray != null && floderJsonArray.size()>0){
            for(int i = 0 ; i<floderJsonArray.size();i++){
                String floderName = floderJsonArray.getString(i);
                int lastIndex = url.toString().lastIndexOf(paramSeparator);
                String urlStr = url.toString().substring(0,lastIndex)+charSeparator+floderName+paramSeparator+url.getQuery();
                URL floderUrl = new URL(urlStr);
                getListService(floderUrl,jsonArraay);//递归获取子目录的服务列表
            }
        }
    }

    /*
     * 分页获取service列表
     * */
    private JSONArray getPageListService(int page,int rows,JSONArray jsonArray) throws Exception {
        if(jsonArray == null){
            throw new  Exception("服务(service)列表为空！");
        }
        JSONArray resultJsonArray = new JSONArray();
        //添加为空判断  2018-3-13
        int start = (page - 1) * rows;
        int length = jsonArray.size();
        if ((rows * page) <= jsonArray.size()) {
            length = rows * page;
        } else {
            length = jsonArray.size();
        }
        for (int i = start; i < length; i++) {
            JSONObject itemJsonObject = jsonArray.getJSONObject(i);
            resultJsonArray.add(itemJsonObject);
        }
        return resultJsonArray;
    }
    @RequestMapping("/getServer")
    public String getServer(String page, String rows, String ip, String port, String name, String password) {
        String key = ip + port + name + password + "1";
        String values = stringRedisTemplate.opsForValue().get("IAgSite:" + key);
       // String values = stringRedisTemplate.opsForValue().get("IAgSite:" + key);
        JSONArray jsonArray1 = new JSONArray();
        if (!Common.isCheckNull(values)) {
            jsonArray1 = JSONArray.fromObject(values);
        } else {
            String token = loginin(name, password, ip, port);
            if (token.indexOf("geoserver") == -1) {
                jsonArray1 = reports(token, ip, port);
                for (int i = 0; i < jsonArray1.size(); ++i) {
                    String type = jsonArray1.getJSONObject(i).getString("type");
                    if (type.indexOf("Map") == -1 && type.indexOf("WMS") == -1 && type.indexOf("WFS") == -1 && type.indexOf("Feature") == -1 && type.indexOf("WMTS") == -1) {
                        jsonArray1.remove(i);
                        --i;
                    }
                }
                stringRedisTemplate.opsForValue().set("IAgSite:" + key, jsonArray1.toString());
            } else {
                return JsonUtils.toJson(new ResultForm(false));
            }
        }
        JSONArray jsonarray = new JSONArray();
        JSONObject jsonobj = new JSONObject();
        for (int i = 0; i < jsonArray1.size(); i++) {
            jsonobj.put("serviceName", jsonArray1.getJSONObject(i).getString("serviceName"));
            jsonobj.put("folderName", jsonArray1.getJSONObject(i).getString("folderName"));
            jsonobj.put("status", jsonArray1.getJSONObject(i).getJSONObject("status").getString("configuredState"));
            jsonobj.put("type", jsonArray1.getJSONObject(i).getString("type"));
            jsonarray.add(jsonobj);

        }
        int start = 0;
        int length = jsonarray.size();
        start = (Integer.parseInt(page) - 1) * Integer.parseInt(rows);
        if (Integer.parseInt(rows) * Integer.parseInt(page) <= jsonarray.size()) {
            length = Integer.parseInt(rows) * Integer.parseInt(page);
        } else {
            length = jsonarray.size();
        }
        JSONArray jsonarray2 = new JSONArray();
        for (int i = start; i < length; i++) {
            jsonarray2.add(jsonarray.get(i));
        }

        String json = "{\"total\":" + jsonarray.size() + ",\"rows\":" + jsonarray2 + "}";
        return json;

    }

    @RequestMapping("/creatser")
    public ResultForm creatser(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        String sercatalog = request.getParameter("sercatalog");
        String mxd = request.getParameter("mxd");
        String sertype = request.getParameter("sertype");

        String token = loginin(name, password, ip, port);
        url = "https://" + ip + ":" + port + "/arcgis/admin/services/createService";
        String serviceName = request.getParameter("serviceName");
        String serTypeName = request.getParameter("serTypeName");
        String result = createService(url, ip, port, token, serviceName, sercatalog, mxd, sertype, serTypeName);
        if (result.indexOf("success") != -1) {
            return new ResultForm(true, result);
        }
        else {
            return new ResultForm(false);
        }

    }

    public String generateToken(String username, String passWord, String client, String ip, String referer, String expiration, String ips, String port) {
        try {
            String url = "https://" + ips + ":" + port + "/arcgis/admin/generateToken";
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
                url = "http://" + ips + ":" + port + "/arcgis/admin/generateToken";
                result = defaultHttpClientUtil.doPost(url, createMap, charset);
            }
            return result;
        } catch (Exception e) {
            return null;
        }

    }

    public String loginin(String name, String password, String ip, String port) {
        //portal
//			defaultHttpClientUtil = new defaultHttpClientUtil();
        //String url = "https://192.168.30.199/arcgis/sharing/rest/generateToken";
        //arcgis server
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
            String localname = ia.getHostName();
            String localip = ia.getHostAddress();
            String generateToken = generateToken(name, password, "requestip", localip, "", "60", ip, port);
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


    public JSONArray reports(String token, String ip, String port) {
        //String url = "https://192.168.30.195:6443/arcgis/admin/services/report?parameters=%5B%22description%22%2C%22iteminfo%22%2C%22properties%22%2C%22status%22%2C%22permissions%22%2C%22instances%22%2C%22extensions%22%5D"  + "&f=json&token=" + token;
        if (token != null) {
            String urls = "https://" + ip + ":" + port + "/arcgis/admin/services?f=pjson&token=" + token;
            String url = "https://" + ip + ":" + port + "/arcgis/admin/services/report?parameters=%5B%22extensions%22%2C%22status%22%5D" + "&f=json&token=" + token;
            String result = defaultHttpClientUtil.doGet(url, charset);
            if (result == null) {
                urls = "http://" + ip + ":" + port + "/arcgis/admin/services?f=pjson&token=" + token;
                url = "http://" + ip + ":" + port + "/arcgis/admin/services/report?parameters=%5B%22extensions%22%2C%22status%22%5D" + "&f=json&token=" + token;
                result = defaultHttpClientUtil.doGet(url, charset);
            }
            JSONObject rejson1 = JSONObject.fromObject(result);
            JSONArray rejsonArray = JSONArray.fromObject(rejson1.values());
            JSONArray rejsonArray1 = rejsonArray.getJSONArray(0);
            String resultS = defaultHttpClientUtil.doGet(urls, charset);
            JSONObject json1 = JSONObject.fromObject(resultS);
            JSONArray json2 = json1.getJSONArray("folders");
            if (json2.size() > 0) {
                for (int i = 0; i < json2.size(); i++) {
                    String resultS1;
                    try {
                        String urls1 = "https://" + ip + ":" + port + "/arcgis/admin/services/" + json2.getString(i) + "/report?parameters=%5B%22extensions%22%2C%22status%22%5D" + "&f=json&token=" + token;
                        resultS1 = defaultHttpClientUtil.doGet(urls1, charset);
                    } catch (Exception e) {
                        resultS1 = null;
//                        String urls1 = "http://" + ip + ":" + port + "/arcgis/admin/services/" + json2.getString(i) + "/report?parameters=%5B%22extensions%22%2C%22status%22%5D" + "&f=json&token=" + token;
//                        resultS1 = defaultHttpClientUtil.doGet(urls1, charset);
                    }
                    if (resultS1 == null) {
                        String urls1 = "http://" + ip + ":" + port + "/arcgis/admin/services/" + json2.getString(i) + "/report?parameters=%5B%22extensions%22%2C%22status%22%5D" + "&f=json&token=" + token;
                        resultS1 = defaultHttpClientUtil.doGet(urls1, charset);
                    }
                    JSONObject json1s = JSONObject.fromObject(resultS1);
                    JSONArray jsonArray = JSONArray.fromObject(json1s.values());
                    JSONArray jsonArray1 = jsonArray.getJSONArray(0);
                    if (jsonArray1.size() > 0) {
                        for (int k = 0; k < jsonArray1.size(); k++) {
                            rejsonArray1.add(jsonArray1.getJSONObject(k));
                        }
                    }
                }

            }
            System.out.println(result);
            return rejsonArray1;
        }
        return null;

    }

    @RequestMapping("/getFeature")
    public ResultForm getFeature(String url, String name, String password) {
        if (!ReadUrlUtil.checkUrl(url)) {
            return new ResultForm(false);
        }
        JSONArray feildsa = new JSONArray();
        if (url.indexOf("arcgis") != -1) {
            if (url.indexOf("WFS") != -1) {
                if (url.indexOf("request=GetCapabilities") == -1) {
                    url += "?request=GetCapabilities&service=WFS";
                }
                Map<String, String> Feature = ReadUrlUtil.queryOGC_WFSLayerTable(url);
                int i = 0;
                for (String key : Feature.keySet()) {
                    JSONObject Json1 = new JSONObject();
                    String value = Feature.get(key);
                    String a[] = url.split("WFSServer");
                    String urls = a[0];
                    String b[] = urls.split("arcgis");
                    String urls1 = b[0] + "arcgis/rest" + b[1] + i + "?f=pjson";
                    String result2 = defaultHttpClientUtil.doGet(urls1, charset);
                    JSONObject json2 = JSONObject.fromObject(result2);
                    Json1.put("layerTable", json2.getString("id"));
                    Json1.put("name", json2.getString("name"));
                    String urls3 = b[0] + "arcgis/rest" + b[1] + "?f=pjson";
                    String result3 = defaultHttpClientUtil.doGet(urls3, charset);
                    JSONObject json3 = JSONObject.fromObject(result3);
                    String extent = "[[" + json3.getJSONObject("initialExtent").getString("ymin") + "],[" + json3.getJSONObject("initialExtent").getString("xmin") + "],[" + json3.getJSONObject("initialExtent").getString("ymax") + "],[" + json3.getJSONObject("initialExtent").getString("xmax") + "],]";
                    Json1.put("extent", extent);
                    String geometryType = json2.getString("geometryType");
                    if (geometryType.toLowerCase().indexOf("line") != -1) {
                        Json1.put("geometryType", "polyline");
                    } else if (geometryType.toLowerCase().indexOf("point") != -1) {
                        Json1.put("geometryType", "point");
                    } else if (geometryType.toLowerCase().indexOf("polygon") != -1 || geometryType.toLowerCase().indexOf("surface") != -1) {
                        Json1.put("geometryType", "polygon");
                    } else {
                        Json1.put("geometryType", "undefine");
                    }

                    feildsa.add(Json1);
                    i++;
                }
            } else if (url.indexOf("WMS") != -1) {
                if (url.indexOf("request=GetCapabilities") == -1) {
                    url += "?request=GetCapabilities&service=WMS";
                }
                Map<String, String> Feature = ReadUrlUtil.queryWMSLayerTable(url);
                int i = 0;
                for (String key : Feature.keySet()) {
                    JSONObject Json1 = new JSONObject();
                    String value = Feature.get(key);
                    Json1.put("layerTable", key);
                    Json1.put("name", value);
                    String a[] = url.split("WMSServer");
                    String urls = a[0];
                    String b[] = urls.split("arcgis");
                    String urls1 = b[0] + "arcgis/rest" + b[1] + i + "?f=pjson";
                    String result2 = defaultHttpClientUtil.doGet(urls1, charset);
                    JSONObject json2 = JSONObject.fromObject(result2);
                    String urls3 = b[0] + "arcgis/rest" + b[1] + "?f=pjson";
                    String result3 = defaultHttpClientUtil.doGet(urls3, charset);
                    JSONObject json3 = JSONObject.fromObject(result3);
                    String extent = "[[" + json3.getJSONObject("initialExtent").getString("ymin") + "],[" + json3.getJSONObject("initialExtent").getString("xmin") + "],[" + json3.getJSONObject("initialExtent").getString("ymax") + "],[" + json3.getJSONObject("initialExtent").getString("xmax") + "],]";
                    Json1.put("extent", extent);
                    String geometryType = json2.getString("geometryType");
                    if (geometryType.toLowerCase().indexOf("line") != -1) {
                        Json1.put("geometryType", "polyline");
                    } else if (geometryType.toLowerCase().indexOf("point") != -1) {
                        Json1.put("geometryType", "point");
                    } else if (geometryType.toLowerCase().indexOf("polygon") != -1 || geometryType.toLowerCase().indexOf("surface") != -1) {
                        Json1.put("geometryType", "polygon");
                    } else {
                        Json1.put("geometryType", "undefine");
                    }
                    feildsa.add(Json1);
                    i++;
                }
            } else if (url.indexOf("WMTS") != -1) {
                String urlss = url + "/1.0.0/WMTSCapabilities.xml";
                Map<String, String> Feature = ReadUrlUtil.queryWMTSLayerTable(urlss);
                int i = 0;
                for (String key : Feature.keySet()) {
                    JSONObject Json1 = new JSONObject();
                    String value = Feature.get(key);
                    Json1.put("layerTable", key);
                    Json1.put("name", value);
                    String a[] = url.split("WMTS");
                    String urls = a[0];
                    String urls1 = urls + i + "?f=pjson";
                    String result2 = defaultHttpClientUtil.doGet(urls1, charset);
                    JSONObject json2 = JSONObject.fromObject(result2);
                    String urls3 = urls + "?f=pjson";
                    String result3 = defaultHttpClientUtil.doGet(urls3, charset);
                    JSONObject json3 = JSONObject.fromObject(result3);
                    String extent = "[[" + json3.getJSONObject("initialExtent").getString("ymin") + "],[" + json3.getJSONObject("initialExtent").getString("xmin") + "],[" + json3.getJSONObject("initialExtent").getString("ymax") + "],[" + json3.getJSONObject("initialExtent").getString("xmax") + "],]";
                    Json1.put("extent", extent);
                    String geometryType = json2.getString("geometryType");
                    if (geometryType.toLowerCase().indexOf("line") != -1) {
                        Json1.put("geometryType", "polyline");
                    } else if (geometryType.toLowerCase().indexOf("point") != -1) {
                        Json1.put("geometryType", "point");
                    } else if (geometryType.toLowerCase().indexOf("polygon") != -1 || geometryType.toLowerCase().indexOf("surface") != -1) {
                        Json1.put("geometryType", "polygon");
                    } else {
                        Json1.put("geometryType", "undefine");
                    }
                    feildsa.add(Json1);
                    i++;
                }
            } else {
                String urls = url + "?f=pjson";
                String result = defaultHttpClientUtil.doGet(urls, charset);
                JSONObject json1 = JSONObject.fromObject(result);
                JSONArray layers = json1.getJSONArray("layers");
                System.out.print(layers);
                for (int i = 0; i < layers.size(); i++) {
                    JSONObject Json1 = new JSONObject();
                    Json1.put("layerTable", layers.getJSONObject(i).getString("id"));
                    Json1.put("name", layers.getJSONObject(i).getString("name"));
                    String url1 = url + "/" + i + "?f=pjson";
                    String result2 = defaultHttpClientUtil.doGet(url1, charset);
                    JSONObject json2 = JSONObject.fromObject(result2);
                    String urls3 = url + "?f=pjson";
                    String result3 = defaultHttpClientUtil.doGet(urls3, charset);
                    JSONObject json3 = JSONObject.fromObject(result3);
                    String extent = "[[" + json3.getJSONObject("initialExtent").getString("ymin") + "],[" + json3.getJSONObject("initialExtent").getString("xmin") + "],[" + json3.getJSONObject("initialExtent").getString("ymax") + "],[" + json3.getJSONObject("initialExtent").getString("xmax") + "],]";
                    Json1.put("extent", extent);
                    String geometryType = json2.getString("geometryType");
                    if (geometryType.toLowerCase().indexOf("line") != -1) {
                        Json1.put("geometryType", "polyline");
                    } else if (geometryType.toLowerCase().indexOf("point") != -1) {
                        Json1.put("geometryType", "point");
                    } else if (geometryType.toLowerCase().indexOf("polygon") != -1 || geometryType.toLowerCase().indexOf("surface") != -1) {
                        Json1.put("geometryType", "polygon");
                    } else {
                        Json1.put("geometryType", "undefine");
                    }
                    feildsa.add(Json1);
                }
            }
        } else if (url.indexOf("geoserver") != -1) {
            if (url.indexOf("workspaces") != -1) {
                String url1 = url + ".json";
                String result1 = defaultHttpClientUtil.dorzGet(url1, charset);
                JSONObject feature = JSONObject.fromObject(result1);
                JSONObject layers1 = feature.getJSONObject("featureTypes");
                JSONArray layers = layers1.getJSONArray("featureType");
                for (int i = 0; i < layers.size(); i++) {
                    JSONObject Json1 = new JSONObject();
                    String url1s = layers.getJSONObject(i).getString("href");
                    String result2 = defaultHttpClientUtil.dorzGet(url1s, charset);
                    JSONObject json2 = JSONObject.fromObject(result2);
                    JSONObject json4 = json2.getJSONObject("featureType");
                    JSONObject json3 = json4.getJSONObject("attributes").getJSONArray("attribute").getJSONObject(0);
                    String geometryType = json3.getString("binding");
                    if (geometryType.toLowerCase().indexOf("line") != -1 || geometryType.toLowerCase().indexOf("geometry") != -1) {
                        Json1.put("geometryType", "polyline");
                    } else if (geometryType.toLowerCase().indexOf("point") != -1) {
                        Json1.put("geometryType", "point");
                    } else if (geometryType.toLowerCase().indexOf("polygon") != -1 || geometryType.toLowerCase().indexOf("surface") != -1) {
                        Json1.put("geometryType", "polygon");
                    } else {
                        Json1.put("geometryType", "undefine");
                    }

                    Json1.put("layerTable", json4.getString("name"));
                    Json1.put("name", json4.getString("name"));
                    feildsa.add(Json1);
                }
            } else {
                String a[] = url.split("\\?");
                String url1 = a[0];
                String b[] = a[1].split("typeName=");
                String c[] = b[1].split("&");
                String typename = c[0];
                String urls = url1 + "?service=WFS&version=1.0.0&request=GetFeature&typeName=" + typename + "&maxFeatures=200&outputFormat=json";
                String types1 = defaultHttpClientUtil.dorzGet(urls, charset);
                JSONObject json = JSONObject.fromObject(types1);
                String types = json.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getString("type");
                JSONObject Json1 = new JSONObject();
                String d[] = url.split("/geoserver/");
                String e[] = typename.split(":");
                String urlgeo = d[0] + "/geoserver/rest/layers/" + e[1] + ".json";
                String results1 = defaultHttpClientUtil.dorzGet(urlgeo, charset);
                JSONObject jsons1 = JSONObject.fromObject(results1);
                String urlgeo1 = jsons1.getJSONObject("layer").getJSONObject("resource").getString("href");
                String results2 = defaultHttpClientUtil.dorzGet(urlgeo1, charset);
                JSONObject jsons2 = JSONObject.fromObject(results2);
                String extent = "[[" + jsons2.getJSONObject("featureType").getJSONObject("latLonBoundingBox").getString("miny") + "],[" + jsons2.getJSONObject("featureType").getJSONObject("latLonBoundingBox").getString("minx") + "],[" + jsons2.getJSONObject("featureType").getJSONObject("latLonBoundingBox").getString("maxy") + "],[" + jsons2.getJSONObject("featureType").getJSONObject("latLonBoundingBox").getString("maxx") + "],]";
                Json1.put("layerTable", typename);
                Json1.put("name", typename);
                Json1.put("extent", extent);
                if (types == "1") {
                    Json1.put("geometryType", "point");
                } else if (types.equals("3")) {
                    Json1.put("geometryType", "polyline");
                } else if (types.equals("4")) {
                    Json1.put("geometryType", "polygon");
                } else {
                    Json1.put("geometryType", "undefine");
                }
                feildsa.add(Json1);
            }
        }
        if (feildsa.size() != 0) {
            return new ResultForm(true, feildsa.toString());
        }
        return new ResultForm(false);
    }

    public String createService(String url, String ip, String port, String token, String serviceName, String ser_catalog, String mxd, String sertype, String serTypeName) {
        Map<String, String> createMap = new HashMap<String, String>();
        Map<String, String> geometry = new HashMap<String, String>();
        Map<String, String> properties = new HashMap<String, String>();
        Map<String, String> extensions = new HashMap<String, String>();
        Map<String, String> propertiesa = new HashMap<String, String>();
        geometry.put("serviceName", serviceName);
        geometry.put("type", sertype);
        geometry.put("description", "mymapservice");
        geometry.put("capabilities", "map");
        geometry.put("provider", "ArcObjects");
        geometry.put("clusterName", "default");
        geometry.put("minInstancesPerNode", "1");
        geometry.put("maxInstancesPerNode", "2");
        geometry.put("instancesPerContainer", "1");
        geometry.put("maxWaitTime", "60");
        geometry.put("maxStartupTime", "300");
        geometry.put("maxIdleTime", "1800");
        geometry.put("maxUsageTime", "600");
        geometry.put("recycleInterval", "24");
        geometry.put("loadBalancing", "ROUND_ROBIN");
        geometry.put("isolationLevel", "HIGH");
        geometry.put("configuredState", "STARTED");
//        geometry.put("recycleStartTime", "00:00");
        geometry.put("keepAliveInterval", "1800");
        geometry.put("private", "false");
        geometry.put("isDefault", "false");
        geometry.put("maxUploadFileSize", "0");
        //geometry.put("allowedUploadFileTypes", "");
        properties.put("maxDomainCodeCount", "25000");
        properties.put("cacheDir", "'C:\\\\arcgisserver\\\\directories\\\\arcgiscache'");
        properties.put("maxImageWidth", "4096");
        properties.put("maxRecordCount", "1000");
        properties.put("antialiasingMode", "None");
        properties.put("enableDynamicLayers", "false");
//        properties.put("dynamicDataWorkspaces", "");
        properties.put("isCached", "false");
        properties.put("hasStaticData", "true");
        properties.put("virtualOutputDir", "'/rest/directories/arcgisoutput'");
        properties.put("exportTilesAllowed", "false");
        properties.put("maxImageHeight", "4096");
        properties.put("cacheOnDemand", "false");
        properties.put("minScale", "288895.27714399999");
        properties.put("schemaLockingEnabled", "true");
        properties.put("useLocalCacheDir", "true");
        properties.put("outputDir", "'C:\\\\arcgisserver\\\\directories\\\\arcgisoutput'");
        properties.put("maxScale", "4513.9887049999998");
        // properties.put("filePath", "'C:\\\\arcgisserver\\\\directories\\\\arcgissystem\\\\arcgisinput\\\\testwfs.MapServer\\\\extracted\\\\v101\\\\testwfs.msd'");
        properties.put("filePath", "'" + mxd + "'");
        properties.put("supportedImageReturnTypes", "URL");
        properties.put("clientCachingAllowed", "true");
        properties.put("textAntialiasingMode", "Force");
        properties.put("maxExportTilesCount", "100000");
        properties.put("textAntialiasingMode", "Force");
        properties.put("ignoreCache", "false");
        properties.put("maxBufferCount", "100");
        properties.put("disableIdentifyRelates", "false");
        properties.put("virtualCacheDir", "'/rest/directories/arcgiscache'");
        propertiesa.put("appSchemaPrefix", "arcgis");
        propertiesa.put("appSchemaURI", "'http://192.168.30.197:6080/arcgis'");
        propertiesa.put("enableTransactions", "false");
        String result =null;
        try {
            String serarr[] = serTypeName.split("、");
            JSONArray extension = new JSONArray();
            if (serarr.length > 1) {
                for (int i = 1; i < serarr.length; i++) {
                    JSONObject extens = new JSONObject();
                    extens.put("typeName", serarr[i]);
                    extens.put("enabled", "true");
                    extens.put("capabilities", "null");
                    extens.put("maxUploadFileSize", "0");
                    if ("WFS".equals(serarr[i])) {
                        JSONObject extens1 = new JSONObject();
                        extens1.put("appSchemaPrefix", serviceName);
                        extens1.put("appSchemaURI", "'http://" + ip + ":" + port + "/arcgis'");
                        extens1.put("enableTransactions", "false");
                        extens.put("properties", extens1.toString());
                    }
                    extension.add(extens);
                }
            }
            geometry.put("properties", properties.toString());
            geometry.put("extensions", JSONArray.fromObject(extension).toString());
            System.out.println(url);
            createMap.put("service", geometry.toString());
            createMap.put("f", "pjson");
            createMap.put("token", token);
            System.out.println(createMap);
            result = defaultHttpClientUtil.doPost(url, createMap, charset);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    //IAgSite

    @RequestMapping("/orgs")
    public String list() throws Exception {
        //List<AgDic> LIST = iAgDic.getAgDicByTypeCode("95389892-58c0-4cc9-b046-e1f8b70d6f41");//数据字典中 锁定编码集A119为服务站点编码
       /* Map<String, String> param = new HashMap<>();
        param.put("page", "1");
        param.put("rows", "999");
        param.put("sort", "itemSortNo");
        param.put("order", "asc");
        param.put("typeId", "95389892-58c0-4cc9-b046-e1f8b70d6f41");

        HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/bsc/dic/code/listItems.do", param);
        JSONObject jsonObject = JSONObject.fromObject(httpRespons.getContent());
        JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("rows"));
        List<AgDic> LIST = new AgDicConverter().convertToList(null, jsonArray);


        JSONArray feilds2 = new JSONArray();
        for (AgDic dic : LIST) {
            String file = dic.getValue();
            JSONObject Json = JSONObject.fromObject(file);
            JSONObject Json1 = new JSONObject();
            Json1.put("ip", Json.get("ip"));
            Json1.put("name", Json.get("name"));
            Json1.put("port", Json.get("port"));
            Json1.put("password", Json.get("password"));
            Json1.put("types", Json.get("type"));
            Json1.put("alias", dic.getName());
            Json1.put("restport", Json.get("restport"));
            Json1.put("code", dic.getId());
            feilds2.add(Json1);
        }
        JSONObject Json = new JSONObject();
        Json.put("tatol", feilds2.size() * 4);
        Json.put("rows", feilds2.toString());
        return Json.toString();*/
        return null;
    }

    /*@RequestMapping("/saveser")
    public String saveser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        String name = request.getParameter("ser_name");
        String password = request.getParameter("ser_psssword");
        String ip = request.getParameter("ser_ip");
        String port = request.getParameter("ser_port");
        String restport = request.getParameter("ser_restport");
        String alias = request.getParameter("ser_alias");
        String type = request.getParameter("ser_types");
        String code1 = request.getParameter("ser_code");

        if (StringUtils.isEmpty(id) || "-1".equals(id)) {
            JSONObject Json1 = new JSONObject();
            Json1.put("ip", ip);
            Json1.put("name", name);
            Json1.put("port", port);
            Json1.put("password", password);
            Json1.put("type", type);
            Json1.put("alias", alias);
            Json1.put("restport", restport);
            AgDic dic = new AgDic();
            dic.setValue(Json1.toString());
            dic.setName(alias);
            dic.setTypeName("服务站点");
            String code = iAgDic.getMaxCode("95389892-58c0-4cc9-b046-e1f8b70d6f41");
            dic.setCode(code);
            dic.setTypeCode("95389892-58c0-4cc9-b046-e1f8b70d6f41");
            try {
                AgDic temp = iAgDic.findAgDicByCode(dic.getCode());
                if (temp != null && !temp.getId().equals(dic.getId()))
                    return JsonUtils.toJson(new ResultForm(false, "该编码已存在！"));
                if (StringUtils.isEmpty(dic.getId())) {
                    dic.setId(UUID.randomUUID().toString());
                    iAgDic.saveAgDic(dic);
                } else {
                    iAgDic.updateAgDic(dic);
                }
                return JsonUtils.toJson(new ResultForm(true));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return JsonUtils.toJson(new ResultForm(false, "保存失败！"));

        } else {
            AgDic temp = iAgDic.findAgDicByCode(code1);
            JSONObject Json1 = new JSONObject();
            Json1.put("ip", ip);
            Json1.put("name", name);
            Json1.put("port", port);
            Json1.put("password", password);
            Json1.put("type", type);
//            Json1.put("alias", alias);
            Json1.put("restport", restport);
            temp.setValue(Json1.toString());
            temp.setName(alias);
            try {
                AgDic temps = iAgDic.findAgDicByCode(temp.getCode());
                if (temps != null && !temps.getId().equals(temp.getId()))
                    return JsonUtils.toJson(new ResultForm(false, "该编码已存在！"));
                if (StringUtils.isEmpty(temp.getId())) {
                    temp.setId(UUID.randomUUID().toString());
                    iAgDic.saveAgDic(temp);
                } else {
                    iAgDic.updateAgDic(temp);
                }
                return JsonUtils.toJson(new ResultForm(true));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return JsonUtils.toJson(new ResultForm(false, "保存失败！"));
        }

    }*/

   /* @RequestMapping("/deleteser")
    public String deleteser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        AgDic temp = iAgDic.findAgDicById(id);
        if (temp == null) return JsonUtils.toJson(new ResultForm(false, "该站点不存在, 删除失败！"));
        if (id != null) {
            String[] ids = null;
            if (temp != null && StringUtils.isNotEmpty(temp.getId())) {
                ids = temp.getId().split(",");
            }
            try {
                if (ids != null && ids.length > 0) {
                    iAgDic.deleteAgDicBatch(ids);
                }
                return JsonUtils.toJson(new ResultForm(true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return JsonUtils.toJson(new ResultForm(false));

        }
        return JsonUtils.toJson(new ResultForm(true));
    }*/

    @RequestMapping("/getSerFlod")
    public String getSerFlod(String ip, String restport) {
        String url = "http://" + ip + ":" + restport + "/arcgis/rest/services?f=pjson";
        String result = defaultHttpClientUtil.doGet(url, charset);
        JSONObject result1 = JSONObject.fromObject(result);
        JSONArray folders = result1.getJSONArray("folders");
        JSONArray folder = new JSONArray();
        JSONObject Json2 = new JSONObject();
        Json2.put("folder", "root(根目录)");
        folder.add(Json2);
        if (folders.size() > 0) {
            for (int i = 0; i < folders.size(); i++) {
                JSONObject Json1 = new JSONObject();
                Json1.put("folder", folders.getString(i));
                folder.add(Json1);
            }
        }
        return folder.toString();

    }

    //清除缓存
    @RequestMapping("/refreshser")
    public String refreshser(String ip, String port, String name, String password) {
        String key = ip + port + name + password;
        String key1 = ip + port + name + password + "1";//连带着有权限与无权限的同一站点一起清除缓存
        try {
                RedisAdaptor.getRedis().remove("IAgSite:" + key);
                RedisAdaptor.getRedis().remove("IAgSite:" + key1);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.toJson(new ResultForm(false));
        }


        return JsonUtils.toJson(new ResultForm(true));
    }

    //得到图层信息
    @RequestMapping("/getcolum1/{ip}/{name}/{port}/{type}/{fold}")
    public JSONObject getcolum1(@PathVariable("ip") String ip, @PathVariable("name") String name, @PathVariable("port") String port, @PathVariable("type") String type, @PathVariable("fold") String fold) {
        url = "http://" + ip + ":"+port+"/arcgis/rest/services/" + fold + "/" + name + "/" + "/MapServer?f=pjson";
        String result = defaultHttpClientUtil.doGet(url, charset);
        JSONObject json1 = JSONObject.fromObject(result);
        if (json1.size() == 1) {
            return null;
        }
        // 2018-11-21 获取缓存类型
        JSONObject js = new JSONObject();
        if(json1.has("singleFusedMapCache")){
            boolean singleFusedMapCache = json1.getBoolean("singleFusedMapCache");
            if (singleFusedMapCache){
                js.put("layerType", "020302");
                js.put("layerTypeCn", "ArcGIS MapServer(Tile)");
                JSONObject tileInfo = json1.getJSONObject("tileInfo");
                String tileSize = tileInfo.getString("rows");
                String format = tileInfo.getString("format");
                js.put("tileSize", tileSize);
                js.put("format", format);
            }else {
                js.put("layerType", "020202");
                js.put("layerTypeCn", "ArcGIS MapServer(Dynamic)");
            }

        }

        return js;
    }

    //得到图层信息
    @RequestMapping("/getcolum/{ip}/{name}/{port}/{type}/{fold}")
    public JSONArray getcolum(@PathVariable("ip") String ip, @PathVariable("name") String name, @PathVariable("port") String port, @PathVariable("type") String type, @PathVariable("fold") String fold,int parentId,int len) {
        url = "http://" + ip + ":"+port+"/arcgis/rest/services/" + fold + "/" + name + "/" + "/MapServer?f=pjson";
        String result = defaultHttpClientUtil.doGet(url, charset);
        JSONObject json1 = JSONObject.fromObject(result);
        if (json1.size() == 1) {
            return null;
        }
        JSONArray layers = json1.getJSONArray("layers");
        JSONObject spatial = json1.getJSONObject("spatialReference");
        JSONObject extents = json1.getJSONObject("initialExtent");
        String extent = "[[" + extents.getString("ymin") + "],[" + extents.getString("xmin") + "],[" + extents.getString("ymax") + "],[" + extents.getString("xmax") + "],]";
        JSONArray feilds = new JSONArray();
        for (int i = 0; i < layers.size(); i++) {
            JSONObject Json1 = new JSONObject();
            // 获取服务下的图层
            JSONObject layer = layers.getJSONObject(i);
            // 获取父id
            int parentLayerId = layer.getInt("parentLayerId");

            Object subLayerIds = layer.get("subLayerIds");
            // 如果是上级目录  判断空 JSONNull
            if (subLayerIds instanceof JSONNull && parentLayerId == -1){
                Json1.put("pid",parentId);
            }
            // subLayerIds 为null并且parentLayerId不等于-1是子图层  则设置父id
            if (subLayerIds instanceof JSONNull && parentLayerId != -1){
                Json1.put("pid",parentLayerId+len);
            }
            // 含有子图层，则为父目录
            if(subLayerIds instanceof JSONArray){
                JSONObject parentJson = new JSONObject();
                parentJson.put("name", layers.getJSONObject(i).getString("name"));
                parentJson.put("serviceName", name);
                parentJson.put("pid",parentId);
                parentJson.put("id",i+len);
                parentJson.put("dir","dir");
                parentJson.put("extent", "");
                parentJson.put("extents", "");
                parentJson.put("wkid", "unknow");
                parentJson.put("geometryType", "");
                parentJson.put("sid", "");
                feilds.add(parentJson);
                continue;
            }


            String url1 = "http://" + ip + ":"+port+"/arcgis/rest/services/" + fold + "/" + name + "/" + "/MapServer/" + i + "?f=pjson";
            String result2 = defaultHttpClientUtil.doGet(url1, charset);
            JSONObject json2 = JSONObject.fromObject(result2);
            String geometryType = json2.getString("geometryType");
            String extents1 = "[[" + json2.getJSONObject("extent").getString("ymin") + "],[" + json2.getJSONObject("extent").getString("xmin") + "],[" + json2.getJSONObject("extent").getString("ymax") + "],[" + json2.getJSONObject("extent").getString("xmax") + "],]";

            /*if ("null".equals(geometryType)) {
                continue;
            }*/

            if (geometryType.toLowerCase().indexOf("line") != -1) {
                Json1.put("geometryType", "polyline");
            } else if (geometryType.toLowerCase().indexOf("point") != -1) {
                Json1.put("geometryType", "point");
            } else if (geometryType.toLowerCase().indexOf("polygon") != -1 || geometryType.toLowerCase().indexOf("surface") != -1) {
                Json1.put("geometryType", "polygon");
            }
            // 因为服务接口的id不是唯一递增，所以需要设置加一个常量值，保证图层id不与父目录的id重复，前端treegrid才能正常遍历
            // 这个值是根据父目录数的经验值得到，
            Json1.put("id",i+1000+len);
            Json1.put("sid", layers.getJSONObject(i).getString("id"));
            Json1.put("name", layers.getJSONObject(i).getString("name"));
            Json1.put("serviceName", name);
            Json1.put("type", type);
            Json1.put("dir", "nodir");
            if (spatial.containsKey("wkid")) {
                Json1.put("wkid", spatial.getString("wkid"));
            } else if (spatial.containsKey("wkt")) {
                String wkt = spatial.getString("wkt");
                Json1.put("wkid", wkt.substring(wkt.indexOf("GEOGCS[\"") + 1, wkt.indexOf("\",DATUM")));
            } else {
                Json1.put("wkid", "unknow");
            }

            Json1.put("extent", extent);
            Json1.put("extents", extents1);
            feilds.add(Json1);
        }
        return feilds;
    }
    //获取站点名称接口
    @RequestMapping("/getSiteName/{newValue}")
    public String getSiteName(@PathVariable("newValue") String newValue) throws Exception {
       // List<AgSite> list = IAgSite.getSiteName(newValue);
        //List<AgDic> LIST = iAgDic.getAgDicByTypeCode("95389892-58c0-4cc9-b046-e1f8b70d6f41");

        Map<String, String> param = new HashMap<>();
        param.put("page", "1");
        param.put("rows", "999");
        param.put("sort", "itemSortNo");
        param.put("order", "asc");
        param.put("typeId", "95389892-58c0-4cc9-b046-e1f8b70d6f41");

        //HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/bsc/dic/code/listItems.do", param);
        //JSONObject jsonObject = JSONObject.fromObject(httpRespons.getContent());
       // JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("rows"));
        List<AgDic> LIST = new ArrayList<>();// new AgDicConverter().convertToList(null, jsonArray);
        JSONArray jsonarray = new JSONArray();
        for (AgDic map : LIST) {
            String file = map.getValue();
            JSONObject Json = JSONObject.fromObject(file);
            if (Json.get("type").equals(newValue)) {
                JSONObject Json1 = new JSONObject();
                Json1.put("serAlias", Json.get("alias"));
                Json1.put("serType", Json.get("type"));
                Json1.put("serIp", Json.get("ip"));
                Json1.put("serPort", Json.get("port"));
                Json1.put("SerName", Json.get("name"));
                Json1.put("SerPassword", Json.get("password"));
                Json1.put("SerRestport", Json.get("restport"));
                Json1.put("code", map.getCode());
                jsonarray.add(Json1);
            }
        }
        return jsonarray.toString();

    }

    //拼接站点信息
    @RequestMapping("/getSitemessa/{newValue}")
    public String getSitemessa(@PathVariable("newValue") String newValue) throws Exception {
        List<AgDic> LIST = iAgDic.getAgDicByTypeCode("95389892-58c0-4cc9-b046-e1f8b70d6f41");
        JSONArray jsonarray = new JSONArray();
        for (AgDic map : LIST) {
            String file = map.getValue();
            JSONObject Json = JSONObject.fromObject(file);
            if (Json.get("type").equals(newValue)) {
                JSONObject Json1 = new JSONObject();
                Json1.put("serAlias", map.getName());
                Json1.put("serType", Json.get("type"));
                Json1.put("serIp", Json.get("ip"));
                Json1.put("serPort", Json.get("port"));
                Json1.put("SerName", Json.get("name"));
                Json1.put("SerPassword", Json.get("password"));
                Json1.put("SerRestport", Json.get("restport"));
                Json1.put("code", map.getCode());
                if (Json.get("name") != null) {
                    String test = checkSite(Json.get("ip").toString(), Json.get("port").toString(), Json.get("restport").toString(), Json.get("name").toString(), Json.get("password").toString());
                    if ("1".equals(test)) {
                        Json1.put("serStaue", "启动");
                    } else if ("2".equals(test)) {
                        Json1.put("serStaue", "启动但权限不可用");
                    } else {
                        Json1.put("serStaue", "未启动");
                    }
                    Json1.put("serRoot", "拥有");
                } else {
                    String test = checkSite(Json.get("ip").toString(), Json.get("port").toString(), Json.get("restport").toString(), Json.get("name").toString(), Json.get("password").toString());
                    if ("3".equals(test)) {
                        Json1.put("serStaue", "启动");
                    } else {
                        Json1.put("serStaue", "未启动");
                    }
                    Json1.put("serRoot", "未拥有");
                }
                jsonarray.add(Json1);
            }
        }
        return jsonarray.toString();

    }

    //检查站点的可用性
    @RequestMapping("/checkSite")
    public String checkSite(String ip, String port, String Restport, String name, String password) {
        if (name != null) {
            String url = "http://" + ip + ":" + Restport + "/arcgis/rest/services/";
            if (ReadUrlUtil.checkUrl(url)) {
                String token = loginin(name, password, ip, port);
                if ("geoserver".equals(token)) {
                    return "2";
                } else {
                    return "1";
                }
            } else {
                url = "http://" + ip + ":" + Restport + "/geoserver/web/";
                if (ReadUrlUtil.checkUrl(url)) {
                    return "1";
                }
                return "5";
            }

        } else {
            String url = "http://" + ip + ":" + Restport + "/arcgis/rest/services/";
            if (ReadUrlUtil.checkUrl(url)) {
                return "3";
            } else {
                return "4";
            }
        }
    }

    //获取站点信息
    @RequestMapping("/getSitemess")
    public String getSitemess(String serCode) throws Exception {
        //AgDic dic = iAgDic.findAgDicByCode(serCode);
        Map<String, String> param = new HashMap<>();
        param.put("page", "1");
        param.put("rows", "999");
        param.put("sort", "itemSortNo");
        param.put("order", "asc");
        param.put("typeId", "95389892-58c0-4cc9-b046-e1f8b70d6f41");

       // HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/bsc/dic/code/listItems.do", param);
        //JSONObject jsonObject = JSONObject.fromObject(httpRespons.getContent());
       // JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("rows"));
        List<AgDic> LIST = new ArrayList<>();//new AgDicConverter().convertToList(null, jsonArray);
        if(LIST!=null){
            for (AgDic dic:LIST){
                if (dic == null) return "{\"total\":0,\"rows\":[]}";
                if(dic.getId().equals(serCode)) {
                    JSONObject list = JSONObject.fromObject(dic.getValue());
                    String kss = "{\"total\":7,\"rows\":[{\"name\":\"站点名称\",\"value\":\"" + dic.getName() + "\",\"group\":\"基础信息\",\"editor\":\"text\"}," +
                            "{\"name\":\"站点类型\",\"value\":\"" + list.getString("type") + "\",\"group\":\"基础信息\",\"editor\":{\"type\":\"combobox\",\"options\":{\"textField\":\"value\",\"valueField\":\"value\",\"data\":[{\"value\":\"ArcGis\"},{\"value\":\"GeoServer\"}]}}}," +
                            "{\"name\":\"ip\",\"value\":\"" + list.getString("ip") + "\",\"group\":\"基础信息\",\"editor\":\"text\"}," +
                            "{\"name\":\"rest端口\",\"value\":\"" + list.getString("restport") + "\",\"group\":\"基础信息\",\"editor\":\"text\"}," +
                            "{\"name\":\"帐号\",\"value\":\"" + list.getString("name") + "\",\"group\":\"权限信息\",\"editor\":\"text\"}," +
                            "{\"name\":\"密码\",\"value\":\"" + list.getString("password") + "\",\"group\":\"权限信息\",\"editor\":\"text\" }," +
                            "{\"name\":\"管理端口\",\"value\":\"" + list.getString("port") + "\",\"group\":\"权限信息\"}]}";
                    return kss;
                }

            }
        }
        return null;

    }

    @RequestMapping("/getSite")
    public AgSite getSite(String serType, String serAlias) throws Exception {
        AgSite list = IAgSite.getSite(serType, serAlias);
        return list;

    }

    @RequestMapping("/checkUrl")
    public String checkUrl(String name, String password, String ip, String port, String restport) {
        String urlS = "http://" + ip + ":" + restport + "/arcgis/rest/services/";
        if (ReadUrlUtil.checkUrl(urlS)) {
            return "true";
//            String token = loginin(name, password, ip, port);
//            if (token.indexOf("geoserver") == -1 && token != null) {
//                return "true";
//            }
        } else {
            String urlS1 = "http://" + ip + ":" + restport + "/geoserver/wfs?service=wfs&version=1.1.0&request=GetCapabilities";
            if (ReadUrlUtil.checkUrl(urlS1)) {
                return "true";
            }
            return "false";
        }


    }

    @RequestMapping("/getGeoSelLayer")
    public String getGeoSelLayer(String reip, String restport, String sellayer) throws Exception {
        String a[] = sellayer.split("、");
        List<Map> dields = IAgSite.getregis();
        JSONArray feilds1 = new JSONArray();
        if (a.length > 0) {
            for (int i = 0; i < a.length; i++) {
                JSONObject Json1 = new JSONObject();
                String b[] = a[i].split(":");
                String Geoname = a[i];
                String urls = "http://" + reip + ":" + restport + "/geoserver/" + b[0] + "/ows?service=wfs&request=GetFeature&version=1.0.0&outputFormat=json&typename=" + Geoname;
                String results = defaultHttpClientUtil.doGet(urls, charset);
                JSONObject json1 = JSONObject.fromObject(results);
                JSONArray json2 = json1.getJSONArray("features");
                if (json2.getJSONObject(0).getJSONObject("geometry").getString("type").toLowerCase().indexOf("line") != -1) {
                    Json1.put("geometryType", "polyline");
                } else if (json2.getJSONObject(0).getJSONObject("geometry").getString("type").toLowerCase().indexOf("point") != -1) {
                    Json1.put("geometryType", "point");
                } else if (json2.getJSONObject(0).getJSONObject("geometry").getString("type").toLowerCase().indexOf("polygon") != -1 || json2.getJSONObject(0).getJSONObject("geometry").getString("type").toLowerCase().indexOf("surface") != -1) {
                    Json1.put("geometryType", "polygon");
                }
                Json1.put("geometryname", json2.getJSONObject(0).getString("geometry_name"));
                Json1.put("serviceName", a[i]);
                Json1.put("name", b[0]);
                String urlgeo = "http://" + reip + ":" + restport + "/geoserver/rest/layers/" + b[1] + ".json";
                String results1 = defaultHttpClientUtil.dorzGet(urlgeo, charset);
                JSONObject jsons1 = JSONObject.fromObject(results1);
                String urlgeo1 = jsons1.getJSONObject("layer").getJSONObject("resource").getString("href");
                String results2 = defaultHttpClientUtil.dorzGet(urlgeo1, charset);
                JSONObject jsons2 = JSONObject.fromObject(results2);
                String extent = "[[" + jsons2.getJSONObject("featureType").getJSONObject("latLonBoundingBox").getString("miny") + "],[" + jsons2.getJSONObject("featureType").getJSONObject("latLonBoundingBox").getString("minx") + "],[" + jsons2.getJSONObject("featureType").getJSONObject("latLonBoundingBox").getString("maxy") + "],[" + jsons2.getJSONObject("featureType").getJSONObject("latLonBoundingBox").getString("maxx") + "],]";
                Json1.put("wkid", json1.getJSONObject("crs").getJSONObject("properties").getString("name"));
                Json1.put("type", "WFSServer、WMSServer");
                Json1.put("type", "WFSServer、WMSServer");
                Json1.put("extent", extent);
                Json1.put("id", i + 10);
                int flags = 1;
                if (dields.size() > 0) {
                    for (Map map : dields) {
                        JSONObject jsonobj = new JSONObject();
                        if (map.containsKey("url") && map.get("url") != null) {
                            String url = map.get("url").toString();
                            //&&url.indexOf("6080")!=-1&&url.indexOf(type)!=-1&&url.indexOf(serviceName)!=-1
                            if (url.indexOf(reip) != -1 && url.indexOf(restport) != -1 && map.get("layer_table").toString().indexOf(a[i]) != -1) {
                                String layer = map.get("layer_table").toString();
                                jsonobj.put("layer", layer);
                                String types = map.get("layer_type").toString();
                                if (flags == 1) {
                                    if (types.equals("040003")) {
                                        Json1.put("retype", "WFSServer");
                                    } else if (types.equals("030003")) {
                                        Json1.put("retype", "WMSServer");
                                    }
                                    flags++;
                                } else {
                                    if (types.equals("040003")) {
                                        Json1.put("retype", Json1.getString("retype") + "、WFSServer");
                                    } else if (types.equals("030003")) {
                                        Json1.put("retype", Json1.getString("retype") + "、WMSServer");
                                    }
                                }
                            }
                        }
                    }
                }
                feilds1.add(Json1);
            }
        }
        return feilds1.toString();
    }



    /**
     * 获取服务图层，包括主服务跟子图层
     * @param fold
     * @param reip
     * @param restport
     * @param sellayer
     * @return
     * @throws Exception
     */
    @RequestMapping("/getSelLayer")
    public String getSelLayer(String fold, String reip, String restport, String sellayer) throws Exception {
        String a[] = sellayer.split(",");
        //sellayer : hchc,MapServer、FeatureServer、 WMSServer,guangzhou,qxylz,MapServer、FeatureServer、 WMSServer,guangzhou
        JSONArray feilds = new JSONArray();
        JSONArray feilds1 = new JSONArray();
        // 获取已注册的服务
        List<Map> dields = IAgSite.getregis();
        int len = 100;

        if (a.length > 0) {
            for (int i = 0; i < a.length / 3; i++) {
                // a[3 * i] a[3 * i + 1] a[3 * i + 2]
                // @RequestMapping("/getcolum/{ip}/{name}/{port}/{type}/{fold}")  传过去父目录id  (i + 1) * 10
                int parentId = i+a.length / 3;
                feilds = getcolum(reip, a[3 * i], restport, a[3 * i + 1], a[3 * i + 2],parentId,len+i);
                len = len+feilds.size();
                if (feilds == null) {
                    String ss = JsonUtils.toJson(new ResultForm(false, a[3 * i]));

                    return JsonUtils.toJson(new ResultForm(false, a[3 * i]));
                }
                if (feilds.size() > 0) {
                    JSONObject Json1 = new JSONObject();
                    Json1.put("serviceName", a[3 * i]);
                    Json1.put("fold", a[3 * i + 2]);
                    Json1.put("type", a[3 * i + 1]);
                    Json1.put("id", parentId);
                    Json1.put("pid", 0);
                    Json1.put("dir","dir");
                    Json1.put("wkid", feilds.getJSONObject(0).getString("wkid"));
                    Json1.put("extent", feilds.getJSONObject(0).getString("extent"));
                    Json1.put("defaulttext", "请选择");
                    JSONObject object = getcolum1(reip, a[3 * i], restport, a[3 * i + 1], a[3 * i + 2]);
                    if (object.has("layerType")){
                        Json1.put("layerType", object.getString("layerType"));
                    }
                    if (object.has("layerTypeCn")){
                        Json1.put("layerTypeCn", object.getString("layerTypeCn"));
                    }
                    if (object.has("tileSize")){
                        Json1.put("tileSize", object.getString("tileSize"));
                    }
                    if (object.has("format")){
                        Json1.put("format", object.getString("format").toLowerCase());
                    }

                    int flags = 1;
                    if (dields.size() > 0) {
                        for (Map map : dields) {
                            JSONObject jsonobj = new JSONObject();
                            if (map.containsKey("url") && map.get("url") != null) {
                                String url = map.get("url").toString();
                                String layer = "";
                                if (map.get("layer_table") == null) {
                                    layer = a[3 * i];
                                } else {
                                    layer = map.get("layer_table").toString();
                                }

                                //&&url.indexOf("6080")!=-1&&url.indexOf(type)!=-1&&url.indexOf(serviceName)!=-1
                                if (url.indexOf(reip) != -1 && url.indexOf(restport) != -1 && url.indexOf(a[3 * i]) != -1 && a[3 * i].equals(layer)) {
                                    jsonobj.put("layer", layer);
                                    String types = map.get("layer_type").toString();
                                    if (flags == 1) {
                                        if (types.equals("040002")) {
                                            Json1.put("retype", "WFSServer");
                                        } else if (types.equals("030002")) {
                                            Json1.put("retype", "WMSServer");
                                        } else if (types.equals("070002")) {
                                            Json1.put("retype", "FeatureServer");
                                        } else if (types.equals("020202")) {
                                            Json1.put("retype", "MapServer");
                                        }
                                        flags++;
                                    } else {
                                        if (types.equals("040002")) {
                                            Json1.put("retype", Json1.getString("retype") + "、WFSServer");
                                        } else if (types.equals("030002")) {
                                            if (Json1.has("retype")){
                                                Json1.put("retype", Json1.getString("retype") + "、WMSServer");
                                            }
                                        } else if (types.equals("070002")) {
                                            if (Json1.has("retype")){
                                                Json1.put("retype", Json1.getString("retype") + "、FeatureServer");
                                            }
                                        } else if (types.equals("020202")) {
                                            if (Json1.has("retype")){
                                                Json1.put("retype", Json1.getString("retype") + "、MapServer");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    feilds1.add(Json1);
                    for (int k = 0; k < feilds.size(); k++) {
                        int ids = i + 10000 + k * 100;
                        JSONObject Json2 = new JSONObject();
                        Json2.put("serviceName", feilds.getJSONObject(k).getString("name"));
                        Json2.put("type", a[3 * i + 1]);
                        Json2.put("fold", a[3 * i + 2]);
                        //Json2.put("_parentId", (i + 1) * 10);
                        /*if (Integer.valueOf(feilds.getJSONObject(k).getInt("pid")) == -1){
                            Json2.put("pid", (i + 1) * 10);
                        }else {
                            Json2.put("pid", Integer.valueOf(feilds.getJSONObject(k).getInt("pid")));
                        }*/
                        Json2.put("pid", Integer.valueOf(feilds.getJSONObject(k).getInt("pid")));
                        Json2.put("dir",feilds.getJSONObject(k).getString("dir"));
                        Json2.put("id", feilds.getJSONObject(k).getInt("id"));
                        Json2.put("wkid", feilds.getJSONObject(k).getString("wkid"));
                        Json2.put("extent", feilds.getJSONObject(k).getString("extents"));
                        Json2.put("name", a[3 * i]);
                        Json2.put("sid", feilds.getJSONObject(k).getString("sid"));
                        JSONObject jsonObject = feilds.getJSONObject(k);
                        if(jsonObject.has("geometryType")){
                            Json2.put("geometryType", feilds.getJSONObject(k).getString("geometryType"));
                        }
                        if (feilds.getJSONObject(k).has("layerType")){
                            Json2.put("layerType", feilds.getJSONObject(k).getString("layerType"));
                        }
                        if (feilds.getJSONObject(k).has("layerTypeCn")){
                            Json2.put("layerTypeCn", feilds.getJSONObject(k).getString("layerTypeCn"));
                        }
                        Json2.put("defaulttext", "请选择");
                        int flag = 1;
                        if (dields.size() > 0) {
                            for (Map map : dields) {
                                JSONObject jsonobj = new JSONObject();
                                if (map.containsKey("url") && map.get("url") != null) {
                                    String url = map.get("url").toString();
                                    String layer = "";
                                    if (map.get("layer_table") == null) {

                                    } else {
                                        layer = map.get("layer_table").toString();
                                    }

                                    //&&url.indexOf("6080")!=-1&&url.indexOf(type)!=-1&&url.indexOf(serviceName)!=-1
                                    if (url.indexOf(reip) != -1 && url.indexOf(restport) != -1 && url.indexOf(a[3 * i]) != -1 && feilds.getJSONObject(k).getString("sid").equals(layer)) {

                                        jsonobj.put("layer", layer);
                                        String types = map.get("layer_type").toString();
                                        if (flag == 1) {
                                            if (types.equals("040002")) {
                                                Json2.put("retype", "WFSServer");
                                            } else if (types.equals("030002")) {
                                                Json2.put("retype", "WMSServer");
                                            } else if (types.equals("070002")) {
                                                Json2.put("retype", "FeatureServer");
                                            } else if (types.equals("020202")) {
                                                Json2.put("retype", "MapServer");
                                            }
                                            flag++;
                                        } else {
                                            if (types.equals("040002")) {
                                                if(Json2.has("retype")){
                                                    Json2.put("retype", Json2.getString("retype") + "、WFSServer");
                                                }
                                            } else if (types.equals("030002")) {
                                                if(Json2.has("retype")){
                                                    Json2.put("retype", Json2.getString("retype") + "、WMSServer");
                                                }
                                            } else if (types.equals("070002")) {
                                                if(Json2.has("retype")){
                                                    Json2.put("retype", Json2.getString("retype") + "、FeatureServer");
                                                }
                                            } else if (types.equals("020202")) {
                                                if(Json2.has("retype")){
                                                    Json2.put("retype", Json2.getString("retype") + "、MapServer");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        feilds1.add(Json2);
                    }
                }
            }
        }

        System.out.print(feilds.toString());
        return feilds1.toString();
    }

    @RequestMapping("/getServmess")
    public String getServmess(String serType, String row) {
        String urls = null;
        String name;
        String sid;
        String geometryType;
        JSONObject jrow = JSONObject.fromObject(row);
        if (jrow.has("name")) {
            name = jrow.getString("name");
            sid = jrow.getString("sid");
            geometryType = jrow.getString("geometryType");
        } else {
            name = jrow.getString("serviceName");
            sid = jrow.getString("serviceName");
            geometryType = "";
        }
        if (serType.indexOf("WFS") != -1) {
            urls = "http://" + jrow.getString("ip") + ":" + jrow.getString("restport") + "/arcgis/services/" + jrow.getString("fold") + "/" + name + "/MapServer/" + serType;
        }
        if (serType.indexOf("WMS") != -1) {
            urls = "http://" + jrow.getString("ip") + ":" + jrow.getString("restport") + "/arcgis/services/" + jrow.getString("fold") + "/" + name + "/MapServer/" + serType;
        }
        if (serType.indexOf("Feature") != -1) {
            urls = "http://" + jrow.getString("ip") + ":" + jrow.getString("restport") + "/arcgis/rest/services/" + jrow.getString("fold") + "/" + name + "/" + serType;
        }
        if (serType.indexOf("Map") != -1) {
            urls = "http://" + jrow.getString("ip") + ":" + jrow.getString("restport") + "/arcgis/rest/services/" + jrow.getString("fold") + "/" + name + "/" + serType;
        }


        String kss = null;
        String nameCn = null;
        if (jrow.has("retype") && jrow.getString("retype").indexOf(serType) != -1) {
            kss = "1";
            nameCn = jrow.getString("nameCn");

        } else {
            kss = "0";
            nameCn = jrow.getString("serviceName");
        }
        JSONObject Json2 = new JSONObject();
        Json2.put("url", urls);
        Json2.put("serType", serType);
        Json2.put("regis", kss);
        Json2.put("nameCn", nameCn);

        return Json2.toString();
    }

    @RequestMapping("/loadMapextent")
    public String loadMapextent(HttpServletRequest request) {
        String result = null;
        try {
            request.setCharacterEncoding("UTF-8");
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            Iterator<String> it = req.getFileNames();
            File dir = new File("C:\\shp\\");
            if (dir.exists()) {
                if (dir.isDirectory()) {
                    System.out.println("dir exists");
                } else {
                    System.out.println("the same name file exists, can not create dir");
                }
            } else {
                System.out.println("dir not exists, create it ...");
                dir.mkdir();
            }
            result = "{\"success\":true,";
            FileInputStream input = null;
            BufferedInputStream inbuff = null;
            FileOutputStream out = null;
            BufferedOutputStream outbuff = null;
            while (it.hasNext()) {
                MultipartFile file = req.getFile(it.next());
                String fileNames = file.getOriginalFilename();
                try {
                    inbuff = new BufferedInputStream(file.getInputStream());
                    fileNames = dir + "\\" + fileNames;
                    out = new FileOutputStream(fileNames);
                    outbuff = new BufferedOutputStream(out);
                    byte[] b = new byte[1024 * 2];
                    int len = 0;
                    while ((len = inbuff.read(b)) != -1) {
                        outbuff.write(b, 0, len);
                    }
                    outbuff.flush();
                } finally {
                    if (outbuff != null) outbuff.close();
                    if (inbuff != null) inbuff.close();
                    if (out != null) out.close();
                    if (input != null) input.close();
                }
            }
            result += "}";
        } catch (Exception e) {
            e.printStackTrace();
            result = "{'success':false}";
        }
        return result;
    }

    //得到geoserver工作空间及存储
    @RequestMapping("/getGeoWork")
    public String getGeoWork(String ip, String port, String name, String password) {
        String RESTURL = "http://" + ip + ":" + port + "/geoserver";
        String RESTUSER = name;
        String RESTPW = password;
        List<String> workspaces = null;
        JSONObject JSJ = new JSONObject();
        JSONArray SSE = new JSONArray();
        try {
            GeoServerRESTReader reader = new GeoServerRESTReader(RESTURL, RESTUSER, RESTPW);
            workspaces = reader.getWorkspaceNames();
            for (String attribute : workspaces) {
                JSJ.put("value", attribute);
                SSE.add(JSJ);
            }
        } catch (Exception mue) {
            mue.printStackTrace();

        }
        return SSE.toString();
    }

    @RequestMapping("/creatgeoser")
    public String creatgeoser(String ip, String port, String name, String password, String shp, String workspace, String layerNames, String store, String sld) {
        String RESTURL = "http://" + ip + ":" + port + "/geoserver";
        String RESTUSER = name;
        String RESTPW = password;
        GeoServerRESTPublisher.UploadMethod ee = GeoServerRESTPublisher.UploadMethod.EXTERNAL;
        List<String> workspaces = null;
        JSONObject JSJ = new JSONObject();
        JSONArray SSE = new JSONArray();
        String result = "";
        try {
            File zipFile = new File("C:\\shp\\" + shp + "");
            File sldFile = new File("C:\\shp\\" + sld + "");
            GeoServerRESTReader reader = new GeoServerRESTReader(RESTURL, RESTUSER, RESTPW);
            GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(RESTURL, RESTUSER, RESTPW);
            workspaces = reader.getWorkspaceNames();
            if (workspaces.contains(workspace)) {
                if (!sld.equals("未导入") && sldFile.exists()) {
//                    if (publisher.publishShp(workspace,"asdsd",sd,"ssdsd", sldFile,sld,"EPSG:4326")) {
//                        result = "ture";
//                    } else {
//                        return  JsonUtils.toJson(new ResultForm(false,"sld名重复"));
//                    }
                    if (publisher.publishStyleInWorkspace(workspace, sldFile, sld)) {
                        result = "ture";
                    } else {
                        return JsonUtils.toJson(new ResultForm(false, "sld名重复"));
                    }
                }
                if (publisher.publishShp(workspace, store, layerNames, zipFile, "EPSG:4326", sld)) {
                    result = "ture";
                } else {
                    return JsonUtils.toJson(new ResultForm(false, "图层名重复或与shp文件名不对应"));
                }
                System.out.println(result);
            }
        } catch (Exception mue) {
            mue.printStackTrace();

        }
        return result;
    }

    @RequestMapping("/getGeostyle")
    public String getGeostyle(String ip, String port, String name, String password) {
        String RESTURL = "http://" + ip + ":" + port + "/geoserver";
        String RESTUSER = name;
        String RESTPW = password;
        RESTStyleList workspaces = null;
        JSONObject JSJ = new JSONObject();
        JSONArray SSE = new JSONArray();
        try {
            GeoServerRESTReader reader = new GeoServerRESTReader(RESTURL, RESTUSER, RESTPW);
            workspaces = reader.getStyles();
            List<String> SD = workspaces.getNames();
            for (String attribute : SD) {
                JSJ.put("value", attribute);
                SSE.add(JSJ);
            }
        } catch (Exception mue) {
            mue.printStackTrace();

        }
        return SSE.toString();
    }

    @RequestMapping("treegrid")
    public String treegrid(){
        List<TestJson> testJsons = new ArrayList<>();
        TestJson json = new TestJson();
        json.setId(10);
        json.setPid(0);
        json.setName("gzbase");
        json.setPermissionValue("true");
        json.setDir("dir");
        testJsons.add(json);

        TestJson l20 = new TestJson();
        l20.setId(20);
        l20.setPid(10);
        l20.setName("L20");
        l20.setPermissionValue("true");
        l20.setDir("dir");
        testJsons.add(l20);

        TestJson l20_1 = new TestJson();
        l20_1.setId(201);
        l20_1.setPid(20);
        l20_1.setName("行政线");
        l20_1.setPermissionValue("true");
        l20_1.setDir("nodir");
        testJsons.add(l20_1);

        TestJson l20_2 = new TestJson();
        l20_2.setId(202);
        l20_2.setPid(20);
        l20_2.setName("地铁站点12");
        l20_2.setPermissionValue("true");
        l20_2.setDir("nodir");
        testJsons.add(l20_2);

        TestJson json3 = new TestJson();
        json3.setId(111);
        json3.setPid(10);
        json3.setName("编辑系统");
        json3.setPermissionValue("true");
        json3.setDir("dir");
        testJsons.add(json3);

        TestJson json1 = new TestJson();
        json1.setId(222);
        json1.setPid(0);
        json1.setName("字典管理");
        json1.setPermissionValue("true");
        json1.setDir("dir");
        testJsons.add(json1);

        TestJson json4 = new TestJson();
        json4.setId(333);
        json4.setPid(20);
        json4.setName("编辑按钮");
        json4.setPermissionValue("true");
        testJsons.add(json4);
       /* String str = "[\" +
                "    {\" +
                "      \"id\": 1,\" +
                "      \"pid\": 0,\" +
                "      \"status\": 1,\" +
                "      \"name\": \"系统管理\",\" +
                "      \"permissionValue\": \"open:system:get\"\" +
                "    },\" +
                "    {\" +
                "      \"id\": 2,\" +
                "      \"pid\": 0,\" +
                "      \"status\": 1,\" +
                "      \"name\": \"字典管理\",\" +
                "      \"permissionValue\": \"open:dict:get\"\" +
                "    },\" +
                "    {\" +
                "      \"id\": 20,\" +
                "      \"pid\": 1,\" +
                "      \"status\": 1,\" +
                "      \"name\": \"新增系统\",\" +
                "      \"permissionValue\": \"open:system:add\"\" +
                "    },\" +
                "    {\" +
                "      \"id\": 21,\" +
                "      \"pid\": 1,\" +
                "      \"status\": 1,\" +
                "      \"name\": \"编辑系统\",\" +
                "      \"permissionValue\": \"open:system:edit\"\" +
                "    },\" +
                "    {\" +
                "      \"id\": 22,\" +
                "      \"pid\": 1,\" +
                "      \"status\": 1,\" +
                "      \"name\": \"删除系统\",\" +
                "      \"permissionValue\": \"open:system:delete\"\" +
                "    },\" +
                "    {\" +
                "      \"id\": 33,\" +
                "      \"pid\": 2,\" +
                "      \"status\": 1,\" +
                "      \"name\": \"系统环境\",\" +
                "      \"permissionValue\": \"open:env:get\"\" +
                "    },\" +
                "    {\" +
                "      \"id\": 333,\" +
                "      \"pid\": 33,\" +
                "      \"status\": 1,\" +
                "      \"name\": \"新增环境\",\" +
                "      \"permissionValue\": \"open:env:add\"\" +
                "    },\" +
                "    {\" +
                "      \"id\": 3333,\" +
                "      \"pid\": 33,\" +
                "      \"status\": 1,\" +
                "      \"name\": \"编辑环境\",\" +
                "      \"permissionValue\": \"open:env:edit\"\" +
                "    },\" +
                "    {\" +
                "      \"id\": 233332,\" +
                "      \"pid\": 33,\" +
                "      \"status\": 0,\" +
                "      \"name\": \"删除环境\",\" +
                "      \"permissionValue\": \"open:env:delete\"\" +
                "    }\" +
                "]";*/
        return JsonUtils.toJson(testJsons);
    }

    public static class TestJson{
        int id;
        int pid;
        int status;
        String name;
        String permissionValue;
        String dir;

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPermissionValue() {
            return permissionValue;
        }

        public void setPermissionValue(String permissionValue) {
            this.permissionValue = permissionValue;
        }
    }
}
