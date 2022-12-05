package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @Author: libc
 * @Date: 2020/12/14
 * @tips: 实体类
 */
@ApiModel(value="Agcim3dentityA实体对象")
public class Agcim3dentityA {
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

    @ApiModelProperty(value="host属性")
    private String host;

    @ApiModelProperty(value="geometry属性")
    private String geometry;

    @ApiModelProperty(value="topologyelements属性")
    private String topologyelements;

    @ApiModelProperty(value="boundingbox属性")
    private String boundingbox;

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host == null ? null : host.trim();
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
}