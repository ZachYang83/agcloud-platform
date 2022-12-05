package com.augurit.agcloud.agcom.agsupport.sc.style.controller;

import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgStyle;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.style.service.IAgStyle;
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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-07-17.
 */

@Api(value = "样式管理",description = "样式管理接口")
@RestController
@RequestMapping("/agsupport/style")
public class AgStyleController {

    @Autowired
    private IAgDir iAgDir;

    @Autowired
    private IAgStyle iAgStyle;

    public static final String ICON_PATH = "ui-static/agcloud/agcom/ui/sc/style/css/image/";

    public static final String THREED_PATH = "3D/";

    /**
     * 样式配置页面
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/index.html")
    @ApiIgnore
    public ModelAndView index(Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/style/index");
    }

    @RequestMapping("/three3Dindex.html")
    @ApiIgnore
    public ModelAndView three3Dindex(Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/style/three3Dindex");
    }

    /**
     * 样式配置首页
     *
     * @param model
     * @param dirLayerId
     * @return
     * @throws Exception
     */
   /* @RequestMapping("/layerStyleConf/index.do")
    public ModelAndView styleConfIndex(Model model, String dirLayerId) throws Exception {
        model.addAttribute("dirLayerId", dirLayerId);
        return new ModelAndView("/agcom/style/layerStyleConfIndex");
    }*/

    /**
     * 样式配置页面
     *
     * @param model
     * @param agStyle
     * @return
     * @throws Exception
     */
   /* @RequestMapping("/styleConf.do")
    public ModelAndView styleConf(Model model, AgStyle agStyle) throws Exception {
        String style = "";
        if (!StringUtils.isEmpty(agStyle.getId())) {
            style = iAgStyle.findStyleById(agStyle.getId()).getStyle();
        }
        model.addAttribute("style", style);
        model.addAttribute("agStyle", JsonUtils.toJson(agStyle));
        ModelAndView modelAndView = new ModelAndView();
        if ("point".equals(agStyle.getType())) {
            if ("0".equals(agStyle.getPointType())) {
                modelAndView = new ModelAndView("/agcom/style/styleConfMarker");
            } else if ("1".equals(agStyle.getPointType())) {
                modelAndView = new ModelAndView("/agcom/style/styleConfDivMarker");
            } else if ("2".equals(agStyle.getPointType())) {
                modelAndView = new ModelAndView("/agcom/style/styleConfDrawMarker");
            }
        } else if ("polyline".equals(agStyle.getType())) {
            modelAndView = new ModelAndView("/agcom/style/styleConfPolyline");
        } else if ("polygon".equals(agStyle.getType())) {
            modelAndView = new ModelAndView("/agcom/style/styleConfPolygon");
        }
        return modelAndView;
    }*/

    @ApiOperation(value = "分页获取样式信息",notes = "分页获取样式信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agStyle",required = false, value = "样式对象信息", dataType = "AgStyle"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:/agsupport/param/paramList?page=1&rows=10&name=", dataType = "Page")
    })
    @RequestMapping(value = "/findStyleListPage",method = RequestMethod.GET)
    public ContentResultForm findStyleListPage(AgStyle agStyle, Page page) throws Exception {
        PageInfo<AgStyle> pageInfo=iAgStyle.findStyleListPage(agStyle, page);
        EasyuiPageInfo<AgStyle> result = PageHelper.toEasyuiPageInfo(pageInfo);
        return new  ContentResultForm<EasyuiPageInfo<AgStyle>>(true,result);
    }

    @ApiOperation(value = "获取样式信息",notes = "获取样式信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agStyle",required = false, value = "样式对象信息", dataType = "AgStyle")
    })
    @RequestMapping(value = "/findStyleList",method = RequestMethod.GET)
    public ContentResultForm findStyleList(AgStyle agStyle) throws Exception {
        List<AgStyle> list= iAgStyle.findStyleList(agStyle);
        return new ContentResultForm(true,list);
    }

    @ApiOperation(value = "通过id查询样式信息",notes = "通过id查询样式信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",required = true, value = "样式信息id", dataType = "String")
    })
    @RequestMapping(value = "/findStyleById",method = RequestMethod.GET)
    public ContentResultForm findStyleById(String id) throws Exception {
        return new ContentResultForm(true,iAgStyle.findStyleById(id));
    }

    @ApiOperation(value = "查询默认样式",notes = "查询默认样式接口")
    @RequestMapping(value = "/findDefaultStyleList",method = RequestMethod.GET)
    public ContentResultForm findDefaultStyleList() throws Exception {
        List<AgStyle> list = iAgStyle.findDefaultStyleList();
        return new ContentResultForm(true,list);
    }

    @ApiOperation(value = "更新图层样式",notes = "更新图层样式接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "layerId",required = true, value = "图层id", dataType = "String"),
            @ApiImplicitParam(name = "styleIds",required = true, value = "样式id", dataType = "String")
    })
    @RequestMapping(value = "/updateLayerStylesRelationship",method = RequestMethod.POST)
    public ResultForm updateLayerStylesRetionship(String layerId, String styleIds) {

        if(StringUtils.isNotEmpty(layerId) && StringUtils.isNotEmpty(styleIds)) {
            try {
                List<String> newStyleIds = Arrays.asList(styleIds.split(","));
                /* 获取当前图层之前所配置的样式集合olderStyles*/
                List<String> olderStyles = iAgStyle.getStyleIdsByLayerId(layerId);
                iAgStyle.updateLayerStylesRetionship(layerId, newStyleIds, olderStyles);
                return new ResultForm(true, "更新成功！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResultForm(false, "更新失败！");
    }

    @ApiOperation(value = "保存样式",notes = "保存样式接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agStyle", value = "样式对象信息", dataType = "AgStyle")
    })
    @RequestMapping(value = "/saveStyle",method = RequestMethod.POST)
    public ResultForm saveStyle(AgStyle agStyle) {
        try {
            if (StringUtils.isEmpty(agStyle.getId())) {
                agStyle.setId(UUID.randomUUID().toString());
                iAgStyle.saveStyle(agStyle);
            } else {
                iAgStyle.updateStyle(agStyle);
            }
            return new ResultForm(true, "保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false, "保存失败！");
    }

    @ApiOperation(value = "删除单条或多条样式",notes = "删除单条或多条样式接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "样式id，多条使用英文逗号隔开", dataType = "String")
    })
    @RequestMapping(value = "/deleteStyle",method = RequestMethod.DELETE)
    public ResultForm deleteStyle(String id) {
        try {
            String[] ids = id.split(",");
            iAgStyle.deleteStyle(ids);
            return new ResultForm(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false,"删除失败");
    }

    /**
     * 获取所有的图标
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取所有的图标",notes = "获取所有的图标")
    @RequestMapping(value = "/getAllIcon",method = RequestMethod.GET)
    public ContentResultForm getAllIcon(HttpServletRequest request) throws Exception {
        //String rootPath = request.getSession().getServletContext().getRealPath("/").replace("\\", "/");
        String uploadAbsolutePath = UploadUtil.getUploadAbsolutePath();
        String uploadRelativePath = UploadUtil.getUploadRelativePath();
        String iconPath = uploadAbsolutePath + ICON_PATH;
        String file = request.getParameter("file");
        if (file != null) {
            iconPath = uploadAbsolutePath + ICON_PATH + file + "/";
        }
        System.out.println("jarPath:"+uploadAbsolutePath);
        List<Map<String,Object>> listIcon =  iAgStyle.findAllIcon(iconPath);
        for(int i = 0;i<listIcon.size();i++){
            Map<String,Object> itemStyle = listIcon.get(i);
            String icon =uploadRelativePath + ICON_PATH + itemStyle.get("icon").toString();
            itemStyle.put("icon",icon);
        }
        return new ContentResultForm(true,listIcon);
    }

    /**
     * 上传图标
     *
     * @param request
     * @throws Exception
     */
    @ApiOperation(value = "上传图标",notes = "上传图标")
    @RequestMapping(value = "/uploadIcon",method = RequestMethod.POST)
    public void uploadIcon(HttpServletRequest request) {
        System.out.println("uploadIcon");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> fileMap = multipartRequest.getMultiFileMap();
        String uploadAbsolutePath = UploadUtil.getUploadAbsolutePath();
        for (Map.Entry<String, List<MultipartFile>> entity : fileMap.entrySet()) {
            List<MultipartFile> multipartFiles = entity.getValue();
            for (MultipartFile mf : multipartFiles) {
                String fileName = mf.getOriginalFilename();
                String iconPath = uploadAbsolutePath + ICON_PATH;
                String file = request.getParameter("file");
                if (file != null) {
                    iconPath = uploadAbsolutePath + ICON_PATH + file + "/";
                }
                if (!new File(iconPath).exists()) {
                    new File(iconPath).mkdirs();
                }
                File uploadFile = new File(iconPath + fileName);
                System.out.println("uploadFilePath:"+uploadFile.getPath());
                if (uploadFile.exists()) {
                    uploadFile.delete();
                }
                try {
                    FileCopyUtils.copy(mf.getBytes(), uploadFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 通过名称删除图标
     *
     * @param iconName
     * @throws Exception
     */
    @ApiOperation(value = "通过图标名称删除图标",notes = "通过id删除图标接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iconName", value = "图标名称", dataType = "String")
    })
    @RequestMapping(value = "/deleteIcon",method = RequestMethod.DELETE)
    public ResultForm deleteIcon(HttpServletRequest request, String iconName) {
        System.out.println("1111111");
        try {
            String iconFileName = iconName;
            if (iconFileName.contains("/")) {
                iconFileName = iconFileName.substring(iconFileName.lastIndexOf('/') + 1);
            }
            String rootPath = UploadUtil.getUploadAbsolutePath();
            File uploadFile = new File(rootPath + ICON_PATH + iconFileName);
            if (uploadFile.exists()) {
                uploadFile.delete();
                return new ResultForm(true, "删除成功");
            } else {
                return new ResultForm(false, "文件不存在");
            }
        }
        catch (Exception e){
            return new ResultForm(false, e.getMessage());
        }
    }

    /**
     * 上传并解析sld文件
     *
     * @param request
     * @throws Exception
     */
    @ApiOperation(value = "上传并解析sld文件",notes = "上传并解析sld文件接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirLayerId", value = "目录图层id", dataType = "String",paramType = "path")
    })
    @RequestMapping(value = "/readSld/{dirLayerId}",method = RequestMethod.POST)
    public ContentResultForm readSld(HttpServletRequest request, @PathVariable("dirLayerId") String dirLayerId) throws Exception {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        String iconRootPath = basePath + ICON_PATH;
        Map result = new HashMap();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile multipartFile = entity.getValue();
            result = iAgStyle.loadStyleConf(multipartFile, dirLayerId, iconRootPath);
        }
        return new ContentResultForm(true,result);
    }

    @ApiOperation(value = "获取表字段数据",notes = "获取表字段数据接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId", value = "数据源id", dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "tableName", value = "表名", dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "column", value = "字段", dataType = "String",paramType = "path")
    })
    @RequestMapping(value = "/listTableColumnData/{dataSourceId}/{tableName}/{column}",method = RequestMethod.GET)
    public ContentResultForm listTableColumnData(@PathVariable("dataSourceId") String dataSourceId, @PathVariable("tableName") String tableName, @PathVariable("column") String column) throws Exception {
        return new ContentResultForm(true,iAgStyle.listTableColumnData(dataSourceId, tableName, column));
    }

    /**
     * 根据ids获取图层样式list
     * @param layerIds
     * @return
     */
    @ApiOperation(value = "根据ids获取图层样式list",notes = "根据ids获取图层样式list接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "layerIds", value = "图层样式ids", dataType = "String")
    })
    @RequestMapping(value = "getStyleListByLayerIds",method = RequestMethod.GET)
    public ContentResultForm getStyleListByLayerIds(String layerIds) throws Exception {

        return new ContentResultForm(true,iAgStyle.getStyleConfList(layerIds));
    }



    /**
     * 上传3D符号相关文件
     *
     * @param request
     * @throws Exception
     */
    @ApiOperation(value = "上传3D符号相关文件",notes = "上传3D符号相关文件")
    @RequestMapping(value = "/upload3DFile",method = RequestMethod.POST)
    public ContentResultForm upload3DFile(HttpServletRequest request) {
        ContentResultForm resultForm = new ContentResultForm(true);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> fileMap = multipartRequest.getMultiFileMap();
        String uploadAbsolutePath = UploadUtil.getUploadAbsolutePath();
        for (Map.Entry<String, List<MultipartFile>> entity : fileMap.entrySet()) {
            List<MultipartFile> multipartFiles = entity.getValue();
            for (MultipartFile mf : multipartFiles) {
                String fileName = mf.getOriginalFilename();
                String iconPath = uploadAbsolutePath + THREED_PATH;
                String file = request.getParameter("file");
                if (file != null) {
                    iconPath = uploadAbsolutePath + THREED_PATH + file + "/";
                }
                if (!new File(iconPath).exists()) {
                    new File(iconPath).mkdirs();
                }
                File uploadFile = new File(iconPath + fileName);
                if (uploadFile.exists()) {
                    uploadFile.delete();
                }

                try {
                    FileCopyUtils.copy(mf.getBytes(), uploadFile);
                    resultForm.setMessage("文件上传成功");
                    resultForm.setContent("upload/"+THREED_PATH+fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return resultForm;
    }

    /**
     * 获取3D符号相关文件路径
     *
     * @throws Exception
     */
    @ApiOperation(value = "上传3D符号相关文件",notes = "上传3D符号相关文件")
    @RequestMapping(value = "/get3DFiles",method = RequestMethod.GET)
    public ContentResultForm get3DFiles(HttpServletRequest request) throws Exception{
        String uploadAbsolutePath = UploadUtil.getUploadAbsolutePath();
        String uploadRelativePath = UploadUtil.getUploadRelativePath();
        String iconPath = uploadAbsolutePath + THREED_PATH;
        String file = request.getParameter("file");
        if (file != null) {
            iconPath = uploadAbsolutePath + THREED_PATH + file + "/";
        }
        System.out.println("jarPath:"+uploadAbsolutePath);
        List<Map<String,Object>> listIcon =  iAgStyle.findAllIcon(iconPath);
        for(int i = 0;i<listIcon.size();i++){
            Map<String,Object> itemStyle = listIcon.get(i);
            String icon =uploadRelativePath + THREED_PATH + itemStyle.get("icon").toString();
            itemStyle.put("icon",icon);
        }
        return new ContentResultForm(true,listIcon);
    }

}
