package com.augurit.agcloud.agcom.agsupport.common.listener;

import com.common.dbcp.DBPool;
import com.common.util.Common;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Agcom监听器
 *
 * @author Hunter
 * @Time 2017-04-27
 */
@WebListener
public class AgcomListener implements ServletContextListener {

    private Log log = LogFactory.getLog(AgcomListener.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //DBPool init  start
        log.info("DBPool contextInitialized");
        Map dsMap = new HashMap();
        dsMap.put("spring.datasource", dataSource);
        DBPool.setDataSourceMap(dsMap);
        DBPool.isPrintSQL = "true".equalsIgnoreCase(Common.getByKey("db.printsql", "true"));
        DBPool.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
