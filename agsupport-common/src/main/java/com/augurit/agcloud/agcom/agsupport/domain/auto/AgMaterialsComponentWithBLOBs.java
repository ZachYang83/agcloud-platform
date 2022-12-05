package com.augurit.agcloud.agcom.agsupport.domain.auto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @Author: qinyg
 * @Date: 2020/10/20
 * @tips: 实体类
 */
@ApiModel(value="AgMaterialsComponentWithBLOBs实体对象")
public class AgMaterialsComponentWithBLOBs extends AgMaterialsComponent {
    @ApiModelProperty(value="glb属性")
    private byte[] glb;

    @ApiModelProperty(value="缩略图")
    private byte[] thumb;

    public byte[] getGlb() {
        return glb;
    }

    public void setGlb(byte[] glb) {
        this.glb = glb;
    }

    public byte[] getThumb() {
        return thumb;
    }

    public void setThumb(byte[] thumb) {
        this.thumb = thumb;
    }
}