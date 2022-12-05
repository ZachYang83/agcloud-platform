package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimRelationFile;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimRelationFileMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimRelationFile;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * bim模型关联文件service
 * Created by fanghh on 2019/12/5.
 */
@Service
public class BimRelationFileImpl implements IBimRelationFile {



    @Autowired
    private AgBimRelationFileMapper bimRelationFileMapper;

    @Override
    public PageInfo findBimRelationFile(String bimId, Page page) {
        PageHelper.startPage(page);
        List<AgBimRelationFile> bimRelationFile = bimRelationFileMapper.findBimRelationFile(bimId);
        return new PageInfo<>(bimRelationFile);
    }

    @Override
    public int delete(String id) {
        return bimRelationFileMapper.delete(id);
    }

    @Override
    public void save(AgBimRelationFile relationFile) {
        relationFile.setId(UUID.randomUUID().toString());
        bimRelationFileMapper.save(relationFile);
    }

    @Override
    public void saveBimRelationFile(String bimId,List<String> fileIds) {
        List<AgBimRelationFile> bimRelationFiles = fileIds.stream().map(x -> {
            AgBimRelationFile relationFile = new AgBimRelationFile();
            relationFile.setFileId(x);
            relationFile.setBimId(bimId);
            return relationFile;
        }).collect(Collectors.toList());
        bimRelationFiles.forEach(this::save);
    }

    @Override
    public List<String> findFileIdByBimId(String bimId) {
        List<AgBimRelationFile> bimRelationFiles = bimRelationFileMapper.findBimRelationFile(bimId);
        return bimRelationFiles.stream().map(AgBimRelationFile::getFileId).collect(Collectors.toList());
    }
}
