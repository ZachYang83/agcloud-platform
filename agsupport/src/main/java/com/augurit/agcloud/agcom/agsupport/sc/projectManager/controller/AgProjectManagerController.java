package com.augurit.agcloud.agcom.agsupport.sc.projectManager.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgProjectManager;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.projectManager.service.IAgProjectManagerService;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: libc
 * @Description: 工程管理 控制类
 * @Date: 2020/7/14 15:04
 * @Version: 1.0
 */
@Api(value = "工程管理", description = "工程管理相关接口")
@RestController
@RequestMapping("/agsupport/configManager/projectConfig")
public class AgProjectManagerController {

    @Autowired
    private IAgProjectManagerService projectManagerService;


    /**
     * 跳转UI对接的静态页面 index.html
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/index.html")
    public ModelAndView index() throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/projectManager/index");
    }


    @ApiOperation(value = "分页获取工程信息", notes = "分页获取工程信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "工程名称", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页信息")
    })
    @GetMapping(value = "/find")
    public ContentResultForm find(String name, Page page) {
        PageInfo<AgProjectManager> pageInfo = projectManagerService.findList(name, page);
        return new ContentResultForm<EasyuiPageInfo>(true, PageHelper.toEasyuiPageInfo(pageInfo));
    }


    @ApiOperation(value = "保存工程信息", notes = "保存工程信息接口")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResultForm add(AgProjectManager agProjectManager, HttpServletRequest request) {
        try {
            projectManagerService.save(agProjectManager, request);
            return new ResultForm(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

//    @ApiOperation(value = "删除工程信息", notes = "删除工程信息接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "工程id", dataType = "String", paramType = "path", required = true)
//    })
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//    public ResultForm deleteProject(@PathVariable("id") String id) {
//        try {
//            if (StringUtils.isEmpty(id)) {
//                return new ResultForm(false, "id不能为空");
//            }
//            projectManagerService.deleteProject(id);
//            return new ResultForm(true, "删除成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResultForm(false, "删除失败");
//        }
//    }

    @ApiOperation(value = "删除/批量删除工程信息", notes = "删除/批量删除工程信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "删除的ids字符串", dataType = "String")
    })
    //@RequestMapping(value = "/deleteProjectData",method = RequestMethod.DELETE)
    @DeleteMapping(value = "/delete")
    public ResultForm delete(String ids) {
        try {
            if (StringUtils.isEmpty(ids)) {
                return new ResultForm(false, "ids不能为空或参数类型传递错误");
            }
            // 封装pIds数组
            String[] pIds = ids.split(",");

            projectManagerService.deleteProjectData(pIds);
            return new ResultForm(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "删除失败");
        }
    }


    @ApiOperation(value = "批量保存工程信息", notes = "批量保存工程信息接口")
    @RequestMapping(value = "/saveProjectData")
    public ResultForm saveProjectData(List<AgProjectManager> dataList) {
        try {
            projectManagerService.saveDataList(dataList);
            return new ResultForm(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "保存失败");
        }
    }


}
