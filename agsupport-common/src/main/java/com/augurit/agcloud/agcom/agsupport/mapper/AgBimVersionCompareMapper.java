package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimVersionCompare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName AgBimVersionCompareMapper
 * @Author Administrator
 * @Version 1.0
 **/
@Mapper
public interface AgBimVersionCompareMapper {

    /**
     * 根据bim模型查找比对记录
     * @param bimFileId
     * @return
     */
    List<AgBimVersionCompare> getByBimFileId(@Param("bimFileId") String bimFileId);

    /**
     * 保存比对记录
     * @param agBimVersionCompare
     * @return
     */
    int save(AgBimVersionCompare agBimVersionCompare);

    /**
     * 更新记录
     * @param agBimVersionCompare
     * @return
     */
    int update(AgBimVersionCompare agBimVersionCompare);

    /**
     * 根据以下三个参数查找唯一记录
     * @param bimFileId bim模型ID
     * @param bimVersionId1 第一个进行比对的bim模型版本ID（共两个）
     * @param bimVersionId2 第二个进行比对的bim模型版本ID（共两个）
     * @return AgBimVersionCompare 唯一记录
     */
    AgBimVersionCompare getByUnique(@Param("bimFileId") String bimFileId, @Param("bimVersionId1") String bimVersionId1, @Param("bimVersionId2") String bimVersionId2);
}
