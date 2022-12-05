package com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.controller;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsProject;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.service.IAgWidgetAssetsProjectService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author: Zihui Li
 * @Date: 2020/11/18
 * @tips:
 */

@Api(value = "微件资产相关接口", description = "微件资产相关接口")
@RestController
@RequestMapping("/agsupport/configManager/widgetAssetsProject")
public class AgWidgetAssetsProjectController {
    private static final Logger logger = LoggerFactory.getLogger(AgWidgetAssetsProjectController.class);

    @Autowired
    private IAgWidgetAssetsProjectService thematicProjectService;

    @RequestMapping("/index.html")
    @ApiIgnore
    public ModelAndView index(Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/widgetAssets/index");
    }


    @PostMapping("/update")
    @ApiOperation(value = "添加微件资产项目", notes = "添加微件资产项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agThematicProject", value = "微件资产项目", dataType = "Object")
    })
    public ContentResultForm update(AgWidgetAssetsProject agThematicProject){
        try {
            if (agThematicProject!=null && agThematicProject.getUniqueIdf().trim()!=null){
                thematicProjectService.updateProject(agThematicProject);
                return new ContentResultForm(true, null,"修改成功");
            }
            else {
                return new ContentResultForm(false, null,  "唯一标识不能为空!");
            }
        } catch (SourceException se){
            logger.info(se.getMessage());
            return new ContentResultForm(false, null, se.getMessage());
        } catch (Exception e) {
            logger.info("-----微件资产项目修改失败-----" + e.getMessage());
            return new ContentResultForm(false, null,  "微件资产项目修改失败");
        }
    }

    @GetMapping("/get")
    @ApiOperation(value = "获取项目编号对应的项目", notes = "获取项目编号对应的项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appSoftId", value = "项目编号", dataType = "String")
    })
    public ContentResultForm get(String appSoftId){
        try {
            if (StringUtils.isNotEmpty(appSoftId)){
                AgWidgetAssetsProject project = thematicProjectService.getProject(appSoftId);
                return new ContentResultForm(true,project,"查询成功");
            }
            else {
                return new ContentResultForm(false,null,"appSoftId should not be empty! ");
            }
        }catch (Exception e){
            logger.info("-----查询失败-----" + e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }

    // Used for tests, delete this method later.

//    @GetMapping("/test")
//    public ContentResultForm test(String softCode){
//        try{
//            if (StringUtils.isEmpty(softCode)){
//                return new ContentResultForm(false,null,"softCode should not be empty");
//            }
//            String uniqueIdf = thematicProjectService.getUniqueIdfBySoftCode(softCode);
//            if (StringUtils.isEmpty(uniqueIdf)){
//                return new ContentResultForm(false,null,"No such uniqueIdf");
//            }
//            return new ContentResultForm(true,uniqueIdf);
//        }catch (Exception e){
//            return new ContentResultForm(false,null,e.getMessage());
//        }
//    }

}
