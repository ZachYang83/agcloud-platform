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
        // ????????????????????????
        String error = "";
        int iCount = 0;
        if (listTable != null && listTable.size() > 0 && listTable.get(0).get("error") != null) {
            error = "????????????????????????????????????????????????????????????";
            listTable = new ArrayList<Map>();
        } else {
            iCount = dataCatalogDao.getTotalLineCountByCondition(tablename, con);
        }
        // ????????????
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
     * ???????????????
     */
    @Override
    public void insertMetaDataDB(MetaDataDB db) throws Exception {
        dataCatalogDao.insertMetaDataDB(db);
    }

    /**
     * ???????????????
     */
    @Override
    public void updateMetaDataDB(MetaDataDB db) throws Exception {
        dataCatalogDao.updateMetaDataDB(db);
    }

    /**
     * ?????????
     */
    @Override
    public int insertMetaDataTable(MetaDataTable table) throws Exception {
        return dataCatalogDao.insertMetaDataTable(table);
    }

    /**
     * ?????????
     */
    @Override
    public void updateMetaDataTable(MetaDataTable table) throws Exception {
        dataCatalogDao.updateMetaDataTable(table);
    }

    /**
     * ????????????
     */
    @Override
    public void updateMetaDataField(MetaDataField field) throws Exception {
        dataCatalogDao.updateMetaDataField(field);
    }

    /**
     * ????????????
     */
    @Override
    public void insertMetaDataField(MetaDataField field) throws Exception {
        dataCatalogDao.insertMetaDataField(field);
    }

    /**
     * ????????????
     */
    @Override
    public void delMetaDataField(String id) throws Exception {
        dataCatalogDao.delMetaDataField(id);
    }

    /**
     * ?????????
     */
    @Override
    public void delMetaDataTable(String id) throws Exception {
        dataCatalogDao.delMetaDataTable(id);
    }

    /**
     * ???????????????
     */
    @Override
    public void delMetaDataDB(String id) throws Exception {
        dataCatalogDao.delMetaDataDB(id);
    }

    /**
     * ???????????????????????????
     */
    @Override
    public void delRelateData(String dbId) throws Exception {
        dataCatalogDao.delRelateData(dbId);
    }

    /**
     * ??????????????????
     */
    @Override
    public void delFieldData(String tableId) throws Exception {
        dataCatalogDao.delFieldData(tableId);
    }

    /**
     * ??????????????????
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
     * ??????????????????
     */
    @Override
    public String listFromTableByCondition(String tablename, int page, int rows, String con) throws Exception {
        List<Map> listTable = dataCatalogDao.listFromTableByCondition(tablename, page, rows, con);
        // ????????????????????????
        String error = "";
        int iCount = 0;
        if (listTable != null && listTable.size() > 0 && listTable.get(0).get("error") != null) {
            error = "????????????????????????????????????????????????????????????";
            listTable = new ArrayList<Map>();
        } else {
            iCount = dataCatalogDao.getTotalLineCountByCondition(tablename, con);
        }
        // ????????????
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(TIMESTAMP.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        String str = "{\"msg\":\"" + error + "\",\"rows\":" + JSONArray.fromObject(listTable, config).toString() + ",\"total\":"
                + iCount + "}";
        return str;
    }

    /**
     * ??????????????????
     */
    @Override
    public List<Map> listMetaTableField_DefAll(String tableID) throws Exception {
        List<Map> listTable = dataCatalogDao.listMETADATA_FIELDAll(tableID);
        return listTable;
    }


    /**
     * ?????????
     */
    @Override
    public Object getMetaDataByTableName(String tableName) throws Exception {
        if (Common.isCheckNull(tableName)) return null;
        return dataCatalogDao.getMetaDataByTableName(tableName);
    }

    /**
     * ????????????
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
     * ???????????????????????????
     */
    @Override
    public List<Map> getRoleInfo(List lii) throws Exception {
        return dataCatalogDao.getRoleInfo(lii);
    }

    /**
     * ????????????????????????
     */
    @Override
    public List<Map> getTableByEditRole(String roleId) throws Exception {
        return dataCatalogDao.getTableByEditRole(roleId);
    }

    /**
     * ????????????????????????
     */
    @Override
    public List<Map> getRecords(List li) throws Exception {
        return dataCatalogDao.getRecords(li);
    }

    /**
     * ????????????????????????
     */
    @Override
    public List<Map> getTableByCheckRole(String roleId) throws Exception {
        return dataCatalogDao.getTableByCheckRole(roleId);
    }

    /**
     * ????????????????????????
     */
    @Override
    public List<Map> getCheckRecord(List li) throws Exception {
        return dataCatalogDao.getCheckRecord(li);
    }

    /**
     * ?????????
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
     * ???????????????
     */
    @Override
    public List<Map> getTableInfoByName(List li) throws Exception {
        return dataCatalogDao.getTableInfoByName(li);
    }

    /**
     * ????????????
     */
    @Override
    public List<Map> getDisById(String roleId, List li) throws Exception {
        return dataCatalogDao.getDisById(roleId, li);
    }

    /**
     * ???????????????
     */
    @Override
    public List<Map> getAllTable() throws Exception {
        return dataCatalogDao.getAllTable();
    }

    /**
     * ??????DB??????
     */
    @Override
    public List<Map> getDBInfoById(List li) throws Exception {
        return dataCatalogDao.getDBInfoById(li);
    }

    /**
     * ??????DB??????
     */
    @Override
    public List<Map> getDBByName(List li) throws Exception {
        return dataCatalogDao.getDBByName(li);
    }

    /**
     * ??????CName
     */
    @Override
    public List<Map> getCNameById(List li) throws Exception {
        return dataCatalogDao.getCNameById(li);
    }

    /**
     * ???????????????
     */
    @Override
    public List<Map> getFieldInfo(List li) throws Exception {
        return dataCatalogDao.getFieldInfo(li);
    }

    /**
     * ??????????????????
     */
    @Override
    public List<Map> getOrgInfoByUser(List li) throws Exception {
        return dataCatalogDao.getOrgInfoByUser(li);
    }

    /**
     * ??????????????????
     */
    @Override
    public List<Map> getTempInfo(List li) throws Exception {
        return dataCatalogDao.getTempInfo(li);
    }

    /**
     * ???????????????
     */
    @Override
    public List<Map> getTableInfo(List li) throws Exception {
        return dataCatalogDao.getTableInfo(li);
    }

    /**
     * ??????????????????
     */
    @Override
    public List<Map> getRoleInfoByUser(List li) throws Exception {
        return dataCatalogDao.getRoleInfoByUser(li);
    }

    /**
     * ??????????????????
     */
    @Override
    public List<Map> findDataRecords() throws Exception {
        return dataCatalogDao.findDataRecords();
    }

    /**
     * ??????????????????
     */
    @Override
    public List<Map> getUserName(List li) throws Exception {
        return dataCatalogDao.getUserName(li);
    }

    /**
     * ??????????????????
     */
    @Override
    public List<Map> getTableData(List li) throws Exception {
        return dataCatalogDao.getTableData(li);
    }

    /**
     * ??????????????????
     */
    @Override
    public List<Map> getTableByName(List li) throws Exception {
        return dataCatalogDao.getTableByName(li);
    }

    /**
     * ??????????????????
     */
    @Override
    public List<Map> getFieldCname(List li) throws Exception {
        return dataCatalogDao.getFieldCname(li);
    }

    /**
     * ???????????????
     */
    @Override
    public List<Map> getModiValue(List li) throws Exception {
        return dataCatalogDao.getModiValue(li);
    }

    /**
     * ??????????????????
     */
    @Override
    public List<Map> getTempInfoById(List li) throws Exception {
        return dataCatalogDao.getTempInfoById(li);
    }

    /**
     * ????????????
     */
    @Override
    public int delTempById(List li) throws Exception {
        return dataCatalogDao.delTempById(li);
    }

    /**
     * ???????????????
     */
    @Override
    public List<Map> getModiByKey(List li) throws Exception {
        return dataCatalogDao.getModiByKey(li);
    }

    /**
     * ????????????
     */
    @Override
    public List<Map> getUserDataByUser(List li) throws Exception {
        return dataCatalogDao.getUserDataByUser(li);
    }

    /**
     * ????????????????????????
     */
    @Override
    public List<Map> getUserDataByDist(List li) throws Exception {
        return dataCatalogDao.getUserDataByDist(li);
    }

    /**
     * ??????????????????
     */
    @Override
    public List<Map> getCheckData(List li) throws Exception {
        return dataCatalogDao.getCheckData(li);
    }

    /**
     * ??????id????????????
     */
    @Override
    public List<Map> getTableById(List li) throws Exception {
        return dataCatalogDao.getTableById(li);
    }

    /**
     * ??????tableid????????????
     */
    @Override
    public List<Map> getFieldByTableId(List li) throws Exception {
        return dataCatalogDao.getFieldByTableId(li);
    }

    /**
     * ??????id??????DB
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
            if (datebaseId.equals("1")) {//??????????????????
                Map m = dataCatalogDao.countRecordByTableAndColumn("AG_WA_METADATA_DB", "dbtype");
                res = "???<span>?????????</span>??????<span class='type'>???????????????</span>???<span class='count'>" + m.get("0") + "</span>??????" +
                        "<span class='type'>???????????????</span>???<span class='count'>" + m.get("1") + "</span>??????";
            } else {
                //???????????????
                MetaDataDB metaDataDB = dataCatalogDao.getMetaDataDB("select * from AG_WA_METADATA_DB where id='" + datebaseId + "'");
                conn = dataCatalogDao.getConnection(metaDataDB.getUrl());

                //?????????????????????????????????
                String sql = "select t.* from AG_WA_METADATA_TABLE t where t.databaseid='" + datebaseId + "' order by t.lastupdate desc,t.name  ";

                List<MetaDataTable> tableList = dataCatalogDao.listMetaDataTable(sql);

                String tableCountStr = "";  //??????????????????
                String czjbCountStr = "";     //??????????????????????????????
                int dbReportCount = 0;      //???????????????????????????
                int tableCount = 0;         //????????????
                int tableReportCount = 0;    //?????????????????????

                if (null != tableList && tableList.size() > 0) {
                    tableCount = tableList.size();

                    tableCountStr += "<br/>?????????<br/>";
                    //??????????????????
                    for (MetaDataTable table : tableList) {
                        String cname = table.getCname();
                        String tname = table.getName();
                        String sumField = table.getSumfield();      //????????????
                        String groupField = table.getGroupfield();  //????????????


                        //----------------------????????????-------------------------------
                        MetaDataField filed = null;
                        double sumValue = 0;       //?????????
                        if (null != sumField && !sumField.equals("") && !sumField.equals("null")) {
                            filed = dataCatalogDao.getMetaDataField("select unit,cname from AG_WA_METADATA_FIELD where name='" + sumField + "' and tableid='" + table.getId() + "'");
                            sumValue = dataCatalogDao.sumByTableAndColumn(conn, tname, sumField);
                        }
                        //?????????????????????????????????
                        String sumCountStr = "";
                        if (sumValue > 0 && null != filed) {
                            sumCountStr = "<div class='sum-container'>???<span class='type'>" + filed.getCname() + "</span>??????" +
                                    "<span class='count'>" + sumValue + "</span>" +
                                    "<span class='unit'>" + (filed.getUnit() != null ? filed.getUnit() : "") + "</span></div>";
                        }

                        //---------------------????????????----------------------------------
                        String dbStr = "";              //???????????? ?????????
                        List<Map> groupCountList = null;   //????????????????????????

                        //???????????????????????? ????????????   ??????????????????????????????????????????
                        boolean stbprpFlag = (null != groupField && groupField.equals("STTP") && tname.equals("ST_STBPRP_B"));

                        //????????????????????????
                        if (stbprpFlag) {
                            filed = dataCatalogDao.getMetaDataField("select dd from AG_WA_METADATA_FIELD where name='" + groupField + "' and tableid='" + table.getId() + "'");
                            dbStr = filed.getDd();
                            Map<String, String> dbMap = (Map<String, String>) JSON.parse(dbStr);
                            //??????
                            List<Map> amountList = dataCatalogDao.listRecordByTableAndColumn(conn, tname, groupField);
                            //??????????????????
                            List<Map> amountList0 = dataCatalogDao.listRecordByTableAndColumn(conn, tname, groupField, " and usfl=0");
                            int ylzAmount1 = dataCatalogDao.getCountByTableName(conn, "ST_STBPRP_B",
                                    " and exists(select stcd from ST_STITEM_B  where mnit = 3 and stcd=t.stcd) and usfl=1");
                            int ylzAmount0 = dataCatalogDao.getCountByTableName(conn, "ST_STBPRP_B",
                                    " and exists(select stcd from ST_STITEM_B  where mnit = 3 and stcd=t.stcd) and usfl=0");
                            for (Map m : amountList) {
                                int total = Integer.valueOf(m.get("amount") == null ? "" : String.valueOf(m.get("amount")));
                                int amount0 = 0;//?????????????????????
                                String type = (String) m.get("type");
                                String ctype = "????????????";
                                if (type != null) {
                                    ctype = dbMap.get(type);
                                }
                                //?????????????????????
                                if (!type.equals("PP")) {
                                    //????????????????????????
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
                                czjbCountStr += "<div class='czjb-container'><span class='type'>" + ctype + "</span>??????<span class='count'>" +
                                        total + "</span>???";
                                if (amount0 > 0) {
                                    czjbCountStr += "???????????????<span class='used-count'>" + (total - amount0) + "</span>??????" +
                                            "??????<span class='disused-count'>" + (amount0) + "</span>???";
                                }
                                czjbCountStr += "???</div>";
                            }
                        }
                        //??????????????????????????????
                        if (null != groupField && !groupField.equals("") && !groupField.equals("null") && !stbprpFlag) {
                            filed = dataCatalogDao.getMetaDataField("select dd from AG_WA_METADATA_FIELD where name='" + groupField + "' and tableid='" + table.getId() + "'");
                            if (null != filed) {
                                dbStr = filed.getDd();
                            }
                            groupCountList = dataCatalogDao.listRecordByTableAndColumn(conn, tname, groupField);
                        }
                        //?????????????????????????????????
                        String typeCountStr = "";
                        if (null != dbStr && !dbStr.equals("") && !dbStr.equals("null") &&
                                null != groupCountList && groupCountList.size() > 0) {
                            typeCountStr = "???";
                            Map<String, String> dbMap = (Map<String, String>) JSON.parse(dbStr);
                            for (Map m : groupCountList) {
                                //amount type
                                String type = (String) m.get("type");
                                String ctype = "????????????";
                                if (type != null) {
                                    ctype = dbMap.get(type);
                                }
                                int amount = Integer.valueOf(m.get("amount") == null ? "" : String.valueOf(m.get("amount")));
                                typeCountStr += "<div class='type-container'><span class='type'>" + ctype + "</span> ?????? <span class='count'>" + amount + "</span>??????</div>";
                            }
                        }
                        //-------------------?????????????????????
                        tableReportCount = dataCatalogDao.getCountByTableName(conn, tname);
                        String s = " ????????? ";
                        if (metaDataDB.getDbtype().equals("1")) {
                            cname = cname.replace("???????????????", "").replace("???????????????", "").replace("???????????????", "").replace("???????????????", "") + " ";
                            tableCountStr += " ";
                            s = " ????????? ";
                        }
                        tableCountStr += "<div class='table-container'><span class='type'>" + cname + "</span>?????????<span class='count'>" + tableReportCount + "</span>" + s;
                        if (!sumCountStr.equals("")) {
                            tableCountStr += sumCountStr;
                        }
                        if (!typeCountStr.equals("")) {
                            tableCountStr += typeCountStr;
                        }
                        tableCountStr += "</div>";
                    }
                }//???????????????
                String flag = metaDataDB.getDbtype().equals("0") ? "??????" : "?????????";
                res = "???<span>" + metaDataDB.getCname() + "</span>???????????? <span class='count'>" + tableCount + "</span>" + flag
                        + ",?????? <span class='count'>" + dbReportCount + "</span>????????????";
                if (!czjbCountStr.equals("")) {//?????????????????????
                    res += "" + czjbCountStr;
                }
                if (!tableCountStr.equals("")) {
                    res += "" + tableCountStr;
                }
            }
        } catch (Exception e) {
            res = "?????????????????????????????????????????????";
            e.printStackTrace();
        } finally {
            dataCatalogDao.closeDBObjects(null, null, conn);
        }
        return res;
    }

    @Override
    public Map getAccountingReport(String datebaseId) {
        //????????????
        Map<String, Object> resMap = new HashMap<String, Object>();
        Connection conn = null;
        try {
            //??????????????????
            List<Map> dataList = new ArrayList<Map>();
            if (datebaseId.equals("1")) {//??????????????????
                String dbStr = "{\"0\":\"???????????????\",\"1\":\"???????????????\"}";
                List<Map> dbList = dataCatalogDao.listRecordByTableAndColumn("AG_WA_METADATA_DB", "dbtype");
                dataList = this.typeListToNameList(dbStr, dbList);
                resMap.put("dbName", "?????????");
                resMap.put("type", "db");
                resMap.put("list", dataList);
            } else {
                //???????????????
                MetaDataDB metaDataDB = dataCatalogDao.getMetaDataDB("select * from AG_WA_METADATA_DB where id='" + datebaseId + "'");
                //????????????
                conn = dataCatalogDao.getConnection(metaDataDB.getUrl());
                //?????????????????????????????????
                String sql = "select t.* from AG_WA_METADATA_TABLE t where t.databaseid='" + datebaseId + "' order by t.lastupdate desc,t.name";
                List<MetaDataTable> tableList = dataCatalogDao.listMetaDataTable(sql);
                int dbReportCount = 0;      //???????????????????????????
                int tableCount = 0;         //????????????
                int tableReportCount;       //???????????????????????????
                List<Map> topGroupList = null;    //????????????????????????
                if (null != tableList && tableList.size() > 0) {
                    tableCount = tableList.size();
                    //??????????????????
                    for (MetaDataTable table : tableList) {
                        String cname = table.getCname();
                        String tname = table.getName();
                        String sumField = table.getSumfield();      //????????????
                        String groupField = table.getGroupfield();  //????????????
                        if (metaDataDB.getDbtype().equals("1")) {
                            cname = cname.replace("???????????????", "").replace("???????????????", "").replace("???????????????", "").replace("???????????????", "") + " ";
                        }
                        //-------------------?????????????????????
                        tableReportCount = dataCatalogDao.getCountByTableName(conn, tname);
                        dbReportCount += tableReportCount;//????????????????????????
                        //----------------------????????????-------------------------------
                        Map sumMap = null;//??????
                        MetaDataField filed = null;
                        double sumValue = 0;       //?????????
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
                        //---------------------????????????----------------------------------
                        List<Map> groupList = null;     //??????????????????
                        //???????????????????????? ????????????
                        if (null != groupField && !groupField.equals("") && !groupField.equals("null")) {
                            filed = dataCatalogDao.getMetaDataField("select dd from AG_WA_METADATA_FIELD where name='" + groupField + "' and tableid='" + table.getId() + "'");
                            String dbStr = filed.getDd();//????????????
                            //???????????????????????????
                            if (groupField.equals("STTP") && tname.equals("ST_STBPRP_B")) {
                                topGroupList = new ArrayList<Map>();
                                Map<String, String> dbMap = (Map<String, String>) JSON.parse(dbStr);
                                List<Map> amountList = dataCatalogDao.listRecordByTableAndColumn(conn, tname, groupField);//??????
                                List<Map> amountList0 = dataCatalogDao.listRecordByTableAndColumn(conn, tname, groupField, " and usfl=0");//??????????????????
                                //???????????????
                                int ylzAmount1 = dataCatalogDao.getCountByTableName(conn, "ST_STBPRP_B",
                                        " and exists(select stcd from ST_STITEM_B  where mnit = 3 and stcd=t.stcd) and usfl=1");
                                int ylzAmount0 = dataCatalogDao.getCountByTableName(conn, "ST_STBPRP_B",
                                        " and exists(select stcd from ST_STITEM_B  where mnit = 3 and stcd=t.stcd) and usfl=0");
                                for (Map m : amountList) {
                                    int total = Integer.valueOf(m.get("amount") == null ? "" : String.valueOf(m.get("amount")));
                                    int amount0 = 0;//?????????????????????
                                    String type = (String) m.get("type");
                                    String ctype = "??????";
                                    if (type != null) {
                                        ctype = dbMap.get(type);
                                    }
                                    if (!type.equals("PP")) {
                                        //????????????????????????
                                        for (Map m0 : amountList0) {
                                            if (m.get("type").equals(m0.get("type"))) {
                                                amount0 = Integer.valueOf(m0.get("amount") == null ? "0" : String.valueOf(m0.get("amount")));
                                                break;
                                            }
                                        }
                                    } else {//?????????????????????
                                        total = ylzAmount1 + ylzAmount0;
                                        amount0 = ylzAmount0;
                                    }
                                    Map groupMap = new HashMap();
                                    groupMap.put("type", ctype);
                                    groupMap.put("amount", total);//?????????
                                    groupMap.put("amount0", amount0);//???????????????
                                    groupMap.put("amount1", (total - amount0));//???????????????
                                    topGroupList.add(groupMap);
                                }
                            } else {
                                //??????????????????????????????
                                groupList = dataCatalogDao.listRecordByTableAndColumn(conn, tname, groupField);
                                groupList = this.typeListToNameList(dbStr, groupList);
                            }
                            //???????????????
                            if (metaDataDB.getCname().equals("???????????????") && cname.equals("??????????????????????????????")) {
                                topGroupList = groupList;
                                groupList = null;
                            }
                        }
                        //??????????????????
                        Map tableMap = new HashMap();
                        tableMap.put("type", cname);
                        tableMap.put("amount", tableReportCount);
                        tableMap.put("sumObj", sumMap);
                        tableMap.put("groupList", groupList);
                        tableMap.put("fieldUnit", table.getFieldunit());
                        dataList.add(tableMap);
                    }//???????????????
                }
                resMap.put("type", "table");
                resMap.put("dbName", metaDataDB.getCname());
                resMap.put("dbType", metaDataDB.getDbtype());
                resMap.put("tableCount", tableCount);
                resMap.put("dbReportCount", dbReportCount);
                resMap.put("list", dataList);
                //?????????????????????
                if (null != topGroupList && topGroupList.size() > 0) {
                    resMap.put("topGroupList", topGroupList);
                }
            }//???????????????
        } catch (Exception e) {
            resMap = null;
            e.printStackTrace();
        } finally {
            dataCatalogDao.closeDBObjects(null, null, conn);
        }
        return resMap;
    }

    /**
     * ???????????? -->???????????? ?????? ????????????
     * [{'PP':45},{'RW':5}]-->[{'?????????':45},{'?????????':5}]
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
                String ctype = "??????";
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
            System.out.println("?????????????????????");
        }
        return dataList;
    }

    /**
     * ?????????????????????
     */
    @Override
    public List<Map> getFromDataNoPage(String tflag, String id, String key) throws Exception {
        return dataCatalogDao.getFromDataNoPage(tflag, id, key);
    }

    /**
     * ????????????
     */
    @Override
    public String getFromData(String tflag, String id, String key, int page, int rows) throws Exception {
        return dataCatalogDao.getFromData(tflag, id, key, page, rows);
    }

    /**
     * ???????????????
     */
    @Override
    public List<Map> getRelateData(String url, String id, String cond) throws Exception {
        return dataCatalogDao.getRelateData(url, id, cond);
    }

    /**
     * ??????????????????
     */
    @Override
    public List<Map> getFieldData(String url, String tableName, String tableId) throws Exception {
        return dataCatalogDao.getFieldData(url, tableName, tableId);
    }

    /**
     * ???????????????
     */
    @Override
    public List<Map> findOrigValue(String url, String tablename, String prifield, String prikey) throws Exception {
        return dataCatalogDao.findOrigValue(url, tablename, prifield, prikey);
    }

    /**
     * ??????????????????
     */
    @Override
    public String getAuthorData(String tflag, String id, String key, int page, int rows, String roleId) throws Exception {
        return dataCatalogDao.getAuthorData(tflag, id, key, page, rows, roleId);
    }

    /**
     * ??????????????????
     */
    @Override
    public String getCheckData(String tflag, String roleName, String con, String roleId, String id, int page, int rows) throws Exception {
        return dataCatalogDao.getCheckData(tflag, roleName, con, roleId, id, page, rows);
    }


    /**
     * ????????????
     */
    @Override
    public List<Map> findData(String url, String tablename, String prifield, String prikey) throws Exception {
        return dataCatalogDao.findData(url, tablename, prifield, prikey);
    }

    /**
     * ??????????????????
     */
    @Override
    public List<Map> getCheckTableInfo(String[] checktypes, String tableid,
                                       String start_time, String end_time, String result) throws Exception {
        return dataCatalogDao.getCheckTableInfo(checktypes, tableid, start_time, end_time, result);
    }

    /**
     * ??????????????????
     */
    @Override
    public String getCheckDataNew(String roleName, String roleId, String checktype, String start_time, String end_time, String tflag, String tableid, int page, int rows, String result) throws Exception {
        return dataCatalogDao.getCheckDataNew(roleName, roleId, checktype, start_time, end_time, tflag, tableid, page, rows, result);
    }

    /**
     * ??????????????????
     */
    @Override
    public String getReportDataNew(String roleName, String roleId, String start_time, String end_time, String checktype, String tflag, String tableid, int page, int rows, String result) throws Exception {
        return dataCatalogDao.getReportDataNew(roleName, roleId, start_time, end_time, checktype, tflag, tableid, page, rows, result);
    }

    /**
     * ????????????
     */
    @Override
    public String saveTemp(String url, String field, String name, String cname) throws Exception {
        return dataCatalogDao.saveTemp(url, field, name, cname);
    }

    /**
     * ??????????????????
     */
    @Override
    public String getTempData(String temp_name, String start_time, String end_time, int page, int rows) throws Exception {
        return dataCatalogDao.getTempData(temp_name, start_time, end_time, page, rows);
    }

    /**
     * ??????
     */
    @Override
    public ResultForm upData(String ids) throws Exception {
        return dataCatalogDao.upData(ids);
    }

    /**
     * ????????????
     */
    @Override
    public ResultForm connectTest(String url) {
        return dataCatalogDao.connectTest(url);
    }

    /**
     * ??????
     */
    @Override
    public ResultForm passCheckDataNo(String type, String view, String loginName, String[] ta, String[] fi, String[] va, String[] idd) throws Exception {
        return dataCatalogDao.passCheckDataNo(type, view, loginName, ta, fi, va, idd);
    }

    /**
     * ??????
     */
    @Override
    public ResultForm passCheckData(String type, String loginName, String[] ta, String[] fi, String[] va, String[] idd) throws Exception {
        return dataCatalogDao.passCheckData(type, loginName, ta, fi, va, idd);
    }

    /*
    * ?????????????????????
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
