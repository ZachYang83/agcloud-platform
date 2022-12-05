package com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.domain;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgIdentifyMancarResult;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;


/**
 * @Author: qinyg
 * @Date: 2020/12/29 10:38
 * @tips: 统一封装查询结果返回值
 */
public class AgIdentifyMancarResultResult {
    @ApiModelProperty(value="识别结果最小时间")
    private Date startTime;
    @ApiModelProperty(value="识别结果最大时间")
    private Date endTime;
    @ApiModelProperty(value="识别结果列表")
    private List<AgIdentifyMancarResult> list;

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

    public List<AgIdentifyMancarResult> getList() {
        return list;
    }

    public void setList(List<AgIdentifyMancarResult> list) {
        this.list = list;
    }
}
