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
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Api(value = "微件配置管理",description = "微件配置信息相关接口")
@RestController
@RequestMapping("/agsupport/configManager/widgetConfig")
public class OpuFuncWidgetConfigController {

    public static void main(String[] args) {
        
    }

    @Autowired
    private IOpuFuncWidgetConfigService opuFuncWidgetConfigService;

//    private MongoFileOperationUtil mongoFileOperationUtil=MongoFileOperationUtil.getInstance();

    @RequestMapping("/index.html")
    public ModelAndView index() throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/widgetConfig/index1");
    }


    @ApiOperation(value = "分页获取微件信息",notes = "分页获取微件信息接口")
    @GetMapping(value = "/find")
    public ContentResultForm find(String name, Page page) throws Exception {
        PageInfo<OpuRsFunc> pageInfo=opuFuncWidgetConfigService.searchParam(name, page);
        return new ContentResultForm<EasyuiPageInfo>(true, PageHelper.toEasyuiPageInfo(pageInfo));
    }


    @ApiOperation(value = "保存微件配置信息",notes = "保存微件配置信息接口")
    @PostMapping(value = "/detail/add")
    public ResultForm addDetail(OpuFuncWidgetConfig opuFuncWidgetConfig){
        try{
            List<OpuFuncWidgetConfig> list=opuFuncWidgetConfigService.searchByParams(opuFuncWidgetConfig);
            if(list!=null && list.size()>0 && !list.get(0).getId().equals(opuFuncWidgetConfig.getId())){
                return new ResultForm(false, "参数标识重复！");
            }else{
                opuFuncWidgetConfigService.save(opuFuncWidgetConfig);
                return new ResultForm(true, "保存成功");
            }
        }catch(Exception e){
            e.printStackTrace();
            return new ResultForm(false, "保存失败");
        }
    }

    @ApiOperation(value = "修改微件配置信息启动状态",notes = "修改微件配置信息启动状态接口")
    @PutMapping(value = "/detail/update")
    public ResultForm updateDetail(String id,String isActive){
        try{
            OpuFuncWidgetConfig oc=opuFuncWidgetConfigService.selectByPrimaryKey(id);
            if(oc!=null){
                oc.setIsActive(isActive);
                opuFuncWidgetConfigService.updateByPrimaryKeySelective(oc);
            }
            return new ResultForm(true, "修改成功");
        }catch(Exception e){
            e.printStackTrace();
            return new ResultForm(false, "修改失败");
        }
    }


    @ApiOperation(value = "删除微件信息",notes = "删除微件信息接口")
    @DeleteMapping(value = "/delete")
    public ResultForm delete(String paramIds){
        try{
            if(StringUtils.isNotEmpty(paramIds)){
                String ids[] = null;
                ResultForm resultForm = null;
                if (org.apache.commons.lang.StringUtils.isNotEmpty(paramIds)) {
                    ids = paramIds.split(",");
                }
                opuFuncWidgetConfigService.deleteWidget(ids);
            }

            return new ResultForm(true, "删除成功");
        }catch(Exception e){
            e.printStackTrace();
            return new ResultForm(false, "删除失败");
        }
    }

    @ApiOperation(value = "删除微件配置信息",notes = "删除微件配置信息接口")
    @DeleteMapping(value = "/detail/delete")
    public ResultForm deleteDetail(String paramIds){
        try{
            if(StringUtils.isNotEmpty(paramIds)) {
                String ids[] = null;
                ResultForm resultForm = null;
                if (org.apache.commons.lang.StringUtils.isNotEmpty(paramIds)) {
                    ids = paramIds.split(",");
                }
                opuFuncWidgetConfigService.deleteConfigData(ids);
            }
            return new ResultForm(true, "删除成功");
        }catch(Exception e){
            e.printStackTrace();
            return new ResultForm(false, "删除失败");
        }
    }

    @ApiOperation(value = "获取微件配置信息",notes = "获取微件配置信息接口")
    @GetMapping(value = "/detail/get")
    public ContentResultForm getDetail(String funcCode) throws Exception{
        try{
            if(StringUtils.isNotEmpty(funcCode)){
                List<OpuFuncWidgetConfig> list=opuFuncWidgetConfigService.getConfigData(funcCode);
                return new ContentResultForm(true, list);
            }else{
                return new ContentResultForm(false, null);
            }
        }catch(Exception e){
            e.printStackTrace();
            return new ContentResultForm(false, null);
        }
    }
    @ApiOperation(value = "批量保存微件配置信息",notes = "批量保存微件配置信息接口")
    @RequestMapping(value = "/saveConfigData")
    public ResultForm saveConfigData(List<OpuFuncWidgetConfig> dataList){
        try{
            opuFuncWidgetConfigService.saveDataList(dataList);
            return new ResultForm(true,"保存成功");
        }catch (Exception e){
            e.printStackTrace();
            return new ResultForm(false,"保存失败");
        }
    }

    @ApiOperation(value = "保存微件信息",notes = "保存微件信息接口")
    //@RequestMapping(value = "/add")
    @PostMapping(value = "/add")
    public ResultForm add(String funcId,String funcName){
        try{
            opuFuncWidgetConfigService.saveWidget(funcId,funcName);
            return new ResultForm(true,"保存成功");
        }catch (Exception e){
            e.printStackTrace();
            return new ResultForm(false,"保存失败");
        }
    }
}
