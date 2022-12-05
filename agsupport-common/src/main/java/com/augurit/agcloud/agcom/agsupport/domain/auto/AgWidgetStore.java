package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 *
 * @Author: qinyg
 * @Date: 2020/10/21
 * @tips: 实体类
 */
@ApiModel(value="AgWidgetStore实体对象")
public class AgWidgetStore {
    @ApiModelProperty(value="id属性")
    private String id;

    @ApiModelProperty(value="微件商店名称")
    private String name;

    @ApiModelProperty(value="路由名称（以压缩包名称命名）")
    private String routeName;

    @ApiModelProperty(value="功能描述")
    private String description;

    @ApiModelProperty(value="版本")
    private String version;

    @ApiModelProperty(value="创建时间")
    private Date createTime;

    @ApiModelProperty(value="修改时间")
    private Date modifyTime;

    @ApiModelProperty(value="微件文件源文件名称")
    private String widgetName;

    @ApiModelProperty(value="文件大小")
    private String size;

    @ApiModelProperty(value="缩略图源文件名称")
    private String thumbName;

    @ApiModelProperty(value="英文名称")
    private String enName;

    @ApiModelProperty(value="拓展")
    private String expand;

    @ApiModelProperty(value="1未审核；2审核通过；3审核拒绝")
    private Integer status;

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

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName == null ? null : routeName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
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

    public String getWidgetName() {
        return widgetName;
    }

    public void setWidgetName(String widgetName) {
        this.widgetName = widgetName == null ? null : widgetName.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public String getThumbName() {
        return thumbName;
    }

    public void setThumbName(String thumbName) {
        this.thumbName = thumbName == null ? null : thumbName.trim();
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName == null ? null : enName.trim();
    }

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand == null ? null : expand.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}