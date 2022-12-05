package com.augurit.agcloud.agcom.agsupport.domain;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponent;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgPermission;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
public class AgMaterialsComponentCustom extends AgMaterialsComponent {
    //参数，授权保存
    @JsonIgnore
    private String auths;

    //授权数量
    private Integer authsNum = 0;

    //授权列表
    private List<AgPermission> authList;

    public String getAuths() {
        return auths;
    }

    public void setAuths(String auths) {
        this.auths = auths;
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
}
