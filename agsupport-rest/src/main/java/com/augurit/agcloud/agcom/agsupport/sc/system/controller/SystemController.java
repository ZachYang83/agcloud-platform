package com.augurit.agcloud.agcom.agsupport.sc.system.controller;


import com.augurit.agcloud.agcom.agsupport.sc.system.service.ISystem;
import com.common.util.Common;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private ISystem system;

    @RequestMapping("/getProperties")
    public String getProperties() {
        Map properties = Common.getProperties("application.properties");
        String json = JSONObject.fromObject(properties).toString();
        return json;
    }

    @RequestMapping("/getToken")
    public String getToken(String code) throws Exception {
        String token = system.getToken(code);
        if (token == null) {
            return "{'error':'无效的请求'}";
        }
        return "{'token':'" + token + "'}";
    }

    @RequestMapping("/checkToken")
    public boolean checkToken(String token) {
        boolean check = system.checkToken(token);
        return check;
    }
}
