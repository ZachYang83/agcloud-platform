package com.augurit.agcloud.agcom.agsupport.sc.dataAudit.dao;

import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;
import com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.util.AgProcedure;
import com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.util.DateUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :16:37 2019/2/20
 * @Modified By:
 */
@Repository
public class AgDataAuditDao {
    /**
     * 获取未审核的记录
     * @param conn
     * @param layerTable
     * @return
     * @throws Exception
     */
    public List<Integer> findList(Connection conn, String layerTable,int auditType,String startTime,String endTime) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Integer> list = new ArrayList<>();
        String sql = "select auditType from "+layerTable+"_TEMP  t where t.STATE=1";
        if (auditType==1 || auditType==2 || auditType==3){
            sql += " AND t.AUDITTYPE="+auditType;
        }
        if (!"".equals(startTime) && startTime!=null){
            sql +="  AND t.SUBMIT_TIME>= TO_DATE('"+startTime+"','yyyy-MM-dd HH24:MI:SS')";
        }
        if (!"".equals(endTime) && endTime!=null){
            sql +=" AND t.SUBMIT_TIME<=TO_DATE('"+endTime+"', 'yyyy-MM-dd HH24:MI:SS')";
        }
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            //不在这里关闭连接
            JDBCUtils.close(null, ps, rs);
        }
        return list;
    }

    public List<Map<String,String>> getLayerTableInfoPage(String dataSourceId, String layerTable,String startTime,String endTime,int pageNum, int pageSize, int auditType,Map<String,String> fieldNames) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        conn = JDBCUtils.getConnection(dataSourceId);
        List<Map<String,String>> list = new ArrayList<>();
        String sql = "";
        int startNum = (pageNum-1)* pageSize+1;
        int endNum = pageNum * pageSize;
        if (auditType==4){
            if (("".equals(startTime) || startTime==null) && ("".equals(endTime) || endTime==null)){
                sql = "select * from " +
                        "(select a.*, ROWNUM RN from  " +
                        "(select * from "+layerTable+"  t where t.STATE=1) a where ROWNUM<="+endNum+") b where RN>="+startNum;
            }else if (!"".equals(startTime) && startTime!=null && ("".equals(endTime) || endTime==null)){
                sql = "select * from " +
                        "(select a.*, ROWNUM RN from  " +
                        "(select * from "+layerTable+"  t where t.STATE=1 AND t.SUBMIT_TIME>= TO_DATE('"+startTime+"','yyyy-MM-dd HH24:MI:SS')) a where ROWNUM<="+endNum+") b where RN>="+startNum;
            }else if (!"".equals(endTime) && endTime!=null && ("".equals(startTime) || startTime==null)){
                sql = "select * from " +
                        "(select a.*, ROWNUM RN from  " +
                        "(select * from "+layerTable+"  t where t.STATE=1 AND t.SUBMIT_TIME<= TO_DATE('"+endTime+"','yyyy-MM-dd HH24:MI:SS')) a where ROWNUM<="+endNum+") b where RN>="+startNum;
            }else if ((!"".equals(startTime) && startTime!=null) && (!"".equals(endTime) && endTime!=null)){
                sql = "select * from " +
                        "(select a.*, ROWNUM RN from  " +
                        "(select * from "+layerTable+"  t where t.STATE=1 AND t.SUBMIT_TIME>= TO_DATE('"+startTime+"','yyyy-MM-dd HH24:MI:SS') AND t.SUBMIT_TIME<= TO_DATE('"+endTime+"','yyyy-MM-dd HH24:MI:SS')) a where ROWNUM<="+endNum+") b where RN>="+startNum;
            }
        }else  if (!"".equals(startTime) && startTime!=null && ("".equals(endTime) || endTime==null)){
            sql = "select * from " +
                    "(select a.*, ROWNUM RN from  " +
                    "(select * from "+layerTable+"  t where t.STATE=1 and t.audittype="+auditType+" AND t.SUBMIT_TIME>= TO_DATE('"+startTime+"','yyyy-MM-dd HH24:MI:SS')) a where ROWNUM<="+endNum+") b where RN>="+startNum;
        }else if (!"".equals(endTime) && endTime!=null && ("".equals(startTime) || startTime==null)){
            sql = "select * from " +
                    "(select a.*, ROWNUM RN from  " +
                    "(select * from "+layerTable+"  t where t.STATE=1 and t.audittype="+auditType+" AND t.SUBMIT_TIME<= TO_DATE('"+endTime+"','yyyy-MM-dd HH24:MI:SS')) a where ROWNUM<="+endNum+") b where RN>="+startNum;
        }else if ((!"".equals(startTime) && startTime!=null) && (!"".equals(endTime)  && endTime!=null)){
            sql = "select * from " +
                    "(select a.*, ROWNUM RN from  " +
                    "(select * from "+layerTable+"  t where t.STATE=1 and t.audittype="+auditType+" AND t.SUBMIT_TIME>= TO_DATE('"+startTime+"','yyyy-MM-dd HH24:MI:SS') AND t.SUBMIT_TIME<= TO_DATE('"+endTime+"','yyyy-MM-dd HH24:MI:SS')) a where ROWNUM<="+endNum+") b where RN>="+startNum;
        }else {
            sql = "select * from " +
                    "(select a.*, ROWNUM RN from  " +
                    "(select * from "+layerTable+"  t where t.STATE=1 and t.audittype="+auditType+") a where ROWNUM<="+endNum+") b where RN>="+startNum;
        }
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData rm = rs.getMetaData();// 得到结果集(rs)的结构信息，比如字段数、字段名等
            int fieldsCount = rm.getColumnCount(); // 返回此 ResultSet 对象中的列数
            while (rs.next()) {
                Map<String,String> info = new HashMap<>();
                for (int i=1;i<=fieldsCount;i++){
                    String fieldName = rm.getColumnName(i).toLowerCase();
                    String fieldNameCn = fieldNames.get(fieldName);
                    //System.out.println(i+" fieldName: "+fieldName+" fieldNameCn: "+fieldNameCn+"Type");
                    if (rm.getColumnType(i)== Types.STRUCT) continue;//屏蔽掉空间字段
                    if (fieldNameCn!=null){
                        if (rm.getColumnType(i)==Types.TIMESTAMP){
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            info.put(fieldNameCn,rs.getTimestamp(i)==null? "":df.format(rs.getTimestamp(i)));
                        }else {
                            info.put(fieldNameCn,rs.getObject(i)==null? "":rs.getObject(i).toString());
                        }
                    }else {
                        if (rm.getColumnType(i)==Types.TIMESTAMP){
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            info.put(fieldName,rs.getTimestamp(i)==null? "":df.format(rs.getTimestamp(i)));
                        }else {
                            info.put(fieldName,rs.getObject(i)==null? "":rs.getObject(i).toString());
                        }
                    }
                }
                info.put("操作","<button class=\"btn btn-success\" style=\"cursor:pointer\" onclick=\"adutidPass()\">通过</button> "+"<button class=\"btn  btn-danger\" style=\"cursor:pointer;\" onclick=\"adutidNotPass()\">驳回</button> ");
                info.put("上报人",info.get("report_person"));
                info.put("上报时间",info.get("submit_time"));
                list.add(info);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }finally {
            JDBCUtils.close(conn,ps,rs);
        }
    }

    public int saveAuditInfo(String dataSourceId,String layerTable,int state,String notPassReason,String primaryKey ,String keyValues) throws Exception {
        Connection conn = null;
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        ResultSet rs = null;
        conn = JDBCUtils.getConnection(dataSourceId);
        //修改临时表里数据的状态
        try {
            conn.setAutoCommit(false);
            String[]  values = keyValues.split(",");
            String sql = "UPDATE "+layerTable+"_TEMP "+" t SET t.state=?, t.NOT_PASS_REASON=?  where t."+primaryKey+"=?";
            pst = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            for (String value:values){
                pst.setInt(1,state);
                pst.setString(2,notPassReason);
                pst.setString(3,value);
                pst.addBatch();
            }
            int[] len = pst.executeBatch();
            //提交，设置事务初始值
            conn.commit();
            conn.setAutoCommit(true);
            //return len.length;
            //如果是审核通过 则将临时表数据插入或者更新到正式表

            if (state==2){
                Map<String,String> nameAndType = getNameAndType(conn,layerTable);
                for (String value:values){
                    LinkedList<Map<String,Object>> updateList = new LinkedList<>();
                    Iterator<Map.Entry<String, String>> it = nameAndType.entrySet().iterator();
                    String spaceColumnName = "";
                    while (it.hasNext()) {
                        Map.Entry<String, String> entry = it.next();
                        String columnName = entry.getKey();
                        String columnType = entry.getValue();
                        if (columnType.equalsIgnoreCase("ST_GEOMETRY")) spaceColumnName = columnName;
                    }
                    String tempTable = layerTable+"_TEMP";
                    pst2 = conn.prepareStatement("select t.*,sde.st_astext("+spaceColumnName+") as "+spaceColumnName+" FROM "+tempTable +" t  WHERE  "+primaryKey+"="+value);
                    rs = pst2.executeQuery();
                    ResultSetMetaData rm = rs.getMetaData();// 得到结果集(rs)的结构信息，比如字段数、字段名等
                    int fieldsCount = rm.getColumnCount(); // 返回此 ResultSet 对象中的列数
                    while (rs.next()){
                        Map<String, Object> info = new HashMap<>();
                        for (int i = 1; i <= fieldsCount; i++) {
                            if (rs.getObject(i)==null){
                                info.put(rm.getColumnName(i),"");
                                continue;
                            }
                            if (rm.getColumnType(i)!=Types.BLOB){
                                if (rm.getColumnType(i)==Types.TIMESTAMP){
                                    info.put(rm.getColumnName(i), rs.getTimestamp(i).toString());
                                }
                                if (spaceColumnName.equalsIgnoreCase(rm.getColumnName(i))){
                                    info.put(rm.getColumnName(i),rs.getString(i));
                                }else {
                                    info.put(rm.getColumnName(i),rs.getObject(i).toString());
                                }
                            }
                        }
                        updateList.add(info);
                    }
                    updateOrInsert(conn,layerTable,updateList,nameAndType,primaryKey,value,4326,true,dataSourceId);
                }
            }
            return len.length;
        } catch (SQLException e) {
            e.printStackTrace();
            try{
                //提交失败，执行回滚操作
                conn.rollback();

            }catch (SQLException e1) {
                e1.printStackTrace();
                throw  new Exception(e1.getMessage());
            }
            e.printStackTrace();
            System.err.println("updateExistsInfo执行失败");
            throw  new Exception(e.getMessage());
        }finally {
            if (pst2!=null) pst2.close();
            JDBCUtils.close(conn,pst,null);
        }
    }

    public int getCount(String dataSourceId, String layerTable) throws Exception {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        conn = JDBCUtils.getConnection(dataSourceId);

        try {
            pst = conn.prepareStatement("select count(*) from  "+layerTable +" where state=1");
            rs = pst.executeQuery();
            while (rs.next()){
                return  rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }finally {
            JDBCUtils.close(conn,pst,rs);
        }
        return 0;
    }

    public int updateOrInsert(Connection conn,String tableName,LinkedList<Map<String, Object>> updateList,Map<String,String> nameAndType,String pkColumn,String pkColumnValue,int srId,boolean procedure,String dataSourceId) throws SQLException {
        //使用Oracle的MERGE INTO 批量插入语句必须至少表里要存在一条数据否则其子查询ON无法使用
        Statement stm = null;
        ResultSet rs = null;
        PreparedStatement pst = null;

        //存储过程名称
        String procedureName = "AG" + tableName.toUpperCase() + "PRO";//存储过程名称
        String procedureSql = "create or replace procedure " + procedureName + "(";;//存储过程语句
        String inParam = "";//存储过程参数
        int inParamLen = 0;//存储过程参数个数
        CallableStatement cstm = null;

        try {
            stm = conn.createStatement();
            int count = 0;
            rs = stm.executeQuery("SELECT count(*) FROM " + tableName);
            while (rs.next()) {
                count = rs.getInt(1);
            }
            if (count<1){
                Iterator<Map.Entry<String, String>>  it_map = nameAndType.entrySet().iterator();
                String insertField = "";
                String valuesParam = "";
                while (it_map.hasNext()){
                    Map.Entry<String, String> entry = it_map.next();
                    if ("ST_GEOMETRY".equalsIgnoreCase(entry.getValue())) continue;//空间字段不加入，后面的存储过程插入会覆盖这条记录
                    insertField += entry.getKey()+",";
                    valuesParam += "?,";
                }
                insertField = insertField.substring(0,insertField.lastIndexOf(","));
                valuesParam = valuesParam.substring(0,valuesParam.lastIndexOf(","));
                pst = conn.prepareStatement("INSERT INTO  "+tableName+"("+insertField+")VALUES ("+valuesParam+")");
            }

            for (Map<String, Object> dataMap : updateList) {
                Iterator<Map.Entry<String, String>> it = nameAndType.entrySet().iterator();
                Iterator<Map.Entry<String, String>> it2 = nameAndType.entrySet().iterator();//用存储过程保存或者更新数据时使用
                StringBuilder setSb = new StringBuilder();
                StringBuilder insertFieldsSb = new StringBuilder();
                StringBuilder insertValuesSb =  new StringBuilder();
                //String myKey = "";
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    String columnName = entry.getKey();
                    String columnType = entry.getValue();
                    Object dataMapValue = dataMap.get(columnName);

                    //判断是否有空间字段
                    if (columnType.equalsIgnoreCase("ST_GEOMETRY")){
                        inParam += "nSHAPE CLOB,";
                        inParamLen++;
                    }else {
                        inParam += "n" + columnName + " " + columnType + ",";
                        inParamLen++;
                    }
//                    if ("VARCHAR2".equalsIgnoreCase(columnType)){
//                        dataMapValue = "'" + dataMapValue + "'";
//                    }else if (("TIMESTAMP").equalsIgnoreCase(columnType) || ("DATE").equalsIgnoreCase(columnType)){
//                        String dateStr = DateUtil.formatDate(DateUtil.FORMAT2, (Date) dataMapValue);
//                        dataMapValue = "TO_DATE('"+dateStr+"', 'YYYY-MM-DD HH24:MI:SS')";
//                    }else if ("NUMBER".equalsIgnoreCase(columnType)){
//                        dataMapValue = Integer.parseInt(dataMapValue.toString());
//                    }
                    //更新数据不需要更新关联字段
                    if (!columnName.equalsIgnoreCase(pkColumn)) {
                        if (columnType.equalsIgnoreCase("ST_GEOMETRY")) {
                            setSb.append( columnName + "=geom,");
                            insertFieldsSb.append( columnName + ",");
                            insertValuesSb.append("geom,");
//                            if (procedure){
//                                setSb.append( columnName + "=geom,");
//                                insertFieldsSb.append( columnName + ",");
//                                insertValuesSb.append("geom,");
//                            }else {
//                                setSb.append( columnName + "=st_geometry('" + dataMapValue + "'," + srId + "),");
//                                insertFieldsSb.append( columnName + ",");
//                                insertValuesSb.append("st_geometry('" + dataMapValue + "'," + srId + "),");
//                            }
                        } else {
                            setSb.append( columnName + "=n"+columnName+",");
                            insertFieldsSb.append( columnName + ",");
                            insertValuesSb.append("n" + columnName+",");
//                            if (procedure){
//                                setSb.append( columnName + "=n"+columnName+",");
//                                insertFieldsSb.append( columnName + ",");
//                                insertValuesSb.append("n" + columnName+",");
//                            }else {
//                                setSb.append(columnName + "=" + dataMapValue + ",");
//                                insertFieldsSb.append( columnName + ",");
//                                insertValuesSb.append(dataMapValue + ",");
//                            }
                        }
                    } else {
                        insertFieldsSb.append( columnName + ",");
                        insertValuesSb.append( "n"+columnName+ ",");
//                        if(procedure){
//                            insertFieldsSb.append( columnName + ",");
//                            insertValuesSb.append( "n"+columnName+ ",");
//                        }else {
//                            insertFieldsSb.append( columnName + ",");
//                            insertValuesSb.append( dataMapValue + ",");//(SELECT NVL(MAX(" + pkColumn + "),0)+1 FROM " + tableName + ")
//                        }
                    }
                }
                String setStr = setSb.substring(0,setSb.lastIndexOf(","));
                String insertFieldsStr = insertFieldsSb.toString();
                String insertValuesStr = insertValuesSb.toString();
                insertFieldsStr = insertFieldsStr.substring(0,insertFieldsStr.lastIndexOf(","));
                insertValuesStr = insertValuesStr.substring(0,insertValuesStr.lastIndexOf(","));

                pkColumn = pkColumn.toUpperCase();
                String sql = "MERGE INTO " + tableName + " t  using ( select " + pkColumn + " from " + tableName + " where ROWNUM=1)ON (" + "t." + pkColumn + "=n"+pkColumn+")WHEN MATCHED THEN UPDATE SET " +
                        "" + setStr + "  WHEN NOT MATCHED THEN  INSERT (" + insertFieldsStr + ") VALUES( " + insertValuesStr + ")";

                //使用存储过程保存或者更新数据
                if (procedure){
                    inParam = inParam.substring(0,inParam.lastIndexOf(","));
                    procedureSql += inParam + " )";
                    procedureSql += "is geom sde.st_geometry; begin geom  := sde.st_geometry(nSHAPE," + srId + ");" + sql + ";end " + procedureName + ";";
                    AgProcedure.shapProcedure(dataSourceId,tableName,procedureSql);//动态创建存储

                    String callValue = "";//存储过程参数
                    for (int i = 0; i < inParamLen; i++) {
                        callValue += "?,";
                    }
                    callValue = callValue.substring(0, callValue.lastIndexOf(","));
                    cstm = conn.prepareCall("{call " + procedureName + "(" + callValue + ")}");
                    int indexNun = 1;
                    int indexOne = 1;
                    while (it2.hasNext()) {
                        Map.Entry<String, String> entry = it2.next();
                        String name = entry.getKey();//字段名
                        String type = entry.getValue();//字段类型
                        Object value = dataMap.get(name);//字段值
                        if("ST_GEOMETRY".equalsIgnoreCase(type)){
                            Clob clob = cstm.getConnection().createClob();
                            clob.setString(1, value.toString());
                            cstm.setClob(indexNun, clob);
                        }else if ("TIMESTAMP".equalsIgnoreCase(type)) {
                            Date date = DateUtil.parse(DateUtil.FORMAT2, dataMap.get(name).toString());
                            if (date != null) {
                                Timestamp tsp = new Timestamp(date.getTime());
                                cstm.setTimestamp(indexNun, tsp);
                            } else {
                                cstm.setTimestamp(indexNun, null);
                            }
                        }else {
                            cstm.setObject(indexNun, dataMap.get(name));
                        }
                        if (count<1 && !"ST_GEOMETRY".equalsIgnoreCase(type)){
                            pst.setObject(indexOne,dataMap.get(name));
                            indexOne++;
                        }
                        indexNun++;
                    }
                    cstm.addBatch();
                }

                //Oracle数据库的更新和插入组合语句需要至少有一条记录作为子判断，要不然无法插入或更新数据
                if (count < 1) {
//                    stm.addBatch("INSERT INTO " + tableName + " (" + pkColumn + ",OBJECTID"+") VALUES( " + 0 + ",0)");
//                    stm.executeBatch();
//                    conn.commit();
//                    count++;
                    pst.executeUpdate();
                }
            }
            if (procedure){
                int[] len = cstm.executeBatch();
                conn.commit();
                System.out.println("使用存储过程数据库处理完 "+len.length);
            }else {
                int[] len = stm.executeBatch();
                System.out.println("update:  " + len.length);
                conn.commit();//提交，
                conn.setAutoCommit(true);//设置事务初始值
                System.out.println("普通提交更新数据完成");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (stm!=null) stm.close();
            JDBCUtils.close(null,pst,rs);
        }
        return 0;
    }

    /**
     * 获取字段的名称和类型
     * @param conn
     * @param tableName
     * @return
     */
    public Map<String,String> getNameAndType(Connection conn, String tableName) {
        String columnName = "";
        String columnType = "";
        Map<String,String> map = new HashMap<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            //"select   column_name   from   cols  t    WHERE   TABLE_name=upper('"+tableName+"') AND t.data_type='ST_GEOMETRY'"
            pst = conn.prepareStatement("select   column_name,data_type   from   cols  t    WHERE   TABLE_name=upper('"+tableName+"')");
            rs = pst.executeQuery();
            while (rs.next()){
                columnName = rs.getString("column_name");
                columnType = rs.getString("data_type");
                map.put(columnName,columnType);
            }
        }catch (Exception e){

        }finally {
            JDBCUtils.close(null,pst,rs);
        }
        return map;
    }
}
