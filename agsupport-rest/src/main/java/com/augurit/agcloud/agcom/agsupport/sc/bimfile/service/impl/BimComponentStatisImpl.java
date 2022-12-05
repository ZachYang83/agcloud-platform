package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimComponentStatis;
import com.common.util.page.Pager;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BimComponentStatisImpl implements IBimComponentStatis {

    @Override
    public Map componentStatisticsBy(String tableName, String datasourceId, Pager page) throws Exception {

       // String sql = "select * from (SELECT ROWNUM no, name, count from (SELECT name，count(name) count FROM TableName where infotype = \'ElementInfo\' GROUP BY name ORDER BY name) ) WHERE no >=FromIdx AND no <= ToIdx ORDER BY name";
       // sql = sql.replace("TableName", tableName).replace("FromIdx", fromIdx).replace("ToIdx", toIdx);
        Map map = new HashMap();

        String sql = "select * from (SELECT ROWNUM, name, count from (SELECT name，count(name) count FROM TableName where infotype = \'ElementInfo\' GROUP BY name ORDER BY name) ) ";
        sql = sql.replace("TableName", tableName);
        long total = JDBCUtils.countResult(datasourceId, sql, null);
        List list = JDBCUtils.findPage(datasourceId, page, sql, null, true);

        map.put("total", total);
        map.put("result", list);
        return map;
    }

    @Override
    public boolean bimAttributeDataIsExist(String tableName, String datasourceId) throws Exception {

        String sql = "select count(*) isExist from user_tables where table_name =upper('TableName')".replace("TableName", tableName);
        Map  map = JDBCUtils.find(datasourceId,sql, null).get(0);
         return Integer.parseInt(map.get("isexist").toString()) >0 ;
    }
}
