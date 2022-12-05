package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgFileStore;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName AgBimFileMapper
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/23 14:59
 * @Version 1.0
 **/
@Mapper
public interface AgBimFileMapper {

    //根据id获取数据
    AgBimFile getById(@Param("id") String id);

    //获取所有数据
    List<AgBimFile> getAll();

    //根据id删除一条数据
    int deleteById(@Param("id") String id);

    //删除更多数据
    int deleteMany(@Param("ids") List<String> ids);

    //添加一条数据
    int add(AgBimFile agBimFile);

    //编辑一条数据
    int update(AgBimFile agBimFile);

    //特殊条件Or分页查询
    List<AgBimFile> getByOrKeyWords(@Param("projectId") String projectId, @Param("keyword") String keyword);

    //特殊条件And分页查询
    List<AgBimFile> getByAndKeyWords(AgBimFile agBimFile);

    //根据id获取更多数据
    List<AgBimFile> getByIds(@Param("ids") List<String> ids);

    AgBimFile getByMd5(@Param("md5") String md5);

    /**
     * 是否有正在发布的bim
     * @return
     */
    int publishing(String id);

    /**
     * 获取关联bim文件
     * @param store
     * @return
     */
    List<AgFileStore> getFileByModuleCode(AgFileStore store);



    /**
     * 根据项目id获取模型
     * @param projectId
     * @return
     */
    List<AgBimFile> findByProjectId(String projectId);
}
