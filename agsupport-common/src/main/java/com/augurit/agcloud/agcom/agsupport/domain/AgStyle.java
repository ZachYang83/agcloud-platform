package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-07-21.
 */
public class AgStyle {
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("样式")
    private String style;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("点类型")
    private String pointType;
    @ApiModelProperty("是否系统")
    private String isSystem;
    @ApiModelProperty("是否正在使用，数值为所属图层数：有多少个图层配置了这个样式")
    private int isUsing; //是否正在使用，数值为所属图层数：有多少个图层配置了这个样式

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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    public int getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(int isUsing) {
        this.isUsing = isUsing;
    }
}
