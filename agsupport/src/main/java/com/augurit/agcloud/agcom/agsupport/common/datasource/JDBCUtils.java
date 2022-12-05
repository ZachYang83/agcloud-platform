package com.augurit.agcloud.agcom.agsupport.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.GetConnectionTimeoutException;
import com.augurit.agcloud.agcom.agsupport.sc.dataAudit.util.ConnTimeOutUtil;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.common.thread.Executer;
import com.common.thread.Job;
import com.common.util.Common;
import com.common.util.page.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.*;

/**
 * @author zhangmingyang
 * @Description: jdbc工具类
 * @date 2019-01-10 16:03
 */
public class JDBCUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCUtils.class);
    private static Pattern order = Pattern.compile("(?i)order\\s*by(\\s*,?\\s*[^\\s]+(\\s*(desc|asc)?))+", 2);
    /**
     * 获取查询的sql
     * @param layer
     * @return
     * @throws Exception
     */
    public static String getQuerySql(Layer layer) throws Exception {
        String dbType = getDbType(layer.getDataSourceId());
        layer.setDataSourceType(dbType);
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

    /**
     * 是否存在表
     * @param dataSourceId
     * @param tableName
     * @return
     */
    public static boolean isExistTable(String dataSourceId,String tableName){
        System.out.println(tableName);
        long startTime = System.currentTimeMillis();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnTimeOutUtil.getConnTimeOut(dataSourceId);
            //conn = JDBCUtils.getConnection(dataSourceId);
            if (conn==null){
                System.out.println("数据源id为："+dataSourceId+"连接失败");
                return false;
            }
            ps = conn.prepareStatement("select count(*) from user_tables where table_name =upper('"+tableName+"')");
            rs = ps.executeQuery();
            while (rs.next()){
                int count = rs.getInt(1);
                if (count>0) return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            long endTime = System.currentTimeMillis();
            System.out.println("时间;"+(endTime-startTime)/1000+" 秒");
            close(conn,ps,rs);
        }
        return false;
    }

    public static List<Map> find(String dataSourceId, String sql, List values){
        List<Map> result = new ArrayList<Map>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Map> list = new ArrayList();
        try {
            String dbType = getDbType(dataSourceId);
            connection = getConnection(dataSourceId);
            preparedStatement = connection.prepareStatement(sql);
            if (values != null){
                setParameter(preparedStatement,values);
            }
            resultSet = preparedStatement.executeQuery();
            result = JDBCUtils.dataPackaging(resultSet,dbType, -1);
            // 字段转小写
            list = convShort(result);


        }catch (Exception e){
            LOGGER.error("查询异常sql:"+sql);
            e.printStackTrace();
        }finally {
            close(connection,preparedStatement,resultSet);
        }

        return list;
    }

    public static void each(String datasourceId, String sql, List values, int limit, Job job) throws Exception {
        Executer exe = null;

        try {
            long total = countResult(datasourceId, sql, values);
            int page = limit == 0 ? 0 : (int)Math.ceil((double)total / (double)limit);
            int size = limit > 100 ? 100 : limit;
            exe = new Executer(size);
            job.setExecuter(exe);

            for(int i = 1; i <= page && !exe.isStop(); ++i) {
                Pager pager = new Pager(i, limit, "");
                List<Map> list = findPage(datasourceId, pager, sql, values, false);
                if (list.size() == 0) {
                    break;
                }

                for(int j = 0; j < list.size() && !exe.isStop(); ++j) {
                    Map data = list.get(j);
                    Job clone = job.clone();
                    clone.setData(data, i, j, total);
                    exe.fork(clone);
                }
            }

            exe.waitlock();
        } catch (Exception var19) {
            throw var19;
        } finally {
            if (exe != null) {
                exe.close();
            }

        }

    }

    public static List<Map> findPage(String datasourceId, Pager pager, String sql, List params, boolean count) throws Exception {
        if (count) {
            long totalcount = countResult(datasourceId, sql, params);
            pager.setTotalElements(totalcount);
        }
        String pageSql = generatePageSql(getDbType(datasourceId), pager, sql);
        List<Map> data = find(datasourceId,pageSql, params);
        return data;
    }

    public static long countResult(String datasourceId, String sql, List params) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long count = 0;
        try {
            String dbType = getDbType(datasourceId);
            connection = getConnection(datasourceId);
            String countSql = prepareCount(sql);
            statement = connection.prepareStatement(countSql);
            if (params != null){
                setParameter(statement,params);
            }
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                count = resultSet.getLong(1);
            }
        } catch (Exception e){
            LOGGER.error("查询数据记录数出错! 数据源id：+"+datasourceId+" sql : "+sql);
            e.printStackTrace();
        } finally {
            close(connection,statement,resultSet);
        }


        return count;
    }

    public static String generatePageSql(String dbType, Pager pager, String sql) {
        StringBuilder sb = new StringBuilder();
        int start = pager.getStart();
        int limit = pager.getPageSize();
        if (!Common.isCheckNull(pager.getOrderString())) {
            sql = sql + pager.getOrderString();
        }

        if (DbConstants.ORACLE.equals(dbType.toLowerCase())) {
            sb.append("select * from ( select tmp_tb.*, ROWNUM row_id from (");
            sb.append(sql);
            sb.append(" ) tmp_tb where ROWNUM <= ");
            sb.append(start + limit);
            sb.append(" ) where row_id > ");
            sb.append(start);
        } else if ("der".equals(dbType)) {
            sb.append(sql);
            sb.append(" OFFSET ").append(start);
            sb.append(" ROWS FETCH NEXT ").append(limit);
            sb.append(" ROWS ONLY ");
        } else if ("db2".equals(dbType)) {
            sb.append("select * from ( select tmp_tb.*, ROW_NUMBER() OVER() AS row_id from (");
            sb.append(sql);
            sb.append(" ) tmp_tb ");
            sb.append(" ) where row_id > ");
            sb.append(start);
            sb.append(" and row_id <= ");
            sb.append(start + limit);
        } else if (DbConstants.MYSQL.equals(dbType)) {
            sb.append(sql);
            sb.append(" LIMIT " + start + "," + limit);
        } else if (DbConstants.POSTGRESQL.equals(dbType)) {
            sb.append(sql);
            sb.append(" LIMIT " + limit + " offset " + start);
        }

        return sb.toString();
    }

    private static String prepareCount(String orgHql) {
        String countSql = "select count(1) " + removeSelect(removeOrders(orgHql));
        return countSql;
    }

    private static String removeSelect(String sql) {
        if (sql.matches("^(?i)\\s*select\\s*distinct.*$")) {
            sql = "select * from (" + sql + ") t";
        }

        int beginPos = sql.toLowerCase().indexOf("from");
        return sql.substring(beginPos);
    }

    private static String removeOrders(String hql) {
        Matcher m = order.matcher(hql);
        StringBuffer sb = new StringBuffer();

        while(m.find()) {
            m.appendReplacement(sb, "");
        }

        m.appendTail(sb);
        return sb.toString();
    }


    public static void close( Connection connection, PreparedStatement preparedStatement,ResultSet resultSet){

        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.error("ResultSet 关闭异常!");
                e.printStackTrace();
            }
        }
        if (preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                LOGGER.error("PreparedStatement关闭异常!");
                e.printStackTrace();
            }
        }
        if (connection != null){
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.error("druid 关闭connection异常!");
                e.printStackTrace();
            }
        }

    }

    public static String getDbType(String dataSourceId) throws Exception {
        return DynamicDataSource.getInstance().getDbType(dataSourceId);
    }

    public static Connection getConnection(String dataSourceId) throws Exception{
        DataSource dataSource = DynamicDataSource.getInstance().getDataSource(dataSourceId);
        DruidDataSource druidDataSource = (DruidDataSource)dataSource;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        }catch (GetConnectionTimeoutException e){
            LOGGER.error("数据库连接超时！，数据库用户："+druidDataSource.getUsername()+"  url:"+druidDataSource.getUrl());
        }catch (SQLException e){
            LOGGER.error("数据库连接异常！，数据库用户："+druidDataSource.getUsername()+"  url:"+druidDataSource.getUrl());
        }
        return connection;
    }

    public static void setParameter(PreparedStatement ps, List varList) throws SQLException {
        if (varList != null) {
            for(int i = 1; i <= varList.size(); ++i) {
                Object obj = varList.get(i - 1);
                setParameter(ps, i, obj);
            }
        }
    }

    public static List dataPackaging(ResultSet resultSet,String dbType, int num) throws Exception {
        List result = new ArrayList();
        ResultSetMetaData rsMetaData = resultSet.getMetaData();

        while((num < 0 || num-- > 0) && resultSet.next()) {
            result.add(mapRow(resultSet,dbType, rsMetaData));
        }

        return result;
    }

    public static List dataPackaging(ResultSet resultSet,String dbType, int startIndex, int num) throws Exception {
        while(startIndex-- > 0 && resultSet.next()) {
        }

        return dataPackaging(resultSet,dbType, num);
    }

    private static Map mapRow(ResultSet resultSet,String dbType, ResultSetMetaData rsMetaData) throws Exception {
        Map record = new LinkedHashMap();
        int columns = rsMetaData.getColumnCount();

        for(int i = 1; i <= columns; ++i) {
            String columnName = rsMetaData.getColumnLabel(i);
            Object obj = getColumrsval(resultSet,dbType, i, columnName);
            record.put(columnName, obj);
        }

        return record;
    }

    private static Object getColumrsval(ResultSet resultSet,String dbType, int columNum, String columnName) throws Exception {
        Object obj = resultSet.getObject(columNum);
        if (isBlank(obj)) {
            return null;
        } else if (obj instanceof Blob) {
            byte[] obj1;
            if (((Blob)obj).length() > 0L) {
                obj1 = ((Blob)obj).getBytes(1L, (int)((Blob)obj).length());
            } else {
                obj1 = null;
            }

            return obj1;
        } else if (obj instanceof Clob) {
            Clob clob_obj = (Clob)obj;

            try {
                obj = clob_obj.getSubString(1L, (int)clob_obj.length());
            } catch (Exception var9) {
                try {
                    obj = clob_obj.getSubString(1L, 2147483647);
                } catch (SQLException var8) {
                }
            }

            return obj;
        } else {
            if (DbConstants.ORACLE.equalsIgnoreCase(dbType)) {
                if (obj instanceof java.util.Date) {
                    obj = resultSet.getTimestamp(columNum);
                }
                if (obj instanceof oracle.sql.TIMESTAMP){
                    obj = resultSet.getTimestamp(columNum);
                }
                if (obj instanceof Timestamp){
                    obj = resultSet.getTimestamp(columNum);
                }
            }
            return obj;
        }
    }

    protected static boolean isBlank(Object str) {
        if (str == null) {
            return true;
        } else {
            String tmp = str.toString();
            if (tmp == null) {
                return true;
            } else {
                tmp = tmp.trim();
                if (tmp.length() == 0) {
                    return true;
                } else {
                    return tmp.equalsIgnoreCase("null") || tmp.equalsIgnoreCase("[NULL]");
                }
            }
        }
    }

    public static void setParameter(PreparedStatement ps, int i, Object obj) throws SQLException {
        if (obj == null) {
            ps.setNull(i, 12);
        } else if (obj instanceof String) {
            ps.setString(i, (String)obj);
        } else if (obj instanceof Integer) {
            ps.setInt(i, (Integer)obj);
        } else if (obj instanceof Short) {
            ps.setShort(i, (Short)obj);
        } else if (obj instanceof Long) {
            ps.setLong(i, (Long)obj);
        } else if (obj instanceof Float) {
            ps.setFloat(i, (Float)obj);
        } else if (obj instanceof Double) {
            ps.setDouble(i, (Double)obj);
        } else if (obj instanceof Boolean) {
            ps.setBoolean(i, (Boolean)obj);
        } else if (obj instanceof BigDecimal) {
            ps.setBigDecimal(i, (BigDecimal)obj);
        } else if (obj instanceof Blob) {
            ps.setBlob(i, (Blob)obj);
        } else if (obj instanceof Date) {
            ps.setDate(i, (Date)obj);
        } else if (obj instanceof java.util.Date) {
            ps.setDate(i, new Date(((java.util.Date)obj).getTime()));
        } else if (obj instanceof Time) {
            ps.setTime(i, (Time)obj);
        } else if (obj instanceof Timestamp) {
            ps.setTimestamp(i, (Timestamp)obj);
        } else if (obj instanceof Byte) {
            ps.setByte(i, (Byte)obj);
        } else if (obj instanceof byte[]) {
            ps.setBytes(i, (byte[])obj);
        } else if (obj instanceof Clob) {
            ps.setClob(i, (Clob)obj);
        } else if (obj instanceof Array) {
            ps.setArray(i, (Array)obj);
        } else if (obj instanceof URL) {
            ps.setURL(i, (URL)obj);
        } else {
            ps.setObject(i, obj);
        }

    }

    public static List convShort(List mapList) {
        if (mapList != null && mapList.size() != 0) {
            List list = new ArrayList();
            Iterator iterator = mapList.iterator();

            while(iterator.hasNext()) {
                list.add(convShort((Map)iterator.next()));
            }

            mapList.clear();
            mapList.addAll(list);
            return mapList;
        } else {
            return new ArrayList();
        }
    }

    public static Map convShort(Map dataMap) {
        if (dataMap == null) {
            return new LinkedHashMap();
        } else {
            LinkedHashMap map = new LinkedHashMap();
            Iterator it2 = dataMap.entrySet().iterator();

            while(it2.hasNext()) {
                Map.Entry entity = (Map.Entry)it2.next();
                String columName = (String)entity.getKey();
                columName = fieldName(columName);
                map.put(columName, entity.getValue());
            }

            return map;
        }
    }

    public static String fieldName(String name) {
        /*if (name.split("_").length == 1) {
            name = name.toLowerCase();
        }*/

        //return firstLowerCase(shortName(name));
        return name = name.toLowerCase();
    }

    public static String firstLowerCase(String name) {
        if (name == null) {
            return null;
        } else if (name.length() == 0) {
            return name;
        } else if (name.length() == 1) {
            return name.toLowerCase();
        } else {
            char ch = name.charAt(0);
            ch = (char)(ch >= 'A' && ch <= 'Z' ? ch + 32 : ch);
            name = ch + name.substring(1);
            return name;
        }
    }
    public static String shortName(String name) {
        if (name == null) {
            return null;
        } else if (name.split("_").length == 1) {
            return name;
        } else {
            name = name.toLowerCase();
            name = name.replaceAll("_$", "");

            int index;
            for(boolean var1 = false; (index = name.indexOf("_")) != -1; index = name.indexOf("_")) {
                char ch = name.charAt(index + 1);
                name = name.replaceFirst("_.", String.valueOf(ch).toUpperCase());
            }

            return name;
        }
    }
}
