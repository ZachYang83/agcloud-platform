package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.common.exception.ExceptionEnum;
import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.common.util.UUIDUtil;
import com.augurit.agcloud.agcom.agsupport.domain.auto.*;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimCheckProjectModelCustomMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgBimCheckProjectModelMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.domain.ModelTableFieldEnum;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.IBimCheckProjectModelService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.IBimCheckService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service.IAgcim3dbuildingService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service.IAgcim3dprojectService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.util.BimZipUtils;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.util.FileEntity;
import com.augurit.agcloud.agcom.agsupport.sc.serverContent.service.IAgServerContentService;
import com.augurit.agcloud.agcom.agsupport.util.ExcelUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: libc
 * @Description: BIM审查项目模型-业务实现类
 * @Date: 2020/11/2 18:13
 * @Version: 1.0
 */
@Service
@Transactional
public class BimCheckProjectModelServiceImpl implements IBimCheckProjectModelService {

    private static final Logger logger = LoggerFactory.getLogger(BimCheckProjectModelServiceImpl.class);

    private static final String BIM_CHECK_MODEL_FILE_ADB_SUFFIX = "adb";
    private static final String BIM_CHECK_MODEL_FILE_ZIP_SUFFIX = "zip";
    private static final String BIM_CHECK_MODEL_FILE_EXCEL_SUFFIX = "xls";
    private static final String BIM_CHECK_MODEL_FILE_PREFIX = "agcim3dentity_";
    private static final String BIM_CHECK_MODEL_OUT_PATH = "bimCheckModel";

    @Autowired
    private AgBimCheckProjectModelMapper bimCheckProjectModelMapper;

    @Autowired
    private AgBimCheckProjectModelCustomMapper bimCheckProjectModelCustomMapper;

    @Autowired
    private IBimCheckService bimCheckService;

    @Autowired
    private IAgServerContentService serverContentService;

    @Autowired
    private IAgcim3dprojectService agcim3dprojectService;

    @Autowired
    private IAgcim3dbuildingService agcim3dbuildingService;

    @Value("${upload.filePath}")
    private String baseOutPath;


    /**
     * @param bimCheckProjectId  BIM审查项目的id
     * @param file               一个3dtiels模型的zip文件压缩包
     * @param agcim3dprojectName BIM审查项目名称('BIM审查')
     * @return
     * @Author: libc
     * @Date: 2020/11/2 18:25
     * @tips: 根据BIM审查项目的id保存3dtiles模型文件
     */
    public void add(String bimCheckProjectId, MultipartFile file, String agcim3dprojectName) {
        logger.info("============BIM审查项目模型保存开始==============");

        if (file == null) {
            logger.error(ExceptionEnum.INVALID_FILE_TYPE.getMessage());
            throw new AgCloudException(ExceptionEnum.INVALID_FILE_TYPE);
        }

        // 判断file类型是否是zip，有后缀
        String filename = file.getOriginalFilename();
        // 上传 .adb 压缩包 （本质也是zip包，只是修改了压缩包的后缀）
        if (filename == null || !(filename.endsWith(BIM_CHECK_MODEL_FILE_ZIP_SUFFIX) || filename.endsWith(BIM_CHECK_MODEL_FILE_ADB_SUFFIX))) {
            logger.error("请上传正确的adb/zip格式文件！");
            throw new AgCloudException(400, "请上传正确的adb/zip格式文件！");
        }

        // 临时zip文件路径
        String zipPath = new StringBuilder(baseOutPath).append(BIM_CHECK_MODEL_OUT_PATH).append(File.separator).append(filename).toString();
        File zipFile = new File(zipPath);
        // zip文件解压路径，解压之后也有可能乱码，要删除
        String zipOutPath = new StringBuilder(baseOutPath).append(BIM_CHECK_MODEL_OUT_PATH).append(File.separator).append(UUIDUtil.getUUID()).toString();
        File zipOutFile = new File(zipOutPath);

        try {
            // 是否去除zip层目录路径
            Boolean isWithoutZipDir = true;
            // 是否加上唯一标识的目录路径
            Boolean isAddUniqueness = true;
            // 解压源文件zip包

            String path = baseOutPath + BIM_CHECK_MODEL_OUT_PATH;
            Map<String, Object> resultMap = BimZipUtils.get3dtilesFileFromZip(file, zipOutPath, isWithoutZipDir, isAddUniqueness);
            List<FileEntity> models = (List<FileEntity>) resultMap.get("models");
            List<FileEntity> modelFiles = (List<FileEntity>) resultMap.get("modelFiles");
            if (CollectionUtils.isEmpty(models) || models.size() > 1) {
                // 一次只支持上传一个模型
                logger.error(ExceptionEnum.DATA_TRANSFER_ERROR.getMessage());
                throw new AgCloudException(ExceptionEnum.DATA_TRANSFER_ERROR);
            }

            if (CollectionUtils.isEmpty(modelFiles)) {
                logger.error(ExceptionEnum.DATA_TRANSFER_ERROR.getMessage());
                throw new AgCloudException(ExceptionEnum.DATA_TRANSFER_ERROR);
            }

            // 使用modelFiles集合重新写出文件
            modelFiles.forEach(fileEntity -> {
                String parentPath = new StringBuffer(path).append("/").append(fileEntity.getName().substring(0,fileEntity.getName().lastIndexOf("/"))).toString();
                File parentFile = new File(parentPath );
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                //创建文件
                File file1 = new File(parentFile , fileEntity.getName().substring(fileEntity.getName().lastIndexOf("/") + 1));
                if (!file1.exists()) {
                    try {
                        file1.createNewFile();
                    } catch (IOException e) {
                        logger.error("BIM审查模型文件创建异常！", e);
                        throw new AgCloudException(500, "BIM审查模型文件创建异常！");
                    }
                }
                try {
                    FileUtils.copyFile(fileEntity.getFile(), file1);
                } catch (IOException e) {
                    logger.error("BIM审查模型文件复制异常！");
                    throw new AgCloudException(500, "BIM审查模型文件复制异常！");
                }
            });

            // 构建AgBimCheckProjectModel对象以及封装数据
            AgBimCheckProjectModel bcpt = createModelObj(bimCheckProjectId, isWithoutZipDir, isAddUniqueness, models, modelFiles, agcim3dprojectName);
            // 保存模型
            bimCheckProjectModelMapper.insertSelective(bcpt);

            // -------------------预留 BIM审查纹理处理业务逻辑开始
            /*
             * sourceId BIM审查项目模型的id
             * modelFiles zip提取的文件集合
             */
            bimCheckService.addAgBimCheckFromJsonFile(bcpt.getId(), modelFiles);
            // -------------------预留 BIM审查纹理处理业务逻辑结束

            // 保存模型信息到服务内容表中
            addModelForServerContent(file, bcpt);
            logger.info("============BIM审查项目模型保存成功==============");
        } catch (SourceException e) {
            throw new AgCloudException(500, e.getMessage());
        } catch (AgCloudException e) {
            throw new AgCloudException(e.getStatus(), e.getMessage());
        } catch (Exception e) {
            logger.error("BIM审查项目模型保存失败！", e);
            throw new AgCloudException(ExceptionEnum.INSERT_OPERATION_FAIL);
        } finally {
            if (zipFile.exists()) {
                zipFile.delete();
            }
            if (zipOutFile.exists()) {
                try {
                    FileUtils.deleteDirectory(zipOutFile);
                } catch (Exception e) {
                    logger.error("BIM审查项目模型--文件夹删除失败！", e);
                    throw new AgCloudException(ExceptionEnum.FILE_DELETE_ERROR);
                }
            }
        }
    }

    /**
     * @param bimCheckProjectId  BIM审查项目id
     * @param isWithoutZipDir    是否去除zip层目录
     * @param isAddUniqueness    是否添加唯一标识层目录
     * @param models             上传的zip文件内 tileset.json 主文件集合
     * @param modelFiles         上传的zip文件内所有文件的集合
     * @param agcim3dprojectName BIM审查项目名称('BIM审查')
     * @return AgBimCheckProjectModel
     * @Author: libc
     * @Date: 2020/11/24 17:24
     * @tips: 构建AgBimCheckProjectModel对象以及封装数据
     */
    private AgBimCheckProjectModel createModelObj(String bimCheckProjectId, Boolean isWithoutZipDir, Boolean isAddUniqueness, List<FileEntity> models, List<FileEntity> modelFiles, String agcim3dprojectName) throws FileNotFoundException {
        AgBimCheckProjectModel bcpt = new AgBimCheckProjectModel();
        bcpt.setId(UUID.randomUUID().toString());

        /*
         *  一次只支持上传一个模型 models.get(0)
         *  fileDirName已经包括了后缀tileset.json主文件 (123445552/baoli1/tileset.json 或 123445552/zip包名/baoli1/tileset.json)
         */
        String fileDirName = models.get(0).getName();
        String subName = fileDirName;
        // 获取到各个3dtiles模型文件名称 （即模型名称）
        if (isAddUniqueness) {
            subName = subName.substring(subName.indexOf("/") + 1);
        }
        if (!isWithoutZipDir) {
            subName = subName.substring(subName.indexOf("/") + 1);
        }
        subName = subName.substring(0, subName.lastIndexOf("/"));
        bcpt.setName(subName);
        bcpt.setAgBimCheckProjectId(bimCheckProjectId);
        // 替换"\"为"/"
        bcpt.setPath(
                new StringBuilder(BIM_CHECK_MODEL_OUT_PATH)
                        .append(File.separator)
                        .append(fileDirName).toString().replaceAll("\\\\","/")
        );
        bcpt.setCreateTime(new Timestamp(System.currentTimeMillis()));
        bcpt.setModifyTime(new Timestamp(System.currentTimeMillis()));

        // 保存模型内部信息 （excel表格导入）
        String tableName = createTableAndSaveModelData(modelFiles, agcim3dprojectName, bcpt);
        bcpt.setInfoRelTableName(tableName);
        return bcpt;
    }

    /**
     * @param modelFiles         上传的zip文件内所有文件的集合
     * @param agcim3dprojectName BIM审查项目名称('BIM审查')
     * @param bcpt               BIM审查项目模型对象
     * @return String 创建的表名称
     * @Author: libc
     * @Date: 2020/11/24 17:10
     * @tips: 保存模型内部信息 （excel表格导入） 以及入库关联表 （agcim3dbuilding）
     */
    private String createTableAndSaveModelData(List<FileEntity> modelFiles, String agcim3dprojectName, AgBimCheckProjectModel bcpt) throws FileNotFoundException {
        String tableName = null;
        for (FileEntity fileEntity : modelFiles) {
            if (fileEntity.getName().contains(BIM_CHECK_MODEL_FILE_PREFIX) && fileEntity.getName().endsWith(BIM_CHECK_MODEL_FILE_EXCEL_SUFFIX)) {
                // agcim3dentity_xxx.xls
                File modelInfoExcel = fileEntity.getFile();
                tableName = modelInfoExcel.getName().substring(0, modelInfoExcel.getName().indexOf("."));
                // 获取表格数据
                List<Object[]> excelData = ExcelUtil.getExcelData(fileEntity.getName(), new FileInputStream(modelInfoExcel));
                // 创建存储表结构 (excel表头)
                Object[] excelHeader = excelData.get(0);
                // 构建对应数据库 agcim3dentity_xxx
                createExcelDataBase(tableName, excelHeader);

                // BIM模型信息入库统一BIM实体管理表 agcim3dbuilding
                addAgcim3dbuilding(agcim3dprojectName, bcpt, tableName, excelData.size() - 1);

                // 导入excel到新建数据库中
                importDataByExcel(tableName, excelData);
            }
        }
        return tableName;
    }

    /**
     * @param agcim3dprojectName BIM项目名
     * @param bcpt               AgBimCheckProjectModel对象
     * @param tableName          BIM模型表名
     * @param entityCount        BIM模型入库构件信息总数
     * @Author: libc
     * @Date: 2020/11/25 15:18
     * @tips: BIM模型信息入库统一BIM实体管理表 agcim3dbuilding
     */
    private void addAgcim3dbuilding(String agcim3dprojectName, AgBimCheckProjectModel bcpt, String tableName, int entityCount) {
        Agcim3dproject projectParam = new Agcim3dproject();
        projectParam.setName(agcim3dprojectName);
        // 根据BIM项目名称获取BIM项目id
        List<Agcim3dproject> projects = agcim3dprojectService.list(projectParam);
        if (CollectionUtils.isEmpty(projects)) {
            logger.error("未找到匹配的项目名称，请联系管理员添加！");
            throw new AgCloudException(400, "未找到匹配的项目名称，请联系管理员添加！");
        }

        Agcim3dbuilding agcim3dbuilding = new Agcim3dbuilding();
        agcim3dbuilding.setProjectid(projects.get(0).getId());
        agcim3dbuilding.setEntitytable(tableName);
        agcim3dbuilding.setEntitycount(String.valueOf(entityCount));
        agcim3dbuilding.setBuildingname(bcpt.getName());
        agcim3dbuildingService.add(agcim3dbuilding);
    }

    /**
     * @param file 上传的模型文件
     * @param bcpt BIM审查项目模型对象
     * @Author: libc
     * @Date: 2020/11/24 16:46
     * @tips: 保存模型信息到服务内容表中
     */
    private void addModelForServerContent(MultipartFile file, AgBimCheckProjectModel bcpt) {
        AgServerContent serverContent = new AgServerContent();
        serverContent.setId(UUID.randomUUID().toString());
        serverContent.setType("3dtiles");
        // 默认分组： BIM审查系统
        serverContent.setGroupId("2");
        // 来源类型 2：代表BIM审查
        serverContent.setSourceType("2");
        serverContent.setTags("BIM审查");
        serverContent.setSize(String.valueOf(file.getSize()));
        serverContent.setTitle(bcpt.getName());
        serverContent.setName(bcpt.getName());
        serverContent.setSourceRelId(bcpt.getId());
        serverContent.setPath(bcpt.getPath());
        serverContent.setCreateTime(new Timestamp(System.currentTimeMillis()));
        serverContent.setModifyTime(new Timestamp(System.currentTimeMillis()));
        serverContentService.addWithoutFile(serverContent);
    }

    /**
     * @param tableName 表名
     * @param excelData excel数据
     * @Author: libc
     * @Date: 2020/11/20 16:48
     * @tips: 将excel中数据批量导入模型信息表中
     */
    private void importDataByExcel(String tableName, List<Object[]> excelData) {
        // 封装列集合
        List<String> columnList = new ArrayList<String>();
        Object[] headrData = excelData.get(0);
        for (int i = 0; i < headrData.length; i++) {
            columnList.add((String) headrData[i]);
        }

        // 封装数据map
        List<Map<String, Object>> dataMapList = new ArrayList<>();
        // 封装动态列字段集合
        List<List<String>> allColumns = new ArrayList<>();
        for (int i = 1; i < excelData.size(); i++) {
            Map<String, Object> dataMap = new ConcurrentHashMap<>();
            // 对应每列要插入数据的字段
            List<String> rowColumn = new ArrayList<>();
            // 跳过表头,从第二行开始
            Object[] rowData = excelData.get(i);
            for (int j = 0; j < rowData.length; j++) {
                if (rowData[j] == null || StringUtils.isEmpty(rowData[j])) {
                    // 空值列直接跳过
                    continue;
                }

                rowColumn.add(columnList.get(j));

                if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_VERSION.getFieldName().equals(columnList.get(j))) {
                    dataMap.put(columnList.get(j), Long.valueOf((String) rowData[j]));
                    continue;
                }

                dataMap.put(columnList.get(j), (String) rowData[j]);
            }
            allColumns.add(rowColumn);
            dataMapList.add(dataMap);
        }

        // 插入数据 开始角标
        int startInsertIndex = 0;
        // 插入数据 结束角标
        int endInsertIndex = 100;
        // 逻辑分页
        int pageNum = 1;
        // 逻辑页大小
        int pageSize = 1000;
        // 数据集合最后元素角标
        int dataMapListLastIndex = dataMapList.size() - 1;
        for (int i = 0; i < dataMapList.size(); i++) {
            if (Integer.valueOf(i % pageSize).equals(Integer.valueOf(0))) {
                startInsertIndex = (pageNum - 1) * pageSize;
                // 取角标，所以-1
                endInsertIndex = startInsertIndex + pageSize - 1;
                // 如果是最后一个元素，则取到集合最后一个角标即可
                endInsertIndex = endInsertIndex > dataMapListLastIndex ? dataMapListLastIndex : endInsertIndex;
                // 每一千次或者到最后一条记录 提交一次保存
                // 批量保存
                bimCheckProjectModelCustomMapper.batchInsertModelInfoByExcel(tableName, allColumns.subList(startInsertIndex, endInsertIndex), dataMapList.subList(startInsertIndex, endInsertIndex));
                pageNum++;
            }
        }
    }

    /**
     * @param tableName   表名
     * @param excelHeader excel表头数组
     * @Author: libc
     * @Date: 2020/11/20 11:06
     * @tips: 根据excel表头构建对应数据库 （表头=》属性字段）
     * 只支持3dtiles 模型数据
     */
    private void createExcelDataBase(String tableName, Object[] excelHeader) {
        // 判断表结构是否已经存在
        AgBimCheckProjectModelExample example = new AgBimCheckProjectModelExample();
        example.createCriteria().andInfoRelTableNameEqualTo(tableName);
        List<AgBimCheckProjectModel> projectModels = bimCheckProjectModelMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(projectModels)) {
            // 如果存在，抛异常
            logger.error("表名不能重复，请联系管理员！");
            throw new AgCloudException(400, "表名不能重复，请联系管理员！");
        }

        StringBuilder createTableSql = new StringBuilder();
        createTableSql.append("CREATE TABLE ").append("public.").append(tableName).append(" (");
        // 遍历数组,拼接字段
        for (int i = 0; i < excelHeader.length; i++) {
            Object obj = excelHeader[i];
            if (i > 0) {
                // 从第二个开始加","间隔
                createTableSql.append(",");
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_ID.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_ID.getFieldType());
                continue;
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_OBJECTID.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_OBJECTID.getFieldType());
                continue;
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_NAME.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_NAME.getFieldType());
                continue;
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_VERSION.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_VERSION.getFieldType());
                continue;
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_INFOTYPE.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_INFOTYPE.getFieldType());
                continue;
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_PROFESSION.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_PROFESSION.getFieldType());
                continue;
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_LEVEL.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_LEVEL.getFieldType());
                continue;
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_CATAGORY.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_CATAGORY.getFieldType());
                continue;
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_FAMILYNAME.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_FAMILYNAME.getFieldType());
                continue;
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_FAMILYTYPE.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_FAMILYTYPE.getFieldType());
                continue;
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_MATERIALID.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_MATERIALID.getFieldType());
                continue;
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_ELEMENTATTRIBUTES.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_ELEMENTATTRIBUTES.getFieldType());
                continue;
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_HOST.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_HOST.getFieldType());
                continue;
            }
            /*if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_CATEGORYPATH.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_CATEGORYPATH.getFieldType());
                continue;
            }*/
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_GEOMETRY.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_GEOMETRY.getFieldType());
                continue;
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_TOPOLOGYELEMENTS.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_TOPOLOGYELEMENTS.getFieldType());
                continue;
            }
            if (ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_BOUNDINGBOX.getFieldName().equals((String) obj)) {
                createTableSql.append((String) obj).append(ModelTableFieldEnum.BIM_CHECK_MODEL_INFO_TABLE_FIELD_BOUNDINGBOX.getFieldType());
                continue;
            }
        }
        createTableSql.append(",");
        // 添加主键
        createTableSql.append("CONSTRAINT ").append(tableName).append("_pkey").append(" PRIMARY KEY (id) ").append(");");

        bimCheckProjectModelCustomMapper.createExcelTable(createTableSql.toString());

    }

    /**
     * @param id 删除的id
     * @return
     * @Author: libc
     * @Date: 2020/11/2 18:25
     * @tips: 删除
     */
    public void delete(String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
            }

           /*
            * ========================删除本地文件操作在serverContent中统一处理=======================
            // 删除文件夹
            String dirPath = new StringBuilder(baseOutPath)
                    .append(bcpm.getPath())
                    .toString();
            dirPath = dirPath.substring(0, dirPath.lastIndexOf("/")); //按照文件夹删除，所以路径上去掉tileset.json这层
            FileUtils.deleteDirectory(new File(dirPath.toString()));*/

            // 根据id查询记录，获取title，即文件名
            AgBimCheckProjectModel bcpm = bimCheckProjectModelMapper.selectByPrimaryKey(id);
            if (bcpm == null) {
                logger.info("未找到相关数据");
                throw new AgCloudException(500, "未找到相关数据");
            }

            // 删除模型关联的其他表数据
            deleteRelData(bcpm);

            // 删除数据库记录
            bimCheckProjectModelMapper.deleteByPrimaryKey(id);
        } catch (AgCloudException e) {
            throw new AgCloudException(e.getStatus(), e.getMessage());
        } catch (Exception e) {
            logger.error(ExceptionEnum.DELETE_OPERATION_FAIL.getMessage());
            throw new AgCloudException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }

    /**
     * @param bcpm BIM审查项目模型对象
     * @Author: libc
     * @Date: 2020/11/27 14:41
     * @tips: 删除模型关联的其他表数据
     */
    private void deleteRelData(AgBimCheckProjectModel bcpm) {

        if (!StringUtils.isEmpty(bcpm.getInfoRelTableName())) {
            // 删除动态excel模型信息数据库
            bimCheckProjectModelCustomMapper.deleteModelInfoTable(bcpm.getInfoRelTableName());
            // 删除agcim3dbuilding中关联数据
            agcim3dbuildingService.deleteByEntitytable(bcpm.getInfoRelTableName());
        }

        // 删除模型对应审查条文
        bimCheckService.deleteAgBimCheckFromSourceId(bcpm.getId());

        // 删除服务内容表中关联数据
        serverContentService.deleteListBySourceRelId(bcpm.getId());
    }

    /**
     * @param bimCheckProjectId BIM审查项目的id
     * @return
     * @Author: libc
     * @Date: 2020/11/3 16:56
     * @tips: 根据bimCheckProjectId 删除项目下的模型集合
     */
    public void deleteListByBimCheckProjectId(String bimCheckProjectId) {
        try {
            if (StringUtils.isEmpty(bimCheckProjectId)) {
                throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
            }
            // 查询项目对应的模型集合
            AgBimCheckProjectModelExample example = new AgBimCheckProjectModelExample();
            AgBimCheckProjectModelExample.Criteria criteria = example.createCriteria();
            criteria.andAgBimCheckProjectIdEqualTo(bimCheckProjectId);
            List<AgBimCheckProjectModel> projectModels = bimCheckProjectModelMapper.selectByExample(example);

            if (CollectionUtils.isEmpty(projectModels)) {
                // 无相关记录，不用进行下面删除操作
                return;
            }

            // 遍历删除关联表数据 包括本地模型文件
            projectModels.forEach(model -> {
                // 删除关联表数据
                deleteRelData(model);
            });

            // 删除数据库记录
            bimCheckProjectModelMapper.deleteByExample(example);
        } catch (Exception e) {
            logger.error(ExceptionEnum.DELETE_OPERATION_FAIL.getMessage());
            throw new AgCloudException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }

    /**
     * @param bimCheckProjectId BIM审查项目的id
     * @return 对应项目下的模型集合
     * @Author: libc
     * @Date: 2020/11/3 17:13
     * @tips: 根据bimCheckProjectId 查询项目下的模型集合
     */
    public List<AgBimCheckProjectModel> findListByBimCheckProjectId(String bimCheckProjectId) {
        AgBimCheckProjectModelExample example = new AgBimCheckProjectModelExample();
        example.createCriteria().andAgBimCheckProjectIdEqualTo(bimCheckProjectId);
        return bimCheckProjectModelMapper.selectByExample(example);
    }

}
