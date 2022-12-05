package com.augurit.agcloud.agcom.agsupport.sc.datacatalog.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.MetaDataDB;
import com.augurit.agcloud.agcom.agsupport.domain.MetaDataField;
import com.augurit.agcloud.agcom.agsupport.domain.MetaDataTable;
import com.augurit.agcloud.agcom.agsupport.sc.datacatalog.controller.DateJsonValueProcessor;
import com.augurit.agcloud.agcom.agsupport.sc.datacatalog.dao.DataCatalogDao;
import com.augurit.agcloud.agcom.agsupport.sc.datacatalog.service.DataCatalogService;
import com.augurit.agcloud.agcom.agsupport.sc.datacatalog.util.TreeNode;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.common.util.Common;
import com.mongodb.util.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import oracle.sql.TIMESTAMP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/2.
 */
@Transactional
@Service
public class DataCatalogServiceImpl implements DataCatalogService {
    @Autowired
    private DataCatalogDao dataCatalogDao;

    @Override
    public List<TreeNode> getMetaDataDBTableTree() throws Exception {
        List<Map> listDb = dataCatalogDao.listMetaData_DB();
        List<Map> listTable = dataCatalogDao.listMetaData_TABLE();
        List<TreeNode> listTreeNode = new ArrayList<>();
        TreeNode treeNodeDb = null;
        if (listDb != null && listDb.size() > 0) {
            for (Map mapDb : listDb) {
                String idInDb = String.valueOf(mapDb.get("ID"));
                treeNodeDb = new TreeNode();
                treeNodeDb.setId(idInDb);
                treeNodeDb.setText(String.valueOf(mapDb.get("CNAME")));
                Map<String, String> mapAttr = new HashMap<>();
                mapAttr.put("NAME", String.valueOf(mapDb.get("NAME")));
                treeNodeDb.setAttributes(mapAttr);
                treeNodeDb.setTflag("db");
                treeNodeDb.setIconCls("icon-folder");
                if (listTable != null && listTable.size() > 0) {
                    List<TreeNode> listChildren = new ArrayList<>();
                    for (Map mapTable : listTable) {
                        if (idInDb.equals(String.valueOf(mapTable.get("DATABASEID")))) {
                            TreeNode treeNodeTable = new TreeNode();
                            treeNodeTable.setId(String.valueOf(mapTable.get("ID")));
                            treeNodeTable.setText(String.valueOf(mapTable.get("CNAME")));
                            Map<String, String> mapAttrTable = new HashMap<>();
                            mapAttrTable.put("NAME", String.valueOf(mapTable.get("NAME")));
                            mapAttrTable.put("TABLEID", String.valueOf(mapTable.get("ID")));
                            treeNodeTable.setAttributes(mapAttrTable);
                            treeNodeTable.setTflag("table");
                            listChildren.add(treeNodeTable);

                        }
                    }
                    treeNodeDb.setChildren(listChildren);
                    treeNodeDb.setState("closed");
                }
                listTreeNode.add(treeNodeDb);
            }
        }
        return listTreeNode;
    }

    @Override
    public String listFromTable(String tablename, int page, int rows, String con) throws Exception {
        List<Map> listTable = dataCatalogDao.listFromTableByCondition(tablename, page, rows, con);
        // 判断是否缺失主键
        String error = "";
        int iCount = 0;
        if (listTable != null && listTable.size() > 0 && listTable.get(0).get("error") != null) {
            error = "缺失主键，请在数据库中设置表的主键字段！";
            listTable = new ArrayList<Map>();
        } else {
            iCount = dataCatalogDao.getTotalLineCountByCondition(tablename, con);
        }
        // 时间格式
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(TIMESTAMP.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        String str = "{\"msg\":\"" + error + "\",\"rows\":" + JSONArray.fromObject(listTable, config).toString() + ",\"total\":"
                + iCount + "}";
        return str;
    }

    @Override
    public List<Map> listMetaTableField_Def(String tableID) throws Exception {
        List<Map> listTable = dataCatalogDao.listMETADATA_FIELD(tableID);
        return listTable;
    }

    /**
     * 保存数据库
     */
    @Override
    public void insertMetaDataDB(MetaDataDB db) throws Exception {
        dataCatalogDao.insertMetaDataDB(db);
    }

    /**
     * 更新数据库
     */
    @Override
    public void updateMetaDataDB(MetaDataDB db) throws Exception {
        dataCatalogDao.updateMetaDataDB(db);
    }

    /**
     * 保存表
     */
    @Override
    public int insertMetaDataTable(MetaDataTable table) throws Exception {
        return dataCatalogDao.insertMetaDataTable(table);
    }

    /**
     * 更新表
     */
    @Override
    public void updateMetaDataTable(MetaDataTable table) throws Exception {
        dataCatalogDao.updateMetaDataTable(table);
    }

    /**
     * 更新字段
     */
    @Override
    public void updateMetaDataField(MetaDataField field) throws Exception {
        dataCatalogDao.updateMetaDataField(field);
    }

    /**
     * 保存字段
     */
    @Override
    public void insertMetaDataField(MetaDataField field) throws Exception {
        dataCatalogDao.insertMetaDataField(field);
    }

    /**
     * 删除字段
     */
    @Override
    public void delMetaDataField(String id) throws Exception {
        dataCatalogDao.delMetaDataField(id);
    }

    /**
     * 删除表
     */
    @Override
    public void delMetaDataTable(String id) throws Exception {
        dataCatalogDao.delMetaDataTable(id);
    }

    /**
     * 删除数据库
     */
    @Override
    public void delMetaDataDB(String id) throws Exception {
        dataCatalogDao.delMetaDataDB(id);
    }

    /**
     * 删除数据库与表关联
     */
    @Override
    public void delRelateData(String dbId) throws Exception {
        dataCatalogDao.delRelateData(dbId);
    }

    /**
     * 删除同步字段
     */
    @Override
    public void delFieldData(String tableId) throws Exception {
        dataCatalogDao.delFieldData(tableId);
    }

    /**
     * 根据表名查询
     *
     * @throws Exception
     */
    @Override
    public List<TreeNode> getMetaDataDBTableTreeByName(String id) throws Exception {
        List<Map> listDb = dataCatalogDao.listMetaData_DBByName(id);
        List<Map> listTable = dataCatalogDao.listMetaData_TABLEByName(id);
        List<TreeNode> listTreeNode = new ArrayList<>();
        TreeNode treeNodeDb = null;
        if (listDb != null && listDb.size() > 0) {
            for (Map mapDb : listDb) {
                String idInDb = String.valueOf(mapDb.get("ID"));
                treeNodeDb = new TreeNode();
                treeNodeDb.setId(idInDb);
                treeNodeDb.setText(String.valueOf(mapDb.get("CNAME")));
                Map<String, String> mapAttr = new HashMap<>();
                mapAttr.put("NAME", String.valueOf(mapDb.get("NAME")));
                treeNodeDb.setAttributes(mapAttr);
                treeNodeDb.setTflag("db");
                treeNodeDb.setIconCls("icon-folder");
                if (listTable != null && listTable.size() > 0) {
                    List<TreeNode> listChildren = new ArrayList<>();
                    for (Map mapTable : listTable) {
                        if (idInDb.equals(String.valueOf(mapTable.get("DATABASEID")))) {
                            TreeNode treeNodeTable = new TreeNode();
                            treeNodeTable.setId(String.valueOf(mapTable.get("ID")));
                            treeNodeTable.setText(String.valueOf(mapTable.get("CNAME")));
                            Map<String, String> mapAttrTable = new HashMap<>();
                            mapAttrTable.put("NAME", String.valueOf(mapTable.get("NAME")));
                            mapAttrTable.put("TABLEID", String.valueOf(mapTable.get("ID")));
                            treeNodeTable.setAttributes(mapAttrTable);
                            treeNodeTable.setTflag("table");
                            listChildren.add(treeNodeTable);
                        }
                    }
                    treeNodeDb.setChildren(listChildren);
                    treeNodeDb.setState("closed");
                }
                listTreeNode.add(treeNodeDb);
            }
        }
        return listTreeNode;
    }

    /**
     * 根据条件查询
     */
    @Override
    public String listFromTableByCondition(String tablename, int page, int rows, String con) throws Exception {
        List<Map> listTable = dataCatalogDao.listFromTableByCondition(tablename, page, rows, con);
        // 判断是否缺失主键
        String error = "";
        int iCount = 0;
        if (listTable != null && listTable.size() > 0 && listTable.get(0).get("error") != null) {
            error = "缺失主键，请在数据库中设置表的主键字段！";
            listTable = new ArrayList<Map>();
        } else {
            iCount = dataCatalogDao.getTotalLineCountByCondition(tablename, con);
        }
        // 时间格式
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(TIMESTAMP.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        String str = "{\"msg\":\"" + error + "\",\"rows\":" + JSONArray.fromObject(listTable, config).toString() + ",\"total\":"
                + iCount + "}";
        return str;
    }

    /**
     * 查询所有字段
     */
    @Override
    public List<Map> listMetaTableField_DefAll(String tableID) throws Exception {
        List<Map> listTable = dataCatalogDao.listMETADATA_FIELDAll(tableID);
        return listTable;
    }


    /**
     * 查询表
     */
    @Override
    public Object getMetaDataByTableName(String tableName) throws Exception {
        if (Common.isCheckNull(tableName)) return null;
        return dataCatalogDao.getMetaDataByTableName(tableName);
    }

    /**
     * 仅数据库
     *
     * @throws Exception
     */
    @Override
    public List<TreeNode> getMetaDataOnlyDBTableTree() throws Exception {
        List<Map> listDb = dataCatalogDao.listMetaData_DB();
        List<Map> listTable = dataCatalogDao.listMetaData_TABLE();
        List<TreeNode> listTreeNode = new ArrayList<>();
        TreeNode treeNodeDb = null;
        if (listDb != null && listDb.size() > 0) {
            for (Map mapDb : listDb) {
                String idInDb = String.valueOf(mapDb.get("ID"));
                treeNodeDb = new TreeNode();
                treeNodeDb.setId(idInDb);
                treeNodeDb.setText(String.valueOf(mapDb.get("CNAME")));
                Map<String, String> mapAttr = new HashMap<>();
                mapAttr.put("NAME", String.valueOf(mapDb.get("NAME")));
                treeNodeDb.setAttributes(mapAttr);
                treeNodeDb.setTflag("db");
                treeNodeDb.setIconCls("icon-folder");
                if (listTable != null && listTable.size() > 0) {
                    List<TreeNode> listChildren = new ArrayList<>();
                    for (Map mapTable : listTable) {
                        if (idInDb.equals(String.valueOf(mapTable.get("DATABASEID")))) {
                            TreeNode treeNodeTable = new TreeNode();
                            treeNodeTable.setId(String.valueOf(mapTable.get("ID")));
                            treeNodeTable.setText(String.valueOf(mapTable.get("CNAME")));
                            Map<String, String> mapAttrTable = new HashMap<>();
                            mapAttrTable.put("NAME", String.valueOf(mapTable.get("NAME")));
                            mapAttrTable.put("TABLEID", String.valueOf(mapTable.get("ID")));
                            treeNodeTable.setAttributes(mapAttrTable);
                            treeNodeTable.setTflag("table");
                            listChildren.add(treeNodeTable);

                        }
                    }
                }
                listTreeNode.add(treeNodeDb);
            }
        }
        return listTreeNode;
    }

    /**
     * 获取用户的角色信息
     */
    @Override
    public List<Map> getRoleInfo(List lii) throws Exception {
        return dataCatalogDao.getRoleInfo(lii);
    }

    /**
     * 获取编辑权限的表
     */
    @Override
    public List<Map> getTableByEditRole(String roleId) throws Exception {
        return dataCatalogDao.getTableByEditRole(roleId);
    }

    /**
     * 判断是否存在记录
     */
    @Override
    public List<Map> getRecords(List li) throws Exception {
        return dataCatalogDao.getRecords(li);
    }

    /**
     * 获取审核权限的表
     */
    @Override
    public List<Map> getTableByCheckRole(String roleId) throws Exception {
        return dataCatalogDao.getTableByCheckRole(roleId);
    }

    /**
     * 获取待审核的记录
     */
    @Override
    public List<Map> getCheckRecord(List li) throws Exception {
        return dataCatalogDao.getCheckRecord(li);
    }

    /**
     * 获取表
     */
    @Override
    public List<Map> getTableByUser(String roleId) throws Exception {
        return dataCatalogDao.getTableByUser(roleId);
    }

    @Override
    public List<Map> getFieldByTable(List li) throws Exception {
        return dataCatalogDao.getFieldByTable(li);
    }

    /**
     * 获取表信息
     */
    @Override
    public List<Map> getTableInfoByName(List li) throws Exception {
        return dataCatalogDao.getTableInfoByName(li);
    }

    /**
     * 获取地区
     */
    @Override
    public List<Map> getDisById(String roleId, List li) throws Exception {
        return dataCatalogDao.getDisById(roleId, li);
    }

    /**
     * 获取所有表
     */
    @Override
    public List<Map> getAllTable() throws Exception {
        return dataCatalogDao.getAllTable();
    }

    /**
     * 获取DB信息
     */
    @Override
    public List<Map> getDBInfoById(List li) throws Exception {
        return dataCatalogDao.getDBInfoById(li);
    }

    /**
     * 获取DB信息
     */
    @Override
    public List<Map> getDBByName(List li) throws Exception {
        return dataCatalogDao.getDBByName(li);
    }

    /**
     * 获取CName
     */
    @Override
    public List<Map> getCNameById(List li) throws Exception {
        return dataCatalogDao.getCNameById(li);
    }

    /**
     * 获取字段诈
     */
    @Override
    public List<Map> getFieldInfo(List li) throws Exception {
        return dataCatalogDao.getFieldInfo(li);
    }

    /**
     * 获取组织信息
     */
    @Override
    public List<Map> getOrgInfoByUser(List li) throws Exception {
        return dataCatalogDao.getOrgInfoByUser(li);
    }

    /**
     * 获取模板信息
     */
    @Override
    public List<Map> getTempInfo(List li) throws Exception {
        return dataCatalogDao.getTempInfo(li);
    }

    /**
     * 获取表信息
     */
    @Override
    public List<Map> getTableInfo(List li) throws Exception {
        return dataCatalogDao.getTableInfo(li);
    }

    /**
     * 获取角色信息
     */
    @Override
    public List<Map> getRoleInfoByUser(List li) throws Exception {
        return dataCatalogDao.getRoleInfoByUser(li);
    }

    /**
     * 获取统计信息
     */
    @Override
    public List<Map> findDataRecords() throws Exception {
        return dataCatalogDao.findDataRecords();
    }

    /**
     * 获取用户姓名
     */
    @Override
    public List<Map> getUserName(List li) throws Exception {
        return dataCatalogDao.getUserName(li);
    }

    /**
     * 获取表单信息
     */
    @Override
    public List<Map> getTableData(List li) throws Exception {
        return dataCatalogDao.getTableData(li);
    }

    /**
     * 根据表名查找
     */
    @Override
    public List<Map> getTableByName(List li) throws Exception {
        return dataCatalogDao.getTableByName(li);
    }

    /**
     * 获取字段中文
     */
    @Override
    public List<Map> getFieldCname(List li) throws Exception {
        return dataCatalogDao.getFieldCname(li);
    }

    /**
     * 查询修改值
     */
    @Override
    public List<Map> getModiValue(List li) throws Exception {
        return dataCatalogDao.getModiValue(li);
    }

    /**
     * 获取模板信息
     */
    @Override
    public List<Map> getTempInfoById(List li) throws Exception {
        return dataCatalogDao.getTempInfoById(li);
    }

    /**
     * 删除模板
     */
    @Override
    public int delTempById(List li) throws Exception {
        return dataCatalogDao.delTempById(li);
    }

    /**
     * 获取修改值
     */
    @Override
    public List<Map> getModiByKey(List li) throws Exception {
        return dataCatalogDao.getModiByKey(li);
    }

    /**
     * 获取用户
     */
    @Override
    public List<Map> getUserDataByUser(List li) throws Exception {
        return dataCatalogDao.getUserDataByUser(li);
    }

    /**
     * 根据地区获取用户
     */
    @Override
    public List<Map> getUserDataByDist(List li) throws Exception {
        return dataCatalogDao.getUserDataByDist(li);
    }

    /**
     * 获取统计信息
     */
    @Override
    public List<Map> getCheckData(List li) throws Exception {
        return dataCatalogDao.getCheckData(li);
    }

    /**
     * 根据id获取表名
     */
    @Override
    public List<Map> getTableById(List li) throws Exception {
        return dataCatalogDao.getTableById(li);
    }

    /**
     * 根据tableid获取字段
     */
    @Override
    public List<Map> getFieldByTableId(List li) throws Exception {
        return dataCatalogDao.getFieldByTableId(li);
    }

    /**
     * 根据id获取DB
     */
    @Override
    public List<Map> getDBById(List li) throws Exception {
        return dataCatalogDao.getDBById(li);
    }


    @Override
    public List<MetaDataTable> listTableByDBId(String datebaseId) throws Exception {
        List<MetaDataTable> listTable = dataCatalogDao.listMetaDataTable(datebaseId);
        return listTable;
    }


    @Override
    public Map countTableByDBId(String datebaseId) throws Exception {
        String table = "AG_WA_METADATA_TABLE";
        String where = "DATABASEID='" + datebaseId + "'";
        String sqlStr = " select " + dataCatalogDao.concatSql(table, where) + " from dual";
        List<Map> list = dataCatalogDao.listMetaData_SQL(sqlStr, null);
        Map resultMap = null;
        if (null != list && list.size() > 0) {
            resultMap = list.get(0);
        }
        return resultMap;
    }


    @Override
    public Map countRecordByTable(String tname) throws Exception {
        return dataCatalogDao.countRecordByTable(tname);
    }

    @Override
    public Map countRecordByTableColumn(String tname, String cname, String where) throws Exception {
        return dataCatalogDao.countRecordByTableAndColumn(tname, cname, where);
    }

    @Override
    public String createAccountingReport(String datebaseId) {
        String res = "";
        Connection conn = null;
        try {
            if (datebaseId.equals("1")) {//如果是数据库
                Map m = dataCatalogDao.countRecordByTableAndColumn("AG_WA_METADATA_DB", "dbtype");
                res = "在<span>数据库</span>中，<span class='type'>关系数据库</span>有<span class='count'>" + m.get("0") + "</span>个，" +
                        "<span class='type'>空间数据库</span>有<span class='count'>" + m.get("1") + "</span>个。";
            } else {
                //获得数据库
                MetaDataDB metaDataDB = dataCatalogDao.getMetaDataDB("select * from AG_WA_METADATA_DB where id='" + datebaseId + "'");
                conn = dataCatalogDao.getConnection(metaDataDB.getUrl());

                //获得该数据库下的所有表
                String sql = "select t.* from AG_WA_METADATA_TABLE t where t.databaseid='" + datebaseId + "' order by t.lastupdate desc,t.name  ";

                List<MetaDataTable> tableList = dataCatalogDao.listMetaDataTable(sql);

                String tableCountStr = "";  //表的统计报告
                String czjbCountStr = "";     //测站基本属性表的统计
                int dbReportCount = 0;      //数据库中的记录数量
                int tableCount = 0;         //表的数量
                int tableReportCount = 0;    //表中的记录数量

                if (null != tableList && tableList.size() > 0) {
                    tableCount = tableList.size();

                    tableCountStr += "<br/>其中：<br/>";
                    //遍历所有的表
                    for (MetaDataTable table : tableList) {
                        String cname = table.getCname();
                        String tname = table.getName();
                        String sumField = table.getSumfield();      //累计字段
                        String groupField = table.getGroupfield();  //分组字段


                        //----------------------累计字段-------------------------------
                        MetaDataField filed = null;
                        double sumValue = 0;       //累计值
                        if (null != sumField && !sumField.equals("") && !sumField.equals("null")) {
                            filed = dataCatalogDao.getMetaDataField("select unit,cname from AG_WA_METADATA_FIELD where name='" + sumField + "' and tableid='" + table.getId() + "'");
                            sumValue = dataCatalogDao.sumByTableAndColumn(conn, tname, sumField);
                        }
                        //累计统计生成的统计语句
                        String sumCountStr = "";
                        if (sumValue > 0 && null != filed) {
                            sumCountStr = "<div class='sum-container'>总<span class='type'>" + filed.getCname() + "</span>为：" +
                                    "<span class='count'>" + sumValue + "</span>" +
                                    "<span class='unit'>" + (filed.getUnit() != null ? filed.getUnit() : "") + "</span></div>";
                        }

                        //---------------------分组统计----------------------------------
                        String dbStr = "";              //数据字典 字符串
                        List<Map> groupCountList = null;   //分组统计的计算值

                        //测站基本表信息表 站类统计   如果测站基本表的话，单独统计
                        boolean stbprpFlag = (null != groupField && groupField.equals("STTP") && tname.equals("ST_STBPRP_B"));

                        //测站基本表的统计
                        if (stbprpFlag) {
                            filed = dataCatalogDao.getMetaDataField("select dd from AG_WA_METADATA_FIELD where name='" + groupField + "' and tableid='" + table.getId() + "'");
                            dbStr = filed.getDd();
                            Map<String, String> dbMap = (Map<String, String>) JSON.parse(dbStr);
                            //总的
                            List<Map> amountList = dataCatalogDao.listRecordByTableAndColumn(conn, tname, groupField);
                            //未使用的站点
                            List<Map> amountList0 = dataCatalogDao.listRecordByTableAndColumn(conn, tname, groupField, " and usfl=0");
                            int ylzAmount1 = dataCatalogDao.getCountByTableName(conn, "ST_STBPRP_B",
                                    " and exists(select stcd from ST_STITEM_B  where mnit = 3 and stcd=t.stcd) and usfl=1");
                            int ylzAmount0 = dataCatalogDao.getCountByTableName(conn, "ST_STBPRP_B",
                                    " and exists(select stcd from ST_STITEM_B  where mnit = 3 and stcd=t.stcd) and usfl=0");
                            for (Map m : amountList) {
                                int total = Integer.valueOf(m.get("amount") == null ? "" : String.valueOf(m.get("amount")));
                                int amount0 = 0;//没有在用的个数
                                String type = (String) m.get("type");
                                String ctype = "未知类型";
                                if (type != null) {
                                    ctype = dbMap.get(type);
                                }
                                //雨量站单独统计
                                if (!type.equals("PP")) {
                                    //取没有在用的数量
                                    for (Map m0 : amountList0) {
                                        if (m.get("type").equals(m0.get("type"))) {
                                            amount0 = Integer.valueOf(m0.get("amount") == null ? "0" : String.valueOf(m0.get("amount")));
                                            break;
                                        }
                                    }
                                } else {
                                    total = ylzAmount1 + ylzAmount0;
                                    amount0 = ylzAmount0;
                                }
                                czjbCountStr += "<div class='czjb-container'><span class='type'>" + ctype + "</span>共有<span class='count'>" +
                                        total + "</span>个";
                                if (amount0 > 0) {
                                    czjbCountStr += "其中，在用<span class='used-count'>" + (total - amount0) + "</span>个、" +
                                            "停用<span class='disused-count'>" + (amount0) + "</span>个";
                                }
                                czjbCountStr += "；</div>";
                            }
                        }
                        //非测站基本表分组统计
                        if (null != groupField && !groupField.equals("") && !groupField.equals("null") && !stbprpFlag) {
                            filed = dataCatalogDao.getMetaDataField("select dd from AG_WA_METADATA_FIELD where name='" + groupField + "' and tableid='" + table.getId() + "'");
                            if (null != filed) {
                                dbStr = filed.getDd();
                            }
                            groupCountList = dataCatalogDao.listRecordByTableAndColumn(conn, tname, groupField);
                        }
                        //分组统计生成的统计语句
                        String typeCountStr = "";
                        if (null != dbStr && !dbStr.equals("") && !dbStr.equals("null") &&
                                null != groupCountList && groupCountList.size() > 0) {
                            typeCountStr = "，";
                            Map<String, String> dbMap = (Map<String, String>) JSON.parse(dbStr);
                            for (Map m : groupCountList) {
                                //amount type
                                String type = (String) m.get("type");
                                String ctype = "未知类型";
                                if (type != null) {
                                    ctype = dbMap.get(type);
                                }
                                int amount = Integer.valueOf(m.get("amount") == null ? "" : String.valueOf(m.get("amount")));
                                typeCountStr += "<div class='type-container'><span class='type'>" + ctype + "</span> 共有 <span class='count'>" + amount + "</span>个；</div>";
                            }
                        }
                        //-------------------对表的数据统计
                        tableReportCount = dataCatalogDao.getCountByTableName(conn, tname);
                        String s = " 条数据 ";
                        if (metaDataDB.getDbtype().equals("1")) {
                            cname = cname.replace("基础信息表", "").replace("基础数据表", "").replace("属性信息表", "").replace("基本信息表", "") + " ";
                            tableCountStr += " ";
                            s = " 个记录 ";
                        }
                        tableCountStr += "<div class='table-container'><span class='type'>" + cname + "</span>，共有<span class='count'>" + tableReportCount + "</span>" + s;
                        if (!sumCountStr.equals("")) {
                            tableCountStr += sumCountStr;
                        }
                        if (!typeCountStr.equals("")) {
                            tableCountStr += typeCountStr;
                        }
                        tableCountStr += "</div>";
                    }
                }//对表的统计
                String flag = metaDataDB.getDbtype().equals("0") ? "张表" : "种要素";
                res = "在<span>" + metaDataDB.getCname() + "</span>中，共有 <span class='count'>" + tableCount + "</span>" + flag
                        + ",总计 <span class='count'>" + dbReportCount + "</span>条数据。";
                if (!czjbCountStr.equals("")) {//测站基本信息表
                    res += "" + czjbCountStr;
                }
                if (!tableCountStr.equals("")) {
                    res += "" + tableCountStr;
                }
            }
        } catch (Exception e) {
            res = "统计报告生成失败，请联系管理员";
            e.printStackTrace();
        } finally {
            dataCatalogDao.closeDBObjects(null, null, conn);
        }
        return res;
    }

    @Override
    public Map getAccountingReport(String datebaseId) {
        //返回结果
        Map<String, Object> resMap = new HashMap<String, Object>();
        Connection conn = null;
        try {
            //组织数据形式
            List<Map> dataList = new ArrayList<Map>();
            if (datebaseId.equals("1")) {//如果是数据库
                String dbStr = "{\"0\":\"关系数据库\",\"1\":\"空间数据库\"}";
                List<Map> dbList = dataCatalogDao.listRecordByTableAndColumn("AG_WA_METADATA_DB", "dbtype");
                dataList = this.typeListToNameList(dbStr, dbList);
                resMap.put("dbName", "数据库");
                resMap.put("type", "db");
                resMap.put("list", dataList);
            } else {
                //获得数据库
                MetaDataDB metaDataDB = dataCatalogDao.getMetaDataDB("select * from AG_WA_METADATA_DB where id='" + datebaseId + "'");
                //获得连接
                conn = dataCatalogDao.getConnection(metaDataDB.getUrl());
                //获得该数据库下的所有表
                String sql = "select t.* from AG_WA_METADATA_TABLE t where t.databaseid='" + datebaseId + "' order by t.lastupdate desc,t.name";
                List<MetaDataTable> tableList = dataCatalogDao.listMetaDataTable(sql);
                int dbReportCount = 0;      //数据库中的记录数量
                int tableCount = 0;         //表的数量
                int tableReportCount;       //每张表中的记录数量
                List<Map> topGroupList = null;    //分组统计的计算值
                if (null != tableList && tableList.size() > 0) {
                    tableCount = tableList.size();
                    //遍历所有的表
                    for (MetaDataTable table : tableList) {
                        String cname = table.getCname();
                        String tname = table.getName();
                        String sumField = table.getSumfield();      //累计字段
                        String groupField = table.getGroupfield();  //分组字段
                        if (metaDataDB.getDbtype().equals("1")) {
                            cname = cname.replace("基础信息表", "").replace("基础数据表", "").replace("属性信息表", "").replace("基本信息表", "") + " ";
                        }
                        //-------------------对表的数据统计
                        tableReportCount = dataCatalogDao.getCountByTableName(conn, tname);
                        dbReportCount += tableReportCount;//库中所有表的个数
                        //----------------------累计字段-------------------------------
                        Map sumMap = null;//总计
                        MetaDataField filed = null;
                        double sumValue = 0;       //累计值
                        if (null != sumField && !sumField.equals("") && !sumField.equals("null")) {
                            filed = dataCatalogDao.getMetaDataField("select unit,cname from AG_WA_METADATA_FIELD where name='" + sumField + "' and tableid='" + table.getId() + "'");
                            sumValue = dataCatalogDao.sumByTableAndColumn(conn, tname, sumField);
                        }
                        if (sumValue > 0 && null != filed) {
                            sumMap = new HashMap();
                            sumMap.put("type", filed.getCname());
                            sumMap.put("value", sumValue);
                            sumMap.put("unit", filed.getUnit() != null ? filed.getUnit() : "");
                        }
                        //---------------------分组统计----------------------------------
                        List<Map> groupList = null;     //普通分组统计
                        //测站基本表信息表 站类统计
                        if (null != groupField && !groupField.equals("") && !groupField.equals("null")) {
                            filed = dataCatalogDao.getMetaDataField("select dd from AG_WA_METADATA_FIELD where name='" + groupField + "' and tableid='" + table.getId() + "'");
                            String dbStr = filed.getDd();//数据字典
                            //测站基本表分组统计
                            if (groupField.equals("STTP") && tname.equals("ST_STBPRP_B")) {
                                topGroupList = new ArrayList<Map>();
                                Map<String, String> dbMap = (Map<String, String>) JSON.parse(dbStr);
                                List<Map> amountList = dataCatalogDao.listRecordByTableAndColumn(conn, tname, groupField);//总的
                                List<Map> amountList0 = dataCatalogDao.listRecordByTableAndColumn(conn, tname, groupField, " and usfl=0");//未使用的站点
                                //雨量站统计
                                int ylzAmount1 = dataCatalogDao.getCountByTableName(conn, "ST_STBPRP_B",
                                        " and exists(select stcd from ST_STITEM_B  where mnit = 3 and stcd=t.stcd) and usfl=1");
                                int ylzAmount0 = dataCatalogDao.getCountByTableName(conn, "ST_STBPRP_B",
                                        " and exists(select stcd from ST_STITEM_B  where mnit = 3 and stcd=t.stcd) and usfl=0");
                                for (Map m : amountList) {
                                    int total = Integer.valueOf(m.get("amount") == null ? "" : String.valueOf(m.get("amount")));
                                    int amount0 = 0;//没有在用的个数
                                    String type = (String) m.get("type");
                                    String ctype = "其他";
                                    if (type != null) {
                                        ctype = dbMap.get(type);
                                    }
                                    if (!type.equals("PP")) {
                                        //取没有在用的数量
                                        for (Map m0 : amountList0) {
                                            if (m.get("type").equals(m0.get("type"))) {
                                                amount0 = Integer.valueOf(m0.get("amount") == null ? "0" : String.valueOf(m0.get("amount")));
                                                break;
                                            }
                                        }
                                    } else {//雨量站单独统计
                                        total = ylzAmount1 + ylzAmount0;
                                        amount0 = ylzAmount0;
                                    }
                                    Map groupMap = new HashMap();
                                    groupMap.put("type", ctype);
                                    groupMap.put("amount", total);//总数量
                                    groupMap.put("amount0", amount0);//停用的数量
                                    groupMap.put("amount1", (total - amount0));//在用的数量
                                    topGroupList.add(groupMap);
                                }
                            } else {
                                //非测站基本表分组统计
                                groupList = dataCatalogDao.listRecordByTableAndColumn(conn, tname, groupField);
                                groupList = this.typeListToNameList(dbStr, groupList);
                            }
                            //如果是水质
                            if (metaDataDB.getCname().equals("水质数据库") && cname.equals("水质监测站基本信息表")) {
                                topGroupList = groupList;
                                groupList = null;
                            }
                        }
                        //组织数据形式
                        Map tableMap = new HashMap();
                        tableMap.put("type", cname);
                        tableMap.put("amount", tableReportCount);
                        tableMap.put("sumObj", sumMap);
                        tableMap.put("groupList", groupList);
                        tableMap.put("fieldUnit", table.getFieldunit());
                        dataList.add(tableMap);
                    }//对表的遍历
                }
                resMap.put("type", "table");
                resMap.put("dbName", metaDataDB.getCname());
                resMap.put("dbType", metaDataDB.getDbtype());
                resMap.put("tableCount", tableCount);
                resMap.put("dbReportCount", dbReportCount);
                resMap.put("list", dataList);
                //测站基本信息表
                if (null != topGroupList && topGroupList.size() > 0) {
                    resMap.put("topGroupList", topGroupList);
                }
            }//如果是表格
        } catch (Exception e) {
            resMap = null;
            e.printStackTrace();
        } finally {
            dataCatalogDao.closeDBObjects(null, null, conn);
        }
        return resMap;
    }

    /**
     * 数据字典 -->类型代码 转为 中文意思
     * [{'PP':45},{'RW':5}]-->[{'雨量站':45},{'拦河坝':5}]
     */
    private List<Map> typeListToNameList(String dbStr, List<Map> groupCountList) {
        List<Map> dataList = null;
        if (null != dbStr && !dbStr.equals("") && !dbStr.equals("null") &&
                null != groupCountList && groupCountList.size() > 0) {
            dataList = new ArrayList<Map>();
            Map<String, String> dbMap = (Map<String, String>) JSON.parse(dbStr);
            Map dataMap = null;
            for (Map m : groupCountList) {
                //amount type
                String type = (String) m.get("type");
                String ctype = "其他";
                if (type != null) {
                    ctype = dbMap.get(type);
                }
                int amount = Integer.valueOf(m.get("amount") == null ? "" : String.valueOf(m.get("amount")));
                dataMap = new HashMap();
                dataMap.put("type", ctype);
                dataMap.put("amount", amount);
                dataList.add(dataMap);
            }
        }
        if (null == dbStr) {
            System.out.println("数据字典为空！");
        }
        return dataList;
    }

    /**
     * 获取未分页数据
     */
    @Override
    public List<Map> getFromDataNoPage(String tflag, String id, String key) throws Exception {
        return dataCatalogDao.getFromDataNoPage(tflag, id, key);
    }

    /**
     * 获取数据
     */
    @Override
    public String getFromData(String tflag, String id, String key, int page, int rows) throws Exception {
        return dataCatalogDao.getFromData(tflag, id, key, page, rows);
    }

    /**
     * 获取关联表
     */
    @Override
    public List<Map> getRelateData(String url, String id, String cond) throws Exception {
        return dataCatalogDao.getRelateData(url, id, cond);
    }

    /**
     * 获取同步字段
     */
    @Override
    public List<Map> getFieldData(String url, String tableName, String tableId) throws Exception {
        return dataCatalogDao.getFieldData(url, tableName, tableId);
    }

    /**
     * 查询原来值
     */
    @Override
    public List<Map> findOrigValue(String url, String tablename, String prifield, String prikey) throws Exception {
        return dataCatalogDao.findOrigValue(url, tablename, prifield, prikey);
    }

    /**
     * 获取授权数据
     */
    @Override
    public String getAuthorData(String tflag, String id, String key, int page, int rows, String roleId) throws Exception {
        return dataCatalogDao.getAuthorData(tflag, id, key, page, rows, roleId);
    }

    /**
     * 获取审核数据
     */
    @Override
    public String getCheckData(String tflag, String roleName, String con, String roleId, String id, int page, int rows) throws Exception {
        return dataCatalogDao.getCheckData(tflag, roleName, con, roleId, id, page, rows);
    }


    /**
     * 查询原值
     */
    @Override
    public List<Map> findData(String url, String tablename, String prifield, String prikey) throws Exception {
        return dataCatalogDao.findData(url, tablename, prifield, prikey);
    }

    /**
     * 获取审核信息
     */
    @Override
    public List<Map> getCheckTableInfo(String[] checktypes, String tableid,
                                       String start_time, String end_time, String result) throws Exception {
        return dataCatalogDao.getCheckTableInfo(checktypes, tableid, start_time, end_time, result);
    }

    /**
     * 获取审核信息
     */
    @Override
    public String getCheckDataNew(String roleName, String roleId, String checktype, String start_time, String end_time, String tflag, String tableid, int page, int rows, String result) throws Exception {
        return dataCatalogDao.getCheckDataNew(roleName, roleId, checktype, start_time, end_time, tflag, tableid, page, rows, result);
    }

    /**
     * 获取上报信息
     */
    @Override
    public String getReportDataNew(String roleName, String roleId, String start_time, String end_time, String checktype, String tflag, String tableid, int page, int rows, String result) throws Exception {
        return dataCatalogDao.getReportDataNew(roleName, roleId, start_time, end_time, checktype, tflag, tableid, page, rows, result);
    }

    /**
     * 创建模板
     */
    @Override
    public String saveTemp(String url, String field, String name, String cname) throws Exception {
        return dataCatalogDao.saveTemp(url, field, name, cname);
    }

    /**
     * 获取模板列表
     */
    @Override
    public String getTempData(String temp_name, String start_time, String end_time, int page, int rows) throws Exception {
        return dataCatalogDao.getTempData(temp_name, start_time, end_time, page, rows);
    }

    /**
     * 上报
     */
    @Override
    public ResultForm upData(String ids) throws Exception {
        return dataCatalogDao.upData(ids);
    }

    /**
     * 连接测试
     */
    @Override
    public ResultForm connectTest(String url) {
        return dataCatalogDao.connectTest(url);
    }

    /**
     * 驳回
     */
    @Override
    public ResultForm passCheckDataNo(String type, String view, String loginName, String[] ta, String[] fi, String[] va, String[] idd) throws Exception {
        return dataCatalogDao.passCheckDataNo(type, view, loginName, ta, fi, va, idd);
    }

    /**
     * 通过
     */
    @Override
    public ResultForm passCheckData(String type, String loginName, String[] ta, String[] fi, String[] va, String[] idd) throws Exception {
        return dataCatalogDao.passCheckData(type, loginName, ta, fi, va, idd);
    }

    /*
    * 同步业务数据库
    * */
    @Override
    public ResultForm alertTableDesc(String url, String sql) throws Exception{
        return dataCatalogDao.alertTableDesc(url, sql);
    }

    @Override
    public Map getFieldInfoByFieldId(String fieldId) throws Exception{
        return dataCatalogDao.getFieldInfoByFieldId(fieldId);
    }

    @Override
    public void delectMetaModify(String val) throws Exception {
        dataCatalogDao.delectMetaModify(val);
    }

}
