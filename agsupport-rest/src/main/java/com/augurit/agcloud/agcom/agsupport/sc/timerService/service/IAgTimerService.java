package com.augurit.agcloud.agcom.agsupport.sc.timerService.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgTimerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by caokp on 2017-08-29.
 */
public interface IAgTimerService {

    /**
     * 按id查找
     *
     * @param id
     * @return
     * @throws Exception
     */
    AgTimerService findTimerServiceById(String id) throws Exception;

    /**
     * 分页查找
     *
     * @param agTimerService
     * @return
     * @throws Exception
     */
    PageInfo<AgTimerService> findTimerServiceList(AgTimerService agTimerService, Page page) throws Exception;

    /**
     * 保存
     *
     * @param file
     * @param agTimerService
     * @throws Exception
     */
    void save(MultipartFile file, AgTimerService agTimerService) throws Exception;

    /**
     * 修改
     *
     * @param agTimerService
     * @throws Exception
     */
    void update(AgTimerService agTimerService) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @throws Exception
     */
    void delete(String id) throws Exception;

    /**
     * 批量删除
     *
     * @param ids
     * @throws Exception
     */
    void deleteBatch(String[] ids) throws Exception;

    /**
     * 运行定时器
     *
     * @param agTimerService
     * @throws Exception
     */
    void timerRun(AgTimerService agTimerService) throws Exception;
}
