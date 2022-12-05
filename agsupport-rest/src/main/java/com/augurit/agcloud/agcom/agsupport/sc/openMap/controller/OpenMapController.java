package com.augurit.agcloud.agcom.agsupport.sc.openMap.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgOpenMapApply;
import com.augurit.agcloud.agcom.agsupport.domain.AgOpenMapAttachFile;
import com.augurit.agcloud.agcom.agsupport.domain.AgOpenMapAuditList;
import com.augurit.agcloud.agcom.agsupport.domain.AgOpenMapCartItem;
import com.augurit.agcloud.agcom.agsupport.sc.openMap.service.IAgOpenMap;
import com.augurit.agcloud.agcom.agsupport.sc.service.service.IAgService;
import com.augurit.agcloud.agcom.agsupport.sc.tag.controller.AgTagController;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.framework.security.user.OpusLoginUser;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("agsupport/openMap")
public class OpenMapController {

    private static final Logger log = Logger.getLogger(AgTagController.class);
    private static final String APPLY_SMS_TEMPLATE = "尊敬的{user}用户，您的申请已提交，请关注审核结果短信通知。";
    private static final String AUDIT_SUCCESS_SMS_TEMPLATE = "尊敬的{user}用户，您的申请已通过审核，请登录系统查看服务地址并测试服务可用性。";
    private static final String AUDIT_FAIL_SMS_TEMPLATE = "尊敬的{user}用户，您的申请未通过审核，原因为【{auditOpinion}】，请及时处理。";

    @Autowired
    private IAgOpenMap iAgOpenMap;

    @Autowired
    private IAgService iAgService;

    @Value(value = "${upload.filePath}")
    private String filePath;

    @Autowired
    private Configuration configuration;

    @Autowired
    private IAgUser iAgUser;


    @ApiOperation(value = "目录树接口",notes = "获取目录树，并返回目录下所有允许外部访问的图层数量")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "getDirTreeOfOpenMap", method = RequestMethod.GET)
    public ContentResultForm getDirTreeOfOpenMap() { //String keyWord, String applyStatus
            return iAgOpenMap.getDirTreeOfOpenMap();
    }

    @ApiOperation(value = "用户目录树接口",notes = "根据当前用户获取目录树，并统计目录中该用户申请的图层数量")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "getDirTreeOfOpenMapByCurrentUser", method = RequestMethod.GET)
    public ContentResultForm getDirTreeOfOpenMapByCurrentUser(HttpServletRequest request) { //String keyWord, String applyStatus
        OpusLoginUser loginUser = getCurrentUser(request);
        String loginName= loginUser.getUser().getLoginName();
        return iAgOpenMap.getDirTreeOfOpenMapByUserId(loginName);
    }


    @ApiOperation(value = "允许外部访问的服务列表接口",notes = "获取对外开放服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirId",value = "目录编号",dataType = "String"),
            @ApiImplicitParam(name = "keyWord",value = "查询关键字",dataType = "String"),
            @ApiImplicitParam(name = "pageNum",value = "页码",dataType = "int"),
            @ApiImplicitParam(name = "pageSize",value = "页大小",dataType = "int")
    })
    @RequestMapping(value = "/openMapLayers", method = RequestMethod.GET)
    public ContentResultForm getLayers(HttpServletRequest request,String dirId,String keyWord ,int pageNum,int pageSize) {
        OpusLoginUser loginUser = getCurrentUser(request);
        String loginName= loginUser.getUser().getLoginName();
        Page page = new Page(pageNum,pageSize);
        return iAgOpenMap.getLayersOfExternal(loginName,dirId,keyWord ,page);
    }

    @ApiOperation(value = "当前用户申请的允许外部访问的服务列表接口",notes = "获取当前用户申请的允许外部访问的服务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirId",value = "目录编号",dataType = "String"),
            @ApiImplicitParam(name = "keyWord",value = "查询关键字",dataType = "String"),
            @ApiImplicitParam(name = "applyStatus",value = "申请状态, 空：全部，0：申请中，1：已拥有，2：驳回",dataType = "String"),
            @ApiImplicitParam(name = "pageNum",value = "页码",dataType = "int"),
            @ApiImplicitParam(name = "pageSize",value = "页大小",dataType = "int")
    })
    @RequestMapping(value = "/openMapLayersByCurrentUser", method = RequestMethod.GET)
    public ContentResultForm getLayers(HttpServletRequest request,String dirId,String keyWord ,String applyStatus,int pageNum,int pageSize) {
        OpusLoginUser loginUser = getCurrentUser(request);
        String loginName= loginUser.getUser().getLoginName();
        Page page = new Page(pageNum,pageSize);
        return iAgOpenMap.getLayersOfExternalForUser(loginName,dirId,keyWord ,applyStatus,page);
    }

    @ApiOperation(value = "获取待提交的申请的接口",notes = "获取待提交申请列表")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/cart/list", method = RequestMethod.GET)
    public ContentResultForm listCart(HttpServletRequest request) {
        OpusLoginUser loginUser = getCurrentUser(request);
        String loginName= loginUser.getUser().getLoginName();
        return iAgOpenMap.findCartItemByLoginName(loginName);
    }

    @ApiOperation(value = "保存待提交申请接口",notes = "保存待提交申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirLayerId",value = "目录图层编号",dataType = "String"),
            @ApiImplicitParam(name = "thumbNail",value = "缩略图",dataType = "String")
    })
    @RequestMapping(value = "/cart/save", method = RequestMethod.POST)
    public ResultForm saveCart(HttpServletRequest request, AgOpenMapCartItem cartItem)  {

        OpusLoginUser loginUser = getCurrentUser(request);
        String loginName= loginUser.getUser().getLoginName();
        cartItem.setLoginName(loginName);
        return iAgOpenMap.saveCartItem(cartItem);
    }

    @ApiOperation(value = "删除待提交申请接口",notes = "删除待提交申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "待提交申请的编号",dataType = "String"),
    })
    @RequestMapping(value = "/cart/del" ,method = RequestMethod.DELETE)
    public ResultForm delCart(String id) {
        return iAgOpenMap.delCartItem(id);
    }

    @ApiOperation(value = "提交服务申请接口",notes = "提交服务申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicant",value = "申请人的姓名",dataType = "String"),
            @ApiImplicitParam(name = "applyDocId",value = "暂时不清楚这个是什么，先忽略",dataType = "String"),
            @ApiImplicitParam(name = "secrecy",value = "是否涉密 0-否，1-是",dataType = "String"),
            @ApiImplicitParam(name = "obtainWay",value = "获取方式：1-在线接口服务，2-其它",dataType = "String"),
            @ApiImplicitParam(name = "obtainWayDesc",value = "获取方式为其它时的说明",dataType = "String"),
            @ApiImplicitParam(name = "validityDate",value = "申请使用天数",dataType = "String"),
            @ApiImplicitParam(name = "workUnit",value = "工作单位",dataType = "String"),
            @ApiImplicitParam(name = "applicantIdCard",value = "身份证号码",dataType = "String"),
            @ApiImplicitParam(name = "workAddress",value = "工作单位地址",dataType = "String"),
            @ApiImplicitParam(name = "applicantMobile",value = "申请人手机号码",dataType = "String"),
            @ApiImplicitParam(name = "applyFor",value = "申请用途",dataType = "String"),
            @ApiImplicitParam(name = "dirLayerIds",value = "资源编号(目录图层ID）",dataType = "String")
    })
    @RequestMapping(value = "submitApplyTable", method = RequestMethod.POST)
    public ResultForm submitApplyTable(HttpServletRequest request, AgOpenMapApply applyInfo) {

        OpusLoginUser loginUser = getCurrentUser(request);
        String loginName= loginUser.getUser().getLoginName();
        String ip = request.getRemoteAddr();
        applyInfo.setIp(ip);
        applyInfo.setApplicantLoginName(loginName);
        setApplyFile(request,applyInfo);
        return iAgOpenMap.submitApplyTable(applyInfo);
    }

    @ApiOperation(value = "提交服务申请接口",notes = "提交服务申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicant",value = "申请人的姓名",dataType = "String"),
            @ApiImplicitParam(name = "applyDocId",value = "暂时不清楚这个是什么，先忽略",dataType = "String"),
            @ApiImplicitParam(name = "secrecy",value = "是否涉密 0-否，1-是",dataType = "String"),
            @ApiImplicitParam(name = "obtainWay",value = "获取方式：1-在线接口服务，2-其它",dataType = "String"),
            @ApiImplicitParam(name = "obtainWayDesc",value = "获取方式为其它时的说明",dataType = "String"),
            @ApiImplicitParam(name = "validityDate",value = "申请使用天数",dataType = "String"),
            @ApiImplicitParam(name = "workUnit",value = "工作单位",dataType = "String"),
            @ApiImplicitParam(name = "applicantIdCard",value = "身份证号码",dataType = "String"),
            @ApiImplicitParam(name = "workAddress",value = "工作单位地址",dataType = "String"),
            @ApiImplicitParam(name = "applicantMobile",value = "申请人手机号码",dataType = "String"),
            @ApiImplicitParam(name = "applyFor",value = "申请用途",dataType = "String"),
            @ApiImplicitParam(name = "dirLayerIds",value = "资源编号(目录图层ID）",dataType = "String")
    })
    @RequestMapping(value = "repeatSubmitApplyTable", method = RequestMethod.POST)
    public ResultForm repeatSubmitApplyTable(HttpServletRequest request, AgOpenMapApply applyInfo) {

        OpusLoginUser loginUser = getCurrentUser(request);
        String loginName= loginUser.getUser().getLoginName();
        String ip = request.getRemoteAddr();
        applyInfo.setIp(ip);
        applyInfo.setApplicantLoginName(loginName);
        setApplyFile(request,applyInfo);
        return iAgOpenMap.repeatSubmitApplyTable(applyInfo);
    }

    @ApiOperation(value = "获取详情接口",notes = "获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirLayerId",value = "目录图层编号",dataType = "String")
    })
    @RequestMapping(value = "/getDetails", method = RequestMethod.GET)
    public ContentResultForm getApplyDetails(HttpServletRequest request,String dirLayerId) {
        OpusLoginUser loginUser = getCurrentUser(request);
        String loginName= loginUser.getUser().getLoginName();
        return iAgOpenMap.getDetails(loginName,dirLayerId);
    }



    @ApiOperation(value = "获取审核列表的接口",notes = "获取审核列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyId",value = "申请号",dataType = "String"),
            @ApiImplicitParam(name = "applicant",value = "申请人姓名",dataType = "String"),
            @ApiImplicitParam(name = "keyWord",value = "查询关键字",dataType = "String"),
            @ApiImplicitParam(name = "auditStatus",value = "审核状态",dataType = "String"),
            @ApiImplicitParam(name = "pageNum",value = "页码",dataType = "int"),
            @ApiImplicitParam(name = "pageSize",value = "页大小",dataType = "int")
    })
    @RequestMapping(value = "auditList", method = RequestMethod.GET)
    public ContentResultForm getAuditList(HttpServletRequest request, String applyId, String applicant, String keyWord, String auditStatus,int pageNum,int pageSize) {
        Page page = new Page(pageNum,pageSize);
        return iAgOpenMap.getAuditList(null,keyWord,auditStatus,page);
    }

    @RequestMapping(value = "auditListForCurrentUser", method = RequestMethod.GET)
    public ContentResultForm getAuditListForCurrentUser(HttpServletRequest request, String applyId, String applicant, String keyWord, String auditStatus,int pageNum,int pageSize) {
        OpusLoginUser loginUser = getCurrentUser(request);
        String loginName= loginUser.getUser().getLoginName();
        Page page = new Page(pageNum,pageSize);
        return iAgOpenMap.getAuditList(loginName,keyWord,auditStatus,page);
    }


    @ApiOperation(value = "审核申请接口",notes = "审核申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyId",value = "申请编号",dataType = "String"),
            @ApiImplicitParam(name = "auditResult",value = "审核结果: 0:待审核 1：通过，2：不通过",dataType = "String"),
            @ApiImplicitParam(name = "auditOpinion",value = "审核意见",dataType = "String")
    })
    @RequestMapping(value = "auditApply", method = RequestMethod.POST)
    public ResultForm auditApply(HttpServletRequest request, String applyId, String auditResult, String auditOpinion) {
        OpusLoginUser loginUser = getCurrentUser(request);
        String loginName= loginUser.getUser().getLoginName();
        return iAgOpenMap.auditApply(loginName,applyId,auditResult,auditOpinion);
    }

    // 下载申请模板文件
    //针对线下申请
    @ApiIgnore
    @RequestMapping({"/downloadTemplate/{serialNo}"})
    public void downloadTemplate(HttpServletResponse resp, @PathVariable("serialNo") String serialNo) throws Exception {
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode("申请服务数据使用申请书.doc", "UTF-8") + "\"");

        List<AgOpenMapApply> applyItems =null;// iAgOpenMap.findApplyItemBySerialNo(serialNo);
        AgOpenMapApply applyItem = applyItems.get(0);

        AgOpenMapAuditList auditList = null;//iAgOpenMap.findAuditListBySerialNo(serialNo);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("applyUser", applyItem.getApplicant());
        model.put("opinion", ""); //处理意见
        model.put("layerNames", "");  //图层名
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

    @ApiIgnore
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


    //@param applyItemId 对应
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
       Map<String, String> param = new HashMap<>();
        param.put("userName", loginName);
        HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/om/getOpuOmUserInfoByUserId.do", param);
        JSONObject jsonObject = JSONObject.fromObject(httpRespons.getContent());
        Map map = com.common.util.JsonUtils.toBean(jsonObject.toString(), HashMap.class);
        String userName = map.get("userName").toString();
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
    }*/

    /*
     * 下载申请材料
     * @param id 文件id

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
    }*/

    private void setApplyFile(HttpServletRequest request,AgOpenMapApply applyInfo){
        //获取上传文件
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String,MultipartFile> mapFile = multipartRequest.getFileMap();
        List<AgOpenMapAttachFile> listAttachFile = new ArrayList<>();
        for(Map.Entry<String, MultipartFile> entity : mapFile.entrySet()){
            MultipartFile file = entity.getValue();
            String fileName = file.getOriginalFilename();
            fileName = fileName.substring(0,fileName.lastIndexOf('.'));
            long fileSize = file.getSize();
            AgOpenMapAttachFile agAgOpenMapAttachFile = new AgOpenMapAttachFile();
            agAgOpenMapAttachFile.setFile(file);
            agAgOpenMapAttachFile.setName(fileName);
            agAgOpenMapAttachFile.setFileSize(String.valueOf(fileSize));
            listAttachFile.add(agAgOpenMapAttachFile);
        }
        applyInfo.setApplyFiles(listAttachFile);
    }

    private OpusLoginUser getCurrentUser(HttpServletRequest request){
        //String authorization = "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJvcHVzTG9naW5Vc2VyIjp7InVzZXIiOnsidXNlcklkIjoiY2E5YjdjNTktY2FmNi00MGExLWEwNTItN2FjN2VkMjViNzdhIiwidXNlck5hbWUiOiLmooHok53lv4MiLCJsb2dpbk5hbWUiOiJsaWFuZ2x4IiwibG9naW5Qd2QiOm51bGwsImlzTG9jayI6bnVsbCwibG9naW5GYWlsTnVtIjpudWxsLCJsb2NrVGltZSI6bnVsbH0sIm9yZ3MiOm51bGwsImN1cnJlbnRPcmdJZCI6IkEiLCJjdXJyZW50T3JnQ29kZSI6IlIwMDEiLCJjdXJyZW50VG1uIjoicGMiLCJhcHBTb2Z0Q29udGV4dHMiOltdLCJ0bW5NZW51Q29udGV4dHMiOltdfSwidXNlcl9uYW1lIjoibGlhbmdseCIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE1Njk0MzM2MTUsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJkOWU5MzI3ZS04NmRjLTQ5M2MtOGI0YS1hZDVkZGMyODUwYzUiLCJjbGllbnRfaWQiOiJhZ2NvbSJ9.RZLJR1V7KJt_UgZoudGLVvjxxYMhXs16k931NTrGnbs";
        String authorization = request.getHeader("Authorization");
        String tokenValue = authorization.substring(authorization.indexOf(' ')+1);
        Claims claims = null;

        try {
            claims = (Claims) Jwts.parser().setSigningKey("agcloudTokenKey88".getBytes("UTF-8")).parseClaimsJws(tokenValue).getBody();
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }

        Object object = claims.get("opusLoginUser");
        ObjectMapper objectMapper = new ObjectMapper();
        OpusLoginUser opusLoginUser = (OpusLoginUser)objectMapper.convertValue(object, OpusLoginUser.class);
        return  opusLoginUser;
    }
}
