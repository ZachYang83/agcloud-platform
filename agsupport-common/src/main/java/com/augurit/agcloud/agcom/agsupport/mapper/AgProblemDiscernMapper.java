package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgProblemDiscern;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: libc
 * @Description: 问题识别模块mapper接口
 * @Date: 2020/8/28 11:38
 * @Version: 1.0
 */
@Mapper
public interface AgProblemDiscernMapper {
    int deleteByPrimaryKey(String id);

    /**
     * 根据id 批量删除记录
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") String[] ids);

    int insert(AgProblemDiscern record);

    int insertSelective(AgProblemDiscern record);

    AgProblemDiscern selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AgProblemDiscern record);

    int updateByPrimaryKey(AgProblemDiscern record);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据描述字段关键字查询
     * @param description 描述
     * @Return
     * @Date 2020/8/28 11:43
     */
    List<AgProblemDiscern> findList(@Param("description") String description);


    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据图片唯一标识查询记录
     * @param imgId 图片唯一标识
     * @Return
     * @Date 2020/8/28 14:17
     */
    AgProblemDiscern findByImgId(@Param("imgId") String imgId, @Param("pType") String pType);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据图片标识查询对象集合
     * @param imgId 图片唯一标识
     * @Return 集合
     * @Date 2020/9/2 11:42
     */
    List<AgProblemDiscern> findListByImgId(@Param("imgId") String imgId, @Param("pType") String pType);
}
