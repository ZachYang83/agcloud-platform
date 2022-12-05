package com.augurit.agcloud.agcom.agsupport.sc.bimManager.util;


import com.augurit.agcloud.agcom.agsupport.domain.auto.AgPermission;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
public class AgUserCustom {

    private String userId;

    private String userName;

    private String userType;

    private String organization;

    private String remark;

    private String userMobile;

    private Long createTime;

    //授权数量
    private Integer authsNum = 0;

    //授权列表
    private List<AgPermission> authList;

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getAuthsNum() {
        return authsNum;
    }

    public void setAuthsNum(Integer authsNum) {
        this.authsNum = authsNum;
    }

    public List<AgPermission> getAuthList() {
        return authList;
    }

    public void setAuthList(List<AgPermission> authList) {
        this.authList = authList;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }


}
