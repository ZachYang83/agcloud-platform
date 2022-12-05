package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserDesignMaterials;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserDesignMaterialsExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgUserDesignMaterialsMapper {
    int countByExample(AgUserDesignMaterialsExample example);

    int deleteByExample(AgUserDesignMaterialsExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgUserDesignMaterials record);

    int insertSelective(AgUserDesignMaterials record);

    List<AgUserDesignMaterials> selectByExample(AgUserDesignMaterialsExample example);

    AgUserDesignMaterials selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgUserDesignMaterials record, @Param("example") AgUserDesignMaterialsExample example);

    int updateByExample(@Param("record") AgUserDesignMaterials record, @Param("example") AgUserDesignMaterialsExample example);

    int updateByPrimaryKeySelective(AgUserDesignMaterials record);

    int updateByPrimaryKey(AgUserDesignMaterials record);
}