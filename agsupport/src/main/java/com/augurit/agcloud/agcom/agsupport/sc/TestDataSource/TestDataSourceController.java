package com.augurit.agcloud.agcom.agsupport.sc.TestDataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.augurit.agcloud.agcom.agsupport.common.datasource.DynamicDataSource;
import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;
import com.augurit.agcloud.agcom.agsupport.domain.AgServer;
import com.augurit.agcloud.agcom.agsupport.sc.server.service.IAgServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangmingyang
 * @Description: TODO
 * @date 2019-01-03 15:25
 */
@RestController
@RequestMapping("/TestDataSource")
public class TestDataSourceController{
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataSourceController.class);
    @RequestMapping("/add")
    public void addDataSource(String dataSourceName,String url,String name ,String port,String password,String driverClassName){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(name);
        datasource.setPassword(password);
        //datasource.setDriverClassName(driverClassName);
        datasource.setMaxActive(10);
        //Map<Object,Object> map = new HashMap<>();
        //map.put(dataSourceName, datasource);
       /* Callable ca1 = new Callable(){
            @Override
            public String call() throws Exception {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "线程完毕";
            }
        };

        DynamicDataSource.getInstance().putDataSource(dataSourceName,dataSourceFuture);*/
    }

    @RequestMapping("/show")
    public Object showDataSource(){
        DynamicDataSource instance = DynamicDataSource.getInstance();
        //Map<Object, DataSource> dataSourceMap = DynamicDataSource.getInstance().getDataSourceMap();
        return instance.getDataSourceMap();//dataSourceMap.keySet();
    }

    @Autowired
    private IAgServer agServer;
    @RequestMapping("/select")
    public Map select(String dataSource,String id) throws Exception {
        //DataSource dataSource1 = DynamicDataSource.getInstance().getConnection(dataSource);
        DataSource dataSource1 = DynamicDataSource.getInstance().getDataSource(dataSource);
        Connection connection = dataSource1.getConnection();
        PreparedStatement statement = connection.prepareStatement("select id,name from ag_server where id=?");
        statement.setString(1,"5ed0f5e8-2db1-40f0-a635-dcd670b63991");
        ResultSet rs = statement.executeQuery();
        Map map = new HashMap();
        while(rs.next()){
            //rs.get+数据库中对应的类型+(数据库中对应的列别名)
            String id1 = rs.getString("id");
            String name = rs.getString("name");
            map.put("id",id1);
            map.put("name",name);
        }
        JDBCUtils.close(connection,statement,rs);
        return map;
    }

}
