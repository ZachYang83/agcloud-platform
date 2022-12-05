package com.augurit.agcloud.agcom.agsupport.sc.app.controller.form;

import com.augurit.agcloud.agcom.agsupport.domain.AgMapParam;

import java.util.Map;

/**
 * Created by Augurit on 2017-05-23.
 */
public class ApiLayerForm {

    private String id;                  //编号
    private String text;                //名称
    private String url;                 //访问路径
    private String layerType;           //图层类型
    private String type;                //图层类型中文名
    private String layerTable;          //图层表名
    private String isBaseMap;           //是否为底图
    private Map state;                  //是否默认显示
    private String isProxy;
    private String proxyUrl;
    private String dirLayerId;          //目录图层id
    private AgMapParam mapParam;        //地图参数

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

    public String getDirLayerId() {
        return dirLayerId;
    }

    public void setDirLayerId(String dirLayerId) {
        this.dirLayerId = dirLayerId;
    }
}
