package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * @Author:
 * @Description:
 * @Date:created in :14:41 2019/3/19
 * @Modified By:
 */
public class AgDefaultValue {
    private String id;//id
    private String key;//键
    private String defaultValue;//值
    private String remark;//备注说明

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

}
