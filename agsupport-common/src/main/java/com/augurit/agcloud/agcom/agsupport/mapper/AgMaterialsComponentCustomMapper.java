package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponent;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponentExample;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponentWithBLOBs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@Mapper
public interface AgMaterialsComponentCustomMapper {
    List<AgMaterialsComponent> selectByExample(AgMaterialsComponentExample example);

    List<AgMaterialsComponent> select(@Param("catagory")String catagory, @Param("name")String name,
                                      @Param("orderBy")String orderBy, @Param("componentCode") String componentCode,
                                      @Param("componentCodeName") String componentCodeName, @Param("specification") String specification);

    List<AgMaterialsComponent> unionSelect(@Param("catagory")String catagory,@Param("name")String name,
                                           @Param("orderBy")String orderBy,
                                           @Param("permisions")List<String> permisions, @Param("componentCode") String componentCode,
                                           @Param("componentCodeName") String componentCodeName, @Param("specification") String specification);

    AgMaterialsComponentWithBLOBs selectThumb(String id);

    AgMaterialsComponentWithBLOBs selectGlb(String id);

    List<AgMaterialsComponentWithBLOBs> sql(String sql);
}
