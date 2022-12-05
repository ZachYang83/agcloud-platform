package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgStyleManager;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgStyleManagerExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @Author: libc
 * @Date: 2020/11/13
 * @tips: mapper接口
 */
@Mapper
public interface AgStyleManagerMapper {
    int countByExample(AgStyleManagerExample example);

    int deleteByExample(AgStyleManagerExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgStyleManager record);

    int insertSelective(AgStyleManager record);

    List<AgStyleManager> selectByExample(AgStyleManagerExample example);

    AgStyleManager selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgStyleManager record, @Param("example") AgStyleManagerExample example);

    int updateByExample(@Param("record") AgStyleManager record, @Param("example") AgStyleManagerExample example);

    int updateByPrimaryKeySelective(AgStyleManager record);

    int updateByPrimaryKey(AgStyleManager record);
}