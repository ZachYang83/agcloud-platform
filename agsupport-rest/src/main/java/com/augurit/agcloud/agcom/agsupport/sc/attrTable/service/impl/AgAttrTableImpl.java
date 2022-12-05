package com.augurit.agcloud.agcom.agsupport.sc.attrTable.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.attrTable.service.IAgAttrTable;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.query.dao.AgDBQueryDao;
import com.common.dbcp.DBHelper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by chendingxing on 2017-08-02.
 */
@Service
public class AgAttrTableImpl implements IAgAttrTable {

    @Autowired
    private AgDBQueryDao agDbQueryDao;

    @Override
    public List<Map> query(Layer layer, Page page) {
        List<Map> results = agDbQueryDao.queryAttrTableBySQL(layer,page);
        return results;
    }

    @Override
    public int update(AgLayer table, String update,String where) throws Exception {
        StringBuffer sql = new StringBuffer("update ");
        sql.append(table.getLayerTable()).append(" set ").append(update);
        if(where != null){
            sql.append(" where ").append(where);
        }
        return DBHelper.executeUpdate(table.getDataSourceId(),sql.toString(),null);
    }

    @Override
    public int insert(AgLayer table, List<Map> insert) throws Exception {
        String []pks = {table.getPkColumn()};
        return DBHelper.save(table.getDataSourceId(),table.getLayerTable(),pks ,insert);
    }

    @Override
    public int delete(AgLayer table,String column,String []values) throws Exception {
        DBHelper.deleteBy(table.getDataSourceId(), table.getLayerTable(),column,values);
        return 0;
    }
}
