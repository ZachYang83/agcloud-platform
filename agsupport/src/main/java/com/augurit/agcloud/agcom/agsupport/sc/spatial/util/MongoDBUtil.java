package com.augurit.agcloud.agcom.agsupport.sc.spatial.util;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-05-04.
 */
public class MongoDBUtil {

    private MongoCollection<Document> mongoCollection;
    private String dbName;

    public MongoDBUtil(String dbName) {
        this.dbName = dbName;
        mongoCollection = getCollection();
    }

    public MongoCollection<Document> getCollection() {
        if (mongoCollection == null) {
            MongoDataSource mongoDataSource = new MongoDataSource();
            return mongoDataSource.getDB().getCollection(dbName);
        } else {
            return mongoCollection;
        }
    }

    /**
     * 查询总记录数
     *
     * @param filter
     * @return
     */
    public double getCount(Bson filter) {
        double result = 0d;
        try {
            result = getCollection().count(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 插入一条数据
     *
     * @param doc
     * @return
     */
    public boolean insertOne(Document doc) {
        boolean result = false;
        try {
            getCollection().insertOne(doc);
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
     * @return
     */
    public boolean insertMany(List<Document> docs) {
        boolean result = false;
        try {
            getCollection().insertMany(docs);
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
     * @return
     */
    public boolean deleteMany(Bson filter) {
        boolean result = false;
        try {
            getCollection().deleteMany(filter);
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
     * @return
     */
    public boolean updateMany(Bson filter, Bson update) {
        boolean result = false;
        try {
            getCollection().updateMany(filter, new Document("$set", update));
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
     * @return
     */
    public ArrayList<Document> find(Bson filter) {
        ArrayList<Document> result = new ArrayList<Document>();
        try {
            FindIterable<Document> results = getCollection().find(filter);
            MongoCursor<Document> iterator = results.iterator();
            while (iterator.hasNext()) {
                result.add(iterator.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return result;
    }

}
