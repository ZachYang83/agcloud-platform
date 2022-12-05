package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service.impl;


import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityXCustom;
import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dbuilding;
import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dproject;
import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dprojectExample;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.Agcim3dprojectMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.domain.Agcim3dbuildingCustom;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.domain.Agcim3dprojectCustom;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service.IAgcim3dbuildingService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service.IAgcim3dentityXService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service.IAgcim3dprojectService;
import com.augurit.agcloud.agcom.agsupport.util.BeanHelper;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@Service
public class Agcim3dentityprojectServiceImpl implements IAgcim3dprojectService {

    private Logger logger = LoggerFactory.getLogger(Agcim3dentityprojectServiceImpl.class);

    @Autowired
    private Agcim3dprojectMapper agcim3dprojectMapper;
    /*
    // 没有在用，注释
    @Autowired
    private Agcim3dCustomMapper agcim3dCustomMapper;*/
    @Autowired
    private IAgcim3dbuildingService agcim3dbuildingService;
    @Autowired
    private IAgcim3dentityXService agcim3dentityXService;


    @Override
    public List<Agcim3dproject> list(Agcim3dproject project) {
        Agcim3dprojectExample example = new Agcim3dprojectExample();
        getAgcim3dentityExample(example, project);
        return agcim3dprojectMapper.selectByExample(example);
    }

    @Override
    public PageInfo<Agcim3dproject> list(Agcim3dproject project, Page page) {
        Agcim3dprojectExample example = new Agcim3dprojectExample();
        getAgcim3dentityExample(example, project);
        PageHelper.startPage(page);
        List<Agcim3dproject> list = agcim3dprojectMapper.selectByExample(example);
        return new PageInfo<>(list);
    }

    /*
    // 没有在用，注释
    @Override
    public Object countCatagory() {
        StringBuffer sb = new StringBuffer();
        sb.append("select catagory as name, count(catagory) as value  from agcim3dproject_a group by catagory");
        List<Object> list = agcim3dCustomMapper.findAllDefineSql(sb.toString());
        return list;
    }*/

    /**
     * @return List<Agcim3dprojectCustom>
     * @Author: libc
     * @Date: 2020/12/10 16:38
     * @tips: 获取BIM项目树结构列表
     */
//    @Cacheable(value = "tree_获取BIM项目树结构列表") 序列化有问题，前端获取的数据都是null ，暂不开启缓存
    @Override
    public List<Agcim3dprojectCustom> tree() {
        // 获取所有项目集合
        List<Agcim3dproject> projectList = list(new Agcim3dproject());
        if (CollectionUtils.isEmpty(projectList)) {
            logger.error("未找到相关项目数据！");
            throw new AgCloudException(500, "未找到相关项目数据！");
        }

        try {
            // 数据转换为自定义项目对象集合
            List<Agcim3dprojectCustom> projectCustomList = BeanHelper.copyWithCollection(projectList, Agcim3dprojectCustom.class);

            // 获取所有模型集合
            List<Agcim3dbuilding> buildingList = agcim3dbuildingService.listBuildings(new Agcim3dbuilding());
            if (CollectionUtils.isEmpty(buildingList)) {
                // 如果模型集合为空，直接返回项目集合
                return projectCustomList;
            }

            // 数据转换为自定义模型对象集合
            List<Agcim3dbuildingCustom> buildingCustomList = BeanHelper.copyWithCollection(buildingList, Agcim3dbuildingCustom.class);
            // 遍历构建自定义添加属性
            buildingCustomList.forEach(bc -> {
                // 根据建筑实体表名以及 infotype=LevelInfo（楼层类型数据）获取对应建筑的楼层信息
                // 先获取建筑实体表名
                String entitytable = bc.getEntitytable();
                Agcim3dentityXCustom entityQuery = new Agcim3dentityXCustom();
                // 楼层类型数据
                entityQuery.setInfotype("LevelInfo");
                List<Agcim3dentityXCustom> dentityXCustoms = agcim3dentityXService.list(entityQuery, entitytable);
                dentityXCustoms.forEach(entity -> {
                    // 建筑实体对象作为第三级树结构 ，默认楼层的level字段是空，所以不影响原数据。
                    entity.setLevel("3");
                });
                bc.setChildren(dentityXCustoms);

                // 模型对象作为第一级树结构
                bc.setLevel("2");
                bc.setName(bc.getBuildingname());
            });

            // 根据项目id过滤模型集合， 将过滤的集合放入对应的项目 children中
            projectCustomList.forEach(project -> {
                String projectId = project.getId();
                // 根据项目id过滤
                List<Agcim3dbuildingCustom> dbuildingCustoms = buildingCustomList.stream().filter(t -> project.getId().equals(t.getProjectid())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(dbuildingCustoms)) {
                    // 如果为null 则给一个空集合，避免前端null报错
                    dbuildingCustoms = new ArrayList<>();
                }
                // 项目对象作为第一级树结构
                project.setLevel("1");
                project.setChildren(dbuildingCustoms);
            });

            return projectCustomList;

        } catch (AgCloudException agCloudException) {
            throw new AgCloudException(agCloudException.getStatus(), agCloudException.getMessage());
        } catch (Exception e) {
            logger.error("查询BIM项目异常！", e);
            throw new AgCloudException(500, "查询BIM项目异常！");
        }
    }

    private void getAgcim3dentityExample(Agcim3dprojectExample example, Agcim3dproject agcim) {
        Agcim3dprojectExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(agcim.getId())) {
            criteria.andIdEqualTo(agcim.getId());
        }
        if (!StringUtils.isEmpty(agcim.getName())) {
            criteria.andNameEqualTo(agcim.getName());
        }
        if (!StringUtils.isEmpty(agcim.getCreattime())) {
            criteria.andCreattimeEqualTo(agcim.getCreattime());
        }
    }
}
