package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.controller;

import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.domain.Agcim3dprojectCustom;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service.IAgcim3dprojectService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: libc
 * @Description: 3d bim 项目工程控制器
 * @Date: 2020/12/10 16:26
 * @Version: 1.0
 */
@RestController
@RequestMapping("/agsupport/bimParamethy/project")
@Api(value = "agcim3dproject BIM项目管理", description = "agcim3dproject BIM项目管理接口")
public class Agcim3dprojectV3Controller {

    @Autowired
    private IAgcim3dprojectService agcim3dprojectService;

    /**
     * @return ResultForm
     * @Author: libc
     * @Date: 2020/12/10 16:30
     * @tips: 获取BIM项目树结构列表
     */
    @GetMapping("/tree")
    @ApiOperation(value = "BIM项目树结构列表", notes = "BIM项目树结构列表接口")
    @ApiImplicitParams({

    })
    public ResultForm tree() {
        List<Agcim3dprojectCustom> list = agcim3dprojectService.tree();
        return new ContentResultForm(true, list, "查询成功");
    }
}
