package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgHouse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/09
 * @Description:
 */
@Mapper
public interface AgHouseCustomMapper {

    List<AgHouse> select(@Param("hourseName") String hourseName,
                         @Param("isShow") String isShow, @Param("orderBy") String orderBy);


    List<AgHouse> unionSelect(@Param("hourseName") String hourseName,
                              @Param("isShow") String isShow, @Param("orderBy") String orderBy,
                              @Param("permisions") List<String> permisions);

    List<AgHouse> sql(String sql);
}
