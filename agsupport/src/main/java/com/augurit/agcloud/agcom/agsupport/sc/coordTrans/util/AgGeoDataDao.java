package com.augurit.agcloud.agcom.agsupport.sc.coordTrans.util;

import com.augurit.agcloud.agcom.agsupport.common.util.DESedeUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgSupDatasource;
import com.augurit.agcloud.agcom.agsupport.sc.agdatasource.service.IAgSupDatasource;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class AgGeoDataDao {

//   // @Value("${postgresql.url}")
//    private static String dataSourceURL = "jdbc:postgresql://192.168.32.84:5432/postgis20";
//
//   // @Value("${postgresql.username}")
//    private static String username = "postgres";
//
//   // @Value("${postgresql.password}")
//    private static String password = "ag123";

    @Autowired
    private IAgSupDatasource supDatasource;

    // private AgSupDatasource dbsource;


//    public AgGeoDataDao() {
//        try {
//            dbsource = supDatasource.selectDataSourceByName("PostGIS规划数据库");
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public boolean inserGeometryIntoSDE(String tableName,String fields, String values) throws SQLException{

        Connection con = null;
        Statement stm = null;
        boolean flag = false;
        try{
            AgSupDatasource dbsource = supDatasource.selectDataSourceByName("PostGIS规划数据库");
          if(dbsource.getDbUrl().contains("postgresql")) {
              String driver = "org.postgresql.Driver";
              Class.forName(driver);
             con = DriverManager.getConnection(dbsource.getDbUrl(), dbsource.getUserName(), DESedeUtil.desDecrypt(dbsource.getPassword()));
             if(con != null) {
                 String sql = "insert into "+tableName+"("+ fields +") " + "values("+ values +")";
                 System.out.println("SQL:" + sql);
                 stm = con.createStatement();
                 stm.execute(sql);
                 flag = true;
             }
          }
        }catch (Exception e) {
           e.printStackTrace();
            flag = false;
        }finally {
            if(con != null) {
                con.close();
            }
            if(stm != null) {
                stm.close();
            }
        }
       return flag;
    }

    public List<Map> getProjectInfoByxmdm(String tableName,String condition) throws SQLException {
            Connection con = null;
            Statement statement = null;
            ResultSet resultSet = null;
            List<Map> data = new ArrayList<Map>();

            try{
                AgSupDatasource dbsource = supDatasource.selectDataSourceByName("PostGIS规划数据库");
                if(dbsource.getDbUrl().contains("postgresql")) {

                    String where = "";

                    if(condition != null) {

                        JSONObject jsonObject = JSONObject.fromObject(condition);
                        Iterator iterator = jsonObject.keys();

                        while (iterator.hasNext()) {
                            String key = (String) iterator.next();
                            String value = jsonObject.get(key).toString();
                            where = "where "+ where +  key +" = '" + value + "'";
                            break;
                        }
                    }


                    String driver = "org.postgresql.Driver";
                    Class.forName(driver);
                    con = DriverManager.getConnection(dbsource.getDbUrl(),  dbsource.getUserName(), DESedeUtil.desDecrypt(dbsource.getPassword()));
                    if(con != null) {
                        String sql = "select t.*, st_astext(geom) wkt from "+tableName+" t " + where;
                        System.out.println("SQL:" + sql);
                        statement = con.createStatement();
                        resultSet = statement.executeQuery(sql);
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        //返回结果
                        while (resultSet.next()) {
                            Map<String, String> map = new HashMap<String, String>();
                            for(int i=1; i<=metaData.getColumnCount(); i++){
                                String column = metaData.getColumnName(i);
                               // if(!"GEOM".equals(column.toUpperCase())) {
                                    String value = resultSet.getString(column.toLowerCase());
                                    map.put(column.toLowerCase(), value);
                               // }
                            }
                            data.add(map);
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if(con != null) {
                    con.close();
                }
                if(statement != null) {
                    statement.close();
                }
            }
            return data;
    }

    public boolean delProjectInfoByxmdm(String tableName, String condition) throws SQLException {
        Connection con = null;
        Statement statement = null;
        int flag = -1;
        try{

            AgSupDatasource dbsource = supDatasource.selectDataSourceByName("PostGIS规划数据库");

            if(dbsource.getDbUrl().contains("postgresql")) {

                String where = "";

                if(condition != null) {

                    JSONObject jsonObject = JSONObject.fromObject(condition);
                    Iterator iterator = jsonObject.keys();

                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        String value = jsonObject.get(key).toString();
                        where = "where "+ where +  key +" = '" + value + "'";
                        break;
                    }
                }

//                if(id != null) {
//                    where = "where "+ where +  "id = '" + id + "'";
//                }else if(xmdm != null){
//                    where = "where "+ where +  "xmdm = '" + xmdm + "'";
//
//                }


                String driver = "org.postgresql.Driver";
                Class.forName(driver);
                con = DriverManager.getConnection(dbsource.getDbUrl(), dbsource.getUserName(), DESedeUtil.desDecrypt(dbsource.getPassword()));
                if(con != null) {
                    String sql = "DELETE FROM  "+tableName+" t " + where;
                    System.out.println("SQL:" + sql);
                    statement = con.createStatement();
                    flag = statement.executeUpdate(sql);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(con != null) {
                con.close();
            }
            if(statement != null) {
                statement.close();
            }
        }
        return flag >= 0;
    }

   public boolean updateGeometryInfo(String tableName, String condition, String  keyValue) throws SQLException {
       Connection con = null;
       Statement statement = null;
       int flag = -1;
       try{

           AgSupDatasource dbsource = supDatasource.selectDataSourceByName("PostGIS规划数据库");

           if(dbsource.getDbUrl().contains("postgresql")) {

               String where = "";

//               if(id != null) {
//                   where = "where "+ where +  "id = '" + id + "'";
//               }else if(xmdm != null){
//                   where = "where "+ where +  "xmdm = '" + xmdm + "'";
//
//               }

               if(condition != null) {

                   JSONObject jsonObject = JSONObject.fromObject(condition);
                   Iterator iterator = jsonObject.keys();

                   while (iterator.hasNext()) {
                       String key = (String) iterator.next();
                       String value = jsonObject.get(key).toString();
                       where = "where "+ where +  key +" = '" + value + "'";
                       break;
                   }
               }


               String driver = "org.postgresql.Driver";
               Class.forName(driver);
               con = DriverManager.getConnection(dbsource.getDbUrl(), dbsource.getUserName(), DESedeUtil.desDecrypt(dbsource.getPassword()));
               if(con != null) {
                   String sql = "UPDATE " + tableName + " set " + keyValue + " " + where;
                   System.out.println("SQL:" + sql);
                   statement = con.createStatement();
                   flag = statement.executeUpdate(sql);
               }
           }
       }
       catch (Exception e) {
           e.printStackTrace();
       }
       finally {
           if(con != null) {
               con.close();
           }
           if(statement != null) {
               statement.close();
           }
       }
       return flag >= 0 ;
    }

}
