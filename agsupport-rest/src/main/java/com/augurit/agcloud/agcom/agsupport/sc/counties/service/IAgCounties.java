package com.augurit.agcloud.agcom.agsupport.sc.counties.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgCounties;

import java.util.List;

/**
 * Created by czh on 2018-05-29.
 */
public interface IAgCounties {

    List<AgCounties> getCounties() throws Exception;

    List<AgCounties> getTown(String xzqhdmCounties) throws Exception;
}
