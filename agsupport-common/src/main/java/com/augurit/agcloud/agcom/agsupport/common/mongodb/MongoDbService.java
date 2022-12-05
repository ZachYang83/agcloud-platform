package com.augurit.agcloud.agcom.agsupport.common.mongodb;

import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.augurit.agcloud.bsc.upload.MongoDbAchieve;
import com.augurit.agcloud.bsc.upload.UploadFileStrategy;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangmingyang
 * @Description: 获取mongodb的连接
 * @date 2019-07-11 16:34
 */
@Component
public class MongoDbService {
    @Autowired
    private UploaderFactory uploaderFactory;
    private MongoTemplate mongoTemplate;

    public MongoTemplate getMongoTemplate() {
        if (this.mongoTemplate == null){
            UploadFileStrategy uploadFileStrategy = uploaderFactory.create(UploadType.MONGODB.getValue());
            MongoDbAchieve mongoDbAchieve = (MongoDbAchieve) uploadFileStrategy;
            MongoTemplate mongoTemplate = mongoDbAchieve.getMongoTemplate();
            this.mongoTemplate = mongoTemplate;
        }
        return this.mongoTemplate;
    }

    /**
     * 插入一条数据
     *
     * @param doc
     * @return
     */
    public boolean insertOne(Document doc,String collectionName) {
        boolean result = false;
        try {
            this.getMongoTemplate().insert(doc,collectionName);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return result;
    }

    /**
     * 查询记录数
     * @param collectionName
     * @return
     */
    public long count(String collectionName){
        return this.count(new Query(),collectionName);
    }
    public long count(Query query,String collectionName){
        return this.getMongoTemplate().count(query,collectionName);
    }

    /**
     * 通过解析sql语句查询数据（目前无法处理groupby语句）
     *
     * @param sql 查询条件
     * @return
     */
    public ArrayList<Map> find(String sql) {
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
            MongoTemplate mongoTemplate = this.getMongoTemplate();
            MongoCollection<Document> mongoCollection = mongoTemplate.getDb().getCollection(dbName);
            //MongoDataSource mongoDataSource = getMongoDB();
            //MongoCollection<Document> mongoCollection = mongoDataSource.getDB().getCollection(dbName);
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
}
