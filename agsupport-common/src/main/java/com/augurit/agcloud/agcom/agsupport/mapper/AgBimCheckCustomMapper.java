package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: qinyg
 * @Date: 2020/11/3
 * @tips:
 */
@Mapper
public interface AgBimCheckCustomMapper {
    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/04 11:48
     * @tips: 分组查询，且统计分组的个数
     * @param groupKey 分组的key，尽量不要给前端传入
     * @return
     */
    List<AgBimCheck> groupByKey(@Param("groupKey")String groupKey, @Param("sourceId")String sourceId);
}
