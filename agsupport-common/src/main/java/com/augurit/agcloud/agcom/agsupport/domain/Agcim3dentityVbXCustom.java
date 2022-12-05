package com.augurit.agcloud.agcom.agsupport.domain;

public class Agcim3dentityVbXCustom {
    private String id;

    private String objectid;

    private String name;

    private Long version;

    private String infotype;

    private String catagory;

    private String profession;

    private String level;

    private String materialid;

    private String elementattributes;

    private String geometry;

    private String topologyelements;

    private String boundingbox;

    private String host;

    private String related;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

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

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory == null ? null : catagory.trim();
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