package com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgBuildingComponent;
import com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.entity.TreeNode;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Version  1.0
 * @Author libc
 * @Description 建筑构件-业务接口
 * @Date 2020/9/3 14:25
 */
public interface IAgBuildingComponentService {
    /**
     * @Version  1.0
     * @Author libc
     * @Description
     * @param name 查询条件 （建筑构件名称）
     * @param page 分页对象
     * @Return 分页 建筑构件列表
     * @Date 2020/9/3 14:25
     */
    PageInfo<AgBuildingComponent> findList(String name, Page page);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 保存建筑构件信息（新增或修改）
     * @param agBuildingComponent 建筑构件信息对象
     * @Return
     * @Date 2020/9/3 14:25
     */
    void save(AgBuildingComponent agBuildingComponent);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 批量删除建筑构件信息
     * @param ids 删除的id集合
     * @Return
     * @Date 2020/9/3 14:25
     */
    void deleteByIds(String[] ids);


    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据id删除建筑构件信息
     * @param id
     * @Return
     * @Date 2020/9/3 14:25
     */
    void deleteById(String id);

    /**
     * @Version  1.0
     * @Author libc
     * @Description excel表格批量导入数据
     * @param file excel文件
     * @Return
     * @Date 2020/9/4 15:16
     */
    void excelImport(MultipartFile file);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据前端条件查询对应集合 （优化）
     * @param
     *      tableCode:表代码(两位阿拉伯数字：01)
     *      largeCode:大类代码(两位阿拉伯数字：01)
     *      mediumCode:中类代码(两位阿拉伯数字：01)
     *      smallCode:小类代码(两位阿拉伯数字：01)
     *      detailCode:细类代码(两位阿拉伯数字：01)
     *      name:类目名称（中文/英文）
     *      filterType:过滤查询类型
     * @param filterType
     * @Return
     * @Date 2020/9/4 16:26
     */
    List<AgBuildingComponent> listByParam(String tableCode, String largeCode, String mediumCode, String smallCode, String detailCode, String name, String filterType);


    /**
     *
     * @Author: libc
     * @Date: 2020/11/2 14:27
     * @tips: 查询整个构件树结构集合
     * @return 树结构的list集合 （优化版）
     * @param tableCode 表代码(两位阿拉伯数字：01)
     */
    List<TreeNode<AgBuildingComponent>> getTreeByTableCode2(String tableCode);





    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据前端条件查询对应集合
     * @param param 条件参数map
     * @Return
     * @Date 2020/9/4 16:26
     */
    /* List<AgBuildingComponent> findByParam(Map<String, String> param);*/

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据前端条件查询对应集合
     * @param
     *      tableCode:表代码(两位阿拉伯数字：01)
     *      largeCode:大类代码(两位阿拉伯数字：01)
     *      mediumCode:中类代码(两位阿拉伯数字：01)
     *      smallCode:小类代码(两位阿拉伯数字：01)
     *      detailCode:细类代码(两位阿拉伯数字：01)
     *      name:类目名称（中文/英文）
     *      filterType:过滤查询类型
     * @param filterType
     * @Return
     * @Date 2020/9/4 16:26
     */
    // 旧版
    List<AgBuildingComponent> findByParam(String tableCode, String largeCode, String mediumCode, String smallCode, String detailCode, String name, String filterType);

    /**
     *
     * @Author: libc
     * @Date: 2020/11/2 14:27
     * @tips: 查询整个构件树结构集合
     * @return 树结构的list集合
     * @param tableCode 表代码(两位阿拉伯数字：01)
     */
    /*List<DirTreeForOpenMap> getTreeByTableCode(String tableCode);*/

}
