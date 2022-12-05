package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.common.exception.ExceptionEnum;
import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dbuilding;
import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dbuildingExample;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.Agcim3dbuildingMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service.IAgcim3dbuildingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: libc
 * @Description: Agcim 三维BIM 业务实现类
 * @Date: 2020/11/25 14:14
 * @Version: 1.0
 */
@Service
public class Agcim3dbuildingServiceImpl implements IAgcim3dbuildingService {

    private Logger logger = LoggerFactory.getLogger(Agcim3dbuildingServiceImpl.class);

    /**
     * Date 时间字符串格式化
     */
    private final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    @Autowired
    private Agcim3dbuildingMapper agcim3dbuildingMapper;

    /**
     * @Author: libc
     * @Date: 2020/11/25 14:14
     * @tips: 新增
     */
    @Override
    @Transactional
    public void add(Agcim3dbuilding agcim3dbuilding) {
        try {
            agcim3dbuilding.setId(UUID.randomUUID().toString());
            agcim3dbuilding.setCreatetime(new SimpleDateFormat(DATE_TIME_FORMAT).format(new Date(System.currentTimeMillis())));
            agcim3dbuildingMapper.insertSelective(agcim3dbuilding);
        } catch (AgCloudException agCloudException) {
            throw new AgCloudException(agCloudException.getStatus(), agCloudException.getMessage());
        } catch (Exception e) {
            logger.error(ExceptionEnum.INSERT_OPERATION_FAIL.getMessage(), e);
            throw new AgCloudException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
     * @param infoRelTableName 关联表名称
     * @Author: libc
     * @Date: 2020/11/25 15:32
     * @tips: 根据BIM模型信息表名删除记录
     */
    @Override
    @Transactional
    public void deleteByEntitytable(String infoRelTableName) {
        try {
            Agcim3dbuildingExample example = new Agcim3dbuildingExample();
            example.createCriteria().andEntitytableEqualTo(infoRelTableName);
            agcim3dbuildingMapper.deleteByExample(example);
        } catch (AgCloudException agCloudException) {
            throw new AgCloudException(agCloudException.getStatus(), agCloudException.getMessage());
        } catch (Exception e) {
            logger.error(ExceptionEnum.DELETE_OPERATION_FAIL.getMessage(), e);
            throw new AgCloudException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }

    /**
     * @param param 查询条件
     * @return List<Agcim3dbuilding>
     * @Author: libc
     * @Date: 2020/12/10 16:56
     * @tips: 根据条件获取模型集合
     */
    @Override
    public List<Agcim3dbuilding> listBuildings(Agcim3dbuilding param) {
        Agcim3dbuildingExample example = new Agcim3dbuildingExample();
        getAgcim3dentityExample(example, param);
        return agcim3dbuildingMapper.selectByExample(example);
    }


    private void getAgcim3dentityExample(Agcim3dbuildingExample example, Agcim3dbuilding param) {
        Agcim3dbuildingExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(param.getId())) {
            criteria.andIdEqualTo(param.getId());
        }
        if (!StringUtils.isEmpty(param.getBuildingname())) {
            criteria.andBuildingnameEqualTo(param.getBuildingname());
        }
    }
}
