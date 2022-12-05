package com.augurit.agcloud.agcom.agsupport.domain;

import java.util.Date;

/**
 * @Author:Dreram
 * @Description: 书签
 * @Date:created in :13:59 2019/4/8
 * @Modified By:
 */
public class AgBookmark {
    private String id;
    private String userId;
    private String data;
    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
