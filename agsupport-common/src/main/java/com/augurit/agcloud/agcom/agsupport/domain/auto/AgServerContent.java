package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

@ApiModel(value="com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContent")
public class AgServerContent {
    @ApiModelProperty(value="id主键id")
    private String id;

    @ApiModelProperty(value="userId用户id")
    private String userId;

    @ApiModelProperty(value="title标题")
    private String title;

    @ApiModelProperty(value="name名称（源文件名）")
    private String name;

    @ApiModelProperty(value="type服务类型")
    private String type;

    @ApiModelProperty(value="path服务文件对应的本地路径")
    private String path;

    @ApiModelProperty(value="size文件大小")
    private String size;

    @ApiModelProperty(value="createTime创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value="modifyTime修改时间")
    private Timestamp modifyTime;

    @ApiModelProperty(value="remark备注")
    private String remark;

    @ApiModelProperty(value="tags标签（可以多个，逗号隔开，如（标签1，标签2））")
    private String tags;

    @ApiModelProperty(value="groupId服务分组id")
    private String groupId;

    @ApiModelProperty(value="sourceType文件来源。1：agcim服务管理； 2：BIM审查")
    private String sourceType;

    @ApiModelProperty(value="sourceRelId文件来源关联的业务ID （BIM审查关联BIM审查项目id）")
    private String sourceRelId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType == null ? null : sourceType.trim();
    }

    public String getSourceRelId() {
        return sourceRelId;
    }

    public void setSourceRelId(String sourceRelId) {
        this.sourceRelId = sourceRelId == null ? null : sourceRelId.trim();
    }
}