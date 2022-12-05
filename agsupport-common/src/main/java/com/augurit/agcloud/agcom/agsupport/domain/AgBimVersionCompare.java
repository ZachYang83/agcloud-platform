package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @ClassName AgBimCompare
 * @Author Administrator
 * @Version 1.0
 **/
@ApiModel(value = "BIM模型比对")
public class AgBimVersionCompare {
    @ApiModelProperty(value = "bim模型比对ID")
    private String id;
    @ApiModelProperty(value = "bim模型ID")
    private String bimFileId;
    @ApiModelProperty(value = "第一个进行比对的bim模型版本ID（共两个）")
    private String bimVersionId1;
    @ApiModelProperty(value = "第二个进行比对的bim模型版本ID（共两个）")
    private String bimVersionId2;
    @ApiModelProperty(value = "比对结果json文件路径")
    private String resultFilePath;
    @ApiModelProperty(value = "比对记录创建时间")
    private Date createTime;
    @ApiModelProperty(value = "比对记录更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "比对结果内容")
    private String resultContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBimFileId() {
        return bimFileId;
    }

    public void setBimFileId(String bimFileId) {
        this.bimFileId = bimFileId;
    }

    public String getBimVersionId1() {
        return bimVersionId1;
    }

    public void setBimVersionId1(String bimVersionId1) {
        this.bimVersionId1 = bimVersionId1;
    }

    public String getBimVersionId2() {
        return bimVersionId2;
    }

    public void setBimVersionId2(String bimVersionId2) {
        this.bimVersionId2 = bimVersionId2;
    }

    public String getResultFilePath() {
        return resultFilePath;
    }

    public void setResultFilePath(String resultFilePath) {
        this.resultFilePath = resultFilePath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getResultContent() {
        return resultContent;
    }

    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }
}
