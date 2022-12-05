package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.config.AgToken;
import com.augurit.agcloud.agcom.agsupport.domain.AgBuildingComponent;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.ISysAgCloudSystemService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.util.*;
import com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.service.IAgBuildingComponentService;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/09
 * @Description:
 */
@Service
public class SysAgCloudSystemServiceImpl implements ISysAgCloudSystemService {
    private static final Logger logger = LoggerFactory.getLogger(SysAgCloudSystemServiceImpl.class);

    @Autowired
    private AgToken agToken;
    @Value("${opus-rest-url}")
    private String opusRestUrl;

    @Autowired
    private IAgDic iAgDic;
    @Autowired
    private IAgBuildingComponentService agBuildingComponentService;

    @Override
    public List<AgUserCustom> getUserList(String orgId) {
        //去aglcoud获取用户列表，再封装参数返回
        List<AgUserCustom> list = new ArrayList<>();
        try{
            String token = agToken.checkToken();
            Map<String, String> param = new HashMap<>();
            param.put("orgId", orgId);
            String url = opusRestUrl + AgcloudUrlUtil.opu_rest_user_list_url;
            String result = HttpClientUtil.getByToken(url, param, token, "utf-8");
            if(!StringUtils.isEmpty(result)) {
                List<Map> listMap = com.augurit.agcloud.framework.util.JsonUtils.parseList(Map.class, result);
                if (listMap != null && listMap.size() > 0) {
                    List<AgUserCustom> agUsers = AgUseConverter.convertToBeanList(list, listMap);
                }
            }
        }catch (Exception e){
            logger.info("--------error getUserList-------------" + e.getMessage());
        }
        return list;
    }

    @Override
    public String getBuildComponentDictionaryName(String tableCode) {
        String tableCodeName = null;
        try{
            List<AgDic> agDicByTypeCode = iAgDic.getAgDicByTypeCode("BUILDING_MODEL_TYPE");
            if(agDicByTypeCode != null && agDicByTypeCode.size() > 0){
                for(AgDic agDic: agDicByTypeCode){
                    if(agDic.getCode().equals(tableCode)){
                        tableCodeName = agDic.getName();
                        break;
                    }
                }
            }
        }catch (Exception e){
            logger.info("-----error    getBuildComponentDictionaryName-----" + e.getMessage());
        }
        return tableCodeName;
    }

    @Override
    public String getBuildComponentName(String filterType, String tableCode, String largeCode, String mediumCode, String smallCode, String detailCode) {
        String name = null;
        try{
            List<AgBuildingComponent> agBuildingComponents = agBuildingComponentService.listByParam(tableCode, largeCode, mediumCode, smallCode, detailCode, null, filterType);
            if(agBuildingComponents != null && agBuildingComponents.size() > 0){
                for(AgBuildingComponent agBuildingComponent : agBuildingComponents){
                    //细类查询，全匹配，只有一条数据
                    if("4".equals(filterType)){
                        name = agBuildingComponent.getChineseName();
                        break;
                    }else{
                        //非细类查询，需要判断返回值细类的属性,是null或者空就是要查询的数据，否则不是
                        if(StringUtils.isEmpty(agBuildingComponent.getDetailCode())){
                            name = agBuildingComponent.getChineseName();
                            break;
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.info("-----error    getBuildComponentName-----" + e.getMessage());
        }
        return name;
    }
}
