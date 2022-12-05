package com.augurit.agcloud.agcom.agsupport.sc.serviceLog.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgServiceLog;
import com.augurit.agcloud.agcom.agsupport.sc.service.controlller.AgServiceController;
import com.augurit.agcloud.agcom.agsupport.sc.serviceLog.service.IAgServiceLog;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created by czh on 2017-12-08.
 */

@Api(value = "服务访问概况",description = "服务访问概况相关接口")
@RestController
@RequestMapping("/agsupport/serviceLog")
public class AgServiceLogController {


    private static Logger logger = LoggerFactory.getLogger(AgServiceController.class);

    @Autowired
    private IAgServiceLog iAgServiceLog;

    @RequestMapping("/index.html")
    public ModelAndView index() {
        return new ModelAndView("agcloud/agcom/agsupport/service/index");
    }

    @RequestMapping("/serviceLog.html")
    public ModelAndView countPage() {
        return new ModelAndView("agcloud/agcom/agsupport/serviceLog/serviceLog");
    }


    @ApiOperation(value = "通过用户获取服务访问概况",notes = "获取服务访问概况接口")
    @RequestMapping(value = "/getLogOrderByUser",method = RequestMethod.GET)
    public ContentResultForm getLogOrderByUser() {
        try {
            List list = iAgServiceLog.getLogOrderByUser();
            return new ContentResultForm<>(true, list, "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContentResultForm<>(false, "", "查询失败");
    }

    @ApiOperation(value = "通过服务获取服务访问概况",notes = "通过服务获取服务访问概况接口")
    @RequestMapping(value = "/getLogOrderByService",method = RequestMethod.GET)
    public ContentResultForm getLogOrderByService() {
        try {
            List list = iAgServiceLog.getLogOrderByService();
            return new ContentResultForm<>(true, list, "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContentResultForm<>(false, "", "查询失败");
    }

    /**
     * 根据服务id获取服务日志访问情况
     * @param serviceId
     * @return 该条服务成功访问总数，花费总时间，失败访问次数
     * @throws Exception
     */
    @ApiOperation(value = "根据服务id获取服务日志访问情况",notes = "根据服务id获取服务日志访问情况(该条服务成功访问总数，花费总时间，失败访问次数)接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId",value = "服务id",required = true,dataType = "String")
    })
    @RequestMapping(value = "getLogByServiceId",method = RequestMethod.GET)
    public ContentResultForm getLogByServiceId(String serviceId) {
        try {
            List<AgServiceLog> list = iAgServiceLog.getLogByServiceId(serviceId);
            return new ContentResultForm(true,list);
        }catch (Exception e){
            e.printStackTrace();
            return new ContentResultForm(true,"","查询失败");
        }
    }

    //获取最近7天热门服务每天访问流量
    @ApiOperation(value = "获取最近7天热门服务每天访问流量",notes = "获取最近7天热门服务每天访问流量接口")
    @RequestMapping(value = "/getHotServiceRecentAllByte",method = RequestMethod.GET)
    public ContentResultForm getServiceByte() {
        try {   //获取热点服务最近访问流量总计
            Map res = iAgServiceLog.getHotServiceRecentAllByte();
            return new ContentResultForm<>(true, res, "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContentResultForm<>(false, "", "查询失败");
    }
}
