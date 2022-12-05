package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgJsonStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName AgJsonStoreMapper
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/23 14:59
 * @Version 1.0
 **/
@Mapper
public interface AgJsonStoreMapper {

    //根据id获得image
    AgJsonStore getById(@Param("id") String id);

    //获得所有image
    List<AgJsonStore> getAll();

    //删除一条数据
    int deleteById(@Param("id") String id);

    //删除更多数据
    int deleteMany(@Param("ids") List<String> ids);

    //添加
    int save(AgJsonStore agJsonStore);

    //修改
    int update(AgJsonStore agJsonStore);

    //特殊条件查询列表
    List<AgJsonStore> getByDomainAndUsage(AgJsonStore agJsonStore);

    //特殊条件查询列表
    List<AgJsonStore> getByDomainOrUsage(AgJsonStore agJsonStore);

    //根据id查找更多
    List<AgJsonStore> getByIds(@Param("ids") List<String> ids);
}
