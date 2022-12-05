package com.augurit.agcloud.agcom.agsupport.sc.analysisReport.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Author: libc
 * @Description: 分析报告业务接口类
 * @Date: 2020/10/25 10:37
 * @Version: 1.0
 */
public interface IAnalysisReportService {
    /**
     *
     * @Author: libc 
     * @Date: 2020/10/25 10:46
     * @tips: CIM退线分析报告下载 (pdf 文档)
     * @param dataList 需要导出的表格内容
     * @return
     */
    void download(ArrayList<Map<String, Object>> dataList, HttpServletRequest request, HttpServletResponse response);
}
