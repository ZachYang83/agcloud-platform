package com.augurit.agcloud.agcom.agsupport.common.advice;

import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.common.exception.ExceptionResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Version  1.0
 * @Author libc
 * @Description 统一异常处理类
 * @Date 2020/9/8 11:55
 */
@RestControllerAdvice
public class ExceptionHandlerController {

    /**
     * ExceptionHandler(AgCloudException.class)
     * 表示当前处理器只处理AgCloudException异常
     * @return
     */
    @ExceptionHandler(AgCloudException.class)
    public ExceptionResult handlerException(AgCloudException e){
//        System.out.println(111);
        return new ExceptionResult(e);
    }

}
