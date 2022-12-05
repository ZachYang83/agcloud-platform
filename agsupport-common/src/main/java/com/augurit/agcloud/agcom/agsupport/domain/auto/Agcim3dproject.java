package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @Author: libc
 * @Date: 2020/12/10
 * @tips: 实体类
 */
@ApiModel(value="Agcim3dproject实体对象")
public class Agcim3dproject {
    @ApiModelProperty(value="项目ID")
    private String id;

    @ApiModelProperty(value="工程名称")
    private String name;

    @ApiModelProperty(value="工程所用的单位")
    private String units;

    @ApiModelProperty(value="项目所在地址，地名")
    private String address;

    @ApiModelProperty(value="行政主体信息（JSON）")
    private String owner;

    @ApiModelProperty(value="项目的创建时间")
    private String creattime;

    @ApiModelProperty(value="工程阶段")
    private String projectphase;

    @ApiModelProperty(value="建筑类型:建筑;市政;桥梁;")
    private String constructiontype;

    @ApiModelProperty(value="经济技术指标")
    private String ecoindex;

    @ApiModelProperty(value="经济技术指标表的表名（如有）")
    private String ecoindextable;

    @ApiModelProperty(value="包括创建者，工程人等不常用的信息。JSON字符串")
    private String metadata;

    @ApiModelProperty(value="服务地址（i3s:3dtiles）")
    private String serverurl;

    @ApiModelProperty(value="服务类型（i3s:3dtiles）")
    private String servertype;

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

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units == null ? null : units.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public String getCreattime() {
        return creattime;
    }

    public void setCreattime(String creattime) {
        this.creattime = creattime == null ? null : creattime.trim();
    }

    public String getProjectphase() {
        return projectphase;
    }

    public void setProjectphase(String projectphase) {
        this.projectphase = projectphase == null ? null : projectphase.trim();
    }

    public String getConstructiontype() {
        return constructiontype;
    }

    public void setConstructiontype(String constructiontype) {
        this.constructiontype = constructiontype == null ? null : constructiontype.trim();
    }

    public String getEcoindex() {
        return ecoindex;
    }

    public void setEcoindex(String ecoindex) {
        this.ecoindex = ecoindex == null ? null : ecoindex.trim();
    }

    public String getEcoindextable() {
        return ecoindextable;
    }

    public void setEcoindextable(String ecoindextable) {
        this.ecoindextable = ecoindextable == null ? null : ecoindextable.trim();
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata == null ? null : metadata.trim();
    }

    public String getServerurl() {
        return serverurl;
    }

    public void setServerurl(String serverurl) {
        this.serverurl = serverurl == null ? null : serverurl.trim();
    }

    public String getServertype() {
        return servertype;
    }

    public void setServertype(String servertype) {
        this.servertype = servertype == null ? null : servertype.trim();
    }
}