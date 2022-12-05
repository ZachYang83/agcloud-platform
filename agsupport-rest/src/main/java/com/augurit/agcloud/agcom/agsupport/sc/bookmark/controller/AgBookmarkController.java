package com.augurit.agcloud.agcom.agsupport.sc.bookmark.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgBookmark;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.agcom.agsupport.sc.bookmark.service.IAgBookmark;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :14:20 2019/4/8
 * @Modified By:
 */
@Api(value = "书签设置接口",description = "书签设置接口")
@RestController
@RequestMapping("/agsupport/bookmark")
public class AgBookmarkController {
    @Autowired
    private IAgBookmark iAgBookmark;

    @ApiOperation(value = "保存或修改书签",notes = "保存或修改书签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",required = true, value = "书签id", dataType = "String"),
            @ApiImplicitParam(name = "userId",required = true, value = "用户id", dataType = "String"),
            @ApiImplicitParam(name = "data",required = true, value = "书签内容", dataType = "String")
    })
    @RequestMapping("/saveBookmark")
    public ContentResultForm saveBookmark(String id ,String userId, String data) {
        AgBookmark agBookmark = null;
        try {
            if (id==null || "".equals(id)){
                agBookmark = new AgBookmark();
                agBookmark.setId(UUID.randomUUID().toString());
                agBookmark.setUserId(userId);
                agBookmark.setData(data);
                agBookmark.setUpdateTime(new Date());
                iAgBookmark.save(agBookmark);
            }else {
                agBookmark = iAgBookmark.findById(id);
                agBookmark.setData(data);
                agBookmark.setUpdateTime(new Date());
                iAgBookmark.update(agBookmark);
            }
            return new ContentResultForm(true,agBookmark,"保存成功");
        } catch (Exception e) {
            return new ContentResultForm(false,agBookmark,"保存失败");
        }
    }

    @ApiOperation(value = "根据用户id获取书签",notes = "根据用户id获取书签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",required = true, value = "用户id", dataType = "String")
    })
    @RequestMapping(value = "/getByUserId",method = RequestMethod.POST)
    public ContentResultForm getByUserId(String userId) {
        try {
            List<AgBookmark> agBookmarks = iAgBookmark.findByUserId(userId);
            return new ContentResultForm(true,agBookmarks,"根据用户id获取书签");
        } catch (Exception e) {
            return new ContentResultForm(false,"","获取书签失败");
        }
    }

    @ApiOperation(value = "根据id获取书签", notes = "根据id获取书签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, value = "id", dataType = "String")
    })
    @RequestMapping("/getById")
    public ContentResultForm getById(String id) {
        try {
            AgBookmark agBookmark = iAgBookmark.findById(id);
            return new ContentResultForm(true, agBookmark, "根据id获取书签");
        } catch (Exception e) {
            return new ContentResultForm(false, "", "获取书签失败");
        }
    }

    @ApiOperation(value = "根据id删除书签", notes = "根据id删除书签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, value = "id", dataType = "String")
    })
    @RequestMapping("/deleteById")
    public String deleteById(String id) {
        try {
            iAgBookmark.deleteById(id);
        } catch (Exception e) {

        }
        return "{\"success\":true,\"message\":\"删除成功！\",\"id\":\"\"}";
    }
}
