package com.augurit.agcloud.agcom.agsupport.sc.serverEffect.service;


import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerEffectGroup;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* <p>
* 服务管理-效果分组表 服务类
* </p>
*
* @author libc
* @since 2020-09-30
*/
public interface IAgServerEffectGroupService {

    /**
    * @Author: libc
    * @Date: 2020-09-30
    * @tips: 服务管理-效果分组表分页列表
    * @param param 查询条件
    * @param page 分页参数
    * @return
    */
    PageInfo<AgServerEffectGroup> find(AgServerEffectGroup param, Page page);

    /**
    * @Author: libc
    * @Date: 2020-09-30
    * @tips: 服务管理-效果分组表详情
    * @param id id
    * @return
    */
    AgServerEffectGroup get(String id);

    /**
    * @Author: libc
    * @Date: 2020-09-30
    * @tips: 服务管理-效果分组表新增
    * @param param 保存对象
    * @return
    */
    void add(AgServerEffectGroup param);

    /**
    * @Author: libc
    * @Date: 2020-09-30
    * @tips: 服务管理-效果分组表修改
    * @param param 修改对象
    * @return
    */
    void update(AgServerEffectGroup param);

    /**
    * @Author: libc
    * @Date: 2020-09-30
    * @tips: 服务管理-效果分组表删除(单个条目)
    * @param id 删除的id
    * @return
    */
    void delete(String id);

    /**
     *
     * @Author: libc
     * @Date: 2020-09-30
     * @tips: 查询所有
     * @param
     * @return
     */
    List<AgServerEffectGroup> findAll();
}
