package com.augurit.agcloud.agcom.agsupport.sc.layer.dao;

import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;
import com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.util.JDBCUtil;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import com.common.dbcp.DBHelper;
import com.common.dbcp.DBPool;
import com.common.util.DBUtil;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.*;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-06-01.
 */
@Repository
public class AgLayerDao {
    public boolean saveVectorLayer(String dataSourceId, String layerTable, String pkColumn, String geometryColumn, Map<String, String> dataMap) {
        boolean isSuccess = false;
        Writer writer = null;
        Connection conn = null;
        DBUtil db = null;
        Statement stm=null;
        try {
            StringBuffer insertSql = new StringBuffer(" insert into ").append(layerTable);
            StringBuffer keySql = new StringBuffer(" (");
            StringBuffer valueSql = new StringBuffer(" (");
            String dbType = JDBCUtils.getDbType(dataSourceId);
            List values = new ArrayList();
            for (String key : dataMap.keySet()) {
                if (!key.equals(WKT_COLUMN) && !key.equals(geometryColumn)) {
                    keySql.append(key).append(", ");
                    if(dataMap.get(key)!="null"){
                        String[] dates=dataMap.get(key).split(",");
                        if(dates.length>1){
                            if(dates[0].indexOf("char")!=-1){
                                valueSql.append("\'"+dates[1]+"\'"+",");
                            }else if(dates[0].indexOf("date")!=-1){
                                String DATA=dates[1].split("\\.")[0];
                                valueSql.append(" to_date(\'"+DATA+"\','yyyy-mm-dd hh24:mi:ss'),");
                            }else {
                                valueSql.append(dates[1]+",");
                            }
                        } else {
                            valueSql.append(dataMap.get(key)+",");
                        }
                    }

                   // if(isNumeric(dataMap.get(key))||dataMap.get(key)=="null"){
                  //      valueSql.append(dataMap.get(key)+",");
                  //  }else {
                   //     valueSql.append("\'"+dataMap.get(key)+"\'"+",");
                   // }

                    values.add(dataMap.get(key));
                }
            }
            keySql.append(")");
            valueSql.append(")");
            insertSql.append(keySql.toString().replaceAll(",[ ]*\\)", " )")).append(" values ").append(valueSql.toString().replaceAll(",[ ]*\\)", " )"));
            conn = JDBCUtils.getConnection(dataSourceId);
            stm = conn.createStatement();
//            db = new DBUtil(conn);
//            db.setAutoCommit(false);
            stm.execute(insertSql.toString());
//            db = new DBUtil(DBPool.getConnection(dataSourceId));
//            db.executeUpdate(insertSql.toString(), values);
            if (dataMap.get(WKT_COLUMN) != null) {
                String wkt = dataMap.get(WKT_COLUMN).toUpperCase();
                String primaryKey = dataMap.get(pkColumn);
                StringBuffer updateSql = new StringBuffer(" update ").append(layerTable).append(" set ").append(geometryColumn).append(" = ");
                if (dbType.equals(DB_TYPE_ORACLE)||ORACLE.equals(dbType)) {
                    String srid = "1";
                    String sql = "select sde.st_srid(" + geometryColumn + ") srid from " + layerTable + " where shape is not null";
//                    Object data = db.findFirstColum(sql);
                    List<Map> data  =  JDBCUtils.find(dataSourceId,sql,null);
                    if (data != null) {
                        srid =data.get(0).get("srid").toString();
                    }
                    updateSql.append(" sde.st_geometry(? ,").append(srid).append(") ");
                } else if (dbType.equals(DB_TYPE_DB2)) {
                    String srid = "1";
                    String sql = "select SRID from layers where table_name='" + layerTable.toUpperCase() + "' ";
//                    Object data = db.findFirstColum(sql);
                    Object data= stm.execute(sql);
                    if (data != null) {
                        srid = data.toString();
                    }
                    if (wkt.startsWith(POINT)) {
                        updateSql.append(" db2gse.st_point(cast(? as clob),").append(srid).append(") ");
                    } else if (wkt.startsWith(MULTIPOINT)) {
                        updateSql.append(" db2gse.st_multipoint(cast(? as clob),").append(srid).append(") ");
                    } else if (wkt.startsWith(LINESTRING)) {
                        updateSql.append(" db2gse.st_linestring(cast(? as clob),").append(srid).append(") ");
                    } else if (wkt.startsWith(MULTILINESTRING)) {
                        updateSql.append(" db2gse.st_multilinestring(cast(? as clob),").append(srid).append(") ");
                    } else if (wkt.startsWith(POLYGON)) {
                        updateSql.append(" db2gse.st_polygon(cast(? as clob),").append(srid).append(") ");
                    } else if (wkt.startsWith(MULTIPOLYGON)) {
                        updateSql.append(" db2gse.st_multipolygon(cast(? as clob),").append(srid).append(") ");
                    }
                } else if (dbType.equals(DB_TYPE_MYSQL)) {
                    updateSql.append(" st_geomfromtext(cast(? as char)) ");
                } else {
                    updateSql.append(" st_geomfromtext(? ) ");
                }
                updateSql.append(" where ").append(pkColumn).append(" = '").append(primaryKey).append("'");
//                conn = DBPool.getConnection(dataSourceId);
                conn = JDBCUtils.getConnection(dataSourceId);
                PreparedStatement stmt = conn.prepareStatement(updateSql.toString());
//                stm.execute(updateSql.toString());
                if (dbType.equals(DB_TYPE_ORACLE)||ORACLE.equals(dbType)) {
                    Clob clob = conn.createClob();
                    writer = clob.setCharacterStream(0L);
                    writer.write(wkt.toCharArray());
                    writer.flush();
                    stmt.setClob(1, clob);
                } else if (dbType.equals(DB_TYPE_MYSQL)) {
                    stmt.setBytes(1, wkt.getBytes());
                } else {
                    stmt.setString(1, wkt);
                }
                stmt.executeUpdate();
                //db.executeUpdate(" delete from " + layerTable + " where " + geometryColumn + " is null ", new ArrayList());
            }
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (db != null) {
                db.close();
            }
        }
        return isSuccess;
    }

    public boolean updateVectorLayer(String dataSourceId, String layerTable, String pkColumn, String geometryColumn, Map<String, String> dataMap, String primaryKey) {
        boolean isSuccess = false;
        Writer writer = null;
        Connection conn = null;
        Statement stm=null;
//        DBUtil db = null;
        try {
            StringBuffer updateSql = new StringBuffer(" update ").append(layerTable).append(" set ");
            List values = new ArrayList();
            String dbType = JDBCUtils.getDbType(dataSourceId);
            for (String key : dataMap.keySet()) {
                if (!key.equals(WKT_COLUMN) && !key.equals(geometryColumn)) {
                    if(dataMap.get(key)!="null"){
                        String[] dates=dataMap.get(key).split(",");
                        if(dates.length>1){
                            if(dates[0].indexOf("char")!=-1){
                                updateSql.append(key).append(" =\'"+dates[1]+"\', ");
                            }else if(dates[0].indexOf("date")!=-1){
                                String DATA=dates[1].split("\\.")[0];
                                updateSql.append(key).append(" = to_date(\'"+DATA+"\','yyyy-mm-dd hh24:mi:ss'),");
                            }else {
                                updateSql.append(key).append(" = "+dates[1]+", ");
                            }
                        } else {
                            updateSql.append(key).append(" ="+dataMap.get(key)+", ");
                        }
                    }

                    values.add(dataMap.get(key));
                }
            }
            updateSql.append(" where ").append(pkColumn).append(" = "+primaryKey+"");
            values.add(primaryKey);
            String sql = updateSql.toString().replaceAll(",[ ]*where", " where");
            conn = JDBCUtils.getConnection(dataSourceId);
            stm = conn.createStatement();
//            db = new DBUtil(conn);
//            db.setAutoCommit(false);
                stm.execute(sql);
//            db = new DBUtil(conn);
//            db.setAutoCommit(false);
//            JDBCUtils.find(dataSourceId,sql,values);
//            db.executeUpdate(sql, values);
            if (dataMap.get(WKT_COLUMN) != null) {
                String wkt = dataMap.get(WKT_COLUMN);
                updateSql = new StringBuffer(" update ").append(layerTable).append(" set ").append(geometryColumn).append(" = ");
                if (dbType.equals(DB_TYPE_ORACLE)||ORACLE.equals(dbType)) {
                    updateSql.append(" sde.st_geometry(? ,sde.st_srid(").append(geometryColumn).append(")) ");
                } else if (dbType.equals(DB_TYPE_DB2)) {
                    if (wkt.startsWith(POINT)) {
                        updateSql.append(" db2gse.st_point(cast(? as clob),db2gse.st_srid(").append(geometryColumn).append(")) ");
                    } else if (wkt.startsWith(MULTIPOINT)) {
                        updateSql.append(" db2gse.st_multipoint(cast(? as clob),db2gse.st_srid(").append(geometryColumn).append(")) ");
                    } else if (wkt.startsWith(LINESTRING)) {
                        updateSql.append(" db2gse.st_linestring(cast(? as clob),db2gse.st_srid(").append(geometryColumn).append(")) ");
                    } else if (wkt.startsWith(MULTILINESTRING)) {
                        updateSql.append(" db2gse.st_multilinestring(cast(? as clob),db2gse.st_srid(").append(geometryColumn).append(")) ");
                    } else if (wkt.startsWith(POLYGON)) {
                        updateSql.append(" db2gse.st_polygon(cast(? as clob),db2gse.st_srid(").append(geometryColumn).append(")) ");
                    } else if (wkt.startsWith(MULTIPOLYGON)) {
                        updateSql.append(" db2gse.st_multipolygon(cast(? as clob),db2gse.st_srid(").append(geometryColumn).append(")) ");
                    }
                } else if (dbType.equals(DB_TYPE_MYSQL)) {
                    updateSql.append(" st_geomfromtext(cast(? as char)) ");
                } else {
                    updateSql.append(" st_geomfromtext(? ) ");
                }
                updateSql.append(" where ").append(pkColumn).append(" = '").append(primaryKey).append("'");
                PreparedStatement stmt = conn.prepareStatement(updateSql.toString());
//                stm.execute(updateSql.toString());
                if (dbType.equals(DB_TYPE_ORACLE)||ORACLE.equals(dbType)) {
                    Clob clob = conn.createClob();
                    writer = clob.setCharacterStream(0L);
                    writer.write(wkt.toCharArray());
                    writer.flush();
                    stmt.setClob(1, clob);
                } else if (dbType.equals(DB_TYPE_MYSQL)) {
                    stmt.setBytes(1, wkt.getBytes());
                } else {
                    stmt.setString(1, wkt);
                }
                stmt.executeUpdate();
            }
            isSuccess = true;
//            db.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
//            if (db != null) {
//                db.close();
//            }
        }
        return isSuccess;
    }

    public boolean deleteVectorLayer(String dataSourceId, String layerTable, String pkColumn, String primaryKey) {
        boolean isSuccess = false;
        Connection conn = null;
        Statement stm=null;
        try {
            StringBuffer deleteSql = new StringBuffer(" delete from ").append(layerTable).append(" where ");
            List values = new ArrayList();
            deleteSql.append(pkColumn).append(" = "+primaryKey +"");
            values.add(primaryKey);
            conn = JDBCUtils.getConnection(dataSourceId);
            stm = conn.createStatement();
//            db = new DBUtil(conn);
//            db.setAutoCommit(false);
            stm.execute(deleteSql.toString());
//            DBHelper.executeUpdate(dataSourceId, deleteSql.toString(), values);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public String getPrimaryKey(String dataSourceId, String layerTable, String pkColumn) throws Exception{
        String primaryKey = UUID.randomUUID().toString();
        String dbType = JDBCUtils.getDbType(dataSourceId);
        if (DB_TYPE_ORACLE.equals(dbType) || DB_TYPE_DB2.equals(dbType)|| ORACLE.equals(dbType)){
            String sql = "select max(" + pkColumn + ") max from " + layerTable;
//            Map data = DBHelper.findFirst(dataSourceId, sql, null);
            List<Map> data  =  JDBCUtils.find(dataSourceId,sql,null);
            if (data.size()>0) {
                primaryKey = String.valueOf(Integer.valueOf(data.get(0).get("max").toString()) + 1);
            } else {
                primaryKey = "1";
            }
        }
        return primaryKey;
    }
    public static boolean isNumeric(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        return isNum.matches();
    }

    }
