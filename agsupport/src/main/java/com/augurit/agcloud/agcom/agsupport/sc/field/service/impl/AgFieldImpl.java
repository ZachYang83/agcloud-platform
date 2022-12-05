package com.augurit.agcloud.agcom.agsupport.sc.field.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.mapper.*;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.ReadUrlUtil;
import com.augurit.agcloud.agcom.agsupport.sc.field.dao.AgFieldDao;
import com.augurit.agcloud.agcom.agsupport.sc.field.service.IAgField;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.common.dbcp.DBHelper;
import com.common.util.Common;
import com.common.util.ReflectBeans;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spatial.GeometryApplication;
import spatial.GeometryOperate;

import java.util.*;

/**
 * Created by Lidw on 2017-04-27.
 */
@Service
public class AgFieldImpl implements IAgField {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgFieldImpl.class);
    @Autowired
    private AgUserLayerMapper agUserLayerMapper;

    @Autowired
    private AgFieldAuthorizeMapper agFieldAuthorizeMapper;

    @Autowired
    private AgLayerFieldMapper agLayerFieldMapper;


    @Autowired
    private IAgDir dir;

    @Autowired
    private AgFieldDao agFieldDao;

    @Autowired
    private IAgUser iAgUser;

    @Autowired
    private AgServerMapper agServerMapper;

   /* @Override
    public AgRoleLayer getLayerConfigByUserId(String dirLayerId, String userId) throws Exception {
        List<AgRole> roleList = null;
        if (!agcloudInfLoad.booleanValue()) {
            roleList = agRoleMapper.findListByUser(new AgRole(), userId);
        } else {
            Map<String, String> param = new HashMap<>();
            param.put("userId", userId);
            HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/ac/getRolesByUserId.do", param);
            JSONArray jsonArray = JSONArray.fromObject(httpRespons.getContent());
            List<Map> listMap = com.common.util.JsonUtils.toList(jsonArray.toString(), HashMap.class);
            roleList = new AgRoleConverter().convertToList(null, listMap);
        }
        AgRoleLayer agRoleLayer = new AgRoleLayer();
        for (AgRole agRole : roleList) {
            agRoleLayer = unionAgRoleLayer(agRoleLayer, iAgRole.getRoleLayer(agRole.getId(), dirLayerId));
        }
        return agRoleLayer;
    }*/

    @Override
    public AgUserLayer getLayerConfigByUserId(String dirLayerId, String userId) throws Exception {

        AgUserLayer agUserLayer = new AgUserLayer();
        agUserLayer = agUserLayerMapper.findByUidAndDid(userId,dirLayerId);
        return agUserLayer;
    }

    @Autowired
    private AgLayerFieldConfMapper agLayerFieldConfMapper;
    @Override
    public List<AgLayerFieldConf> getLayerFields(String dirLayerId, String layerId) throws Exception {
        //AgRoleLayer roleLayer = agRoleLayerMapper.findByRidAndDid(roleId, dirLayerId);
       // if (roleLayer == null) return null;
        //List<AgLayerFieldConf> layerFields = agFieldDao.findByRidAndDlid(roleId, dirLayerId);
        //        // 2019-01-18
        //List<AgLayerFieldConf> layerFields = agFieldDao.findByDirLayerId(dirLayerId);
//        List<AgLayerFieldConf> layerFields = agLayerFieldConfMapper.findByDirLayerId(dirLayerId);
        List<AgLayerFieldConf> layerFields = agLayerFieldConfMapper.findByLayerId(layerId);
        AgLayer agLayer = dir.findLayerByDirLayerId(dirLayerId);
        if (layerFields.size() == 0) {
            if (agLayer.getLayerType().startsWith("01") || "000001".equals(agLayer.getLayerType())) {//矢量图层判断
                try {
                    layerFields = getVectorFieldInfo(agLayer);
                } catch (Exception e) {
                    LOGGER.error("请检查矢量图层配置是否正确!");
                    e.printStackTrace();

                }
            }
            if (agLayer.getLayerType().startsWith("04")) {//WFS图层判断
                try {
                    layerFields = getWfsFieldInfo(agLayer);
                } catch (Exception e) {
                    LOGGER.error("请检查服务地址是否正确!");
                    e.printStackTrace();
                }
            }
            if (agLayer.getLayerType().substring(2, 4).equals("02")) {//MapServer图层判断
                try {
                    layerFields = getMapServerFieldInfo(agLayer);
                } catch (Exception e) {
                    LOGGER.error("请检查服务地址是否正确!");
                    e.printStackTrace();
                }
            }

            if (agLayer.getLayerType().startsWith("07")) {//FeatureServer图层判断
                try {
                    layerFields = getFeatureServerFieldInfo(agLayer);
                } catch (Exception e) {
                    LOGGER.error("请检查服务地址是否正确!");
                    e.printStackTrace();
                }
            }
        } else {
            //初始化权限信息
            layerFields = formatLayerFields(agLayer, layerFields);
        }

        return layerFields;
    }

    @Override
    public List<AgLayerFieldConf> getLayerFieldsByUserId(String dirLayerId, String layerId) throws Exception {
        List<AgLayerFieldConf> layerFields = new ArrayList<AgLayerFieldConf>();
        layerFields = unionLayerFields(layerFields, getLayerFields(dirLayerId, layerId));
        return layerFields;
    }

    private List<AgLayerFieldConf> getVectorFieldInfo(AgLayer agLayer) throws Exception {
        List<AgLayerFieldConf> layerFields = new ArrayList<AgLayerFieldConf>();
        List<Map> data = agFieldDao.getLayerFieldFromDB(agLayer.getDataSourceId(), agLayer.getLayerTable());
        for (int i = 0; i < data.size(); i++) {
            Map map = data.get(i);
            AgLayerFieldConf agLayerFieldConf = new AgLayerFieldConf();
            Object field_name = map.get("field_name");
            if (field_name == null){
                field_name = map.get("FIELD_NAME");
            }

            Object field_type = map.get("field_type");
            if (field_type == null){
                field_type = map.get("FIELD_TYPE");
            }
            Object field_size = map.get("field_size");
            if (field_size == null){
                field_size = map.get("FIELD_SIZE");
            }
            Object nullable = map.get("nullable");
            if (nullable == null){
                nullable = map.get("NULLABLE");
            }
            if (field_name != null ){
                agLayerFieldConf.setFieldName(String.valueOf(field_name));
                agLayerFieldConf.setFieldNameCn(String.valueOf(field_name));
            }
            if (field_type != null){
                agLayerFieldConf.setFieldType(String.valueOf(field_type));
            }
            if (field_size != null){
                agLayerFieldConf.setFieldSize(String.valueOf(field_size));
            }
            if (nullable != null){
                agLayerFieldConf.setFnullable(String.valueOf(nullable));
            }
            // 给字段配置赋默认值
            agLayerFieldConf.setViewInResult("1");
            agLayerFieldConf.setEditable("0");
            if (agLayerFieldConf.getFieldType().contains("varchar")) {
                agLayerFieldConf.setViewInBlurquery("1");
            } else {
                agLayerFieldConf.setViewInBlurquery("0");
            }
            // 主键 以及 空间字段 默认不查询显示
            if (agLayerFieldConf.getFieldName().equals(agLayer.getPkColumn())) {
                agLayerFieldConf.setFieldNameCn("主键");
                agLayerFieldConf.setViewInResult("1");
                agLayerFieldConf.setViewInBlurquery("0");
            } else if (agLayerFieldConf.getFieldName().equals(agLayer.getGeometryColumn())) {
                agLayerFieldConf.setFieldNameCn("空间字段");
                agLayerFieldConf.setViewInResult("0");
                agLayerFieldConf.setViewInBlurquery("0");
            }
            //添加排序字段
            agLayerFieldConf.setOrderNm(String.valueOf(i));
            agLayerFieldConf.setLayerId(agLayer.getId());
            layerFields.add(agLayerFieldConf);
        }
        return layerFields;
    }

    @Override
    public List<AgLayerFieldConf> getRefreshFields(String userLayerId, String layerId) throws Exception {
        //AgRoleLayer agRoleLayer = agRoleLayerMapper.findById(roleLayerId);
       // List<AgLayerFieldConf> fieldsOld = agFieldDao.findByRidAndDlid(agRoleLayer.getRoleId(), agRoleLayer.getDirLayerId());
        List<AgLayerFieldConf> fieldsOld = agFieldDao.findByLayerId(layerId);
        List<AgLayerFieldConf> fieldsNew = new ArrayList<AgLayerFieldConf>();

        AgLayer agLayer = dir.findLayerByLayerId(layerId);
        if (agLayer.getLayerType().startsWith("01") || "000001".equals(agLayer.getLayerType())) {//矢量图层、属性表判断
            try {
                fieldsNew = getVectorFieldInfo(agLayer);
            } catch (Exception e) {
                System.out.println("请检查矢量图层配置是否正确!");
            }
        }
        if (agLayer.getLayerType().startsWith("04")) {//WFS图层判断
            try {
                fieldsNew = getWfsFieldInfo(agLayer);
            } catch (Exception e) {
                System.out.println("请检查服务地址是否正确!");
                return null;
            }
        }

        if (agLayer.getLayerType().substring(2, 4).equals("02")) {//MapServer图层判断
            try {
                fieldsNew = getMapServerFieldInfo(agLayer);
            } catch (Exception e) {
                System.out.println("请检查服务地址是否正确!");
            }
        }

        if (agLayer.getLayerType().startsWith("07")) {//FeatureServer图层判断
            try {
                fieldsNew = getFeatureServerFieldInfo(agLayer);
            } catch (Exception e) {
                System.out.println("请检查服务地址是否正确!");
            }
        }
        if (fieldsOld.size() == 0) {
            return fieldsNew;
        } else {
            List<AgLayerFieldConf> list = new ArrayList<AgLayerFieldConf>();
            List<AgLayerFieldConf> list_delete = new ArrayList<AgLayerFieldConf>();

            for (AgLayerFieldConf fieldData : fieldsOld) {
                boolean flag = false;
                for (AgLayerFieldConf fieldServer : fieldsNew) {
                    if (fieldData.getFieldName().equals(fieldServer.getFieldName())) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    list.add(fieldData);
                } else {
                    list_delete.add(fieldData);
                }
            }

            for (AgLayerFieldConf fieldServer : fieldsNew) {
                boolean flag = false;
                for (AgLayerFieldConf fieldData : fieldsOld) {
                    if (fieldServer.getFieldName().equals(fieldData.getFieldName())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    list.add(fieldServer);
                }
            }

            if (list_delete.size() > 0) deleteLayerFields(list_delete);
            return list;
        }
    }

    @Override
    public void saveLayerFields(List<AgLayerFieldConf> list) throws Exception {
        if (list == null || list.size() <= 0) return;
        //DBHelper.save("ag_layer_field", list);
        agLayerFieldMapper.insertOrUpdateLayerFieldList(list);
    }

    @Override
    public void deleteLayerFields(List<AgLayerFieldConf> list_delete) throws Exception {
        if (list_delete != null && list_delete.size() > 0) {
            for (AgLayerFieldConf agLayerFieldConf : list_delete) {
                this.deleteLayerField(agLayerFieldConf.getId());
            }
        }
    }

    @Override
    public List<AgFieldAuthorize> findFieldAuthorizeByRoleLayerId(String roleLayerId) throws Exception {

        return agFieldAuthorizeMapper.findListByRoleLayerId(roleLayerId);
    }

    @Override
    public List<AgFieldAuthorize> findFieldAuthorizeByFieldId(String fieldId) throws Exception {

        return agFieldAuthorizeMapper.findListByFieldId(fieldId);
    }


    private List<AgLayerFieldConf> getWfsFieldInfo(AgLayer agLayer) {
        List<AgLayerFieldConf> layerFields = new ArrayList<AgLayerFieldConf>();
        StringBuilder readUrl = new StringBuilder();
        if (agLayer.getUrl().indexOf("?") != -1) {
            readUrl.append(agLayer.getUrl().substring(0, agLayer.getUrl().indexOf("?")));
        } else {
            readUrl.append(agLayer.getUrl());
        }
        // 添加WFS图层的参数信息
        LOGGER.info("图层的参数信息:"+agLayer.getLayerTable());
        String typeName = null;
        String keyField = "";
        if (agLayer.getLayerType().endsWith("03")) {
            typeName = agLayer.getLayerTable();
            readUrl.append("/ows?service=wfs&request=DescribeFeatureType&version=1.0.0&typename=");
            LOGGER.info("wfs图层url:"+readUrl.toString() + typeName);
            keyField = ReadUrlUtil.queryWfsKeyField(readUrl.toString() + typeName);
            LOGGER.info("wfs图层keyField:"+keyField);
        } else {
            //ArcGIS WFS 通过 MapServer中获取主键字段
            StringBuffer tempUrl = new StringBuffer();
            tempUrl.append(agLayer.getUrl().replace("WFSServer", "").replace("arcgis/services", "arcgis/rest/services"));

             /* 修正 2019-09-09 */
             // tempUrl.append(agLayer.getLayerTable());
             // tempUrl.append("/0?f=pjson");// 以首个图层的id来获取
             //  keyField = ReadUrlUtil.queryMapServerKeyField(tempUrl.toString());
             // typeName = agLayer.getName();
             // if (typeName.indexOf("SDE.") != -1) typeName = typeName.replace("SDE.", "");
             // readUrl.append("?request=DescribeFeatureType&version=1.0.0&typename=");
            AgLayer layerInfo = ReflectBeans.copy(agLayer, AgLayer.class);
            layerInfo.setUrl(tempUrl.toString());
            layerInfo.setLayerTable("0");
            return getMapServerFieldInfo(layerInfo);

            /*修正 */

        }
        readUrl.append(typeName);
        Map<String, String> map = ReadUrlUtil.queryWfsField(readUrl.toString());

        Set keySet = map.keySet();
        int i = 0;
        for (Iterator it = keySet.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            AgLayerFieldConf lff = new AgLayerFieldConf();
            lff.setFieldName(key);
            lff.setFieldNameCn(key);
            String value = map.get(key);
            if (value.indexOf(":") != -1) {
                value = value.split(":")[1];
            }
            lff.setFieldType(value);
            lff.setLayerId(agLayer.getId());
            //lff.setRoleLayerId(rolelayerId);
            if (keyField != null){
                List<String> keyFields = Arrays.asList(keyField.split(","));
                if (keyFields.contains(key)) {
                    lff.setIsKey("1");
                } else {
                    lff.setIsKey("0");
                }
            }
            // 给字段配置赋默认值
            lff.setViewInResult("1");
            lff.setEditable("0");
            lff.setViewInBlurquery("0");
            i++;
            lff.setOrderNm(String.valueOf(i));

            layerFields.add(lff);
        }
        return layerFields;
    }


    private List<AgLayerFieldConf> getMapServerFieldInfo(AgLayer agLayer) {
        List<AgLayerFieldConf> layerFields = new ArrayList<AgLayerFieldConf>();
        StringBuilder readUrl = new StringBuilder();
        readUrl.append(agLayer.getUrl());
        readUrl.append("/" + agLayer.getLayerTable() + "?f=pjson");
        //------ 2017-12-13 加上token判断
        JSONObject jsonObject = JSONObject.fromObject(agLayer.getData());
        if (jsonObject.has("useToken") && "1".equals(jsonObject.getString("useToken"))) {
            String token = jsonObject.getString("token");
            if (Common.isCheckNull(token)) {
                String serverLink = null;
                if (jsonObject.containsKey("serverLink")) {
                    serverLink = jsonObject.getString("serverLink");
                }
                try {
                    if (Common.isCheckNull(serverLink)) {
                        //List<AgDic> LIST = agDicMapper.getAgDicByTypeCode("95389892-58c0-4cc9-b046-e1f8b70d6f41");
                        List<AgServer> list = agServerMapper.findList(new AgServer());
                        String url = agLayer.getUrl();
                        for (AgServer agServer : list) {
                            //Map map = (Map) JSONObject.toBean((JSONObject) Common.fromObject(agDic.getValue()), HashMap.class);
                            if (url.contains(agServer.getIp().toString()) && url.contains(":" + agServer.getPort().toString()) && agServer.getType().toLowerCase().contains("arcgis")) {
                                token = dir.getToken(agServer.getId());
                                break;
                            }
                        }
                    } else {
                        token = dir.getToken(serverLink);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            readUrl.append("&token=" + token);
        }
        //------
        List<Map> fields = ReadUrlUtil.queryMapServerFieldsByRest(readUrl.toString());
        int i = 0;
        for (Map field : fields) {
            //String key = (String) it.next();
            AgLayerFieldConf lff = new AgLayerFieldConf();
            lff.setFieldName(field.get("name").toString());
            lff.setFieldNameCn(field.get("alias").toString());
            String value = field.get("type").toString();
            if (value.indexOf(":") != -1) {
                value = value.split(":")[1];
            }
            lff.setFieldType(value);
            if ("esriFieldTypeOID".equals(value)) {
                lff.setIsKey("1");
            } else {
                lff.setIsKey("0");
            }
            lff.setLayerId(agLayer.getId());
            //lff.setRoleLayerId(roleLayerId);
            lff.setOrderNm(String.valueOf(i));
            // 给字段配置赋默认值
            lff.setViewInResult("1");
            lff.setEditable("0");
            lff.setViewInBlurquery("0");
            i++;
            layerFields.add(lff);
        }
        return layerFields;
    }

    private List<AgLayerFieldConf> getFeatureServerFieldInfo(AgLayer agLayer) {
        List<AgLayerFieldConf> layerFields = new ArrayList<AgLayerFieldConf>();
        StringBuilder readUrl = new StringBuilder();
        readUrl.append(agLayer.getUrl());
        readUrl.append("/" + agLayer.getLayerTable() + "?f=pjson");
        List<Map> fields = ReadUrlUtil.queryFeatureServerFieldsByRest(readUrl.toString());
//        Set keySet = map.keySet();
        int i = 0;
        for ( Map field : fields ) {
            AgLayerFieldConf lff = new AgLayerFieldConf();
            lff.setFieldName(field.get("name").toString());
            lff.setFieldNameCn(field.get("alias").toString());
            String value = field.get("type").toString();
            if (value.indexOf(":") != -1) {
                value = value.split(":")[1];
            }
            lff.setFieldType(value);
            if ("esriFieldTypeOID".equals(value)) {
                lff.setIsKey("1");
            } else {
                lff.setIsKey("0");
            }
            lff.setLayerId(agLayer.getId());
            //lff.setRoleLayerId(roleLayerId);
            lff.setOrderNm(String.valueOf(i));
            // 给字段配置赋默认值
            lff.setViewInResult("1");
            lff.setEditable("0");
            lff.setViewInBlurquery("0");
            i++;
            layerFields.add(lff);
        }
        return layerFields;
    }

    private List<AgLayerFieldConf> formatLayerFields(AgLayer agLayer, List<AgLayerFieldConf> layerFields) {
        List<AgLayerFieldConf> fLayerFields = layerFields;
        for (int i = 0; i < layerFields.size(); i++) {
            AgLayerFieldConf layerField = layerFields.get(i);
            if (layerField.getEditable() == null) {
                layerField.setEditable("0");
            }
            if (layerField.getViewInBlurquery() == null) {
                //2018-03-06 将没有进行配置的字段 默认为不可查询和不可显示
//                if (layerField.getFieldName().equals(agLayer.getPkColumn())
//                        || layerField.getFieldName().equals(agLayer.getGeometryColumn())
//                        || !layerField.getFieldType().contains("varchar")) {
                layerField.setViewInBlurquery("0");
//                } else {
//                    layerField.setViewInBlurquery("1");
//                }
            }
            if (layerField.getViewInResult() == null) {
//                if (layerField.getFieldName().equals(agLayer.getGeometryColumn())) {
                layerField.setViewInResult("0");
//                } else {
//                    layerField.setViewInResult("1");
//                }
            }
           /* if (layerField.getRoleLayerId() == null) {
                layerField.setRoleLayerId(roleLayerId);
            }*/
            fLayerFields.set(i, layerField);
        }
        return fLayerFields;
    }

    @Override
    public void saveFieldAuthorize(List<AgFieldAuthorize> list) throws Exception {
        if (list == null || list.size() <= 0) return;
        DBHelper.save("ag_field_authorize", list);

    }

    @Override
    public void deleteFieldAuthorize(String id) throws Exception {
        if (id == null) return;
        agFieldAuthorizeMapper.delete(id);
    }

    @Override
    public List<AgLayerField> findLayerFieldByLayerId(String layerId) throws Exception {

        return agLayerFieldMapper.findByLayerId(layerId);
    }

    @Override
    public void deleteLayerField(String id) throws Exception {
        if (id == null) return;
       /* List<AgFieldAuthorize> agFieldAuthorizes = this.findFieldAuthorizeByFieldId(id);//删除字段配置关联
        for (AgFieldAuthorize temp : agFieldAuthorizes) {
            this.deleteFieldAuthorize(temp.getId());
        }*/

        agLayerFieldMapper.delete(id);
    }

    @Override
    public void saveLayerConfig(AgUserLayer agUserLayer) {
        try {
            AgUserLayer agUserLayerFromDB = iAgUser.getUserLayer(agUserLayer.getUserId(), agUserLayer.getDirLayerId());
            String editable = "on".equals(agUserLayer.getEditable()) ? "1" : "0";
            String queryable = "on".equals(agUserLayer.getQueryable()) ? "1" : "0";
            String isShow = "on".equals(agUserLayer.getIsShow()) ? "1" : "0";
            String isBaseMap = "on".equals(agUserLayer.getIsBaseMap()) ? "1" : "0";
            agUserLayerFromDB.setEditable(editable);
            agUserLayerFromDB.setQueryable(queryable);
            agUserLayerFromDB.setIsShow(isShow);
            agUserLayerFromDB.setIsBaseMap(isBaseMap);
            iAgUser.saveUserLayer(agUserLayerFromDB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AgLayerField> getFieldNameAlias(String layerId) throws Exception {
        return agLayerFieldMapper.getFieldNameAlias(layerId);
    }

    @Override
    public AgLayerField getLayerFieldById(String id) throws Exception {
        return agLayerFieldMapper.findById(id);
    }

    @Override
    public AgLayerField getLayerFieldByLayerIdAndFieldName(String layerId, String fieldName) throws Exception {
        return agLayerFieldMapper.getLayerFieldByLayerIdAndFieldName(layerId,fieldName);
    }

    private AgUserLayer unionAgUserLayer(AgUserLayer agUserLayer_1, AgUserLayer agUserLayer_2) {
        AgUserLayer agUserLayer = new AgUserLayer();
        try {
            if (agUserLayer_2.getDirLayerId() == null && agUserLayer_1.getDirLayerId() != null) {
                agUserLayer = agUserLayer_1;
            } else if (agUserLayer_1.getDirLayerId() == null && agUserLayer_2.getDirLayerId() != null) {
                agUserLayer = agUserLayer_2;
            } else {
                agUserLayer = agUserLayer_1;
                if ("1".equals(agUserLayer_1.getEditable()) || "1".equals(agUserLayer_2.getEditable())) {
                    agUserLayer.setEditable("1");
                }
                if ("1".equals(agUserLayer_1.getIsShow()) || "1".equals(agUserLayer_2.getIsShow())) {
                    agUserLayer.setIsShow("1");
                }
                if ("1".equals(agUserLayer_1.getIsBaseMap()) || "1".equals(agUserLayer_2.getIsBaseMap())) {
                    agUserLayer.setIsBaseMap("1");
                }
                if (GeometryApplication.checkWkt(agUserLayer_1.getExtent()) && GeometryApplication.checkWkt(agUserLayer_2.getExtent())) {
                    agUserLayer.setExtent(GeometryOperate.union(agUserLayer_1.getExtent(), agUserLayer_2.getExtent()));
                } else if (GeometryApplication.checkWkt(agUserLayer_2.getExtent())) {
                    agUserLayer.setExtent(agUserLayer_2.getExtent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agUserLayer;
    }

    private List<AgLayerFieldConf> unionLayerFields(List<AgLayerFieldConf> fields_1, List<AgLayerFieldConf> fields_2) {
        List<AgLayerFieldConf> fields = new ArrayList<AgLayerFieldConf>();
        try {
            if ((fields_2 == null || fields_2.size() == 0) && (fields_1 != null || fields_1.size() > 0)) {
                fields = fields_1;
            } else if ((fields_1 == null || fields_1.size() == 0) && (fields_2 != null || fields_2.size() > 0)) {
                fields = fields_2;
            } else {
                fields = fields_1;
                for (int i = 0; i < fields.size(); i++) {
                    AgLayerFieldConf field = fields.get(i);
                    AgLayerFieldConf field_2 = fields_2.get(i);
                    if ("1".equals(field.getViewInResult()) || "1".equals(field_2.getViewInResult())) {
                        field.setViewInResult("1");
                    }
                    if ("1".equals(field.getEditable()) || "1".equals(field_2.getEditable())) {
                        field.setEditable("1");
                    }
                    if ("1".equals(field.getViewInBlurquery()) || "1".equals(field_2.getViewInBlurquery())) {
                        field.setViewInBlurquery("1");
                    }
                    fields.set(i, field);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fields;
    }
}
