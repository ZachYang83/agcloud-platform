package com.augurit.agcloud.agcom.agsupport.sc.service.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.mongodb.MongoDbService;
import com.augurit.agcloud.agcom.agsupport.common.mongodb.MongodbConstant;
import com.augurit.agcloud.agcom.agsupport.common.util.ProxyUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.UUIDUtil;
import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.mapper.AgServiceMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgUserMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.service.service.IAgService;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.MongoDBHelp;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.common.util.Common;
import com.common.util.HttpRespons;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Augurit on 2017-05-02.
 */
@Service
public class AgServiceImpl implements IAgService {

    @Autowired
    private AgServiceMapper agServiceMapper;
    @Autowired
    private AgUserMapper agUserMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IAgUser iAgUser;

    @Value("classpath:mark.json")
    private Resource mark;
    @Value("${agcloud.inf.load}")
    private Boolean agcloudInfLoad;
    @Value("${agcloud.opus.admin.orgId}")
    private String opusAdminOrgId;

    @Override
    @CacheEvict(value = "serviceUserInfo#service_userinfo", key = "#agServiceUserinfo.uuid")
//为了跟agproxy的检验缓存一致，使用uuid做缓存id
    public void updateAgServiceUserinfo(AgServiceUserinfo agServiceUserinfo) {
        agServiceMapper.updateAgServiceUserinfo(agServiceUserinfo);
    }

    @Override
    public PageInfo<AgServiceUserinfo> searchAgServiceUserinfo(AgServiceUserinfo agServiceUserinfo, Page page) {
        PageHelper.startPage(page);
        String opusAdminOrgId = SecurityContext.getCurrentOrgId();
        String agproxyUrl = ProxyUtil.getProxyPreUrl();
        //处理一下申请状态，9表示查询所有
        if ("9".equals(agServiceUserinfo.getFlag()))
            agServiceUserinfo.setFlag(null);
        List<AgServiceUserinfo> agServiceUserinfos = null;
        if (!agcloudInfLoad.booleanValue()) {
            agServiceUserinfos = agServiceMapper.listAgServiceUserinfo(agServiceUserinfo);//从数据库中获取
        } else {
            agServiceUserinfos = agServiceMapper.listAgServiceUserinfoOpus(agServiceUserinfo);//从数据库中获取
            //调用agcloud接口获取用户列表，获取用户信息封装到用户关系列表中
            HttpRespons httpRespons = null;
            try {
                //httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/om/listAllOpuOmUserByOrgId.do?orgId=" + opusAdminOrgId);
                JSONArray users = JSONArray.fromObject(httpRespons.getContent());
                String userName = agServiceUserinfo.getUserName();
                if (Common.isCheckNull(userName)) {
                    //如果用户名字段为空，则不进行关联的模糊查询
                    for (int i = 0; i < agServiceUserinfos.size(); i++) {
                        AgServiceUserinfo temp = agServiceUserinfos.get(i);
                        for (int j = 0; j < users.size(); j++) {
                            if (temp.getUserId().equals(users.getJSONObject(j).getString("userId"))) {
                                temp.setUserName(users.getJSONObject(j).getString("userName"));
                                break;
                            }
                        }
                    }
                } else {
                    List<AgServiceUserinfo> tempList = new ArrayList<>();
                    for (int i = 0; i < agServiceUserinfos.size(); i++) {
                        AgServiceUserinfo temp = agServiceUserinfos.get(i);
                        for (int j = 0; j < users.size(); j++) {
                            //模糊过滤一下userName
                            String userName1 = users.getJSONObject(j).getString("userName");
                            if (userName1.contains(userName) && temp.getUserId().equals(users.getJSONObject(j).getString("userId"))) {
                                temp.setUserName(userName1);
                                tempList.add(temp);
                                break;
                            }
                        }
                    }
                    agServiceUserinfos = tempList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!Common.isCheckNull(agServiceUserinfo.getUrl())) {
            List<AgServiceUserinfo> resultList = new ArrayList<AgServiceUserinfo>();
            for (AgServiceUserinfo asu : agServiceUserinfos) {
                asu.setUrl(agproxyUrl + asu.getUuid());
                if (asu.getUrl().contains(agServiceUserinfo.getUrl())) {
                    resultList.add(asu);
                }
            }
            return new PageInfo<AgServiceUserinfo>(resultList);
        } else {
            for (AgServiceUserinfo asu : agServiceUserinfos) {
                asu.setUrl(agproxyUrl + asu.getUuid());
            }
            return new PageInfo<AgServiceUserinfo>(agServiceUserinfos);
        }
    }

    @Override
    public List<AgServiceUserinfo> findServiceUserinfoByUserId(String userId) {

        return agServiceMapper.findListByUserId(userId);
    }

    @Override
    public List<AgServiceUserinfo> findServiceUserinfoByServiceId(String serviceId) {

        return agServiceMapper.findListByServiceId(serviceId);
    }

    @Override
    public void insertAgServiceUserinfo(AgServiceUserinfo agServiceUserinfo) {
        agServiceMapper.insertAgServiceUserinfo(agServiceUserinfo);
    }

    @Override
    public AgServiceUserinfo getAgServiceUserinfoById(String id) {
        return agServiceMapper.getAgServiceUserinfoById(id);
    }

    @Override
    @CacheEvict(value = "serviceUserInfo#service_userinfo", key = "#agServiceUserinfo.uuid")
    public void deleteAgServiceUserinfo(AgServiceUserinfo agServiceUserinfo) {
        agServiceMapper.deleteAgServiceUserinfo(agServiceUserinfo.getId());
    }

    @Override
    public PageInfo<AgServiceLog> searchAgServiceLog(AgServiceLog agServiceLog, Page page) {
        PageHelper.startPage(page);
        List<AgServiceLog> agServiceLogs;
        try {
            List<AgUser> listUser = iAgUser.searchUsers(agServiceLog.getUserName());
            Pattern ownerPattern=Pattern.compile("^.*"+agServiceLog.getUserName()+".*$", Pattern.CASE_INSENSITIVE);
            Pattern namePattern=Pattern.compile("^.*"+agServiceLog.getName()+".*$", Pattern.CASE_INSENSITIVE);
            Criteria criteria = new Criteria();
            if (StringUtils.isNotBlank(agServiceLog.getName())){
                criteria.and("name").regex(namePattern);
            }
            if (StringUtils.isNotBlank(agServiceLog.getUserName())){
                criteria.and("serviceOwner").regex(ownerPattern);
            }
            if (StringUtils.isNotBlank(agServiceLog.getIp())){
                criteria.and("ip").is(agServiceLog.getIp());
            }
            if (agServiceLog.getAccessTimeStart() != null){
                criteria.and("accessTime").gte(agServiceLog.getAccessTimeStart());
            }
            Query query = new Query(criteria)
                    .skip((page.getPageNum()-1) * page.getPageSize())
                    .limit(page.getPageSize()).with(new Sort(Sort.Direction.DESC,   "accessTime"));

            agServiceLogs = mongoTemplate.find(query, AgServiceLog.class, MongodbConstant.AGCOM_SERVICE_LOG);
            long count = mongoTemplate.count(new Query(criteria), MongodbConstant.AGCOM_SERVICE_LOG);
            //agServiceLogs = agServiceMapper.listAgServiceLogOpus(listIds, agServiceLog);
            //设置UserName
            for (AgServiceLog item : agServiceLogs) {
                List<AgUser> matchUsers = listUser.stream().filter(u -> u.getId().equals(item.getUserId())).collect(Collectors.toList());
                if (matchUsers != null && matchUsers.size() > 0) {
                    item.setUserName(matchUsers.get(0).getUserName());
                }
            }
            PageInfo<AgServiceLog> agServiceLogPageInfo = new PageInfo<>(agServiceLogs);
            agServiceLogPageInfo.setTotal(count);
            return agServiceLogPageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            agServiceLogs = new ArrayList<AgServiceLog>();
            return new PageInfo<AgServiceLog>(agServiceLogs);
        }

    }

    @Override
    public PageInfo<AgServiceLog> findAgServiceLog(AgServiceLog agServiceLog, Page page) {
        String sql = querySql(agServiceLog, page);
        ArrayList<Map> maps = MongoDBHelp.find(sql);
        List<AgServiceLog> agServiceLogs = new ArrayList<>();
        try {
            for (Map log : maps) {
                agServiceLogs.add(MongoDBHelp.map2Bean(log, new AgServiceLog()));
            }
        } catch (Exception e) {
        }
        return new PageInfo<AgServiceLog>(agServiceLogs);
    }

    private String querySql(AgServiceLog agServiceLog, Page page) {
        String sql = "select * from agcom_accessLog where 1=1";
        BasicDBObject condition = null;
        if (agServiceLog != null) {
            if (agServiceLog.getAccessTimeStart() != null) {
                sql += " and accessTime >= " + agServiceLog.getAccessTimeStart();
            } else if (agServiceLog.getAccessTimeEnd() != null) {
                sql += " and accessTime <= " + agServiceLog.getAccessTimeEnd();
            }
        }
        if (page != null) {
            if (page.getOrderBy() != null) {
                sql += " order by " + page.getOrderBy();
            } else if (page.getPageSize() > 0 && page.getPageNum() >= 0) {
                sql += " limit " + page.getPageNum() + "," + page.getPageSize();
            }
        }
        return sql;
    }

    @Override
    @Cacheable(value = "serviceUserInfo#service_userinfo", key = "#id")
    public Object testMapper(String id) {
        AgServiceUserinfo a = agServiceMapper.getAgServiceUserinfoById(id);
//        a.setApplyTimeEnd(new Date());
        return a;
    }

    @Override
    public String getMarks() {
        Scanner scanner = null;
        String markJson = null;
        StringBuilder buffer = new StringBuilder();
        try {
            InputStream inputStream = mark.getInputStream();
            scanner = new Scanner(inputStream, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
            markJson = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return markJson;
    }

    @Override
    public String countByLabel() throws Exception {
        String marksJson = getMarks();
        if (marksJson.startsWith("[")) {
            marksJson = "{\"label\" :" + marksJson + "}";
        }
        JSONObject jsonObject = JSONObject.fromObject(marksJson);
        JSONArray labels = (JSONArray) jsonObject.get("label");
        for (int i = 0; i < labels.size(); i++) {
            JSONObject label = (JSONObject) labels.get(i);
            JSONArray children = (JSONArray) label.get("children");
            for (int j = 0; j < children.size(); j++) {
                JSONObject sla = (JSONObject) children.get(j);
                String labelName = (String) sla.get("text");
                int count = agServiceMapper.countByLabel(labelName);
                if (count > 0) {
                    sla.put("layerCount", count);
                }
            }
        }
        return jsonObject.toString();
    }

    @Override
    public String countByLabelAnduserId(String userId) throws Exception {
        String marksJson = getMarks();
        if (marksJson.startsWith("[")) {
            marksJson = "{\"label\" :" + marksJson + "}";
        }
        JSONObject jsonObject = JSONObject.fromObject(marksJson);
        JSONArray labels = (JSONArray) jsonObject.get("label");
        for (int i = 0; i < labels.size(); i++) {
            JSONObject label = (JSONObject) labels.get(i);
            JSONArray children = (JSONArray) label.get("children");
            for (int j = 0; j < children.size(); j++) {
                JSONObject sla = (JSONObject) children.get(j);
                String labelName = (String) sla.get("text");
                int count = agServiceMapper.countByLabelAndUid(labelName, userId);
                if (count > 0) {
                    sla.put("layerCount", count);
                }
            }
        }
        return jsonObject.toString();
    }

    @Override
    public List<AgLayer> findLayerByLabel(String label, boolean asc) throws Exception {
        List<AgLayer> layers = new ArrayList<>();
        List<AgMetadata> metadatas = agServiceMapper.findByLabel(label, asc);
        for (AgMetadata am : metadatas) {
            AgLayer layer = agServiceMapper.findByMetadataId(am.getId());
            if (layer != null) {
                //将元数据合并到 AgLayer类的扩展字段中
                String data = layer.getData();
                JSONObject jsonData = JSONObject.fromObject(data);
                JSONObject temp = JSONObject.fromObject(am);
                if (temp != null) {
                    temp.remove("id");
                    jsonData.putAll(temp);
                }
                layer.setData(jsonData.toString());
                layers.add(layer);
            }
        }
        return layers;
    }

    @Override
    public List<AgLayer> findByLabelAndUserId(String label, String userId, boolean asc) throws Exception {
        List<AgLayer> layers = new ArrayList<>();
        List<AgMetadata> metadatas = agServiceMapper.findByLabel(label, asc);
        for (AgMetadata am : metadatas) {
            AgLayer layer = agServiceMapper.findByMidAndUid(am.getId(), userId);
            if (layer != null) {
                //将元数据合并到 AgLayer类的扩展字段中
                String data = layer.getData();
                JSONObject jsonData = JSONObject.fromObject(data);
                JSONObject temp = JSONObject.fromObject(am);
                if (temp != null) {
                    temp.remove("id");
                    jsonData.putAll(temp);
                }
                //获取服务申请状态
                AgServiceUserinfo asu = agServiceMapper.getAgServiceUserinfoByUidAndSid(userId, layer.getId());
                int state;
                if (asu == null) {
                    state = -1;
                } else if ("0".equals(asu.getFlag())) {
                    state = 0;
                } else if ("1".equals(asu.getFlag())) {
                    state = 1;
                } else {
                    state = 2;
                }
                jsonData.put("applyState", state);
                layer.setData(jsonData.toString());
                layers.add(layer);
            }
        }
        return layers;
    }

    @Override
    public String applyServer(AgServiceUserinfo agServiceUserinfo) {
        AgServiceUserinfo info = agServiceMapper.getAgServiceUserinfoByUidAndSid(agServiceUserinfo.getUserId(), agServiceUserinfo.getServiceId());
        String uuid = "";
        if (info != null && info.getId() != null) {
            String addrIp = Common.checkNull(info.getIp());
            //只记录一次 ip
            if (!agServiceUserinfo.getIp().equals(addrIp) || addrIp.length() == 0) {
                info.setIp(agServiceUserinfo.getIp());
            }
            if ("2".equals(info.getFlag())) {
                info.setFlag("0");
                info.setApplyTime(new Date());
                info.setApplyOpinion(agServiceUserinfo.getApplyOpinion());
            }
            agServiceMapper.updateAgServiceUserinfo(info);
            uuid = info.getUuid();
        } else {
            // 数据库不存在，则添加信息并插入
            uuid = UUIDUtil.getUUID();
            agServiceUserinfo.setId(UUID.randomUUID().toString());
            agServiceUserinfo.setFlag("0");
            agServiceUserinfo.setApplyTime(new Date());
            agServiceUserinfo.setUuid(uuid);
            agServiceMapper.insertAgServiceUserinfo(agServiceUserinfo);
        }
        return uuid;
    }

    @Override
    public AgServiceUserinfo getApplyInfo(String userId, String serviceId) {
        return agServiceMapper.getAgServiceUserinfoByUidAndSid(userId, serviceId);
    }

    @Override
    public List<AgServiceUserinfo> getServiceUserInfoByServiceIdAndUserName(String serviceId, String userId) {
        return agServiceMapper.getServiceUserInfoByServiceIdAndUserName(serviceId, userId);
    }

}
