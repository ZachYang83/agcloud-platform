package com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.domain;

import io.swagger.annotations.ApiModelProperty;


/**
 * @Author: qinyg
 * @Date: 2020/12/28 14:15
 * @tips:
 */
public class AgIdentifyMancarResultParam {

    @ApiModelProperty(value="人车识别资源表主键id")
    private String sourceId;

    @ApiModelProperty(value="识别时间（格式：yyyy-MM-dd HH:mm:ss）")
    private String baseTime;

    @ApiModelProperty(value="识别间隔（单位：秒）")
    private Integer identifyTime;

    @ApiModelProperty(value="识别人数")
    private Integer numPeople;

    @ApiModelProperty(value="识别车数")
    private Integer numCar;

    @ApiModelProperty(value="备注")
    private String remark;

    public String getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getIdentifyTime() {
        return identifyTime;
    }

    public void setIdentifyTime(Integer identifyTime) {
        this.identifyTime = identifyTime;
    }

    public Integer getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(Integer numPeople) {
        this.numPeople = numPeople;
    }

    public Integer getNumCar() {
        return numCar;
    }

    public void setNumCar(Integer numCar) {
        this.numCar = numCar;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "AgIdentifyMancarResultParam{" +
                "sourceId='" + sourceId + '\'' +
                ", identifyTime='" + identifyTime + '\'' +
                ", numPeople=" + numPeople +
                ", numCar=" + numCar +
                ", remark='" + remark + '\'' +
                '}';
    }
}
