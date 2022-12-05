package com.augurit.agcloud.agcom.agsupport.sc.bimfile.controller;

import com.augurit.agcloud.agcom.agsupport.common.aspect.RestLog;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimProject;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IAgBimProject;
import com.augurit.agcloud.agcom.syslog.service.annotation.SysLog;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 项目Conroller
 * Created by fanghh on 2020/3/18.
 */
@RestController
@Api(value = "BIM项目接口",description = "BIM项目相关接口")
@RequestMapping("agsupport/bimproject")
public class AgBimProjectController {

    @Autowired
    private IAgBimProject iAgBimProject;


    @RestLog("获取BIM项目树")
    @GetMapping("/findTree")
    @ApiOperation(value = "获取BIM项目树",notes = "获取BIM项目树")
    public ContentResultForm findTree(){
        List<AgBimProject> tree = iAgBimProject.findTree();
        return new ContentResultForm(true,tree);
    }
}
