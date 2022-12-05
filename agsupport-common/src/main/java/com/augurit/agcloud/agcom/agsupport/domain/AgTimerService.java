package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * Created by caokp on 2017-08-29.
 */
public class AgTimerService {

    private String id;
    private String name;            //名称
    private String batPath;         //批处理文件路径
    private String type;            //类型，'delay'延时运行，'timing'定时运行
    private String delay;           //延时时间
    private String timing;          //定时时间
    private String circulated;      //是否循环
    private String timeInterval;    //时间间隔
    private String port;            //端口
    private String state;           //运行状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatPath() {
        return batPath;
    }

    public void setBatPath(String batPath) {
        this.batPath = batPath;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCirculated() {
        return circulated;
    }

    public void setCirculated(String circulated) {
        this.circulated = circulated;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }
}
