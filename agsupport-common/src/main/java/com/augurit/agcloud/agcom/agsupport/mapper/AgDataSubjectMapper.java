package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgDataSubject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgDataSubjectMapper {
    int deleteById(String id);
    int deletcByXpath(String xpath);
    int insert(AgDataSubject record);
    AgDataSubject selectById(String id);
    AgDataSubject selectByName(String name);
    List<AgDataSubject> findByXpath(@Param("xpath") String xpath);
    int updateSubject(AgDataSubject record);
    void updateSubjectBatch(List<AgDataSubject> list);
    List<AgDataSubject> findAllSubject();
    List<AgDataSubject> selectByIds(@Param("ids") String[] ids);
    List<AgDataSubject> selectByParenIdAndName(@Param("parentId") String parentId, @Param("name") String name);
    List<AgDataSubject> findByLevel(int level);
}