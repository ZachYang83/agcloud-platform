package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimApi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: AgBimApiMapper
 * @Description: BIM第三方接口映射器
 * @Author: zhangsj
 * @Date: Create in 2020/03/20 13:41
 **/

@Mapper
public interface AgBimApiMapper {

    //新增
    int add(AgBimApi agBimApi);

    //根据Id删除一条数据
    int deleteById(@Param("id") String id);

    //删除更多数据
    int deleteMany(@Param("ids") List<String> ids);

    //根据Id查询一条数据
    AgBimApi getById(@Param("id") String id);

    //查询所有
    List<AgBimApi> getAll();

    //编辑
    int update(AgBimApi agBimApi);

    //特殊条件Or分页查询
    List<AgBimApi> getByNameOrUrl(@Param("name") String name, @Param("url") String url);

    //特殊条件And分页查询
    List<AgBimApi> getByNameAndUrl(AgBimApi agBimApi);

    //根据id查找更多
    List<AgBimApi> getByIds(List<String> stringList);
}
