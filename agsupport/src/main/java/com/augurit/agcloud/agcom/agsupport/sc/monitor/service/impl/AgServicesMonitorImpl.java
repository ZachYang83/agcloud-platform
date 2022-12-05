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
            //获取监控全局开关，如果全局开启则继续，否则不进行任何监控 1:启动监控，-1：停止监控
            AgDefaultValue adv = iAgDefaultValue.findByKey("globalMonitorStatus");
            if (adv!=null){
                String monitorStatusStr = JSONObject.fromObject(adv.getDefaultValue()).getString("monitorStatus");
                int globalMonitorStatus = Integer.parseInt(monitorStatusStr);
                if (globalMonitorStatus==-1) return;
            }

            //获取所有服务的用户名和密码
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
                //totalTimesCount = JSONObject.fromObject(jsonStr).getInt("TOTAL_TIMES_COUNT"); //服务成功访问总数
                JSONObject obj = JSONObject.fromObject(jsonStr);
                //服务成功访问总数
                if (obj.has("TOTAL_TIMES_COUNT")){
                    totalTimesCount = Integer.parseInt(obj.getString("TOTAL_TIMES_COUNT"));
                }else if (obj.has("total_times_count")){
                    totalTimesCount = Integer.parseInt(obj.getString("total_times_count"));
                }

                //badRequestCount = JSONObject.fromObject(jsonStr).getInt("BAD_REQUEST_COUNT");//服务访问失败总数
                if (obj.has("BAD_REQUEST_COUNT")){
                    badRequestCount = Integer.parseInt(obj.getString("BAD_REQUEST_COUNT"));
                }else if (obj.has("bad_request_count")){
                    badRequestCount = Integer.parseInt(obj.getString("bad_request_count"));
                }
                //服务访问失败总数
                if (totalTimesCount > 0) {
                    //totalTimes = JSONObject.fromObject(jsonStr).getInt("REQUEST_TOTAL_TIMES");//服务成功访问总时间
                    //服务成功访问总时间
                    if (obj.has("REQUEST_TOTAL_TIMES")){
                        totalTimes = Integer.parseInt(obj.getString("REQUEST_TOTAL_TIMES"));
                    }else if (obj.has("request_total_times")){
                        totalTimes = Integer.parseInt(obj.getString("request_total_times"));
                    }
                } else {
                    totalTimes = 0;
                }
                doasm.setSuccessRate(totalTimesCount == 0 ? 0 : (int)(((float)totalTimesCount / (float)(totalTimesCount + badRequestCount)) * 100));//成功率
                doasm.setAverageTime(totalTimesCount == 0 ? 0 : totalTimes / totalTimesCount);
                doasm.setRequestTotal(totalTimesCount + badRequestCount);//服务请求总次数

                //System.out.println("第几个：" + i++ + "----" + doasm.getMonitorUrl());
                //使用java标准类库java.net.URL 提取监控地址的ip和端口
                URI uri = new URI(doasm.getMonitorUrl());
                String ip = uri.getHost();
                int port = uri.getPort();
                String userName  = agserversMap.get(ip+port+"u");
                String password = agserversMap.get(ip+port+"p");
                if (userName==null && password==null) {
                    System.out.println("服务:"+doasm.getServiceFullName()+" 未注册 ："+ip+":"+port);
                }else if (password==null) {
                    System.out.println("服务:"+doasm.getServiceFullName()+" 未注册 ："+ip+":"+port);
                }else if (userName==null) {
                    System.out.println("服务:"+doasm.getServiceFullName()+" 未注册 ："+ip+":"+port);
                }
                String token = TokenUtil.getToken(doasm.getMonitorUrl(),userName,password);
                //记录一天中所有的定时请求服务情况
                JSONObject jsonMonitorDetail = new JSONObject();
                //暂时只保留12小时的监控记录
           /* if (doasm.getMonitorDetail() != null) {
                if ((10*count)% (12*60)!=0){
                    jsonMonitorDetail = JSONObject.fromObject(doasm.getMonitorDetail());
                }
            }*/
                if ("errorUrl".equals(token)) {
                    doasm.setStatus(2);//服务地址错误无法访问
                    doasm.setAvailable(0);
                    jsonMonitorDetail.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "服务地址错误无法访问");
                    doasm.setMonitorDetail(jsonMonitorDetail.toString());
                }else if ("noLayerService".equals(token) || token==null){
                    doasm.setStatus(3);//图层服务不存在
                    doasm.setAvailable(0);
                    jsonMonitorDetail.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "图层服务不存在");
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
                        doasm.setStatus(-1);//状态
                    }else {
                        doasm.setStatus(status);//状态
                    }
                    String formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    switch (status) {
                        case 0:
                            jsonMonitorDetail.put(formatDate,"服务停止");
                            break;
                        case 1:
                            jsonMonitorDetail.put(formatDate, "服务已经启动");
                            break;
                        case 2:
                            jsonMonitorDetail.put(formatDate, "服务地址错误无法访问");
                            break;
                        case 3:
                            jsonMonitorDetail.put(formatDate, "图层服务不存在");
                            break;
                        default:
                            break;
                    }
                    doasm.setMonitorDetail(jsonMonitorDetail.toString());
                }
                upList.add(doasm);
                //iAgServicesMonitor.updataAgServicesMonitor(doasm);
            }
            System.out.println("开始更新监控列表 "+upList.size()+" 条");
            for (AgServicesMonitor monitor:upList){
                //打印状态为已删的监控情况
                if (monitor.getStatus()==3) System.out.println("状态为已删的"+monitor.getServiceFullName()+" "+monitor.getMonitorUrl());
                monitor.setLastMonitorTime(new Date());
                iAgServicesMonitor.updataAgServicesMonitor(monitor);
            }
            System.out.println("监控列表更新完成 "+upList.size()+" 条");
            /**
             * 根据告警规则是否推送消息
             * 定时器是10分钟触发一次，发送时间间隔单位是小时
             * 触发推送消息条件： (10*count) % (sendRate * 60)==0
             */

            //获取全局告警设置信息，如果单条没有则使用全局的设置
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

            //如果异常的服务收件人是同一个，则合并告警信息为一封邮件,使用set实现合并
            Set<String> urls = new HashSet<>();
            for (AgServicesMonitor obj:upList){
                String receiveWay = obj.getReceiveWay();
                if ("".equals(receiveWay) || receiveWay==null){
                    if ("".equals(receiveWay_global)&&  receiveWay_global==null) continue;
                    receiveWay = receiveWay_global;//使用全局接收方式
                    if ("email".equals(receiveWay) && emailAddress_global!=null){
                        urls.add(emailAddress_global);//使用全局接收邮箱
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
                    if (sendRate==-1) continue;//时间间隔为-1为不发送告警信息
                    if (sendRate==0) sendRate = sendRate_global;//使用全局发送时间间隔
                    if (url.equals(ag.getEmailAddress()) && ((10*count)%(sendRate * 60)==0) && ag.getStatus()!=1 && ag.getMonitorStatus()==1){
                        Map<String,String> map = new HashMap<>();
                        map.put("系统IP:",hostIp);
                        map.put("服务名称:",ag.getServiceFullName());
                        map.put("服务地址:",ag.getMonitorUrl());
                        map.put("告警信息:",getWarnIfo(ag.getStatus()));
                        contentList.add(map);
                    }else if (url.equals(ag.getEmailAddress()) && ag.getStatus()!=1 && sendRate==10 && ag.getMonitorStatus()==1){//临时加测试邮件发送
                        Map<String,String> map = new HashMap<>();
                        map.put("服务名称:",ag.getServiceFullName());
                        map.put("服务地址:",ag.getMonitorUrl());
                        map.put("告警信息:",getWarnIfo(ag.getStatus()));
                        map.put("系统IP:",hostIp);
                        contentList.add(map);
                    }
                }
                json.put("异常服务数量",contentList.size());
                json.put("异常服务列表",JSONArray.fromObject(contentList));
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
        switch (status) {//状态 -1:服务停止，1：服务已经启动，2：服务地址错误，无法访问，3：图层服务不存在
            case -1:
                info = "服务停止";
                break;
            case 2:
                info = "服务地址错误无法访问 ";
                break;
            case 3:
                info = "图层服务不存在";
                break;
            default:
                break;
        }
        return info;
    }
}
