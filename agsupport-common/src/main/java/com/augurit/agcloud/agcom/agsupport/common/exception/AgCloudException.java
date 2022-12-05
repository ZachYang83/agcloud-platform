package com.augurit.agcloud.agcom.agsupport.common.exception;

/**
 * @Version  1.0
 * @Author libc
 * @Description 自定义统一异常对象，来定义异常状态码
 * @Date 2020/9/4 10:46
 */
public class AgCloudException extends RuntimeException{
    private Integer status;

    public AgCloudException(Integer status, String message) {
        super(message);
        this.status = status;
    }

    public AgCloudException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.status = exceptionEnum.getStatus();
    }

    public Integer getStatus() {
        return status;
    }
}
