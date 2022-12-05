package com.augurit.agcloud.agcom.agsupport.sc.echartsLayer.controller;

import com.augurit.agcloud.agcom.agsupport.common.config.RedisCache.MapCache;
import com.augurit.agcloud.agcom.agsupport.common.config.condition.RedisCondition;
import com.augurit.agcloud.agcom.agsupport.common.util.UUIDUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017-06-20.
 */
@RestController
@RequestMapping("/agsupport/echartsLayers")
public class AgEchartsLayerController {

    @Autowired
    private IAgDic iAgDic;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 文件上传路径
     */
    private String filePath = "";

    public void getFilePath() throws Exception {
        AgDic agDic = iAgDic.findAgDicByCode("A015001");
        filePath = agDic.getValue(); //"C:/upload/"
    }

    /**
     * 利用前段的上传控件获取CSV，并转成JSON返回
     */
    @RequestMapping(value = "/readFile", method = RequestMethod.POST)
    @ResponseBody
    public Object readFile(HttpSession session, HttpServletRequest request, @RequestParam("files") MultipartFile file) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String originalFilename = file.getOriginalFilename();
            JSONArray array = new JSONArray();
            BOMInputStream inputStream = new BOMInputStream(file.getInputStream());
            InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
            CSVUtil util = new CSVUtil(streamReader);
            int row = util.getRowNum();//行
            int col = util.getColNum();//列
            JSONArray nameList = new JSONArray();
            for (int j = 0; j < col; j++) {
                nameList.add(util.getString(0, j));
            }
            for (int i = 1; i < row; i++) {
                JSONObject jsonobject = new JSONObject();
                for (int j = 0; j < col; j++) {
                    jsonobject.put(nameList.get(j), util.getString(i, j));//getString(row, col)
                }
                array.add(jsonobject);
            }
            inputStream.close();
            String fileId = UUIDUtil.getUUID();
            stringRedisTemplate.opsForValue().set("echartsLayerTemp:" + fileId, array.toString(), 60 * 60, TimeUnit.SECONDS);
            map.put("fileId", fileId);
            map.put("fileName", originalFilename);
            map.put("fileData", array);
            map.put("nameList", nameList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 传入json，并保存
     *
     * @param fileId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveFile")
    public Object saveFile(String fileId) throws Exception {
        if (filePath == null || filePath == "") {
            getFilePath();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String key = "echartsLayerTemp:" + fileId;
            if (stringRedisTemplate.hasKey(key)) {
                String echartsLayerTemp = stringRedisTemplate.opsForValue().get(key);
                stringRedisTemplate.opsForValue().set("echartsLayer:" + fileId, echartsLayerTemp);
                if (!new File(filePath).exists()) {
                    new File(filePath).mkdirs();
                }
                File file = new File(filePath + "/" + fileId + ".txt");
                BufferedWriter bi = new BufferedWriter(new FileWriter(file));
                bi.write(echartsLayerTemp);
                bi.close();
                map.put("message", "保存成功");
            } else {
                map.put("message", "timeout");
            }
        } catch (IOException e) {
            e.printStackTrace();
            map.put("message", "保存失败");
        }
        return map;
    }

    /**
     * 根据文件id删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteFilesByIds")
    public Object deleteFilesByIds(String ids) throws Exception {
        if (filePath == null || filePath == "") {
            getFilePath();
        }
        String fileIds[] = null;
        if (StringUtils.isNotEmpty(ids)) {
            fileIds = ids.split(",");
        }
        try {
            String error = "";
            if (fileIds != null && fileIds.length > 0) {
                for (String fileId : fileIds) {
                    if(RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
                        stringRedisTemplate.delete("echartsLayer:" + fileId);//根据key删除缓存
                    }
                    File file = new File(filePath + "/" + fileId + ".txt");
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    } else {
                        error += "该文件不存在！（FileId：" + fileId + "）；";
                    }
                }
            }
            ResultForm mesg = error.equals("") ? new ResultForm(true) : new ResultForm(false, error);
            return JsonUtils.toJson(mesg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false, "删除失败！"));
    }

    /**
     * 根据文件ids获取，并以json返回
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping("/getFileByIds")
    public Object getFileByCSVIds(String ids) {
        String fileIds[] = null;
        if (StringUtils.isNotEmpty(ids)) {
            fileIds = ids.split(",");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String error = "";
            if (fileIds != null && fileIds.length > 0) {
                for (String id : fileIds) {
                    String key = "echartsLayer:" + id;
                    String echartsLayerTemp = "";
                    if (stringRedisTemplate.hasKey(key)) {
                        echartsLayerTemp = stringRedisTemplate.opsForValue().get(key);
                        if (echartsLayerTemp.equals("") || echartsLayerTemp == null) { //若redis的key的value被人为地清空，还可以查本地文件
                            echartsLayerTemp = getStringFromFile(id, key);
                        }
                    } else {
                        echartsLayerTemp = getStringFromFile(id, key);
                    }
                    if (echartsLayerTemp.equals("")) {
                        error += "该文件不存在！（FileId：" + id + "）；";
                    } else {
                        map.put(id, echartsLayerTemp);
                    }
                }
            }
            if (error.equals(""))
                return map;
            else
                return JsonUtils.toJson(new ResultForm(false, error));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false, "获取失败"));
    }

    private String getStringFromFile(String id, String key) throws Exception {
        if (filePath == null || filePath == "") {
            getFilePath();
        }
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath + "/" + id + ".txt"), "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String echartsLayerTemp = new String(sb); //StringBuffer ==> String
        stringRedisTemplate.opsForValue().set(key, echartsLayerTemp);
        return echartsLayerTemp;
    }


}
