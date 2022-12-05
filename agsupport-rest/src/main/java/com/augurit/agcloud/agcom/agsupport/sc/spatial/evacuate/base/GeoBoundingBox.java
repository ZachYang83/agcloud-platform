package com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.base;

public class GeoBoundingBox {
	public double West;
	public double East;
	public double South;
	public double North;

	public GeoBoundingBox() {
	}

	public GeoBoundingBox(boolean initWithEmpty) {
		if (initWithEmpty) {
			West = Double.MAX_VALUE;
			South = Double.MAX_VALUE;

			East = Double.MIN_VALUE;
			North = Double.MIN_VALUE;
		}
	}

	public static GeoBoundingBox combine(GeoBoundingBox box1, GeoBoundingBox box2) {
		if (box1.West == Double.MAX_VALUE && box1.South == Double.MAX_VALUE && box1.East == Double.MIN_VALUE
				&& box1.North == Double.MIN_VALUE)
			return box2;
		if (box2.West == Double.MAX_VALUE && box2.South == Double.MAX_VALUE && box2.East == Double.MIN_VALUE
				&& box2.North == Double.MIN_VALUE)
			return box1;
		GeoBoundingBox newBox = new GeoBoundingBox();
		newBox.East = box1.East > box2.East ? box1.East : box2.East;
		newBox.West = box1.West < box2.West ? box1.West : box2.West;
		newBox.North = box1.North > box2.North ? box1.North : box2.North;
		newBox.South = box1.South < box2.South ? box1.South : box2.South;

		return newBox;
	}

	public void appendPoint(double x, double y) {
		East = East > x ? East : x;
		West = West < x ? West : x;
		North = North > y ? North : y;
		South = South < y ? South : y;
	}
	//region 计算
	public boolean contains(GeoBoundingBox other)
	{
		if(West>other.West)
			return false;
		if(East<other.East)
			return false;
		if(North<other.North)
			return false;
        return !(South > other.South);
    }
	public void add(GeoBoundingBox other) {
		if(West>other.West)
			West= other.West;
		if(East<other.East)
			East =other.East;
		if(North<other.North)
			North= other.North;
		if(South>other.South)
			South= other.South;
	}
	//endregion

	//region 格式
	@Override
	public String toString() {
		return South + "|" + North + " " + West + "-" + East;
	}
	public String toFormatString()
	{
		return String.format("%.6f,%.6f,%.6f,%.6f", North,South,West,East);
	}
	public static GeoBoundingBox parseFromFromatString(String s)
	{
		String[] ss=s.split(",");
		GeoBoundingBox box=new GeoBoundingBox();
		box.North=Double.parseDouble(ss[0]);
		box.South=Double.parseDouble(ss[1]);
		box.West=Double.parseDouble(ss[2]);
		box.East=Double.parseDouble(ss[3]);
		
		return box;
	}

	
	
	//endregion
}
