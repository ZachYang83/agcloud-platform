package com.augurit.agcloud.agcom.agsupport.sc.geocoding.service;

import java.util.List;
import java.util.Map;

/**
 * Created by czh on 2018-02-05.
 */
public interface IAgGeocoding {

    List<Map> searchByAddress(String address) throws Exception;

    List<Map> searchByPoint(String wkt, String type) throws Exception;
}
