package com.augurit.agcloud.agcom.agsupport.sc.serverContent.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContent;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 服务内容管理 服务类
 * </p>
 *
 * @author libc
 * @since 2020-09-15
 */
public interface IAgServerContentService {


    /**
     * @param param 查询条件
     * @param page  分页参数
     * @return
     * @Author: libc
     * @Date: 2020-09-15
     * @tips: 分页列表
     */
    PageInfo<AgServerContent> find(AgServerContent param, Page page);

    /**
     * @param id id
     * @return
     * @Author: libc
     * @Date: 2020-09-15
     * @tips: 详情
     */
    AgServerContent get(String id);

    /**
     * @param param     保存对象
     * @param file      上传zip文件
     * @param request
     * @param paramType 文件来源类型。1：agcim服务管理； 2：BIM审查
     * @param sourceRelId 文件来源关联的业务ID （BIM审查关联BIM审查项目id）
     * @return
     * @Author: libc
     * @Date: 2020-09-15
     * @tips: 新增
     */
    void add(AgServerContent param, MultipartFile file, HttpServletRequest request, String paramType, String sourceRelId);

    /**
     * @param param 修改对象
     * @return
     * @Author: libc
     * @Date: 2020-09-15
     * @tips: 修改
     */
    void update(AgServerContent param);

    /**
     * @param id 删除的id
     * @return
     * @Author: libc
     * @Date: 2020-09-15
     * @tips: 删除(单个条目)
     */
    void delete(String id);

    /**
     * @param id 记录id
     * @Author: libc
     * @Date: 2020/10/19 10:36
     * @tips: 下载3dtiles zip包
     */
    void download(String id, HttpServletRequest request, HttpServletResponse response);



    /**
     * @param sourceRelId 文件来源业务关联id
     * @return
     * @Author: libc
     * @Date: 2020/11/3 16:56
     * @tips: 根据sourceRelId 删除相关联的模型集合
     */
    void deleteListBySourceRelId(String sourceRelId);

    /**
     * @param sourceRelId 文件来源业务关联id
     * @return
     * @Author: libc
     * @Date: 2020/11/3 17:13
     * @tips: 根据sourceRelId 查询模型集合
     */
    List<AgServerContent> findListBySourceRelId(String sourceRelId);

    /**
     *
     * @Author: libc
     * @Date: 2020/11/19 16:53
     * @tips: 新增服务内容（不包含模型文件）
     * @param serverContent
     */
    void addWithoutFile(AgServerContent serverContent);
}
