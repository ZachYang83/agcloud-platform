package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.controller;

import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityResultCustom;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgcim3dentityVbXService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * <p>
 * 把所有的前缀agcim3dentity_xxx的表查询接口合并起来
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@RestController
@RequestMapping("/agsupport/BIM/dentity")
@EnableSwagger2
@Api(value = "agcim3dxxx_xxx合并接口", description = "agcim3dxxx_xxx合并接口")
public class Agcim3dentityVbXController {

    private static final Logger logger = LoggerFactory.getLogger(Agcim3dentityVbXController.class);

    @Autowired
    private IAgcim3dentityVbXService agcim3dentityVbXService;

    @GetMapping("/find")
    @ApiOperation(value = "列表查询(需要对参数进行url编码)", notes = "列表查询(需要对参数进行url编码)")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "tableName", value = "查询条件", dataType = "String", required = true),
            @ApiImplicitParam(name = "id", value = "查询条件", dataType = "String"),
            @ApiImplicitParam(name = "objectid", value = "查询条件", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "查询条件", dataType = "String"),
            @ApiImplicitParam(name = "version", value = "查询条件", dataType = "Long"),
            @ApiImplicitParam(name = "infotype", value = "查询条件", dataType = "String"),
            @ApiImplicitParam(name = "profession", value = "查询条件", dataType = "String"),
            @ApiImplicitParam(name = "level", value = "查询条件", dataType = "String"),
            @ApiImplicitParam(name = "catagory", value = "查询条件", dataType = "String"),
            @ApiImplicitParam(name = "materialid", value = "查询条件", dataType = "String"),
            @ApiImplicitParam(name = "categorypath", value = "查询条件", dataType = "String"),
            @ApiImplicitParam(name = "projectname", value = "查询条件", dataType = "String"),
            @ApiImplicitParam(name = "projectcode", value = "查询条件", dataType = "String"),

            @ApiImplicitParam(name = "filterType", value = "过滤条件（如果此参数不为空，则返回指定字段）", dataType = "String"),

            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "分页参数:page=1&rows=10", dataType = "int"),
    })
    public ContentResultForm find(String tableName, String id, String objectid, String name, Long version, String infotype,
                                  String profession, String level, String catagory, String materialid, String categorypath,
                                  String projectname, String projectcode, Page page, String filterType) {
        try {
            if (StringUtils.isEmpty(tableName)) {
                return new ContentResultForm(false, null, "tableName参数不能为空");
            }
//            if ("agcim3dentity_via".equals(tableName)) {
//                Agcim3dentityVia param = new Agcim3dentityVia();
//                param.setId(id);
//                param.setObjectid(objectid);
//                param.setName(name);
//                param.setVersion(version);
//                param.setInfotype(infotype);
//                param.setProfession(profession);
//                param.setLevel(level);
//                param.setCatagory(catagory);
//                param.setMaterialid(materialid);
//                param.setCategorypath(categorypath);
//                PageInfo<Agcim3dentityVia> list = agcim3dentityViaService.list(param, page, filterType);
//                return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(list));
//            }
//            if ("agcim3dentity_vb1".equals(tableName)) {
//                Agcim3dentityVb1 param = new Agcim3dentityVb1();
//                param.setId(id);
//                param.setObjectid(objectid);
//                param.setName(name);
//                param.setVersion(version);
//                param.setInfotype(infotype);
//                param.setProfession(profession);
//                param.setLevel(level);
//                param.setCatagory(catagory);
//                param.setMaterialid(materialid);
//                param.setCategorypath(categorypath);
//                PageInfo<Agcim3dentityVb1> list = agcim3dentityVb1Service.list(param, page, filterType);
//                return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(list));
//            }
//            if ("agcim3dentity_vb2".equals(tableName)) {
//                Agcim3dentityVb2 param = new Agcim3dentityVb2();
//                param.setId(id);
//                param.setObjectid(objectid);
//                param.setName(name);
//                param.setVersion(version);
//                param.setInfotype(infotype);
//                param.setProfession(profession);
//                param.setLevel(level);
//                param.setCatagory(catagory);
//                param.setMaterialid(materialid);
//                param.setCategorypath(categorypath);
//                PageInfo<Agcim3dentityVb2> list = agcim3dentityVb2Service.list(param, page, filterType);
//                return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(list));
//            }
//            if ("agcim3dentity_vb3".equals(tableName)) {
//                Agcim3dentityVb3 param = new Agcim3dentityVb3();
//                param.setId(id);
//                param.setObjectid(objectid);
//                param.setName(name);
//                param.setVersion(version);
//                param.setInfotype(infotype);
//                param.setProfession(profession);
//                param.setLevel(level);
//                param.setCatagory(catagory);
//                param.setMaterialid(materialid);
//                param.setCategorypath(categorypath);
//                PageInfo<Agcim3dentityVb3> list = agcim3dentityVb3Service.list(param, page, filterType);
//                return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(list));
//            }
//            if ("agcim3dproject".equals(tableName)) {
//                Agcim3dproject param = new Agcim3dproject();
//                param.setId(id);
//                param.setProjectname(projectname);
//                param.setProjectcode(projectcode);
//                PageInfo<Agcim3dproject> list = agcim3dprojectService.list(param, page);
//                return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(list));
//            }
//            return new ContentResultForm(true, null);
            Agcim3dentityResultCustom list = agcim3dentityVbXService.find(tableName, id, objectid, name, version, infotype, profession, level, catagory, materialid, categorypath, projectname, projectcode, page, filterType);
            return new ContentResultForm(true, list);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }

    @GetMapping("/statistics")
    @ApiOperation(value = "统计分类", notes = "统计分类")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "tableName", value = "查询条件,统计的表", dataType = "String", required = true),
            @ApiImplicitParam(name = "name", value = "名称统计（不为空则统计）", dataType = "String"),
            @ApiImplicitParam(name = "level", value = "level统计（不为空则统计）", dataType = "String"),
            @ApiImplicitParam(name = "catagory", value = "分类统计（不为空则统计）", dataType = "String"),

    })
    public ContentResultForm statistics(String tableName, String name, String level, String catagory) {
        try {
            if (StringUtils.isEmpty(tableName)) {
                return new ContentResultForm(false, null, "tableName参数不能为空");
            }
//            if ("agcim3dentity_via".equals(tableName)) {
//                Object obj = agcim3dentityViaService.countCatagory(name, level, catagory);
//                return new ContentResultForm(true, obj);
//            }
//            if ("agcim3dentity_vb1".equals(tableName)) {
//                Object obj = agcim3dentityVb1Service.countCatagory(name, level, catagory);
//                return new ContentResultForm(true, obj);
//            }
//            if ("agcim3dentity_vb2".equals(tableName)) {
//                Object obj = agcim3dentityVb2Service.countCatagory(name, level, catagory);
//                return new ContentResultForm(true, obj);
//            }
//            if ("agcim3dentity_vb3".equals(tableName)) {
//                Object obj = agcim3dentityVb3Service.countCatagory(name, level, catagory);
//                return new ContentResultForm(true, obj);
//            }
//            if ("agcim3dproject".equals(tableName)) {
//                Object obj = agcim3dprojectService.countCatagory();
//                return new ContentResultForm(true, obj);
//            }
//            return new ContentResultForm(true, null);
            Object obj = agcim3dentityVbXService.statistics(tableName, name, level, catagory);
            return new ContentResultForm(true, obj);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }


    @GetMapping("/preview")
    @ApiOperation(value = "预览", notes = "预览")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramType", value = "paramType=1，glb构件预览；", dataType = "String", required = true),
            @ApiImplicitParam(name = "tableName", value = "查询条件,统计的表", dataType = "String", required = true),
            @ApiImplicitParam(name = "id", value = "id", dataType = "String"),
    })
    public void preview(String tableName, String id, String paramType, HttpServletResponse response) {
        try {
            //构件glb预览
            if("1".equals(paramType)){
                Map glb = agcim3dentityVbXService.getGlb(tableName, id);
                if(glb != null && glb.get("glb") != null){
                    FileUtil.writerFile((byte[]) glb.get("glb"), UUID.randomUUID().toString() + ".glb", false, response);
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

}
