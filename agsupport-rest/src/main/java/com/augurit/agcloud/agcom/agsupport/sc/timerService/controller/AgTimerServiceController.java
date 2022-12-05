package com.augurit.agcloud.agcom.agsupport.sc.timerService.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgTimerService;
import com.augurit.agcloud.agcom.agsupport.sc.timerService.service.IAgTimerService;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.common.util.Common;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by caokp on 2017-08-29.
 */
@RestController
@RequestMapping("/agsupport/timerService")
public class AgTimerServiceController {

    private static Logger logger = LoggerFactory.getLogger(AgTimerServiceController.class);

    @Autowired
    private IAgTimerService iAgTimerService;

    @RequestMapping("/index.do")
    public ModelAndView index(HttpServletRequest request, Model model) {

        return new ModelAndView("/agcom/timerService/timerService");
    }

    @RequestMapping("/timerServiceList")
    public EasyuiPageInfo<AgTimerService> timerServiceList(AgTimerService agTimerService, Page page) throws Exception {
        PageInfo<AgTimerService> pageInfo=iAgTimerService.findTimerServiceList(agTimerService, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/save")
    public String save(@RequestParam("file") MultipartFile file, AgTimerService agTimerService) {
        String fileName = file.getOriginalFilename();
        if (StringUtil.isNotEmpty(fileName)) {
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            if (!".bat".equals(suffixName)) {
                return JsonUtils.toJson(new ResultForm(false, "请选择批处理文件！"));
            }
        } else {
            file = null;
        }
        try {
            iAgTimerService.save(file, agTimerService);
            return JsonUtils.toJson(new ResultForm(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false, "保存失败！"));
    }

    @RequestMapping("/delete")
    public String delete(String timerServiceIds) {
        if (!Common.isCheckNull(timerServiceIds)) {
            String[] ids = timerServiceIds.split(",");
            try {
                iAgTimerService.deleteBatch(ids);
                return JsonUtils.toJson(new ResultForm(true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JsonUtils.toJson(new ResultForm(false));
    }

    @RequestMapping("/run")
    public String run(String id) {
        try {
            if(id != null && id != ""){
                AgTimerService agTimerService = iAgTimerService.findTimerServiceById(id);
                if(agTimerService != null){
                    if (agTimerService != null) {
                        iAgTimerService.timerRun(agTimerService);
                    }
                    return JsonUtils.toJson(new ResultForm(true));
                }
                return JsonUtils.toJson(new ResultForm(false,"不存在id为" + id + "的定时服务"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false,"id为空"));
    }

    @RequestMapping("/stop")
    public String stop(String id) {
        try {
            if(id != null && id != ""){
                AgTimerService agTimerService = iAgTimerService.findTimerServiceById(id);
                if(agTimerService != null){
                    agTimerService.setState("0");
                    if (agTimerService != null) {
                        iAgTimerService.update(agTimerService);
                    }
                    return JsonUtils.toJson(new ResultForm(true));
                }
                return JsonUtils.toJson(new ResultForm(false,"不存在id为" + id + "的定时服务"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false,"id为空"));
    }
}
