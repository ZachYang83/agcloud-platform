package com.augurit.agcloud.agcom.agsupport.domain;

import java.util.List;

/**
 * Created by Augurit on 2017-05-02.
 */
public class AgFunc {
    private String id;
    private String name;        //微件名称
    private String code;        //编码
    private String iconClass;   //图标class
    private String type;        //类型
    private String isConfig;    //是否可配置
    private String visiable;    //是否显示
    private String isShortKey;  //是否为快捷键
    private String isHotPlug;   //是否热插拔
    private String isUpload;    //是否为上传插件
    private int orderNm;     //排序字段
    private String data;        //扩展字段json串
    private String isSystem;    //是否为系统级
    private String isMobile;    //是否供移动端使用
    private String roleFuncId;
    private String parentFuncId; //父id
    private List<AgFunc> childrenList;
    private String smallImgPath;
    private String middleImgPath;
    private String bigImgPath;
    private String hugeImgPath;
    private String funcDesc;
    private String widgetType;
    private String funcInvokeUrl;   //微件访问路径
    private String pagePosition;

    public String getFuncInvokeUrl() { return funcInvokeUrl; }

    public void setFuncInvokeUrl(String funcInvokeUrl) { this.funcInvokeUrl = funcInvokeUrl; }

    public String getFuncDesc() {
        return funcDesc;
    }

    public void setFuncDesc(String funcDesc) {
        this.funcDesc = funcDesc;
    }


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

    public int getOrderNm() {
        return orderNm;
    }

    public void setOrderNm(int orderNm) {
        this.orderNm = orderNm;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getRoleFuncId() {
        return roleFuncId;
    }

    public void setRoleFuncId(String roleFuncId) {
        this.roleFuncId = roleFuncId;
    }

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    public String getIsMobile() {
        return isMobile;
    }

    public void setIsMobile(String isMobile) {
        this.isMobile = isMobile;
    }

    public String getParentFuncId() {
        return parentFuncId;
    }

    public void setParentFuncId(String parentFuncId) {
        this.parentFuncId = parentFuncId;
    }

    public List<AgFunc> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<AgFunc> childrenList) {
        this.childrenList = childrenList;
    }

    public String getSmallImgPath() {
        return smallImgPath;
    }

    public void setSmallImgPath(String smallImgPath) {
        this.smallImgPath = smallImgPath;
    }

    public String getMiddleImgPath() {
        return middleImgPath;
    }

    public void setMiddleImgPath(String middleImgPath) {
        this.middleImgPath = middleImgPath;
    }

    public String getBigImgPath() {
        return bigImgPath;
    }

    public void setBigImgPath(String bigImgPath) {
        this.bigImgPath = bigImgPath;
    }

    public String getHugeImgPath() {
        return hugeImgPath;
    }

    public void setHugeImgPath(String hugeImgPath) {
        this.hugeImgPath = hugeImgPath;
    }

    public String getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(String widgetType) {
        this.widgetType = widgetType;
    }

    public String getPagePosition() {
        return pagePosition;
    }

    public void setPagePosition(String pagePosition) {
        this.pagePosition = pagePosition;
    }
}
