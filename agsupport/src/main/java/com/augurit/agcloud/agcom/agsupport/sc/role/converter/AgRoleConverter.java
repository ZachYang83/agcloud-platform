package com.augurit.agcloud.agcom.agsupport.sc.role.converter;

import com.augurit.agcloud.agcom.agsupport.domain.AgRole;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by caokp on 2018-04-19.
 */
public class AgRoleConverter {

    /**
     * 转换agcloud角色对象为agcom
     *
     * @param srcList
     * @param destList
     * @return
     */
    public List<AgRole> convertToList(List<AgRole> srcList, List<Map> destList) {
        if (srcList == null) srcList = new ArrayList<>();
        for (Map map : destList) {
            AgRole agRole = new AgRole();
            if (map.containsKey("roleName") && StringUtils.isNotEmpty(map.get("roleName").toString())) {
                agRole.setName(map.get("roleName").toString());
            }

            if (map.containsKey("roleId") && StringUtils.isNotEmpty(map.get("roleId").toString())) {
                agRole.setId(map.get("roleId").toString());
            }

            if (StringUtils.isNotEmpty(agRole.getId())) {
                srcList.add(agRole);
            }
        }
        return srcList;
    }
}
