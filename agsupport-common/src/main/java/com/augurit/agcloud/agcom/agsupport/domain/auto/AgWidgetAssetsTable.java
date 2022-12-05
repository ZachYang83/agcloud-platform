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
@ApiModel(value="AgWidgetAssetsTable实体对象")
public class AgWidgetAssetsTable {
    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value="项目应用id")
    private String appSoftId;

    @ApiModelProperty(value="表名（当前项目下的表名唯一）")
    private String tableName;

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
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