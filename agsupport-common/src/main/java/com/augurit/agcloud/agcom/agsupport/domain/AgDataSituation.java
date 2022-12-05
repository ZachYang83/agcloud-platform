package com.augurit.agcloud.agcom.agsupport.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 数据统计情况bean
 * @date 2019-08-26 9:53
 */
public class AgDataSituation implements Serializable{
    private static final long serialVersionUID = 2890121004118825795L;
    private String id;
    private Integer dataResourceNum;
    private Long dataResourceSize;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date statisticalTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDataResourceNum() {
        return dataResourceNum;
    }

    public void setDataResourceNum(Integer dataResourceNum) {
        this.dataResourceNum = dataResourceNum;
    }

    public Long getDataResourceSize() {
        return dataResourceSize;
    }

    public void setDataResourceSize(Long dataResourceSize) {
        this.dataResourceSize = dataResourceSize;
    }

    public Date getStatisticalTime() {
        return statisticalTime;
    }

    public void setStatisticalTime(Date statisticalTime) {
        this.statisticalTime = statisticalTime;
    }

    @Override
    public String toString() {
        return "AgDataSituation{" +
                "id='" + id + '\'' +
                ", dataResourceNum=" + dataResourceNum +
                ", dataResourceSize=" + dataResourceSize +
                ", statisticalTime=" + statisticalTime +
                '}';
    }
}
