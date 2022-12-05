package com.augurit.agcloud.agcom.agsupport.domain;



import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: qinyg
 * @Date: 2020/10/25
 * @tips: 需要序列化
 */
public class AgHouseMaterialsCustom implements Serializable {
    @ApiModelProperty(value="主键id")
    private String id;

    @ApiModelProperty(value="名称")
    private String name;

    @ApiModelProperty(value="storeFullPath属性")
    private String storeFullPath;

    @ApiModelProperty(value="点的总数量")
    private Integer total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreFullPath() {
        return storeFullPath;
    }

    public void setStoreFullPath(String storeFullPath) {
        this.storeFullPath = storeFullPath;
    }
}
