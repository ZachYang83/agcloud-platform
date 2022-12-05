package com.augurit.agcloud.agcom.agsupport.sc.role.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.common.config.AgToken;
import com.augurit.agcloud.agcom.agsupport.common.config.OpusRestConstant;
import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.role.converter.AgRoleConverter;
import com.augurit.agcloud.agcom.agsupport.sc.role.service.IAgRole;
import com.augurit.agcloud.agcom.agsupport.util.HttpClientUtil;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import com.common.util.JsonUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Augurit on 2017-04-21.
 */
@Service
public class AgRoleImpl implements IAgRole {
    @Value("${opus-rest-url}")
    private String opusRestUrl;

    @Autowired
    private AgToken agToken;
    @Override
    public PageInfo<AgRole> searchRole(AgRole agRole, Page page) throws Exception {
        String token = agToken.checkToken();
        PageInfo<AgRole> rolePage = null;
        List<AgRole> list = null;
        Map<String, String> param = new HashMap<>();
        param.put("appId", "ff6efd5c672f4e9682e3eec218e5a8a9"); //appId
        param.put("searchKeyWord", agRole.getName());
        param.put("pageNum", String.valueOf(page.getPageNum()));
        param.put("pageSize", String.valueOf(page.getPageSize()));
        String url = opusRestUrl + OpusRestConstant.listAllRolesByAppId;
        String result = HttpClientUtil.getByToken(url, param, token, "utf-8");
        //HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/rs/listAllRolesByAppId.do", param);
        if (StringUtils.isNotBlank(result)){
            JSONObject jsonObject = JSONObject.parseObject(result);
            List<Map> listMap = JsonUtils.toList(jsonObject.get("list").toString(), HashMap.class);
            list = new AgRoleConverter().convertToList(null, listMap);
            rolePage = new PageInfo<>();
            rolePage.setList(list);
            rolePage.setTotal(Long.valueOf(jsonObject.get("total").toString()));
        }
        return rolePage;
    }

}
