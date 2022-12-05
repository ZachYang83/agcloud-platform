package com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgIdentifyMancarSource;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgIdentifyMancarSourceExample;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgIdentifyMancarSourceMapper;
import com.augurit.agcloud.agcom.agsupport.sc.identify.mancar.service.IAgIdentifyMancarSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: qinyg
 * @Date: 2020/12/28 11:35
 * @tips:
 */
@Service
public class AgIdentifyMancarSourceServiceImpl implements IAgIdentifyMancarSourceService {
    //资源存储路径根目录
    private static final String IDENTIFY_ROOT_FILE_PATH = "identify";

    private static final Logger log = LoggerFactory.getLogger(AgIdentifyMancarSourceServiceImpl.class);

    @Value("${upload.filePath}")
    private String baseOutPath;


    @Autowired
    private AgIdentifyMancarSourceMapper identifyMancarSourceMapper;

    @Override
    public List<AgIdentifyMancarSource> find(AgIdentifyMancarSource source) {
        AgIdentifyMancarSourceExample example = new AgIdentifyMancarSourceExample();
        AgIdentifyMancarSourceExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(source.getStatus())){
            criteria.andStatusEqualTo(source.getStatus());
        }
        return identifyMancarSourceMapper.selectByExample(example);
    }

    @Override
    public void add(AgIdentifyMancarSource source, MultipartFile file) {
        String id = UUID.randomUUID().toString();
        if(!StringUtils.isEmpty(source.getId())){
            id = source.getId();
        }
        String originalFilename = file.getOriginalFilename();
        //数据库文件存储路径
        String pathDatabase = IDENTIFY_ROOT_FILE_PATH + "/" + id + "/" + originalFilename;
        String path = baseOutPath + IDENTIFY_ROOT_FILE_PATH + File.separator + id + File.separator + originalFilename;
        //文件上传
        File upload = new File(path);
        if (!upload.exists()) {
            upload.mkdirs();
        }
        try {
            file.transferTo(upload);
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new SourceException("文件上传失败");
        }

        //赋值
        source.setCreateTime(new Date());
        source.setId(id);
        source.setFileName(originalFilename);
        source.setFilePath(pathDatabase);
        source.setStatus("0");//默认0，未识别
        //如果status不为null，赋值
        if(!StringUtils.isEmpty(source.getStatus())){
            source.setStatus(source.getStatus());
        }
        identifyMancarSourceMapper.insert(source);
    }

    @Override
    public void updateSourceStatus(String id) {
        AgIdentifyMancarSource source = new AgIdentifyMancarSource();
        source.setId(id);
        source.setStatus("1");//已识别
        source.setModifyTime(new Date());

        //更新条件
        AgIdentifyMancarSourceExample example = new AgIdentifyMancarSourceExample();
        example.createCriteria().andIdEqualTo(id).andStatusEqualTo("0");
        identifyMancarSourceMapper.updateByExampleSelective(source, example);
    }

    @Override
    public AgIdentifyMancarSource getById(String id) {
        return identifyMancarSourceMapper.selectByPrimaryKey(id);
    }
}
