package com.augurit.agcloud.agcom.agsupport.domain;

public class AgTagLayer {
    private String id;
    private String tagId;
    private String layerId;
    private String name; //图层名称
    private String xpath;//所在的资源目录路径
    private String tagName; //标签名称
    private String tagCatalogId; //标签目录id
    private String applyingNum; //正在申请该服务的用户数
    private String hadNum; //已拥有此服务的用户数
    private String rejectNum; //被驳回此服务申请的用户数
    private String applyStatus; //申请状态
    private String count; //申请状态计数

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getTagCatalogId() {
        return tagCatalogId;
    }

    public void setTagCatalogId(String tagCatalogId) {
        this.tagCatalogId = tagCatalogId;
    }

    public String getName() {
        return name;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getApplyingNum() {
        return applyingNum;
    }

    public void setApplyingNum(String applyingNum) {
        this.applyingNum = applyingNum;
    }

    public String getHadNum() {
        return hadNum;
    }

    public void setHadNum(String hadNum) {
        this.hadNum = hadNum;
    }

    public String getRejectNum() {
        return rejectNum;
    }

    public void setRejectNum(String rejectNum) {
        this.rejectNum = rejectNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }
}
