package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheck;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.util.FileEntity;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.domain.ExcelResponseDomain;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author: libc
 * @Description: BIM审查-业务接口
 * @Date: 2020/9/10 9:32
 * @Version: 1.0
 */
public interface IBimCheckService {

    /**
     * @Version  1.0
     * @Author libc
     * @Description
     * @param cityName,projectName,designCompany,checkResultList,response
     *                cityName:审查城市名称（报告名称）
     *                projectName:项目名称
     *                designCompany:设计单位
     *                checkResultList:BIM审查结果列表
     *                response
     *
     * @Return
     * @Date 2020/9/10 12:02
     */
    String preview(String cityName, String projectName, String designCompany, List<String> checkResultList);

    /**
     * 解析excel文件
     * @param excelFile
     * @return
     */
    List<ExcelResponseDomain> statisticsExcel(MultipartFile excelFile);

    /**
     *
     * @Author: libc
     * @Date: 2020/10/23 17:37
     * @tips: pdf报告下载
     * @param filePath
     * @param request
     * @param response
     * @return
     */
    void download(String filePath, HttpServletRequest request, HttpServletResponse response);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/10/30 15:30
     * @tips: 添加bim审查数据，数据入库
     * @param
     * @return
     */
    void add(AgBimCheck bimCheck);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/10/30 15:35
     * @tips: 删除bim审查数据
     * @param ids 多个删除，逗号分隔
     * @return
     */
    void delete(String ids);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/10/30 15:36
     * @tips: 修改bim审查
     * @param
     * @return
     */
    void update(AgBimCheck bimCheck);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/10/30 15:37
     * @tips: 查询
     * @param
     * @return
     */
    PageInfo<AgBimCheck> find(AgBimCheck bimCheck, Page page);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/03 10:11
     * @tips: 统计BIM审查表的属性和个数，目前统计：
     * @param
     * @return
     */
    Map<String, Object> statisticsBimCheck(String sourceId);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/03 11:26
     * @tips: 添加BIM审查数据
     * @param  sourceId BIM审查项目id
     * @param  fileEntities 数据来源是json文件
     * @return
     */
    void addAgBimCheckFromJsonFile(String sourceId, List<FileEntity> fileEntities);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/03 16:52
     * @tips:通过来源id删除BIM审查数据
     * @param  sourceId 来源id
     * @return
     */
    void deleteAgBimCheckFromSourceId(String sourceId);
}