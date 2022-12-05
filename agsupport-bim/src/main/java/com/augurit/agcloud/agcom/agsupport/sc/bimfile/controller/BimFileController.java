package com.augurit.agcloud.agcom.agsupport.sc.bimfile.controller;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.agcom.agsupport.common.util.io.DataConversionUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimFile;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimVersion;
import com.augurit.agcloud.agcom.agsupport.domain.AgFileStore;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.dto.BimFileListDTO;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.reslut.MultipartFileParam;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimFile;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimVersion;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
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
@Api(value = "BIM管理",description = "BIM管理相关接口")
public class BimFileController {
    public static final String BIM_PATH = "ui-static/agcloud/agcom/ui/sc/bimfile/file/";

    @Autowired
    IBimFile iBimFile;
    @Autowired
    IBimVersion iBimVersion;

    @Value("${upload.filePath}")
    private String basePath;

    /**
     * 样式配置页面
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/index.html")
    public ModelAndView index(Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupportbim/bimfile/index");
    }

    @GetMapping("/getById")
    @ApiOperation(value = "根据ID获取数据")
    @ApiParam(value = "BIM模型ID")
    public ContentResultForm getById(@RequestParam(defaultValue = "") String id) {
        try {
            if (StringUtils.isBlank(id)) {
                return new ContentResultForm(false, "BIM模型ID不能为空!");
            }
            AgBimFile agBimFile_out = iBimFile.getById(id);
            if (null == agBimFile_out) {
                return new ContentResultForm(false, null, "暂无数据!");
            }
            return new ContentResultForm(true, agBimFile_out, "获取成功!");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

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

    @DeleteMapping("/delete")
    @ApiOperation(value = "根据ID删除一条数据")
    @ApiParam(value = "BIM模型ID")
    public ContentResultForm deleteById(@RequestParam(defaultValue = "") String paramId) {
        try {
            if (StringUtils.isBlank(paramId)) {
                return new ContentResultForm(false, null, "BIM模型ID不能为空!");
            }
            boolean falg = iBimFile.deleteById(paramId);
            if (!falg) {
                return new ContentResultForm(false, null, "删除失败!");
            }
            return new ContentResultForm(true, null, "删除成功!");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    @DeleteMapping("/deleteMany")
    @ApiOperation(value = "删除更多信息")
    @ApiParam(value = "BIM模型ID")
    public ContentResultForm deleteMany(@RequestParam(defaultValue = "") String paramIds) {
        try {
            if (StringUtils.isBlank(paramIds)) {
                return new ContentResultForm(false, null, "BIM模型ID不能为空!");
            }
            List<String> idsList = Arrays.asList(paramIds.split(","));
            if (idsList.size() < 1) {
                return new ContentResultForm(false, null, "BIM模型ID不能为空!");
            }
            boolean falg = iBimFile.deleteMany(idsList);
            if (!falg) {
                return new ContentResultForm(false, null, "删除失败!");
            }
            return new ContentResultForm(true, null, "删除成功!");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加一条数据")
    @ApiParam(value = "BIM模型")
    public ContentResultForm add(@RequestBody AgBimFile agBimFile) {
        try {
            iBimFile.saveBimFile(agBimFile);
            return new ContentResultForm(true, agBimFile.getId(), "添加成功!");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    @PostMapping("/addFileAndVersion")
    @ApiOperation(value = "添加bimFile和bimVersion")
    @ApiParam(value = "文件名，文件md5")
    public ContentResultForm addFileAndVersion(String fileName, String md5, String projectId) {
        try {
            boolean flag = iBimFile.addFileAndVersion(fileName, md5, projectId);
            if (!flag) {
                return new ContentResultForm(false, null, "上传失败!");
            }
            return new ContentResultForm(true, null, "上传成功!");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation(value = "编辑一条数据")
    @ApiParam(value = "BIM模型")
    public ContentResultForm update(@RequestBody AgBimFile agBimFile) {
        try {
            agBimFile.setUpdateTime(new Date());
            iBimFile.updateBimFile(agBimFile);
            return new ContentResultForm(true, null, "编辑成功!");
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

    @ApiOperation(value = "文件上传")
    @ApiParam(value = "文件流")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ContentResultForm uploadFile(HttpServletRequest request, MultipartFileParam param) {
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                try {
                    iBimFile.uploadFileByMappedByteBuffer(param);
                } catch (Exception e) {
                    return new ContentResultForm(false, "上传失败", e.getMessage());
                }
            }
            return new ContentResultForm(true, null, "上传成功");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    /**
     * bim模型下载
     *
     * @param paramId
     * @param response
     * @return
     */
    @ApiOperation(value = "BIM模型文件下载，仅支持已发布模型")
    @ApiParam(value = "paramId")
    @GetMapping(value = "/download")
    public void downloadFile(@RequestParam(defaultValue = "") String paramId, HttpServletResponse response) throws IOException {
        if (StringUtils.isBlank(paramId)) {
            DataConversionUtil.setResponseJson(response, JSON.toJSONString(
                    new ContentResultForm(false, null, "ID不能为空!")));
            return;
        }
        AgBimVersion agBimVersion = iBimVersion.getInUseByPkId(paramId);
        //文件不存在
        if (null == agBimVersion) {
            DataConversionUtil.setResponseJson(response, JSON.toJSONString(
                    new ContentResultForm(false, null, "文件不存在!")));
            return;
        }
        String path = basePath + agBimVersion.getBimPath();
//        String newPath = path.replaceAll("\\\\", "\\\\\\\\");
        File file = new File(path);
        //若文件存在，则进行下载
        if (file.exists()) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                DataConversionUtil.setResponseJson(response, JSON.toJSONString(
                        new ContentResultForm(false, null, "下载失败")));
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            DataConversionUtil.setResponseJson(response, JSON.toJSONString(
                    new ContentResultForm(false, null, "下载失败")));
            return;
        }
    }


    @PostMapping("/deleteBim")
    @ApiOperation(value = "根据PATH删除BIM模型")
    @ApiParam("BIM模型PATH")
    public ContentResultForm deleteBim(String path) {
        try {
            if (StringUtils.isBlank(path)) {
                return new ContentResultForm(false, null, "PATH不能为空!");
            }
            if (!FileUtil.checkFileOrFolder(FileUtil.getPath() + path))
                return new ContentResultForm(false, null, "文件或者文件夹不存在!");
            FileUtil.deleteFolder(FileUtil.getPath() + path);
            return new ContentResultForm(true, null, "删除成功!");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    @GetMapping("/saveFileByUrl")
    public String saveFileByUrl(String url) {
        Integer integer = iBimFile.saveFileByUrl(url);
        String res = "下载成功";
        switch (integer) {
            case 0:
                res = "连接错误，文件流异常";
            case 2:
                res = "连接错误，检查网络是否畅通或地址是否正确";
        }
        return res;
    }


    @PostMapping("/startService")
    @ApiOperation(value = "发布bim服务")
    @ApiParam("BIM模型")
    public ContentResultForm startService(String id, boolean polling) {
        if (StringUtils.isBlank(id)) {
            return new ContentResultForm(false, null, "id为空");
        }
        return iBimFile.startService(id, polling);
    }

    @GetMapping("/getFileByModuleCode")
    @ApiOperation(value = "根据业务编号获取记录")
    public ResultForm getFileByModuleCode(@ApiParam(value = "查询条件") AgFileStore store, @RequestParam("bimId") String bimId) {
        List<AgFileStore> files = iBimFile.getFileByModuleCode(store, bimId);
        return new ContentResultForm(true, files);
    }

    @ApiOperation(value = "BIM模型文件检查，仅支持已发布模型")
    @ApiParam(value = "paramId")
    @GetMapping(value = "/checkBIM")
    public ContentResultForm checkBIM(@RequestParam(defaultValue = "") String paramId) throws IOException {
        if (StringUtils.isBlank(paramId)) {
            return new ContentResultForm(false, null, "id为空");
        }
        return iBimFile.checkBimInfo(paramId);
    }

    @ApiOperation(value = "下载BIM模型Zip压缩包中的rvt/rfa文件")
    @GetMapping(value = "/downloadBIMInZip")
    public void downloadBIMInZip(@RequestParam(defaultValue = "") String pkId, @RequestParam(defaultValue = "") String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (StringUtils.isBlank(pkId) || StringUtils.isBlank(fileName)) {
            DataConversionUtil.setResponseJson(response, JSON.toJSONString(
                    new ContentResultForm(false, null, "参数为空!")));
            return;
        }
        iBimFile.downloadBIMinZip(pkId, fileName, request, response);
    }


    @PostMapping("/saveList")
    @ApiOperation(value = "批量生成BIM模型")
    @ApiParam(value = "BIM模型",name = "fileList")
    public ResultForm saveList(@RequestBody BimFileListDTO fileListDTO){
        iBimFile.saveList(fileListDTO);
        return new ContentResultForm(true);
    }
}
