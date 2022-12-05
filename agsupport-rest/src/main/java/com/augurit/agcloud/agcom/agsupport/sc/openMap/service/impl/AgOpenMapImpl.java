package com.augurit.agcloud.agcom.agsupport.sc.openMap.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.util.ProxyUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.mapper.AgDirMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgOpenMapMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.impl.AgDirImpl;
import com.augurit.agcloud.agcom.agsupport.sc.openMap.service.IAgOpenMap;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.agcom.agsupport.util.SMSSender;
import com.augurit.agcloud.agcom.agsupport.util.SimpleNumberGenerator;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.common.util.Common;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AgOpenMapImpl implements IAgOpenMap {
    private static final Logger log = Logger.getLogger(AgOpenMapImpl.class);
    private static final String APPLY_SMS_TEMPLATE = "尊敬的{user}用户，您的申请已提交，请关注审核结果短信通知。";
    private static final String AUDIT_SUCCESS_SMS_TEMPLATE = "尊敬的{user}用户，您的申请已通过审核，请登录系统查看服务地址并测试服务可用性。";
    private static final String AUDIT_FAIL_SMS_TEMPLATE = "尊敬的{user}用户，您的申请未通过审核，原因为【{auditOpinion}】，请及时处理。";
    private static final String RESOURCE_CENTER_DIR = "resource_center/";

    @Autowired
    private AgOpenMapMapper openMapMapper;

    @Autowired
    private AgDirMapper agDirMapper;

    @Autowired
    private AgLayerMapper agLayerMapper;

    @Autowired
    private IAgUser iAgUser;

    /*
    * 获取目录树
    * */
    @Override
    public ContentResultForm getDirTreeOfOpenMap(){
        /*说明：这里的产品设计是前端的目录树只显示专题和专题的下一级节点，点击专题下的目录子节点时显示该节点及其所有子节点关联的图层*/
        try {
            List<DirTreeForOpenMap> result =new ArrayList<DirTreeForOpenMap>();
            List<AgDir> listAllDir = agDirMapper.findAll();//所有目录信息
            List<LayerCountByDir> layerContOfDir = openMapMapper.getLayerCountGroupByDir();//统计每个dir下的允许外部访问的图层数量
            List<AgDir> listDirForProject = listAllDir.stream().filter(o -> o.getParentId() != null && o.getParentId().equals("root")).collect(Collectors.toList());//找到所有专题(不包含子节点）
            for (AgDir dirForProject : listDirForProject) {
                DirTreeForOpenMap nodeProject = new DirTreeForOpenMap();
                List<AgDir> dirChildren = listAllDir.stream().filter(o -> o.getParentId() != null && o.getParentId().equals(dirForProject.getId())).collect(Collectors.toList());//找出专题的下一级节点(不包含孙子节点）
                for (AgDir dirChild : dirChildren) {
                    //allChildrenOfDirChildren 是dirChild节点及其所有下级节点的集合
                    List<AgDir> childrenOfDirChildren = listAllDir.stream().filter(o->o.getXpath()!=null && o.getXpath().indexOf(dirChild.getXpath())==0).collect(Collectors.toList());
                    List<String> dirIds = childrenOfDirChildren.stream().map(o->o.getId()).collect(Collectors.toList());
                    List<LayerCountByDir> matchItems = layerContOfDir.stream().filter(o->dirIds.contains(o.getDirId())).collect(Collectors.toList());
                    Integer layerCount = matchItems.stream().mapToInt(LayerCountByDir::getLayerCount).sum();
                    if(layerCount >0 ) {
                        DirTreeForOpenMap nodeChildOfProject = new DirTreeForOpenMap();
                        nodeChildOfProject.setId(dirChild.getId());
                        nodeChildOfProject.setName(dirChild.getName());
                        nodeChildOfProject.setOrder(dirChild.getOrderNm());
                        nodeChildOfProject.setChildrenCount(layerCount);
                        nodeProject.getChildren().add(nodeChildOfProject);
                    }
                }
                nodeProject.setChildrenCount(nodeProject.getChildren().size());
                if(nodeProject.getChildrenCount()>0) {
                    nodeProject.setId(dirForProject.getId());
                    nodeProject.setName(dirForProject.getName());
                    nodeProject.setOrder(dirForProject.getOrderNm());
                    nodeProject.setChildrenCount(nodeProject.getChildren().size());
                    result.add(nodeProject);
                }
            }
            return new ContentResultForm(true,result,"获取资源目录成功");
        }
        catch (Exception e){
            e.printStackTrace();
            return new ContentResultForm(false,new ArrayList(),"获取资源目录失败:"+e.getMessage());
        }
    }

    /*
     * 获取目录树，并根据用户过滤图层
     * */
    @Override
    public ContentResultForm getDirTreeOfOpenMapByUserId(String loginName){
        /*说明：这里的产品设计是前端的目录树只显示专题和专题的下一级节点，点击专题下的目录子节点时显示该节点及其所有子节点关联的图层*/
        try {
            List<DirTreeForOpenMap> result =new ArrayList<DirTreeForOpenMap>();
            List<AgDir> listAllDir = agDirMapper.findAll();//所有目录信息
            List<LayerCountByDir> layerContOfDir = openMapMapper.getUserApplyLayerCountGroupByDir(loginName);//统计每个dir下的用户申请的图层数量
            List<AgDir> listDirForProject = listAllDir.stream().filter(o -> o.getParentId() != null && o.getParentId().equals("root")).collect(Collectors.toList());//找到所有专题(不包含子节点）
            for (AgDir dirForProject : listDirForProject) {
                DirTreeForOpenMap nodeProject = new DirTreeForOpenMap();
                //dirChildren是专题下的直属子节点
                List<AgDir> dirChildren = listAllDir.stream().filter(o -> o.getParentId() != null && o.getParentId().equals(dirForProject.getId())).collect(Collectors.toList());//找出专题的下一级节点(不包含孙子节点）
                for (AgDir dirChild : dirChildren) {
                    //allChildrenOfDirChildren 是dirChild节点及其所有下级节点的集合
                    List<AgDir> childrenOfDirChildren = listAllDir.stream().filter(o->o.getXpath()!=null && o.getXpath().indexOf(dirChild.getXpath())==0).collect(Collectors.toList());
                    List<String> dirIds = childrenOfDirChildren.stream().map(o->o.getId()).collect(Collectors.toList());
                    List<LayerCountByDir> matchItems = layerContOfDir.stream().filter(o->dirIds.contains(o.getDirId())).collect(Collectors.toList());
                    Integer layerCount = matchItems.stream().mapToInt(LayerCountByDir::getLayerCount).sum();
                    if(layerCount > 0) {
                        DirTreeForOpenMap nodeChildOfProject = new DirTreeForOpenMap();
                        nodeChildOfProject.setId(dirChild.getId());
                        nodeChildOfProject.setName(dirChild.getName());
                        nodeChildOfProject.setOrder(dirChild.getOrderNm());
                        nodeChildOfProject.setChildrenCount(layerCount);
                        nodeProject.getChildren().add(nodeChildOfProject);
                    }
                }
                nodeProject.setChildrenCount(nodeProject.getChildren().size());
                if(nodeProject.getChildrenCount()>0) {
                    nodeProject.setId(dirForProject.getId());
                    nodeProject.setName(dirForProject.getName());
                    nodeProject.setOrder(dirForProject.getOrderNm());
                    nodeProject.setChildrenCount(nodeProject.getChildren().size());
                    result.add(nodeProject);
                }
            }
            return new ContentResultForm(true,result,"获取资源目录成功");
        }
        catch (Exception e){
            e.printStackTrace();
            return new ContentResultForm(false,new ArrayList(),"获取资源目录失败:"+e.getMessage());
        }
    }

    @Override
    public  ContentResultForm getLayersOfExternal(String loginName, String dirId, String keyWord , Page page) {
        try {
            if(Common.isCheckNull(dirId)) {
                dirId = "root";
            }
            AgDir dir = agDirMapper.findById(dirId);
            PageHelper.startPage(page.getPageNum(),page.getPageSize());
            List<AgOpenMapLayer> layers = openMapMapper.findListOfExternal(dir.getXpath(), keyWord);//获取目录下所有对外的图层
            List<AgOpenMapLayer> listApply = openMapMapper.findListOfExternalByUser(loginName,null,null,null);//获取当前用户的申请过的图层
            for (AgOpenMapLayer agOpenMapLayer : layers) {
                setLayerPicture(agOpenMapLayer);
                //为图层贴上申请状态标签
                List<AgOpenMapLayer> matchItems = listApply.stream().filter(o->o.getDirLayerId() != null  && o.getDirLayerId().equals(agOpenMapLayer.getDirLayerId())).collect(Collectors.toList());
                if(matchItems!=null && matchItems.size()>0){
                    AgOpenMapLayer matchOpenMapLayer = matchItems.get(0);
                     String status =matchOpenMapLayer .getApplyStatus();
                     agOpenMapLayer.setApplyStatus(status);
                     if(status.equals("1")) {
                         //已审核通过的资源才判断使用是否超时
                         Date now = new Date();
                         Date auditDate = matchOpenMapLayer.getAuditTime();
                         long diff = now.getTime() - auditDate.getTime();
                         long days = diff / (1000 * 60 * 60 * 24);
                         boolean isOverTime = days > Integer.parseInt(matchOpenMapLayer.getValidityDate());//资源是否超时
                         if (isOverTime) {
                             agOpenMapLayer.setApplyStatus(null);//资源超时视为未拥有，可再申请
                         }
                     }
                }
            }
            return new ContentResultForm(true, new PageInfo<AgOpenMapLayer>(layers), "成功获取对外开放的图层");
        }
        catch (Exception e){
            e.printStackTrace();
            return new ContentResultForm(false, null, "获取图层失败");
        }
    }

    @Override
    public  ContentResultForm getLayersOfExternalForUser(String loginName, String dirId, String keyWord , String applyStatus, Page page) {
        try {
            int pageNUm = page.getPageNum();
            int pageSize =page.getPageSize();
            if(Common.isCheckNull(dirId)) {
                dirId = "root";
            }
            AgDir dir = agDirMapper.findById(dirId);
            PageHelper.startPage(page.getPageNum(),page.getPageSize());
            //applyLayersForDistinct 是为了分页获取(暂时没想到带状态下能正确分页获取的方法);applyLayers是为了处理申请状态
            List<AgOpenMapLayer> applyLayersForDistinct = openMapMapper.findListOfExternalByUserWithDistinct(loginName,dir.getXpath(),keyWord,applyStatus);//获取用户申请资源并过滤掉重复的
            List<AgOpenMapLayer> applyLayers = openMapMapper.findListOfExternalByUser(loginName,dir.getXpath(),keyWord,applyStatus);//获取用户申请记录
            List<AgOpenMapLayer> result = new ArrayList<>();
            for(AgOpenMapLayer applyLayer : applyLayersForDistinct){
                setLayerPicture(applyLayer);
                //为图层贴上申请状态标签
                List<AgOpenMapLayer> matchItems = applyLayers.stream().filter(o->o.getDirLayerId() != null  && o.getDirLayerId().equals(applyLayer.getDirLayerId())).collect(Collectors.toList());
                if(matchItems!=null && matchItems.size()>0){
                    AgOpenMapLayer matchOpenMapLayer = matchItems.get(0);
                    String status =matchOpenMapLayer .getApplyStatus();
                    applyLayer.setApplyStatus(status);
                    if(status.equals("1")) {
                        Date now = new Date();
                        Date auditDate = matchOpenMapLayer.getAuditTime();
                        long diff = now.getTime() - auditDate.getTime();
                        long days = diff / (1000 * 60 * 60 * 24);
                        boolean isOverTime = days > Integer.parseInt(matchOpenMapLayer.getValidityDate());//资源是否超时
                        if (isOverTime) {
                            applyLayer.setApplyStatus(null);//资源超时视为未拥有，可再申请
                        }
                    }
                }
                result.add(applyLayer);
            }
            return new ContentResultForm(true, new PageInfo<AgOpenMapLayer>(result), "成功获取对外开放的图层");
        }
        catch (Exception e){
            e.printStackTrace();
            return new ContentResultForm(false, null, "获取图层失败");
        }
    }




    @Override
    public ContentResultForm getDetails(String loginName, String dirLayerId){
        ContentResultForm resultForm = null;
        com.alibaba.fastjson.JSONObject jsonObjectResult = new com.alibaba.fastjson.JSONObject();
        try {
            AgOpenMapLayer agLayer = openMapMapper.findOpenMapLayerByDirLayerId(dirLayerId);
            if(agLayer != null) {
                agLayer.setProxyUrl("");
                setLayerPicture(agLayer);
                jsonObjectResult.put("layerInfo", agLayer);
                List<AgOpenMapApply> listApply = openMapMapper.findApplyByApplicantLoginNameAndDirLayerId(loginName, dirLayerId);
                if (listApply != null&&listApply.size()>0) {
                    AgOpenMapApply apply = listApply.get(0);
                    List<AgOpenMapApplyItem> listApplyItem = openMapMapper.findApplyItemByApplyId(apply.getId());
                    for (AgOpenMapApplyItem applyItem : listApplyItem) {
                        if (dirLayerId.equals(applyItem.getDirLayerId())) {
                            agLayer.setProxyUrl(applyItem.getProxyUrl());
                            break;
                        }
                    }
                    //关联申请进度
                    List<AgOpenMapApplyProcess> applyProcess = openMapMapper.findApplyProcessByApplyId(apply.getId());
                    apply.setApplyProcess(applyProcess);

                    //关联申请附件
                    List<AgOpenMapAttachFile> files = openMapMapper.findApplyAttachFileByApplyId(apply.getId());
                    apply.setApplyFiles(files);
                    jsonObjectResult.put("applyInfo", apply);
                }
                resultForm = new ContentResultForm(true, jsonObjectResult, "成功获取详情");
            }
            else {
                resultForm = new ContentResultForm(false,null,"申请的资源不存在");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            resultForm = new ContentResultForm(false,null,"发生异常:"+e.getMessage());
        }
        return resultForm;
    }

    @Override
    public ResultForm submitApplyTable(AgOpenMapApply applyInfo){
        ResultForm resultForm = null;
        try {
            AgUser user = iAgUser.findUserByName(applyInfo.getApplicantLoginName());
            String dirLayerIds = applyInfo.getDirLayerIds();
            List<String> listApplyDirLayerId = Arrays.asList(dirLayerIds.split(","));
            if(!Common.isCheckNull(dirLayerIds) && !Common.isCheckNull(applyInfo.getApplicantLoginName())) {
                    //先检测用户是否已持有申请的资源
                    if(checkApplyValid(applyInfo.getApplicantLoginName(),listApplyDirLayerId)) {
                        String applyId = SimpleNumberGenerator.generate();
                        applyInfo.setId(applyId);
                        Date now = new Date();
                        applyInfo.setApplyTime(now);
                        applyInfo.setCurrentProcessStatus("0"); //当前处理状态 申请中
                        openMapMapper.addApply(applyInfo);

                        //2.创建申请成功之后, 就要记录相应处理流程
                        List<AgOpenMapApplyProcess> applyProcesses = new ArrayList<>();

                        AgOpenMapApplyProcess applyProcess001 = new AgOpenMapApplyProcess();
                        applyProcess001.setId(UUID.randomUUID().toString());
                        applyProcess001.setApplyId(applyInfo.getId());
                        applyProcess001.setCode("001"); // 设置流程代码：001 - 用户提交申请表，002-等待审核
                        applyProcess001.setName("用户提交申请表");
                        applyProcess001.setProcessTime(applyInfo.getApplyTime());
                        applyProcess001.setSuccess(1);
                        openMapMapper.addApplyProcess(applyProcess001);  //插入提交申请流程记录

                        //添加申请处理进度信息
                        AgOpenMapApplyProcess applyProcess002 = new AgOpenMapApplyProcess();
                        applyProcess002.setId(UUID.randomUUID().toString());
                        applyProcess002.setApplyId(applyInfo.getId());
                        applyProcess002.setCode("002"); // 设置流程代码：001 - 用户提交申请表，002-等待审核
                        applyProcess002.setName("等待审核");
                        Date now2 = new Date();
                        applyProcess002.setProcessTime(now2);
                        applyProcess002.setSuccess(1);
                        openMapMapper.addApplyProcess(applyProcess002); //插入等待审核流程记录
                        //保存申请资源明细
                        for (String dirLayerId : listApplyDirLayerId) {
                            if (!Common.isCheckNull(dirLayerId)) {
                                AgOpenMapApplyItem applyItem = new AgOpenMapApplyItem();
                                applyItem.setId(UUID.randomUUID().toString());
                                applyItem.setApplyId(applyId);
                                applyItem.setDirLayerId(dirLayerId);
                                openMapMapper.addApplyItem(applyItem);
                                openMapMapper.delCartItemByDirLayerIdAndLoginName(dirLayerId, applyInfo.getApplicantLoginName());
                            } else {
                                resultForm = new ResultForm(false, "申请的资源不存在");
                                break;
                            }
                        }
                        //保存附件
                        saveFile(applyInfo);
                        //下发服务申请通知短信
                        String sms = APPLY_SMS_TEMPLATE.replace("{user}", user.getUserName());
                        SMSSender.sendSms(applyInfo.getApplicantMobile(), sms);
                        resultForm = new ResultForm(true, "提交申请成功，请等待审核");
                    }
                    else {
                        resultForm = new ResultForm(false, "不能重复申请已拥有的资源");
                    }
            }
            else {
                resultForm = new ResultForm(false, "提交的信息有误");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("提交申请失败", e);
            resultForm =new ResultForm(false,"发生异常:"+e.getMessage());
        }
        return resultForm;
    }

    @Override
    public ResultForm repeatSubmitApplyTable(AgOpenMapApply applyInfo){
        ResultForm resultForm = null;
        try {
            AgUser user = iAgUser.findUserByName(applyInfo.getApplicantLoginName());
            String dirLayerIds = applyInfo.getDirLayerIds();
            List<String> listApplyDirLayerId = Arrays.asList(dirLayerIds.split(","));
            if(!Common.isCheckNull(dirLayerIds) && !Common.isCheckNull(applyInfo.getApplicantLoginName())) {
                //先检测用户是否已持有申请的资源
                if(checkApplyValid(applyInfo.getApplicantLoginName(),listApplyDirLayerId)) {
                    List<AgOpenMapApply> listApply = openMapMapper.findApplyByApplicantLoginNameAndDirLayerId(listApplyDirLayerId.get(0), applyInfo.getApplicantLoginName());
                    if(listApply.size()>0) {
                        AgOpenMapApply agOpenMapApply = listApply.get(0);
                        //CurrentProcessStatus=='2'是驳回的申请记录
                        if (agOpenMapApply != null && agOpenMapApply.getCurrentProcessStatus().equals("2")) {
                            String applyId = agOpenMapApply.getId();
                            Date now = new Date();
                            agOpenMapApply.setApplyTime(now);
                            agOpenMapApply.setCurrentProcessStatus("0"); //当前处理状态 申请中
                            openMapMapper.updateApplyForRepeatApply(agOpenMapApply);

                            //2.创建申请成功之后, 就要记录相应处理流程
                            List<AgOpenMapApplyProcess> applyProcesses = new ArrayList<>();

                            AgOpenMapApplyProcess applyProcess001 = new AgOpenMapApplyProcess();
                            applyProcess001.setId(UUID.randomUUID().toString());
                            applyProcess001.setApplyId(applyId);
                            applyProcess001.setCode("001"); // 设置流程代码：001 - 用户提交申请表，002-等待审核
                            applyProcess001.setName("用户提交申请表");
                            applyProcess001.setProcessTime(agOpenMapApply.getApplyTime());
                            applyProcess001.setSuccess(1);
                            openMapMapper.addApplyProcess(applyProcess001);  //插入提交申请流程记录

                            //添加申请处理进度信息
                            AgOpenMapApplyProcess applyProcess002 = new AgOpenMapApplyProcess();
                            applyProcess002.setId(UUID.randomUUID().toString());
                            applyProcess002.setApplyId(applyId);
                            applyProcess002.setCode("002"); // 设置流程代码：001 - 用户提交申请表，002-等待审核
                            applyProcess002.setName("等待审核");
                            Date now2 = new Date();
                            applyProcess002.setProcessTime(now2);
                            applyProcess002.setSuccess(1);
                            openMapMapper.addApplyProcess(applyProcess002); //插入等待审核流程记录
                            // 先删除之前的ApplyItem，再添加重新申请的ApplyItem
                            openMapMapper.delApplyItemByApplyIdRest(applyId);
                            for (String dirLayerId : listApplyDirLayerId) {
                                AgOpenMapApplyItem applyItem = new AgOpenMapApplyItem();
                                applyItem.setId(UUID.randomUUID().toString());
                                applyItem.setApplyId(applyId);
                                applyItem.setDirLayerId(dirLayerId);
                                openMapMapper.addApplyItem(applyItem);

                            }
                            //保存附件
                            saveFile(applyInfo);
                            //下发服务申请通知短信
                            String sms = APPLY_SMS_TEMPLATE.replace("{user}", user.getUserName());
                            SMSSender.sendSms(applyInfo.getApplicantMobile(), sms);
                            resultForm = new ResultForm(true, "提交申请成功，请等待审核");
                        } else {
                            resultForm = new ResultForm(true, "提交的申请不存在");
                        }
                    }else{
                        resultForm = new ResultForm(true, "提交的申请不存在");
                    }
                }
                else {
                    resultForm = new ResultForm(false, "不能重复申请已拥有的资源");
                }
            }
            else {
                resultForm = new ResultForm(false, "提交的信息有误");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("提交申请失败", e);
            resultForm =new ResultForm(false,"发生异常:"+e.getMessage());
        }
        return resultForm;
    }

   public void saveFile(AgOpenMapApply applyInfo) throws Exception{
       //保存附件
       String applyId = applyInfo.getId();
       openMapMapper.deleteAttachfileByApplyId(applyId);
       for(AgOpenMapAttachFile attachFile:applyInfo.getApplyFiles()){
           String fileSaveDir = UploadUtil.getUploadAbsolutePath()+RESOURCE_CENTER_DIR+applyId+ "/";
           attachFile.setId(UUID.randomUUID().toString());
           attachFile.setApplyId(applyId);
           attachFile.setFilePath(UploadUtil.getUploadRelativePath()+RESOURCE_CENTER_DIR+applyId+ "/"+attachFile.getFile().getOriginalFilename());
           openMapMapper.saveApplyFile(attachFile);
           UploadUtil.saveUploadFile(attachFile.getFile(),fileSaveDir,true);
       }
   }

   public boolean checkApplyValid(String loginName,List<String> listDirLayerId) throws Exception{
       List<AgOpenMapLayer> listUserHold = openMapMapper.findValidListOfExternalByUser(loginName);
       List<String> listHoldDirLayerId = listUserHold.stream().map(o->o.getDirLayerId()).collect(Collectors.toList());
       List<String> listMatchDirLayerId = listDirLayerId.stream().filter(o->listHoldDirLayerId.contains(o)).collect(Collectors.toList());
       return listMatchDirLayerId.size()==0;
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
    public ContentResultForm getAuditList(String loginName,  String keyWord, String auditStatus,Page page) {
        try {
            PageHelper.startPage(page.getPageNum(), page.getPageSize());
            //1. 根据申请人用户名获取审核列表
            List<AgOpenMapAuditList> auditList = openMapMapper.findAuditListRest(loginName, keyWord, auditStatus);
            List<String> applyIds = auditList.stream().map(o->o.getId()).collect(Collectors.toList());
            List<AgOpenMapAttachFile> attachFiles = null;
            if(applyIds!=null && applyIds.size()>0) {
                attachFiles = openMapMapper.findApplyAttachFileByApplyIds(applyIds);
            }
            else {
                attachFiles = new ArrayList<AgOpenMapAttachFile>();
            }
            for(AgOpenMapAuditList agOpenMapAudit : auditList){
                String id = agOpenMapAudit.getId();
                List<String> listLayerName = openMapMapper.findApplyLayerNameByApplyId(id);
                String layerNames = "";
                List<AgOpenMapAttachFile> matchAttachFiles = attachFiles.stream().filter(o->o.getApplyId() != null && o.getApplyId().equals(id)).collect(Collectors.toList());
                for(String name : listLayerName){
                    layerNames += name+" ";
                }
                agOpenMapAudit.setLayerNames(layerNames);
                agOpenMapAudit.setApplyFiles(matchAttachFiles);
            }
            return new ContentResultForm(true, new PageInfo(auditList), "获取审核列表成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取审核列表失败", e);
            return new ContentResultForm(false, "发生异常:"+e.getMessage());
        }
    }

    @Override
    public ResultForm auditApply(String loginName,String applyId,String auditResult,String auditOpinion){
        ResultForm resultForm = null;
        String warn ="";
        try {
            if(!Common.isCheckNull(applyId) && !Common.isCheckNull(auditResult) && !Common.isCheckNull(auditOpinion)) {
                AgUser user = iAgUser.findUserByName(loginName);
                AgOpenMapApply agOpenMapApply = openMapMapper.findApplyById(applyId);
                if(!Common.isCheckNull(agOpenMapApply)) {
                    List<AgOpenMapApplyItem> listApplyItem = openMapMapper.findApplyItemByApplyId(applyId);
                    //auditResult==1 代表审核通过,CurrentProcessStatus=="0"代码记录处于待审核状态
                    if (auditResult.equals("1")&&"0".equals(agOpenMapApply.getCurrentProcessStatus())) {
                        //遍历申请资源进行授权
                        for (AgOpenMapApplyItem applyItem : listApplyItem){
                            try {
                                saveProxyUrl(user.getLoginName(),user.getPassword(), agOpenMapApply,applyItem);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                                warn+= "dirLayerId:"+applyItem.getDirLayerId()+"-"+ e.getMessage();
                            }

                        }

                        //授权成功更新申请记录的审核状态
                        agOpenMapApply.setAuditorLoginName(user.getLoginName());
                        agOpenMapApply.setAuditorUserName(user.getUserName());
                        agOpenMapApply.setAuditTime(new Date());
                        agOpenMapApply.setAuditOpinion(auditOpinion);
                        agOpenMapApply.setCurrentProcessStatus(auditResult);
                        openMapMapper.updateApplyForAudit(agOpenMapApply);

                        //写入申请进度记录
                        AgOpenMapApplyProcess applyProcess003 = new AgOpenMapApplyProcess();
                        applyProcess003.setId(UUID.randomUUID().toString());
                        applyProcess003.setApplyId(applyId);
                        applyProcess003.setCode("003"); // 设置流程代码：001 - 用户提交申请表，002-等待审核 003-审核完成
                        String result = "审核人通过申请";
                        applyProcess003.setName(result); // 设置流程名称
                        applyProcess003.setHandle(auditOpinion); //处理意见
                        Date now = new Date();
                        applyProcess003.setProcessTime(now);
                        applyProcess003.setSuccess(1);
                        openMapMapper.addApplyProcess(applyProcess003);

                        // 4.下发服务申请通知短信
                        String sms = AUDIT_SUCCESS_SMS_TEMPLATE.replace("{user}", agOpenMapApply.getApplicant());
                        SMSSender.sendSms(agOpenMapApply.getApplicantMobile(), sms);
                    } else if(auditResult.equals("2")&&"0".equals(agOpenMapApply.getCurrentProcessStatus())){
                        //状态是待审核，审核结果为驳回的申请，遍历申请资源清空代理地址
                        for (AgOpenMapApplyItem applyItem : listApplyItem){
                            applyItem.setProxyUrl("");
                            openMapMapper.updateApplyItemRest(applyItem);
                        }
                        //更新申请记录的审核状态
                        agOpenMapApply.setAuditorLoginName(user.getLoginName());
                        agOpenMapApply.setAuditorUserName(user.getUserName());
                        agOpenMapApply.setAuditTime(new Date());
                        agOpenMapApply.setAuditOpinion(auditOpinion);
                        agOpenMapApply.setCurrentProcessStatus(auditResult);
                        openMapMapper.updateApplyForAudit(agOpenMapApply);

                        //写入申请进度记录
                        AgOpenMapApplyProcess applyProcess003 = new AgOpenMapApplyProcess();
                        applyProcess003.setId(UUID.randomUUID().toString());
                        applyProcess003.setApplyId(applyId);
                        applyProcess003.setCode("003"); // 设置流程代码：001 - 用户提交申请表，002-等待审核 003-审核完成
                        String result = "审核人驳回申请";
                        applyProcess003.setName(result); // 设置流程名称
                        applyProcess003.setHandle(auditOpinion); //处理意见
                        Date now = new Date();
                        applyProcess003.setProcessTime(now);
                        applyProcess003.setSuccess(1);
                        openMapMapper.addApplyProcess(applyProcess003);

                        // 4.下发服务申请通知短信
                        String sms = AUDIT_FAIL_SMS_TEMPLATE.replace("{user}", agOpenMapApply.getApplicant());
                        SMSSender.sendSms(agOpenMapApply.getApplicantMobile(), sms);
                    }
                    resultForm = new ResultForm(true, "审核成功。"+warn);
                }
                else{
                    resultForm = new ResultForm(false,"找不到申请记录");
                }
            }
            else {
                resultForm = new ResultForm(false,"请求参数错误");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            resultForm = new ResultForm(false,"发生异常:"+e.getMessage());
        }
        return  resultForm;
    }

    private void saveProxyUrl(String loginName,String password,AgOpenMapApply apply,AgOpenMapApplyItem applyItem) throws Exception{
        //--------调用外部审核对代理地址，进行授权
        String proxUrl = ProxyUtil.getProxyPreUrl() + "service/serviceApply";//申请授权接口地址
        //String proxUrl ="http://192.168.30.210:8091/proxy/service/serviceApply";//申请授权接口地址
        //2.调proxy 申请接口
        Map<String, String> param2 = new HashMap<>();

        param2.put("loginName", loginName);//审核人账号
        param2.put("password", password);//审核人密码
        param2.put("applicantLoginName", apply.getApplicantLoginName());//申请人账号
        param2.put("serviceId", applyItem.getDirLayerId());
        param2.put("ip", apply.getIp());

        //发送proxy审核申请
        HttpRespons httpRespons2 = new HttpRequester().sendPost(proxUrl, param2);
        JSONObject jsonObject2 = JSONObject.fromObject(httpRespons2.getContent());
        Map map2 = com.common.util.JsonUtils.toBean(jsonObject2.toString(), HashMap.class);
        String uuid = map2.get("uuid").toString();//授权后允许访问资源的地址

        AgLayer agLayer = agLayerMapper.findByDirLayerId(applyItem.getDirLayerId());
        String layerUrl = agLayer.getUrl();

        Pattern pattern = Pattern.compile("/");
        Matcher findMatcher = pattern.matcher(layerUrl);
        int number = 0;
        while(findMatcher.find()) {
            number++;
            if(number == 3){//当“/”第3次出现时停止
                break;
            }
        }
        int i = findMatcher.start();// “/” 第3次出现的位置
        String substring = layerUrl.substring(i+1, layerUrl.length());
        String proxUrlAfterGrant = ProxyUtil.getProxyPreUrl() + substring +"?f=jsapi&uuid="+uuid;
        applyItem.setProxyUrl(proxUrlAfterGrant);
        openMapMapper.updateApplyItemRest(applyItem);
    }







    @Override
    public ContentResultForm findCartItemByLoginName(String loginName)  {
        try {
            List<AgOpenMapCartItem> list = openMapMapper.findCartItemByLoginName(loginName);
            List<AgOpenMapLayer> listLayerOfExternal = openMapMapper.findListOfExternal(null,null);
            for(AgOpenMapCartItem item : list){
                item.setLoginName(null);
                String dirLayerId = item.getDirLayerId();
                List<AgOpenMapLayer> matchList = listLayerOfExternal.stream().filter(o->dirLayerId.equals(o.getDirLayerId())).collect(Collectors.toList());
                if(matchList!=null&&matchList.size()>0){
                    item.setName(matchList.get(0).getName());
                }
            }
            return new ContentResultForm(true,list,"获取申请列表成功");
        }
        catch (Exception e){
            e.printStackTrace();
            return new ContentResultForm(false,"获取申请列表失败");
        }
    }


    @Override
    public ResultForm saveCartItem(AgOpenMapCartItem cartItem){
        try {
            List<AgOpenMapCartItem> cartItems = openMapMapper.findCartItem(cartItem.getDirLayerId(), cartItem.getLoginName());
            if (CollectionUtils.isNotEmpty(cartItems)) {
                return new ResultForm(false, "该资源已在申请列表中");
            }
            cartItem.setId(UUID.randomUUID().toString());
            openMapMapper.saveCartItem(cartItem);
            return new ResultForm(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "发生异常:"+e.getMessage());
        }
    }

    @Override
    public ResultForm delCartItem(String id) {
        ResultForm resultForm = null;
        try {
            openMapMapper.delCartItem(id);
            resultForm = new ResultForm(true,"删除成功");
        }
        catch (Exception e){
            e.printStackTrace();
            resultForm = new ResultForm(false,"发生异常:"+e.getMessage());
        }
        return resultForm;
    }

    private void setLayerPicture(AgOpenMapLayer agOpenMapLayer){
        String data = agOpenMapLayer.getData();
        //处理图片路径
        if(!Common.isCheckNull(data)) {
            JSONObject dataObj = JSONObject.fromObject(data);
            String picture = (String) dataObj.remove("picture");
            picture = UploadUtil.getUploadRelativePath() + AgDirImpl.getLayerPictureDir() + picture;
            dataObj.put("picture", picture);
            agOpenMapLayer.setData(dataObj.toString());
        }

    }


}
