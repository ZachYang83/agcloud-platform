package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 *
 * @Author: Zihui Li
 * @Date: 2020/11/20
 * @tips: 实体类
 */
@ApiModel(value="AgBimCheckProject实体对象")
public class AgBimCheckProject {
    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value="审查项目名")
    private String name;

    @ApiModelProperty(value="创建时间")
    private Date createTime;

    @ApiModelProperty(value="修改时间")
    private Date modifyTime;

    @ApiModelProperty(value="备注")
    private String remark;

    @ApiModelProperty(value="存放json文件")
    private String jsonData;

    @ApiModelProperty(value="模型集合（BIM审查项目模型表）")
    private List<AgBimCheckProjectModel> projectModels;

    @ApiModelProperty(value="模型集合（服务内容表）")
    private List<AgServerContent> serverContents;

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

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData == null ? null : jsonData.trim();
    }

    public List<AgBimCheckProjectModel> getProjectModels() {
        return projectModels;
    }

    public void setProjectModels(List<AgBimCheckProjectModel> projectModels) {
        this.projectModels = projectModels;
    }


    public List<AgServerContent> getServerContents() {
        return serverContents;
    }

    public void setServerContents(List<AgServerContent> serverContents) {
        this.serverContents = serverContents;
    }
}