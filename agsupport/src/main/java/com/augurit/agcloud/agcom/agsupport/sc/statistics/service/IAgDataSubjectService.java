package com.augurit.agcloud.agcom.agsupport.sc.statistics.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgDataSubject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangmingyang
 * @Description: 数据专题接口
 * @date 2019-09-02 10:04
 */
public interface IAgDataSubjectService {
    int deleteById(String id);
    int deletcByXpath(String xpath);
    int insert(AgDataSubject record);

    AgDataSubject selectById(String id);

    AgDataSubject selectByName(String name);

    List<AgDataSubject> findByXpath(String xpath);
    int updateSubject(AgDataSubject record);

    List<AgDataSubject> findAllSubject();

    List<AgDataSubject> selectByParenIdAndName(String parentId,String name);

    List<AgDataSubject> selectByIds(String[] ids);

    void updateSubjectBatch(List<AgDataSubject> list);

    void deleteSubjects(String id);

    List<AgDataSubject> findByLevel(int level);
}
