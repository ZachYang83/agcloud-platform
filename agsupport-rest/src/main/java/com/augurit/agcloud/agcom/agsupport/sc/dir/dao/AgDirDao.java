package com.augurit.agcloud.agcom.agsupport.sc.dir.dao;

import com.augurit.agcloud.agcom.agsupport.common.datasource.DynamicDataSource;
import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;
import com.common.dbcp.DBHelper;
import com.common.util.DBUtil;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.*;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-05-16.
 */
@Repository
public class AgDirDao {
 /*   public boolean checkLayerTable(String dataSourceId, String layerTable, String pkColumn, String geometryColumn) {
        DBUtil db = null;
        try {
            String sql;
            db = DBHelper.getDBUtil(dataSourceId);
            if (db.getDbType().equals(DB_TYPE_ORACLE)) {
                if (geometryColumn != null && !"".equals(geometryColumn)) {
                    sql = "select " + pkColumn + ", sde.st_astext(" + geometryColumn + ") shape from " + layerTable + " where rownum <= 1";
                } else {
                    sql = "select " + pkColumn + " from " + layerTable + " where rownum <= 1";
                }
            } else if (db.getDbType().equals(DB_TYPE_DB2)) {
                if (geometryColumn != null && !"".equals(geometryColumn)) {
                    sql = "select " + pkColumn + ", db2gse.st_astext(" + geometryColumn + ") shape from " + layerTable + " FETCH FIRST 1 ROWS ONLY";
                } else {
                    sql = "select " + pkColumn + " from " + layerTable + " FETCH FIRST 1 ROWS ONLY";
                }
            } else if (db.getDbType().equals(DB_TYPE_MYSQL)) {
                if (geometryColumn != null && !"".equals(geometryColumn)) {
                    sql = "select " + pkColumn + ", st_astext(" + geometryColumn + ") shape from " + layerTable + " limit 1,1";
                } else {
                    sql = "select " + pkColumn + " from " + layerTable + " limit 1,1";
                }
            } else {
                sql = "select " + pkColumn + ", st_astext(" + geometryColumn + ") shape from " + layerTable;
            }
            db.find(sql);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return true;
    }*/

    /**
     * 新版数据源检验
     * @param dataSourceId
     * @param layerTable
     * @param pkColumn
     * @param geometryColumn
     * @date 2019-01-17
     * @return
     */
    public boolean checkLayerTable(String dataSourceId, String layerTable, String pkColumn, String geometryColumn) {
        try {
            String sql;
            //db = DBHelper.getDBUtil(dataSourceId);
            //String dbType = JDBCUtils.getDbType(dataSourceId);
            String dbType = DynamicDataSource.getInstance().getDbType(dataSourceId);
            if (dbType.equals(ORACLE)) {
                if (geometryColumn != null && !"".equals(geometryColumn)) {
                    sql = "select " + pkColumn + ", sde.st_astext(" + geometryColumn + ") shape from " + layerTable + " where rownum <= 1";
                } else {
                    sql = "select " + pkColumn + " from " + layerTable + " where rownum <= 1";
                }
            } else if (dbType.equals(DB_TYPE_DB2)) {
                if (geometryColumn != null && !"".equals(geometryColumn)) {
                    sql = "select " + pkColumn + ", db2gse.st_astext(" + geometryColumn + ") shape from " + layerTable + " FETCH FIRST 1 ROWS ONLY";
                } else {
                    sql = "select " + pkColumn + " from " + layerTable + " FETCH FIRST 1 ROWS ONLY";
                }
            } else if (dbType.equals(MYSQL)) {
                if (geometryColumn != null && !"".equals(geometryColumn)) {
                    sql = "select " + pkColumn + ", st_astext(" + geometryColumn + ") shape from " + layerTable + " limit 1,1";
                } else {
                    sql = "select " + pkColumn + " from " + layerTable + " limit 1,1";
                }
            } else {
                sql = "select " + pkColumn + ", st_astext(" + geometryColumn + ") shape from " + layerTable;
            }
           // db.find(sql);
            List<Map> mapList = JDBCUtils.find(dataSourceId, sql, null);
            return mapList.size() > 0;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
