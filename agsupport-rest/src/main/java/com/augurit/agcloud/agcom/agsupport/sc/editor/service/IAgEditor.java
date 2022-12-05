package com.augurit.agcloud.agcom.agsupport.sc.editor.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgNotice;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * Created by Augurit on 2018-05-16.
 */
public interface IAgEditor {

    void deleteNotice(String id) throws Exception;

    void updateNotice(AgNotice agNotice) throws Exception;

    void saveNotice(AgNotice agNotice) throws Exception;

    AgNotice getNotice(String id) throws Exception;

    PageInfo<AgNotice> findNoticeList(String userId, Page page) throws Exception;
}
