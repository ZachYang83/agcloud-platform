package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserMessage;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserMessageExample;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgUserMessageMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@Service
public class AgUserMessageServiceImpl implements IAgUserMessageService {

    @Autowired
    private AgUserMessageMapper userMessageMapper;


    @Cacheable(value= "userCache")
    @Override
    public Map<String, String> notPermissioList(){
        List<AgUserMessage> agUserMessages = userMessageMapper.selectByExample(null);
        Map<String, String> map = new HashMap<>();
        if(agUserMessages != null && agUserMessages.size() > 0){
            for(AgUserMessage user : agUserMessages){
                map.put(user.getUserId(), user.getUserId());
            }
        }
        return map;
    }

    @Override
    @Transactional
    @CacheEvict(value= "userCache", allEntries = true)
    public void add(AgUserMessage message) {
        AgUserMessageExample example = new AgUserMessageExample();
        example.createCriteria().andUserIdEqualTo(message.getUserId());
        List<AgUserMessage> messages = userMessageMapper.selectByExample(example);
        //没有此用户，才添加
        if(messages == null || messages.size() == 0){
            message.setCreateTime(new Date());
            message.setId(UUID.randomUUID().toString());
            userMessageMapper.insert(message);
        }
    }

}
