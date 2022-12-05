package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.controller;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckProject;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.IAgBimCheckProjectService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: zihui li
 * @Date: 2020/11
 * @Description:
 */
@Api(value = "BIM审查项目接口", description = "BIM审查项目接口")
@RestController
@RequestMapping("/agsupport/bimCheck/project")
public class AgBimCheckProjectV3Controller {
    private static final Logger logger = LoggerFactory.getLogger(AgBimCheckProjectV3Controller.class);

    @Autowired
    private IAgBimCheckProjectService bimCheckProjectService;

    @GetMapping("/find")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramType", value = "1:单独返回项目列表（无模型）；2：返回包含模型列表的项目列表（有模型）；3：返回包含模型列表的项目列表（模型数据来源服务内容表）", dataType = "String",defaultValue = "1",required = true)
    })
    @ApiOperation(value = "BIM审查项目列表", notes = "BIM审查项目列表")
    public ContentResultForm find(@RequestParam(value = "paramType",defaultValue = "1") String paramType) {
        try {
			List<AgBimCheckProject> list = bimCheckProjectService.findAll(paramType);
            return new ContentResultForm(true, list);
        } catch (Exception e) {
            logger.info("-----BIM审查项目获取失败-----" + e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加BIM审查项目", notes = "添加BIM审查项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agBimCheckProject", value = "BIM审查项目", dataType = "Object")
    })
    public ContentResultForm add(AgBimCheckProject agBimCheckProject) {
        try {
            if (agBimCheckProject!=null && agBimCheckProject.getName().trim()!=null){
                String id = bimCheckProjectService.addProject(agBimCheckProject);
                return new ContentResultForm(true, id,"添加成功");
            }
            else {
                return new ContentResultForm(false, null,  "Project name should not be empty!");
            }
        } catch (Exception e) {
            logger.info("-----BIM审查项目添加失败-----" + e.getMessage());
            return new ContentResultForm(false, null,  e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改BIM审查项目", notes = "修改BIM审查项目接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agBimCheckProject",value = "BIM审查项目", dataType = "Object")
    })
    public ContentResultForm update(AgBimCheckProject agBimCheckProject){
        try {
            if (agBimCheckProject == null){
                return new ContentResultForm(false, null,"请选择数据");
            }
            if(agBimCheckProject.getId() == null){
                return new ContentResultForm(false, null,"请选择数据");
            }
            if(agBimCheckProject.getName().trim()!=null){
                bimCheckProjectService.updateProject(agBimCheckProject);
                return new ContentResultForm(true, "修改成功");
            }
            else {
                return new ContentResultForm(false, null,"Project name should not be empty!");
            }
        }catch (Exception e){
            logger.info("-----BIM审查项目更新失败-----" + e.getMessage());
            return new ContentResultForm(false, null,"修改失败");
        }
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除BIM审查项目", notes = "删除BIM审查项目接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "删除的BIM审查项目id", dataType = "String", required = true)
    })
    public ContentResultForm delete(String id){
        try{
            if(id == null){
                return new ContentResultForm(false, null,"请选择数据");
            }
            bimCheckProjectService.deleteProject(id);
            return new ContentResultForm(true, null,"删除成功");
        }catch (Exception e){
            logger.info("-----BIM审查项目删除失败-----" + e.getMessage());
            return new ContentResultForm(false, null,"删除失败");
        }
    }

}
