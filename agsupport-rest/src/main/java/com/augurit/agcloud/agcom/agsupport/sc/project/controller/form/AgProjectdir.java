package com.augurit.agcloud.agcom.agsupport.sc.project.controller.form;

import java.util.Map;

/**
 * Created by yzq on 2017-04-18.
 */
public class AgProjectdir {
    private String id;          //编号
    private String name;        //名称
    private String parentId;    //父节点id
    private String xpath;       //节点路径
    private String dirSeq;      //目录序列
    private Map<String, Object> attributes;  //属性
    private int orderNm;
    private String mapParamId;    //地图参数id
    private  int PROJECTORDER;     //专题排序
    private String OWNER;         //专题创建者


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

    public String getOWNER() {
        return OWNER;
    }

    public void setOWNER(String OWNER) {
        this.OWNER = OWNER;
    }

    public int getPROJECTORDER() {
        return PROJECTORDER;
    }

    public void setPROJECTORDER(int PROJECTORDER) {
        this.PROJECTORDER = PROJECTORDER;
    }
}
