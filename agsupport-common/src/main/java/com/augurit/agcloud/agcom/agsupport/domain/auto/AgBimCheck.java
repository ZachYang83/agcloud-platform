package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 *
 * @Author: qinyg
 * @Date: 2020/11/03
 * @tips: 实体类
 */
@ApiModel(value="AgBimCheck实体对象")
public class AgBimCheck {
    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value="来源id")
    private String sourceId;

    @ApiModelProperty(value="规范分类")
    private String type;

    @ApiModelProperty(value="数据状态")
    private String status;

    @ApiModelProperty(value="条文描述")
    private String basis;

    @ApiModelProperty(value="审查结果描述")
    private String result;

    @ApiModelProperty(value="条文编号")
    private String name;

    @ApiModelProperty(value="条文所属方法")
    private String method;

    @ApiModelProperty(value="规范类型")
    private String classificationType;

    @ApiModelProperty(value="问题构件列表")
    private String elements;

    @ApiModelProperty(value="创建时间")
    private Date createTime;

    @ApiModelProperty(value="修改时间")
    private Date modifyTime;

    @ApiModelProperty(value="备注")
    private String remark;

    @ApiModelProperty(value="条文id")
    private String articleId;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getBasis() {
        return basis;
    }

    public void setBasis(String basis) {
        this.basis = basis == null ? null : basis.trim();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    public String getClassificationType() {
        return classificationType;
    }

    public void setClassificationType(String classificationType) {
        this.classificationType = classificationType == null ? null : classificationType.trim();
    }

    public String getElements() {
        return elements;
    }

    public void setElements(String elements) {
        this.elements = elements == null ? null : elements.trim();
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

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId == null ? null : articleId.trim();
    }
}