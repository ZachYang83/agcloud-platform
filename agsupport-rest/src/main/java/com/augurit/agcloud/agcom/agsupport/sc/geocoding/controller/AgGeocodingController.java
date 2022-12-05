package com.augurit.agcloud.agcom.agsupport.sc.geocoding.controller;

import com.augurit.agcloud.agcom.agsupport.sc.geocoding.service.IAgGeocoding;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.common.util.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.util.*;

/**
 * 地理编码：地址匹配和逆地址匹配
 * Created by czh on 2018-02-05.
 */
@RestController
@RequestMapping("/agsupport/geocoding")
public class AgGeocodingController {

    @Autowired
    private IAgGeocoding iAgGeocoding;

    /**
     * 返回匹配结果最相似的10条记录
     *
     * @param address
     * @return
     */
    @RequestMapping("/searchByAddress")
    public String searchByAddress(String address) {
        try {
            if (Common.isCheckNull(address)) return null;
            address = URLDecoder.decode(address, "utf-8");
            String[] addArr = address.split(",");
            List<Map> list = new ArrayList<Map>();
            List<Map> result;
            for (String add : addArr) {
                list.addAll(iAgGeocoding.searchByAddress(add));
            }
            if (list.size() > 1) {
                Set<Map> addressSet = new TreeSet<Map>((o1, o2) -> o1.get("dz").equals(o2.get("dz")) == true ? 0 : 1);
                addressSet.addAll(list);
                result = new ArrayList<Map>(addressSet);
            } else {
                result = null;
            }
            return JsonUtils.toJson(new ContentResultForm<>(true, result, "查询成功"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ContentResultForm<>(false, null, "查询失败"));
    }

    /**
     * 返回该点半径10米内的记录
     * @param wkt
     * @param type  shape的单位:米(meter)，度(degree)
     * @return
     */
    @RequestMapping("/searchByPoint")
    public String searchByPoint(String wkt,String type) {
        try {
            if (Common.isCheckNull(wkt)) return null;
            String[] wktArr = wkt.split(",");
            List<Map> list = new ArrayList<Map>();
            List<Map> result;
            for (String _wkt : wktArr) {
                String str = _wkt.toUpperCase().replaceAll(" ", "");
                if (str.indexOf("POINT(") != -1 && str.lastIndexOf(")") != -1) {
                    list.addAll(iAgGeocoding.searchByPoint(_wkt,type));
                }
            }
            if (list.size() > 1) {
                Set<Map> wktSet = new TreeSet<Map>((o1, o2) -> o1.get("dz").equals(o2.get("dz")) == true ? 0 : 1);
                wktSet.addAll(list);
                result = new ArrayList<Map>(wktSet);
            } else {
                result = null;
            }
            return JsonUtils.toJson(new ContentResultForm<>(true, result, "查询成功"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ContentResultForm<>(false, null, "查询失败"));
    }
}
