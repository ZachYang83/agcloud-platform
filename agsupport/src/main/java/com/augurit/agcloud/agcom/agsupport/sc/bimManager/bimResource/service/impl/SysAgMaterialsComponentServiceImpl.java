package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgComponentCodeCustom;
import com.augurit.agcloud.agcom.agsupport.domain.AgMaterialsComponentCustom;
import com.augurit.agcloud.agcom.agsupport.domain.auto.*;
import com.augurit.agcloud.agcom.agsupport.mapper.*;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.*;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.ISysAgCloudSystemService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.ISysAgHouseService2;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.ISysAgMaterialsComponentService;
import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.util.*;
import com.github.pagehelper.Page;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@Service
public class SysAgMaterialsComponentServiceImpl implements ISysAgMaterialsComponentService {
    private static final Logger logger = LoggerFactory.getLogger(SysAgMaterialsComponentServiceImpl.class);

    private static final String uploadTempPath = "uploadTemp";

    @Autowired
    private ISysAgHouseService2 sysAgHouseService;
    @Autowired
    private AgMaterialsComponentMapper materialsComponentMapper;
//    @Autowired
//    private AgPermissionMapper agPermissionMapper;
    @Autowired
    private AgMaterialsComponentCustomMapper materialsComponentCustomMapper;
    @Autowired
    private ISysAgCloudSystemService sysAgCloudSystemService;

    @Override
    public PageInfo<AgMaterialsComponent> list(AgMaterialsComponent materials, Page page) {
        AgMaterialsComponentExample materialsExample = new AgMaterialsComponentExample();
        AgMaterialsComponentExample.Criteria criteria = materialsExample.createCriteria();
        materialsExample.setOrderByClause("create_time desc");
        if(!StringUtils.isEmpty(materials.getName())){
            criteria.andNameLike("%" + materials.getName() + "%");
        }
        //类型
        if(!StringUtils.isEmpty(materials.getCatagory())){
            criteria.andCatagoryEqualTo(materials.getCatagory());
        }
        //材质
        if(!StringUtils.isEmpty(materials.getTexture())){
            String[] textures = materials.getTexture().split(",");
            List<String> list = Arrays.asList(textures);
            criteria.andTextureIn(list);
        }
        //厂商
        if(!StringUtils.isEmpty(materials.getVendor())){
            String[] vendors = materials.getVendor().split(",");
            List<String> list = Arrays.asList(vendors);
            criteria.andVendorIn(list);
        }
        Page resultPage = PageHelper.startPage(page);
        List<AgMaterialsComponent> dbList = materialsComponentCustomMapper.selectByExample(materialsExample);
        return new PageInfo<>(dbList);
//        //重新封装数据
//        List<AgMaterialsComponentCustom> resultList = new ArrayList<>();
//        if (dbList != null && dbList.size() > 0) {
//            for (AgMaterialsComponent custom : dbList) {
//                AgMaterialsComponentCustom resultCustom = new AgMaterialsComponentCustom();
//                BeanUtils.copyProperties(custom, resultCustom);
//
//                //设置授权数量
//                AgPermissionExample permissionExample = new AgPermissionExample();
//                permissionExample.createCriteria().andSourceIdEqualTo(resultCustom.getId()).andTypeEqualTo("2");
//                List<AgPermission> agMaterialsPermissions = agPermissionMapper.selectByExample(permissionExample);
//                if(agMaterialsPermissions != null && agMaterialsPermissions.size() > 0){
//                    resultCustom.setAuthsNum(agMaterialsPermissions.size());
//                    resultCustom.setAuthList(agMaterialsPermissions);
//                }
//
//                resultList.add(resultCustom);
//            }
//        }
//        PageInfo<AgMaterialsComponentCustom> pageInfo = new PageInfo<>(resultList);
//        pageInfo.setTotal(resultPage.getTotal());
//        return pageInfo;
    }

    @Override
    public void save(AgMaterialsComponentWithBLOBs entity, MultipartFile thumbFile, MultipartFile glbFile) {
        //缩略图
        if(thumbFile != null && !thumbFile.isEmpty()){
            try{
                entity.setThumb(thumbFile.getBytes());
                entity.setThumbFileName(thumbFile.getOriginalFilename());
            }catch (Exception e){
                throw new SourceException("缩略图上传失败，请稍后再试");
            }
        }
        //glb
        if(glbFile != null && !glbFile.isEmpty()){
            try{
                entity.setGlb(glbFile.getBytes());
                entity.setGlbFileName(glbFile.getOriginalFilename());
                entity.setModelSize(glbFile.getSize() + "");
            }catch (Exception e){
                throw new SourceException("构件glb上传失败，请稍后再试");
            }
        }
        entity.setId(UUID.randomUUID().toString());
        entity.setCreateTime(new Date());
        //1是rfa；2是glb
        entity.setStatus(2);
        materialsComponentMapper.insert(entity);
    }

    public void save(AgMaterialsComponentWithBLOBs entity, List<FileEntity> modelList) throws IOException{
        if(modelList != null && modelList.size() > 0){
            for(FileEntity modelFile : modelList){
                File file = modelFile.getFile();
                InputStream in = null;
                try{
                    in = new FileInputStream(file);
                    byte[] by = new byte[in.available()];
                    in.read(by);
                    //缩略图
                    if(modelFile.getName().endsWith("jpg") || modelFile.getName().endsWith("jpeg") || modelFile.getName().endsWith("png")){
                        entity.setThumb(by);
                        entity.setThumbFileName(file.getName());
                    }
                    //rfa
                    if(modelFile.getName().endsWith("rfa")){
                        entity.setGlb(by);
                        entity.setGlbFileName(file.getName());
                        entity.setModelSize(file.length() +"");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    throw new IOException(e.getMessage());
                }finally {
                    try{
                        if(in != null){
                            in.close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        entity.setId(UUID.randomUUID().toString());
        entity.setCreateTime(new Date());
        //1是rfa；2是glb
        entity.setStatus(1);
        materialsComponentMapper.insert(entity);
    }

    public void save(AgMaterialsComponentWithBLOBs entity, FileEntity modelFile) throws IOException{

        File file = modelFile.getFile();
        InputStream in = new FileInputStream(file);
        byte[] by = new byte[in.available()];
        in.read(by);

        entity.setGlb(by);
        //glb的名称要去掉前缀“xxx/1.glb" 的xxx
        entity.setGlbFileName(file.getName().substring(file.getName().lastIndexOf("/") + 1));
        entity.setModelSize(file.length() +"");

        entity.setId(UUID.randomUUID().toString());
        entity.setCreateTime(new Date());
        //1是rfa；2是glb
        entity.setStatus(2);
        materialsComponentMapper.insert(entity);
    }

    @Override
    @Transactional
    public void update(AgMaterialsComponentCustom materials, AgComponentCodeCustom param, MultipartFile thumbFile, MultipartFile glbFile) {
        AgMaterialsComponentExample example = new AgMaterialsComponentExample();
        example.createCriteria().andIdEqualTo(materials.getId());
        int count = materialsComponentMapper.countByExample(example);

        if(count == 0){
            throw new SourceException("该数据不存在");
        }
        AgMaterialsComponentWithBLOBs entity = new AgMaterialsComponentWithBLOBs();
        entity.setId(materials.getId());
        entity.setModifyTime(new Date());

        //只修改需要修改的属性
        entity.setName(materials.getName());
        entity.setCatagory(materials.getCatagory());
        entity.setTexture(materials.getTexture());
        entity.setMeasure(materials.getMeasure());
        entity.setVendor(materials.getVendor());
        entity.setSinglePrice(materials.getSinglePrice());
        entity.setRemark(materials.getRemark());
        entity.setSpecification(materials.getSpecification());

        if(!StringUtils.isEmpty(param.getTableCode())){
            entity.setComponentCode(param.getTableCode() + "-"  + param.getLargeCode() + "." + param.getMediumCode() + "." + param.getSmallCode() + "." + param.getDetailCode());
            entity.setComponentCodeName(param.getTableCodeName() + "-"  + param.getLargeCodeName() + "." + param.getMediumCodeName()+ "." + param.getSmallCodeName() + "." + param.getDetailCodeName());
        }

        //缩略图
        if(thumbFile != null && !thumbFile.isEmpty()){
            try{
                entity.setThumb(thumbFile.getBytes());
                entity.setThumbFileName(thumbFile.getOriginalFilename());
            }catch (Exception e){
                throw new SourceException("缩略图上传失败，请稍后再试");
            }
        }
        //glb
        if(glbFile != null && !glbFile.isEmpty()){
            try{
                entity.setGlb(glbFile.getBytes());
                entity.setGlbFileName(glbFile.getOriginalFilename());
                entity.setModelSize(glbFile.getSize() + "");
            }catch (Exception e){
                throw new SourceException("构件glb上传失败，请稍后再试");
            }
        }
        materialsComponentMapper.updateByPrimaryKeySelective(entity);

//        //数据访问权限修改，先清空材料列表所有的权限，再重新添加
//        AgPermissionExample materialsePermissionExample = new AgPermissionExample();
//        materialsePermissionExample.createCriteria().andSourceIdEqualTo(materials.getId()).andTypeEqualTo("2");
//        agPermissionMapper.deleteByExample(materialsePermissionExample);
//        //重新添加权限
//        if(!StringUtils.isEmpty(materials.getAuths())){
//            String[] authCodes = materials.getAuths().split(",");
//            for(String code : authCodes){
//                AgPermission permission = new AgPermission();
//                permission.setId(UUID.randomUUID().toString());
//                permission.setSourceId(materials.getId());
//                permission.setCode(code);
//                permission.setType("2");
//                agPermissionMapper.insert(permission);
//            }
//        }
    }

    @Override
    @Transactional
    public void delete(String id) {
        materialsComponentMapper.deleteByPrimaryKey(id);
//        //也要删除该材料下的授权
//        AgPermissionExample materialsePermissionExample = new AgPermissionExample();
//        materialsePermissionExample.createCriteria().andSourceIdEqualTo(id).andTypeEqualTo("2");
//        agPermissionMapper.deleteByExample(materialsePermissionExample);
    }

    @Override
    @Transactional
    public void batchDelete(String ids) {
        for(String id : ids.split(",")){
            this.delete(id);
        }
    }

    @Override
    public void uploadglb(MultipartFile file) {
        String fileUploadPath = null;
        try {
            String baseFilePath = sysAgHouseService.getBaseFilePath();
            fileUploadPath = uploadTempPath + File.separator + UUID.randomUUID().toString();
            String path = baseFilePath + File.separator + fileUploadPath;
            List<FileEntity> fileEntities = BimZipUtils.getGlbFileFromZip(file, path);
            if (fileEntities != null && fileEntities.size() > 0) {
                for (FileEntity entity : fileEntities) {
                    //构件命名规则catagory_objectid.glb
                    String[] name = entity.getName().split("_");
                    if(name.length == 2){
                        //从文件获取分类和对象id
                        String catagory = name[0];
                        String objectid = name[1].substring(0, name[1].lastIndexOf("."));
                        AgMaterialsComponentExample example = new AgMaterialsComponentExample();
                        example.createCriteria().andCatagoryEqualTo(catagory).andObjectidEqualTo(objectid);
                        //查询该属性，查询后，直接更新glb
                        List<AgMaterialsComponent> agcim3dentityVb1s = materialsComponentMapper.selectByExample(example);
                        if(agcim3dentityVb1s != null && agcim3dentityVb1s.size() > 0){
                            for(AgMaterialsComponent evb1: agcim3dentityVb1s){
                                AgMaterialsComponentWithBLOBs update = new AgMaterialsComponentWithBLOBs();
                                update.setId(evb1.getId());
                                File glbFile = entity.getFile();
                                update.setModelSize(glbFile.length()+"");
                                InputStream glbStream = null;
                                try{
                                    glbStream = new FileInputStream(glbFile);
                                    //将inputStream装成byte[]
                                    byte[] byt = new byte[glbStream.available()];
                                    glbStream.read(byt);
                                    update.setGlb(byt);
                                    update.setGlbFileName(entity.getName());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }finally {
                                    if(glbStream != null){
                                        try{
                                            glbStream.close();
                                        }catch (IOException e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                materialsComponentMapper.updateByPrimaryKeySelective(update);
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        } catch (IOException e) {
            logger.info(e.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
        }finally {
            sysAgHouseService.deleteFileFromCategorySourcePath(fileUploadPath);
        }
    }

    @Override
    public Object statistics() {
        //材质、厂商
        Map<String, Object> resultMap = new HashMap<>();
        String texture = "select texture from ag_materials_component group by texture";
        String vendor = "select vendor from ag_materials_component group by vendor";
        List<AgMaterialsComponentWithBLOBs> withBLOBsTexture = materialsComponentCustomMapper.sql(texture);
        List<AgMaterialsComponentWithBLOBs> withBLOBsVendor = materialsComponentCustomMapper.sql(vendor);
        if(withBLOBsTexture != null && withBLOBsTexture.size() > 0){
            List<Map> lists = new ArrayList<>();
            for(AgMaterialsComponentWithBLOBs entity : withBLOBsTexture){
                if(entity != null && !StringUtils.isEmpty(entity.getTexture())){
                    Map<String, String> map = new HashMap<>();
                    map.put("text", entity.getTexture());
                    map.put("value", entity.getTexture());
                    lists.add(map);
                }
            }
            resultMap.put("texture", lists);
        }

        if(withBLOBsVendor != null && withBLOBsVendor.size() > 0){
            List<Map> lists = new ArrayList<>();
            for(AgMaterialsComponentWithBLOBs entity : withBLOBsVendor){
               if(entity != null && !StringUtils.isEmpty(entity.getVendor())){
                   Map<String, String> map = new HashMap<>();
                   map.put("text", entity.getVendor());
                   map.put("value", entity.getVendor());
                   lists.add(map);
               }
            }
            resultMap.put("vendor", lists);
        }
        return resultMap;
    }


    @Override
    @Transactional
    public void saveRfa(MultipartFile modelFile) {
        String fileUploadPath = null;
        try{
            //获取模型信息
            String baseFilePath = sysAgHouseService.getBaseFilePath();
            fileUploadPath = uploadTempPath + File.separator + UUID.randomUUID().toString();
            String path = baseFilePath + File.separator + fileUploadPath;
            Map<String, Object> zip = BimZipUtils.getMaterialsRfaFileFromZip(modelFile, path);
            FileEntity excelFile = (FileEntity)zip.get("excel");;
            List<FileEntity> modelList = (List<FileEntity>)zip.get("modelFiles");
            if(excelFile == null || StringUtils.isEmpty(excelFile.getName())){
                throw new SourceException("zip文件格式不正确，找不到excel文件");
            }
            if(modelList == null || modelList.size() == 0){
                throw new SourceException("zip文件格式不正确，找不到构件文件");
            }
            //读取excel文件内容
            List<Object[]> materialsDatas = BimExcelUtil.getExcelData(excelFile.getName(), new FileInputStream(excelFile.getFile()));
            if(materialsDatas == null || materialsDatas.size() <= 1){
                throw new SourceException("请填写构件信息表数据");
            }

            //上传信息>> excel表结构:  0名称 1类目编码  2材质 3厂商 4价格 5简介
            for(int i = 1; i < materialsDatas.size(); i++){
                AgMaterialsComponentWithBLOBs materials = new AgMaterialsComponentWithBLOBs();
                //excel格式数据判断
                if(materialsDatas.get(i).length != 6){
                    throw new SourceException("excel格式不正确，请重新下载模板");
                }
                materials.setName((String)materialsDatas.get(i)[0]);
                String caCode = (String)materialsDatas.get(i)[1];
                if(!StringUtils.isEmpty(caCode)){
                    //类目编码不为null，格式是：表代码-大类.种类.小类.细类 如：30-13.10.35.10
                    //获取表代码
                    String[] split = caCode.split("-");
                    if(split != null && split.length == 2){
                        //设置类目编码
                        setComponentCode(materials, split);
                    }
                }
                materials.setTexture((String)materialsDatas.get(i)[2]);
                materials.setVendor((String)materialsDatas.get(i)[3]);
                materials.setSinglePrice((String)materialsDatas.get(i)[4]);
                materials.setRemark((String)materialsDatas.get(i)[5]);

                //遍历所有模型数据，找到当前模型的所有信息
                List<FileEntity> thisModels = new ArrayList<>();
                for(FileEntity entity : modelList){
                    if(entity.getName().startsWith(materials.getName())){
                        thisModels.add(entity);
                    }
                }
                //保存模型
                save(materials, thisModels);
            }

        }catch (SourceException e){
            logger.info("--------SourceException-------" + e.getMessage());;
            throw new SourceException(e.getMessage());
        }catch (IOException e){
            logger.info("------上传失败，文件格式不正确---------" + e.getMessage());;
            throw new SourceException("上传失败，文件格式不正确");
        }catch (Exception e){
            logger.info("------上传失败---------" + e.getMessage());;
            throw new SourceException("上传失败");
        }finally {
            if(fileUploadPath != null){
                //删除所有临时文件
                sysAgHouseService.deleteFileFromCategorySourcePath(fileUploadPath);
            }
        }
    }

    private void setComponentCode(AgMaterialsComponentWithBLOBs entity, String[] split) {
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
    public void saveDoorOrWin(AgMaterialsComponentWithBLOBs entity, MultipartFile glbFile, MultipartFile thumbFile) {
        if(StringUtils.isEmpty(entity.getId())){
           entity.setId(UUID.randomUUID().toString());
        }
        //缩略图
        if(thumbFile != null && !thumbFile.isEmpty()){
            try{
                entity.setThumb(thumbFile.getBytes());
                entity.setThumbFileName(thumbFile.getOriginalFilename());
            }catch (Exception e){
                throw new SourceException("缩略图上传失败，请稍后再试");
            }
        }
        //glb
        if(glbFile != null && !glbFile.isEmpty()){
            try{
                entity.setGlb(glbFile.getBytes());
                entity.setGlbFileName(glbFile.getOriginalFilename());
                entity.setModelSize(glbFile.getSize() + "");
            }catch (Exception e){
                throw new SourceException("构件glb上传失败，请稍后再试");
            }
        }
        entity.setCreateTime(new Date());
        //1是rfa；2是glb
        entity.setStatus(2);
        materialsComponentMapper.insert(entity);
    }

    @Override
    @Transactional
    public void saveGlbZip(MultipartFile modelFile) {
        String fileUploadPath = null;
        try{
            //获取模型信息
            String baseFilePath = sysAgHouseService.getBaseFilePath();
            fileUploadPath = uploadTempPath + File.separator + UUID.randomUUID().toString();
            String path = baseFilePath + File.separator + fileUploadPath;
            Map<String, Object> zip = BimZipUtils.getMaterialsGlbFileFromZip(modelFile, path);
            List<FileEntity> modelList = (List<FileEntity>)zip.get("modelFiles");
            if(modelList == null || modelList.size() == 0){
                throw new SourceException("zip文件格式不正确，找不到构件文件");
            }
            for(FileEntity entity : modelList){
                AgMaterialsComponentWithBLOBs materials = new AgMaterialsComponentWithBLOBs();
                String name = entity.getName();
                //此处name携带一个文件夹，文件夹名称就是构件名称
                if(!name.contains("/")){
                    throw new SourceException("zip文件格式不正确，构件文件找不到");
                }
                name = name.substring(0, name.indexOf("/"));
                materials.setName(name);
                //保存模型
                save(materials, entity);
            }
        }catch (SourceException e){
            logger.info("--------SourceException-------" + e.getMessage());;
            throw new SourceException(e.getMessage());
        }catch (IOException e){
            logger.info("------上传失败，文件格式不正确---------" + e.getMessage());;
            throw new SourceException("上传失败，文件格式不正确");
        }catch (Exception e){
            logger.info("------上传失败---------" + e.getMessage());;
            throw new SourceException("上传失败");
        }finally {
            if(fileUploadPath != null){
                //删除所有临时文件
                sysAgHouseService.deleteFileFromCategorySourcePath(fileUploadPath);
            }
        }
    }

    @Override
    public void view(String type, String id, HttpServletResponse response) {
        //type=1图片；type=2glb

        if("1".equals(type)){
            AgMaterialsComponentWithBLOBs withBLOBs = materialsComponentCustomMapper.selectThumb(id);
            FileUtil.writerFile(withBLOBs.getThumb(), StringUtils.isEmpty(withBLOBs.getThumbFileName()) ? UUID.randomUUID().toString() + ".jpg": withBLOBs.getThumbFileName(), false, response);
        }
        if("2".equals(type)){
            AgMaterialsComponentWithBLOBs withBLOBs = materialsComponentCustomMapper.selectGlb(id);
            FileUtil.writerFile(withBLOBs.getGlb(), StringUtils.isEmpty(withBLOBs.getGlbFileName()) ? UUID.randomUUID().toString() + ".glb": withBLOBs.getGlbFileName(), false, response);
        }
    }
}
