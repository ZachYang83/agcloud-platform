package com.augurit.agcloud.agcom.agsupport.sc.common.controller;

import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.io.UploadFile;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传公共类controller
 * Created by fanghh on 2020/3/9.
 */
@RestController
@RequestMapping(value = "/agsupport/file")
@Api(value = "文件统一处理",description = "文件统一处理相关接口")
public class FileController {



    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public ContentResultForm uploadFile(@ApiParam(value = "文件流")MultipartFile file,@ApiParam(value = "文件存放路径") String filePath) {
        try {
            UploadFile uploadFile = FileUtil.getUploadFile(file, filePath);
            if (FileUtil.saveMultipartFile(file, (FileUtil.getPath() + uploadFile.getPath()))) {
                return new ContentResultForm(true, uploadFile, "文件上传成功!");
            }
            return new ContentResultForm(false, null, "文件上传失败!");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除文件")
    public ContentResultForm deleteFile(@ApiParam("图片信息ID")String path) {
        try {
            if (StringUtils.isBlank(path)) {
                return new ContentResultForm(false, null, "请求参数异常");
            }
            if (!FileUtil.checkFileOrFolder(FileUtil.getPath() + path))
                return new ContentResultForm(false, null, "文件或者文件夹不存在");
            FileUtil.deleteFolder(FileUtil.getPath() + path);
            return new ContentResultForm(true, null, "删除成功");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

}
