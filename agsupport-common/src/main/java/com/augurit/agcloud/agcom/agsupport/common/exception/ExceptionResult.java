package com.augurit.agcloud.agcom.agsupport.common.exception;

import org.joda.time.DateTime;

/**
 * @Version  1.0
 * @Author libc
 * @Description 异常时返回一个对象
 * @Date 2020/9/4 10:45
 */
public class ExceptionResult {
    private int status;
    private boolean success;
    private String message;
    private String timestamp;

    public ExceptionResult(AgCloudException e) {
        this.success = false;
        this.status = e.getStatus();
        this.message = e.getMessage();
        this.timestamp = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
    }

    public boolean isSuccess() {
        return this.success;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}