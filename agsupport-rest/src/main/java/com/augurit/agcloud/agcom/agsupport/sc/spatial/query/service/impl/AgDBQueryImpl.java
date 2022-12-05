package com.augurit.agcloud.agcom.agsupport.sc.spatial.query.service.impl;

import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgDataTrans;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Range;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.query.dao.AgDBQueryDao;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.query.service.IAgDBQuery;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import com.common.util.ReflectBeans;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spatial.GeometryApplication;
import spatial.GeometryOperate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.*;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017.
 */
@Service
public class AgDBQueryImpl implements IAgDBQuery {
    @Autowired
    private AgDBQueryDao agDbQueryDao;
    @Autowired
    private IAgDataTrans dataTrans;

    @Override
    public List<Map> queryAll(Layer layer) {
        List<Map> result = new ArrayList<Map>();
        try {
            Layer queryLayer = ReflectBeans.copy(layer, new Layer());
            if (dataTrans.isTransComplete(queryLayer.getDataSourceId(), queryLayer.getLayerTable())) {
                result = agDbQueryDao.queryAllByMDB(queryLayer);
            } else {
                result = agDbQueryDao.queryAllBySQL(queryLayer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public PageInfo<Map> queryAllPage(Layer layer, Page page) {
        List<Map> data = this.queryAll(layer);
        List<Map> result = data.subList(Math.max(page.getPageNum(), 0), Math.min(page.getPageNum() + page.getPageSize(), data.size()));
        PageInfo<Map> pageInfo = new PageInfo<Map>(result);
        pageInfo.setTotal(data.size());
        return pageInfo;
    }

    @Override
    public List<Map> queryLimit(Layer layer, int skip, int limit) {
        List<Map> result = new ArrayList<Map>();
        try {
            Layer queryLayer = ReflectBeans.copy(layer, new Layer());
            result = agDbQueryDao.queryLimitBySQL(queryLayer, skip, limit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map queryByPk(Layer layer, String primaryKey) {
        Map result = new HashMap();
        try {
            Layer queryLayer = ReflectBeans.copy(layer, new Layer());
            result = agDbQueryDao.queryPkBySQL(queryLayer, primaryKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map> queryByPks(Layer layer, List<String> pkList) {
        List<Map> result = new ArrayList<Map>();
        try {
            Layer queryLayer = ReflectBeans.copy(layer, new Layer());
            result = agDbQueryDao.queryPksBySQL(queryLayer, pkList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map> queryByOval(Layer layer, Point point, double distance) {
        List<Map> result = new ArrayList<Map>();
        try {
            Layer queryLayer = ReflectBeans.copy(layer, new Layer());
            if (dataTrans.isTransComplete(queryLayer.getDataSourceId(), queryLayer.getLayerTable())) {
                String wkt = GeometryOperate.buffer(SpatialUtil.pointToWKT(point), distance);
                result = this.queryByWKT(queryLayer, wkt);
            } else {
                if (queryLayer.getxColumn() != null && queryLayer.getyColumn() != null) {
                    result = agDbQueryDao.queryOvalBySQL(queryLayer, point, distance);
                } else {
                    String wkt = GeometryOperate.buffer(SpatialUtil.pointToWKT(point), distance);
                    result = this.queryByWKT(queryLayer, wkt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public PageInfo<Map> queryByOvalPage(Layer layer, Point point, double r, Page page) {
        List<Map> data = this.queryByOval(layer, point, r);
        List<Map> result = data.subList(Math.max(page.getPageNum(), 0), Math.min(page.getPageNum() + page.getPageSize(), data.size()));
        PageInfo<Map> pageInfo = new PageInfo<Map>(result);
        pageInfo.setTotal(data.size());
        return pageInfo;
    }

    @Override
    public List<Map> queryByExtent(Layer layer, Range range) {
        List<Map> result = new ArrayList<Map>();
        try {
            Layer queryLayer = ReflectBeans.copy(layer, new Layer());
            if (dataTrans.isTransComplete(queryLayer.getDataSourceId(), queryLayer.getLayerTable())) {
                String wkt = SpatialUtil.rangeToWKT(range);
                result = this.queryByWKT(queryLayer, wkt);
            } else {
                if (queryLayer.getxColumn() != null && queryLayer.getyColumn() != null) {
                    result = agDbQueryDao.queryRangeBySQL(queryLayer, range);
                } else {
                    String wkt = SpatialUtil.rangeToWKT(range);
                    result = this.queryByWKT(queryLayer, wkt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public PageInfo<Map> queryByExtentPage(Layer layer, Range range, Page page) {
        List<Map> data = this.queryByExtent(layer, range);
        List<Map> result = data.subList(Math.max(page.getPageNum(), 0), Math.min(page.getPageNum() + page.getPageSize(), data.size()));
        PageInfo<Map> pageInfo = new PageInfo<Map>(result);
        pageInfo.setTotal(data.size());
        return pageInfo;
    }

    @Override
    public List<Map> queryByPolyline(Layer layer, List<Point> points) {
        List<Map> result = new ArrayList<Map>();
        try {
            String wkt = SpatialUtil.pointListToWKT(points, LINESTRING);
            result = this.queryByWKT(layer, wkt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public PageInfo<Map> queryByPolylinePage(Layer layer, List<Point> points, Page page) {
        String wkt = SpatialUtil.pointListToWKT(points, LINESTRING);
        return this.queryByWKTPage(layer, wkt, page);
    }

    @Override
    public List<Map> queryByPolygon(Layer layer, List<Point> points) {
        List<Map> result = new ArrayList<Map>();
        try {
            String wkt = SpatialUtil.pointListToWKT(points, POLYGON);
            result = this.queryByWKT(layer, wkt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public PageInfo<Map> queryByPolygonPage(Layer layer, List<Point> points, Page page) {
        String wkt = SpatialUtil.pointListToWKT(points, POLYGON);
        return this.queryByWKTPage(layer, wkt, page);
    }

    @Override
    public List<Map> queryByWKT(Layer layer, String wkt) {
        List<Map> result = new ArrayList<Map>();
        try {
            Layer queryLayer = ReflectBeans.copy(layer, new Layer());
            if (GeometryApplication.checkWkt(wkt)) {
                if (dataTrans.isTransComplete(queryLayer.getDataSourceId(), queryLayer.getLayerTable())) {
                    Range range = SpatialUtil.getRangeByWKT(wkt);
                    List<Map> data = agDbQueryDao.queryRangeByMDB(queryLayer, range);
                    result = GeometryApplication.queryIntersects(data, wkt);
                } else {
                    if (queryLayer.getxColumn() != null && queryLayer.getyColumn() != null) {
                        Range range = SpatialUtil.getRangeByWKT(wkt);
                        List<Map> data = agDbQueryDao.queryRangeBySQL(queryLayer, range);
                        result = GeometryApplication.queryIntersects(data, wkt);
                    } else {
                        List<Map> data = agDbQueryDao.queryAllBySQL(queryLayer);
                        result = GeometryApplication.queryIntersects(data, wkt);
                    }
                }
            } else {
                result = queryAll(layer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public PageInfo<Map> queryByWKTPage(Layer layer, String wkt, Page page) {
        List<Map> data = this.queryByWKT(layer, wkt);
        List<Map> result = data.subList(Math.max(page.getPageNum(), 0), Math.min(page.getPageNum() + page.getPageSize(), data.size()));
        PageInfo<Map> pageInfo = new PageInfo<Map>(result);
        pageInfo.setTotal(data.size());
        return pageInfo;
    }

    @Override
    public List<Map> queryByWKT(List<Map> data, String wkt) {
        List<Map> result = new ArrayList<Map>();
        try {
            result = GeometryApplication.queryIntersects(data, wkt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
