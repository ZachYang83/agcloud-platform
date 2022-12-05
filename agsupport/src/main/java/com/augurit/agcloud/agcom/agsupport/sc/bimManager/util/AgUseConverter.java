package com.augurit.agcloud.agcom.agsupport.sc.bimManager.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date: 2020/09/05
 * @Description:
 */
public class AgUseConverter {

   /**
    *
    * @Author: qinyg
    * @Date: 2020/09/11 14:54
    * @tips:  把map对象转换成AgUserCustom实体对象
    * @param agUser 实体对象
    * @param destMap map对象
    * @return 返回值是AgUserCustom实体对象
    */
    public static AgUserCustom convertToBean(AgUserCustom agUser, Map destMap) {
        if (agUser == null) agUser = new AgUserCustom();
        if (destMap.containsKey("userId") && destMap.get("userId") != null && StringUtils.isNotEmpty(destMap.getOrDefault("userId","").toString())) {
            agUser.setUserId(destMap.get("userId").toString());
        }
        if (destMap.containsKey("loginName") && destMap.get("loginName") != null && StringUtils.isNotEmpty(destMap.getOrDefault("loginName","").toString())) {
            agUser.setUserName(destMap.get("loginName").toString());
        }
        if(destMap.containsKey("createTime") && destMap.get("createTime") != null && StringUtils.isNotEmpty(destMap.getOrDefault("createTime","").toString())){
            agUser.setCreateTime(Long.valueOf(destMap.get("createTime").toString()));
        }
        if(destMap.containsKey("userMobile") && destMap.get("userMobile") != null && StringUtils.isNotEmpty(destMap.getOrDefault("userMobile","").toString())){
            agUser.setUserMobile(destMap.get("userMobile").toString());
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
    public static List<AgUserCustom> convertToBeanList(List<AgUserCustom> srcList, List<Map> destList) {
        if (srcList == null) srcList = new ArrayList<>();
        for (Map map : destList) {
            AgUserCustom aguser = new AgUserCustom();
            AgUserCustom user = convertToBean(aguser, map);
            srcList.add(user);
        }
        return srcList;
    }


    /**
     *
     * @param srcList
     * @param destList
     * @return
     */
    public List<AgUserCustom> convertToList(List<AgUserCustom> srcList, List<Map> destList) {
        if (srcList == null) srcList = new ArrayList<>();
        for (Map map : destList) {
            AgUserCustom aguser = new AgUserCustom();
            if (map.containsKey("userName") && StringUtils.isNotEmpty(map.getOrDefault("userName","").toString())) {
                aguser.setUserName(map.get("userName").toString());
            }

            if (map.containsKey("userId") && StringUtils.isNotEmpty(map.getOrDefault("userId","").toString())) {
                aguser.setUserId(map.get("userId").toString());
            }

            if (StringUtils.isNotEmpty(aguser.getUserId())) {
                srcList.add(aguser);
            }
        }
        return srcList;
    }

}
