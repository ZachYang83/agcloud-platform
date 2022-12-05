package com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.base;


import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;

import java.util.HashMap;
import java.util.Map;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-06-16.
 */
public class DataPoint extends Point {
    public int index = 0;
    public Map data = new HashMap();

    public DataPoint(Point p, Map data, int index) {
        this.index = index;
        this.data = data;
        this.x = p.x;
        this.y = p.y;
    }

    public DataPoint() {

    }
}
