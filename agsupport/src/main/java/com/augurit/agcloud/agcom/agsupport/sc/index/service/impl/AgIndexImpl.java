package com.augurit.agcloud.agcom.agsupport.sc.index.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.config.AgToken;
import com.augurit.agcloud.agcom.agsupport.common.config.OpusRestConstant;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.domain.AgMenu;
import com.augurit.agcloud.agcom.agsupport.domain.AgMenuDir;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.sc.index.service.IAgIndex;
import com.augurit.agcloud.agcom.agsupport.sc.menuDir.service.IAgMenuDir;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.agcom.agsupport.util.HttpClientUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.common.util.Common;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import com.common.util.JsonUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-04-14.
 */
@Service
public class AgIndexImpl implements IAgIndex {

    @Autowired
    private IAgDic iAgDic;
    @Autowired
    private IAgUser iAgUser;
    @Autowired
    private IAgMenuDir iAgMenuDir;
    @Value("${agcloud.inf.load}")
    private Boolean agcloudInfLoad;
    @Value("${opus-rest-url}")
    private String opusRestUrl;

    @Autowired
    private AgToken agToken;
 /*   @Override
    public List<Map> getIndexMenu(String userName) throws Exception {
        List<Map> menus = new ArrayList<>();
        AgUser agUser = iAgUser.findUserByName(userName);
        String userId = agUser.getId();
        List<AgDic> agDics = iAgDic.getAgDicByTypeCode("c7aa4425-a9a0-4eb4-8847-7219ea87b3be");
        List<AgMenu> agMenus = iAgMenu.findMenuByUserId(userId);
        for (int i = 0; i < agDics.size(); i++) {
            List<Map> items = new ArrayList<>();
            AgDic agDic = agDics.get(i);
            for (int j = 0; j < agMenus.size(); j++) {
                AgMenu agMenu = agMenus.get(j);
                if(agMenu.getPcode() == null || agMenu.getPcode().equals("")){
                    continue;
                }
                if (agMenu.getPcode().equals(agDic.getCode()) && agMenu.getIsUse().equals("1")) {
                    Map item = new HashMap();
                    item.put("id", agMenu.getId());
                    item.put("name", agMenu.getName());
                    item.put("url", agMenu.getUrl());
                    item.put("icon", agMenu.getIcon());
                    item.put("isRelpath", agMenu.getIsRelpath());
                    items.add(item);
                }
            }
            if (items != null && items.size() > 0) {
                Map menu = new HashMap();
                menu.put("id", agDic.getId());
                menu.put("code", agDic.getCode());
                menu.put("name", agDic.getName());
                menu.put("items", items);
                menus.add(menu);
            }
        }
        return menus;
    }*/

/*    @Override
    public List<Map> getIndexMenuDir(String userName) throws Exception {
        List<Map> menus = new ArrayList<>();
        AgUser agUser = iAgUser.findUserByName(userName);
        String userId = agUser.getId();
        this.getMenus(userId,menus);
//        this.recursiveGetMenus(userId,"root",menus);
        return menus;
    }*/

    /**
     * 新增 获取左边菜单树
     * @param userName
     * @param dirId
     * @return
     * @throws Exception
     */
  /*  @Override
    public List<Map> getIndexMenuDirTree(String userName, String dirId, String xPath) throws Exception {
        AgUser agUser = iAgUser.findUserByName(userName);
        String userId = agUser.getId();
        List<Map> menuDirs = new ArrayList<>();
        AgMenuDir agMenuDir = new AgMenuDir();
        agMenuDir.setParentId(dirId);
        List<AgMenuDir> agMenuDirs = iAgMenuDir.findMenuDir(agMenuDir);
        for(AgMenuDir amd : agMenuDirs){
            Map dir = new HashMap();
            dir.put("id", amd.getId());
            dir.put("code", amd.getDirSeq());
            dir.put("name", amd.getName());
            dir.put("icon", amd.getIcon());
            dir.put("xPath", amd.getXpath());
            dir.put("type", "dir");
            menuDirs.add(dir);
        }
        if(xPath != null){
            List<AgMenu> agMenus = iAgMenu.findMenuByXpathUser(new AgMenu(), xPath, userId);
            for(AgMenu am : agMenus){
                if(am.getDirId().equals(dirId)){//只拿当前目录下的
                    Map menu = new HashMap();
                    menu.put("id", am.getId());
                    menu.put("name", am.getName());
                    menu.put("icon", am.getIcon());
                    menu.put("xPath", am.getXpath());
                    menu.put("url", am.getUrl());
                    menu.put("isRelpath", am.getIsRelpath());
                    menu.put("type", "menu");
                    menuDirs.add(menu);
                }
            }
        }
        return menuDirs;
    }*/


    /**
     * 通过接口获取菜单信息
     * @param userName
     * @param dirId
     * @return
     * @throws Exception
     */
    public List<Map> getIndexMenuDirTree(String userName, String dirId, String xPath) throws Exception {
        String userId = getOpusAdminOrgId(userName);
        return listUserMenusRest(userId);
    }
    public List<Map> listUserMenusRest(String userId)throws Exception{
        boolean isTree = false;
        String token = agToken.checkToken();
        String url = opusRestUrl + OpusRestConstant.listUserMenus;
        String currentOrgId = SecurityContext.getCurrentOrgId();
        Map map = new HashMap();
        map.put("isTree",String.valueOf(isTree));
        map.put("orgId",currentOrgId);
        map.put("userId",userId);
        map.put("tmnId","1");
        map.put("netName","后端网络入口");
        map.put("appSoftCode","agcom-admin");
        String result = HttpClientUtil.getByToken(url, map, token, "utf-8");
        List<Map> mapList = JsonUtils.toList(result,HashMap.class);
        List<Map> dirMapList = new ArrayList<>();
        //得到所有父菜单
        for (Map rootMap : mapList){
            Object parentMenuId = rootMap.get("parentMenuId");
            if (null == parentMenuId){
                Map dirMap = new HashMap();
                dirMap.put("id", rootMap.get("menuId"));
                dirMap.put("name", rootMap.get("menuName"));
                dirMap.put("icon", rootMap.get("smallImgPath"));
                dirMap.put("xPath", "");
                dirMap.put("type", "dir");
                dirMapList.add(dirMap);
            }
        }
        Pattern pattern = Pattern.compile("/");
        for(Map rootmMenuMap:dirMapList){
            String id = rootmMenuMap.get("id").toString();
            List<Map> menuList = new ArrayList<>();
            for (Map menuMap : mapList){
                Object parentMenuId = menuMap.get("parentMenuId");
                if (null != parentMenuId && id.equals(parentMenuId.toString())){
                    Map menu = new HashMap();
                    menu.put("id", menuMap.get("menuId"));
                    menu.put("name", menuMap.get("menuName"));
                    String menuInnerUrl = menuMap.get("menuInnerUrl").toString();
                    //这里是获取"/"符号的位置
                    Matcher slashMatcher = pattern.matcher(menuInnerUrl);
                    int mIdx = 0;
                    while(slashMatcher.find()) {
                        mIdx++;
                        //当"/"符号第三次出现的位置
                        if(mIdx == 3){
                            break;
                        }
                    }
                    int index = slashMatcher.start();
                    // 去掉前缀，截取URL地址
                    String substring = menuInnerUrl.substring(index, menuInnerUrl.length());
                    menu.put("icon", menuMap.get("smallImgPath"));
                    menu.put("url",substring);
                    menu.put("xPath", "");
                    menu.put("type", "menu");
                    menu.put("isRelpath","1");
                    menuList.add(menu);
                }
            }
            rootmMenuMap.put("menuData",menuList);
        }
        return dirMapList;
    }

    /**
     * 调用agcloud获取用户信息接口，根据用户名获取用户的id
     * @param userName
     * @return
     */
    public String getOpusAdminOrgId(String userName) throws Exception{
        String token = agToken.checkToken();
        //String getOpuOmUserInfoByUserId = "/rest/opus/om/getOpuOmUserInfoByUserId.do";
        Map umap = new HashMap();
        umap.put("userName",userName);
        String url = opusRestUrl + OpusRestConstant.getOpuOmUserInfoByUserId;
        String result = HttpClientUtil.getByToken(url, umap, token, "utf-8");
        //HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl.concat(getOpuOmUserInfoByUserId), umap);
        JSONObject jsonObject = JSONObject.fromObject(result);
        String userId = jsonObject.getString("id");
        return userId;
    }

    /**
     * 旧的 获取系统菜单接口
     * @param userId
     * @param menus
     * @throws Exception
     */
/*    private void getMenus(String userId, List<Map> menus) throws Exception{
        AgMenuDir agMenuDir = new AgMenuDir();
        agMenuDir.setParentId("root");
        List<AgMenuDir> agMenuDirs = iAgMenuDir.findMenuDir(agMenuDir);
        for (int i = 0; i < agMenuDirs.size(); i++) {
            List<Map> items = new ArrayList<>();
            AgMenuDir dir = agMenuDirs.get(i);
            List<AgMenu> agMenus = iAgMenu.findMenuByXpathUser(new AgMenu(), dir.getXpath(), userId);
            for (int j = 0; j < agMenus.size(); j++) {
                AgMenu agMenu = agMenus.get(j);
                if("1".equals(agMenu.getIsUse())){
                    Map item = new HashMap();
                    item.put("id", agMenu.getId());
                    item.put("name", agMenu.getName());
                    item.put("url", agMenu.getUrl());
                    item.put("icon", agMenu.getIcon());
                    item.put("isRelpath", agMenu.getIsRelpath());
                    items.add(item);
                }
            }
            if (items != null && items.size() > 0) {
                Map menu = new HashMap();
                menu.put("id", dir.getId());
                menu.put("code", dir.getDirSeq());
                menu.put("name", dir.getName());
                menu.put("icon", dir.getIcon());
                menu.put("items", items);
                menus.add(menu);
            }
        }
    }*/
    /**
     * 递归获取菜单树，新的
     * @return
     * @throws Exception
     */
   /* private List<Map> recursiveGetMenus(String userId,String parentId,List<Map> menus) throws Exception{
        AgMenuDir agMenuDir = new AgMenuDir();
        agMenuDir.setParentId(parentId);
        List<AgMenuDir> agMenuDirs = iAgMenuDir.findMenuDir(agMenuDir);
        for (int i = 0; i < agMenuDirs.size(); i++) {
            List<Map> items = new ArrayList<>();
            AgMenuDir dir = agMenuDirs.get(i);
            AgMenuDir subAgMenuDir = new AgMenuDir();
            subAgMenuDir.setParentId(dir.getId());
            List<AgMenuDir> subAgMenuDirs = iAgMenuDir.findMenuDir(subAgMenuDir);
            if(subAgMenuDirs != null && subAgMenuDirs.size() > 0){
                recursiveGetMenus(userId,dir.getId(),menus);
            }else{
                List<AgMenu> agMenus = iAgMenu.findMenuByXpathUser(new AgMenu(), dir.getXpath(), userId);
                for (int j = 0; j < agMenus.size(); j++) {
                    AgMenu agMenu = agMenus.get(j);
                    if("1".equals(agMenu.getIsUse())){
                        Map item = new HashMap();
                        item.put("id", agMenu.getId());
                        item.put("name", agMenu.getName());
                        item.put("url", agMenu.getUrl());
                        item.put("icon", agMenu.getIcon());
                        items.add(item);
                    }
                }
                if (items != null && items.size() > 0) {
                    Map menu = new HashMap();
                    menu.put("id", dir.getId());
                    menu.put("code", dir.getDirSeq());
                    menu.put("name", dir.getName());
                    menu.put("icon", dir.getIcon());
                    menu.put("items", items);
                    menus.add(menu);
                }
            }
        }
        return menus;
    }*/

    @Override
    public String getTitle() throws Exception {
        AgDic agDic = iAgDic.findAgDicByCode("A118");
        if (agDic != null) {
            return agDic.getName();
        }
        return null;
    }
}
