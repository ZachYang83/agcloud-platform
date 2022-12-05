package com.augurit.agcloud.agcom.agsupport.sc.dir.controller.form;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgMapParam;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Augurit on 2017-05-23.
 */
public class LayerForm  implements Serializable,Comparable<LayerForm> {

    public static LayerForm createInstance(AgLayer agLayer) throws Exception{
        if(agLayer == null){
            return  null;
        }
        LayerForm layerForm = new LayerForm();
        layerForm.setId(agLayer.getId()==null?"":agLayer.getId());
        layerForm.setText(agLayer.getName()==null?"":agLayer.getName());
        layerForm.setUrl(agLayer.getUrl()==null?"":agLayer.getUrl());
        layerForm.setLayerType(agLayer.getLayerType()==null?"":agLayer.getLayerType());
        layerForm.setFeatureType(agLayer.getFeatureType()==null?"":agLayer.getFeatureType());
        layerForm.setType(agLayer.getLayerTypeCn()==null?"":agLayer.getLayerTypeCn());
        layerForm.setLayerTable(agLayer.getLayerTable()==null?"":agLayer.getLayerTable());
        //layerForm.setIsBaseMap(agLayer.getis);
        layerForm.setParamId(agLayer.getParamId()==null?"":agLayer.getParamId());
        layerForm.setData(agLayer.getData()==null?"":agLayer.getData());
        layerForm.setNameCn(agLayer.getNameCn()==null?"":agLayer.getNameCn());
        layerForm.setIsProxy(agLayer.getIsProxy()==null?"":agLayer.getUrl());
        layerForm.setProxyUrl(agLayer.getProxyUrl()==null?"":agLayer.getProxyUrl());
        layerForm.setVisibleMaxZoom(agLayer.getVisibleMaxZoom()==null?"":agLayer.getVisibleMaxZoom());
        layerForm.setVisibleMinZoom(agLayer.getVisibleMinZoom()==null?"":agLayer.getVisibleMinZoom());
        layerForm.setIsVector(agLayer.getIsVector()==null?"":agLayer.getIsVector());
        layerForm.setDataSourceId(agLayer.getDataSourceId()==null?"":agLayer.getDataSourceId());
        layerForm.setPkColumn(agLayer.getPkColumn()==null?"":agLayer.getPkColumn());
        layerForm.setgeometryColumn(agLayer.getGeometryColumn()==null?"":agLayer.getGeometryColumn());
        layerForm.setSpecular(agLayer.getSpecular() == null?"":agLayer.getSpecular());
        layerForm.setIsSelfPoPup(agLayer.getIsSelfPoPup() == null?"":agLayer.getIsSelfPoPup());
        layerForm.setBaseColor(agLayer.getBaseColor() == null?"":agLayer.getBaseColor());
        layerForm.setSelfPoPupContent(agLayer.getSelfPoPupContent() == null?"":agLayer.getSelfPoPupContent());
        layerForm.setRemarks(agLayer.getRemarks() == null?"":agLayer.getRemarks());
        layerForm.setStyleManagerId(agLayer.getStyleManagerId() == null?"":agLayer.getStyleManagerId());
        return layerForm;
}

    private String id;                  //编号
    private String text;                //名称
    private String url;                 //访问路径
    private String layerType;           //图层类型
    private String featureType;         //要素类型
    private String type;                //图层类型中文名
    private String layerTable;          //图层表名
    private String isBaseMap;           //是否为底图
    private String paramId;             //地图参数id
    private Map state;                  //存放UserLayer中的配置信息
    private String data;
    private String extent;              //图层范围
    private String nameCn;
    private int order;                  //排序
    private String uuid;                  //这个uuid是启用代理时需要设置的

    private String isProxy;
    private String proxyUrl;




    private String visibleMinZoom;          //图层可见最小缩放等级
    private String visibleMaxZoom;          //图层可见最大缩放等级

    //关联信息
    private String userId;         //当前目录图层(dirLayerId)与用户的关联关系id
    private String dirLayerId;          //当前图层是哪个目录下的图层的关联关系id
    private AgMapParam mapParam;        //地图参数

    //矢量图层新增字段
    private String dataSourceId;            //数据源ID
    private String pkColumn;                //矢量图层表主键
    private String geometryColumn;             //矢量图层空间字段
    private String isVector;                //是否是矢量

    private String baseColor;          //图层亮度
    private String specular;             //图层亮度
    private String isSelfPoPup;          //是否弹出
    private String selfPoPupContent;          //弹出内容

    private String remarks;                  //备注
    private String styleManagerId;          //样式管理id

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStyleManagerId() {
        return styleManagerId;
    }

    public void setStyleManagerId(String styleManagerId) {
        this.styleManagerId = styleManagerId;
    }

    public String getFeatureType() {
        return featureType;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLayerType() {
        return layerType;
    }

    public void setLayerType(String layerType) {
        this.layerType = layerType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLayerTable() {
        return layerTable;
    }

    public void setLayerTable(String layerTable) {
        this.layerTable = layerTable;
    }

    public String getIsBaseMap() {
        return isBaseMap;
    }

    public void setIsBaseMap(String isBaseMap) {
        this.isBaseMap = isBaseMap;
    }


    public AgMapParam getMapParam() {
        return mapParam;
    }

    public void setMapParam(AgMapParam mapParam) {
        this.mapParam = mapParam;
    }

    public Map getState() {
        return state;
    }

    public void setState(Map state) {
        this.state = state;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(String pkColumn) {
        this.pkColumn = pkColumn;
    }

    public String getgeometryColumn() {
        return geometryColumn;
    }

    public void setgeometryColumn(String geometryColumn) {
        this.geometryColumn = geometryColumn;
    }

    public String getIsVector() {
        return isVector;
    }

    public void setIsVector(String isVector) {
        this.isVector = isVector;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getDirLayerId() {
        return dirLayerId;
    }

    public void setDirLayerId(String dirLayerId) {
        this.dirLayerId = dirLayerId;
    }


    public String getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(String baseColor) {
        this.baseColor = baseColor;
    }

    public String getSpecular() {
        return specular;
    }

    public void setSpecular(String specular) {
        this.specular = specular;
    }

    public String getIsSelfPoPup() {
        return isSelfPoPup;
    }

    public void setIsSelfPoPup(String isSelfPoPup) {
        this.isSelfPoPup = isSelfPoPup;
    }

    public String getSelfPoPupContent() {
        return selfPoPupContent;
    }

    public void setSelfPoPupContent(String selfPoPupContent) {
        this.selfPoPupContent = selfPoPupContent;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    public String getExtent() {
        return extent;
    }

    public void setExtent(String extent) {
        this.extent = extent;
    }
    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


    public String getIsProxy() {
        return isProxy;
    }

    public void setIsProxy(String isProxy) {
        this.isProxy = isProxy;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public String getVisibleMinZoom() {
        return visibleMinZoom;
    }

    public void setVisibleMinZoom(String visibleMinZoom) {
        this.visibleMinZoom = visibleMinZoom;
    }

    public String getVisibleMaxZoom() {
        return visibleMaxZoom;
    }

    public void setVisibleMaxZoom(String visibleMaxZoom) {
        this.visibleMaxZoom = visibleMaxZoom;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int compareTo(LayerForm layerForm) {
        //重写Comparable接口的compareTo方法
        return this.order - layerForm.order;// 根据年龄升序排列，降序修改相减顺序即可
    }
}
