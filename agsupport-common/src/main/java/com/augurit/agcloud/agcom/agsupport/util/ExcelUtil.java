package com.augurit.agcloud.agcom.agsupport.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author:Dreram
 * @Description: 解析Excel文件
 * @Date:created in :11:27 2018/12/24
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

    public static List<Object[]> parseExcel(String fileName,InputStream in){

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

    public static List<List<String>> parseResourceExcel(String fileName,InputStream in){

        Workbook workbook = null;//Excel文件对象
        Sheet sheet = null;//表单对象
        Row row = null;//行
        Cell cell = null;//列
        List<List<String>> list = new ArrayList<>();
        try {
            if (in==null){
                in = new FileInputStream(new File(fileName)); // 文件流
            }
            //checkExcel(file);
            workbook = getWorkbook(in,fileName);
            sheet = workbook.getSheetAt(0);//获取第一个表单对象
            row = sheet.getRow(0);//获取第一个表单的第一行
            int rows = sheet.getLastRowNum();//获取总行数
            int columns = row.getPhysicalNumberOfCells();//获取第一行的列数



            //遍历行列
            for (int i=1;i<=rows;i++){
                List<String> rowList = new ArrayList<>();
                row = sheet.getRow(i);
                Cell cell1 = row.getCell(0);
                String stringCellValue = cell1.getStringCellValue();
                if (StringUtils.isBlank(stringCellValue)){
                    continue;
                }
                for (int j=0;j<columns;j++){
                    cell = row.getCell(j);
                    Object cellValue = getCellValue(cell);
                    String v = "";
                    if (cellValue != null){
                        v = cellValue.toString();
                    }
                    rowList.add(v);

                }
                list.add(rowList);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }



    public static Object getCellValue(Cell cell){
        Object object = null;
        try {
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
        }catch (Exception e){
            e.printStackTrace();
            object = null;
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

    /**
     * Excel表格导出
     * @param response HttpServletResponse对象
     * @param excelData Excel表格的数据，封装为List<List<String>>
     * @param sheetName sheet的名字
     * @param fileName 导出Excel的文件名
     * @param columnWidth Excel表格的宽度，建议为15
     * @throws IOException 抛IO异常
     */
    public static void exportExcel(HttpServletResponse response,
                                   List<List<String>> excelData,
                                   String sheetName,
                                   String fileName,
                                   int columnWidth) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(columnWidth);
        int rowIndex = 0;
        for(List<String> data : excelData){
            HSSFRow row = sheet.createRow(rowIndex++);
            for (int i = 0; i < data.size(); i++) {
                HSSFCell cell = row.createCell(i);
                //创建一个内容对象
                HSSFRichTextString text = new HSSFRichTextString(data.get(i));
                //将内容对象的文字内容写入到单元格中
                cell.setCellValue(text);
            }
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
        //关闭workbook
        workbook.close();
    }

    /**
     * @Version  1.0
     * @Author libc
     * @Description 模板处理字符串转map
     * @param text 模板字符串，模板定义： excel列号| 备注(属性名)| 属性名
     *             如："0|凭证号|voucherNo,1|会计年度|accountYear,2|会计月度|accountMonth,3|数据来源|dataSource,4|存储类型(0. 电子 1. 电子和纸质)|saveType";
     *
     * @Return
     * @Date 2020/9/4 14:05
     */
    public static Map<String, String> stringToMap(String text) throws Exception {
        Map<String, String> mapHeader = new HashMap<>();
        String[] decollator = text.split(",");
        if (decollator.length > 0) {
            for (String s : decollator) {
                String[] desc = s.split("\\|");
                if (desc.length < 3 || "".equals(desc[0]) || "".equals(desc[2])) {
                    throw new Exception("模板格式有误，请检查！");
                }
                mapHeader.put(desc[0], desc[2]);
            }
        }
        return mapHeader;
    }


    /**
     * @Version  1.0
     * @Author libc
     * @Description:  excel文件解析成相应类型的List
     * @Param: [file, instance, modeText]
     *          file：excel文件
     *          instance：需要转换对象类型
     *          modeText：模板字符串，模板定义： excel列号| 备注(属性名)| 属性名
     * @return: java.util.List<T> 封装好对应对象集合
     * @Date 2020/9/4 14:10
     */
    public static <T> List<T> readExcel(MultipartFile file, Class<T> instance, String modeText) throws Exception {
        return readExcel(file,instance,modeText,null,0,0);
    }

    /**
     * @Version  1.0
     * @Author libc
     * @Description:  excel文件解析成相应类型的List
     * @Param: [file, instance, modeText,startSheetIndex,startRowIndex]
     *          file：excel文件
     *          instance：需要转换对象类型
     *          modeText：模板字符串，模板定义： excel列号| 备注(属性名)| 属性名
     *          startSheetIndex: 设置sheet工作表开始角标
     *          startRowIndex： 设置row行开始角标
     * @return: java.util.List<T> 封装好对应对象集合
     * @Date 2020/9/4 14:10
     */
    public static <T> List<T> readExcel(MultipartFile file, Class<T> instance, String modeText,int startSheetIndex,int startRowIndex) throws Exception {
        return readExcel(file,instance,modeText,null,startSheetIndex,startRowIndex);
    }

    /**
     * @Version  1.0
     * @Author libc
     * @Description:  excel文件解析成相应类型的List
     * @Param: [file, instance, modeText,sheetNameField,startSheetIndex,startRowIndex]
     *          file：excel文件
     *          instance：需要转换对象类型
     *          modeText：模板字符串，模板定义： excel列号| 备注(属性名)| 属性名
     *          sheetNameField： sheet名称对应赋值的对象属性名。如： user.sex ,sex用sheet名称赋值， 则sheetNameField=sex
     *          startSheetIndex: 设置sheet工作表开始角标
     *          startRowIndex： 设置row行开始角标
     * @return: java.util.List<T> 封装好对应对象集合
     * @Date 2020/9/4 14:10
     */
    public static <T> List<T> readExcel(MultipartFile file, Class<T> instance, String modeText,String sheetNameField,int startSheetIndex,int startRowIndex) throws Exception {
        //获取导入模板
        Map<String, String> mapHeader = stringToMap(modeText);
        //接收list
        List<T> tList = new ArrayList<>();

        //创建一个workbook对象
//        HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
        Workbook workbook = getWorkbook(file.getInputStream(), file.getOriginalFilename());
        //获取workbook中标签页的数量
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int sheetNumber = startSheetIndex; sheetNumber < numberOfSheets; sheetNumber++) {

            Sheet sheetAt = workbook.getSheetAt(sheetNumber);
            // 获取工作表名称
            String sheetName = sheetAt.getSheetName();
            //获取行
//            int physicalNumberOfRows = sheetAt.getPhysicalNumberOfRows(); //不包括空行，有可能该长度跟实际长度不符
            int physicalNumberOfRows = sheetAt.getLastRowNum(); //包括空行 ( 起始是0,所以 总行数=sheetAt.getLastRowNum()+1 )
            for (int rowNumber = startRowIndex; rowNumber <= physicalNumberOfRows ; rowNumber++) {
                if (0 == rowNumber) {
                    //跳过标题行 因为我们这里不做任何处理，所以有没有continue,效果都是一样的，精简才是王道。
                } else {
                    //解析第一行，与模板进行匹配
                    Row row = sheetAt.getRow(rowNumber);
                    //空行判断
                    if (null == row) {
                        continue;
                    }
                    //一行一个对象
                    T object = instance.newInstance();
                    Field[] fields = object.getClass().getDeclaredFields();
                    //获取列
//                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    int physicalNumberOfCells = row.getLastCellNum();
                    if (physicalNumberOfCells >= mapHeader.size()) {
                        for (String s : mapHeader.keySet()) {
                            Cell cell = row.getCell(Integer.parseInt(s));
                            for (Field f:fields) {
                                if (mapHeader.get(s).equals(f.getName())) {
                                    // 转换对象跟模板字符名称对应
                                    //获取私有变量
                                    f.setAccessible(true);
                                    f.set(object, getCellValue(cell));
//                                    if (getCellValue(cell)!=null && getCellValue(cell).getClass() == Double.class && f.getName().equals("serial")){
//                                        String cellNum = String.valueOf(getCellValue(cell));
//                                        f.set(object, cellNum);
////                                        int dotIndex = cellNum.indexOf(".");
////                                        f.set(object, cellNum.substring(0,dotIndex));
//                                    }
//                                    else{
//                                        f.set(object, getCellValue(cell));
//                                    }
                                }
                                if (StringUtils.isNotBlank(sheetNameField) && sheetNameField.equals(f.getName())){
                                    // 将sheet 名称 赋值到转换对象属性
                                    //获取私有变量
                                    f.setAccessible(true);
                                    f.set(object, sheetName);
                                }
                            }
                        }
                    }
                    tList.add(object);
                }
            }
        }
        return tList;
    }

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
                    arr[j] = getExcelObjectsCellValue(cell);
                }
            }
            list.add(arr);
        }
        return list;
    }

    private static Object getExcelObjectsCellValue(Cell cell) {
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
                //如果是空，此处设置一个空值
                object = "";
                break;
            default:
                break;
        }
        return object;
    }
}
