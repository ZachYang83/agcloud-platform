package com.augurit.agcloud.agcom.agsupport.sc.labelImport.service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by 陈泽浩 on 2017-05-08.
 */
public interface IAgShpDataImport {

    String getShpData(HttpServletRequest request, String flag);

    String getShpDataByZipFile(String zipPath);
    String getShp(String path);

    String readDwgData(String filePath, String coordType, String projectId, String fileName)throws Exception;


    String getDxfData(HttpServletRequest request, String flag);

    String uploadFile(HttpServletRequest request, String flag);

    boolean deleteFile(String path);
    String readExcel(String filePath);
    String readCsvFile(String path) throws Exception;

    String writeCsvFile(String path, String text) throws IOException;
}
