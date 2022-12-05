package com.augurit.agcloud.agcom.agsupport.sc.bimfile.enums;

/**
 * Created by fanghh on 2019/12/25.
 */
public enum BimPublish {

    NO_PUBLISH("0","未发布"),PUBLISHING("1","发布中"),PUBLISHED("2","已发布"),PUBLISH_ERROR("3","停止");

    private String value;
    private String label;
    BimPublish(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
