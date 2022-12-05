package com.augurit.agcloud.agcom.agsupport.sc.spatial.query.dao;

import com.augurit.agcloud.agcom.agsupport.common.datasource.DynamicDataSource;
import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;
import com.augurit.agcloud.agcom.agsupport.common.mongodb.MongoDbService;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Range;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.MongoDBHelp;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import com.common.dbcp.DBHelper;
import com.common.util.ReflectBeans;
import com.common.util.ReflectMap;
import com.github.pagehelper.Page;
import com.ibm.db2.jcc.am.le;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.*;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-04-14.
 */
@Repository
public class AgDBQueryDao {

    @Autowired
    private MongoDbService mongoDbService;

    public String getQuerySql(Layer layer) throws Exception {
        layer.setDataSourceType(JDBCUtils.getDbType(layer.getDataSourceId()));
        StringBuffer sql = new StringBuffer("select ");
        if (layer.getFields() != null && layer.getFields().size() > 0) {
            for (Object field : layer.getFields()) {
                sql.append(field.toString()).append(", ");
            }
        } else {
            sql.append("a.*, ");
        }
        if (layer.getxColumn() != null && layer.getyColumn() != null) {
            if (layer.getDataSourceType().equals(DB_TYPE_MYSQL)) {
                sql.append(" concat('POINT  ( ',").append(layer.getxColumn()).append(",' ',").append(layer.getyColumn()).append(",')') ");
            } else {
                sql.append(" 'POINT  ( '||").append(layer.getxColumn()).append("||' '||").append(layer.getyColumn()).append("||')' ");
            }
            sql.append(WKT_COLUMN);
        } else if (layer.getWktColumn() != null) {
            sql.append(" ").append(layer.getWktColumn()).append(" ").append(WKT_COLUMN);
        } else if (layer.getGeometryColumn() != null) {
            if (DB_TYPE_ORACLE.equals(layer.getDataSourceType()) || ORACLE.equals(layer.getDataSourceType().toLowerCase())) {
                sql.append(" sde.st_astext(").append(layer.getGeometryColumn()).append(") ").append("\""+WKT_COLUMN+"\"");
                //} else if (DB_TYPE_POSTGIS.equals(layer.getDataSourceType())) {
            }else if(POSTGRESQL.equals(layer.getDataSourceType()) || DB_TYPE_POSTGIS.equals(layer.getDataSourceType().toLowerCase())){
                sql.append(" st_astext(").append(layer.getGeometryColumn()).append(") ").append(WKT_COLUMN);
            } else if (DB_TYPE_MYSQL.equals(layer.getDataSourceType().toLowerCase())) {
                sql.append(" st_astext(").append(layer.getGeometryColumn()).append(") ").append(WKT_COLUMN);
            } else if (DB_TYPE_DB2.equals(layer.getDataSourceType().toLowerCase())) {
                sql.append(" db2gse.st_astext(").append(layer.getGeometryColumn()).append(") ").append(WKT_COLUMN);
            }
        }
        sql.append(" from ").append(layer.getLayerTable()).append(" a ");
        if (layer.getWhere() != null) {
            sql.append(" where ").append(URLDecoder.decode(layer.getWhere(), "utf-8"));
        }
        return sql.toString();
    }

    public String getQuerySql2(Layer layer,String select) {
        StringBuffer sql = new StringBuffer(select);
        if (layer.getxColumn() != null && layer.getyColumn() != null) {
            if (layer.getDataSourceType().equals(DB_TYPE_MYSQL)) {
                sql.append(" concat('POINT  ( ',").append(layer.getxColumn()).append(",' ',").append(layer.getyColumn()).append(",')') ");
            } else {
                sql.append(" 'POINT  ( '||").append(layer.getxColumn()).append("||' '||").append(layer.getyColumn()).append("||')' ");
            }
            sql.append(WKT_COLUMN);
        } else if (layer.getWktColumn() != null) {
            sql.append(" ").append(layer.getWktColumn()).append(" ").append(WKT_COLUMN);
        } else if (layer.getGeometryColumn() != null) {
            if (DB_TYPE_ORACLE.equals(layer.getDataSourceType())) {
                sql.append(" sde.st_astext(").append(layer.getGeometryColumn()).append(") ").append(WKT_COLUMN);
            } else if (DB_TYPE_POSTGIS.equals(layer.getDataSourceType())) {
                sql.append(" st_astext(").append(layer.getGeometryColumn()).append(") ").append(WKT_COLUMN);
            } else if (DB_TYPE_MYSQL.equals(layer.getDataSourceType())) {
                sql.append(" st_astext(").append(layer.getGeometryColumn()).append(") ").append(WKT_COLUMN);
            } else if (DB_TYPE_DB2.equals(layer.getDataSourceType())) {
                sql.append(" db2gse.st_astext(").append(layer.getGeometryColumn()).append(") ").append(WKT_COLUMN);
            }
        }
        sql.append(" from ").append(layer.getLayerTable()).append(" a ");
        if (layer.getWhere() != null) {
            sql.append(" where ").append(layer.getWhere());
        }
        return sql.toString();
    }

    public String getQuerySqlMDB(Layer layer) {
        String key = SpatialUtil.getKey(layer.getDataSourceId(), layer.getLayerTable());
        StringBuffer sql = new StringBuffer("select ");
        if (layer.getFields() != null && layer.getFields().size() > 0) {
            for (Object field : layer.getFields()) {
                sql.append(field.toString()).append(", ");
            }
        } else {
            sql.append("a.*, ");
        }
        sql.append(GEO_COLUMN).append(", ").append(WKT_COLUMN).append(" from ").append(key).append(" a ");
        if (layer.getWhere() != null) {
            sql.append(" where ").append(layer.getWhere());
        }
        String sqlStr = sql.toString();
        if (layer.getValues() != null) {
            for (Object value : layer.getValues()) {
                sqlStr = sqlStr.replaceFirst("[?]", value.toString());
            }
        }
        return sqlStr;
    }

    public List<Map> queryAllBySQL(Layer layer) throws Exception{
        List<Map> result = new ArrayList<Map>();
        layer.setDataSourceType(JDBCUtils.getDbType(layer.getDataSourceId()));
        try {
            List<Map> mapList = JDBCUtils.find(layer.getDataSourceId(), getQuerySql(layer), layer.getValues());
            //result = DBHelper.find(layer.getDataSourceId(), getQuerySql(layer), layer.getValues());
            result = SpatialUtil.formatDataMapList(mapList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public List<Map> queryAllByMDB(Layer layer) {
        List<Map> result = new ArrayList<Map>();
        try {
            String sqlStr = getQuerySqlMDB(layer);
            result = mongoDbService.find(sqlStr);
            //result = MongoDBHelp.find(sqlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map> queryLimitBySQL(Layer layer, int skip, int limit) {
        List<Map> result = new ArrayList<Map>();
        try {
            StringBuffer sql = new StringBuffer("select ");
            List values = new ArrayList();
            if (layer.getFields() != null && layer.getFields().size() > 0) {
                for (Object field : layer.getFields()) {
                    sql.append(field.toString()).append(", ");
                }
            } else {
                sql.append("a.*, ");
            }
            if (layer.getxColumn() != null && layer.getyColumn() != null) {
                if (layer.getDataSourceType().equals(DB_TYPE_MYSQL)) {
                    sql.append(" concat('POINT  ( ',").append(layer.getxColumn()).append(",' ',").append(layer.getyColumn()).append(",')') ");
                } else {
                    sql.append(" 'POINT  ( '||").append(layer.getxColumn()).append("||' '||").append(layer.getyColumn()).append("||')' ");
                }
                sql.append(WKT_COLUMN);
            } else if (layer.getWktColumn() != null) {
                sql.append(" ").append(layer.getWktColumn()).append(" ").append(WKT_COLUMN);
            } else if (layer.getGeometryColumn() != null) {
                if (DB_TYPE_ORACLE.equals(layer.getDataSourceType())) {
                    sql.append(" sde.st_astext(").append(layer.getGeometryColumn()).append(") ").append(WKT_COLUMN);
                } else if (DB_TYPE_POSTGIS.equals(layer.getDataSourceType())) {
                    sql.append(" st_astext(").append(layer.getGeometryColumn()).append(") ").append(WKT_COLUMN);
                } else if (DB_TYPE_MYSQL.equals(layer.getDataSourceType())) {
                    sql.append(" st_astext(").append(layer.getGeometryColumn()).append(") ").append(WKT_COLUMN);
                } else if (DB_TYPE_DB2.equals(layer.getDataSourceType())) {
                    sql.append(" db2gse.st_astext(").append(layer.getGeometryColumn()).append(") ").append(WKT_COLUMN);
                }
            }
            sql.append(" from ").append(layer.getLayerTable()).append(" a ");
            if (layer.getDataSourceType().equals(DB_TYPE_MYSQL) && skip >= 5000) {
                sql.append(" inner join (select ").append(layer.getPkColumn()).append(" from ").append(layer.getLayerTable())
                        .append(" limit ?,?) b on a.").append(layer.getPkColumn()).append(" = b.").append(layer.getPkColumn());
                values.add(skip);
                values.add(limit);

            } else if (layer.getDataSourceType().equals(DB_TYPE_ORACLE)) {
                sql.append(" where ").append(layer.getPkColumn()).append(" is not null ");
                if (layer.getWhere() != null) {
                    sql.append(" and ").append(layer.getWhere());
                }
                if (layer.getValues() != null && layer.getValues().size() > 0) {
                    values.addAll(layer.getValues());
                }
                sql = new StringBuffer(" select * from (select b.*, rownum rn from (").append(sql).append(" ) b where rownum <= ? ) where rn > ?");
                values.add(skip + limit);
                values.add(skip);
            } else if (layer.getDataSourceType().equals(DB_TYPE_DB2)) {
                sql.append(" where ").append(layer.getPkColumn()).append(" is not null ");
                if (layer.getWhere() != null) {
                    sql.append(" and ").append(layer.getWhere());
                }
                if (layer.getValues() != null && layer.getValues().size() > 0) {
                    values.addAll(layer.getValues());
                }
                sql = new StringBuffer(" select * from (select b.*,rownumber() over() as rowid from (").append(sql).append(" ) b ) where rowid > ? and rowid <= ?");
                values.add(skip);
                values.add(skip + limit);

            } else {
                sql.append(" where ").append(layer.getPkColumn()).append(" is not null ");
                if (layer.getWhere() != null) {
                    sql.append(" and ").append(layer.getWhere());
                }
                if (layer.getValues() != null && layer.getValues().size() > 0) {
                    values.addAll(layer.getValues());
                }
                sql.append(" limit ?,? ");
                values.add(skip);
                values.add(limit);
            }
            result = DBHelper.find(layer.getDataSourceId(), sql.toString(), values);
            result = SpatialUtil.formatDataMapList(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 创建搜索视图，默认字sde里面创建
     * @param sql
     */
    public void createView(String sql) throws Exception {
        DBHelper.executeUpdate("sde",sql,null);
    }

    /**
     * 全局搜索使用，跟上一个方法类似，不查空间字段
     * @param layer
     * @param skip
     * @param limit
     * @return
     */
    public List<Map> queryLimitBySQL2(Layer layer, int skip, int limit) {
        List<Map> result = new ArrayList<Map>();
        try {
            StringBuffer sql = new StringBuffer("select ");
            List values = new ArrayList();
            int index = 0;
            if (layer.getFields() != null && layer.getFields().size() > 0) {
                for (Object field : layer.getFields()) {
                    if(index == layer.getFields().size() - 1){
                        sql.append(field.toString());
                    }else{
                        sql.append(field.toString()).append(", ");
                    }
                    index++;
                }
            } else {
                sql.append("a.*");
            }
            sql.append(" from ").append(layer.getLayerTable()).append(" a ");
            if (layer.getDataSourceType().equals(DB_TYPE_MYSQL) && skip >= 5000) {
                sql.append(" inner join (select ").append(layer.getPkColumn()).append(" from ").append(layer.getLayerTable())
                        .append(" limit ?,?) b on a.").append(layer.getPkColumn()).append(" = b.").append(layer.getPkColumn());
                values.add(skip);
                values.add(limit);

            } else if (layer.getDataSourceType().equals(DB_TYPE_ORACLE)) {
                sql.append(" where ").append(layer.getPkColumn()).append(" is not null ");
                if (layer.getWhere() != null) {
                    sql.append(" and ").append(layer.getWhere());
                }
                if (layer.getValues() != null && layer.getValues().size() > 0) {
                    values.addAll(layer.getValues());
                }
                sql = new StringBuffer(" select * from (select b.*, rownum rn from (").append(sql).append(" ) b where rownum <= ? ) where rn > ?");
                values.add(skip + limit);
                values.add(skip);
            } else if (layer.getDataSourceType().equals(DB_TYPE_DB2)) {
                sql.append(" where ").append(layer.getPkColumn()).append(" is not null ");
                if (layer.getWhere() != null) {
                    sql.append(" and ").append(layer.getWhere());
                }
                if (layer.getValues() != null && layer.getValues().size() > 0) {
                    values.addAll(layer.getValues());
                }
                sql = new StringBuffer(" select * from (select b.*,rownumber() over() as rowid from (").append(sql).append(" ) b ) where rowid > ? and rowid <= ?");
                values.add(skip);
                values.add(skip + limit);

            } else {
                sql.append(" where ").append(layer.getPkColumn()).append(" is not null ");
                if (layer.getWhere() != null) {
                    sql.append(" and ").append(layer.getWhere());
                }
                if (layer.getValues() != null && layer.getValues().size() > 0) {
                    values.addAll(layer.getValues());
                }
                sql.append(" limit ?,? ");
                values.add(skip);
                values.add(limit);
            }
            result = DBHelper.find(layer.getDataSourceId(), sql.toString(), values);
            result = SpatialUtil.formatDataMapList(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 全局搜索使用，跟上一个方法类似
     * @param layer
     * @return
     */
    public List<Map> queryAllBySQL2(Layer layer,String select) {
        List<Map> result = new ArrayList<Map>();
        try {
            result = DBHelper.find(layer.getDataSourceId(), getQuerySql2(layer,select), layer.getValues());
//            result = SpatialUtil.formatDataMapList(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map queryPkBySQL(Layer layer, String primaryKey) {
        Map result = new HashMap();
        try {
            StringBuffer where = new StringBuffer(layer.getPkColumn()).append(" = ?");
            List values = new ArrayList();
            values.add(primaryKey);
            if (layer.getWhere() != null) {
                where.append(" and ").append(layer.getWhere());
            }
            if (layer.getValues() != null && layer.getValues().size() > 0) {
                values.addAll(layer.getValues());
            }
            layer.setWhere(where.toString());
            layer.setValues(values);
            List<Map> resultList = queryAllBySQL(layer);
            if (resultList != null && resultList.size() > 0) {
                result = resultList.get(0);
            }
            result = SpatialUtil.formatDataMap(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map> queryPksBySQL(Layer layer, List<String> pkList) {
        List<Map> result = new ArrayList<Map>();
        try {
            if (pkList != null && pkList.size() > 0) {
                StringBuffer where = new StringBuffer(layer.getPkColumn()).append(" in (");
                for (int i = 0; i < pkList.size(); i++) {
                    where.append("?");
                    if (i == pkList.size() - 1) {
                        where.append(" )");
                    } else {
                        where.append(", ");
                    }
                }
                if (layer.getWhere() != null) {
                    where.append(" and ").append(layer.getWhere());
                }
                if (layer.getValues() != null && layer.getValues().size() > 0) {
                    pkList.addAll(layer.getValues());
                }
                layer.setWhere(where.toString());
                layer.setValues(pkList);
                result = queryAllBySQL(layer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map> queryOvalBySQL(Layer layer, Point point, double distance) {
        List<Map> result = new ArrayList<Map>();
        try {
            List values = new ArrayList();
            StringBuffer where = new StringBuffer();
            if (layer.getxColumn() != null && layer.getyColumn() != null) {
                where.append(" (").append(point.x).append(" - ").append(layer.getxColumn()).append(" ) * (")
                        .append(point.x).append(" - ").append(layer.getxColumn()).append(" ) + (").append(point.y)
                        .append(" - ").append(layer.getyColumn()).append(" ) * (").append(point.y).append(" - ")
                        .append(layer.getyColumn()).append(" ) <= ?");
                values.add(distance * distance);
            }
            if (layer.getWhere() != null) {
                where.append(where == new StringBuffer() ? "" : " and ").append(layer.getWhere());
            }
            if (layer.getValues() != null && layer.getValues().size() > 0) {
                values.addAll(layer.getValues());
            }
            layer.setWhere(where.toString());
            layer.setValues(values);
            result = queryAllBySQL(layer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map> queryRangeBySQL(Layer layer, Range range) {
        List<Map> result = new ArrayList<Map>();
        try {
            List values = new ArrayList();
            StringBuffer where = new StringBuffer();
            if (layer.getxColumn() != null && layer.getyColumn() != null) {
                where.append(layer.getxColumn()).append(" >= ? and ").append(layer.getxColumn()).append(" <= ? and ")
                        .append(layer.getyColumn()).append(" >= ? and ").append(layer.getyColumn()).append(" <= ?");
                values.add(range.getMinX());
                values.add(range.getMaxX());
                values.add(range.getMinY());
                values.add(range.getMaxY());
            }
            if (layer.getWhere() != null) {
                where.append(where == new StringBuffer() ? "" : " and ").append(layer.getWhere());
            }
            if (layer.getValues() != null && layer.getValues().size() > 0) {
                values.addAll(layer.getValues());
            }
            layer.setWhere(where.toString());
            layer.setValues(values);
            result = queryAllBySQL(layer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map> queryRangeByMDB(Layer layer, Range range) {
        List<Map> result = new ArrayList<Map>();
        try {
            List values = new ArrayList();
            StringBuffer where = new StringBuffer();
            if (POINT.equals(layer.getLayerType())) {
                where.append(MINX_COLUMN).append(" >= ? and ").append(MAXX_COLUMN).append(" <= ? and ")
                        .append(MINY_COLUMN).append(" >= ? and ").append(MAXY_COLUMN).append(" <= ?");
                values.add(range.getMinX());
                values.add(range.getMaxX());
                values.add(range.getMinY());
                values.add(range.getMaxY());
            } else {
                where.append("(").append(MINX_COLUMN).append(" >= ? or ").append(MAXX_COLUMN).append(" >= ? ) and (")
                        .append(MINX_COLUMN).append(" <= ? or ").append(MAXX_COLUMN).append(" <= ? ) and (")
                        .append(MINY_COLUMN).append(" >= ? or ").append(MAXY_COLUMN).append(" >= ? ) and (")
                        .append(MINY_COLUMN).append(" <= ? or ").append(MAXY_COLUMN).append(" <= ? ) ");
                values.add(range.getMinX());
                values.add(range.getMinX());
                values.add(range.getMaxX());
                values.add(range.getMaxX());
                values.add(range.getMinY());
                values.add(range.getMinY());
                values.add(range.getMaxY());
                values.add(range.getMaxY());
            }
            if (layer.getWhere() != null) {
                where.append(" and ").append(layer.getWhere());
            }
            if (layer.getValues() != null && layer.getValues().size() > 0) {
                values.addAll(layer.getValues());
            }
            layer.setWhere(where.toString());
            layer.setValues(values);
            result = queryAllByMDB(layer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map> queryAttrTableBySQL(Layer attrTable, Page page) {
        List<Map> result = new ArrayList<Map>();
        List values = new ArrayList();
        try {
            StringBuffer sql = new StringBuffer("select ");
            if (attrTable.getFields() != null && attrTable.getFields().size() > 0) {
                int i = 0;
                for (Object field : attrTable.getFields()) {
                    if (i == 0) {
                        sql.append(field.toString());
                        i++;
                    } else {
                        sql.append(",").append(field.toString());
                    }
                }
            } else {
                sql.append("a.*");
            }
            sql.append(" from ").append(attrTable.getLayerTable()).append(" a");
            if (attrTable.getWhere() != null) {
                sql.append(" where ").append(attrTable.getWhere());
            }
            if (page != null) {
                if (page.getOrderBy() != null) {
                    sql.append(" order by ").append(page.getOrderBy());
                }
                if (page.getStartRow() < page.getEndRow()) {
                    if (DB_TYPE_MYSQL.equals(attrTable.getDataSourceType())) {
                        sql.append(" limit ?,?");
                        values.add(page.getStartRow());
                        values.add(page.getEndRow() - page.getStartRow());
                    } else if (DB_TYPE_ORACLE.equals(attrTable.getDataSourceType())) {
                        sql = new StringBuffer(" select * from (select b.*, rownum rn from (").append(sql).append(" ) b where rownum <= ? ) where rn > ?");
                        values.add(page.getEndRow() - page.getStartRow());
                        values.add(page.getStartRow());
                    } else {
                        sql.append(" limit ?,?");
                        values.add(page.getStartRow());
                        values.add(page.getEndRow() - page.getStartRow());
                    }
                }
            }
            result = DBHelper.find(attrTable.getDataSourceId(), sql.toString(), values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map> LinkedQueryBySQL(AgLayer attrTable, AgLayer spaTable, Layer condition) {
        List<Map> result = new ArrayList<Map>();
        try {
            Layer layer = ReflectBeans.copy(spaTable, Layer.class);
            StringBuffer sql = new StringBuffer("select ");
            if (condition.getFields() != null && condition.getFields().size() > 0) {
                List fields = condition.getFields();
                for (int i = 0; i < fields.size(); i++) {
                    if (i == fields.size() - 1) {
                        sql.append("a." + fields.get(i));
                    } else {
                        sql.append("a." + fields.get(i)).append(", ");
                    }
                }
            } else {
                sql.append("a.* ");
            }
            if (layer.getGeometryColumn() != null) {
                if (DB_TYPE_ORACLE.equals(layer.getDataSourceType())) {
                    sql.append(", sde.st_astext(b.").append(spaTable.getGeometryColumn()).append(") ").append(WKT_COLUMN);
                } else if (DB_TYPE_POSTGIS.equals(layer.getDataSourceType())) {
                    sql.append(", st_astext(b.").append(spaTable.getGeometryColumn()).append(") ").append(WKT_COLUMN);
                } else if (DB_TYPE_MYSQL.equals(layer.getDataSourceType())) {
                    sql.append(", st_astext(b.").append(spaTable.getGeometryColumn()).append(") ").append(WKT_COLUMN);
                } else if (DB_TYPE_DB2.equals(layer.getDataSourceType())) {
                    sql.append(", db2gse.st_astext(b.").append(spaTable.getGeometryColumn()).append(") ").append(WKT_COLUMN);
                }
            }
            sql.append(" from ").append(attrTable.getLayerTable()).append(" a, ").append(spaTable.getLayerTable()).append(" b ");
            sql.append(" where ").append("a." + attrTable.getAttrTableField() + " = b." + attrTable.getSpatialTableField());
            if (layer.getWhere() != null) {
                sql.append(" and ").append(layer.getWhere());
            }
            if (condition.getWhere() != null) {
                sql.append(" and ").append(condition.getWhere());
            }
            result = DBHelper.find(layer.getDataSourceId(), sql.toString(), layer.getValues());
            result = SpatialUtil.formatDataMapList(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map> querySpatialTableByMDB(AgLayer spaTable, String spatialTableField) {
        List<Map> result = new ArrayList<>();
        try {
            Layer layer = ReflectBeans.copy(spaTable, Layer.class);
            String key = SpatialUtil.getKey(layer.getDataSourceId(), layer.getLayerTable());
            StringBuffer sql = new StringBuffer("select " + spatialTableField).append(",").append(GEO_COLUMN + ",").append(WKT_COLUMN);
            sql.append(" from ").append(key);
            if (layer.getWhere() != null) {
                sql.append(" where ").append(layer.getWhere());
            }
            //result = MongoDBHelp.find(sql.toString());
            result = mongoDbService.find(sql.toString());
            result = SpatialUtil.formatDataMapList(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Map> querySpatialTableBySQL(AgLayer spaTable, String spatialTableField) {
        List<Map> result = new ArrayList<>();
        try {
            Layer layer = ReflectBeans.copy(spaTable, Layer.class);
            StringBuffer sql = new StringBuffer("select b." + spatialTableField).append(",");
            if (layer.getGeometryColumn() != null) {
                if (DB_TYPE_ORACLE.equals(layer.getDataSourceType())) {
                    sql.append(" sde.st_astext(b.").append(spaTable.getGeometryColumn()).append(") ").append(WKT_COLUMN);
                } else if (DB_TYPE_POSTGIS.equals(layer.getDataSourceType())) {
                    sql.append(" st_astext(b.").append(spaTable.getGeometryColumn()).append(") ").append(WKT_COLUMN);
                } else if (DB_TYPE_MYSQL.equals(layer.getDataSourceType())) {
                    sql.append(" st_astext(b.").append(spaTable.getGeometryColumn()).append(") ").append(WKT_COLUMN);
                } else if (DB_TYPE_DB2.equals(layer.getDataSourceType())) {
                    sql.append(" db2gse.st_astext(b.").append(spaTable.getGeometryColumn()).append(") ").append(WKT_COLUMN);
                }
            }
            sql.append(" from ").append(spaTable.getLayerTable()).append(" b");
            if (layer.getWhere() != null) {
                sql.append(" where ").append(layer.getWhere());
            }
            result = DBHelper.find(layer.getDataSourceId(), sql.toString(), layer.getValues());
            result = SpatialUtil.formatDataMapList(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}