package com.augurit.agcloud.agcom.agsupport.sc.elasticsearch.controller;

import com.augurit.agcloud.agcom.agsupport.util.ExcelUtil;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;


/**
 * @Author:
 * @Description: 自定义词和同义词Excel模板下载
 * @Date:created in :11:02 2019/3/15
 * @Modified By:
 */
@Api(value = "自定义词和同义词Excel模板下载接口",description = "自定义词和同义词Excel模板下载接口")
@RestController
@RequestMapping("/agsupport/wordExcelDown")
public class AgExcelDownController {

    @ApiOperation(value = "自定义词和同义词Excel模板下载",notes = "自定义词和同义词Excel模板下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "excelType" ,value = "模板类型：custom或者synonym",dataType = "String")
    })
    @RequestMapping(value = "/customOrSynonword",method = RequestMethod.POST)
    public void customOrSynonword(String excelType, HttpServletResponse response){
        String excelPath = System.getProperty("user.dir")+ File.separator+"temp"+ File.separator+"excel"+ File.separator;
        String fileName = "";
        if ("custom".equals(excelType)){
            fileName = "自定义词模板.xls";
            excelPath +="自定义词模板.xls";
        }else if ("synonym".equals(excelType)){
            fileName = "同义词模板.xls";
            excelPath +="同义词模板.xls";
        }
        File excelFile = new File(excelPath);
        if (!excelFile.exists() || !excelFile.isFile()) {
            System.out.print("模板文件不存在!");
        }
        try {
            ExcelUtil.down(excelFile,fileName,response);
        } catch (Exception e) {
            System.out.print("下载失败!");
        }
    }
}
