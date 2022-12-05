package com.augurit.agcloud.agcom.agsupport.sc.spatial.stats.service.impl;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Range;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.query.service.IAgDBQuery;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.stats.dao.AgDBStatsDao;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.stats.service.IAgDBStats;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import com.common.util.ReflectBeans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spatial.GeometryApplication;
import spatial.GeometryOperate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.*;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-04-20.
 */
@Service
@SuppressWarnings("unchecked")
public class AgDBStatsImpl implements IAgDBStats {

    @Autowired
    private AgDBStatsDao agDbStatsDao;
    @Autowired
    private IAgDBQuery dbQuery;

    @Override
    public Integer statsCount(Layer layer) {
        Layer statsLayer = ReflectBeans.copy(layer, new Layer());
        return agDbStatsDao.statsCount(statsLayer);
    }

    @Override
    public Map<String, Double> statsAll(Layer layer) {
        Layer statsLayer = ReflectBeans.copy(layer, new Layer());
        return agDbStatsDao.statsAllBySQL(statsLayer);
    }

    @Override
    public Map<String, Double> statsByOval(Layer layer, Point point, double r) {
        Map<String, Double> result = new HashMap();
        try {
            Layer statsLayer = ReflectBeans.copy(layer, new Layer());
            if (statsLayer.getxColumn() != null && statsLayer.getyColumn() != null) {
                result = agDbStatsDao.statsOvalBySQL(statsLayer, point, r);
            } else {
                String wkt = GeometryOperate.buffer(SpatialUtil.pointToWKT(point), r);
                result = this.statsByWKT(statsLayer, wkt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Double> statsByExtent(Layer layer, Range range) {
        Map<String, Double> result = new HashMap();
        try {
            Layer statsLayer = ReflectBeans.copy(layer, new Layer());
            if (statsLayer.getxColumn() != null && statsLayer.getyColumn() != null) {
                result = agDbStatsDao.statsRangeBySQL(statsLayer, range);
            } else {
                String wkt = SpatialUtil.rangeToWKT(range);
                result = this.statsByWKT(statsLayer, wkt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Double> statsByPolyline(Layer layer, List<Point> points) {
        Map<String, Double> result = new HashMap();
        try {
            String wkt = SpatialUtil.pointListToWKT(points, LINESTRING);
            result = this.statsByWKT(layer, wkt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Double> statsByPolygon(Layer layer, List<Point> points) {
        Map<String, Double> result = new HashMap();
        try {
            String wkt = SpatialUtil.pointListToWKT(points, POLYGON);
            result = this.statsByWKT(layer, wkt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Double> statsByWKT(Layer layer, String wkt) {
        Map<String, Double> result = new HashMap();
        try {
            Layer statsLayer = ReflectBeans.copy(layer, new Layer());
            if (GeometryApplication.checkWkt(wkt)) {
                if (statsLayer.getxColumn() != null && statsLayer.getyColumn() != null) {
                    Range range = SpatialUtil.getRangeByWKT(wkt);
                    List<Map> data = dbQuery.queryByExtent(statsLayer, range);
                    result = GeometryApplication.statsIntersects(data, statsLayer.getStatsColumn(), statsLayer.getGroupColumn(), statsLayer.getStatsType(), wkt);
                } else {
                    List<Map> data = dbQuery.queryByWKT(statsLayer, wkt);
                    result = GeometryApplication.statsIntersects(data, statsLayer.getStatsColumn(), statsLayer.getGroupColumn(), statsLayer.getStatsType(), wkt);
                }
            } else {
                result = statsAll(layer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Double> statsByWKT(List<Map> data, String statsColumn, String groupColumn, String type, String wkt) {
        return GeometryApplication.statsIntersects(data, statsColumn, groupColumn, type, wkt);
    }
}
