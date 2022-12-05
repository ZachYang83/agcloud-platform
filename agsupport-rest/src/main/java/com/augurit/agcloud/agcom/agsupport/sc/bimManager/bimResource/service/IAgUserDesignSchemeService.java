package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserDesignMaterials;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserDesignScheme;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
public interface IAgUserDesignSchemeService {

    /**
     * 查询列表
     * @param scheme
     * @param page
     * @return
     */
    PageInfo<AgUserDesignScheme> list(AgUserDesignScheme scheme, Page page);

    /**
     * 添加
     * @param scheme
     * @param thumbFile
     * @param materials
     */
    void add(AgUserDesignScheme scheme, MultipartFile thumbFile, List<AgUserDesignMaterials> materials);

    /**
     * 修改
     * @param scheme
     * @param thumbFile
     * @param materials
     */
    void update(AgUserDesignScheme scheme, MultipartFile thumbFile, List<AgUserDesignMaterials> materials);

    /**
     * 查询详情
     * @param id
     * @return
     */
    AgUserDesignScheme get(String id);

    /**
     * 预览方案背景图
     * @param path
     */
    void view(String path, HttpServletResponse response);

    /**
     * 设置默认
     * @param id
     */
    void setDefault(String id);

    /**
     * 删除
     * @param id
     */
    void delete(String id);

    /**
     * 获取小品/房屋模型
     * @param schemeId 用户设计方案id
     *   1房屋模型；2小品模型
     * @return
     */
    List<AgUserDesignMaterials> getModel(String schemeId, String materialsId);
}
