package com.augurit.agcloud.agcom.agsupport.sc.io.ImageStore.controller;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.agcom.agsupport.common.aspect.RestLog;
import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.io.DataConversionUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.io.FileEntity;
import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.io.UploadFile;
import com.augurit.agcloud.agcom.agsupport.domain.AgImageStore;
import com.augurit.agcloud.agcom.agsupport.sc.io.ImageStore.service.IAgImageStore;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.*;

/**
 * 图片管理
 */
@RestController
@RequestMapping(value = "/io/imagestore")
@Api(value = "AgImageStoreController", tags = {"图片管理"})
public class AgImageStoreController {

    public static final String IMAGE_PATH = "ui-static/agcloud/agcom/ui/sc/io/imagestore/image";
    public static final String CACHE_PATH = "ui-static/agcloud/agcom/ui/sc/io/imagestore/cache/";

    @Autowired
    IAgImageStore iAgImageStore;

    /**
     * 样式配置页面
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/index.html")
    public ModelAndView index(Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/io/imagestore/index");
    }


    /**
     * 获取一条信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getById")
    @ApiOperation(value = "获取一条信息")
    @ApiParam(value = "信息id")
    public ContentResultForm getById(@RequestParam(defaultValue = "") String id) {
        try {
            if (StringUtils.isBlank(id)) {
                return new ContentResultForm(false, "图片信息ID不能为空!");
            }
            AgImageStore imageStore_out = iAgImageStore.getById(id);
            if (null == imageStore_out) {
                return new ContentResultForm(true, "暂无数据!");
            }
            return new ContentResultForm(true, imageStore_out);
        } catch (Exception e) {
            return new ContentResultForm(false, e);
        }
    }

    /**
     * 获取所有信息
     *
     * @return
     */
    @GetMapping("/getAll")
    @ApiOperation(value = "获取所有信息")
    public ContentResultForm getAll() {
        try {
            List<AgImageStore> imageStoreList = iAgImageStore.getAll();
            if (null == imageStoreList || imageStoreList.size() == 0) {
                return new ContentResultForm(true, "暂无数据!");
            }
            return new ContentResultForm(true, imageStoreList);
        } catch (Exception e) {
            return new ContentResultForm(false, e);
        }
    }


    /**
     * 删除一条信息
     *
     * @param paramId
     * @return
     */
    @DeleteMapping("/deleteById")
    @ApiOperation(value = "删除一条信息")
    public ContentResultForm deleteById(@ApiParam(value = "信息id") String paramId) {
        try {
            if (StringUtils.isBlank(paramId)) {
                return new ContentResultForm(false, null, "图片信息ID不能为空");
            }
            boolean falg = iAgImageStore.deleteById(paramId);
            if (!falg) {
                return new ContentResultForm(false, null, "删除失败");
            }
            return new ContentResultForm(true, null, "删除成功");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }


    /**
     * 删除更多信息
     *
     * @param paramIds
     * @return
     */
    @DeleteMapping("/deleteMany")
    @ApiOperation(value = "删除更多信息")
    public ContentResultForm deleteMany(@ApiParam("图片信息ID") String paramIds) {
        try {
            if (StringUtils.isBlank(paramIds)) {
                return new ContentResultForm(false, null, "请求参数异常");
            }
            List<String> idsList = Arrays.asList(paramIds.split(","));
            boolean falg = iAgImageStore.deleteMany(idsList);
            if (!falg) {
                return new ContentResultForm(false, null, "删除失败");
            }
            return new ContentResultForm(true, null, "删除成功");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    /**
     * 添加一条信息
     *
     * @param agImageStore
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加一条信息")
    public ContentResultForm save(@ApiParam(value = "图片信息") AgImageStore agImageStore) {
        try {
            agImageStore.setId(UUID.randomUUID().toString());
            agImageStore.setCreateTime(new Date());
            boolean falg = iAgImageStore.save(agImageStore);
            if (!falg) {
                return new ContentResultForm(false, null, "添加失败");
            }
            return new ContentResultForm(true, agImageStore.getId(), "添加成功");
        } catch (Exception e) {
            return new ContentResultForm(false, e, e.getMessage());
        }
    }

    /**
     * 编辑一条信息
     *
     * @param agImageStore
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "编辑一条信息")
    public ContentResultForm update(@ApiParam(value = "图片信息") AgImageStore agImageStore) {
        try {
            agImageStore.setCreateTime(null);
            boolean falg = iAgImageStore.update(agImageStore);
            if (!falg) {
                return new ContentResultForm(false, null, "编辑失败");
            }
            return new ContentResultForm(true, null, "编辑成功");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    @ApiOperation(
            value = "分页获取数据",
            notes = "分分页获取数据接口"
    )
    @ApiImplicitParams({@ApiImplicitParam(
            name = "agImageStore",
            required = false,
            value = "样式对象信息",
            dataType = "AgImageStore"
    ), @ApiImplicitParam(
            name = "page",
            required = true,
            value = "分页参数:/agsupport/agImageStore/getByDomainAndUsage?page=1&rows=10&name=",
            dataType = "Page"
    )})
    @RestLog(value = "获取地区和使用者")
    @RequestMapping(value = {"/getByDomainAndUsage"}, method = {RequestMethod.GET})
    public ContentResultForm getByDomainAndUsage(AgImageStore agImageStore, Page page) throws Exception {
        PageInfo<AgImageStore> pageInfo = iAgImageStore.getByDomainAndUsage(agImageStore, page);
        EasyuiPageInfo<AgImageStore> result = PageHelper.toEasyuiPageInfo(pageInfo);
        //判断是否为路径漫游，漫游不分页
        String usage = "roam";
        if (agImageStore.getUsage().equals(usage)) {
            List<AgImageStore> list = iAgImageStore.getByDomainAndUsage(agImageStore);
            //配合前端组装EasyuiPageInfo
            EasyuiPageInfo<AgImageStore> info = new EasyuiPageInfo<AgImageStore>();
            info.setRows(list);
            return new ContentResultForm(true, info);
        } else {
            return new ContentResultForm(true, result);
        }
    }

    @ApiOperation(
            value = "分页获取数据",
            notes = "分分页获取数据接口"
    )
    @ApiImplicitParams({@ApiImplicitParam(
            name = "agImageStore",
            required = false,
            value = "样式对象信息",
            dataType = "AgImageStore"
    ), @ApiImplicitParam(
            name = "page",
            required = true,
            value = "分页参数:/agsupport/agImageStore/getByDomainOrUsage?page=1&rows=10&name=",
            dataType = "Page"
    )})
    @RequestMapping(
            value = {"/getByDomainOrUsage"},
            method = {RequestMethod.GET}
    )
    public ContentResultForm getByDomainOrUsage(AgImageStore agImageStore, Page page) throws Exception {
        PageInfo<AgImageStore> pageInfo = iAgImageStore.getByDomainOrUsage(agImageStore, page);
        EasyuiPageInfo<AgImageStore> result = PageHelper.toEasyuiPageInfo(pageInfo);
        return new ContentResultForm(true, result);
    }

    /**
     * 拖拽排序
     *
     * @param agImageStore
     * @param agImageStore 自动装配
     * @param offset       偏移值，为正时向下拖拽，反之向上
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/updateSort"}, method = {RequestMethod.GET})
    @ApiOperation(value = "/updateSort", notes = "拖拽排序")
    public ContentResultForm updateSort(AgImageStore agImageStore, @RequestParam(value = "offset", required = false) int offset) throws Exception {
        AgImageStore agImageStore1 = new AgImageStore();
        agImageStore1.setDomain(agImageStore.getDomain());
        agImageStore1.setUsage(agImageStore.getUsage());
        agImageStore1.setFullpath(agImageStore.getFullpath());
        List<AgImageStore> list = iAgImageStore.getByDomainAndUsage(agImageStore1);
        int sort = Integer.parseInt(iAgImageStore.getById(agImageStore.getId()).getSort());
        int x, y, newSort;
        //sort为原本序号，offset为偏移值
        if (offset < 0) {
            x = sort + offset;
            y = sort;
            //[x,y)范围内的序号加一
            for (int i = x; i < y; i++) {
                newSort = Integer.parseInt(list.get(i).getSort()) + 1;
                list.get(i).setSort(String.valueOf(newSort));
            }
            //移动的序号设置为原本序号+偏移值
            list.get(sort).setSort(String.valueOf(sort + offset));
        } else {
            x = sort;
            y = sort + offset;
            //(x,y]范围内的序号减一
            for (int i = x + 1; i <= y; i++) {
                newSort = Integer.parseInt(list.get(i).getSort()) - 1;
                list.get(i).setSort(String.valueOf(newSort));
            }
            //移动的序号设置为原本序号+偏移值
            list.get(sort).setSort(String.valueOf(sort + offset));
        }
        //更新数据库里的对象
        for (int i = 0; i < list.size(); i++) {
            iAgImageStore.update(list.get(i));
        }
        return new ContentResultForm(true, null, "排序成功");
    }


    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadImage")
    @ApiOperation(value = "文件上传")
    public ContentResultForm uploadImage(@ApiParam(value = "文件流") @RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            UploadFile uploadFile = FileUtil.getUploadFile(file, IMAGE_PATH);
            uploadFile.setPath(FileUtil.getPath() + uploadFile.getPath());

            if (FileUtil.saveMultipartFile(file, uploadFile.getPath())) {
                String path = (uploadFile.getPath().replaceAll("\\\\", "/")).replaceAll("//", "/");
                String p = UploadUtil.getUploadAbsolutePath().replaceAll("\\\\", "/");
                String paths[] = path.split(p);
                uploadFile.setPath("\\" + (paths.length > 1 ? paths[1].replaceAll("/", "\\\\") : paths[0].replaceAll("/", "\\\\")));
                return new ContentResultForm(true, uploadFile, "文件上传成功!");
            }
            return new ContentResultForm(false, null, "文件上传失败!");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    /**
     * 设置默认视点
     *
     * @param id
     * @return
     */
    @GetMapping("/setDefaultViewpoints")
    @ApiOperation(value = "设置默认视点")
    public ContentResultForm setDefaultViewpoints(@ApiParam(value = "图片ID") String id) {
        try {
            if (StringUtils.isBlank(id))
                return new ContentResultForm(false, null, "图片ID未填写!");
            Map<String, Object> result = iAgImageStore.setDefaultViewpoints(id);
            if (!(boolean) result.get("success"))
                return new ContentResultForm(false, null, result.get("msg").toString());
            return new ContentResultForm(true, null, result.get("msg").toString());
        } catch (Exception e) {
            return new ContentResultForm(true, null, e.getMessage());
        }
    }

    @PostMapping({"/convertImageTypes"})
    @ApiOperation("图片格式转换")
    public void convertImageTypes(@ApiParam(value = "图片类型") @RequestParam(defaultValue = "") String imageType, @ApiParam(value = "文件流") @RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (StringUtils.isBlank(imageType)) {
                imageType = "jpg";
            }
            FileEntity fileEntity = FileUtil.getFileEntity(file);
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), 1);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, (ImageObserver) null);

            ByteArrayOutputStream oss = new ByteArrayOutputStream();
            ImageIO.write(newBufferedImage, imageType, oss);
            InputStream input = new ByteArrayInputStream(oss.toByteArray());

            DataConversionUtil.setHeader(request, response, fileEntity.getFileName() + "." + imageType);
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileEntity.getFileName() + "." + imageType, "UTF-8"));
            byte[] buffer = new byte[1024];
            BufferedInputStream bis = null;
            try {
                bis = new BufferedInputStream(input);
                OutputStream os = response.getOutputStream();
                for (int i = bis.read(buffer); i != -1; i = bis.read(buffer)) {
                    os.write(buffer, 0, i);
                }
            } catch (Exception var26) {
                DataConversionUtil.setResponseJson(response, JSON.toJSONString(new ContentResultForm(false, null, "转换失败!")));
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException var25) {
                        var25.printStackTrace();
                    }
                }
            }
        } catch (Exception var28) {
            DataConversionUtil.setResponseJson(response, JSON.toJSONString(new ContentResultForm(false, null, "转换失败!")));
        }
    }


    /**
     * 图片格式转换
     *
     * @param file
     * @return
     */
    @PostMapping("/convertImageType")
    @ApiOperation(value = "图片格式转换")
    public ContentResultForm convertImageType(@ApiParam(value = "图片类型") @RequestParam(defaultValue = "") String imageType, @ApiParam(value = "文件流") @RequestParam("file") MultipartFile file) {
        try {
            if (StringUtils.isBlank(imageType)) {
                imageType = "jpg";
            }
            FileEntity fileEntity = FileUtil.getFileEntity(file);
            fileEntity.setFileType(imageType);
            String root = (FileUtil.getPath() + File.separator + CACHE_PATH).replaceAll("/", "\\\\");
            FileUtil.deleteFolder(root);

            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), 1);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, (ImageObserver) null);

            String path = root + file.getOriginalFilename().substring(0, (file.getOriginalFilename().lastIndexOf(".") + 1)) + imageType;

            FileUtil.createFileFolder(root);
            ImageIO.write(newBufferedImage, imageType, new File(path));

            String url = FileUtil.getUrl() + CACHE_PATH + file.getOriginalFilename().substring(0, (file.getOriginalFilename().lastIndexOf(".") + 1)) + imageType;
            fileEntity.setFileLink(url);

            return new ContentResultForm(true, fileEntity, "转换成功!");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }
}
