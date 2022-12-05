package com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.base;

public class CellPosition {
	public int Row;
    public int Column;
    public int Key;

    public CellPosition(int row, int column, int columnCount)
    {
        Row = row;
        Column = column;
        Key = row * columnCount + column;
    }
}
