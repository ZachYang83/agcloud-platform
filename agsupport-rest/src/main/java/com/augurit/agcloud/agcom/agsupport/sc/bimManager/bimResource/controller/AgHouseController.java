package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.controller;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgHouse;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgSysSetting;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgHouseService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/07
 * @Description:
 */
@RestController
@RequestMapping("/agsupport/BIM/Project")
@Api(value = "房屋相关接口", description = "房屋相关接口")
public class AgHouseController {

    private static final Logger logger = LoggerFactory.getLogger(AgHouseController.class);

    @Autowired
    private IAgHouseService houseService;
    //添加rvt模型
//    @Autowired
//    private ISysAgHouseService sysAgHouseService;


    /**
     * 查询所有资源
     * @param resource
     * @param page
     * @return
     */
    @GetMapping("/find")
    @ApiOperation(value = "房屋列表",notes = "房屋列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hourseName", value = "房屋名称（搜索条件）", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "分页参数:page=1&rows=10", dataType = "int"),
    })
    public ResultForm find(AgHouse resource, Page page){
        try{
            PageInfo<AgHouse> allResource = houseService.list(resource, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(allResource));
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "查询失败");
        }
    }

    /**
     * 查询所有系统设置
     * @return.
     *
     */
    @GetMapping("/get")
    @ApiOperation(value = "查询数据",notes = "查询数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramType", value = "paramType=1查询户型图；paramType=2获取系统设置信息", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "paramType=1查询户型图；id是房屋id", dataType = "String"),
    })
    public ContentResultForm findSysSetting(String paramType, String id){
        try{
            if("1".equals(paramType)){
                List<AgHouse> allResource = houseService.findResourceDir(id);
                return new ContentResultForm(true, allResource);
            }
            if("2".equals(paramType)){
                List<AgSysSetting> list = houseService.getAllSysSetting();
                return new ContentResultForm(true, list, "查询成功");
            }
            return new ContentResultForm(true, null, "查询成功");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }


//    @PostMapping("/add")
//    @ApiOperation(value = "保存房屋信息(zip压缩文件)",notes = "保存房屋信息(zip压缩文件)")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "modelFile", value = "房屋模型（只支持zip压缩文件）", dataType = "File", required = true),
//    })
//    public ResultForm add(MultipartFile modelFile){
//        if(modelFile == null || modelFile.isEmpty()){
//            return new ResultForm(false, "modelFile参数为空");
//        }
//        try {
//            //默认只设计一个根目录
//            String categoryId = "children";
//            sysAgHouseService.saveRvtZip(categoryId, modelFile);
//            return new ContentResultForm(true, null,"保存成功");
//        } catch (SourceException e) {
//            logger.info(e.getMessage());
//            return new ResultForm(false, e.getMessage());
//        }catch (Exception e) {
//            logger.info(e.getMessage());
//            return new ResultForm(false, "上传失败");
//        }
//    }


}
