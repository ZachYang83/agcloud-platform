package com.augurit.agcloud.agcom.agsupport.sc.user.converter;

import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by caokp on 2018-05-03.
 */
public class AgUserConverter {

    /**
     * 转换转换agcloud用户对象为agcom
     *
     * @param agUser
     * @param destMap
     * @return
     */
    public static AgUser convertToBean(AgUser agUser, Map destMap) {
        if (agUser == null) agUser = new AgUser();
        if (destMap.containsKey("userId") && StringUtils.isNotEmpty(destMap.get("userId").toString())) {
            agUser.setId(destMap.get("userId").toString());
        }
        if (destMap.containsKey("loginName") && StringUtils.isNotEmpty(destMap.get("loginName").toString())) {
            agUser.setLoginName(destMap.get("loginName").toString());
        }
        if (destMap.containsKey("userName") && StringUtils.isNotEmpty(destMap.get("userName").toString())) {
            agUser.setUserName(destMap.get("userName").toString());
        }
        if (destMap.containsKey("isActive") && StringUtils.isNotEmpty(destMap.get("isActive").toString())) {
            agUser.setIsActive(destMap.get("isActive").toString());
        }
        if (destMap.containsKey("loginPwd") && StringUtils.isNotEmpty(destMap.get("loginPwd").toString())) {
            agUser.setPassword(destMap.get("loginPwd").toString());
        }
        return agUser;
    }

    /**
     * 转换agcloud用户对象列表为agcom
     *
     * @param srcList
     * @param destList
     * @return
     */
    public static List<AgUser> convertToBeanList(List<AgUser> srcList, List<Map> destList) {
        if (srcList == null) srcList = new ArrayList<>();
        for (Map map : destList) {
            AgUser aguser = new AgUser();
            AgUser user = convertToBean(aguser, map);
            srcList.add(user);
        }
        return srcList;
    }


    /**
     * 转换agcloud角色对象为agcom
     *
     * @param srcList
     * @param destList
     * @return
     */
    public List<AgUser> convertToList(List<AgUser> srcList, List<Map> destList) {
        if (srcList == null) srcList = new ArrayList<>();
        for (Map map : destList) {
            AgUser aguser = new AgUser();
            if (map.containsKey("userName") && com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils.isNotEmpty(map.get("userName").toString())) {
                aguser.setUserName(map.get("userName").toString());
            }

            if (map.containsKey("userId") && com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils.isNotEmpty(map.get("userId").toString())) {
                aguser.setId(map.get("userId").toString());
            }

            if (com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils.isNotEmpty(aguser.getId())) {
                srcList.add(aguser);
            }
        }
        return srcList;
    }

}
