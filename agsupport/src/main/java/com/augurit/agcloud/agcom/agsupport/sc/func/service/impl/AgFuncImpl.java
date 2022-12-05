package com.augurit.agcloud.agcom.agsupport.sc.func.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.config.AgToken;
import com.augurit.agcloud.agcom.agsupport.common.config.OpusRestConstant;
import com.augurit.agcloud.agcom.agsupport.util.HttpClientUtil;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonMapper;
import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.sc.func.convert.AgFuncConverter;
import com.augurit.agcloud.agcom.agsupport.sc.func.service.IAgFunc;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;

/**
 * Created by Augurit on 2017-05-02.
 */
@Service
public class AgFuncImpl implements IAgFunc {
    @Value("${agcloud.opus.admin.orgId}")
    private String orgId;
    @Value("${opus-rest-url}")
    private String opusRestUrl;
    @Autowired
    private AgToken agToken;

    @Override
    public List<AgFunc> findFuncByUser(String userId, String isMobile, Boolean isTree, String appSoftCode, String isCloudSoft, String isAdmin) throws Exception {
        String token = agToken.checkToken();
        Map map = new HashMap();
        map.put("isTree", String.valueOf(false));
        map.put("orgId", orgId);
        map.put("userId", userId);
        map.put("appSoftCode", appSoftCode);
        map.put("isCloudSoft", isCloudSoft);
        map.put("isAdmin", isAdmin);
        String url = opusRestUrl + OpusRestConstant.listUserFuncs;
        String result = HttpClientUtil.getByToken(url, map, token, "utf-8");
        //HttpRespons httpRespons = new HttpRequester().sendGet(opusAdminUrl+"/rest/opus/rs/listUserFuncs.do", map);
        com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(result);
        Object content = json.get("content");
        if (content == null) {
            return new ArrayList<AgFunc>();
        }
        JSONArray jsonObject = JSONArray.fromObject(content.toString());
        List<Map> mapList = new ArrayList<Map>();
        for (int i = 0; i < jsonObject.size(); i++) {
            JSONObject o = (JSONObject) jsonObject.get(i);
            HashMap m = new HashMap<>();

            Iterator iterator = o.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Object value = o.get(key);
                m.put(key, value);
            }
            mapList.add(m);
        }
        // 过滤掉不需要的字段
        List<AgFunc> agFuncs = new ArrayList<>();
        List<AgFunc> rootFuncs = new ArrayList<>();
        for (Map m : mapList) {
            AgFunc agFunc = new AgFuncConverter().convertToAgFunc(m);
            agFuncs.add(agFunc);
            Object parentFuncId = m.get("parentFuncId");
            if (parentFuncId == null || parentFuncId.equals("null")) {
                rootFuncs.add(agFunc);
            }
        }
        // 如果返回树状功能
        if (isTree) {
            // 转换成树状结构
            for (AgFunc agFunc : rootFuncs) {
                agFunc.setChildrenList(getChildren(agFunc.getId(), agFuncs));
            }
            return rootFuncs;
        } else {
            return agFuncs;
        }
    }

    private List<AgFunc> getChildren(String id, List<AgFunc> list) {
        List<AgFunc> children = new ArrayList<>();
        String parentId;
        for (AgFunc agFunc : list) {
            parentId = agFunc.getParentFuncId();
            if (id.equals(parentId)) {
                children.add(agFunc);
            }
        }
        for (AgFunc agFunc : children) {
            agFunc.setChildrenList(getChildren(agFunc.getId(), list));
        }

        if (children.size() == 0) {
            return null;
        }
        return children;
    }


}
