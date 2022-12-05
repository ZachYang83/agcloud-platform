package com.augurit.agcloud.agcom.agsupport.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * bim版本
 *
 * @ClassName AgBimVersion
 * @Description TODO
 * @Author Administrator
 * @Date 2019/12/2 16:15
 * @Version 1.0
 **/
public class AgBimVersion {
    @ApiModelProperty(value = "版本ID")
    private String id;
    @ApiModelProperty(value = "版本关联BIM主键ID")
    private String pkId;
    @ApiModelProperty(value = "变更版本号")
    private String changeVersion;
    @ApiModelProperty(value = "变更名称")
    private String changeName;
    @ApiModelProperty(value = "变更人")
    private String changePeople;
    @ApiModelProperty(value = "变更类型")
    private String changeType;
    @ApiModelProperty(value = "状态")
    private String changeStatus;
    @ApiModelProperty(value = "变更描述")
    private String changeMsg;
    @ApiModelProperty(value = "是否为当前版本")
    private String isCurrent = "NOT_USE";
    @ApiModelProperty(value = "bim存储位置")
    private String bimPath;
    @ApiModelProperty(value = "bim扩展名")
    private String bimExtension;
    @ApiModelProperty(value = "bim存储大小")
    private String bimLength;
    @ApiModelProperty(value = "bim网络链接")
    private String bimUrl;
    @ApiModelProperty(value = "bim坐标信息")
    private String bimCoordinate;
    @ApiModelProperty(value = "bim Md5")
    private String bimMd5;
    @ApiModelProperty(value = "变更时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date changeTime;

    private String fileName;


    @Transient
    private String stateName;
    @Transient
    private String changeTypeName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public String getChangeVersion() {
        return changeVersion;
    }

    public void setChangeVersion(String changeVersion) {
        this.changeVersion = changeVersion;
    }

    public String getChangeName() {
        return changeName;
    }

    public void setChangeName(String changeName) {
        this.changeName = changeName;
    }

    public String getChangePeople() {
        return changePeople;
    }

    public void setChangePeople(String changePeople) {
        this.changePeople = changePeople;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public String getChangeMsg() {
        return changeMsg;
    }

    public void setChangeMsg(String changeMsg) {
        this.changeMsg = changeMsg;
    }

    public String getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(String isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getBimPath() {
        return bimPath;
    }

    public void setBimPath(String bimPath) {
        this.bimPath = bimPath;
    }

    public String getBimExtension() {
        return bimExtension;
    }

    public void setBimExtension(String bimExtension) {
        this.bimExtension = bimExtension;
    }

    public String getBimLength() {
        return bimLength;
    }

    public void setBimLength(String bimLength) {
        this.bimLength = bimLength;
    }

    public String getBimUrl() {
        return bimUrl;
    }

    public void setBimUrl(String bimUrl) {
        this.bimUrl = bimUrl;
    }

    public String getBimCoordinate() {
        return bimCoordinate;
    }

    public void setBimCoordinate(String bimCoordinate) {
        this.bimCoordinate = bimCoordinate;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }


    public String getBimMd5() {
        return bimMd5;
    }

    public void setBimMd5(String bimMd5) {
        this.bimMd5 = bimMd5;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getChangeTypeName() {
        return changeTypeName;
    }

    public void setChangeTypeName(String changeTypeName) {
        this.changeTypeName = changeTypeName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
