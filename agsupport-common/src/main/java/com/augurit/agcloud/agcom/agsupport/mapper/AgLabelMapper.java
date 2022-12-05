package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgLabel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2017-05-11.
 */
@Mapper
public interface AgLabelMapper {
    /**
     * 保存
     *
     * @param agLabel
     * @throws Exception
     */
    void save(AgLabel agLabel) throws Exception;

    /**
     * 查询所有标注
     *
     * @param userId
     * @throws Exception
     */
    List<AgLabel> findList(String userId) throws Exception;

    /**
     * 修改
     *
     * @param agLabel
     * @throws Exception
     */
    void update(AgLabel agLabel) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @throws Exception
     */
    void delete(String id) throws Exception;
}

