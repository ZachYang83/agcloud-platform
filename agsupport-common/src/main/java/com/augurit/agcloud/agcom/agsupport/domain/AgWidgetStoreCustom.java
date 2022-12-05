package com.augurit.agcloud.agcom.agsupport.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetStoreWithBLOBs;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel(value= "微件商店")
public class AgWidgetStoreCustom extends AgWidgetStoreWithBLOBs {

    /**
     * 把图片变成base 64字符串
     */
    private String viewImg;

    public String getViewImg() {
        return viewImg;
    }

    public void setViewImg(String viewImg) {
        this.viewImg = viewImg;
    }


}