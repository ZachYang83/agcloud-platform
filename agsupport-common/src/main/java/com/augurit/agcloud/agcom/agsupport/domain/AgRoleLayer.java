package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * Created by Augurit on 2017-04-25.
 */
public class AgRoleLayer {
    private String id;
    private String roleId;
    private String dirLayerId;
    private String addFlag;
    private String editable;
    private String queryable;
    private String isShow;
    private String isBaseMap;
    private String extent;
    private String queryCon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDirLayerId() {
        return dirLayerId;
    }

    public void setDirLayerId(String dirLayerId) {
        this.dirLayerId = dirLayerId;
    }

    public String getAddFlag() {
        return addFlag;
    }

    public void setAddFlag(String addFlag) {
        this.addFlag = addFlag;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }
    public String getQueryable() {
        return queryable;
    }

    public void setQueryable(String queryable) {
        this.queryable = queryable;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getIsBaseMap() {
        return isBaseMap;
    }

    public void setIsBaseMap(String isBaseMap) {
        this.isBaseMap = isBaseMap;
    }

    public String getExtent() {
        return extent;
    }

    public void setExtent(String extent) {
        this.extent = extent;
    }

    public String getQueryCon() {
        return queryCon;
    }

    public void setQueryCon(String queryCon) {
        this.queryCon = queryCon;
    }
}
