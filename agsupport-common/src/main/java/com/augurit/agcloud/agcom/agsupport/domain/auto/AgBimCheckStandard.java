package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 *
 * @Author: Zihui Li
 * @Date: 2020/12/24
 * @tips: 实体类
 */
@ApiModel(value="AgBimCheckStandard实体对象")
public class AgBimCheckStandard {
    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value="规范审查条文")
    private String clause;

    @ApiModelProperty(value="条文序号")
    private String serial;

    @ApiModelProperty(value="是否强条")
    private String enforce;

    @ApiModelProperty(value="条文内容拆解")
    private String clauseContent;

    @ApiModelProperty(value="关联模型信息")
    private String associateModel;

    @ApiModelProperty(value="条文内容所属类型")
    private String clauseCategory;

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

    public String getClause() {
        return clause;
    }

    public void setClause(String clause) {
        this.clause = clause == null ? null : clause.trim();
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial == null ? null : serial.trim();
    }

    public String getEnforce() {
        return enforce;
    }

    public void setEnforce(String enforce) {
        this.enforce = enforce == null ? null : enforce.trim();
    }

    public String getClauseContent() {
        return clauseContent;
    }

    public void setClauseContent(String clauseContent) {
        this.clauseContent = clauseContent == null ? null : clauseContent.trim();
    }

    public String getAssociateModel() {
        return associateModel;
    }

    public void setAssociateModel(String associateModel) {
        this.associateModel = associateModel == null ? null : associateModel.trim();
    }

    public String getClauseCategory() {
        return clauseCategory;
    }

    public void setClauseCategory(String clauseCategory) {
        this.clauseCategory = clauseCategory == null ? null : clauseCategory.trim();
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