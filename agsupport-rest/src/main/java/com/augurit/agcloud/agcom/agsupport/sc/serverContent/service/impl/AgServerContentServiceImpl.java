package com.augurit.agcloud.agcom.agsupport.sc.serverContent.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.common.exception.ExceptionEnum;
import com.augurit.agcloud.agcom.agsupport.common.util.UUIDUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.io.DataConversionUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContent;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContentExample;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgServerContentMapper;
import com.augurit.agcloud.agcom.agsupport.sc.serverContent.service.IAgServerContentService;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.agcom.agsupport.util.ZipFileUtils;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcom.common.LoginHelpClient;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 服务内容管理 服务实现类
 * </p>
 *
 * @author libc
 * @since 2020-09-15
 */
@Service
@Transactional
public class AgServerContentServiceImpl implements IAgServerContentService {

    private Logger logger = LoggerFactory.getLogger(AgServerContentServiceImpl.class);

    private static final String BIM_MODEL_FILE_SUFFIX = "tileset.json";
    private static final String SERVER_CONTENT_MODEL_FILE_ADB_SUFFIX = "adb";
    private static final String SERVER_CONTENT_MODEL_FILE_ZIP_SUFFIX = "zip";
    private static final String BIM_CHECK_MODEL_OUT_PATH = "bimCheckModel";
    private static final String SERVER_CONTENT_MODEL_FIRST_OUT_PATH = "serverManagerFiles";
    private static final String SERVER_CONTENT_MODEL_SECOND_OUT_PATH = "content";

    @Autowired
    private AgServerContentMapper serverContentMapper;
    @Autowired
    private IAgUser iAgUser;

    @Value("${upload.filePath}")
    private String baseOutPath;

    private String zipOutPath;

   /* @PostConstruct
    public void init(){
        // 构造之后初始化zipOutPath的值， 否则baseOutPath为null
        zipOutPath = baseOutPath + "serverManagerFiles/content";
        logger.info("服务管理内容文件存储目录路径：{}",zipOutPath);
    }*/

    /**
     * @param param 查询条件
     * @param page  分页参数
     * @return
     * @Author: libc
     * @Date: 2020-09-15
     * @tips: 分页列表
     */
    @Override
    public PageInfo<AgServerContent> find(AgServerContent param, Page page) {
        AgServerContentExample example = new AgServerContentExample();
        // 排序
        example.setOrderByClause("modify_time desc");
        example.setOrderByClause("create_time desc");
        AgServerContentExample.Criteria criteria = example.createCriteria();
        //参数封装，如果不为null，需要添加条件
        if (!StringUtils.isEmpty(param.getName())) {
            criteria.andNameLike("%" + param.getName() + "%");
        }
        if (!StringUtils.isEmpty(param.getGroupId())) {
            criteria.andGroupIdEqualTo(param.getGroupId());
        }
        PageHelper.startPage(page);
        List<AgServerContent> agServerContents = serverContentMapper.selectByExample(example);

        return new PageInfo<AgServerContent>(agServerContents);
    }

    /**
     * @param id id
     * @return
     * @Author: libc
     * @Date: 2020-09-15
     * @tips: 详情
     */
    @Override
    public AgServerContent get(String id) {
        return serverContentMapper.selectByPrimaryKey(id);
    }

    /**
     * @param param       保存对象
     * @param file        上传zip文件
     * @param paramType   文件来源类型。1：agcim服务管理； 2：BIM审查
     * @param sourceRelId 文件来源关联的业务ID （BIM审查关联BIM审查项目id）
     * @return
     * @Author: libc
     * @Date: 2020-09-15
     * @tips: 新增
     */
    @Override
    public void add(AgServerContent param, MultipartFile file, HttpServletRequest request, String paramType, String sourceRelId) {
        if (file == null) {
            throw new AgCloudException(ExceptionEnum.INVALID_FILE_TYPE);
        }
        // 判断file类型是否是zip
        String filename = file.getOriginalFilename();
        if (filename == null || !(filename.endsWith(SERVER_CONTENT_MODEL_FILE_ZIP_SUFFIX) || filename.endsWith(SERVER_CONTENT_MODEL_FILE_ADB_SUFFIX))) {
            logger.error("请上传正确的adb/zip格式文件！");
            throw new AgCloudException(400, "请上传正确的adb/zip格式文件！");
        }
        String realFileName = filename.substring(0, filename.lastIndexOf("."));
        try {
            // 文件解压相对路径，（数据库存储）
            String relPath = "";
            //  根据文件来源封装不同文件解压路径
            if ("1".equals(paramType)) {
                // 1：agcim服务管理
                relPath = new StringBuilder().append(SERVER_CONTENT_MODEL_FIRST_OUT_PATH).append(File.separator).append(SERVER_CONTENT_MODEL_SECOND_OUT_PATH).toString();
            } else if ("2".equals(paramType)) {
                // 2：BIM审查
                relPath = new StringBuilder().append(BIM_CHECK_MODEL_OUT_PATH).toString();
            } else {
                throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
            }
            zipOutPath = baseOutPath + relPath;
            logger.info("服务管理内容文件存储目录路径：{}", zipOutPath);

            // 1. 解压zip文件,并在本地路径创建zip文件解压后的目录文件
            Boolean isSave = ZipFileUtils.unZipAndSaveFromZip(file.getInputStream(), zipOutPath);
            if (!isSave) {
                throw new AgCloudException(500, "zip文件保存失败！");
            }
            // 2. 保存解压之后的文件目录路径及内容对象到数据库
            param.setId(UUID.randomUUID().toString());
            param.setCreateTime(new Timestamp(new Date().getTime()));
            param.setModifyTime(new Timestamp(new Date().getTime()));
            param.setSize(String.valueOf(file.getSize()));
            param.setSourceType(paramType);

            // 存主文件相对路径
            param.setPath(new StringBuilder(relPath).append(File.separator).append(realFileName).append(File.separator).append(BIM_MODEL_FILE_SUFFIX).toString());
            logger.info("服务管理内容文件存储路径：{}", new StringBuilder(relPath).append(File.separator).append(realFileName).append(File.separator).append(BIM_MODEL_FILE_SUFFIX).toString());

            // 获取登陆用户
            String loginName = LoginHelpClient.getLoginName(request);
            AgUser agUser = iAgUser.findUserByName(loginName);
            // 用户id
            param.setUserId(agUser.getId());

            // 设置除服务管理外其他来源 信息
            if (!"1".equals(paramType)) {
                // 不提交表单，名称需要从file中获取
                param.setName(realFileName);
                param.setTitle(realFileName);
                param.setType("3dtiles");
                param.setSourceRelId(sourceRelId);
                // 先分组给默认组 1：augurit
                param.setGroupId("1");
            }

            // 保存内容
            serverContentMapper.insertSelective(param);
        } catch (Exception e) {
            logger.error(ExceptionEnum.INSERT_OPERATION_FAIL.getMessage(), e);
            e.printStackTrace();
            throw new AgCloudException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
     * @param param 修改对象
     * @return
     * @Author: libc
     * @Date: 2020-09-15
     * @tips: 修改
     */
    @Override
    public void update(AgServerContent param) {
        try {
            if (StringUtils.isEmpty(param.getId())) {
                throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
            }
            param.setModifyTime(new Timestamp(new Date().getTime()));
            serverContentMapper.updateByPrimaryKeySelective(param);
        } catch (Exception e) {
            throw new AgCloudException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }

    /**
     * @param id 删除的id
     * @return
     * @Author: libc
     * @Date: 2020-09-15
     * @tips: 删除(单个条目)
     */
    @Override
    public void delete(String id) {

        if (StringUtils.isEmpty(id)) {
            throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        // 删除本地文件
        // 1.根据id查询记录，获取title，即文件名
        AgServerContent sc = serverContentMapper.selectByPrimaryKey(id);
        if (sc == null) {
            throw new AgCloudException(500, "未找到相关数据");
        }

        if ("2".equals(sc.getSourceType())) {
            throw new AgCloudException(401, "暂不支持在此系统中删除BIM审查模型！");
        }
        try {
            // 2.删除文件夹
            String dirPath = new StringBuilder(baseOutPath)
                    .append(sc.getPath())
                    .toString();

            dirPath = dirPath.substring(0, dirPath.lastIndexOf("/")); //按照文件夹删除，所以路径上去掉tileset.json这层
            FileUtils.deleteDirectory(new File(dirPath.toString()));
            // 删除数据库记录
            serverContentMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            throw new AgCloudException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }

    /**
     * @param id 记录id
     * @Author: libc
     * @Date: 2020/10/19 10:36
     * @tips: 下载3dtiles zip包
     */
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        // 查询获取要压缩的3dtiles目录路径
        AgServerContent agServerContent = get(id);
        String path = agServerContent.getPath();

        File file = new File(new StringBuilder(baseOutPath).append(path.substring(0, path.lastIndexOf(BIM_MODEL_FILE_SUFFIX) - 1)).toString() );
        if (!file.exists()) {
            logger.error("未找到对应文件夹！");
            throw new AgCloudException(500, "未找到对应文件夹！");
        }
        try {
            ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
            // 压缩及下载
            DataConversionUtil.setHeader(request, response, UUIDUtil.getUUID() + ".zip");
            // 设置一次输出大小，否则会产生多次请求导致下载失败
            response.setHeader("Content-Range", "bytes 0-0,-1");
            ZipFileUtils.compressZip(out, file, file.getName());

        } catch (Exception e) {
            logger.error(ExceptionEnum.FILE_WRITER_ERROR.getMessage(), e);
            throw new AgCloudException(ExceptionEnum.FILE_WRITER_ERROR);
        }
    }


    /**
     * @param sourceRelId 文件来源业务关联id
     * @return
     * @Author: libc
     * @Date: 2020/11/3 16:56
     * @tips: 根据sourceRelId 删除相关联的模型集合
     */
    @Override
    public void deleteListBySourceRelId(String sourceRelId) {

        if (StringUtils.isEmpty(sourceRelId)) {
            throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        // 查询项目对应的模型集合
        AgServerContentExample example = new AgServerContentExample();
        AgServerContentExample.Criteria criteria = example.createCriteria();
        criteria.andSourceRelIdEqualTo(sourceRelId);
        List<AgServerContent> serverContents = serverContentMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(serverContents)) {
            // 无相关记录，不用进行下面删除操作
            return;
        }

        // 遍历删除磁盘文件夹
        serverContents.forEach(content -> {
            String dirPath = new StringBuilder(baseOutPath)
                    .append(content.getPath())
                    .toString();
            dirPath = dirPath.substring(0, dirPath.lastIndexOf("/")); //按照文件夹删除，所以路径上去掉tileset.json这层
            if ("2".equals(content.getSourceType())) {
                // bimCheckModel\3567f7bec3877d03/gzbuilding/tileset.json(path)
                // 如果是BIM审查类型的模型文件， 需要从uuid层删除
                dirPath = dirPath.substring(0, dirPath.lastIndexOf("/")); //按照UUID层文件夹删除，所以路径上去掉gzbuilding这层
            }
            try {
                FileUtils.deleteDirectory(new File(dirPath));
            } catch (IOException e) {
                logger.error("服务相关联3dtiles模型文件删除失败！", e);
                throw new AgCloudException(ExceptionEnum.FILE_DELETE_ERROR);
            }
        });

        try {

            // 删除数据库记录
            serverContentMapper.deleteByExample(example);
        } catch (Exception e) {
            logger.error(ExceptionEnum.DELETE_OPERATION_FAIL.getMessage(), e);
            throw new AgCloudException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
    }

    /**
     * @param sourceRelId 文件来源业务关联id
     * @return
     * @Author: libc
     * @Date: 2020/11/3 17:13
     * @tips: 根据sourceRelId 查询模型集合
     */
    @Override
    public List<AgServerContent> findListBySourceRelId(String sourceRelId) {
        AgServerContentExample example = new AgServerContentExample();
        example.createCriteria().andSourceRelIdEqualTo(sourceRelId);
        return serverContentMapper.selectByExample(example);
    }

    /**
     * @param serverContent
     * @Author: libc
     * @Date: 2020/11/19 16:53
     * @tips: 新增服务内容（不包含模型文件）
     */
    @Override
    public void addWithoutFile(AgServerContent serverContent) {
        try {
            serverContentMapper.insertSelective(serverContent);
        } catch (Exception e) {
            logger.error(ExceptionEnum.INSERT_OPERATION_FAIL.getMessage(), e);
            throw new AgCloudException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

}
