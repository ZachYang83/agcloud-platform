package com.augurit.agcloud.agcom.agsupport.sc.previewLayer.controller;

import io.swagger.annotations.Api;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
@Api(value = "地图服务预览",description = "地图服务预览相关接口")
@RestController
@RequestMapping("/agsupport/previewLayer")
public class PreviewLayerController {

    @RequestMapping("/index.html")
    public ModelAndView index(HttpServletRequest request, Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/previewLayer/index");
    }

    @RequestMapping("/preview.html")
    public ModelAndView preview(HttpServletRequest request, Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/previewLayer/preview");
    }
}
