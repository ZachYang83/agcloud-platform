package com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgBuildingComponent;
import com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.service.IAgBuildingComponentService;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
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

import java.util.List;

/**
 * @Version  1.0
 * @Author libc
 * @Description 构件分类管理-控制器
 * @Date 2020/9/3 14:52
 */
@Api(value = "构件分类管理", description = "构件分类管理相关接口")
@RestController
@RequestMapping("/agsupport/buildingComponent")
public class AgBuildingComponentV1Controller {


    @Autowired
    private IAgBuildingComponentService buildingComponentService;


    /**
     * 跳转UI对接的静态页面 index.html
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/index.html")
    public ModelAndView index() throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/buildingComponent/index");
    }


    @ApiOperation(value = "分页获取构件分类信息", notes = "分页获取构件分类信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "构件分类名称", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页信息")
    })
    @RequestMapping(value = "/findList", method = RequestMethod.GET)
    public ContentResultForm findList(String name, Page page) {
        PageInfo<AgBuildingComponent> pageInfo = buildingComponentService.findList(name, page);
        return new ContentResultForm<EasyuiPageInfo>(true, PageHelper.toEasyuiPageInfo(pageInfo));
    }


    @ApiOperation(value = "保存构件分类信息", notes = "保存构件分类信息接口")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResultForm save(AgBuildingComponent agBuildingComponent) {
        try {
            buildingComponentService.save(agBuildingComponent);
            return new ResultForm(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    @ApiOperation(value = "删除构件分类信息", notes = "删除构件分类信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "构件分类id", dataType = "String", paramType = "path", required = true)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResultForm deleteById(@PathVariable("id") String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                return new ResultForm(false, "id不能为空");
            }
            buildingComponentService.deleteById(id);
            return new ResultForm(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "删除失败");
        }
    }

    @ApiOperation(value = "批量删除构件分类信息", notes = "批量删除构件分类信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "删除的ids字符串", dataType = "String")
    })
    @RequestMapping(value = "/deleteByIds",method = RequestMethod.DELETE)
    public ResultForm deleteByIds(String ids) {
        try {
            if (StringUtils.isEmpty(ids)) {
                return new ResultForm(false, "ids不能为空或参数类型传递错误");
            }
            // 封装pIds数组
            String[] pIds = ids.split(",");

            buildingComponentService.deleteByIds(pIds);
            return new ResultForm(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "删除失败");
        }
    }

    @ApiOperation(value = "excel表格批量导入构件分类", notes = "excel表格批量导入构件分类接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "excel文件", dataType = "String")
    })
    @RequestMapping(value = "/import",method = RequestMethod.POST)
    public ResultForm excelImport(MultipartFile file) {
        try {
            buildingComponentService.excelImport(file);
            return new ResultForm(true, "导入成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "导入失败");
        }
    }


    @ApiOperation(value = "根据条件获取构件分类信息", notes = "根据条件获取构件分类信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableCode", value = "表代码(两位阿拉伯数字：01)",  dataType = "string"),
            @ApiImplicitParam(name = "largeCode", value = "大类代码(两位阿拉伯数字：01)", dataType = "string"),
            @ApiImplicitParam(name = "mediumCode", value = "中类代码(两位阿拉伯数字：01)", dataType = "string"),
            @ApiImplicitParam(name = "smallCode", value = "小类代码(两位阿拉伯数字：01)", dataType = "string"),
            @ApiImplicitParam(name = "detailCode", value = "细类代码(两位阿拉伯数字：01)",  dataType = "string"),
            @ApiImplicitParam(name = "name", value = "类目名称（中文/英文）",dataType = "string"),
            @ApiImplicitParam(name = "filterType", value = "过滤查询类型（1：大类、2：中类、3：小类、4：细类）",dataType = "string")

    }
    )
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ContentResultForm<List<AgBuildingComponent>> get(String tableCode, String largeCode, String mediumCode, String smallCode, String detailCode, String name, String filterType) {
        try {
            List<AgBuildingComponent> bcList = buildingComponentService.listByParam(tableCode,largeCode,mediumCode,smallCode,detailCode,name,filterType);
            return new ContentResultForm(true, bcList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null);
        }
    }


}
