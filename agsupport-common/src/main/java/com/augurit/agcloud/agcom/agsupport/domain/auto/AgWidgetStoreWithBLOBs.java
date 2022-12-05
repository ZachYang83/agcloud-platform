package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @Author: qinyg
 * @Date: 2020/10/21
 * @tips: 实体类
 */
@ApiModel(value="AgWidgetStoreWithBLOBs实体对象")
public class AgWidgetStoreWithBLOBs extends AgWidgetStore {
    @ApiModelProperty(value="缩略图")
    private byte[] thumb;

    @ApiModelProperty(value="微件文件")
    private byte[] widgetFile;

    @ApiModelProperty(value = "md文件")
    private byte[] mdFile;

    public byte[] getThumb() {
        return thumb;
    }

    public void setThumb(byte[] thumb) {
        this.thumb = thumb;
    }

    public byte[] getWidgetFile() {
        return widgetFile;
    }

    public void setWidgetFile(byte[] widgetFile) {
        this.widgetFile = widgetFile;
    }

    public byte[] getMdFile() { return mdFile; }

    public void setMdFile(byte[] mdFile) {
        this.mdFile = mdFile;
    }
}