package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.auto.*;
import com.augurit.agcloud.agcom.agsupport.mapper.*;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.*;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.ISysAgCloudSystemService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.ISysAgHouseService2;
import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.util.*;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/07
 * @Description:
 */
@Service
public class SysAgHouseServiceImpl2 implements ISysAgHouseService2 {

    private static final Logger log = LoggerFactory.getLogger(SysAgHouseServiceImpl2.class);
    private static final String uploadTempPath = "uploadTemp";
    private static final String bim_model_path = "BimModel";

    @Autowired
    private AgHouseMapper agHouseMapper;

    @Autowired
    private ISysAgCloudSystemService sysAgCloudSystemService;
    @Autowired
    private AgHouseCustomMapper houseCustomMapper;
    @Autowired
    private AgSysSettingMapper sysSettingMapper;

    @Value("${upload.filePath}")
    private String filePath;

    public void saveResource(AgHouse resource, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        String path = null;
        //???????????????????????????
        String basePath = getBaseFilePath();
        try {
            path = basePath + File.separator + fileName ;
            //?????????????????????????????????????????????????????????????????????
            String storeFullPath = resource.getStoreFullPath();
            if(!StringUtils.isEmpty(storeFullPath)){
                storeFullPath = storeFullPath.substring(0, storeFullPath.indexOf("/"));
                //??????????????????
                path = basePath + File.separator + storeFullPath + File.separator + fileName;
                //??????????????????
                fileName = storeFullPath + "/" + fileName;
            }
            File upload = new File(path);
            if (!upload.exists()) {
                upload.mkdirs();
            }
            file.transferTo(upload);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //?????????????????????????????????"/"??????,??????filePath???????????????????????????????????????????????????+?????????
       //?????????????????????????????????"/"????????????????????????????????????
        path =  fileName;
        resource.setStoreFullPath(path);
        //?????????????????????
        //?????????????????????????????????????????????
        if (StringUtils.isEmpty(resource.getSourceName())) {
            //???????????????????????????????????????
            resource.setSourceName(fileName);
        } else {
            //??????????????????????????????????????????????????????????????????
            if (!resource.getSourceName().contains("." + suffix)) {
                //?????????????????????,?????????????????????????????????????????????
                resource.setSourceName(resource.getSourceName() + "." + suffix);
            }
        }
        //??????????????????
        if (StringUtils.isEmpty(resource.getOldName())) {
            resource.setOldName(fileName);
        }
        resource.setSuffix(suffix);
        resource.setSize(String.valueOf(file.getSize()));
        resource.setCreateTime(new Date());
        resource.setId(UUID.randomUUID().toString());
        agHouseMapper.insert(resource);
    }

    /**
     * ???????????????????????????FileEntity??????
     * @param resource
     * @param entity
     * @throws RuntimeException
     */
    public void saveResource(AgHouse resource, FileEntity entity) throws RuntimeException {
        String fileName = entity.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        String path = null;

        //???????????????????????????
        String basePath = getBaseFilePath();
        try {
            path = basePath + File.separator +  fileName;
            File upload = new File(path, "");
            writeFileToDisk(entity, upload);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.info("------????????????,???????????????----" + e.getMessage());
            throw new RuntimeException("????????????,???????????????");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("------??????????????????----" + e.getMessage());
            throw new RuntimeException("??????????????????");
        }
        if (path == null) {
            throw new RuntimeException("??????????????????");
        }
        //?????????????????????????????????"/"??????,??????filePath???????????????????????????????????????????????????+?????????
       //?????????????????????????????????"/"????????????????????????????????????
        path =  fileName;
        resource.setStoreFullPath(path);
        //?????????????????????
        //?????????????????????????????????????????????
        if (StringUtils.isEmpty(resource.getSourceName())) {
            //???????????????????????????????????????
            resource.setSourceName(fileName);
        } else {
            //??????????????????????????????????????????????????????????????????
            if (!resource.getSourceName().contains("." + suffix)) {
                //?????????????????????,?????????????????????????????????????????????
                resource.setSourceName(resource.getSourceName() + "." + suffix);
            }
        }
        //??????????????????
        if (StringUtils.isEmpty(resource.getOldName())) {
            resource.setOldName(fileName);
        }
        resource.setSuffix(suffix);
        resource.setSize(String.valueOf(entity.getFile().length()));
        resource.setCreateTime(new Date());
        resource.setId(UUID.randomUUID().toString());
        agHouseMapper.insert(resource);
    }

    private void writeFileToDisk(FileEntity entity, File upload) throws IOException {
        if (!upload.exists()) {
            if(!upload.getParentFile().exists()){
                upload.getParentFile().mkdirs();
            }
            upload.createNewFile();
        }
        File file = entity.getFile();
        InputStream stream = new FileInputStream(file);
        //?????????,???????????????????????????1024?????????
        OutputStream out = new FileOutputStream(upload);
        byte[] b = new byte[1024];
        int i = 0;
        //???????????????????????????????????????????????????,????????????????????????-1
        while((i = stream.read(b)) != -1){
            //???????????????b??????????????????0????????????i???????????????????????????
            out.write(b,0,i);
        }

        out.close();
        stream.close();
    }


    /**
     * ???????????????
     * @param resource ?????????????????????
     * @param thumbEntity ???????????????
     * @param resourceId ?????????????????????id
     * @return
     */
    private String uploadThumbFile(AgHouse resource, FileEntity thumbEntity, String resourceId) {
        String returnPath = null;
        try {
            String filename = thumbEntity.getName();
            String basePath = getBaseFilePath();
            String path = filename;
            returnPath = filename;
            File upload = new File(basePath + File.separator + path);
            writeFileToDisk(thumbEntity, upload);

            AgHouse agResource = new AgHouse();
            agResource.setSourceId(resource.getSourceId());
            agResource.setUserId(resource.getUserId());
            //????????????
            agResource.setStoreFullPath(returnPath);

            //???????????????????????????
            agResource.setIsShow("0");
            //?????????????????????????????????
            agResource.setType("3");

            //????????????
            agResource.setSourceName(filename);
            //???????????????
            agResource.setOldName(filename);
            String suffix = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
            agResource.setSuffix(suffix);
            agResource.setSize(String.valueOf(thumbEntity.getFile().length()));
            agResource.setCreateTime(new Date());
            agResource.setId(UUID.randomUUID().toString());
            agResource.setSourceId(resourceId);
            agHouseMapper.insert(agResource);
        } catch (Exception e) {
            log.info("?????????????????????-------" + e.getMessage());
            throw new RuntimeException("?????????????????????");
        }
        return returnPath;
    }


    /**
     * ???????????????
     * @param resource ?????????????????????
     * @param thumbFile ???????????????
     * @param resourceId ?????????????????????id
     * @return
     */
    private String uploadThumbFile(AgHouse resource, MultipartFile thumbFile, String resourceId) {
        String returnPath = null;
        try {
            //???????????????????????????
            String storeFullPath = resource.getStoreFullPath();
            String sysStorePath = storeFullPath.substring(0, storeFullPath.indexOf("/"));

            String filename = thumbFile.getOriginalFilename();
            String basePath = getBaseFilePath();
            String path = sysStorePath + File.separator + filename;
            returnPath = sysStorePath + "/" + filename;
            File upload = new File(basePath + File.separator + path);
            if (!upload.exists()) {
                upload.mkdirs();
            }
            thumbFile.transferTo(upload);
            AgHouse agResource = new AgHouse();

            agResource.setSourceId(resource.getSourceId());
            agResource.setUserId(resource.getUserId());

            //???????????????????????????
            agResource.setIsShow("0");
            //?????????????????????????????????
            agResource.setType("3");

            //????????????
            agResource.setSourceName(filename);
            //???????????????
            agResource.setOldName(filename);
            String suffix = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
            agResource.setSuffix(suffix);
            agResource.setSize(String.valueOf(thumbFile.getSize()));
            agResource.setCreateTime(new Date());
            agResource.setId(UUID.randomUUID().toString());
            agResource.setSourceId(resourceId);
            agHouseMapper.insert(agResource);
        } catch (Exception e) {
            log.info("?????????????????????-------" + e.getMessage());
            throw new SourceException("?????????????????????");
        }
        return returnPath;
    }

    /**
     * ???????????????????????????????????????
     * @param resource ????????????
     * @param entity ????????????
     * @param type ?????????????????????1??????????????????2??????????????????3???????????????
     */
    private void packageAndSaveResourceNotShow(AgHouse resource,  FileEntity entity, String type) {
        AgHouse agResource = new AgHouse();
        agResource.setSourceId(resource.getSourceId());
        agResource.setUserId(resource.getUserId());

        //???????????????????????????
        agResource.setIsShow("0");
        //????????????
        agResource.setType(type);

        //?????????????????????????????????id
        String filename = entity.getName();

        //???????????????????????????????????????????????????
        String newFileName = filename.substring(filename.lastIndexOf("/") + 1);
        //????????????
        agResource.setSourceName(newFileName);
        //???????????????
        agResource.setOldName(newFileName);
        this.saveResource(agResource, entity);
    }
    /**
     * ???????????????????????????????????????
     * @param resource ????????????
     * @param file ????????????
     * @param type ?????????????????????1??????????????????2??????????????????3???????????????
     */
    private void packageAndSaveResourceNotShow(AgHouse resource, MultipartFile file, String type) {
        AgHouse agResource = new AgHouse();
        agResource.setSourceId(resource.getSourceId());
        agResource.setUserId(resource.getUserId());
        agResource.setStoreFullPath(resource.getStoreFullPath());

        //???????????????????????????
        agResource.setIsShow("0");
        //????????????
        agResource.setType(type);

        //?????????????????????????????????id
        String filename = file.getOriginalFilename();

        //???????????????????????????????????????????????????
        String newFileName = filename.substring(filename.lastIndexOf("/") + 1);
        //????????????
        agResource.setSourceName(newFileName);
        //???????????????
        agResource.setOldName(newFileName);
        this.saveResource(agResource, file);
    }


    @Override
    public PageInfo<AgHouse> find(AgHouse resource, Page page) {
        //??????????????????
        AgHouseExample agResourceExample = new AgHouseExample();
        AgHouseExample.Criteria criteria = agResourceExample.createCriteria();
        //????????????
        if (!StringUtils.isEmpty(resource.getHourseName())) {
            criteria.andHourseNameLike("%" + resource.getHourseName() + "%");
        }
        //????????????
        if(!StringUtils.isEmpty(resource.getStructureType())){
            String[] structures = resource.getStructureType().split(",");
            List<String> list = Arrays.asList(structures);
            criteria.andStructureTypeIn(list);
        }
        //?????????????????????
        criteria.andIsShowEqualTo("1");
        agResourceExample.setOrderByClause("create_time desc");
        PageHelper.startPage(page);
        List<AgHouse> dbList = agHouseMapper.selectByExample(agResourceExample);
        return new PageInfo<>(dbList);
    }

    @Override
    public List<AgSysSetting> getAllSysSetting() {
        return sysSettingMapper.selectByExample(null);
    }


    @Override
    public List<AgHouse> findHouseDir(String id) {
        AgHouseExample agResourceExample = new AgHouseExample();
        agResourceExample.createCriteria().andSourceIdEqualTo(id).andTypeEqualTo("2");
        List<AgHouse> resultList = agHouseMapper.selectByExample(agResourceExample);
        return resultList;
    }

    @Override
    public String findThumb(String id) {
        AgHouse agResource = agHouseMapper.selectByPrimaryKey(id);
        if (agResource != null) {
            return agResource.getThumb();
        }
        return null;
    }


    /**
     * ????????????????????????????????????
     *
     * @return
     * @throws
     */
    @Override
    public String getBaseFilePath() {
//        File basePath = new File(ResourceUtils.getURL("classpath:").getPath());
//        if (!basePath.exists()) {
//            basePath = new File("");
//        }
//        return basePath.getAbsolutePath() + File.separator + filePath;
        return filePath + File.separator + bim_model_path;
    }


    @Override
    @Transactional
    public void update(AgHouseCustom resource, MultipartFile thumbFile, MultipartFile dirFiles[]) {
        AgHouse dbResource = agHouseMapper.selectByPrimaryKey(resource.getId());
        if(dbResource == null){
            throw new RuntimeException("??????????????????");
        }
        AgHouse updateEntity = new AgHouse();
        //???????????????
        if(thumbFile != null && !thumbFile.isEmpty()){
            try{
                //????????????????????????
                String basePath = getBaseFilePath();
                String path = dbResource.getThumb();
                //??????????????????
                if(StringUtils.isEmpty(path)){
                    //????????????????????????????????????
                    String thumbPath = uploadThumbFile(dbResource, thumbFile,  resource.getId());
                    updateEntity.setThumb(thumbPath);
                }else{
                    //??????????????????????????????
                    File upload = new File(basePath + File.separator + path);
                    if (!upload.exists()) {
                        upload.mkdirs();
                    }
                    thumbFile.transferTo(upload);
                }
            }catch (Exception e){
                throw new RuntimeException("???????????????????????????????????????");
            }
        }
        //???????????????
        if(dirFiles != null && dirFiles.length > 0 && !dirFiles[0].isEmpty()){
            //????????????????????????????????????????????????????????????????????????
            AgHouseExample dirExample = new AgHouseExample();
            dirExample.createCriteria().andTypeEqualTo("2").andSourceIdEqualTo(resource.getId());
            List<AgHouse> agResources = agHouseMapper.selectByExample(dirExample);
            //??????????????????????????????????????????????????????
            if(agResources != null && agResources.size() > 0){
                //???????????????????????????
                String storeFullPath = agResources.get(0).getStoreFullPath();
                String sysStorePath = storeFullPath.substring(0, storeFullPath.lastIndexOf("/"));

                //?????????????????????
                agHouseMapper.deleteByExample(dirExample);

                //????????????????????????
                deleteFileFromCategorySourcePath(sysStorePath);
            }

            //???????????????
            dbResource.setSourceId(resource.getId());

            //?????????????????????????????????
            for (MultipartFile file : dirFiles) {
                packageAndSaveResourceNotShow(dbResource,  file, "2");
            }
        }

        //??????????????????????????????
        updateEntity.setId(resource.getId());
        updateEntity.setStructureType(resource.getStructureType());
        updateEntity.setHourseName(resource.getHourseName());
        updateEntity.setHomesteadArea(resource.getHomesteadArea());
        updateEntity.setFloorArea(resource.getFloorArea());
        updateEntity.setCoveredArea(resource.getCoveredArea());
        updateEntity.setCostEstimates(resource.getCostEstimates());
        updateEntity.setRemark(resource.getRemark());
        updateEntity.setModifyTime(new Date());
        if(!StringUtils.isEmpty(resource.getTableName())){
            updateEntity.setTableName(resource.getTableName());
        }
        agHouseMapper.updateByPrimaryKeySelective(updateEntity);
    }



    @Override
    @Transactional
    public void delete(String id) {
        //???????????????????????????????????????????????????????????????
        AgHouse agResource = agHouseMapper.selectByPrimaryKey(id);
        if(agResource == null){
            throw new RuntimeException("??????????????????????????????");
        }

        //???????????????????????????id
        AgHouseExample agResourceExample = new AgHouseExample();
        agResourceExample.createCriteria().andSourceIdEqualTo(id);

        //??????????????????????????????????????????????????????????????????
        String sourcePath = agResource.getStoreFullPath();
        if(!StringUtils.isEmpty(sourcePath)){
            sourcePath = sourcePath.substring(0, sourcePath.indexOf("/"));
        }

        //????????????
        agHouseMapper.deleteByPrimaryKey(id);
        //?????????????????????????????????
        agHouseMapper.deleteByExample(agResourceExample);

        //??????????????????????????????
        deleteFileFromCategorySourcePath(sourcePath);
    }


    /**
     * ??????????????????
     * @param file
     */
    void deleteFile(File file){
        if(file.exists()){
            if(file.isFile()){
                //????????????????????????
                file.delete();
            }
            if(file.isDirectory()){
                //????????????????????????????????????
                File[] files = file.listFiles();
                if(files != null && files.length > 0){
                    for(File delFile : files){
                        deleteFile(delFile);
                    }
                }
                //?????????????????????
                file.delete();
            }
        }
    }




    @Override
    public void deleteFileFromCategorySourcePath(String sourcePath) {
        if (!StringUtils.isEmpty(sourcePath)) {
            //???????????????????????????
            try {
                String baseFile = getBaseFilePath();
                String delFile = baseFile + File.separator + sourcePath;
                //win???linux????????????
                delFile = "/".equals(File.separator) ? delFile.replaceAll("/", File.separator) : delFile.replaceAll("/", File.separator + File.separator);
                log.info("-----delete--------" + delFile);
                File file = new File(delFile);
                if (file.exists()) {
                    deleteFile(file);
                    file.delete();
                }
            } catch (Exception e) {
                log.info("??????????????????FileNotFoundException-----------delFile=" + e.getMessage());
            }
        }
    }



    @Override
    @Transactional
    public void batchDelete(String ids) {
        for(String id : ids.split(",")){
            this.delete(id);
        }
    }

    @Override
    public Object statistics() {
        Map<String, Object> resultMap = new HashMap<>();
        String groupBystructureType = "select structure_type from ag_house group by structure_type";
        List<AgHouse> agResources = houseCustomMapper.sql(groupBystructureType);
        if(agResources != null && agResources.size() > 0){
            List<Map> lists = new ArrayList<>();
            for(AgHouse entity : agResources){
                if(entity != null && !StringUtils.isEmpty(entity.getStructureType())){
                    Map<String, String> map = new HashMap<>();
                    map.put("text", entity.getStructureType());
                    map.put("value", entity.getStructureType());
                    lists.add(map);
                }
            }
            resultMap.put("structureType", lists);
        }
        return resultMap;
    }

    /**
     *
     * @param resource
     * @param allFiles
     * @return
     */
    private void saveRvt(AgHouse resource, List<FileEntity> allFiles){
        //??????????????????????????????
        int modelSize = 0;
        for (FileEntity file : allFiles) {
            modelSize += file.getFile().length();
        }

        FileEntity thumb = null;

        //??????xx.rvt?????????????????????????????????????????????????????????????????????
        for (FileEntity file : allFiles) {
            if (file.getName().endsWith(".rvt")) {
                AgHouse agResource = new AgHouse();
                //????????????
                agResource.setRemark(resource.getRemark());
                agResource.setHourseName(resource.getHourseName());
                agResource.setCostEstimates(resource.getCostEstimates());
                agResource.setFloorArea(resource.getFloorArea());
                agResource.setCoveredArea(resource.getCoveredArea());
                agResource.setHomesteadArea(resource.getHomesteadArea());
//                agResource.setSourceId(resource.getSourceId());
                agResource.setUserId(resource.getUserId());
                agResource.setTableName(resource.getTableName());
                agResource.setStructureType(resource.getStructureType());
                agResource.setModelSize(modelSize + "");

                //??????status=1???rvt???2???3dtiles
                agResource.setStatus(1);
                //????????????????????????
                agResource.setIsShow("1");
                //?????????????????????????????????
                agResource.setType("1");

                //??????????????????
                String filename = file.getName();
                //??????????????????
                agResource.setStoreFullPath(filename);

                //???????????????????????????????????????????????????
                String newFileName = filename.substring(filename.lastIndexOf("/") + 1);
                //????????????
                agResource.setSourceName(newFileName);
                //???????????????
                agResource.setOldName(newFileName);
                //??????????????????????????????
                for (FileEntity thumbFile : allFiles) {
                    //???????????????,?????????????????????/
                    if (thumbFile.getName().split("/").length == 2 && (thumbFile.getName().endsWith("jpg") || thumbFile.getName().endsWith("jpeg") || thumbFile.getName().endsWith("png"))) {
                        String thumbPath = thumbFile.getName();
                        agResource.setThumb(thumbPath);
                        thumb = thumbFile;
                        break;
                    }
                }

                this.saveResource(agResource, file);

                //??????????????????id
                resource.setSourceId(agResource.getId());
                break;
            }
        }

        //???????????????
        if(thumb != null){
            uploadThumbFile(resource, thumb,  resource.getSourceId());
        }

        //???????????????
        List<FileEntity> dirFiles = new ArrayList<>();
        for (FileEntity file : allFiles) {
            //?????????,????????????????????????/???
            if (file.getName().split("/").length == 3 && (file.getName().endsWith("jpg") || file.getName().endsWith("jpeg") || file.getName().endsWith("png"))) {
                dirFiles.add(file);
            }
        }
        //??????????????????null
        if (dirFiles.size() > 0) {
            //?????????????????????????????????
            for (FileEntity file : dirFiles) {
                packageAndSaveResourceNotShow(resource,  file, "2");
            }
        }
    }

    private void save3dtiles(AgHouse resource, List<FileEntity> allFiles){
        //??????????????????????????????
        int modelSize = 0;
        for (FileEntity file : allFiles) {
            modelSize += file.getFile().length();
        }

        //??????3dtilse?????????????????????????????????????????????????????????????????????
        for (FileEntity file : allFiles) {
            if (file.getName().endsWith("tileset.json")) {
                AgHouse agResource = new AgHouse();
                //????????????
                agResource.setRemark(resource.getRemark());
                agResource.setHourseName(resource.getHourseName());
                agResource.setCostEstimates(resource.getCostEstimates());
                agResource.setFloorArea(resource.getFloorArea());
                agResource.setCoveredArea(resource.getCoveredArea());
                agResource.setHomesteadArea(resource.getHomesteadArea());
//                agResource.setSourceId(resource.getSourceId());
                agResource.setUserId(resource.getUserId());
                agResource.setTableName(resource.getTableName());
                agResource.setStructureType(resource.getStructureType());
                agResource.setModelSize(modelSize + "");

                //??????status=1???rvt???2???3dtiles
                agResource.setStatus(2);
                //????????????????????????
                agResource.setIsShow("1");
                //?????????????????????????????????
                agResource.setType("1");

                //??????????????????
                String filename = file.getName();
                //??????????????????
                agResource.setStoreFullPath(filename);

                //???????????????????????????????????????????????????
                String newFileName = filename.substring(filename.lastIndexOf("/") + 1);
                //????????????
                agResource.setSourceName(newFileName);
                //???????????????
                agResource.setOldName(newFileName);

                this.saveResource(agResource, file);

                //??????????????????id
                resource.setSourceId(agResource.getId());
                break;
            }
        }

//        //?????????????????????????????????
//        for (FileEntity file : allFiles) {
//            //??????????????????????????????tileset.json
//            if (file.getName().endsWith("tileset.json")) {
//                continue;
//            }
//            packageAndSaveResourceNotShow(resource,  file, "1");
//        }
    }


    private void setComponentCode(AgHouse  entity, String[] split) {
        //?????????
        String tableCode = split[0];
        //???????????????
        String tableCodeName = sysAgCloudSystemService.getBuildComponentDictionaryName(tableCode);

        String largeCode = "";
        String largeCodeName = "";
        String mediumCode = "";
        String mediumCodeName = "";
        String smallCode = "";
        String smallCodeName = "";
        String detailCode = "";
        String detailCodeName = "";

        //??????????????????
        String caCodeNum = split[1];
        String[] caCodeNums = caCodeNum.split("\\.");
        //4???????????????
        if(caCodeNums.length == 4){
            largeCode = caCodeNums[0];
            mediumCode = caCodeNums[1];
            smallCode = caCodeNums[2];
            detailCode = caCodeNums[3];
            //????????????
            largeCodeName = sysAgCloudSystemService.getBuildComponentName("1", tableCode, largeCode, "00", "00", null);
            //????????????
            mediumCodeName = sysAgCloudSystemService.getBuildComponentName("2", tableCode, largeCode, mediumCode, "00", null);
            //????????????
            smallCodeName = sysAgCloudSystemService.getBuildComponentName("3", tableCode, largeCode, mediumCode, smallCode, null);
            //????????????
            detailCodeName = sysAgCloudSystemService.getBuildComponentName("4", tableCode, largeCode, mediumCode, smallCode, detailCode);
        }
        //????????????
        if(caCodeNums.length == 3){
            largeCode = caCodeNums[0];
            mediumCode = caCodeNums[1];
            smallCode = caCodeNums[2];
            //????????????
            largeCodeName = sysAgCloudSystemService.getBuildComponentName("1", tableCode, largeCode, "00", "00", null);
            //????????????
            mediumCodeName = sysAgCloudSystemService.getBuildComponentName("2", tableCode, largeCode, mediumCode, "00", null);
            //????????????
            smallCodeName = sysAgCloudSystemService.getBuildComponentName("3", tableCode, largeCode, mediumCode, smallCode, null);
        }
        //?????????????????????
        if(caCodeNums.length == 3){
            largeCode = caCodeNums[0];
            mediumCode = caCodeNums[1];
            //????????????
            largeCodeName = sysAgCloudSystemService.getBuildComponentName("1", tableCode, largeCode, "00", "00", null);
            //????????????
            mediumCodeName = sysAgCloudSystemService.getBuildComponentName("2", tableCode, largeCode, mediumCode, "00", null);
        }
        if(!StringUtils.isEmpty(tableCode)){
            entity.setComponentCode(tableCode + "-"  + largeCode + "." + smallCode + "." + mediumCode + "." + detailCode);
            entity.setComponentCodeName(tableCodeName + "-"  + largeCodeName + "." + smallCodeName + "." + mediumCodeName + "." + detailCodeName);
        }
    }

    @Override
    @Transactional
    public void saveRvtZip(MultipartFile modelFile) {
        String fileUploadPath = null;
        try{
            //??????????????????
            String baseFilePath = this.getBaseFilePath();
            fileUploadPath = uploadTempPath + File.separator + UUID.randomUUID().toString();
            String path = baseFilePath + File.separator + fileUploadPath;
            Map<String, Object> zip = BimZipUtils.getHouseRvtFileFromZip(modelFile, path);
            FileEntity excelFile = (FileEntity)zip.get("excel");;
            List<FileEntity> modelList = (List<FileEntity>)zip.get("modelFiles");
            if(excelFile == null || StringUtils.isEmpty(excelFile.getName())){
                throw new SourceException("zip?????????????????????????????????excel??????");
            }
            if(modelList == null || modelList.size() == 0){
                throw new SourceException("zip???????????????????????????????????????????????????");
            }
            //??????excel????????????
            List<Object[]> houseDatas = BimExcelUtil.getExcelData(excelFile.getName(), new FileInputStream(excelFile.getFile()));
            if(houseDatas == null || houseDatas.size() <= 1){
                throw new SourceException("??????????????????????????????");
            }

            //????????????>> excel?????????:  0??????	1????????????	2????????????	3???????????????????????????	4???????????????????????????	5????????????????????????	6??????
            for(int i = 1; i < houseDatas.size(); i++){
                AgHouse house = new AgHouse();
                //excel??????????????????
                if(houseDatas.get(i).length != 7){
                    throw new SourceException("excel???????????????????????????????????????");
                }
                house.setHourseName((String)houseDatas.get(i)[0]);
                String caCode = (String)houseDatas.get(i)[1];
                if(!StringUtils.isEmpty(caCode)){
                    //??????????????????null????????????????????????-??????.??????.??????.?????? ??????30-13.10.35.10
                    //???????????????
                    String[] split = caCode.split("-");
                    if(split != null && split.length == 2){
                        //??????????????????
                        setComponentCode(house, split);
                    }
                }
                house.setStructureType((String)houseDatas.get(i)[2]);
                house.setFloorArea((String)houseDatas.get(i)[3]);
                house.setCoveredArea((String)houseDatas.get(i)[4]);
                house.setCostEstimates((String)houseDatas.get(i)[5]);
                house.setRemark((String)houseDatas.get(i)[6]);

                //????????????????????????????????????????????????????????????
                List<FileEntity> thisModels = new ArrayList<>();
                for(FileEntity entity : modelList){
                    if(entity.getName().startsWith(house.getHourseName() + "/")){
                        thisModels.add(entity);
                    }
                }
                if(thisModels.size() == 0){
                    throw new SourceException("zip???????????????????????????????????????????????????");
                }
                //????????????
                saveRvt(house, thisModels);
            }
        }catch (SourceException e){
            log.info("--------SourceException-------" + e.getMessage());;
            throw new SourceException(e.getMessage());
        }catch (IOException e){
            log.info("------????????????????????????????????????---------" + e.getMessage());;
            throw new SourceException("????????????????????????????????????");
        }catch (Exception e){
            log.info("------????????????---------" + e.getMessage());;
            throw new SourceException("????????????");
        }finally {
            if(fileUploadPath != null){
                //????????????????????????
                deleteFileFromCategorySourcePath(fileUploadPath);
            }
        }
    }

    @Override
    @Transactional
    public void add3dtilesZip(MultipartFile modelFile) {
        String fileUploadPath = null;
        try{
            //??????????????????
            String baseFilePath = this.getBaseFilePath();
            fileUploadPath = uploadTempPath + File.separator + UUID.randomUUID().toString();
            String path = baseFilePath + File.separator + fileUploadPath;
            Map<String, Object> zip = BimZipUtils.getHouse3dtilesFileFromZip(modelFile, path);
            List<FileEntity> models = (List<FileEntity>)zip.get("models");;
            List<FileEntity> modelList = (List<FileEntity>)zip.get("modelFiles");
            if(models == null || models.size()  == 0){
                throw new SourceException("zip?????????????????????");
            }
            if(modelList == null || modelList.size() == 0){
                throw new SourceException("zip???????????????????????????????????????????????????");
            }
            for(FileEntity model: models){
                String houseName = model.getName();
                houseName = houseName.substring(0, houseName.indexOf("/"));
                AgHouse house = new AgHouse();
                house.setHourseName(houseName);
                //????????????????????????????????????????????????????????????
                List<FileEntity> thisModels = new ArrayList<>();
                for(FileEntity entity : modelList){
                    if(entity.getName().startsWith(house.getHourseName())){
                        thisModels.add(entity);
                    }
                }
                //????????????
                save3dtiles(house, thisModels );
            }
        }catch (SourceException e){
            log.info("--------SourceException-------" + e.getMessage());;
            throw new SourceException(e.getMessage());
        }catch (IOException e){
            log.info("------????????????????????????????????????---------" + e.getMessage());;
            throw new SourceException("????????????????????????????????????");
        }catch (Exception e){
            log.info("------????????????---------" + e.getMessage());;
            throw new SourceException("????????????");
        }finally {
            if(fileUploadPath != null){
                //????????????????????????
                deleteFileFromCategorySourcePath(fileUploadPath);
            }
        }
    }


}
