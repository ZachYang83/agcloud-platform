package com.augurit.agcloud.agcom.agsupport.sc.dir.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayerRelated;
import com.augurit.agcloud.agcom.agsupport.domain.AgSupDatasource;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerRelatedMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgServerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgSupDatasourceMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgLayerRelated;
import com.augurit.agcloud.agcom.agsupport.sc.site.util.defaultHttpClientUtil;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public ResultForm getByServiceDirLayerId(String serviceDirLayerId) {
        ContentResultForm resultForm;
        List<AgLayerRelated> list;
        try {
            JSONArray jsonArray = new JSONArray();
            list = agLayerRelatedMapper.getByServiceDirLayerId(serviceDirLayerId);
            List<AgSupDatasource> listDataSource = agSupDatasourceMapper.findAllList();
            for (AgLayerRelated agLayerRelated : list) {
                String id = agLayerRelated.getId();
                int type = agLayerRelated.getType();
                String name = agLayerRelated.getName();
                String dataSourceId = null;
                String dataSourceName = null;
                AgLayer layer = agLayerMapper.findByDirLayerId(agLayerRelated.getRelatedDirLayerId());
                String layerName = null;
                if (layer != null) {
                    layerName = layer.getName();
                    JSONObject jsonObject = JSONObject.parseObject(layer.getData());
                    String dataSourceIdFromLayerData = jsonObject.getString("dataSourceId");
                    List<AgSupDatasource> matchDataSources = listDataSource.stream().filter(o -> o.getId().equals(dataSourceIdFromLayerData)).collect(Collectors.toList());
                    if (matchDataSources.size() > 0) {
                        dataSourceId = dataSourceIdFromLayerData;
                        dataSourceName = matchDataSources.get(0).getName();
                    }
                }
                jsonArray.add(getListItem(id, type, name, dataSourceId, dataSourceName, layerName,agLayerRelated));
            }
            resultForm = new ContentResultForm(true, jsonArray, "获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            resultForm = new ContentResultForm(false, "发生异常:" + e.getMessage());
        }
        return resultForm;
    }

    public JSONObject getListItem(String id, int type, String name, String dataSourceId, String dataSourceName,
                                  String layerName,AgLayerRelated related) {
        JSONObject jsonObject = new JSONObject();
        String typeString = type==0?"属性表关联":"url关联";
        jsonObject.put("id", id);
        jsonObject.put("type", type);
        jsonObject.put("typeString", typeString);
        jsonObject.put("name", name);
        jsonObject.put("dataSourceId", dataSourceId);
        jsonObject.put("dataSourceName", dataSourceName);
        jsonObject.put("layerName", layerName);
        jsonObject.put("serviceDirLayerId", related.getServiceDirLayerId());
        jsonObject.put("serviceFieldName", related.getServiceField());
        jsonObject.put("relatedDirLayerId", related.getRelatedDirLayerId());
        jsonObject.put("relatedFieldName", related.getRelatedField());
        jsonObject.put("url", related.getUrl());
        jsonObject.put("urlParams",related.getUrlParams());
        return jsonObject;
    }
}
