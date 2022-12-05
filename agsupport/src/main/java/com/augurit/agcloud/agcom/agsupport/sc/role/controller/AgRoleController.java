package com.augurit.agcloud.agcom.agsupport.sc.role.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgRole;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.log.service.IAgLog;
import com.augurit.agcloud.agcom.agsupport.sc.role.service.IAgRole;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Augurit on 2017-04-21.
 */
@Api(value = "角色",description = "角色接口")
@RestController
@RequestMapping("/agsupport/role")
public class AgRoleController {

    private static Logger logger = LoggerFactory.getLogger(AgRoleController.class);
    @Autowired
    private IAgRole iAgRole;

    @Autowired
    private IAgLog log;
    /**
     * 查询所有角色
     *
     * @param agRole
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询所有角色",notes = "查询所有角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agRole" ,value = "角色信息对象",dataType = "AgRole"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:page=1&rows=10", dataType = "Page")
    })
    @RequestMapping(value = "/roleDataList",method = RequestMethod.GET)
    public ContentResultForm roleDataList(AgRole agRole, Page page, HttpServletRequest request) throws Exception {
        String pageNum = request.getParameter("page");
        String pageSize = request.getParameter("rows");
        if (StringUtils.isNotBlank(pageNum)){
            page.setPageNum(Integer.valueOf(pageNum));
        }else {
            page.setPageNum(1);
        }
        if (StringUtils.isNotBlank(pageSize)){
            page.setPageSize(Integer.valueOf(pageSize));
        }else {
            page.setPageSize(10);
        }
        PageInfo<AgRole> agRolePageInfo = iAgRole.searchRole(agRole, page);
        return new ContentResultForm<>(true, PageHelper.toEasyuiPageInfo(agRolePageInfo));
    }
}
