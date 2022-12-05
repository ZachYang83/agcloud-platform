package com.augurit.agcloud.agcom.agsupport.sc.statistics.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgDataSubject;
import com.augurit.agcloud.agcom.agsupport.mapper.AgDataOverviewMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgDataSubjectMapper;
import com.augurit.agcloud.agcom.agsupport.sc.statistics.service.IAgDataSubjectService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangmingyang
 * @Description: 数据专题接口实现
 * @date 2019-09-02 10:05
 */
@Service
public class AgDataSubjectServiceImpl implements IAgDataSubjectService {
    @Autowired
    private AgDataSubjectMapper agDataSubjectMapper;
    @Autowired
    private AgDataOverviewMapper agDataOverviewMapper;
    @Override
    public int deleteById(String id) {
        return agDataSubjectMapper.deleteById(id);
    }

    @Override
    public int deletcByXpath(String xpath) {
        return agDataSubjectMapper.deletcByXpath(xpath);
    }

    @Override
    public int insert(AgDataSubject record) {
        return agDataSubjectMapper.insert(record);
    }

    @Override
    public AgDataSubject selectById(String id) {
        return agDataSubjectMapper.selectById(id);
    }

    @Override
    public List<AgDataSubject> findByXpath(String xpath) {
        return agDataSubjectMapper.findByXpath(xpath);
    }

    @Override
    public int updateSubject(AgDataSubject record) {
        return agDataSubjectMapper.updateSubject(record);
    }

    @Override
    public List<AgDataSubject> findAllSubject() {
        return agDataSubjectMapper.findAllSubject();
    }

    @Override
    public List<AgDataSubject> selectByParenIdAndName(String parentId, String name) {
        return agDataSubjectMapper.selectByParenIdAndName(parentId,name);
    }

    @Override
    public List<AgDataSubject> selectByIds(String[] ids) {
        return agDataSubjectMapper.selectByIds(ids);
    }

    @Override
    public void updateSubjectBatch(List<AgDataSubject> list) {
        agDataSubjectMapper.updateSubjectBatch(list);
    }

    @Override
    @Transactional
    public void deleteSubjects(String id){
        AgDataSubject agDataSubject = agDataSubjectMapper.selectById(id);
        if (StringUtils.isBlank(agDataSubject.getXpath())){
            return;
        }
        List<AgDataSubject> subjects = agDataSubjectMapper.findByXpath(agDataSubject.getXpath());
        agDataSubjectMapper.deletcByXpath(agDataSubject.getXpath());
        List<String> subjectIds = subjects.stream().map(AgDataSubject::getId).collect(Collectors.toList());
        agDataOverviewMapper.deleteBySubjectIds(subjectIds.toArray(new String[subjectIds.size()]));
    }

    @Override
    public List<AgDataSubject> findByLevel(int level) {
        return agDataSubjectMapper.findByLevel(level);
    }

    @Override
    public AgDataSubject selectByName(String name) {
        return agDataSubjectMapper.selectByName(name);
    }
}
