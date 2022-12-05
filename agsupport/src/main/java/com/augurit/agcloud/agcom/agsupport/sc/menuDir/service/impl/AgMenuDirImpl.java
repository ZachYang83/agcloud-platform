package com.augurit.agcloud.agcom.agsupport.sc.menuDir.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.config.AgToken;
import com.augurit.agcloud.agcom.agsupport.common.config.OpusRestConstant;
import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.sc.menuDir.service.IAgMenuDir;
import com.augurit.agcloud.agcom.agsupport.sc.user.converter.AgUserConverter;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.agcom.agsupport.util.HttpClientUtil;
import com.common.util.Common;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import com.common.util.JsonUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-07-31.
 */
@Service
public class AgMenuDirImpl implements IAgMenuDir {
    @Value("${agcloud.opus.admin.orgId}")
    private String orgId;
    @Value("${opus-rest-url}")
    private String opusRestUrl;
    @Autowired
    private AgToken agToken;
    @Autowired
    private IAgUser iAgUser;

    @Override
    public List<Object> getMiddleIcon(HttpServletRequest request) {
        String realPath = request.getSession().getServletContext().getRealPath("");
        String relativePath = "/ui-static/agcloud/agcom/ui/sc/index/images/default/";
        String middlePath = realPath + relativePath;
        File imgFolder = new File(middlePath);
        List<Object> list = new ArrayList<Object>();
        if ((imgFolder.exists()) && (imgFolder.isDirectory())) {
            File[] files = imgFolder.listFiles();
            if ((files != null) && (files.length > 0)) {
                for (File file : files) {
                    JSONObject object = new JSONObject();
                    //如果名字包含_hover， 则不加入list
                    if (file.isDirectory() || (file.getName() != null && (file.getName().contains("_hover") || file.getName().contains("_bk")))) {
                        continue;
                    }
                    object.put("name", file.getName());
                    object.put("url", relativePath + file.getName());
                    list.add(object);
                }
            }
        }
        return list;
    }

    @Override
    public List findSoftMenu(String userName) throws Exception {
        String token = agToken.checkToken();
       /* AgUser agUser = new AgUser();
        //调用agcloud接口获取
        Map<String, String> param = new HashMap<>();
        param.put("userName", userName);
        HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/om/getOpuOmUserInfoByUserId.do", param);
        JSONObject jsonObject = JSONObject.fromObject(httpRespons.getContent());
        Map map = JsonUtils.toBean(jsonObject.toString(), HashMap.class);
        agUser = new AgUserConverter().convertToBean(agUser, map);*/
        AgUser agUser = iAgUser.findUserByName(userName);
        String userId = agUser.getId();

        Map<String, String> par = new HashMap<>();
        par.put("orgId", orgId);
        par.put("userId",userId);
        par.put("isAgcloud","1");
        String url = opusRestUrl + OpusRestConstant.listUserApp;
        // 获取agloud用户应用的接口
        //HttpRespons softRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/rs/listUserApp.do", par);
        String result = HttpClientUtil.getByToken(url, par, token, "utf-8");
        List<Map> menuList = JsonUtils.toList(result,HashMap.class);
        return  getAgSoftMenu(menuList);
    }

    public List<AgSoftMenu> getAgSoftMenu(List<Map> maps) {
        List<AgSoftMenu> soft = new ArrayList<>();
        if (maps.size()>0){
            for(Map m : maps){
                Object appSoftId = m.get("appSoftId");
                Object softCode = m.get("softCode");
                Object softName = m.get("softName");
                Object softInnerUrl = m.get("softInnerUrl");
                Object softGovUrl = m.get("softGovUrl");
                Object softOuterUrl = m.get("softOuterUrl");

                Object isImgIcon = m.get("isImgIcon");
                Object smallImgPath = m.get("smallImgPath");
                Object middleImgPath = m.get("middleImgPath");
                Object bigImgPath = m.get("bigImgPath");
                Object hugeImgPath = m.get("hugeImgPath");


                AgSoftMenu agSoftMenu = new AgSoftMenu();
                if (appSoftId != null){
                    agSoftMenu.setId(appSoftId.toString());
                }
                if (softCode != null){
                    agSoftMenu.setSoftCode(softCode.toString());
                }

                if (softName != null){
                    agSoftMenu.setSoftName(softName.toString());
                }
                if (softInnerUrl != null){
                    agSoftMenu.setSoftInnerUrl(softInnerUrl.toString());
                }
                if (softGovUrl != null){
                    agSoftMenu.setSoftGovUrl(softGovUrl.toString());
                }
                if (softOuterUrl != null){
                    agSoftMenu.setSoftOuterUrl(softOuterUrl.toString());
                }
                if (isImgIcon != null){
                    agSoftMenu.setIsImgIcon(isImgIcon.toString());
                }
                if (smallImgPath != null){
                    agSoftMenu.setSmallImgPath(smallImgPath.toString());
                }
                if (middleImgPath != null){
                    agSoftMenu.setMiddleImgPath(middleImgPath.toString());
                }
                if (bigImgPath != null){
                    agSoftMenu.setBigImgPath(bigImgPath.toString());
                }
                if (hugeImgPath != null){
                    agSoftMenu.setHugeImgPath(hugeImgPath.toString());
                }
                soft.add(agSoftMenu);
            }
        }
        return soft;
    }
}
