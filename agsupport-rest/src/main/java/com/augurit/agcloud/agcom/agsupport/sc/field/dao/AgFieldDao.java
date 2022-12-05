package com.augurit.agcloud.agcom.agsupport.sc.field.dao;

import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayerFieldConf;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig;
import com.common.dbcp.DBHelper;
import com.common.util.Common;
import com.common.util.ConfigProperties;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.ORACLE;
import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.POSTGRESQL;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-05-17.
 */
@Repository
public class AgFieldDao {
    public List<Map> getLayerFieldFromDB(String dataSourceId, String layerTable) throws Exception{
        List<Map> data = new ArrayList();
        //String dbType = SpatialUtil.getDbTypeById(dataSourceId);
        //TODO:获取数据库类型
        String dbType = JDBCUtils.getDbType(dataSourceId);
        if (dbType.equals(SpatialConfig.DB_TYPE_MYSQL)) {
            String dbName = null;
            String url = ConfigProperties.getByKey(dataSourceId + ".url");
            if(url != null && url.contains("?")){
                String str = url.split("\\?")[0];
                dbName = str.substring(str.lastIndexOf("/") + 1);
            }else if(url != null){
                dbName = url.substring(url.lastIndexOf("/") + 1);
            }
            String sql;
            List<String> value = new ArrayList();
            value.add(layerTable);
            if(!Common.isCheckNull(dbName)){
                sql = "select lower(column_name) field_name, lower(data_type) field_type,(case when character_maximum_length" +
                        " is not null then character_maximum_length else numeric_precision end ) field_size from" +
                        " information_schema. columns where table_name = ? and table_schema = ? order by ordinal_position";
                value.add(dbName);
            }else{
                sql = "select lower(column_name) field_name, lower(data_type) field_type,(case when character_maximum_length" +
                        " is not null then character_maximum_length else numeric_precision end ) field_size from" +
                        " information_schema. columns where table_name = ? order by ordinal_position";
            }
            //data = DBHelper.find(dataSourceId, sql, value);
            data = JDBCUtils.find(dataSourceId,sql,value);

        } else if (dbType.equals(SpatialConfig.DB_TYPE_ORACLE)||dbType.equals(ORACLE)) {
            String sql = "select lower(column_name) field_name,lower(data_type) field_type,NULLABLE,data_length field_size from" +
                    " user_tab_columns where table_name = upper(?) order by column_id";
            List value = Arrays.asList(layerTable);
            //data = DBHelper.find(dataSourceId, sql, value);
            data = JDBCUtils.find(dataSourceId,sql,value);
        }else if(dbType.equals(SpatialConfig.DB_TYPE_DB2)){
            String sql = "select lower(a.name) field_name,lower(a.coltype) field_type,NULLABLE,a.longlength field_size from" +
                    " sysibm.syscolumns a where a.tbname = upper(?)";
            List value = Arrays.asList(layerTable);
            //data = DBHelper.find(dataSourceId, sql, value);
            data = JDBCUtils.find(dataSourceId,sql,value);
        }else if(dbType.equals(SpatialConfig.DB_TYPE_POSTGIS)||dbType.equals(POSTGRESQL)){
            String sql = "SELECT a.attname AS field_name,t.typname AS field_type,a.attlen AS field_size FROM pg_class c,pg_attribute a " +
                    "LEFT OUTER JOIN pg_description b ON a.attrelid=b.objoid AND a.attnum = b.objsubid,pg_type t " +
                    "WHERE c.relname = lower(?) and a.attnum > 0 and a.attrelid = c.oid and a.atttypid = t.oid ORDER BY a.attnum;";
            List value = Arrays.asList(layerTable);
            //data = DBHelper.find(dataSourceId, sql, value);
            data = JDBCUtils.find(dataSourceId,sql,value);
        }
        return data;
    }


    public List<AgLayerFieldConf> findByDirLayerId(String dirLayerId)throws Exception{
        //String sql = "select * from AG_LAYER_FIELD t WHERE t.layer_id IN(select layer_id from AG_DIR_LAYER t WHERE t.id = ?)";
        String sql ="SELECT  * FROM (select * from AG_LAYER_FIELD t WHERE t.layer_id IN(select layer_id from AG_DIR_LAYER t WHERE t.id = ?)) b" +
                "   LEFT   JOIN AG_FIELD_AUTHORIZE a on a.FIELD_ID = b.ID where  a.FIELD_ID is not null";

        //String sql = "SELECT  * FROM  (select * from AG_FIELD_AUTHORIZE a where a.field_id in (select id from AG_LAYER_FIELD t WHERE t.layer_id IN (select layer_id from AG_DIR_LAYER t WHERE t.id = ?))) b " +
            //    "   LEFT   JOIN AG_LAYER_FIELD on AG_LAYER_FIELD.ID = b.FIELD_ID";
        List<Object> values = new ArrayList<Object>();
        values.add(dirLayerId);

        return DBHelper.find(AgLayerFieldConf.class, sql, values);
    }

    public List<AgLayerFieldConf> findByLayerId(String layerId)throws Exception{
        //String sql = "select * from AG_LAYER_FIELD t WHERE t.layer_id = ?";
        String  sql ="select * from AG_LAYER_FIELD t WHERE t.layer_id =?";
//        " "SELECT  * FROM (select * from AG_LAYER_FIELD t WHERE t.layer_id =?) b" +
//                " LEFT   JOIN AG_FIELD_AUTHORIZE a on a.FIELD_ID = b.ID where  a.FIELD_ID is not null";
//        String sql = "SELECT  * FROM  (select * from AG_FIELD_AUTHORIZE a where a.field_id in (select id from AG_LAYER_FIELD t WHERE t.layer_id = ?)) b " +
//                "   LEFT   JOIN AG_LAYER_FIELD on AG_LAYER_FIELD.ID = b.FIELD_ID";
        List<Object> values = new ArrayList<Object>();
        values.add(layerId);
        return DBHelper.find(AgLayerFieldConf.class, sql, values);
    }
}
