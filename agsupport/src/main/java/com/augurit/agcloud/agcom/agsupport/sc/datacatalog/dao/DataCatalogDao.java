package com.augurit.agcloud.agcom.agsupport.sc.datacatalog.dao;

import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.sc.datacatalog.controller.DateJsonValueProcessor;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.common.dbcp.DBHelper;
import com.common.util.Common;
import com.common.util.ConfigProperties;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import oracle.sql.TIMESTAMP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/1.
 */

@Repository
public class DataCatalogDao {
    @Value("${agcloud.inf.load}")
    private Boolean agcloudInfLoad;
    @Value("${agcloud.opus.admin.orgId}")
    private String opusAdminOrgId;
    @Autowired
    private IAgUser iAgUser;

    public List<Map> listDataByPage(Object page) throws Exception {
        String sql1 = "select t.* from wrp_rvr_bsin t";
        String sql = "select * from ag_demo";
        List<Map> list = DBHelper.find("spring.datasource", sql, null);
        List<Map> list2 = DBHelper.find("spring.datasource", sql1, null);
        return list;
    }

    //获取 元数据库 的表记录
    public List<Map> listMetaData_DB() throws Exception {
        String sql = "select * from AG_WA_METADATA_DB ";
        List<Map> list = DBHelper.find("spring.datasource", sql, null);
        return list;
    }

    //获取 元数据表 的表记录
    public List<Map> listMetaData_TABLE() throws Exception {
        String sql = "select * from AG_WA_METADATA_TABLE order by lastupdate desc,name  ";
        List<Map> list = DBHelper.find("spring.datasource", sql, null);
        return list;
    }

    /**
     * 根据条件查询
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public List<Map> listMetaData_SQL(String sql, List lis) throws Exception {
        List<Map> list = DBHelper.find("spring.datasource", sql, lis);
        return list;
    }

    /**
     * 根据条件查询 获得一条数据
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public Map getMetaData_SQL(String sql) throws Exception {
        Map map = DBHelper.findFirst("spring.datasource", sql, null);
        return map;
    }

    public List<MetaDataDB> listMetaDataDB(String sql) throws Exception {
        List<MetaDataDB> list = DBHelper.find("spring.datasource", MetaDataDB.class, sql, null);
        return list;
    }

    public MetaDataDB getMetaDataDB(String sql) throws Exception {
        MetaDataDB data = DBHelper.findFirst("spring.datasource", MetaDataDB.class, sql, null);
        return data;
    }

    public List<MetaDataTable> listMetaDataTable(String sql) throws Exception {
        List<MetaDataTable> list = DBHelper.find("spring.datasource", MetaDataTable.class, sql, null);
        return list;
    }

    public MetaDataTable getMetaDataTable(String sql) throws Exception {
        MetaDataTable data = DBHelper.findFirst("spring.datasource", MetaDataTable.class, sql, null);
        return data;
    }

    public List<MetaDataField> listMetaDataField(String sql) throws Exception {
        List<MetaDataField> list = DBHelper.find("spring.datasource", MetaDataField.class, sql, null);
        return list;
    }

    public MetaDataField getMetaDataField(String sql) throws Exception {
        MetaDataField data = DBHelper.findFirst("spring.datasource", MetaDataField.class, sql, null);
        return data;
    }

    //根据 tableID，  从AG_WA_METADATA_FIELD表 获取记录，如 字段名、 类型等
    public List<Map> listMETADATA_FIELD(String tableID) throws Exception {
        String sql = "select * from AG_WA_METADATA_FIELD where TABLEID='" + tableID + "' and visible='1' order by dispsort ";
        List<Map> list = DBHelper.find("spring.datasource", sql, null);
        return list;
    }

    // 分页 获取 表的记录
    public List<Map> listFromTable(String tablename, int page, int rows) throws Exception {
        int start = (page - 1) * rows + 1;
        int end = page * rows;
        String prikey = "";
        // 查询表的主键
        String sq = "SELECT cu.* FROM user_cons_columns cu, user_constraints au WHERE cu.constraint_name = au.constraint_name AND au.constraint_type = 'P' AND au.table_name = ? ";
        List lii = new ArrayList();
        lii.add(tablename);
        List<Map> lis = listMetaData_SQL(sq, lii);
        if (lis.size() > 0 && lis.get(0).get("column_name") != null) {
            prikey = lis.get(0).get("column_name").toString();
        }
        // 查询图层id
        String layerid = "";
        String s = " select dirlayerid from AG_WA_METADATA_TABLE where  name=? ";
        List li1 = new ArrayList();
        lii.add(tablename);
        List<Map> li = listMetaData_SQL(s, li1);
        if (li != null && li.size() > 0 && li.get(0) != null && li.get(0).get("dirlayerid") != null) {
            layerid = li.get(0).get("dirlayerid").toString();
        }
        // 判断是否存在主键
        List<Map> list = new ArrayList<Map>();
        if ("".equals(prikey)) {
            System.out.println("数据库中表缺失主键，请在数据库中设置表的主键字段！");
            Map map = new HashMap();
            map.put("error", "数据库中表缺失主键，请在数据库中设置表的主键字段！");
            list.add(map);
        } else {
            String sql = "select a1.* from (select t.*,t." + prikey + " as prikey,'" + prikey + "'as priField,'" + layerid + "' as layerid ,'" + tablename + "' as tablename, rownum rn from " + tablename + " t) a1 where rn between " + start + " and " + end;

            list = DBHelper.find("jdbc", sql, null);
        }
        return list;
    }

    //获取 表的记录 总行数
    public int getTotalLineCount(String tablename) throws Exception {
        String sql = "select count(1) totalline from " + tablename;
        List<Map> list = DBHelper.find("jdbc", sql, null);
        Map map = list.get(0);
        return Integer.parseInt(String.valueOf(map.get("totalline")));
    }

    /**
     * 保存数据库
     *
     * @param db
     */
    public void insertMetaDataDB(MetaDataDB db) throws Exception {
        String[] pks = {"id"};
        DBHelper.save("spring.datasource", "AG_WA_METADATA_DB", pks, Arrays.asList(db));
    }

    /**
     * 更新数据库
     *
     * @param db
     */
    public void updateMetaDataDB(MetaDataDB db) throws Exception {
        List<Object> values = new ArrayList<Object>();
        String set = "";
        if (db.getName() != null && !"".equals(db.getName())) {
            values.add(db.getName());
            set = set + "," + " name=? ";
        }
        if (db.getCname() != null && !"".equals(db.getCname())) {
            values.add(db.getCname());
            set = set + "," + " cname=? ";
        }
        if (db.getUrl() != null && !"".equals(db.getUrl())) {
            values.add(db.getUrl());
            set = set + "," + " url=? ";
        }
        if (db.getDbtype() != null && !"".equals(db.getDbtype())) {
            values.add(db.getDbtype());
            set = set + "," + " dbtype=? ";
        }
        values.add(db.getEdittime());
        set = set + "," + " EDITTIME=? ";
        // 设置set
        if (!"".equals(set)) {
            set = set.substring(1);
        } else {
            set = " 1=1 ";
        }
        values.add(db.getId());
        String sql = " update AG_WA_METADATA_DB set " + set + " where id=? ";
        DBHelper.executeUpdate("spring.datasource", sql, values);
    }

    /**
     * 保存表
     *
     * @param table
     * @throws Exception
     */
    public int insertMetaDataTable(MetaDataTable table) throws Exception {
        String[] pks = {"id"};
        int i = DBHelper.save("spring.datasource", "AG_WA_METADATA_TABLE", pks, Arrays.asList(table));
        return i;
    }

    /**
     * 更新表
     *
     * @param table
     * @throws Exception
     */
    public void updateMetaDataTable(MetaDataTable table) throws Exception {
        List<Object> values = new ArrayList<Object>();
        String set = "";
        if (table.getName() != null && !"".equals(table.getName())) {
            values.add(table.getName());
            set = set + "," + " name=? ";
        }
        if (table.getCname() != null && !"".equals(table.getCname())) {
            values.add(table.getCname());
            set = set + "," + " cname=? ";
        }
        if (table.getDatabaseid() != null && !"".equals(table.getDatabaseid())) {
            values.add(table.getDatabaseid());
            set = set + "," + " databaseid=? ";
        }
        if (table.getLastupdate() != null && !"".equals(table.getLastupdate())) {
            // values.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parseObject(table.getLastupdate()));
            set = set + "," + " lastupdate=to_date('" + table.getLastupdate() + "','yyyy-MM-dd HH24:mi:ss') ";
        }
        // 判断主键
        String sq = "SELECT cu.* FROM user_cons_columns cu, user_constraints au WHERE cu.constraint_name = au.constraint_name AND au.constraint_type = 'P' AND au.table_name = ? ";
        List lii = new ArrayList();
        lii.add(table.getName());
        List<Map> list = listMetaData_SQL(sq, lii);
        if (list.size() > 0 && list.get(0).get("column_name") != null) {
            values.add(list.get(0).get("column_name").toString());
        } else {
            values.add("");
        }
        set = set + "," + " priid=? ";
        if (table.getDirlayerid() != null && !"".equals(table.getDirlayerid())) {
            values.add(table.getDirlayerid());
            set = set + "," + " dirlayerid=? ";
        }
        if (table.getLayer_config() != null && !"".equals(table.getLayer_config())) {
            values.add(table.getLayer_config());
            set = set + "," + " layer_config=? ";
        }
        if (table.getDistfield() != null && !"".equals(table.getDistfield())) {
            values.add(table.getDistfield());
            set = set + "," + " distfield=? ";
        }
        if (table.getSearfield() != null && !"".equals(table.getSearfield())) {
            values.add(table.getSearfield());
            set = set + "," + " searfield=? ";
        }
        if (table.getSumfield() != null && !"".equals(table.getSumfield())) {
            values.add(table.getSumfield());
            set = set + "," + " sumfield=? ";
        }
        if (table.getGroupfield() != null && !"".equals(table.getGroupfield())) {
            values.add(table.getGroupfield());
            set = set + "," + " groupfield=? ";
        }
        if (table.getFieldunit() != null && !"".equals(table.getFieldunit())) {
            values.add(table.getFieldunit());
            set = set + "," + " FIELDUNIT=? ";
        }
        values.add(table.getId());
        // 设置set
        if (!"".equals(set)) {
            set = set.substring(1);
        } else {
            set = " 1=1 ";
        }
        String sql = " update AG_WA_METADATA_TABLE set " + set + " where id=? ";
        DBHelper.executeUpdate("spring.datasource", sql, values);
    }

    /**
     * 更新字段
     *
     * @param field
     */
    public void updateMetaDataField(MetaDataField field) throws Exception {
        List<Object> values = new ArrayList<Object>();
        String set = "";
        if (field.getName() != null && !"".equals(field.getName())) {
            values.add(field.getName());
            set = set + "," + " name=? ";
        }
        if (field.getCname() != null && !"".equals(field.getCname())) {
            values.add(field.getCname());
            set = set + "," + " cname=? ";
        }
        if (field.getDescription() != null && !"".equals(field.getDescription())) {
            values.add(field.getDescription());
            set = set + "," + " description=? ";
        }
        if (field.getType() != null && !"".equals(field.getType())) {
            values.add(field.getType());
            set = set + "," + " type=? ";
        }
        if (field.getConstraint() != null && !"".equals(field.getConstraint())) {
            values.add(field.getConstraint());
            set = set + "," + " constraint=? ";
        }
        if (field.getUnit() != null && !"".equals(field.getUnit())) {
            values.add(field.getUnit());
            set = set + "," + " unit=? ";
        }
        if (field.getDd() != null && !"".equals(field.getDd())) {
            values.add(field.getDd());
            set = set + "," + " dd=? ";
        }
        if (field.getEditable() != null && !"".equals(field.getEditable())) {
            values.add(field.getEditable());
            set = set + "," + " editable=? ";
        }
        if (field.getVisible() != null && !"".equals(field.getVisible())) {
            values.add(field.getVisible());
            set = set + "," + " visible=? ";
        }
        if (field.getTableid() != null && !"".equals(field.getTableid())) {
            values.add(field.getTableid());
            set = set + "," + " tableid=? ";
        }
        if (field.getDispsort() != null && !"".equals(field.getDispsort())) {
            values.add(field.getDispsort());
            set = set + "," + " dispsort=? ";
        }
        if (field.getPrikey() != null && !"".equals(field.getPrikey())) {
            values.add(field.getPrikey());
            set = set + "," + " prikey=? ";
        }
        values.add(field.getId());
        // 设置set
        if (!"".equals(set)) {
            set = set.substring(1);
        } else {
            set = " 1=1 ";
        }
        String sql = " update AG_WA_METADATA_FIELD set " + set + " where id=? ";
        DBHelper.executeUpdate("spring.datasource", sql, values);
    }

    /**
     * 保存字段
     *
     * @param field
     */
    public void insertMetaDataField(MetaDataField field) throws Exception {
        String[] pks = {"id"};
        DBHelper.save("spring.datasource", "AG_WA_METADATA_FIELD", pks, Arrays.asList(field));
    }

    /**
     * 删除字段
     *
     * @param id
     */
    public void delMetaDataField(String id) throws Exception {
        String sql = " delete from AG_WA_METADATA_FIELD where id=? ";
        List values = new ArrayList();
        values.add(id);
        DBHelper.executeUpdate("spring.datasource", sql, values);
    }

    /**
     * 删除表
     *
     * @param id
     */
    public void delMetaDataTable(String id) throws Exception {
        // 删除表字段
        String sql = " delete from AG_WA_METADATA_FIELD where tableid=? ";
        List values = new ArrayList();
        values.add(id);
        DBHelper.executeUpdate("spring.datasource", sql, values);

        // 删除表
        String sql2 = " delete from AG_WA_METADATA_TABLE where id=? ";
        List values2 = new ArrayList();
        values2.add(id);
        DBHelper.executeUpdate("spring.datasource", sql2, values2);
    }

    /**
     * 删除数据库
     *
     * @param id
     * @throws Exception
     */
    public void delMetaDataDB(String id) throws Exception {
        // 查询数据库的表
        String sq = " select * from AG_WA_METADATA_TABLE  where databaseid=? ";
        List lii = new ArrayList();
        lii.add(id);
        List<Map> list = listMetaData_SQL(sq, lii);
        for (int i = 0; i < list.size(); i++) {
            String tableId = list.get(i).get("ID").toString();
            // 删除表
            delMetaDataTable(tableId);
        }

        // 删除数据库
        String sql = " delete from AG_WA_METADATA_DB where id=? ";
        List values = new ArrayList();
        values.add(id);
        DBHelper.executeUpdate("spring.datasource", sql, values);
    }

    /**
     * 删除数据库与表关联
     *
     * @param dbId
     * @throws Exception
     */
    public void delRelateData(String dbId) throws Exception {
        String sql = " delete from AG_WA_METADATA_TABLE where databaseid=? ";
        List values = new ArrayList();
        values.add(dbId);
        DBHelper.executeUpdate("spring.datasource", sql, values);
    }

    /**
     * 删除同步字段
     *
     * @param tableId
     */
    public void delFieldData(String tableId) throws Exception {
        String sql = " delete from AG_WA_METADATA_FIELD where tableid=? ";
        List values = new ArrayList();
        values.add(tableId);
        DBHelper.executeUpdate("spring.datasource", sql, values);
    }

    /**
     * 更新sql
     *
     * @param sql
     * @param li
     */
    public int update_SQL(String sql, List li) throws Exception {
        int i = DBHelper.executeUpdate("spring.datasource", sql, li);
        return i;
    }

    /**
     * 根据表名查询
     *
     * @param id
     * @return
     */
    public List<Map> listMetaData_DBByName(String id) throws Exception {
        List<Map> list = new ArrayList<Map>();
        if ("WRP_".equals(id)) {
            String sql = "select * from AG_WA_METADATA_DB where name='SWGC'";
            list = DBHelper.find("spring.datasource", sql, null);
        }
        return list;
    }

    /**
     * 根据表名查询
     *
     * @param id
     * @return
     */
    public List<Map> listMetaData_TABLEByName(String id) throws Exception {
        List<Map> list = new ArrayList<Map>();
        if ("WRP_".equals(id)) {
            String sql = "select * from AG_WA_METADATA_TABLE where name like '" + id + "%' order by lastupdate desc,name  ";
            list = DBHelper.find("spring.datasource", sql, null);
        }
        return list;
    }

    /**
     * 插入属性授权
     *
     * @param ag
     * @return
     */
    public int insertAuthorData(AgUserData ag) throws Exception {
        String sql = " insert into AG_USER_DATA(id,tableid,userid,distid,ischeck,isedit) values('" + ag.getId() + "','" + ag.getTableid() + "','" + ag.getRoleid() + "','" + ag.getDistid() + "','" + ag.getIscheck() + "','" + ag.getIsedit() + "') ";
        int i = DBHelper.executeUpdate("spring.datasource", sql, null);
        return i;
    }

    /**
     * 查询属性授权
     *
     * @param sql
     * @return
     */
    public List<Map> listAuthorSQL(String sql, List lis) throws Exception {
        List<Map> list = DBHelper.find("spring.datasource", sql, lis);
        return list;
    }

    /**
     * 删除属性授权
     *
     * @param id
     * @return
     */
    public int delAuthorData(String id) throws Exception {
        String sql = " delete from ag_user_data where id=? ";
        List lii = new ArrayList();
        lii.add(id);
        int i = DBHelper.executeUpdate("spring.datasource", sql, lii);
        return i;
    }

    /**
     * 根据条件查询
     *
     * @param tablename
     * @param page
     * @param rows
     * @param cond
     * @return
     * @throws Exception
     */
    public List<Map> listFromTableByCondition(String tablename, int page, int rows, String cond) throws Exception {
        List<Map> list = new ArrayList<Map>();

        // String sql = "select * from  " + tablename;
        int start = (page - 1) * rows + 1;
        int end = page * rows;
        // String sql = "select a1.* from (select t.*, rownum rn from " + tablename + " t) a1 where rn between " + start + " and "  + end;
        String prikey = "";

        // 查询图层id
        String layerid = "";
        String s = " select dirlayerid from AG_WA_METADATA_TABLE where  name=? ";
        List lii = new ArrayList();
        lii.add(tablename);
        List<Map> li = listMetaData_SQL(s, lii);
        if (li != null && li.size() > 0 && li.get(0) != null && li.get(0).get("dirlayerid") != null) {
            layerid = li.get(0).get("dirlayerid").toString();
        }

        // 查询url
        String sql = " select * from AG_WA_METADATA_DB where id=(SELECT databaseid from AG_WA_METADATA_TABLE where name=? and rownum=1) ";
        List li1 = new ArrayList();
        li1.add(tablename);
        String url = listMetaData_SQL(sql, li1).get(0).get("url").toString();

        // 原始jdbc
        Connection con = null;
        Statement stm = null;
        ResultSet rs = null;
        try {
            String[] ur = url.split("\\|");
            String[] userName = ur[1].split("/");
            String[] hosts = ur[2].split("/");
            String db = ur[0];
            String user = userName[0];
            String pwd = userName[1];
            String ip = hosts[0];
            String instance = hosts[1];
            if ("oracle".equals(db)) {
                String driver = "oracle.jdbc.driver.OracleDriver";
                Class.forName(driver);
                String urll = "jdbc:oracle:thin:@" + ip + ":" + instance;
                con = DriverManager.getConnection(urll, user, pwd);

                stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                // 查询表的主键
                String sq = "SELECT cu.* FROM user_cons_columns cu, user_constraints au WHERE cu.constraint_name = au.constraint_name AND au.constraint_type = 'P' AND au.table_name = '" + tablename + "' ";
                System.out.println("SQL:" + sq);
                rs = stm.executeQuery(sq);
                if (rs.first()) {
                    prikey = rs.getString("column_name").toString();
                }


                // 判断是否存在主键
                if ("".equals(prikey)) {
                    System.out.println("数据库中表缺失主键，请在数据库中设置表的主键字段！");
                    Map map = new HashMap();
                    map.put("error", "数据库中表缺失主键，请在数据库中设置表的主键字段！");
                    list.add(map);
                } else {
                    // 查询数据
                    sql = "select a1.* from (select t.*,t." + prikey + " as prikey,'" + prikey + "'as priField,'" + layerid + "' as layerid ,'" + tablename + "' as tablename, rownum rn from " + tablename + " t where " + cond + ") a1 where rn between " + start + " and " + end;
                    // stm=con.prepareStatement(sql);
                    System.out.println("SQL:" + sql);
                    rs = stm.executeQuery(sql);
                    ResultSetMetaData da = rs.getMetaData();

                    // 返回结果
                    while (rs.next()) {
                        Map map = new HashMap();
                        for (int i = 1; i <= da.getColumnCount(); i++) {
                            String c = da.getColumnName(i);
                            if (!"SHAPE".equals(c.toUpperCase())) {
                                String v = rs.getString(c);
                                map.put(c.toLowerCase(), v);
                            }
                        }
                        list.add(map);
                    }
                }
            } else if ("mysql".equals(db)) {
                String driver = "com.mysql.jdbc.Driver";
                Class.forName(driver);
                String urll = "jdbc:mysql://" + ip + "/" + instance;
                con = DriverManager.getConnection(urll, user, pwd);

                stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String sq = "SELECT c.COLUMN_NAME FROM  INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS t,  INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS c WHERE t.TABLE_NAME = c.TABLE_NAME  AND t.TABLE_SCHEMA = '" + instance + "'  AND c.TABLE_SCHEMA = '" + instance + "'  and t.TABLE_NAME='" + tablename + "'  AND t.CONSTRAINT_TYPE = 'PRIMARY KEY' ";
                System.out.println("SQL:" + sq);
                rs = stm.executeQuery(sq);
                if (rs.first()) {
                    prikey = rs.getString("column_name").toString();
                }


                // 判断是否存在主键
                if ("".equals(prikey)) {
                    System.out.println("数据库中表缺失主键，请在数据库中设置表的主键字段！");
                    Map map = new HashMap();
                    map.put("error", "数据库中表缺失主键，请在数据库中设置表的主键字段！");
                    list.add(map);
                } else {
                    // 查询数据
                    sql = "select a1.* from (select t.*,t." + prikey + " as prikey,'" + prikey + "'as priField,'" + layerid + "' as layerid ,'" + tablename + "' as tablename  from " + tablename + " t where " + cond + ") a1   limit " + (start - 1) + " , " + end;
                    // stm=con.prepareStatement(sql);
                    System.out.println("SQL:" + sql);
                    rs = stm.executeQuery(sql);
                    ResultSetMetaData da = rs.getMetaData();

                    // 返回结果
                    while (rs.next()) {
                        Map map = new HashMap();
                        for (int i = 1; i <= da.getColumnCount(); i++) {
                            String c = da.getColumnName(i);
                            if (!"SHAPE".equals(c.toUpperCase())) {
                                String v = rs.getString(c);
                                map.put(c.toLowerCase(), v);
                            }
                        }
                        list.add(map);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

		/*
        String sql = "select a1.* from (select t.*,t." + prikey + " as prikey,'" + prikey + "'as priField,'" + layerid + "' as layerid ,'" + tablename + "' as tablename, rownum rn from " + tablename + " t where " + con + ") a1 where rn between " + start + " and " + end;

        List<Map> list = DBHelper.find("jdbc", sql, null);
        */
        //Pager e = PageUtil.initPager(page);
        return list;
    }

    /**
     * 根据条件查询
     *
     * @param tablename
     * @param cond
     * @return
     * @throws Exception
     */
    public int getTotalLineCountByCondition(String tablename, String cond) throws Exception {
        int i = 0;
        // 查询url
        String sql = " select * from AG_WA_METADATA_DB where id=(SELECT databaseid from AG_WA_METADATA_TABLE where name=? and rownum=1) ";
        List lii = new ArrayList();
        lii.add(tablename);
        String url = listMetaData_SQL(sql, lii).get(0).get("url").toString();
        // 原始jdbc
        Connection con = null;
        Statement stm = null;
        ResultSet rs = null;
        try {
            String[] ur = url.split("\\|");
            String[] userName = ur[1].split("/");
            String[] hosts = ur[2].split("/");
            String db = ur[0];
            String user = userName[0];
            String pwd = userName[1];
            String ip = hosts[0];
            String instance = hosts[1];
            if ("oracle".equals(db)) {
                String driver = "oracle.jdbc.driver.OracleDriver";
                Class.forName(driver);
                String urll = "jdbc:oracle:thin:@" + ip + ":" + instance;
                con = DriverManager.getConnection(urll, user, pwd);
                stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                sql = "select count(1) totalline from " + tablename + " where  " + cond + " ";
                System.out.println("SQL:" + sql);
                rs = stm.executeQuery(sql);
                if (rs.first()) {
                    i = Integer.parseInt(rs.getString("totalline").toString());
                }
            } else if ("mysql".equals(db)) {
                String driver = "com.mysql.jdbc.Driver";
                Class.forName(driver);
                String urll = "jdbc:mysql://" + ip + "/" + instance;
                con = DriverManager.getConnection(urll, user, pwd);
                stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                sql = "select count(1) totalline from " + tablename + " where  " + cond + " ";
                System.out.println("SQL:" + sql);
                rs = stm.executeQuery(sql);
                if (rs.first()) {
                    i = Integer.parseInt(rs.getString("totalline").toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return i;
    }

    /**
     * 删除操作
     *
     * @param del
     * @return
     */
    public int deleteSQL(String del) throws Exception {
        int i = DBHelper.executeUpdate("spring.datasource", del, null);
        return i;
    }

    /**
     * 保存信息表
     *
     * @param mo
     * @return
     */
    public int saveMetaDataModify(MetaDataModify mo) throws Exception {
        String[] pks = {"id"};
        int i = DBHelper.save("spring.datasource", "AG_WA_METADATA_MODIFY", pks, Arrays.asList(mo));
        return i;
    }

    /**
     * 更新信息表
     *
     * @param mo
     * @return
     */
    public int upMetaDataModify(MetaDataModify mo) throws Exception {
        List<Object> values = new ArrayList<Object>();
        String set = "";
        if (mo.getTableName() != null && !"".equals(mo.getTableName())) {
            values.add(mo.getTableName());
            set = set + "," + " table_name=? ";
        }
        if (mo.getPriField() != null && !"".equals(mo.getPriField())) {
            values.add(mo.getPriField());
            set = set + "," + " pri_field=? ";
        }
        if (mo.getPriValue() != null && !"".equals(mo.getPriValue())) {
            values.add(mo.getPriValue());
            set = set + "," + " pri_value=? ";
        }
        if (mo.getFieldDescription() != null && !"".equals(mo.getFieldDescription())) {
            values.add(mo.getFieldDescription());
            set = set + "," + " field_description=? ";
        }
        if (mo.getOrigValue() != null && !"".equals(mo.getOrigValue())) {
            values.add(mo.getOrigValue());
            set = set + "," + " orig_value=? ";
        }
        if (mo.getModiValue() != null && !"".equals(mo.getModiValue())) {
            values.add(mo.getModiValue());
            set = set + "," + " modi_value=? ";
        }
        if (mo.getRemark() != null && !"".equals(mo.getRemark())) {
            values.add(mo.getRemark());
            set = set + "," + " remark=? ";
        }
        values.add(mo.getIsModi());
        set = set + "," + " is_modi=? ";
        values.add(mo.getIsDel());
        set = set + "," + " is_del=? ";
        values.add(mo.getIsAdd());
        set = set + "," + " is_add=? ";
        if (mo.getLastUpdate() != null && !"".equals(mo.getLastUpdate())) {
            values.add(mo.getLastUpdate());
            set = set + "," + " last_update=? ";
        }
        if (mo.getOperaName() != null && !"".equals(mo.getOperaName())) {
            values.add(mo.getOperaName());
            set = set + "," + " opera_name=? ";
        }
        // 设置set
        if (!"".equals(set)) {
            set = set.substring(1);
        } else {
            set = " 1=1 ";
        }
        String sql = " update AG_WA_METADATA_MODIFY set " + set + " where id='" + mo.getId() + "' ";
        int i = DBHelper.executeUpdate("spring.datasource", sql, values);
        return i;
    }

    public Object getMetaDataByTableName(String tableName) throws Exception {
        String sql = "select * from AG_WA_METADATA_TABLE where NAME = '" + tableName + "'";
        return DBHelper.find("spring.datasource", sql, null);
    }

    /**
     * 查询所有字段
     *
     * @param tableID
     * @return
     */
    public List<Map> listMETADATA_FIELDAll(String tableID) throws Exception {
        String sql = "select * from AG_WA_METADATA_FIELD where TABLEID='" + tableID + "'  order by prikey,dispsort ";
        List<Map> list = DBHelper.find("spring.datasource", sql, null);
        return list;
    }

    /**
     * 生成目录
     *
     * @param temp
     * @return
     */
    public int insertMetaDataTemp(MetaDataTemp temp) throws Exception {
        String[] pks = {"id"};
        int i = DBHelper.save("spring.datasource", "AG_WA_METADATA_TEMP", pks, Arrays.asList(temp));
        return i;
    }

    /**
     * 获取角色信息
     *
     * @param lii
     * @return
     */
    public List<Map> getRoleInfo(List lii) throws Exception {
        List<Map> result;
        if (agcloudInfLoad.booleanValue()) {
            result = iAgUser.getRolesByUserName((String)lii.get(0));
            String roleIDs = "";
            for (Map role : result) {
                roleIDs += "," + role.get("roleId").toString();
            }
            roleIDs = roleIDs.substring(1);
            List<Map> roleArr = iAgUser.listRoleByRoleIds(roleIDs);
            List<Map> result2 = new ArrayList<Map>();

            for(Map roleInfo : roleArr) {
                Map<String ,String> map = new HashMap<>();
                map.put("id",roleInfo.get("roleId").toString());
                map.put("name",roleInfo.get("roleName").toString());
                result2.add(map);
            }
            return result2;

        } else {
            String sq = " select r.id,r.name from ag_role r,ag_user_role ur,ag_user u where u.LOGIN_NAME=? and u.id=ur.USER_ID and ur.ROLE_ID=r.id ";
            result = listAuthorSQL(sq, lii);
        }
        return result;
    }

    /**
     * 获取编辑权限的表
     *
     * @param roleId
     * @return
     * @throws Exception
     */
    public List<Map> getTableByEditRole(String roleId) throws Exception {
        String sql = " select tableid from ag_user_data where userid in (" + roleId + ") and isedit='1' ";
        return listAuthorSQL(sql, null);
    }

    /**
     * 查询记录
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getRecords(List li) throws Exception {
        String sql = " select * from AG_WA_METADATA_MODIFY where table_name=( select name from  AG_WA_METADATA_TABLE where id=?) and (IS_MODI in ('-1','1','2') or IS_del  in ('-1','1','2')  or IS_add  in ('-1','1','2') ) ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 获取审核权限的表
     *
     * @param roleId
     * @return
     */
    public List<Map> getTableByCheckRole(String roleId) throws Exception {
        String sql = " select tableid from ag_user_data where userid in (" + roleId + ") and ischeck='1' ";
        return listAuthorSQL(sql, null);
    }

    /**
     * 获取待审核的记录
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getCheckRecord(List li) throws Exception {
        String sql = " select * from AG_WA_METADATA_MODIFY where table_name=( select name from  AG_WA_METADATA_TABLE where id=?) and (IS_MODI  in ('0')  or IS_del  in ('0')  or IS_add  in ('0') ) ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 获取表
     *
     * @param roleId
     * @return
     */
    public List<Map> getTableByUser(String roleId) throws Exception {
        String sql = " select tableid from ag_user_data where userid in (" + roleId + ") ";
        return listAuthorSQL(sql, null);
    }

    /**
     * 获取表的字段
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getFieldByTable(List li) throws Exception {
        String sql = " select * from AG_WA_METADATA_FIELD where tableid=(select id from AG_WA_METADATA_TABLE where name=?  and ROWNUM=1  ) order by dispsort ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 查询表信息
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getTableInfoByName(List li) throws Exception {
        String sql = " select distfield,id from AG_WA_METADATA_TABLE where name=? ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 获取地区
     *
     * @param roleId
     * @param li
     * @return
     */
    public List<Map> getDisById(String roleId, List li) throws Exception {
        String sql = " select distid from ag_user_data where userid in (" + roleId + ") and tableid=? and isedit='1' ";
        return listAuthorSQL(sql, li);
    }

    /**
     * 查询所有表
     *
     * @return
     * @throws Exception
     */
    public List<Map> getAllTable() throws Exception {
        String sql = " select name from AG_WA_METADATA_TABLE ";
        return listMetaData_SQL(sql, null);
    }

    /**
     * 获取DB信息
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getDBInfoById(List li) throws Exception {
        String sql = " select * from AG_WA_METADATA_DB where id=? ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 获取DB信息
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getDBByName(List li) throws Exception {
        String sql = " select * from AG_WA_METADATA_DB where id=(SELECT databaseid from AG_WA_METADATA_TABLE where name=? ) ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 获取CName
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getCNameById(List li) throws Exception {
        String sql = " select name,cname,dd,type from AG_WA_METADATA_FIELD where tableid=(select id from AG_WA_METADATA_TABLE where name=? )  order by dispsort  ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 获取字段信息
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getFieldInfo(List li) throws Exception {
        String sql = " select name,cname,type from AG_WA_METADATA_FIELD where tableid=(select id from AG_WA_METADATA_TABLE where name=? )  ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 获取组织信息
     *
     * @param li
     * @return
     */
    public List<Map> getOrgInfoByUser(List li) throws Exception {
        String sql = " select * from ag_org WHERE id in(select org_id from ag_org_user where user_id=(select id from ag_user where login_name=? ) ) ";
        return listAuthorSQL(sql, li);
    }

    /**
     * 获取模板信息
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getTempInfo(List li) throws Exception {
        String sql = " select * from AG_WA_METADATA_TEMP where upper(table_name)=(select name from AG_WA_METADATA_TABLE where id=? ) ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 获取表停止
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getTableInfo(List li) throws Exception {
        String sql = " select *  from AG_WA_METADATA_TABLE where id=? ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 获取角色信息
     *
     * @param li
     * @return
     */
    public List<Map> getRoleInfoByUser(List li) throws Exception {
        String sql = " select r.* from ag_role r where r.id in (select r.ROLE_ID from ag_user_role r,ag_user u where u.Id=r.USER_ID and u.LOGIN_NAME=? ) and r.NAME in ('超级管理员','平台管理员')  ";
        return listAuthorSQL(sql, li);
    }

    /**
     * 获取统计信息
     *
     * @return
     * @throws Exception
     */
    public List<Map> findDataRecords() throws Exception {
        String sql = " select (select t.cname from AG_WA_METADATA_TABLE t where t.NAME=m.TABLE_NAME) tableName,count(m.TABLE_NAME) numberAll,m.OPERA_NAME from AG_WA_METADATA_MODIFY m where m.IS_ADD='0' or m.IS_DEL='0' or m.IS_MODI='0' GROUP BY m.TABLE_NAME,m.OPERA_NAME ";
        return listMetaData_SQL(sql, null);
    }

    /**
     * 获取用户姓名
     *
     * @param li
     * @return
     */
    public List<Map> getUserName(List li) throws Exception {
        String sql = " select user_name from ag_user where login_name=?  ";
        return listAuthorSQL(sql, li);
    }

    /**
     * 获取表单信息
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getTableData(List li) throws Exception {
        String sql = " select * from AG_WA_METADATA_TABLE where name=? and databaseid=? ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 根据表名查找
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getTableByName(List li) throws Exception {
        String sql = "SELECT id from AG_WA_METADATA_TABLE where name=? ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 获取字段中文
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getFieldCname(List li) throws Exception {
        String sql = " select name,cname from AG_WA_METADATA_FIELD where tableid=(select id from AG_WA_METADATA_TABLE where name=? and rownum=1 ) ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 查询修改值
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getModiValue(List li) throws Exception {
        String sql = " select decode(modi_value,'',orig_value,modi_value) value from AG_WA_METADATA_MODIFY where  table_name=? and  pri_field=? and pri_value=? ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 获取模板
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getTempInfoById(List li) throws Exception {
        String sql = " select * from AG_WA_METADATA_TEMP where id=? ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 删除模板
     *
     * @param li
     * @return
     */
    public int delTempById(List li) throws Exception {
        String sql = " delete from AG_WA_METADATA_TEMP where id=? ";
        return update_SQL(sql, li);
    }

    /**
     * 获取修改表
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getModiByKey(List li) throws Exception {
        String sql = " select * from AG_WA_METADATA_MODIFY where table_name=? and pri_field=? and pri_value=? ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 获取用户
     *
     * @param li
     * @return
     */
    public List<Map> getUserDataByUser(List li) throws Exception {
        String sql = " select * from ag_user_data where  tableid=? and userid=? and isedit!='1' ";
        return listAuthorSQL(sql, li);
    }

    /**
     * 根据地区获取用户
     *
     * @param li
     * @return
     */
    public List<Map> getUserDataByDist(List li) throws Exception {
        String sql = " select * from ag_user_data where  tableid=? and userid=? and distid=? and isedit='1' ";
        return listAuthorSQL(sql, li);
    }

    /**
     * 获取统计信息
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getCheckData(List li) throws Exception {
        String sql = " select  (case when is_add='-1' then 'add' end||case when is_del='-1' then 'del'  end||case when is_modi='-1' then 'modi'  end) checktype,m.* from AG_WA_METADATA_MODIFY m where table_name=? and pri_field=? and pri_value=? ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 根据id获取表名
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getTableById(List li) throws Exception {
        String sql = " select * from AG_WA_METADATA_TABLE where id=? ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 根据tableid获取字段
     *
     * @param li
     * @return
     * @throws Exception d
     */
    public List<Map> getFieldByTableId(List li) throws Exception {
        String sql = " select * from AG_WA_METADATA_FIELD where name=? and tableid=? ";
        return listMetaData_SQL(sql, li);
    }

    /**
     * 根据id获取DB
     *
     * @param li
     * @return
     * @throws Exception
     */
    public List<Map> getDBById(List li) throws Exception {
        String sql = " select * from AG_WA_METADATA_DB where id=(SELECT databaseid from AG_WA_METADATA_TABLE where id=? and rownum=1 ) ";
        return listMetaData_SQL(sql, li);
    }


    /**
     * 根据配置的信息获得驱动连接
     *
     * @param url
     * @return
     * @throws Exception
     */
    public Connection getConnection(String url) throws Exception {
        // 原始jdbc
        Connection con = null;

        String[] ur = url.split("\\|");
        String[] userName = ur[1].split("/");
        String[] hosts = ur[2].split("/");
        String db = ur[0];
        String user = userName[0];
        String pwd = userName[1];
        String ip = hosts[0];
        String instance = hosts[1];
        if ("oracle".equals(db)) {
            String driver = "oracle.jdbc.driver.OracleDriver";
            Class.forName(driver);
            String urll = "jdbc:oracle:thin:@" + ip + ":" + instance;
            con = DriverManager.getConnection(urll, user, pwd);
        } else if ("mysql".equals(db)) {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);
            String urll = "jdbc:mysql://" + ip + "/" + instance;
            con = DriverManager.getConnection(urll, user, pwd);
        }

        return con;
    }

    public void closeDBObjects(ResultSet resultSet, Statement statement,
                               Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Exception e) {
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 根据表名 条件 统计个数
     *
     * @param tableName
     * @return List
     */
    public int getCountByTableName(Connection conn, String tableName) throws Exception {
        return this.getCountByTableName(conn, tableName, null);
    }

    public int getCountByTableName(Connection conn, String tableName, String condition) {
        int amount = 0;
        String sql = "select count(0) amount  from " + tableName + " t where 1=1 ";
        if (null != condition && !condition.equals("")) {
            sql += condition;
        }
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sql);
            // 返回结果
            if (rs.first()) {
                amount = Integer.parseInt(rs.getString("amount").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeDBObjects(rs, stm, null);
        }
        return amount;
    }

    public int getCountByTableName(String tableName, String condition) throws Exception {
        int amount = 0;
        String sql = "select count(0) amount  from " + tableName + " t where 1=1 ";
        if (null != condition && !condition.equals("")) {
            sql += condition;
        }
        Map m = DBHelper.findFirst("jdbc", sql, null);
        if (null != m) {
            amount = Integer.valueOf(m.get("amount") == null ? "0" : String.valueOf(m.get("amount")));
        }
        return amount;
    }

    /**
     * 根据表名 列名 分组统计个数
     *
     * @param tableName
     * @param columnName
     * @return List
     */
    public List<Map> listRecordByTableAndColumn(Connection conn, String tableName, String columnName) {
        return this.listRecordByTableAndColumn(conn, tableName, columnName, null);
    }

    public List<Map> listRecordByTableAndColumn(Connection conn, String tableName, String columnName, String condition) {
        List<Map> resultList = new ArrayList<Map>();
        String sql = "select count(0) amount ," + columnName + " type from " + tableName + " where 1=1 ";
        if (null != condition && !condition.equals("")) {
            sql += condition;
        }
        sql += "group by " + columnName + " order by " + columnName;
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sql);
            ResultSetMetaData da = rs.getMetaData();
            // 返回结果
            while (rs.next()) {
                Map map = new HashMap();
                for (int i = 1; i <= da.getColumnCount(); i++) {
                    String c = da.getColumnName(i);
                    String v = rs.getString(c);
                    map.put(c.toLowerCase(), v);
                }
                resultList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeDBObjects(rs, stm, null);
        }

        return resultList;
    }

    public List<Map> listRecordByTableAndColumn(String tableName, String columnName) throws Exception {
        return this.listRecordByTableAndColumn(tableName, columnName, null);
    }

    public List<Map> listRecordByTableAndColumn(String tableName, String columnName, String condition) throws Exception {
        String sql = "select count(0) amount ," + columnName + " type from " + tableName + " where 1=1 ";
        if (null != condition && !condition.equals("")) {
            sql += condition;
        }
        sql += "group by " + columnName;
        return DBHelper.find("jdbc", sql, null);
    }

    /**
     * 根据表名统计个数
     */
    public Map countRecordByTable(Connection conn, String tableName) {
        Map resultMap = new HashMap();
        String sql = "select count(0) " + tableName + " from " + tableName;
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // 返回结果
            rs = stm.executeQuery(sql);
            ResultSetMetaData da = rs.getMetaData();
            // 返回结果
            while (rs.next()) {
                for (int i = 1; i <= da.getColumnCount(); i++) {
                    String c = da.getColumnName(i);
                    String v = rs.getString(c);
                    resultMap.put(c.toLowerCase(), v);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeDBObjects(rs, stm, null);
        }
        return resultMap;
    }

    public Map countRecordByTable(String tableName) throws Exception {
        return DBHelper.findFirst("jdbc", "select count(0) " + tableName + " from " + tableName, null);
    }

    /**
     * 根据表名和分类统计个数
     */
    public Map countRecordByTableAndColumn(String tableName, String columnName) throws Exception {
        return countRecordByTableAndColumn(tableName, columnName, null);
    }

    public Map countRecordByTableAndColumn(String tableName, String columnName, String where) throws Exception {
        String sql = "select count(0) amount ," + columnName + " type from " + tableName + " where 1=1 ";
        Map resultMap = null;
        if (where != null && !"".equals(where)) {
            sql += " and " + where;
        }
        sql += "group by " + columnName + " order by " + columnName;
        //分组的统计数
        List<Map> rsList = DBHelper.find("jdbc", sql, null);
        if (rsList != null && rsList.size() > 0) {
            resultMap = new HashMap();
            for (Map m : rsList) {
                resultMap.put(String.valueOf(m.get("type")), Integer.valueOf(m.get("amount") == null ? "" : String.valueOf(m.get("amount"))));
            }
        }
        return resultMap;
    }

    /**
     * 拼接统计 表中 数据 的个数
     */
    public String concatSql(String table) throws Exception {
        return concatSql(table, null, null);
    }

    public String concatSql(String table, String where) throws Exception {
        return concatSql(table, where, null);
    }

    public String concatSql(String table, String where, String orderBy) throws Exception {
        String rtSql = "";
        String sql = " select wm_concat('(select count(0) from ' || NAME || ') ' || Name ) sqlstr from " + table + " where 1=1 ";
        if (null != where && !"".equals(where)) {
            sql += " and " + where;
        }
        if (null != orderBy && !"".equals(orderBy)) {
            sql += " order by  " + orderBy;
        }
        Map m = DBHelper.findFirst("jdbc", sql, null);
        if (m != null) {
            rtSql = m.get("sqlstr").toString();
        }
        return rtSql;
    }

    /**
     * 根据表名 和 字段 统计 累加值
     */
    public double sumByTableAndColumn(Connection conn, String tableName, String cname) {
        double sumValue = 0d;
        String sql = "select sum(" + cname + ") sumvalue  from " + tableName;
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sql);
            // 返回结果
            if (rs.first()) {
                sumValue = Double.parseDouble(rs.getString("sumvalue").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeDBObjects(rs, stm, null);
        }
        return sumValue;
    }

    public int sumByTableAndColumn(String tableName, String cname) throws Exception {
        int sumValue = 0;
        Map m = DBHelper.findFirst("jdbc", "select sum(" + cname + ") sumvalue  from " + tableName, null);
        if (null != m) {
            sumValue = Integer.valueOf(m.get("sumvalue") == null ? "" : String.valueOf(m.get("sumvalue")));
        }
        return sumValue;
    }

    /**
     * 获取未分页数据
     *
     * @param tflag
     * @param id
     * @param key
     * @return
     * @throws Exception
     */
    public List<Map> getFromDataNoPage(String tflag, String id, String key) throws Exception {
        List<Map> data = new ArrayList<Map>();
        String sql = "";
        if ("top".equals(tflag)) {  // 顶级
            // 数据库
            if (key != null && !"".equals(key)) {
                sql = " select id,name,cname,url,'top' as tflag from AG_WA_METADATA_DB where name like '%" + key + "%' or  cname like '%" + key + "%' ";
            } else {
                sql = " select id,name,cname,url,'top' as tflag from AG_WA_METADATA_DB ";
            }
            // 判断所有表
            if ("All".equals(id)) {
                sql = "select 'db' as tflag,t.id,t.name,t.cname,to_char(t.lastupdate,'yyyy-MM-dd HH24:mi:ss') lastupdate,t.priid,t.dirlayerid,t.distfield,t.searfield,t.GROUPFIELD,t.SUMFIELD,t.FIELDUNIT from AG_WA_METADATA_TABLE t order by t.lastupdate desc,t.name  ";
            }
            data = listMetaData_SQL(sql, null);
        } else if ("db".equals(tflag)) { // 数据库
            if (id != null && !"".equals(id)) {
                if (key != null && !"".equals(key)) {
                    sql = "select 'db' as tflag,t.id,t.name,t.cname,to_char(t.lastupdate,'yyyy-MM-dd HH24:mi:ss') lastupdate,t.priid,t.dirlayerid,t.distfield,t.searfield,t.GROUPFIELD,t.SUMFIELD,t.FIELDUNIT,(select cname from AG_WA_METADATA_DB where id='" + id + "') db_name,'" + id + "' db_id from AG_WA_METADATA_TABLE t where t.databaseid='" + id + "' and (t.name like '%" + key + "%' or  t.cname like '%" + key + "%' )  order by t.lastupdate desc,t.name   ";
                } else {
                    sql = "select 'db' as tflag,t.id,t.name,t.cname,to_char(t.lastupdate,'yyyy-MM-dd HH24:mi:ss') lastupdate,t.priid,t.dirlayerid,t.distfield,t.searfield,t.GROUPFIELD,t.SUMFIELD,t.FIELDUNIT,(select cname from AG_WA_METADATA_DB where id='" + id + "') db_name,'" + id + "' db_id from AG_WA_METADATA_TABLE t where t.databaseid='" + id + "' order by t.lastupdate desc,t.name  ";
                }
            } else {
                sql = "select 'db' as tflag,t.id,t.name,t.cname,to_char(t.lastupdate,'yyyy-MM-dd HH24:mi:ss') lastupdate,t.priid,t.dirlayerid,t.distfield,t.searfield,t.GROUPFIELD,t.SUMFIELD,t.FIELDUNIT from AG_WA_METADATA_TABLE t order by t.lastupdate desc,t.name  ";
            }
            data = listMetaData_SQL(sql, null);
        } else if ("table".equals(tflag)) { // 表
            if (key != null && !"".equals(key)) {
                sql = " select 'table' as tflag,f.id,f.name,f.cname,f.description,f.type,f.constraint,f.unit,f.dd,decode(f.editable,'1','是','否') as editable,decode(f.visible,'1','是','否') as visible,f.dispsort,decode(f.prikey,'1','是','否') as prikey,(select cname from AG_WA_METADATA_TABLE where id='" + id + "') table_name,'" + id + "' table_id from AG_WA_METADATA_FIELD f where f.tableid='" + id + "'  and (f.name like '%" + key + "%' or  f.cname like '%" + key + "%' )  ";
            } else {
                sql = " select 'table' as tflag,f.id,f.name,f.cname,f.description,f.type,f.constraint,f.unit,f.dd,decode(f.editable,'1','是','否') as editable,decode(f.visible,'1','是','否') as visible,f.dispsort,decode(f.prikey,'1','是','否') as prikey,(select cname from AG_WA_METADATA_TABLE where id='" + id + "') table_name,'" + id + "' table_id from AG_WA_METADATA_FIELD f where f.tableid='" + id + "' ";
            }
            data = listMetaData_SQL(sql, null);
        }
        return data;
    }

    /**
     * 获取数据
     *
     * @param tflag
     * @param id
     * @param key
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    public String getFromData(String tflag, String id, String key, int page, int rows) throws Exception {
        List<Map> data = new ArrayList<Map>();
        String sql = "";
        if ("top".equals(tflag)) {  // 顶级
            // 数据库
            if (key != null && !"".equals(key)) {
                sql = " select id,name,cname,url,dbtype,to_char(EDITTIME,'yyyy-MM-dd HH24:mi:ss') edittime,'top' as tflag from AG_WA_METADATA_DB where name like '%" + key + "%' or  cname like '%" + key + "%' ";
            } else {
                sql = " select id,name,cname,url,dbtype,to_char(EDITTIME,'yyyy-MM-dd HH24:mi:ss') edittime,'top' as tflag from AG_WA_METADATA_DB ";
            }
            data = listMetaData_SQL(sql, null);
        } else if ("db".equals(tflag)) { // 数据库
            if (key != null && !"".equals(key)) {
                sql = "select 'db' as tflag,t.id,t.name,t.cname,to_char(t.lastupdate,'yyyy-MM-dd HH24:mi:ss') lastupdate,t.priid,t.dirlayerid,t.layer_config as layerconfig,t.distfield,t.searfield,t.GROUPFIELD,t.SUMFIELD,(select cname from AG_WA_METADATA_DB where id='" + id + "') db_name,'" + id + "' db_id from AG_WA_METADATA_TABLE t where t.databaseid='" + id + "' and (t.name like '%" + key + "%' or  t.cname like '%" + key + "%' )  order by t.lastupdate desc,t.name   ";
            } else {
                sql = "select 'db' as tflag,t.id,t.name,t.cname,to_char(t.lastupdate,'yyyy-MM-dd HH24:mi:ss') lastupdate,t.priid,t.dirlayerid,t.layer_config as layerconfig,t.distfield,t.searfield,t.GROUPFIELD,t.SUMFIELD,(select cname from AG_WA_METADATA_DB where id='" + id + "') db_name,'" + id + "' db_id from AG_WA_METADATA_TABLE t where t.databaseid='" + id + "' order by t.lastupdate desc,t.name  ";
            }
            data = listMetaData_SQL(sql, null);
        } else if ("table".equals(tflag)) { // 表
            if (key != null && !"".equals(key)) {
                sql = " select 'table' as tflag,f.id,f.name,f.cname,f.description,f.type,f.constraint,f.unit,f.dd,decode(f.editable,'1','是','否') as editable,decode(f.visible,'1','是','否') as visible,f.dispsort,decode(f.prikey,'1','是','否') as prikey,(select cname from AG_WA_METADATA_TABLE where id='" + id + "') table_name,'" + id + "' table_id from AG_WA_METADATA_FIELD f where f.tableid='" + id + "'  and (f.name like '%" + key + "%' or  f.cname like '%" + key + "%' ) order by f.dispsort  ";
            } else {
                sql = " select 'table' as tflag,f.id,f.name,f.cname,f.description,f.type,f.constraint,f.unit,f.dd,decode(f.editable,'1','是','否') as editable,decode(f.visible,'1','是','否') as visible,f.dispsort,decode(f.prikey,'1','是','否') as prikey,(select cname from AG_WA_METADATA_TABLE where id='" + id + "') table_name,'" + id + "' table_id from AG_WA_METADATA_FIELD f where f.tableid='" + id + "'  order by f.dispsort   ";
            }
            data = listMetaData_SQL(sql, null);
        }
        // 查询数据
        int start = (page - 1) * rows + 1;
        int end = page * rows;
        String sq = " select a1.* from ( select t.*,rownum rn  from ( " + sql + " ) t ) a1  where rn between " + start + " and " + end;
        List<Map> listTable = listMetaData_SQL(sq, null);
        // 查询总数
        int iCount = 0;
        if (data != null) {
            iCount = data.size();
        }
        // 时间格式
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(TIMESTAMP.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        String str = "{\"rows\":" + JSONArray.fromObject(listTable, config).toString() + ",\"total\":" + iCount + "}";
        return str;
    }

    /**
     * 获取关联表
     *
     * @param url
     * @param id
     * @param cond
     * @return
     * @throws SQLException
     */
    public List<Map> getRelateData(String url, String id, String cond) throws SQLException {
        List<Map> data = new ArrayList<Map>();
        String sql = "";
        // 原始jdbc
        Connection con = null;
        Statement stm = null;
        ResultSet rs = null;
        try {
            String[] ur = url.split("\\|");
            String[] userName = ur[1].split("/");
            String[] hosts = ur[2].split("/");
            String db = ur[0];
            String user = userName[0];
            String pwd = userName[1];
            String ip = hosts[0];
            String instance = hosts[1];
            if ("oracle".equals(db)) {
                String driver = "oracle.jdbc.driver.OracleDriver";
                Class.forName(driver);
                String urll = "jdbc:oracle:thin:@" + ip + ":" + instance;
                con = DriverManager.getConnection(urll, user, pwd);
                if (con != null) {
                    stm = con.createStatement();
                    sql = "  SELECT t.table_name as name,c.comments as cname,'" + id + "' as dbid  FROM USER_TABLES t left join user_tab_comments c on c.table_name=t.table_name  where " + cond;
                    System.out.println("SQL:" + sql);
                    rs = stm.executeQuery(sql);
                    ResultSetMetaData da = rs.getMetaData();
                    // 返回结果
                    while (rs.next()) {
                        Map map = new HashMap();
                        for (int i = 1; i <= da.getColumnCount(); i++) {
                            String c = da.getColumnName(i);
                            if (!"SHAPE".equals(c.toUpperCase())) {
                                String v = rs.getString(c);
                                map.put(c.toLowerCase(), v);
                            }
                        }
                        // 设置选中
                        map.put("chk", "");
                        data.add(map);
                    }
                }
            } else if ("mysql".equals(db)) {
                String driver = "com.mysql.jdbc.Driver";
                Class.forName(driver);
                String urll = "jdbc:mysql://" + ip + "/" + instance;
                con = DriverManager.getConnection(urll, user, pwd);
                if (con != null) {
                    stm = con.createStatement();
                    sql = " select * from ( select t.table_name as name,t.TABLE_COMMENT as cname,'" + id + "' as dbid from information_schema.tables t where t.table_schema='" + instance + "' and t.table_type='base table' and " + cond + " ) k ";
                    System.out.println("SQL:" + sql);
                    rs = stm.executeQuery(sql);
                    ResultSetMetaData da = rs.getMetaData();

                    // 返回结果
                    while (rs.next()) {
                        Map map = new HashMap();
                        for (int i = 1; i <= da.getColumnCount(); i++) {
                            String c = da.getColumnName(i);
                            if (!"SHAPE".equals(c.toUpperCase())) {
                                String v = rs.getString(c.toLowerCase());
                                map.put(c.toLowerCase(), v);
                            }
                        }
                        // 设置选中
                        map.put("chk", "");
                        data.add(map);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return data;
    }

    /**
     * 获取同步字段
     *
     * @param url
     * @param tableName
     * @param tableId
     * @return
     * @throws SQLException
     */
    public List<Map> getFieldData(String url, String tableName, String tableId) throws SQLException {
        List<Map> data = new ArrayList<Map>();
        // 原始jdbc
        Connection con = null;
        Statement stm = null;
        ResultSet rs = null;
        tableName = tableName.toUpperCase();
        try {
            String[] ur = url.split("\\|");
            String[] userName = ur[1].split("/");
            String[] hosts = ur[2].split("/");
            String db = ur[0];
            String user = userName[0];
            String pwd = userName[1];
            String ip = hosts[0];
            String instance = hosts[1];
            if ("oracle".equals(db)) {
                String driver = "oracle.jdbc.driver.OracleDriver";
                Class.forName(driver);
                String urll = "jdbc:oracle:thin:@" + ip + ":" + instance;
                con = DriverManager.getConnection(urll, user, pwd);

                stm = con.createStatement();
                String sql = "SELECT '" + tableId + "' as tableid, cu.column_name name, cu.comments description, tc.data_type type, tc.nullable constraint, ( SELECT UC.constraint_name FROM user_constraints uc LEFT JOIN user_cons_columns cc  on uc.table_name=CC.table_name and UC.constraint_name=CC.constraint_name WHERE uc.constraint_type = 'P' AND uc.TABLE_NAME = '" + tableName + "'  and CC.column_name=cu.column_name ) prikey FROM user_col_comments cu LEFT JOIN	user_tab_columns tc on cu.table_name = tc.table_name and cu.column_name=tc.column_name where tc.table_name = '" + tableName + "' ";
                System.out.println("SQL:" + sql);
                rs = stm.executeQuery(sql);
                ResultSetMetaData da = rs.getMetaData();

                // 返回结果
                while (rs.next()) {
                    Map map = new HashMap();
                    for (int i = 1; i <= da.getColumnCount(); i++) {
                        String c = da.getColumnName(i);
                        if (!"SHAPE".equals(c.toUpperCase())) {
                            String v = rs.getString(c);
                            map.put(c.toLowerCase(), v);
                        }
                    }

                    // 是否选中
                    List li3 = new ArrayList();
                    li3.add(rs.getString("name"));
                    li3.add(tableId);
                    List<Map> d = getFieldByTableId(li3);
                    if (d != null && d.size() > 0) {
                        map.put("chk", d.get(0).get("id"));
                    } else {
                        map.put("chk", null);
                    }
                    data.add(map);
                }
            } else if ("mysql".equals(db)) {
                String driver = "com.mysql.jdbc.Driver";
                Class.forName(driver);
                String urll = "jdbc:mysql://" + ip + "/" + instance;
                con = DriverManager.getConnection(urll, user, pwd);

                stm = con.createStatement();
                String sql = " SELECT	DISTINCT * FROM	(SELECT	 '" + tableId + "' AS tableid,	 tc.column_name NAME, tc.column_comment description, tc.data_type type, ( case tc.IS_NULLABLE WHEN 'YES' then 'Y' WHEN 'NO' THEN 'N' END ) as  'CONSTRAINT', ( SELECT	 c.COLUMN_NAME	 FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS t, INFORMATION_SCHEMA.KEY_COLUMN_USAGE  c WHERE t.table_name = '" + tableName + "'  and t.TABLE_NAME = c.TABLE_NAME AND t.TABLE_SCHEMA = '" + instance + "' AND c.TABLE_SCHEMA = '" + instance + "' AND t.CONSTRAINT_TYPE = 'PRIMARY KEY' and c.COLUMN_NAME=tc.COLUMN_NAME ) prikey	 FROM information_schema.COLUMNS tc		WHERE  tc.table_name = '" + tableName + "'	) k ";
                System.out.println("SQL:" + sql);
                rs = stm.executeQuery(sql);
                ResultSetMetaData da = rs.getMetaData();

                // 返回结果
                while (rs.next()) {
                    Map map = new HashMap();
                    for (int i = 1; i <= da.getColumnCount(); i++) {
                        String c = da.getColumnName(i);
                        if (!"SHAPE".equals(c.toUpperCase())) {
                            String v = rs.getString(c);
                            map.put(c.toLowerCase(), v);
                        }
                    }
                    // 是否选中
                    List li3 = new ArrayList();
                    li3.add(rs.getString("name"));
                    li3.add(tableId);
                    List<Map> d = getFieldByTableId(li3);
                    if (d != null && d.size() > 0) {
                        map.put("chk", d.get(0).get("id"));
                    } else {
                        map.put("chk", null);
                    }
                    data.add(map);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return data;
    }

    /**
     * 查询原来值
     *
     * @param url
     * @param tablename
     * @param prifield
     * @param prikey
     * @return
     * @throws SQLException
     */
    public List<Map> findOrigValue(String url, String tablename, String prifield, String prikey) throws SQLException {
        List<Map> dat = new ArrayList<Map>();
        // 原始jdbc
        Connection con = null;
        Statement stm = null;
        ResultSet rs = null;
        try {
            String[] ur = url.split("\\|");
            String[] userName = ur[1].split("/");
            String[] hosts = ur[2].split("/");
            String db = ur[0];
            String user = userName[0];
            String pwd = userName[1];
            String ip = hosts[0];
            String instance = hosts[1];
            if ("oracle".equals(db)) {
                String driver = "oracle.jdbc.driver.OracleDriver";
                Class.forName(driver);
                String urll = "jdbc:oracle:thin:@" + ip + ":" + instance;
                con = DriverManager.getConnection(urll, user, pwd);
                stm = con.createStatement();
                String sq = " select * from " + tablename + " where " + prifield + "='" + prikey + "' ";
                System.out.println("SQL:" + sq);
                rs = stm.executeQuery(sq);
                ResultSetMetaData da = rs.getMetaData();
                // 返回结果
                while (rs.next()) {
                    Map map = new HashMap();
                    for (int i = 1; i <= da.getColumnCount(); i++) {
                        String c = da.getColumnName(i);
                        if (!"SHAPE".equals(c.toUpperCase())) {
                            String v = rs.getString(c);
                            map.put(c.toLowerCase(), v);
                        }
                    }
                    dat.add(map);
                }

            } else if ("mysql".equals(db)) {
                String driver = "com.mysql.jdbc.Driver";
                Class.forName(driver);
                String urll = "jdbc:mysql://" + ip + "/" + instance;
                con = DriverManager.getConnection(urll, user, pwd);
                stm = con.createStatement();
                String sq = " select * from " + tablename + " where " + prifield + "='" + prikey + "' ";
                System.out.println("SQL:" + sq);
                rs = stm.executeQuery(sq);
                ResultSetMetaData da = rs.getMetaData();
                // 返回结果
                while (rs.next()) {
                    Map map = new HashMap();
                    for (int i = 1; i <= da.getColumnCount(); i++) {
                        String c = da.getColumnName(i);
                        if (!"SHAPE".equals(c.toUpperCase())) {
                            String v = rs.getString(c);
                            map.put(c.toLowerCase(), v);
                        }
                    }
                    dat.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return dat;
    }

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
     * @throws Exception
     */
    public String getAuthorData(String tflag, String id, String key, int page, int rows, String roleId) throws Exception {
        List<Map> data = new ArrayList<Map>();
        String tablename = "";
        String sql = "";
        // 角色查询
        String con = "";
        if (roleId != null && !"".equals(roleId)) {
            con = " ( userid in (" + roleId + ") ) ";
        } else {
            con = " ( 1=1 ) ";
        }
        // 查询表id
        String ke = "(";
        if (key != null && !"".equals(key)) {
            sql = " select id from AG_WA_METADATA_TABLE where cname like '%" + key + "%' ";
            List<Map> tab = listMetaData_SQL(sql, null);
            if (tab != null && tab.size() > 0) {
                for (int i = 0; i < tab.size(); i++) {
                    if ("(".equals(ke)) {
                        ke += " d.tableid='" + tab.get(i).get("id").toString() + "' ";
                    } else {
                        ke += " or d.tableid='" + tab.get(i).get("id").toString() + "' ";
                    }
                }
            } else {
                ke += " 1=2 ";
            }
        } else {
            ke += " 1=1 ";
        }
        ke += " ) ";
        if ("table".equals(tflag)) { // 表
            if (key != null && !"".equals(key)) {
                sql = " select d.id,d.tableid,d.ischeck,d.isedit,case	 d.distid when	'All' 	then	'全区'	when	'440103' 	then	'荔湾区'	when	'440104' then		'越秀区'	when	'440105' then	'海珠区'	when	'440106' then		'天河区' when		'440111' then		'白云区'	when	'440112'	then	'黄埔区' when		'440113'	then	'番禺区' when		'440114' then	'花都区' when		'440115'	then	'南沙区'	when	'440183' then		'增城区' when		'440184' then		'从化区'	else	'' end	 AS distname,d.userid,(select u.name from ag_role u where u.id=d.userid ) as rolename from ag_user_data d where  " + con + " and d.tableid='" + id + "' and  " + ke + "  order by d.tableid,d.userid,d.distid,d.ischeck,d.isedit  ";
            } else {
                sql = " select d.id,d.tableid,d.ischeck,d.isedit,case	 d.distid when	'All' 	then	'全区'	when	'440103' 	then	'荔湾区'	when	'440104' then		'越秀区'	when	'440105' then	'海珠区'	when	'440106' then		'天河区' when		'440111' then		'白云区'	when	'440112'	then	'黄埔区' when		'440113'	then	'番禺区' when		'440114' then	'花都区' when		'440115'	then	'南沙区'	when	'440183' then		'增城区' when		'440184' then		'从化区'	else	'' end	 AS distname,d.userid,(select u.name from ag_role u where u.id=d.userid ) as rolename from ag_user_data d where " + con + " and d.tableid='" + id + "'  order by d.tableid,d.userid,d.distid,d.ischeck,d.isedit  ";
            }
            data = listAuthorSQL(sql, null);
        } else {
            if (key != null && !"".equals(key)) {
                sql = " select d.id,d.tableid,d.ischeck,d.isedit,case	 d.distid when	'All' 	then	'全区'	when	'440103' 	then	'荔湾区'	when	'440104' then		'越秀区'	when	'440105' then	'海珠区'	when	'440106' then		'天河区' when		'440111' then		'白云区'	when	'440112'	then	'黄埔区' when		'440113'	then	'番禺区' when		'440114' then	'花都区' when		'440115'	then	'南沙区'	when	'440183' then		'增城区' when		'440184' then		'从化区'	else	'' end	 AS distname,d.userid,(select u.name from ag_role u where u.id=d.userid ) as rolename from ag_user_data d where " + con + " and  " + ke + "   order by d.tableid,d.userid,d.distid,d.ischeck,d.isedit  ";
            } else {
                sql = " select d.id,d.tableid,d.ischeck,d.isedit,case	 d.distid when	'All' 	then	'全区'	when	'440103' 	then	'荔湾区'	when	'440104' then		'越秀区'	when	'440105' then	'海珠区'	when	'440106' then		'天河区' when		'440111' then		'白云区'	when	'440112'	then	'黄埔区' when		'440113'	then	'番禺区' when		'440114' then	'花都区' when		'440115'	then	'南沙区'	when	'440183' then		'增城区' when		'440184' then		'从化区'	else	'' end	 AS distname,d.userid,(select u.name from ag_role u where u.id=d.userid ) as rolename from ag_user_data d where " + con + "  order by d.tableid,d.userid,d.distid,d.ischeck,d.isedit  ";
            }
            data = listAuthorSQL(sql, null);
        }
        // 查询表单数据
        int start = (page - 1) * rows;
        int end = rows;
        String sq = "";
        if (ConfigProperties.getByKey("spring.datasource.driver-class-name").indexOf("oracle") == -1) {
            sq = "select a.* from ( " + sql + " ) a LIMIT " + start + " , " + end + ""; // mysql
        } else {
            sq = " select a1.* from ( select t.*,rownum rn  from ( " + sql + " ) t ) a1  where rn between " + start + " and " + end; // oracle
        }
        List<Map> listTable = listAuthorSQL(sq, null);
        if (listTable != null && listTable.size() > 0) {
            // 设置表名
            for (int i = 0; i < listTable.size(); i++) {
                Map map = listTable.get(i);
                String tableid = map.get("tableid").toString();
                // 查询表名
                List<Map> dat = new ArrayList<Map>();
                List lii = new ArrayList();
                lii.add(tableid);
                dat = getTableById(lii);
                String json = JsonUtils.toJson(dat);
                JSONArray array = JSONArray.fromObject(json);
                if (array != null && array.size() > 0) {
                    JSONObject o = JSONObject.fromObject(array.get(0));
                    tablename = o.get("cname").toString();
                }
                listTable.get(i).put("tablename", tablename);
            }
        }
        // 查询总数
        int iCount = 0;
        if (data != null) {
            iCount = data.size();
        }
        // 时间格式
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(TIMESTAMP.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        String str = "{\"rows\":" + JSONArray.fromObject(listTable, config).toString() + ",\"total\":" + iCount + "}";
        return str;
    }

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
     * @throws Exception
     */
    public String getCheckData(String tflag, String roleName, String con, String roleId, String id, int page, int rows) throws Exception {
        String sql = "";
        List<Map> data = new ArrayList<Map>();
        if ("超级管理员".equals(roleName)) {
            // 元限定条件
        } else {
            // 判断是否拥有权限
            List<Map> tables = getTableByCheckRole(roleId);
            if (tables != null && tables.size() > 0) {
                // 获取权限的表
                String ta = "";
                for (int i = 0; i < tables.size(); i++) {
                    if ("".equals(ta)) {
                        ta += " table_name=(select name from AG_WA_METADATA_TABLE where id='" + tables.get(i).get("tableid").toString() + "') ";
                    } else {
                        ta += " or table_name=(select name from AG_WA_METADATA_TABLE where id='" + tables.get(i).get("tableid").toString() + "') ";
                    }
                }
                con += " and (" + ta + ") ";
            } else {
                con += " and 1=2 "; // 没有表时
            }
        }
        // 查询
        if (tflag != null && "table".equals(tflag)) { // 表
            sql = " select m.* from AG_WA_METADATA_MODIFY m where (" + con + ") and m.table_name=( select a.name from AG_WA_METADATA_TABLE a where a.id='" + id + "'   ) ";
        } else {
            sql = " select m.* from AG_WA_METADATA_MODIFY m where (" + con + ") ";
        }
        data = listMetaData_SQL(sql, null);
        // 查询数据
        int start = (page - 1) * rows + 1;
        int end = page * rows;
        String sq = " select a1.* from ( select t.*,rownum rn  from ( " + sql + " ) t ) a1  where rn between " + start + " and " + end;
        List<Map> listTable = listMetaData_SQL(sq, null);
        // 查询总数
        int iCount = 0;
        if (data != null) {
            iCount = data.size();
        }
        // 时间格式
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(TIMESTAMP.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        String str = "{\"rows\":" + JSONArray.fromObject(listTable, config).toString() + ",\"total\":" + iCount + "}";
        return str;
    }

    /**
     * 查询原值
     *
     * @param url
     * @param tablename
     * @param prifield
     * @param prikey
     * @return
     * @throws SQLException
     */
    public List<Map> findData(String url, String tablename, String prifield, String prikey) throws SQLException {
        List<Map> data = new ArrayList<Map>();
        // 原始jdbc
        Connection con = null;
        Statement stm = null;
        ResultSet rs = null;
        try {
            String[] ur = url.split("\\|");
            String[] userName = ur[1].split("/");
            String[] hosts = ur[2].split("/");
            String db = ur[0];
            String user = userName[0];
            String pwd = userName[1];
            String ip = hosts[0];
            String instance = hosts[1];
            if ("oracle".equals(db)) {
                String driver = "oracle.jdbc.driver.OracleDriver";
                Class.forName(driver);
                String urll = "jdbc:oracle:thin:@" + ip + ":" + instance;
                con = DriverManager.getConnection(urll, user, pwd);
                stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String sql = " select t.*, '" + tablename + "' as tablename,'" + prifield + "' as prifield ,'" + prikey + "' as prikey  from " + tablename + " t where t." + prifield + "='" + prikey + "' ";
                System.out.println("SQL:" + sql);
                rs = stm.executeQuery(sql);
                ResultSetMetaData da = rs.getMetaData();
                // 返回结果
                while (rs.next()) {
                    Map map = new HashMap();
                    for (int i = 1; i <= da.getColumnCount(); i++) {
                        String c = da.getColumnName(i);
                        if (!"SHAPE".equals(c.toUpperCase())) {
                            String v = rs.getString(c);
                            map.put(c.toLowerCase(), v);
                        }
                    }
                    data.add(map);
                }
            } else if ("mysql".equals(db)) {
                String driver = "com.mysql.jdbc.Driver";
                Class.forName(driver);
                String urll = "jdbc:mysql://" + ip + "/" + instance;
                con = DriverManager.getConnection(urll, user, pwd);
                stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String sql = " select t.*, '" + tablename + "' as tablename,'" + prifield + "' as prifield ,'" + prikey + "' as prikey  from " + tablename + " t where t." + prifield + "='" + prikey + "' ";
                System.out.println("SQL:" + sql);
                rs = stm.executeQuery(sql);
                ResultSetMetaData da = rs.getMetaData();
                // 返回结果
                while (rs.next()) {
                    Map map = new HashMap();
                    for (int i = 1; i <= da.getColumnCount(); i++) {
                        String c = da.getColumnName(i);
                        if (!"SHAPE".equals(c.toUpperCase())) {
                            String v = rs.getString(c);
                            map.put(c.toLowerCase(), v);
                        }
                    }
                    data.add(map);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return data;
    }

    /**
     * 获取审核信息
     *
     * @param checktypes
     * @param tableid
     * @param start_time
     * @param end_time
     * @param result
     * @return
     * @throws Exception
     */
    public List<Map> getCheckTableInfo(String[] checktypes, String tableid, String start_time, String end_time, String result) throws Exception {
        List<Map> data = new ArrayList<Map>();
        String sql = "";
        String con = "";
        if (checktypes.length == 2) {
            String va = checktypes[1];
            if ("1".equals(va)) {  // 审核记录
                va = "1','2";
            } else if ("All".equals(va)) { // 全部
                va = "-1','0','1','2";
            }
            // 判断结果类型
            if (!"".equals(result) && result != null) {
                va = result;
            }
            if (!"".equals(con)) {
                if ("All".equals(checktypes[0])) { // 全部审核类型
                    con += " and  ( is_add in ( '" + va + "') or is_del in ( '" + va + "') or is_modi in ( '" + va + "') ) ";
                } else {
                    con += " and  " + checktypes[0] + " in ( '" + va + "')";
                }
            } else {
                if ("All".equals(checktypes[0])) {
                    con += "  ( is_add in ( '" + va + "') or is_del in ( '" + va + "') or is_modi in ( '" + va + "') ) ";
                } else {
                    con += " " + checktypes[0] + " in ( '" + va + "')";
                }
            }
        }
        // 获取时间范围
        String sq = " select max(last_update) end_time,min(last_update) start_time from AG_WA_METADATA_MODIFY where (" + con + ") and table_name=(select name from AG_WA_METADATA_TABLE where id='" + tableid + "') ";
        List<Map> dat = listMetaData_SQL(sq, null);
        if (start_time == null || "".equals(start_time)) {
            if (dat != null && dat.size() > 0 && dat.get(0).get("start_time") != null) {
                start_time = dat.get(0).get("start_time").toString();
            }
        }
        if (end_time == null || "".equals(end_time)) {
            if (dat != null && dat.size() > 0 && dat.get(0).get("end_time") != null) {
                end_time = dat.get(0).get("end_time").toString();
            }
        }
        if (start_time != null && !"".equals(start_time)) {
            if (!"".equals(con)) {
                con += " and  to_date(last_update,'yyyy-MM-dd HH24:mi:ss')>=to_date('" + start_time + "','yyyy-MM-dd HH24:mi:ss')";
            } else {
                con += " to_date(last_update,'yyyy-MM-dd HH24:mi:ss')>=to_date('" + start_time + "','yyyy-MM-dd HH24:mi:ss')";
            }
        }
        if (end_time != null && !"".equals(end_time)) {
            if (!"".equals(con)) {
                con += " and  to_date(last_update,'yyyy-MM-dd HH24:mi:ss')<=to_date('" + end_time + "','yyyy-MM-dd HH24:mi:ss')";
            } else {
                con += " to_date(last_update,'yyyy-MM-dd HH24:mi:ss')<=to_date('" + end_time + "','yyyy-MM-dd HH24:mi:ss')";
            }
        }
        if ("".equals(con)) {
            con = " 1=1 ";
        }
        if (tableid != null && !"".equals(tableid)) {
            if ("All".equals(checktypes[0])) {
                sql = " select  '" + start_time + "' as start_time,'" + end_time + "' as end_time, NVL(SUM (CASE IS_ADD WHEN '-1' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '-1' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '-1' THEN 1 END),0) AS All11,NVL(SUM (CASE IS_ADD WHEN '0' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '0' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '0' THEN 1 END),0) AS All0,NVL(SUM (CASE IS_ADD WHEN '1' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '1' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '1' THEN 1 END),0) AS All1,NVL(SUM (CASE IS_ADD WHEN '2' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '2' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '2' THEN 1 END),0) AS All2 from AG_WA_METADATA_MODIFY where table_name=(select name from AG_WA_METADATA_TABLE where id='" + tableid + "') and (" + con + ")  GROUP BY table_name ";
            } else {
                sql = " select  '" + start_time + "' as start_time,'" + end_time + "' as end_time,  sum(case IS_ADD when '-1' then 1 end) as add11,sum(case IS_ADD when '0' then 1 end) as add0,sum(case IS_ADD when '1' then 1 end) as add1,sum(case IS_ADD when '2' then 1 end) as add2,sum(case IS_del when '-1' then 1 end) as del11,sum(case IS_del when '0' then 1 end) as del0,sum(case IS_del when '1' then 1 end) as del1,sum(case IS_del when '2' then 1 end) as del2,sum(case IS_MODI when '-1' then 1 end) as mod11,sum(case IS_MODI when '0' then 1 end) as mod0,sum(case IS_MODI when '1' then 1 end) as mod1,sum(case IS_MODI when '2' then 1 end) as mod2 from AG_WA_METADATA_MODIFY where table_name=(select name from AG_WA_METADATA_TABLE where id='" + tableid + "') and (" + con + ")  GROUP BY table_name ";
            }
        }
        data = listMetaData_SQL(sql, null);
        return data;
    }

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
    public String getCheckDataNew(String roleName, String roleId, String checktype, String start_time, String end_time, String tflag, String tableid, int page, int rows, String result) throws Exception {
        List<Map> data = new ArrayList<Map>();
        // 拼装条件
        String sql = "";
        String con = "";
        String[] checktypes = checktype.split(",");
        if (checktypes.length == 2) {
            String va = checktypes[1];
            if ("1".equals(va)) {  // 审核记录
                va = "1','2";
            } else if ("All".equals(va)) { // 全部
                va = "0','1','2";
            }
            // 判断结果类型
            if (!"".equals(result) && result != null) {
                va = result;
            }
            if (!"".equals(con)) {
                if ("All".equals(checktypes[0])) { // 全部审核类型
                    con += " and  ( is_add in ( '" + va + "') or is_del in ( '" + va + "') or is_modi in ( '" + va + "') ) ";
                } else {
                    con += " and  " + checktypes[0] + " in ( '" + va + "')";
                }
            } else {
                if ("All".equals(checktypes[0])) { // 全部审核类型
                    con += "  ( is_add in ( '" + va + "') or is_del in ( '" + va + "') or is_modi in ( '" + va + "') ) ";
                } else {
                    con += " " + checktypes[0] + " in ( '" + va + "')";
                }
            }
        }
        if (start_time != null && !"".equals(start_time)) {
            if (!"".equals(con)) {
                con += " and  to_date(last_update,'yyyy-MM-dd HH24:mi:ss')>=to_date('" + start_time + "','yyyy-MM-dd HH24:mi:ss')";
            } else {
                con += " to_date(last_update,'yyyy-MM-dd HH24:mi:ss')>=to_date('" + start_time + "','yyyy-MM-dd HH24:mi:ss')";
            }
        }
        if (end_time != null && !"".equals(end_time)) {
            if (!"".equals(con)) {
                con += " and  to_date(last_update,'yyyy-MM-dd HH24:mi:ss')<=to_date('" + end_time + "','yyyy-MM-dd HH24:mi:ss')";
            } else {
                con += " to_date(last_update,'yyyy-MM-dd HH24:mi:ss')<=to_date('" + end_time + "','yyyy-MM-dd HH24:mi:ss')";
            }
        }
        if ("超级管理员".equals(roleName)) {
            // 元限定条件
        } else {
            // 判断是否拥有权限
            List<Map> tables = getTableByCheckRole(roleId);
            if (tables != null && tables.size() > 0) {
                // 获取权限的表
                String ta = "";
                for (int i = 0; i < tables.size(); i++) {
                    if ("".equals(ta)) {
                        ta += " table_name=(select name from AG_WA_METADATA_TABLE where id='" + tables.get(i).get("tableid").toString() + "') ";
                    } else {
                        ta += " or table_name=(select name from AG_WA_METADATA_TABLE where id='" + tables.get(i).get("tableid").toString() + "') ";
                    }
                }
                con += " and (" + ta + ") ";
            } else {
                con += " and 1=2 "; // 没有表时
            }
        }
        // 返回结果
        int iCount = 0;
        List<Map> listTable = new ArrayList<Map>();
        // 判断类型
        if ("table".equals(tflag)) {
            // 查询
            if (tableid != null && !"".equals(tableid)) { // 表
                sql = " select NVL2(IS_ADD,'is_add','')||NVL2(IS_DEL,'is_del','')||NVL2(IS_MODI,'is_modi','') checktype,NVL(IS_ADD,NVL(IS_DEL,NVL(IS_MODI,''))) checkstat,m.* from AG_WA_METADATA_MODIFY m where (" + con + ") and m.table_name=( select a.name from AG_WA_METADATA_TABLE a where a.id='" + tableid + "'   ) ";
            } else {
                sql = " select NVL2(IS_ADD,'is_add','')||NVL2(IS_DEL,'is_del','')||NVL2(IS_MODI,'is_modi','') checktype,NVL(IS_ADD,NVL(IS_DEL,NVL(IS_MODI,''))) checkstat,m.* from AG_WA_METADATA_MODIFY m where (" + con + ") ";
            }
            data = listMetaData_SQL(sql, null);
            // 查询数据
            int start = (page - 1) * rows + 1;
            int end = page * rows;
            String sq = " select a1.* from ( select t.*,rownum rn  from ( " + sql + " ) t ) a1  where rn between " + start + " and " + end;
            List<Map> res = listMetaData_SQL(sq, null);
            // 转换成数据目录值
            for (int i = 0; i < res.size(); i++) {
                String json = "";
                if ("is_modi".equals(res.get(i).get("checktype")) && res.get(i).get("modi_value") != null && !"".equals(res.get(i).get("modi_value"))) {
                    json = res.get(i).get("modi_value").toString();
                } else {
                    json = res.get(i).get("orig_value").toString();
                }
                JSONObject js = JSONObject.fromObject(json);
                Iterator iterator = js.keys();
                Map map = new HashMap();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    String value = js.getString(key);
                    map.put(key.toLowerCase(), value);
                }
                // 拼装参数
                map.put("orig_value", res.get(i).get("orig_value").toString());
                map.put("modi_value", res.get(i).get("modi_value") == null ? "" : res.get(i).get("modi_value").toString());
                map.put("tablename", res.get(i).get("table_name").toString());
                map.put("prifield", res.get(i).get("pri_field").toString());
                map.put("prikey", res.get(i).get("pri_value").toString());
                map.put("rowid", res.get(i).get("id").toString());
                String opera_name = "";
                String ss = "";
                if (res.get(i).get("opera_name") != null && !"".equals(res.get(i).get("opera_name"))) {
                    ss = " select u.user_name from ag_user u where u.LOGIN_NAME='" + (res.get(i).get("opera_name") == null ? "" : res.get(i).get("opera_name").toString()) + "'  ";
                    List<Map> listt = listAuthorSQL(ss, null);
                    if (listt != null && listt.size() > 0) {
                        opera_name = listt.get(0).get("user_name").toString();
                    }
                }
                map.put("opera_name", opera_name);
                map.put("checktype", res.get(i).get("checktype").toString());
                map.put("checkstat", res.get(i).get("checkstat").toString());
                map.put("checkview", res.get(i).get("check_view") == null ? "" : res.get(i).get("check_view").toString());
                listTable.add(map);
            }
            // 查询总数
            if (data != null) {
                iCount = data.size();
            }
        } else if ("top".equals(tflag)) {
            // 查询条件
            sql = " select (select t.DATABASEID from AG_WA_METADATA_TABLE t where t.name=m.table_name) db_id,(select t.id from AG_WA_METADATA_TABLE t where t.name=m.table_name) table_id,(select t.cname from AG_WA_METADATA_TABLE t where t.name=m.table_name ) table_name, NVL (SUM (CASE IS_ADD WHEN '0' THEN 1 END),0) add0,NVL (SUM (CASE IS_DEL WHEN '0' THEN 1 END),0) del0,NVL (SUM (CASE IS_MODI WHEN '0' THEN 1 END),0) modi0,NVL(SUM (CASE IS_ADD WHEN '-1' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '-1' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '-1' THEN 1 END),0) type11,NVL(SUM (CASE IS_ADD WHEN '0' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '0' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '0' THEN 1 END),0) type0,NVL(SUM (CASE IS_ADD WHEN '1' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '1' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '1' THEN 1 END),0) type1,NVL(SUM (CASE IS_ADD WHEN '2' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '2' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '2' THEN 1 END),0) type2 from AG_WA_METADATA_MODIFY m where (" + con + ") and m.TABLE_NAME in (select name from AG_WA_METADATA_TABLE) group by m.table_name ";
            data = listMetaData_SQL(sql, null);
            // 查询数据
            int start = (page - 1) * rows + 1;
            int end = page * rows;
            String sq = " select a1.* from ( select t.*,rownum rn  from ( " + sql + " ) t ) a1  where rn between " + start + " and " + end;
            listTable = listMetaData_SQL(sq, null);
            // 查询总数
            if (data != null) {
                iCount = data.size();
            }
        } else if ("db".equals(tflag)) {
            // 查询条件
            sql = " select (select t.DATABASEID from AG_WA_METADATA_TABLE t where t.name=m.table_name) db_id,(select t.id from AG_WA_METADATA_TABLE t where t.name=m.table_name) table_id,(select t.cname from AG_WA_METADATA_TABLE t where t.name=m.table_name ) table_name, NVL (SUM (CASE IS_ADD WHEN '0' THEN 1 END),0) add0,NVL (SUM (CASE IS_DEL WHEN '0' THEN 1 END),0) del0,NVL (SUM (CASE IS_MODI WHEN '0' THEN 1 END),0) modi0,NVL(SUM (CASE IS_ADD WHEN '-1' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '-1' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '-1' THEN 1 END),0) type11,NVL(SUM (CASE IS_ADD WHEN '0' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '0' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '0' THEN 1 END),0) type0,NVL(SUM (CASE IS_ADD WHEN '1' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '1' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '1' THEN 1 END),0) type1,NVL(SUM (CASE IS_ADD WHEN '2' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '2' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '2' THEN 1 END),0) type2 from AG_WA_METADATA_MODIFY m where (" + con + ") and m.table_name in ( select name from AG_WA_METADATA_TABLE where databaseid='" + tableid + "' ) group by m.table_name ";
            data = listMetaData_SQL(sql, null);
            // 查询数据
            int start = (page - 1) * rows + 1;
            int end = page * rows;
            String sq = " select a1.* from ( select t.*,rownum rn  from ( " + sql + " ) t ) a1  where rn between " + start + " and " + end;
            listTable = listMetaData_SQL(sq, null);
            // 查询总数
            if (data != null) {
                iCount = data.size();
            }
        }
        // 时间格式
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(TIMESTAMP.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        String str = "{\"rows\":" + JSONArray.fromObject(listTable, config).toString() + ",\"total\":" + iCount + "}";
        return str;
    }

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
     * @throws Exception
     */
    public String getReportDataNew(String roleName, String roleId, String start_time, String end_time, String checktype, String tflag, String tableid, int page, int rows, String result) throws Exception {
        List<Map> data = new ArrayList<Map>();
        // 拼装条件
        String sql = "";
        String con = "";
        String[] checktypes = checktype.split(",");
        if (checktypes.length == 2) {
            String va = checktypes[1];
            if ("1".equals(va)) {  // 审核记录
                va = "1','2";
            } else if ("All".equals(va)) { // 全部
                va = "-1','0','1','2";
            }
            // 判断结果类型
            if (!"".equals(result) && result != null) {
                va = result;
            }
            if (!"".equals(con)) {
                if ("All".equals(checktypes[0])) { // 全部审核类型
                    con += " and  ( is_add in ( '" + va + "') or is_del in ( '" + va + "') or is_modi in ( '" + va + "') ) ";
                } else {
                    con += " and  " + checktypes[0] + " in ( '" + va + "')";
                }
            } else {
                if ("All".equals(checktypes[0])) { // 全部审核类型
                    con += "  ( is_add in ( '" + va + "') or is_del in ( '" + va + "') or is_modi in ( '" + va + "') ) ";
                } else {
                    con += " " + checktypes[0] + " in ( '" + va + "')";
                }
            }
        }
        if (start_time != null && !"".equals(start_time)) {
            if (!"".equals(con)) {
                con += " and  to_date(last_update,'yyyy-MM-dd HH24:mi:ss')>=to_date('" + start_time + "','yyyy-MM-dd HH24:mi:ss')";
            } else {
                con += " to_date(last_update,'yyyy-MM-dd HH24:mi:ss')>=to_date('" + start_time + "','yyyy-MM-dd HH24:mi:ss')";
            }
        }
        if (end_time != null && !"".equals(end_time)) {
            if (!"".equals(con)) {
                con += " and  to_date(last_update,'yyyy-MM-dd HH24:mi:ss')<=to_date('" + end_time + "','yyyy-MM-dd HH24:mi:ss')";
            } else {
                con += " to_date(last_update,'yyyy-MM-dd HH24:mi:ss')<=to_date('" + end_time + "','yyyy-MM-dd HH24:mi:ss')";
            }
        }
        if ("超级管理员".equals(roleName)) {
            // 无限定条件
        } else {
            // 判断是否拥有权限
            List<Map> tables = getTableByEditRole(roleId);
            if (tables != null && tables.size() > 0) {
                // 获取权限的表
                String ta = "";
                for (int i = 0; i < tables.size(); i++) {
                    if ("".equals(ta)) {
                        ta += " table_name=(select name from AG_WA_METADATA_TABLE where id='" + tables.get(i).get("tableid").toString() + "') ";
                    } else {
                        ta += " or table_name=(select name from AG_WA_METADATA_TABLE where id='" + tables.get(i).get("tableid").toString() + "') ";
                    }
                }
                con += " and (" + ta + ") ";
            } else {
                con += " and 1=2 "; // 没有表时
            }
        }
        // 返回结果
        int iCount = 0;
        List<Map> listTable = new ArrayList<Map>();
        // 判断类型
        if ("table".equals(tflag)) {
            // 查询
            if (tableid != null && !"".equals(tableid)) { // 表
                sql = " select NVL2(IS_ADD,'is_add','')||NVL2(IS_DEL,'is_del','')||NVL2(IS_MODI,'is_modi','') checktype,NVL(IS_ADD,NVL(IS_DEL,NVL(IS_MODI,''))) checkstat, m.* from AG_WA_METADATA_MODIFY m where (" + con + ") and m.table_name=( select a.name from AG_WA_METADATA_TABLE a where a.id='" + tableid + "'   ) order by checktype,checkstat ";
            } else {
                sql = " select NVL2(IS_ADD,'is_add','')||NVL2(IS_DEL,'is_del','')||NVL2(IS_MODI,'is_modi','') checktype,NVL(IS_ADD,NVL(IS_DEL,NVL(IS_MODI,''))) checkstat, m.* from AG_WA_METADATA_MODIFY m where (" + con + ")  order by checktype,checkstat  ";
            }
            data = listMetaData_SQL(sql, null);
            // 查询数据
            int start = (page - 1) * rows + 1;
            int end = page * rows;
            String sq = " select a1.* from ( select t.*,rownum rn  from ( " + sql + " ) t ) a1  where rn between " + start + " and " + end;
            List<Map> res = listMetaData_SQL(sq, null);
            // 转换成数据目录值
            for (int i = 0; i < res.size(); i++) {
                String json = "";
                if ("is_modi".equals(res.get(i).get("checktype")) && res.get(i).get("modi_value") != null && !"".equals(res.get(i).get("modi_value"))) {
                    json = res.get(i).get("modi_value").toString();
                } else {
                    json = res.get(i).get("orig_value").toString();
                }
                JSONObject js = JSONObject.fromObject(json);
                Iterator iterator = js.keys();
                Map map = new HashMap();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    String value = js.getString(key);
                    map.put(key.toLowerCase(), value);
                }
                // 拼装参数
                map.put("orig_value", res.get(i).get("orig_value").toString());
                map.put("modi_value", res.get(i).get("modi_value") == null ? "" : res.get(i).get("modi_value").toString());
                map.put("tablename", res.get(i).get("table_name").toString());
                map.put("prifield", res.get(i).get("pri_field").toString());
                map.put("prikey", res.get(i).get("pri_value").toString());
                map.put("checktype", res.get(i).get("checktype").toString());
                map.put("checkstat", res.get(i).get("checkstat").toString());
                map.put("checktypeText", res.get(i).get("checktype").toString());
                map.put("checkstatText", res.get(i).get("checkstat").toString());
                map.put("checkview", res.get(i).get("check_view") == null ? "" : res.get(i).get("check_view").toString());
                listTable.add(map);
            }
            // 查询总数
            if (data != null) {
                iCount = data.size();
            }
        } else if ("top".equals(tflag)) {
            // 查询条件
            sql = " select (select t.DATABASEID from AG_WA_METADATA_TABLE t where t.name=m.table_name and rownum=1 ) db_id,(select t.id from AG_WA_METADATA_TABLE t where t.name=m.table_name and rownum=1 ) table_id,(select t.cname from AG_WA_METADATA_TABLE t where t.name=m.table_name and rownum=1  ) table_name,NVL(SUM (CASE IS_ADD WHEN '-1' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '-1' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '-1' THEN 1 END),0) type11,NVL(SUM (CASE IS_ADD WHEN '0' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '0' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '0' THEN 1 END),0) type0,NVL(SUM (CASE IS_ADD WHEN '1' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '1' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '1' THEN 1 END),0) type1,NVL(SUM (CASE IS_ADD WHEN '2' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '2' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '2' THEN 1 END),0) type2 from AG_WA_METADATA_MODIFY m where (" + con + ") and m.TABLE_NAME in (select name from AG_WA_METADATA_TABLE) group by m.table_name ";
            data = listMetaData_SQL(sql, null);
            // 查询数据
            int start = (page - 1) * rows + 1;
            int end = page * rows;
            String sq = " select a1.* from ( select t.*,rownum rn  from ( " + sql + " ) t ) a1  where rn between " + start + " and " + end;
            listTable = listMetaData_SQL(sq, null);
            // 查询总数
            if (data != null) {
                iCount = data.size();
            }
        } else if ("db".equals(tflag)) {
            // 查询条件
            sql = " select (select t.DATABASEID from AG_WA_METADATA_TABLE t where t.name=m.table_name and rownum=1 ) db_id,(select t.id from AG_WA_METADATA_TABLE t where t.name=m.table_name  and rownum=1 ) table_id,(select t.cname from AG_WA_METADATA_TABLE t where t.name=m.table_name  and rownum=1  ) table_name,NVL(SUM (CASE IS_ADD WHEN '-1' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '-1' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '-1' THEN 1 END),0) type11,NVL(SUM (CASE IS_ADD WHEN '0' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '0' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '0' THEN 1 END),0) type0,NVL(SUM (CASE IS_ADD WHEN '1' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '1' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '1' THEN 1 END),0) type1,NVL(SUM (CASE IS_ADD WHEN '2' THEN 1 END),0)+NVL(SUM (CASE IS_del WHEN '2' THEN 1 END),0)+NVL(SUM (CASE IS_MODI WHEN '2' THEN 1 END),0) type2 from AG_WA_METADATA_MODIFY m where (" + con + ") and m.table_name in ( select name from AG_WA_METADATA_TABLE where databaseid='" + tableid + "' ) group by m.table_name ";
            data = listMetaData_SQL(sql, null);
            // 查询数据
            int start = (page - 1) * rows + 1;
            int end = page * rows;
            String sq = " select a1.* from ( select t.*,rownum rn  from ( " + sql + " ) t ) a1  where rn between " + start + " and " + end;
            listTable = listMetaData_SQL(sq, null);
            // 查询总数
            if (data != null) {
                iCount = data.size();
            }
        }
        // 时间格式
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(TIMESTAMP.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        String str = "{\"rows\":" + JSONArray.fromObject(listTable, config).toString() + ",\"total\":" + iCount + "}";
        return str;
    }

    /**
     * 创建模板
     *
     * @param url
     * @param field
     * @param name
     * @param cname
     * @return
     * @throws SQLException
     */
    public String saveTemp(String url, String field, String name, String cname) throws SQLException {
        String msg = "";
        JSONArray arr = JSONArray.fromObject(field);
        String fie = "";
        // 原始jdbc
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            String[] ur = url.split("\\|");
            String[] userName = ur[1].split("/");
            String[] hosts = ur[2].split("/");
            String db = ur[0];
            String user = userName[0];
            String pwd = userName[1];
            String ip = hosts[0];
            String instance = hosts[1];
            if ("oracle".equals(db)) {
                String driver = "oracle.jdbc.driver.OracleDriver";
                Class.forName(driver);
                String urll = "jdbc:oracle:thin:@" + ip + ":" + instance;
                con = DriverManager.getConnection(urll, user, pwd);
                // 建表语句
                if (arr.size() > 0) {
                    for (int j = 0; j < arr.size(); j++) {
                        JSONObject obj = (JSONObject) arr.get(j);
                        if ("N".equals(obj.get("isNull")) || "否".equals(obj.get("isNull"))) { // 判断是否为空
                            if ("Y".equals(obj.get("isPri")) || "是".equals(obj.get("isPri"))) { // 判断是否主键
                                fie += ", " + obj.get("field") + " " + obj.get("type") + " PRIMARY KEY  NOT NULL  ";
                            } else {
                                fie += ", " + obj.get("field") + " " + obj.get("type") + "   NOT NULL  ";
                            }
                        } else {
                            fie += ", " + obj.get("field") + " " + obj.get("type") + "   ";
                        }
                    }
                }
                if (!"".equals(fie)) {
                    fie = fie.substring(1);
                }
                // 创建表
                String sql = " create table " + name + " ( " + fie + " ) ";
                System.out.println("SQL:" + sql);
                stm = con.prepareStatement(sql);
                stm.executeUpdate(sql);
                // 获取字段
                List com = new ArrayList(); // 注释
                String co = " comment on table " + name + " is '" + cname + "'  ";
                com.add(co);  // 表注释
                for (int j = 0; j < arr.size(); j++) {
                    JSONObject obj = (JSONObject) arr.get(j);
                    String comm = " comment on column " + name + "." + obj.get("field") + " is '" + obj.get("comment") + "'  ";
                    com.add(comm);
                }
                // 创建注释
                for (int i = 0; i < com.size(); i++) {
                    stm = con.prepareStatement(com.get(i).toString());
                    stm.executeUpdate(com.get(i).toString());
                }
            } else if ("mysql".equals(db)) {
                String driver = "com.mysql.jdbc.Driver";
                Class.forName(driver);
                String urll = "jdbc:mysql://" + ip + "/" + instance;
                con = DriverManager.getConnection(urll, user, pwd);
                // 建表语句
                if (arr.size() > 0) {
                    for (int j = 0; j < arr.size(); j++) {
                        JSONObject obj = (JSONObject) arr.get(j);
                        String type = obj.get("type").toString().replaceAll("NUMBER", "int").replaceAll("VARCHAR2", "VARCHAR");
                        if ("N".equals(obj.get("isNull")) || "否".equals(obj.get("isNull"))) { // 判断是否为空
                            if ("Y".equals(obj.get("isPri")) || "是".equals(obj.get("isPri"))) { // 判断是否主键
                                fie += ", " + obj.get("field") + " " + type.toLowerCase() + " NOT NULL  PRIMARY KEY   ";
                            } else {
                                fie += ", " + obj.get("field") + " " + type.toLowerCase() + "   NOT NULL  ";
                            }
                        } else {
                            fie += ", " + obj.get("field") + " " + type.toLowerCase() + "   ";
                        }
                    }
                }
                if (!"".equals(fie)) {
                    fie = fie.substring(1);
                }
                // 创建表
                String sql = " create table " + name + " ( " + fie + " ) ";
                System.out.println("SQL:" + sql);
                stm = con.prepareStatement(sql);
                stm.executeUpdate(sql);
                // 获取字段
                List com = new ArrayList(); // 注释
                String co = " alter table " + name + " comment '" + cname + "'  ";
                com.add(co);  // 表注释
                for (int j = 0; j < arr.size(); j++) {
                    JSONObject obj = (JSONObject) arr.get(j);
                    String comm = " alter table " + name + " modify column " + obj.get("field") + " int comment '" + obj.get("comment") + "' ";
                    com.add(comm);
                }
                // 创建注释
                for (int i = 0; i < com.size(); i++) {
                    stm = con.prepareStatement(com.get(i).toString());
                    stm.executeUpdate(com.get(i).toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return msg;
    }

    /**
     * 获取模板列表
     *
     * @param temp_name
     * @param start_time
     * @param end_time
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    public String getTempData(String temp_name, String start_time, String end_time, int page, int rows) throws Exception {
        String whe = " 1=1 ";
        if (temp_name != null && !"".equals(temp_name)) {
            whe += " and temp_name like '%" + temp_name + "%'  ";
        }
        if (start_time != null && !"".equals(start_time)) {
            whe += " and to_date(last_update,'yyyy-MM-dd HH24:mi:ss') >= to_date('" + start_time + "','yyyy-MM-dd HH24:mi:ss')  ";
        }
        if (end_time != null && !"".equals(end_time)) {
            whe += " and to_date(last_update,'yyyy-MM-dd HH24:mi:ss') <= to_date('" + end_time + "','yyyy-MM-dd HH24:mi:ss')  ";
        }
        List<Map> listTable = new ArrayList<Map>();
        String sql = " select * from AG_WA_METADATA_TEMP where ( " + whe + " ) order by last_update  ";
        // 查询数据
        int start = (page - 1) * rows + 1;
        int end = page * rows;
        String sq = " select a1.* from ( select t.*,rownum rn  from ( " + sql + " ) t ) a1  where rn between " + start + " and " + end;
        listTable = listMetaData_SQL(sql, null);
        // 查询总数
        int iCount = 0;
        if (listTable != null) {
            iCount = listTable.size();
        }
        // 时间格式
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(TIMESTAMP.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        String str = "{\"rows\":" + JSONArray.fromObject(listTable, config).toString() + ",\"total\":" + iCount + "}";
        return str;
    }

    /**
     * 上报
     *
     * @param ids
     * @return
     */
    public ResultForm upData(String ids) throws Exception {
        ResultForm res = new ResultForm(false, "操作无效！");
        String[] id = ids.split(",");
        for (String field : id) {
            String[] fie = field.split("\\|");
            String sql = " update AG_WA_METADATA_MODIFY set  " + fie[3] + "='0' where table_name='" + fie[0] + "' and pri_field='" + fie[1] + "' and pri_value='" + fie[2] + "'  ";
            int i = update_SQL(sql, null);
            if (i < 1) {
                res.setSuccess(false);
                res.setMessage("数据上报失败！");
                break;
            } else {
                res.setSuccess(true);
                res.setMessage("数据上报成功！");
            }
        }
        return res;
    }

    /**
     * 连接测试
     *
     * @param url
     * @return
     */
    public ResultForm connectTest(String url) {
        ResultForm res = new ResultForm(false, "连接失败！");
        Connection con = null;
        try {
            String[] ur = url.split("\\|");
            String[] userName = ur[1].split("/");
            if (ur.length < 3 || userName.length < 2 || ur[2].split("/").length < 2) {
                res.setMessage("缺失必要的参数！");
                res.setSuccess(false);
            } else {
                String[] hosts = ur[2].split("/");
                String db = ur[0];
                String user = userName[0];
                String pwd = userName[1];
                String ip = hosts[0];
                String instance = hosts[1];
                // 数据库类型
                if ("oracle".equals(db)) {
                    String driver = "oracle.jdbc.driver.OracleDriver";
                    Class.forName(driver);
                    String urll = "jdbc:oracle:thin:@" + ip + ":" + instance;
                    con = DriverManager.getConnection(urll, user, pwd);
                    if (con != null) {
                        res.setMessage("测试连接成功！");
                        res.setSuccess(true);
                    } else {
                        res.setMessage("测试连接失败！请联系系统管理员。");
                        res.setSuccess(false);
                    }
                } else if ("mysql".equals(db)) {
                    String driver = "com.mysql.jdbc.Driver";
                    Class.forName(driver);
                    String urll = "jdbc:mysql://" + ip + "/" + instance;
                    con = DriverManager.getConnection(urll, user, pwd);
                    if (con != null) {
                        res.setMessage("测试连接成功！");
                        res.setSuccess(true);
                    } else {
                        res.setMessage("测试连接失败！请联系系统管理员。");
                        res.setSuccess(false);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setMessage("测试连接失败！请联系系统管理员。");
            res.setSuccess(false);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    /**
     * 驳回
     *
     * @param loginName
     * @param ta
     * @param fi
     * @param va
     * @param idd
     * @return
     * @throws Exception
     */
    public ResultForm passCheckDataNo(String type, String view, String loginName, String[] ta, String[] fi, String[] va, String[] idd) throws Exception {
        ResultForm res = new ResultForm(false, "操作失败!");
        List<Map> data = new ArrayList<Map>();
        for (int z = 0; z < ta.length; z++) {
            String tablename = ta[z];
            String prifield = fi[z];
            String privalue = va[z];
            String iddd = idd[z];
            // 执行操作       开始
            // 获取信息表
            String sql = "";
            if (iddd != null && !"".equals(iddd)) {
                sql = " select NVL2(IS_ADD,'add','')||NVL2(IS_DEL,'del','')||NVL2(IS_MODI,'modi','') checktype,m.* from AG_WA_METADATA_MODIFY m where id='" + iddd + "' ";
            } else {
                sql = " select NVL2(IS_ADD,'add','')||NVL2(IS_DEL,'del','')||NVL2(IS_MODI,'modi','') checktype,m.* from AG_WA_METADATA_MODIFY m where table_name='" + tablename + "' and pri_field='" + prifield + "' and pri_value='" + privalue + "' ";
            }
            data = listMetaData_SQL(sql, null);
            // 替换审核类型
            if (data != null && data.size() > 0) {
                type = data.get(0).get("checktype").toString();
            }
            if ("modi".equals(type)) {
                String sq = " update AG_WA_METADATA_MODIFY set AUDITOR='" + loginName + "',check_view='" + view + "',is_modi='2',last_update='" + new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date()) + "' where id='" + data.get(0).get("id") + "' ";
                int j = update_SQL(sq, null);
                if (j > 0) {
                    res.setSuccess(true);
                    res.setMessage("修改审核操作成功！");
                } else {
                    res.setSuccess(false);
                    res.setMessage("修改审核操作失败！请联系系统管理员。");
                    // 结束循环
                    break;
                }
            } else if ("del".equals(type)) {
                String sq = " update AG_WA_METADATA_MODIFY set AUDITOR='" + loginName + "',check_view='" + view + "', is_del='2',is_modi='',is_add='',last_update='" + new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date()) + "' where id='" + data.get(0).get("id") + "' ";
                int j = update_SQL(sq, null);
                if (j > 0) {
                    res.setSuccess(true);
                    res.setMessage("删除审核操作成功！");
                } else {
                    res.setSuccess(false);
                    res.setMessage("删除审核操作失败！请联系系统管理员。");
                    // 结束循环
                    break;
                }
            } else if ("add".equals(type)) {
                // 修改信息表
                String s = " update AG_WA_METADATA_MODIFY set AUDITOR='" + loginName + "',check_view='" + view + "', is_add='2',last_update='" + new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date()) + "' where id='" + data.get(0).get("id") + "' ";
                int j = update_SQL(s, null);
                if (j > 0) {
                    res.setSuccess(true);
                    res.setMessage("新增审核操作成功！");
                } else {
                    res.setSuccess(false);
                    res.setMessage("新增审核操作失败！请联系系统管理员。");
                    // 结束循环
                    break;
                }
            }
            // 执行操作       结束
        }
        return res;
    }

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
     * @throws Exception
     */
    public ResultForm passCheckData(String type, String loginName, String[] ta,
                                    String[] fi, String[] va, String[] idd) throws Exception {
        ResultForm res = new ResultForm(false, "操作失败!");
        List<Map> data = new ArrayList<Map>();
        for (int z = 0; z < ta.length; z++) {
            String tablename = ta[z];
            String prifield = fi[z];
            String privalue = va[z];
            String iddd = idd[z];
            // 执行操作       开始
            // 字段信息
            List lii = new ArrayList();
            lii.add(tablename);
            List<Map> fields = getFieldInfo(lii);
            // 获取信息表
            String sql = "";
            if (iddd != null && !"".equals(iddd)) {
                sql = " select NVL2(IS_ADD,'add','')||NVL2(IS_DEL,'del','')||NVL2(IS_MODI,'modi','') checktype,m.* from AG_WA_METADATA_MODIFY m where id='" + iddd + "' ";
            } else {
                sql = " select NVL2(IS_ADD,'add','')||NVL2(IS_DEL,'del','')||NVL2(IS_MODI,'modi','') checktype,m.* from AG_WA_METADATA_MODIFY m where table_name='" + tablename + "' and pri_field='" + prifield + "' and pri_value='" + privalue + "' ";
            }
            data = listMetaData_SQL(sql, null);
            // 替换审核类型
            if (data != null && data.size() > 0) {
                type = type = data.get(0).get("checktype").toString();
            }
            if ("modi".equals(type)) {
                if (data != null && data.size() > 0) {
                    // 更新数据库
                    String upJson = "".equals(data.get(0).get("modi_value")) ? "{}" : data.get(0).get("modi_value").toString();
                    JSONObject up = JSONObject.fromObject(upJson);
                    String set = "";
                    Iterator iterator = up.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        if (!prifield.equals(key)) {
                            String value = up.getString(key);
                            // 判断是否日期类型
                            boolean isDate = false;
                            for (int i = 0; i < fields.size(); i++) {
                                if (key.equals(fields.get(i).get("name")) && fields.get(i).get("type") != null && ("DATE".equals(fields.get(i).get("type").toString().toUpperCase()))) {
                                    isDate = true;
                                    break;
                                }
                            }
                            if (isDate && value != null && !"".equals(value)) {
                                set += "," + key + "=to_date('" + value + "','yyyy-MM-dd HH24:mi:ss')";
                            } else {
                                set += "," + key + "='" + value + "'";
                            }
                        }
                    }
                    if (!"".equals(set)) {
                        set = set.substring(1);
                    } else {
                        set = " 1=1 ";
                    }
                    int i = 0;
                    // 查询url
                    List li2 = new ArrayList();
                    li2.add(tablename);
                    List<Map> re = getDBByName(li2);
                    String url = re.get(0).get("url").toString();
                    // 原始jdbc
                    Connection con = null;
                    Statement stm = null;
                    ResultSet rs = null;
                    try {
                        String[] ur = url.split("\\|");
                        String[] userName = ur[1].split("/");
                        String[] hosts = ur[2].split("/");
                        String db = ur[0];
                        String user = userName[0];
                        String pwd = userName[1];
                        String ip = hosts[0];
                        String instance = hosts[1];
                        if ("oracle".equals(db)) {
                            String driver = "oracle.jdbc.driver.OracleDriver";
                            Class.forName(driver);
                            String urll = "jdbc:oracle:thin:@" + ip + ":" + instance;
                            con = DriverManager.getConnection(urll, user, pwd);
                            String sq = " update " + tablename + " set " + set + " where " + prifield + "='" + privalue + "'";
                            System.out.println("SQL:" + sq);
                            stm = con.prepareStatement(sq);
                            i = stm.executeUpdate(sq);
                        } else if ("mysql".equals(db)) {
                            String driver = "com.mysql.jdbc.Driver";
                            Class.forName(driver);
                            String urll = "jdbc:mysql://" + ip + "/" + instance;
                            con = DriverManager.getConnection(urll, user, pwd);
                            String sq = " update " + tablename + " set " + set + " where " + prifield + "='" + privalue + "'";
                            System.out.println("SQL:" + sq);
                            stm = con.prepareStatement(sq);
                            i = stm.executeUpdate(sq);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (con != null) {
                            con.close();
                        }
                        if (stm != null) {
                            stm.close();
                        }
                        if (rs != null) {
                            rs.close();
                        }
                    }
                    // 修改信息表
                    if (i > 0) {
                        String sq = " update AG_WA_METADATA_MODIFY set AUDITOR='" + loginName + "',modi_value='',orig_value='" + data.get(0).get("modi_value").toString().replaceAll("'", "''") + "',is_modi='1',last_update='" + new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date()) + "' where id='" + data.get(0).get("id") + "' ";
                        int j = update_SQL(sq, null);
                        if (j > 0) {
                            res.setSuccess(true);
                            res.setMessage("修改审核操作成功！");
                        } else {
                            res.setSuccess(false);
                            res.setMessage("修改审核操作失败！请联系系统管理员。");
                            // 跳出循环
                            break;
                        }
                    } else {
                        res.setSuccess(false);
                        res.setMessage("修改审核操作失败！请联系系统管理员。");
                        // 跳出循环
                        break;
                    }
                }
            } else if ("del".equals(type)) {
                if (data != null && data.size() > 0) {
                    int i = 0;
                    // 查询url
                    List li2 = new ArrayList();
                    li2.add(tablename);
                    List<Map> re = getDBByName(li2);
                    String url = re.get(0).get("url").toString();
                    // 原始jdbc
                    Connection con = null;
                    Statement stm = null;
                    ResultSet rs = null;
                    try {
                        String[] ur = url.split("\\|");
                        String[] userName = ur[1].split("/");
                        String[] hosts = ur[2].split("/");
                        String db = ur[0];
                        String user = userName[0];
                        String pwd = userName[1];
                        String ip = hosts[0];
                        String instance = hosts[1];
                        if ("oracle".equals(db)) {
                            String driver = "oracle.jdbc.driver.OracleDriver";
                            Class.forName(driver);
                            String urll = "jdbc:oracle:thin:@" + ip + ":" + instance;
                            con = DriverManager.getConnection(urll, user, pwd);
                            String sq = " delete from  " + tablename + " where " + prifield + "='" + privalue + "'";
                            System.out.println("SQL:" + sq);
                            stm = con.prepareStatement(sq);
                            i = stm.executeUpdate(sq);
                        } else if ("mysql".equals(db)) {
                            String driver = "com.mysql.jdbc.Driver";
                            Class.forName(driver);
                            String urll = "jdbc:mysql://" + ip + "/" + instance;
                            con = DriverManager.getConnection(urll, user, pwd);
                            String sq = " delete from  " + tablename + " where " + prifield + "='" + privalue + "'";
                            System.out.println("SQL:" + sq);
                            stm = con.prepareStatement(sq);
                            i = stm.executeUpdate(sq);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (con != null) {
                            con.close();
                        }
                        if (stm != null) {
                            stm.close();
                        }
                        if (rs != null) {
                            rs.close();
                        }
                    }
                    // 修改信息表
                    if (i > 0) {
                        String sq = " update AG_WA_METADATA_MODIFY set AUDITOR='" + loginName + "',is_del='1',is_modi='',is_add='',last_update='" + new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date()) + "' where id='" + data.get(0).get("id") + "' ";
                        int j = update_SQL(sq, null);
                        if (j > 0) {
                            res.setSuccess(true);
                            res.setMessage("删除审核操作成功！");
                        } else {
                            res.setSuccess(false);
                            res.setMessage("删除审核操作失败！请联系系统管理员。");
                            // 跳出循环
                            break;
                        }
                    } else {
                        res.setSuccess(false);
                        res.setMessage("删除审核操作失败！请联系系统管理员。");
                        // 跳出循环
                        break;
                    }
                }
            } else if ("add".equals(type)) {
                if (data != null && data.size() > 0) {
                    // 数据入库
                    String fie = "";
                    String val = "";
                    String add = data.get(0).get("orig_value") == null ? "" : data.get(0).get("orig_value").toString();
                    JSONObject addJson = JSONObject.fromObject(add);
                    Iterator iterator = addJson.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        String value = addJson.getString(key);
                        if (value != null && !"".equals(value)) {
                            // 判断是否日期
                            boolean isDate = false;
                            for (int i = 0; i < fields.size(); i++) {
                                if (key.equals(fields.get(i).get("name").toString()) && fields.get(i).get("type") != null && ("DATE".equals(fields.get(i).get("type").toString().toUpperCase()))) {
                                    isDate = true;
                                    break;
                                }
                            }
                            if ("".equals(fie)) {
                                fie += key;
                                if (isDate) {
                                    val += "to_date('" + value + "','yyyy-MM-dd HH24:mi:ss')";
                                } else {
                                    val += "'" + value + "'";
                                }
                            } else {
                                fie += "," + key;
                                if (isDate) {
                                    val += ",to_date('" + value + "','yyyy-MM-dd HH24:mi:ss')";
                                } else {
                                    val += ",'" + value + "'";
                                }
                            }
                        }
                    }
                    List<Map> dataa = new ArrayList<Map>();
                    // 查询url
                    List li2 = new ArrayList();
                    li2.add(tablename);
                    List<Map> re = getDBByName(li2);
                    String url = re.get(0).get("url").toString();
                    // 原始jdbc
                    Connection con = null;
                    Statement stm = null;
                    ResultSet rs = null;
                    try {
                        String[] ur = url.split("\\|");
                        String[] userName = ur[1].split("/");
                        String[] hosts = ur[2].split("/");
                        String db = ur[0];
                        String user = userName[0];
                        String pwd = userName[1];
                        String ip = hosts[0];
                        String instance = hosts[1];
                        if ("oracle".equals(db)) {
                            String driver = "oracle.jdbc.driver.OracleDriver";
                            Class.forName(driver);
                            String urll = "jdbc:oracle:thin:@" + ip + ":" + instance;
                            con = DriverManager.getConnection(urll, user, pwd);
                            stm = con.createStatement();
                            sql = " select * from  " + tablename + " where " + prifield + "='" + privalue + "' ";
                            System.out.println("SQL:" + sql);
                            rs = stm.executeQuery(sql);
                            ResultSetMetaData da = rs.getMetaData();
                            // 返回结果
                            while (rs.next()) {
                                Map map = new HashMap();
                                for (int i = 1; i <= da.getColumnCount(); i++) {
                                    String c = da.getColumnName(i);
                                    if (!"SHAPE".equals(c.toUpperCase())) {
                                        String v = rs.getString(c);
                                        map.put(c.toLowerCase(), v);
                                    }
                                }
                                dataa.add(map);
                            }
                        } else if ("mysql".equals(db)) {
                            String driver = "com.mysql.jdbc.Driver";
                            Class.forName(driver);
                            String urll = "jdbc:mysql://" + ip + "/" + instance;
                            con = DriverManager.getConnection(urll, user, pwd);
                            stm = con.createStatement();
                            sql = " select * from  " + tablename + " where " + prifield + "='" + privalue + "' ";
                            System.out.println("SQL:" + sql);
                            rs = stm.executeQuery(sql);
                            ResultSetMetaData da = rs.getMetaData();
                            // 返回结果
                            while (rs.next()) {
                                Map map = new HashMap();
                                for (int i = 1; i <= da.getColumnCount(); i++) {
                                    String c = da.getColumnName(i);
                                    if (!"SHAPE".equals(c.toUpperCase())) {
                                        String v = rs.getString(c);
                                        map.put(c.toLowerCase(), v);
                                    }
                                }
                                dataa.add(map);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (con != null) {
                            con.close();
                        }
                        if (stm != null) {
                            stm.close();
                        }
                        if (rs != null) {
                            rs.close();
                        }
                    }
                    if (dataa != null && dataa.size() > 0) {
                        res.setSuccess(false);
                        res.setMessage("主键冲突，操作失败！");
                        break;
                    } else {
                        int k = 0;
                        try {
                            String[] ur = url.split("\\|");
                            String[] userName = ur[1].split("/");
                            String[] hosts = ur[2].split("/");
                            String db = ur[0];
                            String user = userName[0];
                            String pwd = userName[1];
                            String ip = hosts[0];
                            String instance = hosts[1];
                            if ("oracle".equals(db)) {
                                String driver = "oracle.jdbc.driver.OracleDriver";
                                Class.forName(driver);
                                String urll = "jdbc:oracle:thin:@" + ip + ":" + instance;
                                con = DriverManager.getConnection(urll, user, pwd);
                                String s = " insert into " + tablename + "(" + fie + ") values(" + val + ")";
                                System.out.println("SQL:" + s);
                                stm = con.prepareStatement(s);
                                k = stm.executeUpdate(s);
                            } else if ("mysql".equals(db)) {
                                String driver = "com.mysql.jdbc.Driver";
                                Class.forName(driver);
                                String urll = "jdbc:mysql://" + ip + "/" + instance;
                                con = DriverManager.getConnection(urll, user, pwd);
                                String s = " insert into " + tablename + "(" + fie + ") values(" + val + ")";
                                System.out.println("SQL:" + s);
                                stm = con.prepareStatement(s);
                                k = stm.executeUpdate(s);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (con != null) {
                                con.close();
                            }
                            if (stm != null) {
                                stm.close();
                            }
                            if (rs != null) {
                                rs.close();
                            }
                        }
                        if (k > 0) {
                            // 修改信息表
                            String s = " update AG_WA_METADATA_MODIFY set AUDITOR='" + loginName + "',is_del='',is_modi='',is_add='1',last_update='" + new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date()) + "' where id='" + data.get(0).get("id") + "' ";
                            int j = update_SQL(s, null);
                            if (j > 0) {
                                res.setSuccess(true);
                                res.setMessage("新增审核操作成功！");
                            } else {
                                res.setSuccess(false);
                                res.setMessage("新增审核操作失败！请联系系统管理员。");
                                // 跳出循环
                                break;
                            }
                        } else {
                            res.setSuccess(false);
                            res.setMessage("新增审核失败！请联系系统管理员。");
                            // 跳出循环
                            break;
                        }
                    }
                }
            }
        }
        // 执行操作       结束
        return res;
    }

    //同步更新数据表
    public ResultForm alertTableDesc(String url, String sql) throws Exception{
        ResultForm res = new ResultForm(false, "操作失败!");
        int i = 0;
        // 原始jdbc
        Connection con = null;
        Statement stm = null;
        ResultSet rs = null;
        try {
            String[] ur = url.split("\\|");
            String[] userName = ur[1].split("/");
            String[] hosts = ur[2].split("/");
            String db = ur[0];
            String user = userName[0];
            String pwd = userName[1];
            String ip = hosts[0];
            String instance = hosts[1];
            if ("oracle".equals(db)) {
                String driver = "oracle.jdbc.driver.OracleDriver";
                Class.forName(driver);
                String urll = "jdbc:oracle:thin:@" + ip + ":" + instance;
                con = DriverManager.getConnection(urll, user, pwd);
                String sq = sql;
                System.out.println("SQL:" + sq);
                stm = con.prepareStatement(sq);
                i = stm.executeUpdate(sq);
            } else if ("mysql".equals(db)) {
                String driver = "com.mysql.jdbc.Driver";
                Class.forName(driver);
                String urll = "jdbc:mysql://" + ip + "/" + instance;
                con = DriverManager.getConnection(urll, user, pwd);
                String sq = sql;
                System.out.println("SQL:" + sq);
                stm = con.prepareStatement(sq);
                i = stm.executeUpdate(sq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        if (i > 0) {
            // 修改信息表
           res.setSuccess(false);
           res.setMessage("新增审核操作失败！请联系系统管理员。");
                // 跳出循环

        } else {
            res.setSuccess(false);
            res.setMessage("新增审核失败！请联系系统管理员。");
            // 跳出循环
        }

        return res;
    }

    /*
     * 根据 字段id,获取字段信息
     * */
    public Map getFieldInfoByFieldId(String fieldId) throws Exception{
        String sql = " select name,cname,type from AG_WA_METADATA_FIELD where id = '" + fieldId + "'";
        return getMetaData_SQL(sql);
    }

    /*
     * 刪除審批修改記錄
     * */
    public void delectMetaModify(String val) throws Exception {
        // 刪除與審批相關的記錄 AG_WA_METADATA_MODIFY
        String sql = " delete from AG_WA_METADATA_MODIFY where table_name=? ";
        List values = new ArrayList();
        values.add(val);
        DBHelper.executeUpdate("spring.datasource", sql, values);
    }

}
