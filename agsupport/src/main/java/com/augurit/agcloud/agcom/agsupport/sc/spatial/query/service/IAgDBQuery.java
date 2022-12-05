package com.augurit.agcloud.agcom.agsupport.sc.spatial.query.service;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Range;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017.
 */
public interface IAgDBQuery {

    /**
     * 查询所有数据
     *
     * @param layer 图层表
     * @return
     */
    List<Map> queryAll(Layer layer);

    PageInfo<Map> queryAllPage(Layer layer, Page page);

    /**
     * 分批次查询
     *
     * @param layer
     * @return
     */
    List<Map> queryLimit(Layer layer, int skip, int limit);

    /**
     * 根据主键查询数据
     *
     * @param layer      图层表
     * @param primaryKey 主键值
     * @return
     */
    Map queryByPk(Layer layer, String primaryKey);

    /**
     * 根据主键查询数据
     *
     * @param layer  图层表
     * @param pkList 主键list
     * @return
     */
    List<Map> queryByPks(Layer layer, List<String> pkList);

    /**
     * 对圆进行查询
     *
     * @param layer 图层表
     * @param point 圆心
     * @param r     半径
     * @return
     */
    List<Map> queryByOval(Layer layer, Point point, double r);

    PageInfo<Map> queryByOvalPage(Layer layer, Point point, double r, Page page);

    /**
     * 对矩形范围进行查询
     *
     * @param layer 图层表
     * @param range 矩形范围
     * @return
     */
    List<Map> queryByExtent(Layer layer, Range range);

    PageInfo<Map> queryByExtentPage(Layer layer, Range range, Page page);

    /**
     * 对线进行查询
     *
     * @param layer
     * @param points 坐标列
     * @return
     */
    List<Map> queryByPolyline(Layer layer, List<Point> points);

    PageInfo<Map> queryByPolylinePage(Layer layer, List<Point> points, Page page);

    /**
     * 对面范围进行查询
     *
     * @param layer
     * @param points 坐标列
     * @return
     */
    List<Map> queryByPolygon(Layer layer, List<Point> points);

    PageInfo<Map> queryByPolygonPage(Layer layer, List<Point> points, Page page);

    /**
     * 对输入的WKT空间范围字符串进行查询
     *
     * @param layer
     * @param wkt
     * @return
     */
    List<Map> queryByWKT(Layer layer, String wkt);

    PageInfo<Map> queryByWKTPage(Layer layer, String wkt, Page page);

    /**
     * 对输入的WKT空间范围字符串进行查询 data中至少要有wkt值和x y值其中一组 优先读取wkt
     *
     * @param data
     * @param wkt
     * @return
     */
    List<Map> queryByWKT(List<Map> data, String wkt);
}
