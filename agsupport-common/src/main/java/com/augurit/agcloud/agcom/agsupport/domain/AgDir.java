package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * Created by Augurit on 2017-04-18.
 */
public class AgDir {
    @ApiModelProperty(value = "目录ID")
    private String id;          //编号
    @ApiModelProperty(value = "目录名称",required = true)
    private String name;        //名称
    @ApiModelProperty(value = "父节点ID")
    private String parentId;    //父节点id
    @ApiModelProperty(value = "节点路径")
    private String xpath;       //节点路径
    @ApiModelProperty(value = "目录序列")
    private String dirSeq;      //目录序列
    private Map<String, Object> attributes;  //属性
    @ApiModelProperty(value = "排序",dataType = "Integer")
    private int orderNm;
    private String mapParamId;    //地图参数id
    @ApiModelProperty(value = "关联信息",dataType = "string")
    private String info;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getDirSeq() {
        return dirSeq;
    }

    public void setDirSeq(String dirSeq) {
        this.dirSeq = dirSeq;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public int getOrderNm() {
        return orderNm;
    }

    public void setOrderNm(int orderNm) {
        this.orderNm = orderNm;
    }

    public String getMapParamId() {
        return mapParamId;
    }

    public void setMapParamId(String mapParamId) {
        this.mapParamId = mapParamId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
