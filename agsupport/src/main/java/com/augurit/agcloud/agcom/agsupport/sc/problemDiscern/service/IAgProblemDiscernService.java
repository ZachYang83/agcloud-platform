package com.augurit.agcloud.agcom.agsupport.sc.problemDiscern.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgProblemDiscern;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: libc
 * @Description: 问题识别模块-业务接口
 * @Date: 2020/8/28 13:54
 * @Version: 1.0
 */
public interface IAgProblemDiscernService {

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据条件（描述）分页查询集合
     * @param description 描述信息
     * @param page 分页对象
     * @Return
     * @Date 2020/8/28 14:03
     */
    PageInfo<AgProblemDiscern> findList(String description, Page page);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 保存问题识别记录（新增或修改）
     * @param agProblemDiscern 问题识别对象
     * @param file
     * @Return
     * @Date 2020/8/28 14:05
     */
    void save(AgProblemDiscern agProblemDiscern, String problemType, MultipartFile file) throws Exception;

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据id批量删除
     * @param ids 批量删除的id数组
     * @Return
     * @Date 2020/8/28 14:06
     */
    void deleteByIds(String[] ids);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据id删除
     * @param id
     * @Return
     * @Date 2020/8/28 14:06
     */
    void deleteById(String id);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据图片标识查询对象
     * @param imgId 图片唯一标识
     * @Return
     * @Date 2020/9/2 11:42
     */
    AgProblemDiscern findByImgId(String imgId, String problemType);

}
