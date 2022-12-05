package com.augurit.agcloud.agcom.agsupport.sc.topology.service.impl;

import com.augurit.agcloud.agcom.agsupport.sc.topology.service.IAgSpatialUtilsService;
import com.esri.sde.sdk.client.SeCoordinateReference;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeShape;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**   
* @Author：李德文 
* @CreateDate： 2014-06-14
* @Description: 拓扑实现类
* @Copyright: 广州奥格智能科技有限公司
*/ 
@Repository
@Transactional
public class AgSpatialUtilsServiceImpl implements IAgSpatialUtilsService {
	
	/**
	 * 对面、线点进行抽稀
	 * @param wkt  WKT格式的空间数据
	 * @param d	 抽稀限制阀值(不同坐标系不同，可以通过分辨率的方式算出)
	 * @return 抽稀后的WKT格式
	 */
	public String geometryGeneralize(String wkt,double d){
		return null;
//		double d1 = 0;
//		List<AgPoint> points = AgSpatialUtilsDao.paseWKTtoPoint(wkt);
//		if(points.size() < 10){
//			return null;
//		}
//		List<AgPoint> newPoints = new ArrayList<AgPoint>();
//		for(int i = 0,il = points.size();i < il; ++i){
//			AgPoint p1 = null;
//			AgPoint p2 = points.get(i);
//			AgPoint p3 = null;
//			if(i != 0 && i != points.size() - 1){
//				p1 = newPoints.get(newPoints.size() - 1);
//				p3 = points.get(i + 1);
//				
//				double dis1 = Math.hypot(Math.abs(p2.getX() - p1.getX()), Math.abs(p2.getY() - p1.getY()));
//				double dis2 = Math.hypot(Math.abs(p2.getX() - p3.getX()), Math.abs(p2.getY() - p3.getY()));
//				double dis3 = Math.hypot(Math.abs(p1.getX() - p3.getX()), Math.abs(p1.getY() - p3.getY()));
//				if(dis1 + dis2 <= dis3 + d){
//					continue;
//				}
//			}
//			newPoints.add(p2);
//		}
//		System.out.println(points.size() + ":" + newPoints.size());
//		return AgSpatialUtilsDao.pasePointsToWKT(newPoints,wkt.substring(0, wkt.indexOf("(")).trim());
	}
	/**
	 * 当第二个几何完全被第一个几何包含的时候返回true
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
	public Boolean stContains(String wkt1,String wkt2){
		try {
			Geometry g1 = new WKTReader().read(wkt1);
			Geometry g2 = new WKTReader().read(wkt2);
			return g1.contains(g2);
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 当两个几何相交的时候返回true
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.augurit.agcom.agos.topology.service.IAgSpatialUtilsService#stIntersects(java.lang.String, java.lang.String)
	 */
	public Boolean stIntersects(String wkt1,String wkt2){
		try {
			Geometry g1 = new WKTReader().read(wkt1);
			Geometry g2 = new WKTReader().read(wkt2);
			return g1.intersects(g2);
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 如果两个几何相交的部分都不在两个几何的内部，则返回true
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
	public Boolean stTouches(String wkt1,String wkt2){
		try {
			Geometry g1 = new WKTReader().read(wkt1);
			Geometry g2 = new WKTReader().read(wkt2);
			return g1.touches(g2);
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 如果两个几何的交集生成空集，则返回true
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
	public Boolean stDisjoint(String wkt1,String wkt2){
		try {
			Geometry g1 = new WKTReader().read(wkt1);
			Geometry g2 = new WKTReader().read(wkt2);
			return g1.disjoint(g2);
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 如果这两个几何完全相同，则返回true
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
	public Boolean stEquals(String wkt1,String wkt2){
		try {
			Geometry g1 = new WKTReader().read(wkt1);
			Geometry g2 = new WKTReader().read(wkt2);
			return g1.equals(g2);
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 *  求几何的凸包
	 * 
	 * @param wkt1
	 * @return
	 */
	public String stConvexHull(String wkt1){
		try {
			Geometry g1 = new WKTReader().read(wkt1);
			return g1.convexHull().toText();
		} catch (Exception e) {
			return "error:" + e.getMessage();
		}
	}
	/**
	 *  求几何的边界
	 * 
	 * @param wkt1
	 * @return
	 */
	public String stBoundary(String wkt1){
		try {
			Geometry g1 = new WKTReader().read(wkt1);
			return g1.getBoundary().toText();
		} catch (Exception e) {
			return "error:" + e.getMessage();
		}
	}
	/**
	 *  求几何的形心
	 * 
	 * @param wkt1
	 * @return
	 */
	public String stCentroid(String wkt1){
		try {
			Geometry g1 = new WKTReader().read(wkt1);
			return g1.getCentroid().toText();
		} catch (Exception e) {
			return "error:" + e.getMessage();
		}
	}
	/**
	 * 根据几何对象和距离，求围绕源对象的缓冲区的几何对象
	 * 
	 * @param wkt1
	 * @param distance
	 * @return
	 */
	public String stBuffer(String wkt1,String distance){
		try {
			Geometry g1 = new WKTReader().read(wkt1);
			return g1.buffer(Long.valueOf(distance)).toText();
		} catch (Exception e) {
			return "error:" + e.getMessage();
		}
	}
	/**
	 * 求2个几何相交的部分
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
	public String stIntersection(String wkt1,String wkt2){
		try {
			Geometry g1 = new WKTReader().read(wkt1);
			Geometry g2 = new WKTReader().read(wkt2);
			return g1.intersection(g2).toText();
		} catch (Exception e) {
			return "error:" + e.getMessage();
		}
	}
	/**
	 * 求2个几何不相交的部分
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
	public String stSymmetricDiff(String wkt1,String wkt2){
		try {
			Geometry g1 = new WKTReader().read(wkt1);
			Geometry g2 = new WKTReader().read(wkt2);
			return g1.symDifference(g2).toText();
		} catch (Exception e) {
			return "error:" + e.getMessage();
		}
	}
	/**
	 * 求2个几何的并集
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
	public String stUnion(String wkt1,String wkt2){
		try {
			Geometry g1 = new WKTReader().read(wkt1);
			Geometry g2 = new WKTReader().read(wkt2);
			return g1.union(g2).toText();
		} catch (Exception e) {
			return "error:" + e.getMessage();
		}
	}
	/**
	 * 求2个几何的最短距离
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
	public String stDistance(String wkt1,String wkt2){
		try {
			Geometry g1 = new WKTReader().read(wkt1);
			Geometry g2 = new WKTReader().read(wkt2);
			return g1.distance(g2) + "";
		} catch (Exception e) {
			return "error:" + e.getMessage();
		}
	}
	/**
	 * 求几何体的周长
	 * 
	 * @param wkt
	 * @return
	 */
	public String stLength(String wkt){
		try {
			Geometry g = new WKTReader().read(wkt);
			return g.getLength() + "";
		} catch (Exception e) {
			return "error:" + e.getMessage();
		}
	}
	/**
	 * 求几何体的面积
	 * 
	 * @param wkt
	 * @return
	 */
	public String stArea(String wkt){
		try {
			Geometry g = new WKTReader().read(wkt);
			return g.getArea() + "";
		} catch (Exception e) {
			return "error:" + e.getMessage();
		}
	}
	
	public static SeShape getSeShape(SeLayer layer, String wkt){
		if(wkt == null || wkt.equals("")){
			return null;
		}
		try {
			String minx = layer.getExtent().getMinX() + "";
			String maxx = layer.getExtent().getMaxX() + "";
			String miny = layer.getExtent().getMinY() + "";
			String maxy = layer.getExtent().getMaxY() + "";
			String pwkt = "POLYGON(("+ minx + " " + maxy + "," + maxx + " " + maxy + "," + maxx + " " + miny + "," + minx + " " + miny + "," + minx + " " + maxy +"))";
			Geometry g1 = new WKTReader().read(wkt);
			Geometry g2 = new WKTReader().read(pwkt);
			wkt = g1.intersection(g2).toText();
			if(wkt.indexOf("EMPTY") != -1){
				return null;
			}
			SeCoordinateReference cr = layer.getCoordRef();
			SeShape shape = new SeShape(cr);
			shape.generateFromText(wkt);
			return shape;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断多边形是否有效，如果无效纠正多边形（解决自相交错误）
	 * @param destination
	 * @return
	 */
	public Geometry validate(String destination){
		Polygonizer getPolygonizerInvalidInn = new Polygonizer();  //存放内圈
		Collection<Polygon> collectionOut = new ArrayList<>();  //存放外圈
		try {
			Geometry geometry = new WKTReader().read(destination);
			if(geometry.isValid()){
				geometry.normalize();
				return geometry;
			}else {
				for(int i = geometry.getNumGeometries();i-- >0;){
					Polygon geometryN = (Polygon) geometry.getGeometryN(i);
					this.tPolygonOut(geometryN,collectionOut);
					this.tPolygonInne(geometryN,getPolygonizerInvalidInn);
				}

				return this.tPolygonGeometry(collectionOut,getPolygonizerInvalidInn);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String buffer0(String s) {
		try {
			Geometry geometry = new WKTReader().read(s);
			if(geometry.isValid()){
				return geometry.toText();
			}else {
				Geometry buffer = geometry.buffer(0);
				return buffer.toText();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得无效图形外圈
	 * @param polygon
	 */
	public void tPolygonOut(Polygon polygon,Collection<Polygon> collectionOut){
		LineString exteriorRing = polygon.getExteriorRing();
		this.tLineOut(exteriorRing,collectionOut);
	}

	/**
	 * 获得无效图形内圈，并重构内圈
	 * @param polygon
	 */
	public void tPolygonInne(Polygon polygon,Polygonizer polygonizerInvalidInn){
		for (int i = polygon.getNumInteriorRing();i-- > 0;){
			LineString lineString = polygon.getInteriorRingN(i);
			if(lineString instanceof LinearRing){
				lineString = lineString.getFactory().createLineString(lineString.getCoordinateSequence());
			}
			Point point = lineString.getFactory().createPoint(lineString.getCoordinateN(0));
			Geometry union = lineString.union(point);
			polygonizerInvalidInn.add(union);

		}
	}

	/**
	 * 重构多边形外圈
	 * @param lineString
	 * @param collectionOut
	 */
	public void tLineOut(LineString lineString,Collection<Polygon> collectionOut){
		Polygonizer polygonizer = new Polygonizer();
		if(lineString instanceof LinearRing){
			lineString = lineString.getFactory().createLineString(lineString.getCoordinateSequence());
		}
		Point point = lineString.getFactory().createPoint(lineString.getCoordinateN(0));
		Geometry union = lineString.union(point);

		polygonizer.add(union);
		Collection polygons = polygonizer.getPolygons();
		if (!polygons.isEmpty() || polygons.size() > 0){
			Iterator<Geometry> iterator = polygons.iterator();
			boolean isCon = false;//判断重构出来的多边形外圈是否有效，有效则加入容器无效则移除
			Collection<Geometry> geometries = new ArrayList<>();
			//第一个
			try {
				geometries.add(iterator.next());
			}catch (Exception e){
				System.out.print(e);
			}
			while (iterator.hasNext()){
				boolean isvaild = false;
				Geometry last = iterator.next();
				//trueGeom.add(last);
				Coordinate[] c2 = last.getCoordinates();
				Iterator<Geometry> geometriesIterator = geometries.iterator();
				while (geometriesIterator.hasNext()){
					Geometry next = geometriesIterator.next();
					Coordinate[] c1 = next.getCoordinates();
					if(c1.length > c2.length){
						isCon = this.isContent(c1,c2);//判断两者图形节点的包含关系
					}else {
						isCon = this.isContent(c2,c1);
					}
					if(isCon){
						if(c1.length < c2.length){
							geometriesIterator.remove();
							isvaild = true;
						}else {
							isvaild = false;
							break;
						}
					}else {
						isvaild = true;
					}
				}
				if (isvaild){
					geometries.add(last);
				}
			}
			Iterator<Geometry> i = geometries.iterator();
			while (i.hasNext()){
				collectionOut.add((Polygon) i.next());
			}
		}
	}

	/**
	 * 转为有效的多边形几何体
	 * @param collectionOut 外圈
	 * @param getPolygonizerInvalidInn 内圈
	 * @return
	 */
	public Geometry tPolygonGeometry( Collection<Polygon> collectionOut,Polygonizer getPolygonizerInvalidInn){

		Iterator<Polygon> iteratorout = collectionOut.iterator();
		Geometry polygonOut = this.getMultiPolygon(iteratorout);

		Collection invalidInner = getPolygonizerInvalidInn.getPolygons();
		Iterator<Polygon> iteratorinner = invalidInner.iterator();
		Geometry polygonInner = this.getMultiPolygon(iteratorinner);

		Geometry sym = polygonOut.symDifference(polygonInner);

		return sym;
	}

	public Geometry getMultiPolygon(Iterator<Polygon> i){
		Geometry geom = i.next();
		while (i.hasNext()){
			try {
				Polygon p = i.next();
				geom = geom.symDifference(p);
			}catch (Exception e){
				System.out.print("getMultiPolygon error: "+e);
			}
		}
		return geom;
	}

	/**
	 * 判断一个图形的节点跟另外一个图形的节点完全重合
	 * @param a 节点坐标
	 * @param b 节点坐标
	 * @return
	 */
	public boolean isContent(Coordinate[] a,Coordinate[] b){
		if (a == null || b == null || a.length < b.length){
			return false;
		}

		boolean[] booleans = new boolean[a.length];
		int same = 0;
		for (int i = 0;i<b.length;i++){
			for (int j = 0;j<a.length;j++){
				if(b[i].equals(a[j])&&  !booleans[j]){
					booleans[j] = true;
					same++;
					break;
				}
			}
		}
		boolean b1 = same == b.length;
		return b1;
	}
}
