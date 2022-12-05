package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;

public class AgDataOverview {
    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "数据名称",dataType = "string")
    private String dataName;
    @ApiModelProperty(value = "主题目录类型",dataType = "string")
    private String subjectType;
    @ApiModelProperty(value = "图层类型",dataType = "string")
    private String dataType;
    @ApiModelProperty(value = "数据容量",dataType = "long")
    private Long dataSize;
    @ApiModelProperty(value = "数据源类型",dataType = "string")
    private String datasourceType;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "专题ID",dataType = "string")
    private String subjectId;

    @ApiModelProperty(value = "主题目录名称，用于显示")
    private String subjectName;
    @ApiModelProperty(value = "数据类型别名，用于显示")
    private String dataTypeCn;
    @ApiModelProperty(value = "数据源类型别名，用于显示")
    private String datasourceTypeCn;
    @ApiModelProperty(value = "所在目录，用于显示")
    private String xpath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Long getDataSize() {
        return dataSize;
    }

    public void setDataSize(Long dataSize) {
        this.dataSize = dataSize;
    }

    public String getDatasourceType() {
        return datasourceType;
    }

    public void setDatasourceType(String datasourceType) {
        this.datasourceType = datasourceType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDataTypeCn() {
        return dataTypeCn;
    }

    public void setDataTypeCn(String dataTypeCn) {
        this.dataTypeCn = dataTypeCn;
    }

    public String getDatasourceTypeCn() {
        return datasourceTypeCn;
    }

    public void setDatasourceTypeCn(String datasourceTypeCn) {
        this.datasourceTypeCn = datasourceTypeCn;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    @Override
    public String toString() {
        return "AgDataOverview{" +
                "id='" + id + '\'' +
                ", dataName='" + dataName + '\'' +
                ", subjectType='" + subjectType + '\'' +
                ", dataType='" + dataType + '\'' +
                ", dataSize=" + dataSize +
                ", datasourceType='" + datasourceType + '\'' +
                ", createTime=" + createTime +
                ", subjectId='" + subjectId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", dataTypeCn='" + dataTypeCn + '\'' +
                ", datasourceTypeCn='" + datasourceTypeCn + '\'' +
                ", xpath='" + xpath + '\'' +
                '}';
    }
}