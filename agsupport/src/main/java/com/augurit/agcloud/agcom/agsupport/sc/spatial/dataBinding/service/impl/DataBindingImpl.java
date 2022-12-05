package com.augurit.agcloud.agcom.agsupport.sc.spatial.dataBinding.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayerFieldConf;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.field.dao.AgFieldDao;
import com.augurit.agcloud.agcom.agsupport.sc.field.service.impl.AgFieldImpl;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgDataTrans;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.dataBinding.service.IDataBinding;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.query.dao.AgDBQueryDao;
import com.augurit.agcloud.agcom.agsupport.sc.user.converter.AgUserConverter;
import com.common.util.*;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by chendingxing on 2018-03-15.
 */
@Service
public class DataBindingImpl implements IDataBinding {

    @Autowired
    private IAgDir iDir;

    @Autowired
    private AgLayerMapper layerMapper;

    @Autowired
    private AgDBQueryDao dbQueryDao;

    @Autowired
    private AgFieldDao fieldDao;

    private String []dbNumberType = {"bigint","smallint","tinyint","int","integer",
            "number","numeric","float","double","real","decimal"};

    @Autowired
    private IAgDataTrans dataTrans;

    @Autowired
    private AgFieldImpl fieldImpl;

    @Value("${agcloud.inf.load}")
    private Boolean agcloudInfLoad;
    /**
     * 根据用户id和图层类型，查找图层，并使用featureType过滤
     * @param userId
     * @param type
     * @param featureType
     * @return
     * @throws Exception
     */
    @Override
    public List<AgLayer> findListByUserIdAndType(String userId, String type, String featureType) throws Exception {
        List<AgLayer> layers = new ArrayList<AgLayer>();
        //查询所有属性表
        layers = layerMapper.findListByUserIdAndType(userId, type);
        //根据featureType过滤
        List<AgLayer> result = new ArrayList<>();
        for(AgLayer al : layers){
            if(layerMapper.filterByFeatureType(al.getVectorLayerId(),featureType)){
                result.add(al);
            }
        }
        return  result;
    }

    @Override
    public List<AgLayer> findAllByUserId(String userId, String type) throws Exception {
        List<AgLayer> layers = new ArrayList<AgLayer>();
        //查询所有属性表
        layers = layerMapper.findListByUserIdAndType(userId, type);
        for(AgLayer al : layers){
            String featureType = layerMapper.getVectorLayerFeatureType(al.getVectorLayerId());
            al.setFeatureType(featureType);
        }
        return layers;
    }

    @Override
    public List<AgLayer> findAllEditableByUserId(String userId, String type) throws Exception {
        List<AgLayer> layers = new ArrayList<AgLayer>();
        //查询所有属性表
        layers = layerMapper.findEditableListByUserIdAndType(userId, type);
        for(AgLayer al : layers){
            String featureType = layerMapper.getVectorLayerFeatureType(al.getVectorLayerId());
            al.setFeatureType(featureType);
        }
        return layers;
    }

    /**
     * 根据属性表的dirLayerId和查询条件，返回热力图渲染数据，包括属性表字段
     * @param dirLayerId
     * @param conditions
     * @return
     * @throws Exception
     */
    @Override
    public List<Map> findBindingData(String userId,String dirLayerId,List<String> conditions) throws Exception {
        AgLayer attrTable = iDir.findLayerByDirLayerId(dirLayerId);
        if(attrTable == null) return null;
        AgLayer spaTable = iDir.findLayerByDirLayerId(attrTable.getVectorLayerId());
        if(spaTable == null) return null;
        Layer attrCondition = ReflectBeans.copy(attrTable, Layer.class);
        if(conditions.size() > 0){
            attrCondition.setWhere(conditions.get(0));
        }
        List<AgLayerFieldConf> layerFields = fieldImpl.getLayerFieldsByUserId(dirLayerId,userId);
        List queryFields = new ArrayList();
        for(AgLayerFieldConf afc : layerFields){
            if("1".equals(afc.getViewInResult())){
                queryFields.add(afc.getFieldName());
            }
        }
        attrCondition.setFields(queryFields);
        List<Map> result = null;
        if(attrTable.getDataSourceId().equals(spaTable.getDataSourceId())){
            //同源
            result = dbQueryDao.LinkedQueryBySQL(attrTable, spaTable, attrCondition);
        }else{
            //不同源，先分别到各自数据源上查询数据后，再在代码中合并
            List<Map> airmMaps = new ArrayList<>();
            List<Map> attrMaps = dbQueryDao.queryAttrTableBySQL(attrCondition,null);
            if(attrMaps.size() > 0){
                boolean useMongoDB = dataTrans.isTransComplete(spaTable.getDataSourceId(), spaTable.getLayerTable());
                List<Map> spaMaps;
                if(useMongoDB){
                    spaMaps = dbQueryDao.querySpatialTableByMDB(spaTable,attrTable.getSpatialTableField());
                }else{
                    spaMaps = dbQueryDao.querySpatialTableBySQL(spaTable, attrTable.getSpatialTableField());
                }
                if(spaMaps.size() > 0){
                    for(Map am : attrMaps){
                        for(Map sm: spaMaps){
                            if(am.containsKey(attrTable.getAttrTableField()) && am.get(attrTable.getAttrTableField()) != null
                                    && (am.get(attrTable.getAttrTableField()).toString().equals(sm.get(attrTable.getSpatialTableField()).toString())
                                    || am.get(attrTable.getAttrTableField()) == sm.get(attrTable.getSpatialTableField()))){
                                am.putAll(sm);
                                airmMaps.add(am);
                                break;
                            }
                        }
                    }
                    result = airmMaps;
                }
            }
        }
        return result;
    }

    @Override
    public List<Map> queryData(String userId, String tableName, List<String> conditions) throws Exception {
        AgLayer layer = iDir.findLayerBylayerTableAndUserId(userId, tableName);
        return this.findBindingData(userId,layer.getDirLayerId(),conditions);
    }

    /**
     * 查找业务表的可查询字段且是 数字类型的
     * @param dirLayerId
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public List findFieldByTableId(String dirLayerId,String userId) throws Exception {
        AgLayer attrTable = iDir.findLayerByDirLayerId(dirLayerId);
        if(attrTable == null) return null;
        List result = new ArrayList<>();
        List<String> filter = Arrays.asList(dbNumberType);
        List<AgLayerFieldConf> layerFields = fieldImpl.getLayerFieldsByUserId(dirLayerId,userId);
        if(layerFields.size() > 0){
            for(AgLayerFieldConf alff : layerFields){
                if("1".equals(alff.getViewInResult()) && filter.contains(alff.getFieldType())){
                    Map<String,String> map = new HashMap();
                    map.put("field_name",alff.getFieldName());
                    map.put("field_name_cn",alff.getFieldNameCn());
                    map.put("field_type",alff.getFieldType());
                    result.add(map);
                }
            }
        }else{
            List<Map> fieldMap = fieldDao.getLayerFieldFromDB(attrTable.getDataSourceId(), attrTable.getLayerTable());
            for (int i = 0; i < fieldMap.size(); i++) {
                Map map = fieldMap.get(i);
                String field_type = String.valueOf(map.get("field_type")).trim();
                if(filter.contains(field_type.toLowerCase())){
                    result.add(map);
                }
            }
        }
        return result;
    }

    /**
     * 查找业务表的所有字段，先从数据库里的字段配置表读取，读不到数据再到数据库实体表读取
     *  注意返回格式不一致
     *  字段配置表读取 layerFields list中的  字段是AgLayerFieldConf类的集合 字段例如：fieldName
     *  数据库实体表读取 字段是 带下划线的 例如： field_name
     *  注意前端解析要判断
     */
    @Override
    public List findAllFieldByTableId(String dirLayerId,String userId) throws Exception {
        AgLayer attrTable = iDir.findLayerByDirLayerId(dirLayerId);
        if(attrTable == null) return null;
        List result = null;
        List<AgLayerFieldConf> layerFields = fieldImpl.getLayerFieldsByUserId(dirLayerId,userId);
        if(layerFields.size() > 0){
            result = layerFields;
        }else{
            result = fieldDao.getLayerFieldFromDB(attrTable.getDataSourceId(), attrTable.getLayerTable());
        }
        return result;
    }
}
