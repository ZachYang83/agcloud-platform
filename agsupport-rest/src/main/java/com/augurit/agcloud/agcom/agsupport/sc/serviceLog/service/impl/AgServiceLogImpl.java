package com.augurit.agcloud.agcom.agsupport.sc.serviceLog.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgServiceLog;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.mapper.AgServiceMapper;
import com.augurit.agcloud.agcom.agsupport.sc.serviceLog.service.IAgServiceLog;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.common.util.HttpRespons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
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
        if (!agcloudInfLoad.booleanValue()) {
            agServiceLogs = agServiceMapper.getLogOrderByUser();//从数据库中获取
        } else {
            agServiceLogs = agServiceMapper.getLogOrderByUserOpus();//从数据库中获取
            fillUserNameByOpus(agServiceLogs);
        }
        return agServiceLogs;
    }

    private void fillUserNameByOpus(List<AgServiceLog> agServiceLogs) throws Exception{
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
                    if (temp.getUserId() != null && temp.getUserId().equals(users.get(i).getId())) {
                        String userName = "";
                        if (null != users.get(i).getUserName()){
                            userName = users.get(i).getUserName();
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
        List<AgServiceLog> agServiceLogs;
        if (!agcloudInfLoad.booleanValue()) {
            agServiceLogs = agServiceMapper.getLogOrderByService();//从数据库中获取
        } else {
            agServiceLogs = agServiceMapper.getLogOrderByServiceOpus();//从数据库中获取
            fillUserNameByOpus(agServiceLogs);
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

    @Override
    public Map getHotServiceRecentAllByte() throws Exception {
        List<AgServiceLog> agServiceLogs =null;
        //获取最近7天的日期
        ArrayList<String> pastDayList = new ArrayList<>();
        for (int i=0; i<7; i++) {
            pastDayList.add(getPastDate(i));
        }
        String startTime = pastDayList.get(pastDayList.size()-1) + " 00:00:00", endTime = pastDayList.get(0) + " 23:59:59";
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
        }
        map.put("result", rs);
        return map;

    }

}
