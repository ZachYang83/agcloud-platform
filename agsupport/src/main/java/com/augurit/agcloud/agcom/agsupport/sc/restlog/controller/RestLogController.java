package com.augurit.agcloud.agcom.agsupport.sc.restlog.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgSysLog;
import com.augurit.agcloud.agcom.agsupport.sc.restlog.service.IRestLog;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.github.pagehelper.Page;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@Controller
@RequestMapping("/agsupport/restLog")
public class RestLogController {

    private static Logger logger = LoggerFactory.getLogger(RestLogController.class);

    @Autowired
    private IRestLog restLog;

    @RequestMapping("/index.html")
    @ApiIgnore
    public ModelAndView index() {
        return new ModelAndView("agcloud/agcom/agsupport/serviceLog/restLog");
    }



    @ApiOperation(value = "分页获取rest调用日志",notes = "分页获取rest调用日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agSysLog",required = false, value = "日志对象信息", dataType = "AgSysLog"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:/agsupport/param/paramList?page=1&rows=10&name=", dataType = "Page")
    })
    @RequestMapping(value = "/searchRestLog",method = RequestMethod.GET)
    @ResponseBody
    public ContentResultForm searchRestLog(AgSysLog agSysLog, Page page) throws Exception{
        Map<String, Object> data = restLog.search(agSysLog, page);
        return new ContentResultForm(true,data);
    }
}
