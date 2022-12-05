package com.augurit.agcloud.agcom.agsupport.sc.spatial.topology.service.impl;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.topology.service.IAgOperate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import org.springframework.stereotype.Service;
import spatial.GeometryOperate;

import java.util.List;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017.
 */
@Service
public class AgOperateImpl implements IAgOperate {
    @Override
    public String buffer(String wkt, double dBuffer) throws ParseException {
        return GeometryOperate.buffer(wkt, dBuffer);
    }

    @Override
    public Geometry buffer(Geometry geometry, double dBuffer) {
        return GeometryOperate.buffer(geometry, dBuffer);
    }

    @Override
    public String intersect(String wkt1, String wkt2) throws ParseException {
        return GeometryOperate.intersect(wkt1, wkt2);
    }

    @Override
    public Geometry intersect(Geometry geometry1, Geometry geometry2) {
        return GeometryOperate.intersect(geometry1, geometry2);
    }

    @Override
    public String convexHull(String wkt) throws ParseException {
        return GeometryOperate.convexHull(wkt);
    }

    @Override
    public Geometry convexHull(Geometry geometry) {
        return GeometryOperate.convexHull(geometry);
    }

    @Override
    public String union(String wkt1, String wkt2) throws ParseException {
        return GeometryOperate.union(wkt1, wkt2);
    }

    @Override
    public Geometry union(Geometry geometry1, Geometry geometry2) {
        return GeometryOperate.union(geometry1, geometry2);
    }

    @Override
    public String difference(String wkt1, String wkt2) throws ParseException {
        return GeometryOperate.difference(wkt1, wkt2);
    }

    @Override
    public Geometry difference(Geometry geometry1, Geometry geometry2) {
        return GeometryOperate.difference(geometry1, geometry2);
    }

    @Override
    public String symDifference(String wkt1, String wkt2) throws ParseException {
        return GeometryOperate.symDifference(wkt1, wkt2);
    }

    @Override
    public Geometry symDifference(Geometry geometry1, Geometry geometry2) {
        return GeometryOperate.symDifference(geometry1, geometry2);
    }

    @Override
    public String closingLineString(String wkt) throws ParseException {
        return GeometryOperate.closingLineString(wkt);
    }

    @Override
    public Geometry closingLineString(LineString lineString) {
        return GeometryOperate.closingLineString(lineString);
    }

    @Override
    public String distance(String wkt1, String wkt2) throws ParseException {
        return GeometryOperate.distance(wkt1, wkt2);
    }

    @Override
    public String distance(Geometry geometry1, Geometry geometry2) {
        return GeometryOperate.distance(geometry1, geometry2);
    }

    @Override
    public String clone(String wkt) throws ParseException {
        return GeometryOperate.clone(wkt);
    }

    @Override
    public Geometry clone(Geometry geometry) {
        return GeometryOperate.clone(geometry);
    }

    @Override
    public String smooth(String wkt, double fit) throws ParseException {
        return GeometryOperate.smooth(wkt, fit);
    }

    @Override
    public Geometry smooth(Geometry geometry, double fit) throws ParseException {
        return GeometryOperate.smooth(geometry, fit);
    }

    @Override
    public String sparse(String wkt, double fit) throws ParseException {
        return GeometryOperate.sparse(wkt, fit);
    }

    @Override
    public Geometry sparse(Geometry geometry, double fit) throws ParseException {
        return GeometryOperate.sparse(geometry, fit);
    }

    @Override
    public String pasePointsToWKT(List<Point> points, String type) {
        return GeometryOperate.pasePointsToWKT(points, type);
    }
}
