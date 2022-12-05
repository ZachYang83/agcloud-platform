package com.augurit.agcloud.agcom.agsupport.sc.statistics.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgDataOverview;
import com.augurit.agcloud.agcom.agsupport.domain.AgDataSubject;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.domain.AgSupDatasource;
import com.augurit.agcloud.agcom.agsupport.sc.agdatasource.service.IAgSupDatasource;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.statistics.service.IAgDataOverviewService;
import com.augurit.agcloud.agcom.agsupport.sc.statistics.service.IAgDataSubjectService;
import com.augurit.agcloud.agcom.agsupport.util.ExcelUtil;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * @author zhangmingyang
 * @Description: 资源数据注册
 * @date 2019-08-22 18:23
 */
@Api(value = "资源数据注册",description = "资源数据注册接口")
@RestController
@RequestMapping("/agsupport/dataOverview")
public class AgDataOverviewController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgDataOverviewController.class);
    @Autowired
    private IAgDataOverviewService agDataOverviewService;
    @Autowired
    private IAgDataSubjectService agDataSubjectService;
    @Autowired
    private IAgDic agDic;
    @Autowired
    private IAgSupDatasource agSupDatasource;

    @Value("${upload.filePath}")
    private String uploadPath;
    @RequestMapping("/index.html")
    @ApiIgnore
    public ModelAndView index(Model model){
        return new ModelAndView("agcloud/agcom/agsupport/statistics/index");
    }

    @ApiOperation(value = "资源数据信息查询",notes = "资源数据信息查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agDataOverview",value = "资源数据对象",dataType = "AgDataOverview"),
            @ApiImplicitParam(name = "page",value = "分页参数:page=1&rows=10",dataType = "Page")
    })
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public ContentResultForm findAll(AgDataOverview agDataOverview, Page page) throws Exception{
        ContentResultForm resultForm = new ContentResultForm(true);
        PageInfo all = agDataOverviewService.findAll(agDataOverview,page);
        resultForm.setContent(all);
        return resultForm;
    }

    @ApiOperation(value = "保存或者修改资源数据",notes = "保存或者修改资源数据接口")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResultForm save(AgDataOverview agDataOverview) throws Exception{
        if (StringUtils.isNotBlank(agDataOverview.getId())){
            agDataOverviewService.updateAgDataOverview(agDataOverview);
        }else {
            agDataOverview.setCreateTime(new Date());
            agDataOverview.setId(UUID.randomUUID().toString());
            agDataOverviewService.insert(agDataOverview);
        }
        return new ResultForm(true,"保存成功");
    }

    @ApiOperation(value = "删除资源数据",notes = "删除资源数据接口")
    @ApiImplicitParam(name = "ids",value = "资源数据ID,多个ID逗号分隔",dataType = "string")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public ResultForm delete(String ids) throws Exception{
        if (StringUtils.isNotBlank(ids)){
            String[] dataOverviewIds = ids.split(",");
            if (dataOverviewIds.length > 0){
                for (String id : dataOverviewIds){
                    agDataOverviewService.deleteById(id);
                }
            }
            return new ResultForm(true,"删除成功!");
        }
        return new ResultForm(true,"删除失败!");
    }

    @ApiOperation(value = "获取数据类型",notes = "获取数据类型接口")
    @RequestMapping(value = "/getDataType",method = RequestMethod.GET)
    public ContentResultForm getDataType(){
        ContentResultForm resultForm = new ContentResultForm(true);
        try {
            List<AgDic> agDicByTypeCode = agDic.getAgDicByTypeCode("A201");
            resultForm.setContent(agDicByTypeCode);
        } catch (Exception e) {
            resultForm.setMessage("查询出错");
            resultForm.setSuccess(false);
            e.printStackTrace();
        }
        return resultForm;
    }

    @ApiOperation(value = "获取数据源信息",notes = "获取数据源信息接口")
    @RequestMapping(value = "/getDataSource",method = RequestMethod.GET)
    public ContentResultForm getDataSource(){
        ContentResultForm resultForm = new ContentResultForm(true);
        try {
            List<AgDic> agDicByTypeCode = agDic.getAgDicByTypeCode("A202");
            resultForm.setContent(agDicByTypeCode);
        } catch (Exception e) {
            resultForm.setMessage("查询出错");
            resultForm.setSuccess(false);
            e.printStackTrace();
        }
        return resultForm;
    }

    @ApiOperation(value = "下载资源数据模板",notes = "下载资源数据模板接口")
    @RequestMapping(value = "/downExcel",method = RequestMethod.POST)
    public void downExcel(HttpServletResponse response){
        String fileName = "资源数据.xls";
        String excelPath = uploadPath+ File.separator+"temp"+ File.separator+"excel"+ File.separator+fileName;
        File excelFile = new File(excelPath);
        if (!excelFile.exists() || !excelFile.isFile()) {
            System.out.print("模板文件不存在!");
        }
        try {
            ExcelUtil.down(excelFile,fileName,response);
        } catch (Exception e) {
            System.out.print("下载失败!");
        }
    }
    @ApiOperation(value = "导入资源数据Excel文件",notes = "导入资源数据Excel文件接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subjectId" ,value = "主题Id",dataType = "string"),
            @ApiImplicitParam(name = "file" ,value = "Excel文件",dataType = "MultipartFile")
    })
    @RequestMapping(value = "/importExcelResource/{subjectId}",method = RequestMethod.POST)
    public ResultForm importExcelResource(@PathVariable("subjectId") String subjectId, MultipartFile file){
        ResultForm resultForm = new ResultForm(true);
        Map typeCn = getDataTypeCn();
        Map dataresourceTypeCn = getDataresourceTypeCn();
        List<AgDataOverview> dataOverviewList = new ArrayList<>();
        try {
            String fileName = file.getOriginalFilename();
            if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")){
                resultForm.setMessage("文件格式错误！请用模板文件!");
                resultForm.setSuccess(false);
                return resultForm;
            }
            List<List<String>> listRow = ExcelUtil.parseResourceExcel(fileName,file.getInputStream());
            for (List<String> list : listRow){
                AgDataOverview agDataOverview = new AgDataOverview();
                String dataTypeCn = list.get(1);
                String dataSize = list.get(2);
                String dataResourceTypeCn = list.get(3);
                Object dataType = typeCn.get(dataTypeCn);
                Object dataResourceType = dataresourceTypeCn.get(dataResourceTypeCn);
                if (dataType != null){
                    agDataOverview.setId(UUID.randomUUID().toString());
                    agDataOverview.setDataName(list.get(0));
                    agDataOverview.setDataType(dataType.toString());
                    agDataOverview.setDatasourceType(dataResourceType.toString());
                    if(dataSize.indexOf(".") > 0){
                        dataSize = dataSize.replaceAll("0+?$", "");
                        dataSize = dataSize.replaceAll("[.]$", "");
                    }
                    agDataOverview.setDataSize(Long.valueOf(dataSize));
                    agDataOverview.setCreateTime(new Date());
                    agDataOverview.setSubjectId(subjectId);
                    dataOverviewList.add(agDataOverview);
                }else {
                    continue;
                }
            }
            agDataOverviewService.insertBatch(dataOverviewList);
        }catch (Exception e){
            resultForm.setSuccess(false);
            resultForm.setMessage("导入文件错误!请检查是否按照模板导入!");
            e.printStackTrace();
        }
        return resultForm;
    }

    private Map getDataTypeCn(){
        List<AgDic> agDicList = null;
        try {
            agDicList = agDic.getAgDicByTypeCode("A201");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map map = new HashMap();
        for (AgDic agDic : agDicList){
            map.put(agDic.getName(),agDic.getCode());
        }
        return map;
    }

    private Map getDataresourceTypeCn(){
        List<AgDic> agDicList = null;
        try {
            agDicList = agDic.getAgDicByTypeCode("A202");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map map = new HashMap();
        for (AgDic agDic : agDicList){
            map.put(agDic.getName(),agDic.getCode());
        }
        return map;
    }
}
