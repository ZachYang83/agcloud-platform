package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimRelationFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * bim关联文件mapper
 * Created by fanghh on 2019/12/5.
 */
@Mapper
public interface AgBimRelationFileMapper {

    /**
     * 查询bim模型关联文件
     * @param bimId
     * @return
     */
    List<AgBimRelationFile> findBimRelationFile(String bimId);

    /**
     * 保存关联记录
     * @param relationFile
     */
    void save(AgBimRelationFile relationFile);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int delete(String id);
}
