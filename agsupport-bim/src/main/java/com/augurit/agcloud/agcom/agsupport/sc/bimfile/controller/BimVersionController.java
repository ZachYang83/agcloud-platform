package com.augurit.agcloud.agcom.agsupport.sc.bimfile.controller;

import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimVersion;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimVersionCompare;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.reslut.MultipartFileParam;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimVersion;
import com.augurit.agcloud.agcom.agsupport.util.AgDicUtils;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName BimVersionController
 * @Description TODO
 * @Author Administrator
 * @Date 2019-12-12 13:37
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/agsupport/bimversion")
@Api(value = "BIM版本管理",description = "BIM版本管理相关接口")
public class BimVersionController {
    @Autowired
    IBimVersion iBimVersion;

    @Value("${upload.filePath}")
    String uploadFilePath;

    @Autowired
    private AgDicUtils agDicUtils;
    /**
     * 样式配置页面
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/index.html")
    public ModelAndView index(Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupportbim/bimversion/index");
    }

    @ApiOperation(
            value = "分页获取数据",
            notes = "分页获取数据接口"
    )
    @ApiImplicitParams({@ApiImplicitParam(
            name = "agBimVersion",
            required = false,
            value = "BIM模型版本信息",
            dataType = "AgBimVersion"
    ), @ApiImplicitParam(
            name = "page",
            required = true,
            value = "分页参数:/agsupport/bimversion/getByOrKeyWords?page=1&rows=10&pkId=&keyword=",
            dataType = "Page"
    )})
    @RequestMapping(
            value = {"/getByOrKeyWords"},
            method = {RequestMethod.GET}
    )
    public ContentResultForm getByOrKeyWords(String pkId, String keyword, Page page) throws Exception {
        PageInfo<AgBimVersion> pageInfo = iBimVersion.getByOrKeyWords(pkId, keyword, page);
        EasyuiPageInfo<AgBimVersion> result = PageHelper.toEasyuiPageInfo(pageInfo);
        return new ContentResultForm(true, result);
    }

    @ApiOperation("获取数据字典")
    @GetMapping("/getDataDic")
    public ContentResultForm getDataDic() throws Exception {
        HashMap<String, List> result = new HashMap<>();
        List<AgDic> bim_change_type = agDicUtils.getAgDicByTypeCode("BIM_CHANGE_TYPE");
        List<AgDic> bim_version_state = agDicUtils.getAgDicByTypeCode("BIM_VERSION_STATE");
        result.put("bim_change_type", bim_change_type);
        result.put("bim_version_state", bim_version_state);
        return new ContentResultForm(true, result, "数据字典获取成功");
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加一条数据")
    @ApiParam(value = "BIM模型版本")
    public ContentResultForm add(AgBimVersion agBimVersion, MultipartFileParam param) {
        try {
            Integer f = iBimVersion.completeVersion(agBimVersion, param);
            if(f == 2){
                return new ContentResultForm(false, null, "版本号重复");
            }
            if(f == 0){
                iBimVersion.completeVersionWithExistVersion(agBimVersion);
            }
            agBimVersion.setChangeTime(new Date());
            boolean flag = iBimVersion.add(agBimVersion);
            if (!flag) {
                return new ContentResultForm(false, null, "添加失败!");
            }
            if(f == 1){
                iBimVersion.uploadFileByMappedByteBuffer(param);
            }
            return new ContentResultForm(true, agBimVersion.getId(), "添加成功!");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "根据ID删除一条数据")
    @ApiParam(value = "BIM模型版本ID")
    public ContentResultForm deleteById(@RequestParam(defaultValue = "") String paramId) {
        try {
            if (StringUtils.isBlank(paramId)) {
                return new ContentResultForm(false, null, "BIM模型ID不能为空!");
            }
            boolean flag = iBimVersion.deleteById(paramId);
            if (!flag) {
                return new ContentResultForm(false, null, "删除失败!");
            }
            return new ContentResultForm(true, null, "删除成功!");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    @DeleteMapping("/deleteMany")
    @ApiOperation(value = "删除更多信息")
    @ApiParam(value = "BIM模型版本ID")
    public ContentResultForm deleteMany(@RequestParam(defaultValue = "") String paramIds) {
        try {
            if (StringUtils.isBlank(paramIds)) {
                return new ContentResultForm(false, null, "BIM模型ID不能为空!");
            }
            List<String> idsList = Arrays.asList(paramIds.split(","));
            if (null == idsList || idsList.size() < 1) {
                return new ContentResultForm(false, null, "BIM模型ID不能为空!");
            }
            boolean flag = iBimVersion.deleteMany(idsList);
            if (!flag) {
                return new ContentResultForm(false, null, "删除失败!");
            }
            return new ContentResultForm(true, null, "删除成功!");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation(value = "编辑一条数据")
    @ApiParam(value = "BIM模型版本")
    public ContentResultForm update(AgBimVersion agBimVersion, MultipartFileParam param) {
        try {
            Integer f = iBimVersion.completeVersion(agBimVersion, param);
            if(f == 2){
                return new ContentResultForm(false, null, "版本号重复");
            }
            agBimVersion.setChangeTime(new Date());
            boolean flag = iBimVersion.update(agBimVersion);
            if (!flag) {
                return new ContentResultForm(false, null, "编辑失败!");
            }
            if(f == 1){
                iBimVersion.uploadFileByMappedByteBuffer(param);
            }
            return new ContentResultForm(true, null, "编辑成功!");
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }

    @GetMapping("/bimCompare")
    @ApiOperation(value = "模型比对")
    @ApiParam(value = "模型比对ids")
    public ContentResultForm bimCompare(String id1, String id2) {
        AgBimVersionCompare agBimVersionCompare = iBimVersion.bimCompare(id1, id2);
        if(null != agBimVersionCompare){
            return new ContentResultForm(true, agBimVersionCompare.getResultContent(), "比对成功");
        }else{
            return new ContentResultForm(false, null, "比对失败");
        }
    }

    @GetMapping("/getCompareData")
    @ApiOperation(value = "获取模型比对数据")
    @ApiParam(value = "模型比对ids")
    public ContentResultForm getCompareData(String id1, String id2) {
        AgBimVersionCompare agBimVersionCompare = iBimVersion.getCompareData(id1, id2);
        if(null != agBimVersionCompare){
            return new ContentResultForm(true, agBimVersionCompare.getResultContent(), "获取比对数据成功");
        }else{
            return new ContentResultForm(false, null, "获取比对数据失败");
        }
    }


    /**
     * 秒传判断，断点判断
     *
     * @param md5
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/checkFileMd5", method = RequestMethod.POST)
    @ApiOperation(value = "根据ID获取数据")
    @ApiParam(value = "BIM模型MD5")
    public ContentResultForm checkFileMd5(@RequestParam("uid") String uid, @RequestParam("md5") String md5) throws IOException {
        try {
            if (StringUtils.isBlank(md5)) {
                return new ContentResultForm(false, null, "BIM模型MD5不能为空!");
            }
            SecurityContext.getCurrentUserName();
            //判断是否上传
            List<AgBimVersion> agBimVersion = iBimVersion.getByMd5(md5);
            if (null == agBimVersion || agBimVersion.size()==0) {
                return new ContentResultForm(true, 101, "BIM模型未上传!");
            } else {
                if (FileUtil.checkFileOrFolder(uploadFilePath + agBimVersion.get(0).getBimPath())) {
                    return new ContentResultForm(true,  100, "文件已存在");
                }
                File confFile = new File((uploadFilePath + agBimVersion.get(0).getBimPath()+ ".conf"));
                if(confFile.exists()) {
                    byte[] completeList = FileUtils.readFileToByteArray(confFile);
                    List<String> missChunkList = new LinkedList<>();
                    for (int i = 0; i < completeList.length; i++) {
                        if (completeList[i] != Byte.MAX_VALUE) {
                            missChunkList.add(i + "");
                        }
                    }
                    return new ContentResultForm(true, missChunkList, "BIM模型已上传!");
                }else{
                    return new ContentResultForm(true, 101, "BIM模型未上传!");
                }
            }
        } catch (Exception e) {
            return new ContentResultForm(false, null, e.getMessage());
        }
    }


    @PostMapping("/save")
    @ApiOperation(value = "保存版本")
    @ApiParam(value = "版本对象")
    public ContentResultForm saveBimVersion(AgBimVersion bimVersion){
        bimVersion.setId(UUID.randomUUID().toString());
        iBimVersion.add(bimVersion);
        return new ContentResultForm(true,null);
    }


    @PutMapping("/update")
    @ApiOperation(value = "更新版本")
    @ApiParam(value = "版本对象")
    public ContentResultForm updateBimVersion(AgBimVersion bimVersion){
        iBimVersion.update(bimVersion);
        return new ContentResultForm(true,null);
    }


    @GetMapping("/findBimVersionCode/{bimId}")
    @ApiOperation(value = "获取最大版本")
    @ApiParam(value = "模型id")
    public ContentResultForm findBimVersionCode(@PathVariable("bimId") String bimId){
        String maxVersion = iBimVersion.findMaxVersion(bimId);
        if(StringUtils.isBlank(maxVersion)){
            maxVersion = "1.0";
        }
        maxVersion = new BigDecimal(maxVersion).add(BigDecimal.ONE).toString();
        return new ContentResultForm(true,maxVersion);
    }
}
