package com.augurit.agcloud.agcom.agsupport.sc.portal.config.controller;

import com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.entity.TreeNode;
import com.augurit.agcloud.agcom.agsupport.sc.portal.config.service.ISysConfigService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @Author: libc
 * @Description: portal 系统配置控制器
 * @Date: 2020/12/23 14:23
 * @Version: 1.0
 */
@Api(value = "portal 系统配置", description = "portal 系统配置相关接口")
@RestController
@RequestMapping("/agsupport/portal/sysConfig")
public class SysConfigController {

    @Autowired
    private ISysConfigService sysConfigService;

    @ApiOperation(value = "获取文件目录树结构", notes = "获取文件目录树结构接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirType", value = "查询的目录类型", dataType = "String"),
    })
    @GetMapping(value = "/tree")
    public ContentResultForm tree(String dirType) {
        List<TreeNode<File>> treeList = sysConfigService.tree(dirType);
        return new ContentResultForm(true, treeList, "查询成功");
    }

    @ApiOperation(value = "上传节点目录对应的md说明文档", notes = "上传节点目录对应的md说明文档接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "md文档", dataType = "MultipartFile"),
            @ApiImplicitParam(name = "dirPath", value = "对应文件目录路径", dataType = "String"),
    })
    @PostMapping(value = "/upload")
    public ContentResultForm upload(@RequestParam("file") MultipartFile file, String dirPath) {
        sysConfigService.uploadFile(file, dirPath);
        return new ContentResultForm(true, null, "上传成功");
    }

    @ApiOperation(value = "md文档预览", notes = "md文档预览接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filePath", value = "md文档绝对路径", dataType = "String"),
    })
    @GetMapping(value = "/preview")
    public ContentResultForm preview(String filePath) {
        String mdFileStr = sysConfigService.getMdFile(filePath);
        return new ContentResultForm(true, mdFileStr, "查询成功");
    }

    @ApiOperation(value = "新增节点（文件夹）", notes = "新增节点（文件夹）接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "newFileName", value = "文件夹名称", dataType = "String"),
            @ApiImplicitParam(name = "dirPath", value = "对应文件目录路径", dataType = "String"),
    })
    @PostMapping(value = "/add")
    public ContentResultForm add(String newFileName, String dirPath) {
        sysConfigService.add(dirPath, newFileName);
        return new ContentResultForm(true, null, "新增成功");
    }

    @ApiOperation(value = "修改节点（文件夹）", notes = "修改节点（文件夹）接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modifyFileName", value = "修改的文件夹名称", dataType = "String"),
            @ApiImplicitParam(name = "dirPath", value = "选中的文件目录路径", dataType = "String"),
    })
    @PostMapping(value = "/update")
    public ContentResultForm update(String modifyFileName, String dirPath) {
        sysConfigService.update(dirPath, modifyFileName);
        return new ContentResultForm(true, null, "修改成功");
    }

    @ApiOperation(value = "删除节点（文件夹）", notes = "删除节点（文件夹）接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirPath", value = "对应文件目录路径", dataType = "String"),
    })
    @DeleteMapping(value = "/delete")
    public ContentResultForm delete( String dirPath) {
        sysConfigService.delete(dirPath);
        return new ContentResultForm(true, null, "删除成功");
    }



}
