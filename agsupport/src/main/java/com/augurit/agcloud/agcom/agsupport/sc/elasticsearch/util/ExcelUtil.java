package com.augurit.agcloud.agcom.agsupport.sc.elasticsearch.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :16:40 2019/5/7
 * @Modified By:
 */
public class ExcelUtil {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    /**
     *
     * @param in 输入流
     * @param fileName  文件名称
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(InputStream in, String  fileName) throws Exception{
        Workbook workbook = null;
        if (fileName.endsWith(EXCEL_XLS)){
            workbook = new HSSFWorkbook(in);
        }else if (fileName.endsWith(EXCEL_XLSX)){
            workbook = new XSSFWorkbook(in);
        }
        return workbook;
    }

    /**
     * 判断是否为Excel文件
     * @param file
     * @throws Exception
     */
    public static void checkExcel(File file) throws Exception{
        if(!(file.isFile() && (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)))){
            throw new Exception("不是Excel文件");
        }
    }

    public static List<Object[]> parseExcel(String fileName, InputStream in){

        Workbook workbook = null;//Excel文件对象
        Sheet sheet = null;//表单对象
        Row row = null;//行
        Cell cell = null;//列
        try {
            if (in==null){
                in = new FileInputStream(new File(fileName)); // 文件流
            }
            //checkExcel(file);
            workbook = getWorkbook(in,fileName);
            sheet = workbook.getSheetAt(0);//获取第一个表单对象
            row = sheet.getRow(0);//获取第一个表单的第一行
            int rows = sheet.getPhysicalNumberOfRows();//获取总行数
            int columns = row.getPhysicalNumberOfCells();//获取第一行的列数

            List<Object[]> list = new ArrayList<>();

            //遍历行列
            for (int i=0;i<rows;i++){
                Object[] arr = new Object[columns];
                row = sheet.getRow(i);
                for (int j=0;j<columns;j++){
                    cell = row.getCell(j);
                    //String cellValue = cell.getStringCellValue();
                    //System.out.println("cellValue"+cellValue);
                    arr[j] = getCellValue(cell);
                }
                list.add(arr);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Object getCellValue(Cell cell){
        Object object = null;
        switch (cell.getCellTypeEnum()){
            case BOOLEAN:
                object = cell.getBooleanCellValue();
                break;
            case ERROR:
                object = cell.getErrorCellValue();
                break;
            case NUMERIC:
                object = cell.getNumericCellValue();
                break;
            case STRING:
                object = cell.getStringCellValue();
                break;
            default:
                break;
        }
        return object;
    }


    public static void down(File excelFile, String fileName, HttpServletResponse response) throws Exception {
        FileInputStream in = null; // 文件流;
        Workbook wb = null;
        in = new FileInputStream(excelFile);
        wb = getWorkbook(in,excelFile.getName());
        in.close();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        fileName = new String (fileName.getBytes ("utf-8"),"ISO8859-1");
        response.setHeader("Content-Disposition","attachment;filename="+fileName);
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }
}
