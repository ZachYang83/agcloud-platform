package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.domain;

/**
 * @Author: libc
 * @Description: 模型excel导入表结构字段枚举类
 * @Date: 2020/11/20 11:43
 * @Version: 1.0
 */
public enum ModelTableFieldEnum {
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_ID("id"," varchar(100)"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_OBJECTID("objectid", " varchar(20)"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_NAME("name", " varchar(200)"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_VERSION("version", " int8"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_INFOTYPE("infotype", " varchar(20)"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_PROFESSION("profession", " varchar(200)"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_LEVEL("level", " varchar(200)"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_CATAGORY("catagory", " varchar(200)"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_FAMILYNAME("familyname", " varchar(200)"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_FAMILYTYPE("familytype", " varchar(200)"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_MATERIALID("materialid", " varchar(100)"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_ELEMENTATTRIBUTES("elementattributes", " text"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_CATEGORYPATH("categorypath", " varchar(500)"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_HOST("host", " varchar(200)"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_GEOMETRY("geometry", " text"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_TOPOLOGYELEMENTS("topologyelements", " text"),
    BIM_CHECK_MODEL_INFO_TABLE_FIELD_BOUNDINGBOX("boundingbox", " text"),
    ;


    // 字段名称
    private String fieldName;
    // 字段类型
    private String fieldType;

    private ModelTableFieldEnum(String fieldName, String fieldType){
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }
}
