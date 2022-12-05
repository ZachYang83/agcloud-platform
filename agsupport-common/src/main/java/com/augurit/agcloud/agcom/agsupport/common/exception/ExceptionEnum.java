package com.augurit.agcloud.agcom.agsupport.common.exception;

/**
 * @Version  1.0
 * @Author libc
 * @Description 统一异常状态枚举
 * @Date 2020/9/4 10:37
 */
public enum ExceptionEnum {
    INVALID_FILE_TYPE(400, "无效的文件类型！"),
    INVALID_PARAM_ERROR(400, "无效的请求参数！"),
    INVALID_PHONE_NUMBER(400, "无效的手机号码"),
    INVALID_VERIFY_CODE(400, "验证码错误！"),
    INVALID_USERNAME_PASSWORD(400, "无效的用户名和密码！"),
    INVALID_SERVER_ID_SECRET(400, "无效的服务id和密钥！"),
    INVALID_NOTIFY_PARAM(400, "回调参数有误！"),
    INVALID_NOTIFY_SIGN(400, "回调签名有误！"),


    DATA_TRANSFER_ERROR(500, "数据转换异常！"),
    INSERT_OPERATION_FAIL(500, "新增操作失败！"),
    UPDATE_OPERATION_FAIL(500, "更新操作失败！"),
    DELETE_OPERATION_FAIL(500, "删除操作失败！"),
    FILE_UPLOAD_ERROR(500, "文件上传失败！"),
    FILE_DELETE_ERROR(500, "文件删除失败！"),
    FILE_WRITER_ERROR(500, "文件写入失败！"),
    FILE_READ_ERROR(500, "文件写出失败！"),
    FILE_SERVER_ERROR(500, "文件服务器异常！"),
    STREAM_RESOURCES_CLOSE_ERROR(500,"流资源关闭失败！"),
    DIRECTORY_WRITER_ERROR(500, "目录写入失败！"),
    SEND_MESSAGE_ERROR(500, "短信发送失败！"),
    EXCEL_IMPORT_ERROR(500, "excel表格导入失败！"),
    PDF_CREATE_ERROR(500, "pdf表格生成失败！"),

    UNAUTHORIZED(401, "登录失效或未登录！");

    private int status;
    private String message;

    //枚举对象中的构造方法不能为public，不能被外部调用
    ExceptionEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}