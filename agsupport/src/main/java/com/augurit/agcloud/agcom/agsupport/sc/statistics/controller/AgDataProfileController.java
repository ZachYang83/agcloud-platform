package com.augurit.agcloud.agcom.agsupport.sc.statistics.controller;

import io.swagger.annotations.Api;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author zhangmingyang
 * @Description: TODO
 * @date 2019-08-27 9:57
 */
@Api(value = "资源概览",description = "资源概览接口")
@RestController
@RequestMapping("/agsupport/dataProfile")
public class AgDataProfileController {
    @RequestMapping("/index.html")
    @ApiIgnore
    public ModelAndView index(Model model){
        return new ModelAndView("agcloud/agcom/agsupport/dataProfile/index");
    }
}
