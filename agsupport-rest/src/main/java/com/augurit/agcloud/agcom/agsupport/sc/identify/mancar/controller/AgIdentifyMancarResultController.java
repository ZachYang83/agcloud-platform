package com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.controller;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.domain.AgIdentifyMancarResultResult;
import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.domain.AgIdentifyMancarResultParam;
import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.domain.AgIdentifyMancarResultStatisticsResult;
import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.service.IAgIdentifyMancarResultService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Author: qinyg
 * @Date: 2020/12/28 14:33
 * @tips: 人车识别结果
 */
@RestController
@RequestMapping("/agsupport/identify/mancarResult")
@Api(value = "人车识别结果接口", description = "人车识别结果接口")
public class AgIdentifyMancarResultController {
    private static final Logger logger = LoggerFactory.getLogger(AgIdentifyMancarResultController.class);

    @Autowired
    private IAgIdentifyMancarResultService resultService;


    @GetMapping("/find")
    @ApiOperation(value = "列表",notes = "列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceId", value = "视频主键id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "startTimeParam", value = "开始时间（yyyy-MM-dd HH:mm:ss）", dataType = "String"),
            @ApiImplicitParam(name = "endTimeParam", value = "结束时间（yyyy-MM-dd HH:mm:ss）", dataType = "String"),
    })
    public ResultForm find(String sourceId, String startTimeParam, String endTimeParam){
        if(StringUtils.isEmpty(sourceId)){
            return new ResultForm(false, "sourceId不能为空");
        }
        try{
            AgIdentifyMancarResultResult result = resultService.get(sourceId, startTimeParam, endTimeParam);
            return new ContentResultForm(true, result);
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "查询失败");
        }
    }



    @PostMapping("/add")
    @ApiOperation(value = "添加",notes = "添加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "识别结束后的视频", dataType = "File"),
    })
    public ResultForm add(AgIdentifyMancarResultParam param, MultipartFile file){
        if(StringUtils.isEmpty(param.getSourceId())){
            return new ResultForm(false, "sourceId不能为空");
        }
        try{
            resultService.add(param, file);
            return new ContentResultForm(true, "", "添加成功");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "添加失败");
        }
    }


    @GetMapping("/statistics")
    @ApiOperation(value = "统计",notes = "统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceId", value = "视频主键id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "times", value = "每隔几秒（正整数，单位：秒；默认 5）", required = true, dataType = "Integer"),
    })
    public ResultForm statistics(String sourceId, @RequestParam(defaultValue = "5") Integer times){
        if(StringUtils.isEmpty(sourceId)){
            return new ResultForm(false, "sourceId不能为空");
        }
        try{
            AgIdentifyMancarResultStatisticsResult result = resultService.statisticsPeopleAndCarNum(sourceId, times);
            return new ContentResultForm(true, result);
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "查询失败");
        }
    }

    @GetMapping("/download")
    @ApiOperation(value = "下载/导出",notes = "下载/导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceId", value = "视频主键id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "startTimeParam", value = "开始时间（yyyy-MM-dd HH:mm:ss）", dataType = "String"),
            @ApiImplicitParam(name = "endTimeParam", value = "结束时间（yyyy-MM-dd HH:mm:ss）", dataType = "String"),
    })
    public void download(String sourceId, String startTimeParam, String endTimeParam, HttpServletRequest request, HttpServletResponse response){
        try{
            resultService.exportExcel(sourceId, startTimeParam, endTimeParam, request, response);
        }catch (SourceException e){
            logger.info(e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }
}
