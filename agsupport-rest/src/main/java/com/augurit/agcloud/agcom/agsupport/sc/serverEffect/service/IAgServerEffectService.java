package com.augurit.agcloud.agcom.agsupport.sc.serverEffect.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerEffect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* <p>
*  服务管理-效果服务类
* </p>
*
* @author libc
* @since 2020-09-29
*/
public interface IAgServerEffectService {

    /**
    * @Author: libc
    * @Date: 2020-09-29
    * @tips: 分页列表
    * @param param 查询条件
    * @param page 分页参数
    * @return
    */
    PageInfo<AgServerEffect> find(AgServerEffect param, Page page);

    /**
    * @Author: libc
    * @Date: 2020-09-29
    * @tips: 详情
    * @param id id
    * @return
    */
    AgServerEffect get(String id);

    /**
    * @Author: libc
    * @Date: 2020-09-29
    * @tips: 新增
    * @param param 保存对象
    * @param file 上传文件
     * @return
    */
    void add(AgServerEffect param, MultipartFile file ,HttpServletRequest request);

    /**
    * @Author: libc
    * @Date: 2020-09-29
    * @tips: 修改
    * @param param 修改对象
    * @return
    */
    void update(AgServerEffect param);

    /**
    * @Author: libc
    * @Date: 2020-09-29
    * @tips: 删除
    * @param id 删除的id
    * @return
    */
    void delete(String id);

}
