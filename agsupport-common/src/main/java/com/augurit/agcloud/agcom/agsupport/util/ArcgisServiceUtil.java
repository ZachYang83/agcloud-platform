package com.augurit.agcloud.agcom.agsupport.util;

import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :17:47 2018/10/31
 * @Modified By:
 */
public class ArcgisServiceUtil {
    public static String getServiceReport(String monitorUrl,String token){//http://192.168.11.18:6080/arcgis/rest/services/guangzhou/gzbase/MapServer
        try {
            if ("http://192.168.19.6:6080/arcgis/rest/services/leaflet_test/qm/MapServer/wmts".equals(monitorUrl)){
                //monitorUrl = "http://192.168.19.6:6080/arcgis/rest/services/leaflet_test/qm/MapServer";
            }
            String type = monitorUrl.substring(monitorUrl.lastIndexOf("/")+1);
            //wmts类型要去掉wmts然后按照MapServer去解析地址
            //http://192.168.19.6:6080/arcgis/rest/services/leaflet_test/qm/MapServer/wmts
            if (!"MapServer".equals(type) && !"FeatureServer".equals(type)){
                Map<String,String> param = new HashMap<>();
                param.put("f","json");
                HttpRequester httpRequester = new HttpRequester();
                httpRequester.setConnectTimeout(3000);
                HttpRespons httpRespons = httpRequester.sendPost(monitorUrl,param);
                int code = httpRespons.getCode();
                JSONObject json = new JSONObject();
                if (code!=200){
                    json.put("available",0);
                    json.put("status",-1);//服务停止
                    return json.toString();
                }
                monitorUrl = monitorUrl.substring(0,monitorUrl.lastIndexOf("/"));
                type = "MapServer";
            }
            int url_len = monitorUrl.lastIndexOf("arcgis")+6;
            int services_len = monitorUrl.indexOf("services")+9;
            String tmpStr = monitorUrl.substring(services_len,monitorUrl.lastIndexOf("/"));
            //判断是根目录还是有文件夹
            String folderName = "";
            if (tmpStr.lastIndexOf("/")==-1){
                folderName = "/";
            }else {
                folderName = tmpStr.substring(0,tmpStr.lastIndexOf("/"));
            }
            String serviceName = tmpStr.substring(tmpStr.lastIndexOf("/")+1);
            //String type = monitorUrl.substring(monitorUrl.lastIndexOf("/")+1);
            JSONObject resultJson = new JSONObject();
               // 由于featureServer不能通过官网提供接口获取服务信息
               // 一、判断这个featureServer的url是否可以访问
               //可以访问：就把接口参数type设置为MapServer，featureServer的状态取相同名称的mapserver的状态
               // 不可以访问：设置监控服务的状态为3，返回
            if ("FeatureServer".equals(type)){
                Map<String,String> param = new HashMap<>();
                param.put("f","json");
                HttpRequester httpRequester = new HttpRequester();
                httpRequester.setConnectTimeout(2000);
                HttpRespons httpRespons = httpRequester.sendPost(monitorUrl,param);
                int code = httpRespons.getCode();
                if (code!=200){
                    resultJson.put("available",0);
                    resultJson.put("status",-1);//服务停止
                    return resultJson.toString();
                }else {
                    type = "MapServer";
                }
            }
            monitorUrl = monitorUrl.substring(0,url_len)+"/admin/services";
            String url = monitorUrl+"/"+folderName+"/report";

            String  reports = "";
            JSONObject obj = new JSONObject();
            //MapServer类型
            Map<String, String> param = new HashMap<>();
            param.put("f", "json");
            List<String> parametersList = new ArrayList<>();
            parametersList.add("instances");
            parametersList.add("status");
            parametersList.add("description");
            param.put("parameters ", parametersList.toString());

            JSONObject servicesJson = new JSONObject();
            servicesJson.put("folderName",folderName);
            servicesJson.put("serviceName",serviceName);
            servicesJson.put("type",type);
            param.put("services", "["+servicesJson.toString()+"]");
            param.put("token",token);
            //http://192.168.11.18:6080/arcgis/admin/generateToken
            HttpRespons httpRespons = new HttpRequester().sendPost(url, param);
            obj = JSONObject.fromObject(httpRespons.getContent());

            if (obj.has("reports")){
                reports = obj.getString("reports");
            }
            if ("[]".equals(reports) || "".equals(reports)){//图层不存在
                resultJson.put("available",0);
                resultJson.put("status",3);//服务不存在
            }else {
                reports = reports.substring(1,reports.length()-1);
                String status = JSONObject.fromObject(reports).getString("status");
                String realTimeState = JSONObject.fromObject(status).getString("realTimeState");//服务状态
                Object instances = JSONObject.fromObject(reports).get("instances");
                double max  = JSONObject.fromObject(instances).getInt("max");
                double free  = JSONObject.fromObject(instances).getInt("free");
                if ("STARTED".equals(realTimeState)){
                    resultJson.put("status",1);//启动状态
                    resultJson.put("available", (free/max)*100.0);//可用率
                }else if ("STOPPED".equals(realTimeState)){
                    resultJson.put("status",0);//停止状态
                    resultJson.put("available",0);//可用率
                }
            }
            return resultJson.toString();
        }catch (Exception e){
           System.out.println(e.getMessage());
            return null;
        }
    }
}
