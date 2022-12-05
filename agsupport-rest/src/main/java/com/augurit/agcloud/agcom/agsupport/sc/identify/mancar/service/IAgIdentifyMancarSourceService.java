package com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgIdentifyMancarSource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: qinyg
 * @Date: 2020/12/28 11:35
 * @tips: 人车识别资源表
 */
public interface IAgIdentifyMancarSourceService {
    /**
     *
     * @Author: qinyg
     * @Date: 2020/12/28 13:38
     * @tips: 查询视频资源列表
     * @param
     * @return
     */
    List<AgIdentifyMancarSource> find(AgIdentifyMancarSource source);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/12/28 13:43
     * @tips: 添加视频
     * @param
     * @param file 视频文件
     * @return
     */
    void add(AgIdentifyMancarSource source, MultipartFile file);


    /**
     *
     * @Author: qinyg
     * @Date: 2020/12/28 14:11
     * @tips: 修改资源状态为已读
     * @param id 主键id
     * @return
     */
    void updateSourceStatus(String id);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/12/28 14:12
     * @tips: 通过主键获取数据
     * @param
     * @return
     */
    AgIdentifyMancarSource getById(String id);
}
