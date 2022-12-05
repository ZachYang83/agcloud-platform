package com.augurit.agcloud.agcom.agsupport.sc.keyvalue.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgKeyValue;
import com.augurit.agcloud.agcom.agsupport.sc.keyvalue.service.KeyValueService;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequestMapping("/agsupport/keyvalue")
@RestController
public class KeyValueController {

    @Autowired
    private KeyValueService keyValueService;

    @RequestMapping("/findKeyValueList")
    public ContentResultForm findKeyValueList(String name, Page page) throws Exception {
        PageInfo<AgKeyValue> list = this.keyValueService.findKeyValueList(name, page);
        EasyuiPageInfo<AgKeyValue> agSupKeyValueEasyuiPageInfo = PageHelper.toEasyuiPageInfo(list);
        return new ContentResultForm<EasyuiPageInfo>(true,agSupKeyValueEasyuiPageInfo);
    }
    @ApiImplicitParams({@ApiImplicitParam(
            name = "domain",
            value = "非必填",
            required = false,
            dataType = "string",
            paramType = "query"
    ),@ApiImplicitParam(
            name = "key",
            value = "非必填",
            required = false,
            dataType = "string",
            paramType = "query"
    )})
    @RequestMapping("/findKeyValue")
    public ContentResultForm findKeyValue(@RequestParam(value="domain",required = false) String domain,@RequestParam(value="key",required = false) String key) throws Exception {
        List<AgKeyValue> list = this.keyValueService.findKeyValue(domain, key);
        return new ContentResultForm<List<AgKeyValue>>(true,list);
    }

    @RequestMapping("/save")
    public ResultForm saveKeyValue(AgKeyValue keyvalue) throws Exception {
        if(StringUtils.isNotBlank(keyvalue.getId())){
            this.keyValueService.update(keyvalue);
        }else {
            keyvalue.setId(UUID.randomUUID().toString());
            this.keyValueService.save(keyvalue);
        }
        return new ResultForm(true,"保存成功");
    }

    @RequestMapping("/delete")
    public ResultForm deleteKeyValue(String ids) throws Exception {
        String[] idArr = ids.split(",");
        List<String> idList = Arrays.asList(idArr);
        this.keyValueService.delete(idList);
        return new ResultForm(true,"删除成功");
    }
    @RequestMapping("/index.html")
    public ModelAndView index(HttpServletRequest request, Model model) throws Exception {
        //String loginName = LoginHelpClient.getLoginName(request);
        //AgUser agUser = iAgUser.findUserByName(loginName);
        return new ModelAndView("agcloud/agcom/agsupport/keyvalue/index");
    }
}
