package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by fanghh on 2019/12/5.
 */
@ApiModel(value = "BIM关联文件")
public class AgBimRelationFile {

    @ApiModelProperty(value = "关联主键")
    private String id;
    @ApiModelProperty(value = "bim模型ID")
    private String bimId;
    @ApiModelProperty(value = "文件ID")
    private String fileId;
    private AgBimFile bimFile;
    private AgFileStore fileStore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBimId() {
        return bimId;
    }

    public void setBimId(String bimId) {
        this.bimId = bimId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public AgBimFile getBimFile() {
        return bimFile;
    }

    public void setBimFile(AgBimFile bimFile) {
        this.bimFile = bimFile;
    }

    public AgFileStore getFileStore() {
        return fileStore;
    }

    public void setFileStore(AgFileStore fileStore) {
        this.fileStore = fileStore;
    }
}
