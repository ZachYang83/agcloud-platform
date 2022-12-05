package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

public class AgTag {
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    //   private String layerIds;
    @ApiModelProperty("目录id")
    private String catalogId;
    @ApiModelProperty("打上了这个标签的图层总数")
    private int layerNum; // 打上了这个标签的图层总数
    @ApiModelProperty("排序")
    private int sort; //排序



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

//    public String getLayerIds() {
//        return layerIds;
//    }
//
//    public void setLayerIds(String layerIds) {
//        this.layerIds = layerIds;
//    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public int getLayerNum() {
        return layerNum;
    }

    public void setLayerNum(int layerNum) {
        this.layerNum = layerNum;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
