package com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author lizih
 * @Date 2020/11/20
 * @Version 1.0
 */
public interface IAgWidgetAssetsTableService {

    /**
     * @Author Zihui Li
     * @Date: 2020/11/23 9:40
     * @tips:
     * @Param softCode, 项目编码
     * @Param tableName, 项目内表名（非全名）
     * @Param orderByColumn, 按照哪个属性排序
     * @Param orderDesc, 0：升序 1: 降序
     * @Param page,
     * @return com.github.pagehelper.PageInfo<java.lang.Object>
     */
    PageInfo<Object> findAllPage(String softCode, String tableName, String orderByColumn, String orderDesc, Page page);

    /**
     * @Author Zihui Li
     * @Date: 2020/11/23 9:41
     * @tips:
     * @Param softCode, 项目编码
     * @Param tableName, 项目内表名（非全名）
     * @Param orderByColumn, 按照哪个属性排序
     * @Param orderDesc,  0：升序 1: 降序
     * @return java.util.List<java.lang.Object>
     */
    List<Object> findAll(String softCode, String tableName, String orderByColumn, String orderDesc);
    
    /**
     * @Author Zihui Li
     * @Date: 2020/11/23 9:47
     * @tips: 
     * @Param softCode, 项目编码
     * @Param tableName, 项目内表名（非全名）
     * @Param selectColumns, 需要查询的属性，用逗号隔开
     * @Param searchCondition, 查询条件， where 后面的sql语句
     * @Param orderByColumn, 按照哪个属性排序
     * @Param orderDesc, 0：升序 1: 降序
     * @Param page，
     * @return com.github.pagehelper.PageInfo<java.lang.Object>
     */
    PageInfo<Object> findConditionPage(String softCode, String tableName, String selectColumns,
                                   String searchCondition, String orderByColumn, String orderDesc, Page page);
    
    /**
     * @Author Zihui Li
     * @Date: 2020/11/23 9:47
     * @tips: 
     * @Param softCode, 项目编码
     * @Param tableName, 项目内表名（非全名）
     * @Param selectColumns, 需要查询的属性，用逗号隔开
     * @Param searchCondition, 查询条件， where 后面的sql语句
     * @Param orderByColumn,  按照哪个属性排序
     * @Param orderDesc,  0：升序 1: 降序
     * @return java.util.List<java.lang.Object>
     */
    List<Object> findCondition(String softCode, String tableName, String selectColumns,
                                   String searchCondition, String orderByColumn, String orderDesc);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/23 9:28
     * @tips: 添加专题表数据，参数从request中获取
     * @param softCode 项目编码
     * @param tableName 专题表表名
     * @param request 所有的请求数据
     * @return
     */
    void add(String softCode, String tableName, HttpServletRequest request);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/23 9:30
     * @tips: 修改专题表数据，参数从request中获取
     * @param softCode 项目编码
     * @param tableName 专题表表名
     * @param request 所有的请求数据
     * @return
     */
    void update(String softCode, String tableName, HttpServletRequest request);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/23 9:31
     * @tips: 删除专题表数据
     * @param softCode 项目编码
     * @param tableName 专题表表名
     * @param ids 专题表id
     * @return
     */
    void delete(String softCode, String tableName, String ids);

    /**
     * @Author Zihui Li
     * @Date: 2020/11/26 10:18
     * @tips: 获取bytea类型文件
     * @Param softCode 项目编码
     * @Param tableName 专题表表名
     * @Param id 专题表id
     * @Param fieldName 所取文件在数据库对应的字段名
     * @return byte[]
     */
    byte[] getTargetFile(String softCode, String tableName, String id, String fieldName);


}
