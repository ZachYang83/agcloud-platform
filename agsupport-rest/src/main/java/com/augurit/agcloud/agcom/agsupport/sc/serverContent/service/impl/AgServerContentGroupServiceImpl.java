package com.augurit.agcloud.agcom.agsupport.sc.serverContent.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.common.exception.ExceptionEnum;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContentExample;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContentGroup;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContentGroupExample;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgServerContentGroupMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgServerContentMapper;
import com.augurit.agcloud.agcom.agsupport.sc.serverContent.service.IAgServerContentGroupService;
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
* 服务内容分组表 服务实现类
* </p>
*
* @author libc
* @since 2020-09-23
*/
@Service
@Transactional
public class AgServerContentGroupServiceImpl implements IAgServerContentGroupService {

    @Autowired
    private AgServerContentGroupMapper contentGroupMapper;

    @Autowired
    private AgServerContentMapper contentMapper;


    /**
    * @Author: libc
    * @Date: 2020-09-23
    * @tips: 服务内容分组表分页列表
    * @param param 查询条件
    * @param page 分页参数
    * @return
    */
    @Override
    public PageInfo<AgServerContentGroup> find(AgServerContentGroup param, Page page) {
        AgServerContentGroupExample example = new AgServerContentGroupExample();
        example.setOrderByClause("modify_time desc");
        example.setOrderByClause("create_time desc");
        AgServerContentGroupExample.Criteria criteria = example.createCriteria();
        //参数封装，如果不为null，需要添加条件
        if (!StringUtils.isEmpty(param.getName())) {
            criteria.andNameLike("%" + param.getName() + "%");
        }
        PageHelper.startPage(page);
        List<AgServerContentGroup> agServerContentGroups = contentGroupMapper.selectByExample(example);
        return new PageInfo<AgServerContentGroup>(agServerContentGroups);
    }

    /**
    * @Author: libc
    * @Date: 2020-09-23
    * @tips: 服务内容分组表详情
    * @param id id
    * @return
    */
    @Override
    public AgServerContentGroup get(String id) {
        return contentGroupMapper.selectByPrimaryKey(id);
    }

    /**
    * @Author: libc
    * @Date: 2020-09-23
    * @tips: 服务内容分组表新增
    * @param param 保存对象
    * @return
    */
    @Override
    public void add(AgServerContentGroup param) {
        try{
            param.setId(UUID.randomUUID().toString());
            param.setCreateTime(new Timestamp(new Date().getTime()));
            param.setModifyTime(new Timestamp(new Date().getTime()));
            contentGroupMapper.insertSelective(param);
        }catch (Exception e){
            throw new AgCloudException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
    * @Author: libc
    * @Date: 2020-09-23
    * @tips: 服务内容分组表修改
    * @param param 修改对象
    * @return
    */
    @Override
    public void update(AgServerContentGroup param) {
        try{
            if (StringUtils.isEmpty(param.getId())){
                throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
            }
            param.setModifyTime(new Timestamp(new Date().getTime()));
            contentGroupMapper.updateByPrimaryKeySelective(param);
        }catch (Exception e){
            throw new AgCloudException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }

    /**
    * @Author: libc
    * @Date: 2020-09-23
    * @tips: 服务内容分组表删除(单个条目)
    * @param id 删除的id
    * @return
    */
    @Override
    public void delete(String id) {
        try{
            if (StringUtils.isEmpty(id)){
                throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
            }
            // 删除关联服务内容表数据
            AgServerContentExample example = new AgServerContentExample();
            AgServerContentExample.Criteria criteria = example.createCriteria();
            // groupId
            criteria.andGroupIdEqualTo(id);
            contentMapper.deleteByExample(example);

            // 删除关联服务内容分组表数据
            contentGroupMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            throw new AgCloudException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }

    /**
     *
     * @Author: libc
     * @Date: 2020/9/23 10:21
     * @tips: 查询所有
     * @param
     * @return
     */
    public List<AgServerContentGroup> findAll() {
        // 添加排序参数
        AgServerContentGroupExample example = new AgServerContentGroupExample();
        example.setOrderByClause("id asc");
        return contentGroupMapper.selectByExample(example);
    }


}
