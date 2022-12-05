package com.augurit.agcloud.agcom.agsupport.sc.dic.converter;

import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :15:58 2018/8/6
 * @Modified By:
 */
public class AgDicConverter {

    /**
     * 转换agcloud角色对象为agcom
     * @param srcList
     * @param destList
     * @return
     */
    public List<AgDic> convertToList(List<AgDic> srcList, List<Map> destList){
        if (srcList == null) srcList = new ArrayList<>();
        for (Map map : destList) {
            AgDic agDic = new AgDic();
            if (map.containsKey("label") && StringUtils.isNotBlank(map.get("label").toString())){
                agDic.setName(map.get("label").toString());
            }
            if (map.containsKey("value") && StringUtils.isNotBlank(map.get("value").toString())){
                agDic.setCode(map.get("value").toString());
                agDic.setValue(map.get("value").toString());
            }

            if (map.containsKey("typeCode") && com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils.isNotEmpty(map.get("typeCode").toString())) {
                agDic.setTypeCode(map.get("typeCode").toString());
            }
            if (map.containsKey("typeName") && com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils.isNotEmpty(map.get("typeName").toString())) {
                agDic.setTypeName(map.get("typeName").toString());
            }

            if (map.containsKey("itemName") && com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils.isNotEmpty(map.get("itemName").toString())) {
                agDic.setName(map.get("itemName").toString());
            }
            if (map.containsKey("itemCode") && com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils.isNotEmpty(map.get("itemCode").toString())) {
                agDic.setCode(map.get("itemCode").toString());
                agDic.setValue(map.get("itemCode").toString());
            }

            srcList.add(agDic);
        }
        return srcList;
    }

}
