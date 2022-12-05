package com.augurit.agcloud.agcom.agsupport.domain;

import com.augurit.agcloud.agcom.agsupport.util.SysUtil;

import java.util.Map;

/**
 * Created by Augurit on 2017-11-21.
 */
public class AgMenuDir {
    private String id;          //编号
    private String name;        //名称
    private String parentId;    //父节点id
    private String xpath;       //节点路径
    private String dirSeq;      //目录序列
    private String url;         //菜单地址
    private Map<String, Object> attributes;  //属性
    private int orderNm;
    private String sysId;    //关联系统id（预留）
    private String icon;    //目录图标
    private String middleIcon;//目录中图标

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

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getUrl() {
        return SysUtil.replaceMenuOut(url);
    }

    public void setUrl(String url) {
        this.url = SysUtil.replaceMenuIn(url);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMiddleIcon() {
        return middleIcon;
    }

    public void setMiddleIcon(String middleIcon) {
        this.middleIcon = middleIcon;
    }
}
