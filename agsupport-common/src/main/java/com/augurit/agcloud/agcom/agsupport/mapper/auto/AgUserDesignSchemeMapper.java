package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserDesignScheme;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserDesignSchemeExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgUserDesignSchemeMapper {
    int countByExample(AgUserDesignSchemeExample example);

    int deleteByExample(AgUserDesignSchemeExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgUserDesignScheme record);

    int insertSelective(AgUserDesignScheme record);

    List<AgUserDesignScheme> selectByExample(AgUserDesignSchemeExample example);

    AgUserDesignScheme selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgUserDesignScheme record, @Param("example") AgUserDesignSchemeExample example);

    int updateByExample(@Param("record") AgUserDesignScheme record, @Param("example") AgUserDesignSchemeExample example);

    int updateByPrimaryKeySelective(AgUserDesignScheme record);

    int updateByPrimaryKey(AgUserDesignScheme record);
}