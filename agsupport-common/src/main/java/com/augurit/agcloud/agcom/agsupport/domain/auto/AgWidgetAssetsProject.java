package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 *
 * @Author: Zihui Li
 * @Date: 2020/12/4
 * @tips: 实体类
 */
@ApiModel(value="AgWidgetAssetsProject实体对象")
public class AgWidgetAssetsProject {
    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value="项目编号")
    private String appSoftId;

    @ApiModelProperty(value="项目唯一标识")
    private String uniqueIdf;

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

    public String getAppSoftId() {
        return appSoftId;
    }

    public void setAppSoftId(String appSoftId) {
        this.appSoftId = appSoftId == null ? null : appSoftId.trim();
    }

    public String getUniqueIdf() {
        return uniqueIdf;
    }

    public void setUniqueIdf(String uniqueIdf) {
        this.uniqueIdf = uniqueIdf == null ? null : uniqueIdf.trim();
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