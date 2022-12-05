package com.augurit.agcloud.agcom.agsupport.sc.analysisReport.controller;

import com.augurit.agcloud.agcom.agsupport.sc.analysisReport.service.IAnalysisReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Author: libc
 * @Description: 分析报告控制器
 * @Date: 2020/10/25 10:36
 * @Version: 1.0
 */
@RestController
@Api(value = "分析报告", description = "分析报告相关接口")
@RequestMapping("/agsupport/applicationManager/analysisReport")
public class AnalysisReportController {

    @Autowired
    private IAnalysisReportService analysisReportService;

    @ApiOperation(value = "CIM退线分析报告下载", notes = "CIM退线分析报告下载接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataList", value = "表格数据数组", dataType = "List",required = true),
    }
    )
    @PostMapping(value = "/download")
    public void  download(@RequestBody ArrayList<Map<String,Object>> dataList, HttpServletRequest request, HttpServletResponse response) {
        analysisReportService.download(dataList,request,response);
    }


}
