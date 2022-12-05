package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by Augurit on 2017-04-27.
 */
public class AgUser implements Serializable{
    @ApiModelProperty("编号")
    private String id;                //编号
    @ApiModelProperty("登录名称")
    private String loginName;        //登录名称
    @ApiModelProperty("登录密码")
    private String password;        //登录密码
    @ApiModelProperty("用户名称")
    private String userName;        //用户名称
    @ApiModelProperty("机构id")
    private String orgId;            //机构Id
    @ApiModelProperty("机构名称")
    private String orgName;            //机构名称
    @ApiModelProperty("是否有效")
    private String isActive;        //是否有效
    @ApiModelProperty("机构编码")
    private String orgCode;
    @ApiModelProperty("用户角色id")
    private String userRoleId;
    @ApiModelProperty("机构用户id")
    private String orgUserId;
    @ApiModelProperty("机构路径")
    private String orgPath;
    @ApiModelProperty("角色id")
    private String roleId;
    @ApiModelProperty("电话")
    private String tel;
    @ApiModelProperty("邮件")
    private String email;
    @ApiModelProperty("qq")
    private String qq;
    @ApiModelProperty("办公电话")
    private String officeTel;
    @ApiModelProperty("用户图层id")
    private String userLayerId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getOfficeTel() {
        return officeTel;
    }

    public void setOfficetel(String officeTel) {
        this.officeTel = officeTel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getOrgUserId() {
        return orgUserId;
    }

    public void setOrgUserId(String orgUserId) {
        this.orgUserId = orgUserId;
    }

    public String getOrgPath() {
        return orgPath;
    }

    public void setOrgPath(String orgPath) {
        this.orgPath = orgPath;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUserLayerId() {
        return userLayerId;
    }

    public void setUserLayerId(String userLayerId) {
        this.userLayerId = userLayerId;
    }
}
