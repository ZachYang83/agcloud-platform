package com.augurit.agcloud.agcom.agsupport.sc.monitor.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgDefaultValue;
import com.augurit.agcloud.agcom.agsupport.domain.AgServicesMonitor;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.agcom.agsupport.sc.defaultvalue.service.IAgDefaultValue;
import com.augurit.agcloud.agcom.agsupport.sc.monitor.service.IAgServicesMonitor;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :11:15 2018/10/30
 * @Modified By:
 */
@Api(value = "服务监控管理接口",description = "服务监控管理接口")
@RestController
@RequestMapping("/agsupport/monitor")
public class ServiceMonitorController {

    @Autowired
    private IAgServicesMonitor iAgServicesMonitor;

    @Autowired
    private IAgDefaultValue iAgDefaultValue;

    @RequestMapping("/index.html")
    public ModelAndView index(Model model) throws Exception {
        String globalMinotorStatus = "";
        AgDefaultValue agDefaultValue = iAgDefaultValue.findByKey("globalMonitorStatus");
        if (agDefaultValue != null) {
            globalMinotorStatus = JSONObject.fromObject(agDefaultValue.getDefaultValue()).getString("monitorStatus");
        }
        model.addAttribute("globalMinotorStatus",globalMinotorStatus);
        return new ModelAndView("agcloud/agcom/agsupport/monitor/index");
    }

    /**
     * 获取监控列表
     * @param agServicesMonitor
     * @param page
     * @param refresh 是否刷新
     * @return
     * @throws Exception
     */

    @ApiOperation(value = "分页获取监控列表",notes = "分页获取监控列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agServicesMonitor",required = true, value = "监控信息对象", dataType = "AgServicesMonitor"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:/agsupport/param/paramList?page=1&rows=10&name=", dataType = "Page"),
            @ApiImplicitParam(name = "refresh",required = true, value = "是否刷新当前页监控列表", dataType = "boolean")
    })
    @RequestMapping(value = "/monitorList",method = RequestMethod.POST)
    public ContentResultForm monitorList(AgServicesMonitor agServicesMonitor, Page page, boolean refresh) throws Exception {
         PageInfo<AgServicesMonitor> pageInfo=iAgServicesMonitor.searchAgServicesMonitor(agServicesMonitor, page);
        if (refresh){
            List<AgServicesMonitor> list = pageInfo.getList();
            iAgServicesMonitor.refreshServerStatus(list,-1);//手动刷新不计入定时器监控次数
            PageInfo<AgServicesMonitor> afterRefresh=iAgServicesMonitor.searchAgServicesMonitor(agServicesMonitor, page);
            return new ContentResultForm<EasyuiPageInfo>(true,PageHelper.toEasyuiPageInfo(afterRefresh));
        }else {
            return new ContentResultForm<EasyuiPageInfo>(true,PageHelper.toEasyuiPageInfo(pageInfo));
        }
    }

    @ApiOperation(value = "根据监控url获取监控信息",notes = "根据监控url获取监控信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "monitorUrl",required = true, value = "监控url", dataType = "String")
    })
    @RequestMapping(value = "/findByMonitorUrl",method = RequestMethod.POST)
    public ContentResultForm findByMonitorUrl(String monitorUrl)throws Exception{
        return new ContentResultForm(true,iAgServicesMonitor.findByMonitorUrl(monitorUrl),"监控信息");
    }

    @ApiOperation(value = "根据监控id获取监控信息",notes = "根据监控id获取监控信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",required = true, value = "id", dataType = "String")
    })
    @RequestMapping(value = "/findById",method = RequestMethod.POST)
    public ContentResultForm findById(String id)throws Exception{
        return new ContentResultForm(true,iAgServicesMonitor.findById(id),"监控信息");
    }

    @ApiOperation(value = "保存监控信息",notes = "保存监控信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agServicesMonitor" ,value = "监控信息对象",dataType = "AgServicesMonitor")
    })
    @RequestMapping("/save")
    public ResultForm save(AgServicesMonitor agServicesMonitor)throws Exception{
        try {
            iAgServicesMonitor.saveAgServicesMonitor(agServicesMonitor);
            return new ResultForm(true, "保存成功");
        }catch (Exception e){
            return new ResultForm(false, "保存失败");
        }
    }

    @ApiOperation(value = "修改监控信息",notes = "修改监控信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agServicesMonitor" ,value = "监控信息对象",dataType = "AgServicesMonitor")
    })
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResultForm updata(AgServicesMonitor agServicesMonitor) {
        try {
            iAgServicesMonitor.updataAgServicesMonitor(agServicesMonitor);
            return new ResultForm(true, "修改成功");
        }catch (Exception e){
            return new ResultForm(false, "修改失败");
        }
    }

    @ApiOperation(value = "根据url删除监控信息",notes = "根据url删除监控信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "monitorUrl" ,value = "监控url",dataType = "String")
    })
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResultForm delete(String monitorUrl){
        try {
            iAgServicesMonitor.deleteByMonitorUrl(monitorUrl);
            return new ResultForm(true, "删除成功");
        }catch (Exception e){
            return new ResultForm(false, "删除失败");
        }
    }
    @ApiOperation(value = "修改监控状态",notes = "修改监控状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agServicesMonitor" ,value = "监控信息对象",dataType = "AgServicesMonitor")
    })
    @RequestMapping(value = "/changeMonitorStatus",method = RequestMethod.POST)
    public ResultForm changeMonitorStatus(AgServicesMonitor agServicesMonitor) {
        try {
            iAgServicesMonitor.changeMonitorStatus(agServicesMonitor);
            return new ResultForm(true, "修改监控状态成功");
        }catch (Exception e){
            return new ResultForm(false, "修改监控状态失败");
        }
    }
    @ApiOperation(value = "修改所有监控状态",notes = "修改所有监控状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agServicesMonitor" ,value = "监控信息对象",dataType = "AgServicesMonitor")
    })
    @RequestMapping(value = "/changeAllMonitorStatus",method = RequestMethod.POST)
    public ResultForm changeAllMonitorStatus(AgServicesMonitor agServicesMonitor) {
        try {
            AgDefaultValue agDefaultValue = iAgDefaultValue.findByKey("globalMonitorStatus");
            JSONObject json = new JSONObject();
            json.put("monitorStatus",agServicesMonitor.getMonitorStatus());
            if (agDefaultValue==null) {
                AgDefaultValue adv = new AgDefaultValue();
                adv.setId(UUID.randomUUID().toString());
                adv.setKey("globalMonitorStatus");
                adv.setDefaultValue(json.toString());
                adv.setRemark("全局监控状态");
                iAgDefaultValue.save(adv);
            }else {
                agDefaultValue.setDefaultValue(json.toString());
                iAgDefaultValue.update(agDefaultValue);
            }
            //iAgServicesMonitor.changeAllMonitorStatus(agServicesMonitor);
            return new ResultForm(true, "修改所有监控状态成功");
        }catch (Exception e){
            return new ResultForm(false, "修改所有监控状态失败");
        }
    }

    @ApiOperation(value = "修改告警信息",notes = "修改告警信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agServicesMonitor" ,value = "监控信息对象",dataType = "AgServicesMonitor")
    })
    @RequestMapping("/updateSetWarnInfo")
    public ResultForm updateSetWarnInfo(AgServicesMonitor agServicesMonitor){
        try {
            iAgServicesMonitor.updateSetWarnInfo(agServicesMonitor);
            return new ResultForm(true, "修改成功");
        }catch (Exception e){
            return new ResultForm(false, "修改失败");
        }
    }

    @ApiOperation(value = "获取全局监控状态",notes = "获取全局监控状态")
    @RequestMapping(value = "/getGlobalMonitorStatus",method = RequestMethod.GET)
    public ContentResultForm getGlobalMonitorStatus() {
        String monitorStauts = "";
        try {
            AgDefaultValue agDefaultValue = iAgDefaultValue.findByKey("globalMonitorStatus");
            if (agDefaultValue != null) {
                monitorStauts = JSONObject.fromObject(agDefaultValue.getDefaultValue()).getString("monitorStatus");
            }
        } catch (Exception e) {
            return new ContentResultForm(false,"","获取状态失败");
        }
        return new ContentResultForm(true,monitorStauts,"获取状态成功");
    }
}
