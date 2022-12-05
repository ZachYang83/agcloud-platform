package com.augurit.agcloud.agcom.agsupport.sc.externalService.service;


import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.sc.externalService.service.impl.DateChangeRuleImpl;

public abstract class AbstractChangeRule {
    private String fieldName;
    private int fieldIndex;
    private String format;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public static AbstractChangeRule createInstance(JSONObject jsonObject){
        AbstractChangeRule abstractChangeRule = null;
        String fieldName = jsonObject.getString("fieldName");
        String type = jsonObject.getString("type");
        String format = jsonObject.getString("format");
        if("date".equals(type)){
            abstractChangeRule = new DateChangeRuleImpl();
            abstractChangeRule.setFieldName(fieldName);
            abstractChangeRule.setFormat(format);
        }
        return abstractChangeRule;
    }

    /*
     *@content 内容
     *@fieldContent 规则
     * */
    public abstract String handle(String content);
}
