package com.augurit.agcloud.agcom.agsupport.sc.spatial.stats.dao;

import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Range;
import com.common.dbcp.DBHelper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.*;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-04-20.
 */
@Repository
public class AgDBStatsDao {

    public Integer statsCount(Layer layer) {
        Integer count = 0;
        try {
            long startMili = System.currentTimeMillis();
            StringBuffer sql = new StringBuffer("select ").append(" count(*) ").append(STATS_COLUMN).append(" from ")
                    .append(layer.getLayerTable()).append(" a where ").append(layer.getPkColumn()).append(" is not null ");
            if (layer.getWhere() != null) {
                sql.append(" and ").append(layer.getWhere());
            }
            List<Map> data = JDBCUtils.find(layer.getDataSourceId(), sql.toString(), layer.getValues());
            //List<Map> data = DBHelper.find(layer.getDataSourceId(), sql.toString(), layer.getValues());
            count = Integer.parseInt(data.get(0).get(STATS_COLUMN).toString());
            long endMili = System.currentTimeMillis();
            System.out.println("--------------------计算表" + layer.getLayerTable() + "总记录数共耗时：" + (endMili - startMili) / 1000.0 + "秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public Map<String, Double> statsAllBySQL(Layer layer) {
        Map<String, Double> result = new HashMap();
        try {
            StringBuffer sql = new StringBuffer("select ");
            if (layer.getGroupColumn() == null) {
                if (layer.getStatsType().equals(STATS_COUNT)) {
                    sql.append(" count(").append(layer.getStatsColumn()).append(") ").append(STATS_COLUMN);
                } else if (layer.getStatsType().equals(STATS_SUM)) {
                    sql.append(" sum(").append(layer.getStatsColumn()).append(") ").append(STATS_COLUMN);
                } else if (layer.getStatsType().equals(STATS_AVG)) {
                    sql.append(" avg(").append(layer.getStatsColumn()).append(") ").append(STATS_COLUMN);
                } else if (layer.getStatsType().equals(STATS_MAX)) {
                    sql.append(" max(").append(layer.getStatsColumn()).append(") ").append(STATS_COLUMN);
                } else if (layer.getStatsType().equals(STATS_MIN)) {
                    sql.append(" min(").append(layer.getStatsColumn()).append(") ").append(STATS_COLUMN);
                }
            } else {
                if (layer.getStatsType().equals(STATS_COUNT)) {
                    sql.append(" count(").append(layer.getStatsColumn()).append(") ").append(STATS_COLUMN).append(", ").append(layer.getGroupColumn());
                } else if (layer.getStatsType().equals(STATS_SUM)) {
                    sql.append(" sum(").append(layer.getStatsColumn()).append(") ").append(STATS_COLUMN).append(", ").append(layer.getGroupColumn());
                } else if (layer.getStatsType().equals(STATS_AVG)) {
                    sql.append(" avg(").append(layer.getStatsColumn()).append(") ").append(STATS_COLUMN).append(", ").append(layer.getGroupColumn());
                } else if (layer.getStatsType().equals(STATS_MAX)) {
                    sql.append(" max(").append(layer.getStatsColumn()).append(") ").append(STATS_COLUMN).append(", ").append(layer.getGroupColumn());
                } else if (layer.getStatsType().equals(STATS_MIN)) {
                    sql.append(" min(").append(layer.getStatsColumn()).append(") ").append(STATS_COLUMN).append(", ").append(layer.getGroupColumn());
                }
            }
            sql.append(" from ").append(layer.getLayerTable()).append(" a where 1=1");
            if (layer.getWhere() != null) {
                sql.append(" and ").append(layer.getWhere());
            }
            if (layer.getGroupColumn() != null) {
                sql.append(" group by ").append(layer.getGroupColumn());
            }
            List<Map> data = DBHelper.find(layer.getDataSourceId(), sql.toString(), layer.getValues());
            result = this.formatResult(data, layer.getStatsColumn(), layer.getGroupColumn());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Double> statsOvalBySQL(Layer layer, Point point, double distance) {
        Map<String, Double> result = new HashMap();
        try {
            List values = new ArrayList();
            StringBuffer where = new StringBuffer(" 1=1 ");
            if (layer.getxColumn() != null && layer.getyColumn() != null) {
                where.append(" and ").append(" (").append(point.x).append(" - ").append(layer.getxColumn()).append(" ) * (")
                        .append(point.x).append(" - ").append(layer.getxColumn()).append(" ) + (").append(point.y)
                        .append(" - ").append(layer.getyColumn()).append(" ) * (").append(point.y).append(" - ")
                        .append(layer.getyColumn()).append(" ) <= ?");
                values.add(distance * distance);
                if (layer.getWhere() != null) {
                    where.append(" and ").append(layer.getWhere());
                }
                if (layer.getValues() != null && layer.getValues().size() > 0) {
                    values.addAll(layer.getValues());
                }
                layer.setWhere(where.toString());
                layer.setValues(values);
                result = statsAllBySQL(layer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Double> statsRangeBySQL(Layer layer, Range range) {
        Map<String, Double> result = new HashMap();
        try {
            List values = new ArrayList();
            StringBuffer where = new StringBuffer(" 1=1 ");
            if (layer.getWhere() != null) {
                where.append(" and ").append(layer.getWhere());
            }
            if (layer.getxColumn() != null && layer.getyColumn() != null) {
                where.append(" and ").append(layer.getxColumn()).append(" >= ? and ").append(layer.getxColumn()).append(" <= ? and ")
                        .append(layer.getyColumn()).append(" >= ? and ").append(layer.getyColumn()).append(" <= ?");
                values.add(range.getMinX());
                values.add(range.getMaxX());
                values.add(range.getMinY());
                values.add(range.getMaxY());
                if (layer.getWhere() != null) {
                    where.append(" and ").append(layer.getWhere());
                }
                if (layer.getValues() != null && layer.getValues().size() > 0) {
                    values.addAll(layer.getValues());
                }
                layer.setWhere(where.toString());
                layer.setValues(values);
                result = statsAllBySQL(layer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将数据库查询结果转换为Map格式
     *
     * @param data
     * @param statsColumn
     * @param groupColumn
     * @return
     */
    private Map<String, Double> formatResult(List<Map> data, String statsColumn, String groupColumn) {
        Map<String, Double> result = new HashMap();
        try {
            for (Map dataMap : data) {
                double statsResult = Double.valueOf(dataMap.get(STATS_COLUMN).toString());
                if (groupColumn == null) {
                    result.put(statsColumn, statsResult);
                } else {
                    result.put((String) dataMap.get(groupColumn), statsResult);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}