package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @Author: qinyg
 * @Date: 2020/09/30
 * @tips: 实体类
 */
@ApiModel(value="AgSysSetting实体对象")
public class AgSysSetting {
    @ApiModelProperty(value="id属性")
    private String id;

    @ApiModelProperty(value="name属性")
    private String name;

    @ApiModelProperty(value="type属性")
    private String type;

    @ApiModelProperty(value="path属性")
    private String path;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}