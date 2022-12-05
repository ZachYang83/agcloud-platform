package com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.common.exception.ExceptionEnum;
import com.augurit.agcloud.agcom.agsupport.domain.AgBuildingComponent;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.domain.DirTreeForOpenMap;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBuildingComponentMapper;
import com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.entity.TreeNode;
import com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.service.IAgBuildingComponentService;
import com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.util.TreeUtils;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.util.ExcelUtil;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Version 1.0
 * @Author libc
 * @Description 建筑构件-业务实现类
 * @Date 2020/9/3 14:31
 */
@Service
@Transactional
public class AgBuildingComponentServiceImpl implements IAgBuildingComponentService {

    private Logger logger = LoggerFactory.getLogger(AgBuildingComponentServiceImpl.class);

    // excel 文件格式
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";
    // excel数据格式模板 模板字符串，模板定义：excel列号| 备注(属性名)| 属性名
    private static final String MODEL_TETX = "0|类目编码|code,1|类目中文|chineseName,2|类目英文|englishName";

    @Autowired
    private AgBuildingComponentMapper buildingComponentMapper;

    @Autowired
    private IAgDic agDic; // 数字字典service

    /**
     * @param name 查询条件 （建筑构件名称）
     * @param page 分页对象
     * @Version 1.0
     * @Author libc
     * @Description
     * @Return 分页 建筑构件列表
     * @Date 2020/9/3 14:31
     */
    public PageInfo<AgBuildingComponent> findList(String name, Page page) {
        PageHelper.startPage(page);
        List<AgBuildingComponent> list = buildingComponentMapper.findList(name);
        return new PageInfo<AgBuildingComponent>(list);
    }

    /**
     * @param agBuildingComponent 建筑构件信息对象
     * @Version 1.0
     * @Author libc
     * @Description 保存建筑构件信息（新增或修改）
     * @Return
     * @Date 2020/9/3 14:31
     */
    public void save(AgBuildingComponent agBuildingComponent) {
        // 拆解类目编码
        disassembleCode(agBuildingComponent);

        if (StringUtils.isNotEmpty(agBuildingComponent.getId())) {
            // 修改时间
            agBuildingComponent.setModifyTime(new Timestamp(new Date().getTime()));
            buildingComponentMapper.updateByPrimaryKeySelective(agBuildingComponent);
        } else {
            // 创建时间
            agBuildingComponent.setCreateTime(new Timestamp(new Date().getTime()));
            agBuildingComponent.setId(UUID.randomUUID().toString());
            buildingComponentMapper.insertSelective(agBuildingComponent);
        }
    }

    /**
     * @param buildingComponent
     * @Version 1.0
     * @Author libc
     * @Description 拆解类目编码
     * @Return
     * @Date 2020/9/4 10:04
     */
    private void disassembleCode(AgBuildingComponent buildingComponent) {
        if (buildingComponent == null) {
            return;
        }
        String code = buildingComponent.getCode();
        if (StringUtils.isBlank(code)) {
            return;
        }
        // 编码：30-01.10.40.20
        // 30
        String tableCode = code.substring(0, code.indexOf("-"));
        // 赋值tableCode
        if (StringUtils.isNotBlank(tableCode)) {
            buildingComponent.setTableCode(tableCode);
        }
        // 01.10.40.20
        String extCode = code.substring(code.indexOf("-") + 1);
        // [01,10,40,20]
        String[] extCodeArr = extCode.split("\\.");
        if (extCodeArr == null || extCodeArr.length == 0) {
            return;
        }
        // 循环extCodeArr数组，赋值对应的级别代码
        for (int i = 0; i < extCodeArr.length; i++) {
            switch (i) {
                case 0:
                    buildingComponent.setLargeCode(StringUtils.isNotBlank(extCodeArr[0]) ? extCodeArr[0] : "");
                    break;
                case 1:
                    buildingComponent.setMediumCode(StringUtils.isNotBlank(extCodeArr[1]) ? extCodeArr[1] : "");
                    break;
                case 2:
                    buildingComponent.setSmallCode(StringUtils.isNotBlank(extCodeArr[2]) ? extCodeArr[2] : "");
                    break;
                case 3:
                    buildingComponent.setDetailCode(StringUtils.isNotBlank(extCodeArr[3]) ? extCodeArr[3] : "");
                    break;
                default:
            }
        }
    }


    /**
     * @param ids 删除的id集合
     * @Version 1.0
     * @Author libc
     * @Description 批量删除建筑构件信息  (逻辑删除)
     * @Return
     * @Date 2020/9/3 14:31
     */
    public void deleteByIds(String[] ids) {
        buildingComponentMapper.deleteByIds(ids);
    }


    /**
     * @param id
     * @Version 1.0
     * @Author libc
     * @Description 根据id删除建筑构件信息 (逻辑删除)
     * @Return
     * @Date 2020/9/3 14:31
     */
    public void deleteById(String id) {
        buildingComponentMapper.deleteByPrimaryKey(id);
    }

    /**
     * @Version  1.0
     * @Author libc
     * @Description excel表格批量导入数据
     * @param file excel文件
     * @Return
     * @Date 2020/9/4 15:16
     */
    public void excelImport(MultipartFile file) {
        try {
            if (file == null) {
                throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
            }
            String fileName = file.getOriginalFilename();
            if (!fileName.endsWith(EXCEL_XLSX) || fileName.endsWith(EXCEL_XLS)) {
                throw new AgCloudException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            // excel 转换建筑构件对象集合
            List<AgBuildingComponent> bcList = ExcelUtil.readExcel(file, AgBuildingComponent.class, MODEL_TETX, "type",1,1);
            // 查询构件编码名称对应的编码值
            List<AgDic> cCodeList = agDic.getAgDicByTypeCode("COMPONENT_CODE");

            // 遍历集合
            bcList.forEach(bc -> {
                // 新增，赋值uuid
                bc.setId(UUID.randomUUID().toString());
                // 创建时间
                bc.setCreateTime(new Timestamp(new Date().getTime()));
                // 拆解类目编码
                disassembleCode(bc);
                // 构件编码类型转换
                cCodeList.forEach(cc -> {
                    if (cc.getName().equals(bc.getType())){
                        bc.setType(cc.getValue());
                    }
                });
            });
            System.out.println(bcList);
            // 批量保存构件对象
            buildingComponentMapper.insertList(bcList);
        }catch (Exception e){
            throw new AgCloudException(ExceptionEnum.EXCEL_IMPORT_ERROR);
        }
    }


    /**
     * @param tableCode:表代码(两位阿拉伯数字：01) largeCode:大类代码(两位阿拉伯数字：01)
     *                                  mediumCode:中类代码(两位阿拉伯数字：01)
     *                                  smallCode:小类代码(两位阿拉伯数字：01)
     *                                  detailCode:细类代码(两位阿拉伯数字：01)
     *                                  name:类目名称（中文/英文）
     *                                  filterType:过滤查询类型
     * @param filterType
     * @Version 1.0
     * @Author libc
     * @Description 根据前端条件查询对应集合（优化）
     * @Return
     * @Date 2020/9/4 16:26
     */
    @Override
    public List<AgBuildingComponent> listByParam(String tableCode, String largeCode, String mediumCode, String smallCode, String detailCode, String name, String filterType) {
        List<AgBuildingComponent> result = new ArrayList<>();
        // 查询所有
        List<AgBuildingComponent> allBuildComponents = buildingComponentMapper.findList(name);
        // 根据过滤参数 筛选集合数据
        switch (filterType) {
            case "1":
                // 查询大类，将大类代码赋值null,过滤本类具体值，才能查询出本类列表集合
                result = allBuildComponents.stream().filter(t -> StringUtils.equals(tableCode, t.getTableCode()) && !StringUtils.equals("00", t.getLargeCode()) && StringUtils.equals("00", t.getMediumCode()) && StringUtils.equals("00", t.getSmallCode())).collect(Collectors.toList());
                break;
            case "2":
                // 查询中类，将中类代码赋值null,过滤本类具体值，才能查询出本类列表集合
                result = allBuildComponents.stream().filter(t -> StringUtils.equals(tableCode, t.getTableCode()) && StringUtils.equals(largeCode, t.getLargeCode()) && !StringUtils.equals("00", t.getMediumCode()) && StringUtils.equals("00", t.getSmallCode())).collect(Collectors.toList());
                break;
            case "3":
                // 查询小类，将小类代码null,过滤本类具体值，才能查询出本类列表集合
                result = allBuildComponents.stream().filter(t -> StringUtils.equals(tableCode, t.getTableCode()) && StringUtils.equals(largeCode, t.getLargeCode()) && StringUtils.equals(mediumCode, t.getMediumCode()) && !StringUtils.equals("00", t.getSmallCode()) && t.getDetailCode() == null).collect(Collectors.toList());
                break;
            case "4":
                // 查询细类，将细类代码null,过滤本类具体值，才能查询出本类列表集合
                result = allBuildComponents.stream().filter(t -> StringUtils.equals(tableCode, t.getTableCode()) && StringUtils.equals(largeCode, t.getLargeCode()) && StringUtils.equals(mediumCode, t.getMediumCode()) && StringUtils.equals(smallCode, t.getSmallCode()) && t.getDetailCode() != null).collect(Collectors.toList());
                break;
            default:
        }

        return result;
    }


    /**
     * @param tableCode 表代码
     * @return 树结构的list集合
     * @Author: libc
     * @Date: 2020/11/2 14:27
     * @tips: 查询整个构件树结构集合2 （优化树结构）
     */
    @Cacheable(value = "componentTreeCache2")
    public List<TreeNode<AgBuildingComponent>> getTreeByTableCode2(String tableCode) {
        List<TreeNode<AgBuildingComponent>> result = new ArrayList<>();
        try {
            // 查询所有的构件分类总集合
            List<AgBuildingComponent> allBuildComponents = buildingComponentMapper.findList(null);
            if (StringUtils.isNotBlank(tableCode)) {
                // 封装树不包括表代码层
                // 按照表代码过滤总集合
                List<AgBuildingComponent> largeComponents = allBuildComponents.stream().filter(t -> StringUtils.equals(tableCode, t.getTableCode())).collect(Collectors.toList());
                // 获取树结构
                result = TreeUtils.getTreeList(largeComponents, AgBuildingComponent::getCode, AgBuildingComponent::getChineseName, AgBuildingComponent::getParentCode);
            } else {
                // 封装树包括表代码层
                // 查询所有表代码集合
                List<AgDic> tableCodes = agDic.getAgDicByTypeCode("BUILDING_MODEL_TYPE");
                if (CollectionUtils.isEmpty(tableCodes)) {
                    logger.error("找不到对应的表代码数据字典");
                    throw new AgCloudException(500, "找不到对应的表代码数据字典");
                }
                // 添加表代码集合到总集合中
                tableCodes.forEach(tableObj -> {
                    AgBuildingComponent bc = new AgBuildingComponent();
                    bc.setCode(tableObj.getCode());
                    bc.setChineseName(tableObj.getName());
                    allBuildComponents.add(bc);
                });
                // 获取树结构
                result = TreeUtils.getTreeList(allBuildComponents, i -> new TreeNode<>(i, i.getCode(), i.getChineseName(), i.getParentCode()));
            }
        } catch (Exception e) {
            logger.error(ExceptionEnum.DATA_TRANSFER_ERROR.getMessage(), e);
            throw new AgCloudException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }
        return result;
    }


    /**
     * @Version 1.0
     * @Author libc
     * @Description 根据前端条件查询对应集合
     * @param param 条件参数map
     * @Return
     * @Date 2020/9/4 16:26
     */
   /*public List<AgBuildingComponent> findByParam(Map<String, String> param) {

        return buildingComponentMapper.findByParam(param);
    }*/

    /**
     * @param tableCode:表代码(两位阿拉伯数字：01) largeCode:大类代码(两位阿拉伯数字：01)
     *                                  mediumCode:中类代码(两位阿拉伯数字：01)
     *                                  smallCode:小类代码(两位阿拉伯数字：01)
     *                                  detailCode:细类代码(两位阿拉伯数字：01)
     *                                  name:类目名称（中文/英文）
     *                                  filterType:过滤查询类型
     * @param filterType
     * @Version 1.0
     * @Author libc
     * @Description 根据前端条件查询对应集合 （旧版 不使用）
     *              推荐使用listByParam方法代替
     * @Return
     * @Date 2020/9/4 16:26
     */
    @Override
    @Deprecated
    public List<AgBuildingComponent> findByParam(String tableCode, String largeCode, String mediumCode, String smallCode, String detailCode, String name, String filterType) {
        List<AgBuildingComponent> resultList = new ArrayList<AgBuildingComponent>();
        Iterator<AgBuildingComponent> iterator = null;
        switch (filterType) {
            case "1":
                // 查询大类，将大类代码赋值null,过滤本类具体值，才能查询出本类列表集合
                largeCode = null;
                // 查询大类，将中类代码赋值"00"
                mediumCode = "00";
                // 查询大类，将小类代码赋值"00"
                smallCode = "00";
                // 查询集合
                resultList = buildingComponentMapper.findByParam(tableCode, largeCode, mediumCode, smallCode, detailCode, name);
                // 过滤大类为“00” ，避免跟表代码分类重复
                iterator = resultList.iterator();
                while (iterator.hasNext()) {
                    AgBuildingComponent bc = iterator.next();
                    if ("00".equals(bc.getLargeCode())) {
                        // 移除大类为00 数据
                        iterator.remove();
                    }
                }
                break;
            case "2":
                // 查询中类，将中类代码赋值null,过滤本类具体值，才能查询出本类列表集合
                mediumCode = null;
                // 查询中类，将小类代码赋值"00"
                smallCode = "00";
                // 查询集合
                resultList = buildingComponentMapper.findByParam(tableCode, largeCode, mediumCode, smallCode, detailCode, name);
                // 过滤中类为“00” ，避免跟大类代码分类重复
                iterator = resultList.iterator();
                while (iterator.hasNext()) {
                    AgBuildingComponent bc = iterator.next();
                    if ("00".equals(bc.getMediumCode())) {
                        // 移除中类为00 数据
                        iterator.remove();
                    }
                }
                break;
            case "3":
                // 查询小类，将小类代码null,过滤本类具体值，才能查询出本类列表集合
                smallCode = null;
                // 查询小类，将细类代码null
                detailCode = null;
                // 查询集合
                resultList = buildingComponentMapper.findByParam(tableCode, largeCode, mediumCode, smallCode, detailCode, name);
                // 过滤小类为“00” ，避免跟中类代码分类重复
                iterator = resultList.iterator();
                while (iterator.hasNext()) {
                    AgBuildingComponent bc = iterator.next();
                    // 细类有数据的也过滤
                    if ("00".equals(bc.getSmallCode()) || bc.getDetailCode() != null) {
                        // 移除小类为00 数据
                        iterator.remove();
                    }
                }
                break;
            case "4":
                // 查询细类，将细类代码null,过滤本类具体值，才能查询出本类列表集合
                detailCode = null;
                // 查询集合
                resultList = buildingComponentMapper.findByParam(tableCode, largeCode, mediumCode, smallCode, detailCode, name);
                //过滤detailCode=null 记录
                iterator = resultList.iterator();
                while (iterator.hasNext()) {
                    AgBuildingComponent bc = iterator.next();
                    // 细类有数据的也过滤
                    if (bc.getDetailCode() == null) {
                        // 移除小类为00 数据
                        iterator.remove();
                    }
                }
                break;
            default:
        }
        return resultList;
    }


    /**
     * @param tableCode
     * @return 树结构的list集合
     * @Author: libc
     * @Date: 2020/11/2 14:27
     * @tips: 查询整个构件树结构集合 (旧版不使用)
     */
//    @Cacheable(value = "componentTreeCache")
 /*   public List<DirTreeForOpenMap> getTreeByTableCode(String tableCode) {
        ArrayList<DirTreeForOpenMap> result = new ArrayList<>();
        try {
            // 查询所有表代码集合
            List<AgDic> tableCodes = agDic.getAgDicByTypeCode("BUILDING_MODEL_TYPE");

            if (CollectionUtils.isEmpty(tableCodes)) {
                throw new AgCloudException(500, "找不到对应的表代码数据字典");
            }

            // 是否需要返回表代码层
            if (StringUtils.isNotBlank(tableCode)) {
                // 不需要表代码层
                // 获取所有大类代码集合
                List<AgBuildingComponent> largeCodes = findByParam(tableCode, null, null, null, null, null, "1");
                if (CollectionUtils.isEmpty(largeCodes)) {
                    throw new AgCloudException(500, "未找到相关数据");
                }
                // 有则遍历大类代码集合
                // 封装树结构第一层， 大类代码集合
                for (AgBuildingComponent largeCode : largeCodes) {
                    DirTreeForOpenMap largeNode = new DirTreeForOpenMap();
                    largeNode.setId(largeCode.getCode());
                    largeNode.setName(largeCode.getChineseName());
                    // 获取所有中类代码集合
                    List<AgBuildingComponent> mediumCodes = findByParam(tableCode, largeCode.getLargeCode(), null, null, null, null, "2");
                    if (CollectionUtils.isEmpty(mediumCodes)) {
                        // 无中类代码子节点
                        largeNode.setChildren(new ArrayList<>());
                    } else {
                        // 有则遍历中类代码集合
                        // 封装树结构第二层， 中类代码集合
                        largeNode.setChildrenCount(mediumCodes.size());
                        ArrayList<DirTreeForOpenMap> mediumNodes = new ArrayList<>();
                        for (AgBuildingComponent mediumCode : mediumCodes) {
                            DirTreeForOpenMap mediumNode = new DirTreeForOpenMap();
                            mediumNode.setId(mediumCode.getCode());
                            mediumNode.setName(mediumCode.getChineseName());
                            // 获取所有小类代码集合
                            List<AgBuildingComponent> smallCodes = findByParam(tableCode, largeCode.getLargeCode(), mediumCode.getMediumCode(), null, null, null, "3");
                            if (CollectionUtils.isEmpty(smallCodes)) {
                                // 无小类代码子节点
                                mediumNode.setChildren(new ArrayList<>());
                                mediumNodes.add(mediumNode);
                            } else {
                                // 有则遍历小类代码集合
                                // 封装树结构第三层， 小类代码集合
                                mediumNode.setChildrenCount(smallCodes.size());
                                ArrayList<DirTreeForOpenMap> smallNodes = new ArrayList<>();
                                for (AgBuildingComponent smallCode : smallCodes) {
                                    DirTreeForOpenMap smallNode = new DirTreeForOpenMap();
                                    smallNode.setId(smallCode.getCode());
                                    smallNode.setName(smallCode.getChineseName());
                                    // 获取所有小类代码集合
                                    List<AgBuildingComponent> detailCodes = findByParam(tableCode, largeCode.getLargeCode(), mediumCode.getMediumCode(), smallCode.getSmallCode(), null, null, "4");
                                    if (CollectionUtils.isEmpty(detailCodes)) {
                                        // 无细类代码子节点
                                        smallNode.setChildren(new ArrayList<>());
                                        smallNodes.add(smallNode);
                                    } else {
                                        // 有则遍历细类代码集合
                                        // 封装树结构第四层， 细类代码集合
                                        smallNode.setChildrenCount(detailCodes.size());
                                        ArrayList<DirTreeForOpenMap> detailNodes = new ArrayList<>();
                                        for (AgBuildingComponent detailCode : detailCodes) {
                                            DirTreeForOpenMap detailNode = new DirTreeForOpenMap();
                                            detailNode.setId(detailCode.getCode());
                                            detailNode.setName(detailCode.getChineseName());
                                            detailNodes.add(detailNode);
                                        }
                                        smallNode.setChildren(detailNodes);
                                        smallNodes.add(smallNode);
                                    }
                                }
                                mediumNode.setChildren(smallNodes);
                                mediumNodes.add(mediumNode);
                            }
                        }
                        largeNode.setChildren(mediumNodes);
                    }
                    result.add(largeNode);
                }
            } else {
                // 需要表代码层
                // 封装树结构第一层，表代码集合
                for (AgDic tableDic : tableCodes) {
                    DirTreeForOpenMap tableNode = new DirTreeForOpenMap();
                    tableNode.setId(tableDic.getCode());
                    tableNode.setName(tableDic.getName());
                    tableNode.setOrder(tableDic.getOrderNm());
                    // 获取所有大类代码集合
                    List<AgBuildingComponent> largeCodes = findByParam(tableDic.getCode(), null, null, null, null, null, "1");
                    if (CollectionUtils.isEmpty(largeCodes)) {
                        // 无大类代码子节点
                        tableNode.setChildren(new ArrayList<>());
                        result.add(tableNode);
                    } else {
                        // 有则遍历大类代码集合
                        // 封装树结构第二层， 大类代码集合
                        tableNode.setChildrenCount(largeCodes.size());
                        ArrayList<DirTreeForOpenMap> largeNodes = new ArrayList<>();
                        for (AgBuildingComponent largeCode : largeCodes) {
                            DirTreeForOpenMap largeNode = new DirTreeForOpenMap();
                            largeNode.setId(largeCode.getCode());
                            largeNode.setName(largeCode.getChineseName());
                            // 获取所有中类代码集合
                            List<AgBuildingComponent> mediumCodes = findByParam(tableDic.getCode(), largeCode.getLargeCode(), null, null, null, null, "2");
                            if (CollectionUtils.isEmpty(mediumCodes)) {
                                // 无中类代码子节点
                                largeNode.setChildren(new ArrayList<>());
                                largeNodes.add(tableNode);
                            } else {
                                // 有则遍历中类代码集合
                                // 封装树结构第三层， 中类代码集合
                                largeNode.setChildrenCount(mediumCodes.size());
                                ArrayList<DirTreeForOpenMap> mediumNodes = new ArrayList<>();
                                for (AgBuildingComponent mediumCode : mediumCodes) {
                                    DirTreeForOpenMap mediumNode = new DirTreeForOpenMap();
                                    mediumNode.setId(mediumCode.getCode());
                                    mediumNode.setName(mediumCode.getChineseName());
                                    // 获取所有小类代码集合
                                    List<AgBuildingComponent> smallCodes = findByParam(tableDic.getCode(), largeCode.getLargeCode(), mediumCode.getMediumCode(), null, null, null, "3");
                                    if (CollectionUtils.isEmpty(smallCodes)) {
                                        // 无小类代码子节点
                                        mediumNode.setChildren(new ArrayList<>());
                                        mediumNodes.add(mediumNode);
                                    } else {
                                        // 有则遍历小类代码集合
                                        // 封装树结构第四层， 小类代码集合
                                        mediumNode.setChildrenCount(smallCodes.size());
                                        ArrayList<DirTreeForOpenMap> smallNodes = new ArrayList<>();
                                        for (AgBuildingComponent smallCode : smallCodes) {
                                            DirTreeForOpenMap smallNode = new DirTreeForOpenMap();
                                            smallNode.setId(smallCode.getCode());
                                            smallNode.setName(smallCode.getChineseName());
                                            // 获取所有小类代码集合
                                            List<AgBuildingComponent> detailCodes = findByParam(tableDic.getCode(), largeCode.getLargeCode(), mediumCode.getMediumCode(), smallCode.getSmallCode(), null, null, "4");
                                            if (CollectionUtils.isEmpty(detailCodes)) {
                                                // 无细类代码子节点
                                                smallNode.setChildren(new ArrayList<>());
                                                smallNodes.add(smallNode);
                                            } else {
                                                // 有则遍历细类代码集合
                                                // 封装树结构第五层， 细类代码集合
                                                smallNode.setChildrenCount(detailCodes.size());
                                                ArrayList<DirTreeForOpenMap> detailNodes = new ArrayList<>();
                                                for (AgBuildingComponent detailCode : detailCodes) {
                                                    DirTreeForOpenMap detailNode = new DirTreeForOpenMap();
                                                    detailNode.setId(detailCode.getCode());
                                                    detailNode.setName(detailCode.getChineseName());
                                                    detailNodes.add(detailNode);
                                                }
                                                smallNode.setChildren(detailNodes);
                                                smallNodes.add(smallNode);
                                            }
                                        }
                                        mediumNode.setChildren(smallNodes);
                                        mediumNodes.add(mediumNode);
                                    }
                                }
                                largeNode.setChildren(mediumNodes);
                                largeNodes.add(largeNode);
                            }
                        }
                        tableNode.setChildren(largeNodes);
                        result.add(tableNode);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new AgCloudException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }
        return result;
    }*/

}
