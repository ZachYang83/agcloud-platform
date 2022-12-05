package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 *
 * @Author: qinyg
 * @Date: 2020/12/17
 * @tips: 实体类
 */
@ApiModel(value="AgHouse实体对象")
public class AgHouse {
    @ApiModelProperty(value="主键ID")
    private String id;

    @ApiModelProperty(value="源文件名称")
    private String sourceName;

    @ApiModelProperty(value="文件存储路径")
    private String storeFullPath;

    @ApiModelProperty(value="文件后缀")
    private String suffix;

    @ApiModelProperty(value="文件总大小")
    private String size;

    @ApiModelProperty(value="1模型图类型；2户型图类型；3缩略图")
    private String type;

    @ApiModelProperty(value="类别id")
    private String categoryId;

    @ApiModelProperty(value="创建时间")
    private Date createTime;

    @ApiModelProperty(value="修改时间")
    private Date modifyTime;

    @ApiModelProperty(value="备注")
    private String remark;

    @ApiModelProperty(value="源文件名称")
    private String oldName;

    @ApiModelProperty(value="房屋名称")
    private String hourseName;

    @ApiModelProperty(value="宅基地面积（平方米）")
    private String homesteadArea;

    @ApiModelProperty(value="占地面积（平方米）")
    private String floorArea;

    @ApiModelProperty(value="建筑面积（平方米）")
    private String coveredArea;

    @ApiModelProperty(value="造价估计（平方米）")
    private String costEstimates;

    @ApiModelProperty(value="是否在列表展示（1展示，0不展示）")
    private String isShow;

    @ApiModelProperty(value="资源模型关联id（当前表）")
    private String sourceId;

    @ApiModelProperty(value="用户id")
    private String userId;

    @ApiModelProperty(value="缩略图base64")
    private String thumb;

    @ApiModelProperty(value="模型关联属性表名")
    private String tableName;

    @ApiModelProperty(value="结构类型")
    private String structureType;

    @ApiModelProperty(value="模型大小")
    private String modelSize;

    @ApiModelProperty(value="1是rft；2是3dtiles")
    private Integer status;

    @ApiModelProperty(value="构件类目编码：表代码-大类.中类.小类.细类")
    private String componentCode;

    @ApiModelProperty(value="构件类目编码名称：表代码-大类.中类.小类.细类")
    private String componentCodeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName == null ? null : sourceName.trim();
    }

    public String getStoreFullPath() {
        return storeFullPath;
    }

    public void setStoreFullPath(String storeFullPath) {
        this.storeFullPath = storeFullPath == null ? null : storeFullPath.trim();
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix == null ? null : suffix.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
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

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName == null ? null : oldName.trim();
    }

    public String getHourseName() {
        return hourseName;
    }

    public void setHourseName(String hourseName) {
        this.hourseName = hourseName == null ? null : hourseName.trim();
    }

    public String getHomesteadArea() {
        return homesteadArea;
    }

    public void setHomesteadArea(String homesteadArea) {
        this.homesteadArea = homesteadArea == null ? null : homesteadArea.trim();
    }

    public String getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(String floorArea) {
        this.floorArea = floorArea == null ? null : floorArea.trim();
    }

    public String getCoveredArea() {
        return coveredArea;
    }

    public void setCoveredArea(String coveredArea) {
        this.coveredArea = coveredArea == null ? null : coveredArea.trim();
    }

    public String getCostEstimates() {
        return costEstimates;
    }

    public void setCostEstimates(String costEstimates) {
        this.costEstimates = costEstimates == null ? null : costEstimates.trim();
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow == null ? null : isShow.trim();
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId == null ? null : sourceId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb == null ? null : thumb.trim();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    public String getStructureType() {
        return structureType;
    }

    public void setStructureType(String structureType) {
        this.structureType = structureType == null ? null : structureType.trim();
    }

    public String getModelSize() {
        return modelSize;
    }

    public void setModelSize(String modelSize) {
        this.modelSize = modelSize == null ? null : modelSize.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode == null ? null : componentCode.trim();
    }

    public String getComponentCodeName() {
        return componentCodeName;
    }

    public void setComponentCodeName(String componentCodeName) {
        this.componentCodeName = componentCodeName == null ? null : componentCodeName.trim();
    }
}