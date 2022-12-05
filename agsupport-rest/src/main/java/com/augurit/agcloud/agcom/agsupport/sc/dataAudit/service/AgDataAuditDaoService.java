package com.augurit.agcloud.agcom.agsupport.sc.dataAudit.service;

import com.augurit.agcloud.agcom.agsupport.sc.dataAudit.dao.AgDataAuditDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :15:36 2019/2/20
 * @Modified By:
 */
@Service
public class AgDataAuditDaoService {

    @Autowired
    private AgDataAuditDao auditDao;

    public List<Integer> findList(Connection conn, String layerTable, int auditType, String startTime, String endTime) {
        try {
            return auditDao.findList(conn,layerTable,auditType,startTime,endTime);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Map<String, String>> getLayerTableInfoPage(String dataSourceId, String layerTable,String startTime,String endTime, int pageNum, int pageSize, int auditType, LinkedList<Map<String, String>> tableFields) throws Exception {
        Map<String,String> map = new HashMap<>();
        for (Map<String,String> tableField : tableFields){
            String fieldName = tableField.get("fieldName");
            String fieldNameCn = tableField.get("fieldNameAlias");
            map.put(fieldName,fieldNameCn);//字段名称，字段中文名称
        }
        return auditDao.getLayerTableInfoPage(dataSourceId,layerTable,startTime,endTime,pageNum,pageSize,auditType,map);
    }

    public int saveAuditInfo(String dataSourceId,String layerTable,int state,String primaryKey,String keyValues,String notPassReason) throws Exception {

        return auditDao.saveAuditInfo(dataSourceId,layerTable,state,notPassReason,primaryKey ,keyValues);
    }

    public int getCount(String dataSourceId,String layerTable) throws Exception {
        return auditDao.getCount(dataSourceId,layerTable);
    }
}
