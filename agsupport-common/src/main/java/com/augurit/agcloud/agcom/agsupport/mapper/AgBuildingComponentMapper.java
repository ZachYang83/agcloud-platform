package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgBuildingComponent;
import com.augurit.agcloud.agcom.agsupport.domain.AgBuildingComponent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: libc
 * @Description: 建筑构件-Mapper
 * @Date: 2020/9/3 11:48
 * @Version: 1.0
 */
@Mapper
public interface AgBuildingComponentMapper {

    int deleteByPrimaryKey(String id);

    /**
     * 根据id 批量删除记录
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") String[] ids);

    int insert(AgBuildingComponent record);

    int insertSelective(AgBuildingComponent record);

    AgBuildingComponent selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AgBuildingComponent record);

    int updateByPrimaryKey(AgBuildingComponent record);

    List<AgBuildingComponent> findList(@Param("name") String name);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据类目编码查询对象
     * @param code 类目编码
     * @Return
     * @Date 2020/9/3 11:55
     */
//    AgBuildingComponent findByCode(String code);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据前端条件参数查询对象 (rest使用 ：API接口)
     * @param
     * @Return
     * @Date 2020/9/3 11:57
     */
  /*  List<AgBuildingComponent> findByParam(@Param("map") Map<String, String> map);*/

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据前端条件查询对应集合 (rest使用 ：API接口)
     * @param
     *      tableCode:表代码(两位阿拉伯数字：01)
     *      largeCode:大类代码(两位阿拉伯数字：01)
     *      mediumCode:中类代码(两位阿拉伯数字：01)
     *      smallCode:小类代码(两位阿拉伯数字：01)
     *      detailCode:细类代码(两位阿拉伯数字：01)
     *      name:类目名称（中文/英文）
     * @Return
     * @Date 2020/9/4 16:26
     */
    List<AgBuildingComponent> findByParam(@Param("tableCode") String tableCode,@Param("largeCode") String largeCode, @Param("mediumCode")String mediumCode,@Param("smallCode") String smallCode, @Param("detailCode")String detailCode, @Param("name")String name);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 批量保存构件对象 (部分字段，sql不可用)
     * @param bcList 建筑构件对象集合
     * @Return
     * @Date 2020/9/7 11:53
     */
//    void insertListSelective(@Param("bcList")List<AgBuildingComponent> bcList);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 批量保存构件对象 (所有字段)
     * @param bcList 建筑构件对象集合
     * @Return
     * @Date 2020/9/7 11:53
     */
    void insertList(@Param("bcList")List<AgBuildingComponent> bcList);
}
