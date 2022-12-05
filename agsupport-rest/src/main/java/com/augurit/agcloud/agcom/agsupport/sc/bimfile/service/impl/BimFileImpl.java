package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimFile;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimFileMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimFile;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName BimFileImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2019/12/4 15:48
 * @Version 1.0
 **/
@Service
public class BimFileImpl implements IBimFile {

    @Autowired
    AgBimFileMapper agBimFileMapper;

    /**
     * 获取所有数据
     *
     * @return
     * @throws RuntimeException
     */
    @Override
    public List<AgBimFile> getAll() throws RuntimeException {
        return agBimFileMapper.getAll();
    }

    /**
     * 特殊条件Or分页查询
     *
     * @param keyword
     * @param page
     * @return
     * @throws RuntimeException
     */
    @Override
    public PageInfo<AgBimFile> getByOrKeyWords(String projectId, String keyword, Page page) throws RuntimeException {
        PageHelper.startPage(page);
        List<AgBimFile> list = agBimFileMapper.getByOrKeyWords(projectId, keyword);
        return new PageInfo<>(list);
    }

}
