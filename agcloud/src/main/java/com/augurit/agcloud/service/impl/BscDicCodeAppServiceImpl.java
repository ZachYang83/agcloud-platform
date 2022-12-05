package com.augurit.agcloud.service.impl;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.mapper.BscDicCodeAppMapper;
import com.augurit.agcloud.service.BscDicCodeAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName BscDicCodeService
 * @Description TODO
 * @Author Administrator
 * @Date 2019/11/25 11:48
 * @Version 1.0
 **/
@Service
@Transactional
public class BscDicCodeAppServiceImpl implements BscDicCodeAppService {

    private static Logger logger = LoggerFactory.getLogger(com.augurit.agcloud.bsc.sc.dic.code.service.impl.BscDicCodeServiceImpl.class);
    @Autowired
    private BscDicCodeAppMapper bscDicCodeMapper;


    public List<BscDicCodeItem> getRootOrgActiveItemsByTypeCode(String typeCode) {
        return this.getActiveItemsByTypeCode(typeCode, "A");
    }

    public List<BscDicCodeItem> getActiveItemsByTypeCode(String typeCode, String orgId) {
        if (StringUtils.isBlank(typeCode)) {
            throw new InvalidParameterException(new Object[]{"参数typeCode为空!"});
        } else {
            List<BscDicCodeItem> itemList = bscDicCodeMapper.getActiveItemsByTypeCode(typeCode, orgId);
            return itemList;
        }
    }
}
