package com.augurit.agcloud.agcom.agsupport.domain;


import java.util.Date;

public class AgOpenMapLayer implements Cloneable {

    private String id;
    private String name;
    private String nameCn;
//    private String url;
    private String layerType;
    private String featureType;
    private String shortCut;
    private String addFlag;
    private String layerTable;
    private String paramId;
    private String vectorLayerId;
    private String isProxy;
//    private String proxyUrl;
    private String data;
    private String extent;
    private String tagNames;
    private String layerTypeCn;
    private String picture;
    private String filePath;
    private AgOpenMapApplyItem applyItem;
    private AgOpenMapAuditList auditList; //审核表单
    private String tagId; //标签id
    private String applyStatus; //审核状态

    private String completed; // 0:，  1:代表此item 不再与图层挂载，说明它是被驳回后，图层被重新申请了

    private String dirLayerId;
    private String url;
    private String isBaseMap;
    private String isShow;
    private String proxyUrl;
    private String metadataId;
    private String isExternal;
    private String visibleMinZoom;
    private String visibleMaxZoom;
    private Date auditTime;//审核时间
    private String validityDate;//允许使用天数

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }


    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public AgOpenMapAuditList getAuditList() {
        return auditList;
    }

    public void setAuditList(AgOpenMapAuditList auditList) {
        this.auditList = auditList;
    }

    public AgOpenMapApplyItem getApplyItem() {
        return applyItem;
    }

    public void setApplyItem(AgOpenMapApplyItem applyItem) {
        this.applyItem = applyItem;
    }

    public String getTagNames() {
        return tagNames;
    }

    public void setTagNames(String tagNames) {
        this.tagNames = tagNames;
    }

    private String isVector;
    private String createTime;
    private String illustration;// 服务说明

    public AgOpenMapLayer() {}

    public AgOpenMapLayer(AgLayer layer) {
        this.setId(layer.getId());
        this.setName(layer.getName());
        this.setNameCn(layer.getNameCn());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }

    public String getLayerType() {
        return layerType;
    }

    public void setLayerType(String layerType) {
        this.layerType = layerType;
    }

    public String getFeatureType() {
        return featureType;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    public String getShortCut() {
        return shortCut;
    }

    public void setShortCut(String shortCut) {
        this.shortCut = shortCut;
    }

    public String getAddFlag() {
        return addFlag;
    }

    public void setAddFlag(String addFlag) {
        this.addFlag = addFlag;
    }

    public String getLayerTable() {
        return layerTable;
    }

    public void setLayerTable(String layerTable) {
        this.layerTable = layerTable;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getVectorLayerId() {
        return vectorLayerId;
    }

    public void setVectorLayerId(String vectorLayerId) {
        this.vectorLayerId = vectorLayerId;
    }

    public String getIsProxy() {
        return isProxy;
    }

    public void setIsProxy(String isProxy) {
        this.isProxy = isProxy;
    }

//    public String getProxyUrl() {
//        return proxyUrl;
//    }
//
//    public void setProxyUrl(String proxyUrl) {
//        this.proxyUrl = proxyUrl;
//    }

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

    public String getLayerTypeCn() {
        return layerTypeCn;
    }

    public void setLayerTypeCn(String layerTypeCn) {
        this.layerTypeCn = layerTypeCn;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getIsVector() {
        return isVector;
    }

    public void setIsVector(String isVector) {
        this.isVector = isVector;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getIllustration() {
        return illustration;
    }

    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }

    public String getDirLayerId() {
        return dirLayerId;
    }

    public void setDirLayerId(String dirLayerId) {
        this.dirLayerId = dirLayerId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsBaseMap() {
        return isBaseMap;
    }

    public void setIsBaseMap(String isBaseMap) {
        this.isBaseMap = isBaseMap;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }

    public String getIsExternal() {
        return isExternal;
    }

    public void setIsExternal(String isExternal) {
        this.isExternal = isExternal;
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

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(String validityDate) {
        this.validityDate = validityDate;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
