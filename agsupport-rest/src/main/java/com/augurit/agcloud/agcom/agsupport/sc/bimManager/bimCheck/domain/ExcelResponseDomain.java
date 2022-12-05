package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.domain;

import java.util.List;

/**
 * @Author: qinyg
 * @Date: 2020/9/21
 * @tips: 解析excel的返回值
 */
public class ExcelResponseDomain {

    private String key;
    private List<String> value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }
}
