package com.augurit.agcloud.agcom.agsupport.sc.coordTrans.util;

import com.coordtrans.bean.BaseCoordinate;

import java.math.BigDecimal;

public class CoordTransUtil {

	static double pi = 3.14159265358979324;
	static double a = 6378245.0;
	static double ee = 0.00669342162296594323;
	public final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

	public static BaseCoordinate bd2wgs(BaseCoordinate coor_bd) {
		double[] coor = bd2wgs(coor_bd.getY(), coor_bd.getX());
		return new BaseCoordinate(coor[1], coor[0]);
	}

	public static BaseCoordinate wgs2bd(BaseCoordinate coor_wgs) {
		double[] coor = wgs2bd(coor_wgs.getY(), coor_wgs.getX());
		return new BaseCoordinate(coor[1], coor[0]);
	}

	public static double[] bd2wgs(double lat, double lon) {
		double[] wgs2bd = wgs2bd(lat, lon);
		double[] bd2wgs = new double[] { 2 * lat - wgs2bd[0], 2 * lon - wgs2bd[1] };
		return bd2wgs;
	}

	public static double[] wgs2bd(double lat, double lon) {
		double[] wgs2gcj = wgs2gcj(lat, lon);
		double[] gcj2bd = gcj2bd(wgs2gcj[0], wgs2gcj[1]);
		return gcj2bd;
	}

	private static double[] gcj2bd(double lat, double lon) {
		double x = lon, y = lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		return new double[] { bd_lat, bd_lon };
	}

	private static double[] bd2gcj(double lat, double lon) {
		double x = lon - 0.0065, y = lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		return new double[] { gg_lat, gg_lon };
	}

	private static double[] wgs2gcj(double lat, double lon) {
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double mgLat = lat + dLat;
		double mgLon = lon + dLon;
		double[] loc = { mgLat, mgLon };
		return loc;
	}

	private static double transformLat(double lat, double lon) {
		double ret = -100.0 + 2.0 * lat + 3.0 * lon + 0.2 * lon * lon + 0.1 * lat * lon + 0.2 * Math.sqrt(Math.abs(lat));
		ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lon * pi) + 40.0 * Math.sin(lon / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(lon / 12.0 * pi) + 320 * Math.sin(lon * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static double transformLon(double lat, double lon) {
		double ret = 300.0 + lat + 2.0 * lon + 0.1 * lat * lat + 0.1 * lat * lon + 0.1 * Math.sqrt(Math.abs(lat));
		ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(lat / 12.0 * pi) + 300.0 * Math.sin(lat / 30.0 * pi)) * 2.0 / 3.0;
		return ret;
	}

	public static double round(double a, int num) {
		BigDecimal A = new BigDecimal(a);
		return A.setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static void main(String[] args) {
		//百度坐标 116.397428 39.90923
		//wgs84坐标 116.384811 39.901487
		BaseCoordinate coor_bd_1 = new BaseCoordinate(116.397428, 39.90923);
		BaseCoordinate coor_84_1 = bd2wgs(coor_bd_1);
		System.out.println("百度转wgs84：" + coor_84_1.getX());
		System.out.println("百度转wgs84：" + coor_84_1.getY());
		BaseCoordinate coor_84_2 = new BaseCoordinate(116.384811, 39.901487);
		BaseCoordinate coor_bd_2 = wgs2bd(coor_84_2);
		System.out.println("wgs84转百度：" + coor_bd_2.getX());
		System.out.println("wgs84转百度：" + coor_bd_2.getY());
	}

}
