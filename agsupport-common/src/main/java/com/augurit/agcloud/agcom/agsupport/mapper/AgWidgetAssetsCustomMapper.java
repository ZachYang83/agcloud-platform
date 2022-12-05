package com.augurit.agcloud.agcom.agsupport.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Author: qinyg
 * @Date: 2020/11/17
 * @tips: mapper接口
 */
@Mapper
public interface AgWidgetAssetsCustomMapper {

    List<Object> executeDefineSql(String sql);
/**
 * @Author Zihui Li
 * @Date: 2020/11/20 17:28
 * @tips: 
 * @Param tableFullName, 表全名
 * @Param orderByColumn, 按照哪个属性进行排序
 * @Param orderDescString, 排序规则，0: 升序，1: 降序
 * @return java.util.List<java.lang.Object>
 */
    List<Object> selectAllFromArbitraryTable(@Param("tableFullName") String tableFullName,
                                             @Param("orderByColumn") String orderByColumn,
                                             @Param("orderDescString") String orderDescString);

    /**
     * @Author Zihui Li
     * @Date: 2020/11/20 17:28
     * @tips: 
     * @Param tableFullName, 表全名
     * @Param selectColumns, 需要查询的属性，用逗号分隔
     * @Param searchCondition, 查询条件
     * @Param orderByColumn, 按照哪个属性进行排序
     * @Param orderDescString,  排序规则，0: 升序，1: 降序
     * @return java.util.List<java.lang.Object>
     */
    List<Object> selectRecordsFromArbitraryTable(@Param("tableFullName") String tableFullName,
                                                 @Param("selectColumns") String selectColumns,
                                                 @Param("searchCondition") String searchCondition,
                                                 @Param("orderByColumn") String orderByColumn,
                                                 @Param("orderDescString") String orderDescString);
    
    /**
     * @Author Zihui Li
     * @Date: 2020/11/23 10:53
     * @tips: 查询有tableFullName字段的表的数量，用于检测该表是否存在
     * @Param tableFullName
     * @return int
     */
    int tableNum(@Param("tableFullName") String tableFullName);

    /**
     * @Author Zihui Li
     * @Date: 2020/11/24 16:24
     * @tips:
     * @Param tableFullName 表全名
     * @Param id 查询字段对应的主键id
     * @Param fieldName 查询文件在数据库中的字段名
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String,Object> getTargetFile(@Param("tableFullName") String tableFullName,
                                             @Param("id") String id,
                                             @Param("fieldName") String fieldName);

    /**
     * @Author Zihui Li
     * @Date: 2020/11/27 15:58
     * @tips: 根据表名查询表内所有存在的字段名
     * @Param tableFullName
     * @return java.util.List<java.lang.String>
     */
    List<String> getColumnsFromArbitraryTable(@Param("tableFullName") String tableFullName);

    /**
     * @Author Zihui Li
     * @Date: 2020/11/27 14:09
     * @tips: 检查字段名是否在该表中
     * @Param tableFullName 表全名
     * @Param columnName 字段名
     * @return int 返回1则表中有该字段，0则没有
     */
    int columnExistCheck(@Param("tableFullName") String tableFullName, @Param("columnName") String columnName);

}