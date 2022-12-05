package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 *
 * @Author: libc
 * @Date: 2020/11/20
 * @tips: 实体类
 */
@ApiModel(value="AgBimCheckProjectModel实体对象")
public class AgBimCheckProjectModel {
    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value="名称")
    private String name;

    @ApiModelProperty(value="3dtiles模型文件存储路径")
    private String path;

    @ApiModelProperty(value="bim审查项目对应id")
    private String agBimCheckProjectId;

    @ApiModelProperty(value="备注")
    private String remark;

    @ApiModelProperty(value="创建时间")
    private Date createTime;

    @ApiModelProperty(value="修改时间")
    private Date modifyTime;

    @ApiModelProperty(value="模型信息关联的表名称")
    private String infoRelTableName;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getAgBimCheckProjectId() {
        return agBimCheckProjectId;
    }

    public void setAgBimCheckProjectId(String agBimCheckProjectId) {
        this.agBimCheckProjectId = agBimCheckProjectId == null ? null : agBimCheckProjectId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public String getInfoRelTableName() {
        return infoRelTableName;
    }

    public void setInfoRelTableName(String infoRelTableName) {
        this.infoRelTableName = infoRelTableName == null ? null : infoRelTableName.trim();
    }
}