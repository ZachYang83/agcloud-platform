package com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MinGroupSaver {
    private List<Group> m_groups = new ArrayList<>();
    private int[] m_levels;

    //region 属性
    public List<Group> getGroups() {
        return m_groups;
    }
    //endregion

    //region 构造函数
    public MinGroupSaver(int pointCount) {
        m_levels = new int[pointCount];
        for (int i = 0; i < pointCount; i++)
            m_levels[i] = -1;
    }
    //endregion

    //region 保存

    /**
     * 清除原来的重新进行保存
     *
     * @param groups
     */
    public void save(List<Group> groups) {
        m_groups.clear();
        for (Group group : groups) {
            m_groups.add(group.clone());
        }
    }
    //endregion

    //region 标记点的Level
    public void markPointLevel(int level) {
        for (Group group : m_groups) {
            DataPoint p = group.getNearestPoint();
            if (m_levels[p.index] == -1)
                m_levels[p.index] = level;
        }
    }

    public void markLeftPointLevel(int level) {
        for (int i = 0; i < m_levels.length; i++) {
            if (m_levels[i] == -1)
                m_levels[i] = level;
        }
    }
    //endregion


    public void print() {
        System.out.println("-->" + m_groups.size());
        m_groups.sort(new Comparator<Group>() {
            @Override
            public int compare(Group o1, Group o2) {
                int t = Double.compare(o1.center.x, o2.center.x);
                if (t == 0)
                    return Double.compare(o1.center.y, o2.center.y);
                return t;
            }
        });
        int markedCount = 0;
        for (int i = 0; i < m_levels.length; i++) {
            if (m_levels[i] != -1)
                markedCount++;
        }
        System.out.println("MarkedCount:" + markedCount);
    }
}
