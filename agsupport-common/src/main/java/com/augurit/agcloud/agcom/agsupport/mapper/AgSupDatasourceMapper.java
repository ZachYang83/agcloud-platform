package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgSupDatasource;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgSupDatasourceMapper {
    int deleteByPrimaryKey(String id) throws Exception;
    int insert(AgSupDatasource record)throws Exception;
    AgSupDatasource selectByPrimaryKey(String id)throws Exception;
    AgSupDatasource selectByDataSourceName(String name) throws Exception;
    int updateDataSource(AgSupDatasource record)throws Exception;

    List<AgSupDatasource> findList(@Param("name") String name) throws Exception;
    List<AgSupDatasource> findAllList() throws Exception;
}