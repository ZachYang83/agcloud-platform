package com.augurit.agcloud.agcom.agsupport.sc.datacatalog.service;

import com.augurit.agcloud.agcom.agsupport.domain.MetaDataDB;
import com.augurit.agcloud.agcom.agsupport.domain.MetaDataField;
import com.augurit.agcloud.agcom.agsupport.domain.MetaDataTable;
import com.augurit.agcloud.agcom.agsupport.sc.datacatalog.util.TreeNode;
import com.augurit.agcloud.framework.ui.result.ResultForm;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/2.
 */
public interface DataCatalogService {
    //构建 树
    List<TreeNode> getMetaDataDBTableTree() throws Exception;

    //分页 获取记录
    String listFromTable(String tablename, int page, int rows, String con) throws Exception;

    List<Map> listMetaTableField_Def(String tableID) throws Exception;

    void insertMetaDataDB(MetaDataDB db) throws Exception;

    /**
     * 更新数据库
     *
     * @param db
     */
    void updateMetaDataDB(MetaDataDB db) throws Exception;

    /**
     * 保存表
     *
     * @param table
     */
    int insertMetaDataTable(MetaDataTable table) throws Exception;

    /**
     * 更新表
     *
     * @param table
     */
    void updateMetaDataTable(MetaDataTable table) throws Exception;

    /**
     * 更新字段
     *
     * @param field
     */
    void updateMetaDataField(MetaDataField field) throws Exception;

    /**
     * 保存字段
     *
     * @param field
     */
    void insertMetaDataField(MetaDataField field) throws Exception;

    /**
     * 删除字段
     *
     * @param id
     * @throws Exception
     */
    void delMetaDataField(String id) throws Exception;

    /**
     * 删除表
     *
     * @param id
     */
    void delMetaDataTable(String id) throws Exception;

    /**
     * 删除数据库
     *
     * @param id
     */
    void delMetaDataDB(String id) throws Exception;

    /**
     * 删除数据库与表关联
     *
     * @param dbId
     */
    void delRelateData(String dbId) throws Exception;

    /**
     * 删除同步字段
     *
     * @param tableId
     */
    void delFieldData(String tableId) throws Exception;

    /**
     * 根据表名查询
     *
     * @param id
     * @return
     */
    List<TreeNode> getMetaDataDBTableTreeByName(String id) throws Exception;

    /**
     * 根据条件查询
     *
     * @param tablename
     * @param page
     * @param rows
     * @param con
     * @return
     */
    String listFromTableByCondition(String tablename, int page, int rows, String con) throws Exception;

    /**
     * 根据表名获取图层配置信息
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    Object getMetaDataByTableName(String tableName) throws Exception;

    /**
     * 查询所有字段
     *
     * @param tableID
     * @return
     */
    List<Map> listMetaTableField_DefAll(String tableID) throws Exception;

    /**
     * 仅数据库
     *
     * @return
     */
    List<TreeNode> getMetaDataOnlyDBTableTree() throws Exception;

    /**
     * 获取用户角色信息
     *
     * @param lii
     * @return
     * @throws Exception
     */
    List<Map> getRoleInfo(List lii) throws Exception;

    /**
     * 获取编辑权限的表
     *
     * @param roleId
     * @return
     */
    List<Map> getTableByEditRole(String roleId) throws Exception;

    /**
     * 判断是否存在记录
     *
     * @param li1
     * @return
     * @throws Exception
     */
    List<Map> getRecords(List li1) throws Exception;

    /**
     * 获取审核权限的表
     *
     * @param roleId
     * @return
     */
    List<Map> getTableByCheckRole(String roleId) throws Exception;

    /**
     * 获取待审核的记录
     *
     * @param li1
     * @return
     */
    List<Map> getCheckRecord(List li1) throws Exception;

    /**
     * 获取表
     *
     * @param roleId
     * @return
     * @throws Exception
     */
    List<Map> getTableByUser(String roleId) throws Exception;

    /**
     * 获取表的字段
     *
     * @param li
     * @return
     */
    List<Map> getFieldByTable(List li) throws Exception;

    /**
     * 获取表信息
     *
     * @param li
     * @return
     */
    List<Map> getTableInfoByName(List li) throws Exception;

    /**
     * 获取地区
     *
     * @param li
     * @return
     */
    List<Map> getDisById(String roleId, List li) throws Exception;

    /**
     * 获取所有表
     *
     * @return
     */
    List<Map> getAllTable() throws Exception;

    /**
     * 获取DB信息
     *
     * @param li
     * @return
     */
    List<Map> getDBInfoById(List li) throws Exception;

    /**
     * 获取DB信息
     *
     * @param li
     * @return
     * @throws Exception
     */
    List<Map> getDBByName(List li) throws Exception;

    /**
     * 获取CName
     *
     * @param lii
     * @return
     * @throws Exception
     */
    List<Map> getCNameById(List lii) throws Exception;

    /**
     * 获取字段信息
     *
     * @param lii
     * @return
     */
    List<Map> getFieldInfo(List lii) throws Exception;

    /**
     * 获取组织信息
     *
     * @param li
     * @return
     */
    List<Map> getOrgInfoByUser(List li) throws Exception;

    /**
     * 获取模板信息
     *
     * @param li
     * @return
     */
    List<Map> getTempInfo(List li) throws Exception;

    /**
     * 获取表信息
     *
     * @param li
     * @return
     */
    List<Map> getTableInfo(List li) throws Exception;

    /**
     * 获取角色信息
     *
     * @param li
     * @return
     */
    List<Map> getRoleInfoByUser(List li) throws Exception;

    /**
     * 获取统计信息
     *
     * @return
     */
    List<Map> findDataRecords() throws Exception;

    /**
     * 获取用户姓名
     *
     * @param li
     * @return
     */
    List<Map> getUserName(List li) throws Exception;

    /**
     * 获取表单数据
     *
     * @param li
     * @return
     */
    List<Map> getTableData(List li) throws Exception;

    /**
     * 根据表名查找
     *
     * @param li
     * @return
     */
    List<Map> getTableByName(List li) throws Exception;

    /**
     * 获取字段中文
     *
     * @param lii
     * @return
     */
    List<Map> getFieldCname(List lii) throws Exception;

    /**
     * 查询修改值
     *
     * @param li
     * @return
     * @throws Exception
     */
    List<Map> getModiValue(List li) throws Exception;

    /**
     * 获取模板信息
     *
     * @param li
     * @return
     * @throws Exception
     */
    List<Map> getTempInfoById(List li) throws Exception;

    /**
     * 删除模板
     *
     * @param li
     * @return
     */
    int delTempById(List li) throws Exception;

    /*
     * 获取修改表
     */
    List<Map> getModiByKey(List li) throws Exception;

    /**
     * 获取用户
     *
     * @param li
     * @return
     */
    List<Map> getUserDataByUser(List li) throws Exception;

    /**
     * 根据地区获取用户
     *
     * @param li
     * @return
     */
    List<Map> getUserDataByDist(List li) throws Exception;

    /**
     * 获取统计信息
     *
     * @param li
     * @return
     */
    List<Map> getCheckData(List li) throws Exception;

    /**
     * 根据id获取表名
     *
     * @param li
     * @return
     */
    List<Map> getTableById(List li) throws Exception;

    /**
     * 获取tableid获取字段
     *
     * @param li
     * @return
     * @throws Exception
     */
    List<Map> getFieldByTableId(List li) throws Exception;

    /**
     * 根据id获取DB
     *
     * @param li
     * @return
     */
    List<Map> getDBById(List li) throws Exception;

    /**
     * 数据库中所有的表
     *
     * @param datebaseId
     * @return
     */
    List<MetaDataTable> listTableByDBId(String datebaseId) throws Exception;


    /**
     * 数据库中统计 表的数量
     *
     * @param datebaseId
     * @return {表名1:数量1,表名2:数量2}
     */
    Map countTableByDBId(String datebaseId) throws Exception;

    /**
     * 根据表名统计 记录个数
     *
     * @param tname
     * @return {'表名',数量}
     */
    Map countRecordByTable(String tname) throws Exception;

    /**
     * 根据表名和列名分组统计个数
     *
     * @param tname
     * @param tname
     * @return
     */

    Map countRecordByTableColumn(String tname, String cname, String where) throws Exception;

    /**
     * 根据数据库id统计数据库信息（返回封装好的html）
     *
     * @param datebaseId
     * @return String
     */
    String createAccountingReport(String datebaseId) throws Exception;

    /**
     * 根据数据库id统计数据库信息（返回封装好对象，需要在js中解析拼接字符串）
     *
     * @param datebaseId
     * @return Map
     */
    Map getAccountingReport(String datebaseId) throws Exception;

    /**
     * 获取未分页数据
     *
     * @param tflag
     * @param id
     * @param key
     * @return
     */
    List<Map> getFromDataNoPage(String tflag, String id, String key) throws Exception;

    /**
     * 获取数据
     *
     * @param tflag
     * @param id
     * @param key
     * @param page
     * @param rows
     * @return
     */
    String getFromData(String tflag, String id, String key, int page, int rows) throws Exception;

    /**
     * 获取关联表
     *
     * @param url
     * @param id
     * @param cond
     * @return
     */
    List<Map> getRelateData(String url, String id, String cond) throws Exception;

    /**
     * 获取同步字段
     *
     * @param url
     * @param tableName
     * @param tableId
     * @return
     */
    List<Map> getFieldData(String url, String tableName, String tableId) throws Exception;

    /**
     * 查询原来值
     *
     * @param url
     * @param tablename
     * @param prifield
     * @param prikey
     * @return
     */
    List<Map> findOrigValue(String url, String tablename, String prifield, String prikey) throws Exception;

    /**
     * 获取授权数据
     *
     * @param tflag
     * @param id
     * @param key
     * @param page
     * @param rows
     * @param roleId
     * @return
     */
    String getAuthorData(String tflag, String id, String key, int page,
                         int rows, String roleId) throws Exception;

    /**
     * 获取审核数据
     *
     * @param roleName
     * @param con
     * @param roleId
     * @param id
     * @param page
     * @param rows
     * @return
     */
    String getCheckData(String tflag, String roleName, String con, String roleId, String id,
                        int page, int rows) throws Exception;

    /**
     * 查询原值
     *
     * @param url
     * @param tablename
     * @param prifield
     * @param prikey
     * @return
     */
    List<Map> findData(String url, String tablename, String prifield,
                       String prikey) throws Exception;

    /**
     * 获取审核信息
     *
     * @param checktypes
     * @param tableid
     * @param start_time
     * @param end_time
     * @param result
     * @return
     */
    List<Map> getCheckTableInfo(String[] checktypes, String tableid,
                                String start_time, String end_time, String result) throws Exception;

    /**
     * 获取审核信息
     *
     * @param roleName
     * @param roleId
     * @param checktype
     * @param start_time
     * @param end_time
     * @param tflag
     * @param tableid
     * @param page
     * @param rows
     * @param result
     * @return
     * @throws Exception
     */
    String getCheckDataNew(String roleName, String roleId, String checktype,
                           String start_time, String end_time, String tflag, String tableid,
                           int page, int rows, String result) throws Exception;

    /**
     * 获取上报信息
     *
     * @param roleName
     * @param roleId
     * @param start_time
     * @param end_time
     * @param checktype
     * @param tflag
     * @param tableid
     * @param page
     * @param rows
     * @param result
     * @return
     */
    String getReportDataNew(String roleName, String roleId, String start_time,
                            String end_time, String checktype, String tflag, String tableid,
                            int page, int rows, String result) throws Exception;

    /**
     * 创建模板
     *
     * @param url
     * @param field
     * @param name
     * @param cname
     * @return
     */
    String saveTemp(String url, String field, String name, String cname) throws Exception;

    /**
     * 获取模板列表
     *
     * @param temp_name
     * @param start_time
     * @param end_time
     * @param page
     * @param rows
     * @return
     */
    String getTempData(String temp_name, String start_time, String end_time,
                       int page, int rows) throws Exception;

    /**
     * 上报
     *
     * @param ids
     * @return
     */
    ResultForm upData(String ids) throws Exception;

    /**
     * 连接测试
     *
     * @param url
     * @return
     */
    ResultForm connectTest(String url) throws Exception;

    /**
     * 驳回
     *
     * @param loginName
     * @param loginName
     * @param view
     * @param ta
     * @param fi
     * @param va
     * @param idd
     * @return
     */
    ResultForm passCheckDataNo(String type, String view, String loginName, String[] ta, String[] fi, String[] va, String[] idd) throws Exception;

    /**
     * 通过
     *
     * @param type
     * @param loginName
     * @param ta
     * @param fi
     * @param va
     * @param idd
     * @return
     */
    ResultForm passCheckData(String type, String loginName, String[] ta, String[] fi, String[] va, String[] idd) throws Exception;

    /**
     * 同步业务数据表
     */
    ResultForm alertTableDesc(String url, String sql) throws Exception;

    /*
    * 根据 字段id,获取字段信息
    * */
    Map getFieldInfoByFieldId(String fieldId) throws Exception;

    void delectMetaModify(String val) throws Exception;

}
