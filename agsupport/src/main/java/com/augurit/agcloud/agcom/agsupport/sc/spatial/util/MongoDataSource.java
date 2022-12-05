package com.augurit.agcloud.agcom.agsupport.sc.spatial.util;

import com.common.util.ConfigProperties;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.*;


public class MongoDataSource {

    private MongoClient mongoClient;
    private String url;
    private String dbName;

    public MongoDataSource(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoDataSource() {
        if (mongoClient == null) {
            //获取配置信息
            url = ConfigProperties.getByKey("mongoDB.url");
            dbName = ConfigProperties.getByKey("mongoDB.db");
            //创建连接池
            mongoClient = getMongoClient(url);
        }
    }

    public MongoClient getMongoClient(String url) {
        MongoClientURI connectionString = new MongoClientURI(url);
        MongoClient client = new MongoClient(connectionString);
        return client;
    }

    public MongoClient getMongoClient(String host, MongoClientOptions myOptions) {
        return new MongoClient(host, myOptions);
    }

    public MongoDatabase getDB() {
        MongoDatabase connection = null;
        if (this.mongoClient != null) {
            connection = this.mongoClient.getDatabase(this.dbName);
        }
        return connection;
    }

    public MongoDatabase getDB(String dbName) {
        MongoDatabase connection = null;
        if (this.mongoClient != null) {
            connection = this.mongoClient.getDatabase(dbName);
        }
        return connection;
    }

    public synchronized void closeMongoClient() {
        if (this.mongoClient != null) {
            mongoClient.close();
        }
    }

    public static void main(String[] args) {
        String layerTable = "test";
        Map map = new HashMap();
        map.put("a", "1");
        map.put("b", "2");
        Document doc1 = new Document("time", new Date().getTime()).append("name", "001").append("x", 5).append("y", 5).append("data", map);
        Document doc2 = new Document("time", new Date().getTime()).append("name", "001").append("x", 5).append("y", 5).append("data", map);
        Document doc3 = new Document("time", new Date().getTime()).append("name", "002").append("x", 10).append("y", 10).append("data", map);
        Document doc4 = new Document("time", new Date().getTime()).append("name", "002").append("x", 10).append("y", 10).append("data", map);
        List<Document> docs = new ArrayList<>();
        docs.add(doc3);
        docs.add(doc4);
        MongoDataSource mongoDataSource = new MongoDataSource();
        MongoCollection<Document> mongoCollection = mongoDataSource.getDB().getCollection(layerTable);
        //增
        mongoCollection.insertOne(doc1);
        mongoCollection.insertOne(doc2);
        mongoCollection.insertMany(docs);
        //改
        map.put("c", "3");
        mongoCollection.updateMany(Filters.eq("name", "001"), new Document("$set", new Document("data", map)));
        //查 (x>=5 or y<5) and (x<=5 or y>5)
        // x >= 5
        BasicDBObject X5 = new BasicDBObject("x", new BasicDBObject("$gte", 5));
        // y < 5
        BasicDBObject y5 = new BasicDBObject("y", new BasicDBObject("$lt", 5));
        // x <= 5
        BasicDBObject x5 = new BasicDBObject("x", new BasicDBObject("$lte", 5));
        // y > 5
        BasicDBObject Y5 = new BasicDBObject("y", new BasicDBObject("$gt", 5));
        // (x>=5 or y<5)
        BasicDBObject X5ory5 = new BasicDBObject("$or", Arrays.asList(X5, y5));
        // (x<=5 or y>5)
        BasicDBObject x5orY5 = new BasicDBObject("$or", Arrays.asList(x5, Y5));
        // (x>=5 or y<5) and (x<=5 or y>5)
        BasicDBObject X5ory5Andx5orY5 = new BasicDBObject("$and", Arrays.asList(X5ory5, x5orY5));
        FindIterable<Document> results = mongoCollection.find(X5ory5Andx5orY5);
        MongoCursor<Document> iterator = results.iterator();
        ArrayList<Document> list = new ArrayList<Document>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        double num = mongoCollection.count(new Document());
        //删
        mongoCollection.deleteMany(new Document());
        mongoDataSource.closeMongoClient();
        System.out.println(list.size());
    }
}
