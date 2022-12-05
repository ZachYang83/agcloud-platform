package com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.dao;
import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;
import com.augurit.agcloud.agcom.agsupport.sc.dataAudit.util.ConnTimeOutUtil;
import com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.util.AgProcedure;
import com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.util.DateUtil;
import com.common.util.ConfigProperties;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;
import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :11:00 2019/1/10
 * @Modified By:
 */
@Repository
public class AgDataUpdateDao {

    /**
     * 数据库类型
     */
    public static final String DB_TYPE_ORACLE = "ora";
    public static final String DB_TYPE_MYSQL = "mysql";
    public static final String DB_TYPE_POSTGRESQL = "pos";
    public static final String DB_TYPE_DB2 = "db2";

    public String getSrId(String dataSourceId, String tableName) {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection(dataSourceId);
            //conn = JDBCUtil.getConn();
            String sql = "select s.auth_srid from SPATIAL_REFERENCES s,LAYERS l where l.srid=s.srid and l.table_name='" + tableName.toUpperCase() + "'";
            pre = conn.prepareStatement(sql);
            rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getString(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(rs, pre, null, conn);
        }
        return null;
    }

    public String getDbType(String dataSourceId) {
        Connection conn = null;
        String dbType = "";
        try {
            //conn = JDBCUtil.getConn();
            conn = JDBCUtils.getConnection(dataSourceId);
            DatabaseMetaData metaData = conn.getMetaData();
            String driverName = metaData.getDriverName();
            driverName = driverName.toLowerCase().replaceAll(" ", "");
           // System.out.println(driverName);
            switch (driverName) {
                case "postgresqljdbcdriver":
                    dbType = DB_TYPE_POSTGRESQL;
                    break;
                case "oraclejdbcdriver":
                    dbType = DB_TYPE_ORACLE;
                    break;
                default:
                    dbType = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(null, null, null, conn);
        }
        return dbType;
    }

    public void delMunch(LinkedList<String> delList, String dataSourceId, String dbType, String tableName, String contactOnlyTableFiled) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM " + tableName + " WHERE  " + contactOnlyTableFiled + "=?";
        try {
            //conn = JDBCUtil.getConn();
            conn = JDBCUtils.getConnection(dataSourceId);
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            for (String value : delList) {
                pstmt.setObject(1, value);
                pstmt.addBatch();
            }
            int[] delLen = pstmt.executeBatch();
            System.out.println("删除多余数据 : " + delLen.length);
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
                System.out.println("删除失败,回滚");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    public Map<String, String> findAll(String dataSourceId, String dbType, String tableName, String contactOnlyTableFiled) {
        Connection conn = null;
        Statement stm = null;
        ResultSet rs = null;
        String sql = "select " + contactOnlyTableFiled + " from " + tableName;
        Map<String, String> map = new HashMap<>();
        try {
            conn = JDBCUtils.getConnection(dataSourceId);
            //conn = JDBCUtil.getConn();
            stm = conn.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                map.put(rs.getString(1), "2");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    /**
     * @param contactFieldMap     map<tableField,shpField>
     * @param updateList          list<map<shpField,shpValue>>
     * @param dataSourceId
     * @param dbType
     * @param tableName
     * @param procedure           是否创建存储过程
     * @param contactOnlyShpField
     */
    public void updateOrsave(LinkedList<Map<String, Object>> updateList, String dataSourceId, String dbType, String tableName, String contactOnlyShpField, Map<String, String> contactFieldMap, String srId, boolean procedure) throws Exception {
        long upstartTime = System.currentTimeMillis();
        contactOnlyShpField = contactOnlyShpField.toUpperCase();
        Connection conn = null;
        PreparedStatement ps = null;
        Statement stm = null;
        ResultSet rs = null;
        String sql = "";
        String tempValues = "";
        Map<String, String> columnType = null;
        //获取表字段类型 COMPLEX  SDE.ST_GEOMETRY
        columnType = getColumnType(dataSourceId,"select * from " + tableName + " t where  ROWNUM = 1");
        try {
            //conn = JDBCUtil.getConn();
            conn = JDBCUtils.getConnection(dataSourceId);
            conn.setAutoCommit(false); //设置事务属性
            //Oracle数据库创建存储过程
            if (procedure && DB_TYPE_ORACLE.equals(dbType)) {
                //使用Oracle的MERGE INTO 批量插入语句必须至少表里要存在一条数据否则其子查询ON无法使用
                stm = conn.createStatement();
                int count = 0;
                rs = stm.executeQuery("SELECT count(*) FROM " + tableName);
                while (rs.next()) {
                    count = rs.getInt(1);
                }

                String procedureName = "ag" + tableName.toLowerCase() + "pro";
                procedureName = procedureName.toUpperCase();
                int inParamLen = 0;
                String insSql = "";
                String set = "";
                String insVal = "";


                Iterator<Map.Entry<String, String>> fieldMapIt = contactFieldMap.entrySet().iterator();
                String setSql = "";
                String insValues = "";
                String procedureSql = "create or replace procedure " + procedureName + "(";
                String inParam = "";
                String contactOnlyShpFieldValue = "";
                boolean czSHAPE = false;//标记是否更新空间字段
                while (fieldMapIt.hasNext()) {
                    Map.Entry<String, String> entry = fieldMapIt.next();
                    String tableColumnName = entry.getKey().toUpperCase();
                    String type = columnType.get(tableColumnName);
                    if ("SHAPE".equals(tableColumnName)) {
                        type = "CLOB";
                        czSHAPE = true;
                    }
                    if (!tableColumnName.equals(contactOnlyShpField)) {
                        setSql += tableColumnName + "=n" + tableColumnName + ",";
                        set += tableColumnName + "=?,";
                    } else {
                        contactOnlyShpFieldValue = "n" + tableColumnName;
                    }
                    insSql += tableColumnName + ",";
                    insValues += "n" + tableColumnName + ",";
                    insVal += "?,";
                    inParam += "n" + tableColumnName + " " + type + ",";
                    inParamLen++;
                }
                setSql = setSql.substring(0, setSql.lastIndexOf(","));
                if (czSHAPE) setSql = setSql.replace("nSHAPE", "geom");
                insSql = insSql.substring(0, insSql.lastIndexOf(","));
                if (!czSHAPE){
                    inParam +="nSHAPE  CLOB";
                }else {
                    inParam = inParam.substring(0, inParam.lastIndexOf(","));
                }
                insValues = insValues.substring(0, insValues.lastIndexOf(","));
                if (czSHAPE)insValues = insValues.replace("nSHAPE", "geom");
                set = set.substring(0, set.lastIndexOf(","));
                insVal = insVal.substring(0, insVal.lastIndexOf(","));
                String upOrInsSql = "MERGE INTO " + tableName + " t USING (SELECT "+contactOnlyShpField+" from " + tableName + " WHERE ROWNUM = 1 ) ON (t."+contactOnlyShpField+" ="+contactOnlyShpFieldValue+") WHEN MATCHED THEN UPDATE  " +
                        "SET " + setSql + " WHEN NOT MATCHED THEN INSERT (" + insSql + ",OBJECTID) VALUES(" + insValues + ",(SELECT NVL(MAX(OBJECTID),0)+1 FROM " + tableName + "))";

                procedureSql += inParam + " )";
                procedureSql += "is geom sde.st_geometry; begin geom  := sde.st_geometry(nSHAPE," + srId + ");" + upOrInsSql + ";end " + procedureName + ";";
                AgProcedure.shapProcedure(dataSourceId,tableName,procedureSql);//动态创建存储
                //System.out.println("存储过程参数: "+inParam);
                String callValue = "";//存储过程参数
                for (int i = 0; i < inParamLen; i++) {
                    callValue += "?,";
                }
                callValue = callValue.substring(0, callValue.lastIndexOf(","));
                CallableStatement cstm = conn.prepareCall("{call " + procedureName + "(" + callValue + ")}");
                //pst = (OraclePreparedStatement) conn.prepareCall("{call "+procedureName+"("+callValue+")}");
                int commitCount = 0;
                for (Map<String, Object> dataMap : updateList) {
                    Iterator<Map.Entry<String, String>> it = contactFieldMap.entrySet().iterator();
                    int indexNun = 1;
                    while (it.hasNext()) {
                        Map.Entry<String, String> entry = it.next();
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if (contactOnlyShpField.equals(key)){
                            tempValues += "'"+dataMap.get(value).toString()+"'"+",";
                        }else {
                            tempValues +="''"+",";
                        }
                        if ("the_geom".equals(value)) {
                            //conn = conn.unwrap(OracleConnection.class);// 增加此代码，将Connection转换为OracleConnection
                            //CLOB clob = new CLOB((OracleConnection) conn);
                            //clob = oracle.sql.CLOB.createTemporary((OracleConnection) conn, true, 1);
                            Clob clob = cstm.getConnection().createClob();
                            clob.setString(1, dataMap.get(value).toString());
                            //System.out.println("indexNum: "+indexNun+" columnName: "+value+"columnValue: "+dataMap.get(value));
                            cstm.setClob(indexNun, clob);
                        } else {
                            if ("TIMESTAMP".equals(columnType.get(value))) {
                                String dateStr = DateUtil.formatDate(DateUtil.FORMAT2, (Date) dataMap.get(value));
                                try {
                                    Date date = DateUtil.parse(DateUtil.FORMAT2, dateStr);
                                    if (date != null) {
                                        Timestamp tsp = new Timestamp(date.getTime());
                                        cstm.setTimestamp(indexNun, tsp);
                                        //System.out.println("indexNum: "+indexNun+" columnName: "+value+"columnValue: "+dataMap.get(value));
                                    } else {
                                        cstm.setTimestamp(indexNun, null);
                                        //System.out.println("indexNum: "+indexNun+" columnName: "+value+"columnValue: "+dataMap.get(value));
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    System.out.println("时间格式用问题");
                                }
                            } else {
                               // System.out.println(indexNun+" "+value+"   "+dataMap.get(value)+"  "+columnType.get(value));
                                cstm.setObject(indexNun, dataMap.get(value));
                                //System.out.println("indexNum: "+indexNun+" columnName: "+value+"columnValue: "+dataMap.get(value));
                            }
                        }
                        indexNun++;
                    }
                    if (count < 1) {
                        tempValues = tempValues.substring(0,tempValues.length()-1);
                        String insertSql = "INSERT INTO "+ tableName +"(" + insSql + ",OBJECTID) VALUES(" + tempValues + ",(SELECT NVL(MAX(OBJECTID),0)+1 FROM " + tableName + "))";
                        stm.execute(insertSql);
                        conn.commit();
                        count++;
                    } else {
                        cstm.addBatch();
                    }
                    commitCount++;
                    if (commitCount % 500 == 0) {
                        System.out.println("批量提交500条，已经comit数据库等待数据库处理");
                        int[] len = cstm.executeBatch();
                        conn.commit();
                        System.out.println("数据库处理完 "+len.length);
                    }
                }
                int[] len = cstm.executeBatch();
                conn.commit();
                System.out.println("update "+len.length);
                System.out.println("更新数据完成");
            } else {
                if (DB_TYPE_ORACLE.equals(dbType)) {
                    //使用Oracle的MERGE INTO 批量插入语句必须至少表里要存在一条数据否则其子查询ON无法使用
                    stm = conn.createStatement();
                    int count = 0;
                    rs = stm.executeQuery("SELECT count(*) FROM " + tableName);
                    while (rs.next()) {
                        count = rs.getInt(1);
                    }
                    int commitCount = 0;
                    for (Map<String, Object> dataMap : updateList) {
                        Iterator<Map.Entry<String, String>> it = contactFieldMap.entrySet().iterator();
                        StringBuilder setSb = new StringBuilder();
                        StringBuilder insertFieldsSb = new StringBuilder();
                        StringBuilder insertValuesSb =  new StringBuilder();
                        String myKey = "";
                        while (it.hasNext()) {
                            Map.Entry<String, String> entry = it.next();
                            String key = entry.getKey();
                            String value = entry.getValue();
                            Object dataMapValue = dataMap.get(value);
                            /*if("DTUPDATE".equals(value)){
                                System.out.println(columnType.get(value.toUpperCase()));
                            }*/
                            if ("VARCHAR2".equals(columnType.get(value.toUpperCase()))){
                                dataMapValue = "'" + dataMapValue + "'";
                            }else if (("TIMESTAMP").equals(columnType.get(value)) || ("DATE").equals(columnType.get(value))){
                                String dateStr = DateUtil.formatDate(DateUtil.FORMAT2, (Date) dataMapValue);
                                dataMapValue = "TO_DATE('"+dateStr+"', 'YYYY-MM-DD HH24:MI:SS')";
                            }
                            if (!key.toLowerCase().equals(contactOnlyShpField.toLowerCase())) {//更新数据不需要更新关联字段
                                if (entry.getValue().equals("the_geom")) {
                                    setSb.append( key + "=sde.st_geometry('" + dataMapValue + "'," + srId + "),");
                                    insertFieldsSb.append( key + ",");
                                    insertValuesSb.append("sde.st_geometry('" + dataMapValue + "'," + srId + "),");
                                } else {
                                    setSb.append( entry.getKey() + "=" + dataMapValue + ",");
                                    insertFieldsSb.append( key + ",");
                                    insertValuesSb.append(dataMapValue + ",");
                                }
                            } else {
                                if (key.toLowerCase().equals(contactOnlyShpField.toLowerCase())) myKey = value;
                                insertFieldsSb.append( key + ",");
                                insertValuesSb.append( dataMapValue + ",");
                            }
                        }
                        String setStr = setSb.substring(0,setSb.lastIndexOf(","));
                        sql = "MERGE INTO " + tableName + " t  using ( select " + contactOnlyShpField + " from " + tableName + " where ROWNUM=1)ON (" + "t." + contactOnlyShpField + "='"+dataMap.get(myKey)+"')WHEN MATCHED THEN UPDATE SET " +
                                "" + setStr + "  WHEN NOT MATCHED THEN  INSERT (" + insertFieldsSb.toString() + "OBJECTID) VALUES( " + insertValuesSb.toString() + "(SELECT NVL(MAX(OBJECTID),0)+1 FROM " + tableName + "))";

                        if (count < 1) {
                            stm.addBatch("INSERT INTO " + tableName + " (" + insertFieldsSb.toString() + "OBJECTID) VALUES( " + insertValuesSb.toString() + "(SELECT NVL(MAX(" + contactOnlyShpField + "),0)+1 FROM " + tableName + "))");
                            conn.commit();
                           // System.out.println("更新目标表是空表");
                            count++;
                        } else {
                            stm.addBatch(sql);
                        }
                        commitCount++;
                        //100条提交一次
                        if (commitCount % 500 == 0) {
                            long uptimeE = System.currentTimeMillis();
                            System.out.println("批量提交500条，已经comit数据库等待数据库处理");
                            int[] len = stm.executeBatch();
                            conn.commit();//提交，
                            System.out.println("update : " + len.length);
                        }

                    }
                    int[] len = stm.executeBatch();
                    System.out.println("update:  " + len.length);
                    conn.commit();//提交，
                    conn.setAutoCommit(true);//设置事务初始值
                    System.out.println("更新数据完成");
                    long updataTimeend = System.currentTimeMillis();
                    System.out.println("总共耗时条数据耗时：" + (updataTimeend - upstartTime) / 1000 + "秒");
                }
            }

        } catch (SQLException e) {
            try {
                conn.rollback();//提交失败，执行回滚操作
            } catch (SQLException e1) {
                e1.printStackTrace();
                System.err.println("updateExistsInfo回滚执行失败!!!");
                throw new Exception("回滚执行失败");
            }
            e.printStackTrace();
            System.err.println("updateExistsInfo执行失败");
            throw new Exception(e.getMessage());
        } finally {
            closeConnection(rs, ps, stm, conn);
        }
    }


    /**
     * 获取字段
     *
     * @param sql
     * @return
     */
    public Map<String, String> getColumnType(String dataSourceId,String sql) {
        Map<String, String> columnMap = new HashMap<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection(dataSourceId);
            //conn = JDBCUtil.getConn();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            ResultSetMetaData rm = rs.getMetaData();// 得到结果集(rs)的结构信息，比如字段数、字段名等
            int fieldsCount = rm.getColumnCount(); // 返回此 ResultSet 对象中的列数
            for (int i = 1; i <= fieldsCount; i++) {
                columnMap.put(rm.getColumnName(i), rm.getColumnTypeName(i));
            }
            return columnMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            closeConnection(rs, pstm, null, conn);
        }
    }


    /**
     * 创建临时表
     * @param dataSourceId
     * @param tableName
     */
    public void createTempTable(String dataSourceId,String tableName) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = JDBCUtils.getConnection(dataSourceId);
            //conn = JDBCUtil.getConn();
            String sql = "create table  "+tableName+"_TEMP as select * from  "+tableName+" where 1=2";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection(null, ps, null, conn);
        }
    }

    /**
     * 给临时表添加审核字段state
     * @param dataSourceId
     * @param tableName
     */
    public void addColumn(String dataSourceId,String tableName){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection(dataSourceId);
            //conn = JDBCUtil.getConn();
            ps = conn.prepareStatement("alter table  "+tableName+"_TEMP add (state NUMBER,auditType NUMBER,SUBMIT_TIME DATE,REPORT_PERSON VARCHAR2(50),NOT_PASS_REASON VARCHAR2(500),AUDIT_TIME DATE)");
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection(null, ps, null, conn);
        }
    }

    public void clearTable(String dataSourceId,String tableName){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection(dataSourceId);
            //conn = JDBCUtil.getConn();
            ps = conn.prepareStatement("delete from "+tableName);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection(null, ps, null, conn);
        }
    }

    public void saveUserNameAndTime(String dataSourceId,String tableName,String userName,String curTime){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection(dataSourceId);
            ps = conn.prepareStatement("UPDATE "+tableName+" t set t.REPORT_PERSON ='"+userName+"' ,t.submit_time= TO_DATE('"+curTime+"', 'YYYY-MM-DD HH24:MI:SS')");
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection(null, ps, null, conn);
        }
    }

    /**
     * 是否存在表
     * @param dataSourceId
     * @param tableName
     * @return
     */
    public boolean isExistTable(String dataSourceId,String tableName){
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
            closeConnection(rs,ps,null,conn);
        }
        return false;
    }

    /**
     * 更新数据和更新前数据对比
     * @param dataSourceId
     * @param layerTable
     * @param fieldName
     * @param fieldValue
     * @return
     * @throws Exception
     */
    public Map<String,Object> compareData(String dataSourceId,String layerTable,String fieldName, String fieldValue) throws Exception {
        Map<String,Object> resultMap = new HashMap<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection(dataSourceId);
            ps = conn.prepareStatement("SELECT * FROM "+layerTable+" WHERE "+fieldName+"='"+fieldValue+"'");
            rs = ps.executeQuery();
            ResultSetMetaData rm = rs.getMetaData();// 得到结果集(rs)的结构信息，比如字段数、字段名等
            int fieldsCount = rm.getColumnCount(); // 返回此 ResultSet 对象中的列数
            Map<String,Object> OldInfoMap = new HashMap<>();
            while (rs.next()){
                for (int i = 1; i <= fieldsCount; i++){
                    if (rs.getObject(i)!=null){
                        if (rm.getColumnType(i)==Types.TIMESTAMP || rm.getColumnType(i)==Types.DATE){
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            OldInfoMap.put(rm.getColumnName(i),df.format(rs.getTimestamp(i)));
                        }else {
                            OldInfoMap.put(rm.getColumnName(i),rs.getObject(i).toString());
                        }
                    }else {
                        OldInfoMap.put(rm.getColumnName(i),"");
                    }
                }
            }
            JSONObject json = JSONObject.fromObject(OldInfoMap);
            resultMap.put("old",json);
            ps = conn.prepareStatement("SELECT * FROM "+layerTable+"_TEMP WHERE "+fieldName+"='"+fieldValue+"'");
            rs = ps.executeQuery();
            ResultSetMetaData rm1 = rs.getMetaData();// 得到结果集(rs)的结构信息，比如字段数、字段名等
            int fieldsCount1 = rm1.getColumnCount(); // 返回此 ResultSet 对象中的列数
            Map<String,Object> NewInfoMap = new HashMap<>();
            while (rs.next()){
                for (int i = 1; i <= fieldsCount1; i++){
                    if (rs.getObject(i)!=null){
                        if (rm1.getColumnType(i)==Types.TIMESTAMP || rm1.getColumnType(i)==Types.DATE){
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            NewInfoMap.put(rm1.getColumnName(i),df.format(rs.getTimestamp(i)));
                        }else {
                            NewInfoMap.put(rm1.getColumnName(i),rs.getObject(i).toString());
                        }
                    }else {
                        NewInfoMap.put(rm1.getColumnName(i),"");
                    }
                }
            }
            JSONObject json1 = JSONObject.fromObject(NewInfoMap);
            resultMap.put("new",json1);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(rs,ps,null,conn);
        }
        return resultMap;
    }

    /**
     * 保存修正的更新的数据
     * @param dataSourceId
     * @param layerTable
     * @param updateJson
     * @return
     */
    public void saveEditData(String dataSourceId,String layerTable,String primaryKeyName,String primaryKeyValue,String updateJson) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        Map<String, String> type = getColumnType(dataSourceId,"select * from "+layerTable +"_TEMP where ROWNUM=1");
        StringBuilder sb = new StringBuilder("UPDATE  "+layerTable+"_TEMP SET ");
        JSONObject jsonObject = JSONObject.fromObject(updateJson);
        Iterator it = jsonObject.keys();
        while (it.hasNext()){
            String key = (String) it.next();
            String value = jsonObject.getString(key);
            if (type.get(key).equals("TIMESTAMP")){
                sb.append(key+"=to_date('"+value+"','YYYY-MM-DD HH24:MI:SS'),");
            }else {
                sb.append(key+"='"+value+"',");
            }

        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(" WHERE "+primaryKeyName+"='"+primaryKeyValue+"'");
        conn = JDBCUtils.getConnection(dataSourceId);
        try {
            ps = conn.prepareStatement(sb.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }finally {
            closeConnection(null,ps,null,conn);
        }
    }


    /**
     * 获取临时表和旧表不同的数据
     * @param dataSourceId
     * @param layerTable
     * @param primaryKeyName
     * @param fieldNames
     * @return
     * @throws Exception
     */
    public Map<String, Object> getDifferent(String dataSourceId, String layerTable, String primaryKeyName, String fieldNames) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String oldTable = layerTable.substring(0,layerTable.lastIndexOf("_"));
        String sql = "select " + primaryKeyName + " from (select " + fieldNames + " from " + oldTable + " minus select " + fieldNames + " from " + layerTable + ")";
        conn = JDBCUtils.getConnection(dataSourceId);
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            Map<String, Object> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getString(1), "true");
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            closeConnection(rs, ps, null, conn);
        }
    }

    public int submitCheck(String dataSourceId, String layerTable, String fieldName, String fieldValues,String auditTypes) throws Exception { Connection conn = JDBCUtils.getConnection(dataSourceId);
        PreparedStatement ps = null;
        String sql = "UPDATE " + layerTable + " SET STATE = 1 ,AUDITTYPE=?  where " + fieldName + "=?";
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            String[] values = fieldValues.split(",");
            String[] types = auditTypes.split(",");
            int indexType = 0;
            for (String value : values) {
                ps.setObject(1, Integer.parseInt(types[indexType]));
                ps.setObject(2, value);
                ps.addBatch();
                indexType++;
            }
            int[] len = ps.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("提交审核：" + len.length + " 条");
            return len.length;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new Exception("提交审核回滚失败");
            }
            throw new Exception(e.getMessage());
        } finally {
            closeConnection(null, ps, null, conn);
        }
    }

    public Map<String, String> getFieldNameAlias(String layerId) throws Exception {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String username = ConfigProperties.getByKey("spring.datasource.username");
        String password = ConfigProperties.getByKey("spring.datasource.password");
        String url = ConfigProperties.getByKey("spring.datasource.url");

        try {
            String sql = "SELECT DISTINCT FIELD_NAME,FIELD_NAME_CN    FROM AG_LAYER_FIELD a where a.LAYER_ID='" + layerId + "'";
            conn = DriverManager.getConnection(url, username, password);
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            Map<String, String> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getString(1), rs.getString(2));
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }finally {
            closeConnection(rs,pstm,null,conn);
        }
    }

    /**
     * 关闭数据库连接
     *
     * @param conn
     * @param rs
     * @param pre
     */
    public static void closeConnection(ResultSet rs, PreparedStatement pre, Statement stm, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (pre != null) pre.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BufferedWriter readFile() {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(new File("D:/mytempsql/zrr.txt"));
            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);
            return bw;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
