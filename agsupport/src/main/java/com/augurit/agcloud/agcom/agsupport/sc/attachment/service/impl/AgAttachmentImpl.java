package com.augurit.agcloud.agcom.agsupport.sc.attachment.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgAttachment;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.mapper.AgAttachmentMapper;
import com.augurit.agcloud.agcom.agsupport.sc.attachment.service.IAgAttachment;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcom.common.LoginHelpClient;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by caokp on 2017-08-28.
 */
@Service
public class AgAttachmentImpl implements IAgAttachment {

    @Autowired
    private AgAttachmentMapper agAttachmentMapper;

    @Autowired
    private IAgDic iAgDic;

    @Override
    public PageInfo<AgAttachment> findAttachmentList(AgAttachment agAttachment, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AgAttachment> list = agAttachmentMapper.findList(agAttachment);
        return new PageInfo<AgAttachment>(list);
    }

    @Override
    public AgAttachment findAttachmentById(String id) throws Exception {
        return agAttachmentMapper.findById(id);
    }

    @Override
    public void upload(MultipartFile[] files, HttpServletRequest request, AgAttachment agAttachment) throws Exception {
        String loginName = LoginHelpClient.getLoginName(request);
        try {
            String uploadPath = "C:/upload";//默认上传至C:/upload文件夹
            AgDic agDic = iAgDic.findAgDicByCode("A015001");
            if (agDic != null && StringUtils.isNotEmpty(agDic.getValue())) uploadPath = agDic.getValue();
            File tempDirPath = new File(uploadPath);
            if (!tempDirPath.exists()) {
                tempDirPath.mkdirs();
            }
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                String tempName = UUID.randomUUID().toString() + suffixName;
                File itemFile = new File(uploadPath + File.separator + tempName);
                file.transferTo(itemFile);

                AgAttachment temp = new AgAttachment();
                temp.setId(UUID.randomUUID().toString());
                temp.setName(fileName);
                temp.setFilePath(uploadPath + File.separator + tempName);
                temp.setFuncName(agAttachment.getFuncName());
                temp.setUploadDate(new Date());
                temp.setUploadMan(loginName);
                temp.setRemark(agAttachment.getRemark());
                this.saveAttachment(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAttachment(AgAttachment agAttachment) throws Exception {

        agAttachmentMapper.save(agAttachment);
    }

    @Override
    public void deleteAttachment(String id) throws Exception {
        AgAttachment temp = this.findAttachmentById(id);
        if (temp != null) {
            if (StringUtils.isNotEmpty(temp.getFilePath())) {
                File file = new File(temp.getFilePath());
                if (file.exists()) file.delete();
            }
            agAttachmentMapper.delete(id);
        }
    }

    @Override
    public void deleteBatch(String[] ids) throws Exception {
        if (ids == null || ids.length < 0) return;
        for (String id : ids) {
            this.deleteAttachment(id);
        }
    }
}
