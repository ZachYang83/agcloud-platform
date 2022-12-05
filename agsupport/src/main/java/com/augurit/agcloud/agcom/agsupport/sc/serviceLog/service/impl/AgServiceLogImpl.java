package com.augurit.agcloud.agcom.agsupport.sc.serviceLog.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.mongodb.MongodbConstant;
import com.augurit.agcloud.agcom.agsupport.domain.AgServiceLog;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.mapper.AgServiceMapper;
import com.augurit.agcloud.agcom.agsupport.sc.serviceLog.service.IAgServiceLog;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.common.util.HttpRespons;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017-12-08.
 */
@Service
public class AgServiceLogImpl implements IAgServiceLog {

    @Autowired
    private AgServiceMapper agServiceMapper;
    @Value("${agcloud.inf.load}")
    private Boolean agcloudInfLoad;
    @Value("${agcloud.opus.admin.orgId}")
    private String opusAdminOrgId;
    @Autowired
    private IAgUser iAgUser;

    public List<AgServiceLog> getLogOrderByUser() throws Exception {
        List<AgServiceLog> agServiceLogs;
        agServiceLogs = agServiceMapper.getLogOrderByService();//从数据库中获取

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.project("serviceOwner")
                        .andExpression("serviceOwner").as("serviceOwner"),
                Aggregation.group("serviceOwner")
                        .count()
                        .as("totalTimes"),
                Aggregation.sort(new Sort(Sort.Direction.DESC, "totalTimes"))
        );
        AggregationResults<Map> aggregate = mongoTemplate.aggregate(aggregation, MongodbConstant.AGCOM_SERVICE_LOG, Map.class);
        List<Map> mappedResults = aggregate.getMappedResults();
        for (Map map : mappedResults) {
            AgServiceLog agServiceLog = new AgServiceLog();
            agServiceLog.setUserName(map.get("_id").toString());
            agServiceLog.setTotalTimes(Long.valueOf(map.get("totalTimes").toString()));
            agServiceLogs.add(agServiceLog);
        }

        return agServiceLogs;
    }

    private void fillUserNameByOpus(List<AgServiceLog> agServiceLogs) throws Exception {
        //调用agcloud接口获取用户列表，获取用户信息封装到用户关系列表中
        HttpRespons httpRespons = null;
        try {
            //httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/om/listAllOpuOmUserByOrgId.do?orgId=" + opusAdminOrgId);
            //JSONArray users = JSONArray.fromObject(httpRespons.getContent());
            List<AgUser> users = iAgUser.findOrgUsers();
            //如果用户名字段为空，则不进行关联的模糊查询
            for (int i = 0; i < agServiceLogs.size(); i++) {
                AgServiceLog temp = agServiceLogs.get(i);
                for (int j = 0; j < users.size(); j++) {
                    if (temp.getUserId() != null && temp.getUserId().equals(users.get(j).getId())) {
                        String userName = "";
                        if (null != users.get(j).getUserName()) {
                            userName = users.get(j).getUserName();
                        }
                        temp.setUserName(userName);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<AgServiceLog> getLogOrderByService() throws Exception {
        List<AgServiceLog> agServiceLogs = new ArrayList<>();
        agServiceLogs = agServiceMapper.getLogOrderByService();//从数据库中获取

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.project("name")
                        .andExpression("name").as("name"),
                Aggregation.group("name")
                        .count()
                        .as("totalTimes"),
                Aggregation.sort(new Sort(Sort.Direction.DESC, "totalTimes"))
        );
        AggregationResults<Map> aggregate = mongoTemplate.aggregate(aggregation, MongodbConstant.AGCOM_SERVICE_LOG, Map.class);
        List<Map> mappedResults = aggregate.getMappedResults();
        for (Map map : mappedResults) {
            AgServiceLog agServiceLog = new AgServiceLog();
            agServiceLog.setName(map.get("_id").toString());
            agServiceLog.setTotalTimes(Long.valueOf(map.get("totalTimes").toString()));
            agServiceLogs.add(agServiceLog);
        }

        return agServiceLogs;
    }


    @Override
    public List<AgServiceLog> getLogByServiceId(String serviceId) throws Exception {
        List<AgServiceLog> agServiceLogs;
        agServiceLogs = agServiceMapper.getLogByServiceId(serviceId);
        return agServiceLogs;
    }

    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Map getHotServiceRecentAllByte() throws Exception {
        ArrayList<String> pastDayList = new ArrayList<>();
        for (int i=0; i<7; i++) {
            pastDayList.add(getPastDate(i));
        }
        Map map = new HashMap();
        List<Map> rs = new ArrayList<>();
        Criteria eatCriteria = Criteria.where("visitDay").gte(pastDayList.get(6)).lte(pastDayList.get(0));
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(eatCriteria),
                Aggregation.project("name", "byteSize")
                        .andExpression("name").as("name"),
                Aggregation.group("name")
                        .sum("byteSize")
                        .as("byteSizeSum"),
                Aggregation.sort(new Sort(Sort.Direction.DESC, "byteSizeSum"))
        );
        AggregationResults<Map> aggregate = mongoTemplate.aggregate(aggregation, MongodbConstant.AGCOM_SERVICE_LOG, Map.class);
        List<Map> mappedResults = aggregate.getMappedResults();

        String serviceName = "";
        Map _map = new LinkedHashMap();
        // 获取最近七天访问量前五的服务
        int n = 0;
        for (Map a : mappedResults) {
            n++;
            serviceName = a.get("_id").toString();
            _map.put(serviceName, a.get("byteSizeSum"));
            if (n == 5){
                break;
            }
        }
        // 获取这五个服务的最近七天的每天的访问量
        Criteria dayCriteria = Criteria.where("visitDay").gte(pastDayList.get(6)).lte(pastDayList.get(0));
        Aggregation dayAggregation = Aggregation.newAggregation(
                Aggregation.match(dayCriteria),
                Aggregation.project("name", "byteSize","visitDay")
                        .andExpression("name").as("name"),
                Aggregation.group("name","visitDay")
                        .sum("byteSize")
                        .as("byteSizeSum")
        );
        AggregationResults<Map> dayAggregate = mongoTemplate.aggregate(dayAggregation, MongodbConstant.AGCOM_SERVICE_LOG, Map.class);
        List<Map> dayMappedResults = dayAggregate.getMappedResults();
        // 找出对应服务的访问量，拼装数据
        for(Object key : _map.keySet()){
            String serviceNameM = key.toString();
            ArrayList array = new ArrayList();
            for(int j=pastDayList.size() - 1; j>=0; j--) {
                String day = pastDayList.get(j);
                long size = 0;
                for(int k = 0; k < dayMappedResults.size() ; k++) {
                    if(dayMappedResults.get(k).get("visitDay").equals(day) && dayMappedResults.get(k).get("name").toString().equals(serviceNameM)) {
                        String s = dayMappedResults.get(k).get("byteSizeSum").toString();
                        if (StringUtils.isNotBlank(s)){
                            size = Long.valueOf(s) / 1024;
                        }
                        break;
                    }
                }
                array.add(size);
            }

            Map _m = new HashMap();
            _m.put(serviceNameM, array);
            rs.add(_m);
        }

 /*       String startTime = pastDayList.get(pastDayList.size()-1) + " 00:00:00", endTime = pastDayList.get(0) + " 23:59:59";
        //String[] ids; //获取 最近7日访问总量前5的图层ID
        List<String> ids = new ArrayList<>();
        List<Map<String,Object>> idsMap  = agServiceMapper.getHotService(startTime,endTime);
        if (idsMap.size()>0){
            for (Map map : idsMap){
                if (map.containsKey("ID")){
                    ids.add(map.get("ID").toString());
                }
                if (map.containsKey("id")){
                    ids.add(map.get("id").toString());
                }
            }
        }
        List<Map> rs = new ArrayList<>();
        Map map = new HashMap();
        map.put("times",pastDayList);

        for (int a=0; a<ids.size(); a++){
            String serviceId = ids.get(a);
            List<AgServiceLog> list = agServiceMapper.getHotServiceRecentAllByte(serviceId, startTime, endTime);
            ArrayList array = new ArrayList();
            String serviceName = list.size() > 0? list.get(0).getName(): "未知服务-"+a;
            for(int j=pastDayList.size() - 1; j>=0; j--) {  //找出对应日期服务的访问流量总量
                String day = pastDayList.get(j);
                double allByte = 0;
                for(int k=0;k<list.size();k++) {
                   if(list.get(k).getVisitDay().equals(day)) {
                       double f = list.get(k).getByteSize() / 1024;
                       BigDecimal b = new BigDecimal(f);
                       allByte = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                   }
                }
                array.add(allByte);
            }
            Map _map = new HashMap();
            _map.put(serviceName, array);
            rs.add(_map);
        }*/
        map.put("result", rs);
        map.put("times", pastDayList);
        return map;

    }

}
