package com.augurit.agcloud.agcom.agsupport.sc.reg.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.reg.service.IAgRegService;
import com.augurit.agcloud.framework.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by caokp on 2017-08-07.
 */
@RestController
@RequestMapping("/agsupport/reg")
public class AgRegController {
    private static Logger logger = LoggerFactory.getLogger(AgRegController.class);

    @Autowired
    private IAgRegService regService;

    @Autowired
    private IAgDir iAgDir;

    /**
     * 展示服务目录页面
     */
    @RequestMapping("/serviceCatalog.do")
    public ModelAndView serviceCatalog() {

        return new ModelAndView("agcom/reg/serviceCatalog");
    }

    @RequestMapping("/serviceCatalog2.do")
    public ModelAndView serviceCatalog2() {

        return new ModelAndView("agcom/reg/serviceCatalog2");
    }

    /**
     * 通用服务--初始化年份
     *
     * @return
     */
    @RequestMapping("/initYears")
    public String initYears() throws Exception {
        List<String> years = regService.getYears();
        if (years.size() > 0) return JsonUtils.toJson(years);
        return null;
    }

    /**
     * 处理服务
     *
     * @return
     */
    @RequestMapping("/serviceProcess.do")
    public ModelAndView serviceProcess(Model model) throws Exception{
        AgLayer agLayer = iAgDir.findLayerByNameCn("geometry.url");
        if (agLayer == null) agLayer = new AgLayer();
        model.addAttribute("geometry", agLayer);
        return new ModelAndView("agcom/reg/serviceProcess");
    }

    /**
     * 服务指南
     */
    @RequestMapping("/serviceGuide.do")
    public ModelAndView serviceGuide(String serviceName, Model model) throws Exception {
        AgLayer agLayer = iAgDir.findLayerByNameCn("geometry.url");
        if (agLayer == null) agLayer = new AgLayer();
        model.addAttribute("geometry", agLayer);
        model.addAttribute("serviceName", serviceName);
        return new ModelAndView("agcom/reg/serviceGuide");
    }

    /**
     * 服务测试
     */
    @RequestMapping("/serviceTest.do")
    public ModelAndView serviceTest(String serviceName, Model model) {
        model.addAttribute("serviceName", serviceName);
        return new ModelAndView("agcom/reg/serviceTest");
    }

    /**
     * 地图服务列表
     */
    @RequestMapping("/mapServiceList.do")
    public ModelAndView mapServiceList() {
        return new ModelAndView("agcom/reg/mapServiceList");
    }

    /**
     * 地图服务示例
     */
    @RequestMapping("/examples.do")
    public ModelAndView examples(String num, HttpServletRequest request, Model model) throws Exception {
        AgLayer agLayer = iAgDir.findLayerByNameCn("leaflet.url");
        if (agLayer == null) agLayer = new AgLayer();
        model.addAttribute("leaflet", agLayer);
        request.setAttribute("serviceNum", num);
        return new ModelAndView("agcom/reg/examples");
    }

    /**
     * 空间分析服务列表
     */
    @RequestMapping("/spatialAnalysis.do")
    public ModelAndView spatialAnalysis() {
        return new ModelAndView("agcom/reg/spatialAnalysis");
    }
}
