package com.augurit.agcloud.agcom.agsupport.sc.openMap.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.config.AgToken;
import com.augurit.agcloud.agcom.agsupport.common.util.ProxyUtil;
import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgOpenMapMapper;
import com.augurit.agcloud.agcom.agsupport.sc.openMap.service.IAgOpenMap;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.common.util.Common;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import com.github.pagehelper.PageHelper;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AgOpenMapImpl implements IAgOpenMap {

    static long SECOND = 60*60*24;
    @Autowired
    private AgOpenMapMapper openMapMapper;

    @Autowired
    private AgLayerMapper agLayerMapper;

    @Autowired
    private IAgUser iAgUser;
    @Override
    public List<AgTagLayer> findLayerAndTag(String loginName) throws Exception{
      return  openMapMapper.findLayerAndTag(loginName);
    }

    @Override
    public List<AgOpenMapLayer> findOpenLayersByconditons(String loginName, String tagId, String tabType,String keyWord,String applyStatus, Integer pageNum, Integer pageSize) throws Exception {


        //对过期的申请进行清除
        //1. 根据登录名，获取所有申请
        List<AgOpenMapApplyItem> userAllItems = openMapMapper.findUserApplyItemByLoginName(loginName);
        AgUser user = iAgUser.findUserByName(loginName);
        String userId = user.getId();

        for(AgOpenMapApplyItem item : userAllItems) {
            String applyTimestr =  item.getApplyTime().split("[.]")[0];

            try {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timeStamp = String.valueOf(sdf.parse(applyTimestr).getTime()/1000);

                //获取当前时间时间戳
                long curTimestamp = new Date().getTime() / 1000;
                int valDay = item.getValidityDate();
                if(item.getValidityDate() <= 365) {
                    if (curTimestamp >=  Long.parseLong(timeStamp) + valDay*SECOND) {
                        //进行本地审核数据删除
                        String itemID = item.getId();
                        openMapMapper.delApplyItemById(itemID);
                        openMapMapper.delApplyProcessByItemId(itemID);
//                        openMapMapper.delAuditListByItemId(itemID);
                        //TODO 删除 proxy 代理权限，记录
                        openMapMapper.delApplyProxyService(userId, item.getLayerId());
                    }
                    else {

                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }




         PageHelper.startPage(pageNum,pageSize);
        //获取所有对外开放的图层
        List<AgOpenMapLayer> openLayers = openMapMapper.findOpenLayersByCondition(loginName, tagId, keyWord, tabType, applyStatus);
        //
        //  PageInfo pageResult = new PageInfo(openLayers);

        //1.openLayers 挂载 申请Item
        for(AgOpenMapLayer openLayer : openLayers) {

            //根据图层Id获取applyItemId, 再根据 applyItemId, 获取applyItem
            // AgOpenMapApplyItem applyItem = openMapMapper.findApplyItemByLayerIdAndLoginName(openLayer.getId(), loginName);

            //根据 申请人的用户登录名 查找申请Item
            AgOpenMapApplyItem applyItem = openMapMapper.findApplyItemByLayerIdAndApplicant(openLayer.getId(), loginName);


            if(applyItem != null) {

                //审核通过之后，显示代理地址
                if(applyItem.getCurrentProcessStatus().equals("1")) {
                    AgLayer agLayer = agLayerMapper.findByLayerId(applyItem.getLayerId());
                    String url = agLayer.getUrl();

                    Pattern pattern = Pattern.compile("/");
                    Matcher findMatcher = pattern.matcher(url);
                    int number = 0;
                    while(findMatcher.find()) {
                        number++;
                        if(number == 3){//当“/”第3次出现时停止
                            break;
                        }
                    }
                    int i = findMatcher.start();// “/” 第3次出现的位置
                    String substring = url.substring(i+1, url.length());
                    applyItem.setProxyURL(ProxyUtil.getProxyPreUrl() + substring +"?uuid="+applyItem.getProxyURL());
                }

                //申请Item 绑定 处理记录
                List<AgOpenMapApplyProcess> applyProcess = openMapMapper.findApplyProcessByApplyItemId(applyItem.getId());
                applyItem.setApplyProcess(applyProcess);

                //申请Item 绑定 申请材料
                List<AgOpenMapAttachFile> files = openMapMapper.findApplyFilesByApplyItemId(applyItem.getId());
                applyItem.setApplyFiles(files);

                openLayer.setApplyItem(applyItem);
            }
        }
        return openLayers;

    }

    /*
    * 废弃
    * */
    @Override
    public List<AgOpenMapLayer> findOpenLayers(String loginName,String tagId, String tabType,String keyWord,String applyStatus, Integer pageNum, Integer pageSize) throws Exception {

    // PageHelper.startPage(1,2);
        //获取所有对外开放的图层
        List<AgOpenMapLayer> openLayers = openMapMapper.findOpenLayers(keyWord);
    //
    //  PageInfo pageResult = new PageInfo(openLayers);

        //1.openLayers 挂载 申请Item
        for(AgOpenMapLayer openLayer : openLayers) {

            //根据图层Id获取applyItemId, 再根据 applyItemId, 获取applyItem
           // AgOpenMapApplyItem applyItem = openMapMapper.findApplyItemByLayerIdAndLoginName(openLayer.getId(), loginName);

           //根据 申请人的用户登录名 查找申请Item
            AgOpenMapApplyItem applyItem = openMapMapper.findApplyItemByLayerIdAndApplicant(openLayer.getId(), loginName);
            if(applyItem != null) {
                //申请Item 绑定 处理记录
                List<AgOpenMapApplyProcess> applyProcess = openMapMapper.findApplyProcessByApplyItemId(applyItem.getId());
                applyItem.setApplyProcess(applyProcess);

                //申请Item 绑定 申请材料
                List<AgOpenMapAttachFile> files = openMapMapper.findApplyFilesByApplyItemId(applyItem.getId());
                applyItem.setApplyFiles(files);

                openLayer.setApplyItem(applyItem);
            }
        }

        //2.tabType == 1 时，即我的资源
        List<AgOpenMapLayer> rLayers = new ArrayList<>();
        if("1".equals(tabType)) { //我的资源,说明openLayer 都绑定了 applyItem
            for(AgOpenMapLayer layer : openLayers) {
                if(layer.getApplyItem() != null) {
                    rLayers.add(layer);
                }

            }
        }else {
            rLayers = openLayers;
        }

        //3.根据 审核状态 applyStatus，进行筛选, 当applyStatus = 空时，返回全部
        if(StringUtils.isEmpty(applyStatus)){
            return rLayers;
        }else {
            List<AgOpenMapLayer> rLayers2 = new ArrayList<>();
            if(applyStatus.equals("3")){ //可申请，包括申请失败，从未申请过
                for(AgOpenMapLayer layer2 : rLayers) {
                    if(layer2.getApplyItem() == null){
                        rLayers2.add(layer2);
                    }else if (layer2.getApplyItem() != null && layer2.getApplyItem().getCurrentProcessStatus().equals("0")){
                        rLayers2.add(layer2);
                    }
                }
            }else { //申请失败
                for(AgOpenMapLayer layer2 : rLayers) {
                    if(layer2.getApplyItem() != null && layer2.getApplyItem().getCurrentProcessStatus().equals(applyStatus)){
                        rLayers2.add(layer2);
                    }
                }
            }
            return rLayers2;
        }
    }

    @Override
   public AgOpenMapApplyItem createApplyItemByInfo(String serialNo, String layerId, String applyFor, String secrecy, String obtainWay,
                                                   String obtainWayDesc, int validityDate, String ip, AgCloudUser user) throws Exception {

        //新建一个申请Item
        AgOpenMapApplyItem applyItem = new AgOpenMapApplyItem();
        applyItem.setId(UUID.randomUUID().toString());
        applyItem.setSerialNo(serialNo); //设置申请序列号
        applyItem.setLayerId(layerId);
        applyItem.setApplicant(user.getUserName());
        applyItem.setLoginName(user.getName());
        applyItem.setSecrecy(secrecy);
        applyItem.setIp(ip);
        Date now = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        applyItem.setApplyTime(ft.format(now));
        applyItem.setObtainWay(obtainWay);
        applyItem.setObtainWayDesc(obtainWayDesc);
        applyItem.setValidityDate(validityDate);
        applyItem.setCurrentProcessStatus("0"); //当前处理状态 申请中
        applyItem.setApplyFor(applyFor);

        //用户相关信息
        applyItem.setWorkUnit(user.getOrgName());
        applyItem.setWorkAddress(user.getUserFamilyAddress()); //TODO 暂时用这个地址
        applyItem.setApplicantIDCard(user.getUserIdCardNo());
        applyItem.setApplicantMobile(user.getUserMobile());

        openMapMapper.addApplyItem(applyItem);

        return applyItem;
    }




    @Override
    public void saveApplyFile(AgOpenMapAttachFile fileInfo) throws Exception {
        openMapMapper.saveApplyFile(fileInfo);
    }

    @Override
    public AgOpenMapAttachFile getFileInfo(String id) throws Exception{
        return openMapMapper.getFileInfo(id);
    }

    @Override
     public void createApplyProcess(AgOpenMapApplyProcess applyProcess) throws Exception {
        openMapMapper.addApplyProcess(applyProcess);
     }


    @Override
    public void createAuditList(AgOpenMapAuditList auditList) throws Exception {
        openMapMapper.addAuditList(auditList);
    }

    @Override
    public List<AgOpenMapAuditList> findAuditList(String loginName, String tabType,String serialNo, String applicant, String keyWord, String auditStatus) throws Exception {

        List<AgOpenMapAuditList> auditLists = openMapMapper.findAuditList(loginName,tabType,serialNo, applicant, keyWord, auditStatus);

        //挂载 审核列表 的 applyItem
        for(AgOpenMapAuditList auditList : auditLists) {

            //根据 申请人的用户登录名 查找申请Item
            List<AgOpenMapApplyItem> applyItems = openMapMapper.findApplyItemBySerialNo(auditList.getSerialNo());

            if(applyItems.size() >= 1){ //对于批量是一样

                List<AgOpenMapLayer> infos = new ArrayList<>();

                for( AgOpenMapApplyItem applyItem : applyItems){

                    //审核通过之后，显示代理地址
                    if(applyItem.getCurrentProcessStatus().equals("1")) {
                        applyItem.setProxyURL(ProxyUtil.getProxyPreUrl() + applyItem.getProxyURL());
                    }

                 // AgOpenMapApplyItem applyItem = applyItems.get(0);
                    AgOpenMapLayer layer = openMapMapper.findOpenLayerById(applyItem.getLayerId());
                    List<AgOpenMapApplyProcess> applyProcess = openMapMapper.findApplyProcessByApplyItemId(applyItem.getId());
                    applyItem.setApplyProcess(applyProcess);
                    //申请Item 绑定 申请材料
                    List<AgOpenMapAttachFile> files = openMapMapper.findApplyFilesByApplyItemId(applyItem.getId());
                    applyItem.setApplyFiles(files);

                    layer.setApplyItem(applyItem);
                    infos.add(layer);
                }
                auditList.setApplyResourceInfos(infos);
            }

        }
        return auditLists;
    }

    @Override
    public AgOpenMapApplyItem findApplyItemByLayerIdAndLoginName(String layerId, String loginName) throws Exception {
        return openMapMapper.findApplyItemByLayerIdAndLoginName(layerId, loginName);
    }


    @Override
    public List<AgOpenMapApplyItem> findApplyItemBySerialNo(String serialNo) throws Exception{
        return openMapMapper.findApplyItemBySerialNo(serialNo);
    }


    @Override
    public AgOpenMapAuditList findAuditListBySerialNo(String serialNo) throws Exception{
        return openMapMapper.findAuditListBySerialNo(serialNo);
    }

    @Override
    public void updateAuditList(String id, String auditStatus, String auditor, String opinion) throws Exception {
        openMapMapper.updateAuditList(id, auditStatus, auditor, opinion);
    }

    @Override
    public void updateApplyItem(String id, String currentProcessStatus, String proxyURL,String auditor) throws Exception {
        openMapMapper.updateApplyItem(id, currentProcessStatus, proxyURL, auditor);
    }

    @Override
    public AgOpenMapApplyItem findApplyItemByLayerIdAndApplicant(String layerId, String loginName) throws Exception{
       return openMapMapper.findApplyItemByLayerIdAndApplicant(layerId, loginName);
    }

    @Override
    public void updateApplyItemByOldApplyItem(AgOpenMapApplyItem applyItem) throws Exception{
        openMapMapper.updateApplyItemByOldApplyItem(applyItem);
    }

    @Override
    public void updateAuditListByOldAuditList(AgOpenMapAuditList oldAuditList) throws Exception {
        openMapMapper.updateAuditListByOldAuditList(oldAuditList);
    }

    @Override
    public List<AgOpenMapCartItem> findCartItem(String layerId, String loginName) throws Exception {
       return openMapMapper.findCartItem(layerId, loginName);
    }

    @Override
    public void saveCartItem(AgOpenMapCartItem cartItem) throws Exception {
        openMapMapper.saveCartItem(cartItem);
    }

    @Override
    public void delCartItem(String id) throws Exception {
        openMapMapper.delCartItem(id);
    }

    @Override
    public void createApplyItemAndLayerIdList(String applyItemId, String layerId, String loginName) throws Exception{
        openMapMapper.createApplyItemAndLayerIdList(applyItemId, layerId, loginName);
    }

    @Override
    public void createBatchApplyItemAndLayerIdList(String applyItemId, List<String> layerIds, String loginName) throws Exception {
        openMapMapper.insertApplyItemAndLayerIdListBatch(applyItemId, layerIds, loginName);
    }

    @Override
    public void delCartItemByLayerIdAndLoginName(String layerId, String loginName) throws Exception{
        openMapMapper.delCartItemByLayerIdAndLoginName(layerId, loginName);
    }

    @Override
    public String getBeforeProxyId(String layerId) throws Exception {
       return openMapMapper.getBeforeProxyId(layerId);
    }


}
