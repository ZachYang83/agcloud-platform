package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by Augurit on 2017-04-18.
 */
public class AgMapParam implements Serializable{
    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("地图参数名")
    private String name;
    @ApiModelProperty("切图原点")
    private String origin;
    @ApiModelProperty("图层范围")
    private String extent;
    @ApiModelProperty("图层中心")
    private String center;
    @ApiModelProperty("分辨率")
    private String scales;
    @ApiModelProperty("初始化等级")
    private String zoom;
    @ApiModelProperty("标识符")
    private String addFlag;
    @ApiModelProperty("坐标系统")
    private String reference;
    @ApiModelProperty("最小显示等级")
    private String minZoom;
    @ApiModelProperty("最大显示等级")
    private String maxZoom;
    @ApiModelProperty("切图原点")
    private String tileOrigin;
    @ApiModelProperty("默认地图")
    private String defaultMap;


    public String getTileOrigin() {
        return tileOrigin;
    }

    public void setTileOrigin(String tileOrigin) {
        this.tileOrigin = tileOrigin;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getExtent() {
        return extent;
    }

    public void setExtent(String extent) {
        this.extent = extent;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getScales() {
        return scales;
    }

    public void setScales(String scales) {
        this.scales = scales;
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public String getAddFlag() {
        return addFlag;
    }

    public void setAddFlag(String addFlag) {
        this.addFlag = addFlag;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(String minZoom) {
        this.minZoom = minZoom;
    }

    public String getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(String maxZoom) {
        this.maxZoom = maxZoom;
    }

    public String getDefaultMap() {
        return defaultMap;
    }

    public void setDefaultMap(String defaultMap) {
        this.defaultMap = defaultMap;
    }
}
