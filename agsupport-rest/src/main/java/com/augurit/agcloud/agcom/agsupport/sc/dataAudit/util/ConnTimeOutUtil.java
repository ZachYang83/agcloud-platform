package com.augurit.agcloud.agcom.agsupport.sc.dataAudit.util;

import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;

import java.sql.Connection;
import java.util.concurrent.*;

public class ConnTimeOutUtil {
    public static Connection getConnTimeOut(String dataSourceId){
        long s = System.currentTimeMillis();
        Connection conn = null;
        Callable<Connection> task = new Callable<Connection>() {
            @Override
            public Connection call() throws Exception {
                Connection conn2 = JDBCUtils.getConnection(dataSourceId);
                return conn2;
            }
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Connection> future = executorService.submit(task);
        try {
            conn = future.get(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
           return null;
        } catch (TimeoutException e) {
            System.out.println("连接时间数据源超时："+dataSourceId);
            return null;
        }finally {
            long e = System.currentTimeMillis();
            System.out.println("连接时间："+(e-s)/1000);
        }
        return conn;
    }
}
