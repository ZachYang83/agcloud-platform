package com.augurit.agcloud.agcom.agsupport.util;

import com.augurit.agcloud.agcom.agsupport.common.config.AgToken;
import com.augurit.agcloud.agcom.agsupport.common.config.OpusRestConstant;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanghh on 2020/3/12.
 */
@Component
public class AgDicUtils {

    @Value("${agcloud.opus.admin.orgId}")
    private String orgId;
    @Value("${opus-rest-url}")
    private String opusRestUrl;

    @Autowired
    private AgToken agToken;

    public  List<AgDic> getAgDicByTypeCode(String typeCode) {
        String token = agToken.checkToken();
        Map<String, String> param = new HashMap<>();
        param.put("typeCode", typeCode);
        String url = opusRestUrl + OpusRestConstant.lgetItemsByTypeCode;
        String result = HttpClientUtil.getByToken(url, param, token, "utf-8");
        JSONArray jsonArray = JSONArray.fromObject(result);
        List<AgDic> list = this.convertToList(null, jsonArray);
        return list;
    }

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

            if (map.containsKey("typeCode") && StringUtils.isNotEmpty(map.get("typeCode").toString())) {
                agDic.setTypeCode(map.get("typeCode").toString());
            }
            if (map.containsKey("typeName") && StringUtils.isNotEmpty(map.get("typeName").toString())) {
                agDic.setTypeName(map.get("typeName").toString());
            }

            if (map.containsKey("itemName") && StringUtils.isNotEmpty(map.get("itemName").toString())) {
                agDic.setName(map.get("itemName").toString());
            }
            if (map.containsKey("itemCode") && StringUtils.isNotEmpty(map.get("itemCode").toString())) {
                agDic.setCode(map.get("itemCode").toString());
                agDic.setValue(map.get("itemCode").toString());
            }

            srcList.add(agDic);
        }
        return srcList;
    }
}
