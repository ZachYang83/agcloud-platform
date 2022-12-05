package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckProjectModel;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckProjectModelExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * @Author: libc
 * @Date: 2020/11/20
 * @tips: mapper接口
 */
@Mapper
public interface AgBimCheckProjectModelMapper {
    int countByExample(AgBimCheckProjectModelExample example);

    int deleteByExample(AgBimCheckProjectModelExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgBimCheckProjectModel record);

    int insertSelective(AgBimCheckProjectModel record);

    List<AgBimCheckProjectModel> selectByExample(AgBimCheckProjectModelExample example);

    AgBimCheckProjectModel selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgBimCheckProjectModel record, @Param("example") AgBimCheckProjectModelExample example);

    int updateByExample(@Param("record") AgBimCheckProjectModel record, @Param("example") AgBimCheckProjectModelExample example);

    int updateByPrimaryKeySelective(AgBimCheckProjectModel record);

    int updateByPrimaryKey(AgBimCheckProjectModel record);


}