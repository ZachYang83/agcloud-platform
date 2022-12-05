package com.augurit.agcloud.agcom.agsupport.util;


import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.common.exception.ExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: libc
 * @Description: 数据转换工具类； DO转DTO （数据库对象转业务对象）
 * @Date: 2020/12/10 17:28
 * @Version: 1.0
 */
public class BeanHelper {

    private static Logger logger = LoggerFactory.getLogger(BeanHelper.class);

    public static <T> T copyProperties(Object source, Class<T> target){
        try {
            T t = target.newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (Exception e) {
            logger.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new AgCloudException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }
    }

    public static <T> List<T> copyWithCollection(List<?> sourceList, Class<T> target){
        try {
            return sourceList.stream().map(s -> copyProperties(s, target)).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new AgCloudException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }
    }

    public static <T> Set<T> copyWithCollection(Set<?> sourceList, Class<T> target){
        try {
            return sourceList.stream().map(s -> copyProperties(s, target)).collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new AgCloudException(ExceptionEnum.DATA_TRANSFER_ERROR);
        }
    }
}
