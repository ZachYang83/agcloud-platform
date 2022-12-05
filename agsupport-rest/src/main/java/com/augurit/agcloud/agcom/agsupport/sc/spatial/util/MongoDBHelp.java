package com.augurit.agcloud.agcom.agsupport.sc.spatial.util;

import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.common.dbcp.DBHelper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import org.apache.commons.beanutils.BeanUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-05-04.
 */
public class MongoDBHelp {

    private static MongoDataSource mongoDB = null;

    private static MongoDataSource getMongoDB(){
        if(mongoDB == null){
            mongoDB = new MongoDataSource();
        }
        return mongoDB;
    }
    /**
     * 查询总记录数
     *
     * @param filter
     * @param dbName
     * @return
     */
    public static Integer getCount(Bson filter, String dbName) {
        Integer result = 0;
        MongoDataSource mongoDataSource = getMongoDB();
        MongoCollection<Document> db = mongoDataSource.getDB().getCollection(dbName);
        try {
            result = Integer.parseInt(String.valueOf(db.count(filter)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 插入一条数据
     *
     * @param doc
     * @param dbName
     * @return
     */
    public static boolean insertOne(Document doc, String dbName) {
        boolean result = false;
        MongoDataSource mongoDataSource = getMongoDB();
        MongoCollection<Document> db = mongoDataSource.getDB().getCollection(dbName);
        try {
            db.insertOne(doc);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return result;
    }

    /**
     * 插入多条数据
     *
     * @param docs
     * @param dbName
     * @return
     */
    public static boolean insertMany(List<Document> docs, String dbName) {
        boolean result = false;
        MongoDataSource mongoDataSource = getMongoDB();
        MongoCollection<Document> db = mongoDataSource.getDB().getCollection(dbName);
        try {
            db.insertMany(docs);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除数据
     *
     * @param filter
     * @param dbName
     * @return
     */
    public static boolean deleteMany(Bson filter, String dbName) {
        boolean result = false;
        MongoDataSource mongoDataSource = getMongoDB();
        MongoCollection<Document> db = mongoDataSource.getDB().getCollection(dbName);
        try {
            db.deleteMany(filter);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return result;
    }

    /**
     * 更新数据
     *
     * @param filter 过滤条件
     * @param update 更新内容
     * @param dbName 表名
     * @return
     */
    public static boolean updateMany(Bson filter, Bson update, String dbName) {
        boolean result = false;
        MongoDataSource mongoDataSource = getMongoDB();
        MongoCollection<Document> db = mongoDataSource.getDB().getCollection(dbName);
        try {
            db.updateMany(filter, new Document("$set", update));
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return result;
    }

    /**
     * 查询数据
     *
     * @param filter 查询条件
     * @param dbName 查询表名
     * @return
     */
    public static List<Map> find(Bson filter, String dbName) {
        List<Map> result = new ArrayList<Map>();
        MongoDataSource mongoDataSource = getMongoDB();
        MongoCollection<Document> db = mongoDataSource.getDB().getCollection(dbName);
        try {
            result = db.find(filter).into(new ArrayList<Map>());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return result;
    }

    /**
     * 通过解析sql语句查询数据（目前无法处理groupby语句）
     *
     * @param sql 查询条件
     * @return
     */
    public static ArrayList<Map> find(String sql) {
        ArrayList<Map> result = new ArrayList<Map>();
        try {
            //解析sql语句
            MySqlStatementParser sqlStatementParser = new MySqlStatementParser(sql);
            SQLSelectStatement sqlStatement = (SQLSelectStatement) sqlStatementParser.parseSelect();
            SQLSelect sqlSelect = sqlStatement.getSelect();
            SQLSelectQueryBlock sqlSelectQuery = (SQLSelectQueryBlock) sqlSelect.getQuery();
            //获取表名
            String dbName = sqlSelectQuery.getFrom().toString();
            //获取别名
            String byName = sqlSelectQuery.getFrom().getAlias();
            //获取查询列
            List<SQLSelectItem> selectList = sqlSelectQuery.getSelectList();
            BasicDBObject selectField = FormatSQLUtil.formatSelect(selectList, byName);
            //获取where条件
            String where = FormatSQLUtil.getWhereString(sqlSelectQuery.getWhere(), byName);
            BasicDBObject whereField = FormatSQLUtil.formatWhere(where);
            //获取group by语句
            SQLSelectGroupByClause groupByClause = sqlSelectQuery.getGroupBy();
            //获取order by语句
            List<SQLSelectOrderByItem> orderByItemList = new ArrayList<SQLSelectOrderByItem>();
            if (sqlSelectQuery.getOrderBy() != null) {
                orderByItemList = sqlSelectQuery.getOrderBy().getItems();
            }
            BasicDBObject sortField = FormatSQLUtil.formatSort(orderByItemList, byName);
            //获取limit
            SQLLimit sqlLimit = sqlSelectQuery.getLimit();
            //开始查询
            MongoDataSource mongoDataSource = getMongoDB();
            MongoCollection<Document> mongoCollection = mongoDataSource.getDB().getCollection(dbName);
            if (sqlLimit != null) {
                int skip = Integer.valueOf(sqlLimit.getOffset().toString());
                int limit = Integer.valueOf(sqlLimit.getRowCount().toString());
                result = mongoCollection.find(whereField).projection(selectField).sort(sortField).skip(skip).limit(limit).into(new ArrayList<Map>());
            } else {
                result = mongoCollection.find(whereField).projection(selectField).sort(sortField).into(new ArrayList<Map>());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 把实体bean对象转换成Document
     * @param bean
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> Document bean2Document(T bean) throws IllegalArgumentException,
            IllegalAccessException {
        if (bean == null) {
            return null;
        }
        Document doc = new Document();
        // 获取对象对应类中的所有属性域
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 获取属性名
            String varName = field.getName();
            // 修改访问控制权限
            boolean accessFlag = field.isAccessible();
            if (!accessFlag) {
                field.setAccessible(true);
            }
            Object param = field.get(bean);
            if (param == null) {
                continue;
            } else if (param instanceof Integer) {//判断变量的类型
                int value = ((Integer) param).intValue();
                doc.put(varName, value);
            } else if (param instanceof String) {
                String value = (String) param;
                doc.put(varName, value);
            } else if (param instanceof Double) {
                double value = ((Double) param).doubleValue();
                doc.put(varName, value);
            } else if (param instanceof Float) {
                float value = ((Float) param).floatValue();
                doc.put(varName, value);
            } else if (param instanceof Long) {
                long value = ((Long) param).longValue();
                doc.put(varName, value);
            } else if (param instanceof Boolean) {
                boolean value = ((Boolean) param).booleanValue();
                doc.put(varName, value);
            } else if (param instanceof Date) {
                Date value = (Date) param;
                doc.put(varName, value);
            }
            // 恢复访问控制权限
            field.setAccessible(accessFlag);
        }
        return doc;
    }

    /**
     * 把Map(Document)转换成bean对象
     * @param map
     * @param bean
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static <T> T map2Bean(Map map, T bean) throws IllegalAccessException,
            InvocationTargetException {
        if (bean == null) {
            return null;
        }
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String varName = field.getName();
            Object object = map.get(varName);
            if (object != null) {
                BeanUtils.setProperty(bean, varName, object);
            }
        }
        return bean;
    }

    /**
     * 清除
     * @param table
     * @return
     */
    public static boolean clear(String table){
        boolean flag;
        try{
            if(table == null || "".equals(table)){
                MongoIterable<String> tables = getMongoDB().getDB().listCollectionNames();
                MongoCursor<String> it = tables.iterator();
                while (it.hasNext()){
                    deleteMany(new Document(),it.next());
                }
            }else{
                deleteMany(new Document(),table);
            }
            flag = true;
        }catch (Exception e){
            flag = false;
        }
        return flag;
    }

    public static void main(String[] args) throws Exception{
        String sql = "show index from ag_spatial_temp_biger WHERE key_name = 'PRIMARY'";
        Map data = (Map) DBHelper.findFirstColum("spring.datasource", sql, null);
        System.out.println(data.toString());
    }
}
