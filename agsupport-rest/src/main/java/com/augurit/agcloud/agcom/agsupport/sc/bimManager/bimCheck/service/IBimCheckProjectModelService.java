package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckProjectModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: libc
 * @Description: BIM审查项目模型-业务接口
 * @Date: 2020/11/2 18:12
 * @Version: 1.0
 */
public interface IBimCheckProjectModelService {

    /**
     *
     * @Author: libc
     * @Date: 2020/11/2 18:25
     * @tips: 根据BIM审查项目的id保存3dtiles模型文件
     * @param bimCheckProjectId BIM审查项目的id
     * @param file 一个3dtiels模型的zip文件压缩包
     * @param agcim3dprojectName BIM审查项目名称('BIM审查')
     * @return
     */
    void add(String bimCheckProjectId, MultipartFile file, String agcim3dprojectName);


    /**
     * @Author: libc
     * @Date: 2020/11/2 18:25
     * @tips: 删除
     * @param id 删除的id
     * @return
     */
    void delete(String id);

    /**
     *
     * @Author: libc
     * @Date: 2020/11/3 16:56
     * @tips: 根据bimCheckProjectId 删除项目下的模型集合
     * @param bimCheckProjectId BIM审查项目的id
     * @return
     */
    void deleteListByBimCheckProjectId(String bimCheckProjectId);


    /**
     *
     * @Author: libc
     * @Date: 2020/11/3 17:13
     * @tips: 根据bimCheckProjectId 查询项目下的模型集合
     * @param bimCheckProjectId BIM审查项目的id
     * @return 对应项目下的模型集合
     */
    List<AgBimCheckProjectModel> findListByBimCheckProjectId(String bimCheckProjectId);
}
