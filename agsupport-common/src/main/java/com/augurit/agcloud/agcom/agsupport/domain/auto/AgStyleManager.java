package com.augurit.agcloud.agcom.agsupport.domain.auto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel(value = "AgStyleManager实体对象")
public class AgStyleManager {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "名称", dataType = "string")
    private String name;

    @ApiModelProperty(value = "样式信息", dataType = "string")
    private String information;

    @ApiModelProperty(value = "创建时间", dataType = "string")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改时间", dataType = "string")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    @ApiModelProperty(value = "备注", dataType = "string")
    private String remark;

    @ApiModelProperty(value = "样式图片存储base64", dataType = "string")
    private String viewImg;

    @ApiModelProperty(value = "可应用该样式的图层类型")
    private String layerType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information == null ? null : information.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getViewImg() {
        return viewImg;
    }

    public void setViewImg(String viewImg) {
        this.viewImg = viewImg == null ? null : viewImg.trim();
    }

    public String getLayerType() {
        return layerType;
    }

    public void setLayerType(String layerType) {


        this.layerType = layerType == null ? null : layerType.trim();
    }


    @Override
    public String toString() {
        return "AgStyleManager{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", information='" + information + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", remark='" + remark + '\'' +
                ", viewImg='" + viewImg + '\'' +
                ", layerType='" + layerType + '\'' +
                '}';
    }
}