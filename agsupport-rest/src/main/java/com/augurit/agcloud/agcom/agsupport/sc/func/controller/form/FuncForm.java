package com.augurit.agcloud.agcom.agsupport.sc.func.controller.form;

import java.util.Map;

/**
 * Created by Augurit on 2017-05-18.
 */
public class FuncForm {
    private String id;
    private String name;        //微件名称
    private String code;        //编码
    private String iconClass;   //图标class
    private String type;        //微件类型
    private String isConfig;    //是否可配置
    private String visiable;    //是否显示
    private String isShortKey;  //是否为快捷键
    private String isHotPlug;   //是否热插拔
    private String isUpload;    //是否为上传插件
    private Map json;        //扩展字段

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Map getJson() {
        return json;
    }

    public void setJson(Map json) {
        this.json = json;
    }

    public String getIsHotPlug() {
        return isHotPlug;
    }

    public void setIsHotPlug(String isHotPlug) {
        this.isHotPlug = isHotPlug;
    }

    public String getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(String isUpload) {
        this.isUpload = isUpload;
    }
}
