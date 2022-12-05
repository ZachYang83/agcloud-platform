package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 *
 * @Author: qinyg
 * @Date: 2020/09/30
 * @tips: 实体类
 */
@ApiModel(value="AgCategory实体对象")
public class AgCategory {
    @ApiModelProperty(value="（1初始化数据需要id=root）（2再初始化一条数据p_id=root，id=children）（3再初始化一条数据p_id=root，id=childrenMaterials）")
    private String id;

    @ApiModelProperty(value="name属性")
    private String name;

    @ApiModelProperty(value="level属性")
    private String level;

    @ApiModelProperty(value="isEnable属性")
    private String isEnable;

    @ApiModelProperty(value="pId属性")
    private String pId;

    @ApiModelProperty(value="createTime属性")
    private Date createTime;

    @ApiModelProperty(value="modifyTime属性")
    private Date modifyTime;

    @ApiModelProperty(value="remark属性")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable == null ? null : isEnable.trim();
    }

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId == null ? null : pId.trim();
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