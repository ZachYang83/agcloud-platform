package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 *
 * @Author: qinyg
 * @Date: 2020/10/25
 * @tips: 实体类
 */
@ApiModel(value="AgUserDesignMaterials实体对象")
public class AgUserDesignMaterials {
    @ApiModelProperty(value = "id属性")
    private String id;

    @ApiModelProperty(value = "1房屋模型；2小品模型；3构件模型")
    private String type;

    @ApiModelProperty(value = "模型名称")
    private String name;

    @ApiModelProperty(value = "坐标")
    private String position;

    @ApiModelProperty(value = "方向")
    private String orientation;

    @ApiModelProperty(value = "模型地址")
    private String url;

    @ApiModelProperty(value = "模型矩阵")
    private String modelMatrix;

    @ApiModelProperty(value = "属性地址")
    private String propertyUrl;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "ag_user_design_scheme的主键")
    private String designSchemeId;

    @ApiModelProperty(value = "角度")
    private String angle;

    @ApiModelProperty(value = "房屋关联属性表名")
    private String tableName;

    @ApiModelProperty(value = "组件")
    private String components;

    @ApiModelProperty(value = "样式")
    private String style;

    @ApiModelProperty(value = "tile url")
    private String tileUrl;

    @ApiModelProperty(value = "组件类型")
    private String componentType;

    @ApiModelProperty(value = "构件id，前端参数")
    private String componentId;

    @ApiModelProperty(value = "包围盒")
    private String boundingbox;

    @ApiModelProperty(value = "拓扑关系")
    private String topologyelements;

    @ApiModelProperty(value = "obb包围盒中心")
    private String obbCenter;

    @ApiModelProperty(value = "rvt模型包围盒中心与3dtiles模型包围盒中心差")
    private String subtract;

    @ApiModelProperty(value = "逗号分隔（自关联。房屋所关联的构件或者构件所在的房屋）")
    private String relationIds;

    @ApiModelProperty(value = "尺寸（长x宽x高）")
    private String measure;

    @ApiModelProperty(value = "构件的尺寸,用来计算再次加载时剖切的大小")
    private String size;

    @ApiModelProperty(value = "保存剖切的位置")
    private String clipMatrix;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation == null ? null : orientation.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getModelMatrix() {
        return modelMatrix;
    }

    public void setModelMatrix(String modelMatrix) {
        this.modelMatrix = modelMatrix == null ? null : modelMatrix.trim();
    }

    public String getPropertyUrl() {
        return propertyUrl;
    }

    public void setPropertyUrl(String propertyUrl) {
        this.propertyUrl = propertyUrl == null ? null : propertyUrl.trim();
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getDesignSchemeId() {
        return designSchemeId;
    }

    public void setDesignSchemeId(String designSchemeId) {
        this.designSchemeId = designSchemeId == null ? null : designSchemeId.trim();
    }

    public String getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        this.angle = angle == null ? null : angle.trim();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    public String getComponents() {
        return components;
    }

    public void setComponents(String components) {
        this.components = components == null ? null : components.trim();
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style == null ? null : style.trim();
    }

    public String getTileUrl() {
        return tileUrl;
    }

    public void setTileUrl(String tileUrl) {
        this.tileUrl = tileUrl == null ? null : tileUrl.trim();
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType == null ? null : componentType.trim();
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId == null ? null : componentId.trim();
    }

    public String getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(String boundingbox) {
        this.boundingbox = boundingbox == null ? null : boundingbox.trim();
    }

    public String getTopologyelements() {
        return topologyelements;
    }

    public void setTopologyelements(String topologyelements) {
        this.topologyelements = topologyelements == null ? null : topologyelements.trim();
    }

    public String getObbCenter() {
        return obbCenter;
    }

    public void setObbCenter(String obbCenter) {
        this.obbCenter = obbCenter == null ? null : obbCenter.trim();
    }

    public String getSubtract() {
        return subtract;
    }

    public void setSubtract(String subtract) {
        this.subtract = subtract == null ? null : subtract.trim();
    }

    public String getRelationIds() {
        return relationIds;
    }

    public void setRelationIds(String relationIds) {
        this.relationIds = relationIds == null ? null : relationIds.trim();
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure == null ? null : measure.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public String getClipMatrix() {
        return clipMatrix;
    }

    public void setClipMatrix(String clipMatrix) {
        this.clipMatrix = clipMatrix == null ? null : clipMatrix.trim();
    }
}