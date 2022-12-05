package com.augurit.agcloud.agcom.agsupport.sc.spatial.topology.service.impl;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.topology.service.IAgRelated;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import org.springframework.stereotype.Service;
import spatial.GeometryRelated;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017.
 */
@Service
public class AgRelatedImpl implements IAgRelated {
    @Override
    public boolean stEquals(String wkt1, String wkt2) throws ParseException {
        return GeometryRelated.stEquals(wkt1, wkt2);
    }

    @Override
    public boolean stEquals(Geometry geometry1, Geometry geometry2) {
        return GeometryRelated.stEquals(geometry1, geometry2);
    }

    @Override
    public boolean stDisjoint(String wkt1, String wkt2) throws ParseException {
        return GeometryRelated.stDisjoint(wkt1, wkt2);
    }

    @Override
    public boolean stDisjoint(Geometry geometry1, Geometry geometry2) {
        return GeometryRelated.stDisjoint(geometry1, geometry2);
    }

    @Override
    public boolean stIntersects(String wkt1, String wkt2) throws ParseException {
        return GeometryRelated.stIntersects(wkt1, wkt2);
    }

    @Override
    public boolean stIntersects(Geometry geometry1, Geometry geometry2) {
        return GeometryRelated.stIntersects(geometry1, geometry2);
    }

    @Override
    public boolean stContains(String wkt1, String wkt2) throws ParseException {
        return GeometryRelated.stContains(wkt1, wkt2);
    }

    @Override
    public boolean stContains(Geometry geometry1, Geometry geometry2) {
        return GeometryRelated.stContains(geometry1, geometry2);
    }

    @Override
    public boolean stCrosses(String wkt1, String wkt2) throws ParseException {
        return GeometryRelated.stCrosses(wkt1, wkt2);
    }

    @Override
    public boolean stCrosses(Geometry geometry1, Geometry geometry2) {
        return GeometryRelated.stCrosses(geometry1, geometry2);
    }

    @Override
    public boolean stWithin(String wkt1, String wkt2) throws ParseException {
        return GeometryRelated.stWithin(wkt1, wkt2);
    }

    @Override
    public boolean stWithin(Geometry geometry1, Geometry geometry2) {
        return GeometryRelated.stWithin(geometry1, geometry2);
    }

    @Override
    public boolean stTouches(String wkt1, String wkt2) throws ParseException {
        return GeometryRelated.stTouches(wkt1, wkt2);
    }

    @Override
    public boolean stTouches(Geometry geometry1, Geometry geometry2) {
        return GeometryRelated.stTouches(geometry1, geometry2);
    }

    @Override
    public boolean stCoveredBy(String wkt1, String wkt2) throws ParseException {
        return GeometryRelated.stCoveredBy(wkt1, wkt2);
    }

    @Override
    public boolean stCoveredBy(Geometry geometry1, Geometry geometry2) {
        return GeometryRelated.stCoveredBy(geometry1, geometry2);
    }

    @Override
    public boolean stCovers(String wkt1, String wkt2) throws ParseException {
        return GeometryRelated.stCovers(wkt1, wkt2);
    }

    @Override
    public boolean stCovers(Geometry geometry1, Geometry geometry2) {
        return GeometryRelated.stCovers(geometry1, geometry2);
    }

    @Override
    public boolean stEqualsExact(String wkt1, String wkt2) throws ParseException {
        return GeometryRelated.stEqualsExact(wkt1, wkt2);
    }

    @Override
    public boolean stEqualsExact(Geometry geometry1, Geometry geometry2) {
        return GeometryRelated.stEqualsExact(geometry1, geometry2);
    }

    @Override
    public boolean stEqualsExact(String wkt1, String wkt2, double tolerance) throws ParseException {
        return GeometryRelated.stEqualsExact(wkt1, wkt2, tolerance);
    }

    @Override
    public boolean stEqualsExact(Geometry geometry1, Geometry geometry2, double tolerance) {
        return GeometryRelated.stEqualsExact(geometry1, geometry2, tolerance);
    }

    @Override
    public boolean stEqualsNorm(String wkt1, String wkt2) throws ParseException {
        return GeometryRelated.stEqualsNorm(wkt1, wkt2);
    }

    @Override
    public boolean stEqualsNorm(Geometry geometry1, Geometry geometry2) {
        return GeometryRelated.stEqualsNorm(geometry1, geometry2);
    }

    @Override
    public boolean stEqualsTopo(String wkt1, String wkt2) throws ParseException {
        return GeometryRelated.stEqualsTopo(wkt1, wkt2);
    }

    @Override
    public boolean stEqualsTopo(Geometry geometry1, Geometry geometry2) {
        return GeometryRelated.stEqualsTopo(geometry1, geometry2);
    }

    @Override
    public boolean stIsWithinDistance(String wkt1, String wkt2, double distance) throws ParseException {
        return GeometryRelated.stIsWithinDistance(wkt1, wkt2, distance);
    }

    @Override
    public boolean stIsWithinDistance(Geometry geometry1, Geometry geometry2, double distance) {
        return GeometryRelated.stIsWithinDistance(geometry1, geometry2, distance);
    }

    @Override
    public boolean stOverlaps(String wkt1, String wkt2) throws ParseException {
        return GeometryRelated.stOverlaps(wkt1, wkt2);
    }

    @Override
    public boolean stOverlaps(Geometry geometry1, Geometry geometry2) {
        return GeometryRelated.stOverlaps(geometry1, geometry2);
    }

    @Override
    public boolean stRelate(String wkt1, String wkt2, String intersectionPattern) throws ParseException {
        return GeometryRelated.stRelate(wkt1, wkt2, intersectionPattern);
    }

    @Override
    public boolean stRelate(Geometry geometry1, Geometry geometry2, String intersectionPattern) {
        return GeometryRelated.stRelate(geometry1, geometry2, intersectionPattern);
    }

    @Override
    public int stCompareTo(String wkt1, String wkt2) throws ParseException {
        return GeometryRelated.stCompareTo(wkt1, wkt2);
    }

    @Override
    public int stCompareTo(Geometry geometry1, Geometry geometry2) {
        return GeometryRelated.stCompareTo(geometry1, geometry2);
    }
}
