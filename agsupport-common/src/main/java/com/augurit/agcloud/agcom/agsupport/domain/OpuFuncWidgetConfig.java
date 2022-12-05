package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * create by: libc
 * description: 地图运维-配置管理-微件管理
 * create time: 16:23 2020/7/3
 */
public class OpuFuncWidgetConfig {

    // id
    private String id;
    //微件编码
    private String funcCode;
    // 参数标识
    private String configType;
    // 参数类型
    private String configKey;
    // 是否激活使用 "1":激活  “0”:未激活
    private String isActive;
    // 参数值
    private String configValue;
    // 微件访问地址
    private String funcInvokeUrl;

    public String getFuncInvokeUrl() {
        return funcInvokeUrl;
    }

    public void setFuncInvokeUrl(String funcInvokeUrl) {
        this.funcInvokeUrl = funcInvokeUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
}