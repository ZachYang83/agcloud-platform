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
        //文件上传到文件系统
        String basePath = getBaseFilePath();
        try {
            path = basePath + File.separator + fileName ;
            //如果已经有路径，路径的设置需要从已有的里面选择
            String storeFullPath = resource.getStoreFullPath();
            if(!StringUtils.isEmpty(storeFullPath)){
                storeFullPath = storeFullPath.substring(0, storeFullPath.indexOf("/"));
                //地址重新赋值
                path = basePath + File.separator + storeFullPath + File.separator + fileName;
                //路径重新赋值
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

        //设置路径，此处路径就用"/"设置,去掉filePath的根目录，保存的路径结构式：分类名+文件名
       //判断，如果是文件名携带"/"，就去掉前面带路径的部分
        path =  fileName;
        resource.setStoreFullPath(path);
        //设置属性等信息
        //如果没设置名称，就以文件名命名
        if (StringUtils.isEmpty(resource.getSourceName())) {
            //没设置名称，和原文件名一样
            resource.setSourceName(fileName);
        } else {
            //如果当前修改的名称也携带了后缀，就不需要修改
            if (!resource.getSourceName().contains("." + suffix)) {
                //如果设置了名称,不包含后缀，需要添加名称的后缀
                resource.setSourceName(resource.getSourceName() + "." + suffix);
            }
        }
        //设置原文件名
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
     * 保存箱细腻，参数从FileEntity获取
     * @param resource
     * @param entity
     * @throws RuntimeException
     */
    public void saveResource(AgHouse resource, FileEntity entity) throws RuntimeException {
        String fileName = entity.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        String path = null;

        //文件上传到文件系统
        String basePath = getBaseFilePath();
        try {
            path = basePath + File.separator +  fileName;
            File upload = new File(path, "");
            writeFileToDisk(entity, upload);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.info("------添加失败,文件找不到----" + e.getMessage());
            throw new RuntimeException("添加失败,文件找不到");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("------上传添加失败----" + e.getMessage());
            throw new RuntimeException("上传添加失败");
        }
        if (path == null) {
            throw new RuntimeException("文件上传失败");
        }
        //设置路径，此处路径就用"/"设置,去掉filePath的根目录，保存的路径结构式：分类名+文件名
       //判断，如果是文件名携带"/"，就去掉前面带路径的部分
        path =  fileName;
        resource.setStoreFullPath(path);
        //设置属性等信息
        //如果没设置名称，就以文件名命名
        if (StringUtils.isEmpty(resource.getSourceName())) {
            //没设置名称，和原文件名一样
            resource.setSourceName(fileName);
        } else {
            //如果当前修改的名称也携带了后缀，就不需要修改
            if (!resource.getSourceName().contains("." + suffix)) {
                //如果设置了名称,不包含后缀，需要添加名称的后缀
                resource.setSourceName(resource.getSourceName() + "." + suffix);
            }
        }
        //设置原文件名
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
        //开始读,每次读的大小默认为1024个字节
        OutputStream out = new FileOutputStream(upload);
        byte[] b = new byte[1024];
        int i = 0;
        //判断缓冲区返回数据是否已经到了结尾,如果结尾，则返回-1
        while((i = stream.read(b)) != -1){
            //从字节数组b中将其实位置0，长度为i个字节的内容输出。
            out.write(b,0,i);
        }

        out.close();
        stream.close();
    }


    /**
     * 上传预览图
     * @param resource 预览图相关信息
     * @param thumbEntity 预览图文件
     * @param resourceId 预览图所关联的id
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
            //保存路径
            agResource.setStoreFullPath(returnPath);

            //设置本条信息不可见
            agResource.setIsShow("0");
            //设置此类型是预览图类型
            agResource.setType("3");

            //名称设置
            agResource.setSourceName(filename);
            //旧名称设置
            agResource.setOldName(filename);
            String suffix = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
            agResource.setSuffix(suffix);
            agResource.setSize(String.valueOf(thumbEntity.getFile().length()));
            agResource.setCreateTime(new Date());
            agResource.setId(UUID.randomUUID().toString());
            agResource.setSourceId(resourceId);
            agHouseMapper.insert(agResource);
        } catch (Exception e) {
            log.info("上传缩略图失败-------" + e.getMessage());
            throw new RuntimeException("上传缩略图失败");
        }
        return returnPath;
    }


    /**
     * 上传预览图
     * @param resource 预览图相关信息
     * @param thumbFile 预览图文件
     * @param resourceId 预览图所关联的id
     * @return
     */
    private String uploadThumbFile(AgHouse resource, MultipartFile thumbFile, String resourceId) {
        String returnPath = null;
        try {
            //获取模型存储的路径
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

            //设置本条信息不可见
            agResource.setIsShow("0");
            //设置此类型是预览图类型
            agResource.setType("3");

            //名称设置
            agResource.setSourceName(filename);
            //旧名称设置
            agResource.setOldName(filename);
            String suffix = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
            agResource.setSuffix(suffix);
            agResource.setSize(String.valueOf(thumbFile.getSize()));
            agResource.setCreateTime(new Date());
            agResource.setId(UUID.randomUUID().toString());
            agResource.setSourceId(resourceId);
            agHouseMapper.insert(agResource);
        } catch (Exception e) {
            log.info("上传缩略图失败-------" + e.getMessage());
            throw new SourceException("上传缩略图失败");
        }
        return returnPath;
    }

    /**
     * 重新组装资源参数，保存资源
     * @param resource 资源信息
     * @param entity 资源文件
     * @param type 设置资源类型；1模型图类型；2户型图类型；3缩略图类型
     */
    private void packageAndSaveResourceNotShow(AgHouse resource,  FileEntity entity, String type) {
        AgHouse agResource = new AgHouse();
        agResource.setSourceId(resource.getSourceId());
        agResource.setUserId(resource.getUserId());

        //设置本条信息不可见
        agResource.setIsShow("0");
        //设置类型
        agResource.setType(type);

        //从树形结构里面获取分类id
        String filename = entity.getName();

        //重新设置文件名称，去掉文件路径前缀
        String newFileName = filename.substring(filename.lastIndexOf("/") + 1);
        //名称设置
        agResource.setSourceName(newFileName);
        //旧名称设置
        agResource.setOldName(newFileName);
        this.saveResource(agResource, entity);
    }
    /**
     * 重新组装资源参数，保存资源
     * @param resource 资源信息
     * @param file 资源文件
     * @param type 设置资源类型；1模型图类型；2户型图类型；3缩略图类型
     */
    private void packageAndSaveResourceNotShow(AgHouse resource, MultipartFile file, String type) {
        AgHouse agResource = new AgHouse();
        agResource.setSourceId(resource.getSourceId());
        agResource.setUserId(resource.getUserId());
        agResource.setStoreFullPath(resource.getStoreFullPath());

        //设置本条信息不可见
        agResource.setIsShow("0");
        //设置类型
        agResource.setType(type);

        //从树形结构里面获取分类id
        String filename = file.getOriginalFilename();

        //重新设置文件名称，去掉文件路径前缀
        String newFileName = filename.substring(filename.lastIndexOf("/") + 1);
        //名称设置
        agResource.setSourceName(newFileName);
        //旧名称设置
        agResource.setOldName(newFileName);
        this.saveResource(agResource, file);
    }


    @Override
    public PageInfo<AgHouse> find(AgHouse resource, Page page) {
        //查询参数封装
        AgHouseExample agResourceExample = new AgHouseExample();
        AgHouseExample.Criteria criteria = agResourceExample.createCriteria();
        //查询条件
        if (!StringUtils.isEmpty(resource.getHourseName())) {
            criteria.andHourseNameLike("%" + resource.getHourseName() + "%");
        }
        //结构类型
        if(!StringUtils.isEmpty(resource.getStructureType())){
            String[] structures = resource.getStructureType().split(",");
            List<String> list = Arrays.asList(structures);
            criteria.andStructureTypeIn(list);
        }
        //默认展示的信息
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
     * 获取系统文件上传的根路径
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
            throw new RuntimeException("该数据不存在");
        }
        AgHouse updateEntity = new AgHouse();
        //修改缩略图
        if(thumbFile != null && !thumbFile.isEmpty()){
            try{
                //直接覆盖此缩略图
                String basePath = getBaseFilePath();
                String path = dbResource.getThumb();
                //是否有缩略图
                if(StringUtils.isEmpty(path)){
                    //没有缩略图，需要重新添加
                    String thumbPath = uploadThumbFile(dbResource, thumbFile,  resource.getId());
                    updateEntity.setThumb(thumbPath);
                }else{
                    //已经有缩略图，就替换
                    File upload = new File(basePath + File.separator + path);
                    if (!upload.exists()) {
                        upload.mkdirs();
                    }
                    thumbFile.transferTo(upload);
                }
            }catch (Exception e){
                throw new RuntimeException("缩略图上传失败，请稍后再试");
            }
        }
        //修改户型图
        if(dirFiles != null && dirFiles.length > 0 && !dirFiles[0].isEmpty()){
            //先删除所有的户型图信息，户型图分类，户型图源文件
            AgHouseExample dirExample = new AgHouseExample();
            dirExample.createCriteria().andTypeEqualTo("2").andSourceIdEqualTo(resource.getId());
            List<AgHouse> agResources = agHouseMapper.selectByExample(dirExample);
            //是否有户型图，没有直接添加，不用删除
            if(agResources != null && agResources.size() > 0){
                //查询资源父分类目录
                String storeFullPath = agResources.get(0).getStoreFullPath();
                String sysStorePath = storeFullPath.substring(0, storeFullPath.lastIndexOf("/"));

                //删除户型图信息
                agHouseMapper.deleteByExample(dirExample);

                //删除户型图源文件
                deleteFileFromCategorySourcePath(sysStorePath);
            }

            //添加户型图
            dbResource.setSourceId(resource.getId());

            //再次遍历资源，上传资源
            for (MultipartFile file : dirFiles) {
                packageAndSaveResourceNotShow(dbResource,  file, "2");
            }
        }

        //只修改需要修改的属性
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
        //资源关联数据有：资源信息，分类信息，资源表
        AgHouse agResource = agHouseMapper.selectByPrimaryKey(id);
        if(agResource == null){
            throw new RuntimeException("数据不存在，删除失败");
        }

        //查询当前所有的分类id
        AgHouseExample agResourceExample = new AgHouseExample();
        agResourceExample.createCriteria().andSourceIdEqualTo(id);

        //查询资源父分类目录，获取父路径的第一个文件夹
        String sourcePath = agResource.getStoreFullPath();
        if(!StringUtils.isEmpty(sourcePath)){
            sourcePath = sourcePath.substring(0, sourcePath.indexOf("/"));
        }

        //删除资源
        agHouseMapper.deleteByPrimaryKey(id);
        //删除资源关联的所有信息
        agHouseMapper.deleteByExample(agResourceExample);

        //删除所有资源目录数据
        deleteFileFromCategorySourcePath(sourcePath);
    }


    /**
     * 循环删除文件
     * @param file
     */
    void deleteFile(File file){
        if(file.exists()){
            if(file.isFile()){
                //是文件，直接删除
                file.delete();
            }
            if(file.isDirectory()){
                //是文件夹，判断是否有文件
                File[] files = file.listFiles();
                if(files != null && files.length > 0){
                    for(File delFile : files){
                        deleteFile(delFile);
                    }
                }
                //直接删除文件夹
                file.delete();
            }
        }
    }




    @Override
    public void deleteFileFromCategorySourcePath(String sourcePath) {
        if (!StringUtils.isEmpty(sourcePath)) {
            //从磁盘删除存储文件
            try {
                String baseFile = getBaseFilePath();
                String delFile = baseFile + File.separator + sourcePath;
                //win和linux路径转换
                delFile = "/".equals(File.separator) ? delFile.replaceAll("/", File.separator) : delFile.replaceAll("/", File.separator + File.separator);
                log.info("-----delete--------" + delFile);
                File file = new File(delFile);
                if (file.exists()) {
                    deleteFile(file);
                    file.delete();
                }
            } catch (Exception e) {
                log.info("删除文件失败FileNotFoundException-----------delFile=" + e.getMessage());
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
        //统计模型文件的总大小
        int modelSize = 0;
        for (FileEntity file : allFiles) {
            modelSize += file.getFile().length();
        }

        FileEntity thumb = null;

        //找到xx.rvt上传，设置可见，因为整条记录就展示这条信息可见
        for (FileEntity file : allFiles) {
            if (file.getName().endsWith(".rvt")) {
                AgHouse agResource = new AgHouse();
                //设置备注
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

                //设置status=1是rvt，2是3dtiles
                agResource.setStatus(1);
                //设置本条信息可见
                agResource.setIsShow("1");
                //设置此类型是模型图类型
                agResource.setType("1");

                //获取文件名称
                String filename = file.getName();
                //设置存储路径
                agResource.setStoreFullPath(filename);

                //重新设置文件名称，去掉文件路径前缀
                String newFileName = filename.substring(filename.lastIndexOf("/") + 1);
                //名称设置
                agResource.setSourceName(newFileName);
                //旧名称设置
                agResource.setOldName(newFileName);
                //从文件列表找到缩略图
                for (FileEntity thumbFile : allFiles) {
                    //设置缩略图,缩略图只带一个/
                    if (thumbFile.getName().split("/").length == 2 && (thumbFile.getName().endsWith("jpg") || thumbFile.getName().endsWith("jpeg") || thumbFile.getName().endsWith("png"))) {
                        String thumbPath = thumbFile.getName();
                        agResource.setThumb(thumbPath);
                        thumb = thumbFile;
                        break;
                    }
                }

                this.saveResource(agResource, file);

                //设置父类资源id
                resource.setSourceId(agResource.getId());
                break;
            }
        }

        //上传缩略图
        if(thumb != null){
            uploadThumbFile(resource, thumb,  resource.getSourceId());
        }

        //上传户型图
        List<FileEntity> dirFiles = new ArrayList<>();
        for (FileEntity file : allFiles) {
            //户型图,户型图携带两个“/”
            if (file.getName().split("/").length == 3 && (file.getName().endsWith("jpg") || file.getName().endsWith("jpeg") || file.getName().endsWith("png"))) {
                dirFiles.add(file);
            }
        }
        //文件夹是否为null
        if (dirFiles.size() > 0) {
            //再次遍历资源，上传资源
            for (FileEntity file : dirFiles) {
                packageAndSaveResourceNotShow(resource,  file, "2");
            }
        }
    }

    private void save3dtiles(AgHouse resource, List<FileEntity> allFiles){
        //统计模型文件的总大小
        int modelSize = 0;
        for (FileEntity file : allFiles) {
            modelSize += file.getFile().length();
        }

        //找到3dtilse上传，设置可见，因为整条记录就展示这条信息可见
        for (FileEntity file : allFiles) {
            if (file.getName().endsWith("tileset.json")) {
                AgHouse agResource = new AgHouse();
                //设置备注
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

                //设置status=1是rvt，2是3dtiles
                agResource.setStatus(2);
                //设置本条信息可见
                agResource.setIsShow("1");
                //设置此类型是模型图类型
                agResource.setType("1");

                //获取文件名称
                String filename = file.getName();
                //设置存储路径
                agResource.setStoreFullPath(filename);

                //重新设置文件名称，去掉文件路径前缀
                String newFileName = filename.substring(filename.lastIndexOf("/") + 1);
                //名称设置
                agResource.setSourceName(newFileName);
                //旧名称设置
                agResource.setOldName(newFileName);

                this.saveResource(agResource, file);

                //设置父类资源id
                resource.setSourceId(agResource.getId());
                break;
            }
        }

//        //再次遍历资源，上传资源
//        for (FileEntity file : allFiles) {
//            //排除掉上面已经上传的tileset.json
//            if (file.getName().endsWith("tileset.json")) {
//                continue;
//            }
//            packageAndSaveResourceNotShow(resource,  file, "1");
//        }
    }


    private void setComponentCode(AgHouse  entity, String[] split) {
        //表代码
        String tableCode = split[0];
        //表代码名称
        String tableCodeName = sysAgCloudSystemService.getBuildComponentDictionaryName(tableCode);

        String largeCode = "";
        String largeCodeName = "";
        String mediumCode = "";
        String mediumCodeName = "";
        String smallCode = "";
        String smallCodeName = "";
        String detailCode = "";
        String detailCodeName = "";

        //构件编码分类
        String caCodeNum = split[1];
        String[] caCodeNums = caCodeNum.split("\\.");
        //4个分类都有
        if(caCodeNums.length == 4){
            largeCode = caCodeNums[0];
            mediumCode = caCodeNums[1];
            smallCode = caCodeNums[2];
            detailCode = caCodeNums[3];
            //大类名称
            largeCodeName = sysAgCloudSystemService.getBuildComponentName("1", tableCode, largeCode, "00", "00", null);
            //中类名称
            mediumCodeName = sysAgCloudSystemService.getBuildComponentName("2", tableCode, largeCode, mediumCode, "00", null);
            //小类名称
            smallCodeName = sysAgCloudSystemService.getBuildComponentName("3", tableCode, largeCode, mediumCode, smallCode, null);
            //细类名称
            detailCodeName = sysAgCloudSystemService.getBuildComponentName("4", tableCode, largeCode, mediumCode, smallCode, detailCode);
        }
        //细类没有
        if(caCodeNums.length == 3){
            largeCode = caCodeNums[0];
            mediumCode = caCodeNums[1];
            smallCode = caCodeNums[2];
            //大类名称
            largeCodeName = sysAgCloudSystemService.getBuildComponentName("1", tableCode, largeCode, "00", "00", null);
            //中类名称
            mediumCodeName = sysAgCloudSystemService.getBuildComponentName("2", tableCode, largeCode, mediumCode, "00", null);
            //小类名称
            smallCodeName = sysAgCloudSystemService.getBuildComponentName("3", tableCode, largeCode, mediumCode, smallCode, null);
        }
        //小类、细类没有
        if(caCodeNums.length == 3){
            largeCode = caCodeNums[0];
            mediumCode = caCodeNums[1];
            //大类名称
            largeCodeName = sysAgCloudSystemService.getBuildComponentName("1", tableCode, largeCode, "00", "00", null);
            //中类名称
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
            //获取模型信息
            String baseFilePath = this.getBaseFilePath();
            fileUploadPath = uploadTempPath + File.separator + UUID.randomUUID().toString();
            String path = baseFilePath + File.separator + fileUploadPath;
            Map<String, Object> zip = BimZipUtils.getHouseRvtFileFromZip(modelFile, path);
            FileEntity excelFile = (FileEntity)zip.get("excel");;
            List<FileEntity> modelList = (List<FileEntity>)zip.get("modelFiles");
            if(excelFile == null || StringUtils.isEmpty(excelFile.getName())){
                throw new SourceException("zip文件格式不正确，找不到excel文件");
            }
            if(modelList == null || modelList.size() == 0){
                throw new SourceException("zip文件格式不正确，找不到房屋模型文件");
            }
            //读取excel文件内容
            List<Object[]> houseDatas = BimExcelUtil.getExcelData(excelFile.getName(), new FileInputStream(excelFile.getFile()));
            if(houseDatas == null || houseDatas.size() <= 1){
                throw new SourceException("请填写房屋信息表数据");
            }

            //上传信息>> excel表结构:  0名称	1类目编码	2结构类型	3占地面积（平方米）	4建筑面积（平方米）	5造价估计（万元）	6简介
            for(int i = 1; i < houseDatas.size(); i++){
                AgHouse house = new AgHouse();
                //excel格式数据判断
                if(houseDatas.get(i).length != 7){
                    throw new SourceException("excel格式不正确，请重新下载模板");
                }
                house.setHourseName((String)houseDatas.get(i)[0]);
                String caCode = (String)houseDatas.get(i)[1];
                if(!StringUtils.isEmpty(caCode)){
                    //类目编码不为null，格式是：表代码-大类.种类.小类.细类 如：30-13.10.35.10
                    //获取表代码
                    String[] split = caCode.split("-");
                    if(split != null && split.length == 2){
                        //设置类目编码
                        setComponentCode(house, split);
                    }
                }
                house.setStructureType((String)houseDatas.get(i)[2]);
                house.setFloorArea((String)houseDatas.get(i)[3]);
                house.setCoveredArea((String)houseDatas.get(i)[4]);
                house.setCostEstimates((String)houseDatas.get(i)[5]);
                house.setRemark((String)houseDatas.get(i)[6]);

                //遍历所有模型数据，找到当前模型的所有信息
                List<FileEntity> thisModels = new ArrayList<>();
                for(FileEntity entity : modelList){
                    if(entity.getName().startsWith(house.getHourseName() + "/")){
                        thisModels.add(entity);
                    }
                }
                if(thisModels.size() == 0){
                    throw new SourceException("zip文件格式不正确，房屋模型文件找不到");
                }
                //保存模型
                saveRvt(house, thisModels);
            }
        }catch (SourceException e){
            log.info("--------SourceException-------" + e.getMessage());;
            throw new SourceException(e.getMessage());
        }catch (IOException e){
            log.info("------上传失败，文件格式不正确---------" + e.getMessage());;
            throw new SourceException("上传失败，文件格式不正确");
        }catch (Exception e){
            log.info("------上传失败---------" + e.getMessage());;
            throw new SourceException("上传失败");
        }finally {
            if(fileUploadPath != null){
                //删除所有临时文件
                deleteFileFromCategorySourcePath(fileUploadPath);
            }
        }
    }

    @Override
    @Transactional
    public void add3dtilesZip(MultipartFile modelFile) {
        String fileUploadPath = null;
        try{
            //获取模型信息
            String baseFilePath = this.getBaseFilePath();
            fileUploadPath = uploadTempPath + File.separator + UUID.randomUUID().toString();
            String path = baseFilePath + File.separator + fileUploadPath;
            Map<String, Object> zip = BimZipUtils.getHouse3dtilesFileFromZip(modelFile, path);
            List<FileEntity> models = (List<FileEntity>)zip.get("models");;
            List<FileEntity> modelList = (List<FileEntity>)zip.get("modelFiles");
            if(models == null || models.size()  == 0){
                throw new SourceException("zip文件格式不正确");
            }
            if(modelList == null || modelList.size() == 0){
                throw new SourceException("zip文件格式不正确，找不到房屋模型文件");
            }
            for(FileEntity model: models){
                String houseName = model.getName();
                houseName = houseName.substring(0, houseName.indexOf("/"));
                AgHouse house = new AgHouse();
                house.setHourseName(houseName);
                //遍历所有模型数据，找到当前模型的所有信息
                List<FileEntity> thisModels = new ArrayList<>();
                for(FileEntity entity : modelList){
                    if(entity.getName().startsWith(house.getHourseName())){
                        thisModels.add(entity);
                    }
                }
                //保存模型
                save3dtiles(house, thisModels );
            }
        }catch (SourceException e){
            log.info("--------SourceException-------" + e.getMessage());;
            throw new SourceException(e.getMessage());
        }catch (IOException e){
            log.info("------上传失败，文件格式不正确---------" + e.getMessage());;
            throw new SourceException("上传失败，文件格式不正确");
        }catch (Exception e){
            log.info("------上传失败---------" + e.getMessage());;
            throw new SourceException("上传失败");
        }finally {
            if(fileUploadPath != null){
                //删除所有临时文件
                deleteFileFromCategorySourcePath(fileUploadPath);
            }
        }
    }


}
