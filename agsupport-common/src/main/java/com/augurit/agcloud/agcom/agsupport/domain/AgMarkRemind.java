package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * @author zhangmy
 * @Description: 标注分享消息提醒功能bean
 * @date 2019-10-31 15:20
 */
public class AgMarkRemind {
    private String id;
    private String userId;
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
