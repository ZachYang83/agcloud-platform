package com.augurit.agcloud.agcom.agsupport.sc.attachment.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgAttachment;
import com.augurit.agcloud.agcom.agsupport.sc.attachment.service.IAgAttachment;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.common.util.Common;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
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
import java.util.ArrayList;

/**
 * Created by caokp on 2017-08-28.
 */
@RestController
@RequestMapping("/agsupport/attachment")
public class AgAttachmentController {

    private static Logger logger = LoggerFactory.getLogger(AgAttachmentController.class);

    @Autowired
    private IAgAttachment iAgAttachment;

    @RequestMapping("/index.do")
    public ModelAndView index(HttpServletRequest request, Model model) {

        return new ModelAndView("/agcom/attachment/attachmentIndex");
    }

    @RequestMapping("/attachmentList")

    public EasyuiPageInfo<AgAttachment> attachmentList(AgAttachment agAttachment, Page page) throws Exception {
        PageInfo<AgAttachment> pageInfo = iAgAttachment.findAttachmentList(agAttachment, page);
        return  PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/upload")
    public String upload(@RequestParam("files") MultipartFile[] files, HttpServletRequest request, AgAttachment agAttachment) {
        try {
            if (files.length == 0)
                return JsonUtils.toJson(new ResultForm(false,"文件为空"));
            iAgAttachment.upload(files, request, agAttachment);
            return JsonUtils.toJson(new ResultForm(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false));
    }

    @RequestMapping("/delete")
    public String delete(String attachmentIds) throws Exception {
        if (!Common.isCheckNull(attachmentIds)) {
            String[] ids = attachmentIds.split(",");
            ArrayList<String> isNotExistIds = new ArrayList<String>();
            for (String id : ids) {
                AgAttachment temp = iAgAttachment.findAttachmentById(id);
                if(temp == null)
                    isNotExistIds.add(id);
            }
            try {
                iAgAttachment.deleteBatch(ids);
                if(isNotExistIds.size() == 0)
                    return JsonUtils.toJson(new ResultForm(true,"全部删除成功"));
                else
                    return JsonUtils.toJson(new ResultForm(true,"但id为" + isNotExistIds.toString() + "的附件删除失败"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JsonUtils.toJson(new ResultForm(false));
    }
}
