package com.augurit.agcloud.agcom.agsupport.sc.geocoding.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.util.SwLocateUtil;
import com.augurit.agcloud.agcom.agsupport.sc.geocoding.service.IAgGeocoding;
import com.common.dbcp.DBHelper;
import com.common.dbcp.DBPool;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by czh on 2018-02-05.
 */
@Service
public class AgGeocodingImpl implements IAgGeocoding {

    public List<Map> searchByAddress(String address) {
        try {
            DBPool.isPrintSQL = true;
            SwLocateUtil st = new SwLocateUtil("geo", "BUS", "DZ"); //数据源,标准地址库表名,地址字段名
            List<Map> list = st.searchMap(address);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map> searchByPoint(String wkt, String type) {
        try {
            DBPool.isPrintSQL = true;
            Pattern pattern = Pattern.compile("(?<=\\()(.+?)(?=\\))");
            Matcher matcher = pattern.matcher(wkt);
            String wktStr = null;
            if (matcher.find())
                wktStr = matcher.group();
            String[] xy = wktStr.trim().split(" ");
            if (xy.length != 2) return null;
            String srid = this.getSrid("geo", "BUS");
            String range = type.equals("degree") ? "0.0001020" : "10";
            String sql = "select sde.st_astext(SHAPE) wkt,DZ from BUS t where sde.st_intersects(t.shape,(select sde.st_buffer(sde.st_point(" + xy[0] + "," + xy[1] + ", " + srid + "), " + range + ") from dual)) = 1";
            List<Map> list = DBHelper.find("geo", sql, null);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Srid
     *
     * @param datasourceId
     * @param st_table
     * @return
     */
    private String getSrid(String datasourceId, String st_table) throws Exception {
        String sql = " SELECT SRID FROM SDE.ST_GEOMETRY_COLUMNS WHERE upper(TABLE_NAME) = upper('" + st_table + "') AND upper(OWNER) = upper('sde')";
        Map map = DBHelper.findFirst(datasourceId, sql, null);
        return map.get("SRID").toString();
    }
}
