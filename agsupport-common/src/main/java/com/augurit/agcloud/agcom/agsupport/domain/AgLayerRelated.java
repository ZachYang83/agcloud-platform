package com.augurit.agcloud.agcom.agsupport.domain;


public class AgLayerRelated {

    private String id;
    private int type = 0;
    private String name;
    private String serviceDirLayerId;//关联服务标识
    private String serviceField;//服务关联字段
    private Object relatedDataSource;//关联图层信息的数据源
    private String relatedDirLayerId;//关联属性表标识
    private String relatedField;//关联属性表字段
    private String url;//关联图层url
    private String urlParams;//url扩展参数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceDirLayerId() {
        return serviceDirLayerId;
    }

    public void setServiceDirLayerId(String serviceDirLayerId) {
        this.serviceDirLayerId = serviceDirLayerId;
    }

    public String getServiceField() {
        return serviceField;
    }

    public void setServiceField(String serviceField) {
        this.serviceField = serviceField;
    }

    public Object getRelatedDataSource() {
        return relatedDataSource;
    }

    public void setRelatedDataSource(Object relatedDataSource) {
        this.relatedDataSource = relatedDataSource;
    }

    public String getRelatedDirLayerId() {
        return relatedDirLayerId;
    }

    public void setRelatedDirLayerId(String relatedDirLayerId) {
        this.relatedDirLayerId = relatedDirLayerId;
    }

    public String getRelatedField() {
        return relatedField;
    }

    public void setRelatedField(String relatedField) {
        this.relatedField = relatedField;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(String urlParams) {
        this.urlParams = urlParams;
    }
}
