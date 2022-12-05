package com.augurit.agcloud.agcom.agsupport.sc.bimfile.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimFile;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimFile;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName BimFileController
 * @Description TODO
 * @Author Administrator
 * @Date 2019/12/4 15:45
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/agsupport/bimfile")
@Api(value = "BimFileController", tags = {"bim模型管理"})
public class BimFileController {

    @Autowired
    IBimFile iBimFile;

    @GetMapping("/getAll")
    @ApiOperation(value = "获取所有数据")
    public ContentResultForm getAll() {
        try {
            List<AgBimFile> agBimFileList = iBimFile.getAll();
            if (null == agBimFileList || agBimFileList.size() == 0) {
                return new ContentResultForm(false, null, "暂无数据!");
            }
            return new ContentResultForm(true, agBimFileList, "获取成功!");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    @ApiOperation(
            value = "分页获取数据",
            notes = "分页获取数据接口"
    )
    @ApiImplicitParams({@ApiImplicitParam(
            name = "agBimFile",
            required = false,
            value = "BIM模型信息",
            dataType = "AgBimFile"
    ), @ApiImplicitParam(
            name = "page",
            required = true,
            value = "分页参数:/agsupport/bimstore/getByOrKeyWords?page=1&rows=10&keyword=",
            dataType = "Page"
    )})
    @RequestMapping(
            value = {"/getByOrKeyWords"},
            method = {RequestMethod.GET}
    )
    public ContentResultForm getByOrKeyWords(String keyword, String projectId, Page page) throws Exception {
        PageInfo<AgBimFile> pageInfo = iBimFile.getByOrKeyWords(projectId, keyword, page);
        EasyuiPageInfo<AgBimFile> result = PageHelper.toEasyuiPageInfo(pageInfo);
        return new ContentResultForm(true, result);
    }

}
