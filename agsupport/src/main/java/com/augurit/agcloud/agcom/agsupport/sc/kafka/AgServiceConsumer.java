package com.augurit.agcloud.agcom.agsupport.sc.kafka;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.agcom.agsupport.common.mongodb.MongoDbService;
import com.augurit.agcloud.agcom.agsupport.common.mongodb.MongodbConstant;
import com.augurit.agcloud.agcom.agsupport.domain.AgServiceLog;
import com.augurit.agcloud.agcom.agsupport.domain.AgSysLog;
import com.augurit.agcloud.agcom.agsupport.util.DateUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

/**
 * @author zhangmingyang
 * @Description: 服务日志消费类
 * @date 2019-09-25 14:32
 */
@Component
@ConditionalOnProperty(
        prefix = "kafka",
        name = "enable",
        havingValue = "true"
)
public class AgServiceConsumer {
    private static final Logger log = LoggerFactory.getLogger(AgServiceConsumer.class);
    @Autowired
    private MongoDbService mongoDbService;

    /**
     * 服务日期存入mongodb
     * @param consumerRecord
     */
    @KafkaListener(topics = {"agsupport-service-log"})
    public void consumer(ConsumerRecord<?,?> consumerRecord){
        Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if(kafkaMessage.isPresent()){
            //得到Optional实例中的值
            Object message = kafkaMessage.get();
            AgServiceLog agServiceLog = JSON.parseObject(message.toString(), AgServiceLog.class);
            Date accessTime = agServiceLog.getAccessTime();
            agServiceLog.setVisitDay(DateUtil.formatDate(DateUtil.FORMAT1,accessTime));
            //log.info("消费消息:"+agServiceLog);
            MongoTemplate mongoTemplate = mongoDbService.getMongoTemplate();
            if (mongoTemplate != null){
                mongoTemplate.insert(agServiceLog, MongodbConstant.AGCOM_SERVICE_LOG);
            }
        }
    }

    /**
     * rest服务调用信息存入mongodb
     *
     * @param consumerRecord
     */
    @KafkaListener(topics = {"agsupport-rest-service-log"})
    public void consumerRestServiceLog(ConsumerRecord<?, ?> consumerRecord) {
        Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
            //得到Optional实例中的值
            Object message = kafkaMessage.get();
            AgSysLog agSysLog = JSON.parseObject(message.toString(), AgSysLog.class);
            MongoTemplate mongoTemplate = mongoDbService.getMongoTemplate();
            if (mongoTemplate != null) {
                mongoTemplate.insert(agSysLog, MongodbConstant.AGSUPPORT_REST_SERVICE_LOG);
            }
        }
    }
}
