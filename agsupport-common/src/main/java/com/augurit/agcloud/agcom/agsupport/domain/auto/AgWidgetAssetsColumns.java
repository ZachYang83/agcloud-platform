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
@ApiModel(value="AgWidgetAssetsColumns实体对象")
public class AgWidgetAssetsColumns {
    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value="专题表id")
    private String thematicTableId;

    @ApiModelProperty(value="字段名称")
    private String columnName;

    @ApiModelProperty(value="字段类型")
    private String columnType;

    @ApiModelProperty(value="创建时间")
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getThematicTableId() {
        return thematicTableId;
    }

    public void setThematicTableId(String thematicTableId) {
        this.thematicTableId = thematicTableId == null ? null : thematicTableId.trim();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType == null ? null : columnType.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}