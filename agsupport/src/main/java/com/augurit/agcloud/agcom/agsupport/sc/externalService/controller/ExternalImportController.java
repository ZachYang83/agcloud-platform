package com.augurit.agcloud.agcom.agsupport.sc.externalService.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.sc.externalService.service.AbstractChangeRule;
import com.augurit.agcloud.agcom.agsupport.sc.externalService.service.AbstractFilterRule;
import com.augurit.agcloud.agcom.agsupport.sc.externalService.service.TestApiService;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgLayer;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.namespace.QName;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "外部数据导入",description = "外部数据导入相关服务接口")
@RestController
@RequestMapping("/external")
public class ExternalImportController {

    @Autowired
    private IAgLayer iAgLayer;
    @Autowired
    private IAgDic iAgDic;
    private static String STATUS_ENABLE = "1";//服务状态-可用
    private static String SAVE_ENABLE = "1";//是否转存-是

    @RequestMapping("/importByWebService")
    public ResultForm importByWebService(String url) {
        try {
            //layerId = "b34e9496-83c6-4265-a9dc-93135315d12g";
            String encodeUrl = URLEncoder.encode(url,"utf-8");
            String param = "/importByWebService?url="+encodeUrl;
            AgLayer agLayer = iAgLayer.findByLayerUrl(param.toUpperCase());
            if(agLayer==null){
                return new ResultForm(false,"请求的服务不存在");
            }
            if(STATUS_ENABLE.equals(agLayer.getStatus())){
                String namespace = "http://com.augurit.agcloud.agcom.agsupport/webservice";
                String params = "{\"userName\":\"augurit\",\"password\":\"augurit\"}";
                String data = callExternalWebService(namespace,url,"getData",params);//从外部接口获取需要处理的数据
                JSONObject jsonObjectData = JSONObject.parseObject(data);
                String typeValue = jsonObjectData.getString("type");
                List<AbstractFilterRule> listFilterRule= new ArrayList<>();
                List<AbstractChangeRule> listChangeRule= new ArrayList<>();
                if("file".equals(typeValue)) {
                    //处理的数据是一个文件
                    String fileName = "NEW_" + jsonObjectData.getString("fileName");//文件被接收后另存为的名称
                    String fileContent = jsonObjectData.getString("fileContent");//文件的内容
                    String saveFilePath = UploadUtil.getUploadAbsolutePath() + fileName;
                    receiveFile(saveFilePath, fileContent);//暂存文件

                    //根据字典配置获得清洗(过滤)规则
                    JSONArray jsonArrayFilter = getConfig("FILTER_RULE_CODE",agLayer.getFilterRuleCode());
                    for (int i = 0; i < jsonArrayFilter.size(); i++) {
                        JSONObject jsonObject = jsonArrayFilter.getJSONObject(i);
                        listFilterRule.add(AbstractFilterRule.createInstance(jsonObject));
                    }

                    //根据字典配置获得转换规则
                    JSONArray jsonArrayChange = getConfig("CHANGE_RULE_CODE",agLayer.getChangeRuleCode());
                    for (int i = 0; i < jsonArrayChange.size(); i++) {
                        JSONObject jsonObject = jsonArrayChange.getJSONObject(i);
                        listChangeRule.add(AbstractChangeRule.createInstance(jsonObject));
                    }

                    StringBuilder sb = handleFile(saveFilePath,listFilterRule,listChangeRule);
                    //根据字典配置获得转发规则
                    JSONArray jsonArraySend = getConfig("SEND_RULE_CODE",agLayer.getSendRuleCode());
                    for (int i = 0; i < jsonArraySend.size(); i++) {
                        JSONObject jsonObject = jsonArraySend.getJSONObject(i);
                        namespace = jsonObject.getString("namespace");
                        String sendUrl = jsonObject.getString("sendUrl");
                        String methodName = jsonObject.getString("methodName");
                        callExternalWebService(namespace,sendUrl,methodName,sb.toString());
                    }
                    if(SAVE_ENABLE.equals(agLayer.getSaveEnable())){
                        saveAfterHandle(saveFilePath,sb.toString());
                    }
                }


                return new ContentResultForm<String>(true,data,"请求成功");
            }
            else {
                return new ResultForm(true,"服务已关闭");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false,"发生异常:"+e.getMessage());
        }
    }

    public JSONArray getConfig(String agdicTypeCode,String ruleCode) throws Exception{
        //根据字典配置获得转换规则
        JSONArray jsonArray = null;
        List<AgDic> listChangeDic = iAgDic.getAgDicByTypeCode(agdicTypeCode);
        List<AgDic> listMatchChangeRule = listChangeDic.stream().filter(o->o.getValue().equals(ruleCode)).collect(Collectors.toList());
        if(listMatchChangeRule.size()>0) {
            AgDic changeConfig = listMatchChangeRule.get(0);
            String jsonConfigValue = changeConfig.getName();
            jsonArray = JSONArray.parseArray(jsonConfigValue);
        }
        else{
            jsonArray = new JSONArray();
        }
        return jsonArray;
    }

    private String callExternalWebService(String namespace,String url,String methodName,String xmlParams) throws Exception{
        // 接口地址
        //url = "http://localhost:8291/agsupport/ws/testApiService?wsdl";
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf
                .createClient(url);
        // url为调用webService的wsdl地址
        QName name = new QName(namespace, methodName);
        // namespace是命名空间，methodName是方法名
        Object[] objects;
        objects = client.invoke(name, xmlParams);
        String result = objects[0].toString();
        System.out.println(result);
        return result;
    }

    private void receiveFile(String filePath,String fileContent) throws IOException{
        BufferedWriter bufferWritter=null;
        try{
            File localFile = new File(filePath);
            fileContent = fileContent.replaceAll("data:image/png;base64,","");
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = decoder.decode(fileContent);
            // 输出流
            OutputStream os = new FileOutputStream(localFile);
            os.write(bytes);
            os.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void saveAfterHandle(String filePath,String fileContent) throws IOException{
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,"GBK");
        BufferedWriter bufferWritter=new BufferedWriter(outputStreamWriter);
        try{
            bufferWritter.write(fileContent);
            bufferWritter.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
        finally {
            if(bufferWritter!=null){
                bufferWritter.close();
                outputStreamWriter.close();
                fileOutputStream.close();
            }
        }
    }

    private StringBuilder handleFile(String filePath, List<AbstractFilterRule> listFilterRule,List<AbstractChangeRule> listChangeRule) throws IOException{
        FileInputStream fileInputStream =null;
        BufferedReader br=null;
        StringBuilder sb = new StringBuilder();
        try{
            File localFile = new File(filePath);
            fileInputStream = new FileInputStream(localFile);
            br= new BufferedReader(new InputStreamReader(fileInputStream,"GBK"));
            String lineString = null;
            int index =-1;//转换字段的索引
            while ((lineString=br.readLine())!=null){
                String[] arr = lineString.split(",");
                if(index==-1){
                    for(int i = 0;i<arr.length;i++){
                        for(AbstractFilterRule filterRule:listFilterRule){
                            if(arr[i].equals(filterRule.getFieldName())){
                                filterRule.setFieldIndex(i);
                            }
                        }

                        for(AbstractChangeRule changeRule:listChangeRule){
                            if(arr[i].equals(changeRule.getFieldName())){
                                changeRule.setFieldIndex(i);
                            }
                        }
                    }
                    sb.append(StringUtils.join(arr,",")+"\n");
                }
                else {
                    int successFilterRuleCount=0;
                    for(AbstractFilterRule filterRule:listFilterRule){
                        if(filterRule.handle(arr[filterRule.getFieldIndex()])){
                            successFilterRuleCount+=1;
                        }
                    }
                    if(successFilterRuleCount==listFilterRule.size()){
                        for(AbstractChangeRule changeRule:listChangeRule){
                            arr[changeRule.getFieldIndex()]=changeRule.handle(arr[changeRule.getFieldIndex()]);
                        }
                        sb.append(StringUtils.join(arr,",")+"\n");
                    }
                }
                index+=1;
            }
        }catch(Exception e){
            e.printStackTrace();
            sb.delete(0,sb.length());
            sb.append(e.getMessage());
        }
        finally {
            if(br!=null){
                br.close();
            }
            if(fileInputStream!=null){
                fileInputStream.close();
            }
        }
        return sb;
    }
}
