package com.augurit.agcloud.agcom.agsupport.sc.bookmark.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgBookmark;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBookmarkMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bookmark.service.IAgBookmark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :14:18 2019/4/8
 * @Modified By:
 */
@Service
public class AgBookmarkImlp implements IAgBookmark{
    @Autowired
    private AgBookmarkMapper agBookmarkMapper;

    @Override
    public void save(AgBookmark agBookmark) throws Exception {
        agBookmarkMapper.save(agBookmark);
    }

    @Override
    public void update(AgBookmark agBookmark) throws Exception {
        agBookmarkMapper.update(agBookmark);
    }

    @Override
    public AgBookmark findById(String id) throws Exception {
        return agBookmarkMapper.findById(id);
    }
    @Override
    public List<AgBookmark> findByUserId(String userId) throws Exception {
        return agBookmarkMapper.findByUserId(userId);
    }

    @Override
    public void deleteById(String id) throws Exception {
        agBookmarkMapper.deleteById(id);
    }
}
