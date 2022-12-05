package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * @Author:zrr
 * @Description:
 * @Date:created in :16:58 2018/7/13
 * @Modified By:
 */
public class AgUserProject {
    private String id;
    private String UserId;//用户id
    private String ProjectId;//专题id



    //getter and setter methold
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
