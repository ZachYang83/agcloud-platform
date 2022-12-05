package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Augurit on 2017-04-21.
 */
public class AgRole {
    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("角色名称")
    private String name;
    @ApiModelProperty("flag")
    private String flag;
    @ApiModelProperty("角色图层id")
    private String roleLayerId;
    @ApiModelProperty("角色功能id")
    private String roleFuncId;
    @ApiModelProperty("角色菜单id")
    private String roleMenuId;
    @ApiModelProperty("用户角色id")
    private String userRoleId;

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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getRoleLayerId() {
        return roleLayerId;
    }

    public void setRoleLayerId(String roleLayerId) {
        this.roleLayerId = roleLayerId;
    }

    public String getRoleFuncId() {
        return roleFuncId;
    }

    public void setRoleFuncId(String roleFuncId) {
        this.roleFuncId = roleFuncId;
    }

    public String getRoleMenuId() {
        return roleMenuId;
    }

    public void setRoleMenuId(String roleMenuId) {
        this.roleMenuId = roleMenuId;
    }

    public String getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }
}
