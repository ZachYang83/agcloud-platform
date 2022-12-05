package com.augurit.agcloud.agcom.agsupport.sc.problemDiscern.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgProblemDiscern;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.problemDiscern.service.IAgProblemDiscernService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: libc
 * @Description: 问题识别模块-控制器
 * @Date: 2020/8/28 13:56
 * @Version: 1.0
 */
@Api(value = "问题识别", description = "问题识别相关接口")
@RestController
@RequestMapping("/agsupport/problemDiscern")
public class AgProblemDiscernController {
    @Autowired
    private IAgProblemDiscernService problemDiscernService;


    /**
     * 跳转UI对接的静态页面 index.html
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/index.html")
    public ModelAndView index() throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/problemDiscern/index");
    }


    @ApiOperation(value = "分页获取问题识别", notes = "分页获取问题识别接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "description", value = "描述", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页信息")
    })
    @RequestMapping(value = "/findList", method = RequestMethod.GET)
    public ContentResultForm findList(String description, Page page) {
        PageInfo<AgProblemDiscern> pageInfo = problemDiscernService.findList(description, page);
        return new ContentResultForm<EasyuiPageInfo>(true, PageHelper.toEasyuiPageInfo(pageInfo));
    }


    @ApiOperation(value = "保存问题识别", notes = "保存问题识别接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemType", value = "1:BIM审查；2：标签管理", dataType = "String",defaultValue = "1",required = true)
    })
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultForm save(AgProblemDiscern problemDiscern, String problemType, MultipartFile file) {
        try {
            problemDiscernService.save(problemDiscern, problemType, file);
            return new ResultForm(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    @ApiOperation(value = "删除问题识别", notes = "删除问题识别接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "问题识别id", dataType = "String", paramType = "path", required = true)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResultForm deleteById(@PathVariable("id") String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                return new ResultForm(false, "id不能为空");
            }
            problemDiscernService.deleteById(id);
            return new ResultForm(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "删除失败");
        }
    }

    @ApiOperation(value = "批量删除问题识别", notes = "批量删除问题识别接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "删除的ids字符串", dataType = "String")
    })
    @RequestMapping(value = "/deleteByIds", method = RequestMethod.DELETE)
    public ResultForm deleteByIds(String ids) {
        try {
            if (StringUtils.isEmpty(ids)) {
                return new ResultForm(false, "ids不能为空或参数类型传递错误");
            }
            // 封装pIds数组
            String[] pIds = ids.split(",");

            problemDiscernService.deleteByIds(pIds);
            return new ResultForm(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "删除失败");
        }
    }
}
