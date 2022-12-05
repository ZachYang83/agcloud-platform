package com.augurit.agcloud.agcom.agsupport.sc.reg.service;

import java.util.List;

/**
 * Created by caokp on 2017-08-07.
 */
public interface IAgRegService {

    /**
     * 获取服务注册年份
     *
     * @return
     * @throws Exception
     */
    List<String> getYears() throws Exception;
}
