package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 *
 * @Author: qinyg
 * @Date: 2020/10/20
 * @tips: 实体类
 */
@ApiModel(value="AgMaterialsComponent实体对象")
public class AgMaterialsComponent {
    @ApiModelProperty(value="id属性")
    private String id;

    @ApiModelProperty(value="objectid属性")
    private String objectid;

    @ApiModelProperty(value="name属性")
    private String name;

    @ApiModelProperty(value="version属性")
    private Long version;

    @ApiModelProperty(value="infotype属性")
    private String infotype;

    @ApiModelProperty(value="profession属性")
    private String profession;

    @ApiModelProperty(value="level属性")
    private String level;

    @ApiModelProperty(value="catagory属性")
    private String catagory;

    @ApiModelProperty(value="familyname属性")
    private String familyname;

    @ApiModelProperty(value="familytype属性")
    private String familytype;

    @ApiModelProperty(value="materialid属性")
    private String materialid;

    @ApiModelProperty(value="elementattributes属性")
    private String elementattributes;

    @ApiModelProperty(value="categorypath属性")
    private String categorypath;

    @ApiModelProperty(value="geometry属性")
    private String geometry;

    @ApiModelProperty(value="topologyelements属性")
    private String topologyelements;

    @ApiModelProperty(value="boundingbox属性")
    private String boundingbox;

    @ApiModelProperty(value="材质")
    private String texture;

    @ApiModelProperty(value="尺寸（长x宽x高）")
    private String measure;

    @ApiModelProperty(value="厂商")
    private String vendor;

    @ApiModelProperty(value="单价（元）")
    private String singlePrice;

    @ApiModelProperty(value="材料关联属性表名")
    private String tableName;

    @ApiModelProperty(value="createTime属性")
    private Date createTime;

    @ApiModelProperty(value="modifyTime属性")
    private Date modifyTime;

    @ApiModelProperty(value="备注")
    private String remark;

    @ApiModelProperty(value="缩略图文件名")
    private String thumbFileName;

    @ApiModelProperty(value="glb文件名")
    private String glbFileName;

    @ApiModelProperty(value="1是rfa；2是glb")
    private Integer status;

    @ApiModelProperty(value="模型大小")
    private String modelSize;

    @ApiModelProperty(value="构件类目编码：表代码-大类.中类.小类.细类")
    private String componentCode;

    @ApiModelProperty(value="构件类目编码名称：表代码-大类.中类.小类.细类")
    private String componentCodeName;

    @ApiModelProperty(value="长x宽x高")
    private String specification;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid == null ? null : objectid.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getInfotype() {
        return infotype;
    }

    public void setInfotype(String infotype) {
        this.infotype = infotype == null ? null : infotype.trim();
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession == null ? null : profession.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory == null ? null : catagory.trim();
    }

    public String getFamilyname() {
        return familyname;
    }

    public void setFamilyname(String familyname) {
        this.familyname = familyname == null ? null : familyname.trim();
    }

    public String getFamilytype() {
        return familytype;
    }

    public void setFamilytype(String familytype) {
        this.familytype = familytype == null ? null : familytype.trim();
    }

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid == null ? null : materialid.trim();
    }

    public String getElementattributes() {
        return elementattributes;
    }

    public void setElementattributes(String elementattributes) {
        this.elementattributes = elementattributes == null ? null : elementattributes.trim();
    }

    public String getCategorypath() {
        return categorypath;
    }

    public void setCategorypath(String categorypath) {
        this.categorypath = categorypath == null ? null : categorypath.trim();
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry == null ? null : geometry.trim();
    }

    public String getTopologyelements() {
        return topologyelements;
    }

    public void setTopologyelements(String topologyelements) {
        this.topologyelements = topologyelements == null ? null : topologyelements.trim();
    }

    public String getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(String boundingbox) {
        this.boundingbox = boundingbox == null ? null : boundingbox.trim();
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture == null ? null : texture.trim();
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure == null ? null : measure.trim();
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor == null ? null : vendor.trim();
    }

    public String getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(String singlePrice) {
        this.singlePrice = singlePrice == null ? null : singlePrice.trim();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
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

    public String getThumbFileName() {
        return thumbFileName;
    }

    public void setThumbFileName(String thumbFileName) {
        this.thumbFileName = thumbFileName == null ? null : thumbFileName.trim();
    }

    public String getGlbFileName() {
        return glbFileName;
    }

    public void setGlbFileName(String glbFileName) {
        this.glbFileName = glbFileName == null ? null : glbFileName.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getModelSize() {
        return modelSize;
    }

    public void setModelSize(String modelSize) {
        this.modelSize = modelSize == null ? null : modelSize.trim();
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

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification == null ? null : specification.trim();
    }
}