package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * 属性授权
 *
 * @author Administrator
 */
public class AgUserData {
    private String id;
    private String tableid;
    private String roleid;
    private String distid;
    private String isedit;
    private String ischeck;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getDistid() {
        return distid;
    }

    public void setDistid(String distid) {
        this.distid = distid;
    }

    public String getIsedit() {
        return isedit;
    }

    public void setIsedit(String isedit) {
        this.isedit = isedit;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

}
