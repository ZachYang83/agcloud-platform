package com.augurit.agcloud.agcom.agsupport.sc.serverContent.service;


import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContentGroup;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* <p>
* 服务内容分组表 服务类
* </p>
*
* @author libc
* @since 2020-09-23
*/
public interface IAgServerContentGroupService {

    /**
    * @Author: libc
    * @Date: 2020-09-23
    * @tips: 服务内容分组表分页列表
    * @param param 查询条件
    * @param page 分页参数
    * @return
    */
    PageInfo<AgServerContentGroup> find(AgServerContentGroup param, Page page);

    /**
    * @Author: libc
    * @Date: 2020-09-23
    * @tips: 服务内容分组表详情
    * @param id id
    * @return
    */
    AgServerContentGroup get(String id);

    /**
    * @Author: libc
    * @Date: 2020-09-23
    * @tips: 服务内容分组表新增
    * @param param 保存对象
    * @return
    */
    void add(AgServerContentGroup param);

    /**
    * @Author: libc
    * @Date: 2020-09-23
    * @tips: 服务内容分组表修改
    * @param param 修改对象
    * @return
    */
    void update(AgServerContentGroup param);

    /**
    * @Author: libc
    * @Date: 2020-09-23
    * @tips: 服务内容分组表删除(单个条目)
    * @param id 删除的id
    * @return
    */
    void delete(String id);

    /**
     *
     * @Author: libc
     * @Date: 2020/9/23 10:21
     * @tips: 查询所有
     * @param
     * @return
     */
    List<AgServerContentGroup> findAll();

}
