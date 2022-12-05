package com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.service;

import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.domain.AgIdentifyMancarResultResult;
import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.domain.AgIdentifyMancarResultParam;
import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.domain.AgIdentifyMancarResultStatisticsResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: qinyg
 * @Date: 2020/12/28 11:33
 * @tips: 人车识别结果表
 */
public interface IAgIdentifyMancarResultService {
    /**
     *
     * @Author: qinyg
     * @Date: 2020/12/28 14:16
     * @tips: 添加识别结果
     * @param
     * @return
     */
    void add(AgIdentifyMancarResultParam param, MultipartFile file);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/12/29 10:59
     * @tips: 查询所有视频识别结果
     * @param sourceId 查询条件，识别视频id
     * @param startTimeParam 查询条件，开始时间
     * @param endTimeParam 查询条件结束时间
     * @return
     */
    AgIdentifyMancarResultResult get(String sourceId, String startTimeParam, String endTimeParam);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/12/29 14:22
     * @tips: 每隔 times 秒，统计人数和车数
     * @param sourceId 查询条件，识别视频id
     * @param times 每隔几秒，单位：秒
     * @return
     */
    AgIdentifyMancarResultStatisticsResult statisticsPeopleAndCarNum(String sourceId, Integer times);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/12/30 16:20
     * @tips: 导出成excel
     * @param
     * @return
     */
    void exportExcel(String sourceId, String startTimeParam, String endTimeParam, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
