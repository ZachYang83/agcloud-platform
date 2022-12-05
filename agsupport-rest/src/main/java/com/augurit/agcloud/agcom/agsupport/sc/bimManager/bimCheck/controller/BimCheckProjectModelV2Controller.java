package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.controller;

import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.IBimCheckProjectModelService;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: libc
 * @Description: BIM审查项目模型-控制器
 * @Date: 2020/11/2 18:11
 * @Version: 1.0
 */
@Api(value = "BIM审查项目模型管理", description = "BIM审查项目模型管理接口")
@RestController
@RequestMapping("/agsupport/applicationManager/bimCheckProjectModel")
public class BimCheckProjectModelV2Controller {

    @Autowired
    private IBimCheckProjectModelService bimCheckProjectModelService;

    @ApiOperation(value = "新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bimCheckProjectId", value = "BIM审查项目Id", dataType = "String"),
            @ApiImplicitParam(name = "file", value = "上传zip文件", dataType = "MultipartFile"),
            @ApiImplicitParam(name = "agcim3dprojectName", value = "BIM审查项目名称('BIM审查')", dataType = "String",required = true)
    })
    @PostMapping(value = "/add")
    public ResultForm add(String bimCheckProjectId, @RequestParam("file") MultipartFile file, @RequestParam(defaultValue = "BIM审查") String agcim3dprojectName) {
        bimCheckProjectModelService.add(bimCheckProjectId,file,agcim3dprojectName);
        return new ResultForm(true, "添加成功");
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    })
    @DeleteMapping(value = "/delete")
    public ResultForm delete(String id) {
        bimCheckProjectModelService.delete(id);
        return new ResultForm(true, "删除成功");
    }


}
