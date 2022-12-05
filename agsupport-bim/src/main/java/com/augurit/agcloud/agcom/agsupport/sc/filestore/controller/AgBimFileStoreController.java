package com.augurit.agcloud.agcom.agsupport.sc.filestore.controller;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.agcom.agsupport.common.util.io.DataConversionUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgFileStore;
import com.augurit.agcloud.agcom.agsupport.sc.filestore.IAgBimFileStore;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 文件管理
 */
@RestController
@RequestMapping(value = "/agsupport/filestore")
@Api(value = "文件管理",description = "文件管理相关接口")
public class AgBimFileStoreController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgBimFileStoreController.class);



    @Autowired
    IAgBimFileStore iAgBimFileStore;


    /**
     * 样式配置页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/index.html")
    public ModelAndView index(Model model) {
        return new ModelAndView("agcloud/agcom/agsupportbim/filestore/index");
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
            AgFileStore agFileStore_out = iAgBimFileStore.getById(id);
            if (null == agFileStore_out) {
                return new ContentResultForm(true, "暂无数据!");
            }
            return new ContentResultForm(true, agFileStore_out);
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
            List<AgFileStore> agFileStoreList = iAgBimFileStore.getAll();
            if (null == agFileStoreList || agFileStoreList.size() == 0) {
                return new ContentResultForm(true, "暂无数据!");
            }
            return new ContentResultForm(true, agFileStoreList);
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
            boolean falg = iAgBimFileStore.deleteById(paramId);
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
            boolean falg = iAgBimFileStore.deleteMany(idsList);
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
     * @param agAgFileStore
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加一条信息")
    public ContentResultForm save(@ApiParam(value = "图片信息") AgFileStore agAgFileStore) {
        try {
            agAgFileStore.setId(UUID.randomUUID().toString());
            if (agAgFileStore.getUploadTime() == null)
                agAgFileStore.setUploadTime(new Date());
            boolean falg = iAgBimFileStore.save(agAgFileStore);
            if (!falg) {
                return new ContentResultForm(false, null, "添加失败");
            }
            return new ContentResultForm(true, agAgFileStore.getId(), "添加成功");
        } catch (Exception e) {
            return new ContentResultForm(false, e, e.getMessage());
        }
    }

    /**
     * 编辑一条信息
     *
     * @param agAgFileStore
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "编辑一条信息")
    public ContentResultForm update(@ApiParam(value = "图片信息") AgFileStore agAgFileStore) {
        try {
            boolean falg = iAgBimFileStore.update(agAgFileStore);
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
            name = "agAgFileStore",
            value = "样式对象信息",
            dataType = "AgStyle"
    ), @ApiImplicitParam(
            name = "page",
            required = true,
            value = "分页参数:/agsupport/io/filestore/getByDomainAndUsage?page=1&rows=10&name=",
            dataType = "Page"
    )})
    @RequestMapping(
            value = {"/getByDomainAndUsage"},
            method = {RequestMethod.GET}
    )
    public ContentResultForm getByDomainAndUsage(AgFileStore agAgFileStore, Page page) {
        PageInfo<AgFileStore> pageInfo = iAgBimFileStore.getByDomainOrUsage(agAgFileStore, page);
        EasyuiPageInfo<AgFileStore> result = PageHelper.toEasyuiPageInfo(pageInfo);
        return new ContentResultForm(true, result);
    }


    /**
     * 文件下载
     *
     * @param domain
     * @param usage
     * @param name
     * @param request
     * @param response
     */
    @GetMapping("/downloadFile")
    @ApiOperation(value = "文件下载")
    public void downloadFile(@ApiParam(value = "区域名称") @RequestParam(defaultValue = "") String domain,
                             @ApiParam(value = "使用者") @RequestParam(defaultValue = "") String usage,
                             @ApiParam(value = "名称") @RequestParam(defaultValue = "") String name,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        if (StringUtils.isBlank(domain) || StringUtils.isBlank(usage)) {
            DataConversionUtil.setResponseJson(response, JSON.toJSONString(
                    new ContentResultForm(false, null, "参数domain与usage不能为空!")));
            return;
        }
        AgFileStore agFileStore = new AgFileStore();
        agFileStore.setDomain(domain);
        agFileStore.setUsage(usage);
        agFileStore.setName(name);
        List<AgFileStore> agFileStoreList = iAgBimFileStore.downloadByDomainAndUsage(agFileStore);
        //文件不存在
        if (null == agFileStoreList ||
                agFileStoreList.size() == 0 ||
                StringUtils.isBlank(agFileStoreList.get(0).getPath()) ||
                StringUtils.isBlank(agFileStoreList.get(0).getExtension())) {
            DataConversionUtil.setResponseJson(response, JSON.toJSONString(
                    new ContentResultForm(false, null, "文件不存在!")));
            return;
        }
        StringBuilder filePath = new StringBuilder(FileUtil.getPath() + agFileStoreList.get(0).getPath());
        StringBuilder fileExtension = new StringBuilder(agFileStoreList.get(0).getExtension());
        StringBuilder fileName = new StringBuilder(agFileStoreList.get(0).getName());

        //文件夹
        if ((fileExtension.toString()).equals("gltf")) {
            try {
                boolean flag = FileUtil.CompressUtil.zip(filePath.toString());
                if (!flag) {
                    DataConversionUtil.setResponseJson(response, JSON.toJSONString(
                            new ContentResultForm(false, null, "下载失败!")));
                    return;
                } else {
                    filePath.append(".zip");
                    fileName.append(".zip");
                }
            } catch (Exception e) {
                DataConversionUtil.setResponseJson(response, JSON.toJSONString(
                        new ContentResultForm(false, null, e.getMessage())));
                return;
            }
        } else {
            fileName.append(".").append(fileExtension);
        }

        File file = new File(filePath.toString());
        if (file.exists()) {
            //设置下载参数
            DataConversionUtil.setHeader(request, response, fileName.toString());
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
                try {
                    if ((fileExtension.toString()).equals("gltf"))
                        System.out.println(FileUtil.deleteFolder(filePath.toString()));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            DataConversionUtil.setResponseJson(response, JSON.toJSONString(
                    new ContentResultForm(false, null, "下载失败")));
        }
    }
}
