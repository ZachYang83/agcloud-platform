package com.augurit.agcloud.agcom.agsupport.sc.defaultvalue.controller;
import com.augurit.agcloud.agcom.agsupport.domain.AgDefaultValue;
import com.augurit.agcloud.agcom.agsupport.sc.defaultvalue.service.IAgDefaultValue;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author:
 * @Description:
 * @Date:created in :15:19 2019/3/19
 * @Modified By:
 */
@Api(value = "树表接口",description = "树表接口")
@RestController
@RequestMapping("/agsupport/defaultvalue")
public class AgDefaultValueController {
    @Autowired
    private IAgDefaultValue iAgDefaultValue;

    @ApiOperation(value = "保存数据",notes = "保存数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agDefaultValue",required = true, value = "树表信息对象", dataType = "AgDefaultValue")
    })
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResultForm save(AgDefaultValue agDefaultValue) {
        JSONObject json = new JSONObject();
        try {
            agDefaultValue.setId(UUID.randomUUID().toString());
            iAgDefaultValue.save(agDefaultValue);
            return new ResultForm(true,"保存成功!");
        } catch (Exception e) {
            return new ResultForm(false,"保存失败!");
        }
    }
    @ApiOperation(value = "修改数据",notes = "修改数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agDefaultValue",required = true, value = "树表信息对象", dataType = "AgDefaultValue")
    })
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResultForm update(AgDefaultValue agDefaultValue) {
        JSONObject json = new JSONObject();
        try {
            iAgDefaultValue.update(agDefaultValue);
            return new ResultForm(false,"修改成功");
        } catch (Exception e) {
            return new ResultForm(false,"修改失败");
        }
    }
    @ApiOperation(value = "根据key获取信息",notes = "根据key获取信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key",required = true, value = "树表信息的key", dataType = "String")
    })
    @RequestMapping(value = "/findByKey",method = RequestMethod.POST)
    public ContentResultForm findByKey(String key) {
        try {
            return new ContentResultForm(true,iAgDefaultValue.findByKey(key),"根据key获取信息" );
        } catch (Exception e) {
            return new ContentResultForm(false,"","根据key获取信息失败" );
        }
    }
}
