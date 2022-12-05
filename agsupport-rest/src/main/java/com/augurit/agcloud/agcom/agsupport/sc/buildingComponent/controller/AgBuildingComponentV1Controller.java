package com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgBuildingComponent;
import com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.service.IAgBuildingComponentService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Version 1.0
 * @Author libc
 * @Description 构件分类-控制器
 * @Date 2020/9/3 14:52
 */
@Api(value = "构件分类管理", description = "构件分类管理相关接口")
@RestController
@RequestMapping("/agsupport/buildingComponent")
public class AgBuildingComponentV1Controller {


    @Autowired
    private IAgBuildingComponentService buildingComponentService;


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
    public ContentResultForm<List<AgBuildingComponent>> get(String tableCode,String largeCode,String mediumCode,String smallCode,String detailCode,String name,String filterType) {
        try {
            List<AgBuildingComponent> bcList = buildingComponentService.listByParam(tableCode,largeCode,mediumCode,smallCode,detailCode,name,filterType);
            return new ContentResultForm(true, bcList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null);
        }
    }


}
