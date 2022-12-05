package com.augurit.agcloud.agcom.agsupport.sc.editor.controller;

import com.augurit.agcloud.agcom.agsupport.common.util.fileUtils;
import com.augurit.agcloud.agcom.agsupport.domain.AgNotice;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.sc.editor.service.IAgEditor;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
 * Created by czh on 2018-05-16.
 */
@RestController
@RequestMapping("/agsupport/notice")
public class AgEditorController {
    private static Logger logger = LoggerFactory.getLogger(AgEditorController.class);

    @Autowired
    public IAgEditor iAgEditor;

    @Autowired
    private IAgUser iAgUser;

    @RequestMapping("/index.do")
    public ModelAndView index() {
        return new ModelAndView("/agcom/editor/editorIndex");
    }
    @Value("${agcloud.inf.load}")
    public Boolean agcloudFlag;

    @RequestMapping("/saveNotice")
    public String saveNotice(AgNotice agNotice, String userName, HttpServletRequest request) {
        try {
            if (Common.isCheckNull(agNotice)) return JsonUtils.toJson(new ResultForm(false));
            agNotice.setReleaseTime(new Date());
            if (!Common.isCheckNull(agNotice.getId())) {
                iAgEditor.updateNotice(agNotice);
            } else {
                agNotice.setId(UUID.randomUUID().toString());
                String loginName = LoginHelpClient.getLoginName(request);
                AgUser agUser = iAgUser.findUserByName(loginName);
                agNotice.setUserId(agUser.getId());
                agNotice.setUserName(loginName);
                iAgEditor.saveNotice(agNotice);
            }
            return JsonUtils.toJson(new ResultForm(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false));
    }

    @RequestMapping("/delNotice")
    public String deleteNotice(String id) {
        try {
            if (Common.isCheckNull(id)) return null;
            String picPath = iAgEditor.getNotice(id).getPicturesPath();
            this.delPic(picPath);
            iAgEditor.deleteNotice(id);
            return JsonUtils.toJson(new ResultForm(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false));
    }

    @RequestMapping("/noticeList")
    public EasyuiPageInfo<AgNotice> noticeList(HttpServletRequest request, Page page) throws Exception {
        PageInfo<AgNotice> list = null;
        if ("T".equals(request.getParameter("mobile"))) {
            list = iAgEditor.findNoticeList(null, page);
        } else {
            String loginName = LoginHelpClient.getLoginName(request);
            AgUser agUser = iAgUser.findUserByName(loginName);
            list = iAgEditor.findNoticeList(agUser.getId(), page);
        }
        PageInfo<AgNotice> pageInfo=list;
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/delPic")
    public void delPic(String picPath) {
        try {
            if (!Common.isCheckNull(picPath)) {
                String path[] = picPath.split(",");
                for (String str : path) {
                    if (!str.equals(""))
                        fileUtils.deleteFile(str);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
