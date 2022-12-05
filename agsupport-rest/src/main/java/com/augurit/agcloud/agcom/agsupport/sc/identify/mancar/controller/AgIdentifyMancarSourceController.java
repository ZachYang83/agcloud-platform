package com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.controller;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgIdentifyMancarSource;
import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.service.IAgIdentifyMancarSourceService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: qinyg
 * @Date: 2020/12/28 14:34
 * @tips:
 */
@RestController
@Api(value = "人车识别视频接口", description = "人车识别视频接口")
@RequestMapping("/agsupport/identify/mancarSource")
public class AgIdentifyMancarSourceController {
    private static final Logger logger = LoggerFactory.getLogger(AgIdentifyMancarSourceController.class);

    @Autowired
    private IAgIdentifyMancarSourceService sourceService;

    @GetMapping("/find")
    @ApiOperation(value = "列表",notes = "列表")
    public ResultForm find(AgIdentifyMancarSource source, @RequestParam(defaultValue = "1") String status){
        //赋值默认值
        source.setStatus(status);
        try{
            List<AgIdentifyMancarSource> list = sourceService.find(source);
            return new ContentResultForm(true, list);
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "查询失败");
        }
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加",notes = "添加")
    public ResultForm add(AgIdentifyMancarSource source, MultipartFile file){
        if(file == null){
            return new ResultForm(false, "文件为空，请上传文件");
        }
        if(file.isEmpty()){
            return new ResultForm(false, "文件为空，请上传文件");
        }
        try{
            sourceService.add(source, file);
            return new ContentResultForm(true, "", "添加成功");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "添加失败");
        }
    }
}
