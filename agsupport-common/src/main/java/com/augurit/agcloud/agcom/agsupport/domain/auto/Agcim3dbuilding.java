package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @Author: libc
 * @Date: 2020/12/10
 * @tips: 实体类
 */
@ApiModel(value="Agcim3dbuilding实体对象")
public class Agcim3dbuilding {
    @ApiModelProperty(value="ID")
    private String id;

    @ApiModelProperty(value="项目id")
    private String projectid;

    @ApiModelProperty(value="BIM建筑名称")
    private String buildingname;

    @ApiModelProperty(value="位置")
    private String location;

    @ApiModelProperty(value="创建时间")
    private String createtime;

    @ApiModelProperty(value="建筑类型")
    private String buildingtype;

    @ApiModelProperty(value="建筑用途")
    private String usage;

    @ApiModelProperty(value="建筑面积")
    private String builtuparea;

    @ApiModelProperty(value="建筑高度")
    private String height;

    @ApiModelProperty(value="模型的包围盒")
    private String boundingbox;

    @ApiModelProperty(value="模型的基线")
    private String baseline;

    @ApiModelProperty(value="实体表的表名(存储建筑所有构件信息)")
    private String entitytable;

    @ApiModelProperty(value="构件的数量")
    private String entitycount;

    @ApiModelProperty(value="材质表的表名")
    private String materialtable;

    @ApiModelProperty(value="几何表的表名")
    private String geometrytable;

    @ApiModelProperty(value="几何表的类型")
    private String geometrytype;

    @ApiModelProperty(value="以JSON的形式表示其他经济技术指标，比如容积率，绿地率，楼层数")
    private String buildingecoindex;

    @ApiModelProperty(value="其他信息。JSON字符串")
    private String metadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid == null ? null : projectid.trim();
    }

    public String getBuildingname() {
        return buildingname;
    }

    public void setBuildingname(String buildingname) {
        this.buildingname = buildingname == null ? null : buildingname.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    public String getBuildingtype() {
        return buildingtype;
    }

    public void setBuildingtype(String buildingtype) {
        this.buildingtype = buildingtype == null ? null : buildingtype.trim();
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage == null ? null : usage.trim();
    }

    public String getBuiltuparea() {
        return builtuparea;
    }

    public void setBuiltuparea(String builtuparea) {
        this.builtuparea = builtuparea == null ? null : builtuparea.trim();
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height == null ? null : height.trim();
    }

    public String getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(String boundingbox) {
        this.boundingbox = boundingbox == null ? null : boundingbox.trim();
    }

    public String getBaseline() {
        return baseline;
    }

    public void setBaseline(String baseline) {
        this.baseline = baseline == null ? null : baseline.trim();
    }

    public String getEntitytable() {
        return entitytable;
    }

    public void setEntitytable(String entitytable) {
        this.entitytable = entitytable == null ? null : entitytable.trim();
    }

    public String getEntitycount() {
        return entitycount;
    }

    public void setEntitycount(String entitycount) {
        this.entitycount = entitycount == null ? null : entitycount.trim();
    }

    public String getMaterialtable() {
        return materialtable;
    }

    public void setMaterialtable(String materialtable) {
        this.materialtable = materialtable == null ? null : materialtable.trim();
    }

    public String getGeometrytable() {
        return geometrytable;
    }

    public void setGeometrytable(String geometrytable) {
        this.geometrytable = geometrytable == null ? null : geometrytable.trim();
    }

    public String getGeometrytype() {
        return geometrytype;
    }

    public void setGeometrytype(String geometrytype) {
        this.geometrytype = geometrytype == null ? null : geometrytype.trim();
    }

    public String getBuildingecoindex() {
        return buildingecoindex;
    }

    public void setBuildingecoindex(String buildingecoindex) {
        this.buildingecoindex = buildingecoindex == null ? null : buildingecoindex.trim();
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata == null ? null : metadata.trim();
    }
}