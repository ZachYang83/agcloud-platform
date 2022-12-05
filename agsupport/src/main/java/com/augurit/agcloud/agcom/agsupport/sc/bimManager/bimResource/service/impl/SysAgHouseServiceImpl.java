//package com.augurit.agcloud.agcom.agsupport.sc.bim.service.impl;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.augurit.agcloud.agcom.agsupport.domain.auto.*;
//import com.augurit.agcloud.agcom.agsupport.mapper.*;
//import com.augurit.agcloud.agcom.agsupport.mapper.auto.*;
//import com.augurit.agcloud.agcom.agsupport.sc.bim.service.ISysAgCloudSystemService;
//import com.augurit.agcloud.agcom.agsupport.sc.bim.service.ISysAgHouseService;
//import com.augurit.agcloud.agcom.agsupport.sc.bim.util.*;
//import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
//import com.augurit.agcloud.agcom.agsupport.util.CsvUtil;
//import com.augurit.agcloud.framework.ui.pager.PageHelper;
//import com.github.pagehelper.Page;
//import com.github.pagehelper.PageInfo;
//import org.bson.Document;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.ResourceUtils;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.*;
//import java.util.*;
//
///**
// * Created with IntelliJ IDEA.
// *
// * @Auther: qinyg
// * @Date: 2020/07
// * @Description:
// */
//@Service
//public class SysAgHouseServiceImpl implements ISysAgHouseService {
//
//    private static final Logger log = LoggerFactory.getLogger(SysAgHouseServiceImpl.class);
//    private static final String uploadTempPath = "uploadTemp";
//
//    @Autowired
//    private AgHouseMapper agHouseMapper;
//    @Autowired
//    private AgCategoryMapper categoryMapper;
//    @Autowired
//    private AgHouseCustomMapper houseCustomMapper;
//    @Autowired
//    private AgSysSettingMapper sysSettingMapper;
////    @Autowired
//    private MongoTemplate mongoTemplate;
//    @Autowired
//    private AgPermissionMapper agPermissionMapper;
//    @Autowired
//    private ISysAgCloudSystemService sysAgCloudSystemService;
//
//
//
//    @Value("${upload.filePath}")
//    private String filePath;
//
//    @Override
//    @Transactional
//    public void saveResource(AgHouse resource, MultipartFile file) throws SourceException {
//        String fileName = file.getOriginalFilename();
//        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
//        String path = null;
//        String parentCategoryPath = this.getParentCategoryPath(resource.getCategoryId());
//        //如果文件名携带"/"，就截取去掉路径部分，只保留文件名
//        String realName = null;
//        //文件上传到文件系统
//        try {
//            String basePath = getBaseFilePath();
//            //如果文件名称带有文件夹，需要获取最终的文件名
//            if (fileName.lastIndexOf("/") != -1) {
//                //最终文件名设置
//                realName = fileName.substring(fileName.lastIndexOf("/") + 1);
//            }
//            //再次判断，如果当前已经设置了资源名称，再次重命名
//            if (!StringUtils.isEmpty(resource.getSourceName())) {
//                //如果当前修改的名称也携带了后缀，就不需要修改
//                if (resource.getSourceName().contains("." + suffix)) {
//                    //携带了相同后缀，就以此作为名称
//                    realName = resource.getSourceName();
//                } else {
//                    //如果设置了名称,不包含后缀，需要添加名称的后缀
//                    realName = resource.getSourceName() + "." + suffix;
//                }
//            }
//            path = parentCategoryPath + File.separator + (realName == null ? fileName : realName);
//            File upload = new File(basePath + File.separator + path);
//            if (!upload.exists()) {
//                upload.mkdirs();
//            }
//            file.transferTo(upload);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (path == null) {
//            throw new SourceException("文件上传失败");
//        }
////        //设置路径，此处路径就用"/"设置,去掉filePath的根目录，保存的路径结构式：分类名+文件名
////       //判断，如果是文件名携带"/"，就去掉前面带路径的部分
////        path =  parentCategoryPath + "/" +  (realName == null ? fileName : realName);
////        resource.setStoreFullPath(path);
//        //设置属性等信息
//        //如果没设置名称，就以文件名命名
//        if (StringUtils.isEmpty(resource.getSourceName())) {
//            //没设置名称，和原文件名一样
//            resource.setSourceName(fileName);
//        } else {
//            //如果当前修改的名称也携带了后缀，就不需要修改
//            if (!resource.getSourceName().contains("." + suffix)) {
//                //如果设置了名称,不包含后缀，需要添加名称的后缀
//                resource.setSourceName(resource.getSourceName() + "." + suffix);
//            }
//        }
//        //设置原文件名
//        if (StringUtils.isEmpty(resource.getOldName())) {
//            resource.setOldName(fileName);
//        }
//        resource.setSuffix(suffix);
//        resource.setSize(String.valueOf(file.getSize()));
//        resource.setCreateTime(new Date());
//        resource.setId(UUID.randomUUID().toString());
//        agHouseMapper.insert(resource);
//    }
//
//    /**
//     * 保存箱细腻，参数从FileEntity获取
//     * @param resource
//     * @param entity
//     * @throws SourceException
//     */
//    public void saveResource(AgHouse resource, FileEntity entity) throws SourceException {
//        String fileName = entity.getName();
//        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
//        String path = null;
//        String parentCategoryPath = this.getParentCategoryPath(resource.getCategoryId());
//        //如果文件名携带"/"，就截取去掉路径部分，只保留文件名
//        String realName = null;
//        //文件上传到文件系统
//        try {
//            String basePath = getBaseFilePath();
//            //如果文件名称带有文件夹，需要获取最终的文件名
//            if (fileName.lastIndexOf("/") != -1) {
//                //最终文件名设置
//                realName = fileName.substring(fileName.lastIndexOf("/") + 1);
//            }
//            //再次判断，如果当前已经设置了资源名称，再次重命名
//            if (!StringUtils.isEmpty(resource.getSourceName())) {
//                //如果当前修改的名称也携带了后缀，就不需要修改
//                if (resource.getSourceName().contains("." + suffix)) {
//                    //携带了相同后缀，就以此作为名称
//                    realName = resource.getSourceName();
//                } else {
//                    //如果设置了名称,不包含后缀，需要添加名称的后缀
//                    realName = resource.getSourceName() + "." + suffix;
//                }
//            }
//            path = parentCategoryPath + File.separator + (realName == null ? fileName : realName);
//            File upload = new File(basePath + File.separator + path, "");
//            writeFileToDisk(entity, upload);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            log.info("------添加失败,文件找不到----" + e.getMessage());
//            throw new SourceException("添加失败,文件找不到");
//        } catch (IOException e) {
//            e.printStackTrace();
//            log.info("------上传添加失败----" + e.getMessage());
//            throw new SourceException("上传添加失败");
//        }
//        if (path == null) {
//            throw new SourceException("文件上传失败");
//        }
////        //设置路径，此处路径就用"/"设置,去掉filePath的根目录，保存的路径结构式：分类名+文件名
////       //判断，如果是文件名携带"/"，就去掉前面带路径的部分
////        path =  parentCategoryPath + "/" +  (realName == null ? fileName : realName);
////        resource.setStoreFullPath(path);
//        //设置属性等信息
//        //如果没设置名称，就以文件名命名
//        if (StringUtils.isEmpty(resource.getSourceName())) {
//            //没设置名称，和原文件名一样
//            resource.setSourceName(fileName);
//        } else {
//            //如果当前修改的名称也携带了后缀，就不需要修改
//            if (!resource.getSourceName().contains("." + suffix)) {
//                //如果设置了名称,不包含后缀，需要添加名称的后缀
//                resource.setSourceName(resource.getSourceName() + "." + suffix);
//            }
//        }
//        //设置原文件名
//        if (StringUtils.isEmpty(resource.getOldName())) {
//            resource.setOldName(fileName);
//        }
//        resource.setSuffix(suffix);
//        resource.setSize(String.valueOf(entity.getFile().length()));
//        resource.setCreateTime(new Date());
//        resource.setId(UUID.randomUUID().toString());
//        agHouseMapper.insert(resource);
//    }
//
//    private void writeFileToDisk(FileEntity entity, File upload) throws IOException {
//        if (!upload.exists()) {
//            if(!upload.getParentFile().exists()){
//                upload.getParentFile().mkdirs();
//            }
//            upload.createNewFile();
//        }
//        File file = entity.getFile();
//        InputStream stream = null;
//        OutputStream out = null;
//        try{
//            stream = new FileInputStream(file);
//            //开始读,每次读的大小默认为1024个字节
//            out = new FileOutputStream(upload);
//            byte[] b = new byte[1024];
//            int i = 0;
//            //判断缓冲区返回数据是否已经到了结尾,如果结尾，则返回-1
//            while((i = stream.read(b)) != -1){
//                //从字节数组b中将其实位置0，长度为i个字节的内容输出。
//                out.write(b,0,i);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            throw new IOException(e.getMessage());
//        }finally {
//            try{
//                if(out != null){
//                    out.close();
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            try{
//                if(stream != null){
//                    stream.close();
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 上传预览图
//     * @param resource 预览图相关信息
//     * @param thumbFile 预览图文件
//     * @param categoryId 预览图存储分类id
//     * @param resourceId 预览图所关联的id
//     * @return
//     */
//    private String uploadThumbFile(AgHouse resource, MultipartFile thumbFile, String categoryId, String resourceId) {
//        String returnPath = null;
//        try {
//            String filename = thumbFile.getOriginalFilename();
//            String basePath = getBaseFilePath();
//            String parentCategoryPath = this.getParentCategoryPath(categoryId);
//            String path = parentCategoryPath + File.separator + filename;
//            returnPath = parentCategoryPath + "/" + filename;
//            File upload = new File(basePath + File.separator + path);
//            if (!upload.exists()) {
//                upload.mkdirs();
//            }
//            thumbFile.transferTo(upload);
//            AgHouse agResource = new AgHouse();
//
//            agResource.setSourceId(resource.getSourceId());
//            agResource.setUserId(resource.getUserId());
//
//            //设置本条信息不可见
//            agResource.setIsShow("0");
//            //设置此类型是预览图类型
//            agResource.setType("3");
//
//            //设置分类id
//            agResource.setCategoryId(categoryId);
//            //名称设置
//            agResource.setSourceName(filename);
//            //旧名称设置
//            agResource.setOldName(filename);
//            String suffix = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
//            agResource.setSuffix(suffix);
//            agResource.setSize(String.valueOf(thumbFile.getSize()));
//            agResource.setCreateTime(new Date());
//            agResource.setId(UUID.randomUUID().toString());
//            agResource.setSourceId(resourceId);
//            agHouseMapper.insert(agResource);
//        } catch (Exception e) {
//            log.info("上传缩略图失败-------" + e.getMessage());
//            throw new SourceException("上传缩略图失败");
//        }
//        return returnPath;
//    }
//
//    /**
//     * 上传预览图
//     * @param resource 预览图相关信息
//     * @param thumbEntity 预览图文件
//     * @param categoryId 预览图存储分类id
//     * @param resourceId 预览图所关联的id
//     * @return
//     */
//    private String uploadThumbFile(AgHouse resource, FileEntity thumbEntity, String categoryId, String resourceId) {
//        String returnPath = null;
//        try {
//            String filename = thumbEntity.getName();
//            //缩略图名称需要去掉前缀
//            filename = filename.substring(filename.lastIndexOf("/") + 1);
//            String basePath = getBaseFilePath();
//            String parentCategoryPath = this.getParentCategoryPath(categoryId);
//            String path = parentCategoryPath + File.separator + filename;
//            returnPath = parentCategoryPath + "/" + filename;
//            File upload = new File(basePath + File.separator + path);
//            writeFileToDisk(thumbEntity, upload);
//
//            AgHouse agResource = new AgHouse();
//            agResource.setSourceId(resource.getSourceId());
//            agResource.setUserId(resource.getUserId());
//
//            //设置本条信息不可见
//            agResource.setIsShow("0");
//            //设置此类型是预览图类型
//            agResource.setType("3");
//
//            //设置分类id
//            agResource.setCategoryId(categoryId);
//            //名称设置
//            agResource.setSourceName(filename);
//            //旧名称设置
//            agResource.setOldName(filename);
//            String suffix = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
//            agResource.setSuffix(suffix);
//            agResource.setSize(String.valueOf(thumbEntity.getFile().length()));
//            agResource.setCreateTime(new Date());
//            agResource.setId(UUID.randomUUID().toString());
//            agResource.setSourceId(resourceId);
//            agHouseMapper.insert(agResource);
//        } catch (Exception e) {
//            log.info("上传缩略图失败-------" + e.getMessage());
//            throw new SourceException("上传缩略图失败");
//        }
//        return returnPath;
//    }
//
//    private String save3dtiles(AgHouse resource, MultipartFile thumbFile, MultipartFile modelFiles[]) {
//        //统计模型文件的总大小
//        int modelSize = 0;
//        for (MultipartFile file : modelFiles) {
//            modelSize += file.getSize();
//        }
//
//        //遍历数据，重新封装，找到文件夹名称
//        Map<String, String> dirNames = saveCategoryFromFileList(resource.getCategoryId(), modelFiles);
//
//        String returnParentCategoryId = null;
//        //找到tileset.json,上传，设置可见，因为整条记录就展示这条信息可见
//        for (MultipartFile file : modelFiles) {
//            if (file.getOriginalFilename().contains("tileset.json")) {
//                AgHouse agResource = new AgHouse();
//                //设置备注
//                agResource.setRemark(resource.getRemark());
//                agResource.setHourseName(resource.getHourseName());
//                agResource.setCostEstimates(resource.getCostEstimates());
//                agResource.setFloorArea(resource.getFloorArea());
//                agResource.setCoveredArea(resource.getCoveredArea());
//                agResource.setHomesteadArea(resource.getHomesteadArea());
////                agResource.setSourceId(resource.getSourceId());
//                agResource.setUserId(resource.getUserId());
//                agResource.setTableName(resource.getTableName());
//                agResource.setStructureType(resource.getStructureType());
//                agResource.setModelSize(modelSize + "");
//
//                //设置status=1是rvt，2是3dtiles
//                agResource.setStatus(2);
//                //设置本条信息可见
//                agResource.setIsShow("1");
//                //设置此类型是模型图类型
//                agResource.setType("1");
//
//                //从树形结构里面获取分类id
//                String filename = file.getOriginalFilename();
//                //因为从文件名称获取到的目录树里面的url结构前面默认添加了“/”，所以此处需要拼接上“/”
//                String categoryId = dirNames.get("/" + filename.substring(0, filename.lastIndexOf("/")));
//                //设置分类id
//                agResource.setCategoryId(categoryId);
//                //重新设置文件名称，去掉文件路径前缀
//                String newFileName = filename.substring(filename.lastIndexOf("/") + 1);
//                //名称设置
//                agResource.setSourceName(newFileName);
//                //旧名称设置
//                agResource.setOldName(newFileName);
//                //设置缩略图
//                if (thumbFile != null && !thumbFile.isEmpty()) {
//                    String parentCategoryPath = this.getParentCategoryPath(categoryId);
//                    String thumbPath = parentCategoryPath + "/" + thumbFile.getOriginalFilename();
//                    agResource.setThumb(thumbPath);
//                }
//                this.saveResource(agResource, file);
//                //设置返回值父类id
//                returnParentCategoryId = categoryId;
//                //设置父类资源id
//                resource.setSourceId(agResource.getId());
//                break;
//            }
//        }
//
//
//        //如果资源父级分类id为null，没找到tileset.json
//        if (returnParentCategoryId == null) {
//            throw new SourceException("请选择3dtiles文件");
//        }
//
//        //上传缩略图
//        uploadThumbFile(resource, thumbFile, returnParentCategoryId, resource.getSourceId());
//
//        //再次遍历资源，上传资源
//        for (MultipartFile file : modelFiles) {
//            //排除掉上面已经上传的tileset.json
//            if (file.getOriginalFilename().contains("tileset.json")) {
//                continue;
//            }
//            packageAndSaveResourceNotShow(resource, dirNames, file, "1");
//        }
//        return returnParentCategoryId;
//    }
//
//    /**
//     * 重新组装资源参数，保存资源
//     * @param resource 资源信息
//     * @param dirNames 目录信息
//     * @param file 资源文件
//     * @param type 设置资源类型；1模型图类型；2户型图类型；3缩略图类型
//     */
//    private void packageAndSaveResourceNotShow(AgHouse resource, Map<String, String> dirNames, MultipartFile file, String type) {
//        AgHouse agResource = new AgHouse();
//        agResource.setSourceId(resource.getSourceId());
//        agResource.setUserId(resource.getUserId());
//
//        //设置本条信息不可见
//        agResource.setIsShow("0");
//        //设置类型
//        agResource.setType(type);
//
//        //从树形结构里面获取分类id
//        String filename = file.getOriginalFilename();
//        //因为从文件名称获取到的目录树里面的url结构前面默认添加了“/”，所以此处需要拼接上“/”
//        String categoryId = dirNames.get("/" + filename.substring(0, filename.lastIndexOf("/")));
//        //设置分类id
//        agResource.setCategoryId(categoryId);
//        //重新设置文件名称，去掉文件路径前缀
//        String newFileName = filename.substring(filename.lastIndexOf("/") + 1);
//        //名称设置
//        agResource.setSourceName(newFileName);
//        //旧名称设置
//        agResource.setOldName(newFileName);
//        this.saveResource(agResource, file);
//    }
//
//
//    /**
//     * 重新组装资源参数，保存资源
//     * @param resource 资源信息
//     * @param dirNames 目录信息
//     * @param entity 资源文件
//     * @param type 设置资源类型；1模型图类型；2户型图类型；3缩略图类型
//     */
//    private void packageAndSaveResourceNotShow(AgHouse resource, Map<String, String> dirNames, FileEntity entity, String type) {
//        AgHouse agResource = new AgHouse();
//        agResource.setSourceId(resource.getSourceId());
//        agResource.setUserId(resource.getUserId());
//
//        //设置本条信息不可见
//        agResource.setIsShow("0");
//        //设置类型
//        agResource.setType(type);
//
//        //从树形结构里面获取分类id
//        String filename = entity.getName();
//        //因为从文件名称获取到的目录树里面的url结构前面默认添加了“/”，所以此处需要拼接上“/”
//        String categoryId = dirNames.get("/" + filename.substring(0, filename.lastIndexOf("/")));
//        //设置分类id
//        agResource.setCategoryId(categoryId);
//        //重新设置文件名称，去掉文件路径前缀
//        String newFileName = filename.substring(filename.lastIndexOf("/") + 1);
//        //名称设置
//        agResource.setSourceName(newFileName);
//        //旧名称设置
//        agResource.setOldName(newFileName);
//        this.saveResource(agResource, entity);
//    }
//
//    @Override
//    public void saveTableFileToMongodb(String collcetionName, MultipartFile file){
//        if(file == null || file.isEmpty()){
//            return;
//        }
//        String filename = file.getOriginalFilename();
//        boolean collectionExists = mongoTemplate.collectionExists(collcetionName);
//        if(collectionExists){
//            throw new SourceException("关联数据已存在");
//        }
//        try{
//            if(filename.endsWith(".csv")){
//                saveCsvFileToMongodb(file, collcetionName);
//            }else if(filename.endsWith(".xls") || filename.endsWith(".xlsx")){
//                saveXlsFileToMongodb(file, collcetionName);
//            }else{
//                throw new SourceException("关联数据格式只支持csv/xls/xlsx");
//            }
//        }catch (SourceException e){
//            log.info("---------上传关联数据到mongodb失败--------" + e.getMessage());
//            //出现异常回退表名
//            boolean collection = mongoTemplate.collectionExists(collcetionName);
//            if(collection){
//                mongoTemplate.dropCollection(collcetionName);
//            }
//            throw new SourceException(e.getMessage());
//        }catch (Exception e){
//            log.info("---------上传关联数据到mongodb失败--------" + e.getMessage());
//            //出现异常回退表名
//            boolean collection = mongoTemplate.collectionExists(collcetionName);
//            if(collection){
//                mongoTemplate.dropCollection(collcetionName);
//            }
//            throw new SourceException("关联数据解析失败，请用标准的模板导入");
//        }
//    }
//    private void saveXlsFileToMongodb(MultipartFile file, String collcetionName) {
//        List<Object[]> excelData = BimExcelUtil.getExcelData(file);
//        if(excelData == null || excelData.size() == 0){
//            throw new SourceException("关联数据表格内容不存在");
//        }
//        //创建表格集合
//        mongoTemplate.createCollection(collcetionName);
//        //组装数据
//        for(int i = 1; i < excelData.size(); i++){
//            if(excelData.get(i) != null && excelData.get(i).length > 0){
//                Document document = new Document();
//                for(int j = 0; j < excelData.get(i).length; j ++){
//                    //第一行是表的属性
//                    document.put((String)excelData.get(0)[j], excelData.get(i)[j]);
//                }
//                mongoTemplate.save(document, collcetionName);
//            }
//        }
//    }
//
//    private void saveCsvFileToMongodb(MultipartFile file, String collcetionName) {
//        Object[][] csvData = CsvUtil.getCsvData(file);
//        if(csvData == null || csvData.length == 0){
//            throw new SourceException("关联数据表格内容不存在");
//        }
//        //创建表格集合
//        mongoTemplate.createCollection(collcetionName);
//        //组装数据
//        for(int i = 1; i < csvData.length; i++){
//            if(csvData[i] != null && csvData[i].length > 0){
//                Document document = new Document();
//                for(int j = 0; j < csvData[i].length; j ++){
//                    document.put((String)csvData[0][j], csvData[i][j]);
//                }
//                mongoTemplate.save(document, collcetionName);
//            }
//        }
//    }
//
//    @Override
//    @Transactional
//    public void save(AgHouse resource, MultipartFile thumbFile, MultipartFile modelFiles[], MultipartFile dirFiles[], MultipartFile tableFile) throws SourceException {
//        //表名是否是以文件方式
//        String collcetionName = null;
//        if(tableFile != null && !tableFile.isEmpty()){
//            //获取关联表的表名,以文件名命名
//            collcetionName = tableFile.getOriginalFilename().substring(0, tableFile.getOriginalFilename().lastIndexOf("."));
//
//            //保存3dtiles的模型
//            resource.setTableName(collcetionName);
//        }
//        String parentCategoryId = save3dtiles(resource, thumbFile, modelFiles);
//
//        //文件夹是否为null
//        if (dirFiles != null && dirFiles.length > 0 && !dirFiles[0].isEmpty()) {
//            //设置父级分类id
//            resource.setCategoryId(parentCategoryId);
//            //遍历数据，重新封装，找到文件夹名称,且保存
//            Map<String, String> dirNames = saveCategoryFromFileList(resource.getCategoryId(), dirFiles);
//            //再次遍历资源，上传资源
//            for (MultipartFile file : dirFiles) {
//                packageAndSaveResourceNotShow(resource, dirNames, file, "2");
//            }
//        }
//
//        //上传关联文件到数据表mongodb
//        saveTableFileToMongodb(collcetionName, tableFile);
//    }
//
//    /**
//     * 从参数文件列表中获取目录树结构，且保存
//     * @param categoryId 资源父分类id
//     * @param dirFiles 资源文件列表
//     * @return 以文件全路径为key，value是分类的id，如key=/source/tileset.json,value=cb0247ef-00fa-434c-91ef-4f73904fd42c
//     */
//    @Override
//    public Map<String, String> saveCategoryFromFileList(String categoryId, MultipartFile[] dirFiles) {
//        StringBuffer sb = new StringBuffer();
//        for (MultipartFile file : dirFiles) {
//            //判断文件名称是否携带文件夹，如果携带，需要把该文件夹的名称作为分类名称
//            String filename = file.getOriginalFilename();
//            sb.append(filename.substring(0, filename.lastIndexOf("/")));
//            sb.append(",");
//        }
//        //遍历文件夹，获取树形结构
//        String dirStrs = sb.toString().substring(0, sb.length() - 1);
//        //文件目录树形结构
//        JSONObject jsonObject = PathToTreeUtil.pathToTree(dirStrs);
//        //遍历，创建目录
//        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("content");
//        //设置树形结构返回值，把分类id放入map集合，使用path路径作为key
//        Map<String, String> dirNames = new HashMap<>();
//        if (jsonArray != null) {
//            for (int i = 0; i < jsonArray.size(); i++) {
//                JSONObject childJsonObj = jsonArray.getJSONObject(i);
//                String name = childJsonObj.getString("name");
//                String url = childJsonObj.getString("url");
//                //赋值分类
//                AgCategory category = new AgCategory();
//                category.setName(name);
//                category.setPId(categoryId);
//                //创建分类
//                AgCategory saveCategory = this.saveCategory(category);
//                //添加分类，把分类id放入map结合
//                dirNames.put(url, saveCategory.getId());
//                dealWithChildrenDir(childJsonObj.getJSONArray("content"), dirNames, saveCategory.getId());
//            }
//        }
//        return dirNames;
//    }
//
//
//    public Map<String, String> saveCategoryFromFileList(String categoryId, List<FileEntity> dirFiles) {
//        StringBuffer sb = new StringBuffer();
//        for (FileEntity file : dirFiles) {
//            //判断文件名称是否携带文件夹，如果携带，需要把该文件夹的名称作为分类名称
//            String filename = file.getName();
//            sb.append(filename.substring(0, filename.lastIndexOf("/")));
//            sb.append(",");
//        }
//        //遍历文件夹，获取树形结构
//        String dirStrs = sb.toString().substring(0, sb.length() - 1);
//        //文件目录树形结构
//        JSONObject jsonObject = PathToTreeUtil.pathToTree(dirStrs);
//        //遍历，创建目录
//        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("content");
//        //设置树形结构返回值，把分类id放入map集合，使用path路径作为key
//        Map<String, String> dirNames = new HashMap<>();
//        if (jsonArray != null) {
//            for (int i = 0; i < jsonArray.size(); i++) {
//                JSONObject childJsonObj = jsonArray.getJSONObject(i);
//                String name = childJsonObj.getString("name");
//                String url = childJsonObj.getString("url");
//                //赋值分类
//                AgCategory category = new AgCategory();
//                category.setName(name);
//                category.setPId(categoryId);
//                //创建分类
//                AgCategory saveCategory = this.saveCategory(category);
//                //添加分类，把分类id放入map结合
//                dirNames.put(url, saveCategory.getId());
//                dealWithChildrenDir(childJsonObj.getJSONArray("content"), dirNames, saveCategory.getId());
//            }
//        }
//        return dirNames;
//    }
//
//    private void dealWithChildrenDir(JSONArray jsonArray, Map<String, String> dirNames, String categoryId) {
//        if (jsonArray != null) {
//            for (int i = 0; i < jsonArray.size(); i++) {
//                JSONObject childJsonObj = jsonArray.getJSONObject(i);
//                String name = childJsonObj.getString("name");
//                String url = childJsonObj.getString("url");
//                //赋值分类
//                AgCategory category = new AgCategory();
//                category.setName(name);
//                category.setPId(categoryId);
//                //创建分类
//                AgCategory saveCategory = this.saveCategory(category);
//                //添加分类，把分类id放入map结合
//                dirNames.put(url, saveCategory.getId());
//                dealWithChildrenDir(childJsonObj.getJSONArray("content"), dirNames, saveCategory.getId());
//            }
//        }
//    }
//
//    @Override
//    public List<Ztree> getAllCategory() {
//        List<Ztree> returnList = new ArrayList<>();
//        //查询所有有效的分类数据
//        AgCategoryExample example = new AgCategoryExample();
//        example.createCriteria().andIsEnableEqualTo("1");
//        example.setOrderByClause("create_time asc");
//        List<AgCategory> categoryList = categoryMapper.selectByExample(example);
//        String rootId = null;
//        if (categoryList != null && categoryList.size() > 0) {
//            for (AgCategory category : categoryList) {
//                Ztree ztree = new Ztree();
//                ztree.setId(category.getId());
//                ztree.setpId(category.getPId());
//                ztree.setInfo(category.getRemark());
//                ztree.setName(category.getName());
//                ztree.setMapParamId(rootId);
//                if (category.getId().equals("root")) {
//                    rootId = category.getId();
//                    ztree.setMapParamId(rootId);
//                }
//                returnList.add(ztree);
//            }
//        }
//        return returnList;
//    }
//
//    @Override
//    public PageInfo<AgHouseCustom> find(AgHouse resource, Page page) {
//        //查询参数封装
//        AgHouseExample agResourceExample = new AgHouseExample();
//        AgHouseExample.Criteria criteria = agResourceExample.createCriteria();
//        //查看该分类是否有子级
//        List<String> categoryIds = null;
//        //categoryId不为null
//        if (!StringUtils.isEmpty(resource.getCategoryId())) {
//            String categoryId = resource.getCategoryId();
//            categoryIds = new ArrayList<>();
//            //把自身条件添加进去
//            categoryIds.add(categoryId);
//            AgCategory category = new AgCategory();
//            category.setPId(categoryId);
//            AgCategoryExample agCategoryExample = new AgCategoryExample();
//            agCategoryExample.createCriteria().andPIdEqualTo(categoryId);
//            List<AgCategory> categoryList = categoryMapper.selectByExample(agCategoryExample);
//            if (categoryList != null && categoryList.size() > 0) {
//                for (AgCategory dbCategory : categoryList) {
//                    //此处需要支持多级分类，多级分类不管是否有效，都需要查询
//                    categoryIds.add(dbCategory.getId());
//                    getChildren(dbCategory, categoryIds);
//                }
//            }
//            criteria.andCategoryIdIn(categoryIds);
//        }
//        //查询条件
//        if (!StringUtils.isEmpty(resource.getHourseName())) {
//            criteria.andHourseNameLike("%" + resource.getHourseName() + "%");
//        }
//        //结构类型
//        if(!StringUtils.isEmpty(resource.getStructureType())){
//            String[] structures = resource.getStructureType().split(",");
//            List<String> list = Arrays.asList(structures);
//            criteria.andStructureTypeIn(list);
//        }
//        //默认展示的信息
//        criteria.andIsShowEqualTo("1");
//        agResourceExample.setOrderByClause("create_time desc");
//        Page resultPage = PageHelper.startPage(page);;
//        List<AgHouse> dbList = agHouseMapper.selectByExample(agResourceExample);
//        //返回值重新封装
//        List<AgHouseCustom> resultList = new ArrayList<>();
//        if (dbList != null && dbList.size() > 0) {
//            for (AgHouse custom : dbList) {
//                AgHouseCustom resultCustom = new AgHouseCustom();
//                BeanUtils.copyProperties(custom, resultCustom);
//                //设置存储路径
//                resultCustom.setStoreFullPath(this.getParentCategoryPath(custom.getCategoryId()) + "/" + custom.getSourceName());
//
//                //设置授权数量
//                AgPermissionExample permissionExample = new AgPermissionExample();
//                permissionExample.createCriteria().andSourceIdEqualTo(resultCustom.getId()).andTypeEqualTo("3");
//                List<AgPermission> agResourcePermissions = agPermissionMapper.selectByExample(permissionExample);
//                if(agResourcePermissions != null && agResourcePermissions.size() > 0){
//                    resultCustom.setAuthsNum(agResourcePermissions.size());
//                    resultCustom.setAuthList(agResourcePermissions);
//                }
//
//                resultList.add(resultCustom);
//            }
//        }
//        PageInfo<AgHouseCustom> pageInfo = new PageInfo<>(resultList);
//        pageInfo.setTotal(resultPage.getTotal());
//        return pageInfo;
//    }
//
//    public void getChildren(AgCategory category, List<String> categoryIds) {
//        AgCategoryExample agCategoryExample = new AgCategoryExample();
//        agCategoryExample.createCriteria().andPIdEqualTo(category.getId());
//        List<AgCategory> categoryList = categoryMapper.selectByExample(agCategoryExample);
//        if (categoryList != null && categoryList.size() > 0) {
//            for (AgCategory dbCategory : categoryList) {
//                categoryIds.add(dbCategory.getId());
//                getChildren(dbCategory, categoryIds);
//            }
//        }
//    }
//
//
//    @Override
//    public AgCategory saveCategory(AgCategory category) {
//        //判断，如果分类名称相同，就不需要创建，抛出异常
//        AgCategoryExample example = new AgCategoryExample();
//        example.createCriteria().andNameEqualTo(category.getName()).andPIdEqualTo(category.getPId());
//        List<AgCategory> agCategories = categoryMapper.selectByExample(example);
//        if (agCategories != null && agCategories.size() > 0) {
//            throw new SourceException("资源目录已存在");
////            return agCategories.get(0);
//        }
//
//        //设置参数
//        category.setCreateTime(new Date());
//        category.setId(UUID.randomUUID().toString());
//        //默认设置 可用1
//        category.setIsEnable("1");
//        categoryMapper.insert(category);
//        return category;
//    }
//
//    @Override
//    public void deleteCategory(String id) {
//        AgCategory category = categoryMapper.selectByPrimaryKey(id);
//        if (category == null) {
//            throw new SourceException("参数错误");
//        }
//        if ("root".equals(category.getId())) {
//            throw new SourceException("此数据不能删除");
//        }
//        category.setIsEnable("0");
//        categoryMapper.updateByPrimaryKey(category);
//    }
//
//    @Override
//    public void updateCategory(AgCategory category) {
//        //判断是否修改了分类名称
//        AgCategory dbCategory = categoryMapper.selectByPrimaryKey(category.getId());
//        if (dbCategory == null) {
//            throw new SourceException("数据不存在，修改失败");
//        }
//        //如果名称不一样，需要修改分类名称
//        if (!dbCategory.getName().equals(category.getName())) {
//            try {
//                //从当前分类中找到父路径分类
//                String parentCategoryPath = this.getParentCategoryPathNoIncloudParam(category.getId());
//                parentCategoryPath = "/".equals(File.separator) ? parentCategoryPath.replaceAll("/", File.separator) : parentCategoryPath.replaceAll("/", File.separator + File.separator);
//                //获取当前文件路径
//                String basePath = getBaseFilePath();
//                //拼接系统文件夹和当前文件的路径
//                String oldPath = basePath + File.separator + parentCategoryPath + dbCategory.getName();
//                String newPath = basePath + File.separator + parentCategoryPath + category.getName();
//                File oldFile = new File(oldPath);
//                if(oldFile.exists()){
//                    File newFile = new File(newPath);
//                    //重命名
//                    boolean renameTo = oldFile.renameTo(newFile);
//                    if (!renameTo) {
//                        log.info("修改文件夹失败------------oldFile=" + oldFile);
//                        log.info("修改文件夹失败------------newFile=" + newFile);
//                        throw new SourceException("修改失败资源目录");
//                    }
//                }
//            } catch (Exception e) {
//                log.info(e.getMessage());
//                throw new SourceException("资源占用，不能修改");
//            }
//        }
//        //重新组装修改的数据
//        AgCategory agCategory = new AgCategory();
//        agCategory.setId(category.getId());
//        agCategory.setModifyTime(new Date());
//        agCategory.setName(category.getName());
//        agCategory.setRemark(category.getRemark());
//        categoryMapper.updateByPrimaryKeySelective(agCategory);
//    }
//
//    @Override
//    public List<AgSysSetting> getAllSysSetting() {
//        return sysSettingMapper.selectByExample(null);
//    }
//
//    @Override
//    public String getResourceViewPath(Map<String, String> map) {
//        //资源分类，查询用到
//        List<String> sourceCategoryIds = new ArrayList<>();
//        //资源名称
//        String fileName = null;
//        //遍历参数
//        Set<Map.Entry<String, String>> entrySet = map.entrySet();
//        //最后一个是文件名
//        int i = 0;
//
//        //分类id集合缓存，查询需要用到
//        List<String> categoryIds = null;
//        for (Map.Entry<String, String> entry : entrySet) {
//            String value = entry.getValue();
//            //把value里面的参数获取出来，查询分类，最终获取到分类id，再用分类id和名称确定该文件
//            i++;
//            if (i == entrySet.size()) {
//                //最后一层循环，只对文件名赋值
//                fileName = value;
//                break;
//            }
//            //获取分类信息
//            AgCategoryExample example = new AgCategoryExample();
//            //名称eq，必须是可用的
//            AgCategoryExample.Criteria criteria = example.createCriteria();
//            criteria.andNameEqualTo(value).andIsEnableEqualTo("1");
//            if (categoryIds != null) {
//                criteria.andPIdIn(categoryIds);
//            }
//            List<AgCategory> agCategories = categoryMapper.selectByExample(example);
//            //查询数失败，参数错误
//            if (agCategories == null || agCategories.size() == 0) {
//                return null;
//            }
//            //重新清空信息
//            if (categoryIds != null) {
//                categoryIds.clear();
//            }
//            //如果为null，重新创建
//            if (categoryIds == null) {
//                categoryIds = new ArrayList<>();
//            }
//            for (AgCategory agCategory : agCategories) {
//                categoryIds.add(agCategory.getId());
//                //倒数第二层循环赋值，找到分类id，下一层循环为文件资源名
//                if (i == entrySet.size() - 1) {
//                    sourceCategoryIds.add(agCategory.getId());
//                }
//            }
//        }
//        //查询资源
//        AgHouseExample agResourceExample = new AgHouseExample();
//        AgHouseExample.Criteria agResourceExampleCriteria = agResourceExample.createCriteria();
//        if (sourceCategoryIds.size() > 0) {
//            agResourceExampleCriteria.andCategoryIdIn(sourceCategoryIds);
//        }
//        agResourceExampleCriteria.andSourceNameEqualTo(fileName);
//        List<AgHouse> agResources = agHouseMapper.selectByExample(agResourceExample);
//        if (agResources == null || agResources.size() == 0) {
//            return null;
//        }
//        //如果有多个资源，只获取第一个
//        AgHouse agResource = agResources.get(0);
//        //从第一个中获取最终路径
//        String parentCategoryPath = this.getParentCategoryPath(agResource.getCategoryId());
//        return parentCategoryPath + "/" + agResource.getSourceName();
//    }
//
//    @Override
//    public String getTreeDirPath(String sourceId) {
//
//        //查询资源信息
//        AgHouse agResource = agHouseMapper.selectByPrimaryKey(sourceId);
//        if (agResource == null) {
//            return null;
//        }
//        String categoryPath = getParentCategoryPath(agResource.getCategoryId());
//        if (categoryPath != null) {
//            categoryPath += "/" + agResource.getSourceName();
//        }
//        return getApiURL() + categoryPath;
//    }
//
//
//    @Override
//    public String getParentCategoryPath(String categoryId) {
//        //通过资源id查询分类名称
//        if (StringUtils.isEmpty(categoryId)) {
//            throw new SourceException("分类参数有误");
//        }
//        //查询当前资源绑定的分类
//        AgCategory category = categoryMapper.selectByPrimaryKey(categoryId);
//        if (category == null) {
//            log.info("---------categoryId=" + categoryId);
//            throw new SourceException("分类查询失败");
//        }
//        //如果当前分类的pid是root，就是顶层，直接组装参数返回
//        if ("root".equals(category.getPId())) {
//            return category.getName();
//        }
//        //分类级别的名称列表，后面需要重新组装
//        List<String> categoryNames = new ArrayList<>();
//        getParentCategoryName(category.getPId(), categoryNames);
//        StringBuffer sb = new StringBuffer();
//        for (int i = categoryNames.size() - 1; i >= 0; i--) {
//            sb.append(categoryNames.get(i));
//            sb.append("/");
//        }
//        return sb.toString() + category.getName();
//    }
//
//
//    @Override
//    public String getParentCategoryPathNoIncloudParam(String categoryId) {
//        //通过资源id查询分类名称
//        if (StringUtils.isEmpty(categoryId)) {
//            return null;
//        }
//        //查询当前资源绑定的分类
//        AgCategory category = categoryMapper.selectByPrimaryKey(categoryId);
//        if (category == null) {
//            return null;
//        }
//        //如果当前分类的pid是root，就是顶层，直接组装参数返回
//        if ("root".equals(category.getPId())) {
//            return "";
//        }
//        //分类级别的名称列表，后面需要重新组装
//        List<String> categoryNames = new ArrayList<>();
//        getParentCategoryName(category.getPId(), categoryNames);
//        StringBuffer sb = new StringBuffer();
//        for (int i = categoryNames.size() - 1; i >= 0; i--) {
//            sb.append(categoryNames.get(i));
//            sb.append("/");
//        }
//        return sb.toString();
//    }
//
//    @Override
//    public List<AgHouse> findHouseDir(String id) {
//        AgHouseExample agResourceExample = new AgHouseExample();
//        agResourceExample.createCriteria().andSourceIdEqualTo(id).andTypeEqualTo("2");
//        List<AgHouse> resultList = agHouseMapper.selectByExample(agResourceExample);
//        if (resultList != null && resultList.size() > 0) {
//            for (AgHouse custom : resultList) {
//                custom.setStoreFullPath(this.getParentCategoryPath(custom.getCategoryId()) + "/" + custom.getSourceName());
//            }
//        }
//
//        return resultList;
//    }
//
//    @Override
//    public String findThumb(String id) {
//        AgHouse agResource = agHouseMapper.selectByPrimaryKey(id);
//        if (agResource != null) {
//            return agResource.getThumb();
//        }
//        return null;
//    }
//
//
//    /**
//     * 获取分类分类名称，放到list集合
//     *
//     * @param pid
//     * @param categoryName
//     */
//    private void getParentCategoryName(String pid, List<String> categoryName) {
//        AgCategory category = categoryMapper.selectByPrimaryKey(pid);
//        if (category != null) {
//            categoryName.add(category.getName());
//            //如果是root，已经查询到信息
//            if ("root".equals(category.getPId())) {
//                return;
//            }
//            getParentCategoryName(category.getPId(), categoryName);
//        }
//    }
//
//    /**
//     * 或许系统设置的api
//     *
//     * @return
//     */
//    private String getApiURL() {
//        //找到type=3为接口访问
//        AgSysSettingExample example = new AgSysSettingExample();
//        example.createCriteria().andTypeEqualTo("3");
//        List<AgSysSetting> agSysSettings = sysSettingMapper.selectByExample(example);
//        if (agSysSettings != null && agSysSettings.size() > 0) {
//            return agSysSettings.get(0).getPath();
//        }
//        return null;
//    }
//
//    /**
//     * 获取系统文件上传的根路径
//     *
//     * @return
//     * @throws FileNotFoundException
//     */
//    @Override
//    public String getBaseFilePath() throws FileNotFoundException {
////        File basePath = new File(ResourceUtils.getURL("classpath:").getPath());
////        if (!basePath.exists()) {
////            basePath = new File("");
////        }
////        return basePath.getAbsolutePath() + File.separator + filePath;
//        return filePath;
//    }
//
//
//
//
//    @Override
//    @Transactional
//    public void update(AgHouseCustom resource, MultipartFile thumbFile, MultipartFile dirFiles[]) {
//        AgHouse dbResource = agHouseMapper.selectByPrimaryKey(resource.getId());
//        if(dbResource == null){
//            throw new SourceException("该数据不存在");
//        }
//        AgHouse updateEntity = new AgHouse();
//        //修改缩略图
//        if(thumbFile != null && !thumbFile.isEmpty()){
//            try{
//                //直接覆盖此缩略图
//                String basePath = getBaseFilePath();
//                String path = dbResource.getThumb();
//                //是否有缩略图
//                if(StringUtils.isEmpty(path)){
//                    //没有缩略图，需要重新添加
//                    String thumbPath = uploadThumbFile(resource, thumbFile, dbResource.getCategoryId(), resource.getId());
//                    updateEntity.setThumb(thumbPath);
//                }else{
//                    //已经有缩略图，就替换
//                    File upload = new File(basePath + File.separator + path);
//                    if (!upload.exists()) {
//                        upload.mkdirs();
//                    }
//                    thumbFile.transferTo(upload);
//                }
//            }catch (Exception e){
//                throw new SourceException("缩略图上传失败，请稍后再试");
//            }
//        }
//        //修改户型图
//        if(dirFiles != null && dirFiles.length > 0 && !dirFiles[0].isEmpty()){
//            //先删除所有的户型图信息，户型图分类，户型图源文件
//            AgHouseExample dirExample = new AgHouseExample();
//            dirExample.createCriteria().andTypeEqualTo("2").andSourceIdEqualTo(resource.getId());
//            List<AgHouse> agResources = agHouseMapper.selectByExample(dirExample);
//            //是否有户型图，没有直接添加，不用删除
//            if(agResources != null && agResources.size() > 0){
//                //查询资源父分类目录
//                String sourcePath = this.getParentCategoryPath(agResources.get(0).getCategoryId());
//
//                //删除户型图信息
//                agHouseMapper.deleteByExample(dirExample);
//
//                //删除户型图分类
//                AgCategoryExample delCategoryExample = new AgCategoryExample();
//                delCategoryExample.createCriteria().andIdEqualTo(agResources.get(0).getCategoryId());
//                categoryMapper.deleteByExample(delCategoryExample);
//
//                //删除户型图源文件
//                deleteFileFromCategorySourcePath(sourcePath);
//            }
//
//            //添加户型图
//            dbResource.setSourceId(resource.getId());
//            //遍历数据，重新封装，找到文件夹名称,且保存
//            Map<String, String> dirNames = saveCategoryFromFileList(dbResource.getCategoryId(), dirFiles);
//            //再次遍历资源，上传资源
//            for (MultipartFile file : dirFiles) {
//                packageAndSaveResourceNotShow(dbResource, dirNames, file, "2");
//            }
//        }
//
//        //只修改需要修改的属性
//        updateEntity.setId(resource.getId());
//        updateEntity.setStructureType(resource.getStructureType());
//        updateEntity.setHourseName(resource.getHourseName());
//        updateEntity.setHomesteadArea(resource.getHomesteadArea());
//        updateEntity.setFloorArea(resource.getFloorArea());
//        updateEntity.setCoveredArea(resource.getCoveredArea());
//        updateEntity.setCostEstimates(resource.getCostEstimates());
//        updateEntity.setRemark(resource.getRemark());
//        updateEntity.setModifyTime(new Date());
//        agHouseMapper.updateByPrimaryKeySelective(updateEntity);
//
//        //数据访问权限修改，先清空所有的权限，再重新添加
//        AgPermissionExample resourcePermissionExample = new AgPermissionExample();
//        resourcePermissionExample.createCriteria().andSourceIdEqualTo(resource.getId()).andTypeEqualTo("3");
//        agPermissionMapper.deleteByExample(resourcePermissionExample);
//        //重新添加权限
//        if(!StringUtils.isEmpty(resource.getAuths())){
//            String[] authCodes = resource.getAuths().split(",");
//            for(String code : authCodes){
//                AgPermission permission = new AgPermission();
//                permission.setId(UUID.randomUUID().toString());
//                permission.setSourceId(resource.getId());
//                permission.setCode(code);
//                permission.setType("3");
//                agPermissionMapper.insert(permission);
//            }
//        }
//    }
//
//
//
//    @Override
//    @Transactional
//    public void delete(String id) {
//        //资源关联数据有：资源信息，分类信息，资源表
//        AgHouse agResource = agHouseMapper.selectByPrimaryKey(id);
//        if(agResource == null){
//            throw new SourceException("数据不存在，删除失败");
//        }
//        //当前分类父分类信息
//        AgCategory category = categoryMapper.selectByPrimaryKey(agResource.getCategoryId());
//        if(category == null){
//            throw new SourceException("信息有误，删除失败");
//        }
//        //需要删除的分类ids
//        Set<String> categoryIds = new HashSet<>();
//        categoryIds.add(agResource.getCategoryId());
//        //查询当前所有的分类id
//        AgHouseExample agResourceExample = new AgHouseExample();
//        agResourceExample.createCriteria().andSourceIdEqualTo(id);
//        List<AgHouse> agResourceList = agHouseMapper.selectByExample(agResourceExample);
//        if(agResourceList != null && agResourceList.size() > 0){
//            for(AgHouse categoryResource : agResourceList){
//                categoryIds.add(categoryResource.getCategoryId());
//            }
//        }
//
//        //查询资源父分类目录
//        String sourcePath = this.getParentCategoryPath(agResource.getCategoryId());
//
//        //删除资源
//        agHouseMapper.deleteByPrimaryKey(id);
//        //删除资源关联的所有信息
//        agHouseMapper.deleteByExample(agResourceExample);
//
//        //删除分类
//        AgCategoryExample delCategoryExample = new AgCategoryExample();
//        //重新组装需要删除的分类id，变成list
//        List<String> delCategoryIds = new ArrayList<>();
//        for(String categoryId: categoryIds){
//            delCategoryIds.add(categoryId);
//        }
//        delCategoryExample.createCriteria().andIdIn(delCategoryIds);
//        categoryMapper.deleteByExample(delCategoryExample);
//
////        //删除关联表
////        if(!StringUtils.isEmpty(agResource.getTableName())){
////            mongoTemplate.dropCollection(agResource.getTableName());
////        }
//
//        //删除所有资源目录数据
//        deleteFileFromCategorySourcePath(sourcePath);
//
//        //删除该资源的权限
//        AgPermissionExample example = new AgPermissionExample();
//        example.createCriteria().andSourceIdEqualTo(id).andTypeEqualTo("3");
//        agPermissionMapper.deleteByExample(example);
//    }
//
//
//    /**
//     * 循环删除文件
//     * @param file
//     */
//    void deleteFile(File file){
//        if(file.exists()){
//            if(file.isFile()){
//                //是文件，直接删除
//                file.delete();
//            }
//            if(file.isDirectory()){
//                //是文件夹，判断是否有文件
//                File[] files = file.listFiles();
//                if(files != null && files.length > 0){
//                    for(File delFile : files){
//                        deleteFile(delFile);
//                    }
//                }
//                //直接删除文件夹
//                file.delete();
//            }
//        }
//    }
//
//
//
//
//    @Override
//    public void deleteFileFromCategorySourcePath(String sourcePath) {
//        if (!StringUtils.isEmpty(sourcePath)) {
//            //从磁盘删除存储文件
//            try {
//                String baseFile = getBaseFilePath();
//                String delFile = baseFile + File.separator + sourcePath;
//                //win和linux路径转换
//                delFile = "/".equals(File.separator) ? delFile.replaceAll("/", File.separator) : delFile.replaceAll("/", File.separator + File.separator);
//                log.info("-----delete--------" + delFile);
//                File file = new File(delFile);
//                if (file.exists()) {
//                    deleteFile(file);
//                    file.delete();
//                }
//            } catch (FileNotFoundException e) {
//                log.info("删除文件失败FileNotFoundException-----------delFile=" + e.getMessage());
//            }
//        }
//    }
//
//    @Override
//    public Object statistics() {
//        Map<String, Object> resultMap = new HashMap<>();
//        String groupBystructureType = "select structure_type from ag_house group by structure_type";
//        List<AgHouse> agResources = houseCustomMapper.sql(groupBystructureType);
//        if(agResources != null && agResources.size() > 0){
//            List<Map> lists = new ArrayList<>();
//            for(AgHouse entity : agResources){
//                if(entity != null && !StringUtils.isEmpty(entity.getStructureType())){
//                    Map<String, String> map = new HashMap<>();
//                    map.put("text", entity.getStructureType());
//                    map.put("value", entity.getStructureType());
//                    lists.add(map);
//                }
//            }
//            resultMap.put("structureType", lists);
//        }
//        return resultMap;
//    }
//
//
//
//    @Override
//    @Transactional
//    public void batchDelete(String ids) {
//        for(String id : ids.split(",")){
//            this.delete(id);
//        }
//    }
//
//    /**
//     *
//     * @param rootCategoryId
//     * @param resource
//     * @param allFiles
//     * @return
//     */
//    private String saveRvt(String rootCategoryId, AgHouse resource, List<FileEntity> allFiles){
//        //统计模型文件的总大小
//        int modelSize = 0;
//        for (FileEntity file : allFiles) {
//            modelSize += file.getFile().length();
//        }
//
//        //遍历数据，重新封装，找到文件夹名称
//        Map<String, String> categoryNames = saveCategoryFromFileList(rootCategoryId, allFiles);
//
//        FileEntity thumb = null;
//
//        String returnParentCategoryId = null;
//        //找到xx.rvt上传，设置可见，因为整条记录就展示这条信息可见
//        for (FileEntity file : allFiles) {
//            if (file.getName().endsWith(".rvt")) {
//                AgHouse agResource = new AgHouse();
//                //设置备注
//                agResource.setRemark(resource.getRemark());
//                agResource.setHourseName(resource.getHourseName());
//                agResource.setCostEstimates(resource.getCostEstimates());
//                agResource.setFloorArea(resource.getFloorArea());
//                agResource.setCoveredArea(resource.getCoveredArea());
//                agResource.setHomesteadArea(resource.getHomesteadArea());
////                agResource.setSourceId(resource.getSourceId());
//                agResource.setUserId(resource.getUserId());
//                agResource.setTableName(resource.getTableName());
//                agResource.setStructureType(resource.getStructureType());
//                agResource.setModelSize(modelSize + "");
//
//                //设置status=1是rvt，2是3dtiles
//                agResource.setStatus(1);
//                //设置本条信息可见
//                agResource.setIsShow("1");
//                //设置此类型是模型图类型
//                agResource.setType("1");
//
//                //从树形结构里面获取分类id
//                String filename = file.getName();
//                //因为从文件名称获取到的目录树里面的url结构前面默认添加了“/”，所以此处需要拼接上“/”
//                String categoryId = categoryNames.get("/" + filename.substring(0, filename.lastIndexOf("/")));
//                //设置分类id
//                agResource.setCategoryId(categoryId);
//                //重新设置文件名称，去掉文件路径前缀
//                String newFileName = filename.substring(filename.lastIndexOf("/") + 1);
//                //名称设置
//                agResource.setSourceName(newFileName);
//                //旧名称设置
//                agResource.setOldName(newFileName);
//                //从文件列表找到缩略图
//                for (FileEntity thumbFile : allFiles) {
//                    //设置缩略图,缩略图只带一个/
//                    if (thumbFile.getName().split("/").length == 2 && (thumbFile.getName().endsWith("jpg") || thumbFile.getName().endsWith("jpeg") || thumbFile.getName().endsWith("png"))) {
//                        String parentCategoryPath = this.getParentCategoryPath(categoryId);
//                        String thumbPath = parentCategoryPath + "/" + thumbFile.getName().substring(thumbFile.getName().lastIndexOf("/") + 1);
//                        agResource.setThumb(thumbPath);
//                        thumb = thumbFile;
//                    }
//                }
//
//
//                this.saveResource(agResource, file);
//                //设置返回值父类id
//                returnParentCategoryId = categoryId;
//                //设置父类资源id
//                resource.setSourceId(agResource.getId());
//                break;
//            }
//        }
//        //保存缩略图和户型图
//        //如果资源父级分类id为null，没找到rvt
//        if (returnParentCategoryId == null) {
//            throw new SourceException("文件中不存在rvt模型");
//        }
//
//        //上传缩略图
//        if(thumb != null){
//            uploadThumbFile(resource, thumb, returnParentCategoryId, resource.getSourceId());
//        }
//
//        //上传户型图
//        List<FileEntity> dirFiles = new ArrayList<>();
//        for (FileEntity file : allFiles) {
//            //户型图,户型图携带两个“/”
//            if (file.getName().split("/").length == 3 && (file.getName().endsWith("jpg") || file.getName().endsWith("jpeg") || file.getName().endsWith("png"))) {
//                dirFiles.add(file);
//            }
//        }
//        //文件夹是否为null
//        if (dirFiles.size() > 0) {
//            //设置父级分类id
//            resource.setCategoryId(returnParentCategoryId);
//            //遍历数据，重新封装，找到文件夹名称,且保存
//            Map<String, String> dirNames = saveCategoryFromFileList(resource.getCategoryId(), dirFiles);
//            //再次遍历资源，上传资源
//            for (FileEntity file : dirFiles) {
//                packageAndSaveResourceNotShow(resource, dirNames, file, "2");
//            }
//        }
//
//        return returnParentCategoryId;
//    }
//
//    private String save3dtiles(String rootCategoryId, AgHouse resource, List<FileEntity> allFiles){
//        //统计模型文件的总大小
//        int modelSize = 0;
//        for (FileEntity file : allFiles) {
//            modelSize += file.getFile().length();
//        }
//
//        //遍历数据，重新封装，找到文件夹名称
//        Map<String, String> categoryNames = saveCategoryFromFileList(rootCategoryId, allFiles);
//
//        String returnParentCategoryId = null;
//        //找到xx.rvt上传，设置可见，因为整条记录就展示这条信息可见
//        for (FileEntity file : allFiles) {
//            if (file.getName().endsWith("tileset.json")) {
//                AgHouse agResource = new AgHouse();
//                //设置备注
//                agResource.setRemark(resource.getRemark());
//                agResource.setHourseName(resource.getHourseName());
//                agResource.setCostEstimates(resource.getCostEstimates());
//                agResource.setFloorArea(resource.getFloorArea());
//                agResource.setCoveredArea(resource.getCoveredArea());
//                agResource.setHomesteadArea(resource.getHomesteadArea());
////                agResource.setSourceId(resource.getSourceId());
//                agResource.setUserId(resource.getUserId());
//                agResource.setTableName(resource.getTableName());
//                agResource.setStructureType(resource.getStructureType());
//                agResource.setModelSize(modelSize + "");
//
//                //设置status=1是rvt，2是3dtiles
//                agResource.setStatus(2);
//                //设置本条信息可见
//                agResource.setIsShow("1");
//                //设置此类型是模型图类型
//                agResource.setType("1");
//
//                //从树形结构里面获取分类id
//                String filename = file.getName();
//                //因为从文件名称获取到的目录树里面的url结构前面默认添加了“/”，所以此处需要拼接上“/”
//                String categoryId = categoryNames.get("/" + filename.substring(0, filename.lastIndexOf("/")));
//                //设置分类id
//                agResource.setCategoryId(categoryId);
//                //重新设置文件名称，去掉文件路径前缀
//                String newFileName = filename.substring(filename.lastIndexOf("/") + 1);
//                //名称设置
//                agResource.setSourceName(newFileName);
//                //旧名称设置
//                agResource.setOldName(newFileName);
//
//                this.saveResource(agResource, file);
//                //设置返回值父类id
//                returnParentCategoryId = categoryId;
//                //设置父类资源id
//                resource.setSourceId(agResource.getId());
//                break;
//            }
//        }
//
//        //再次遍历资源，上传资源
//        for (FileEntity file : allFiles) {
//            //排除掉上面已经上传的tileset.json
//            if (file.getName().endsWith("tileset.json")) {
//                continue;
//            }
//            packageAndSaveResourceNotShow(resource, categoryNames, file, "1");
//        }
//        return returnParentCategoryId;
//    }
//
//
//
//    private void setComponentCode(AgHouse  entity, String[] split) {
//        //表代码
//        String tableCode = split[0];
//        //表代码名称
//        String tableCodeName = sysAgCloudSystemService.getBuildComponentDictionaryName(tableCode);
//
//        String largeCode = "";
//        String largeCodeName = "";
//        String mediumCode = "";
//        String mediumCodeName = "";
//        String smallCode = "";
//        String smallCodeName = "";
//        String detailCode = "";
//        String detailCodeName = "";
//
//        //构件编码分类
//        String caCodeNum = split[1];
//        String[] caCodeNums = caCodeNum.split("\\.");
//        //4个分类都有
//        if(caCodeNums.length == 4){
//            largeCode = caCodeNums[0];
//            mediumCode = caCodeNums[1];
//            smallCode = caCodeNums[2];
//            detailCode = caCodeNums[3];
//            //大类名称
//            largeCodeName = sysAgCloudSystemService.getBuildComponentName("1", tableCode, largeCode, "00", "00", null);
//            //中类名称
//            mediumCodeName = sysAgCloudSystemService.getBuildComponentName("2", tableCode, largeCode, mediumCode, "00", null);
//            //小类名称
//            smallCodeName = sysAgCloudSystemService.getBuildComponentName("3", tableCode, largeCode, mediumCode, smallCode, null);
//            //细类名称
//            detailCodeName = sysAgCloudSystemService.getBuildComponentName("4", tableCode, largeCode, mediumCode, smallCode, detailCode);
//        }
//        //细类没有
//        if(caCodeNums.length == 3){
//            largeCode = caCodeNums[0];
//            mediumCode = caCodeNums[1];
//            smallCode = caCodeNums[2];
//            //大类名称
//            largeCodeName = sysAgCloudSystemService.getBuildComponentName("1", tableCode, largeCode, "00", "00", null);
//            //中类名称
//            mediumCodeName = sysAgCloudSystemService.getBuildComponentName("2", tableCode, largeCode, mediumCode, "00", null);
//            //小类名称
//            smallCodeName = sysAgCloudSystemService.getBuildComponentName("3", tableCode, largeCode, mediumCode, smallCode, null);
//        }
//        //小类、细类没有
//        if(caCodeNums.length == 3){
//            largeCode = caCodeNums[0];
//            mediumCode = caCodeNums[1];
//            //大类名称
//            largeCodeName = sysAgCloudSystemService.getBuildComponentName("1", tableCode, largeCode, "00", "00", null);
//            //中类名称
//            mediumCodeName = sysAgCloudSystemService.getBuildComponentName("2", tableCode, largeCode, mediumCode, "00", null);
//        }
//        if(!StringUtils.isEmpty(tableCode)){
//            entity.setComponentCode(tableCode + "-"  + largeCode + "." + smallCode + "." + mediumCode + "." + detailCode);
//            entity.setComponentCodeName(tableCodeName + "-"  + largeCodeName + "." + smallCodeName + "." + mediumCodeName + "." + detailCodeName);
//        }
//    }
//
//    @Override
//    @Transactional
//    public void saveRvtZip(String rootCategoryId, MultipartFile modelFile) {
//        String fileUploadPath = null;
//        try{
//            //获取模型信息
//            String baseFilePath = this.getBaseFilePath();
//            fileUploadPath = uploadTempPath + File.separator + UUID.randomUUID().toString();
//            String path = baseFilePath + File.separator + fileUploadPath;
//            Map<String, Object> zip = BimZipUtils.getHouseRvtFileFromZip(modelFile, path);
//            FileEntity excelFile = (FileEntity)zip.get("excel");;
//            List<FileEntity> modelList = (List<FileEntity>)zip.get("modelFiles");
//            if(excelFile == null || StringUtils.isEmpty(excelFile.getName())){
//                throw new SourceException("zip文件格式不正确，找不到excel文件");
//            }
//            if(modelList == null || modelList.size() == 0){
//                throw new SourceException("zip文件格式不正确，找不到房屋模型文件");
//            }
//            //读取excel文件内容
//            List<Object[]> houseDatas = BimExcelUtil.getExcelData(excelFile.getName(), new FileInputStream(excelFile.getFile()));
//            if(houseDatas == null || houseDatas.size() <= 1){
//                throw new SourceException("请填写房屋信息表数据");
//            }
//
//            //上传信息>> excel表结构:  0名称	1类目编码	2结构类型	3占地面积（平方米）	4建筑面积（平方米）	5造价估计（万元）	6简介
//            for(int i = 1; i < houseDatas.size(); i++){
//                AgHouse house = new AgHouse();
//                //excel格式数据判断
//                if(houseDatas.get(i).length != 7){
//                    throw new SourceException("excel格式不正确，请重新下载模板");
//                }
//                house.setHourseName((String)houseDatas.get(i)[0]);
//                String caCode = (String)houseDatas.get(i)[1];
//                if(!StringUtils.isEmpty(caCode)){
//                    //类目编码不为null，格式是：表代码-大类.种类.小类.细类 如：30-13.10.35.10
//                    //获取表代码
//                    String[] split = caCode.split("-");
//                    if(split != null && split.length == 2){
//                        //设置类目编码
//                        setComponentCode(house, split);
//                    }
//                }
//                house.setStructureType((String)houseDatas.get(i)[2]);
//                house.setFloorArea((String)houseDatas.get(i)[3]);
//                house.setCoveredArea((String)houseDatas.get(i)[4]);
//                house.setCostEstimates((String)houseDatas.get(i)[5]);
//                house.setRemark((String)houseDatas.get(i)[6]);
//
//                //遍历所有模型数据，找到当前模型的所有信息
//                List<FileEntity> thisModels = new ArrayList<>();
//                for(FileEntity entity : modelList){
//                    if(entity.getName().startsWith(house.getHourseName() + "/")){
//                        thisModels.add(entity);
//                    }
//                }
//                if(thisModels.size() == 0){
//                    throw new SourceException("zip文件格式不正确，房屋模型文件找不到");
//                }
//                //保存模型
//                saveRvt(rootCategoryId, house, thisModels);
//            }
//        }catch (SourceException e){
//            log.info("--------SourceException-------" + e.getMessage());;
//            throw new SourceException(e.getMessage());
//        }catch (IOException e){
//            log.info("------上传失败，文件格式不正确---------" + e.getMessage());;
//            throw new SourceException("上传失败，文件格式不正确");
//        }catch (Exception e){
//            log.info("------上传失败---------" + e.getMessage());;
//            throw new SourceException("上传失败");
//        }finally {
//           if(fileUploadPath != null){
//               //删除所有临时文件
//               deleteFileFromCategorySourcePath(fileUploadPath);
//           }
//        }
//    }
//
//    @Override
//    @Transactional
//    public void add3dtilesZip(String rootCategoryId, MultipartFile modelFile) {
//        String fileUploadPath = null;
//        try{
//            //获取模型信息
//            String baseFilePath = this.getBaseFilePath();
//            fileUploadPath = uploadTempPath + File.separator + UUID.randomUUID().toString();
//            String path = baseFilePath + File.separator + fileUploadPath;
//            Map<String, Object> zip = BimZipUtils.getHouse3dtilesFileFromZip(modelFile, path);
//            List<FileEntity> models = (List<FileEntity>)zip.get("models");;
//            List<FileEntity> modelList = (List<FileEntity>)zip.get("modelFiles");
//            if(models == null || models.size()  == 0){
//                throw new SourceException("zip文件格式不正确");
//            }
//            if(modelList == null || modelList.size() == 0){
//                throw new SourceException("zip文件格式不正确，找不到房屋模型文件");
//            }
//            for(FileEntity model: models){
//                String houseName = model.getName();
//                houseName = houseName.substring(0, houseName.indexOf("/"));
//                AgHouse house = new AgHouse();
//                house.setHourseName(houseName);
//                //遍历所有模型数据，找到当前模型的所有信息
//                List<FileEntity> thisModels = new ArrayList<>();
//                for(FileEntity entity : modelList){
//                    if(entity.getName().startsWith(house.getHourseName())){
//                        thisModels.add(entity);
//                    }
//                }
//                //保存模型
//                save3dtiles(rootCategoryId, house, thisModels );
//            }
//        }catch (SourceException e){
//            log.info("--------SourceException-------" + e.getMessage());;
//            throw new SourceException(e.getMessage());
//        }catch (IOException e){
//            log.info("------上传失败，文件格式不正确---------" + e.getMessage());;
//            throw new SourceException("上传失败，文件格式不正确");
//        }catch (Exception e){
//            log.info("------上传失败---------" + e.getMessage());;
//            throw new SourceException("上传失败");
//        }finally {
//            if(fileUploadPath != null){
//                //删除所有临时文件
//                deleteFileFromCategorySourcePath(fileUploadPath);
//            }
//        }
//    }
//
//
//}
