package com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.util;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :9:33 2019/1/10
 * @Modified By:
 */
public class JDBCUtil {

    public static Connection getConn(){
        Connection conn = null;
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@192.168.11.236:1521/orcl";
            String user = "sde";
            String pasw = "sde";

            //String url = "jdbc:oracle:thin:@localhost:1521/orcl";
            //String user = "agcom";
            //String pasw = "agcom";
            conn = DriverManager.getConnection(url,user,pasw);

            //Class.forName("org.postgresql.Driver");
            //String url = "jdbc:postgresql://192.168.30.107:5432/agsupport_agcloud";
            //String user = "postgres";
            //String pasw = "123456";
            //conn = DriverManager.getConnection(url,user,pasw);
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }
}
