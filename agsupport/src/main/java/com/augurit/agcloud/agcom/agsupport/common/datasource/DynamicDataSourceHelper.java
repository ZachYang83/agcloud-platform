package com.augurit.agcloud.agcom.agsupport.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.augurit.agcloud.agcom.agsupport.common.util.DESedeUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgSupDatasource;
import com.augurit.agcloud.agcom.agsupport.sc.agdatasource.service.IAgSupDatasource;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangmingyang
 * @Description: 从数据源管理配置中获取数据源相关信息
 * @date 2019-01-10 14:08
 */
@Component
public class DynamicDataSourceHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceHelper.class);

    @Autowired
    private IAgSupDatasource iAgSupDatasource;

    private static DynamicDataSourceHelper dataSourceHelper;

    @PostConstruct
    public void init(){
        dataSourceHelper = this;
        dataSourceHelper.iAgSupDatasource = this.iAgSupDatasource;
    }
    /**
     * 根据数据源id创建数据源连接池
     * @param dataSourceId
     * @return
     */
    public static DataSource createDataSource(String dataSourceId){
        AgSupDatasource agSupDatasource = null;
        try {
            agSupDatasource = dataSourceHelper.iAgSupDatasource.selectDataSourceById(dataSourceId);
        }catch (Exception e){
            LOGGER.info("根据数据源id查询数据源异常!");
            e.printStackTrace();
        }
        DruidDataSource datasource = new DruidDataSource();
        if (agSupDatasource != null){
            datasource.setUrl(agSupDatasource.getDbUrl());
            datasource.setUsername(agSupDatasource.getUserName());
            datasource.setPassword(DESedeUtil.desDecrypt(agSupDatasource.getPassword()));
            datasource.setDbType(agSupDatasource.getDbType().toLowerCase());
            if (agSupDatasource.getDbType().toLowerCase().equals(DbConstants.ORACLE)){
                datasource.setDriverClassName(DbConstants.ORACLE_DRIVER);
                datasource.setValidationQuery("select 1 from dual");
            }
            if (agSupDatasource.getDbType().toLowerCase().equals(DbConstants.POSTGRESQL)){
                datasource.setDriverClassName(DbConstants.POSTGRESQL_DRIVER);
                datasource.setValidationQuery("select 1");
            }
            String maxconnection = agSupDatasource.getMaxconnection();
            String minconnection = agSupDatasource.getMinconnection();
            if (StringUtils.isNotBlank(maxconnection)){
                datasource.setMaxActive(Integer.valueOf(maxconnection));
            }
            if (StringUtils.isNotBlank(minconnection)){
                datasource.setMinIdle(Integer.valueOf(minconnection));
            }
            datasource.setMaxWait(10000);
            //datasource.setRemoveAbandoned(true);
            //datasource.setRemoveAbandonedTimeout(300);
            //调试的时候测试用
            //datasource.isLogAbandoned();
            datasource.setTestWhileIdle(true);
            datasource.setTestOnBorrow(true);
            datasource.setTestOnReturn(true);
            //设置获取连接出错时的自动重连次数
            datasource.setConnectionErrorRetryAttempts(2);
            //设置获取连接时的重试次数，-1为不重试
            datasource.setNotFullTimeoutRetryCount(-1);
            datasource.setTimeBetweenConnectErrorMillis(300000);
            //datasource.setTimeBetweenEvictionRunsMillis(30000);
            // 默认 1000L * 60L * 30L;
            //datasource.setMaxEvictableIdleTimeMillis(300000);
            return datasource;
        }else {
            return null;
        }

    }

}
