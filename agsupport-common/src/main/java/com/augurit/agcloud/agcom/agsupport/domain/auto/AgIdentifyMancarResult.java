package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 *
 * @Author: qinyg
 * @Date: 2020/12/28
 * @tips: 实体类
 */
@ApiModel(value="AgIdentifyMancarResult实体对象")
public class AgIdentifyMancarResult {
    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value="人车识别资源表主键id")
    private String sourceId;

    @ApiModelProperty(value="识别时间")
    private Date identifyTime;

    @ApiModelProperty(value="识别人数")
    private Integer numPeople;

    @ApiModelProperty(value="识别车数")
    private Integer numCar;

    @ApiModelProperty(value="创建时间")
    private Date createTime;

    @ApiModelProperty(value="修改时间")
    private Date modifyTime;

    @ApiModelProperty(value="备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId == null ? null : sourceId.trim();
    }

    public Date getIdentifyTime() {
        return identifyTime;
    }

    public void setIdentifyTime(Date identifyTime) {
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}