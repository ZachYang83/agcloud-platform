package com.augurit.agcloud.agcom.agsupport.sc.serverEffect.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.common.exception.ExceptionEnum;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerEffect;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerEffectExample;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgServerEffectMapper;
import com.augurit.agcloud.agcom.agsupport.sc.serverEffect.service.IAgServerEffectService;
import com.augurit.agcloud.agcom.agsupport.sc.serverEffect.util.FileUploadMinioUtils;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.agcom.agsupport.util.VerifyFileTypeUtils;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcom.common.LoginHelpClient;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
* <p>
*  服务管理-效果服务实现类
* </p>
*
* @author libc
* @since 2020-09-29
*/
@Service
@Transactional
public class AgServerEffectServiceImpl implements IAgServerEffectService {

    @Autowired
    private AgServerEffectMapper effectMapper;

    @Autowired
    private IAgUser iAgUser;

    /**
    * @Author: libc
    * @Date: 2020-09-29
    * @tips: 分页列表
    * @param param 查询条件
    * @param page 分页参数
    * @return
    */
    @Override
    public PageInfo<AgServerEffect> find(AgServerEffect param, Page page) {
        AgServerEffectExample example = new AgServerEffectExample();
        // 排序
        example.setOrderByClause("modify_time desc");
        example.setOrderByClause("create_time desc");
        AgServerEffectExample.Criteria criteria = example.createCriteria();
        //参数封装，如果不为null，需要添加条件
        if (!StringUtils.isEmpty(param.getName())) {
            criteria.andNameLike("%" + param.getName() + "%");
        }
        // 分组id
        if (!StringUtils.isEmpty(param.getGroupId())) {
            criteria.andGroupIdEqualTo(param.getGroupId());
        }
        // 文件类型，前端主要分两大类：（多媒体：multiMedia； 文档：doc）
        if (!StringUtils.isEmpty(param.getType())){
            if ("multiMedia".equals(param.getType())){
                // 多媒体类型文件  （video，audio，image）
                String[] str = {"video","audio","image"};
                criteria.andTypeIn(CollectionUtils.arrayToList(str));
            }else {
                // 文档：doc
                criteria.andTypeEqualTo(param.getType());
            }
        }
        PageHelper.startPage(page);
        List<AgServerEffect> agServerEffects = effectMapper.selectByExample(example);
        return new PageInfo<AgServerEffect>(agServerEffects);
    }

    /**
    * @Author: libc
    * @Date: 2020-09-29
    * @tips: 详情
    * @param id id
    * @return
    */
    @Override
    public AgServerEffect get(String id) {
        return effectMapper.selectByPrimaryKey(id);
    }

    /**
    * @Author: libc
    * @Date: 2020-09-29
    * @tips: 新增
    * @param param 保存对象
    * @param file
     * @return
    */
    @Override
    public void add(AgServerEffect param, MultipartFile file, HttpServletRequest request) {
        try{
            // 1. 上传文件
            String fileUrl = FileUploadMinioUtils.upload(file, "agcim", "effect");
            // 2.1 设置基本信息
            param.setId(UUID.randomUUID().toString());
            // 获取登陆用户
            String loginName = LoginHelpClient.getLoginName(request);
            AgUser agUser = iAgUser.findUserByName(loginName);
            // 用户id
            param.setUserId(agUser.getId());
            param.setCreateTime(new Date(System.currentTimeMillis()));
            param.setModifyTime(new Date(System.currentTimeMillis()));
            // 2.2 设置文件信息
            param.setUrl(fileUrl);
            // 设置文件类型。image：图片 ； audio：音频 ； video：视频 ； doc：文档。
            param.setType(VerifyFileTypeUtils.getFileType(file));
            param.setSize(String.valueOf(file.getSize()));
            // 2.3 保存对象
            effectMapper.insertSelective(param);
        }catch (Exception e){
            throw new AgCloudException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
    * @Author: libc
    * @Date: 2020-09-29
    * @tips: 修改
    * @param param 修改对象
    * @return
    */
    @Override
    public void update(AgServerEffect param) {
        try{
            if(StringUtils.isEmpty(param.getId())){
                throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
            }
            param.setModifyTime(new Timestamp(new Date().getTime()));
            effectMapper.updateByPrimaryKeySelective(param);
        }catch (Exception e){
            throw new AgCloudException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }

    /**
    * @Author: libc
    * @Date: 2020-09-29
    * @tips: 删除
    * @param id 删除的id
    * @return
    */
    @Override
    public void delete(String id) {
        try{
            if (StringUtils.isEmpty(id)){
                throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
            }
            String[] idsArr = id.split(",");
            if (idsArr == null || idsArr.length == 0){
                throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
            }
            if (idsArr.length == 1){
                // 删除单个记录
                effectMapper.deleteByPrimaryKey(id);
            }else {
                // 批量删除记录
                AgServerEffectExample example = new AgServerEffectExample();
                AgServerEffectExample.Criteria criteria = example.createCriteria();
                // ids
                criteria.andIdIn(CollectionUtils.arrayToList(idsArr));
                effectMapper.deleteByExample(example);
            }
        }catch (Exception e){
            throw new AgCloudException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }


}
