package com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.method;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.base.Cell4Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.base.NearestPos4Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.base.RoadMapManager4Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.base.Shapefile_Point;

import java.util.ArrayList;
import java.util.List;

/// <summary>
/// 通过Cell的方式计算最短距离
/// </summary>
public class CellMinDisAlgorithm4Point {

    /**
     * 找到一个点附近XX范围内的所有点
     *
     * @param manager
     * @param point
     * @param maxDistanceSqFilter
     * @return
     */
    public static List<NearestPos4Point> GetNearestPoints(RoadMapManager4Point manager, Point point, double maxDistanceSqFilter) {
        Cell4Point containCell = manager.GetCellByPos(point);
        if (containCell == null) {
            return null;
        }
        List<NearestPos4Point> items = new ArrayList<>();
        addPointsByDistanceFilter(point, items, containCell, maxDistanceSqFilter);

        int round = 1;
        while (true) {
            List<Integer> checkKeys = new ArrayList<Integer>();
            for (int i = containCell.CellPos.Column - round; i <= containCell.CellPos.Column + round; i++) {
                int row = containCell.CellPos.Row - round;
                int column = i;
                int key = manager.getColumnCount() * row + column;
                checkKeys.add(key);

                row = containCell.CellPos.Row + round;
                key = manager.getColumnCount() * row + column;
                checkKeys.add(key);
            }
            for (int i = containCell.CellPos.Row - round + 1; i <= containCell.CellPos.Row + round - 1; i++) {
                int row = i;
                int column = containCell.CellPos.Column - round;
                int key = manager.getColumnCount() * row + column;
                checkKeys.add(key);
                column = containCell.CellPos.Column + round;
                key = manager.getColumnCount() * row + column;
                checkKeys.add(key);
            }
            boolean exist = false;
            for (int key : checkKeys) {
                if (manager.getRoadCells().containsKey(key) == false) {
                    continue;
                }
                Cell4Point remoteCell = manager.getRoadCells().get(key);
                double d = GetMinDistanceSqToBox(manager, remoteCell, new Shapefile_Point(point.x, point.y), containCell);
                if (d > maxDistanceSqFilter)
                    continue;
                addPointsByDistanceFilter(point, items, remoteCell, maxDistanceSqFilter);
                exist = true;
            }
            if (exist == false)
                return items;
            round++;
        }
    }

    private static void addPointsByDistanceFilter(Point pos, List<NearestPos4Point> items, Cell4Point containCell, double maxDistanceSqFilter) {
        for (Point point : containCell.Items) {
            double d = (point.x - pos.x) * (point.x - pos.x) + (point.y - pos.y) * (point.y - pos.y);
            if (d < maxDistanceSqFilter) {
                items.add(new NearestPos4Point(point, d));
            }
        }
    }

    /**
     * 计算一个点到一个格子的最短距离
     *
     * @param manager
     * @param remoteCell
     * @param pos
     * @param posCell
     * @return
     */
    private static double GetMinDistanceSqToBox(RoadMapManager4Point manager, Cell4Point remoteCell,
                                                Shapefile_Point pos, Cell4Point posCell) {
        double minX = remoteCell.CellPos.Column * manager.getCellSize() + manager.getBoxX();
        double maxX = remoteCell.CellPos.Column * manager.getCellSize() + manager.getBoxX() + manager.getCellSize();

        double minY = remoteCell.CellPos.Row * manager.getCellSize() + manager.getBoxY();
        double maxY = remoteCell.CellPos.Row * manager.getCellSize() + manager.getBoxY() + manager.getCellSize();

        if (posCell.CellPos.Row == remoteCell.CellPos.Row) {
            if (posCell.CellPos.Column < remoteCell.CellPos.Column) {
                return (minX - pos.x) * (minX - pos.x);
            } else {
                return (pos.x - maxX) * (pos.x - maxX);
            }
        } else if (posCell.CellPos.Column == remoteCell.CellPos.Column) {
            if (posCell.CellPos.Row < remoteCell.CellPos.Row) {
                return (minY - pos.y) * (minY - pos.y);
            } else {
                return (pos.y - maxY) * (pos.y - maxY);
            }
        }

        double d1 = Intersection.GetDistanceSq(new Shapefile_Point(minX, minY), pos);
        double d2 = Intersection.GetDistanceSq(new Shapefile_Point(maxX, minY), pos);
        double d3 = Intersection.GetDistanceSq(new Shapefile_Point(minX, maxY), pos);
        double d4 = Intersection.GetDistanceSq(new Shapefile_Point(maxX, maxY), pos);

        double minD = d1;
        minD = minD < d2 ? minD : d2;
        minD = minD < d3 ? minD : d3;
        minD = minD < d4 ? minD : d4;

        return minD;
    }
}
