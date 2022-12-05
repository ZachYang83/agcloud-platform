package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by yzq on 2019-10-20.
 */
public class AgMarker implements Serializable {
    @ApiModelProperty(value = "编号")
    private String id;            //编号
    @ApiModelProperty(value = "用户名称")
    private String userName;        //用户名称
    @ApiModelProperty(value = "用户Id")
    private String userId;        //用户名称
    @ApiModelProperty(value = "创建时间")
    private String creatTime;        //创建时间
    @ApiModelProperty(value = "图片位置")
    private String imagePath;        //图片位置
    @ApiModelProperty(value = "图片名称")
    private String imageName;        //图片名称
    @ApiModelProperty(value = "备注")
    private String remakes;        //备注
    @ApiModelProperty(value = "图片")
    private String imageBase;
    @ApiModelProperty(value = "范围")
    private String extent;

    @ApiModelProperty(value = "用户是否阅读字段，0：未阅读 1：已阅读")
    private String viewState;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getRemakes() {
        return remakes;
    }

    public void setRemakes(String remakes) {
        this.remakes = remakes;
    }

    public String getImageBase() {
        return imageBase;
    }

    public void setImageBase(String imageBase) {
        this.imageBase = imageBase;
    }

    public String getExtent() {
        return extent;
    }

    public void setExtent(String extent) {
        this.extent = extent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getViewState() {
        return viewState;
    }

    public void setViewState(String viewState) {
        this.viewState = viewState;
    }
}
