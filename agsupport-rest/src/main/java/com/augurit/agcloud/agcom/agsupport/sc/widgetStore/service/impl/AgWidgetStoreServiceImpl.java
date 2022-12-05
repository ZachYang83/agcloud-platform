package com.augurit.agcloud.agcom.agsupport.sc.widgetStore.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgWidgetStoreCustom;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetStoreExample;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetStoreWithBLOBs;
import com.augurit.agcloud.agcom.agsupport.mapper.AgWidgetStoreCustomMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgWidgetStoreMapper;
import com.augurit.agcloud.agcom.agsupport.sc.widgetStore.service.IAgWidgetStoreService;
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
import java.util.Base64;
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
public class AgWidgetStoreServiceImpl implements IAgWidgetStoreService {
    private static final Logger logger = LoggerFactory.getLogger(AgWidgetStoreServiceImpl.class);
    @Autowired
    private AgWidgetStoreMapper agWidgetStoreMapper;
    @Autowired
    private AgWidgetStoreCustomMapper agWidgetStoreCustomMapper;
    @Value("${upload.filePath}")
    private String uploadFilePath;

    @Override
    public PageInfo<AgWidgetStoreCustom> list(String name, Page page) {
        AgWidgetStoreExample example = new AgWidgetStoreExample();
        example.setOrderByClause("create_time desc");
        AgWidgetStoreExample.Criteria criteria = example.createCriteria();
        //只能是审核通过的数据
        criteria.andStatusEqualTo(2);
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        PageHelper.startPage(page);
        List<AgWidgetStoreCustom> list = agWidgetStoreCustomMapper.selectByExampleWitThumbhBLOBs(example);
        if(list != null && list.size() >0){
            for(AgWidgetStoreCustom en: list){
                byte[] thumb = en.getThumb();
                if(thumb != null && thumb.length > 0){
                    en.setViewImg(Base64.getEncoder().encodeToString(thumb));
                }
                en.setThumb(null);
            }
        }
        return new PageInfo<AgWidgetStoreCustom>(list);
    }

    @Override
    @Transactional
    public void save(AgWidgetStoreWithBLOBs store, MultipartFile thumbFile, MultipartFile widgetFile) {
        String id = UUID.randomUUID().toString();
        store.setId(id);
        store.setCreateTime(new Date());
        //默认未审核
        store.setStatus(1);
        //缩略图从zip获取
//        try {
//            if (thumbFile != null && !thumbFile.isEmpty()) {
//                store.setThumb(thumbFile.getBytes());
//                store.setThumbName(thumbFile.getOriginalFilename());
//            }
//        } catch (IOException e) {
//            logger.info("-----------微件商店获取文件缩略图失败------------" + e.getMessage());
//        }

        if (widgetFile != null && !widgetFile.isEmpty()) {
            //解压路径
            String outputPath = null;
            try {
                store.setWidgetFile(widgetFile.getBytes());
                String widgetFileName = widgetFile.getOriginalFilename();
                store.setWidgetName(widgetFileName);
                store.setRouteName(widgetFileName.substring(0, widgetFileName.lastIndexOf(".")));
                store.setSize(String.valueOf(widgetFile.getSize()));

                //解析压缩包，从压缩包里面获取数据
                outputPath = uploadFilePath + id;
                outputPath = outputPath.replaceAll("/", "/".equals(File.separator) ? File.separator : File.separator + File.separator);
                List<File> files = FileUtil.CompressUtil.unZip(widgetFile, outputPath);
                if (files != null && files.size() > 0) {
                    //缩略图名称
                    String imageFileName = setStoreFromMDFile(store, files);
                    //设置缩略图
                    if (imageFileName != null) {
                        setThumbFile(store, files, imageFileName);
                    }
                    // 设置md文件
                    setMdFile(store,files);
                }
            } catch (Exception e) {
                logger.info("----------解压失败，请重新上传压缩文件------------" + e.getMessage());
                throw new RuntimeException("解压失败，请重新上传压缩文件");
            } finally {
                try {
                    FileUtil.CompressUtil.deleteFile(outputPath);
                } catch (Exception e) {
                    logger.info("-------清空解压后的文件失败-----------");
                }
            }
        }
        agWidgetStoreMapper.insert(store);
    }

    private void setMdFile(AgWidgetStoreWithBLOBs store,List<File> files) throws IOException {
        // 获取md二进制文件
        for (File file:files){
            InputStream in = null;
            try {
                if (file.getName().endsWith(".md")){
                    in = new FileInputStream(file);
                    byte[] mdFileBytes = new byte[in.available()];
                    in.read(mdFileBytes);
                    store.setMdFile(mdFileBytes);
                    break;
                }
            }catch (Exception e){
                logger.info("----------md文件读取失败------------" + e.getMessage());
            }finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    private String setStoreFromMDFile(AgWidgetStoreWithBLOBs store, List<File> files) {
        String imageFileName = null;
        for (File localFile : files) {
            //解析md文件，或者里面的信息
            if (localFile.getName().endsWith(".md")) {
                InputStreamReader read = null;
                BufferedReader bufferedReader = null;
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(localFile);
                    read = new InputStreamReader(fileInputStream, "utf-8");
                    bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while ((lineTxt = bufferedReader.readLine()) != null) {
                        //## 名称
                        if ("## 名称".equals(lineTxt)) {
                            lineTxt = bufferedReader.readLine();
                            if (!StringUtils.isEmpty(lineTxt)) {
                                store.setName(lineTxt);
                            }
                        }
                        //## 英文名
                        if ("## 英文名".equals(lineTxt)) {
                            lineTxt = bufferedReader.readLine();
                            if (!StringUtils.isEmpty(lineTxt)) {
                                store.setEnName(lineTxt);
                            }
                        }
                        //## 缩略图
                        if ("## 缩略图".equals(lineTxt)) {
                            lineTxt = bufferedReader.readLine();
                            if (!StringUtils.isEmpty(lineTxt) && lineTxt.length() > 6) {
                                //截取图片地址 例如：![](./logo.png)
                                imageFileName = lineTxt.substring(5, lineTxt.length() - 1);
                                //去掉名称"/"，只要真实名称，通过真实名称匹配
                                imageFileName = imageFileName.substring(imageFileName.lastIndexOf("/") + 1);
                            }
                        }
                        //## sdk版本
                        if ("## sdk版本".equals(lineTxt)) {
                            lineTxt = bufferedReader.readLine();
                            if (!StringUtils.isEmpty(lineTxt)) {
                                store.setVersion(lineTxt);
                            }
                        }
                        //## 描述
                        if ("## 描述".equals(lineTxt)) {
                            lineTxt = bufferedReader.readLine();
                            if (!StringUtils.isEmpty(lineTxt)) {
                                store.setDescription(lineTxt);
                            }
                        }
                        //## 扩展
                        if ("## 扩展".equals(lineTxt)) {
                            lineTxt = bufferedReader.readLine();
                            if (!StringUtils.isEmpty(lineTxt)) {
                                store.setExpand(lineTxt);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                        }
                    }
                    if (read != null) {
                        try {
                            read.close();
                        } catch (IOException e) {
                        }
                    }
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                        }
                    }
                }
                //找到md文件退出循环
                break;
            }
        }
        return imageFileName;
    }

    private void setThumbFile(AgWidgetStoreWithBLOBs store, List<File> files, String imageFileName) {
        for (File localFile : files) {
            if (localFile.getName().endsWith(imageFileName)) {
                InputStream in = null;
                try {
                    in = new FileInputStream(localFile);
                    byte[] imageBytes = new byte[in.available()];
                    in.read(imageBytes);
                    store.setThumb(imageBytes);
                    store.setThumbName(imageFileName);
                } catch (Exception e) {
                    logger.info("----------读取缩略图失败------------" + e.getMessage());
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e) {
                        }
                    }
                }
                //找到了就退出
                break;
            }
        }
    }


    @Override
    @Transactional
    public void update(AgWidgetStoreWithBLOBs store, MultipartFile thumbFile, MultipartFile widgetFile) {
        AgWidgetStoreExample example = new AgWidgetStoreExample();
        example.createCriteria().andIdEqualTo(store.getId());
        int count = agWidgetStoreMapper.countByExample(example);
        if (count != 0) {
            AgWidgetStoreWithBLOBs entity = new AgWidgetStoreWithBLOBs();
            entity.setId(store.getId());
            entity.setModifyTime(new Date());

            //修改审核状态
            if(!StringUtils.isEmpty(store.getStatus())){
                entity.setStatus(store.getStatus());
            }

            //赋值修改属性,从zip里面获取
//            entity.setName(store.getName());
//            entity.setDescription(store.getDescription());
//            entity.setVersion(store.getVersion());
//
//            try {
//                if (thumbFile != null && !thumbFile.isEmpty()) {
//                    entity.setThumb(thumbFile.getBytes());
//                    entity.setThumbName(thumbFile.getOriginalFilename());
//                }
//            } catch (IOException e) {
//                logger.info("-----------微件商店获取文件缩略图失败------------" + e.getMessage());
//            }
//            try {
//                if (widgetFile != null && !widgetFile.isEmpty()) {
//                    entity.setWidgetFile(widgetFile.getBytes());
//                    String widgetFileName = widgetFile.getOriginalFilename();
//                    entity.setWidgetName(widgetFileName);
//                    entity.setRouteName(widgetFileName.substring(0, widgetFileName.lastIndexOf(".")));
//                }
//            } catch (IOException e) {
//                logger.info("-----------微件商店获取文件缩略图失败------------" + e.getMessage());
//            }
            if (widgetFile != null && !widgetFile.isEmpty()) {
                //解压路径
                String outputPath = null;
                try {
                    entity.setWidgetFile(widgetFile.getBytes());
                    String widgetFileName = widgetFile.getOriginalFilename();
                    entity.setWidgetName(widgetFileName);
                    entity.setRouteName(widgetFileName.substring(0, widgetFileName.lastIndexOf(".")));
                    entity.setSize(String.valueOf(widgetFile.getSize()));

                    //解析压缩包，从压缩包里面获取数据
                    outputPath = uploadFilePath + entity.getId();
                    outputPath = outputPath.replaceAll("/", "/".equals(File.separator) ? File.separator : File.separator + File.separator);
                    List<File> files = FileUtil.CompressUtil.unZip(widgetFile, outputPath);
                    if (files != null && files.size() > 0) {
                        //缩略图名称
                        String imageFileName = setStoreFromMDFile(entity, files);
                        //设置缩略图
                        if (imageFileName != null) {
                            setThumbFile(entity, files, imageFileName);
                        }
                        // 设置md文件
                        setMdFile(store,files);
                    }
                } catch (Exception e) {
                    logger.info("----------解压失败，请重新上传压缩文件------------" + e.getMessage());
                    throw new RuntimeException("解压失败，请重新上传压缩文件");
                } finally {
                    try {
                        FileUtil.CompressUtil.deleteFile(outputPath);
                    } catch (Exception e) {
                        logger.info("-------清空解压后的文件失败-----------");
                    }
                }
            }
            agWidgetStoreMapper.updateByPrimaryKeySelective(entity);
        }
    }

    @Override
    @Transactional
    public void deleteBatch(String ids) {
        if(!StringUtils.isEmpty(ids)){
            String[] idArr = ids.split(",");
            for(String id: idArr){
                agWidgetStoreMapper.deleteByPrimaryKey(id);
            }
        }
    }

    @Override
    public AgWidgetStoreWithBLOBs getThumb(String id) {
        AgWidgetStoreWithBLOBs store = agWidgetStoreMapper.selectByPrimaryKey(id);
        return store;
    }

    @Override
    public AgWidgetStoreWithBLOBs getWidget(String id) {
        AgWidgetStoreWithBLOBs store = agWidgetStoreMapper.selectByPrimaryKey(id);
        return store;
    }

    @Override
    public String getMdFile(String id) {
        AgWidgetStoreWithBLOBs store = agWidgetStoreMapper.selectByPrimaryKey(id);
        return readMdFile(store,store.getMdFile());
    }

    private String readMdFile(AgWidgetStoreWithBLOBs store, byte[] bytes) {
        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        InputStream fileInputStream = null;
        StringBuffer strBuff = new StringBuffer();
        String mdFileString = null;
        try {
            fileInputStream = new ByteArrayInputStream(bytes);
            read = new InputStreamReader(fileInputStream, "utf-8");
            bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                strBuff.append(lineTxt);
                strBuff.append("\n");
                //## 缩略图
                if ("## 缩略图".equals(lineTxt)) {
                    lineTxt = bufferedReader.readLine();
                    if (!StringUtils.isEmpty(lineTxt) && lineTxt.length() > 6) {
                        String base64Thumb = Base64.getEncoder().encodeToString(store.getThumb());
                        strBuff.append("![](data:image/png;base64,");
                        strBuff.append(base64Thumb);
                        strBuff.append(")");
                        strBuff.append("\n");
                    }
                }
            }
            mdFileString = strBuff.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                }
            }
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e) {
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return mdFileString;
    }

}
