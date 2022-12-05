package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgFileStore;
import com.augurit.agcloud.agcom.agsupport.domain.AgImageStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName AgImageStoreMapper
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/23 14:59
 * @Version 1.0
 **/
@Mapper
public interface AgImageStoreMapper {

    //根据id获得image
    AgImageStore getById(@Param("id") String id);

    //获得所有image
    List<AgImageStore> getAll();

    //删除一条数据
    int deleteById(@Param("id") String id);

    //删除更多数据
    int deleteMany(@Param("ids") List<String> ids);

    //添加
    int save(AgImageStore agImageStore);

    //修改
    int update(AgImageStore agImageStore);

    //特殊条件查询列表
    List<AgImageStore> getByDomainAndUsage(AgImageStore agImageStore);

    //特殊条件查询列表
    List<AgImageStore> getByDomainOrUsage(AgImageStore agImageStore);

    //根据id查找更多
    List<AgImageStore> getByIds(List<String> stringList);
}
