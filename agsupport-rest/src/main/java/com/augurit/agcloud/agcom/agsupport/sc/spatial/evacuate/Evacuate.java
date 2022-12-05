package com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.base.*;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.method.CellMinDisAlgorithm4Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.method.Intersection;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Evacuate {
    private List<Group> m_groups;
    private List<DataPoint> m_points;
    private double m_buffer;
    private RoadMapManager4Point m_roadMapManager;
    private int m_minGroupCount;
    private MinGroupSaver m_saver;

    public List<Map> beginEvacuate(List<Map> datas, int level) {
        List<Map> resultMap = new ArrayList<Map>();
        List<Point> points = new ArrayList<Point>();
        m_points = new ArrayList<DataPoint>();
        for (int i = 0; i < datas.size(); i++) {
            Map data = datas.get(i);
            Point p = SpatialUtil.WKTToPoint(data.get(SpatialConfig.WKT_COLUMN).toString());
            m_points.add(new DataPoint(p, data, i));
            points.add(p);
        }
        m_saver = new MinGroupSaver(m_points.size());
        m_roadMapManager = new RoadMapManager4Point();
        GeoBoundingBox box = m_roadMapManager.computeGeoBox(points);
        m_roadMapManager.Load(box, 50 / Intersection.MeterPerDegree());
        m_roadMapManager.BuildRoadCell(m_points);

        long t1 = System.currentTimeMillis();
        boolean fastMethod = true;

        double bufferSize = 80000 / (Math.pow(2, level - 5));
        System.out.println("----------------------------------------");
        System.out.println("Level:" + level + " BufferSize:" + bufferSize);
        m_buffer = bufferSize / Intersection.MeterPerDegree();
        m_buffer = m_buffer * m_buffer;

        m_minGroupCount = Integer.MAX_VALUE;
        m_groups = new ArrayList<>();
        Group g1 = new Group(m_points.get(0));
        m_groups.add(g1);

        int count = 100;
        int haveNotUpdateCount = 0;
        for (int k = 0; k < count; k++) {
            System.out.print(".");
            if (k % 10 == 0 && k != 0)
                System.out.println();
            if (k == 0 || fastMethod == false)
                firstRound();
            else
                nextRound();
            if (m_minGroupCount > m_groups.size()) {
                m_minGroupCount = m_groups.size();
                m_saver.save(m_groups);
                haveNotUpdateCount = 0;
            } else {
                haveNotUpdateCount++;
                if (haveNotUpdateCount > 3) {
                    System.out.println("Out by not update " + k);
                    break;
                }
            }
        }
        System.out.println("  MinGroupCount:" + m_minGroupCount);
        m_saver.markPointLevel(level);
        m_saver.markLeftPointLevel(18);
        long t2 = System.currentTimeMillis();
        System.out.println();
        System.out.println("FAST:" + fastMethod + "  time:" + (t2 - t1) + "  MinGroupCount:" + m_minGroupCount + "  " + m_points.size());
        m_saver.print();
        for (Group g : m_groups) {
            resultMap.add(g.center.data);
        }
        return resultMap;
    }

    private void nextRound() {
        clearGroup();

        int[] pointBelongGroup = new int[m_points.size()];
        double[] pointBelongDistance = new double[m_points.size()];
        for (int i = 0; i < pointBelongGroup.length; i++) {
            pointBelongGroup[i] = -1;
            pointBelongDistance[i] = Double.MAX_VALUE;
        }
        for (int j = 0; j < m_groups.size(); j++) {
            Group selGroup = m_groups.get(j);
            List<NearestPos4Point> items = CellMinDisAlgorithm4Point.GetNearestPoints(m_roadMapManager, selGroup.center, m_buffer);
            if (items == null) {
                continue;
            }
            for (NearestPos4Point point : items) {
                int pointIndex = ((DataPoint) point.point).index;
                if (pointBelongDistance[pointIndex] > point.DistanceSq) {
                    pointBelongGroup[pointIndex] = j;
                    pointBelongDistance[pointIndex] = point.DistanceSq;
                }
            }
        }
        for (int i = 0; i < m_points.size(); i++) {
            int pointToGroupIndex = pointBelongGroup[i];
            if (pointToGroupIndex > -1) {
                m_groups.get(pointToGroupIndex).Add(m_points.get(i));
            } else {
                Group g2 = new Group(m_points.get(i));
                m_groups.add(g2);
            }
        }
        tryCombineGroup();
        clearEmptyGroup();
        computeCenter();
    }

    private void firstRound() {
        clearGroup();
        for (int i = 0; i < m_points.size(); i++) {
            int minJ = -1;
            double minD = Double.MAX_VALUE;
            for (int j = 0; j < m_groups.size(); j++) {
                Group selGroup = m_groups.get(j);
                double d = selGroup.getDistance(m_points.get(i));
                if (d < minD && d < m_buffer) {
                    minJ = j;
                    minD = d;
                }
            }
            if (minJ >= 0) {
                m_groups.get(minJ).Add(m_points.get(i));
            } else {
                Group g2 = new Group(m_points.get(i));
                m_groups.add(g2);
            }
        }
        tryCombineGroup();
        clearEmptyGroup();
        computeCenter();
    }

    private void clearGroup() {
        for (int j = m_groups.size() - 1; j > -1; j--) {
            Group selGroup = m_groups.get(j);
            selGroup.Items.clear();
        }
    }

    private void clearEmptyGroup() {
        for (int j = m_groups.size() - 1; j > -1; j--) {
            Group selGroup = m_groups.get(j);
            if (selGroup.Items.size() == 0) {
                m_groups.remove(j);
            }
        }
    }

    private void computeCenter() {
        for (int j = m_groups.size() - 1; j > -1; j--) {
            Group selGroup = m_groups.get(j);
            selGroup.computeCenter();
        }
    }

    private void tryCombineGroup() {
        for (int i = 0; i < m_groups.size(); i++) {
            Group group1 = m_groups.get(i);
            if (group1.Items.size() == 0)
                continue;
            for (int j = i + 1; j < m_groups.size(); j++) {
                Group group2 = m_groups.get(j);
                if (group2.Items.size() == 0)
                    continue;
                double dis = (group1.center.x - group2.center.x) * (group1.center.x - group2.center.x)
                        + (group1.center.y - group2.center.y) * (group1.center.y - group2.center.y);
                if (dis > m_buffer)
                    continue;
                if (canCombineAnotherGroup(group1, group2)) {
                    group1.Items.addAll(group2.Items);
                    group2.Items.clear();
                    group1.computeCenter();
                } else if (canCombineAnotherGroup(group2, group1)) {
                    group2.Items.addAll(group1.Items);
                    group1.Items.clear();
                    group2.computeCenter();
                    break;
                } else if (canCombineBothGroup(group1, group2)) {
                    group1.Items.addAll(group2.Items);
                    group2.Items.clear();
                    group1.computeCenter();
                }
            }
        }
    }

    private boolean canCombineAnotherGroup(Group group1, Group group2) {
        for (int i = 0; i < group2.Items.size(); i++) {
            double x = (group1.center.x - group2.Items.get(i).x);
            double y = (group1.center.y - group2.Items.get(i).y);
            double d = x * x + y * y;
            if (d > m_buffer)
                return false;
        }
        return true;
    }

    private boolean canCombineBothGroup(Group group1, Group group2) {
        double nx = 0;
        double ny = 0;
        Group[] groups = new Group[]{group1, group2};
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < groups[j].Items.size(); i++) {
                nx += groups[j].Items.get(i).x;
                ny += groups[j].Items.get(i).y;
            }
        }
        nx /= (group1.Items.size() + group2.Items.size());
        ny /= (group1.Items.size() + group2.Items.size());

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < groups[j].Items.size(); i++) {
                double x = (nx - groups[j].Items.get(i).x);
                double y = (ny - groups[j].Items.get(i).y);
                double d = x * x + y * y;
                if (d > m_buffer)
                    return false;
            }
        }
        return true;
    }

}
