package com.augurit.agcloud.agcom.agsupport.sc.topology.service.impl;

import com.augurit.agcloud.agcom.agsupport.sc.topology.service.IAgSpatialUtilsService;
import com.esri.sde.sdk.client.SeCoordinateReference;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeShape;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
