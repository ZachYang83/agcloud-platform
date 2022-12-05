package com.augurit.agcloud.agcom.agsupport.common.exception;

/**
 * Created with IntelliJ IDEA.
 *
 *  自定义异常类
 *
 * @Auther: qinyg
 * @Date: 2020/07
 * @Description:
 */
public class SourceException extends RuntimeException{
    public SourceException(){
        super();
    }
    public SourceException(String message){
        super(message);
    }
}
