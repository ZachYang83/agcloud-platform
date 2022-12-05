package com.augurit.agcloud.agcom.agsupport.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName AgBimFile
 * @Description TODO
 * @Author Administrator
 * @Date 2019/12/2 16:41
 * @Version 1.0
 **/
@ApiModel(value = "BIM模型")
public class AgBimFile {
    @ApiModelProperty(value = "bim模型ID")
    private String id;
    @ApiModelProperty(value = "bim模型名称")
    private String name;
    @ApiModelProperty(value = "bim模型别名")
    private String alias;
    @ApiModelProperty(value = "BIM服务")
    private String serviceUrl;
    @ApiModelProperty(value = "要素服务")
    private String serviceFactorUrl;
    @ApiModelProperty(value = "审批单位")
    private String approvalUnit;
    @ApiModelProperty(value = "建设单位")
    private String constructionUnit;
    @ApiModelProperty(value = "用途")
    private String purpose;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "bim模型md5")
    private String bimMd5;
    @ApiModelProperty(value = "关联服务清单")
    private String relationServiceList;
    @ApiModelProperty(value = "bim模型创建用户")
    private String createName;
    @ApiModelProperty(value = "bim模型创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "bim模型修改时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "服务状态")
    private String state = "0";

    @ApiModelProperty(value = "项目id")
    private String projectId;

    @Transient
    private String fileUrl;

    private AgBimVersion bimVersion;

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getServiceFactorUrl() {
        return serviceFactorUrl;
    }

    public void setServiceFactorUrl(String serviceFactorUrl) {
        this.serviceFactorUrl = serviceFactorUrl;
    }


    public String getApprovalUnit() {
        return approvalUnit;
    }

    public void setApprovalUnit(String approvalUnit) {
        this.approvalUnit = approvalUnit;
    }

    public String getConstructionUnit() {
        return constructionUnit;
    }

    public void setConstructionUnit(String constructionUnit) {
        this.constructionUnit = constructionUnit;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getBimMd5() {
        return bimMd5;
    }

    public void setBimMd5(String bimMd5) {
        this.bimMd5 = bimMd5;
    }

    public String getRelationServiceList() {
        return relationServiceList;
    }

    public void setRelationServiceList(String relationServiceList) {
        this.relationServiceList = relationServiceList;
    }
    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public AgBimVersion getBimVersion() {
        return bimVersion;
    }

    public void setBimVersion(AgBimVersion bimVersion) {
        this.bimVersion = bimVersion;
    }
}
