package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgMapCustom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/07
 * @Description:
 */
@Mapper
public interface AgMapMapper {

    List<Map<String, Object>> findAll(@Param("tableName") String tableName, @Param("sqlParam") String sqlParam);


    List<Object> findAllDef(String sql);

    AgMapCustom findBytes(String sql);

    byte[] findBytes2(String sql);
}
