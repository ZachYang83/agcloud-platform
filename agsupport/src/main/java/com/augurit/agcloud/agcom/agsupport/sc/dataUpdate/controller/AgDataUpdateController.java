package com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.controller;
import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.dao.AgDataUpdateDao;
import com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.service.AgDataUpdateService;
import com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.util.DateUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :15:07 2019/1/4
 * @Modified By:
 */
@Api(value = "数据更新接口",description = "数据更新接口")
@RestController
@RequestMapping("/agsupport/dataUpdate")
public class AgDataUpdateController {

    @Autowired
    private AgDataUpdateService agDataUpdateService;

    @Autowired
    private AgDataUpdateDao agDataUpdateDao;

    @RequestMapping("/index.do")
    public ModelAndView index(Model model) {
        String currentUserId = SecurityContext.getCurrentUserId();
        model.addAttribute("userId",currentUserId);
        return new ModelAndView("/agcom/dataUpdate/index");
    }


    @ApiOperation(value = "上传文件",notes = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file" ,value = "文件",dataType = "MultipartFile")
    })
    @RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
    public ContentResultForm uploadFile(@RequestParam MultipartFile file){
        String  outFilePath = "";
        String outFilePath_shp = "";
        String outFilePath_dbf = "";
        String messages = "";
        boolean success = false;
        String timestamp = String.valueOf(System.currentTimeMillis());
        JSONObject result = new JSONObject();
        try {
            String tempZipPath = System.getProperty("user.dir")+java.io.File.separator+"temp"+java.io.File.separator+"shape";
            File unZipFile = new File(tempZipPath);//如果解压目录不存在则创建
            if (!unZipFile.exists()) {
                unZipFile.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(file.getInputStream());//ZipInputStream用来读取压缩文件的输入流
            ZipEntry zipEntry;
            while((zipEntry = zin.getNextEntry()) != null){
                String zipEntryName = zipEntry.getName();
                zipEntryName = zipEntryName.replace("/", File.separator);
                outFilePath = tempZipPath + File.separator + timestamp + zipEntryName;
                String dirPath = outFilePath.substring(0,outFilePath.lastIndexOf(File.separator));
                File tempFileDir = new File(dirPath);
                if (!tempFileDir.exists()){//文件夹不存在则创建
                    tempFileDir.mkdirs();
                }
                if (outFilePath.endsWith("shp")){
                    outFilePath_shp = outFilePath;
                }else if (outFilePath.endsWith("dbf")){
                    outFilePath_dbf = outFilePath;
                }else if (outFilePath.endsWith("\\")){
                    continue;
                }
                OutputStream outputStream = new FileOutputStream(outFilePath);
                byte[] bytes = new byte[4096];
                int len;
                while ((len = zin.read(bytes)) > 0) {
                    outputStream.write(bytes, 0, len);
                }
                outputStream.close();
                //必须调用closeEntry()方法来读入下一项
                zin.closeEntry();
            }
            zin.closeEntry();
            List<String> title = new ArrayList<>();
            if (!"".equals(outFilePath_dbf) && !"".equals(outFilePath_shp)){
                title = agDataUpdateService.getTitledInfo(outFilePath_shp);
                success = true;
            }else if ("".equals(outFilePath_dbf)){
                messages +="缺少dbf文件 ";
            }else if ("".equals(outFilePath_shp)){
                messages +="缺少shap文件";
            }

            result.put("title",title);
            result.put("outFilePath_shp",outFilePath_shp.substring(outFilePath_shp.indexOf("temp"),outFilePath_shp.length()));
            return new ContentResultForm(success,result,messages);
        }catch (Exception e){
            return new ContentResultForm(true,result,e.getMessage());
        }
    }

    /**
     * 更新数据,先将数据写入临时表,表名： layerTable_temp ，增加字段：审核状态 state
     *
     * keepdata  true：以上传文件数据为准，删掉原来多余的数据；false：保留原来的数据
     * @param uploadFilePath 文件路径
     * @param keepData  true:删除原来多余数据，false:保留原来多余数据
     * @param contactOnlyShpField  表和shp文件关联标识（表的字段） //map<tableField,shpField>
     * @param contactField json格式  key是原表字段名称，value是shp文件字段名称 json  {"tableField":"shapField"}
     * @param dataSourceId
     * @param layerTable
     * @return
     */
    @ApiOperation(value = "更新数据",notes = "更新数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uploadFilePath" ,value = "文件相对路径",dataType = "String"),
            @ApiImplicitParam(name = "keepData" ,value = "是否保留原来数据",dataType = "boolean"),
            @ApiImplicitParam(name = "contactOnlyShpField" ,value = "表和shap文件关联字段",dataType = "String"),
            @ApiImplicitParam(name = "contactField" ,value = "表和shap文件对应映射字段",dataType = "String"),
            @ApiImplicitParam(name = "dataSourceId" ,value = "数据源id",dataType = "String"),
            @ApiImplicitParam(name = "layerTable" ,value = "表名",dataType = "String"),
            @ApiImplicitParam(name = "userName" ,value = "用户名",dataType = "String")
    })
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ContentResultForm update(String uploadFilePath, boolean keepData, String contactOnlyShpField, String contactField, String dataSourceId, String layerTable,String userName) {
        try {
            //创建临时表，增加审核字段 state
            if (!agDataUpdateDao.isExistTable(dataSourceId,layerTable+"_TEMP")){
                agDataUpdateDao.createTempTable(dataSourceId,layerTable);
                agDataUpdateDao.addColumn(dataSourceId,layerTable);
            }else {
                //清空临时表数据
                //agDataUpdateDao.clearTable(dataSourceId,layerTable+"_TEMP");
            }
            Map<String, String> contactFieldMap = new HashMap<>();
            contactFieldMap = JSONObject.fromObject(contactField);
            String filePath = System.getProperty("user.dir") + File.separator + uploadFilePath;
            String updateResult = agDataUpdateService.readSHP_DBF(filePath, keepData, contactOnlyShpField, contactFieldMap, dataSourceId, layerTable+"_TEMP");
            agDataUpdateDao.saveUserNameAndTime(dataSourceId,layerTable+"_TEMP",userName, DateUtil.formatDate(DateUtil.FORMAT2,new Date()));
            JSONObject json = JSONObject.fromObject(updateResult);
            json.put("filePath",filePath);
            return new ContentResultForm(true,json.toString(),"更新数据");
        } catch (Exception e) {
            return new ContentResultForm(false,"","更新数据失败");
        }
    }

    /**
     * 更新前后数据对比
     * @param dataSourceId
     * @param layerTable
     * @param fieldName
     * @param fieldValue
     * @return
     */
    @ApiOperation(value = "更新前后数据对比",notes = "更新前后数据对比")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId" ,value = "数据源id",dataType = "String"),
            @ApiImplicitParam(name = "layerTable" ,value = "表名",dataType = "String"),
            @ApiImplicitParam(name = "fieldName" ,value = "字段名称",dataType = "String"),
            @ApiImplicitParam(name = "fieldValue" ,value = "字段值",dataType = "String")
    })
    @RequestMapping(value = "/compareData",method = RequestMethod.POST)
    public ContentResultForm compareData(String dataSourceId, String layerTable, String fieldName, String fieldValue) {
        try {
            Map<String, Object> result = agDataUpdateDao.compareData(dataSourceId, layerTable, fieldName, fieldValue);
            return new ContentResultForm(true,result,"更新前后数据对比");
        } catch (Exception e) {
            return new ContentResultForm(false,"","更新前后数据对比失败");
        }
    }


    /**
     * 保存对比修正后的数据
     * @param dataSourceId
     * @param layerTable
     * @param fieldName
     * @param fieldValue
     * @param updateJson
     * @return
     */
    @ApiOperation(value = "保存对比修正后的数据",notes = "保存对比修正后的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId" ,value = "数据源id",dataType = "String"),
            @ApiImplicitParam(name = "layerTable" ,value = "表名",dataType = "String"),
            @ApiImplicitParam(name = "fieldName" ,value = "主键字段名称",dataType = "String"),
            @ApiImplicitParam(name = "fieldValue" ,value = "主键字段值",dataType = "String"),
            @ApiImplicitParam(name = "updateJson" ,value = "更新数据，json格式字符串格式，key为字段名称，value为字段值",dataType = "String")
    })
    @RequestMapping(value = "/saveEditData",method = RequestMethod.POST)
    public ResultForm saveEditData(String dataSourceId, String layerTable, String fieldName, String fieldValue, String updateJson) {
        try {
            agDataUpdateDao.saveEditData(dataSourceId, layerTable, fieldName, fieldValue, updateJson);
            return new ResultForm(true, "保存成功！");
        } catch (Exception e) {
            return new ResultForm(false, "保存失败！");
        }
    }

    /**
     * 提交审核的数据  state  1：提交审核(待审核)，2：审核通过，3：审核不通过
     * @param dataSourceId
     * @param layerTable
     * @param fieldName
     * @param fieldValues
     * @param auditTypes  数据修改类型
     * @return
     */
    @ApiOperation(value = "提交审核的数据",notes = "提交审核的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId" ,value = "数据源id",dataType = "String"),
            @ApiImplicitParam(name = "layerTable" ,value = "表名",dataType = "String"),
            @ApiImplicitParam(name = "fieldName" ,value = "主键字段名称",dataType = "String"),
            @ApiImplicitParam(name = "fieldValue" ,value = "主键字段值",dataType = "String"),
            @ApiImplicitParam(name = "updateJson" ,value = "更新数据，json格式字符串格式，key为字段名称，value为字段值",dataType = "String")
    })
    @RequestMapping(value = "/submitCheck",method = RequestMethod.POST)
    public ResultForm saveEditData(String dataSourceId, String layerTable, String fieldName, String fieldValues,String auditTypes,int param) {
        JSONObject json = new JSONObject();
        try {
            int len = agDataUpdateDao.submitCheck(dataSourceId,layerTable+"_TEMP",fieldName,fieldValues,auditTypes);
            return new ResultForm(true, "提交审核的数据成功！");
        } catch (Exception e) {
            return new ResultForm(false, "提交审核的数据失败！");
        }
    }

    @ApiOperation(value = "数据预览",notes = "数据预览")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uploadFilePath" ,value = "文件相对路径",dataType = "String")
    })
    @RequestMapping(value = "/previewFile",method = RequestMethod.POST)
    public ContentResultForm previewFile(String uploadFilePath) {
        try {
            String filePath = System.getProperty("user.dir") + File.separator + uploadFilePath;
            List<String> list = agDataUpdateService.readSHP(filePath);
            return new ContentResultForm(true,list,"数据预览");
        } catch (Exception e) {
            return new ContentResultForm(false,"","获取预览数据失败");
        }
    }
}
