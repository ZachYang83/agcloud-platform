package com.augurit.agcloud.agcom.agsupport.sc.io.FileStore.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgFileStore;
import com.augurit.agcloud.agcom.agsupport.mapper.AgFileStoreMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.io.FileStore.service.IAgFileStore;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * @ClassName AgFileStoreImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/23 14:49
 * @Version 1.0
 **/
@Service
public class AgFileStoreImpl implements IAgFileStore {

    @Autowired
    AgFileStoreMapper agFileStoreMapper;

    /**
     * 根据id获得image
     *
     * @param id
     * @return
     */
    @Override
    public AgFileStore getById(String id) throws RuntimeException {
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("getById is not null");
        }
        return agFileStoreMapper.getById(id);
    }

    /**
     * 获得所有image
     *
     * @return
     * @throws RuntimeException
     */
    @Override
    public List<AgFileStore> getAll() throws RuntimeException {
        return agFileStoreMapper.getAll();
    }

    /**
     * 删除一条数据
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(String id) throws Exception {
        AgFileStore agFileStore = agFileStoreMapper.getById(id);
        if (null == agFileStore)
            return false;
        if (agFileStoreMapper.deleteById(id) > 0) {
            FileUtil.deleteFolder(FileUtil.getPath() + agFileStore.getPath());
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
        List<AgFileStore> agFileStoreList = agFileStoreMapper.getByIds(stringList);
        if(null == agFileStoreList || agFileStoreList.size() == 0)
            return false;
        if(agFileStoreMapper.deleteMany(stringList) > 0){
            String path = FileUtil.getPath();
            for (AgFileStore agFileStore : agFileStoreList){
                FileUtil.deleteFolder(path + agFileStore.getPath());
            }
            return true;
        }
        return false;
    }

    /**
     * 添加一条信息
     *
     * @param agFileStore
     * @return
     * @throws RuntimeException
     */
    @Override
    public boolean save(AgFileStore agFileStore) throws Exception {
        if (StringUtils.isNotBlank(agFileStore.getExtension()) && agFileStore.getExtension().toLowerCase().equals("gltf")) {
            //解压前根路径
            String path = agFileStore.getPath();
            //解压后根路径
            String paths = agFileStore.getPath().substring(0, agFileStore.getPath().lastIndexOf("."));
            agFileStore.setPath(paths);
            agFileStore.setUrl(FileUtil.toUrl(agFileStore.getPath()));
            //文件解压
            boolean flag = FileUtil.CompressUtil.unZip(FileUtil.getPath() + path);
            //解压失败
            if (!flag) {
                throw new RuntimeException("文件解压失败!");
            }
            //获取解压文件路径
            String newPath = agFileStore.getPath().substring(0, agFileStore.getPath().lastIndexOf(File.separator) + 1) + agFileStore.getName();
            //重命名文件夹名称
            new File(FileUtil.getPath() + newPath).renameTo(new File(FileUtil.getPath() + agFileStore.getPath()));
            //删除压缩文件
            FileUtil.deleteFolder(FileUtil.getPath() + path);
        }
        return agFileStoreMapper.save(agFileStore) > 0 ? true : false;
    }

    /**
     * 编辑一条信息
     *
     * @param agFileStore
     * @return
     * @throws RuntimeException
     */
    @Override
    public boolean update(AgFileStore agFileStore) throws RuntimeException {
        return agFileStoreMapper.update(agFileStore) > 0 ? true : false;
    }

    /**
     * 特殊条件查询列表
     *
     * @param agFileStore
     * @param page
     * @return
     * @throws RuntimeException
     */
    @Override
    public PageInfo<AgFileStore> getByDomainOrUsage(AgFileStore agFileStore, Page page) throws RuntimeException {
        PageHelper.startPage(page);
        List<AgFileStore> list = agFileStoreMapper.getByDomainOrUsage(agFileStore);
        return new PageInfo<AgFileStore>(list);
    }

    /**
     * 特殊条件查询列表
     *
     * @param agFileStore
     * @return
     * @throws RuntimeException
     */
    @Override
    public List<AgFileStore> downloadByDomainAndUsage(AgFileStore agFileStore) throws RuntimeException {
        List<AgFileStore> list = agFileStoreMapper.downloadByDomainAndUsage(agFileStore);
        return list;
    }
}
