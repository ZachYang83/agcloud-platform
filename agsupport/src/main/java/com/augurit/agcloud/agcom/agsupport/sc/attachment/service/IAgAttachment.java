package com.augurit.agcloud.agcom.agsupport.sc.attachment.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgAttachment;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by caokp on 2017-08-28.
 */
public interface IAgAttachment {

    /**
     * 分页查询
     *
     * @param agAttachment
     * @param page
     * @return
     */
    PageInfo<AgAttachment> findAttachmentList(AgAttachment agAttachment, Page page) throws Exception;

    /**
     * 按id查找附件
     *
     * @param id
     * @return
     * @throws Exception
     */
    AgAttachment findAttachmentById(String id) throws Exception;

    /**
     * 上传附件
     *
     * @param request
     * @param agAttachment
     * @throws Exception
     */
    void upload(MultipartFile[] files, HttpServletRequest request, AgAttachment agAttachment) throws Exception;

    /**
     * 保存附件
     *
     * @param agAttachment
     * @throws Exception
     */
    void saveAttachment(AgAttachment agAttachment) throws Exception;

    /**
     * 删除附件
     *
     * @param id
     * @throws Exception
     */
    void deleteAttachment(String id) throws Exception;

    /**
     * 批量删除
     *
     * @param ids
     * @throws Exception
     */
    void deleteBatch(String[] ids) throws Exception;
}
