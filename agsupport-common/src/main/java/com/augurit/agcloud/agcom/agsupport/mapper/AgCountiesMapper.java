package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgCounties;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Augurit on 2018-05-29.
 */
@Mapper
public interface AgCountiesMapper {

    List<AgCounties> findCounties() throws Exception;

    List<AgCounties> findTown(String xzqhdmCounties) throws Exception;
}
