package com.augurit.agcloud.agcom.agsupport.sc.io.ImageStore.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgFileStore;
import com.augurit.agcloud.agcom.agsupport.domain.AgImageStore;
import com.augurit.agcloud.agcom.agsupport.mapper.AgImageStoreMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.io.ImageStore.service.IAgImageStore;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AgImageStoreImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/23 14:49
 * @Version 1.0
 **/
@Service
public class AgImageStoreImpl implements IAgImageStore {

    @Autowired
    AgImageStoreMapper agImageStoreMapper;

    /**
     * 根据id获得image
     *
     * @param id
     * @return
     */
    @Override
    public AgImageStore getById(String id) throws RuntimeException {
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("getById is not null");
        }
        return agImageStoreMapper.getById(id);
    }

    /**
     * 获得所有image
     *
     * @return
     * @throws RuntimeException
     */
    @Override
    public List<AgImageStore> getAll() throws RuntimeException {
        return agImageStoreMapper.getAll();
    }

    /**
     * 删除一条数据
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(String id) throws Exception {
        AgImageStore agImageStore = agImageStoreMapper.getById(id);
        if (null == agImageStore)
            return false;
        else if (agImageStoreMapper.deleteById(id) > 0) {
            //动态改变对应的序号问题
            AgImageStore agImageStore1 = new AgImageStore();
            agImageStore1.setUsage(agImageStore.getUsage());
            agImageStore1.setDomain(agImageStore.getDomain());
            agImageStore1.setFullpath(agImageStore.getFullpath());
            List<AgImageStore> list = agImageStoreMapper.getByDomainAndUsage(agImageStore1);
            int index = Integer.parseInt(agImageStore.getSort());
            int newSort = 0;
            for (int i = index; i < list.size(); i++) {
                newSort = Integer.parseInt(list.get(i).getSort()) - 1;
                list.get(i).setSort(String.valueOf(newSort));
                agImageStoreMapper.update(list.get(i));
            }
            FileUtil.deleteFolder(FileUtil.getPath() + agImageStore.getPath());
            return true;
        }
        return false;
    }

    /**
     * 删除更多数据
     *
     * @param stringList
     * @return
     * @throws RuntimeException
     */
    @Override
    public boolean deleteMany(List<String> stringList) throws Exception {
        List<AgImageStore> agImageStoreList = agImageStoreMapper.getByIds(stringList);
        if (null == agImageStoreList || agImageStoreList.size() == 0)
            return false;
        else if (agImageStoreMapper.deleteMany(stringList) > 0) {

            /*批量删除代码然后更改序号（未测试）

            int index = Integer.parseInt(agImageStoreList.get(0).getSort());
            AgImageStore agImageStore = new AgImageStore();
            agImageStore.setDomain(agImageStoreList.get(0).getDomain());
            agImageStore.setUsage(agImageStoreList.get(0).getUsage());
            agImageStore.setFullpath(agImageStoreList.get(0).getFullpath());
            List<AgImageStore> list = agImageStoreMapper.getByDomainAndUsage(agImageStore);
            for (int i = index; i < list.size(); i++) {
                list.get(i).setSort(String.valueOf(i));
                agImageStoreMapper.update(list.get(i));
            }*/

            String path = FileUtil.getPath();
            for (AgImageStore agImageStore1 : agImageStoreList) {
                FileUtil.deleteFolder(path + agImageStore1.getPath());
            }
            return true;
        }
        return false;
    }

    /**
     * 添加一条信息
     *
     * @param agImageStore
     * @return
     * @throws RuntimeException
     */
    @Override
    public boolean save(AgImageStore agImageStore) throws RuntimeException {
        AgImageStore agImageStore_in = new AgImageStore();
        agImageStore_in.setDomain(agImageStore.getDomain());
        agImageStore_in.setUsage(agImageStore.getUsage());
        List<AgImageStore> imageStoreList = agImageStoreMapper.getByDomainAndUsage(agImageStore_in);
        agImageStore.setSort(String.valueOf(imageStoreList.size()));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(agImageStore.getInformation())) {
            JSONObject jsonObject = JSON.parseObject(agImageStore.getInformation());

            //默认视点为空
            if (null == jsonObject || null == jsonObject.getBoolean("IsInitialSpot")) {
                return agImageStoreMapper.save(agImageStore) > 0 ? true : false;
            }
            //不为空且设置为false
            if (!jsonObject.getBoolean("IsInitialSpot")) {
                jsonObject.put("IsInitialSpot", false);
                agImageStore.setInformation(jsonObject.toJSONString());
            }
            //不为空且设置为true
            else {
                //判断是否存在默认视点
                for (int i = 0; i < imageStoreList.size(); i++) {
                    //获取图片信息
                    String information = imageStoreList.get(i).getInformation();
                    //非空判断
                    if (org.apache.commons.lang3.StringUtils.isBlank(information)) {
                        continue;
                    }
                    //转换json对象
                    JSONObject jsonObjects = JSON.parseObject(information);
                    //存在默认视点(不允许设置)
                    if (null != jsonObjects && null != jsonObjects.getBoolean("IsInitialSpot") && jsonObjects.getBoolean("IsInitialSpot")) {
                        jsonObject.put("IsInitialSpot", false);
                        agImageStore.setInformation(jsonObject.toJSONString());
                        break;
                    }
                    //未有该区域视点(允许设置)
                    if (i == (imageStoreList.size() - 1)) {
                        jsonObject.put("IsInitialSpot", true);
                        agImageStore.setInformation(jsonObject.toJSONString());
                    }
                }
                //未有该区域视点(允许设置)
                if (null == imageStoreList || imageStoreList.size() == 0) {
                    jsonObject.put("IsInitialSpot", true);
                    agImageStore.setInformation(jsonObject.toJSONString());
                }
            }
        }
        return agImageStoreMapper.save(agImageStore) > 0 ? true : false;
    }

    /**
     * 编辑一条信息
     *
     * @param agImageStore
     * @return
     * @throws RuntimeException
     */
    @Override
    public boolean update(AgImageStore agImageStore) throws RuntimeException {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(agImageStore.getInformation())) {
            JSONObject jsonObject = JSON.parseObject(agImageStore.getInformation());
            //默认视点为空
            if (null == jsonObject || null == jsonObject.getBoolean("IsInitialSpot")) {
                return agImageStoreMapper.update(agImageStore) > 0 ? true : false;
            }
            //不为空且设置为false
            if (!jsonObject.getBoolean("IsInitialSpot")) {
                jsonObject.put("IsInitialSpot", false);
                agImageStore.setInformation(jsonObject.toJSONString());
            }
            //不为空且设置为true
            else {
                AgImageStore agImageStore_in = new AgImageStore();
                agImageStore_in.setDomain(agImageStore.getDomain());
                agImageStore_in.setUsage(agImageStore.getUsage());
                //判断是否存在默认视点
                List<AgImageStore> imageStoreList = agImageStoreMapper.getByDomainAndUsage(agImageStore_in);
                for (int i = 0; i < imageStoreList.size(); i++) {
                    //如果是当前节点
                    if (agImageStore.getId().equals(imageStoreList.get(i).getId()))
                        continue;
                    //获取图片信息
                    String information = imageStoreList.get(i).getInformation();
                    //非空判断
                    if (org.apache.commons.lang3.StringUtils.isBlank(information)) {
                        continue;
                    }
                    //转换json对象
                    JSONObject jsonObjects = JSON.parseObject(information);
                    //存在默认视点(不允许设置)
                    if (null != jsonObjects &&
                            null != jsonObjects.getBoolean("IsInitialSpot") &&
                            jsonObjects.getBoolean("IsInitialSpot")) {
                        jsonObject.put("IsInitialSpot", false);
                        agImageStore.setInformation(jsonObject.toJSONString());
                        break;
                    }
                    //未有该区域视点(允许设置)
                    if (i == (imageStoreList.size() - 1)) {
                        jsonObject.put("IsInitialSpot", true);
                        agImageStore.setInformation(jsonObject.toJSONString());
                    }
                }
                //未有该区域视点(允许设置)
                if (null == imageStoreList || imageStoreList.size() == 0) {
                    jsonObject.put("IsInitialSpot", true);
                    agImageStore.setInformation(jsonObject.toJSONString());
                }
            }
        }
        return agImageStoreMapper.update(agImageStore) > 0 ? true : false;
    }

    /**
     * 特殊条件查询列表（不分页）
     *
     * @param agImageStore
     * @return
     * @throws RuntimeException
     */
    public List<AgImageStore> getByDomainAndUsage(AgImageStore agImageStore) throws RuntimeException {
        List<AgImageStore> list = agImageStoreMapper.getByDomainAndUsage(agImageStore);
        return list;
    }

    /**
     * 特殊条件查询列表
     *
     * @param agImageStore
     * @param page
     * @return
     * @throws RuntimeException
     */
    @Override
    public PageInfo<AgImageStore> getByDomainAndUsage(AgImageStore agImageStore, Page page) throws RuntimeException {
        PageHelper.startPage(page);
        List<AgImageStore> list = agImageStoreMapper.getByDomainAndUsage(agImageStore);
        return new PageInfo<AgImageStore>(list);
    }

    /**
     * 特殊条件查询列表
     *
     * @param agImageStore
     * @param page
     * @return
     * @throws RuntimeException
     */
    @Override
    public PageInfo<AgImageStore> getByDomainOrUsage(AgImageStore agImageStore, Page page) throws RuntimeException {
        PageHelper.startPage(page);
        List<AgImageStore> list = agImageStoreMapper.getByDomainOrUsage(agImageStore);
        return new PageInfo<AgImageStore>(list);
    }

    /**
     * 设置默认视点
     *
     * @param id
     * @return
     * @throws RuntimeException
     */
    @Override
    public Map<String, Object> setDefaultViewpoints(String id) throws RuntimeException {
        AgImageStore agImageStore_in = agImageStoreMapper.getById(id);
        JSONObject jsonObject_in = JSON.parseObject(agImageStore_in.getInformation());
        Map<String, Object> result = new HashMap<>();
        //默认视点为空
        if (null == agImageStore_in || null == jsonObject_in || null == jsonObject_in.getBoolean("IsInitialSpot")) {
            result.put("success", true);
            result.put("msg", "不允许设置默认视点!");
            return result;
        }

        //已经设置为默认视点
        if (null != agImageStore_in && null != jsonObject_in && jsonObject_in.getBoolean("IsInitialSpot")) {
            result.put("success", true);
            result.put("msg", "已经设置为默认视点!");
            return result;
        }

        AgImageStore agImageStore = new AgImageStore();
        agImageStore.setDomain(agImageStore_in.getDomain());
        agImageStore.setUsage(agImageStore_in.getUsage());

        List<AgImageStore> imageStoreList = agImageStoreMapper.getByDomainAndUsage(agImageStore);
        //无其他视点
        if (null == imageStoreList || imageStoreList.size() == 0) {
            JSONObject jsonObject = JSON.parseObject(agImageStore_in.getInformation());
            jsonObject.put("IsInitialSpot", true);
            agImageStore_in.setInformation(jsonObject.toJSONString());
            //修改实体类
            AgImageStore agImageStore_ins = new AgImageStore();
            agImageStore_ins.setId(id);
            agImageStore_ins.setInformation(jsonObject.toJSONString());
            agImageStoreMapper.update(agImageStore_ins);
        } else {
            for (int i = 0; i < imageStoreList.size(); i++) {
                //获取图片信息
                String information = imageStoreList.get(i).getInformation();

                //非空判断
                if (org.apache.commons.lang3.StringUtils.isBlank(information)) {
                    continue;
                }
                //转换json对象
                JSONObject jsonObject = JSON.parseObject(information);

                //获取默认属性
                Boolean flag = jsonObject.getBoolean("IsInitialSpot");
                //为false退出本次循环
                if ((null == flag || flag == false) && !imageStoreList.get(i).getId().equals(id)) {
                    continue;
                }
                //修改该属性
                jsonObject.put("IsInitialSpot", false);
                //如果为修改的id则改为true
                if (imageStoreList.get(i).getId().equals(id)) {
                    jsonObject.put("IsInitialSpot", true);
                }
                //修改实体类
                AgImageStore agImageStore_ins = new AgImageStore();
                agImageStore_ins.setId(imageStoreList.get(i).getId());
                agImageStore_ins.setInformation(jsonObject.toJSONString());
                agImageStoreMapper.update(agImageStore_ins);
            }
        }
        result.put("success", true);
        result.put("msg", "设置默认视点成功!");
        return result;
    }
}
