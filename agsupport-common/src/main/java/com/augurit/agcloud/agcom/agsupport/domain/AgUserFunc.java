package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * Created by Augurit on 2017-05-18.
 */
public class AgUserFunc {
    private String id;
    private String funcId;      //微件id
    private String userId;          //用户id
    private String isConfig;        //是否可配置
    private String visiable;        //是否显示
    private String isShortKey;      //是否为快捷键
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsConfig() {
        return isConfig;
    }

    public void setIsConfig(String isConfig) {
        this.isConfig = isConfig;
    }

    public String getVisiable() {
        return visiable;
    }

    public void setVisiable(String visiable) {
        this.visiable = visiable;
    }

    public String getIsShortKey() {
        return isShortKey;
    }

    public void setIsShortKey(String isShortKey) {
        this.isShortKey = isShortKey;
    }
}
