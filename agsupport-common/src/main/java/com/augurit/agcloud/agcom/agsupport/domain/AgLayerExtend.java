package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * Created by Augurit on 2017-04-21.
 */
public class AgLayerExtend {
    private String id;
    private String name;
    private String value;
    private String parentId;

    public AgLayerExtend() {
    }

    public AgLayerExtend(String id, String name, String value, String parentId) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
