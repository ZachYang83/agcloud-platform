package com.augurit.agcloud.agcom.agsupport.domain;

public class AgMapCustom {
    private String id;

    private byte[] glb;

    public String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getGlb() {
        return glb;
    }

    public void setGlb(byte[] glb) {
        this.glb = glb;
    }
}