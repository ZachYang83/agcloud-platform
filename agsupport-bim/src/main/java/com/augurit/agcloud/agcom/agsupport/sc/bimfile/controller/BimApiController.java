package com.augurit.agcloud.agcom.agsupport.sc.bimfile.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimApi;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimApi;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: BimApiController
 * @Description: 第三方接口管理
 * @Author: zhangsj
 * @Date: Create in 2020/03/23 10:52
 **/

@RestController
@RequestMapping(value = "/agsupport/agbimapi")
@Api(value = "第三方接口管理",description = "BIM管理相关接口")
public class BimApiController {

    @Autowired
    IBimApi iBimApi;

    /**
     * 样式配置页面
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/index.html")
    public ModelAndView index(Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupportbim/bimfile/index");
    }

    /**
     * 获取一条信息
     * @param id
     * @return
     */
    @GetMapping("/getById")
    @ApiOperation(value = "获取一条信息")
    @ApiParam(value = "接口Id")
    public ContentResultForm getById(@RequestParam(defaultValue = "")String id){
        try{
            if (StringUtils.isBlank(id)){
                return new ContentResultForm(false,"接口id为空!");
            }
            AgBimApi agBimApi = iBimApi.getById(id);
            if (null == agBimApi){
                return new ContentResultForm(true,"暂无数据!");
            }
            return new ContentResultForm(true,agBimApi, "查询成功!");
        }catch (Exception e){
            return new ContentResultForm(false,e,e.getMessage());
        }
    }

    /**
     * 获取所有接口
     * @return
     */
    @GetMapping("/getAll")
    @ApiOperation(value = "获取所有接口")
    public Object getAll(){
        try{
            List<AgBimApi> agBimApiList = iBimApi.getAll();
            if (null == agBimApiList || agBimApiList.size() == 0){
                return new ContentResultForm(true,null,"暂无数据!");
            }return agBimApiList;
        }catch (Exception e){
            return new ContentResultForm(false,e,e.getMessage());
        }
    }

    /**
     * 删除一条接口数据
     * @param id
     * @return
     */
    @DeleteMapping("/deleteById")
    @ApiOperation(value = "删除一条接口数据")
    public ContentResultForm deleteById(@ApiParam(value = "接口Id")String id){
        try{
            if (StringUtils.isBlank(id)){
                return new ContentResultForm(false, null, "接口Id为空!");
            }
            boolean flag = iBimApi.deleteById(id);
            if (!flag){
                return new ContentResultForm(false, null, "删除失败!");
            }
            return new ContentResultForm(true, null, "删除成功!");
        }catch (Exception e){
            return new ContentResultForm(false, e, e.getMessage());
        }
    }

    /**
     * 删除更多信息
     * @param paramIds
     * @return
     */
    @DeleteMapping("/deleteMany")
    @ApiOperation(value = "删除更多信息")
    public ContentResultForm deleteMany(@ApiParam("接口Id")String paramIds){
        try{
            if (StringUtils.isBlank(paramIds)){
                return new ContentResultForm(false, null, "请求参数异常");
            }
            List<String> idsList = Arrays.asList(paramIds.split(","));
            boolean flag = iBimApi.deleteMany(idsList);
            if (!flag){
                return new ContentResultForm(false, null, "删除失败!");
            }
            return new ContentResultForm(true, null, "删除成功!");
        } catch (Exception e) {
            return new ContentResultForm(false, e, e.getMessage());
        }
    }

    /**
     * 添加一条接口信息
     * @param agBimApi
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加一条接口信息")
    public ContentResultForm save(@ApiParam(value = "接口信息") AgBimApi agBimApi){
        try{
            agBimApi.setId(UUID.randomUUID().toString());
            agBimApi.setCreateTime(new Date());
            boolean flag = iBimApi.addAgBimApi(agBimApi);
            if (!flag){
                return new ContentResultForm(false, null, "添加失败!");
            }
            return new ContentResultForm(true, agBimApi.getId(), "添加成功!");
        }catch (Exception e){
            return new ContentResultForm(false, e, e.getMessage());
        }
    }

    /**
     * 编辑一条信息
     *
     * @param agBimApi
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "编辑一条信息")
    public ContentResultForm update(@ApiParam(value = "json信息") AgBimApi agBimApi) {
        try {
            agBimApi.setCreateTime(null);
            boolean falg = iBimApi.update(agBimApi);
            if (!falg) {
                return new ContentResultForm(false, null, "编辑失败");
            }
            return new ContentResultForm(true, agBimApi.getId(), "编辑成功");
        } catch (Exception e) {
            return new ContentResultForm(false, e,e.getMessage());
        }
    }

    @ApiOperation(
            value = "分页获取数据",
            notes = "分页获取数据接口"
    )
    @ApiImplicitParams({@ApiImplicitParam(
            name = "agBimApi",
            required = false,
            value = "BIM第三方接口",
            dataType = "AgBimApi"
    ), @ApiImplicitParam(
            name = "page",
            required = true,
            value = "分页参数:/agsupport/agbimapi/getByNameOrUrl?page=1&rows=10&name=&url=",
            dataType = "Page"
    )})
    @RequestMapping(
            value = {"/getByNameOrUrl"},
            method = {RequestMethod.GET}
    )
    public ContentResultForm getByNameOrUrl(String name, String url, Page page) throws Exception {
        PageInfo<AgBimApi> pageInfo = iBimApi.getByNameOrUrl(name, url, page);
        EasyuiPageInfo<AgBimApi> result = PageHelper.toEasyuiPageInfo(pageInfo);
        return new ContentResultForm(true, result);
    }

}
