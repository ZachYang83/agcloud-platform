package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgProjectManager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: libc
 * @Description: 工程管理Mapper
 * @Date: 2020/7/14 14:06
 * @Version: 1.0
 */
@Mapper
public interface AgProjectManagerMapper {

    int deleteByPrimaryKey(String id);

    /**
     * 根据id 批量删除记录
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") String[] ids);

    int insert(AgProjectManager record);

    int insertSelective(AgProjectManager record);

    AgProjectManager selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AgProjectManager record);

    int updateByPrimaryKey(AgProjectManager record);

    List<AgProjectManager> findList(@Param("name") String name);

    AgProjectManager findByName(String name);

    List<AgProjectManager> findAll();

    AgProjectManager findByParam(@Param("map")Map<String, String> map);
}
