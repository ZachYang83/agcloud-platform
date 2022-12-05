package com.augurit.agcloud.agcom.agsupport.sc.statistics.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgDataSituation;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.sc.statistics.service.IAgStatisticsService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

/**
 * @author zhangmingyang
 * @Description: 数据概览
 * @date 2019-08-19 14:16
 */
@Api(value = "数据概览",description = "数据概览接口")
@RestController
@RequestMapping("/agsupport/statistics")
public class StatisticsController {
    private static final Logger log = LoggerFactory.getLogger(StatisticsController.class);
    @Autowired
    private IAgStatisticsService agStatisticsService;
    @Autowired
    private IAgDic iAgDic;
    @RequestMapping("/index.html")
    @ApiIgnore
    public ModelAndView index(Model model){
        return new ModelAndView("agcloud/agcom/agsupport/statistics/index");
    }

    @ApiOperation(value = "统计资源数量",notes = "统计资源数量接口")
    @RequestMapping(value = "/getResourceCount",method = RequestMethod.GET)
    public ContentResultForm getResourceCount(){
        ContentResultForm resultForm = new ContentResultForm(true);
        try {
            Integer count = agStatisticsService.layerCount();
            resultForm.setContent(count);
            resultForm.setMessage("资源数量");
        }catch (Exception e){
            resultForm.setSuccess(false);
            resultForm.setMessage("统计出错");
            log.error("统计异常",e);
        }
        return resultForm;
    }

    @ApiOperation(value = "按照数据类型分别统计数量",notes = "按照数据类型分别统计数量接口")
    @RequestMapping(value = "/getCountByDataType",method = RequestMethod.GET)
    public ContentResultForm getCountByDataType(){
        ContentResultForm resultForm = new ContentResultForm(true);
        try {
            List<Map> listMap = agStatisticsService.countByDataType();
            List<AgDic> agDicList = iAgDic.getAgDicByTypeCode("A201");
            List<Map> resultMap = new ArrayList<>();
            for (Map map : listMap){
                for (AgDic agDic : agDicList){
                    if (map.get("dataType").equals(agDic.getCode())){
                        map.put("dataTypeName",agDic.getName());
                        resultMap.add(map);
                    }
                }
            }
            resultForm.setContent(resultMap);
        }catch (Exception e){
            resultForm.setSuccess(false);
            resultForm.setMessage("统计出错");
            e.printStackTrace();
        }
        return resultForm;
    }
    @ApiOperation(value = "按数据源分类",notes = "按数据源分类统计数量接口")
    @RequestMapping(value = "/getCountByDataSource",method = RequestMethod.GET)
    public ContentResultForm getCountByDataSource(){
        ContentResultForm resultForm = new ContentResultForm(true);
        try {
            List<Map> listMap = agStatisticsService.countByDataSource();
            List<Map> resultMap = new ArrayList<>();
            List<AgDic> agDicList = iAgDic.getAgDicByTypeCode("A202");
            for (Map map : listMap){
                for (AgDic agDic : agDicList){
                    if (map.get("dataResourceType").equals(agDic.getCode())){
                        map.put("dataResourceTypeName",agDic.getName());
                        resultMap.add(map);
                    }
                }
            }
            resultForm.setContent(resultMap);
        }catch (Exception e){
            resultForm.setSuccess(false);
            resultForm.setMessage("统计出错");
            e.printStackTrace();
        }

        return resultForm;
    }

    @ApiOperation(value = "资源容量",notes = "统计资源容量接口")
    @RequestMapping(value = "getResourceSize",method = RequestMethod.GET)
    public ContentResultForm getResourceSize(){
        ContentResultForm resultForm = new ContentResultForm(true);
        try {
            Long resourceSize = agStatisticsService.getResourceSize();
            resultForm.setContent(resourceSize);
        }catch (Exception e){
            log.error("系统异常!",e);
            resultForm.setSuccess(false);
        }
        return resultForm;
    }

    @ApiOperation(value = "主题目录数据情况",notes = "获取主题目录数据情况接口")
    @RequestMapping(value = "getResourceSizeBySubjectType",method = RequestMethod.GET)
    public ContentResultForm getResourceSizeBySubjectType(){
        ContentResultForm resultForm = new ContentResultForm(true);
        try {
            List<Map> resourceSizeBySubjectType = agStatisticsService.getResourceSizeBySubjectType();
            resultForm.setContent(resourceSizeBySubjectType);
        }catch (Exception e){
            log.error("系统异常!",e);
            resultForm.setSuccess(false);
        }
        return resultForm;
    }
    @ApiOperation(value = "数据变更情况",notes = "数据变更情况接口")
    @RequestMapping(value = "getResourceSituationByTime",method = RequestMethod.GET)
    public ContentResultForm getResourceSituationByTime(){
        ContentResultForm resultForm = new ContentResultForm(true);
        try {
            List<Map> resourceSituationByTime = agStatisticsService.getResourceSituationByTime();
            resultForm.setContent(resourceSituationByTime);
        }catch (Exception e){
            log.error("统计出错!",e);
            resultForm.setMessage("统计出错!");
            resultForm.setSuccess(false);
        }
        return resultForm;
    }

    /**
     * 每天晚上2点统计数据
     */
    @ApiIgnore
    @Scheduled(cron = "0 0 2 * * ?")
    public void excute(){
        try {
            AgDataSituation agDataSituation = agStatisticsService.statisticsCountAndSize();
            agDataSituation.setId(UUID.randomUUID().toString());
            agDataSituation.setStatisticalTime(new Date());
            agStatisticsService.insert(agDataSituation);
        }catch (Exception e){
            log.error("统计出错",e);
        }

    }

}
