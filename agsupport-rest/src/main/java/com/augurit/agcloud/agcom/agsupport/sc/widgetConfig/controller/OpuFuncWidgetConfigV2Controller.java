package com.augurit.agcloud.agcom.agsupport.sc.widgetConfig.controller;

import com.augurit.agcloud.agcom.agsupport.domain.OpuFuncWidgetConfig;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.widgetConfig.service.IOpuFuncWidgetConfigService;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.opus.common.domain.OpuRsFunc;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Api(value = "微件配置管理",description = "微件配置信息相关接口")
@RestController
@RequestMapping("/agsupport/configManager/widgetConfig")
public class OpuFuncWidgetConfigV2Controller {

    @Autowired
    private IOpuFuncWidgetConfigService opuFuncWidgetConfigService;


    @ApiOperation(value = "获取微件配置信息列表",notes = "获取微件配置信息列表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code" ,value = "微件编码",dataType = "String"),
            @ApiImplicitParam(name = "url" ,value = "微件访问地址",dataType = "String")
    })
    //@RequestMapping(value = "/getConfigList",method = RequestMethod.GET)
    @GetMapping(value = "/find")
    public ContentResultForm find(String url,String code) throws Exception{
        try{
            if(StringUtils.isNotEmpty(url) || StringUtils.isNotEmpty(code)){
                List<OpuFuncWidgetConfig> list=opuFuncWidgetConfigService.getConfigListByUrlOrCode(url,code);
                return new ContentResultForm(true, list);
            }else{
                return new ContentResultForm(false, "url or code cannot be empty");
            }
        }catch(Exception e){
            e.printStackTrace();
            return new ContentResultForm(false, null);
        }
    }

    @ApiOperation(value = "获取微件配置信息",notes = "获取微件配置信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code" ,value = "微件编码",dataType = "String"),
            @ApiImplicitParam(name = "key" ,value = "参数标识",dataType = "String",required = true),
            @ApiImplicitParam(name = "url" ,value = "微件访问地址",dataType = "String")
    })
    // @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    @GetMapping(value = "/get")
    public ContentResultForm get(String url,String code,String key) throws Exception{
        try{
            if(StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(key)){
                OpuFuncWidgetConfig owc=opuFuncWidgetConfigService.getConfigDataByKey(url,code,key);
                return new ContentResultForm(true, owc);
            }else{
                return new ContentResultForm(false, "url,code,key cannot be empty");
            }
        }catch(Exception e){
            e.printStackTrace();
            return new ContentResultForm(false, null);
        }
    }
}
