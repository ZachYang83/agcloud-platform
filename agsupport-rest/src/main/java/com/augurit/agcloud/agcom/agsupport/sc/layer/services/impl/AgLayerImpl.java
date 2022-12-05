package com.augurit.agcloud.agcom.agsupport.sc.layer.services.impl;

import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayerFieldConf;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayerRelated;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerFieldConfMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerRelatedMapper;
import com.augurit.agcloud.agcom.agsupport.sc.layer.dao.AgLayerDao;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgDataTrans;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.common.util.Common;
import com.common.util.ReflectBeans;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.GEO_COLUMN;
import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.WKT_COLUMN;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-06-01.
 */
@Service
public class AgLayerImpl implements IAgLayer {
    @Autowired
    private AgLayerDao agLayerDao;
    @Autowired
    private IAgDataTrans iAgDataTrans;
    @Autowired
    private AgLayerMapper agLayerMapper;

    @Autowired
    private AgLayerRelatedMapper agLayerRelatedMapper;
    @Autowired
    private AgLayerFieldConfMapper agLayerFieldConfMapper;

    @Override
    public boolean saveVectorLayer(AgLayer agLayer, JSONObject dataObj) {
        boolean isSuccess = false;
        try {
            Map<String, String> dataMap = new HashMap<String, String>();
            Iterator iterator = dataObj.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                if (key != agLayer.getPkColumn() && !key.equals(agLayer.getPkColumn())
                        && key != agLayer.getGeometryColumn() && !key.equals(agLayer.getGeometryColumn())) {
                    if (key.equals(GEO_COLUMN)) {
                        String wkt = SpatialUtil.jsonToWkt(dataObj.getString(key));
                        dataMap.put(WKT_COLUMN, wkt);
                    } else {
                        dataMap.put(key, dataObj.getString(key));
                    }
                }
            }
            String primaryKey = "";
            if (dataObj.get(agLayer.getPkColumn()) != null) {
                primaryKey = dataObj.get(agLayer.getPkColumn()).toString();
                isSuccess = agLayerDao.updateVectorLayer(agLayer.getDataSourceId(), agLayer.getLayerTable(), agLayer.getPkColumn(),
                        agLayer.getGeometryColumn(), dataMap, primaryKey);
            } else {
                primaryKey = agLayerDao.getPrimaryKey(agLayer.getDataSourceId(), agLayer.getLayerTable(), agLayer.getPkColumn());
                dataMap.put(agLayer.getPkColumn(), primaryKey);
                isSuccess = agLayerDao.saveVectorLayer(agLayer.getDataSourceId(), agLayer.getLayerTable(), agLayer.getPkColumn(),
                        agLayer.getGeometryColumn(), dataMap);
            }
            if (isSuccess) {
                Layer layer = ReflectBeans.copy(agLayer, Layer.class);
                isSuccess = iAgDataTrans.updateOrSaveByPk(layer, primaryKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    @Override
    public boolean deleteVectorLayer(AgLayer agLayer, JSONObject dataObj) {
        boolean isSuccess = false;
        try {
            String obj = dataObj.get(agLayer.getPkColumn()).toString();
            if (dataObj.get(agLayer.getPkColumn()) != null) {
                String primaryKey = dataObj.get(agLayer.getPkColumn()).toString();
                isSuccess = agLayerDao.deleteVectorLayer(agLayer.getDataSourceId(), agLayer.getLayerTable(), agLayer.getPkColumn(), primaryKey);
                if (isSuccess) {
                    isSuccess = iAgDataTrans.deleteDataByPk(agLayer.getDataSourceId(), agLayer.getLayerTable(), primaryKey);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    @Override
    public AgLayer findByLayerId(@Param("layerId") String layerId) throws Exception {
        return agLayerMapper.findByLayerId(layerId);
    }

    @Override
    public List<AgLayer> findAllAgentUrlList() throws Exception {
        return agLayerMapper.getAllAgentUrl();
    }

    @Override
    public List<AgLayer> getVectorLayer() throws Exception {
        return agLayerMapper.findVectorLayer();
    }

    @Override
    public PageInfo<AgLayer> getVectorLayerByPage(Page page) throws Exception {
        PageHelper.startPage(page);
        List<AgLayer> list = agLayerMapper.findVectorLayer();
        return new PageInfo<AgLayer>(list);
    }
    @Override
    public Integer findUserlayerCount(String userId) throws Exception {
        return agLayerMapper.findUserlayerCount(userId);
    }

    @Override
    public ContentResultForm getAssociatedInfo(String serviceDirLayerId,String relatedDirLayerId,String jsonParams){
        ContentResultForm resultForm = null;
        try {
            com.alibaba.fastjson.JSONObject jsonObjectParams = com.alibaba.fastjson.JSONObject.parseObject(jsonParams);
            if(!Common.isCheckNull(serviceDirLayerId)&&!Common.isCheckNull(relatedDirLayerId)&&jsonObjectParams.size()>0) {
                List<AgLayerRelated> listLayerRelated = agLayerRelatedMapper.getByServiceDirLayerIdAndRelatedDirLayerId(serviceDirLayerId,relatedDirLayerId);
                AgLayer agLayer = agLayerMapper.findByDirLayerId(relatedDirLayerId);
                com.alibaba.fastjson.JSONObject jsonObjectData = com.alibaba.fastjson.JSONObject.parseObject(agLayer.getData());
                String dataSourceId = jsonObjectData.getString("dataSourceId");
                String initString = " WHERE ";
                String whereString = initString;
                if(agLayer!=null && !Common.isCheckNull(agLayer.getLayerTable()) && !Common.isCheckNull(dataSourceId)) {
                    if (listLayerRelated != null) {
                        for (AgLayerRelated agLayerRelated : listLayerRelated) {
                            String serviceField = agLayerRelated.getServiceField();
                            String relatedField = agLayerRelated.getRelatedField();
                            if(!Common.isCheckNull(jsonObjectParams.get(serviceField)) && !Common.isCheckNull(relatedField)){
                                if(whereString.equals(initString)){
                                    whereString = whereString + relatedField + " = '" + jsonObjectParams.get(serviceField).toString()+"'";
                                }
                                else {
                                    whereString = whereString + "AND" + relatedField + " = '" + jsonObjectParams.get(serviceField).toString()+"'";
                                }
                            }
                        }
                        String sql = "SELECT * FROM "+agLayer.getLayerTable().toUpperCase();
                        if(whereString.equals(initString)){
                            whereString = whereString + " 1<>1";
                            resultForm = new ContentResultForm(false, null, "查询不到相关信息");
                        }
                        else {
                            sql = sql + whereString;
                            List<Map> list = JDBCUtils.find(dataSourceId, sql, null);
                            List<Map<String,Object>> listResult = new ArrayList();
                            List<AgLayerFieldConf> layerFields = agLayerFieldConfMapper.findByLayerId(agLayer.getId());//获取别名配置
                            for(Map map : list){
                                Iterator<Map.Entry<Object,Object>> iterator = map.entrySet().iterator();
                                Map<String,Object> mapResult = new HashMap();
                                while (iterator.hasNext()){
                                    Map.Entry<Object,Object> entry = iterator.next();
                                    String key = entry.getKey().toString();
                                    List<AgLayerFieldConf> listFieldConf = layerFields.stream().filter(o->key!=null && key.equals(o.getFieldName())).collect(Collectors.toList());
                                    if(listFieldConf.size()>0){
                                        if("1".equals(listFieldConf.get(0).getViewInResult())) {
                                            mapResult.put(listFieldConf.get(0).getFieldNameCn(), entry.getValue());
                                        }
                                    }
                                    else {
                                            mapResult.put(key, entry.getValue());
                                    }
                                }
                                listResult.add(mapResult);
                            }
                            resultForm = new ContentResultForm(true, listResult, "获取成功");
                        }
                    }
                    else {
                        resultForm = new ContentResultForm(false,null,"找不到关联的业务配置信息，请检查");
                    }
                }
                else {
                    resultForm = new ContentResultForm(false,null,"找不到关联的图层信息，请检查");
                }
            }
            else {
            resultForm = new ContentResultForm(false,null,"参数不能为空");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            resultForm = new ContentResultForm(false,null,"发生异常："+e.getMessage());
        }
        return resultForm;
    }

}
