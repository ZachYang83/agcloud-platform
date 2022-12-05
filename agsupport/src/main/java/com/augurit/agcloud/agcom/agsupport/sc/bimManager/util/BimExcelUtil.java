package com.augurit.agcloud.agcom.agsupport.sc.bimManager.util;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/07
 * @Description:
 */
public class BimExcelUtil {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    /**
     * @param in       输入流
     * @param fileName 文件名称
     * @return
     * @throws Exception
     */
    private static Workbook getWorkbook(InputStream in, String fileName) throws Exception {
        Workbook workbook = null;
        if (fileName.endsWith(EXCEL_XLS)) {
            workbook = new HSSFWorkbook(in);
        } else if (fileName.endsWith(EXCEL_XLSX)) {
            workbook = new XSSFWorkbook(in);
        }else{
            workbook = new HSSFWorkbook(in);
        }
        return workbook;
    }

    /**
     * 判断是否为Excel文件
     *
     * @param file
     * @throws Exception
     */
    public static void checkExcel(File file) throws Exception {
        if (!(file.isFile() && (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)))) {
            throw new Exception("不是Excel文件");
        }
    }

    /**
     * 获取excel数据
     *
     * @param fileName excel全名
     * @param in       excel的文件流
     * @return
     */
    public static List<Object[]> getExcelData(String fileName, InputStream in) {

        Workbook workbook = null;//Excel文件对象
        Sheet sheet = null;//表单对象
        Row row = null;//行
        Cell cell = null;//列
        try {
            if (in == null) {
                in = new FileInputStream(new File(fileName)); // 文件流
            }
            //checkExcel(file);
            List<Object[]> list = getExcelObjects(fileName, in);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(in != null){
                try{
                    in.close();
                }catch (IOException e){
                    e.getMessage();
                }
            }
        }
    }

    private static List<Object[]> getExcelObjects(String fileName, InputStream in) throws Exception {
        Workbook workbook;
        Sheet sheet;
        Row row;
        Cell cell;
        workbook = getWorkbook(in, fileName);
        sheet = workbook.getSheetAt(0);//获取第一个表单对象
        row = sheet.getRow(0);//获取第一个表单的第一行
        int rows = sheet.getPhysicalNumberOfRows();//获取总行数
        int columns = row.getPhysicalNumberOfCells();//获取第一行的列数

        List<Object[]> list = new ArrayList<>();

        //遍历行列
        for (int i = 0; i < rows; i++) {
            Object[] arr = new Object[columns];
            row = sheet.getRow(i);
            for (int j = 0; j < columns; j++) {
                cell = row.getCell(j);
                //String cellValue = cell.getStringCellValue();
                //System.out.println("cellValue"+cellValue);
                if(cell != null){
                    arr[j] = getCellValue(cell);
                }
            }
            list.add(arr);
        }
        return list;
    }

    /**
     * 获取excel数据
     *
     * @param file excel文件
     * @return
     */
    public static List<Object[]> getExcelData(MultipartFile file) {

        InputStream inputStream = null;
        try {
            //checkExcel(file);
            inputStream = file.getInputStream();
            List<Object[]> list = getExcelObjects(file.getOriginalFilename(), inputStream);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SourceException("关联数据解析失败，请用标准的excel模板导入");
        } finally {
            try{
                if(inputStream != null){
                    inputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static Object getCellValue(Cell cell) {
        Object object = null;
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                object = cell.getBooleanCellValue();
                break;
            case ERROR:
                object = cell.getErrorCellValue();
                break;
            case NUMERIC:
//                object = cell.getNumericCellValue();
                //数字类型的日期格式转换
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String format = sf.format(date);
                    if (format.contains("00:00:00")) {
                        //处理一下日期2020-07-15 00:00:00，这种格式，只要日期2020-07-15
                        object = format.substring(0, 11);
                    } else {
                        object = format;
                    }
                } else {
//                    double dValue = cell.getNumericCellValue();
//                    DecimalFormat df = new DecimalFormat("0");
//                    object = df.format(dValue);
//                    object = dValue;
                    //数字类型的非日期强制用string读取
                    cell.setCellType(CellType.STRING);
                    object = cell.getStringCellValue();
                }
                break;
            case STRING:
                object = cell.getStringCellValue();
                break;
            case FORMULA:
                object = cell.getCellFormula();
                break;
            case BLANK:
                break;
            default:
                break;
        }
        return object;
    }


    public static void down(File excelFile, String fileName, HttpServletResponse response) throws Exception {
        FileInputStream in = null; // 文件流;
        Workbook wb = null;
        OutputStream os = null;
        try{
            in = new FileInputStream(excelFile);
            wb = getWorkbook(in, excelFile.getName());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            fileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();
            wb.write(os);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(in != null){
                    in.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                if(os != null){
                    os.flush();
                    os.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
//            String fileName = "TwoStoreyBuilding.xls";
//            String path = "H:\\文档\\TwoStoreyBuilding.xls";

//            String fileName = "TwoStoreyBuilding_mini.csv";
//            String path = "H:\\文档\\TwoStoreyBuilding_mini.csv";

            String fileName = "TwoStoreyBuilding-2.xls";
            String path = "H:\\文档\\TwoStoreyBuilding-2.xls";


            FileInputStream fileInputStream = new FileInputStream(new File(path));
            List<Object[]> objects = getExcelData(fileName, fileInputStream);
            if (objects != null && objects.size() > 0) {
                for (Object[] objArr : objects) {
                    if (objArr != null && objArr.length > 0) {
                        String s = "";
                        for (Object obj : objArr) {
                            s += obj.toString();
                            s += "---";
                        }
                        System.out.println(s);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
