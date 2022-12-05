package com.augurit.agcloud.agcom.agsupport.sc.stylemanager.controller;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgStyleManager;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.stylemanager.service.IAgStyleManagerService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/07
 * @Description:
 */
@Api(value = "样式管理", description = "样式管理相关接口")
@RestController
@RequestMapping("/agsupport/configManager/styleConfig")
public class AgStyleManagerV2Controller {

    @Autowired
    private IAgStyleManagerService styleManagerService;


    @GetMapping("/get")
    @ApiOperation(value = "查询样式", notes = "通过id查询样式接口")
    public ContentResultForm get(@ApiParam(value = "样式主键")String id) {
        try{
            if(StringUtils.isNotEmpty(id)){
                AgStyleManager agStyleManager = styleManagerService.findById(id);
                return new ContentResultForm(true, agStyleManager);
            }
            else {
                return new ContentResultForm(false, "Query id cannot be empty");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ContentResultForm(false, null);
        }

    }


    /**
     * 分页查询样式
     * @param name
     * @param page
     * @return
     */
    @GetMapping("/find")
    @ApiOperation(value = "分页查询样式", notes = "分页查询样式相关接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "样式名称", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "String"),
            @ApiImplicitParam(name = "layerType", value = "图层类型", dataType = "String"),
    })
    public ContentResultForm find(String name,String layerType, Page page) {
        PageInfo<AgStyleManager> styleList = styleManagerService.findStyleList(name, layerType, page);
        return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(styleList));
    }


    @PostMapping("/add")
    @ApiOperation(value = "添加样式", notes = "添加样式接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "styleManager", value = "样式信息", dataType = "Object"),
            @ApiImplicitParam(name = "file", value = "样式预览图", dataType = "File")
    })
    public ResultForm add(AgStyleManager styleManager, MultipartFile file){
        try{
            if(file != null && !file.isEmpty()){
                String viewImg = Base64.getEncoder().encodeToString(file.getBytes());
                styleManager.setViewImg(viewImg);
            }
            styleManagerService.saveStyle(styleManager);
            return new ResultForm(true, "添加成功");
        }catch (Exception e){
            return new ResultForm(false, "添加失败");
        }
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除样式", notes = "删除样式接口")
    public ResultForm delete(String ids){
        try{
            if(ids == null){
                return new ResultForm(false, "请选择数据");
            }
            styleManagerService.deleteStyleBatch(ids);
            return new ResultForm(true, "删除成功");
        }catch (Exception e){
            return new ResultForm(false, "删除失败");
        }
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改样式", notes = "修改样式接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "styleManager", value = "样式信息", dataType = "Object"),
            @ApiImplicitParam(name = "file", value = "样式预览图", dataType = "File"),
            @ApiImplicitParam(name = "paramType", value = "修改类型参数，‘1’只修改样式；‘2’修改样式以及绑定图层", dataType = "String",defaultValue = "1"),
            @ApiImplicitParam(name = "layerId", value = "图层id", dataType = "String")
    })
    public ResultForm update(AgStyleManager styleManager, MultipartFile file,@RequestParam(value = "paramType",defaultValue = "1") String paramType,String layerId){
        try{
            if(styleManager == null){
                return new ResultForm(false, "请选择数据");
            }
            if(styleManager.getId() == null){
                return new ResultForm(false, "请选择数据");
            }
            if(file != null && !file.isEmpty()){
                String viewImg = Base64.getEncoder().encodeToString(file.getBytes());
                styleManager.setViewImg(viewImg);
            }
            styleManagerService.updateStyle(styleManager,paramType,layerId);
            return new ResultForm(true, "修改成功");
        }catch (Exception e){
            return new ResultForm(false, "修改失败");
        }
    }
}
