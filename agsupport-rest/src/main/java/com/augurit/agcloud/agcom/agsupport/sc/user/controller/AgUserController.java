package com.augurit.agcloud.agcom.agsupport.sc.user.controller;

import com.augurit.agcloud.agcom.agsupport.common.aspect.RestLog;
import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.log.service.IAgLog;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
import com.common.util.MD5;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Augurit on 2017-04-27.
 */
@Api(value = "用户管理",description = "用户管理相关接口")
@RestController
@RequestMapping("/agsupport/user")
public class AgUserController {

    private static Logger logger = LoggerFactory.getLogger(AgUserController.class);
    private static final String ROWS = "rows";
    private static final String PAGESIZE = "page";

    @Autowired
    private IAgUser iAgUser;
    @Autowired
    private IAgDir iAgDir;
    @Autowired
    private IAgLog log;


  /*  @RequestMapping("/author.do")
    public ModelAndView author() {
        return new ModelAndView("agcom/user/userAuthor");
    }*/
    /**
     * 调用接口根据机构id获取机构
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "调用接口根据机构id获取机构",notes = "调用接口根据机构id获取机构接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "机构id",dataType = "String")
    })
     @RequestMapping(value = "/tree",method = RequestMethod.GET)
     public ContentResultForm tree(String id) throws Exception {
         String result = iAgUser.findOrgTreeById(id);
         return new ContentResultForm<String>(true,result);
     }

    @ApiOperation(value = "分页获取用户信息",notes = "分页获取用户信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agUser" ,value = "用户信息对象",dataType = "AgUser"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:/agsupport/param/paramList?page=1&rows=10&name=", dataType = "Page")
    })
    @RequestMapping(value = "/userList",method = RequestMethod.GET)
    public ContentResultForm userList(AgUser agUser, Page page) throws Exception {
        PageInfo<AgUser> pageInfo=iAgUser.searchUser(agUser, page);
        EasyuiPageInfo<AgUser> result = PageHelper.toEasyuiPageInfo(pageInfo);
        return new ContentResultForm<EasyuiPageInfo<AgUser>>(true,result);
    }

    /**
     * 根据机构id，用户名称调用agcloud接口分页获取用户信息
     * @param orgId
     * @param agUser
     * @param page
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据机构id，用户名称调用agcloud接口分页获取用户信息",notes = "根据机构id，用户名称调用agcloud接口分页获取用户信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgId" ,value = "机构id",dataType = "String"),
            @ApiImplicitParam(name = "agUser" ,value = "用户信息对象",dataType = "AgUser"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:/agsupport/param/paramList?page=1&rows=10&name=", dataType = "Page")
    })
    @RequestMapping(value = "/usersByOrgId",method = RequestMethod.GET)
    public ContentResultForm usersByOrgId(String orgId, AgUser agUser, Page page, HttpServletRequest request) throws Exception {
        page.setPageNum(Integer.valueOf(request.getParameter("page")));
        page.setPageSize(Integer.valueOf(request.getParameter("rows")));
        PageInfo<AgUser> pageInfo = null;
        if(Common.isCheckNull(agUser.getUserName())) {
            pageInfo = iAgUser.searchUser(agUser, page, orgId);
        }else{
            pageInfo = iAgUser.findOrgUsers(agUser.getUserName(), page);
        }
        //PageInfo<AgUser> pageInfo=iAgUser.searchUser(agUser, page, orgId);
        EasyuiPageInfo<AgUser> result = PageHelper.toEasyuiPageInfo(pageInfo);
        return new ContentResultForm<EasyuiPageInfo<AgUser>>(true,result);
    }


    @ApiOperation(value = "根据所属目录获取用户",notes = "根据所属目录获取用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "xpath" ,value = "目录路径",dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "isContain" ,value = "isContain",dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "agUser" ,value = "用户信息对象",dataType = "AgUser"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:/agsupport/param/paramList?page=1&rows=10&name=", dataType = "Page")
    })
    @RequestMapping(value = "/usersByXpath/{xpath}",method = RequestMethod.GET)
    public ContentResultForm usersByXpath(@PathVariable("xpath") String xpath, String isContain, AgUser agUser, Page page) throws Exception {
        PageInfo<AgUser> pageInfo=iAgUser.searchUser(agUser, page, xpath);
        return new ContentResultForm<EasyuiPageInfo>(true,PageHelper.toEasyuiPageInfo(pageInfo));
    }


    @ApiOperation(value = "根据角色id分页获取用户",notes = "根据角色id分页获取用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId" ,value = "角色id",dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "agUser" ,value = "用户信息对象",dataType = "AgUser"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:/agsupport/param/paramList?page=1&rows=10&name=", dataType = "Page")
    })
    @RequestMapping(value = "/userListByRole/{roleId}",method = RequestMethod.GET)
    public ContentResultForm userListByRole(@PathVariable("roleId") String roleId, AgUser agUser, Page page) throws Exception {
        PageInfo<AgUser> pageInfo=iAgUser.searchUserByRole(agUser, page, roleId);
        return new ContentResultForm<EasyuiPageInfo>(true,PageHelper.toEasyuiPageInfo(pageInfo));
    }

    /**
     * 根据角色id获取用户信息
     * @param roleId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据角色id获取用户信息",notes = "根据角色id获取用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId" ,value = "角色id",dataType = "String")
    })
    @RequestMapping(value = "/findUserByRoleId",method = RequestMethod.GET)
    public ContentResultForm findUserByRoleId(String roleId) throws Exception{
        List<AgUser> listUsers = iAgUser.findUserByRoleId(roleId);
        return new ContentResultForm<List<AgUser>>(true,listUsers);
    }
    /**
     * 根据多个角色id获取用户信息
     * @param roleIds
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据角色id获取用户信息",notes = "根据角色id获取用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleIds" ,value = "多个角色ids,逗号分隔",dataType = "String")
    })
    @RequestMapping(value = "/findUsersByRoleIds",method = RequestMethod.GET)
    public ContentResultForm findUsersByRoleIds(String roleIds) throws Exception{
        List<AgUser> usersByRoleIds = iAgUser.findUsersByRoleIds(roleIds);
        return new ContentResultForm(true,usersByRoleIds);
    }

    /**
     * 根据多个角色id 分页获取用户信息
     * @param roleIds
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据角色id获取用户信息",notes = "根据角色id获取用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleIds" ,value = "多个角色ids,逗号分隔",dataType = "String"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:page=1&rows=10", dataType = "Page")
    })
    @RequestMapping(value = "/findUsersByRoleIdsHasPage",method = RequestMethod.GET)
    public ContentResultForm findUsersByRoleIdsHasPage(String roleIds,String userName,Page page, HttpServletRequest request) throws Exception{
        page.setPageNum(Integer.valueOf(request.getParameter("page")));
        page.setPageSize(Integer.valueOf(request.getParameter("rows")));
        PageInfo<AgUser> agUserPageInfo = iAgUser.findUsersByRoleIds(roleIds,userName, page);

        return new ContentResultForm(true,agUserPageInfo);
    }

    /**
     * 按图层id获取用户
     *
     * @param layerId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "按图层id获取用户",notes = "按图层id获取用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "layerId" ,required = true,value = "图层id",dataType = "String")
    })
    @RequestMapping(value = "/findUserByLayer",method = RequestMethod.GET)
    public ContentResultForm findUserByLayer(String layerId) throws Exception {
        return new ContentResultForm(true,iAgUser.findUserByLayer(layerId));
    }

    /**
     * 按图层id以及微件分享表的confID获取可分享用户（包含已分享用户）的信息
     *
     * @param layerId
     * @param confId
     * @return
     * @throws Exception
     */
/*    @RequestMapping("/findUserByLayerAndShareConfId")
    public JSONObject findUserByLayerAndShareConfId(String layerId, String confId) throws Exception {
        List<AgUser> agUserList = null;
        if (layerId == null || layerId.equals("")) { //若layerId为""，查询出所有的用户，针对echartsLayer
            agUserList = iAgUser.findUsers();
        } else {
            agUserList = iAgUser.findUserByLayer(layerId);
        }
        List<AgFuncShare> agFuncShareList = iAgFuncShare.findByConfId(confId);
        JSONObject jsonObject = new JSONObject();
        for (AgUser agUse : agUserList) {
            JSONObject json = new JSONObject();
            json.put("id", agUse.getId());
            json.put("userName", agUse.getLoginName());
            json.put("share", false);
            jsonObject.put(agUse.getId(), json);
        }
        for (AgFuncShare agFuncShare : agFuncShareList) {
            String userId = agFuncShare.getUserId();
            JSONObject userObj = jsonObject.getJSONObject(userId);
            if (userObj != null) {
                userObj.put("share", true);
            }
        }
        return jsonObject;
    }*/


    /**
     * 验证用户登录
     *
     * @param userName
     * @param passWord
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "验证用户登录",notes = "验证用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName" ,required = true,value = "用户名称",dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "passWord" ,required = true,value = "密码",dataType = "String",paramType = "path")
    })
    @RequestMapping(value = "/checkLogin/{userName}/{passWord}",method = RequestMethod.POST)
    public boolean checkLogin(@PathVariable("userName") String userName, @PathVariable("passWord") String passWord) throws Exception {
        boolean isSuccess = false;
        AgUser agUser = iAgUser.findUserByName(userName);
        if (agUser != null) {
            String md5Pwd = MD5.GetMD5Code(passWord).toLowerCase();
            String realPwd = agUser.getPassword().toLowerCase();
            passWord = passWord.toLowerCase();
            isSuccess = realPwd.equals(passWord) || realPwd.equals(md5Pwd);
        }
        return isSuccess;
    }

    @ApiOperation(value = "登录日志",notes = "验登录日志接口")
    @RequestMapping(value = "/loginLog",method = RequestMethod.POST)
    public void loginLog(HttpServletRequest request) throws Exception {
        BufferedReader reader = null;
        String loginLog = "";
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                loginLog += lines;
            }
            log.info(URLDecoder.decode(loginLog, "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * 验证用户登录
     *
     * @param loginName
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "验证用户登录",notes = "验证用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName" ,required = true,value = "登录名称",dataType = "String",paramType = "path")
    })
    @RequestMapping(value = "/getPwdByName/{loginName}",method = RequestMethod.POST)
    public ContentResultForm getPwdByName(@PathVariable("loginName") String loginName) throws Exception {
        AgUser agUser = iAgUser.findUserByName(loginName);
        if (agUser != null) return new ContentResultForm(true,agUser.getPassword());
        return null;
    }

    /**
     * 通过用户id获取用户
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "通过用户id获取用户",notes = "通过用户id获取用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId" ,required = true,value = "用户id",dataType = "String",paramType = "path")
    })
    @RequestMapping(value = "/getUserById/{userId}",method = RequestMethod.GET)
    public ContentResultForm getUserById(@PathVariable("userId") String userId) throws Exception {
        String result = "{'loginName':'','password':''}";
        AgUser agUser = iAgUser.findUserById(userId);
        if (agUser != null) {
            result = "{'loginName':'" + agUser.getLoginName() + "','password':'" + agUser.getPassword() + "'}";
        }
        return new ContentResultForm(true,result);
    }

    /**
     * 通过用户名获取code
     *
     * @param loginName
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "通过用户名获取code",notes = "通过用户名获取code接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName" ,required = true,value = "用户名称",dataType = "String",paramType = "path")
    })
    @RequestMapping(value = "/getCodeByUser/{loginName}",method = RequestMethod.GET)
    public ContentResultForm getCodeByUser(@PathVariable("loginName") String loginName) throws Exception {
        String result = "error:001";
        AgUser agUser = iAgUser.findUserByName(loginName);
        if (agUser != null) {
            String code = UUID.randomUUID().toString();
            if (iAgUser.saveCode(code, loginName)) {
                result = code;
            }
        }
        return new ContentResultForm(true,result);
    }

    /**
     * 通过token获取用户名及登陆密码
     *
     * @param token
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "通过token获取用户名及登陆密码",notes = "通过token获取用户名及登陆密码接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token" ,required = true,value = "token",dataType = "String",paramType = "path")
    })
    @RequestMapping(value = "getUserByToken/{token}",method = RequestMethod.POST)
    public ContentResultForm getUserByToken(@PathVariable("token") String token) throws Exception {
        String result = "{'loginName':'','password':''}";

        if (token == null)
            return null;
        String loginName = iAgUser.getLoginNameByToken(token);
        AgUser agUser = iAgUser.findUserByName(loginName);
        if (agUser != null) {
            result = "{'loginName':'" + agUser.getLoginName() + "','password':'" + agUser.getPassword() + "'}";
        }
        return new ContentResultForm(true,result);
    }

    /**
     * 获取用户id
     *
     * @param loginName
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取用户id",notes = "获取用户id接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName" ,required = true,value = "登录名称",dataType = "String",paramType = "path")
    })
    @RequestMapping(value = "/getUserIdByName/{loginName}",method = RequestMethod.GET)
    public ContentResultForm getUserIdByName(@PathVariable("loginName") String loginName) throws Exception {
        AgUser agUser = iAgUser.findUserByName(loginName);
        if (agUser != null) return new ContentResultForm(true,agUser.getId());
        return new ContentResultForm(false,"","获取用户id失败");
    }

    /**
     * 根据登录名称获取用户
     *
     * @param loginName
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据登录名称获取用户名称",notes = "根据登录名称获取用户名称接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName" ,required = true,value = "登录名称",dataType = "String",paramType = "path")
    })
    @RequestMapping(value = "/getUserByName/{loginName}",method = RequestMethod.GET)
    public ContentResultForm getUserByName(@PathVariable("loginName") String loginName) throws Exception {
        AgUser user = iAgUser.findUserByName(loginName);
        if (user != null) return new ContentResultForm(true,user.getUserName());
        return new ContentResultForm(false,"","根据登录名称获取用户失败");
    }

    /**
     * 根据登录名称获取用户
     *
     * @param loginName
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据登录名称获取用户信息",notes = "根据登录名称获取用户信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName" ,required = true,value = "登录名称",dataType = "String",paramType = "path")
    })
    @RestLog(value ="根据登录名称获取用户信息")
    @RequestMapping(value = "/findUserByName/{loginName}",method = RequestMethod.GET)
    public ContentResultForm findUserByName(@PathVariable("loginName") String loginName) throws Exception {
        AgUser user = iAgUser.findUserByName(loginName);
        if (user != null){
            return new ContentResultForm(true,user);
        }
        return new ContentResultForm(false,"","根据登录名称获取用户信息失败");
    }

    /**
     * 按图层id获取用户
     *
     * @param parentCode
     * @return
     * @throws Exception
     */
  /*  @RequestMapping("/findListByParent")
    public String findListByParent(String parentCode) throws Exception {
        List<AgOrg> list = iAgUser.findListByParent(parentCode);
        StringBuffer sb = new StringBuffer();
        for (AgOrg org : list) {
            sb.append("#" + org.getName());
        }
        return sb.toString();
    }*/

    /**
     * 修改密码
     *
     * @param newPassword
     * @return
     */
    @ApiOperation(value = "修改密码",notes = "修改密码接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "newPassword" ,required = true,value = "新密码",dataType = "String")
    })
    @RequestMapping(value = "/modifyPassword",method = RequestMethod.POST)
    public ResultForm modifyPassword(String newPassword, HttpServletRequest request) throws Exception {
        JSONObject jsonObject = this.getLogInfo(request, "修改用户登录密码");
        if (Common.isCheckNull(newPassword)) return new ResultForm(false, "输入密码为空！");
        String loginName = LoginHelpClient.getLoginName(request);
        AgUser agUser = iAgUser.findUserByName(loginName);
        try {
            if (agUser != null) {
                agUser.setPassword(newPassword);
                iAgUser.updateUser(agUser);
            }
            jsonObject.put("operResult", "成功");
            log.info(jsonObject.toString());
            return new ResultForm(true);
        } catch (Exception e) {
            jsonObject.put("operResult", "失败");
            if (e.getMessage() != null) {
                jsonObject.put("exceptionMessage", e.getMessage().substring(0, e.getMessage().length() > 500 ? 500 : e.getMessage().length()));
            }
            log.error(jsonObject.toString());
            e.printStackTrace();
        }
        return new ResultForm(false, "修改失败！");
    }

    /**
     * 获取日志基本信息 2018-3-12
     *
     * @param request
     * @param funcName
     * @return
     */
    private JSONObject getLogInfo(HttpServletRequest request, String funcName) {
        JSONObject jsonObject = new JSONObject();
        String loginName = LoginHelpClient.getLoginName(request);
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        jsonObject.put("loginName", loginName);
        jsonObject.put("sysName", "agsupport");
        jsonObject.put("ipAddress", Common.getIpAddr(request));
        jsonObject.put("browser", userAgent.getBrowser().getName());
        jsonObject.put("funcName", funcName);
        return jsonObject;
    }

    /**
     * 用户授权图层
     *
     * @param userIds
     * @param dirLayerIds
     * @return
     */
    @ApiOperation(value = "用户授权图层",notes = "用户授权图层接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userIds" ,value = "用户ids",dataType = "String"),
            @ApiImplicitParam(name = "dirLayerIds" ,value = "目录图层ids",dataType = "String"),
            @ApiImplicitParam(name = "queryable" ,value = "是否可查询",dataType = "String"),
            @ApiImplicitParam(name = "editable" ,value = "是否可编辑",dataType = "String"),
            @ApiImplicitParam(name = "isBaseMap" ,value = "是否为底图",dataType = "String")
    })
    @CacheEvict(value = "user#project_layer_tree",allEntries = true)
    @RequestMapping(value = "/authorLayer",method = RequestMethod.POST)
    public ResultForm authorLayer(String userIds, String dirLayerIds,String editable,String isBaseMap, HttpServletRequest request) {
        String uIds[] = userIds.split(",");
        String dIds[] = dirLayerIds.split(",");
        JSONObject jsonObject = this.getLogInfo(request, "图层授权");
        try {
            if (uIds.length>0 && dIds.length>0){
                List<AgUserLayer> list = new ArrayList<>();
                for (String userId:uIds){
                    for (String dirLayerId:dIds){
                        AgUserLayer temp = iAgUser.getUserLayer(userId, dirLayerId);
                        if ((temp==null || StringUtils.isEmpty(temp.getId())) && StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(dirLayerId)){
                            temp = new AgUserLayer();
                            //temp.setId(UUID.randomUUID().toString());
                            temp.setUserId(userId);
                            temp.setDirLayerId(dirLayerId);
                            AgLayer ag = iAgDir.findLayerByDirLayerId(dirLayerId);
                            String layerType = ag.getLayerType();
                            // 给可查询图层设置默认值
                            if ("01".equals(layerType.substring(0, 2)) || "020202".equals(layerType.substring(0, 6))|| "03".equals(layerType.substring(0, 6))|| "04".equals(layerType.substring(0, 2)) || "07".equals(layerType.substring(0, 2)) ||"000001".equals(layerType)){
                                temp.setQueryable("1");
                            }
                            temp.setEditable(editable);
                            temp.setIsBaseMap(isBaseMap);
                            list.add(temp);
                        }

                    }
                }
                if (list.size() > 0) {
                    iAgUser.saveUserLayerBatch(list);
                }
            }
            jsonObject.put("operResult", "成功");
            log.info(jsonObject.toString());
            return new ResultForm(true);
        }catch (Exception e){
            jsonObject.put("operResult", "失败");
            if (e.getMessage() != null) {
                jsonObject.put("exceptionMessage", e.getMessage().substring(0, e.getMessage().length() > 500 ? 500 : e.getMessage().length()));
            }
            log.error(jsonObject.toString());
            e.printStackTrace();
        }
        return new ResultForm(false);
    }

    @RequestMapping(value = "/authorDirLayerWithChildrenByDir",method = RequestMethod.POST)
    public ResultForm authorDirLayerWithChildrenByDir(String userIds, String dirId, HttpServletRequest request) {
        if(Common.isCheckNull(userIds)||Common.isCheckNull(dirId)){
            return new ResultForm(false,"授权用户或被授权目录不存在");
        }
        try {
            StringBuilder sbDirLayerIds = new StringBuilder(500);
            AgDir agDir = iAgDir.findDirById(dirId);
            List<AgDirLayer> listDirLayerWithChildren = iAgDir.findDirLayerByDirXpath(agDir.getXpath());
            String separator =",";
            for(AgDirLayer agDirLayer : listDirLayerWithChildren){
                sbDirLayerIds.append(agDirLayer.getId()+separator);
            }
            int length = sbDirLayerIds.length();
            if(length>0){
                sbDirLayerIds.deleteCharAt(length-1);
            }
            return authorLayer(userIds,sbDirLayerIds.toString(),null,null,request);
        }catch (Exception e){
            return new ResultForm(false,e.getMessage());
        }

    }

    /**
     * 根据图层获取用户集合
     *
     * @param dirLayerId
     * @param agUser
     * @param page
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据图层获取用户集合",notes = "根据图层获取用户集合接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirLayerId" ,value = "目录图层ids",dataType = "String"),
            @ApiImplicitParam(name = "agUser" ,value = "用户信息对象",dataType = "AgUser"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:/agsupport/param/paramList?page=1&rows=10&name=", dataType = "Page")
    })
    @RequestMapping(value = "/userListByLayer/{dirLayerId}",method = RequestMethod.GET)
    public ContentResultForm userListByLayer(@PathVariable("dirLayerId") String dirLayerId, AgUser agUser, Page page, HttpServletRequest request) throws Exception{
        page.setPageNum(Integer.valueOf(request.getParameter(PAGESIZE)));
        page.setPages(Integer.valueOf(request.getParameter(ROWS)));
        PageInfo<AgUser> pageInfo=iAgUser.searchUserByLayer(agUser, dirLayerId, page);
        EasyuiPageInfo<AgUser> result = PageHelper.toEasyuiPageInfo(pageInfo);
        return new ContentResultForm<EasyuiPageInfo<AgUser>>(true,result);
    }

    @ApiOperation(value = "移除图层权限",notes = "移除图层权限接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userLayerIds" ,value = "用户图层ids",dataType = "String")
    })
    @RequestMapping(value = "/delUserLayer",method = RequestMethod.DELETE)
    public ResultForm delUserLayer(String userLayerIds, HttpServletRequest request) {
        String ids[] = null;
        JSONObject jsonObject = this.getLogInfo(request, "图层权限移除");
        if (StringUtils.isNotEmpty(userLayerIds)) {
            ids = userLayerIds.split(",");
        }
        try {
            if (ids != null && ids.length > 0) {
                iAgUser.delUserLayerBatch(ids);
            }
            jsonObject.put("operResult", "成功");
            log.info(jsonObject.toString());
            return new ResultForm(true);
        } catch (Exception e) {
            jsonObject.put("operResult", "失败");
            if (e.getMessage() != null) {
                jsonObject.put("exceptionMessage", e.getMessage().substring(0, e.getMessage().length() > 500 ? 500 : e.getMessage().length()));
            }
            log.error(jsonObject.toString());
            e.printStackTrace();
        }
        return new ResultForm(false);
    }
}
