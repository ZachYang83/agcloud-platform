package com.augurit.agcloud.agcom.agsupport.sc.externalService.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.sc.externalService.service.TestApiService;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import java.io.*;
import java.util.Base64;

//@Component
//@WebService(name = "testApiService",
//        targetNamespace = "http://com.augurit.agcloud.agcom.agsupport/webservice",
//        endpointInterface = "com.augurit.agcloud.agcom.agsupport.sc.externalService.service.TestApiService")
public class TestApiServiceImpl implements TestApiService {
    public static String USER_NAME = "augurit";
    public static String PASSWORD = "augurit";
    @Override
    public String getData(String json) {
        JSONObject jsonResult = new JSONObject();
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            String userName = jsonObject.getString("userName");
            String password = jsonObject.getString("password");
            if (USER_NAME.equals(userName) && PASSWORD.equals(password)) {
                String filePath = UploadUtil.getUploadAbsolutePath();
                String fileName = "4人口热力.csv";
                String company = "CompanyA";
                jsonResult.put("from",company);
                jsonResult.put("type","file");
                jsonResult.put("fileName",fileName);
                File file = new File(filePath + fileName );
                if (file.isFile() && file.exists()) { //判断文件是否存在
                    FileInputStream fileInputStream = new FileInputStream(file);
                    String fileContent = inputStreamToString(fileInputStream);
                    jsonResult.put("fileContent",fileContent);
                    fileInputStream.close();
                }
            }
        }
        catch (Exception e){
         }
        return  JSONObject.toJSONString(jsonResult);
    }

    @Override
    public String receiveData(String json) {
        System.out.println(json);
        return "success";
    }

    private String inputStreamToString(InputStream inputStream) throws Exception{
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        Base64.Encoder encoder = Base64.getEncoder();
        String str = encoder.encodeToString(bytes);//返回Base64编码过的字节数组字符串
        return str;
    }
}
