package com.augurit.agcloud.agcom.agsupport.sc.dic.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.config.AgToken;
import com.augurit.agcloud.agcom.agsupport.common.config.OpusRestConstant;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.sc.dic.converter.AgDicConverter;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.util.HttpClientUtil;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Augurit on 2017-04-18.
 */
@Service
public class AgDicImpl implements IAgDic {
    @Value("${agcloud.opus.admin.orgId}")
    private String orgId;
    @Value("${opus-rest-url}")
    private String opusRestUrl;
    @Autowired
    private AgToken agToken;

    @Override
    public List<AgDic> getAgDicByTypeCode(String typeCode) throws Exception {
        String token = agToken.checkToken();
        System.out.println("先请求token:" + token);
        Map<String, String> param = new HashMap<>();
        param.put("typeCode", typeCode);
        String url = opusRestUrl + OpusRestConstant.lgetItemsByTypeCode;
        String result = HttpClientUtil.getByToken(url, param, token, "utf-8");

        //HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/bsc/dic/code/lgetItemsByTypeCode.do", param);
        //JSONObject jsonObject = JSONObject.fromObject(httpRespons.getContent());
        JSONArray jsonArray = JSONArray.fromObject(result);
        List<AgDic> list = new AgDicConverter().convertToList(null, jsonArray);
        return list;
    }


    @Override
    public AgDic findAgDicByCode(String code) throws Exception {
        String token = agToken.checkToken();
        Map<String, String> param = new HashMap<>();
        param.put("typeCode", code);
        String url = opusRestUrl + OpusRestConstant.lgetItemsByTypeCode;
        //HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/bsc/dic/code/lgetItemsByTypeCode.do", param);
        //JSONObject jsonObject = JSONObject.fromObject(httpRespons.getContent());
        String result = HttpClientUtil.getByToken(url, param, token, "utf-8");
        JSONArray jsonArray = JSONArray.fromObject(result);
        List<AgDic> list = new AgDicConverter().convertToList(null, jsonArray);
        if (list.size()>0){
            return list.get(0);
        }
        return new AgDic();
    }

    @Override
    public AgDic findValueByCoorName(String name) throws Exception {
        String token = agToken.checkToken();
        Map<String, String> map = new HashMap<>();
        map.put("typeCode", "A103");
        map.put("itemName", name);
        map.put("orgId", orgId);
        //HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/bsc/dic/code/listItemByTypeCodeAndItemName.do", map);
        String url = opusRestUrl + OpusRestConstant.listItemByTypeCodeAndItemName;
        String result = HttpClientUtil.getByToken(url, map, token, "utf-8");
        JSONArray jsonArray = JSONArray.fromObject(result);
        List<AgDic> list = new AgDicConverter().convertToList(null, jsonArray);
        return list.get(0);
    }

    @Override
    public AgDic findValueByCoordTransName(String name) throws Exception {
        //调用agcloud获取
        String token = agToken.checkToken();
        Map<String, String> map = new HashMap<>();
        map.put("typeCode", "B001");
        map.put("itemName", name);
        map.put("orgId", orgId);
        //HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/bsc/dic/code/listItemByTypeCodeAndItemName.do", map);
        String url = opusRestUrl + OpusRestConstant.listItemByTypeCodeAndItemName;
        String result = HttpClientUtil.getByToken(url, map, token, "utf-8");
        JSONArray jsonArray = JSONArray.fromObject(result);
        List<AgDic> list = new AgDicConverter().convertToList(null, jsonArray);
        return list.get(0);
    }
    @Override
    public AgDic getAgDicByTypeCodeAndItemName(String typeCode,String itemName)throws Exception{
        String token = agToken.checkToken();
        Map<String, String> param = new HashMap<>();
        param.put("typeCode", typeCode);
        param.put("itemName", itemName);
        param.put("orgId", orgId);
        String url = opusRestUrl + OpusRestConstant.listItemByTypeCodeAndItemName;
        String result = HttpClientUtil.getByToken(url, param, token, "utf-8");
        JSONArray jsonArray = JSONArray.fromObject(result);
        List<AgDic> list = new AgDicConverter().convertToList(null, jsonArray);
        return list.get(0);
    }
}
