package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgBookmark;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :14:01 2019/4/8
 * @Modified By:
 */
@Mapper
public interface AgBookmarkMapper {

    /**
     * 保存
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
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public List<AgBookmark> findByUserId(String userId) throws Exception;

    /**
     *
     * @param id  书签id
     * @throws Exception
     */
    public void deleteById(String id) throws Exception;
}
