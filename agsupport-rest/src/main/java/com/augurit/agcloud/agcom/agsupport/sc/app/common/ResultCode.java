package com.augurit.agcloud.agcom.agsupport.sc.app.common;

/**
 * @Auther: zhangmingyang
 * @Date: 2018/9/29 17:31
 * @Description: 接口返回值错误码信息
 */
public enum ResultCode {
    /* 成功状态码 */
    SUCCESS(0, "成功"),
    FAILURE(1, "内部接口调用异常"),
    TOKEN_INVALID(2, "token无效!");
    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
