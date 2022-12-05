package com.augurit.agcloud.agcom.agsupport.util;
import java.sql.*;
import java.util.regex.Pattern;

public class JdbcSqlServerUtil {
    static {
        try {
            //注册驱动
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        }
        catch (Exception ex){
            System.out.println("注册SQLServerDriver驱动失败");
        }
    }

    public static Connection getConnection(String connectUrl,String user,String password) throws Exception{
        DriverManager.setLoginTimeout(1);
        return  DriverManager.getConnection(connectUrl,user,password);
    }

    private static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
            + "(\\b(select|update|union|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

    private static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

    public static boolean isValidParam(String str)
    {
        if (sqlPattern.matcher(str).find())
        {
            return false;
        }
        return true;
    }

    /*
     *
     * 释放资源
     *
     *
     */
    public static void release(Connection conn,Statement st,ResultSet rs) {
        closeRs(rs);
        closeSt(st);
        closeConn(conn);
    }

    private static void closeRs(ResultSet rs) {
        try {
            if(rs!=null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs=null;
        }

    }

    private static void closeSt(Statement st) {
        try {
            if(st!=null) {
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            st=null;
        }

    }

    private static void closeConn(Connection conn) {
        try {
            if(conn!=null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn=null;
        }

    }
}
