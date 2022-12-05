package com.augurit.agcloud.agcom.agsupport.sc.restlog.service.impl;


import com.augurit.agcloud.agcom.agsupport.common.mongodb.MongodbConstant;
import com.augurit.agcloud.agcom.agsupport.domain.AgSysLog;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.restlog.service.IRestLog;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestLogImpl implements IRestLog {

    @Autowired
    private MongoTemplate  mongoTemplate;


    @Override
    public Map<String,Object> search(AgSysLog agSysLog, Page page) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Map<String,Object> map = new HashMap<>(8);
        Query query = new Query();
        Criteria criteria = new Criteria();
        if(StringUtils.isNotBlank(agSysLog.getUserName())){
            criteria.and("userName").is(agSysLog.getUserName());
        }
        if(StringUtils.isNotBlank(agSysLog.getIpAddress())){
            criteria.and("ipAddress").is(agSysLog.getIpAddress());
        }
        if(StringUtils.isNotBlank(agSysLog.getAccessTimeStart()) && StringUtils.isNotBlank(agSysLog.getAccessTimeEnd())){
            criteria.andOperator(Criteria.where("operDate").gte(dateFormat.parse(agSysLog.getAccessTimeStart())),
                    Criteria.where("operDate").lte(dateFormat.parse(agSysLog.getAccessTimeEnd())));
        }else if(StringUtils.isNotBlank(agSysLog.getAccessTimeStart())){
            criteria.and("operDate").gte(dateFormat.parse(agSysLog.getAccessTimeStart()));
        }else if(StringUtils.isNotBlank(agSysLog.getAccessTimeEnd())){
            criteria.and("operDate").lte(dateFormat.parse(agSysLog.getAccessTimeEnd()));
        }
        query.addCriteria(criteria).with(new Sort(Sort.Direction.DESC,"operDate"));
        map.put("total",mongoTemplate.count(query, MongodbConstant.AGSUPPORT_REST_SERVICE_LOG));
        Pageable pageable = PageRequest.of(page.getPageNum()-1, page.getPageSize());
        List<AgSysLog> agSysLogs = mongoTemplate.find(query.with(pageable), AgSysLog.class,MongodbConstant.AGSUPPORT_REST_SERVICE_LOG);
        map.put("rows",agSysLogs);
        return map;
    }
}
