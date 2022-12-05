package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgMapParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Augurit on 2017-04-18.
 */
@Mapper
public interface AgParamMapper {

    List<AgMapParam> searchAll() throws Exception;

    /**
     * 按条件查询
     *
     * @param name
     * @return
     * @throws Exception
     */
    List<AgMapParam> findList(@Param("name") String name) throws Exception;

    AgMapParam findById(@Param("id") String id) throws Exception;

    List<AgMapParam> findByName(@Param("name") String name) throws Exception;

    List<AgMapParam> findDefaultMap(@Param("defaultMap") String defaultMap) throws Exception;

    /**
     * 按所有字段查询
     *
     * @param agMapParam
     * @return
     * @throws Exception
     */
    AgMapParam find(AgMapParam agMapParam) throws Exception;

    /**
     * 保存
     *
     * @param agMapParam
     * @throws Exception
     */
    void save(AgMapParam agMapParam) throws Exception;

    /**
     * 批量保存
     *
     * @param list
     * @throws Exception
     */
    void saveBatch(List<AgMapParam> list) throws Exception;

    /**
     * 修改
     *
     * @param agMapParam
     * @throws Exception
     */
    void update(AgMapParam agMapParam) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @throws Exception
     */
    void delete(@Param("id") String id) throws Exception;

    /**
     * 批量删除
     *
     * @param ids
     * @throws Exception
     */
    void deleteBatch(@Param("ids") String[] ids) throws Exception;

    /**
     * 批量删除非默认地图参数
     *
     * @param ids
     * @throws Exception
     */
    void deleteBatchNotDefaultParam(@Param("ids") String[] ids) throws Exception;
}
