package com.augurit.agcloud.agcom.agsupport.sc.problemDiscern.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.common.exception.ExceptionEnum;
import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgProblemDiscern;
import com.augurit.agcloud.agcom.agsupport.mapper.AgProblemDiscernMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.problemDiscern.service.AgProblemDiscernService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
public class AgProblemDiscernServiceImpl implements AgProblemDiscernService {
    private static final Logger logger = LoggerFactory.getLogger(AgProblemDiscernService.class);

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
     * @param files
     * @Return
     * @Date 2020/8/28 14:05
     */
    public void save(AgProblemDiscern problemDiscern, String paramType, MultipartFile[] files) throws Exception {
        if (files == null || files.length == 0){
            throw new AgCloudException(ExceptionEnum.INVALID_FILE_TYPE);
        }
        try{
            if (StringUtils.isNotEmpty(problemDiscern.getId())) {
                // 修改
                // 修改时间
                problemDiscern.setModifyTime(new Timestamp(new Date().getTime()));
                // 获取图片二进制数组
                byte[] problemImgData = files[0].getBytes();
                files[0].getInputStream().read(problemImgData);
                problemDiscern.setProblemImg(problemImgData);
                if ("1".equals(paramType) || "2".equals(paramType)){
                    problemDiscern.setpType(paramType);
                }
                else {
                    throw new AgCloudException(400,"标识符错误，只能为 1：BIM审查或者 2：标签管理");
                }
                problemDiscernMapper.updateByPrimaryKeySelective(problemDiscern);
            } else {
                // 新增
                //------------注释掉此部分判断 start-----------------------
                // 查询图片标识是否已存在
//            AgProblemDiscern pd = findByImgId(problemDiscern.getImgId());
//            if (pd != null){
//                throw new AgCloudException(400,"图片唯一标识已存在");
//            }
                //------------注释掉此部分判断 end-----------------------

                if ("1".equals(paramType)){
                    //先删除掉所有数据，再重新添加
                    List<AgProblemDiscern> listByImgId = problemDiscernMapper.findListByImgId(problemDiscern.getImgId(),paramType);
                    if(listByImgId != null && listByImgId.size() > 0){
                        int arrLength = listByImgId.size();
                        String[] deleteIdArrs = new String[arrLength];
                        for(int i = 0; i < arrLength; i++){
                            deleteIdArrs[i] = listByImgId.get(i).getId();
                        }
                        problemDiscernMapper.deleteByIds(deleteIdArrs);
                    }
                }

                // 遍历新增多条图片（多条记录） 同一个imgId
                for (int i = 0;i < files.length;i++) {
                    AgProblemDiscern apd = new AgProblemDiscern();
                    // 基本信息
                    apd.setDescription(problemDiscern.getDescription());
                    apd.setImgId(problemDiscern.getImgId());
                    apd.setRemark(problemDiscern.getRemark());
                    // 获取图片二进制数组
                    byte[] tempImgData = files[i].getBytes();
                    files[i].getInputStream().read(tempImgData);
                    apd.setProblemImg(tempImgData);
                    // 创建时间
                    apd.setCreateTime(new Timestamp(new Date().getTime()));
                    apd.setId(UUID.randomUUID().toString());
                    apd.setpType(paramType);
                    problemDiscernMapper.insertSelective(apd);
                }
            }
        }catch (Exception e){
            logger.error("标签管理保存失败！");
            logger.info(e.getMessage());
            e.printStackTrace();
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

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据图片标识查询对象集合
     * @param imgId 图片唯一标识
     * @Return 集合
     * @Date 2020/9/2 11:42
     */
    public List<AgProblemDiscern> findListByImgId(String imgId, String problemType) {
        return problemDiscernMapper.findListByImgId(imgId, problemType);
    }

//    @Override
//    public void view(String id, HttpServletResponse response){
//        try {
//            AgProblemDiscern problemDiscern = problemDiscernMapper.selectByPrimaryKey(id);
//            FileUtil.writerFile(problemDiscern.getProblemImg(),problemDiscern.getId()+".png",false,response);
//        }catch (Exception e){
//            logger.error("标签管理预览失败！");
//            logger.info(e.getMessage());
//            e.printStackTrace();
//        }
//    }

}
