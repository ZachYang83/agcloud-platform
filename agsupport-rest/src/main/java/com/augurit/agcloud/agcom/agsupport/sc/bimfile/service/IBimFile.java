package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimFile;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @ClassName IBimFile
 * @Description TODO
 * @Author Administrator
 * @Date 2019/12/4 15:48
 * @Version 1.0
 **/
public interface IBimFile {

    /**
     * 获取所有数据
     *
     * @return
     * @throws RuntimeException
     */
    List<AgBimFile> getAll() throws RuntimeException;

    /**
     * 特殊条件Or分页查询
     *
     * @param keyword
     * @param page
     * @return
     * @throws RuntimeException
     */
    PageInfo<AgBimFile> getByOrKeyWords(String ProjectId,String keyword, Page page) throws RuntimeException;
}
