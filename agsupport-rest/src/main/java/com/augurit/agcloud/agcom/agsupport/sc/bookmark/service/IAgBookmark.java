package com.augurit.agcloud.agcom.agsupport.sc.bookmark.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgBookmark;

import java.util.List;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :14:16 2019/4/8
 * @Modified By:
 */
public interface IAgBookmark {
    /**
     * 保存书签
     * @param agBookmark
     * @throws Exception
     */
    void save(AgBookmark agBookmark) throws Exception;

    /**
     * 更新
     * @param agBookmark
     * @throws Exception
     */
    void update(AgBookmark agBookmark) throws Exception;


    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public AgBookmark findById(String id) throws Exception;

    /**
     * 查询书签
     * @param userId
     * @return
     * @throws Exception
     */
    public List<AgBookmark> findByUserId(String userId) throws Exception;

    /**
     * 通过id删除书签
     * @param id
     * @return
     * @throws Exception
     */
    public void deleteById(String id) throws Exception;

}
