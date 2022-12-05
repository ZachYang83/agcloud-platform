package com.augurit.agcloud.agcom.agsupport.sc.spatial.config;

import spatial.GeometryApplication;

import java.text.SimpleDateFormat;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-04-20.
 */
public class SpatialConfig {

    /**
     * 数据库字段 避免字段重复 字段实际值采用ag+MD5码截取其中前10位的方式 生成方法 SpatialUtil.generateMD5Code
     */
    public static final String WKT_COLUMN = GeometryApplication.WKT;
    public static final String GEO_COLUMN = "agecc174e3e0";
    public static final String STATS_COLUMN = "ag780e223b21";

    /**
     * 数据库类型
     */
    public static final String DB_TYPE_ORACLE = "ora";
    public static final String DB_TYPE_MYSQL = "mysql";
    //public static final String DB_TYPE_POSTGIS = "pos";
    public static final String DB_TYPE_POSTGIS = "postgresql";
    public static final String DB_TYPE_DB2 = "db2";

    public static final String ORACLE            = "oracle";
    public static final String POSTGRESQL        = "postgresql";
    public static final String MYSQL             = "mysql";

    /**
     * 统计类型 sum 求和 count 求总数量 avg 求平均值 max 求最大值 min 求最小值
     */
    public static final String STATS_SUM = GeometryApplication.STATS_SUM;
    public static final String STATS_COUNT = GeometryApplication.STATS_COUNT;
    public static final String STATS_AVG = GeometryApplication.STATS_AVG;
    public static final String STATS_MAX = GeometryApplication.STATS_MAX;
    public static final String STATS_MIN = GeometryApplication.STATS_MIN;

    /**
     * WKT类型
     */
    public static final String POINT = "POINT";
    public static final String LINESTRING = "LINESTRING";
    public static final String POLYGON = "POLYGON";
    public static final String MULTIPOINT = "MULTIPOINT";
    public static final String MULTILINESTRING = "MULTILINESTRING";
    public static final String MULTIPOLYGON = "MULTIPOLYGON";

    /**
     * MongoDB配置
     */
    public static final int INSERT_NUM = 50;
    //一次缓冲的数目
    public static final int TRANS_NUM = 10 * 1000;
    public static final String MINX_COLUMN = "agfb9c130e79";
    public static final String MAXX_COLUMN = "agffe810180a";
    public static final String MINY_COLUMN = "ag182a633d45";
    public static final String MAXY_COLUMN = "agb72fa73c7a";
    public static final String CREATETIME_COLUMN = "ag9b6e55d52d";
    public static final String PK_COLUMN = "agab4c94d612";
    //public static final String NUM_COLUMN = "agb74a48df85";
    public static final String DATA_TRANS_LOG_TABLE = "data_trans_log";

    /**
     * 数据缓冲msg
     */
    public static final String MSG_1 = "数据缓冲成功！请继续新的数据缓冲！";
    public static final String MSG_2 = "数据缓冲失败！请检查图层配置信息重新进行数据缓冲！";
    public static final String MSG_3 = "停止数据缓冲成功！请继续下一步操作";
    public static final String MSG_4 = "服务器正在进行数据缓冲，请稍后再试！";
    public static final String MSG_5 = "服务器正在对该图层进行数据缓冲，请稍后再试！";
    public static final String MSG_6 = "清除缓冲数据成功！";
    public static final String MSG_7 = "清除缓冲数据失败，请稍后再试";
    public static final String MSG_8 = "正在初始化缓冲中 ···";
    public static final String MSG_9 = "正在查询数据 ···";
    public static final String MSG_10 = "请检查MongDB环境配置~";

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
