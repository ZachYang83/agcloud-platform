package com.augurit.agcloud.agcom.agsupport.util;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import net.sf.json.JSONObject;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :17:00 2018/10/31
 * @Modified By:
 */
public class TokenUtil {

   public static String getToken(String monitorUrl,String userName,String password){//http://192.168.11.18:6080/arcgis/rest/services/guangzhou/gzbase/MapServer
       String httpResponsReqult = "";
      try {
          int len = monitorUrl.lastIndexOf("arcgis")+6;
          monitorUrl = monitorUrl.substring(0,len)+"/admin/generateToken";
          InetAddress address = InetAddress.getLocalHost();
          String ip=address.getHostAddress().toString(); //获取本机ip
          Map<String, String> param = new HashMap<>();
          param.put("f", "json");
          param.put("username", userName);
          param.put("password", password);
          param.put("client", "ip");
          param.put("ip", ip);
          param.put("expiration", "600");
          //http://192.168.11.18:6080/arcgis/admin/generateToken
          HttpRequester httpRequester = new HttpRequester();
          httpRequester.setReadTimeout(10000);
          httpRequester.setConnectTimeout(10000);
          HttpRespons httpRespons = httpRequester.sendPost(monitorUrl, param);
          httpResponsReqult = httpRespons.getContent();
          //System.out.println("请求token返回的结果"+httpResponsReqult);
          if (httpResponsReqult.contains("Invalid URL") && httpResponsReqult.contains("error") && httpResponsReqult.contains("400")){
              return "noLayerService";
          }
          return JSONObject.fromObject(httpResponsReqult).get("token").toString();
      }catch (Exception e){
          if (e.getMessage()!=null && e.getMessage().contains("error url")){
              return "errorUrl";
          }
          return null;
      }
   }
}
