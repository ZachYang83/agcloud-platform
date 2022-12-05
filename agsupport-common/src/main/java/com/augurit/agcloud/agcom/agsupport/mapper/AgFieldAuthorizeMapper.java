package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgFieldAuthorize;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Augurit on 2017-06-07.
 */
@Mapper
public interface AgFieldAuthorizeMapper {

    /**
     * 按角色目录图层id查找
     *
     * @param roleLayerId
     * @return
     * @throws Exception
     */
    List<AgFieldAuthorize> findListByRoleLayerId(@Param("roleLayerId") String roleLayerId) throws Exception;

    /**
     * 按字段id查找
     *
     * @param fieldId
     * @return
     * @throws Exception
     */
    List<AgFieldAuthorize> findListByFieldId(@Param("fieldId") String fieldId) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @throws Exception
     */
    void delete(@Param("id") String id) throws Exception;
}
