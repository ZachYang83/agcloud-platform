package com.augurit.agcloud.agcom.agsupport.sc.address.service;


import java.util.List;
import java.util.Map;

/**
 * Created by Augurit on 2017-07-06.
 */
public interface IAgAddress {
    /**
     * 匹配地址
     * @param address 需要匹配的地址
     * @return
     */
    List matchAddress(String address) throws Exception;

    /**
     * 批量匹配地址
     * @param addressList 需要匹配地址集合
     * @return
     */
    Map<String,List<Map>> matchAddressList(List<String> addressList) throws Exception;

    /**
     * 匹配地址
     * @param addressMap 地址对象
     * @param addressCol 地址列名
     * @return
     */
    List<Map> matchAddressMap(Map addressMap, String addressCol) throws Exception;

    /**
     * 批量匹配地址
     * @param addressObjList 地址对象集合
     * @param addressCol 地址列名
     * @return
     */
    Map<String,List<Map>> matchAddressMapList(List<Map> addressObjList, String addressCol) throws Exception;
}
