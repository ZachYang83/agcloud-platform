package com.augurit.agcloud.agcom.agsupport.sc.analysisReport.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.common.exception.ExceptionEnum;
import com.augurit.agcloud.agcom.agsupport.common.util.UUIDUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.io.DataConversionUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.sc.analysisReport.service.IAnalysisReportService;
import com.augurit.agcloud.agcom.agsupport.util.DateUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.List;

/**
 * @Author: libc
 * @Description: 分析报告业务实现类
 * @Date: 2020/10/25 10:38
 * @Version: 1.0
 */
@Service
public class AnalysisReportServiceImpl implements IAnalysisReportService {

    private static Logger logger = LoggerFactory.getLogger(AnalysisReportServiceImpl.class);

    // 退线分析报告模板路径
    private String retreatTemplatePath = new ClassPathResource("com/augurit/agcloud/agcom/agsupport/fileTemplate/pdf/retreatAnalysisReportTemplate.pdf").getPath();

    // pdf 页设置
    private static final Integer FIRST_PAGE_ROW = 28; // table表格第一页展示行数
    private static final Integer TOW_PAGE_ROW = 33; // table表格单页（每页）展示行数


    /**
     * @param dataList 需要导出的表格内容
     * @return
     * @Author: libc
     * @Date: 2020/10/25 10:46
     * @tips: CIM退线分析报告下载  (pdf 文档)
     */
    public void download(ArrayList<Map<String, Object>> dataList, HttpServletRequest request, HttpServletResponse response) {
        // 封装table 列表数据
        List<List<String>> tableList = createDataForPdf(dataList);

        // 生成pdf 并导出
        creatPdf(tableList, request, response);
    }

    /**
     * @param dataList 表格数据集合
     * @return pdf表格数据集合
     * @Author: libc
     * @Date: 2020/10/26 10:22
     * @tips: 封装生成pdf所需要的数据
     */
    private List<List<String>> createDataForPdf(ArrayList<Map<String, Object>> dataList) {
        List<List<String>> tableList = new ArrayList<List<String>>();
        //设置表头数据
        List<String> titleTableList = new ArrayList<String>();
        titleTableList.add("序号"); // 序号
        titleTableList.add("名称"); // 名称
        titleTableList.add("实际距离"); // 实际距离
        titleTableList.add("控制距离"); // 控制距离
        titleTableList.add("差值"); // 差值
        tableList.add(titleTableList);

        int index = 1; // table 中第一行的序号
        for (Map<String, Object> map : dataList) {
            // list2代表table中一行数据
            List<String> tableRowList = new ArrayList<String>();
            tableRowList.add(String.valueOf(index)); // 序号
            tableRowList.add((String) map.get("name")); // 名称
            tableRowList.add((String) map.get("actualDistance")); // 实际距离
            tableRowList.add((String) map.get("controlDistance")); // 控制距离
            tableRowList.add((String) map.get("differenceValue")); // 差值
            tableList.add(tableRowList);
            index++;
        }
        return tableList;
    }


    /**
     * @param tableList pdf表格数据集合
     * @param request
     * @param response
     * @return
     * @Author: libc
     * @Date: 2020/10/26 10:26
     * @tips: 根据pdf模板生成pdf报告并用response输出下载
     */
    private void creatPdf(List<List<String>> tableList, HttpServletRequest request, HttpServletResponse response) {
        logger.info("=========CIM退线分析报告-开始生成pdf===========");
        // 利用模板生成pdf
        // 模板路径
        System.out.println(retreatTemplatePath);
        // 生成的pdf 路径 如：c:/home/agsupportFiles/bimcheck/report/file/uuid.pdf
//        String newPDFPath = new StringBuilder(bimPdfOutPath)
//                .append(File.separator)
//                .append(UUID.randomUUID())
//                .append(".pdf").toString();
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
            out = response.getOutputStream();// 输出流
            // 设置头信息
            DataConversionUtil.setHeader(request, response, "CIM退线分析报告" + UUIDUtil.getUUID() + ".pdf");
            response.setHeader("Content-Range", "bytes 0-0,-1");
//            newFile = new File(newPDFPath);
            // 获取上级文件夹file对象
//            File parentFile = newFile.getParentFile();
//            // 创建文件夹目录
//            if (!parentFile.exists()) {
//                parentFile.mkdirs(); // 能创建多级目录
//            }
//            if (!newFile.exists()) {
//                newFile.createNewFile(); // 有路径才能创建文件
//            }

//            out = new FileOutputStream(newPDFPath);
            reader = new PdfReader(retreatTemplatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            stamper.setFormFlattening(true); //不可编辑
            AcroFields form = stamper.getAcroFields();
            // 文字类的内容处理
//            Map<String, String> dataMap = (Map<String, String>) map.get("dataMap");
            form.addSubstitutionFont(bf);
            form.setField("createTime", DateUtil.formatDate(DateUtil.FORMAT2, new Date()));
//            for (String key : dataMap.keySet()) {
//                String value = dataMap.get(key);
//                form.setField(key, value);
//                if ("cityName".equals(key)) {
//                    // 标题加粗 (这个没有生效，暂时没找到解决方案)
//                    form.setGenerateAppearances(true);
//                    form.setFieldProperty(key, "textfont", bold, null);
//                    form.setField(key, value);
//                } else {
//                    form.setField(key, value);
//                }
//            }
            // 表格类
            List<List<String>> lists = tableList;
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
                    cell.setBorderWidth(1);
                    // 垂直居中
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    // 水平居中
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);

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
                        //第一页显示条数
                        table.writeSelectedRows(0, FIRST_PAGE_ROW, signRect.getLeft(), signRect.getTop(), under);
                    }
                    //空白模板每页显示条数
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

            logger.info("=========CIM退线分析报告-生成pdf成功===========");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            logger.info("=========CIM退线分析报告-生成pdf失败===========");
            throw new AgCloudException(ExceptionEnum.PDF_CREATE_ERROR);
        }
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
}
