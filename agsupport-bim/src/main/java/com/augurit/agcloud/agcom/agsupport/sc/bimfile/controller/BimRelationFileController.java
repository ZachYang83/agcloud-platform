package com.augurit.agcloud.agcom.agsupport.sc.bimfile.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimRelationFile;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimRelationFile;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 关联文件controller
 * Created by fanghh on 2019/12/5.
 */

@RestController
@RequestMapping("/agsupport/bimRelationFile")
@Api(value = "BIM关联文件",description = "BIM关联文件相关接口")
public class BimRelationFileController {

    @Autowired
    private IBimRelationFile bimRelationFile;

    @ApiOperation(value = "获取bim模型关联的文件", notes = "获取bim模型关联的文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bimId", required = true, value = "bim模型Id", dataType = "String"),
            @ApiImplicitParam(name = "page", required = true, value = "分页参数", dataType = "Page")})
    @GetMapping("/findBimRelationFile")
    public ContentResultForm findBimRelationFile(String bimId, Page page) throws Exception {
        PageInfo<AgBimRelationFile> pageInfo = bimRelationFile.findBimRelationFile(bimId, page);
        return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(pageInfo));
    }

    @ApiOperation(value = "保存bim关联的文件记录", notes = "保存bim关联的文件记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bimId", required = true, value = "bim模型Id", dataType = "String"),
            @ApiImplicitParam(name = "fileIds", required = true, value = "文件id数组", dataType = "List")})
    @PostMapping("/saveBimRelationFile")
    public ContentResultForm saveBimRelationFile(@RequestParam("bimId") String bimId,@RequestParam("fileIds[]") List<String> fileIds) {
        if (CollectionUtils.isEmpty(fileIds)) {
            return new ContentResultForm(false, "","保存记录为空");
        }
        bimRelationFile.saveBimRelationFile(bimId, fileIds);
        return new ContentResultForm(true, "","操作成功");
    }

    @ApiOperation(value = "删除", notes = "删除保存bim关联的文件记录")
    @ApiImplicitParam(name = "id", required = true, value = "记录Id", dataType = "String")
    @DeleteMapping("/delete")
    public ContentResultForm delete(String ids) {
        if (StringUtils.isBlank(ids)) {
            return new ContentResultForm(false,"","id为空");
        }
        String[] splitId = ids.split(",");
        Arrays.stream(splitId).forEach(x -> bimRelationFile.delete(x));
        return new ContentResultForm(true, "","操作成功");
    }
}
