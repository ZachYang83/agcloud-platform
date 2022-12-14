package com.augurit.agcloud.agcom.agsupport.sc.monitor.service.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.agcom.agsupport.common.util.DESedeUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgDefaultValue;
import com.augurit.agcloud.agcom.agsupport.domain.AgServer;
import com.augurit.agcloud.agcom.agsupport.domain.AgServicesMonitor;
import com.augurit.agcloud.agcom.agsupport.mapper.AgServerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgServicesMonitorMapper;
import com.augurit.agcloud.agcom.agsupport.sc.defaultvalue.service.IAgDefaultValue;
import com.augurit.agcloud.agcom.agsupport.sc.monitor.service.IAgServicesMonitor;
import com.augurit.agcloud.agcom.agsupport.sc.monitor.thread.AsyncTaskService;
import com.augurit.agcloud.agcom.agsupport.sc.serviceLog.service.IAgServiceLog;
import com.augurit.agcloud.agcom.agsupport.util.ArcgisServiceUtil;
import com.augurit.agcloud.agcom.agsupport.util.TokenUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :14:13 2018/10/30
 * @Modified By:
 */
@Service
public class AgServicesMonitorImpl implements IAgServicesMonitor{

    private static String emailTitle;

    @Value("${mail.title}")
    private  void setEmailTitle(String emailTitle) throws UnsupportedEncodingException {
        AgServicesMonitorImpl.emailTitle = new String(emailTitle.getBytes("ISO-8859-1"),"GBK");
    }

    @Autowired
    private AgServicesMonitorMapper agServicesMonitorMapper;

    @Autowired
    private IAgServicesMonitor iAgServicesMonitor;

    @Autowired
    private IAgServiceLog iAgServiceLog;

    @Autowired
    private AgServerMapper agServerMapper;

    @Autowired
    private AsyncTaskService taskService;

    @Autowired
    private IAgDefaultValue iAgDefaultValue;

    @Override
    public PageInfo<AgServicesMonitor> searchAgServicesMonitor(AgServicesMonitor agServicesMonitor, Page page) throws Exception {
        PageHelper.startPage(page);
        PageInfo<AgServicesMonitor> servicesMonitorPage = null;
        List<AgServicesMonitor> list = null;
        PageHelper.startPage(page);
        list = agServicesMonitorMapper.findList(agServicesMonitor);
        servicesMonitorPage = new PageInfo<AgServicesMonitor>(list);
        return servicesMonitorPage;
    }

    @Override
    public List<AgServicesMonitor> findAll() throws Exception {
        return agServicesMonitorMapper.findAllList();
    }

    @Override
    public void saveAgServicesMonitor(AgServicesMonitor agServicesMonitor) throws Exception {
        agServicesMonitorMapper.save(agServicesMonitor);
    }

    @Override
    public void updataAgServicesMonitor(AgServicesMonitor agServicesMonitor) throws Exception {
        agServicesMonitorMapper.update(agServicesMonitor);
    }

    @Override
    public void deleteByMonitorUrl(String monitorUrl) throws Exception {
        agServicesMonitorMapper.deleteByMonitorUrl(monitorUrl);
    }

    @Override
    public void delByIds(List<String> ids) throws Exception {
        agServicesMonitorMapper.delByIds(ids);
    }

    @Override
    public void changeMonitorStatus(AgServicesMonitor agServicesMonitor) throws Exception {
        agServicesMonitorMapper.changeMonitorStatus(agServicesMonitor);
    }

    @Override
    public void changeAllMonitorStatus(AgServicesMonitor agServicesMonitor) throws Exception {
        agServicesMonitorMapper.changeAllMonitorStatus(agServicesMonitor);
    }

    @Override
    public AgServicesMonitor findByMonitorUrl(String monitorUrl) throws Exception {
        return agServicesMonitorMapper.findByMonitorUrl(monitorUrl);
    }

    @Override
    public AgServicesMonitor findById(String id) throws Exception {
        return agServicesMonitorMapper.findById(id);
    }

    @Override
    public void refreshServerStatus(List<AgServicesMonitor> agServicesMonitorList,long count)  {
        try {
            //???????????????????????????????????????????????????????????????????????????????????? 1:???????????????-1???????????????
            AgDefaultValue adv = iAgDefaultValue.findByKey("globalMonitorStatus");
            if (adv!=null){
                String monitorStatusStr = JSONObject.fromObject(adv.getDefaultValue()).getString("monitorStatus");
                int globalMonitorStatus = Integer.parseInt(monitorStatusStr);
                if (globalMonitorStatus==-1) return;
            }

            //???????????????????????????????????????
            AgServer agServer = new AgServer();
            Map<String,String > agserversMap = new HashMap<>();//key:ip+port;value:username,password
            List<AgServer> agServers = agServerMapper.findList(agServer);
            for (AgServer server:agServers){
                agserversMap.put(server.getIp()+server.getPort()+"u",server.getUserName());
                agserversMap.put(server.getIp()+server.getPort()+"p",DESedeUtil.desDecrypt("AGSUPPORT_AGCOM_AUGURIT", server.getPassword()));
            }
            List<AgServicesMonitor> upList = new ArrayList<>();
            for (AgServicesMonitor doasm : agServicesMonitorList) {
                JSONArray json = JSONArray.fromObject(iAgServiceLog.getLogByServiceId(doasm.getId()));
                int totalTimesCount = 0;
                int badRequestCount = 0;
                int totalTimes = 0;
                String jsonStr = JSONArray.fromObject(json).toString();
                jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
                //totalTimesCount = JSONObject.fromObject(jsonStr).getInt("TOTAL_TIMES_COUNT"); //????????????????????????
                JSONObject obj = JSONObject.fromObject(jsonStr);
                //????????????????????????
                if (obj.has("TOTAL_TIMES_COUNT")){
                    totalTimesCount = Integer.parseInt(obj.getString("TOTAL_TIMES_COUNT"));
                }else if (obj.has("total_times_count")){
                    totalTimesCount = Integer.parseInt(obj.getString("total_times_count"));
                }

                //badRequestCount = JSONObject.fromObject(jsonStr).getInt("BAD_REQUEST_COUNT");//????????????????????????
                if (obj.has("BAD_REQUEST_COUNT")){
                    badRequestCount = Integer.parseInt(obj.getString("BAD_REQUEST_COUNT"));
                }else if (obj.has("bad_request_count")){
                    badRequestCount = Integer.parseInt(obj.getString("bad_request_count"));
                }
                //????????????????????????
                if (totalTimesCount > 0) {
                    //totalTimes = JSONObject.fromObject(jsonStr).getInt("REQUEST_TOTAL_TIMES");//???????????????????????????
                    //???????????????????????????
                    if (obj.has("REQUEST_TOTAL_TIMES")){
                        totalTimes = Integer.parseInt(obj.getString("REQUEST_TOTAL_TIMES"));
                    }else if (obj.has("request_total_times")){
                        totalTimes = Integer.parseInt(obj.getString("request_total_times"));
                    }
                } else {
                    totalTimes = 0;
                }
                doasm.setSuccessRate(totalTimesCount == 0 ? 0 : (int)(((float)totalTimesCount / (float)(totalTimesCount + badRequestCount)) * 100));//?????????
                doasm.setAverageTime(totalTimesCount == 0 ? 0 : totalTimes / totalTimesCount);
                doasm.setRequestTotal(totalTimesCount + badRequestCount);//?????????????????????

                //System.out.println("????????????" + i++ + "----" + doasm.getMonitorUrl());
                //??????java????????????java.net.URL ?????????????????????ip?????????
                URI uri = new URI(doasm.getMonitorUrl());
                String ip = uri.getHost();
                int port = uri.getPort();
                String userName  = agserversMap.get(ip+port+"u");
                String password = agserversMap.get(ip+port+"p");
                if (userName==null && password==null) {
                    System.out.println("??????:"+doasm.getServiceFullName()+" ????????? ???"+ip+":"+port);
                }else if (password==null) {
                    System.out.println("??????:"+doasm.getServiceFullName()+" ????????? ???"+ip+":"+port);
                }else if (userName==null) {
                    System.out.println("??????:"+doasm.getServiceFullName()+" ????????? ???"+ip+":"+port);
                }
                String token = TokenUtil.getToken(doasm.getMonitorUrl(),userName,password);
                //????????????????????????????????????????????????
                JSONObject jsonMonitorDetail = new JSONObject();
                //???????????????12?????????????????????
           /* if (doasm.getMonitorDetail() != null) {
                if ((10*count)% (12*60)!=0){
                    jsonMonitorDetail = JSONObject.fromObject(doasm.getMonitorDetail());
                }
            }*/
                if ("errorUrl".equals(token)) {
                    doasm.setStatus(2);//??????????????????????????????
                    doasm.setAvailable(0);
                    jsonMonitorDetail.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "??????????????????????????????");
                    doasm.setMonitorDetail(jsonMonitorDetail.toString());
                }else if ("noLayerService".equals(token) || token==null){
                    doasm.setStatus(3);//?????????????????????
                    doasm.setAvailable(0);
                    jsonMonitorDetail.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "?????????????????????");
                    doasm.setMonitorDetail(jsonMonitorDetail.toString());
                }else {
                    String statusAndAvailable = ArcgisServiceUtil.getServiceReport(doasm.getMonitorUrl(), token);
                    int status;
                    if (statusAndAvailable==null){
                        status=2;
                    }else {
                        status = Integer.parseInt(JSONObject.fromObject(statusAndAvailable).getString("status"));
                        doasm.setAvailable(Double.parseDouble(JSONObject.fromObject(statusAndAvailable).getString("available")));
                    }
                    if (status==0){
                        doasm.setStatus(-1);//??????
                    }else {
                        doasm.setStatus(status);//??????
                    }
                    String formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    switch (status) {
                        case 0:
                            jsonMonitorDetail.put(formatDate,"????????????");
                            break;
                        case 1:
                            jsonMonitorDetail.put(formatDate, "??????????????????");
                            break;
                        case 2:
                            jsonMonitorDetail.put(formatDate, "??????????????????????????????");
                            break;
                        case 3:
                            jsonMonitorDetail.put(formatDate, "?????????????????????");
                            break;
                        default:
                            break;
                    }
                    doasm.setMonitorDetail(jsonMonitorDetail.toString());
                }
                upList.add(doasm);
                //iAgServicesMonitor.updataAgServicesMonitor(doasm);
            }
            System.out.println("???????????????????????? "+upList.size()+" ???");
            for (AgServicesMonitor monitor:upList){
                //????????????????????????????????????
                if (monitor.getStatus()==3) System.out.println("??????????????????"+monitor.getServiceFullName()+" "+monitor.getMonitorUrl());
                monitor.setLastMonitorTime(new Date());
                iAgServicesMonitor.updataAgServicesMonitor(monitor);
            }
            System.out.println("???????????????????????? "+upList.size()+" ???");
            /**
             * ????????????????????????????????????
             * ????????????10??????????????????????????????????????????????????????
             * ??????????????????????????? (10*count) % (sendRate * 60)==0
             */

            //???????????????????????????????????????????????????????????????????????????
            AgDefaultValue agDefaultValue = iAgDefaultValue.findByKey("globalWarnInfo");
            JSONObject jsonStr = new JSONObject();
            String defaultValue = "";
            String receiveWay_global = "";
            String emailAddress_global = "";
            String phone_global = "";
            double sendRate_global = 1;
            if (agDefaultValue!=null){
                defaultValue = agDefaultValue.getDefaultValue();
                jsonStr = JSONObject.fromObject(defaultValue);
                receiveWay_global = jsonStr.getString("receiveWay");
                emailAddress_global = jsonStr.getString("emailAddress");
                phone_global = jsonStr.getString("phone");
                sendRate_global = Double.valueOf(jsonStr.getString("sendRate"));
            }

            //?????????????????????????????????????????????????????????????????????????????????,??????set????????????
            Set<String> urls = new HashSet<>();
            for (AgServicesMonitor obj:upList){
                String receiveWay = obj.getReceiveWay();
                if ("".equals(receiveWay) || receiveWay==null){
                    if ("".equals(receiveWay_global)&&  receiveWay_global==null) continue;
                    receiveWay = receiveWay_global;//????????????????????????
                    if ("email".equals(receiveWay) && emailAddress_global!=null){
                        urls.add(emailAddress_global);//????????????????????????
                    }
                }else if ("email".equals(receiveWay) && obj.getEmailAddress()!=null){
                    urls.add(obj.getEmailAddress());
                }
            }

            InetAddress address = InetAddress.getLocalHost();
            String hostIp = address.getHostAddress();
            for (String url:urls){
                JSONObject json = new JSONObject();
                List< Map<String,String>> contentList = new ArrayList<>();
                for (AgServicesMonitor ag:upList){
                    double sendRate = ag.getSendRate();
                    if (sendRate==-1) continue;//???????????????-1????????????????????????
                    if (sendRate==0) sendRate = sendRate_global;//??????????????????????????????
                    if (url.equals(ag.getEmailAddress()) && ((10*count)%(sendRate * 60)==0) && ag.getStatus()!=1 && ag.getMonitorStatus()==1){
                        Map<String,String> map = new HashMap<>();
                        map.put("??????IP:",hostIp);
                        map.put("????????????:",ag.getServiceFullName());
                        map.put("????????????:",ag.getMonitorUrl());
                        map.put("????????????:",getWarnIfo(ag.getStatus()));
                        contentList.add(map);
                    }else if (url.equals(ag.getEmailAddress()) && ag.getStatus()!=1 && sendRate==10 && ag.getMonitorStatus()==1){//???????????????????????????
                        Map<String,String> map = new HashMap<>();
                        map.put("????????????:",ag.getServiceFullName());
                        map.put("????????????:",ag.getMonitorUrl());
                        map.put("????????????:",getWarnIfo(ag.getStatus()));
                        map.put("??????IP:",hostIp);
                        contentList.add(map);
                    }
                }
                json.put("??????????????????",contentList.size());
                json.put("??????????????????",JSONArray.fromObject(contentList));
                if(contentList!=null && contentList.size()>0){
                    taskService.sendEmailTask(emailTitle,url,json.toString());
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSetWarnInfo(AgServicesMonitor agServicesMonitor) throws Exception {
        agServicesMonitorMapper.updateSetWarnInfo(agServicesMonitor);
    }

    private String getWarnIfo(int status) {
        String info = "";
        switch (status) {//?????? -1:???????????????1????????????????????????2???????????????????????????????????????3????????????????????????
            case -1:
                info = "????????????";
                break;
            case 2:
                info = "?????????????????????????????? ";
                break;
            case 3:
                info = "?????????????????????";
                break;
            default:
                break;
        }
        return info;
    }
}
