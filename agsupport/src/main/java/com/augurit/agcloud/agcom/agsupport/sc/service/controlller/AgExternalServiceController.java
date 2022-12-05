package com.augurit.agcloud.agcom.agsupport.sc.service.controlller;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.sc.dir.controller.AgDirController;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcom.common.LoginHelpClient;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangmy
 * @Description: 管理从外部接入的服务
 * @date 2019-11-11 13:39
 */
@Api(value = "外部服务管理",description = "外部服务管理接口")
@RestController
@RequestMapping("/agsupport/externalServiceManager")
public class AgExternalServiceController {
    @Autowired
    private IAgUser iAgUser;

    private static Logger logger = LoggerFactory.getLogger(AgDirController.class);
    @Autowired
    private IAgDir iAgDir;

    @Autowired
    private IAgLayer iAgLayer;

    /**
     * 获取用户的外部图层
     *
     * @return
     */
    @ApiOperation(value = "根据用户ID查询外部图层",notes = "根据用户ID查询外部图层接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户ID",dataType = "String",paramType = "path",required = true),
            @ApiImplicitParam(name = "name",value = "图层名称",dataType = "String"),
            @ApiImplicitParam(name = "page",value = "分页参数:page=1&rows=10",dataType = "String"),
    })
    @RequestMapping(value = "/searchFromExternalLayer",method = RequestMethod.GET)
    public ContentResultForm searchFromExternalLayer(HttpServletRequest request, String name,String status ,Page page) throws Exception {
        AgUser user = iAgUser.findUserByName(LoginHelpClient.getLoginName(request));
        PageInfo<AgLayer> pageInfo =iAgDir.searchFromExternalLayer(name, page, user.getId(),status);
        return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(pageInfo));
    }

    /**
     * 获取用户的外部图层
     *
     * @return
     */
    @ApiOperation(value = "启动或停用图层服务",notes = "启用或停用外部图层接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "多个图层ID的集合，用逗号分隔",dataType = "String",required = true),
            @ApiImplicitParam(name = "status",value = "图层状态，启用：1；停用：0",dataType = "String")
    })
    @RequestMapping(value = "/updateStatus",method = RequestMethod.POST)
    public ResultForm updateStatus(String[] ids, String status) throws Exception {
        boolean a = ids==null;
        boolean b = ids.length==0;
        boolean c = !"0".equals(status)&&!"1".equals(status);
        if(ids==null||ids.length==0||(!"0".equals(status)&&!"1".equals(status))){
            return new ResultForm(false, "请求参数不正确！");
        }
        iAgLayer.updateLayersStatus(ids,status);
        return new ResultForm(true, "请求成功");
    }


    @ApiOperation(value = "设置外部服务处理规则",notes = "设置外部服务处理规则接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "图层ID",dataType = "String",required = true),
            @ApiImplicitParam(name = "changeRuleCode",value = "转换规则的名称",dataType = "String"),
            @ApiImplicitParam(name = "filterRuleCode",value = "清洗规则的名称",dataType = "String"),
            @ApiImplicitParam(name = "sendRuleCode",value = "转发规则的名称",dataType = "String"),
            @ApiImplicitParam(name = "actionRuleCode",value = "行为规则的名称",dataType = "String"),
            @ApiImplicitParam(name = "saveEnable",value = "是否转存，1-是，0-否",dataType = "String"),
            @ApiImplicitParam(name = "errorRuleCode",value = "异常规则的名称",dataType = "String"),
            @ApiImplicitParam(name = "status",value = "图层状态，启用：1；停用：0",dataType = "String")
    })
    @RequestMapping(value = "/setHandleRules",method = RequestMethod.POST)
    public ResultForm updateStatus(AgLayer agLayer) throws Exception {
        String layerId = agLayer.getId();
        if(layerId==null||layerId.length()==0){
            return new ResultForm(false,"请求参数不正确");
        }
        iAgLayer.setHandleRules(agLayer);
        return new ResultForm(true, "请求成功");
    }
}
