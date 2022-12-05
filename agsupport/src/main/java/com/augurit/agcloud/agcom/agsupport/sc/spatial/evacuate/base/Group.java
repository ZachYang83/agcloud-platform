package com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.base;

import java.util.ArrayList;
import java.util.List;

public class Group {
    public List<DataPoint> Items = new ArrayList<DataPoint>();
    public DataPoint center = new DataPoint();

    public Group(DataPoint p) {
        center.index = p.index;
        center.data = p.data;
        center.x = p.x;
        center.y = p.y;
        Items.add(p);
    }

    public void Add(DataPoint p) {
        Items.add(p);
    }

    public double getDistance(DataPoint p) {
        double d = (p.x - center.x) * (p.x - center.x) + (p.y - center.y) * (p.y - center.y);
        return d;
    }

    public void computeCenter() {
        center.x = 0;
        center.y = 0;

        for (int i = 0; i < Items.size(); i++) {
            center.x += Items.get(i).x;
            center.y += Items.get(i).y;
        }
        center.x /= Items.size();
        center.y /= Items.size();
    }

    public DataPoint getNearestPoint() {
        double minD = Double.MAX_VALUE;
        DataPoint selP = null;
        for (DataPoint p : Items) {
            double d = getDistance(p);
            if (minD > d) {
                selP = p;
            }
        }
        return selP;
    }

    public Group clone() {
        Group item = new Group(center);
        for (DataPoint p : Items) {
            item.Add(p);
        }
        return item;
    }


}
