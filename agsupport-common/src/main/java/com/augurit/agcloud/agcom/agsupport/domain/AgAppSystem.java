package com.augurit.agcloud.agcom.agsupport.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class AgAppSystem {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "系统名称",required = true, dataType = "string")
    private String appName;
    @ApiModelProperty(value = "代理登录地址",dataType = "string")
    private String appLoginUrl;
    @ApiModelProperty(value = "首页url",required = true,dataType = "string")
    private String appUrl;
    @ApiModelProperty(value = "游客用户",dataType = "string")
    private String publicUserName;
    @ApiModelProperty(value = "游客用户密码",dataType = "string")
    private String publicPwd;
    @ApiModelProperty(value = "是否有效 0：无效，1：有效,与静默登录功能相关，默认为不开启，默认无效",dataType = "string")
    private String status;
    @ApiModelProperty(value = "是否单点登录，0：表示否，1：表示是，默认非单点登录",dataType = "string")
    private String useSso;
    @ApiModelProperty(value = "图标名称",dataType = "string")
    private String iconAddr;
    @ApiModelProperty(value = "是否IE打开，0：表示不是ie打开，1：表示是,默认不是ie打开",dataType = "string")
    private String useIe;
    @ApiModelProperty(value = "打开方式,0：表示标签页弹出，1：平台桌面弹出,默认标签页弹出",dataType = "string")
    private String openType;
    @ApiModelProperty(value = "原登录地址",dataType = "string")
    private String originalAppLoginUrl;
    @ApiModelProperty(value = "是否固定应用，0表示否，1表示是，默认否",dataType = "string")
    private String ismustShowInDesktop;
    @ApiModelProperty(value = "是否设置可视范围，0表示否，1表示是，默认否",dataType = "string")
    private String issetVisibleRange;
    @ApiModelProperty(value = "创建时间,后台自动生成",dataType = "string")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "修改时间，后台修改",dataType = "string")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date modifyTime;
    @ApiModelProperty(value = "是否已添加到桌面，0：未添加 1：已添加，默认否",dataType = "string")
    private String authorizeStatus;
    @ApiModelProperty(value = "图标底色",dataType = "string")
    private String backGroundColor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public String getAppLoginUrl() {
        return appLoginUrl;
    }

    public void setAppLoginUrl(String appLoginUrl) {
        this.appLoginUrl = appLoginUrl == null ? null : appLoginUrl.trim();
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl == null ? null : appUrl.trim();
    }

    public String getPublicUserName() {
        return publicUserName;
    }

    public void setPublicUserName(String publicUserName) {
        this.publicUserName = publicUserName == null ? null : publicUserName.trim();
    }

    public String getPublicPwd() {
        return publicPwd;
    }

    public void setPublicPwd(String publicPwd) {
        this.publicPwd = publicPwd == null ? null : publicPwd.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getUseSso() {
        return useSso;
    }

    public void setUseSso(String useSso) {
        this.useSso = useSso == null ? null : useSso.trim();
    }

    public String getIconAddr() {
        return iconAddr;
    }

    public void setIconAddr(String iconAddr) {
        this.iconAddr = iconAddr == null ? null : iconAddr.trim();
    }

    public String getUseIe() {
        return useIe;
    }

    public void setUseIe(String useIe) {
        this.useIe = useIe == null ? null : useIe.trim();
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType == null ? null : openType.trim();
    }

    public String getOriginalAppLoginUrl() {
        return originalAppLoginUrl;
    }

    public void setOriginalAppLoginUrl(String originalAppLoginUrl) {
        this.originalAppLoginUrl = originalAppLoginUrl == null ? null : originalAppLoginUrl.trim();
    }

    public String getIsmustShowInDesktop() {
        return ismustShowInDesktop;
    }

    public void setIsmustShowInDesktop(String ismustShowInDesktop) {
        this.ismustShowInDesktop = ismustShowInDesktop == null ? null : ismustShowInDesktop.trim();
    }

    public String getIssetVisibleRange() {
        return issetVisibleRange;
    }

    public void setIssetVisibleRange(String issetVisibleRange) {
        this.issetVisibleRange = issetVisibleRange == null ? null : issetVisibleRange.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getBackGroundColor() {
        return backGroundColor;
    }

    public void setBackGroundColor(String backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    @Override
    public String toString() {
        return "AgAppSystem{" +
                "id='" + id + '\'' +
                ", appName='" + appName + '\'' +
                ", appLoginUrl='" + appLoginUrl + '\'' +
                ", appUrl='" + appUrl + '\'' +
                ", publicUserName='" + publicUserName + '\'' +
                ", publicPwd='" + publicPwd + '\'' +
                ", status='" + status + '\'' +
                ", useSso='" + useSso + '\'' +
                ", iconAddr='" + iconAddr + '\'' +
                ", useIe='" + useIe + '\'' +
                ", openType='" + openType + '\'' +
                ", originalAppLoginUrl='" + originalAppLoginUrl + '\'' +
                ", ismustShowInDesktop='" + ismustShowInDesktop + '\'' +
                ", issetVisibleRange='" + issetVisibleRange + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", authorizeStatus='" + authorizeStatus + '\'' +
                ", backGroundColor='" + backGroundColor + '\'' +
                '}';
    }
}