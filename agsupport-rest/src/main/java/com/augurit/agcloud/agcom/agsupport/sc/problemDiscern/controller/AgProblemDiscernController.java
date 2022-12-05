package com.augurit.agcloud.agcom.agsupport.sc.problemDiscern.controller;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.AgProblemDiscern;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.problemDiscern.service.AgProblemDiscernService;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author: libc
 * @Description: 问题识别模块-控制器
 * @Date: 2020/8/28 13:56
 * @Version: 1.0
 */
@Api(value = "问题识别", description = "问题识别相关接口")
@RestController
@RequestMapping("/agsupport/problemDiscern")
public class AgProblemDiscernController {

    private static final Logger logger = LoggerFactory.getLogger(AgProblemDiscernController.class);

    @Autowired
    private AgProblemDiscernService problemDiscernService;



    @ApiOperation(value = "保存问题识别", notes = "保存问题识别接口")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "problemDiscern",value = "问题识别对象",dataType = "AgProblemDiscern",paramType = "body"),
                    @ApiImplicitParam(name = "problemType", value = "1:BIM审查；2：标签管理", dataType = "String",defaultValue = "1",required = true),
                    @ApiImplicitParam(name = "files",value = "问题图片(可以一次传多张)",dataType = "MultipartFile[]")
            }
    )
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultForm save( AgProblemDiscern problemDiscern, @RequestParam(value = "problemType",defaultValue = "1") String problemType, @RequestParam(value = "files") MultipartFile[] files) {
        try {
            problemDiscernService.save(problemDiscern, problemType, files);
            return new ResultForm(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    @ApiOperation(value = "根据图片标识查询数据", notes = "根据图片标识查询数据接口")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "imgId",value = "图片标识号",dataType = "String"),
                    @ApiImplicitParam(name = "problemType", value = "1:BIM审查；2：标签管理", dataType = "String",defaultValue = "1",required = true),
                    @ApiImplicitParam(name = "paramType",value = "查询类型参数: 1=返回单个对象；2=返回对象集合",dataType = "String",defaultValue = "1",required = true),
            }
    )
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ContentResultForm getByImgId( @RequestParam(value = "imgId") String imgId, @RequestParam(value = "problemType",defaultValue = "1") String problemType, @RequestParam(value = "paramType",defaultValue = "2") String paramType) {
        try {
            if ("1".equals(paramType)){
                // 查询单个
                AgProblemDiscern problemDiscern = problemDiscernService.findByImgId(imgId, problemType);
                if(problemDiscern == null){
                    return new ContentResultForm(true, null,"未查询到对应数据");
                }
                return new ContentResultForm<AgProblemDiscern>(true, problemDiscern,"查询数据成功");
            }else if ("2".equals(paramType)){
                // 查询多个图片
                List<AgProblemDiscern> pds = problemDiscernService.findListByImgId(imgId, problemType);
                if (CollectionUtils.isEmpty(pds)){
                    return new ContentResultForm(true, null,"未查询到对应数据");
                }
                return new ContentResultForm<List<AgProblemDiscern>>(true, pds,"查询数据成功");
            }
            return new ContentResultForm(true, null,"查询类型参数未知！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, e.getMessage(),"查询数据失败");
        }
    }

//    @ApiOperation(value="预览标签管理图片", notes = "预览标签管理图片接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id",value = "预览图片的id", dataType = "String")
//    })
//    @GetMapping(value = "/preview")
//    public void preview(String id, HttpServletResponse response){
//        try{
//            problemDiscernService.view(id, response);
//        }catch (SourceException e){
//            logger.info("-----预览文件源error-------" + e.getMessage());
//        }catch (Exception e){
//            logger.info("-----预览文件error-------" + e.getMessage());
//        }
//    }

    @ApiOperation(value = "批量删除问题识别", notes = "批量删除问题识别接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "删除的ids字符串,用逗号分开", dataType = "String")
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ContentResultForm delete(String ids) {
        try {
            if (StringUtils.isEmpty(ids)) {
                return new ContentResultForm(false, "ids不能为空或参数类型传递错误");
            }
            // 封装pIds数组
            String[] pIds = ids.split(",");

            problemDiscernService.deleteByIds(pIds);
            return new ContentResultForm(true,null, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, e.getMessage(),"删除失败");
        }
    }

}
