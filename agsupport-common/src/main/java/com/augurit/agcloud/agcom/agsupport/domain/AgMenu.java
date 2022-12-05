package com.augurit.agcloud.agcom.agsupport.domain;

import com.augurit.agcloud.agcom.agsupport.util.SysUtil;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-07-21.
 */
public class AgMenu {
    private String id;
    private String name;
    private String url;
    private String isRelpath;//是否相对路径 1是 0否
    private String pcode;
    private String orderNm;
    private String isUse;
    private String isSystem;
    private String roleMenuId;
    private String dirId;
    private String xpath;
    private String icon;
    private String middleIcon;

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

    public String getUrl() {
        return SysUtil.replaceMenuOut(url);
    }

    public void setUrl(String url) {
        this.url = SysUtil.replaceMenuIn(url);
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getOrderNm() {
        return orderNm;
    }

    public void setOrderNm(String orderNm) {
        this.orderNm = orderNm;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    public String getRoleMenuId() {
        return roleMenuId;
    }

    public void setRoleMenuId(String roleMenuId) {
        this.roleMenuId = roleMenuId;
    }

    public String getDirId() {
        return dirId;
    }

    public void setDirId(String dirId) {
        this.dirId = dirId;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIsRelpath() {
        return isRelpath;
    }

    public void setIsRelpath(String isRelpath) {
        this.isRelpath = isRelpath;
    }

    public String getMiddleIcon() {
        return middleIcon;
    }

    public void setMiddleIcon(String middleIcon) {
        this.middleIcon = middleIcon;
    }
}

