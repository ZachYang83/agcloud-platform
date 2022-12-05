package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgFileStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName AgFileStoreMapper
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/23 14:59
 * @Version 1.0
 **/
@Mapper
public interface AgFileStoreMapper {

    //根据id获得image
    AgFileStore getById(@Param("id") String id);

    //获得所有image
    List<AgFileStore> getAll();

    //删除一条数据
    int deleteById(@Param("id") String id);

    //删除更多数据
    int deleteMany(@Param("ids") List<String> ids);

    //添加
    int save(AgFileStore agFileStore);

    //修改
    int update(AgFileStore agFileStore);

    //特殊条件查询列表
    List<AgFileStore> getByDomainAndUsage(AgFileStore agFileStore);

    //特殊条件查询列表
    List<AgFileStore> getByDomainOrUsage(AgFileStore agFileStore);

    //根据条件查询
    List<AgFileStore> downloadByDomainAndUsage(AgFileStore agFileStore);

    //更多数据
    List<AgFileStore> getByIds(@Param("ids") List<String> ids);

    //根据业务code，usage条件查询
    List<AgFileStore> getFileByModuleCode(AgFileStore store);
}
