package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @Author: qinyg
 * @Date: 2020/09/30
 * @tips: 实体类
 */
@ApiModel(value="AgPermission实体对象")
public class AgPermission {
    @ApiModelProperty(value="id属性")
    private String id;

    @ApiModelProperty(value="来源id")
    private String sourceId;

    @ApiModelProperty(value="数据字典的code值")
    private String code;

    @ApiModelProperty(value="1用户；2构件；3房屋")
    private String type;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
}