package com.augurit.agcloud.agcom.agsupport.sc.service.controlller;

import com.augurit.agcloud.agcom.agsupport.common.util.ProxyUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgServiceLog;
import com.augurit.agcloud.agcom.agsupport.domain.AgServiceUserinfo;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.service.service.IAgService;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.common.util.Common;
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
import java.util.Date;
import java.util.List;

/**
 * Created by Augurit on 2017-05-02.
 */
@Api(value = "服务访问相关",description = "服务访问相关接口")
@RestController
@RequestMapping("/agsupport/service")
public class AgServiceController {
    private static Logger logger = LoggerFactory.getLogger(AgServiceController.class);

    @Autowired
    private IAgService iAgService;

    @Autowired
    private IAgUser iAgUser;

    @Autowired
    private IAgLayer iAgLayer;

 /*   @RequestMapping("/accreditPage.do")
    public ModelAndView accreditPage() {
        return new ModelAndView("/agcom/service/accredit");
    }
*/
    @RequestMapping(value = "/accredit",method = RequestMethod.POST)
    public void accredit(HttpServletRequest request) {
        String flag = request.getParameter("flag");
        String approveOpinion = request.getParameter("message");
        String[] ids = request.getParameterValues("ids");
        for (int i = 0; i < ids.length; i++) {
            AgServiceUserinfo agServiceUserinfo = iAgService.getAgServiceUserinfoById(ids[i]);
            agServiceUserinfo.setFlag(Common.checkNull(flag, "0"));
            agServiceUserinfo.setApproveOpinion(approveOpinion);
            agServiceUserinfo.setApproveTime(new Date());
            iAgService.updateAgServiceUserinfo(agServiceUserinfo);
        }
    }

    @RequestMapping(value = "/getServiceUserinfoList",method = RequestMethod.GET)
    public EasyuiPageInfo<AgServiceUserinfo> getServiceUserinfoList(AgServiceUserinfo agServiceUserinfo, Page page) {
        PageInfo<AgServiceUserinfo> pageInfo=iAgService.searchAgServiceUserinfo(agServiceUserinfo, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping(value = "/deleteServiceUserinfo",method = RequestMethod.DELETE)
    public String deleteServiceUserinfo(HttpServletRequest request) {
        String[] ids = request.getParameterValues("ids");
        for (String id : ids) {
            AgServiceUserinfo asu = iAgService.getAgServiceUserinfoById(id);
            if (asu != null)
                iAgService.deleteAgServiceUserinfo(asu);
        }
        return JsonUtils.toJson(new ResultForm(true));
    }

    @RequestMapping(value = "/inputServiceUserinfo",method = RequestMethod.GET)
    public String inputServiceUserinfo(String id) {
        AgServiceUserinfo agServiceUserinfo = iAgService.getAgServiceUserinfoById(id);
        return JsonUtils.toJson(agServiceUserinfo);
    }

    @ApiOperation(value = "根据条件查询服务日志信息",notes = "根据条件查询服务日志信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agServiceLog",value = "服务信息对象",dataType = "AgServiceLog"),
            @ApiImplicitParam(name = "page",value = "分页参数:page=1&rows=10",dataType = "Page")
    })
    @RequestMapping(value = "/getServiceLogList",method = RequestMethod.GET)
    public ContentResultForm getServiceLogList(AgServiceLog agServiceLog, Page page) {
        PageInfo<AgServiceLog> pageInfo=iAgService.searchAgServiceLog(agServiceLog, page);
        return new ContentResultForm<>(true,PageHelper.toEasyuiPageInfo(pageInfo)) ;
    }

//    //根据 service id, 登录名，获取service userInfo
//    public List<AgServiceUserinfo> getServiceUserInfoByServiceIdAndUserName(String[] serviceIds, String userId) throws Exception {
//        List<AgServiceUserinfo> serviceUserinfos = iAgService.getServiceUserInfoByServiceIdAndUserName(serviceIds, userId);
//        return serviceUserinfos;
//    }
//
//    public void applyServices(String flag, String approveOpinion, String[] ids) {
////        String flag = request.getParameter("flag");
////        String approveOpinion = request.getParameter("message");
////        String[] ids = request.getParameterValues("ids");
//        for (int i = 0; i < ids.length; i++) {
//            AgServiceUserinfo agServiceUserinfo = iAgService.getAgServiceUserinfoById(ids[i]);
//            agServiceUserinfo.setFlag(Common.checkNull(flag, "0"));
//            agServiceUserinfo.setApproveOpinion(approveOpinion);
//            agServiceUserinfo.setApproveTime(new Date());
//            iAgService.updateAgServiceUserinfo(agServiceUserinfo);
//        }
//    }

    /**
     * 以下两个是用做测试的接口
     *
     * @throws Exception
     */
  /*  @RequestMapping("/testGet")
    public String testGet(String id) throws Exception {
        //        AgServiceUserinfo asu1  = (AgServiceUserinfo)iAgService.testMapper(id);
        //        return asu1.getApproveOpinion();
        List<Map> agcom_accessLog = MongoDBHelp.find(new Document(), "agcom_accessLog");
        List<AgServiceLog> logs = new ArrayList<>();
        for (Map m : agcom_accessLog) {
            logs.add(MongoDBHelp.map2Bean(m, new AgServiceLog()));
        }
        return JsonUtils.toJson(logs);
    }

    @RequestMapping("/testUpdate")
    public void testUpdate(String id, String update) throws Exception {
        AgServiceUserinfo asu1 = (AgServiceUserinfo) iAgService.testMapper(id);
        System.out.println(asu1.getApproveOpinion());
        asu1.setApproveOpinion(update);
        iAgService.updateAgServiceUserinfo(asu1);
    }*/

    /**
     * 获取在mark.json文件中配置的图层标签分类
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getMarks",method = RequestMethod.GET)
    public String getMarks() throws Exception {
        return iAgService.getMarks();
    }

    @RequestMapping(value = "/getLeftMenu",method = RequestMethod.GET)
    public String getLeftMenu() throws Exception {
        return iAgService.countByLabel();
    }

    @RequestMapping(value = "/getLeftMenuByUser",method = RequestMethod.GET)
    public String getLeftMenuByUser(String userId) throws Exception {
        return iAgService.countByLabelAnduserId(userId);
    }

    /**
     * 根据标签获取图层
     *
     * @param label
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/findLayerByLayerLabel",method = RequestMethod.GET)
    public String findLayerByLayerLabel(String label, boolean asc) throws Exception {
        List<AgLayer> layers = iAgService.findLayerByLabel(label, asc);
        return JsonUtils.toJson(layers);
    }

    /**
     * 根据标签和userId获取图层
     *
     * @param label
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/findByLabelAndUserId",method = RequestMethod.GET)
    public String findByLabelAndUserId(String label, String userId, boolean asc) throws Exception {
        List<AgLayer> layers = iAgService.findByLabelAndUserId(label, userId, asc);
        return JsonUtils.toJson(layers);
    }

    @RequestMapping(value = "/applyServer",method = RequestMethod.POST)
    public String applyServer(AgServiceUserinfo agServiceUserinfo, HttpServletRequest request) throws Exception {
        AgUser agUser = iAgUser.findUserByName(agServiceUserinfo.getUserName());
        if (agUser == null) {
            return JsonUtils.toJson(new ResultForm(false, "用户不存在！"));
        }
        AgLayer layer = iAgLayer.findByLayerId(agServiceUserinfo.getServiceId());
        if (layer == null) {
            return JsonUtils.toJson(new ResultForm(false, "服务不存在！"));
        }
        String ip = request.getRemoteAddr();
        agServiceUserinfo.setUserId(agUser.getId());
        agServiceUserinfo.setIp(ip);
        String uuid = iAgService.applyServer(agServiceUserinfo);
        return JsonUtils.toJson(new ResultForm(true, ProxyUtil.getProxyPreUrl() + uuid));
    }

    @RequestMapping(value = "/getApplyInfo",method = RequestMethod.GET)
    public String getApplyInfo(String userName, String serviceId) throws Exception {
        AgUser agUser = iAgUser.findUserByName(userName);
        if (agUser != null) {
            AgServiceUserinfo applyInfo = iAgService.getApplyInfo(agUser.getId(), serviceId);
            if (applyInfo != null) {
                applyInfo.setUrl(ProxyUtil.getProxyPreUrl() + applyInfo.getUuid());
                return JsonUtils.toJson(applyInfo);
            }
        }
        return null;
    }
}
