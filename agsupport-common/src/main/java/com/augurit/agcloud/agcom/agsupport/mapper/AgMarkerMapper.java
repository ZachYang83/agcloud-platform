package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgMarkRemind;
import com.augurit.agcloud.agcom.agsupport.domain.AgMarker;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserMarker;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017-12-29.
 */
@Mapper
@Repository
public interface AgMarkerMapper {


    List<AgMarker> getMarkers(@Param("userId") String userId) throws Exception;

    AgMarker getMarkerbyId(@Param("id") String id) throws Exception;

    void saveMarker(AgMarker agMarker) throws Exception;

    void saveUserMarker(AgUserMarker agUserMarker) throws Exception;
    void updateUserMarker(AgUserMarker agUserMarker) throws Exception;
    void deleteMarker(@Param("id") String id) throws Exception;

    void deleteUserMarkerbymarkId(@Param("markId") String markId) throws Exception;

    void updateMarker(AgMarker agMarker) throws Exception;

    void deleteUserMarker(@Param("userId") String userId, @Param("markId") String markId) throws Exception;
    AgUserMarker getUserMarker(@Param("userId") String userId, @Param("markId") String markId) throws Exception;

    List<AgUserMarker> getUserMarkerByUserAndView(@Param("userId") String userId, @Param("viewState") String viewState) throws Exception;

    void saveMarkRemind(AgMarkRemind agMarkRemind) throws Exception;
    void updateMarkRemind(AgMarkRemind agMarkRemind)throws Exception;
    AgMarkRemind findMarkRemindByUserId(@Param("userId") String userId);
}
