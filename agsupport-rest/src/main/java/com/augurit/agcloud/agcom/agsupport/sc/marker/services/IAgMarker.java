package com.augurit.agcloud.agcom.agsupport.sc.marker.services;


import com.augurit.agcloud.agcom.agsupport.domain.AgMarkRemind;
import com.augurit.agcloud.agcom.agsupport.domain.AgMarker;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserMarker;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * Created by yzq on 2019-10-20.
 */
public interface IAgMarker {
    List getMarkers(String userId) throws Exception;
    void shareMarker(String userId,String markId,String userName) throws Exception;
    void deleteMarker(String userId,String markId) throws Exception;
    void saveMarker(AgMarker agMarker, String userId) throws Exception;
    void updateMarker(AgMarker agMarker,String userId) throws Exception;
    void readMarker(String userId,String markId) throws Exception;

    AgUserMarker getUserMarker(String userId, String markId) throws Exception;

    List<AgUserMarker> getUserMarkerByUserAndView(String userId,String viewState) throws Exception;

    void saveMarkRemind(AgMarkRemind agMarkRemind) throws Exception;
    void updateMarkRemind(AgMarkRemind agMarkRemind)throws Exception;
    AgMarkRemind findMarkRemindByUserId(@Param("userId") String userId);
}
