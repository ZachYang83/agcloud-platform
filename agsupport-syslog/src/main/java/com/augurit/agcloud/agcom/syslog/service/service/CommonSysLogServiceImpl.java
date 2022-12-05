package com.augurit.agcloud.agcom.syslog.service.service;

import com.augurit.agcloud.agcom.syslog.domain.CommonSysLog;
import com.augurit.agcloud.agcom.syslog.mapper.CommonSysLogMapper;
import com.augurit.agcloud.bsc.upload.MongoDbAchieve;
import com.augurit.agcloud.bsc.upload.UploadFileStrategy;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CommonSysLogServiceImpl implements CommonSysLogService{
    @Autowired
    private CommonSysLogMapper commonSysLogMapper;
    @Autowired
    private UploaderFactory uploaderFactory;
    private MongoTemplate mongoTemplate;
    @Override
    @Async
    public void save(CommonSysLog commonSysLog) {
        commonSysLogMapper.save(commonSysLog);
    }

    @Override
    @Async
    public void saveToMongodb(CommonSysLog commonSysLog, String collectionName) {
        MongoTemplate mongoTemplate = getMongoTemplate();
        if (mongoTemplate != null){
            mongoTemplate.insert(commonSysLog, collectionName);
        }
    }

    public MongoTemplate getMongoTemplate() {
        if (this.mongoTemplate == null){
            UploadFileStrategy uploadFileStrategy = uploaderFactory.create(UploadType.MONGODB.getValue());
            MongoDbAchieve mongoDbAchieve = (MongoDbAchieve) uploadFileStrategy;
            MongoTemplate mongoTemplate = mongoDbAchieve.getMongoTemplate();
            this.mongoTemplate = mongoTemplate;
        }
        return this.mongoTemplate;
    }
}
