package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimVersion;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName AgBimVersionMapper
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/23 14:59
 * @Version 1.0
 **/
@Mapper
public interface AgBimVersionMapper {

    //根据id获取数据
    AgBimVersion getById(@Param("id") String id);

    //根据pkId获取更多数据
    List<AgBimVersion> getByPkId(@Param("pkId") String pkId);

    //获取所有数据
    List<AgBimVersion> getAll();

    //根据id删除一条数据
    int deleteById(@Param("id") String id);

    //删除更多数据
    int deleteMany(@Param("ids") List<String> ids);

    //根pkId删除一条数据
    int deleteByPkId(@Param("pkId") String... pkId);

    //添加一条数据
    int add(AgBimVersion agBimVersion);

    //编辑一条数据
    int update(AgBimVersion agBimVersion);

    //特殊条件Or分页查询
    List<AgBimVersion> getByOrKeyWords(@Param("pkId") String pkId, @Param("keyword") String keyword);

    //特殊条件And分页查询
    List<AgBimVersion> getByAndKeyWords(AgBimVersion agBimVersion);

    //根据id获取更多数据
    List<AgBimVersion> getByIds(@Param("ids") List<String> ids);

    //获取变更类型
    List<BscDicCodeItem> getChangeTypeList(String typeCode);

    //获取一个模型bimFile的最新版本
    AgBimVersion getLatestChangeVersion(@Param("pkId") String pkId);

    //根据md5获取数据
    List<AgBimVersion> getByMd5(@Param("md5") String md5);

    //根据pkId查找当前正在使用的版本
    AgBimVersion getInUseByPkId(@Param("pkId") String pkId);

    //将某一模型下面的所有版本设为未使用
    void setAllNotUse(@Param("pkId") String pkId);

    /**
     * 获取最大的版本号
     * @param bimId
     * @return
     */
    String findMaxVersion(String bimId);


}
