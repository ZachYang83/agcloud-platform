package com.augurit.agcloud.agcom.agsupport.sc.spatial.topology.controller;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.RqGpsCheck;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.topology.service.IAgRelated;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.common.dbcp.DBHelper;
import com.common.util.ReflectBeans;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.LINESTRING;
import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.POLYGON;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-04-28.
 */
@RestController
@RequestMapping("/agsupport/related")
public class AgRelatedController {

    @Autowired
    private IAgRelated related;

    /**
     * 判断两个几何图形是否相离
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stDisjoint")
    public String stDisjoint(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            if (related.stDisjoint(wkt1, wkt2)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断两个几何图形是否有交集
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stIntersects")
    public String stIntersects(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            if (related.stIntersects(wkt1, wkt2)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断几何图形1是否包含几何图形２
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stContains")
    public String stContains(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            if (related.stContains(wkt1, wkt2)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 判断几何图形1是否在几何图形2里面
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stWithin")
    public String stWithin(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            if (related.stWithin(wkt1, wkt2)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断几何图形1是否穿过几何图形2
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stCrosses")
    public String stCrosses(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            if (related.stCrosses(wkt1, wkt2)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断几何图形1是否与几何图形2相连
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stTouches")
    public String stTouches(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            if (related.stTouches(wkt1, wkt2)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断几何图形1是否被几何图形2覆盖
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stCoveredBy")
    public String stCoveredBy(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            if (related.stCoveredBy(wkt1, wkt2)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断几何图形1是否覆盖几何图形2
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stCovers")
    public String stCovers(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            if (related.stCovers(wkt1, wkt2)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断两个形状是否相等（post）
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stEquals")
    public String stEquals(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            if (related.stEquals(wkt1, wkt2)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断两个几何图形完全相等
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stEqualsExact")
    public String stEqualsExact(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            if (related.stEqualsExact(wkt1, wkt2)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断两个几何图形在指定的容差下，是否严格相等（post）
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stEqualsExactEx")
    public String stEqualsExactEx(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            double tolerance = Double.valueOf(request.getParameter("tolerance"));
            if (related.stEqualsExact(wkt1, wkt2, tolerance)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 将两个几何图形标准化之后进行判断。
     * 该方法相对昂贵，为实现最大的性能，应该在合适的时候对单个几何图形进行规范化处理。（post）
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stEqualsNorm")
    public String stEqualsNorm(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            if (related.stEqualsNorm(wkt1, wkt2)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据SFS，判断两个几何图形拓扑相等
     * SFS具有如下定义
     * 两个几何形状至少有一个公共点，两个几何图形没有点落在另一个几何图形之外。（post）
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stEqualsTopo")
    public String stEqualsTopo(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            if (related.stEqualsTopo(wkt1, wkt2)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断几何图形2在几何图形1的指定距离内
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stIsWithinDistance")
    public String stIsWithinDistance(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            double distance = Double.valueOf(request.getParameter("distance"));
            if (related.stIsWithinDistance(wkt1, wkt2, distance)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 几何图形至少有一个点不在另一个几何图形内，他们具有相同的维度，两个图形的交集的维度与这两个图形的维度相同
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stOverlaps")
    public String stOverlaps(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            if (related.stOverlaps(wkt1, wkt2)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断两个几何要素是否满足九交模型的关系
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stRelate")
    public String stRelate(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            String intersectionPattern = request.getParameter("intersectionPattern");
            if (related.stRelate(wkt1, wkt2, intersectionPattern)) {
                result = "{\"status\":\"true\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 比较几何图形1大于、小于、等于几何图形2
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/stCompareTo")
    public String stCompareTo(HttpServletRequest request) throws JSONException {
        String result = "{\"status\":\"false\"}";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            result = "{\"status\":\"" + related.stCompareTo(wkt1, wkt2) + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *   保存巡查轨迹测试
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/saveAllPoint")
    public String saveAllPoint(HttpServletResponse response, HttpServletRequest request) throws JSONException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String result = "{\"success\":\"true\"}";
        try {
            String userId = request.getParameter("userId");
            String x = request.getParameter("x");
            String y = request.getParameter("y");
            String time = request.getParameter("time");
            System.out.println("ags存储数据时间"+time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(time);
            RqGpsCheck rqGpsCheck=new RqGpsCheck();
            rqGpsCheck.setUserId(userId);
            rqGpsCheck.setX(x);
            rqGpsCheck.setY(y);
            rqGpsCheck.setUploadTime(date);
            DBHelper.save("ag_rq_gps_check", Arrays.asList(rqGpsCheck));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 保存巡查轨迹测试
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/gpsCheck")
    public String gpsCheck(HttpServletResponse response, HttpServletRequest request) throws JSONException {

        try {
            response.setHeader("Access-Control-Allow-Origin", "*");

            //request.getParameter("time")

            List<Point> pointList = new ArrayList<>();
            String param = request.getParameter("wkt2");
            JSONArray jsonArray = JSONArray.fromObject(param);
            for (int i = 0; i < jsonArray.size(); i++) {
                Point form = new Point();
                Object o = jsonArray.get(i);
                ReflectBeans.beanToMap(o);
                ReflectBeans.copy(o, form);
                pointList.add(form);
            }
            String userId = request.getParameter("userId");
            String uploadTime = request.getParameter("uploadTime");
            List values = new ArrayList();
            values.add(userId);

            String sql = "select x,y from ag_rq_gps_check where user_id=? ";
            if (StringUtils.isNotBlank(uploadTime)) {

                values.add(uploadTime);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
                String str = sdf.format(new Date());
                sql += " and upload_time <'" + str + "'";
            }
            sql+=" order by upload_time asc ";
            List<Point> points = DBHelper.find(Point.class, sql, values);

            String wkt1 = SpatialUtil.pointListToWKT(points, LINESTRING);
            String wkt2 = SpatialUtil.pointListToWKT(pointList, POLYGON);
            System.out.println("=====wkt1" + wkt1);
            System.out.println("=====wkt2" + wkt2);
            boolean flag = related.stIntersects(wkt1, wkt2);
            if (flag) {
                // return  points;
                return JsonUtils.toJson(new ContentResultForm<List<Point>>(true, points));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false));
    }

    /**
     * 保存巡查轨迹测试
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/gpsTrajectory")
    public String gpsTrajectory(HttpServletResponse response, HttpServletRequest request) throws JSONException {

        try {
            // 跨域
            response.setHeader("Access-Control-Allow-Origin", "*");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Calendar c = new GregorianCalendar();
            // 获取指定时间
            String time=request.getParameter("time");
            String user_id=request.getParameter("user_id");
            // 字符串转日期
            Date date = sdf.parse(time);
            System.out.println("gps获取数据时间"+time);
            System.out.println("指定时间time:"+sdf.format(date));
            c.setTime(date);//设置参数时间
            c.add(Calendar.SECOND,-5);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
            date=c.getTime(); //这个时间就是日期往后推一天的结果
            String str = sdf.format(date);
            System.out.println("指定时间前5秒时间str："+str);



            List values=new ArrayList();
            values.add(time);
            values.add(str);

            //  String sql="select user_id,x,y from ag_rq_gps_check where upload_time > '"+str+"' and upload_time <='"+time+"' order by upload_time ";
            String sql="select u.user_name ,rq.x,rq.y from ag_user u ,ag_rq_gps_check rq where u.id=rq.user_id and  rq.upload_time > '"+str+"' and rq.upload_time <='"+time+"' ";

            if (StringUtils.isNotBlank(user_id)){

                sql+=" and rq.user_id='"+user_id+"'";
            }
            sql+=" order by rq.upload_time ";
            List<Map>  points= DBHelper.find(sql,null);
            if (points.size()<1)   return JsonUtils.toJson(new ResultForm(false));
            return JsonUtils.toJson(new ContentResultForm<List<Map>>(true, points));
            // return JsonUtils.toJson(new ContentResultForm<Map>(true, points.get(0)));

            //  boolean flag = related.stIntersects(wkt1, wkt2);
            /*if (flag) {
                // return  points;
                return JsonUtils.toJson(new ContentResultForm<List<Point>>(true, points));

            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false));
    }

    @RequestMapping("/getGpsUserName")
    public String getGpsUserName(HttpServletResponse response, HttpServletRequest request) throws JSONException {

        try {
            // 跨域
            response.setHeader("Access-Control-Allow-Origin", "*");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Calendar c = new GregorianCalendar();
            // 获取指定时间
            String time=request.getParameter("time");
            // 字符串转日期
            Date date = sdf.parse(time);
            System.out.println("指定时间time:"+sdf.format(date));
            c.setTime(date);//设置参数时间
            c.add(Calendar.SECOND,-5);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
            date=c.getTime(); //这个时间就是日期往后推一天的结果
            String str = sdf.format(date);
            System.out.println("指定时间前5秒时间str："+str);

            // 查询间隔时间点的单个点

            List values=new ArrayList();
            values.add(time);
            values.add(str);

            //  String sql="select user_id,x,y from ag_rq_gps_check where upload_time > '"+str+"' and upload_time <='"+time+"' order by upload_time ";
            String sql="select u.id,u.user_name from ag_user u ,ag_rq_gps_check rq where u.id=rq.user_id and  rq.upload_time > '"+str+"' and rq.upload_time <='"+time+"' order by rq.upload_time";
            List<Map>  userNames= DBHelper.find(sql,null);
            if (userNames.size()<1)   return JsonUtils.toJson(new ResultForm(false));
            return JsonUtils.toJson(new ContentResultForm<List<Map>>(true, userNames));
            // return JsonUtils.toJson(new ContentResultForm<Map>(true, points.get(0)));
            //  boolean flag = related.stIntersects(wkt1, wkt2);
            /*if (flag) {
                // return  points;
                return JsonUtils.toJson(new ContentResultForm<List<Point>>(true, points));

            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false));
    }




    public String  time() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = new GregorianCalendar();
        Date date = new Date();
        System.out.println("系统当前时间:"+df.format(date));
        c.setTime(date);//设置参数时间
        c.add(Calendar.SECOND,-5);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
        date=c.getTime(); //这个时间就是日期往后推一天的结果
        String str = df.format(date);
        System.out.println("系统前5秒时间："+str);
        return str;
    }

    /*public static void main(String[] args) {

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Calendar c = new GregorianCalendar();
            // 获取指定时间
            String time="2017-11-09 00:00:00";
            // 字符串转日期
            Date date = sdf.check(time);
            System.out.println("指定时间:"+sdf.format(date));
            c.setTime(date);//设置参数时间
            c.add(Calendar.SECOND,-5);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
            date=c.getTime(); //这个时间就是日期往后推一天的结果
            String str = sdf.format(date);
            System.out.println("指定时间前5秒时间："+str);

            // 查询间隔时间点的单个点

            List values=new ArrayList();
            values.add(time);
            values.add(str);
            String sql="select user_id,x,y from ag_rq_gps_check where upload_time > '"+str+"' and upload_time <='"+time+"' order by upload_time ";
            List<Map>  points= DBHelper.find(sql,null);
            System.out.println(JsonUtils.toJson(points));

        }catch (Exception e) {
            e.printStackTrace();
        }

    }*/


  /* public static void main(String[] args) {
       RedisMap  map=new RedisMap();

    }*/

}
