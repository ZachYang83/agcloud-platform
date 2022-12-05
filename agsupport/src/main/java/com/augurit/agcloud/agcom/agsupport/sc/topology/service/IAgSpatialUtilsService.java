package com.augurit.agcloud.agcom.agsupport.sc.topology.service;

/**   
* @Author：李德文 
* @CreateDate： 2014-06-14
* @Description: 拓扑接口类
* @Copyright: 广州奥格智能科技有限公司
*/ 
public interface IAgSpatialUtilsService {
	/**
	 * SDE根据图层名获取该图层所有字段信息
	 * 
	 * @param name
	 * @param owner
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	/*public List<Field> getFieldInfoByLayerNameForSDE(String name, String owner,
			Connection conn) throws SQLException;*/


	/**
	 * SDE根据图层名获取该图层的基本配置信息
	 * 
	 * @param name
	 * @param owner
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	/*public AgLayerInfo getLayerInfoByLayerNameForSDE(String name, String owner,
			Connection conn) throws SQLException;*/


//	public Connection getConnection(DataSource ds);

	/**
	 * 返回WKT格式的空间数据的所有坐标点
	 * 
	 * @param wkt
	 * @return
	 */
//	public List<AgPoint> paseWKTtoPoint(String wkt);

	/**
	 * 对面、线点进行抽稀
	 * 
	 * @param wkt
	 *            WKT格式的空间数据
	 * @param d
	 *            抽稀限制阀值(不同坐标系不同，可以通过分辨率的方式算出)
	 * @return 抽稀后的WKT格式
	 */
    String geometryGeneralize(String wkt, double d);

	/**
	 * 当第二个几何完全被第一个几何包含的时候返回true
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
    Boolean stContains(String wkt1, String wkt2);

	/**
	 * 当两个几何相交的时候返回true
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
    Boolean stIntersects(String wkt1, String wkt2);

	/**
	 * 如果两个几何相交的部分都不在两个几何的内部，则返回true
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
    Boolean stTouches(String wkt1, String wkt2);
	
	/**
	 * 如果两个几何的交集生成空集，则返回true
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
    Boolean stDisjoint(String wkt1, String wkt2);
	
	/**
	 * 如果这两个几何完全相同，则返回true
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
    Boolean stEquals(String wkt1, String wkt2);
	
	/**
	 *  求几何的凸包
	 * 
	 * @param wkt1
	 * @return
	 */
    String stConvexHull(String wkt1);
	
	/**
	 *  求几何的边界
	 * 
	 * @param wkt1
	 * @return
	 */
    String stBoundary(String wkt1);
	
	/**
	 *  求几何的形心
	 * 
	 * @param wkt1
	 * @return
	 */
    String stCentroid(String wkt1);
	
	/**
	 * 根据几何对象和距离，求围绕源对象的缓冲区的几何对象
	 * 
	 * @param wkt1
	 * @param distance
	 * @return
	 */
    String stBuffer(String wkt1, String distance);

	/**
	 * 求2个几何相交的部分
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
    String stIntersection(String wkt1, String wkt2);

	/**
	 * 求2个几何不相交的部分
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
    String stSymmetricDiff(String wkt1, String wkt2);

	/**
	 * 求2个几何的并集
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
    String stUnion(String wkt1, String wkt2);

	/**
	 * 求2个几何的最短距离
	 * 
	 * @param wkt1
	 * @param wkt2
	 * @return
	 */
    String stDistance(String wkt1, String wkt2);

	/**
	 * 求几何体的周长
	 * 
	 * @param wkt
	 * @return
	 */
    String stLength(String wkt);

	/**
	 * 求几何体的面积
	 * 
	 * @param wkt
	 * @return
	 */
    String stArea(String wkt);

}
