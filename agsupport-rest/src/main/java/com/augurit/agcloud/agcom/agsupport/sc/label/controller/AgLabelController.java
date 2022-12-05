package com.augurit.agcloud.agcom.agsupport.sc.label.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgLabel;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.label.service.IAgLabel;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.common.util.Common;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by Administrator on 2017-05-11.
 */
@Api(value = "标注",description = "标注接口")
@RestController
@RequestMapping("/agsupport/label")
public class AgLabelController {

    @Autowired
    private IAgLabel iAgLabel;

    @ApiOperation(value = "获取标注信息",notes = "获取标注信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",required = true, value = "用户id", dataType = "String")
    })
    @RequestMapping(value = "/labelList",method = RequestMethod.GET)
    public ContentResultForm labelList(String userId) {
        try {
            List<AgLabel> list = iAgLabel.findAllLabel(userId);
            //这里要将list转为listMap是为了兼容以前的一些前端请求
            List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
            for(int i = 0 ; i < list.size();i++){
                Map<String,Object> item = new HashMap<String,Object>();
                item.put("id",list.get(i).getId());
                item.put("userId",list.get(i).getUserId());
                String data = list.get(i).getData();
                Map mapData =(Map)com.alibaba.fastjson.JSON.parse(data);
                item.put("data",mapData);
                listMap.add(item);
            }
            return new ContentResultForm(true,listMap,"");
        } catch (Exception e) {
            return new ContentResultForm(false,new JSONArray(),"获取标注信息失败");
        }
    }

    @ApiOperation(value = "保存标注信息",notes = "保存标注信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agLabel",required = true, value = "标注信息", dataType = "AgLabel")
    })
    @RequestMapping(value = "/saveLabel",method = RequestMethod.POST)
    public ContentResultForm saveLabel(AgLabel agLabel) {
        try {
            if (Common.isCheckNull(agLabel.getData()) || Common.isCheckNull(agLabel.getUserId())) {
                return new ContentResultForm(false, "", "数据不完整！");
            }
            if (StringUtils.isEmpty(agLabel.getId())) {
                agLabel.setId(UUID.randomUUID().toString());
                iAgLabel.saveLabel(agLabel);
            } else {
                iAgLabel.updateLabel(agLabel);
            }
            JSONObject json = new JSONObject();
            json.put("id", agLabel.getId());
            return new ContentResultForm(true, json, "保存数据成功！");
        } catch (Exception e) {
            return new ContentResultForm(true, "", "保存数据失败！");
        }
    }

    @ApiOperation(value = "删除标注信息",notes = "删除标注信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",required = true, value = "标注信息", dataType = "String")
    })
    @RequestMapping(value = "/deleteLabel",method = RequestMethod.DELETE)
    public ResultForm deleteLabel(String id) {
        try {
            if (StringUtils.isEmpty(id)) return null;
            iAgLabel.deleteLabel(id);
            return new ResultForm(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false, "删除数据失败！");
    }
}
