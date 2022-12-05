package com.augurit.agcloud.agcom.agsupport.sc.user.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.config.AgToken;
import com.augurit.agcloud.agcom.agsupport.common.config.CasConfig;
import com.augurit.agcloud.agcom.agsupport.common.config.OpusRestConstant;
import com.augurit.agcloud.agcom.agsupport.common.config.RedisCache.MapCache;
import com.augurit.agcloud.agcom.agsupport.common.config.condition.RedisCondition;
import com.augurit.agcloud.agcom.agsupport.domain.AgOrg;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserLayer;
import com.augurit.agcloud.agcom.agsupport.mapper.AgUserLayerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgUserMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.Ztree;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.MongoDBHelp;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import com.augurit.agcloud.agcom.agsupport.sc.user.converter.AgUserConverter;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.agcom.agsupport.util.HttpClientUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.common.dbcp.DBHelper;
import com.common.util.Common;
import com.common.util.JsonUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Augurit on 2017-04-27.
 */
@Transactional
@Service
public class AgUserImpl implements IAgUser {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgUserImpl.class);
    private static final String ROWS = "rows";
    private static final String PAGESIZE = "pageSize";
    private static final String TOTAL = "total";

    @Autowired
    private AgUserMapper agUserMapper;
    @Autowired
    private AgUserLayerMapper agUserLayerMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String prod_db = "jdbc";
    @Value("${agcloud.inf.load}")
    private Boolean agcloudInfLoad;
    @Value("${agcloud.opus.admin.orgId}")
    private String adminorgId;
    @Value("${opus-rest-url}")
    private String opusRestUrl;
    @Autowired
    private AgToken agToken;
    /**
     * 调用agcloud查询所有子机构接口
     *
     * @return
     * @throws Exception
     */
    @Override
    public String findOrgTreeById(String orgId) throws Exception {
        String token = agToken.checkToken();
        List<Ztree> ztrees = new ArrayList<>();
        if (agcloudInfLoad.booleanValue()) {
            List<AgOrg> list = new ArrayList<>();
            //String rest = "/rest/opus/om/getOpuOmOrgAsyncZTree.do?orgId=";
            String url = opusRestUrl + OpusRestConstant.getOpuOmOrgAsyncZTree;
            Map paramsMap = new HashMap();
            if (StringUtils.isNotBlank(orgId)) {
                paramsMap.put("orgId", orgId);
            } else {
                paramsMap.put("orgId", adminorgId);
            }
            //HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl.concat(rest));
            String result = HttpClientUtil.getByToken(url, paramsMap, token, "utf-8");
            System.out.println(result);
            List<Map> mapList = JsonUtils.toList(result, HashMap.class);
            for (Map map : mapList) {
                AgOrg agOrg = new AgOrg();
                agOrg.setId(map.get("orgId").toString());
                agOrg.setName(map.get("orgName").toString());
                agOrg.setOrgCode(map.get("orgCode").toString());
                agOrg.setParentOrgCode(null != map.get("parentOrgId") ? map.get("parentOrgId").toString() : null);
                agOrg.setParent(Boolean.valueOf(map.get("isParent").toString()));
                list.add(agOrg);
            }
            // 构造机构树
        /*    for (AgOrg agOrg : list) {
                Tree tree = new Tree();
                tree.setId(agOrg.getId());
                tree.setText(agOrg.getName());
                tree.setPid(agOrg.getParentOrgCode());
                if (agOrg.getParent()){
                    tree.setState("closed");
                }
                tree.setIconCls("icon-folder");
                ztrees.add(tree);
            }*/
            for (AgOrg agOrg : list) {
                Ztree ztree = new Ztree();
                ztree.setId(agOrg.getId());
                ztree.setName(agOrg.getName());
                ztree.setpId(agOrg.getParentOrgCode());
                if (agOrg.getParent()) {
                    ztree.setOpen("true");
                } else {
                    ztree.setOpen("false");
                }
                ztree.setIsParent(String.valueOf(agOrg.getParent()));
                ztrees.add(ztree);
            }


        }
        return com.augurit.agcloud.framework.util.JsonUtils.toJson(ztrees);
    }

    @Override
    public List<AgUser> findUserByRoleId(String roleId) throws Exception {
        String token = agToken.checkToken();
        Map<String, String> param = new HashMap<>();
        param.put("roleId", roleId);
        String url = opusRestUrl + OpusRestConstant.listOpuOmUserByRoleId;
        //HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/om/listOpuOmUserByRoleId.do", param);
        String result = HttpClientUtil.getByToken(url, param, token, "utf-8");
        List<Map> listMap = JsonUtils.toList(result, HashMap.class);
        List<AgUser> userList = new ArrayList<>();
        List<AgUser> agUserList = new AgUserConverter().convertToBeanList(userList, listMap);
        return agUserList;
    }

    @Override
    public List<AgUser> findUsersByRoleIds(String roleIds) throws Exception {
        String token = agToken.checkToken();
        Map<String, String> param = new HashMap<>();
        List<AgUser> agUserList = new ArrayList<>();
        if (StringUtils.isNotBlank(roleIds)) {
            param.put("roleIds", roleIds);
            //HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/om/listOpuOmUserByRoleId.do", param);
            String url = opusRestUrl + OpusRestConstant.listOpuOmUserByRoleIdsNoPage;
            String result = HttpClientUtil.getByToken(url, param, token, "utf-8");
            List<Map> listMap = JsonUtils.toList(result, HashMap.class);
            List<AgUser> userList = new ArrayList<>();
            agUserList = new AgUserConverter().convertToBeanList(userList, listMap);
        }
        return agUserList;
    }

    @Override
    public PageInfo<AgUser> findUsersByRoleIds(String roleIds,String userName, Page page) {
        String token = agToken.checkToken();
        Map<String, String> param = new HashMap<>();
        List<AgUser> agUserList = new ArrayList<>();
        PageInfo<AgUser> pageInfo = new PageInfo<>();
        if (StringUtils.isNotBlank(roleIds)) {
            param.put("roleIds", roleIds);
            param.put("pageNum", String.valueOf(page.getPageNum()));
            param.put("pageSize", String.valueOf(page.getPageSize()));
            String url = opusRestUrl + OpusRestConstant.listOpuOmUserByRoleIds;
            String result = HttpClientUtil.getByToken(url, param, token, "utf-8");
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);
            List<Map> listMap = JsonUtils.toList(jsonObject.get(ROWS).toString(), HashMap.class);
            List<AgUser> userList = new ArrayList<>();
            agUserList = new AgUserConverter().convertToBeanList(userList, listMap);
            if(!Common.isCheckNull(userName)){
                agUserList = agUserList.stream().filter(u->u.getUserName()!=null&&u.getUserName().contains(userName)).collect(Collectors.toList());
            }
            pageInfo.setTotal(Long.valueOf(jsonObject.get(TOTAL).toString()));
            pageInfo.setList(agUserList);
        }
        return pageInfo;
    }

    /**
     * 根据登录名获取用户角色集合
     * @param userName
     * @return
     */
    public List<Map> getRolesByUserName(String userName){
        String token = agToken.checkToken();
        Map<String, String> param = new HashMap<>();
        List<Map> listMap = new ArrayList<>();
        if (StringUtils.isNotBlank(userName)) {
            param.put("userName", userName);
            String url = opusRestUrl + OpusRestConstant.getRolesByUserName;
            String result = HttpClientUtil.getByToken(url, param, token, "utf-8");
            JSONArray jsonArray = JSONArray.fromObject(result);
            listMap = JsonUtils.toList(jsonArray.toString(), HashMap.class);
        }
        return listMap;
    }

    public List<Map> listRoleByRoleIds(String roleIds){
        String token = agToken.checkToken();
        Map<String, String> param = new HashMap<>();
        List<Map> listMap = new ArrayList<>();
        if (StringUtils.isNotBlank(roleIds)) {
            param.put("roleIds", roleIds);
            param.put("pageNum","1");
            param.put("pageSize","100");
            String url = opusRestUrl + OpusRestConstant.listRoleByRoleIds;
            String result = HttpClientUtil.getByToken(url, param, token, "utf-8");
            if (StringUtils.isNotBlank(result)){
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);
                listMap = JsonUtils.toList(jsonObject.get("list").toString(), HashMap.class);
            }
        }
        return listMap;
    }
    @Override
    public PageInfo<AgUser> searchUser(AgUser agUser, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AgUser> list = agUserMapper.findList(agUser);
//        if (list != null && list.size() > 0) {
//            for (AgUser temp : list) {
//                temp.setPassword(DESedeUtil.desDecrypt(temp.getPassword()));
//            }
//
//        }
        return new PageInfo<AgUser>(list);
    }

    @Override
    public PageInfo<AgUser> searchUser(AgUser agUser, Page page, String orgId) throws Exception {
        String token = agToken.checkToken();
        PageInfo<AgUser> pageInfo = new PageInfo<>();
        List<AgUser> list = new ArrayList<>();
        if (agcloudInfLoad.booleanValue()) {
            Map map = new HashMap();
            String orgIdcons = "orgId";
            String userName = "userName";
            if (StringUtils.isNotBlank(orgId)) {
                map.put(orgIdcons, orgId);
            } else {
                map.put(orgIdcons, SecurityContext.getCurrentOrgId());
            }
            map.put(userName, agUser.getUserName());
            map.put("pageNum", String.valueOf(page.getPageNum()));
            map.put("pageSize", String.valueOf(page.getPageSize()));
            //String rest = "/rest/opus/om/listAllOpuOmUserInfoByOrgId.do";
            //HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl.concat(rest), map);
            String url = opusRestUrl + OpusRestConstant.listAllOpuOmUserInfoByOrgId;
            String result = HttpClientUtil.getByToken(url, map, token, "utf-8");
            JSONObject jsonObject = JSONObject.fromObject(result);
            List<Map> listMap = JsonUtils.toList(jsonObject.get("rows").toString(), HashMap.class);
            pageInfo.setTotal(Long.valueOf(jsonObject.get(TOTAL).toString()));
            List<AgUser> agUsers = AgUserConverter.convertToBeanList(list, listMap);
            pageInfo.setList(agUsers);
        }
        return pageInfo;
    }

    @Override
    public List<AgUser> searchUsers(String userName) throws Exception {
        String token = agToken.checkToken();
        List<AgUser> list = new ArrayList<>();
        Map map = new HashMap();
        map.put("orgId", adminorgId);
        //HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl.concat(rest), map);
        String url = opusRestUrl + OpusRestConstant.listAllOpuOmUserByOrgId;
        String result = HttpClientUtil.getByToken(url, map, token, "utf-8");
        JSONArray jsonArray = JSONArray.fromObject(result);
        //List<Map> listMap = JsonUtils.toList(jsonObject.get("rows").toString(), HashMap.class);
        List<AgUser> agUsers = AgUserConverter.convertToBeanList(list, jsonArray);
        List<AgUser> matchUsers = agUsers.stream().filter(
                u->!Common.isCheckNull(u.getUserName()) && u.getUserName().contains(userName)).collect(Collectors.toList()
        );
        return matchUsers;
    }
    /*在机构中找出登录名称或用户名称和name一致的用户*/
    private List<AgUser> findOrgUsers(String name) throws Exception {
        List<AgUser> allUsers = findOrgUsers();
        List<AgUser> matchUserList = allUsers.stream().filter(u->(u.getLoginName()!=null && u.getLoginName().contains(name))|| (u.getUserName()!=null && u.getUserName().contains(name))).collect(Collectors.toList());
        return matchUserList;
    }
    @Override
    /*在机构中找出登录名称或用户名称和name一致的用户,并分页返回 */
    public PageInfo<AgUser> findOrgUsers(String name,Page page) throws Exception {
        List<AgUser> matchUserList = findOrgUsers(name);
        List<AgUser> pageUsers = null;
        if(page.getPageSize()<=0){
            page.setPageSize(10);
        }
        if(page.getPageNum()<=0){
            page.setPageNum(1);
        }
        int total = matchUserList.size();
        page.setTotal(total);
        //List.subList(startIndex,endIndex)这个函数获取的记录是包含开始索引，不包含结束索引
        int startIndex = (page.getPageNum()-1)*page.getPageSize();
        int endIndex = page.getPageNum()*page.getPageSize();
        if(total>endIndex){
            pageUsers = matchUserList.subList(startIndex,endIndex);
        }else{
            pageUsers = matchUserList.subList(startIndex,matchUserList.size());
        }
        page.addAll(pageUsers);
        PageInfo<AgUser> pageInfoUser = new PageInfo<AgUser>(page);
        return pageInfoUser;
    }


    @Override
    public List<AgUser> findOrgUsers() throws Exception {
        String token = agToken.checkToken();
        List<AgUser> list = new ArrayList<>();
        Map map = new HashMap();
        map.put("orgId", adminorgId);
        String url = opusRestUrl + OpusRestConstant.listAllOpuOmUserByOrgId;
        String result = HttpClientUtil.getByToken(url, map, token, "utf-8");
        JSONArray jsonArray = JSONArray.fromObject(result);
        List<AgUser> agUsers = AgUserConverter.convertToBeanList(list, jsonArray);
        return agUsers;
    }

    @Override
    public PageInfo<AgUser> searchUserByRole(AgUser agUser, Page page, String roleId) throws Exception {
        PageHelper.startPage(page);
        List<AgUser> list = agUserMapper.findListByRole(agUser, roleId);
        return new PageInfo<AgUser>(list);
    }


    @Override
    //@Cacheable(value = "findUserByName#ag_user", key = "#loginName", condition = "#loginName != null")
    public AgUser findUserByName(String loginName) throws Exception {
        String token = agToken.checkToken();
        AgUser agUser = new AgUser();
        //调用agcloud接口获取
        Map<String, String> param = new HashMap<>();
        //这里的userName参数实际是对应用户表的login_name字段，接口就是这么的不规范，令人疑惑
        param.put("userName", loginName);
        //HttpRespons httpRespons = new HttpRequester()
        //        .sendPost(opusAdminUrl + "/rest/opus/om/getOpuOmUserInfoByUserId.do", param);
        String url = opusRestUrl + OpusRestConstant.getOpuOmUserInfoByUserId;
        String result = HttpClientUtil.getByToken(url, param, token, "utf-8");
        if(StringUtils.isNotBlank(result)){
            Map<String, Object> map = com.augurit.agcloud.framework.util.JsonUtils.mapFromJson(result);
            agUser = new AgUserConverter().convertToBean(agUser, map);
        }
        return agUser;
    }

    @Override
    public AgUser findUserById(String userId) throws Exception {
        List<AgUser> list = new ArrayList<>();
        String token = agToken.checkToken();
        String url = opusRestUrl + OpusRestConstant.listUserByUserIds;
        Map param = new HashMap();
        param.put("userIds", userId);
        List<AgUser> agUsers = new ArrayList<>();
        String result = HttpClientUtil.getByToken(url, param, token, "utf-8");
        if (StringUtils.isNotBlank(result)){
            Map<String, Object> map = com.augurit.agcloud.framework.util.JsonUtils.mapFromJson(result);
            List<Map> listMap = (List<Map>) map.get("rows");
            agUsers = AgUserConverter.convertToBeanList(list, listMap);
        }
        if (agUsers.size() > 0){
            return agUsers.get(0);
        }
        return null;
    }

    @Override
    public List<AgUser> findUsersByUserIds(String userIds) throws Exception {
        List<AgUser> list = new ArrayList<>();
        String token = agToken.checkToken();
        String url = opusRestUrl + OpusRestConstant.listUserByUserIds;
        Map param = new HashMap();
        param.put("userIds", userIds);
        List<AgUser> agUsers = new ArrayList<>();
        String result = HttpClientUtil.getByToken(url, param, token, "utf-8");
        if (StringUtils.isNotBlank(result)){
            JSONObject jsonObject = JSONObject.fromObject(result);
            List<Map> listMap = JsonUtils.toList(jsonObject.get("rows").toString(), HashMap.class);
            agUsers = AgUserConverter.convertToBeanList(list, listMap);
        }

        return agUsers;
    }

    @Override
    public void saveUser(AgUser agUser) throws Exception {
        if (agUser == null) return;
        agUserMapper.save(agUser);
        if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
            stringRedisTemplate.delete("agUserIdList#ag_user:allUserId");
            stringRedisTemplate.delete("findUserByName#ag_user:" + agUser.getLoginName());
        }
    }


    @Override
    public AgUserLayer getUserLayer(String userId, String dirLayerId) throws Exception {
        AgUserLayer agUserLayer = agUserLayerMapper.findByUidAndDid(userId, dirLayerId);
        return agUserLayer;
    }

    @Override
    public void saveUserLayerBatch(List<AgUserLayer> list) throws Exception {
        if (list == null || list.size() <= 0) return;
        for (AgUserLayer agUserLayer : list) {
            this.saveUserLayer(agUserLayer);
        }
    }

    @Override
    public void saveUserLayer(AgUserLayer userLayer) throws Exception {
        if (userLayer == null) return;
        if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
            /*List<AgUser> list = agUserMapper.findListByRole(new AgUser(), userLayer.getUserId());
            List<String> keys = new ArrayList<String>();
            if (list.size() > 0) {
                for (AgUser agUser : list) {
                    keys.add("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + agUser.getId());
//                    keys.add("access#role.user_role.role_layer:" + agUser.getId() + roleLayer.getDirLayerId());
                }
            }*/
            List<String> keys = new ArrayList<String>();
            keys.add("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + userLayer.getId());
            Set<String> keys1 = stringRedisTemplate.keys("access#role.user_role.role_layer:*");
            stringRedisTemplate.delete(keys1);//清除redis缓存
            stringRedisTemplate.delete(keys);//清除redis缓存
            Set<String> userProject = stringRedisTemplate.keys("user#project_layer_tree:*");
            stringRedisTemplate.delete(userProject);

        }
        if (userLayer.getId() != null) {
            agUserLayerMapper.updateUserLayer(userLayer);
        } else {
            userLayer.setId(UUID.randomUUID().toString());
            agUserLayerMapper.saveBatch(Arrays.asList(userLayer));
        }

    }

    @Override
    public PageInfo<AgUser> searchUserByLayer(AgUser agUser, String dirLayerId, Page page) throws Exception {
        String token = agToken.checkToken();
        PageInfo<AgUser> userPage = null;
        List<AgUserLayer> userLayerList = agUserLayerMapper.findListByDirLayerId(dirLayerId);
        String userIds = "";
        Map<String, String> tempMap = new HashMap<String, String>();
        for (AgUserLayer tempUserLayer : userLayerList) {
            userIds += tempUserLayer.getUserId() + ",";
            tempMap.put(tempUserLayer.getUserId(), tempUserLayer.getId());
        }
        if (userIds.length() > 0) userIds = userIds.substring(0, userIds.length() - 1);
        List<AgUser> list = new ArrayList<>();
        if (!agcloudInfLoad.booleanValue()) {
            PageHelper.startPage(page);
            list = agUserMapper.findListByIds(agUser, userIds.split(","));
            userPage = new PageInfo<AgUser>(list);
        } else {
            Map<String, String> param = new HashMap<>();
            param.put("userIds", userIds);
            param.put("userName", agUser.getUserName());
            param.put("pageNum", String.valueOf(page.getPageNum()));
            param.put("pageSize", String.valueOf(page.getPageSize()));
            String url = opusRestUrl + OpusRestConstant.listUserByUserIds;
            //HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/om/listUserByUserIds.do", param);
            String result = HttpClientUtil.getByToken(url, param, token, "utf-8");
            if (StringUtils.isNotBlank(result)){
                //JSONObject jsonObject = JSONObject.fromObject(result);
                //List<Map> listMap = JsonUtils.toList(jsonObject.get("rows").toString(), HashMap.class);
                JSONObject jsonObject = JSONObject.fromObject(result);
                List<Map> listMap = JsonUtils.toList(jsonObject.get("rows").toString(), HashMap.class);
                list = new AgUserConverter().convertToList(null, listMap);
                userPage = new PageInfo<>();
                userPage.setList(list);
                userPage.setTotal(Long.valueOf(jsonObject.get("total").toString()));
            }

        }
        for (AgUser tempUser : list) {
            tempUser.setUserLayerId(tempMap.get(tempUser.getId()));
        }
        return userPage;
    }

    @Override
    public void delUserLayerBatch(String[] ids) throws Exception {
        if (ids == null || ids.length <= 0) return;
        for (String id : ids) {
            this.deleteUserLayer(id);
        }
    }

    @Override
    public void deleteUserLayer(String id) throws Exception {
        if (id == null) return;
        if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
            List<String> list = agUserMapper.findListByUserLayer(id);
            List<String> keys = new ArrayList<String>();
            if (list.size() > 0) {
                //String dirLayerId = agUserLayerMapper.findById(id).getDirLayerId();
                for (String userId : list) {
                    keys.add("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + userId);
//                    keys.add("access#role.user_role.role_layer:" + agUser.getId() + dirLayerId);
                }
            }
            Set<String> keys1 = stringRedisTemplate.keys("access#role.user_role.role_layer:*");
            stringRedisTemplate.delete(keys1);//清除redis缓存
            stringRedisTemplate.delete(keys);//清除redis缓存
            // 清除图层树缓存
            Set<String> userProject = stringRedisTemplate.keys("user#project_layer_tree:*");
            stringRedisTemplate.delete(userProject);

        }

        agUserLayerMapper.delete(id);
    }


    @Override
    public void updateUser(AgUser agUser) throws Exception {
        if (agUser == null) {
            return;
        } else {
            if (agUser.getId() != null) {
                //用户基本信息修改的沿革历史记录
                AgUser oldAgUser = agUserMapper.findById(agUser.getId());

                //agHistChangeService.logAgHistAppWithUserUpdate(agUser, oldAgUser);

                agUserMapper.update(agUser);
            }
        }

    }

    @Override
    public List<Map> findOrgsFromProd() throws Exception {
        String sql = "select t.org_name,t.org_code,t1.org_code as parent_org_code from om_org t left join om_org t1 on t.parent_org_id = t1.org_id order by t.org_code ";
        return DBHelper.find(this.prod_db, sql, null);
    }

    @Override
    public List<Map> findUsersFromProd() throws Exception {
        String sql = "select x.login_name,x.password,x.user_name,x.is_active,c.org_code,c.org_name from (select a.*, case when b.org_id is not null then b.org_id else (select f.org_id from om_position e left join om_org f on e.party_id=f.org_id where e.pos_id=(select pos_id from om_user_position where user_id=a.user_id)) end as org_id from om_user a left join om_user_org b on a.user_id=b.user_id ) x left join om_org c on x.org_id = c.org_id where c.org_name is not null";
        return DBHelper.find(this.prod_db, sql, null);
    }

    @Override
    public List<String> findUserByLayer(String layerId) throws Exception {
        return agUserMapper.findListByLayer(layerId);
    }

    @Override
    public List<AgUser> findUsers() throws Exception {
        return agUserMapper.findUsers();
    }

    @Override
    public boolean saveCode(String code, String loginName) {
        String token = SpatialUtil.generateMD5Code(loginName + code);
        Document doc = new Document(CasConfig.ID_COLUMN, code).append(CasConfig.LOGIN_NAME_COLUMN, loginName).append(CasConfig.TOKEN_COLUMN, token)
                .append(CasConfig.CHECK_TIME_COLUMN, 0).append(CasConfig.CREATE_TIME_COLUMN, CasConfig.sdf.format(new Date()));
        return MongoDBHelp.insertOne(doc, CasConfig.CAS_LOGIN_CACHE_TABLE);
    }

    @Override
    public String getLoginNameByToken(String token) {
        String loginName = "";
        try {
            BasicDBObject dbObject = new BasicDBObject(CasConfig.TOKEN_COLUMN, token);
            List<Map> result = MongoDBHelp.find(dbObject, CasConfig.CAS_LOGIN_CACHE_TABLE);
            if (result != null && result.size() > 0) {
                Map dataMap = result.get(0);
                Date createDate = CasConfig.sdf.parse(dataMap.get(CasConfig.CREATE_TIME_COLUMN).toString());
                int checkTimes = Integer.parseInt(dataMap.get(CasConfig.CHECK_TIME_COLUMN).toString());
                if (checkTimes < CasConfig.CAS_CHECK_MAX_TIMES) {
                    if (new Date().getTime() - createDate.getTime() <= CasConfig.CAS_CHECK_MAX_TIME) {
                        loginName = dataMap.get(CasConfig.LOGIN_NAME_COLUMN).toString();
                    }
                    Document doc = new Document(CasConfig.CHECK_TIME_COLUMN, ++checkTimes);
                    MongoDBHelp.updateMany(dbObject, doc, CasConfig.CAS_LOGIN_CACHE_TABLE);
                } else {
                    MongoDBHelp.deleteMany(dbObject, CasConfig.CAS_LOGIN_CACHE_TABLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginName;
    }
}
