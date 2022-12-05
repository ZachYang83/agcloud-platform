package com.augurit.agcloud.agcom.agsupport.sc.widgetStore.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgWidgetStoreCustom;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetStoreWithBLOBs;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
public interface IAgWidgetStoreService {

    /**
     * 列表
     * @param name
     * @param page
     * @return
     */
    PageInfo<AgWidgetStoreCustom> list(String name, Page page);

    /**
     * 添加
     * @param store
     * @param thumbFile
     * @param widgetFile
     */
    void save(AgWidgetStoreWithBLOBs store, MultipartFile thumbFile, MultipartFile widgetFile);

    /**
     * 修改
     * @param store
     * @param thumbFile
     * @param widgetFile
     */
    void update(AgWidgetStoreWithBLOBs store, MultipartFile thumbFile, MultipartFile widgetFile);

    /**
     * 删除
     * @param ids
     */
    void deleteBatch(String ids);

    /**
     * 获取缩略图
     * @param id
     * @return
     */
    AgWidgetStoreWithBLOBs getThumb(String id);

    /**
     * 获取微件文件
     * @param id
     * @return
     */
    AgWidgetStoreWithBLOBs getWidget(String id);

    /**
     * 获取md文件
     * @param id
     * @return
     */
    String getMdFile(String id);
}
