package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Administrator on 2017-03-31.
 */
public class AgLabel {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("标注信息")
    private String data;

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
