package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.common.exception.ExceptionEnum;
import com.augurit.agcloud.agcom.agsupport.common.util.io.DataConversionUtil;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheck;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckExample;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimCheckCustomMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgBimCheckMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.util.FileEntity;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.domain.ExcelResponseDomain;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.IBimCheckService;
import com.augurit.agcloud.agcom.agsupport.util.ExcelUtil;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.List;
import java.util.*;

/**
 * @Author: libc
 * @Description: BIM审查-业务实现类
 * @Date: 2020/9/10 9:33
 * @Version: 1.0
 */
@Service
@Transactional
public class BimCheckServiceImpl implements IBimCheckService {

    private static Logger logger = LoggerFactory.getLogger(BimCheckServiceImpl.class);

    // pdf模板目录路径
    private String templateBimPdfPath;

    // agcloud文件输出默认根路径
    @Value("${upload.filePath}")
    private String baseOutPath;

    // 生成pdf报表存放路径
    private String bimPdfOutPath;

    private static final Integer FIRST_PAGE_ROW = 13; // table表格第一页展示行数
    private static final Integer TOW_PAGE_ROW = 18; // table表格单页（每页）展示行数

    private static final String BIM_CHECK_JSON_FILE_NAME = "ReviewDB"; //BIM审查，jsonFile名称

    @Autowired
    private AgBimCheckMapper agBimCheckMapper;
    @Autowired
    private AgBimCheckCustomMapper agBimCheckCustomMapper;

    @PostConstruct
    private void init() {
        // 构造之后初始化bimPdfOutPath的值， 否则baseOutPath为null
        bimPdfOutPath = baseOutPath + "bimcheck/report/file";
        // 初始化模板路径
        ClassPathResource classPathResource = new ClassPathResource("com/augurit/agcloud/agcom/agsupport/fileTemplate/pdf/bimCheckTemplate.pdf");
        templateBimPdfPath = classPathResource.getPath();
    }

    /**
     * @param cityName:审查城市名称（报告名称）
     * @return 生成的pdf文件url
     * @Author: libc
     * @Date: 2020/9/11 20:49
     * @tips: 生成pdf文件，并返回pdf文件的请求url
     * @Param projectName:项目名称
     * @Param designCompany:设计单位
     * @Param checkResultList:BIM审查结果列表
     */
    public String preview(String cityName, String projectName, String designCompany, List<String> checkResultList) {
        // 封装pdf数据源map
        Map<String, Object> map = createDataMapForPdf(cityName, projectName, designCompany, checkResultList);

        // 生成pdf
        File newPdfFile = creatPdf(map);

        return newPdfFile.getAbsolutePath();
    }

    @Override
    public List<ExcelResponseDomain> statisticsExcel(MultipartFile excelFile) {
        List<ExcelResponseDomain> returnList = new ArrayList<>();
        try {
            List<Object[]> list = ExcelUtil.getExcelData(excelFile.getOriginalFilename(), excelFile.getInputStream());
            //只做一次循环，返回所有数据，直接设置好对象
            ExcelResponseDomain entityH1 = new ExcelResponseDomain();
            ExcelResponseDomain entityH2 = new ExcelResponseDomain();
            ExcelResponseDomain entityH3 = new ExcelResponseDomain();
            ExcelResponseDomain entityH4 = new ExcelResponseDomain();
            ExcelResponseDomain entityH5 = new ExcelResponseDomain();
            ExcelResponseDomain entityL1 = new ExcelResponseDomain();
            ExcelResponseDomain entityL2 = new ExcelResponseDomain();
            ExcelResponseDomain entityL3 = new ExcelResponseDomain();
            ExcelResponseDomain entityL4 = new ExcelResponseDomain();

            //设置key
            entityH1.setKey("H1");
            entityH2.setKey("H2");
            entityH3.setKey("H3");
            entityH4.setKey("H4");
            entityH5.setKey("H5");
            entityL1.setKey("L1");
            entityL2.setKey("L2");
            entityL3.setKey("L3");
            entityL4.setKey("L4");

            //初始化value
            List<String> listH1 = new ArrayList<>();
            List<String> listH2 = new ArrayList<>();
            List<String> listH3 = new ArrayList<>();
            List<String> listH4 = new ArrayList<>();
            List<String> listH5 = new ArrayList<>();
            List<String> listL1 = new ArrayList<>();
            List<String> listL2 = new ArrayList<>();
            List<String> listL3 = new ArrayList<>();
            List<String> listL4 = new ArrayList<>();

            //模板从第6行开始读，读取到第一个列包含“需整改处理房间”文字，就读取到最后
            for (int i = 5; i < list.size(); i++) {
                Object[] excelCell = list.get(i);
                //房间编号(0),H(1),H1(2),H2(3),H3(4),H4(5),H5(6),L1(7),L2(8),L3(9),L4(10),最大偏差(11),极差(12),极差(13)
                listH1.add((String) excelCell[2]);
                listH2.add((String) excelCell[3]);
                listH3.add((String) excelCell[4]);
                listH4.add((String) excelCell[5]);
                listH5.add((String) excelCell[6]);
                listL1.add((String) excelCell[7]);
                listL2.add((String) excelCell[8]);
                listL3.add((String) excelCell[9]);
                listL4.add((String) excelCell[10]);
            }

            //赋值value
            entityH1.setValue(listH1);
            entityH2.setValue(listH2);
            entityH3.setValue(listH3);
            entityH4.setValue(listH4);
            entityH5.setValue(listH5);
            entityL1.setValue(listL1);
            entityL2.setValue(listL2);
            entityL3.setValue(listL3);
            entityL4.setValue(listL4);
            //设置返回值
            returnList.add(entityH1);
            returnList.add(entityH2);
            returnList.add(entityH3);
            returnList.add(entityH4);
            returnList.add(entityH5);
            returnList.add(entityL1);
            returnList.add(entityL2);
            returnList.add(entityL3);
            returnList.add(entityL4);
        } catch (IOException e) {
            logger.info("-----解析excel错误---" + e.getMessage());
        }
        return returnList;
    }

    /**
     * @param filePath
     * @param request
     * @param response
     * @return
     * @Author: libc
     * @Date: 2020/10/23 17:37
     * @tips: pdf报告下载
     */
    public void download(String filePath, HttpServletRequest request, HttpServletResponse response) {
        logger.info("=========BIM审查-pdf报告下载开始===========");
        FileInputStream inputStream = null;
        OutputStream out = null;
        try {
            if (StringUtils.isEmpty(filePath)) {
                throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
            }
            // URL解码
            filePath = URLDecoder.decode(filePath, "utf-8");
            File file = new File(filePath);
            String fileName = file.getName();
            inputStream = new FileInputStream(file);
            out = response.getOutputStream();
            // 设置头信息
            DataConversionUtil.setHeader(request, response, fileName);
            response.setHeader("Content-Range", "bytes 0-0,-1");

            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = inputStream.read(buffer)) != -1){
                out.write(buffer,0,read);
            }
            out.flush();
//                inputStream.read(by);
//            FileUtil.writerFile(by, fileName, true, response);
            logger.info("=========BIM审查-pdf报告下载成功===========");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("=========BIM审查-pdf报告下载失败===========");
            throw new AgCloudException(ExceptionEnum.FILE_WRITER_ERROR);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("=========BIM审查-pdf报告下载失败===========");
                    throw new AgCloudException(ExceptionEnum.FILE_WRITER_ERROR);
                }
            }

        }

    }

    /**
     * @param cityName:审查城市名称（报告名称）
     * @return 封装好的数据源map
     * @Author: libc
     * @Date: 2020/9/11 20:57
     * @tips: 封装pdf数据源map
     * @Param projectName:项目名称
     * @Param designCompany:设计单位
     * @Param checkResultList:BIM审查结果列表
     */
    private Map<String, Object> createDataMapForPdf(String cityName, String projectName, String designCompany, List<String> checkResultList) {
        Map<String, Object> map = new HashMap<String, Object>();


        // pdf模板表格之外的form数据 （对应paf模板中form的key）
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("cityName", cityName);
        dataMap.put("projectName", projectName);
        dataMap.put("designCompany", designCompany);

        // 封装table 列表数据
        List<List<String>> tableList = new ArrayList<List<String>>();
        int index = 1; // table 中第一行的序号
        for (String s : checkResultList) {
            // list2代表table中一行数据
            List<String> tableRowList = new ArrayList<String>();
            tableRowList.add(String.valueOf(index));
            tableRowList.add(s);
            tableList.add(tableRowList);
            index++;
        }


        map.put("dataMap", dataMap);
        map.put("tableList", tableList);
        return map;
    }

    /**
     * @param map: 封装好的数据源
     * @return 生成的pdf文件
     * @Author: libc
     * @Date: 2020/9/11 20:59
     * @tips: 生成pdf报表文件
     */
    private File creatPdf(Map<String, Object> map) {
        File newFile = null;
        logger.info("=========BIM审查-开始生成pdf===========");
        // 利用模板生成pdf
        // 模板路径
//        String templatePath = "D:\\work\\resourceManager\\agcloud-platform\\agsupport-rest\\src\\main\\resources\\com\\augurit\\agcloud\\agcom\\agsupport\\fileTemplate\\pdf\\ceshi2.pdf";
//        String templatePath = TEMPLATE_BIM_PDF_PATH;
        System.out.println(templateBimPdfPath);
        // 生成的pdf 路径 如：c:/home/agsupportFiles/bimcheck/report/file/uuid.pdf
        String newPDFPath = new StringBuilder(bimPdfOutPath)
                .append(File.separator)
                .append(UUID.randomUUID())
                .append(".pdf").toString();
        PdfReader reader;
        OutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            BaseFont bf = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            // 雅黑粗体（标题字体格式）
            BaseFont bold =
                    BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
//            BaseFont bold = BaseFont.createFont("C:/WINDOWS/Fonts/MSYHBD.TTC,1",
//                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);// 第三方字体会导致pdf文件过大 10M以上
//            out = response.getOutputStream();// 输出流
            newFile = new File(newPDFPath);
            // 获取上级文件夹file对象
            File parentFile = newFile.getParentFile();
            // 创建文件夹目录
            if (!parentFile.exists()) {
                parentFile.mkdirs(); // 能创建多级目录
            }
            if (!newFile.exists()) {
                newFile.createNewFile(); // 有路径才能创建文件
            }

            out = new FileOutputStream(newPDFPath);
            reader = new PdfReader(templateBimPdfPath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            stamper.setFormFlattening(true); //不可编辑
            AcroFields form = stamper.getAcroFields();
            // 文字类的内容处理
            Map<String, String> dataMap = (Map<String, String>) map.get("dataMap");
            form.addSubstitutionFont(bf);
            for (String key : dataMap.keySet()) {
                String value = dataMap.get(key);
//                form.setField(key, value);
                if ("cityName".equals(key)) {
                    // 标题加粗 (这个没有生效，暂时没找到解决方案)
                    form.setGenerateAppearances(true);
                    form.setFieldProperty(key, "textfont", bold, null);
                    form.setField(key, value);
                } else {
                    form.setField(key, value);
                }
            }
            // 表格类
            List<List<String>> lists = (List<List<String>>) map.get("tableList");
            Rectangle signRect = form.getFieldPositions("tableList").get(0).position;
            //表格位置
            int column = lists.get(0).size();
            int row = lists.size();
            PdfPTable table = new PdfPTable(column);
            float tatalWidth = signRect.getRight() - signRect.getLeft() - 1;
            int size = lists.get(0).size();
            float width[] = new float[size];
            // 设置表格中一行 多列的宽度
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    // 第一列
                    width[i] = 40.2f;
                } else {
                    width[i] = (tatalWidth - 40.2f) / (size - 1);
                }
            }
            table.setTotalWidth(width);
            table.setLockedWidth(true);
            table.setKeepTogether(true);
            table.setSplitLate(false);
            table.setSplitRows(true);
            Font FontProve = new Font(bf, 12, 0);
            //表格数据填写
            for (int i = 0; i < row; i++) {
                List<String> list = lists.get(i);
                for (int j = 0; j < column; j++) {
                    Paragraph paragraph = new Paragraph(String.valueOf(list.get(j)), FontProve);
//                        Paragraph paragraph = new Paragraph(String.valueOf(list.get(j)));
                    PdfPCell cell = new PdfPCell(paragraph);
//                        cell.setBorderWidth(1);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    if (j == 0) { //序号 水平居中
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    }
                    cell.setLeading(0, (float) 1.4);
                    table.addCell(cell);
                }
            }


            //获取总行数
            int totalRow = table.getRows().size();
            //计算需要分页的总页数
            int totalpage = caculatePageable(totalRow);
            Document document = new Document();
            document.open();
            if (totalpage == 1) {
                //获table页面
                PdfContentByte under = stamper.getOverContent(1);
                //添加table
                table.writeSelectedRows(0, -1, signRect.getLeft(), signRect.getTop(), under);

            } else {
                //目前模板中暂时解决分页方案 有几页就动态增加几页的空白模板
                for (int i = 1; i <= totalpage; i++) {
                    document.newPage();
                    PdfContentByte under = stamper.getOverContent(i);
                    if (i == 1) {
                        //第一页显示13条
                        table.writeSelectedRows(0, FIRST_PAGE_ROW, signRect.getLeft(), signRect.getTop(), under);
                    }
                    //空白模板每页显示18条
                    else {
                        table.writeSelectedRows(FIRST_PAGE_ROW + TOW_PAGE_ROW * (i - 2), FIRST_PAGE_ROW + TOW_PAGE_ROW * (i - 1), 86, 730, under);
                    }
                }
            }

            document.close();

            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.close();
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            int pageNum = totalpage;
            for (int i = 1; i <= pageNum; i++) {
                PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), i);
                copy.addPage(importPage);
            }
            doc.close();


            logger.info("=========BIM审查-生成pdf成功===========");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("=========BIM审查-生成pdf失败===========");
            throw new AgCloudException(ExceptionEnum.INVALID_FILE_TYPE);
        }
        return newFile;
    }


    /**
     * @param totalRow: 总记录数
     * @return 总页数
     * @Author: libc
     * @Date: 2020/9/11 17:15
     * @tips: 计算分页
     */
    private int caculatePageable(int totalRow) {
        int page = (totalRow - FIRST_PAGE_ROW) % TOW_PAGE_ROW == 0 ? (totalRow - FIRST_PAGE_ROW) / TOW_PAGE_ROW + 1 : (totalRow - FIRST_PAGE_ROW) / TOW_PAGE_ROW + 2;
        return totalRow < FIRST_PAGE_ROW ? 1 : page;

    }

    @Override
    @Transactional
    public void add(AgBimCheck bimCheck) {
        bimCheck.setId(UUID.randomUUID().toString());
        bimCheck.setCreateTime(new Date());
        agBimCheckMapper.insert(bimCheck);
    }

    @Override
    @Transactional
    public void delete(String ids) {
        if(!StringUtils.isEmpty(ids)){
            String[] idsArr = ids.split(",");
            if(idsArr != null && idsArr.length > 0){
                for(String id: idsArr){
                    agBimCheckMapper.deleteByPrimaryKey(id);
                }
            }
        }
    }

    @Override
    @Transactional
    public void update(AgBimCheck bimCheck) {
        AgBimCheck agBimCheck = agBimCheckMapper.selectByPrimaryKey(bimCheck.getId());
        if(agBimCheck != null){
            //修改时间
            bimCheck.setModifyTime(new Date());
            //创建时间不修改
            bimCheck.setCreateTime(agBimCheck.getCreateTime());
            agBimCheckMapper.updateByPrimaryKeySelective(bimCheck);
        }
    }

    @Override
    public PageInfo<AgBimCheck> find(AgBimCheck bimCheck, Page page) {
        AgBimCheckExample example = new AgBimCheckExample();
        //排序
//        example.setOrderByClause(" create_time desc");
        AgBimCheckExample.Criteria criteria = example.createCriteria();
        //判断参数，赋值
        if(!StringUtils.isEmpty(bimCheck.getType())){
            List<String> types = new ArrayList<>();
            for(String type: bimCheck.getType().split(",")){
                types.add(type);
            }
            criteria.andTypeIn(types);
        }
        if(!StringUtils.isEmpty(bimCheck.getClassificationType())){
            List<String> types = new ArrayList<>();
            for(String type: bimCheck.getClassificationType().split(",")){
                types.add(type);
            }
            criteria.andClassificationTypeIn(types);
        }
        if(!StringUtils.isEmpty(bimCheck.getStatus())){
            criteria.andStatusEqualTo(bimCheck.getStatus());
        }
        if(!StringUtils.isEmpty(bimCheck.getSourceId())){
            criteria.andSourceIdEqualTo(bimCheck.getSourceId());
        }
        if(!StringUtils.isEmpty(bimCheck.getName())){
            criteria.andNameLike("%" + bimCheck.getName() + "%");
        }
        PageHelper.startPage(page);
        List<AgBimCheck> agBimChecks = agBimCheckMapper.selectByExample(example);
        return new PageInfo<>(agBimChecks);
    }

    @Override
    public Map<String, Object> statisticsBimCheck(String sourceId) {
        Map<String, Object> resultMap = new HashMap<>();
        //规范类型
        List<AgBimCheck> classificationTypes = agBimCheckCustomMapper.groupByKey("classification_type", sourceId);
        List<Map<String, Object>> resultClassificationTypes = new ArrayList<>();
        if(classificationTypes != null && classificationTypes.size() > 0){
            for(AgBimCheck bimCheck: classificationTypes){
                Map<String, Object> map = new HashMap<>();
                String classificationType = bimCheck.getClassificationType();
                map.put("classificationType", classificationType);
                map.put("count", bimCheck.getName());
                //获取该规范类型下的所有数据，对属性name（条文编号）进行树形结构排列
                AgBimCheckExample example = new AgBimCheckExample();
                example.createCriteria().andClassificationTypeEqualTo(classificationType);
                List<AgBimCheck> agBimChecks = agBimCheckMapper.selectByExample(example);
                if(agBimChecks != null && agBimChecks.size() > 0){
                    HashMap<String, Object> root = new HashMap<>();
                    ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
                    root.put("content", arrayList);
                    for(AgBimCheck check : agBimChecks){
                        addPath(root, check.getName(), check.getType(), check.getBasis());
                    }
                    arrayList = (ArrayList<HashMap<String, Object>>)root.get("content");
                    dealTreeResult(arrayList);
                    map.put("tree", arrayList);
                }
                resultClassificationTypes.add(map);
            }
        }
        resultMap.put("classificationTypes", resultClassificationTypes);
        //规范分类
        List<AgBimCheck> types = agBimCheckCustomMapper.groupByKey("type", sourceId);
        List<Map<String, String>> resultTypes = new ArrayList<>();
        if(types != null && types.size() > 0){
            for(AgBimCheck bimCheck: types){
                Map<String, String> map = new HashMap<>();
                map.put("type", bimCheck.getType());
                map.put("count", bimCheck.getName());
                resultTypes.add(map);
            }
        }
        resultMap.put("types", resultTypes);
        return resultMap;
    }

    private void dealTreeResult(ArrayList<HashMap<String, Object>> list){
        if(list != null && list.size() > 0){
            for(HashMap<String, Object> map : list){
                ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>)map.get("content");
                //都去掉treeCheck值
                map.remove("treeCheck");
                if(arrayList != null && arrayList.size() > 0){
                    //如果不是最后一层，去掉
                    map.remove("type");
                    //不是最后一层，默认吧title的值赋值成name
                    map.put("title", map.get("name"));
                    dealTreeResult(arrayList);
                }
            }
        }
    }

    /**
     * 转换成tree工具
     * @param root map结婚，必须设置一个content值
     * @param path 所有的name的属性，例如：5.6.1
     * @param type 规范分类
     * @param basis 条文描述
     * @return
     */
    private static void addPath(HashMap<String, Object> root, String path, String type, String basis) {
        String url = "";
        String[] pathArr = path.split("\\.");
        for (String name : pathArr) {
            url +=  name + ".";
            boolean flag = true;
            for (HashMap<String, Object> node : (ArrayList<HashMap<String, Object>>) root.get("content")) {
                if (node.get("treeCheck").equals(name)) {
                    root = node;
                    flag = false;
                    break;
                }
            }
            if (flag) {
                HashMap<String, Object> new_node = new HashMap<>();
                new_node.put("treeCheck", name);
                new_node.put("type", type);
                new_node.put("title", basis);
                new_node.put("name", url.substring(0, url.length() - 1));
                new_node.put("content", new ArrayList<HashMap<String, Object>>());
                ((ArrayList<HashMap<String, Object>>) root.get("content")).add(new_node);
                root = new_node;
            }
        }
    }



    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/03 14:34
     * @tips:  条文对应关系
     *                                  { originFile: "住宅设计规范", value: 1 },
     *                                  { originFile: "建筑抗震设计规范", value: 2 },
     *                                  { originFile: "建筑设计防火规范", value: 3 },
     *                                  { originFile: "建筑防排烟系统技术标准", value: 4 },
     *                                  { originFile: "人民防空地下室设计规范", value: 5 },
     *                                  { originFile: "公共建筑节能设计标准", value: 6 },
     *                                  { originFile: "中小学校设计规范", value: 7 },
     *                                  { originFile: "住宅建筑规范", value: 8 },
     *                                  { originFile: "消防给水及消防栓系统计算规范", value: 9 },
     * @param
     * @return
     */
    private String getClassificationType(String originFile){
        String classificationType = null;
        if(StringUtils.isEmpty(originFile)){
            return classificationType;
        }
        if(originFile.contains("住宅设计规范")){
            return "1";
        }
        if(originFile.contains("建筑抗震设计规范")){
            return "2";
        }
        if(originFile.contains("建筑设计防火规范")){
            return "3";
        }
        if(originFile.contains("建筑防排烟系统技术标准")){
            return "4";
        }
        if(originFile.contains("人民防空地下室设计规范")){
            return "5";
        }
        if(originFile.contains("公共建筑节能设计标准")){
            return "6";
        }
        if(originFile.contains("中小学校设计规范")){
            return "7";
        }
        if(originFile.contains("住宅建筑规范")){
            return "8";
        }
        if(originFile.contains("消防给水及消防栓系统计算规范")){
            return "9";
        }
        return classificationType;
    }

    @Override
    @Transactional
    public void addAgBimCheckFromJsonFile(String sourceId, List<FileEntity> fileEntities) {
        if(fileEntities != null && fileEntities.size() > 0){
            try{
                for(FileEntity entity : fileEntities){
                    String jsonStr = null;
                    if(entity.getFile() == null){
                        continue;
                    }
                    if(entity.getFile().getPath().contains(BIM_CHECK_JSON_FILE_NAME) || entity.getFile().getName().contains(BIM_CHECK_JSON_FILE_NAME)){
                        jsonStr = FileUtils.readFileToString(entity.getFile(), "UTF-8");
                        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
                        if(jsonArray != null){
                            int size = jsonArray.size();
                            for(int i = 0; i < size; i++){
                                /**
                                 classificationType 这个要根据 OriginFile中文《》里面的内容来判断
                                 status根据 Elements来判断  Elements为空就是true  不为空就是·1false
                                 basis = CheckContent.Description
                                 result = Reason
                                 name = Chapter,
                                 Elements = Elements
                                 */
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("Id");
                                String check = jsonObject.getString("Check");
                                String profession = jsonObject.getString("Profession");
                                String originFile = jsonObject.getString("OriginFile");
                                String itemName = jsonObject.getString("ItemName");
                                String chapter = jsonObject.getString("Chapter");
                                String keyText = jsonObject.getString("KeyText");
                                JSONObject checkContent = jsonObject.getJSONObject("CheckContent");
                                boolean isMatch = jsonObject.getBoolean("IsMatch");
                                String reason = jsonObject.getString("Reason");
                                JSONArray elements  = jsonObject.getJSONArray("Elements");

                                AgBimCheck bimCheck = new AgBimCheck();
                                bimCheck.setSourceId(sourceId);
                                bimCheck.setClassificationType(getClassificationType(originFile));
                                bimCheck.setStatus((elements == null || elements.size() == 0) ? "0" : "1");
                                bimCheck.setBasis(checkContent != null ? checkContent.getString("Description") : null);
                                bimCheck.setResult(reason);
                                bimCheck.setName(chapter);
                                bimCheck.setElements(elements != null ? elements.toJSONString(): null);
                                bimCheck.setType("1");
                                bimCheck.setArticleId(id);
                                this.add(bimCheck);
                            }
                        }
                    }
                }
            }catch (Exception e){
                logger.info(e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void deleteAgBimCheckFromSourceId(String sourceId) {
        AgBimCheckExample example = new AgBimCheckExample();
        example.createCriteria().andSourceIdEqualTo(sourceId);
        agBimCheckMapper.deleteByExample(example);
    }

}
