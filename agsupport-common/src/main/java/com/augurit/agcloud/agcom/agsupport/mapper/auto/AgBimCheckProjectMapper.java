package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckProject;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckProjectExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @Author: Zihui Li
 * @Date: 2020/11/20
 * @tips: mapper接口
 */
@Mapper
public interface AgBimCheckProjectMapper {
    int countByExample(AgBimCheckProjectExample example);

    int deleteByExample(AgBimCheckProjectExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgBimCheckProject record);

    int insertSelective(AgBimCheckProject record);

    List<AgBimCheckProject> selectByExample(AgBimCheckProjectExample example);

    AgBimCheckProject selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgBimCheckProject record, @Param("example") AgBimCheckProjectExample example);

    int updateByExample(@Param("record") AgBimCheckProject record, @Param("example") AgBimCheckProjectExample example);

    int updateByPrimaryKeySelective(AgBimCheckProject record);

    int updateByPrimaryKey(AgBimCheckProject record);
}