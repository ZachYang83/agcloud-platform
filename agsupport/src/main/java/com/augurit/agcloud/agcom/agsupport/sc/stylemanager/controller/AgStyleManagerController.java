package com.augurit.agcloud.agcom.agsupport.sc.stylemanager.controller;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgStyleManager;
import com.augurit.agcloud.agcom.agsupport.sc.stylemanager.service.IAgStyleManagerService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

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
public class AgStyleManagerController {

    @Autowired
    private IAgStyleManagerService styleManagerService;

    /**
     * 样式管理页面
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/index.html")
    @ApiIgnore
    public ModelAndView index(Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/styleManager/index");
    }

    /**
     * 查询样式
     * @param name
     * @param page
     * @return
     */
    @GetMapping("/find")
    @ApiOperation(value = "查询样式", notes = "分页查询样式相关接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "样式名称", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "String"),
            @ApiImplicitParam(name = "layerType", value = "图层类型", dataType = "String"),
    })
    public ContentResultForm find(String name,String layerType, Page page) {
        PageInfo<AgStyleManager> styleList = styleManagerService.findStyleList(name, layerType, page);
        return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(styleList));
    }
//    删除查询所有样式接口
//    @GetMapping("/findAllStyle")
//    @ApiOperation(value = "查询所有样式", notes = "样式接口")
//    public ContentResultForm findAllStyle() {
//        List<AgStyleManager> styleList = styleManagerService.findAllStyle();
//        return new ContentResultForm(true, styleList);
//    }


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
            @ApiImplicitParam(name = "file", value = "样式预览图", dataType = "File")
    })
    public ResultForm update(AgStyleManager styleManager, MultipartFile file){
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
            styleManagerService.updateStyle(styleManager);
            return new ResultForm(true, "修改成功");
        }catch (Exception e){
            return new ResultForm(false, "修改失败");
        }
    }


    @GetMapping("/get")
    @ApiOperation(value = "查询样式", notes = "通过id查询样式接口")
    public ContentResultForm get(@ApiParam(value = "样式主键")String id) {
        AgStyleManager agStyleManager = styleManagerService.findById(id);
        return new ContentResultForm(true, agStyleManager);
    }
}
