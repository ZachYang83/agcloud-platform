package com.augurit.agcloud.agcom.agsupport.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: libc
 * @Description: BIM审查项目模型自定义方法mapper
 * @Date: 2020/11/27 10:29
 * @Version: 1.0
 */
@Mapper
public interface AgBimCheckProjectModelCustomMapper {

    /**
     *
     * @Author: libc
     * @Date: 2020/11/20 16:05
     * @tips: 创建excel 模型信息表
     * @param createTableSql sql语句
     */
    void createExcelTable(@Param("createTableSql")String createTableSql);

    /**
     *
     * @Author: libc
     * @Date: 2020/11/24 9:42
     * @tips: 根据模型新表名删除表
     * @param infoRelTableName 模型信息关联表名
     */
    void deleteModelInfoTable(@Param("infoRelTableName") String infoRelTableName);

    /**
     *
     * @Author: libc
     * @Date: 2020/11/24 11:25
     * @tips: 批量保存模型信息 V2 版本 （动态拼接要插入数据的列字段）
     * @param tableName 表名
     * @param allColumns 所有数据对应的列字段
     * @param dataList 数据map集合
     */
    void batchInsertModelInfoByExcel(@Param("tableName")String tableName, @Param("allColumns")List<List<String>> allColumns, @Param("dataList") List<Map<String, Object>> dataList);
}
