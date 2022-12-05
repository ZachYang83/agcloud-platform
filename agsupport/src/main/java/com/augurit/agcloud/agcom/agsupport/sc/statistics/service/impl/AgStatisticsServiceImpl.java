package com.augurit.agcloud.agcom.agsupport.sc.statistics.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.mapper.*;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.sc.statistics.service.IAgStatisticsService;
import com.augurit.agcloud.framework.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhangmingyang
 * @Description: 数据概览接口实现
 * @date 2019-08-20 14:27
 */
@Service
public class AgStatisticsServiceImpl implements IAgStatisticsService {
    @Autowired
    private AgStatisticsMapper agStatisticsMapper;
    @Autowired
    private IAgDic agDic;
    @Autowired
    private AgSupDatasourceMapper agSupDatasourceMapper;
    @Autowired
    private AgDataOverviewMapper agDataOverviewMapper;

    @Autowired
    private AgDataSubjectMapper agDataSubjectMapper;
    @Autowired
    private AgDataSituationMapper agDataSituationMapper;
    @Override
    public Integer layerCount() throws Exception {
        return agStatisticsMapper.layerCount();
    }

    @Override
    public List<Map> countByDataType() throws Exception {
        return agStatisticsMapper.countByDataType();
    }

    @Override
    public List<Map> countByDataSource() {
        return agStatisticsMapper.countByDataSource();
    }

    @Override
    public Long getResourceSize() {
        return agStatisticsMapper.getResourceSize();
    }

    @Override
    public List<Map> getResourceSizeBySubjectType() {
        List<Map> resultMap = new ArrayList<>();
        // 获取需要统计的主题
        List<AgDataSubject> subjects = agDataSubjectMapper.findByLevel(0);
        // 获取主题下的所有子目录
        if (subjects.size() > 0){
            for (AgDataSubject subject : subjects){
                List<AgDataSubject> bysubjects = agDataSubjectMapper.findByXpath(subject.getXpath());
                List<String> collect = bysubjects.stream().map(AgDataSubject::getId).collect(Collectors.toList());
                Map map  = agStatisticsMapper.countBySubjectIds(collect);
                map.put("subjectName",subject.getSubjectName());
                resultMap.add(map);
            }
        }
        return resultMap;
    }

    @Override
    public int insert(AgDataSituation agDataSituation) {
        return agDataSituationMapper.insert(agDataSituation);
    }

    @Override
    public List<Map> getResourceSituationByTime() throws Exception{
        List<Map> maps = new ArrayList<>();
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = new Date();
        List<AgDataSituation> all = agDataSituationMapper.findAll();
        for (AgDataSituation agData : all){
            String s = dateToStr(agData.getStatisticalTime(), sDateFormat);
            String nowStr = dateToStr(nowDate,sDateFormat);
            if (s.equals(nowStr)){
                all.remove(agData);
                break;
            }
        }
        // 先统计当天的数据
        AgDataSituation agDataSituation = agDataSituationMapper.statisticsCountAndSize();
        agDataSituation.setStatisticalTime(nowDate);
        //all.add(all.size(),agDataSituation);
        for (AgDataSituation agData : all){
            Map m = new HashMap();
            m.put("dayValue",dateToStr(agData.getStatisticalTime(),sDateFormat));
            m.put("dataSize",agData.getDataResourceSize());
            m.put("dataCount",agData.getDataResourceNum());
            maps.add(m);
        }

        // 按照容量大小存进map,以去掉重复的容量的数据
        Map<String,Map> rmap = new LinkedHashMap();
        int size = all.size();
        for (int i = size-1;i>=0;i--){
            if(true){

            }
            rmap.put(maps.get(i).get("dataSize").toString(),maps.get(i));
        }
        List<Map> result = new ArrayList<>();
        // 显示7天的数据
        int n = 0;
        for (Map.Entry<String, Map> entry : rmap.entrySet()){
            n++;
            result.add(entry.getValue());
            if ( n == 7){
                break;
            }
        }
        // 当天数据要加入
        int index = 0;
        Map _map = new LinkedHashMap();
        _map.put("dataSize",agDataSituation.getDataResourceSize());
        _map.put("dataCount",agDataSituation.getDataResourceNum());
        _map.put("dayValue",dateToStr(nowDate,sDateFormat));
        result.set(index,_map);
        return result;
    }

    public String dateToStr(Date date,SimpleDateFormat sDateForma){
        try {
            String str = sDateForma.format(date);
            return str;
        } catch(Exception px) {
            px.printStackTrace();
        }
        return null;
    }

    @Override
    public AgDataSituation statisticsCountAndSize() {
        return agDataSituationMapper.statisticsCountAndSize();
    }

    @Override
    public List<AgDataSituation> findAll() {
        return agDataSituationMapper.findAll();
    }
}
