package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 *
 * @Author: qinyg
 * @Date: 2020/09/30
 * @tips: 实体类
 */
@ApiModel(value="AgUserDesignScheme实体对象")
public class AgUserDesignScheme {
    @ApiModelProperty(value="id属性")
    private String id;

    @ApiModelProperty(value="方案名称")
    private String name;

    @ApiModelProperty(value="地块信息")
    private String landInfo;

    @ApiModelProperty(value="保存位置")
    private String location;

    @ApiModelProperty(value="方案介绍")
    private String description;

    @ApiModelProperty(value="预览图存储位置")
    private String thumb;

    @ApiModelProperty(value="用户id")
    private String userId;

    @ApiModelProperty(value="创建时间")
    private Date createTime;

    @ApiModelProperty(value="修改时间")
    private Date modifyTime;

    @ApiModelProperty(value="是否是默认（0否，1是）")
    private String isDefault;

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

    public String getLandInfo() {
        return landInfo;
    }

    public void setLandInfo(String landInfo) {
        this.landInfo = landInfo == null ? null : landInfo.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb == null ? null : thumb.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
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

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault == null ? null : isDefault.trim();
    }
}