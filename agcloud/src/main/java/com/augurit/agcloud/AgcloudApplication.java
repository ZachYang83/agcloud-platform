package com.augurit.agcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan(basePackages = {"com.augurit", "org.flowable.app"})//加入flowabale包扫描
@MapperScan(basePackages = {"com.augurit.**.mapper", "org.flowable.**.mapper"})//加入mapper扫描
public class AgcloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgcloudApplication.class, args);
    }

}
