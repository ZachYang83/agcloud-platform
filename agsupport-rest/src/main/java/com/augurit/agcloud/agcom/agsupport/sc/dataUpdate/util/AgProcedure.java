package com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.util;

import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author:Dreram
 * @Description: 创建存储过程
 * @Date:created in :16:13 2019/1/17
 * @Modified By:
 */
public class AgProcedure {

    /**
     * 创建存储过程
     * @param dataSourceId
     * @param layerTable
     * @param procedureSql
     * @return
     */
    public static String shapProcedure(String dataSourceId,String layerTable,String procedureSql) throws Exception {
        PreparedStatement pstm = null;
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection(dataSourceId);
            pstm = conn.prepareStatement(procedureSql);
            pstm.executeUpdate();
            System.out.println("创建存储过程成功： "+procedureSql);
            return "创建存储过程成功";
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("创建存储过程失败： "+procedureSql);
            return null;
        } finally {
                try {
                    if (pstm != null) pstm.close();
                    if (conn !=null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
