package com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @Author: qinyg
 * @Date: 2020/12/29 14:14
 * @tips: 识别结果统计返回值统一封装
 */
public class AgIdentifyMancarResultStatisticsResult {
    @ApiModelProperty(value="识别结果最小时间")
    private Date startTime;

    @ApiModelProperty(value="识别结果最大时间")
    private Date endTime;

    @ApiModelProperty(value="识别人数")
    private List<AgIdentifyMancarResultStatisticsDomain> list;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<AgIdentifyMancarResultStatisticsDomain> getList() {
        return list;
    }

    public void setList(List<AgIdentifyMancarResultStatisticsDomain> list) {
        this.list = list;
    }
}
