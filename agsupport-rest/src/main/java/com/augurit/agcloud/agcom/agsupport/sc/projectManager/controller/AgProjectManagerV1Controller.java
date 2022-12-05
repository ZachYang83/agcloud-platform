package com.augurit.agcloud.agcom.agsupport.sc.projectManager.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgProjectManager;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.projectManager.service.AgProjectManagerService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: libc
 * @Description: 工程管理 控制类
 * @Date: 2020/7/14 15:04
 * @Version: 1.0
 */
@Api(value = "工程管理", description = "工程管理相关接口")
@RestController
@RequestMapping("/agsupport/projectManager")
public class AgProjectManagerV1Controller {

    @Autowired
    private AgProjectManagerService projectManagerService;


   /* @ApiOperation(value = "获取所有工程信息", notes = "获取所有工程信息接口")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ContentResultForm findAll() {
        try {
            List<AgProjectManager> list = projectManagerService.findAll();
            return new ContentResultForm(true, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null);
        }
    }*/

    @ApiOperation(value = "获取工程配置信息", notes = "获取工程配置信息接口")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "param",value = "查询条件参数",required = true,dataType = "string")
    )
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ContentResultForm get(@RequestParam Map<String,String> param) {
        try {
            if (MapUtils.isEmpty(param)){
                return new ContentResultForm(false, null,"参数不能为空");
            }
            AgProjectManager pm = projectManagerService.findByParam(param);
            return new ContentResultForm(true, pm.getExtendData());
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null);
        }
    }


}
