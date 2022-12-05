package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * Created by yzq on 2017-04-18.
 */
public class AgProjectdir {
    private String id;          //编号
    private String name;        //名称
    private String parentId;    //父节点id
    private String xpath;       //节点路径
    private String dirSeq;      //目录序列
    private int orderNm;
    private String mapParamId;    //地图参数id
    private int projectorder;     //专题排序
    private String owner;         //专题创建者
    private String roleids;         //授权者


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

    public int getProjectorder() {
        return projectorder;
    }

    public void setProjectorder(int projectorder) {
        this.projectorder = projectorder;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRoleids() {
        return roleids;
    }

    public void setRoleids(String roleids) {
        this.roleids = roleids;
    }
}


