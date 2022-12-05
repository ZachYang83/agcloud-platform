package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author:zrr
 * @Description:
 * @Date:created in :16:58 2018/7/13
 * @Modified By:
 */
public class AgUserLayer {
    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("用户id")
    private String userId;//用户id
    @ApiModelProperty("图层id")
    private String dirLayerId;//图层id
    @ApiModelProperty("addFlag")
    private String addFlag;
    @ApiModelProperty("可编辑 0:不可以，1：可以")
    private String editable;//可编辑 0:不可以，1：可以
    @ApiModelProperty("可查询 0:不可以，1：可以")
    private String queryable;//可查询 0:不可以，1：可以
    @ApiModelProperty("显示")
    private String isShow;//显示
    @ApiModelProperty("显示底图  0：不显示，1;显示")
    private String isBaseMap;//显示底图  0：不显示，1;显示
    @ApiModelProperty("范围")
    private String extent;
    @ApiModelProperty("查询图标")
    private String queryCon;


    //getter and setter methold
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String roleId) {
        this.userId = roleId;
    }

    public String getDirLayerId() {
        return dirLayerId;
    }

    public void setDirLayerId(String dirLayerId) {
        this.dirLayerId = dirLayerId;
    }

    public String getAddFlag() {
        return addFlag;
    }

    public void setAddFlag(String addFlag) {
        this.addFlag = addFlag;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }
    public String getQueryable() {
        return queryable;
    }

    public void setQueryable(String queryable) {
        this.queryable = queryable;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getIsBaseMap() {
        return isBaseMap;
    }

    public void setIsBaseMap(String isBaseMap) {
        this.isBaseMap = isBaseMap;
    }

    public String getExtent() {
        return extent;
    }

    public void setExtent(String extent) {
        this.extent = extent;
    }

    public String getQueryCon() {
        return queryCon;
    }

    public void setQueryCon(String queryCon) {
        this.queryCon = queryCon;
    }
}
