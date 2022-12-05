package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.common.util.io.DataConversionUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.io.UploadFile;
import com.augurit.agcloud.agcom.agsupport.domain.AgDir;
import com.augurit.agcloud.agcom.agsupport.domain.AgDirLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgMetadata;
import com.augurit.agcloud.agcom.agsupport.mapper.AgDirLayerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgDirMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgMetadataMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.enums.BimPublish;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.reslut.MultipartFileParam;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IAgBimProject;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimFile;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimRelationFile;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimVersion;
import com.augurit.agcloud.agcom.agsupport.util.AgDicUtils;
import com.augurit.agcloud.agcom.agsupport.util.HttpRequestUtils;
import com.augurit.agcloud.agcom.agsupport.util.UrlEncode;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimFile;
import com.augurit.agcloud.agcom.agsupport.domain.AgFileStore;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimProject;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimVersion;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimFileMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgFileStoreMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimVersionMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.dto.BimFileListDTO;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.reslut.FileMD5Util;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcom.common.LoginHelpClient;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static java.util.concurrent.CompletableFuture.runAsync;

/**
 * @ClassName BimFileImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2019/12/4 15:48
 * @Version 1.0
 **/
@Transactional
@Service
public class BimFileImpl implements IBimFile {

    public static final Logger LOGGER = LoggerFactory.getLogger(BimFileImpl.class);
    public static final String BIM_PATH = "/bimfile";
    public static final long CHUNK_SIZE = 104857600;
    private static final String ERROR = "error";
    private static final String ERROR_MSG = "发布失败";
    private static final String RUNNING = "running";
    private static final String RUNNING_MSG = "发布中";
    private static final String SUCCESS = "success";
    private static final String SUCCESS_MSG = "发布完成";

    private static final String IN_USE = "IN_USE";
    private static final String INIT_VERSION = "1.0";
    private static final String CHANGE_TYPE = "shejibiangeng";

    @Value("${bim.base.path}")
    private String basePath;

    @Autowired
    AgBimFileMapper agBimFileMapper;

    @Autowired
    AgBimVersionMapper agBimVersionMapper;

    @Autowired
    BimVersionImpl bimVersionIml;

    @Autowired
    IBimRelationFile iBimRelationFile;

    @Autowired
    AgFileStoreMapper agFileStoreMapper;

    @Autowired
    IBimVersion iBimVersion;

    @Autowired
    IAgBimProject iAgBimProject;

    @Autowired
    AgDirMapper agDirMapper;

    @Autowired
    AgMetadataMapper agMetadataMapper;

    @Autowired
    AgDirLayerMapper agDirLayerMapper;

    @Autowired
    AgLayerMapper agLayerMapper;

    @Autowired
    private HttpServletRequest request;

    private Socket socket;


    /**
     * 根据id获取数据
     *
     * @param id
     * @return
     * @throws RuntimeException
     */
    @Override
    public AgBimFile getById(String id) throws RuntimeException {
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("getById is not null");
        }
        return agBimFileMapper.getById(id);
    }

    /**
     * 获取所有数据
     *
     * @return
     * @throws RuntimeException
     */
    @Override
    public List<AgBimFile> getAll() throws RuntimeException {
        return agBimFileMapper.getAll();
    }

    /**
     * 根据id删除一条数据
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(String id) {
        AgBimFile agBimFile = agBimFileMapper.getById(id);
        if (null == agBimFile)
            return false;
        if (agBimFileMapper.deleteById(id) > 0) {
            List<AgBimVersion> byPkId = agBimVersionMapper.getByOrKeyWords(id, null);
            int len = byPkId.size();
            if (len > 0) {
                for (AgBimVersion agBimVersion : byPkId) {
                    bimVersionIml.deleteFileByVersion(agBimVersion);
                }
                agBimVersionMapper.deleteByPkId(id);
            }
            return true;
        }
        return false;
    }

    /**
     * 删除更多数据
     *
     * @param stringList
     * @return
     */
    @Override
    public boolean deleteMany(List<String> stringList) {
        List<AgBimFile> agFileStoreList = agBimFileMapper.getByIds(stringList);
        if (null == agFileStoreList || agFileStoreList.size() == 0)
            return false;
        if (agBimFileMapper.deleteMany(stringList) > 0) {
            int len = stringList.size();
            for (String pkId : stringList) {
                List<AgBimVersion> byPkId = agBimVersionMapper.getByOrKeyWords(pkId, null);
                int len2 = byPkId.size();
                if (len2 > 0) {
                    for (AgBimVersion agBimVersion : byPkId) {
                        bimVersionIml.deleteFileByVersion(agBimVersion);
                    }
                }
            }
            agBimVersionMapper.deleteByPkId(stringList.toArray(new String[len]));
            return true;
        }
        return false;
    }

    /**
     * 添加一条数据
     *
     * @param agBimFile
     * @return
     * @throws RuntimeException
     */
    @Override
    public boolean add(AgBimFile agBimFile) throws RuntimeException {
        return agBimFileMapper.add(agBimFile) > 0;
    }

    /**
     * 编辑一条数据
     *
     * @param agBimFile
     * @return
     * @throws RuntimeException
     */
    @Override
    public boolean update(AgBimFile agBimFile) throws RuntimeException {
        return agBimFileMapper.update(agBimFile) > 0;
    }

    /**
     * 特殊条件Or分页查询
     *
     * @param projectId
     * @param keyword
     * @param page
     * @return
     * @throws RuntimeException
     */
    @Override
    public PageInfo<AgBimFile> getByOrKeyWords(String projectId, String keyword, Page page) throws RuntimeException {
        PageHelper.startPage(page);
        List<AgBimFile> list = agBimFileMapper.getByOrKeyWords(projectId, keyword);
        return new PageInfo<>(list);
    }

    /**
     * 特殊条件And分页查询
     *
     * @param agBimFile
     * @param page
     * @return
     * @throws RuntimeException
     */
    @Override
    public List<AgBimFile> getByAndKeyWords(AgBimFile agBimFile, Page page) throws RuntimeException {
        List<AgBimFile> list = agBimFileMapper.getByAndKeyWords(agBimFile);
        return list;
    }

    /**
     * 根据md5获取文件信息
     *
     * @param md5
     * @return
     * @throws RuntimeException
     */
    @Override
    public AgBimFile getByMd5(String md5) throws RuntimeException {
        return agBimFileMapper.getByMd5(md5);
    }

    @Transactional
    @Override
    public ContentResultForm startService(String id, boolean polling) {
        if (agBimFileMapper.publishing(id) > 0) {
            return new ContentResultForm(false, ERROR, "有正在发布bim模型，请稍后");
        }
//        169.254.152.191
        AgBimFile bimFile = getById(id);
        if (BimPublish.PUBLISHING.getValue().equals(bimFile.getState())) {
            if (socket.isClosed()) {
                bimFile.setState(BimPublish.PUBLISH_ERROR.getValue());
                update(bimFile);
                return new ContentResultForm(false, ERROR, ERROR_MSG);
            }
            return new ContentResultForm(true, RUNNING, RUNNING_MSG);
        }
        if (BimPublish.PUBLISHED.getValue().equals(bimFile.getState())) {
            if (polling) {
                return new ContentResultForm(true, SUCCESS, SUCCESS_MSG);
            } else {
                return new ContentResultForm(false, ERROR, "已发布");
            }
        }
        if (BimPublish.PUBLISH_ERROR.getValue().equals(bimFile.getState()) && polling) {
            return new ContentResultForm(false, ERROR, ERROR_MSG);
        }
        if (BimPublish.NO_PUBLISH.getValue().equals(bimFile.getState()) || BimPublish.PUBLISH_ERROR.getValue().equals(bimFile.getState())) {
            AgBimVersion bimVersion = bimVersionIml.getInUseByPkId(id);
            if (Objects.isNull(bimVersion)) {
                return new ContentResultForm(false, ERROR, "版本不存在");
            }
            runAsync(() -> {
                OutputStream os = null;
                PrintWriter pw = null;
                InputStream is = null;
                BufferedReader br = null;
                try {
                    socket = new Socket("192.168.21.39", 4444);
                    socket.setKeepAlive(true);
                    os = socket.getOutputStream();
                    pw = new PrintWriter(os);
                    //输入流
                    is = socket.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is));
                    String rvtUrl = basePath + bimVersion.getBimPath();
                    JSONObject object = new JSONObject();
                    object.put("command", "start");
                    JSONObject data = new JSONObject();
                    data.put("rvtUrl", rvtUrl);
                    object.put("data", data);
                    pw.write(object.toString());
                    pw.flush();
                    PrintWriter finalPw = pw;
                    //发送心跳
                    runAsync(() -> {
                        while (!socket.isClosed()) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            finalPw.write("running");
                            finalPw.flush();
                        }
                    });
                    //接收服务器的相应
                    String reply;
                    while (!((reply = br.readLine()) == null)) {
                        JSONObject obj = JSONObject.parseObject(reply);
                        String status = obj.getString("status");
                        if ("running".equals(status)) {
                            bimFile.setState(BimPublish.PUBLISHING.getValue());
                            update(bimFile);
                        }
                        if ("success".equals(status)) {
                            JSONObject dataObj = obj.getJSONObject("data");
                            String url = dataObj.getString("url");
                            bimVersionIml.update(bimVersion);
                            bimFile.setServiceUrl(url);
                            bimFile.setState(BimPublish.PUBLISHED.getValue());
                            update(bimFile);
                            break;
                        }
                        if ("error".equals(status)) {
                            bimFile.setState(BimPublish.PUBLISH_ERROR.getValue());
                            update(bimFile);
                            break;
                        }
                    }
                } catch (IOException e) {
                    bimFile.setState(BimPublish.PUBLISH_ERROR.getValue());
                    update(bimFile);
                    LOGGER.error(e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                        if (br != null) {
                            br.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                        if (pw != null) {
                            pw.close();
                        }
                        if (os != null) {
                            os.close();
                        }
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOGGER.error(e.getMessage());
                    }
                }
            });
            return new ContentResultForm(true, RUNNING, RUNNING_MSG);
        }
        return new ContentResultForm(false, ERROR, ERROR_MSG);
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
        String uploadDirPath = FileUtil.getPath() + BIM_PATH + param.getMd5();
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
            if (FileUtil.saveFile(tmpFile, (FileUtil.getPath() + uploadFile.getPath()))) {
                AgBimFile agBimFile = new AgBimFile();
                agBimFile.setId(UUID.randomUUID().toString());
                agBimFile.setName(uploadFile.getName());
                agBimFile.setAlias(uploadFile.getAlias());
                agBimFileMapper.add(agBimFile);
                AgBimVersion agBimVersion = new AgBimVersion();
                agBimVersion.setId(UUID.randomUUID().toString());
                agBimVersion.setChangeName(uploadFile.getName());
                agBimVersion.setChangeVersion("1.0");
                agBimVersion.setPkId(agBimFile.getId());
                agBimVersion.setBimMd5(param.getMd5());
                agBimVersion.setBimPath(uploadFile.getPath());
                agBimVersion.setBimExtension(uploadFile.getExtension());
                agBimVersion.setBimLength(Integer.toString(new Double(Math.ceil(param.getSize() / 1024)).intValue()));
                agBimVersion.setBimUrl(uploadFile.getUrl());
                agBimVersionMapper.add(agBimVersion);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean addFileAndVersion(String fileName, String md5, String projectId) {
        try {
            String uploadDirPath = FileUtil.getPath() + BIM_PATH + md5;
            String tempFileName = fileName + "_tmp";
            File tmpFile = new File(uploadDirPath, tempFileName);
            String folder = (BIM_PATH + md5);

            UploadFile uploadFile = FileUtil.getUploadFiles(tmpFile, folder);
            uploadFile.setPath(uploadFile.getPath().replaceAll("_tmp", ""));
            uploadFile.setUrl(uploadFile.getUrl().replaceAll("_tmp", ""));
            uploadFile.setExtension(uploadFile.getExtension().replaceAll("_tmp", ""));

            AgBimFile agBimFile = new AgBimFile();
            agBimFile.setId(UUID.randomUUID().toString());
            agBimFile.setUpdateTime(null);
            agBimFile.setName(uploadFile.getName());
            agBimFile.setAlias(uploadFile.getAlias());
            agBimFile.setCreateTime(new Date());
            agBimFile.setProjectId(projectId);
            agBimFileMapper.add(agBimFile);

            AgBimVersion agBimVersion = new AgBimVersion();
            AgBimVersion bimVersionByMd5 = agBimVersionMapper.getByMd5(md5).get(0);
            agBimVersion.setBimPath(bimVersionByMd5.getBimPath());
            agBimVersion.setBimExtension(bimVersionByMd5.getBimExtension());
            agBimVersion.setBimUrl(bimVersionByMd5.getBimUrl());
            agBimVersion.setBimLength(bimVersionByMd5.getBimLength());
            agBimVersion.setBimMd5(md5);
            agBimVersion.setId(UUID.randomUUID().toString());
            agBimVersion.setChangeName(uploadFile.getName());
            agBimVersion.setChangeVersion("1.0");
            agBimVersion.setPkId(agBimFile.getId());
            agBimVersion.setChangeTime(new Date());
            agBimVersionMapper.add(agBimVersion);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Integer saveFileByUrl(String url) {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        String absPath = FileUtil.getPath() + BIM_PATH;
        String fileMD5 = null;
        try {
            String encodedUrl = UrlEncode.encoder(url);
            InputStream input = HttpRequestUtils.sendGet(encodedUrl, null);
            if (null == input) {
                //连接错误，文件流异常
                return 0;
            }
            String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
            String temPath = FileUtil.getPath() + BIM_PATH + "temFile\\" + fileName;
            File file = FileUtil.createFile(new File(temPath));
            bos = new BufferedOutputStream(new FileOutputStream(temPath));
            bis = new BufferedInputStream(input);
            int index;
            byte[] bytes = new byte[1024 * 1024];
            long startTime = System.currentTimeMillis();
            while ((index = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, index);
                bos.flush();
            }
            double costTime = (double) (System.currentTimeMillis() - startTime) / 1000;
            System.out.println("花费时间：" + costTime + "s");

            fileMD5 = FileMD5Util.getFileMD5(file);

            AgBimFile agBimFile = new AgBimFile();
            agBimFile.setId(UUID.randomUUID().toString());
            agBimFile.setName(fileName);
            agBimFile.setAlias(FileUtil.getRandom());
            agBimFileMapper.add(agBimFile);

            AgBimVersion agBimVersion = new AgBimVersion();
            agBimVersion.setId(UUID.randomUUID().toString());
            agBimVersion.setChangeName(fileName);
            agBimVersion.setChangeVersion("1.0");
            agBimVersion.setPkId(agBimFile.getId());
            agBimVersion.setBimMd5(fileMD5);
            agBimVersion.setBimPath(BIM_PATH + fileMD5 + "\\" + fileName);
            agBimVersion.setBimExtension(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()));
            agBimVersion.setBimLength(Integer.toString(new Double(Math.ceil(file.length() / 1024)).intValue()));
            agBimVersion.setBimUrl(FileUtil.toUrl(agBimVersion.getBimPath()));
            agBimVersionMapper.add(agBimVersion);


            //下载成功
            return 1;
        } catch (Exception e) {
            //"连接错误，检查网络是否畅通或地址是否正确
            e.printStackTrace();
            return 2;
        } finally {
            try {
                if (null != bos) {
                    bos.close();
                    if (StringUtils.isNotBlank(fileMD5)) {
                        new File(absPath + "temFile").renameTo(new File(absPath + fileMD5));
                    }
                }
                if (null != bis) {
                    bis.close();
                }
            } catch (IOException e) {
            }
        }
    }

    @Override
    public List<AgFileStore> getFileByModuleCode(AgFileStore store, String bimId) throws RuntimeException {
        List<String> fileIds = iBimRelationFile.findFileIdByBimId(bimId);
        List<AgFileStore> list = agFileStoreMapper.getFileByModuleCode(store);
        list.removeIf(x -> fileIds.contains(x.getId()));
        return list;
    }

    @Override
    public ContentResultForm checkBimInfo(String id) throws IOException {
        AgBimVersion agBimVersion = iBimVersion.getInUseByPkId(id);
        //文件不存在
        if (null == agBimVersion) {
            return new ContentResultForm(false, null, "文件不存在");
        }
        String path = basePath + agBimVersion.getBimPath();
        File file = new File(path);
        BimInfo bimInfo = new BimInfo();
        if (file.exists()) {
            String fileName = file.getName();
            String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            ZipEntry zipEntry = null;
            int fileNum = 1;
            List<String> rvtList = new ArrayList<>();
            List<String> irList = new ArrayList<>();
            List<String> otherList = new ArrayList<>();
            if (fileType.equals(".zip")) {
                ZipFile zf = new ZipFile(path);
                fileNum = zf.size() - 1;
                ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(path), Charset.forName("GBK"));
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (zipEntry.isDirectory()) {
                        continue;
                    } else {
                        String fn = zipEntry.getName().substring(zipEntry.getName().lastIndexOf("/") + 1);
                        String ft = fn.substring(fn.lastIndexOf("."), fn.length());
                        if (ft.equals(".rvt") || ft.equals("rfa")) rvtList.add(fn);
                        else otherList.add(fn);
                        if (!isValidFileName(fn)) irList.add(fn);
                    }
                }
            } else if (fileType.equals(".rar")) {
                return new ContentResultForm(false, null, "暂不支持rar格式检查");
            } else if (fileType.equals(".rvt") || fileType.equals(".rfa")) {
                if (!isValidFileName(fileName)) irList.add(fileName);
                rvtList.add(fileName);
            } else {
                if (!isValidFileName(fileName)) irList.add(fileName);
                otherList.add(fileName);
            }
            bimInfo.setRvtList(rvtList);
            bimInfo.setIrList(irList);
            bimInfo.setOtherList(otherList);
            bimInfo.setRvtNum(rvtList.size());
            bimInfo.setIrNum(irList.size());
            bimInfo.setOtherNum(otherList.size());
            bimInfo.setFileNum(fileNum);
        }
        return new ContentResultForm(true, bimInfo, "分析成功！");
    }

    @Override
    public void downloadBIMinZip(String pkId, String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        AgBimVersion agBimVersion = iBimVersion.getInUseByPkId(pkId);
        //文件不存在
        if (null == agBimVersion) {
            DataConversionUtil.setResponseJson(response, JSON.toJSONString(
                    new ContentResultForm(false, null, "文件不存在!")));
            return;
        }
        String path = basePath + agBimVersion.getBimPath();
        ZipFile zf = new ZipFile(path);
        // 配置文件下载
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        // 下载文件能正常显示中文
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        byte[] buffer = new byte[1024];
        InputStream in = null;
        BufferedInputStream bis = null;
        try {
            in = zf.getInputStream(zf.getEntry(fileName));
            bis = new BufferedInputStream(in);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }

        } catch (Exception e) {
            DataConversionUtil.setResponseJson(response, JSON.toJSONString(
                    new ContentResultForm(false, null, "下载失败")));
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    DataConversionUtil.setResponseJson(response, JSON.toJSONString(
                            new ContentResultForm(false, null, "下载失败")));
                    return;
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    DataConversionUtil.setResponseJson(response, JSON.toJSONString(
                            new ContentResultForm(false, null, "下载失败")));
                    return;
                }
            }
            zf.close();
        }
    }

    public static class BimInfo {

        private int fileNum;
        private int rvtNum;
        private int irNum;
        private int otherNum;
        private List<String> rvtList;
        private List<String> irList;
        private List<String> otherList;

        public int getFileNum() {
            return fileNum;
        }

        public void setFileNum(int fileNum) {
            this.fileNum = fileNum;
        }

        public int getRvtNum() {
            return rvtNum;
        }

        public void setRvtNum(int rvtNum) {
            this.rvtNum = rvtNum;
        }

        public int getIrNum() {
            return irNum;
        }

        public void setIrNum(int irNum) {
            this.irNum = irNum;
        }

        public int getOtherNum() {
            return otherNum;
        }

        public void setOtherNum(int otherNum) {
            this.otherNum = otherNum;
        }

        public List<String> getRvtList() {
            return rvtList;
        }

        public void setRvtList(List<String> rvtList) {
            this.rvtList = rvtList;
        }

        public List<String> getIrList() {
            return irList;
        }

        public void setIrList(List<String> irList) {
            this.irList = irList;
        }

        public List<String> getOtherList() {
            return otherList;
        }

        public void setOtherList(List<String> otherList) {
            this.otherList = otherList;
        }
    }

    public static boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.length() > 255)
            return false;
        else
            return fileName.matches(
                    "[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
    }

    @Override
    public void saveList(BimFileListDTO fileListDTO) {
        String loginName = LoginHelpClient.getLoginName(request);
        fileListDTO.getFileList().forEach(file->{
            AgBimFile bimFile = new AgBimFile();
            bimFile.setId(UUID.randomUUID().toString());
            bimFile.setProjectId(fileListDTO.getProjectId());
            bimFile.setName(file.getName());
            bimFile.setAlias(file.getName());
            bimFile.setCreateName(loginName);
            bimFile.setCreateTime(new Date());
            agBimFileMapper.add(bimFile);
            AgBimVersion version = new AgBimVersion();
            version.setId(UUID.randomUUID().toString());
            version.setChangeName(file.getName());
            version.setChangeVersion(INIT_VERSION);
            version.setChangeType(CHANGE_TYPE);
            version.setChangePeople(loginName);
            version.setChangeTime(new Date());
            version.setBimUrl(file.getUrl());
            version.setBimPath(file.getPath());
            version.setBimLength(String.valueOf(file.getLength()));
            version.setBimExtension(file.getExtension());
            version.setPkId(bimFile.getId());
            version.setFileName(file.getName());
            version.setIsCurrent(IN_USE);
            agBimVersionMapper.add(version);
        });
    }

    @Override
    public void saveBimFile(AgBimFile bimFile) {
        bimFile.setId(UUID.randomUUID().toString());
        bimFile.setUpdateTime(null);
        bimFile.setCreateTime(new Date());
        this.add(bimFile);
        AgBimVersion bimVersion = bimFile.getBimVersion();
        if(StringUtils.isNotBlank(bimVersion.getFileName())){
            bimVersion.setId(UUID.randomUUID().toString());
            bimVersion.setChangeVersion(INIT_VERSION);
            bimVersion.setPkId(bimFile.getId());
            bimVersion.setChangeType(CHANGE_TYPE);
            bimVersion.setIsCurrent(IN_USE);
            bimVersion.setChangePeople(LoginHelpClient.getLoginName(request));
            bimVersion.setChangeTime(new Date());
            agBimVersionMapper.add(bimVersion);
        }
    }

    @Override
    public void updateBimFile(AgBimFile agBimFile) {
        update(agBimFile);
        AgBimVersion bimVersion = agBimFile.getBimVersion();
        bimVersion.setChangeTime(new Date());
        bimVersion.setChangePeople(LoginHelpClient.getLoginName(request));
        if(StringUtils.isNotBlank(bimVersion.getId()) ) {
            if(StringUtils.isNotBlank(bimVersion.getFileName())){
                agBimVersionMapper.update(bimVersion);
            }else{
                agBimVersionMapper.deleteById(bimVersion.getId());
            }
        }
        if(StringUtils.isBlank(bimVersion.getId()) && StringUtils.isNotBlank(bimVersion.getFileName()) ){
            bimVersion.setId(UUID.randomUUID().toString());
            String maxVersion = agBimVersionMapper.findMaxVersion(agBimFile.getId());
            if(StringUtils.isBlank(maxVersion)){
                maxVersion = INIT_VERSION;
            }
            bimVersion.setIsCurrent(IN_USE);
            bimVersion.setChangeVersion(maxVersion);
            bimVersion.setPkId(agBimFile.getId());
            bimVersion.setChangeType(CHANGE_TYPE);
            bimVersion.setChangePeople(LoginHelpClient.getLoginName(request));
            bimVersion.setChangeTime(new Date());
            agBimVersionMapper.add(bimVersion);
        }
    }

    @Override
    public void saveDirAndLayer(AgBimFile bimFile) throws Exception {
        List<AgBimProject> projectList = iAgBimProject.findProjectTree(bimFile.getProjectId());
        StringBuilder path = new StringBuilder("/目录管理/BIM资源");
        AgDir rootDir = agDirMapper.findAgDirByXpath(path.toString());
        if(Objects.isNull(rootDir)){
            LOGGER.error("目录不存在,图层自动生成错误");
            return;
        }
        StringBuilder dirSeq = new StringBuilder(rootDir.getDirSeq());
        String parentId = rootDir.getId();
        String dicId = null;
        for(AgBimProject project:projectList){
            AgDir dir = new AgDir();
            dir.setId(UUID.randomUUID().toString());
            dirSeq.insert(0, dir.getId() + ",");
            dir.setDirSeq(dirSeq.toString());
            path.append("/").append(project.getProjectName());
            dir.setXpath(path.toString());
            dir.setParentId(parentId);
            dir.setOrderNm(Integer.parseInt(agDirMapper.getOrder(parentId))+1);
            parentId = dir.getId();
            agDirMapper.save(dir);
            dicId = dir.getId();
        }
        String loginName = LoginHelpClient.getLoginName(request);
        AgMetadata metadata = new AgMetadata();
        metadata.setId(UUID.randomUUID().toString());
        metadata.setCreateTime(new Date());
        metadata.setOwner(loginName);
        agMetadataMapper.insertAgMetadata(metadata);
        AgLayer layer = new AgLayer();
        layer.setId(UUID.randomUUID().toString());
        layer.setName(bimFile.getName());
        layer.setNameCn(bimFile.getName());
        layer.setUrl(bimFile.getServiceUrl());
        layer.setLayerType("090002");
        layer.setMetadataId(metadata.getId());
        layer.setDirId(dicId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        layer.setCreateDate(dateFormat.format(new Date()));
        layer.setCreator(loginName);
        agLayerMapper.save(layer);
        AgDirLayer dirLayer = new AgDirLayer();
        dirLayer.setId(UUID.randomUUID().toString());
        dirLayer.setDirId(dicId);
        dirLayer.setLayerId(layer.getId());
        dirLayer.setOrderNm(Integer.parseInt(agDirLayerMapper.getMaxOrder())+1);
        agDirLayerMapper.save(dirLayer);
    }

    @Override
    public List<AgBimFile> findByProjectId(String projectId) {
        return agBimFileMapper.findByProjectId(projectId);
    }


}
