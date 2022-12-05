package com.augurit.agcloud.agcom.agsupport.sc.spatial.buffer.service.impl;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Range;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.buffer.service.IAgDBBuffer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.query.service.IAgDBQuery;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.stats.service.IAgDBStats;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spatial.GeometryOperate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.LINESTRING;
import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.POLYGON;

/**
 * Created by q on 2017-03-31.
 */
@Service
public class AgDBBufferImpl implements IAgDBBuffer {

    @Autowired
    private IAgDBQuery dbQuery;

    @Autowired
    private IAgDBStats dbStats;

    @Override
    public List<Map> bufferQueryByPoint(Layer layer, Point point, double distance) {
        return dbQuery.queryByOval(layer, point, distance);
    }

    @Override
    public List<Map> bufferQueryByExtent(Layer layer, Range range) {
        return dbQuery.queryByExtent(layer, range);
    }

    @Override
    public List<Map> bufferQueryByPolyline(Layer layer, List<Point> points, double distance) {
        List<Map> result = new ArrayList<Map>();
        try {
            String wkt = SpatialUtil.pointListToWKT(points, LINESTRING);
            String bufferWKT = GeometryOperate.buffer(wkt, distance);
            result = dbQuery.queryByWKT(layer, bufferWKT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map> bufferQueryByPolygon(Layer layer, List<Point> points, double distance) {
        List<Map> result = new ArrayList<Map>();
        try {
            String wkt = SpatialUtil.pointListToWKT(points, POLYGON);
            String bufferWKT = GeometryOperate.buffer(wkt, distance);
            result = dbQuery.queryByWKT(layer, bufferWKT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map> bufferQueryByWKT(Layer layer, String wkt, double distance) {
        List<Map> result = new ArrayList<Map>();
        try {
            String bufferWKT = GeometryOperate.buffer(wkt, distance);
            result = dbQuery.queryByWKT(layer, bufferWKT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map> bufferQueryByWKT(List<Map> data, String wkt, double distance) {
        List<Map> result = new ArrayList<Map>();
        try {
            String bufferWKT = GeometryOperate.buffer(wkt, distance);
            result = dbQuery.queryByWKT(data, bufferWKT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map bufferStatsByPoint(Layer layer, Point point, double distance) {
        Map result = new HashMap();
        try {
            result = dbStats.statsByOval(layer, point, distance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map bufferStatsByExtent(Layer layer, Range range) {
        Map result = new HashMap();
        try {
            result = dbStats.statsByExtent(layer, range);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map bufferStatsByPolyline(Layer layer, List<Point> points, double distance) {
        Map result = new HashMap();
        try {
            String wkt = SpatialUtil.pointListToWKT(points, LINESTRING);
            String bufferWKT = GeometryOperate.buffer(wkt, distance);
            result = dbStats.statsByWKT(layer, bufferWKT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map bufferStatsByPolygon(Layer layer, List<Point> points, double distance) {
        Map result = new HashMap();
        try {
            String wkt = SpatialUtil.pointListToWKT(points, POLYGON);
            String bufferWKT = GeometryOperate.buffer(wkt, distance);
            result = dbStats.statsByWKT(layer, bufferWKT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map bufferStatsByWKT(Layer layer, String wkt, double distance) {
        Map result = new HashMap();
        try {
            String bufferWKT = GeometryOperate.buffer(wkt, distance);
            result = dbStats.statsByWKT(layer, bufferWKT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map bufferStatsByWKT(List<Map> data, String statsColumn, String groupColumn, String type, String wkt, double distance) {
        Map result = new HashMap();
        try {
            String bufferWKT = GeometryOperate.buffer(wkt, distance);
            result = dbStats.statsByWKT(data, statsColumn, groupColumn, type, bufferWKT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
