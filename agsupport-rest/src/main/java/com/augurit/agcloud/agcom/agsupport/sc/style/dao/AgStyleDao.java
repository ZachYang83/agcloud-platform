package com.augurit.agcloud.agcom.agsupport.sc.style.dao;

import com.common.dbcp.DBHelper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-10-11.
 */
@Repository
public class AgStyleDao {

    private int limit = 50;

    public List<Map> listTableColumnData(String dataSourceId, String tableName, String column) throws Exception {
        String sql = "select distinct " + column + " as value from " + tableName + " where " + column + " is not null ";
        double count = DBHelper.countResult(dataSourceId, sql, null);
        if (count > limit) {
            return new ArrayList<Map>();
        }
        return DBHelper.find(dataSourceId, sql, null);
    }
}
