package com.augurit.agcloud.agcom.agsupport.common.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 对截取地址的正则进行改良
 * 2018-1-31
 */
public class LocatePattern {

    Map pattern = new LinkedHashMap();

    public LocatePattern() {
        Object[][] keys = new Object[][]{
                new Object[]{"村", 0.9},
                new Object[]{"路", 0.9},
                new Object[]{"街", 0.9},
                new Object[]{"巷", 0.2},
                new Object[]{"栋", 0.1},
                new Object[]{"套", 0.1},
                new Object[]{"房", 0.1},
                new Object[]{"大街", 0.9},
                new Object[]{"大道", 0.9},
                new Object[]{"花园", 0.1},
                new Object[]{"小区", 0.1}
        };
        add(keys);
    }

    public LocatePattern(Object[][] keys) {
        add(keys);
    }

    public void add(Object[][] keys) {
        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i][0];
            String score = String.valueOf(keys[i][1]);
            String regex = "^.*([^" + key + "]{2}" + key + ").*$";
            pattern.put(key, new Object[]{Pattern.compile(regex), score});
        }
    }

    public Map getPattern() {
        return pattern;
    }

    public void setPattern(Map pattern) {
        this.pattern = pattern;
    }

    public Iterator<Map.Entry> iterator() {
        Iterator it = pattern.entrySet().iterator();
        return it;
    }
}
