package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.controller;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @Author: qinyg
 * @Date: 2020/12/14 14:05
 * @tips:
 *
 */
@RestController
@RequestMapping("/agsupport/bimResource/template")
@Api(value = "模板下载相关接口", description = "模板下载相关接口")
public class DownLoadTemplateV3Controller {

    private static final Logger logger = LoggerFactory.getLogger(DownLoadTemplateV3Controller.class);


    /**
     * type == 1  downloadTemplate/房屋信息模板.xls
     * type == 2  downloadTemplate/材料信息模板.xls
     * @param type
     * @param response
     */
    @GetMapping("/download")
    @ApiOperation(value = "下载模板接口",notes = "下载模板接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value =
                            "type = 1 下载：房屋信息模板.xls;" +
                            "type = 2 下载：材料信息模板.xls;"
                    , dataType = "String"),
    })
    public void download(String type, HttpServletRequest request, HttpServletResponse response){
        InputStream inputStream = null;
        try{
            String fileName = "";
            logger.info("--------down-----------type=" + type);
            if("1".equals(type)){
                ClassPathResource classPathResource = new ClassPathResource("com/augurit/agcloud/agcom/agsupport/fileTemplate/excel/房屋信息模板.xls");
                inputStream = classPathResource.getInputStream();
                fileName = classPathResource.getFilename();
            }
            if("2".equals(type)){
                ClassPathResource classPathResource = new ClassPathResource("com/augurit/agcloud/agcom/agsupport/fileTemplate/excel/材料信息模板.xls");
                inputStream = classPathResource.getInputStream();
                fileName = classPathResource.getFilename();
            }
            byte[] by = new byte[inputStream.available()];
            inputStream.read(by);
            fileName = FileUtil.encodeFileName(fileName, request);

            FileUtil.writerFile(by, fileName, true, response);
        }catch (SourceException e){
            logger.info(e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
