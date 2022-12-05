package com.augurit.agcloud.agcom.agsupport.util;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther:
 * @Date: 2020/06
 * @Description:
 */
public class CsvUtil {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvUtil.class);

    /**
     * 解析csv文件并转成bean
     * @param file csv文件
     * @param clazz 类
     * @param <T> 泛型
     * @return 泛型bean集合
     */
    public static <T> List<T> getCsvData(MultipartFile file, Class<T> clazz) {
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(file.getInputStream(), "gbk");
        } catch (Exception e) {

            e.printStackTrace();
        }

        HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(clazz);

        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(in)
//                .withSeparator(',')
                .withQuoteChar('\'')
                .withMappingStrategy(strategy).build();
        return csvToBean.parse();
    }

    /**
     * 获取csv数据，返回一个数组，第一行就是表头，其余的都是数据
     *
     * @param csvFile 数据
     * @return 数组
     */
    public static Object[][] getCsvData(MultipartFile csvFile) {
        BufferedReader file = null;
        try {
            List<String[]> records = new ArrayList<String[]>();
            String record;
            // 设定GBK字符集，使用带缓冲区的字符输入流BufferedReader读取文件内容
            file = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), "GBK"));
            // file.readLine(); //跳过表头所在的行

            // 遍历数据行并存储在名为records的ArrayList中，每一行records中存储的对象为一个String数组
            while ((record = file.readLine()) != null) {
                String fields[] = record.split(",");
                records.add(fields);
            }
            // 将存储测试数据的List转换为一个Object的二维数组
            Object[][] results = new Object[records.size()][];
            // 设置二位数组每行的值，每行是一个Object对象
            for (int i = 0; i < records.size(); i++) {
                results[i] = (Object[]) records.get(i);
            }
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SourceException("关联数据解析失败，请用标准的CSV模板导入");
        } finally {
            try{
                if(file != null){
                    // 关闭文件
                    file.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
