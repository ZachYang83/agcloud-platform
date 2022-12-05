package com.augurit.agcloud.agcom.agsupport.sc.layer.services.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerMapper;
import com.augurit.agcloud.agcom.agsupport.sc.layer.dao.AgLayerDao;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgDataTrans;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import com.common.util.ReflectBeans;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    public AgLayer findByLayerUrl(@Param("url") String url) throws Exception {
        return agLayerMapper.findByLayerUrl(url);
    }

    @Override
    public List<AgLayer> findAllAgentUrlList() throws Exception {
        return agLayerMapper.getAllAgentUrl();
    }

    @Override
    public List<AgLayer> findByDirLayerIdsNotWithData(List<String> dirLayerIds) throws Exception{
        return agLayerMapper.findByDirLayerIdsNotWithData(dirLayerIds);
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
    public void updateLayersStatus(String[] ids, String status) throws Exception{
        for (String id : ids){
            agLayerMapper.changeStatus(id,status);
        }
    }

    @Override
    public void setHandleRules(AgLayer agLayer) throws Exception{
        agLayerMapper.setHandleRules(agLayer);
    }
}
