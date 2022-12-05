package com.augurit.agcloud.mapper;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName BscDicCodeMapper
 * @Description TODO
 * @Author Administrator
 * @Date 2019/11/25 11:41
 * @Version 1.0
 **/
@Mapper
public interface BscDicCodeAppMapper {


    List<BscDicCodeItem> getActiveItemsByTypeCode(@Param("typeCode")String typeCode,@Param("orgId") String orgId);
}
