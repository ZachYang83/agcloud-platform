package com.augurit.agcloud.agcom.agsupport.sc.regainWork.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.sc.regainWork.service.IRegainWork;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Augurit on 2017-04-18.
 */
@Api(value = "疫情复工项目信息",description = "疫情复工项目信息接口")
@RestController
@RequestMapping("/agsupport/regainWork")
public class regainWorkController {

    private static Logger logger = LoggerFactory.getLogger(regainWorkController.class);

    @Autowired
    private IRegainWork iRegainWork;

    @ApiOperation(value = "统计全市项目数据",notes = "统计全市项目数据接口")
    @RequestMapping(value = "/getStatistics",method = RequestMethod.GET)
    public ContentResultForm getStatistics() {
        try {
            JSONObject jsonObject = iRegainWork.getStatisticsOfCity();
            return new ContentResultForm(true,jsonObject,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "按部门分组获取项目统计信息",notes = "按部门分组获取项目统计信息接口")
    @RequestMapping(value = "/getStatisticsByBM",method = RequestMethod.GET)
    public ContentResultForm getStatisticsByBM() {
        try {
            JSONArray jsonArray = iRegainWork.getStatisticsByBM();
            return new ContentResultForm(true,jsonArray,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "按地区分组获取项目统计信息",notes = "按地区分组获取项目统计信息接口")
    @RequestMapping(value = "/getStatisticsByDQ",method = RequestMethod.GET)
    public ContentResultForm getStatisticsByDQ() {
        try {
            JSONArray jsonArray = iRegainWork.getStatisticsByDQ();
            return new ContentResultForm(true,jsonArray,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "按项目类型分组获取项目统计信息",notes = "按项目类型分组获取项目统计信息接口")
    @RequestMapping(value = "/getStatisticsByXMLX",method = RequestMethod.GET)
    public ContentResultForm getStatisticsByXMLX() {
        try {
            JSONArray jsonArray = iRegainWork.getStatisticsByXMLX();
            return new ContentResultForm(true,jsonArray,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "按部门分组获取重点企业(项目)统计信息",notes = "按部门分组获取重点企业(项目)统计信息接口")
    @RequestMapping(value = "/getStatisticsOfZDXM",method = RequestMethod.GET)
    public ContentResultForm getStatisticsOfZDXM() {
        try {
            JSONArray jsonArray = iRegainWork.getStatisticsOfZDXM();
            return new ContentResultForm(true,jsonArray,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "获取重点企业(项目)清单信息",notes = "获取重点企业(项目)清单信息接口")
    @RequestMapping(value = "/getZDXMQD",method = RequestMethod.GET)
    public ContentResultForm getZDXMQD() {
        try {
            JSONArray jsonArray = iRegainWork.getZDXMQD();
            return new ContentResultForm(true,jsonArray,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "获取帮扶-重点项目信息",notes = "获取帮扶-重点项目信息接口")
    @RequestMapping(value = "/getZDXMOfBF",method = RequestMethod.GET)
    public ContentResultForm getZDXMOfBF() {
        try {
            JSONArray jsonArray = iRegainWork.getZDXMOfBF();
            return new ContentResultForm(true,jsonArray,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "获取帮扶-城市更新重点项目信息",notes = "获取帮扶-城市更新重点项目信息接口")
    @RequestMapping(value = "/getCSGXOfBF",method = RequestMethod.GET)
    public ContentResultForm getCSGXOfBF() {
        try {
            JSONArray jsonArray = iRegainWork.getCSGXOfBF();
            return new ContentResultForm(true,jsonArray,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "获取帮扶-市建筑业重点企业信息",notes = "获取帮扶-市建筑业重点企业信息接口")
    @RequestMapping(value = "/getSJZYOfBF",method = RequestMethod.GET)
    public ContentResultForm getSJZYOfBF() {
        try {
            JSONArray jsonArray = iRegainWork.getSJZYOfBF();
            return new ContentResultForm(true,jsonArray,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "获取帮扶-房地产开发企业信息",notes = "获取帮扶-房地产开发企业信息接口")
    @RequestMapping(value = "/getFDCKFOfBF",method = RequestMethod.GET)
    public ContentResultForm getFDCKFOfBF() {
        try {
            JSONArray jsonArray = iRegainWork.getFDCKFOfBF();
            return new ContentResultForm(true,jsonArray,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "获取复工趋势信息",notes = "获取复工趋势信息接口")
    @RequestMapping(value = "/getStatisticsOfQS",method = RequestMethod.GET)
    public ContentResultForm getStatisticsOfQS() {
        try {
            JSONArray jsonArray = iRegainWork.getStatisticsOfQS();
            return new ContentResultForm(true,jsonArray,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "按地区分组获取诚信企业项目统计信息",notes = "按地区分组获取诚信企业项目统计信息接口")
    @RequestMapping(value = "/getStatisticsOfCXQY",method = RequestMethod.GET)
    public ContentResultForm getStatisticsOfCXQY() {
        try {
            JSONArray jsonArray = iRegainWork.getStatisticsOfCXQY();
            return new ContentResultForm(true,jsonArray,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "获取诚信企业项目清单信息",notes = "获取诚信企业项目清单信息")
    @RequestMapping(value = "/getXMQDOfCXQY",method = RequestMethod.GET)
    public ContentResultForm getXMQDOfCXQY() {
        try {
            JSONArray jsonArray = iRegainWork.getXMQDOfCXQY();
            return new ContentResultForm(true,jsonArray,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "获取项目清单信息",notes = "获取项目清单信息接口")
    @RequestMapping(value = "/getXMQD",method = RequestMethod.GET)
    public ContentResultForm getXMQD() {
        try {
            JSONArray jsonArray = iRegainWork.getYQDT_XMQD();
            return new ContentResultForm(true,jsonArray,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "根据工程编码查询项目上报的疫情信息",notes = "根据工程编码查询项目上报的疫情信息接口")
    @ApiImplicitParam(name = "gcbm",value = "工程编码",dataType = "String")
    @RequestMapping(value = "/getProjectInfo",method = RequestMethod.GET)
    public ContentResultForm getProjectInfo(String gcbm) {
        try {
            JSONObject jsonObject =  iRegainWork.getProjectInfo(gcbm);
            return new ContentResultForm(true,jsonObject,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "根据工程编码查询项目基础信息",notes = "根据工程编码查询项目基础信息接口")
    @ApiImplicitParam(name = "gcbm",value = "工程编码",dataType = "String")
    @RequestMapping(value = "/getProjectBasicInfo",method = RequestMethod.GET)
    public ContentResultForm getProjectBasicInfo(String gcbm) {
        try {
            JSONObject jsonObject =  iRegainWork.getProjectBasicInfo(gcbm);
            return new ContentResultForm(true,jsonObject,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "根据工程编码查询监理单位信息",notes = "根据工程编码查询监理单位信息接口")
    @ApiImplicitParam(name = "gcbm",value = "工程编码",dataType = "String")
    @RequestMapping(value = "/getProjectJLDW",method = RequestMethod.GET)
    public ContentResultForm getProjectJLDW(String gcbm) {
        try {
            JSONObject jsonObject =  iRegainWork.getProjectJLDW(gcbm);
            return new ContentResultForm(true,jsonObject,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "根据工程编码查询建设单位信息",notes = "根据工程编码查询建设单位信息接口")
    @ApiImplicitParam(name = "gcbm",value = "工程编码",dataType = "String")
    @RequestMapping(value = "/getProjectJSDW",method = RequestMethod.GET)
    public ContentResultForm getProjectJSDW(String gcbm) {
        try {
            JSONObject jsonObject =  iRegainWork.getProjectJSDW(gcbm);
            return new ContentResultForm(true,jsonObject,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "根据工程编码查询施工单位信息",notes = "根据工程编码查询施工单位信息接口")
    @ApiImplicitParam(name = "gcbm",value = "工程编码",dataType = "String")
    @RequestMapping(value = "/getProjectSGDW",method = RequestMethod.GET)
    public ContentResultForm getProjectSGDW(String gcbm) {
        try {
            JSONObject jsonObject =  iRegainWork.getProjectSGDW(gcbm);
            return new ContentResultForm(true,jsonObject,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "根据工程编码查询工人信息",notes = "根据工程编码查询工人信息接口")
    @ApiImplicitParam(name = "gcbm",value = "工程编码",dataType = "String")
    @RequestMapping(value = "/getWorkerInfo",method = RequestMethod.GET)
    public ContentResultForm getWorkerInfo(String gcbm) {
        try {
            JSONArray jsonArray =  iRegainWork.getWorkerInfo(gcbm);
            return new ContentResultForm(true,jsonArray,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }

    @ApiOperation(value = "测试",notes = "测试接口")
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public ContentResultForm test() {
        try {
            return new ContentResultForm(true,null,"请求成功");
        }
        catch (Exception ex){
            return new ContentResultForm(false,null,"发生异常:"+ex.getMessage());
        }
    }
}
