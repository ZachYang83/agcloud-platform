package com.augurit.agcloud.agcom.agsupport.sc.openMap.controller;

import com.augurit.agcloud.agcom.agsupport.common.util.ProxyUtil;
import com.augurit.agcloud.agcom.agsupport.util.SMSSender;
import com.augurit.agcloud.agcom.agsupport.util.SimpleNumberGenerator;
import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.mapper.AgOpenMapMapper;
import com.augurit.agcloud.agcom.agsupport.sc.openMap.service.IAgOpenMap;
import com.augurit.agcloud.agcom.agsupport.sc.service.controlller.AgServiceController;
import com.augurit.agcloud.agcom.agsupport.sc.service.service.IAgService;
import com.augurit.agcloud.agcom.agsupport.sc.tag.controller.AgTagController;
import com.augurit.agcloud.agcom.agsupport.sc.tag.service.IAgTag;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.common.util.Common;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import freemarker.template.Configuration;
import freemarker.template.Template;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.augurit.agcloud.agcom.agsupport.util.SMSSender.*;

@RestController
@RequestMapping("agsupport/openMap")
public class OpenMapController {

    private static final Logger log = Logger.getLogger(AgTagController.class);
    private static final String APPLY_SMS_TEMPLATE = "尊敬的{user}用户，您的申请已提交，请关注审核结果短信通知。";
    private static final String AUDIT_SUCCESS_SMS_TEMPLATE = "尊敬的{user}用户，您的申请已通过审核，请登录系统查看服务地址并测试服务可用性。";
    private static final String AUDIT_FAIL_SMS_TEMPLATE = "尊敬的{user}用户，您的申请未通过审核，原因为【{auditOpinion}】，请及时处理。";


    @Autowired
    private IAgTag iAgTag;

    @Autowired
    private IAgOpenMap iAgOpenMap;

    @Autowired
    private AgOpenMapMapper openMapMapper;

    @Autowired
    private IAgService iAgService;

    @Value(value = "${upload.filePath}")
    private String filePath;

    @Autowired
    private Configuration configuration;

    @Autowired
    private IAgUser iAgUser;

    /*
     * 根据 筛选条件，获取标签目录
     *
     */
    @RequestMapping("layerTagTree")
    public String getLayerTagTreeByCondition(String loginName, String tabType) { //String keyWord, String applyStatus

        List<Map> result = new ArrayList<>();
        try {
            List<AgTagCatalog> tagCatalogs = iAgTag.getAllTagCatalogs(); //获取所有标签目录
            List<AgTag> tags = iAgTag.getAllTags();                     //获取所有标签
            loginName = tabType.equals("1") ? loginName: "";
            List<AgTagLayer> tagLayers = iAgOpenMap.findLayerAndTag(loginName);  //获取所有标签图层
            List<Map<String, Object>> array = JSONArray.fromObject(JsonUtils.toJson(tagCatalogs));
            for (Map<String, Object> tagCatalog : array) {
                List<Map> tagArr = new ArrayList<>();
                for (AgTag tag : tags) {
                    if (tag.getCatalogId().equals(tagCatalog.get("id"))) {//标签 挂到 标签目录下

                        for (AgTagLayer tagLayer : tagLayers) {  //标签图层 挂到 标签下
                            boolean isMatch = tagLayer.getTagId().equals(tag.getId().toString());
                            if (isMatch) {
                                int increase = 1;
                                tag.setLayerNum(tag.getLayerNum() + increase);
                            }
                        }
                        if(tag.getLayerNum() > 0) {
                            tagArr.add(JSONObject.fromObject(tag));
                        }
                    }
                }
                tagCatalog.put("tags", tagArr);
                if(tagArr.size() > 0) result.add(tagCatalog);

            }
        } catch (Exception e) {
            log.error("获取标签目录失败", e);
            return JsonUtils.toJson(new ResultForm(false, "获取标签目录失败"));
        }
        return JsonUtils.toJson(new ContentResultForm<>(true, result, "获取标签目录成功"));
    }

    /**
     * 获取对外开放图层
     *
     * @param loginName   用户登录名
     * @param tabType     选项卡类型 0:全部资源，1：我的资源(即进入了申请流程的资源)，这个接口只能填0，1。（2：我的审核，3：平台审核）,必填
     * @param tagId       标签ID,
     * @param keyWord     与关键字相匹配的图层
     * @param applyStatus 申请状态, 空：全部，0：申请中，1：已拥有，2：驳回，3：可申请
     * @return
     * @throws Exception
     */
    @RequestMapping("/openMapLayers")
    public String list(String loginName, String tabType, String keyWord, String applyStatus, String tagId, Integer pageNum, Integer pageSize) {
        try {
            List<AgOpenMapLayer> openLayers = iAgOpenMap.findOpenLayersByconditons(loginName, tagId, tabType, keyWord, applyStatus, pageNum, pageSize);

            return JsonUtils.toJson(new ContentResultForm(true, new PageInfo(openLayers)));
        } catch (Exception e) {
            log.error("获取服务图层失败", e);
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false, "获取服务图层失败"));
    }


    /**
     * 申请人提交申请表: 包括 单个 和 批量
     *
     * @param obtainWay 服务获取方式
     * @return
     * @throws Exception
     */
    @RequestMapping("submitApplyTable")
    public String submitApplyTable(String layerId, String layerName, String applyFor, String secrecy,
                                   String obtainWay, String obtainWayDesc, String ip,int validityDate, AgCloudUser user) {

        if (StringUtils.isEmpty(layerId)) {
            return JSONObject.fromObject(new ResultForm(false, "未获取到服务资源")).toString();
        }
        try {

            String[] layerIdArr = layerId.split(",");
            //TODO 单个申请
            if (layerIdArr.length == 1) {
                layerId = layerIdArr[0];
                AgOpenMapApplyItem applyItem = iAgOpenMap.findApplyItemByLayerIdAndApplicant(layerId, user.getName()); //name为loginName

            if(applyItem != null) {
                boolean isRepeatApply = false;
                if (applyItem.getCurrentProcessStatus().equals("0") || applyItem.getCurrentProcessStatus().equals("1")) { //
                    isRepeatApply = true;
                    return JsonUtils.toJson(new ResultForm(false, "提交申请失败,列表中有已申请过的服务了"));
                }
            }
                boolean isRepeat = false; //是否重复申请
                if (applyItem != null) {
                    if(applyItem.getCurrentProcessStatus().equals("2")) { //驳回之后，可以再申请
                        applyItem.setApplyTime(applyItem.getApplyTime().split("[.]")[0]);
                        applyItem.setCompleted("1");
                        iAgOpenMap.updateApplyItemByOldApplyItem(applyItem);
                    }else {
                        isRepeat = true;
                    }
                }
                if (!isRepeat) { //首次申请
                    //1.为用户创建申请Item,并插入数据库
                    applyItem = iAgOpenMap.createApplyItemByInfo(SimpleNumberGenerator.generate(),layerId, applyFor, secrecy, obtainWay, obtainWayDesc, validityDate, ip, user);
//                    applyItem.setSerialNo(SimpleNumberGenerator.generate()); //设置申请序列号

                    //2.创建申请Item成功之后, 就要记录相应处理流程
                    List<AgOpenMapApplyProcess> applyProcesses = new ArrayList<>();

                    AgOpenMapApplyProcess applyProcess = new AgOpenMapApplyProcess();
                    applyProcess.setId(UUID.randomUUID().toString());
                    applyProcess.setApplyItemId(applyItem.getId());
                    applyProcess.setCode("001"); // 设置流程代码：001 - 用户提交申请表，002-等待审核
                    applyProcess.setName("用户提交申请表");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                    applyProcess.setProcessTime(dateFormat.parse(applyItem.getApplyTime()));
                    applyProcess.setSuccess(1);
                    iAgOpenMap.createApplyProcess(applyProcess);  //插入一条流程记录
                    applyProcesses.add(applyProcess);

                    //----
                    AgOpenMapApplyProcess applyProcess2 = new AgOpenMapApplyProcess();
                    applyProcess2.setId(UUID.randomUUID().toString());
                    applyProcess2.setApplyItemId(applyItem.getId());
                    applyProcess2.setCode("002"); // 设置流程代码：001 - 用户提交申请表，002-等待审核
                    applyProcess2.setName("等待审核");
                    Date now2 = new Date();
                    SimpleDateFormat ft2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    applyProcess2.setProcessTime(now2);
                    applyProcess2.setSuccess(1);

                    iAgOpenMap.createApplyProcess(applyProcess2); //插入一条流程记录
                    applyProcesses.add(applyProcess2);

                    applyItem.setApplyProcess(applyProcesses);
                    applyItem.setCurrentProcessStatus("0"); //设置申请item为申请中

                    //----
                    //3. 申请成功之后，就要新加一条 审核记录
                    AgOpenMapAuditList auditList = new AgOpenMapAuditList();
                    auditList.setId(UUID.randomUUID().toString());
                    auditList.setApplicant(applyItem.getApplicant());
                    auditList.setSerialNo(applyItem.getSerialNo());
                    auditList.setApplyTime(applyItem.getApplyTime());
                    auditList.setLayerName(layerName);

                    iAgOpenMap.createAuditList(auditList);

                    //4. 记入一条关联信息：applyItemId 一 layerId
                    //   iAgOpenMap.createApplyItemAndLayerIdList(applyItem.getId(), layerId, applyItem.getLoginName());
                }

                // 下发服务申请通知短信
                String sms = APPLY_SMS_TEMPLATE.replace("{user}", user.getUserName());
                sendSms(user.getUserMobile(), sms);

                List<AgOpenMapApplyItem> appis = new ArrayList<>();
                appis.add(applyItem);

                return JsonUtils.toJson(new ContentResultForm(true, appis, "提交申请成功，请等待审核"));
            } else { // TODO 批量申请

                //批量申请，有一个统一的申请流水号
                String sameSerialNo = SimpleNumberGenerator.generate(); //设置申请序列号
                Date now = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String sameApplyTime = ft.format(now);

                Date later = new Date();
                String sameTime = ft.format(later);

                List<AgOpenMapApplyItem> applyItems = new ArrayList<>();

                boolean isRepeatApply = false;
                for (String layerIdString : layerIdArr) {
                    AgOpenMapApplyItem applyItem = iAgOpenMap.findApplyItemByLayerIdAndApplicant(layerIdString, user.getName());
                    if (applyItem != null) {
                        if(applyItem.getCurrentProcessStatus().equals("0") || applyItem.getCurrentProcessStatus().equals("1")) { //
                            isRepeatApply = true;
                            return JsonUtils.toJson(new ResultForm(false, "提交申请失败,列表中有已申请过的服务了"));
                        }
                    }
                }

                for (String layerIdString : layerIdArr) {

                    AgOpenMapApplyItem applyItem = iAgOpenMap.findApplyItemByLayerIdAndApplicant(layerIdString, user.getLoginName());

                    boolean isRepeat = false;

                    if (applyItem != null) {
                        if(applyItem.getCurrentProcessStatus().equals("2")) { //让之前驳回的失败
                            applyItem.setApplyTime(applyItem.getApplyTime().split("[.]")[0]);
                            applyItem.setCompleted("1");
                            iAgOpenMap.updateApplyItemByOldApplyItem(applyItem);
                        }else {
                            isRepeat = true;
                        }
                    }

                    if(!isRepeat) {
                        //1.为用户创建申请Item
                        applyItem = iAgOpenMap.createApplyItemByInfo(sameSerialNo, layerIdString, applyFor, secrecy, obtainWay, obtainWayDesc, validityDate,ip, user);

                        applyItem.setSerialNo(sameSerialNo);
                        applyItem.setApplyTime(sameApplyTime);

                        //2.创建申请Item成功之后, 就要记录相应处理流程
                        List<AgOpenMapApplyProcess> applyProcesses = new ArrayList<>();

                        AgOpenMapApplyProcess applyProcess = new AgOpenMapApplyProcess();
                        applyProcess.setId(UUID.randomUUID().toString());
                        applyProcess.setApplyItemId(applyItem.getId());
                        applyProcess.setCode("001"); // 设置流程代码：001 - 用户提交申请表，002-等待审核
                        applyProcess.setName("用户提交申请表");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                        applyProcess.setProcessTime(dateFormat.parse(applyItem.getApplyTime()));
                        applyProcess.setSuccess(1);
                        iAgOpenMap.createApplyProcess(applyProcess);
                        applyProcesses.add(applyProcess);

                        //----
                        AgOpenMapApplyProcess applyProcess2 = new AgOpenMapApplyProcess();
                        applyProcess2.setId(UUID.randomUUID().toString());
                        applyProcess2.setApplyItemId(applyItem.getId());
                        applyProcess2.setCode("002"); // 设置流程代码：001 - 用户提交申请表，002-等待审核
                        applyProcess2.setName("等待审核");
                        applyProcess2.setProcessTime(later);
                        applyProcess2.setSuccess(1);

                        iAgOpenMap.createApplyProcess(applyProcess2);
                        applyProcesses.add(applyProcess2);

                        applyItem.setApplyProcess(applyProcesses);
                        applyItem.setCurrentProcessStatus("0"); //申请中
                    }

                    applyItems.add(applyItem);

                    iAgOpenMap.delCartItemByLayerIdAndLoginName(layerIdString, applyItem.getLoginName());
                }

                //3. 申请成功之后，就要新加一条 审核记录
                AgOpenMapAuditList auditList = new AgOpenMapAuditList();
                auditList.setId(UUID.randomUUID().toString());
                auditList.setApplicant(user.getUserName());
                auditList.setSerialNo(sameSerialNo);
                auditList.setApplyTime(sameApplyTime);
                auditList.setLayerName(layerName); //设置图层名称


                iAgOpenMap.createAuditList(auditList);



                // 下发服务申请通知短信
                String sms = APPLY_SMS_TEMPLATE.replace("{user}", user.getUserName());
                sendSms(user.getUserMobile(), sms);
                return JsonUtils.toJson(new ContentResultForm(true, applyItems,"提交申请成功，请等待审核" ));

            }


        } catch (Exception e) {
            log.error("提交申请失败", e);
        }

        return JsonUtils.toJson(new ResultForm(false, "提交申请失败"));
    }



    /*
     * 获取审核列表
     * @param
     */
    @RequestMapping("auditList")
    public String getAuditList(String loginName, String tabType, String serialNo, String applicant, String keyWord, String auditStatus, Integer pageNum, Integer pageSize) {

        Page<AgOpenMapAuditList> page = new Page();
        if (null != pageNum && null != pageSize) {
            page = new Page(pageNum, pageSize);

            PageHelper.startPage(pageNum, pageSize);
        }

        try { //用户有审核权限时
            AgUser user = iAgUser.findUserByName(loginName);
            String userName = user.getUserName();
            // AgUser user = iAgUser.findUserByName(loginName);

            //1. 根据申请人用户名获取审核列表
            List<AgOpenMapAuditList> auditLists = iAgOpenMap.findAuditList(userName, tabType, serialNo, applicant, keyWord, auditStatus);

            //对于没审核过的，动态设置审核人，
            for (AgOpenMapAuditList auditList : auditLists) {
                //如果审核人为空，即谁有权审核，谁就可以成为审核人
                if (StringUtils.isEmpty(auditList.getAuditor())) {
                    auditList.setAuditor(userName);
                }
                //如果审核状态为空，设置为审核中
                if (StringUtils.isEmpty(auditList.getAuditStatus())) {
                    auditList.setAuditStatus("0");
                }
            }

            return JsonUtils.toJson(new ContentResultForm(true, new PageInfo(auditLists), "获取审核列表成功"));
        } catch (Exception e) {
            log.error("获取审核列表失败", e);
        }
        return JsonUtils.toJson(new ResultForm(false, "获取审核列表失败"));
    }


    /*
     * 审核申请
     * @param serialNo 申请序列号
     * @param auditOpinion 审核意见
     * @auditResult 审核结果: 0:待审核 1：通过，2：不通过
     */
    @RequestMapping("auditApply")
    public String auditApply(String loginName, String pwd, String serialNo, String auditResult, String auditOpinion) {

        try {
            //没填审核意见，不允许审核
            if (StringUtils.isEmpty(auditOpinion)) {
                return JsonUtils.toJson(new ResultForm(false, "审核操作失败，请填写审核意见"));
            }

            //根据 SerialNo 查找 审核List
            AgOpenMapAuditList auditList = iAgOpenMap.findAuditListBySerialNo(serialNo);

            //根据 SerialNo 查找 申请Item
            List<AgOpenMapApplyItem> applyItems = iAgOpenMap.findApplyItemBySerialNo(serialNo);

            //获取用户名
           /* Map<String, String> param = new HashMap<>();
            param.put("userName", loginName);
            HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/om/getOpuOmUserInfoByUserId.do", param);
            JSONObject jsonObject = JSONObject.fromObject(httpRespons.getContent());
            Map map = com.common.util.JsonUtils.toBean(jsonObject.toString(), HashMap.class);*/
            AgUser user = iAgUser.findUserByName(loginName);
            String userId = user.getId();
            String userName = user.getUserName();
            String userPassword = user.getPassword();

         //   AgUser user = iAgUser.findUserByName(loginName);

            //说明是单个申请, 则单个审核
            if (applyItems.size() == 1) {
                AgOpenMapApplyItem applyItem = applyItems.get(0);
                //1.审核，插入一条处理记录
                List<AgOpenMapApplyProcess> applyProcesses = new ArrayList<>();
                AgOpenMapApplyProcess applyProcess = new AgOpenMapApplyProcess();
                applyProcess.setId(UUID.randomUUID().toString());
                applyProcess.setApplyItemId(applyItem.getId());
                applyProcess.setCode("003"); // 设置流程代码：001 - 用户提交申请表，002-等待审核 003-审核完成
                String result = "审核人通过申请";
                if (auditResult.equals("2")) { // "2"不通过审核
                    result = "审核人驳回申请";
                }
                applyProcess.setName(result); // 设置流程名称
                applyProcess.setHandle(auditOpinion); //处理意见
                Date now = new Date();
                applyProcess.setProcessTime(now);
                applyProcess.setSuccess(1);
                iAgOpenMap.createApplyProcess(applyProcess); //插入一条流程记录

                //2.更新审核List信息：审核结果，审核人
                iAgOpenMap.updateAuditList(auditList.getId(), auditResult, userName, auditOpinion);

                //3. 更新申请Item的当前处理状态, currentProcessStauts "0":申请中，"1":已拥有, "2":申请失败; 设置代理URL
                String currentProcessStatus = auditResult;
                if (currentProcessStatus.equals("2")) { //不通过时
                    iAgOpenMap.updateApplyItem(applyItem.getId(), currentProcessStatus, "", userName);
                } else { //通过时，暴露代理URL

                    //--------调用外部审核对代理地址，进行授权
                    //1.根据layer id 获取 图层代理id
                    String beforeLayerId =   iAgOpenMap.getBeforeProxyId(applyItem.getLayerId());
                    String proxyServerURL =  ProxyUtil.getProxyPreUrl() +beforeLayerId;

                    //
                    String proxUrl = ProxyUtil.getProxyPreUrl() + "service/serviceApply";

                    //2.调proxy 申请接口
                    Map<String, String> param2 = new HashMap<>();

                    param2.put("loginName", loginName);
                    param2.put("password", userPassword);
                    param2.put("serviceId", applyItem.getLayerId());
                    param2.put("ip", applyItem.getIp());

                    //发送proxy审核申请
                    HttpRespons httpRespons2 = new HttpRequester().sendPost(proxUrl, param2);
                    JSONObject jsonObject2 = JSONObject.fromObject(httpRespons2.getContent());
                    Map map2 = com.common.util.JsonUtils.toBean(jsonObject2.toString(), HashMap.class);

                    String afterLayerId = map2.get("uuid").toString();

                    AgServiceController serviceController = new  AgServiceController();

                    List<AgServiceUserinfo> serviceUserinfos = getServiceUserInfoByServiceIdAndUserName(applyItem.getLayerId(), userId);
                    String ids = "";
                    for (AgServiceUserinfo info : serviceUserinfos){
                        ids += info.getId() + ",";
                    }
                   String[] infoIds = ids.split(",");

                    applyServices("1", "地图资源审核通过",infoIds);

                    //审核通过 proxy 申请
                    iAgOpenMap.updateApplyItem(applyItem.getId(), currentProcessStatus, afterLayerId, userName);//ProxyUtil.getProxyPreUrl()
                }

                // 4.下发服务申请通知短信
                String sms = "";
                if (auditResult.equals("0")) {
                    sms = AUDIT_FAIL_SMS_TEMPLATE.replace("{user}", applyItem.getApplicant());
                } else {
                    sms = AUDIT_SUCCESS_SMS_TEMPLATE.replace("{user}", applyItem.getApplicant());
                }
                sendSms(applyItem.getApplicantMobile(), sms);

                return JsonUtils.toJson(new ContentResultForm(true, auditList, "审核操作成功"));

            }
            else { //批量审核

                String applicant = "";
                String applicantMobile = "";
                for (AgOpenMapApplyItem applyItem : applyItems) {

                    if (applicant.equals("")) {
                        applicant = applyItem.getApplicant();
                    }
                    if (applicantMobile.equals("")) {
                        applicantMobile = applyItem.getApplicantMobile();
                    }

                    //1.审核，插入一条处理记录
                    AgOpenMapApplyProcess applyProcess = new AgOpenMapApplyProcess();
                    applyProcess.setId(UUID.randomUUID().toString());
                    applyProcess.setApplyItemId(applyItem.getId());
                    applyProcess.setCode("003"); // 设置流程代码：001 - 用户提交申请表，002-等待审核 003-审核完成
                    String result = "审核人通过申请";
                    if (auditResult.equals("2")) { // "2"不通过审核
                        result = "审核人驳回申请";
                    }
                    applyProcess.setName(result); // 设置流程名称
                    applyProcess.setHandle(auditOpinion); //处理意见
                    Date now = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    applyProcess.setProcessTime(now);
                    applyProcess.setSuccess(1);
                    iAgOpenMap.createApplyProcess(applyProcess);

                    //3. 更新申请Item的当前处理状态, currentProcessStauts "0":申请中，"1":已拥有, "2":申请失败; 设置代理URL
                    String currentProcessStatus = auditResult;
                    if (currentProcessStatus.equals("2")) { //不通过时
                        iAgOpenMap.updateApplyItem(applyItem.getId(), currentProcessStatus, "", userName);
                    } else {

                        //--------调用外部审核对代理地址，进行授权
                        //1.根据layer id 获取 图层代理id
                        String beforeLayerId =   iAgOpenMap.getBeforeProxyId(applyItem.getLayerId());
                        String proxyServerURL =  ProxyUtil.getProxyPreUrl() +beforeLayerId;

                        //
                        String proxUrl = ProxyUtil.getProxyPreUrl() + "service/serviceApply";

                        //2.调proxy 申请接口
                        Map<String, String> param2 = new HashMap<>();

                        param2.put("loginName", loginName);
                        param2.put("password", pwd);
                        param2.put("serviceId", applyItem.getLayerId());
                        param2.put("ip", applyItem.getIp());

                        //发送proxy审核申请
                        HttpRespons httpRespons2 = new HttpRequester().sendPost(proxUrl, param2);
                        JSONObject jsonObject2 = JSONObject.fromObject(httpRespons2.getContent());
                        Map map2 = com.common.util.JsonUtils.toBean(jsonObject2.toString(), HashMap.class);

                        String afterLayerId = map2.get("uuid").toString();

                        AgServiceController serviceController = new  AgServiceController();

                        List<AgServiceUserinfo> serviceUserinfos = getServiceUserInfoByServiceIdAndUserName(applyItem.getLayerId(), userId);
                        String ids = "";
                        for (AgServiceUserinfo info : serviceUserinfos){
                            ids += info.getId() + ",";
                        }
                        String[] infoIds = ids.split(",");

                        applyServices("1", "地图资源审核通过",infoIds);

                        iAgOpenMap.updateApplyItem(applyItem.getId(), currentProcessStatus, afterLayerId, userName);
                    }
                }

                //2.更新审核List信息：审核结果，审核人
                iAgOpenMap.updateAuditList(auditList.getId(), auditResult, userName, auditOpinion);


                // 4.下发服务申请通知短信
                String sms = "";
                if (auditResult.equals("0")) {
                    sms = AUDIT_FAIL_SMS_TEMPLATE.replace("{user}", applicant);
                } else {
                    sms = AUDIT_SUCCESS_SMS_TEMPLATE.replace("{user}", applicant);
                }
                sendSms(applicantMobile, sms);
                return JsonUtils.toJson(new ContentResultForm(true, auditList, "审核操作成功"));
            }

        } catch (Exception e) {
            log.error("审核操作失败", e);
        }
        return JsonUtils.toJson(new ResultForm(false, "审核操作失败"));
    }

    //根据 service id, 登录名，获取service userInfo
    public List<AgServiceUserinfo> getServiceUserInfoByServiceIdAndUserName(String serviceId, String userId) {
        List<AgServiceUserinfo> serviceUserinfos = new ArrayList<>();
        try {
             serviceUserinfos = iAgService.getServiceUserInfoByServiceIdAndUserName(serviceId, userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return serviceUserinfos;
    }

    public void applyServices(String flag, String approveOpinion, String[] ids) {
        for (int i = 0; i < ids.length; i++) {
            AgServiceUserinfo agServiceUserinfo = iAgService.getAgServiceUserinfoById(ids[i]);
            agServiceUserinfo.setFlag(Common.checkNull(flag, "0"));
            agServiceUserinfo.setApproveOpinion(approveOpinion);
            agServiceUserinfo.setApproveTime(new Date());
            iAgService.updateAgServiceUserinfo(agServiceUserinfo);
        }
    }



    /*
     * 获取 购物车列表
     * @param loginName 用户登录名
     */
    @RequestMapping({"/cart/list"})
    public String listCart(String loginName) {
        try {
            List<AgOpenMapCartItem> cartItems = iAgOpenMap.findCartItem(null, loginName);
            return JsonUtils.toJson(new ContentResultForm(true, cartItems));
        } catch (Exception e) {
            log.error("根据登录名【" + loginName + "】获取购物车申请资源列表信息失败");
        }

        return JsonUtils.toJson(new ResultForm(false, "获取购物车申请服务列表信息失败"));
    }


    /*
     * 添加到 购物车
     */
    @RequestMapping({"/cart/save"})
    public String saveCart(AgOpenMapCartItem cartItem) throws Exception {

        List<AgOpenMapCartItem> cartItems = iAgOpenMap.findCartItem(cartItem.getLayerId(), cartItem.getLoginName());
        if (CollectionUtils.isNotEmpty(cartItems)) {
            return JsonUtils.toJson(new ResultForm(false, "已添加的不能加入购物车"));
        }

        cartItem.setId(UUID.randomUUID().toString());

        try {
            iAgOpenMap.saveCartItem(cartItem);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.toJson(new ResultForm(false, "添加失败"));
        }

        return JsonUtils.toJson(new ContentResultForm<AgOpenMapCartItem>(true, cartItem));
    }

    /*
     * 删除购物车
     * @param id cartItem的Id
     */
    @RequestMapping({"/cart/del"})
    public String delCart(String id) {
        try {
            iAgOpenMap.delCartItem(id);
            return JsonUtils.toJson(new ResultForm(true, "移除成功"));
        } catch (Exception e) {
            log.error("通过ID移除服务资源失败", e);
        }
        return JsonUtils.toJson(new ResultForm(false, "移除失败"));
    }


    // 下载申请模板文件
    //针对线下申请
    @RequestMapping({"/downloadTemplate/{serialNo}"})
    public void downloadTemplate(HttpServletResponse resp, @PathVariable("serialNo") String serialNo) throws Exception {
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode("申请服务数据使用申请书.doc", "UTF-8") + "\"");

        List<AgOpenMapApplyItem> applyItems = iAgOpenMap.findApplyItemBySerialNo(serialNo);
        AgOpenMapApplyItem applyItem = applyItems.get(0);

        AgOpenMapAuditList auditList = iAgOpenMap.findAuditListBySerialNo(serialNo);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("applyUser", applyItem.getApplicant());
        model.put("opinion", auditList.getOpinion()); //处理意见
        model.put("layerNames", auditList.getLayerName());  //图层名
        model.put("taskInfo", applyItem);

        Template template = configuration.getTemplate("apply.ftl", "utf-8");
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        IOUtils.write(content.getBytes("utf-8"), resp.getOutputStream());
    }

    /**
     * 申请人提交申请相关材料
     * //2018-10-17  1.修改：
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/submitApplyFile/{loginName}/{applyItemId}", method = RequestMethod.POST)
    public void submitApplyFile(HttpServletRequest req, HttpServletResponse resp, @PathVariable("applyItemId") String applyItemId, @PathVariable("loginName") String loginName, @RequestParam("file") MultipartFile file) throws Exception {

        //设置响应数据resp.setContentType("text/plain; charset=UTF-8");
        resp.resetBuffer();

        String resultStr = "{\"success\": false, \"message\": \"上传失败\"}";
        try {
            List<AgOpenMapAttachFile> fileInfo = excuteUpload(null, applyItemId, file, req, loginName);
            if (fileInfo != null) {
                resultStr = JSONArray.fromObject(fileInfo).toString();
            }
        } catch (Exception e) {
            log.error("上传附件[" + file.getOriginalFilename() + "]失败...", e);
        }
        // 将成功上传的附件信息返回回去
        IOUtils.write(resultStr.getBytes("utf-8"), resp.getOutputStream());
        resp.getOutputStream().flush();
    }

    /*
     * @param applyItemId 对应
     */
    private List<AgOpenMapAttachFile> excuteUpload(String uploadDir, String applyItemIds, MultipartFile file, HttpServletRequest req, String loginName) throws Exception {
        if (Strings.isNullOrEmpty(uploadDir)) {
            uploadDir = filePath;
        }

        if (StringUtils.isEmpty(applyItemIds)) {
            applyItemIds = "0";
        }
        String originalFilename = file.getOriginalFilename();
        // IE8下会拿到文件的路径名
        if (originalFilename.indexOf("\\") != -1) {// windows环境
            originalFilename = originalFilename.substring(originalFilename.lastIndexOf("\\") + 1);
        }
        if (originalFilename.indexOf("/") != -1) {
            originalFilename = originalFilename.substring(originalFilename.lastIndexOf("/") + 1);
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String id = UUID.randomUUID().toString(); //这个作为保存在服务器的文件名
        // 上传文件名
        String newFilename = id + suffix;
        File serverFile = new File(uploadDir + newFilename);
        file.transferTo(serverFile); // 将上传的文件写入到服务器端文件内


//
     /*   Map<String, String> param = new HashMap<>();
        param.put("userName", loginName);
        HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/om/getOpuOmUserInfoByUserId.do", param);
        JSONObject jsonObject = JSONObject.fromObject(httpRespons.getContent());
        Map map = com.common.util.JsonUtils.toBean(jsonObject.toString(), HashMap.class);
        String userName = map.get("userName").toString();*/
        AgUser user = iAgUser.findUserByName(loginName);
        String userName = user.getUserName();


        String[] applyItemIdArr =  applyItemIds.split(",");
        List<AgOpenMapAttachFile> fileInfos = new ArrayList<>();

        for (String applyItemId : applyItemIdArr){
            AgOpenMapAttachFile fileInfo = new AgOpenMapAttachFile();
            fileInfo.setId(UUID.randomUUID().toString());
            fileInfo.setOriginalFilename(originalFilename);
            fileInfo.setSuffix(suffix);
            fileInfo.setFileSize(Math.floor(file.getSize() / 1024d + 0.5) + "KB");
            fileInfo.setNameId(id); //设置保存在服务器的文件名

            fileInfo.setCreatorLoginName(loginName);
            fileInfo.setCreatorUserName(userName);
            fileInfo.setApplyItemId(applyItemId);
            iAgOpenMap.saveApplyFile(fileInfo);
            fileInfos.add(fileInfo);
        }

//        FileInfo fileInfo = new FileInfo();
//        fileInfo.setId(id);
//        fileInfo.setOriginalFilename(originalFilename);
//        fileInfo.setSuffix(suffix);
//        fileInfo.setFileSize(Math.floor(file.getSize() / 1024d + 0.5) + "KB");
//
        return fileInfos;
    }

    /*
     * 下载申请材料
     * @param id 文件id
     */
    @RequestMapping(value = "/downloadApplyFile/{id}", method = RequestMethod.GET)
    public void download(HttpServletResponse resp, @PathVariable("id") String id) throws Exception {
        InputStream is = null;
        try {
            // 获取文件信息
            AgOpenMapAttachFile fileInfo = iAgOpenMap.getFileInfo(id);

            String originalFileName = URLEncoder.encode(fileInfo.getOriginalFilename(), "UTF-8");
            originalFileName = originalFileName.replaceAll("\\+", " ");

            is = new FileInputStream(filePath + fileInfo.getNameId() + fileInfo.getSuffix());
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + originalFileName + "\"");

            // 将成功上传的附件信息返回回去
            IOUtils.copy(is, resp.getOutputStream());
        } catch (Exception e) {
            log.error("下载文件出错...", e);
            // 异常处理
            resp.setContentType("text/plain; charset=UTF-8");
            resp.resetBuffer();
            IOUtils.write("{\"success\": false, \"message\": \"下载文件出错...\"}".getBytes("utf-8"), resp.getOutputStream());
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
