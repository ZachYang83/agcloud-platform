package com.augurit.agcloud.agcom.agsupport.common.util;
import com.common.util.ConfigProperties;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017-06-16.
 */
@RestController
@RequestMapping("/agsupport/geoServer")
public class GeoServerUtil {

    @RequestMapping("/getGeoServerInfo")
    public static JSONObject getGeoServerInfo(){
        JSONObject geoServerInfo = new JSONObject();
        geoServerInfo.put("url", ConfigProperties.getByKey("geoserver.url"));
        geoServerInfo.put("user", ConfigProperties.getByKey("geoserver.user"));
        geoServerInfo.put("password", ConfigProperties.getByKey("geoserver.password"));
        return geoServerInfo;
    }
}
