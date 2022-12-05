package com.augurit.agcloud.agcom.agsupport.sc.dir.util;

/**
 * 目录树(ztree)
 */
public class Ztree {
    private String id;   //节点id
    private String pId;  //父节点pId I必须大写
    private String name; //节点名称
    private String open = "false"; //是否展开树节点，默认不展开
    private String isParent;
    private String mapParamId;
    private int order;//树节点的业务类型,value:project(专题);projectDir(专题目录);dir(目录)
    private String info; // 关联信息

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }

    public String getMapParamId() {
        return mapParamId;
    }

    public void setMapParamId(String mapParamId) {
        this.mapParamId = mapParamId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
