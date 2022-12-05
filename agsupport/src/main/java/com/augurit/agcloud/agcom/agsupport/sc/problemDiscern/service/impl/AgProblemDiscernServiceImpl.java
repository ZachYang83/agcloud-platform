package com.augurit.agcloud.agcom.agsupport.sc.problemDiscern.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgProblemDiscern;
import com.augurit.agcloud.agcom.agsupport.mapper.AgProblemDiscernMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.problemDiscern.service.IAgProblemDiscernService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: libc
 * @Description: 问题识别模块-业务实现类
 * @Date: 2020/8/28 13:56
 * @Version: 1.0
 */
@Service
@Transactional
public class AgProblemDiscernServiceImpl implements IAgProblemDiscernService {

    @Autowired
    private AgProblemDiscernMapper problemDiscernMapper;

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据条件（描述）分页查询集合
     * @param description 描述信息
     * @param page 分页对象
     * @Return
     * @Date 2020/8/28 14:03
     */
    public PageInfo<AgProblemDiscern> findList(String description, Page page) {
        PageHelper.startPage(page);
        List<AgProblemDiscern> list = problemDiscernMapper.findList(description);
        return new PageInfo<AgProblemDiscern>(list);
    }

    /**
     * @Version  1.0
     * @Author libc
     * @Description 保存问题识别记录（新增或修改）
     * @param problemDiscern 问题识别对象
     * @param file
     * @Return
     * @Date 2020/8/28 14:05
     */
    public void save(AgProblemDiscern problemDiscern, String problemType, MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()){
            // 获取图片二进制数组
            byte[] problemImgData = file.getBytes();
            file.getInputStream().read(problemImgData);
            problemDiscern.setProblemImg(problemImgData);
        }
        if (StringUtils.isNotEmpty(problemDiscern.getId())) {
            // 查询图片标识是否已存在
            AgProblemDiscern pd = findByImgId(problemDiscern.getImgId(),problemType);
            if (pd != null && !pd.getId().equals(problemDiscern.getId())){
                throw  new Exception("图片唯一标识已存在");
            }
            if ("1".equals(problemType) || "2".equals(problemType)){
                problemDiscern.setpType(problemType);
            }
            else {
                throw new Exception("标识符错误，只能为 1：BIM审查或者 2：标签管理");
            }
            // 修改时间
            problemDiscern.setModifyTime(new Timestamp(new Date().getTime()));
            problemDiscernMapper.updateByPrimaryKeySelective(problemDiscern);
        } else {
            // 查询图片标识是否已存在
            AgProblemDiscern pd = findByImgId(problemDiscern.getImgId(),problemType);
            if (pd != null){
                throw  new Exception("图片唯一标识已存在");
            }
            // 创建时间
            problemDiscern.setCreateTime(new Timestamp(new Date().getTime()));
            problemDiscern.setId(UUID.randomUUID().toString());
            problemDiscern.setpType(problemType);
            problemDiscernMapper.insertSelective(problemDiscern);
        }
    }

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据id批量删除
     * @param ids 批量删除的id数组
     * @Return
     * @Date 2020/8/28 14:06
     */
    public void deleteByIds(String[] ids) {
        problemDiscernMapper.deleteByIds(ids);
    }

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据id删除
     * @param id
     * @Return
     * @Date 2020/8/28 14:06
     */
    public void deleteById(String id) {
        problemDiscernMapper.deleteByPrimaryKey(id);
    }

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据图片标识查询对象
     * @param imgId 图片唯一标识
     * @Return
     * @Date 2020/9/2 11:42
     */
    public AgProblemDiscern findByImgId(String imgId, String problemType) {
        return problemDiscernMapper.findByImgId(imgId, problemType);
    }

}
