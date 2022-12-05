package com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Author: qinyg
 * @Date: 2020/12/30 9:17
 * @tips:
 */
public class AgIdentifyMancarResultStatisticsDomain {
    @ApiModelProperty(value="识别时间-开始")
    private Date identifyTimeStart;
    @ApiModelProperty(value="识别时间-开始")
    private Date identifyTimeEnd;
    @ApiModelProperty(value="总人数")
    private Integer totalNumPeople;
    @ApiModelProperty(value="总车数")
    private Integer totalNumCar;


    public Date getIdentifyTimeStart() {
        return identifyTimeStart;
    }

    public void setIdentifyTimeStart(Date identifyTimeStart) {
        this.identifyTimeStart = identifyTimeStart;
    }

    public Date getIdentifyTimeEnd() {
        return identifyTimeEnd;
    }

    public void setIdentifyTimeEnd(Date identifyTimeEnd) {
        this.identifyTimeEnd = identifyTimeEnd;
    }

    public Integer getTotalNumPeople() {
        return totalNumPeople;
    }

    public void setTotalNumPeople(Integer totalNumPeople) {
        this.totalNumPeople = totalNumPeople;
    }

    public Integer getTotalNumCar() {
        return totalNumCar;
    }

    public void setTotalNumCar(Integer totalNumCar) {
        this.totalNumCar = totalNumCar;
    }
}
