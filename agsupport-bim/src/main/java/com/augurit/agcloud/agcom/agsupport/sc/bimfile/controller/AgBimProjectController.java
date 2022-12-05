package com.augurit.agcloud.agcom.agsupport.sc.bimfile.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimProject;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IAgBimProject;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcom.common.LoginHelpClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/agsupport/bimproject")
@Api(value = "BIM项目管理",description = "BIM项目管理相关接口")
public class AgBimProjectController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AgBimProjectController.class);

    @Autowired
    private IAgBimProject iAgBimProject;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/findAll")
    @ApiOperation(value = "获取所有数据")
    public ContentResultForm findAll() {
        List<AgBimProject> projectList = iAgBimProject.findAll();
        return new ContentResultForm(true,projectList);
    }

    @GetMapping("/find/{id}")
    @ApiOperation(value = "根据id获取数据")
    public ContentResultForm find(@PathVariable("id") String id) {
        if(StringUtils.isBlank(id)){
            return new ContentResultForm(false,null,"id为空");
        }
        AgBimProject agBimProject = iAgBimProject.find(id);
        return new ContentResultForm(true,agBimProject);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存数据")
    public ContentResultForm save(AgBimProject project) {
        project.setId(UUID.randomUUID().toString());
        String loginName = LoginHelpClient.getLoginName(request);
        project.setCreateName(loginName);
        project.setCreateTime(new Date());
        iAgBimProject.save(project);
        return new ContentResultForm(true);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新数据")
    public ContentResultForm update(AgBimProject project) {
        if(StringUtils.isBlank(project.getId())){
            return new ContentResultForm(false,null,"id为空");
        }
        iAgBimProject.update(project);
        return new ContentResultForm(true);
    }


    @DeleteMapping("/delete/{id}")
    public ContentResultForm delete(@PathVariable("id")String id){
        if(StringUtils.isBlank(id)){
            return new ContentResultForm(false,null,"id为空");
        }
        iAgBimProject.delete(id);
        return new ContentResultForm(true);
    }


}
