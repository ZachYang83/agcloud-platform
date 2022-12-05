package com.augurit.agcloud.agcom.agsupport.sc.serverEffect.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.common.exception.ExceptionEnum;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerEffectExample;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerEffectGroup;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerEffectGroupExample;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgServerEffectGroupMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgServerEffectMapper;
import com.augurit.agcloud.agcom.agsupport.sc.serverEffect.service.IAgServerEffectGroupService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
* <p>
* 服务管理-效果分组表 服务实现类
* </p>
*
* @author libc
* @since 2020-09-30
*/
@Service
@Transactional
public class AgServerEffectGroupServiceImpl implements IAgServerEffectGroupService {

    @Autowired
    private AgServerEffectGroupMapper effectGroupMapper;

    @Autowired
    private AgServerEffectMapper effectMapper;

    /**
    * @Author: libc
    * @Date: 2020-09-30
    * @tips: 服务管理-效果分组表分页列表
    * @param param 查询条件
    * @param page 分页参数
    * @return
    */
    @Override
    public PageInfo<AgServerEffectGroup> find(AgServerEffectGroup param, Page page) {
        AgServerEffectGroupExample example = new AgServerEffectGroupExample();
        // 排序
        example.setOrderByClause("modify_time desc");
        example.setOrderByClause("create_time desc");
        AgServerEffectGroupExample.Criteria criteria = example.createCriteria();
        //参数封装，如果不为null，需要添加条件
        if (!StringUtils.isEmpty(param.getName())) {
            criteria.andNameLike("%" + param.getName() + "%");
        }
        PageHelper.startPage(page);
        List<AgServerEffectGroup> agServerEffectGroups = effectGroupMapper.selectByExample(example);
        return new PageInfo<AgServerEffectGroup>(agServerEffectGroups);
    }

    /**
    * @Author: libc
    * @Date: 2020-09-30
    * @tips: 服务管理-效果分组表详情
    * @param id id
    * @return
    */
    @Override
    public AgServerEffectGroup get(String id) {
        return effectGroupMapper.selectByPrimaryKey(id);
    }

    /**
    * @Author: libc
    * @Date: 2020-09-30
    * @tips: 服务管理-效果分组表新增
    * @param param 保存对象
    * @return
    */
    @Override
    public void add(AgServerEffectGroup param) {
        try{
            param.setId(UUID.randomUUID().toString());
            param.setCreateTime(new Timestamp(new Date().getTime()));
            param.setModifyTime(new Timestamp(new Date().getTime()));
            effectGroupMapper.insertSelective(param);
        }catch (Exception e){
            throw new AgCloudException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
    * @Author: libc
    * @Date: 2020-09-30
    * @tips: 服务管理-效果分组表修改
    * @param param 修改对象
    * @return
    */
    @Override
    public void update(AgServerEffectGroup param) {
        try{
            if (StringUtils.isEmpty(param.getId())){
                throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
            }
            param.setModifyTime(new Timestamp(new Date().getTime()));
            effectGroupMapper.updateByPrimaryKeySelective(param);
        }catch (Exception e){
            throw new AgCloudException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }

    /**
    * @Author: libc
    * @Date: 2020-09-30
    * @tips: 服务管理-效果分组表删除(单个条目)
    * @param id 删除的id
    * @return
    */
    @Override
    public void delete(String id) {
        try{
            if (StringUtils.isEmpty(id)){
                throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
            }
            // 删除关联效果数据
            AgServerEffectExample example = new AgServerEffectExample();
            AgServerEffectExample.Criteria criteria = example.createCriteria();
            // groupId
            criteria.andGroupIdEqualTo(id);
            effectMapper.deleteByExample(example);

            // 删除效果分组
            effectGroupMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            throw new AgCloudException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }

    /**
     *
     * @Author: libc
     * @Date: 2020-09-30
     * @tips: 查询所有
     * @return
     */
    public List<AgServerEffectGroup> findAll() {
        // 添加排序参数
        AgServerEffectGroupExample example = new AgServerEffectGroupExample();
        example.setOrderByClause("id asc");
        return effectGroupMapper.selectByExample(example);
    }


}
