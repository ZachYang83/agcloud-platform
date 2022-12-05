package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.impl;


import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserDesignMaterials;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserDesignMaterialsExample;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserDesignScheme;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserDesignSchemeExample;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgUserDesignMaterialsMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgUserDesignSchemeMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgHouseService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgUserDesignSchemeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@Service
public class AgUserDesignSchemeServiceImpl implements IAgUserDesignSchemeService {

    private static final Logger logger = LoggerFactory.getLogger(AgUserDesignSchemeServiceImpl.class);
    //用户设计的缩略图存储位置
    private static final String thumbFilePath = "userDesign";
    @Autowired
    private AgUserDesignSchemeMapper schemeMapper;
    @Autowired
    private IAgHouseService sysAgHouseService;
    @Autowired
    private AgUserDesignMaterialsMapper materialsMapper;

    @Override
    public PageInfo<AgUserDesignScheme> list(AgUserDesignScheme scheme, Page page) {
        AgUserDesignSchemeExample example = new AgUserDesignSchemeExample();
        example.setOrderByClause("create_time desc");
        AgUserDesignSchemeExample.Criteria criteria = example.createCriteria();

        //用户名称模糊搜索
        if(!StringUtils.isEmpty(scheme.getName())){
            criteria.andNameLike("%" + scheme.getName() + "%");
        }
        //是否默认
        if(!StringUtils.isEmpty(scheme.getIsDefault())){
            criteria.andIsDefaultEqualTo(scheme.getIsDefault());
        }
        //过滤当前登录用户
        String userId = SecurityContext.getCurrentUser().getUserId();
        criteria.andUserIdEqualTo(userId);
        PageHelper.startPage(page);
        List<AgUserDesignScheme> agUserDesignSchemes = schemeMapper.selectByExample(example);
        return new PageInfo<AgUserDesignScheme>(agUserDesignSchemes);
    }

    @Override
    @Transactional
    public void add(AgUserDesignScheme scheme, MultipartFile thumbFile, List<AgUserDesignMaterials> materials) {
        String basePath = null;
        try{
            String returnPath = null;
            if(thumbFile != null && !thumbFile.isEmpty()){
                String filename = thumbFile.getOriginalFilename();
                //文件重命名
                filename = UUID.randomUUID().toString() + filename.substring(filename.lastIndexOf("."));
                basePath = sysAgHouseService.getBaseFilePath();
                //使用当前设计名称作为存储缩略图的路径
                String parentCategoryPath = thumbFilePath;
                String path = parentCategoryPath + File.separator + filename;
                returnPath = parentCategoryPath + "/" + filename;
                File upload = new File(basePath + File.separator + path);
                if (!upload.exists()) {
                    upload.mkdirs();
                }
                thumbFile.transferTo(upload);
            }
            String id = scheme.getId();
            //如果前端生成了id，就不用后端自己生成
            if(StringUtils.isEmpty(id)){
                id = UUID.randomUUID().toString();
                scheme.setId(id);
            }
            Date createTime = new Date();
            scheme.setId(id);
            scheme.setCreateTime(createTime);
            scheme.setThumb(returnPath);
            //0非默认，1默认
            scheme.setIsDefault("0");
            schemeMapper.insert(scheme);

            //插入属性
            if(materials != null && materials.size() > 0){
                for(AgUserDesignMaterials material: materials){
                    //添加
                    if(StringUtils.isEmpty(material.getId())){
                        material.setId(UUID.randomUUID().toString());
                    }
                    material.setCreateTime(createTime);
                    material.setDesignSchemeId(id);
                    material.setUserId(scheme.getUserId());
                    materialsMapper.insert(material);
                }
            }
        }catch (Exception e){
            logger.info(e.getMessage());
            throw new SourceException("添加失败");
        }
    }

    @Override
    @Transactional
    public void update(AgUserDesignScheme scheme, MultipartFile thumbFile, List<AgUserDesignMaterials> materials) {
        String basePath = null;
        try{
            AgUserDesignScheme agUserDesignScheme = schemeMapper.selectByPrimaryKey(scheme.getId());
            if(agUserDesignScheme == null){
                throw new SourceException("数据不存在，不能修改");
            }
            if(thumbFile != null && !thumbFile.isEmpty()){
                //使用原名当做图片名
                String filename = agUserDesignScheme.getThumb().substring(agUserDesignScheme.getThumb().lastIndexOf("/"));;
                basePath = sysAgHouseService.getBaseFilePath();
                //使用当前设计名称作为存储缩略图的路径
                String parentCategoryPath = thumbFilePath;
                String path = parentCategoryPath + File.separator + filename;
                File upload = new File(basePath + File.separator + path);
                if (!upload.exists()) {
                    upload.mkdirs();
                }
                thumbFile.transferTo(upload);
            }
            Date modifyTIme = new Date();
            scheme.setModifyTime(modifyTIme);
            schemeMapper.updateByPrimaryKeySelective(scheme);

            //插入属性
            //插入之前首先清空所有的属性
            AgUserDesignMaterialsExample example = new AgUserDesignMaterialsExample();
            example.createCriteria().andDesignSchemeIdEqualTo(scheme.getId());
            materialsMapper.deleteByExample(example);
            if(materials != null && materials.size() > 0){
                for(AgUserDesignMaterials material: materials){
                    //添加
                    if(StringUtils.isEmpty(material.getId())){
                        material.setId(UUID.randomUUID().toString());
                    }
                    material.setCreateTime(modifyTIme);
                    material.setDesignSchemeId(scheme.getId());
                    material.setUserId(scheme.getUserId());
                    materialsMapper.insert(material);
                }
            }
        }catch (Exception e){
            logger.info(e.getMessage());
            throw new SourceException("修改失败");
        }
    }

    @Override
    public AgUserDesignScheme get(String id) {
        return schemeMapper.selectByPrimaryKey(id);
    }

    @Override
    public void view(String path, HttpServletResponse response){
        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            String baseFile = sysAgHouseService.getBaseFilePath();
            inputStream = new FileInputStream(baseFile + File.separator + path);
            //将inputStream装成byte[]
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            if (bytes != null) {
                response.addHeader("Content-Length", bytes.length + "");
                String s = null;
                try{
                    s = new String(path.substring(path.lastIndexOf("/"), path.length()).getBytes());
                }catch (Exception e){
                    try{
                        s = new String(path.substring(path.lastIndexOf("\\"), path.length()).getBytes());
                    }catch (Exception e1){

                    }
                }
                response.addHeader("Content-Disposition", "filename=" + s);
                //下面展示的是下载的弹框提示，如果要打开，就不需要设置
//        response.setContentType("application/octet-stream");
                outputStream = response.getOutputStream();
                outputStream.write(bytes);
            }
        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        } catch (IOException e) {
            logger.info(e.getMessage());
        }catch (Exception e) {
            logger.info(e.getMessage());
        }finally {
            try{
                if(inputStream != null){
                    inputStream.close();
                }
            }catch (IOException e){
                logger.info(e.getMessage());
            }
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
        }
    }


    @Override
    @Transactional
    public void setDefault(String id){
        //只能存在一个默认
        AgUserDesignScheme agUserDesignScheme = schemeMapper.selectByPrimaryKey(id);
        if(agUserDesignScheme == null){
            throw new SourceException("数据不存在，设置失败");
        }
        //把以前设置默认的数据删除
        AgUserDesignSchemeExample example = new AgUserDesignSchemeExample();
        example.createCriteria().andUserIdEqualTo(agUserDesignScheme.getUserId()).andIsDefaultEqualTo("1");
        int i = schemeMapper.countByExample(example);
        if(i != 0){
            AgUserDesignScheme scheme = new AgUserDesignScheme();
            scheme.setIsDefault("0");
            schemeMapper.updateByExampleSelective(scheme, example);
        }

        AgUserDesignScheme scheme = new AgUserDesignScheme();
        scheme.setIsDefault("1");
        scheme.setId(id);
        scheme.setModifyTime(new Date());
        schemeMapper.updateByPrimaryKeySelective(scheme);
    }

    private void deleteThumb(String sourcePath) {
        if (!StringUtils.isEmpty(sourcePath)) {
            //从磁盘删除存储文件
            try {
                String baseFile = sysAgHouseService.getBaseFilePath();
                String delFile = baseFile + File.separator + sourcePath;
                //win和linux路径转换
                delFile = "/".equals(File.separator) ? delFile.replaceAll("/", File.separator) : delFile.replaceAll("/", File.separator + File.separator);
                logger.info("-----delete--------" + delFile);
                File file = new File(delFile);
                if (file.exists()) {
                    deleteFile(file);
                    file.delete();
                }
            } catch (FileNotFoundException e) {
                logger.info("删除文件失败FileNotFoundException-----------delFile=" + e.getMessage());
            }
        }
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
    @Transactional
    public void delete(String id){
        AgUserDesignScheme agUserDesignScheme = schemeMapper.selectByPrimaryKey(id);
        if(agUserDesignScheme == null){
            throw new SourceException("数据不存在，不能删除");
        }
        //需要删除缩略图
        if(!StringUtils.isEmpty(agUserDesignScheme.getThumb())){
            deleteThumb(agUserDesignScheme.getThumb());
        }

        //查询所有的关联信息，删除
        AgUserDesignMaterialsExample example = new AgUserDesignMaterialsExample();
        example.createCriteria().andDesignSchemeIdEqualTo(id);
        materialsMapper.deleteByExample(example);
        //删除设计
        schemeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<AgUserDesignMaterials> getModel(String schemeId, String materialsId) {

        //如果materialsId有数据，需要过滤过滤后，可以直接返回
        if(!StringUtils.isEmpty(materialsId)){
            List<AgUserDesignMaterials> result = null;
            AgUserDesignMaterials materials = materialsMapper.selectByPrimaryKey(materialsId);
            String relationIds = materials.getRelationIds();
            if(!StringUtils.isEmpty(relationIds)){
                //分割参数，数据格式是：id1,id2,id3,id4&&其他参数；只需要截取&&前面的ids即可
                String[] split = relationIds.split("&&");
                if(split != null && split.length > 0){
                    String idStr = split[0];
                    String[] ids = idStr.split(",");
                    if(ids != null && ids.length > 0){
                        AgUserDesignMaterialsExample example = new AgUserDesignMaterialsExample();
                        AgUserDesignMaterialsExample.Criteria criteria = example.createCriteria();
                        List<String> paramIds = new ArrayList<>();
                        for(String id : ids){
                            paramIds.add(id);
                        }
                        criteria.andIdIn(paramIds);
                        result = materialsMapper.selectByExample(example);
                    }
                }
            }
            return result;
        }
        //再次查询
        AgUserDesignMaterialsExample example = new AgUserDesignMaterialsExample();
        AgUserDesignMaterialsExample.Criteria criteria = example.createCriteria();
        criteria.andDesignSchemeIdEqualTo(schemeId);
        List<AgUserDesignMaterials> agUserDesignMaterials = materialsMapper.selectByExample(example);
        return agUserDesignMaterials;
    }
}
