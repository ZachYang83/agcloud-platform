package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;

public class AgDataSubject {
    @ApiModelProperty(value = "数据专题主键")
    private String id;
    @ApiModelProperty(value = "数据专题名称")
    private String subjectName;
    @ApiModelProperty(value = "数据专题父ID")
    private String parentId;
    @ApiModelProperty(value = "数据专题排序字段")
    private Integer orderNo;
    @ApiModelProperty(value = "路径字段")
    private String xpath;
    @ApiModelProperty(value = "主题层级")
    private Integer subjectLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public Integer getSubjectLevel() {
        return subjectLevel;
    }

    public void setSubjectLevel(Integer subjectLevel) {
        this.subjectLevel = subjectLevel;
    }

    @Override
    public String toString() {
        return "AgDataSubject{" +
                "id='" + id + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", parentId='" + parentId + '\'' +
                ", orderNo=" + orderNo +
                ", xpath='" + xpath + '\'' +
                ", subjectLevel=" + subjectLevel +
                '}';
    }
}