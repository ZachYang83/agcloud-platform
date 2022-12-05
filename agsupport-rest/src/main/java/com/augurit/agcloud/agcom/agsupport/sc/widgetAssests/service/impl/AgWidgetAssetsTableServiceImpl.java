package com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsColumns;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsColumnsExample;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsTable;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsTableExample;
import com.augurit.agcloud.agcom.agsupport.mapper.AgWidgetAssetsCustomMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgWidgetAssetsProjectCustomMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgWidgetAssetsColumnsMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgWidgetAssetsTableMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.domain.AgWidgetAssetsTableCustom;
import com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.service.IAgWidgetAssetsTableService;
import com.augurit.agcloud.agcom.agsupport.util.WidgetAssetsUtils;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author lizih
 * @Date 2020/11/19
 * @Version 1.0
 * TODO isLegal方法有待完善
 */

@Service
public class AgWidgetAssetsTableServiceImpl implements IAgWidgetAssetsTableService {

    @Autowired
    private AgWidgetAssetsProjectCustomMapper agThematicProjectCustomMapper;

    @Autowired
    private AgWidgetAssetsCustomMapper agThematicCustomMapper;
    @Autowired
    private AgWidgetAssetsTableMapper agThematicTableMapper;
    @Autowired
    private AgWidgetAssetsColumnsMapper agThematicColumnsMapper;

    // 分页查询全部
    @Override
    public PageInfo<Object> findAllPage(String softCode, String tableName, String orderByColumn, String orderDesc, Page page) {
        String tableFullName = getTableFullNameBySoftCode(softCode, tableName);
        String orderDescString = "asc";
        if ("1".equals(orderDesc)){ //降序
            orderDescString = "desc";
        }
        isLegalCertainField(orderByColumn);
        PageHelper.startPage(page);
        List<Object> list = agThematicCustomMapper.selectAllFromArbitraryTable(tableFullName, orderByColumn, orderDescString);
        return new PageInfo<>(list);
    }

    // 查询全部
    @Override
    public List<Object> findAll(String softCode, String tableName, String orderByColumn, String orderDesc) {

        String tableFullName = getTableFullNameBySoftCode(softCode, tableName);
        String orderDescString = "asc";
        if ("1".equals(orderDesc)){ //降序
            orderDescString = "desc";
        }
        isLegalCertainField(orderByColumn);
        List<Object> list = agThematicCustomMapper.selectAllFromArbitraryTable(tableFullName, orderByColumn, orderDescString);
        return list;
    }


    // 条件查询
    @Override
    public List<Object> findCondition(String softCode, String tableName, String selectColumns,
                                          String searchCondition, String orderByColumn, String orderDesc) {
        Map<String,String> vars = findConditionFilter(softCode, tableName, selectColumns, searchCondition, orderByColumn, orderDesc);
        List<Object> list = agThematicCustomMapper.selectRecordsFromArbitraryTable(vars.get("tableFullName"),
                vars.get("selectColumns"), vars.get("searchCondition"),
                vars.get("orderByColumn"), vars.get("orderDescString"));
        return list;
    }

    // 分页条件查询
    @Override
    public PageInfo<Object> findConditionPage(String softCode, String tableName, String selectColumns,
                                          String searchCondition, String orderByColumn, String orderDesc, Page page) {
        Map<String,String> vars = findConditionFilter(softCode, tableName, selectColumns, searchCondition, orderByColumn, orderDesc);
        PageHelper.startPage(page);
        List<Object> list = agThematicCustomMapper.selectRecordsFromArbitraryTable(vars.get("tableFullName"),
                vars.get("selectColumns"), vars.get("searchCondition"),
                vars.get("orderByColumn"), vars.get("orderDescString"));
        return new PageInfo<>(list);
    }

    /**
     * @Author Zihui Li
     * @Date: 2020/11/27 15:27
     * @tips: 校验传入的参数是否合法，筛查排除可能造成sql注入的坏语句
     * @Param softCode 项目编码
     * @Param tableName 表名（非全表名）
     * @Param selectColumns 需要查询的字段名，逗号隔开
     * @Param searchCondition 查询条件，where语句后的sql语句（需重点筛查合法性，筛查方法为isLegal）
     * @Param orderByColumn 根据某个字段排序
     * @Param orderDesc 排序规则 "0":升序 "1":降序
     * @return java.util.Map<java.lang.String,java.lang.String> key为参数名，value为参数名对应的值
     */
    private Map<String,String> findConditionFilter(String softCode, String tableName, String selectColumns,
                                             String searchCondition, String orderByColumn, String orderDesc){
        Map<String,String> vars = new HashMap<>();
        // 校验tableFullName是否存在
        String tableFullName = getTableFullNameBySoftCode(softCode, tableName);
        vars.put("tableFullName",tableFullName);

        // 校验selectColumns是否合法
        if (StringUtils.isEmpty(selectColumns)){
            throw new SourceException("selectColumns 所选择的字段属性不能为空且必须用逗号分隔 ");
        }
        if (selectColumns.trim().equals("*")){
            vars.put("selectColumns",selectColumns);
        } else {
            List<String> columnsOfTable = agThematicCustomMapper.getColumnsFromArbitraryTable(tableFullName);
            if (columnsOfTable==null){
                throw new SourceException("该表不存在或该表未设置字段。若看到此信息，请检查后端代码逻辑漏洞");
            }
            List<String> selectColumnsList = Arrays.asList(selectColumns.split(","));
            for (String ele:selectColumnsList){
                isLegalCertainField(ele);
                if (!columnsOfTable.contains(ele.trim())){
                    throw new SourceException("所查询的字段：" + ele + "不在表中，请检查每个字段名是否存在并合法，并用逗号隔开。");
                }
            }
            vars.put("selectColumns",String.join(",",selectColumnsList));
        }

        //校验searchCondition是否合法
        Boolean legal = isLegal(searchCondition);
        if (!legal) {
            throw new SourceException("searchCondition is illegal. DELETE, TRUNCATE, DROP and damaging punctuations are not allowed. ");
        }
        vars.put("searchCondition",searchCondition);

        //给orderDescString赋值
        String orderDescString = "asc";
        if ("1".equals(orderDesc)){ //降序
            orderDescString = "desc";
        }
        vars.put("orderDescString",orderDescString);

        // 校验orderByColumn是否合法，限定只能依据一个属性进行排序
        isLegalCertainField(orderByColumn);
        vars.put("orderByColumn",orderByColumn.trim());
        return vars;
    }

    @Override
    public byte[] getTargetFile(String softCode, String tableName, String id, String fieldName) {
        String uniqueIdfRes = agThematicProjectCustomMapper.getUniqueIdBySoftCode(softCode);
        String tableFullName = WidgetAssetsUtils.TABLE_PREFIX + uniqueIdfRes + "_" + tableName;
        isLegalCertainField(fieldName);
        if (agThematicCustomMapper.columnExistCheck(tableFullName, fieldName) < 1){
            throw new SourceException("illegal column name. 该字段不存在，请检查字段");
        }
        Map<String,Object> fileMap = agThematicCustomMapper.getTargetFile(tableFullName, id, fieldName);
        if (fileMap == null){
            return null;
        }
        return  (byte[]) fileMap.get(fieldName);
    }

    /**
     * @Author Zihui Li
     * @Date: 2020/11/27 16:00
     * @tips: 判断某个字段名是否合法,不合法则抛出异常，合法则不做操作
     * @Param fieldName 字段名
     * @return void
     */
    private void isLegalCertainField(String fieldName){
        String regxCheck = "[a-z_]{1,15}";
        Boolean fieldNameLegal = Pattern.matches(regxCheck, fieldName.trim());
        if (!fieldNameLegal){
            throw new SourceException("字段名非法，只能小写字母或下划线，1到15个字符，错误字段："+ fieldName);
        }
    }

    /**
     * @Author Zihui Li
     * @Date: 2020/11/27 16:00
     * @tips: 暂时保留，后续可能有需求要用到。用途：将数据库内byte[]文件转成String
     * @Param softCode, tableName, id, fieldName
     * @return java.lang.String
     */
    private String getTargetFileString(String softCode, String tableName, String id, String fieldName) {
        byte[] fileBytes = getTargetFile(softCode, tableName, id, fieldName);

        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        InputStream in = null;
        StringBuffer strBuff = new StringBuffer();
        String targetFileString = null;

        try{
            in = new ByteArrayInputStream(fileBytes);
            read = new InputStreamReader(in, "utf-8");
            bufferedReader = new BufferedReader(read);
            String lineTxt = null;

            while ((lineTxt = bufferedReader.readLine()) != null) {
                strBuff.append(lineTxt);
                strBuff.append("\n");
            }
            targetFileString = strBuff.toString();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                }
            }
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return targetFileString;
    }

    /**
     * @Author Zihui Li
     * @Date: 2020/11/27 16:01
     * @tips: 用softCode和tableName联合查出表全名
     * @Param [softCode, tableName]
     * @return java.lang.String
     */
    private String getTableFullNameBySoftCode(String softCode, String tableName) {
        if (StringUtils.isEmpty(softCode)) {
            throw new SourceException("SoftCode should not be empty. ");
        }
        String uniqueIdfRes = agThematicProjectCustomMapper.getUniqueIdBySoftCode(softCode);
        if (StringUtils.isEmpty(uniqueIdfRes)){
            // 如果该项目的唯一标识还未设置或softCode错误导致无法查出唯一标识，抛出异常，提醒设置标识。
            throw new SourceException("错误原因1：UniqueIdf hasn't been set. 请先设置该项目的唯一标识" +
                    "错误原因2：应用编号不存在，请查看softCode参数是否有误 ");
        }
        String tableFullName = WidgetAssetsUtils.TABLE_PREFIX + uniqueIdfRes + "_" + tableName;
        if (agThematicCustomMapper.tableNum(tableFullName) < 1){
            throw new SourceException("This table does not exist. 请检查tableName或者softCode是否正确，项目唯一标识是否已设置。");
        }
        return tableFullName;
    }

    /**
     * @Author qinyg, Zihui Li
     * @Date: 2020/11/27 16:01
     * @tips: 判断searchCondition是否合法, 有待完善
     * @Param [searchCondition]
     * @return java.lang.Boolean
     */
    private Boolean isLegal(String searchCondition){
        if (searchCondition==null){
            return true;
        }
        String lowerCaseCondition = searchCondition.toLowerCase();
        // 查询sql语句中不能包含删除数据表的操作，其他非法操作过滤有待完善
        List<String> illegalWords = Arrays.asList("delete ", "drop ", "truncate ","alter ", ";","--",
                "exec ","xp_cmdshell ","select ","insert ","update ","create ","rename ","exists ","master. ","restore ","or 1=","call ");
        for (String keyword:illegalWords){
            if (lowerCaseCondition.contains(keyword)){
                return false;
            }
        }
        return true;
    }


    @Override
    @Transactional
    public void add(String softCode, String tableName, HttpServletRequest request) {
        //目标专题表
        String targetTableName = getTableFullNameBySoftCode(softCode, tableName);
        if(StringUtils.isEmpty(targetTableName)){
            throw new SourceException("表项目标识不存在,需要先设置项目标识");
        }

        //应用id
        String appSoftId = agThematicProjectCustomMapper.getAppSoftIdBySoftCode(softCode);
        if(StringUtils.isEmpty(appSoftId)){
            throw new SourceException("应用编号不存在，请查看softCode参数是否有误");
        }
        //获取所有的表属性
        List<AgWidgetAssetsColumns> agThematicColumns = getAgThematicColumns(tableName, appSoftId);

        //获取所有非文件属性
        List<AgWidgetAssetsTableCustom> allRequestParam = getAllRequestParam(agThematicColumns, request);
        //获取所有文件属性
        getAllRequestFileParam(request, agThematicColumns, allRequestParam);

        //参数不能为空
        if(allRequestParam.size() == 0){
            throw new SourceException("需要保存的属性不能为空");
        }

        //拼装插入参数
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ");
        sql.append(targetTableName);
        sql.append(" (");
        //拼装所有的column
        for(AgWidgetAssetsTableCustom custom : allRequestParam){
            sql.append("\"" + custom.getColumnName() + "\",");
        }
        //去掉最后一个“，”
        sql.delete(sql.length() - 1, sql.length());
        sql.append(" )");
        //拼装所有的value
        sql.append(" values(");
        for(AgWidgetAssetsTableCustom custom : allRequestParam){
            //判断value的值
            getAddColumnType(sql, custom);
        }
        //去掉最后一个“，”
        sql.delete(sql.length() - 1, sql.length());
        sql.append(" )");
        agThematicCustomMapper.executeDefineSql(sql.toString());
    }

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/27 14:32
     * @tips: 查询专题表的所有属性
     * @param tableName 表明
     * @param appSoftId 应用编号
     * @return
     */
    private List<AgWidgetAssetsColumns> getAgThematicColumns(String tableName, String appSoftId) {
        //查询agThematicTableId
        AgWidgetAssetsTableExample tableExample = new AgWidgetAssetsTableExample();
        tableExample.createCriteria().andAppSoftIdEqualTo(appSoftId).andTableNameEqualTo(tableName);
        List<AgWidgetAssetsTable> agThematicTables = agThematicTableMapper.selectByExample(tableExample);
        if (agThematicTables == null || agThematicTables.size() == 0) {
            throw new SourceException("表名不存在,参数tableName有误");
        }
        String agThematicTableId = agThematicTables.get(0).getId();

        //查询该目标专题表的所有属性
        AgWidgetAssetsColumnsExample columnsExample = new AgWidgetAssetsColumnsExample();
        columnsExample.createCriteria().andThematicTableIdEqualTo(agThematicTableId);
        List<AgWidgetAssetsColumns> agThematicColumns = agThematicColumnsMapper.selectByExample(columnsExample);
        if (agThematicColumns == null || agThematicColumns.size() == 0) {
            throw new SourceException("表属性未设置,参数tableName有误");
        }
        return agThematicColumns;
    }

    private void getAddColumnType(StringBuffer sql, AgWidgetAssetsTableCustom custom) {
        String columnType = custom.getColumnType();
        //1字符；2整数；3 文本；4 文件
        if ("1".equals(columnType)) {
            sql.append("'" + custom.getColumnValue() + "',");
        }
        if ("2".equals(columnType)) {
            sql.append(custom.getColumnValue() + ",");
        }
        if ("3".equals(columnType)) {
            sql.append("'" + custom.getColumnValue() + "',");
        }
        if ("4".equals(columnType)) {
            MultipartFile file = (MultipartFile)custom.getColumnValue();
            try {
                byte[] bytes = file.getBytes();
                String fileStr = Base64.getEncoder().encodeToString(bytes);
                sql.append("decode('" + fileStr + "','base64'),");
            } catch (IOException e) {
                throw new SourceException("文件获取失败，请重新尝试");
            }
        }
    }

    private void getUpdateColumnType(StringBuffer sql, AgWidgetAssetsTableCustom custom) {
        String columnType = custom.getColumnType();
        //1字符；2整数；3 文本；4 文件
        if ("1".equals(columnType)) {
            sql.append(custom.getColumnName() + "= '" + custom.getColumnValue() + "',");
        }
        if ("2".equals(columnType)) {
            sql.append(custom.getColumnName() + "=" + custom.getColumnValue() + ",");
        }
        if ("3".equals(columnType)) {
            sql.append(custom.getColumnName() + "='" + custom.getColumnValue() + "',");
        }
        if ("4".equals(columnType)) {
            MultipartFile file = (MultipartFile)custom.getColumnValue();
            try {
                byte[] bytes = file.getBytes();
                String fileStr = Base64.getEncoder().encodeToString(bytes);
                sql.append(custom.getColumnName() + "=decode('" + fileStr + "','base64'),");
            } catch (IOException e) {
                throw new SourceException("文件获取失败，请重新尝试");
            }
        }
    }

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/23 10:36
     * @tips: 获取文件属性
     * @param request 文件值
     * @param agThematicColumns 专题表的所有属性
     * @param allRequestParam 请求的所有二次封装参数
     * @return
     */
    private void getAllRequestFileParam(HttpServletRequest request, List<AgWidgetAssetsColumns> agThematicColumns, List<AgWidgetAssetsTableCustom> allRequestParam){
        //是否是文件类型
        if(!(request instanceof MultipartHttpServletRequest)){
            return;
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> fileNames = multipartRequest.getFileNames();
        if(fileNames != null){
            while (fileNames.hasNext()) {
                //column的名称
                String columnName =fileNames.next();
                //字段名非法验证
                isLegalCertainField(columnName);
                //获取属性的字段类型
                String columType = checkThematicTableCoumnIsExist(agThematicColumns, columnName);
                //判断是否是文件类型
                if(!"4".equals(columType)){
                    throw new SourceException("此字段：" + columnName + " 不是文件类型");
                }
                MultipartFile file = multipartRequest.getFile(columnName);
                if(file != null && !file.isEmpty()){
                    //赋值
                    AgWidgetAssetsTableCustom custom = new AgWidgetAssetsTableCustom();
                    custom.setColumnName(columnName);
                    custom.setColumnValue(file);
                    custom.setColumnType(columType);
                    allRequestParam.add(custom);
                }
            }
        }
//        if(fileKeys != null && fileKeys != ""){
//            for(String fileKey : fileKeys.split(",")){
//                //字段名非法验证
//                isLegalCertainField(fileKey);
//                //获取属性的字段类型
//                String columType = checkThematicTableCoumnIsExist(agThematicColumns, fileKey);
//                //判断是否是文件类型
//                if(!"4".equals(columType)){
//                    throw new SourceException("此字段：" + fileKey + " 不是文件类型");
//                }
//                MultipartFile file = multipartRequest.getFile(fileKey);
//                if(file != null && !file.isEmpty()){
//                    //赋值
//                    AgThematicTableCustom custom = new AgThematicTableCustom();
//                    custom.setColumnName(fileKey);
//                    custom.setColumnValue(file);
//                    custom.setColumnType(columType);
//                    allRequestParam.add(custom);
//                }
//            }
//        }
    }

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/23 9:45
     * @tips: 获取所有的参数，需要排除掉softCode、tableName、fileKeys、同时检查column的有效性
     * @param agThematicColumns 专题表的所有属性
     * @param
     * @return
     */
    private List<AgWidgetAssetsTableCustom> getAllRequestParam(List<AgWidgetAssetsColumns> agThematicColumns, HttpServletRequest request) {
        List<AgWidgetAssetsTableCustom> res = new ArrayList<>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                //column的名称
                String columnName = (String) temp.nextElement();
                //排查掉需要排除的数据
                if("softCode".equals(columnName) || "tableName".equals(columnName) || "fileKeys".equals(columnName)){
                    continue;
                }
                //字段名非法验证
                isLegalCertainField(columnName);

                //参数有效性检查，检查传入的属性是否已经配置
                String columType = checkThematicTableCoumnIsExist(agThematicColumns, columnName);
                //column的值
                String columnValue = request.getParameter(columnName);

                //判断整数类型的属性值是否是整数
                if("2".equals(columType)){
                    try{
                        Integer.valueOf(columnValue);
                    }catch (Exception e){
                        throw new SourceException("属性：" + columnName + " 必须是整数");
                    }
                }

                //过滤掉文件类型
                if("4".equals(columType)){
                    continue;
                }

                //赋值
                AgWidgetAssetsTableCustom custom = new AgWidgetAssetsTableCustom();
                custom.setColumnName(columnName);
                custom.setColumnValue(columnValue);
                custom.setColumnType(columType);
                res.add(custom);
            }
        }
        return res;
    }

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/23 10:14
     * @tips: 检查该属性是否存在，如果存在则返回该属性的类型，不存在则抛出异常
     * @param  agThematicColumns 专题表的所有属性
     * @param  columnName 需要检查的属性
     * @return 需要检查的属性的类型
     */
    private String checkThematicTableCoumnIsExist(List<AgWidgetAssetsColumns> agThematicColumns, String columnName){
        String columnType = null;
        for(AgWidgetAssetsColumns thematicColumns : agThematicColumns){
            if(thematicColumns.getColumnName().equals(columnName)){
                columnType = thematicColumns.getColumnType();
            }
        }
        if(columnType == null){
            throw new SourceException("属性：" + columnName + " 不存在，请先设置");
        }
        return columnType;
    }


    @Override
    @Transactional
    public void update(String softCode, String tableName, HttpServletRequest request) {
        //目标专题表
        String targetTableName = getTableFullNameBySoftCode(softCode, tableName);
        if(StringUtils.isEmpty(targetTableName)){
            throw new SourceException("表项目标识不存在,需要先设置项目标识");
        }

        //验证id是否有效
        String id = request.getParameter("id");
        //id不能为空
        if(StringUtils.isEmpty(id)){
            throw new SourceException("id参数不能为空");
        }
        //先查询一下该数据是否存在
        List<Object> objects = agThematicCustomMapper.executeDefineSql("select count(1) from " + targetTableName + " where id = '" + id + "'");
        if(objects == null || objects.size() == 0){
            throw new SourceException("该数据不存在，修改失败");
        }
        Map map = (Map)objects.get(0);
        Long count = (Long)map.get("count");
        if(count == null || count == 0){
            throw new SourceException("该数据不存在，修改失败");
        }

        //应用id
        String appSoftId = agThematicProjectCustomMapper.getAppSoftIdBySoftCode(softCode);
        if(StringUtils.isEmpty(appSoftId)){
            throw new SourceException("应用编号不存在，请查看softCode参数是否有误");
        }

        //获取所有的表属性
        List<AgWidgetAssetsColumns> agThematicColumns = getAgThematicColumns(tableName, appSoftId);

        //获取所有非文件属性
        List<AgWidgetAssetsTableCustom> allRequestParam = getAllRequestParam(agThematicColumns, request);
        //获取所有文件属性
        getAllRequestFileParam(request, agThematicColumns, allRequestParam);

        //参数不能为空
        if(allRequestParam.size() == 0){
            throw new SourceException("需要保存的属性不能为空");
        }

        //拼装插入参数
        StringBuffer sql = new StringBuffer();
        sql.append("update ");
        sql.append(targetTableName);
        sql.append(" set ");
        //拼装所有的column
        for(AgWidgetAssetsTableCustom custom : allRequestParam){
            //如果是id，可以不设置修改
            if("id".equals(custom.getColumnName())){
                continue;
            }
            getUpdateColumnType(sql, custom);
        }
        //去掉最后一个“，”
        sql.delete(sql.length() - 1, sql.length());
        sql.append(" where id=");
        sql.append("'" + id + "'");

        //更新
        agThematicCustomMapper.executeDefineSql(sql.toString());
    }

    @Override
    @Transactional
    public void delete(String softCode, String tableName, String ids) {
        //目标专题表
        String targetTableName = getTableFullNameBySoftCode(softCode, tableName);
        if(StringUtils.isEmpty(targetTableName)){
            throw new SourceException("表项目标识不存在,需要先设置项目标识");
        }
        StringBuffer sql = new StringBuffer();
        sql.append("delete from ");
        sql.append(targetTableName);
        sql.append(" where id in(");
        for(String id : ids.split(",")){
            sql.append("'" + id + "',");
        }
        //去掉最后一个“，”
        sql.delete(sql.length() - 1, sql.length());
        sql.append(")");
        agThematicCustomMapper.executeDefineSql(sql.toString());
    }


}
