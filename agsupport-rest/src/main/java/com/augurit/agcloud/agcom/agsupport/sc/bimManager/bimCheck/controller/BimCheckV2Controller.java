package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.controller;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheck;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.domain.ExcelResponseDomain;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.IBimCheckService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author: libc
 * @Description: BIM审查控制器 （审查意见pdf文件生成/预览/下载）
 * @Date: 2020/9/8 16:20
 * @Version: 1.0
 */
@RestController
@Api(value = "BIM审查", description = "BIM审查相关接口")
@RequestMapping("/agsupport/applicationManager/bimCheck")
public class BimCheckV2Controller {

    @Autowired
    private IBimCheckService bimCheckService;

    @ApiOperation(value = "BIM审查pdf报告预览", notes = "BIM审查pdf报告预览接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cityName", value = "审查城市名称（报告名称）", dataType = "string"),
            @ApiImplicitParam(name = "projectName", value = "项目名称", dataType = "string"),
            @ApiImplicitParam(name = "designCompany", value = "设计单位", dataType = "string"),
            @ApiImplicitParam(name = "checkResultList", value = "BIM审查结果列表", dataType = "List"),
    }
    )
    @RequestMapping(value = "/preview", method = RequestMethod.POST)
    public ContentResultForm preview(@RequestBody Map<String, Object> metaMap, HttpServletResponse response) {
        String cityName = (String) metaMap.get("cityName");
        String projectName = (String) metaMap.get("projectName");
        String designCompany = (String) metaMap.get("designCompany");
        List<String> checkResultList = (List<String>) metaMap.get("checkResultList");
        String bimPdfPath = bimCheckService.preview(cityName, projectName, designCompany, checkResultList);
        return new ContentResultForm(true,bimPdfPath,"生成pdf报告成功！");
    }

    @ApiOperation(value = "BIM pdf报告下载", notes = "BIM pdf报告下载接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filePath", value = "文件路径", dataType = "string",required = true),
    }
    )
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void  download(String filePath, HttpServletRequest request,HttpServletResponse response) {
        bimCheckService.download(filePath,request,response);
    }

    @ApiOperation(value = "BIM审查统计数据", notes = "BIM审查统计数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramType", value = "paramType=1统计excel表格H1-L4的数据（参数excelFile）；paramType=2 统计BIM审查信息；", dataType = "string"),
            @ApiImplicitParam(name = "excelFile", value = "paramType=1,需要统计的excel文件", dataType = "string"),
            @ApiImplicitParam(name = "sourceId", value = "paramType=2,来源id", dataType = "string"),

    }
    )
    @RequestMapping(value = "/statistics", method = RequestMethod.POST)
    public ResultForm statistics(String paramType, MultipartFile excelFile, String sourceId) {
        if("1".equals(paramType)){
            List<ExcelResponseDomain> list = bimCheckService.statisticsExcel(excelFile);
            return new ContentResultForm(true, list, "获取成功");
        }
        if("2".equals(paramType)){
            if(StringUtils.isEmpty(sourceId)){
                return new ResultForm(false,"sourceId参数不能为空");
            }
            Map<String, Object> map = bimCheckService.statisticsBimCheck(sourceId);
            return new ContentResultForm<>(true, map, "查询成功");
        }
        return new ResultForm(false,"paramType参数错误");
    }

    @ApiOperation(value = "BIM审查数据添加", notes = "BIM审查数据添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResultForm add(AgBimCheck bimCheck) {
        try{
            bimCheckService.add(bimCheck);
            return new ResultForm(true,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new ResultForm(false,"添加失败");
        }
    }

    @ApiOperation(value = "BIM审查数据修改", notes = "BIM审查数据修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResultForm update(AgBimCheck bimCheck) {
        try{
            bimCheckService.update(bimCheck);
            return new ResultForm(true,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new ResultForm(false,"修改失败");
        }
    }

    @ApiOperation(value = "BIM审查数据删除", notes = "BIM审查数据删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "多个用逗号拼接", dataType = "string"),
         }
    )
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResultForm delete(String ids) {
        try{
            bimCheckService.delete(ids);
            return new ResultForm(true,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new ResultForm(false,"删除失败");
        }
    }

    @ApiOperation(value = "BIM审查数据查询", notes = "BIM审查数据查询")
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "规范分类（多个用逗号分隔）", dataType = "String"),
            @ApiImplicitParam(name = "classificationType", value = "规范类型（多个用逗号分隔）", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "分页参数:page=1&rows=10", dataType = "int"),
    })
    public ResultForm find(AgBimCheck bimCheck, Page page) {
        try{
            PageInfo<AgBimCheck> agBimCheckPageInfo = bimCheckService.find(bimCheck, page);
            return new ContentResultForm<>(true, PageHelper.toEasyuiPageInfo(agBimCheckPageInfo),"查询成功");
        }catch (Exception e){
            e.printStackTrace();
            return new ResultForm(false,"查询失败");
        }
    }



}
