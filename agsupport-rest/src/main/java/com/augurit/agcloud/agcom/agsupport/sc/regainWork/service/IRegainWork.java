package com.augurit.agcloud.agcom.agsupport.sc.regainWork.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface IRegainWork {
    Map<String,List<Project>> getRegainedSZProject() throws Exception;//按地区分组获取已复工的市政项目
    Map<String,List<Project>> getRegainedFJProject() throws Exception;//按地区分组获取已复工的房建项目
    List<Project> getRegainedProject() throws Exception;//获取已复工项目
    Map<String,List<Project>> getPlanRegainSZProject(String regainDate) throws Exception;//按地区分组获取计划复工的市政项目
    Map<String,List<Project>> getPlanRegainFJProject(String regainDate) throws Exception;//按地区分组获取计划复工的房建项目
    List<Project> getPlanRegainProject(String regainDate) throws Exception;//获取计划复工项目
    Map<String,List<Project>> getNoPlanRegainSZProject() throws Exception;//按地区分组获取无复工计划的市政项目
    Map<String,List<Project>> getNoPlanRegainFJProject() throws Exception;//按地区分组获取无复工计划的房建项目
    List<Project> getNoPlanRegainProject() throws Exception;//获取无复工计划的项目

    JSONObject getStatisticsOfCity() throws Exception;//统计全市的项目数据
    JSONArray getStatisticsByBM() throws Exception;//按部门分组统计
    JSONArray getStatisticsByDQ() throws Exception;//按地区分组统计
    JSONArray getStatisticsByXMLX() throws Exception;//按项目类型分组统计
    JSONArray getStatisticsOfZDXM() throws Exception;//按部门分组统计重点企业(项目)数据
    JSONArray getZDXMQD() throws Exception;//获取重点企业(项目)数据
    JSONArray getZDXMOfBF() throws Exception;//获取帮扶-重点项目列表
    JSONArray getCSGXOfBF() throws Exception;//获取帮扶-重点项目列表
    JSONArray getSJZYOfBF() throws Exception;//获取帮扶-重点项目列表
    JSONArray getFDCKFOfBF() throws Exception;//获取帮扶-重点项目列表
    JSONArray getStatisticsOfQS() throws Exception;//获取复工趋势统计数据
    JSONArray getStatisticsOfCXQY() throws Exception;//按地区分组获取诚信企业项目统计数据
    JSONArray getXMQDOfCXQY() throws Exception;//获取诚信企业项目清单
    JSONArray getYQDT_XMQD() throws Exception;//获取项目清单信息

    JSONObject getProjectBasicInfo(String gcbm) throws Exception;//获取工程基础信息
    JSONObject getProjectInfo(String gcbm) throws Exception;//根据项目编号获取项目上报的疫情详细信息
    JSONArray getWorkerInfo(String gcbm) throws Exception;//根据项目编号获取项目人员详细信息
    JSONObject getProjectJSDW(String gcbm) throws Exception;//根据项目编号获取建设单位详细信息
    JSONObject getProjectJLDW(String gcbm) throws Exception;//根据项目编号获取监理单位详细信息
    JSONObject getProjectSGDW(String gcbm) throws Exception;//根据项目编号获取施工单位详细信息
    void writeRedisCache();//更新复工项目信息缓存
}
