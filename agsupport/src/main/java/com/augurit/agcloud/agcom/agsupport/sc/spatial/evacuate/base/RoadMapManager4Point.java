package com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.base;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoadMapManager4Point {
    private List<DataPoint> m_roadLines;
    private Map<Integer, Cell4Point> m_roadCells;
    private GeoBoundingBox m_geoBox;
    private double m_cellSize;
    private int m_columnCount;
    private int m_rowCount;
    private boolean m_isDisposed = false;

    //region 对外属性
    public double getBoxX() {
        return m_geoBox.West;
    }

    public double getBoxY() {
        return m_geoBox.South;
    }

    public List<DataPoint> getRoadLines() {
        return m_roadLines;
    }

    public Map<Integer, Cell4Point> getRoadCells() {
        return m_roadCells;
    }

    public double getCellSize() {
        return m_cellSize;
    }

    public int getColumnCount() {
        return m_columnCount;
    }

    public int getRowCount() {
        return m_rowCount;
    }

    public boolean isDisposed() {
        return m_isDisposed;
    }
    //endregion

    //region 初始化
    public void Load(GeoBoundingBox geoBox, double cellSize) {
        m_geoBox = geoBox;
        m_cellSize = cellSize;

        m_columnCount = (int) ((geoBox.East - geoBox.West) / m_cellSize) + 1;
        m_rowCount = (int) ((geoBox.North - geoBox.South) / m_cellSize) + 1;
    }

    public void BuildRoadCell(List<DataPoint> roadLines) {
        m_roadLines = roadLines;
        m_roadCells = new HashMap<Integer, Cell4Point>();
        for (int i = 0; i < roadLines.size(); i++) {
            FindCell(roadLines.get(i), i);
        }
    }

    private void FindCell(Point point, int srIndex) {
        CellPosition k1 = GetCellPosition(point);
        AddLineToCell(point, k1);
    }

    private void AddLineToCell(Point line, CellPosition pos) {
        if (m_roadCells.containsKey(pos.Key) == false) {
            Cell4Point cell = new Cell4Point();
            cell.CellPos = pos;
            cell.Items.add(line);
            m_roadCells.put(pos.Key, cell);
        } else {
            m_roadCells.get(pos.Key).Items.add(line);
        }
    }
    //endregion

    //region 根据点获得Cell
    public CellPosition GetCellPosition(Point shapefile_Point) {
        return GetCellPosition(shapefile_Point.x, shapefile_Point.y);
    }

    public CellPosition GetCellPosition(double px, double py) {
        double dx = px - getBoxX();
        double dy = py - getBoxY();

        int x = (int) (dx / m_cellSize);
        int y = (int) (dy / m_cellSize);

        CellPosition cp = new CellPosition(y, x, m_columnCount);
        return cp;
    }

    /**
     * 根据位置找到存在数据的Cell
     *
     * @param pos
     * @return
     */
    public Cell4Point GetCellByPos(Point pos) {
        CellPosition cellPos = GetCellPosition(pos);
        if (cellPos.Column < 0 || cellPos.Row < 0 || cellPos.Column > m_columnCount || cellPos.Row > m_rowCount)
            return null;//超过范围返回空
        if (m_roadCells.containsKey(cellPos.Key) == false) {
            Cell4Point cell = new Cell4Point();
            cell.CellPos = cellPos;
            return cell;//在范围内，但是没有数据
        }
        return m_roadCells.get(cellPos.Key);
    }
    //endregion

    //region 其他
    public GeoBoundingBox getCellBoundingBox(Cell4Point cell) {
        GeoBoundingBox box = new GeoBoundingBox(true);
        box.West = m_geoBox.West + cell.CellPos.Column * m_cellSize;
        box.East = box.West + m_cellSize;
        box.South = m_geoBox.South + cell.CellPos.Row * m_cellSize;
        box.North = box.South + m_cellSize;

        return box;
    }

    public GeoBoundingBox computeGeoBox(List<Point> points) {
        GeoBoundingBox item = new GeoBoundingBox(true);
        for (Point point : points) {
            item.appendPoint(point.x, point.y);
        }
        return item;
    }
    //endregion

    //region 卸载

    /**
     * 卸载cell数据
     */
    public void dispose() {
        if (m_roadCells != null) {
            m_roadCells.clear();
            m_roadCells = null;
            m_isDisposed = true;
        }
    }
    //endregion
}
