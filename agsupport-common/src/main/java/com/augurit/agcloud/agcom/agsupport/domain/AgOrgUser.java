package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * Created by Augurit on 2017-04-27.
 */
public class AgOrgUser {
    private String id;
    private String orgId;   //机构id
    private String userId;  //用户id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
