package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author lizih
 * @Date 2020/12/21 11:06
 * @Version 1.0
 */
@Mapper
public interface AgBimCheckStandardCustomMapper {
    void insertList(@Param("bimCheckStandardClauseList") List<AgBimCheckStandard> bimCheckStandardClauseList);

    List<String> getCategories();

    List<String> getClauses(@Param("clause_category") String clause_category);

    List<AgBimCheckStandard> getClauseContents(@Param("clause_category") String clause_category, @Param("clause") String clause);

//    List<AgBimCheckStandard> groupByKey(@Param("groupKey") String groupKey);
}
