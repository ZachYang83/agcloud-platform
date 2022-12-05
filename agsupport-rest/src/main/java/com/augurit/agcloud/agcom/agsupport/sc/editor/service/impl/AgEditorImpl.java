package com.augurit.agcloud.agcom.agsupport.sc.editor.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgNotice;
import com.augurit.agcloud.agcom.agsupport.mapper.AgNoticeMapper;
import com.augurit.agcloud.agcom.agsupport.sc.editor.service.IAgEditor;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.common.util.Common;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Augurit on 2018-05-16.
 */
@Service
public class AgEditorImpl implements IAgEditor {

    @Autowired
    public AgNoticeMapper agNoticeMapper;

    @Override
    public void deleteNotice(String id) throws Exception {
        if (Common.isCheckNull(id)) return;
        agNoticeMapper.delete(id);
    }

    @Override
    public void updateNotice(AgNotice agNotice) throws Exception {
        if (Common.isCheckNull(agNotice)) return;
        agNoticeMapper.update(agNotice);
    }

    @Override
    public void saveNotice(AgNotice agNotice) throws Exception {
        if (Common.isCheckNull(agNotice)) return;
        agNoticeMapper.save(agNotice);
    }

    @Override
    public PageInfo<AgNotice> findNoticeList(String userId, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AgNotice> list = agNoticeMapper.findList(userId);
        return new PageInfo<AgNotice>(list);
    }

    @Override
    public AgNotice getNotice(String id) throws Exception {
        if (Common.isCheckNull(id)) return null;
        return agNoticeMapper.getNotice(id);
    }

}
