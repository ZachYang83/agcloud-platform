package com.augurit.agcloud.agcom.agsupport.sc.dir.controller.form;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgMapParam;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Augurit on 2017-05-23.
 */
public class LayerForm implements Serializable,Comparable<LayerForm> {

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
        layerForm.setLayerVersion(agLayer.getLayerVersion() == null? "":agLayer.getLayerVersion());
        layerForm.setLayerAggregateName(agLayer.getLayerAggregateName() == null? "":agLayer.getLayerAggregateName());
        layerForm.setProxyType(agLayer.getIsProxy() == null? "":agLayer.getIsProxy());
        return layerForm;
}

    private String id;                  //??????
    private String text;                //??????
    private String url;                 //????????????
    private String layerType;           //????????????
    private String featureType;         //????????????
    private String type;                //?????????????????????
    private String layerTable;          //????????????
    private String isBaseMap;           //???????????????
    private String paramId;             //????????????id
    private Map state;                  //??????UserLayer??????????????????
    private String data;
    private String extent;              //????????????
    private String nameCn;
    private int order;                  //??????
    private String uuid;                  //??????uuid?????????????????????????????????

    private String isProxy;
    private String proxyUrl;




    private String visibleMinZoom;          //??????????????????????????????
    private String visibleMaxZoom;          //??????????????????????????????

    //????????????
    private String userId;         //??????????????????(dirLayerId)????????????????????????id
    private String dirLayerId;          //??????????????????????????????????????????????????????id
    private AgMapParam mapParam;        //????????????

    //????????????????????????
    private String dataSourceId;            //?????????ID
    private String pkColumn;                //?????????????????????
    private String geometryColumn;             //????????????????????????
    private String isVector;                //???????????????

    private String baseColor;          //????????????
    private String specular;             //????????????
    private String isSelfPoPup;          //????????????
    private String selfPoPupContent;          //????????????

    private String remarks;                  //??????
    private String styleManagerId;          //????????????id
    private String layerVersion;          //???????????????
    private String layerAggregateName;          //????????????
    private String proxyType;          //?????????????????????isProxy??????????????????????????????????????????????????????????????????????????????????????????isProxy

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    public String getLayerVersion() {
        return layerVersion;
    }

    public void setLayerVersion(String layerVersion) {
        this.layerVersion = layerVersion;
    }

    public String getLayerAggregateName() {
        return layerAggregateName;
    }

    public void setLayerAggregateName(String layerAggregateName) {
        this.layerAggregateName = layerAggregateName;
    }

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
        //??????Comparable?????????compareTo??????
        return this.order - layerForm.order;// ?????????????????????????????????????????????????????????
    }
}
