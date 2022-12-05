package com.augurit.agcloud.agcom.agsupport.sc.dir.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;
import com.augurit.agcloud.agcom.agsupport.common.util.DESedeUtil;
import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerRelatedMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgServerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgSupDatasourceMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgLayerRelated;
import com.augurit.agcloud.agcom.agsupport.sc.site.util.defaultHttpClientUtil;
import com.augurit.agcloud.agcom.agsupport.util.HttpClientUtil;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.common.util.Common;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Augurit on 2017-04-17.
 */
@Service
public class AgLayerRelatedImpl implements IAgLayerRelated {

    @Autowired
    private AgLayerRelatedMapper agLayerRelatedMapper;

    @Autowired
    private AgLayerMapper agLayerMapper;
    @Autowired
    private AgServerMapper agServerMapper;
    @Autowired
    private AgSupDatasourceMapper agSupDatasourceMapper;

    private defaultHttpClientUtil defaultHttpClientUtil = new defaultHttpClientUtil();

    @Value("${agcloud.inf.load}")
    private Boolean agcloudInfLoad;

    @Override
    public ContentResultForm getPropertyTablesByDataSourceId(String dataSourceId) {
        ContentResultForm resultForm = null;
        try {
            if (!Common.isCheckNull(dataSourceId)) {
                List<DirLayer> listDirLayer = agLayerRelatedMapper.findPropertyTablesByDataSourceId(dataSourceId);
                resultForm = new ContentResultForm(true, listDirLayer, "获取成功");
            } else {
                resultForm = new ContentResultForm(false, "参数dataSouceId不能为空");
            }
        } catch (Exception e) {
            resultForm = new ContentResultForm(false, "发生异常:" + e.getMessage());
        }
        return resultForm;
    }

    @Override
    public ContentResultForm getServiceFieldsByDirLayerId(String dirLayerId) {
        ContentResultForm resultForm = null;
        try {
            AgLayer layer = agLayerMapper.findByDirLayerId(dirLayerId);
            String layerUrl = layer.getUrl();
            String layerTable = layer.getLayerTable();
            if (Common.isCheckNull(layerUrl)) {
                resultForm = new ContentResultForm(false, null, "获取失败,图层服务地址配置错误");
            } else {
                String serviceUrl = layerUrl;
                String token = getServiceToken(serviceUrl);
                HashMap<String, String> params = new HashMap<>();
                params.put("token",token);
                if(StringUtils.isNotBlank(layerTable)){
                    if(serviceUrl.endsWith("/")){
                        serviceUrl = serviceUrl+layerTable;
                    }
                    else{
                        serviceUrl = serviceUrl+"/"+layerTable;
                    }
                }else {
                    // 如果索引没填，但是图层包含子图层，则取子图层的第一个图层的索引
                    String layerServiceUrl = serviceUrl;
                    if(!layerServiceUrl.contains("?f=pjson")){
                        layerServiceUrl +="?f=pjson";
                    }
                    String responseResult = "";
                    if (serviceUrl.contains("https")){

                        responseResult =  HttpClientUtil.getBySslPost(layerServiceUrl,params,null,"utf-8");
                    }else {
                        responseResult =  defaultHttpClientUtil.doPost(layerServiceUrl,params,"utf-8");
                    }
                    JSONObject jsonObjectResult = JSONArray.parseObject(responseResult);
                    JSONArray layers = jsonObjectResult.getJSONArray("layers");
                    if (layers != null){
                        JSONObject o = layers.getJSONObject(0);
                        layerTable = o.get("id").toString();
                        serviceUrl = serviceUrl+"/"+layerTable;
                    }
                }
                if (!serviceUrl.contains("?f=pjson")) {
                    serviceUrl += "?f=pjson";
                }
                String responseResult = "";
                if (serviceUrl.contains("https")){
                    responseResult =  HttpClientUtil.getBySslPost(serviceUrl,params,null,"utf-8");
                }else {
                    responseResult =  defaultHttpClientUtil.doPost(serviceUrl,params,"utf-8");
                }
                JSONObject jsonObjectResult = JSONArray.parseObject(responseResult);
                JSONArray jsonArrayResult = new JSONArray();
                if (jsonObjectResult.get("fields") != null) {
                    JSONArray jsonArray = jsonObjectResult.getJSONArray("fields");
                    for (Object object : jsonArray) {
                        Map map = (Map) object;
                        Object nameObj = map.get("name");
                        if (nameObj != null && !nameObj.toString().toLowerCase().contains("shape")) {
                            jsonArrayResult.add(object);
                        }
                    }
                    resultForm = new ContentResultForm(true, jsonArrayResult, "获取成功");
                }
            }
            if (resultForm == null) {
                resultForm = new ContentResultForm(false, null, "获取失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultForm = new ContentResultForm(false,null,"服务访问异常!");
        }
        return resultForm;
    }

    /**
     * 获取站点服务的token
     * @param serviceUrl
     * @return
     * @throws Exception
     */
    public String getServiceToken(String serviceUrl) throws Exception{
        String token = "";
        String siteName = serviceUrl.replace("http://","").replace("https://","");//去除http和https前缀
        String [] arr = siteName.split("/");
        siteName = arr[0];
        AgServer agServer = agServerMapper.selectServerBySiteName(siteName);
        // 过期时间两周
        int expires = 20160;
        if (agServer != null){
            String sitetoken = agServer.getToken();
            if (StringUtils.isNotBlank(sitetoken)){
                token = sitetoken;
            }else {
                String userName = agServer.getUserName();
                String password = agServer.getPassword();
                password = DESedeUtil.desDecrypt(password);
                if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)){
                    Map params = new HashMap();
                    params.put("username",userName);
                    params.put("password",password);
                    params.put("request","getToken");
                    params.put("referer","127.0.0.1");
                    params.put("expiration",String.valueOf(expires));
                    params.put("f","json");
                    String url = "https://"+siteName+"/arcgis/sharing/rest/generateToken";
                    String result = HttpClientUtil.getBySslPost(url, params, null, "utf-8");
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
                    Object resultToken = jsonObject.get("token");
                    if (resultToken != null){
                        token = resultToken.toString();
                    }
                }

            }
        }
        return token;
    }

    @Override
    public ContentResultForm getTableFieldsByDirLayerId(String dirLayerId) {
        ContentResultForm resultForm = null;
        try {
            AgLayer layer = agLayerMapper.findByDirLayerId(dirLayerId);
            JSONObject jsonObject = JSONObject.parseObject(layer.getData());
            String layerTable = layer.getLayerTable();
            if (jsonObject.get("dataSourceId") != null && !Common.isCheckNull(layerTable)) {
                String dataSourceId = jsonObject.getString("dataSourceId");
                if (JDBCUtils.isExistTable(dataSourceId, layerTable)) {
                    String sql = "select column_name columnName from all_tab_cols where table_name =upper('" + layerTable + "')";
                    List<Map> listColumn = JDBCUtils.find(dataSourceId, sql, null);
                    resultForm = new ContentResultForm(true, listColumn, "获取成功");
                }
            }
            if (resultForm == null) {
                resultForm = new ContentResultForm(false, null, "获取失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultForm = new ContentResultForm(false, null, "发生异常:" + e.getMessage());
        }
        return resultForm;
    }

    @Override
    public ResultForm addLayerRelated(AgLayerRelated agLayerRelated) {
        ResultForm resultForm = null;
        try {
            if (checkValid(agLayerRelated)) {
                agLayerRelated.setId(UUID.randomUUID().toString());
                agLayerRelatedMapper.save(agLayerRelated);
                resultForm = new ResultForm(true, "保存成功");
            } else {
                resultForm = new ResultForm(false, "提交的信息不完整，请检查");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultForm = new ResultForm(false, "发生异常:" + e.getMessage());
        }
        return resultForm;
    }

    @Override
    public ResultForm deleteLayerRelated(String id) {
        ResultForm resultForm = null;
        try {
            if (!Common.isCheckNull(id)) {
                agLayerRelatedMapper.delete(id);
                resultForm = new ResultForm(true, "删除成功");
            } else {
                resultForm = new ResultForm(false, "参数id不能为空");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultForm = new ResultForm(false, "发生异常:" + e.getMessage());
        }
        return resultForm;
    }

    @Override
    public ResultForm deleteLayerRelated(List<String> ids) {
        ResultForm resultForm = null;
        try {
            if (!Common.isCheckNull(ids) && ids.size() > 0) {
                agLayerRelatedMapper.deleteBatch(ids);
                resultForm = new ResultForm(true, "删除成功");
            } else {
                resultForm = new ResultForm(false, "参数id不能为空");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultForm = new ResultForm(false, "发生异常:" + e.getMessage());
        }
        return resultForm;
    }

    @Override
    public ResultForm editLayerRelated(AgLayerRelated agLayerRelated) {
        ResultForm resultForm = null;
        try {
            if (checkValid(agLayerRelated) && !Common.isCheckNull(agLayerRelated.getId())) {
                agLayerRelatedMapper.update(agLayerRelated);
                resultForm = new ResultForm(true, "更新成功");
            } else {
                resultForm = new ResultForm(false, "提交的信息不完整，请检查");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultForm = new ResultForm(false, "发生异常:" + e.getMessage());
        }
        return resultForm;
    }

    @Override
    public ResultForm getByServiceDirLayerId(Page page, String serviceDirLayerId) {
        ContentResultForm resultForm = null;
        List<AgLayerRelated> list = null;
        try {
            JSONObject jsonObjectResult = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            PageHelper.startPage(page.getPageNum(), page.getPageSize());
            list = agLayerRelatedMapper.getByServiceDirLayerId(serviceDirLayerId);
            PageInfo<AgLayerRelated> pageInfo = new PageInfo<>(list);
            List<AgSupDatasource> listDataSource = agSupDatasourceMapper.findAllList();
            for (AgLayerRelated agLayerRelated : list) {
                String id = agLayerRelated.getId();
                int type = agLayerRelated.getType();
                String name = agLayerRelated.getName();
                String dataSourceId = null;
                String dataSourceName = null;
                String serviceFieldName = agLayerRelated.getServiceField();
                String relatedDirLayerId = agLayerRelated.getRelatedDirLayerId();
                String relatedFieldName = agLayerRelated.getRelatedField();
                String url = agLayerRelated.getUrl();
                String urlParams = agLayerRelated.getUrlParams();
                AgLayer layer = agLayerMapper.findByDirLayerId(agLayerRelated.getRelatedDirLayerId());
                String layerName = null;
                if (layer != null) {
                    layerName = layer.getName();
                    JSONObject jsonObject = JSONObject.parseObject(layer.getData());
                    String dataSourceIdFromLayerData = jsonObject.getString("dataSourceId");
                    List<AgSupDatasource> matchDataSources = listDataSource.stream().filter(o -> o.getId().equals(dataSourceIdFromLayerData)).collect(Collectors.toList());
                    if (matchDataSources != null && matchDataSources.size() > 0) {
                        dataSourceId = dataSourceIdFromLayerData;
                        dataSourceName = matchDataSources.get(0).getName();
                    }
                }
                jsonArray.add(getListItem(id, type, name, dataSourceId, dataSourceName, layerName, serviceDirLayerId, serviceFieldName, relatedDirLayerId, relatedFieldName, url, urlParams));
            }
            jsonObjectResult.put("list", jsonArray);
            jsonObjectResult.put("rowCount", pageInfo.getTotal());
            resultForm = new ContentResultForm(true, jsonObjectResult, "获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            resultForm = new ContentResultForm(false, "发生异常:" + e.getMessage());
        }
        return resultForm;
    }

    @Override
    public ResultForm getById(String id) {
        ContentResultForm resultForm = null;
        try {
            List<AgSupDatasource> listDataSource = agSupDatasourceMapper.findAllList();
            AgLayerRelated agLayerRelated = agLayerRelatedMapper.findById(id);
            if(agLayerRelated.getType() == 0){
                AgLayer layer = agLayerMapper.findByDirLayerId(agLayerRelated.getRelatedDirLayerId());
                JSONObject jsonObject = JSONObject.parseObject(layer.getData());
                String dataSourceId = jsonObject.getString("dataSourceId");
                List<AgSupDatasource> matchDataSources = listDataSource.stream().filter(o -> o.getId().equals(dataSourceId)).collect(Collectors.toList());
                if (matchDataSources.size() > 0) {
                    agLayerRelated.setRelatedDataSource(matchDataSources.get(0));
                }
            }
            resultForm = new ContentResultForm(true, agLayerRelated, "获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            resultForm = new ContentResultForm(false, "发生异常:" + e.getMessage());
        }
        return resultForm;
    }

    private boolean checkValid(AgLayerRelated agLayerRelated) {
        boolean isValid = !Common.isCheckNull(agLayerRelated)
                && !Common.isCheckNull(agLayerRelated.getName());
        if (0 == agLayerRelated.getType()) {
            isValid = isValid && !Common.isCheckNull(agLayerRelated.getServiceDirLayerId())
                    && !Common.isCheckNull(agLayerRelated.getServiceField())
                    && !Common.isCheckNull(agLayerRelated.getRelatedDirLayerId())
                    && !Common.isCheckNull(agLayerRelated.getRelatedField());
        } else {
            isValid = isValid && !Common.isCheckNull(agLayerRelated.getUrl());
        }
        return isValid;
    }

    public JSONObject getListItem(String id, int type, String name, String dataSourceId, String dataSourceName,
                                  String layerName, String serviceDirLayerId, String serviceFieldName,
                                  String relatedDirLayerId, String relatedFieldName, String url, String urlParams) {
        JSONObject jsonObject = new JSONObject();
        String typeString = "";
        switch (type) {
            case 0:
                typeString = "属性表关联";
                break;
            case 1:
                typeString = "url关联";
        }
        jsonObject.put("id", id);
        jsonObject.put("type", type);
        jsonObject.put("typeString", typeString);
        jsonObject.put("name", name);
        jsonObject.put("dataSourceId", dataSourceId);
        jsonObject.put("dataSourceName", dataSourceName);
        jsonObject.put("layerName", layerName);
        jsonObject.put("serviceDirLayerId", serviceDirLayerId);
        jsonObject.put("serviceFieldName", serviceFieldName);
        jsonObject.put("relatedDirLayerId", relatedDirLayerId);
        jsonObject.put("relatedFieldName", relatedFieldName);
        jsonObject.put("url", url);
        jsonObject.put("urlParams",urlParams);
        return jsonObject;
    }
}
