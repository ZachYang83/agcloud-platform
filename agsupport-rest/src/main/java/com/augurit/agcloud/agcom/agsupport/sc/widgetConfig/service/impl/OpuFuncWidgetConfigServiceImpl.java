package com.augurit.agcloud.agcom.agsupport.sc.widgetConfig.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.OpuFuncWidgetConfig;
import com.augurit.agcloud.agcom.agsupport.mapper.OpuFuncWidgetConfigMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.widgetConfig.service.IOpuFuncWidgetConfigService;
import com.augurit.agcloud.agx.common.domain.AgxRsCloudSoft;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.opus.common.domain.OpuRsFunc;
import com.augurit.agcloud.opus.common.mapper.OpuRsFuncMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OpuFuncWidgetConfigServiceImpl implements IOpuFuncWidgetConfigService {

    @Autowired
    private OpuFuncWidgetConfigMapper opuFuncWidgetConfigMapper;
    @Autowired
    private OpuRsFuncMapper opuRsFuncMapper;
    @Value("${agcom-widget-url}")
    private String agcomWidgetUrl;
    @Value("${agcom-widget-name}")
    private String agcomWidgetName;


    /**
     * 获取所有激活微件配置
     * @param funcCode
     * @return
     * @throws Exception
     */
    public List<OpuFuncWidgetConfig> getAllConfigDataForRest(String funcCode,String key) throws Exception{
        return opuFuncWidgetConfigMapper.getAllConfigDataForRest(funcCode,key);
    }

    /**
     * 获取微件对应配置
     * @param funcInvokeUrl
     * @param funcCode
     * @param configKey
     * @return
     * @throws Exception
     */
    public OpuFuncWidgetConfig getConfigDataByKey(String funcInvokeUrl,String funcCode,String configKey)throws Exception{
        return opuFuncWidgetConfigMapper.getConfigDataByKey(funcInvokeUrl,funcCode,configKey);
    }

    /**
     * 根据微件访问地址或微件编码获取微件配置信息列表
     * @param funcInvokeUrl
     * @param funcCode
     * @return
     */
    public List<OpuFuncWidgetConfig> getConfigListByUrlOrCode(String funcInvokeUrl,String funcCode) {
        List<OpuFuncWidgetConfig> listByUrl = opuFuncWidgetConfigMapper.getConfigListByUrlOrCode(funcInvokeUrl,funcCode);
        return listByUrl;
    }


    /***
     * 按条件查询
     * @param record
     * @return
     * @throws Exception
     */
    public List<OpuFuncWidgetConfig> searchByParams(OpuFuncWidgetConfig record) throws Exception{
        return opuFuncWidgetConfigMapper.searchByParams(record);
    }


    /**
     * 按条件分页查询
     *
     * @param name
     * @param page
     * @return
     * @throws Exception
     */
    public PageInfo<OpuRsFunc> searchParam(String name, Page page) throws Exception{
        PageHelper.startPage(page);
        List<OpuRsFunc> list = opuFuncWidgetConfigMapper.findList(name);
        //处理文件url
        if(list!=null && list.size()>0){
            for(OpuRsFunc opuRsFunc:list){
                String funcInvokeUrl=opuRsFunc.getFuncInvokeUrl();
                opuRsFunc.setFuncInvokeUrl(null);
                if(StringUtils.isNotEmpty(funcInvokeUrl)){
                    if(funcInvokeUrl.lastIndexOf("/")!=-1){
                        String str=funcInvokeUrl.substring(0,funcInvokeUrl.lastIndexOf("/"));
                        if(StringUtils.isNotEmpty(str)){
                            String fileUrl=agcomWidgetUrl+str+"/"+agcomWidgetName;
                            opuRsFunc.setFuncInvokeUrl(fileUrl);
                        }
//                        //判断文件是否存在
//                        try{
//                            URL serverUrl=new URL(fileUrl);
//                            HttpURLConnection con=(HttpURLConnection)serverUrl.openConnection();
//                            String message=con.getHeaderField(0);
//                            if(StringUtils.isNotEmpty(message) && message.startsWith("HTTP/1.1 404")){
//                                //不存在
//                                opuRsFunc.setFuncInvokeUrl(null);
//                            }else{
//                                opuRsFunc.setFuncInvokeUrl(fileUrl);
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                            opuRsFunc.setFuncInvokeUrl(null);
//                        }
                    }
                }
            }
        }
        return new PageInfo<OpuRsFunc>(list);
    }

    /**
     * 根据微件编码获取微件配置数据
     * @param funcCode
     * @return
     */
    public List<OpuFuncWidgetConfig> getConfigData(String funcCode)throws Exception{
        return opuFuncWidgetConfigMapper.getConfigData(funcCode);
    }

    /**
     * 保存配置信息
     * @param record
     * @throws Exception
     */
    public void save(OpuFuncWidgetConfig record) throws Exception{
        if(StringUtils.isNotEmpty(record.getId())){
            opuFuncWidgetConfigMapper.updateByPrimaryKeySelective(record);
        }else{
            record.setId(UUID.randomUUID().toString());
            opuFuncWidgetConfigMapper.insert(record);
        }
    }
    /**
     * 删除配置信息
     * @param ids
     * @throws Exception
     */
    public void deleteConfigData(String[] ids) throws Exception{
        if(ids!=null && ids.length>0){
            for(String id:ids){
                opuFuncWidgetConfigMapper.deleteByPrimaryKey(id);
            }
        }
    }

    /**
     * 删除微件信息
     * @param ids
     * @throws Exception
     */
    public void deleteWidget(String[] ids) throws Exception{
        if(ids!=null && ids.length>0){
            List<String> datas=new ArrayList<String>();
            for(String id:ids){
                datas.add(id);
            }
            opuRsFuncMapper.batchDeleteModuleOrFunc(datas);
        }
    }

    /**
     * 保存微件信息
     * @param funcId
     * @param funcName
     * @throws Exception
     */
    public void saveWidget(String funcId,String funcName) throws Exception{
        if(StringUtils.isNotEmpty(funcId) && StringUtils.isNotEmpty(funcName)){
            OpuRsFunc orc=opuRsFuncMapper.getModuleOrFuncById(funcId);
            orc.setFuncName(funcName);
            opuRsFuncMapper.updateModuleAndFunc(orc);
        }
    }
    /**
     * 批量保存配置
     * @param dataList
     * @throws Exception
     */
    public void saveDataList(List<OpuFuncWidgetConfig> dataList) throws Exception{
        if(dataList!=null && dataList.size()>0){
            for(OpuFuncWidgetConfig oc:dataList){
                if(StringUtils.isNotEmpty(oc.getId())){
                    opuFuncWidgetConfigMapper.updateByPrimaryKeySelective(oc);
                }else{
                    oc.setId(UUID.randomUUID().toString());
                    opuFuncWidgetConfigMapper.insert(oc);
                }
            }
        }
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        return opuFuncWidgetConfigMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(OpuFuncWidgetConfig record) {
        return opuFuncWidgetConfigMapper.insert(record);
    }

    @Override
    public int insertSelective(OpuFuncWidgetConfig record) {
        return opuFuncWidgetConfigMapper.insertSelective(record);
    }

    @Override
    public OpuFuncWidgetConfig selectByPrimaryKey(String id) {
        return opuFuncWidgetConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OpuFuncWidgetConfig record) {
        return opuFuncWidgetConfigMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(OpuFuncWidgetConfig record) {
        return opuFuncWidgetConfigMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(OpuFuncWidgetConfig record) {
        return opuFuncWidgetConfigMapper.updateByPrimaryKey(record);
    }
}