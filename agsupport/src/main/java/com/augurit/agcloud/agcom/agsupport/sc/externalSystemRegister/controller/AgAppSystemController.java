package com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.controller;

import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgAppSystem;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserThirdapp;
import com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service.IAppSystemAuthorizeService;
import com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service.IAppSystemService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author zhangmy
 * @Description: 外部系统Controller类
 * @date 2019-10-14 16:31
 */
@Api(value = "第三方应用系统注册", description = "第三方应用系统注册接口")
@RestController()
@RequestMapping("/agsupport/appsystem")
public class AgAppSystemController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgAppSystemController.class);
    public static final String systemIconPath = "app_system_icon/";

    @Autowired
    private IAppSystemService appSystemService;
    @Autowired
    private IAppSystemAuthorizeService appSystemAuthorizeService;
    @RequestMapping("/index.html")
    @ApiIgnore
    public ModelAndView index() {
        return new ModelAndView("agcloud/agcom/agsupport/appsystem/index");
    }

    @ApiOperation(value = "第三方应用系统数据信息分页查询", notes = "第三方应用系统数据信息分页查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appName", value = "系统名称", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "Page")
    })
    @GetMapping("/findAll")
    public ContentResultForm findAll(String appName, Page page) {
        ContentResultForm resultForm = new ContentResultForm(true);
        try {
            PageInfo<AgAppSystem> list = appSystemService.findAll(appName, page);
            resultForm.setContent(list);
        } catch (Exception e) {
            resultForm.setSuccess(false);
            resultForm.setMessage("查询数据出错");
        }
        return resultForm;
    }

    @ApiOperation(value = "第三方应用系统数据新增或修改", notes = "第三方应用系统数据新增或修改接口")
    @PostMapping("/save")
    public ContentResultForm save(AgAppSystem agAppSystem, HttpServletRequest request) {
        ContentResultForm resultForm = new ContentResultForm(true);
        try {
            String fileName = saveSystemIcon(request);
            if (StringUtils.isNotBlank(agAppSystem.getId())) {
                agAppSystem.setModifyTime(new Date());
                if (StringUtils.isNotBlank(fileName)){
                    // 如果修改了图标需要删除旧的图标文件
                    String iconAddr = agAppSystem.getIconAddr();
                    String filePath = UploadUtil.getUploadAbsolutePath() + systemIconPath;
                    File file = new File(filePath + iconAddr);
                    if (file.exists() && file.isFile()) {
                        file.delete();
                    }
                    agAppSystem.setIconAddr(fileName);
                }
                appSystemService.updateAppSystem(agAppSystem);
            } else {
                agAppSystem.setId(UUID.randomUUID().toString());
                agAppSystem.setCreateTime(new Date());
                // 新增默认未添加桌面
                agAppSystem.setAuthorizeStatus("0");
                if (StringUtils.isNotBlank(fileName)){
                    agAppSystem.setIconAddr(fileName);
                }
                appSystemService.insert(agAppSystem);
            }
            resultForm.setMessage("保存成功!");

        } catch (Exception e) {
            resultForm.setSuccess(false);
            resultForm.setMessage("保存出错!");
            LOGGER.error(e.getMessage());
        }
        return resultForm;
    }

    private String saveSystemIcon(HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();  //获取Content-Type
        String fileName = "";
        if (contentType.toLowerCase().startsWith("multipart/")) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                MultipartFile file = entity.getValue();
                if (file.getSize() > 0) {
                    String filePath = UploadUtil.getUploadAbsolutePath() + systemIconPath;
                    File systemIconPath = new File(filePath);
                    if (!systemIconPath.exists()) {
                        systemIconPath.mkdir();
                    }
                    fileName = file.getOriginalFilename();
                    String suffixName = fileName.substring(fileName.lastIndexOf("."));
                    fileName = UUID.randomUUID().toString() + suffixName;
                    File temp = new File(filePath + fileName);
                    file.transferTo(temp);
                }
            }
        }

        return fileName;
    }

    @ApiOperation(value = "第三方应用系统数据删除", notes = "第三方应用系统数据删除接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", required = true, value = "应用系统id,删除多个使用英文逗号拼接id", dataType = "string")
    })
    @DeleteMapping("/delete")
    public ContentResultForm delete(String ids) {
        ContentResultForm resultForm = new ContentResultForm(true);
        if (StringUtils.isBlank(ids)) {
            resultForm.setSuccess(false);
            resultForm.setMessage("id不能为空!");
        } else {
            String[] split = ids.split(",");
            List<String> deletelist = new ArrayList<>();
            List<String> nodeletelist = new ArrayList<>();
            for (String id : split){
                List<AgUserThirdapp> agUserThirdappList = appSystemAuthorizeService.findByAppId(id);
                if (agUserThirdappList.size()>0){
                    AgAppSystem agAppSystem = appSystemService.findById(id);
                    nodeletelist.add(agAppSystem.getAppName());
                }else {
                    deletelist.add(id);
                }
            }
            if (deletelist.size()>0){
                appSystemService.deleteByIds(deletelist);
            }
            resultForm.setContent(nodeletelist);
        }
        return resultForm;
    }

    @ApiOperation(value = "个性化桌面第三方应用系统查询所有数据", notes = "个性化桌面第三方应用系统查询所有数据接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appName", value = "系统名称", dataType = "string"),
    })
    @GetMapping("/find")
    public ContentResultForm find(String appName) {
        ContentResultForm resultForm = new ContentResultForm(true);
        List<AgAppSystem> ls = appSystemService.findAll(appName);
        List<AgAppSystem> hasAdd = new ArrayList<>();
        List<AgAppSystem> noAdd = new ArrayList<>();
        for (AgAppSystem agAppSystem : ls){
            String authorizeStatus = agAppSystem.getAuthorizeStatus();
            if ("0".equals(authorizeStatus)){
                noAdd.add(agAppSystem);
            }else {
                hasAdd.add(agAppSystem);
            }
        }
        HashMap map = new HashMap();
        map.put("hasAdd",hasAdd);
        map.put("noAdd",noAdd);
        resultForm.setContent(map);
        return resultForm;
    }

}
