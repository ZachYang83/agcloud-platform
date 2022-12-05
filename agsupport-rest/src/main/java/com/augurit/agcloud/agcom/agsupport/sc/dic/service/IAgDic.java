package com.augurit.agcloud.agcom.agsupport.sc.dic.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgDic;

import java.util.List;

/**
 * Created by Augurit on 2017-04-18.
 */
public interface IAgDic {
    /**
     * 根据typeCode获取字典
     *
     * @param typeCode
     * @return
     * @throws Exception
     */
    List<AgDic> getAgDicByTypeCode(String typeCode) throws Exception;

    /**
     * 按code查询
     *
     * @param code
     * @return
     * @throws Exception
     */
    AgDic findAgDicByCode(String code) throws Exception;

    /**
     * 按照名称查询坐标系统的值
     *
     * @param name
     * @return
     * @throws Exception
     */
    AgDic findValueByCoorName(String name) throws Exception;

    /**
     * @param name
     * @return
     * @throws Exception
     */
    AgDic findValueByCoordTransName(String name) throws Exception;

    AgDic getAgDicByTypeCodeAndItemName(String typeCode,String itemName) throws Exception;
}
