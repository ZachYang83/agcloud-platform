package com.augurit.agcloud.agcom.agsupport.sc.spatial.topology.controller;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.topology.service.IAgOperate;
import net.sf.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-04-28.
 */
@RestController
@RequestMapping("/agsupport/operate")
public class AgOperateController {

    @Autowired
    private IAgOperate operate;

    /**
     * 缓冲区
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/buffer")
    public String buffer(HttpServletRequest request) throws JSONException {
        String result = "";
        try {
            String wkt = request.getParameter("wkt");
            double dBuffer = Double.valueOf(request.getParameter("dBuffer"));
            result = operate.buffer(wkt, dBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 相交
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/intersect")
    public String intersect(HttpServletRequest request) throws JSONException {
        String post = "";
        String result = "";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            result = operate.intersect(wkt1, wkt2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取凸包
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/convexHull")
    public String convexHull(HttpServletRequest request) throws JSONException {
        String result = "";
        try {
            String wkt = request.getParameter("wkt");
            result = operate.convexHull(wkt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 合并
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/union")
    public String union(HttpServletRequest request) throws JSONException {
        String result = "";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            result = operate.union(wkt1, wkt2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 擦除
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/difference")
    public String difference(HttpServletRequest request) throws JSONException {
        String result = "";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            result = operate.difference(wkt1, wkt2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 交集取反
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/symDifference")
    public String symDifference(HttpServletRequest request) throws JSONException {
        String result = "";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            result = operate.symDifference(wkt1, wkt2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 弧段闭合
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/closingLineString")
    public String closingLineString(HttpServletRequest request) throws JSONException {
        String result = "";
        try {
            String wkt = request.getParameter("wkt");
            result = operate.closingLineString(wkt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 距离
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/distance")
    public String distance(HttpServletRequest request) throws JSONException {
        String result = "";
        try {
            String wkt1 = request.getParameter("wkt1");
            String wkt2 = request.getParameter("wkt2");
            result = operate.distance(wkt1, wkt2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 克隆
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/clone")
    public String clone(HttpServletRequest request) throws JSONException {
        String result = "";
        try {
            String wkt = request.getParameter("wkt");
            result = operate.clone(wkt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 平滑
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/smooth")
    public String smooth(HttpServletRequest request) throws JSONException {
        String result = "";
        try {
            String wkt = request.getParameter("wkt");
            double fit = Double.valueOf(request.getParameter("fit"));
            result = operate.smooth(wkt, fit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 抽稀
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/sparse")
    public String sparse(HttpServletRequest request) throws JSONException {
        String result = "";
        try {
            String wkt = request.getParameter("wkt");
            double fit = Double.valueOf(request.getParameter("fit"));
            result = operate.sparse(wkt, fit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
