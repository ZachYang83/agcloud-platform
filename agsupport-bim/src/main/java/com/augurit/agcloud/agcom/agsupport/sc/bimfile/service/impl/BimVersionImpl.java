package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.io.UploadFile;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimVersion;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimVersionCompare;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimFileMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimVersionCompareMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimVersionMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.reslut.BimVersionCompareUtil;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.reslut.MultipartFileParam;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimVersion;
import com.augurit.agcloud.agcom.agsupport.util.AgDicUtils;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.reslut.FileMD5Util;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName BimFileImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2019-12-12 13:37
 * @Version 1.0
 **/
@Service
public class BimVersionImpl implements IBimVersion {

    private static final String BIM_PATH = "ui-static/agcloud/agcom/ui/sc/bimFile/file/";
    private static final long CHUNK_SIZE = 104857600;
    @Value("${bim.compare.resFilePath}")
    private String bimCompareResFilePath;
    @Value("${bim.compare.exeFilePath}")
    private String bimCompareExeFilePath;
    @Value("${upload.filePath}")
    private String uploadFilePath;

    @Autowired
    AgBimVersionMapper agBimVersionMapper;
    @Autowired
    AgBimVersionCompareMapper agBimVersionCompareMapper;
    @Autowired
    AgBimFileMapper agBimFileMapper;

    @Autowired
    private AgDicUtils agDicUtils;


    @Override
    public PageInfo<AgBimVersion> getByOrKeyWords(String pkId, String keyword, Page page) {
        PageHelper.startPage(page);
        List<AgBimVersion> list = agBimVersionMapper.getByOrKeyWords(pkId, keyword);
        List<AgDic> stateList = agDicUtils.getAgDicByTypeCode("BIM_VERSION_STATE");
        List<AgDic> changeTypeList = agDicUtils.getAgDicByTypeCode("BIM_CHANGE_TYPE");
        list.forEach(x -> {
            stateList.stream()
                    .filter(state -> state.getCode().equals(x.getIsCurrent()))
                    .findFirst()
                    .ifPresent(agDic -> x.setStateName(agDic.getName()));
            changeTypeList.stream()
                    .filter(changeType -> changeType.getCode().equals(x.getChangeType()))
                    .findFirst()
                    .ifPresent(agDic -> x.setChangeTypeName(agDic.getName()));
        });
        return new PageInfo<>(list);
    }

    @Override
    public AgBimVersion getInUseByPkId(String pkId) {
        return agBimVersionMapper.getInUseByPkId(pkId);
    }

    @Override
    public AgBimVersionCompare bimCompare(String id1, String id2) {
        AgBimVersion bimVersion1 = agBimVersionMapper.getById(id1);
        AgBimVersion bimVersion2 = agBimVersionMapper.getById(id2);
        String resPath = bimCompareResFilePath + id1 + "_" + id2 + "_" + "result.json";
        String bimVersionFilePath1 = FileUtil.getPath() + bimVersion1.getBimPath();
        String bimVersionFilePath2 = FileUtil.getPath() + bimVersion2.getBimPath();
        BimVersionCompareUtil.bimVersionCompare(bimCompareExeFilePath, resPath, bimVersionFilePath1, bimVersionFilePath2);
        String s = BimVersionCompareUtil.getCompareContent(resPath);
        AgBimVersionCompare agBimVersionCompare = agBimVersionCompareMapper.getByUnique(bimVersion1.getPkId(), bimVersion1.getId(), bimVersion2.getId());
        if (null != agBimVersionCompare && StringUtils.isNotBlank(agBimVersionCompare.getId())) {
            agBimVersionCompare.setUpdateTime(new Date());
            agBimVersionCompare.setResultFilePath(resPath);
            agBimVersionCompare.setResultContent(s);
            agBimVersionCompareMapper.update(agBimVersionCompare);
        } else {
            agBimVersionCompare = new AgBimVersionCompare();
            agBimVersionCompare.setResultFilePath(resPath);
            agBimVersionCompare.setId(UUID.randomUUID().toString());
            agBimVersionCompare.setBimFileId(bimVersion1.getPkId());
            agBimVersionCompare.setBimVersionId1(bimVersion1.getId());
            agBimVersionCompare.setBimVersionId2(bimVersion2.getId());
            agBimVersionCompare.setResultContent(s);
            agBimVersionCompare.setCreateTime(new Date());
            agBimVersionCompareMapper.save(agBimVersionCompare);
        }
        return agBimVersionCompare;
    }

    @Override
    public AgBimVersionCompare getCompareData(String id1, String id2) {
        AgBimVersion bimVersion1 = agBimVersionMapper.getById(id1);
        AgBimVersion bimVersion2 = agBimVersionMapper.getById(id2);
        return agBimVersionCompareMapper.getByUnique(bimVersion1.getPkId(), bimVersion1.getId(), bimVersion2.getId());
    }

    @Override
    public String findMaxVersion(String bimId) {
        return agBimVersionMapper.findMaxVersion(bimId);
    }


    @Override
    public boolean add(AgBimVersion agBimVersion) {
        if ("IN_USE".equals(agBimVersion.getIsCurrent())) {
            agBimVersionMapper.setAllNotUse(agBimVersion.getPkId());
        }
        return (agBimVersionMapper.add(agBimVersion) > 0);
    }

    @Override
    public boolean deleteById(String id) throws Exception {
        AgBimVersion agBimVersion = agBimVersionMapper.getById(id);
        if (null != agBimVersion) {
            deleteFileByVersion(agBimVersion);
            return (agBimVersionMapper.deleteById(id) > 0);
        }
        return false;
    }

    /**
     * 删除bimversion时判断是否有其他版本指向同一个文件，如果有不删除文件，否则删除文件
     *
     * @param agBimVersion
     * @return
     */
    public boolean deleteFileByVersion(AgBimVersion agBimVersion) {
        try {
            List<AgBimVersion> versionsByMd5 = agBimVersionMapper.getByMd5(agBimVersion.getBimMd5());
            boolean noOther = true;
            for (int i = 0; i < versionsByMd5.size(); i++) {
                if (!agBimVersion.getId().equals(versionsByMd5.get(i).getId())) {
                    noOther = false;
                    break;
                }
            }
            if (noOther) {
                FileUtil.deleteFolder(FileUtil.getPath() + BIM_PATH + agBimVersion.getBimMd5());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteMany(List<String> idsList) throws Exception {
        try {
            List<AgBimVersion> agBimVersionList = agBimVersionMapper.getByIds(idsList);
            if (null != agBimVersionList && agBimVersionList.size() > 0) {
                for (int i = 0; i < agBimVersionList.size(); i++) {
                    AgBimVersion agBimVersion = agBimVersionList.get(i);
                    deleteFileByVersion(agBimVersion);
                }
            }
            return (agBimVersionMapper.deleteMany(idsList) > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(AgBimVersion agBimVersion) {
        if ("IN_USE".equals(agBimVersion.getIsCurrent())) {
            agBimVersionMapper.setAllNotUse(agBimVersion.getPkId());
        }
        return (agBimVersionMapper.update(agBimVersion) > 0);
    }

    /**
     * 大文件上传
     *
     * @param param
     * @throws RuntimeException
     */
    @Override
    public boolean uploadFileByMappedByteBuffer(MultipartFileParam param) throws Exception {
        String fileName = param.getName();
        String uploadDirPath = uploadFilePath + BIM_PATH + param.getMd5();
        String tempFileName = fileName + "_tmp";
        File tmpDir = new File(uploadDirPath);
        File tmpFile = new File(uploadDirPath, tempFileName);

        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        RandomAccessFile tempRaf = new RandomAccessFile(tmpFile, "rw");
        FileChannel fileChannel = tempRaf.getChannel();

        //写入该分片数据
        long offset = CHUNK_SIZE * param.getChunk();
        byte[] fileData = param.getFile().getBytes();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
        mappedByteBuffer.put(fileData);
        //释放
        FileMD5Util.freedMappedByteBuffer(mappedByteBuffer);
        fileChannel.close();
        boolean isOk = FileMD5Util.checkAndSetUploadProgress(param, uploadDirPath);
        if (isOk) {
            String folder = (BIM_PATH + param.getMd5());
            UploadFile uploadFile = FileUtil.getUploadFiles(tmpFile, folder);
            uploadFile.setPath(uploadFile.getPath().replaceAll("_tmp", ""));
            uploadFile.setUrl(uploadFile.getUrl().replaceAll("_tmp", ""));
            uploadFile.setExtension(uploadFile.getExtension().replaceAll("_tmp", ""));
            return FileUtil.saveFile(tmpFile, (uploadFilePath + uploadFile.getPath()));
        }
        return false;
    }

    @Override
    public Integer completeVersion(AgBimVersion agBimVersion, MultipartFileParam param) throws Exception {
        if (StringUtils.isBlank(agBimVersion.getChangeVersion())) {
            AgBimVersion latestChangeVersion = agBimVersionMapper.getLatestChangeVersion(agBimVersion.getPkId());
            if (null != latestChangeVersion) {
                agBimVersion.setChangeVersion(String.valueOf(Double.parseDouble(latestChangeVersion.getChangeVersion()) + 1.0D));
            } else {
                agBimVersion.setChangeVersion("1.0");
            }
        } else {
            Boolean checkBimVersion = checkBimVersion(agBimVersion);
            if (!checkBimVersion) {
                return 2;
            }
        }
        if (null == param.getFile()) {
            return 0;
        }
        try {
            String fileName = param.getName();
            String uploadDirPath = uploadFilePath + BIM_PATH + param.getMd5();
            String tempFileName = fileName + "_tmp";
            File tmpFile = new File(uploadDirPath, tempFileName);
            String folder = (BIM_PATH + param.getMd5());
            UploadFile uploadFile = FileUtil.getUploadFiles(tmpFile, folder);

            if (StringUtils.isBlank(agBimVersion.getId())) {
                agBimVersion.setId(UUID.randomUUID().toString());
            }
            agBimVersion.setBimMd5(param.getMd5());
            agBimVersion.setBimLength(Integer.toString(new Double(Math.ceil(param.getSize() / 1024)).intValue()));

            agBimVersion.setBimExtension(uploadFile.getExtension().replaceAll("_tmp", ""));
            if (StringUtils.isBlank(agBimVersion.getBimPath())) {
                agBimVersion.setBimPath(uploadFile.getPath().replaceAll("_tmp", ""));
            }
            if (StringUtils.isBlank(agBimVersion.getBimUrl())) {
                agBimVersion.setBimUrl(uploadFile.getUrl().replaceAll("_tmp", ""));
            }
            if (StringUtils.isBlank(agBimVersion.getChangeName())) {
                agBimVersion.setChangeName(uploadFile.getName());
            }
            return 1;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<AgBimVersion> getByMd5(String md5) {
        return agBimVersionMapper.getByMd5(md5);
    }

    @Override
    public Boolean completeVersionWithExistVersion(AgBimVersion agBimVersion) {
        try {
            AgBimVersion bimVersionByMd5 = agBimVersionMapper.getByMd5(agBimVersion.getBimMd5()).get(0);
            agBimVersion.setId(UUID.randomUUID().toString());
            agBimVersion.setBimPath(bimVersionByMd5.getBimPath());
            agBimVersion.setBimExtension(bimVersionByMd5.getBimExtension());
            agBimVersion.setBimUrl(bimVersionByMd5.getBimUrl());
            agBimVersion.setBimLength(bimVersionByMd5.getBimLength());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 校验版本号是否已经存在
     *
     * @param agBimVersion
     * @return true: 通过校验  false：不通过（有重复）
     */
    private Boolean checkBimVersion(AgBimVersion agBimVersion) {
        List<AgBimVersion> agBimVersionList = agBimVersionMapper.getByOrKeyWords(agBimVersion.getPkId(), null);
        if (null == agBimVersionList || agBimVersionList.size() == 0) {
            return true;
        }

        String changeVersion = agBimVersion.getChangeVersion();
        if (StringUtils.isNotBlank(changeVersion)) {
            for (int i = 0; i < agBimVersionList.size(); i++) {
                if (agBimVersionList.get(i).getId().equals(agBimVersion.getId())) {
                    continue;
                }
                if (changeVersion.equals(agBimVersionList.get(i).getChangeVersion())) {
                    return false;
                }
            }
        }
        return true;
    }
}
