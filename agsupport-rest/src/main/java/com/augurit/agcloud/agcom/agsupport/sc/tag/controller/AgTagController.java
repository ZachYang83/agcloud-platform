package com.augurit.agcloud.agcom.agsupport.sc.tag.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgTag;
import com.augurit.agcloud.agcom.agsupport.domain.AgTagCatalog;
import com.augurit.agcloud.agcom.agsupport.domain.AgTagLayer;
import com.augurit.agcloud.agcom.agsupport.sc.tag.service.IAgTag;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Api(value = "图层标签管理",description = "图层标签管理相关接口")
@RestController
@RequestMapping("/agsupport/tag")
public class AgTagController {

    private static final Logger log = Logger.getLogger(AgTagController.class);

    @Autowired
    private IAgTag iAgTag;

    @RequestMapping("/index.html")
    public ModelAndView index(HttpServletRequest request, Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/tag/index");//layerTagManage
    }

    @ApiOperation(value = "获取标签目录",notes = "获取标签目录接口")
    @RequestMapping(value = "/getTagCatalogs",method = RequestMethod.GET)
    public ContentResultForm findAllTagCatalogs() {
        List<Map> result = new ArrayList<>();
        try {
            List<AgTagCatalog> tagCatalogs = iAgTag.getAllTagCatalogs();
            List<AgTag> tags = iAgTag.getAllTags();
            List<Map<String, Object>> array = JSONArray.fromObject(JsonUtils.toJson(tagCatalogs));

            for(Map<String, Object> tagCatalog : array) {
                List<Map> tagArr = new ArrayList<>();
                for (AgTag tag:tags) {
                    if(tag.getCatalogId().equals(tagCatalog.get("id"))) {
                        tagArr.add(JSONObject.fromObject(tag));
                    }
                }
                tagCatalog.put("tags",tagArr);
                result.add(tagCatalog);
            }

        }catch (Exception e) {
            log.error("获取标签目录失败",e);
            result.clear();
            return new ContentResultForm<List<Map>>(false, result,"获取标签目录失败");
        }
        return new ContentResultForm<List<Map>>(true, result,"获取标签目录成功");
    }

    @ApiOperation(value = "获取标签目录",notes = "获取标签目录接口")
    @RequestMapping(value = "getTagCatalog",method = RequestMethod.GET)
    public ContentResultForm findAllTagCatalog() {
        List<Map> result = new ArrayList<>();
        try {
            List<AgTagCatalog> tagCatalogs = iAgTag.getAllTagCatalogs();
            List<AgTag> tags = iAgTag.getAllTags();
            List<Map<String, Object>> array = JSONArray.fromObject(JsonUtils.toJson(tagCatalogs));

            for(Map<String, Object> tagCatalog : array) {
                List<Map> tagArr = new ArrayList<>();
                for (AgTag tag:tags) {
                    if(tag.getCatalogId().equals(tagCatalog.get("id"))) {
                        tagArr.add(JSONObject.fromObject(tag));
                    }
                }
                tagCatalog.put("tags",tagArr);
                result.add(tagCatalog);
            }

        }catch (Exception e) {
            log.error("获取标签目录失败",e);
            return new ContentResultForm(false, "","获取标签目录失败");
        }
        return new ContentResultForm<>(true, result,"获取标签目录成功");
    }

    @ApiOperation(value = "通过id获取标签目录",notes = "通过id获取标签目录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",required = true, value = "标签id", dataType = "String")
    })
    @RequestMapping(value = "getTagCatalogById",method = RequestMethod.GET)
    public ContentResultForm findAllTagCatalogById(String id) {
        List<Map> result = new ArrayList<>();
        try {
            AgTagCatalog tagCatalog = iAgTag.getTagCatalogById(id);
            List<AgTag> tags = iAgTag.getAllTagByCatalogId(id);
            Map<String, Object> tagCatalogMap = JSONObject.fromObject(JsonUtils.toJson(tagCatalog));
            tagCatalogMap.put("tags",tags);
            return new ContentResultForm<>(true, tagCatalog,"获取标签目录成功");
        }catch (Exception e) {
            log.error("获取标签目录失败",e);
            return new ContentResultForm(false, "","获取标签目录失败");
        }
    }


    @ApiOperation(value = "通过id获取标签目录",notes = "通过id获取标签目录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",required = true, value = "获取标签目录id", dataType = "String")
    })
    @RequestMapping(value = "getTagsByCatalogId",method = RequestMethod.GET)
    public ContentResultForm findTagsBycatalogId(String id) {
        try {
            List<AgTag> tags = iAgTag.getAllTagByCatalogId(id);
            return new ContentResultForm<>(true, tags,"获取标签目录成功");
        }catch (Exception e) {
            log.error("获取标签目录失败",e);
            return new ContentResultForm<>(false, "","获取标签目录失败");
        }
    }


    @ApiOperation(value = "保存标签",notes = "保存标签接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tag",required = true, value = "标签对象信息", dataType = "AgTag")
    })
    @RequestMapping(value = "saveTag",method = RequestMethod.POST)
    public ContentResultForm saveTag(AgTag tag) {

        String message;
        try {
            if (StringUtils.isEmpty(tag.getId())) { //id为空,新增
                tag.setId(UUID.randomUUID().toString());
                iAgTag.addTag(tag);
                message = "添加成功";
            } else { // id非空，修改
                iAgTag.updateTag(tag);
                message = "修改成功";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, "","保存失败");

        }
        return new ContentResultForm<AgTag>(true, tag, message);
    }

    @ApiOperation(value = "保存标签目录",notes = "保存标签目录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tagCatalog",required = true, value = "标签目录对象信息", dataType = "AgTagCatalog")
    })
    @RequestMapping(value = "saveTagCatalog",method = RequestMethod.POST)
    public ContentResultForm saveTagCatalog(AgTagCatalog tagCatalog) {

        String message;
        try {
            if (StringUtils.isEmpty(tagCatalog.getId())) { //id为空,新增
                tagCatalog.setId(UUID.randomUUID().toString());
                iAgTag.addTagCatalog(tagCatalog);
                message = "添加成功";
            } else { // id非空，修改
                iAgTag.updateTagCatalog(tagCatalog);
                message = "修改成功";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, "","保存失败");
        }
        return new ContentResultForm<AgTagCatalog>(true, tagCatalog, message);
    }

    @ApiOperation(value = "删除标签目录",notes = "删除标签目录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",required = true, value = "标签目录id", dataType = "String")
    })
    @RequestMapping(value = "deleteTagCatalog",method = RequestMethod.DELETE)
    public ResultForm delTagCatalog(String id) {
        try {
            iAgTag.deleteTagCatalog(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "删除失败");
        }
        return new ResultForm(true, "删除成功");
    }

    @ApiOperation(value = "删除标签",notes = "删除标签接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",required = true, value = "标签id", dataType = "String")
    })
    @RequestMapping(value = "deleteTag",method = RequestMethod.DELETE)
    public ResultForm delTag(String id) {
        try {
            iAgTag.deleteTag(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "删除失败");
        }
        return new ResultForm(true, "删除成功");
    }


    @ApiOperation(value = "获取开放图层",notes = "获取开放图层接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName", value = "服务名称", dataType = "String"),
            @ApiImplicitParam(name = "tagId", value = "标签id", dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int")
    })
    @RequestMapping(value = "getOpenLayerNoTag",method = RequestMethod.GET)
    public ContentResultForm getOpenLayerNoTag(String serviceName, String tagId ,int pageNum, int pageSize) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<AgLayer> tagLayer = iAgTag.getOpenLayerwithoutTag(serviceName, tagId);
           return new ContentResultForm<>(true, new PageInfo<>(tagLayer) ,"获取开放图层成功");
        }catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, "","获取开放图层失败");
        }
    }

    @ApiOperation(value = "根据标签获取开放图层",notes = "根据标签取开放图层接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tagId", value = "标签id", dataType = "String"),
            @ApiImplicitParam(name = "catalogId", value = "目录id", dataType = "String"),
            @ApiImplicitParam(name = "serviceName", value = "服务名称", dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int")
    })
    @RequestMapping(value = "getOpenLayerWithTag", method = RequestMethod.GET)
    public ContentResultForm findOpenLayerWithTag(String tagId, String catalogId, String serviceName, int pageNum, int pageSize) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<AgTagLayer> tagLayers = iAgTag.getOpenLayerwithTag(tagId, catalogId, serviceName);
            List<AgTagLayer> countLayers = iAgTag.countTagLayerApplyNum();
            for (AgTagLayer tagLayer : tagLayers) {
                for (AgTagLayer countLayer : countLayers) {
                    if (countLayer.getLayerId().equals(tagLayer.getLayerId())) {
                        if (countLayer.getApplyStatus().equals("0")) { //申请中的数量
                            tagLayer.setApplyingNum(countLayer.getCount());
                        } else if (countLayer.getApplyStatus().equals("1")) {  //已拥有的数量
                            tagLayer.setHadNum(countLayer.getCount());
                        } else if (countLayer.getApplyStatus().equals("2")) { //已驳回的数量
                            tagLayer.setRejectNum(countLayer.getCount());
                        }
                    }
                }
                if (tagLayer.getApplyingNum() == null) tagLayer.setApplyingNum("0");
                if (tagLayer.getHadNum() == null) tagLayer.setHadNum("0");
                if (tagLayer.getRejectNum() == null) tagLayer.setRejectNum("0");
            }
            return new ContentResultForm<>(true, new PageInfo<>(tagLayers), "获取开放图层成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, "", "获取开放图层失败");
        }
    }


    @ApiOperation(value = "添加标签图层", notes = "添加标签图层接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tagId", value = "标签id", dataType = "String"),
            @ApiImplicitParam(name = "layerIds", value = "图层ids多个用英文逗号隔开", dataType = "String")
    })
    @RequestMapping(value = "addTagLayer", method = RequestMethod.POST)
    public ResultForm addTagLayerByBatch(String tagId, String layerIds) {
        try {
            String[] layerIdArr = layerIds.split(",");
            List<AgTagLayer> tagLayers = new ArrayList<>();
            for (String layerId : layerIdArr) {
                AgTagLayer tagLayer = new AgTagLayer();
                tagLayer.setId(UUID.randomUUID().toString());
                tagLayer.setLayerId(layerId);
                tagLayer.setTagId(tagId);
                tagLayers.add(tagLayer);
            }
//            layerIds = layerIds.concat(",");
            iAgTag.insertTagLayer(tagLayers);
            return new ResultForm(true, "添加标签图层成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false, "添加标签图层失败");
    }

    @ApiOperation(value = "删除标签图层", notes = "删除标签图层接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "layerIds", value = "图层ids多个用英文逗号隔开", dataType = "String")
    })
    @RequestMapping(value = "removeTayLayerByLayerIds", method = RequestMethod.DELETE)
    public ResultForm removeTayLayerByLayerIds(String layerIds) {
        try {
            iAgTag.removeTayLayerByLayerIds(layerIds);
            return new ResultForm(true, "删除标签图层成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false, "删除标签图层失败");
    }

    @ApiOperation(value = "是否可以删除", notes = "是否可以删除接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标签id", dataType = "String"),
            @ApiImplicitParam(name = "isTag", value = "isTag", dataType = "String")
    })
    @RequestMapping(value = "canDelete", method = RequestMethod.DELETE)
    public ResultForm wetherCanDeleteTag(String id, String isTag) {

        try {
            int num = iAgTag.wetherCanDeleteTag(id, isTag);
            if (num == 0) {
                return new ResultForm(true, "可以删除");
            } else {
                return new ResultForm(false, "不可以删除");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false, "不可以删除");
    }

}
