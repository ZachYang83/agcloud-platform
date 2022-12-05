package com.augurit.agcloud.agcom.agsupport.sc.stylemanager.controller;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgStyleManager;
import com.augurit.agcloud.agcom.agsupport.sc.stylemanager.service.IAgStyleManagerService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/07
 * @Description:
 */
@Api(value = "样式管理", description = "样式管理相关接口")
@RestController
@RequestMapping("/agsupport/stylemanager")
public class AgStyleManagerV1Controller {

    @Autowired
    private IAgStyleManagerService styleManagerService;


    @GetMapping("/findById")
    @ApiOperation(value = "查询样式", notes = "通过id查询样式接口")
    public ContentResultForm findById(@ApiParam(value = "样式主键")String id) {
        AgStyleManager agStyleManager = styleManagerService.findById(id);
        return new ContentResultForm(true, agStyleManager);
    }
}
